/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
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
import com.application.utils.ApplicationLoader;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
import com.application.utils.FileLog;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.permission.PermissionHelper;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class VideoDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = VideoDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

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
	
	private AppCompatButton mVideoDownloadBtn;

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
	
	private long mReportStart = 0;
	private long mReportStop = 0;
	private long mReportDuration = 0;
	
	private boolean isVideoPause = false;

	private Handler mHandler;

	private Thread mSeekBarThread;
	
	private PauseControl  mThreadSafe = new PauseControl();

	private boolean isShareOptionEnable = true;
	private boolean isContentLiked = false;
	
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
	private String mContentFileDuration;
	private String mContentFileSize;
	private boolean mContentIsRead;
	
	private ArrayList<String> mContentLanguageList = new ArrayList<>();
	private ArrayList<String> mContentFileLinkList = new ArrayList<>();
	private ArrayList<String> mContentFilePathList = new ArrayList<>();
	private ArrayList<String> mContentFileSizeList = new ArrayList<>();
	
	private boolean isFromNotification = false;
	
	private PermissionHelper mPermissionHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		setSecurity();
		initToolBar();
		initUi();
		checkPermissionModel();
		getIntentData();
		initUiWithData();
		initAnimation();
		setUiListener();
		setSwipeRefreshLayoutCustomisation();
		ApplicationLoader.getPreferences().setVideoViewPosition(-1);
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		decryptFileOnResume();
		resumeVideoFromFullScreen();
		super.onResume();
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
	
	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		try{
			if (isShareOptionEnable) {
				menu.findItem(R.id.action_share).setVisible(true);
			} else {
				menu.findItem(R.id.action_share).setVisible(false);
			}
			
			if (isContentLiked) {
				menu.findItem(R.id.action_like).setIcon(R.drawable.ic_liked);
			} else {
				menu.findItem(R.id.action_like).setIcon(R.drawable.ic_like);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
			checkWhetherUserPlayVideoOrNotAndUpdateToApi();
			finish();
			AndroidUtilities.exitWindowAnimation(VideoDetailActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(VideoDetailActivity.this, MotherActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, isFromTraining(mCategory));
				startActivity(mIntent);
			}
			return true;
		case R.id.action_like:
			mVideoLikeTv.performClick();
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		checkWhetherUserPlayVideoOrNotAndUpdateToApi();
		if(isFromNotification){
			Intent mIntent = new Intent(VideoDetailActivity.this, MotherActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, isFromTraining(mCategory));
			startActivity(mIntent);
		}
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
	
	private void resumeVideoFromFullScreen(){
		try{
			int mVideoViewPosition = ApplicationLoader.getPreferences().getVideoViewPosition(); 
			if (mVideoViewPosition != -1) {
				mVideoDescFrameLayout.setVisibility(View.GONE);
				mVideoCoverIv.setVisibility(View.GONE);
				mVideoPlayIv.setVisibility(View.GONE);
				mVideoView.requestFocus();
				mVideoView.seekTo(mVideoViewPosition);
				mVideoView.start();
				mProgress = mVideoViewPosition; 
//				runOnSeekBarThread();
				mThreadSafe.unpause();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		refreshFeedActionFromApi();
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		
		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentVideoDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentVideoDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentVideoDetailLanguageHeaderTv);
		
		mVideoDownloadBtn = (AppCompatButton)findViewById(R.id.fragmentVideoDetailVideoDownloadBtn);

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

		mVideoPlayIv.setEnabled(false);
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(VideoDetailActivity.this).applyThemeWithBy(VideoDetailActivity.this, VideoDetailActivity.this, mToolBar, mVideoByTv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void getIntentData(){
		mIntent = getIntent();
		try{
			if(mIntent!=null){
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).toString();
				isFromNotification = mIntent.getBooleanExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, false);
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
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(VideoDetailActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(VideoDetailActivity.this);
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
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT));
			mContentLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)));
			
			Cursor mCursorFile = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID+"=?", new String[]{mId}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			if(mCursorFile!=null && mCursorFile.getCount() > 0){
				mCursorFile.moveToFirst();
				do {
					mContentFileLinkList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK)));
					mContentFilePathList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH)));
					mContentLanguageList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG)));
					mContentFileSizeList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE)));
					if(Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mContentFilePath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH));
						mContentFileLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK));
						mContentFileDuration = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION));
						mContentLanguage = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG));
						mContentFileSize = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE));
						mContentFileThumbLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK));
						mContentFileThumbPath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH));
					}
				} while (mCursorFile.moveToNext());
				
			}
			if(mCursorFile!=null)
				mCursorFile.close();
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
			
			Cursor mCursorFile = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID+"=?", new String[]{mId}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
			if(mCursorFile!=null && mCursorFile.getCount() > 0){
				mCursorFile.moveToFirst();
				do {
					mContentFileLinkList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK)));
					mContentFilePathList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH)));
					mContentLanguageList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG)));
					mContentFileSizeList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE)));
					if(Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
						mContentFilePath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH));
						mContentFileLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK));
						mContentLanguage = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG));
						mContentFileDuration = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION));
						mContentFileSize = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE));
						mContentFileThumbLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK));
						mContentFileThumbPath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH));
					}
				} while (mCursorFile.moveToNext());
				
			}
			if(mCursorFile!=null)
				mCursorFile.close();
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
				mVideoNewsLinkLayout.setVisibility(View.GONE);
			}
			
			if(mContentIsLike){
				mVideoLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
				mVideoLikeTv.setTextColor(getResources().getColor(R.color.red));
				isContentLiked = true;
			}
			
			if(mContentLanguageList!=null && mContentLanguageList.size() > 1){
				setLanguageChipsLayout(mContentLanguageList);
			}else{
				mLanguageLinearLayout.setVisibility(View.GONE);
			}
			
			if(checkIfFileExists(mContentFilePath)){
//				mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
				if(!TextUtils.isEmpty(mContentFilePath)){
					mVideoDownloadBtn.setVisibility(View.GONE);
					initVideoPlayer(mContentFilePath);	
					mVideoPlayIv.setEnabled(true);
				}else{
					mVideoDownloadBtn.setVisibility(View.VISIBLE);
					AndroidUtilities.showSnackBar(VideoDetailActivity.this, getResources().getString(R.string.file_download));
				}
			}else{
				downloadFileInBackground();
			}
			
			if(checkIfFileExists(mContentFileThumbPath)){
				mVideoCoverIv.setImageURI(Uri.parse(mContentFileThumbPath));
			}
			
			if(!TextUtils.isEmpty(mContentFileDuration)){
				mVideoDescTotalTv.setText(mContentFileDuration);
			}else{
				mVideoDescTotalTv.setVisibility(View.GONE);
			}
			
			updateReadInDb();
			supportInvalidateOptionsMenu();
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

	private void initVideoPlayer(String mVideoPath) {
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
						mReportStop = System.currentTimeMillis();
						mReportDuration += mReportStop - mReportStart;
						mReportDuration = 0;
						UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.PLAY, Utilities.getTimeFromMilliSeconds(mTotalDuration));
					}
				});
				
				runOnSeekBarThread();
			}
		});
		
		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				catchErrorOnVideoView();
				return true;
			}
		});
	}
	
	private void catchErrorOnVideoView(){
		try{
			AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(VideoDetailActivity.this);
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
					AndroidUtilities.enterWindowAnimation(VideoDetailActivity.this);
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
				mVideoMediaControllerCurrentDurationTv.setText(Utilities
						.getTimeFromMilliSeconds((long) mProgress));
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
		mVideoView.pause();
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
		setSwipeRefreshListener();
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
				if(checkIfFileExists(mContentFilePath)){
					if(!isVideoPause){
						mProgress = 0;
						UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.PLAY, "");
						playVideo();
						mReportStart = System.currentTimeMillis();
					}else{
						mVideoView.requestFocus();
						mVideoView.seekTo(ApplicationLoader.getPreferences().getVideoViewPosition());
						mVideoView.start();
						mVideoPlayIv.setVisibility(View.GONE);
						mVideoMediaControllerFrameLayout.setVisibility(View.VISIBLE);
						mVideoPauseIv.setVisibility(View.VISIBLE);
//						runOnSeekBarThread();
						mThreadSafe.unpause();
						mReportStart = System.currentTimeMillis();
					}					
				}else{
					downloadFileInBackground();
				}
			}
		});

		mVideoPauseIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
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
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					Intent mIntentVideoFullScreen = new Intent(VideoDetailActivity.this, VideoFullScreenActivity.class);
					mIntentVideoFullScreen.putExtra(AppConstants.INTENTCONSTANTS.FILEPATH, mContentFilePath);
					mIntentVideoFullScreen.putExtra(AppConstants.INTENTCONSTANTS.TIME, mVideoView.getCurrentPosition());
					mIntentVideoFullScreen.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
					mIntentVideoFullScreen.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mCategory);
					startActivity(mIntentVideoFullScreen);
//					mSeekBarThread.interrupt();
					mVideoView.pause();
					mThreadSafe.pause();
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
		});
		
		mVideoNewsLinkLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntentWebView = new Intent(VideoDetailActivity.this, WebViewActivity.class);
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
						mContentLikeCount  = String.valueOf(Integer.parseInt(mContentLikeCount)+1);
						if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
							ContentValues values = new ContentValues();
							values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, "true");
							values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mContentLikeCount);
							getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
						}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
							ContentValues values = new ContentValues();
							values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, "true");
							values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mContentLikeCount);
							getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
						}
						isContentLiked = true;
						mContentIsLike = true;
						mVideoLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
						mVideoLikeTv.setText(mContentLikeCount);
						mVideoLikeTv.setTextColor(getResources().getColor(R.color.red));
						UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LIKE, "");
					}else{
						if(isContentLiked){
							mContentLikeCount = String.valueOf(Integer.parseInt(mContentLikeCount)-1);
							if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
								ContentValues values = new ContentValues();
								values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, "false");
								values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mContentLikeCount);
								getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
							}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
								ContentValues values = new ContentValues();
								values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, "false");
								values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mContentLikeCount);
								getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
							}
							isContentLiked = false;
							mContentIsLike =false;
							mVideoLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
							mVideoLikeTv.setText(mContentLikeCount);
							mVideoLikeTv.setTextColor(getResources().getColor(R.color.item_activity_color));
							UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.UNLIKE, "");
						}
					}
					supportInvalidateOptionsMenu();
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
		});
		
		mVideoDownloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				downloadFileInBackground();
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
	
	@SuppressLint("NewApi") 
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				toolBarRefresh();
			}
		});
	}
	
	private void refreshFeedActionFromApi(){
		if(Utilities.isInternetConnected()){
			if(!mSwipeRefreshLayout.isRefreshing()){
				mSwipeRefreshLayout.setRefreshing(true);
			}
			FetchActionAsyncTask mFetchActionAsyncTask = new FetchActionAsyncTask(VideoDetailActivity.this, mId, mCategory, TAG);
			mFetchActionAsyncTask.execute();
			mFetchActionAsyncTask.setOnPostExecuteListener(new OnPostExecuteTaskListener() {
				@Override
				public void onPostExecute(String mViewCount, String mLikeCount) {
					// TODO Auto-generated method stub
					updateFeedActionToDBAndUi(mViewCount, mLikeCount);
					mSwipeRefreshLayout.setRefreshing(false);
					mToolBarMenuRefresh.setVisibility(View.VISIBLE);
					mToolBarMenuRefreshProgress.setVisibility(View.GONE);
				}
			});
		}
	}
	
	private void updateFeedActionToDBAndUi(String mViewCount, String mLikeCount){
		if(mViewCount!=null){
			ContentValues mValues = new ContentValues();
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, mViewCount);
				getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				mValues.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, mViewCount);
				getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, mValues, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
			}
			mVideoViewTv.setText(mViewCount);
		}
		
		if(mLikeCount!=null){
			ContentValues mValues = new ContentValues();
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mViewCount);
				getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				mValues.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mViewCount);
				getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, mValues, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
			}
			mVideoLikeTv.setText(mLikeCount);
		}
	}

	private void setLanguageChipsLayout(final ArrayList<String> mContentLanguageList) {
		mLanguageLinearLayout.setVisibility(View.VISIBLE);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(2, 2, 2, 2);
		for (int i = 0; i < mContentLanguageList.size(); i++) {
			ChipsLayout mChip = new ChipsLayout(this);
			if(mContentLanguageList.get(i).equalsIgnoreCase(mContentLanguage)){
				mChip.setDrawable(R.drawable.ic_language_done);				
			}else{
				mChip.setDrawable(R.drawable.ic_chips_download);
			}
			mChip.setText(mContentLanguageList.get(i));
			mChip.setLayoutParams(params);
			final int j = i;
			mChip.getChipLayout().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if(!mContentLanguageList.get(j).equalsIgnoreCase(mContentLanguage)){
						mContentLanguage = mContentLanguageList.get(j);
						updateVariablesForDiffLanguage(j);
						mVideoView.pause();
						downloadFileInBackgroundForDiffLanguage();
					}		 
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
	
	private void downloadFileInBackground(){
		try{
			mVideoPlayIv.setEnabled(false);
			if(Utilities.isInternetConnected()){
				checkContentFilePath();
				DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
						VideoDetailActivity.this, false, false,
						mContentFileLink, mContentFilePath,
						AppConstants.TYPE.VIDEO, Long.parseLong(mContentFileSize), TAG);
				mDownloadAsyncTask.execute();
				mVideoDownloadBtn.setVisibility(View.VISIBLE);
				mDownloadAsyncTask.setOnPostExecuteListener(new OnPostExecuteListener() {
					@Override
					public void onPostExecute(boolean isDownloaded) {
						// TODO Auto-generated method stub
						if(isDownloaded){
//							mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
							if(!TextUtils.isEmpty(mContentFilePath)){
								initVideoPlayer(mContentFilePath);
								mVideoPlayIv.setEnabled(true);
								mVideoDownloadBtn.setVisibility(View.GONE);
								Utilities.downloadQueue.postRunnable(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Utilities.downloadFile(AppConstants.TYPE.VIDEO, true, false, mContentFileThumbLink, Utilities.getFileName(mContentFileThumbLink));
									}
								});
							}else{
								mVideoDownloadBtn.setVisibility(View.VISIBLE);
								AndroidUtilities.showSnackBar(VideoDetailActivity.this, getResources().getString(R.string.file_download));
							}
						}
					}
				});
			}else{
				Utilities.showCrouton(VideoDetailActivity.this, mCroutonViewGroup, getResources().getString(R.string.file_not_available), Style.ALERT);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateVariablesForDiffLanguage(int mPosition){
		mContentFileLink = mContentFileLinkList.get(mPosition);
		mContentFilePath = mContentFilePathList.get(mPosition);
		mContentFileSize = mContentFileSizeList.get(mPosition);
	}
	
	private void checkContentFilePath(){
		try{
			if(TextUtils.isEmpty(mContentFilePath)){
				if(!TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mCategory)){
					Cursor mCursor;
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
						mCursor = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID+"=?", new String[]{mId}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
						if(mCursor.getCount()  >0){
								mCursor.moveToFirst();
								do {
									if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
										mContentFilePath = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH));
									}
								} while (mCursor.moveToNext());
						}
					}else{
						mCursor = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID+"=?", new String[]{mId}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
						if(mCursor.getCount()  >0){
								mCursor.moveToFirst();
								do {
									if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
										mContentFilePath = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH));
									}
								} while (mCursor.moveToNext());
						}
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
	
	private void updateLanguageChip(){
		try{
			AndroidUtilities.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int mPosition = -1;
					for (int i = 0; i < mContentLanguageList.size(); i++) {
						if (mContentLanguage.equalsIgnoreCase(mContentLanguageList.get(i))) {
							mPosition = i;
							break;
						}
						
					}
					if(mPosition!= -1){
						for(int i = 0; i< mContentLanguageList.size() ;i++){
							ChipsLayout mChip = (ChipsLayout)mLanguageFlowLayout.getChildAt(i);
							mChip.setDrawable(R.drawable.ic_chips_download);
						}
						ChipsLayout mChip = (ChipsLayout)mLanguageFlowLayout.getChildAt(mPosition);
						mChip.setDrawable(R.drawable.ic_language_done);
					}
				}
			});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void downloadFileInBackgroundForDiffLanguage(){
		mVideoPlayIv.setEnabled(false);
		if(Utilities.isInternetConnected()){
			DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
					VideoDetailActivity.this, false, false,
					mContentFileLink, mContentFilePath,
					AppConstants.TYPE.VIDEO, Long.parseLong(mContentFileSize), TAG);
			mDownloadAsyncTask.execute();
			mDownloadAsyncTask.setOnPostExecuteListener(new OnPostExecuteListener() {
				@Override
				public void onPostExecute(boolean isDownloaded) {
					// TODO Auto-generated method stub
					if(isDownloaded){
//						mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
						if(!TextUtils.isEmpty(mContentFilePath)){
							initVideoPlayer(mContentFilePath);
							mVideoPlayIv.setEnabled(true);
							updateDBWithDiffLanguageAsDefault();
							updateLanguageChip();
							Utilities.downloadQueue.postRunnable(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Utilities.downloadFile(AppConstants.TYPE.VIDEO, true, false, mContentFileThumbLink, Utilities.getFileName(mContentFileThumbLink));
								}
							});
						}	
					}
				}
			});
		}else{
			Utilities.showCrouton(VideoDetailActivity.this, mCroutonViewGroup, getResources().getString(R.string.internet_unavailable), Style.ALERT);
		}
	}
	
	private void updateDBWithDiffLanguageAsDefault(){
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			ContentValues values = new ContentValues();
			values.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, "false");
			getContentResolver().update(DBConstant.Mobcast_File_Columns.CONTENT_URI, values, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
			
			ContentValues mNewValues = new ContentValues();
			mNewValues.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, "true");
			getContentResolver().update(DBConstant.Mobcast_File_Columns.CONTENT_URI, mNewValues, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?" +"  AND "+ DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG + "=?", new String[]{mId, mContentLanguage});
		}else{
			ContentValues values = new ContentValues();
			values.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, "false");
			getContentResolver().update(DBConstant.Training_File_Columns.CONTENT_URI, values, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
			
			ContentValues mNewValues = new ContentValues();
			mNewValues.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, "true");
			getContentResolver().update(DBConstant.Training_File_Columns.CONTENT_URI, mNewValues, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?" +"  AND "+ DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG + "=?", new String[]{mId, mContentLanguage});
		}
	}

	private void playVideo() {
		mVideoDescFrameLayout.setVisibility(View.GONE);
		mVideoCoverIv.setVisibility(View.GONE);
		mVideoPlayIv.setVisibility(View.GONE);
		mVideoView.requestFocus();
		mVideoView.start();
		mVideoDownloadBtn.setVisibility(View.GONE);
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

	@SuppressWarnings("deprecation")
	private void showShareDialog() {
		showDialog(0);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.SHARE, "");
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		String mShareContent = mContentTitle + "\n\n" + mContentDesc + "\n\n\n"+ getResources().getString(R.string.share_advertisement);
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				mShareContent).limit(R.integer.bs_initial_grid_row).build();
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mVideoDownloadBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void setSwipeRefreshLayoutCustomisation() {
		mSwipeRefreshLayout.setColorSchemeColors(
				Color.parseColor(AppConstants.COLOR.MOBCAST_RED),
				Color.parseColor(AppConstants.COLOR.MOBCAST_YELLOW),
				Color.parseColor(AppConstants.COLOR.MOBCAST_PURPLE),
				Color.parseColor(AppConstants.COLOR.MOBCAST_GREEN),
				Color.parseColor(AppConstants.COLOR.MOBCAST_BLUE));
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
