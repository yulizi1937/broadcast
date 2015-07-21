/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.application.ui.view.ChipsLayout;
import com.application.ui.view.DiscreteSeekBar;
import com.application.ui.view.DiscreteSeekBar.OnProgressChangeListener;
import com.application.ui.view.FlowLayout;
import com.application.ui.view.LineRenderer;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.VisualizerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FileLog;
import com.application.utils.Style;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
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
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private LinearLayout mLanguageLinearLayout;

	private FlowLayout mLanguageFlowLayout;

	private FrameLayout mCroutonViewGroup;
	
	private AppCompatTextView mAudioTitleTv;
	private AppCompatTextView mAudioByTv;
	private AppCompatTextView mAudioViewTv;
	private AppCompatTextView mAudioLikeTv;
	private AppCompatTextView mAudioSummaryTextTv;
	private AppCompatTextView mLanguageHeaderTv;
	
	private AppCompatTextView mAudioControllerCurrentDurationTv;
	
	private VisualizerView mVisualizerView;
	
	private ImageView mShareIv;
	private ImageView mPlayIv;
	
	private AppCompatTextView mAudioNewsLinkTv;

	private LinearLayout mAudioNewsLinkLayout;
	
	private DiscreteSeekBar mDiscreteSeekBar;
	int mProgress = 0;
	int mTotalDuration = 0;
	Handler mHandler;
	
	private MediaPlayer mPlayer;
	private Thread mSeekBarThread;
	
	private boolean isShareOptionEnable = true;
	
	private boolean isPlaying = false;
	
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
	private String mContentFileSize;
	private boolean mContentIsRead;
	
	private ArrayList<String> mContentLanguageList = new ArrayList<>();
	private ArrayList<String> mContentFileLinkList = new ArrayList<>();
	private ArrayList<String> mContentFilePathList = new ArrayList<>();
	private ArrayList<String> mContentFileSizeList = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_detail);
		initToolBar();
		initUi();
		getIntentData();
		initUiWithData();
		setUiListener();
		setSwipeRefreshLayoutCustomisation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		decryptFileOnResume();
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
			if(isShareOptionEnable){
				menu.findItem(R.id.action_share).setVisible(true);
			}else{
				menu.findItem(R.id.action_share).setVisible(false);
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
			finish();
			AndroidUtilities.exitWindowAnimation(AudioDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(AudioDetailActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Audio");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(AudioDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		refreshFeedActionFromApi();
		/*new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mToolBarMenuRefresh.setVisibility(View.VISIBLE);
				mToolBarMenuRefreshProgress.setVisibility(View.GONE);
			}
		}, 5000);*/
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		
		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentAudioDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentAudioDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentAudioDetailLanguageHeaderTv);
		
		mAudioTitleTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailTitleTv);
		
		mAudioByTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailByTv);
		mAudioSummaryTextTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailSummaryTv);
		mAudioViewTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailViewTv);
		mAudioLikeTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailLikeTv);
		mAudioControllerCurrentDurationTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailSeekBarTv);
		
		mPlayIv = (ImageView)findViewById(R.id.fragmentAudioDetailImageView);
		
		mVisualizerView = (VisualizerView)findViewById(R.id.fragmentAudioDetailVisualizerView);
		
		mDiscreteSeekBar = (DiscreteSeekBar)findViewById(R.id.fragmentAudioDetailSeekBar);
		
		mAudioNewsLinkTv = (AppCompatTextView)findViewById(R.id.fragmentAudioDetailLinkTv);
		
		mAudioNewsLinkLayout = (LinearLayout)findViewById(R.id.fragmentAudioDetailViewSourceLayout);
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
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(AudioDetailActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(AudioDetailActivity.this);
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
			mAudioTitleTv.setText(mContentTitle);
			mAudioSummaryTextTv.setText(mContentDesc);
			if(!TextUtils.isEmpty(mContentLikeCount)){
				mAudioLikeTv.setText(mContentLikeCount);	
			}
			
			if(!TextUtils.isEmpty(mContentViewCount)){
				mAudioViewTv.setText(mContentViewCount);
			}
			
			mAudioByTv.setText(Utilities.formatBy(mContentBy, mContentDate, mContentTime));
			
			if(TextUtils.isEmpty(mContentLink)){
				mAudioNewsLinkLayout.setVisibility(View.GONE);
			}
			
			if(mContentIsLike){
				mAudioLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
				mAudioLikeTv.setTextColor(getResources().getColor(R.color.red));
			}
			
			if(mContentLanguageList!=null && mContentLanguageList.size() > 1){
				setLanguageChipsLayout(mContentLanguageList);
			}else{
				mLanguageLinearLayout.setVisibility(View.GONE);
			}
			
			if(checkIfFileExists(mContentFilePath)){
				mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
				if(!TextUtils.isEmpty(mContentFilePath)){
					initMediaPlayer(mContentFilePath);	
				}
			}else{
				downloadFileInBackground();
			}
			
			if(checkIfFileExists(mContentFileThumbPath)){
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
	
	private void downloadFileInBackground(){
		DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
				AudioDetailActivity.this, false, true,
				mContentFileLink, mContentFilePath,
				AppConstants.TYPE.AUDIO, Long.parseLong(mContentFileSize), TAG);
		mDownloadAsyncTask.execute();
		mDownloadAsyncTask.setOnPostExecuteListener(new OnPostExecuteListener() {
			@Override
			public void onPostExecute(boolean isDownloaded) {
				// TODO Auto-generated method stub
				if(isDownloaded){
					mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
					if(!TextUtils.isEmpty(mContentFilePath)){
						initMediaPlayer(mContentFilePath);
					}	
				}
			}
		});
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

	private void initUiWithData(){
		mAudioNewsLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
	}
	
	  private void initMediaPlayer(String mPath)
	  {
		mPlayer = MediaPlayer.create(this, Uri.parse(mPath));
	    mPlayer.setLooping(false);
	    mPlayer.start();
	    mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mTotalDuration = mPlayer.getDuration();
				mPlayIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_audio));
				mDiscreteSeekBar.setMin(0);
				mDiscreteSeekBar.setMax(mTotalDuration);
				isPlaying = true;
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
		setSwipeRefreshListener();
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
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.PLAY, "");
				if(isPlaying){
					mPlayer.pause();
					mPlayIv.setImageResource(R.drawable.ic_play_audio);
					isPlaying = false;
				}else{
					mPlayer.start();
				}
			}
		});
		
		mAudioNewsLinkLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntentWebView = new Intent(AudioDetailActivity.this, WebViewActivity.class);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE, AppConstants.INTENTCONSTANTS.OPEN);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.LINK, mContentLink);
				startActivity(mIntentWebView);
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LINK, "");
			}
		});
		
		mAudioLikeTv.setOnClickListener(new View.OnClickListener() {
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
						mAudioLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
						mAudioLikeTv.setText(String.valueOf(Integer.parseInt(mContentLikeCount)+1));
						mAudioLikeTv.setTextColor(getResources().getColor(R.color.red));
						UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LIKE, "");
					}
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
				
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
			FetchActionAsyncTask mFetchActionAsyncTask = new FetchActionAsyncTask(AudioDetailActivity.this, mId, mCategory, TAG);
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
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_READ_NO, mViewCount);
				getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
			}
			mAudioViewTv.setText(mViewCount);
		}
		
		if(mLikeCount!=null){
			ContentValues mValues = new ContentValues();
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mViewCount);
				getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
			}
			mAudioLikeTv.setText(mLikeCount);
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
						mPlayer.stop();
						downloadFileInBackgroundForDiffLanguage();
					}		 
				}
			});
			mLanguageFlowLayout.addView(mChip);
		}
	}
	
	private void updateVariablesForDiffLanguage(int mPosition){
		mContentFileLink = mContentFileLinkList.get(mPosition);
		mContentFilePath = mContentFilePathList.get(mPosition);
		mContentFileSize = mContentFileSizeList.get(mPosition);
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
		if(Utilities.isInternetConnected()){
			DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
					AudioDetailActivity.this, false, true,
					mContentFileLink, mContentFilePath,
					AppConstants.TYPE.AUDIO, Long.parseLong(mContentFileSize), TAG);
			mDownloadAsyncTask.execute();
			mDownloadAsyncTask.setOnPostExecuteListener(new OnPostExecuteListener() {
				@Override
				public void onPostExecute(boolean isDownloaded) {
					// TODO Auto-generated method stub
					if(isDownloaded){
						mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
						if(!TextUtils.isEmpty(mContentFilePath)){
							initMediaPlayer(mContentFilePath);
							updateDBWithDiffLanguageAsDefault();
							updateLanguageChip();
						}	
					}
				}
			});
		}else{
			Utilities.showCrouton(AudioDetailActivity.this, mCroutonViewGroup, getResources().getString(R.string.internet_unavailable), Style.ALERT);
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
			
		}
	}
	
	private void setMediaPlayerListener(){
		try{
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if (mProgress - 1 == mTotalDuration) {
						resetMediaPlayer();	
					}
				}
			});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void resetMediaPlayer(){
		mPlayIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_audio));
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
	
	private void decryptFileOnResume(){
		try{
			if(mContentFilePath!=null){
				mContentFilePath = mContentFilePath.replace("_decrypted", "");
				mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void deleteDecryptedFile(){
		try{
			if(mContentFilePath!=null){
				if(mContentFilePath.contains("_decrypted")){
					new File(mContentFilePath).delete();
				}	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}
	
	protected BottomSheet getShareAction(){
		String mShareContent = mContentTitle + "\n\n" + mContentDesc + "\n\n\n"+ getResources().getString(R.string.share_advertisement);
    	return getShareActions(new BottomSheet.Builder(this).grid().title(mShareContent), "Hello ").limit(R.integer.bs_initial_grid_row).build();
    }

	private void setMaterialRippleView() {
		try {
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
}
