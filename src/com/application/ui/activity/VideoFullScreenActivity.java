/**
 * 
 */
package com.application.ui.activity;

import java.io.File;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.application.ui.view.DiscreteSeekBar;
import com.application.ui.view.DiscreteSeekBar.OnProgressChangeListener;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.permission.PermissionHelper;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class VideoFullScreenActivity extends SwipeBackBaseActivity {
	private static final String TAG = VideoFullScreenActivity.class
			.getSimpleName();

	private FrameLayout mCroutonViewGroup;

	private VideoView mVideoView;

	private ImageView mVideoPlayIv;
	private ImageView mVideoPauseIv;
	private ImageView mVideoFullScreenIv;

	private AppCompatTextView mVideoMediaControllerTotalDurationTv;
	private AppCompatTextView mVideoMediaControllerCurrentDurationTv;
	private DiscreteSeekBar mVideoMediaControllerDiscreteSeekBar;

	private FrameLayout mVideoMediaControllerFrameLayout;

	private Animation mAnimFadeIn;
	private Animation mAnimFadeOut;

	private int mProgress;
	private int mTotalDuration;

	private Handler mHandler;
	private Thread mSeekBarThread;
	
	private PauseControl mThreadSafe = new PauseControl();
	
	private Intent mIntent;
	private String mContentFilePath;
	private String mId;
	private String mCategory;
	
	private long mReportStart = 0;
	private long mReportStop = 0;
	private long mReportDuration = 0;
	
	private boolean isVideoPause = false;
	
	private PermissionHelper mPermissionHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		hideNavigationBar();
		setContentView(R.layout.activity_video_fullscreen);
		setSecurity();
		initUi();
		checkPermissionModel();
		initAnimation();
		getIntentData();
		setUiListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		decryptFileOnResume();
		initVideoPlayer(mContentFilePath);
		mThreadSafe.unpause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			checkWhetherUserPlayVideoOrNotAndUpdateToApi();
			finish();
			AndroidUtilities.exitWindowAnimation(VideoFullScreenActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mVideoFullScreenIv = (ImageView) findViewById(R.id.fragmentVideoFullScreenMediaControllerFullScreenIv);
		mVideoPauseIv = (ImageView)findViewById(R.id.fragmentVideoFullScreenVideoPauseIv);
		mVideoPlayIv = (ImageView)findViewById(R.id.fragmentVideoFullScreenVideoPlayIv);
		
		mVideoFullScreenIv = (ImageView)findViewById(R.id.fragmentVideoFullScreenMediaControllerFullScreenIv);

		mVideoView = (VideoView) findViewById(R.id.fragmentVideoFullScreenVideoView);

		mVideoMediaControllerCurrentDurationTv = (AppCompatTextView) findViewById(R.id.fragmentVideoFullScreenMediaControllerCurrentPositionTv);
		mVideoMediaControllerTotalDurationTv = (AppCompatTextView) findViewById(R.id.fragmentVideoFullScreenMediaControllerTotalPositionTv);

		mVideoMediaControllerDiscreteSeekBar = (DiscreteSeekBar) findViewById(R.id.fragmentVideoFullScreenMediaControllerSeekBar);

		mVideoMediaControllerFrameLayout = (FrameLayout) findViewById(R.id.fragmentVideoFullScreenMediaControllerLayout);
	}
	
	private void getIntentData(){
		try{
			mIntent = getIntent();
			mContentFilePath = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.FILEPATH);
			mProgress = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.TIME, -1);
			mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
			mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
			mReportStart = System.currentTimeMillis();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		saveVideoViewPosition();
		onPause();
		mThreadSafe.pause();
		checkWhetherUserPlayVideoOrNotAndUpdateToApi();
		AndroidUtilities.exitWindowAnimation(VideoFullScreenActivity.this);
		super.onBackPressed();
	}
	
	private void checkWhetherUserPlayVideoOrNotAndUpdateToApi(){
		try{
			if(mReportStart > 0){
				mReportStop = System.currentTimeMillis();
				mReportDuration += mReportStop - mReportStart;
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.PLAY, Utilities.getTimeFromMilliSeconds(mReportDuration));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@Override
	protected void onPause() {
		cleanUp();
//		deleteDecryptedFile();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		cleanUp();
		super.onDestroy();
	}
	
	private void saveVideoViewPosition() {
		ApplicationLoader.getPreferences().setVideoViewPosition(
				mVideoView.getCurrentPosition());
	}
	

	private void initVideoPlayer(String mVideoPath) {
		try{
			mVideoView.setVideoPath(mVideoPath);
			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mTotalDuration = mVideoView.getDuration();
					mVideoMediaControllerDiscreteSeekBar.setMax(mTotalDuration);
					mVideoMediaControllerTotalDurationTv.setText(Utilities.getTimeFromMilliSeconds((long)mTotalDuration));
					mVideoMediaControllerDiscreteSeekBar
							.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
								@Override
								public int transform(int value) {
									return value * 100;
								}

								@Override
								public String transformToString(int value) {
									// TODO Auto-generated method stub
									return Utilities.getTimeFromMilliSeconds((long)value);
								}

								@Override
								public boolean useStringTransform() {
									// TODO Auto-generated method stub
									return true;
								}

							});
					runOnSeekBarThread();
					
					mVideoView.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mediaPlayer) {
							// TODO Auto-generated method stub
							mReportStop = System.currentTimeMillis();
							mReportDuration += mReportStop - mReportStart;
							UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.PLAY, Utilities.getTimeFromMilliSeconds(mTotalDuration));
							mReportDuration = 0;
						}
					});
				}
			});
			if(mProgress!=-1){
				mVideoView.requestFocus();
				mVideoView.start();
				mVideoView.seekTo(mProgress);
			}
			mVideoView.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					catchErrorOnVideoView();
					return true;
				}
			});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void catchErrorOnVideoView(){
		try{
			AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(VideoFullScreenActivity.this);
			mAlertDialog.setTitle(getResources().getString(R.string.videoview_error));
			mAlertDialog.setMessage(getResources().getString(R.string.videoview_error_message));
			mAlertDialog.setCancelable(true);
			mAlertDialog.setPositiveButton(getResources().getString(R.string.OK), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Utilities.deleteAppFolder(new File(mContentFilePath));
					startActivity(mIntent);
					finish();
				}
			});
			mAlertDialog.show();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void runOnSeekBarThread() {
		mHandler = new Handler();
		final Runnable mSeekBarRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mVideoMediaControllerDiscreteSeekBar.setProgress(mProgress);
				mVideoMediaControllerCurrentDurationTv.setText(Utilities.getTimeFromMilliSeconds((long)mProgress));
			}
		};

		Runnable mSeekBarProgressRunnable = new Runnable() {
			public void run() {
				synchronized (this) {
					while (mProgress <= mTotalDuration) {
						if (!mThreadSafe.needToPause) {
							mHandler.post(mSeekBarRunnable);
							mProgress++;
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
			}
		};

		mSeekBarThread = new Thread(mSeekBarProgressRunnable);
		mSeekBarThread.start();
	}
	
	public class PauseControl {
	    private boolean needToPause = false;
	    public synchronized void pausePoint() {
	        while (!needToPause) {
	            try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }

	    public synchronized void pause() {
	        needToPause = true;
	    }

	    public synchronized void unpause() {
	        needToPause = false;
	        this.notifyAll();
	    }
	}

	private void initAnimation() {
		mAnimFadeIn = AnimationUtils.loadAnimation(
				VideoFullScreenActivity.this, android.R.anim.fade_in);
		mAnimFadeOut = AnimationUtils.loadAnimation(
				VideoFullScreenActivity.this, android.R.anim.fade_out);

		mAnimFadeIn.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mVideoMediaControllerFrameLayout.setVisibility(View.VISIBLE);
				mVideoPauseIv.setVisibility(View.VISIBLE);
			}
		});

		mAnimFadeOut.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mVideoMediaControllerFrameLayout.setVisibility(View.GONE);
				mVideoPauseIv.setVisibility(View.GONE);
			}
		});
	}

	private void cleanUp() {
		mVideoView.pause();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */

	private void setUiListener() {
		setOnTouchListener();
		setSeekBarListener();
		setOnClickListener();
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		mVideoPlayIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isVideoPause){
					mProgress = 0;
					playVideo();	
				}else{
					mVideoView.requestFocus();
					mVideoView.seekTo(ApplicationLoader.getPreferences().getVideoViewPosition());
					mVideoView.start();
					mVideoPlayIv.setVisibility(View.GONE);
					mVideoMediaControllerFrameLayout.setVisibility(View.VISIBLE);
					mVideoPauseIv.setVisibility(View.VISIBLE);
//					runOnSeekBarThread();
					mThreadSafe.unpause();
					mReportStart = System.currentTimeMillis();
				}	
			}
		});
		
		mVideoPauseIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mVideoView.pause();
				mVideoMediaControllerFrameLayout.setVisibility(View.GONE);
				mVideoPauseIv.setVisibility(View.GONE);
				mVideoPlayIv.setVisibility(View.VISIBLE);
				isVideoPause = true;
				mReportStop = System.currentTimeMillis();
				mReportDuration += mReportStop - mReportStart;
				try {
					ApplicationLoader.getPreferences().setVideoViewPosition(mVideoView.getCurrentPosition());
//					mSeekBarThread.interrupt();
					mThreadSafe.pause();
					UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.PLAY, Utilities.getTimeFromMilliSeconds(mReportDuration));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		mVideoFullScreenIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void setOnTouchListener() {
		mVideoView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				setMediaControllerLayout();
				return true;
			}
		});
	}

	private void setMediaControllerLayout() {
		if (mVideoView.isPlaying()) {
			mVideoMediaControllerFrameLayout.startAnimation(mAnimFadeIn);
			mVideoMediaControllerFrameLayout.setVisibility(View.VISIBLE);
			/*AndroidUtilities.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mVideoMediaControllerFrameLayout
							.startAnimation(mAnimFadeOut);
				}
			}, 3000);*/
		}
	}

	private void setSeekBarListener() {
		mVideoMediaControllerDiscreteSeekBar
				.setOnProgressChangeListener(new OnProgressChangeListener() {
					@Override
					public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProgressChanged(DiscreteSeekBar seekBar,
							int value, boolean fromUser) {
						// TODO Auto-generated method stub
						if (fromUser) {
							// this is when actually seekbar has been seeked to
							// a new position
							try{
								if(mSeekBarThread!=null){
									mSeekBarThread.sleep(1);	
								}
							}catch(InterruptedException e){
							}
							mVideoView.seekTo(value);
							mProgress = value;
						}
					}
				});
	}

	private void playVideo() {
		mVideoView.requestFocus();
		mVideoView.start();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void hideNavigationBar(){
		if(AndroidUtilities.isAboveIceCreamSandWich()){
			View decorView = getWindow().getDecorView();
			// Hide both the navigation bar and the status bar.
			// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
			// a general rule, you should design your app to hide the status bar whenever you
			// hide the navigation bar.
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			              | View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}
	
	private void decryptFileOnResume(){
		try{
			if(Utilities.isContainsDecrypted(mContentFilePath)){
					mContentFilePath = mContentFilePath.replace(AppConstants.decrypted, "");
					mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void deleteDecryptedFile(){
		try{
			if(Utilities.isContainsDecrypted(mContentFilePath)){
					new File(mContentFilePath).delete();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * AndroidM: Permission Model
	 */
	private void checkPermissionModel() {
		try{
			if (AndroidUtilities.isAboveMarshMallow()) {
				mPermissionHelper = PermissionHelper.getInstance(this);
				mPermissionHelper.setForceAccepting(false)
						.request(AppConstants.PERMISSION.STORAGE);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mPermissionHelper.onActivityForResult(requestCode);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			@NonNull String[] permissions, @NonNull int[] grantResults) {
		mPermissionHelper.onRequestPermissionsResult(requestCode, permissions,
				grantResults);
	}

	@Override
	public void onPermissionGranted(String[] permissionName) {
		try{
			getIntentData();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	public void onPermissionDeclined(String[] permissionName) {
		AndroidUtilities.showSnackBar(this, getString(R.string.permission_message_denied));
	}

	@Override
	public void onPermissionPreGranted(String permissionName) {
	}

	@Override
	public void onPermissionNeedExplanation(String permissionName) {
		getAlertDialog(permissionName).show();
	}

	@Override
	public void onPermissionReallyDeclined(String permissionName) {
		AndroidUtilities.showSnackBar(this, getString(R.string.permission_message_denied));
	}

	@Override
	public void onNoPermissionNeeded() {
	}

	public AlertDialog getAlertDialog(final String permission) {
		AlertDialog builder = null;
		if (builder == null) {
			builder = new AlertDialog.Builder(this).setTitle(
					getResources().getString(R.string.app_name)+ " requires " + permission + " permission").create();
		}
		builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPermissionHelper.requestAfterExplanation(permission);
					}
				});
		builder.setMessage(getResources().getString(R.string.permission_message_externalstorage));
		return builder;
	}
	
	
	/**
	 * Google Analytics v3
	 */
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
