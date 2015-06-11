/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.view.BottomSheet;
import com.application.ui.view.DiscreteSeekBar;
import com.application.ui.view.LineRenderer;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.VisualizerView;
import com.application.ui.view.DiscreteSeekBar.OnProgressChangeListener;
import com.application.utils.AndroidUtilities;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AudioDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = AudioDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;
	
	private AppCompatTextView mAudioTitleTv;
	private AppCompatTextView mAudioByTv;
	private AppCompatTextView mAudioViewTv;
	private AppCompatTextView mAudioSummaryTextTv;
	
	private AppCompatTextView mAudioControllerCurrentDurationTv;
	
	private VisualizerView mVisualizerView;
	
	private ImageView mShareIv;
	private ImageView mPlayIv;
	
	private DiscreteSeekBar mDiscreteSeekBar;
	int mProgress = 0;
	int mTotalDuration = 0;
	Handler mHandler;
	
	private MediaPlayer mPlayer;
	private Thread mSeekBarThread;
	
	private boolean isShareOptionEnable = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_detail);
		initToolBar();
		initUi();
		initMediaPlayer();
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
		if(isShareOptionEnable){
			menu.findItem(R.id.action_share).setVisible(true);
		}else{
			menu.findItem(R.id.action_share).setVisible(false);
		}
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
			AndroidUtilities.exitWindowAnimation(AudioDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	  @Override
	  protected void onPause()
	  {
	    cleanUp();
	    super.onPause();
	  }

	  @Override
	  protected void onDestroy()
	  {
	    cleanUp();
	    super.onDestroy();
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
		
		mAudioTitleTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailTitleTv);
		
		mAudioByTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailByTv);
		mAudioSummaryTextTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailSummaryTv);
		mAudioViewTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailViewTv);
		mAudioControllerCurrentDurationTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailSeekBarTv);
		
		mPlayIv = (ImageView)findViewById(R.id.fragmentAudioDetailImageView);
		
		mVisualizerView = (VisualizerView)findViewById(R.id.fragmentAudioDetailVisualizerView);
		
		mDiscreteSeekBar = (DiscreteSeekBar)findViewById(R.id.fragmentAudioDetailSeekBar);
	}

	  private void initMediaPlayer()
	  {
	    mPlayer = MediaPlayer.create(this, R.raw.test);
	    mPlayer.setLooping(false);
	    mPlayer.start();
	    mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mTotalDuration = mPlayer.getDuration();
				mPlayIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
				mDiscreteSeekBar.setMin(0);
				mDiscreteSeekBar.setMax(mTotalDuration);
				mDiscreteSeekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
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
			}
		});

	    // We need to link the visualizer view to the media player so that
	    // it displays something
	    mVisualizerView.link(mPlayer);

	    // Start with just line renderer
	    addLineRenderer();
	  }

	  private void cleanUp()
	  {
	    if (mPlayer != null)
	    {
	      mVisualizerView.release();
	      mPlayer.release();
	      mPlayer = null;
	    }
	  }
	  
	  private void addLineRenderer()
	  {
	    Paint linePaint = new Paint();
	    linePaint.setStrokeWidth(1f);
	    linePaint.setAntiAlias(true);
	    linePaint.setColor(Color.argb(88, 0, 128, 255));

	    Paint lineFlashPaint = new Paint();
	    lineFlashPaint.setStrokeWidth(5f);
	    lineFlashPaint.setAntiAlias(true);
	    lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
	    LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
	    mVisualizerView.addRenderer(lineRenderer);
	  }

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.AudioDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setMediaPlayerListener();
		setSeekBarListener();
		setToolbarOption();
	}
	
	private void setToolbarOption(){
		
	}
	
	private void setSeekBarListener(){
		mDiscreteSeekBar.setOnProgressChangeListener(new OnProgressChangeListener() {
			@Override
			public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(DiscreteSeekBar seekBar, int value,
					boolean fromUser) {
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
					mPlayer.seekTo(value);
					mProgress = value;
				}
			}
		});
	}
	
	private void setOnClickListener(){
		mPlayIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void setMediaPlayerListener(){
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (mProgress - 1 == mTotalDuration) {
					resetMediaPlayer();	
				}
			}
		});
	}

	private void resetMediaPlayer(){
		mPlayIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
		mProgress = 0;
		mDiscreteSeekBar.setProgress(0);
	}
	
	private void runOnSeekBarThread(){
		mHandler = new Handler();
		final Runnable mSeekBarRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDiscreteSeekBar.setProgress(mProgress);
				mAudioControllerCurrentDurationTv.setText(Utilities.getTimeFromMilliSeconds((long)mProgress));
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
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}
	
	protected BottomSheet getShareAction(){
    	return getShareActions(new BottomSheet.Builder(this).grid().title("Share To "), "Hello ").limit(R.integer.bs_initial_grid_row).build();
    }

	private void setMaterialRippleView() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
