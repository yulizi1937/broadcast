/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.sqlite.DBConstant;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FileLog;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
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
	private AppCompatTextView mVideoLikeTv;
	private AppCompatTextView mVideoSummaryTextTv;
	private AppCompatTextView mVideoDescTotalTv;
	
	private AppCompatTextView mLiveStreamNewsLinkTv;

	private LinearLayout mLiveStreamNewsLinkLayout;
	
	private YouTubePlayerView youTubeView;
	
	private YouTubePlayer youTubePlayer;

	private boolean isShareOptionEnable = false;
	
	private Intent mIntent;
	private String mId;
	private String mCategory;
	private String mContentTitle;
	private String mContentDesc;
	private String mContentLikeCount;
	private String mContentViewCount;
	private String mContentBy;
	private String mContentLink;
	private String mContentDate;
	private String mContentTime;
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsRead;
	private String mContentLiveStreamURL;
	
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
		initUiWithData();
		getIntentData();
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
		case R.id.action_report:
			Intent mIntent  = new Intent(YouTubeLiveStreamActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:LiveStream");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(YouTubeLiveStreamActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	  @Override
	  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
	      boolean wasRestored) {
	    if (!wasRestored) {
//	      player.cueVideo("sw4hmqVPe0E");
	      youTubePlayer = player;
	      player.cueVideo("bHMZOrGOhKs");
	      if(mContentLiveStreamURL!=null)
	    	  player.cueVideo(mContentLiveStreamURL);  
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

		mVideoTitleTv = (AppCompatTextView) findViewById(R.id.fragmentLiveStreamYouTubeTitleTv);

		mVideoByTv = (AppCompatTextView) findViewById(R.id.fragmentLiveStreamYouTubeByTv);
		mVideoSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentLiveStreamYouTubeSummaryTv);
		mVideoViewTv = (AppCompatTextView) findViewById(R.id.fragmentLiveStreamYouTubeViewTv);
		mVideoLikeTv = (AppCompatTextView) findViewById(R.id.fragmentLiveStreamYouTubeLikeTv);
		
		mLiveStreamNewsLinkTv = (AppCompatTextView)findViewById(R.id.fragmentLiveStreamLinkTv);
		
		mLiveStreamNewsLinkLayout = (LinearLayout)findViewById(R.id.fragmentLiveStreamViewSourceLayout);
		
		youTubeView = (YouTubePlayerView) findViewById(R.id.fragmentLiveStreamYouTubePlayerView);
	    youTubeView.initialize(AppConstants.YOUTUBE.API_KEY, this);
	    
	}
	
	private void initUiWithData(){
		mLiveStreamNewsLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
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
						getDataFromDBForTraining(mCursor);
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getDataFromDBForMobcast(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT));
			mContentLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)));
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId}, null);
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				mContentLiveStreamURL = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE));
			}
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
			
			isShareOptionEnable = mContentIsSharing;
			setIntentDataToUi();
		}
	}
	
	private void getDataFromDBForTraining(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT));
			mContentLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LINK));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ)));
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId}, null);
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				mContentLiveStreamURL = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE));
			}
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
			
			isShareOptionEnable = mContentIsSharing;
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
				mLiveStreamNewsLinkLayout.setVisibility(View.GONE);
			}
			mContentLiveStreamURL = mContentLiveStreamURL.substring(mContentLiveStreamURL.lastIndexOf("=")+1, mContentLiveStreamURL.length());
			
			try{
				youTubePlayer.cueVideo(mContentLiveStreamURL);
			}catch(Exception e){
				
			}
			
			if(mContentIsLike){
				mVideoLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
				mVideoLikeTv.setTextColor(getResources().getColor(R.color.red));
			}
			
			updateReadInDb();
			if(!mContentIsRead){
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateReadInDb(){
		ContentValues values = new ContentValues();
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, "true");
			getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});	
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, "true");
			getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
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
		mLiveStreamNewsLinkLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntentWebView = new Intent(YouTubeLiveStreamActivity.this, WebViewActivity.class);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE, AppConstants.INTENTCONSTANTS.OPEN);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.LINK, mContentLink);
				startActivity(mIntentWebView);
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LINK, "");
			}
		});
		
		mVideoLikeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				try{
					if(!mContentIsLike){
						if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
							ContentValues values = new ContentValues();
							values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, "true");
							values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, String.valueOf(Integer.parseInt(mContentLikeCount)+1));
							getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
						}
						mVideoLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
						mVideoLikeTv.setText(String.valueOf(Integer.parseInt(mContentLikeCount)+1));
						mVideoLikeTv.setTextColor(getResources().getColor(R.color.red));
						UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LIKE, "");
					}
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
		});
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
