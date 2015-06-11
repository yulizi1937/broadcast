/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
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
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class YouTubeLiveStreamActivity extends YouTubeFailureRecoveryActivity implements AppCompatCallback{
	private static final String TAG = YouTubeLiveStreamActivity.class
			.getSimpleName();

	private Toolbar mToolBar;
	
	private AppCompatDelegate mAppComatDelegate;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mVideoTitleTv;
	private AppCompatTextView mVideoByTv;
	private AppCompatTextView mVideoViewTv;
	private AppCompatTextView mVideoSummaryTextTv;
	private AppCompatTextView mVideoDescTotalTv;
	
	private YouTubePlayerView youTubeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAppComatDelegate = AppCompatDelegate.create(this, this);
		mAppComatDelegate.installViewFactory();
        mAppComatDelegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        mAppComatDelegate.setContentView(R.layout.activity_livestream_youtube_detail);
        initToolBar();
        mAppComatDelegate.setSupportActionBar(mToolBar);
		initUi();
		setUiListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_event_detail, menu);
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
			AndroidUtilities.exitWindowAnimation(YouTubeLiveStreamActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	  @Override
	  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
	      boolean wasRestored) {
	    if (!wasRestored) {
	      player.cueVideo("sw4hmqVPe0E");
	    }
	  }

	  @Override
	  protected YouTubePlayer.Provider getYouTubePlayerProvider() {
	    return (YouTubePlayerView) findViewById(R.id.fragmentLiveStreamYouTubePlayerView);
	  }
	  
		/* (non-Javadoc)
		 * @see android.support.v7.app.AppCompatCallback#onSupportActionModeFinished(android.support.v7.view.ActionMode)
		 */
		@Override
		public void onSupportActionModeFinished(ActionMode arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see android.support.v7.app.AppCompatCallback#onSupportActionModeStarted(android.support.v7.view.ActionMode)
		 */
		@Override
		public void onSupportActionModeStarted(ActionMode arg0) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see android.support.v7.app.AppCompatCallback#onWindowStartingSupportActionMode(android.support.v7.view.ActionMode.Callback)
		 */
		@Override
		@Nullable
		public ActionMode onWindowStartingSupportActionMode(Callback arg0) {
			// TODO Auto-generated method stub
			return null;
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

		mVideoTitleTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailTitleTv);

		mVideoByTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailByTv);
		mVideoSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailSummaryTv);
		mVideoViewTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailViewTv);
		mVideoDescTotalTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailDescTotalTextView);
		
		youTubeView = (YouTubePlayerView) findViewById(R.id.fragmentLiveStreamYouTubePlayerView);
	    youTubeView.initialize(AppConstants.YOUTUBE.API_KEY, this);
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
				R.string.YouTubeLiveStreamActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
//		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}

	private void setOnClickListener() {
	}

	private void setOnTouchListener() {
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
