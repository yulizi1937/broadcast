/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.application.sqlite.DBConstant;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.ChipsLayout;
import com.application.ui.view.DiscreteSeekBar;
import com.application.ui.view.DiscreteSeekBar.OnProgressChangeListener;
import com.application.ui.view.FlowLayout;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class VideoDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = VideoDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private LinearLayout mLanguageLinearLayout;

	private FlowLayout mLanguageFlowLayout;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mVideoTitleTv;
	private AppCompatTextView mVideoByTv;
	private AppCompatTextView mVideoViewTv;
	private AppCompatTextView mVideoLikeTv;
	private AppCompatTextView mVideoSummaryTextTv;
	private AppCompatTextView mVideoDescTotalTv;
	private AppCompatTextView mLanguageHeaderTv;

	private VideoView mVideoView;

	private ImageView mVideoPlayIv;
	private ImageView mVideoPauseIv;
	private ImageView mShareIv;
	private ImageView mVideoCoverIv;
	private ImageView mVideoFullScreenIv;

	private AppCompatTextView mVideoNewsLinkTv;

	private LinearLayout mVideoNewsLinkLayout;
	
	private AppCompatTextView mVideoMediaControllerTotalDurationTv;
	private AppCompatTextView mVideoMediaControllerCurrentDurationTv;
	private DiscreteSeekBar mVideoMediaControllerDiscreteSeekBar;

	private FrameLayout mVideoDescFrameLayout;
	private FrameLayout mVideoMediaControllerFrameLayout;

	private Animation mAnimFadeIn;
	private Animation mAnimFadeOut;

	private int mProgress;
	private int mTotalDuration;

	private Handler mHandler;

	private Thread mSeekBarThread;

	private boolean isShareOptionEnable = true;
	
	private Intent mIntent;
	private String mId;
	private String mCategory;
	private String mContentTitle;
	private String mContentDesc;
	private String mContentLikeCount;
	private String mContentViewCount;
	private String mContentBy;
	private String mContentLink;
	private String mContentFileLink;
	private String mContentFilePath;
	private String mContentFileThumbLink;
	private String mContentFileThumbPath;
	private String mContentLanguage;
	private String mContentDate;
	private String mContentTime;
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	
	private ArrayList<String> mContentLanguageList = new ArrayList<>();
	private ArrayList<String> mContentFileLinkList = new ArrayList<>();
	private ArrayList<String> mContentFilePathList = new ArrayList<>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		initToolBar();
		initUi();
		getIntentData();
		initUiWithData();
		initAnimation();
//		initVideoPlayer();
		setUiListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		/*
		 * if(isShareOptionEnable){
		 * menu.findItem(R.id.action_share).setVisible(true); }else{
		 * menu.findItem(R.id.action_share).setVisible(false); }
		 */
		return super.onPrepareOptionsPanel(view, menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_text_detail, menu);
		if (AndroidUtilities.isAboveGingerBread()) {
			MenuItem refreshItem = menu
					.findItem(R.id.action_refresh_actionable);
			if (refreshItem != null) {
				View mView = MenuItemCompat.getActionView(refreshItem);
				MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout) mView
						.findViewById(R.id.toolBarActionItemRefresh);
				mToolBarMenuRefreshProgress = (ProgressWheel) mView
						.findViewById(R.id.toolBarActionItemProgressWheel);
				mToolBarMenuRefresh = (ImageView) mView
						.findViewById(R.id.toolBarActionItemImageView);
				mToolBarMenuRefreshLayout
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View mView) {
								// TODO Auto-generated method stub
								toolBarRefresh();
							}
						});
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_refresh_actionable:
			toolBarRefresh();
			return true;
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(VideoDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(VideoDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Video");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(VideoDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mToolBarMenuRefresh.setVisibility(View.VISIBLE);
				mToolBarMenuRefreshProgress.setVisibility(View.GONE);
			}
		}, 5000);
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentVideoDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentVideoDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailLanguageHeaderTv);

		mVideoTitleTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailTitleTv);

		mVideoByTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailByTv);
		mVideoSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailSummaryTv);
		mVideoViewTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailViewTv);
		mVideoLikeTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailLikeTv);
		mVideoDescTotalTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailDescTotalTextView);

		mVideoDescFrameLayout = (FrameLayout) findViewById(R.id.fragmentVideoDetailDescLayout);

		mShareIv = (ImageView) findViewById(R.id.fragmentVideoDetailDescShareImageView);
		mVideoPlayIv = (ImageView) findViewById(R.id.fragmentVideoDetailVideoPlayIv);
		mVideoPauseIv = (ImageView) findViewById(R.id.fragmentVideoDetailVideoPauseIv);
		mVideoCoverIv = (ImageView) findViewById(R.id.fragmentVideoDetailVideoImageView);
		mVideoFullScreenIv = (ImageView) findViewById(R.id.fragmentVideoDetailMediaControllerFullScreenIv);

		mVideoView = (VideoView) findViewById(R.id.fragmentVideoDetailVideoView);

		mVideoMediaControllerCurrentDurationTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailMediaControllerCurrentPositionTv);
		mVideoMediaControllerTotalDurationTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailMediaControllerTotalPositionTv);

		mVideoMediaControllerDiscreteSeekBar = (DiscreteSeekBar) findViewById(R.id.fragmentVideoDetailMediaControllerSeekBar);

		mVideoMediaControllerFrameLayout = (FrameLayout) findViewById(R.id.fragmentVideoDetailMediaControllerLayout);
		
		mVideoNewsLinkTv = (AppCompatTextView)findViewById(R.id.fragmentVideoDetailLinkTv);
		
		mVideoNewsLinkLayout = (LinearLayout)findViewById(R.id.fragmentVideoDetailViewSourceLayout);
		
		mVideoViewTv.setVisibility(View.GONE);
		mVideoLikeTv.setVisibility(View.GONE);
	}

	private void getIntentData(){
		mIntent = getIntent();
		try{
			if(mIntent!=null){
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).toString();
				if(!TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mCategory)){
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
						mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId}, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " DESC");
						getDataFromDBForMobcast(mCursor);
					}else{
						mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId}, DBConstant.Training_Columns.COLUMN_TRAINING_ID + " DESC");
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}
			}else{
				initVideoPlayer("dummy");
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void initUiWithData(){
		mVideoNewsLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
	}
	
	private void getDataFromDBForMobcast(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT));
			mContentLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME));
			
			Cursor mCursorFile = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID+"=?", new String[]{mId}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			if(mCursorFile!=null && mCursorFile.getCount() > 0){
				mCursorFile.moveToFirst();
				do {
					mContentFileLinkList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK)));
					mContentFilePathList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH)));
					mContentLanguageList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG)));
					if(Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mContentFilePath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH));
						mContentFileLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK));
						mContentLanguage = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG));
						mContentFileThumbLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK));
						mContentFileThumbPath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH));
					}
				} while (mCursorFile.moveToNext());
				
			}
			if(mCursorFile!=null)
				mCursorFile.close();
			
			setIntentDataToUi();
		}
	}
	
	private void setIntentDataToUi(){
		try{
			mVideoTitleTv.setText(mContentTitle);
			mVideoSummaryTextTv.setText(mContentDesc);
			if(!TextUtils.isEmpty(mContentLikeCount)){
				mVideoLikeTv.setText(mContentLikeCount);	
			}
			
			if(!TextUtils.isEmpty(mContentViewCount)){
				mVideoViewTv.setText(mContentViewCount);
			}
			
			mVideoByTv.setText(Utilities.formatBy(mContentBy, mContentDate, mContentTime));
			
			if(TextUtils.isEmpty(mContentLink)){
				mVideoNewsLinkLayout.setVisibility(View.GONE);
			}
			
			if(mContentLanguageList!=null && mContentLanguageList.size() > 1){
				setLanguageChipsLayout(mContentLanguageList);
			}else{
				mLanguageLinearLayout.setVisibility(View.GONE);
			}
			
			if(checkIfFileExists(mContentFilePath)){
				mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
				if(!TextUtils.isEmpty(mContentFilePath)){
					initVideoPlayer(mContentFilePath);	
				}
			}
			
			if(checkIfFileExists(mContentFileThumbPath)){
				mVideoCoverIv.setImageURI(Uri.parse(mContentFileThumbPath));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	protected void onPause() {
		cleanUp();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		cleanUp();
		super.onDestroy();
	}

	private void initVideoPlayer(String mVideoPath) {
//			mVideoView.setVideoPath("android.resource://" + getPackageName()
//			+ "/raw/hdfcconverted");
		mVideoView.setVideoPath(mVideoPath);
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mTotalDuration = mVideoView.getDuration();
				mVideoMediaControllerDiscreteSeekBar.setMin(0);
				mVideoMediaControllerDiscreteSeekBar.setMax(mTotalDuration);
				mVideoMediaControllerTotalDurationTv.setText(Utilities
						.getTimeFromMilliSeconds((long) mTotalDuration));
				mVideoMediaControllerDiscreteSeekBar
						.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
							@Override
							public int transform(int value) {
								return value * 100;
							}

							@Override
							public String transformToString(int value) {
								// TODO Auto-generated method stub
								return Utilities
										.getTimeFromMilliSeconds((long) value);
							}

							@Override
							public boolean useStringTransform() {
								// TODO Auto-generated method stub
								return true;
							}

						});
				
				mVideoView.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mediaPlayer) {
						// TODO Auto-generated method stub
						mVideoDescFrameLayout.setVisibility(View.VISIBLE);
						mVideoCoverIv.setVisibility(View.VISIBLE);
						mVideoPlayIv.setVisibility(View.VISIBLE);
					}
				});
				
				runOnSeekBarThread();
			}
		});
	}

	private void runOnSeekBarThread() {
		mHandler = new Handler();
		final Runnable mSeekBarRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mVideoMediaControllerDiscreteSeekBar.setProgress(mProgress);
				mVideoMediaControllerCurrentDurationTv.setText(Utilities
						.getTimeFromMilliSeconds((long) mProgress));
			}
		};

		final Runnable mSeekBarProgressRunnable = new Runnable() {
			public void run() {
				while (mProgress <= mTotalDuration) {
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
		};

		mSeekBarThread = new Thread(mSeekBarProgressRunnable);
		mSeekBarThread.start();
	}

	private void initAnimation() {
		mAnimFadeIn = AnimationUtils.loadAnimation(VideoDetailActivity.this,
				android.R.anim.fade_in);
		mAnimFadeOut = AnimationUtils.loadAnimation(VideoDetailActivity.this,
				android.R.anim.fade_out);

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
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.VideoDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setOnTouchListener();
		setSeekBarListener();
//		setLanguageChipsLayout();
	}

	private void setOnClickListener() {
		mShareIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showShareDialog();
			}
		});

		mVideoPlayIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playVideo();
			}
		});

		mVideoPauseIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mVideoView.pause();
			}
		});

		mVideoFullScreenIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntentVideoFullScreen = new Intent(
						VideoDetailActivity.this, VideoFullScreenActivity.class);
				startActivity(mIntentVideoFullScreen);
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

	private void setLanguageChipsLayout(final ArrayList<String> mContentLanguageList) {
		mLanguageLinearLayout.setVisibility(View.VISIBLE);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(2, 2, 2, 2);
		/*final String[] mLanguages = new String[] { "English", "Hindi",
				"Marathi", "Gujarati", "Bengali", "Telugu", "Kannad",
				"Punjabi", "Siddhi", "Bhojpuri" };*/
		for (int i = 0; i < 3; i++) {
			ChipsLayout mChip = new ChipsLayout(this);
			mChip.setDrawable(R.drawable.ic_chips_download);
//			mChip.setText(mLanguages[i]);
			mChip.setText(mContentLanguageList.get(i));
			mChip.setLayoutParams(params);
			final int j = i;
			mChip.getChipLayout().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					 Utilities.showCrouton(VideoDetailActivity.this,
					 mCroutonViewGroup, mContentLanguageList.get(j).toString(), Style.INFO);
				}
			});
			mLanguageFlowLayout.addView(mChip);
		}
	}

	private void setMediaControllerLayout() {
		if (mVideoView.isPlaying()) {
			mVideoMediaControllerFrameLayout.startAnimation(mAnimFadeIn);
			mVideoMediaControllerFrameLayout.setVisibility(View.VISIBLE);
			AndroidUtilities.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mVideoMediaControllerFrameLayout
							.startAnimation(mAnimFadeOut);
				}
			}, 3000);
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
							try {
								if (mSeekBarThread != null) {
									mSeekBarThread.sleep(1);
								}
							} catch (InterruptedException e) {
							}
							mVideoView.seekTo(value);
							mProgress = value;
						}
					}
				});
	}

	private void playVideo() {
		mVideoDescFrameLayout.setVisibility(View.GONE);
		mVideoCoverIv.setVisibility(View.GONE);
		mVideoPlayIv.setVisibility(View.GONE);
		mVideoView.requestFocus();
		mVideoView.start();
	}

	@SuppressWarnings("deprecation")
	private void showShareDialog() {
		showDialog(0);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				"Hello ").limit(R.integer.bs_initial_grid_row).build();
	}

	private void setMaterialRippleView() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
