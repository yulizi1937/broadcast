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
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
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
import com.application.ui.adapter.ImageDetailPagerAdapter;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.ChipsLayout;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.FlowLayout;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
import com.application.utils.FileLog;
import com.application.utils.Style;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = ImageDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

	private LinearLayout mLanguageLinearLayout;

	private FlowLayout mLanguageFlowLayout;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mImageTitleTv;
	private AppCompatTextView mImageByTv;
	private AppCompatTextView mImageViewTv;
	private AppCompatTextView mImageLikeTv;
	private AppCompatTextView mImageSummaryTextTv;
	private AppCompatTextView mLanguageHeaderTv;

	private ViewPager mImageViewPager;
	private CirclePageIndicator mImageCirclePageIndicator;

	private AppCompatTextView mImageNewsLinkTv;

	private LinearLayout mImageNewsLinkLayout;

	private ImageView mImageNextIv;
	private ImageView mImagePrevIv;
	private ImageView mShareIv;

	private ImageDetailPagerAdapter mAdapter;

	private ArrayList<String> mArrayListString;

	private boolean isTraining = false;// HDFC
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
	private String mContentFileThumbLink;
	private String mContentFileThumbPath;
	private String mContentLanguage;
	private String mContentDate;
	private String mContentFileSize;
	private String mContentTime;
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsRead;
	
	private ArrayList<String> mContentFileLink = new ArrayList<>();
	private ArrayList<String> mContentFilePath = new ArrayList<>();
	private ArrayList<String> mContentDecryptedFilePath = new ArrayList<>();


	private ArrayList<String> mContentLanguageList = new ArrayList<>();
	private ArrayList<String> mContentFileLinkList = new ArrayList<>();
	private ArrayList<String> mContentFilePathList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
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
		decryptFileOnResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		deleteDecryptedFile();
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
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		try{
			if (isShareOptionEnable) {
				menu.findItem(R.id.action_share).setVisible(true);
			} else {
				menu.findItem(R.id.action_share).setVisible(false);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return super.onPrepareOptionsPanel(view, menu);
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
			AndroidUtilities.exitWindowAnimation(ImageDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(ImageDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Image");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ImageDetailActivity.this);
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
		}, R.integer.refresh_time_detail);*/
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentImageDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentImageDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailLanguageHeaderTv);

		mImageTitleTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailTitleTv);
		mImageByTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailByTv);
		mImageSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailSummaryTv);
		mImageViewTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailViewTv);
		mImageLikeTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailLikeTv);

		mImageNextIv = (ImageView) findViewById(R.id.fragmentImageDetailNextIv);
		mImagePrevIv = (ImageView) findViewById(R.id.fragmentImageDetailPreviousIv);

		mImageViewPager = (ViewPager) findViewById(R.id.fragmentImageDetailViewPager);
		mImageCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentImageDetailCirclePageIndicator);

		mImageNewsLinkTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailLinkTv);

		mImageNewsLinkLayout = (LinearLayout) findViewById(R.id.fragmentImageDetailViewSourceLayout);

	}

	private void initUiWithData() {
		mImageNewsLinkTv.setText(Html.fromHtml(getResources().getString(
				R.string.sample_news_detail_link)));

	}

	private void getIntentData() {
		mIntent = getIntent();
		try {
			if (mIntent != null) {
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(
						AppConstants.INTENTCONSTANTS.CATEGORY).toString();
				if (!TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mCategory)) {
					if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)) {
						mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI,null,DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID+ "=?",new String[] { mId },DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID+ " DESC");
						getDataFromDBForMobcast(mCursor);
					} else if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)) {
						mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI,null,DBConstant.Training_Columns.COLUMN_TRAINING_ID+ "=?",new String[] { mId },DBConstant.Training_Columns.COLUMN_TRAINING_ID+ " DESC");
						getDataFromDBForTraining(mCursor);
					}
					if (mCursor != null) {
						mCursor.close();
					}
				} else {
					finish();
					AndroidUtilities.exitWindowAnimation(ImageDetailActivity.this);
				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(ImageDetailActivity.this);
		}
	}

	private void getDataFromDBForMobcast(Cursor mCursor) {
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)));
			mContentIsSharing = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT));
			mContentLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)));

			Cursor mCursorFile = getContentResolver().query(
					DBConstant.Mobcast_File_Columns.CONTENT_URI, null,
					DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?",
					new String[] { mId },
					DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			if (mCursorFile != null && mCursorFile.getCount() > 0) {
				mCursorFile.moveToFirst();
				do {
					mContentFileLinkList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK)));
					mContentFilePathList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH)));
					mContentLanguageList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG)));
					if (Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))) {
						mContentFilePath.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH)));
						mContentFileLink.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK)));
						mContentLanguage = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG));
						mContentFileSize = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE));
						mContentFileThumbLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK));
						mContentFileThumbPath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH));
					}
				} while (mCursorFile.moveToNext());

			}
			if (mCursorFile != null)
				mCursorFile.close();
			isShareOptionEnable = mContentIsSharing;
			setIntentDataToUi();
		}
	}
	
	private void getDataFromDBForTraining(Cursor mCursor) {
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE)));
			mContentIsSharing = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT));
			mContentLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LINK));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ)));

			Cursor mCursorFile = getContentResolver().query(
					DBConstant.Training_File_Columns.CONTENT_URI, null,
					DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?",
					new String[] { mId },
					DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
			if (mCursorFile != null && mCursorFile.getCount() > 0) {
				mCursorFile.moveToFirst();
				do {
					mContentFileLinkList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK)));
					mContentFilePathList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH)));
					mContentLanguageList.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG)));
					if (Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))) {
						mContentFilePath.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH)));
						mContentFileLink.add(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK)));
						mContentLanguage = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG));
						mContentFileSize = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE));
						mContentFileThumbLink = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK));
						mContentFileThumbPath = mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH));
					}
				} while (mCursorFile.moveToNext());

			}
			if (mCursorFile != null)
				mCursorFile.close();
			isShareOptionEnable = mContentIsSharing;
			setIntentDataToUi();
		}
	}

	private void setIntentDataToUi() {
		try {
			mImageTitleTv.setText(mContentTitle);
			mImageSummaryTextTv.setText(mContentDesc);
			if (!TextUtils.isEmpty(mContentLikeCount)) {
				mImageLikeTv.setText(mContentLikeCount);
			}

			if (!TextUtils.isEmpty(mContentViewCount)) {
				mImageViewTv.setText(mContentViewCount);
			}

			mImageByTv.setText(Utilities.formatBy(mContentBy, mContentDate,
					mContentTime));

			if (TextUtils.isEmpty(mContentLink)) {
				mImageNewsLinkLayout.setVisibility(View.GONE);
			}
			
			if(mContentIsLike){
				mImageLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
				mImageLikeTv.setTextColor(getResources().getColor(R.color.red));
			}
			
			if (mContentLanguageList != null && mContentLanguageList.size() > 1) {
//				setLanguageChipsLayout(mContentLanguageList);
				mLanguageLinearLayout.setVisibility(View.GONE);
			} else {
				mLanguageLinearLayout.setVisibility(View.GONE);
			}

			boolean isDownloadedAndExists = true;
			for (int i = 0; i < mContentFilePath.size(); i++) {
				if (checkIfFileExists(mContentFilePath.get(i))) {
					mContentDecryptedFilePath.add(Utilities.fbConcealDecryptFile(TAG,
							new File(mContentFilePath.get(i))));
					if (!TextUtils.isEmpty(mContentDecryptedFilePath.get(i))) {
						isDownloadedAndExists &= true;
					}else{
						isDownloadedAndExists &= false;
					}
				} else {
					downloadFileInBackground(i);
				}
			}
			
			if(isDownloadedAndExists){
				setImageViewPager(mContentDecryptedFilePath);
			}

			if (checkIfFileExists(mContentFileThumbPath)) {
			}

			updateReadInDb();
			supportInvalidateOptionsMenu();
			if (!mContentIsRead) {
				UserReport.updateUserReportApi(mId, mCategory,
						AppConstants.REPORT.READ, "");
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void downloadFileInBackground(final int position) {
		try{
			DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
					ImageDetailActivity.this, false, true,
					mContentFileLink.get(position), mContentFilePath.get(position),
					AppConstants.TYPE.IMAGE, Long.parseLong(mContentFileSize), TAG);
			mDownloadAsyncTask.execute();
			mDownloadAsyncTask
					.setOnPostExecuteListener(new OnPostExecuteListener() {
						@Override
						public void onPostExecute(boolean isDownloaded) {
							// TODO Auto-generated method stub
							if (isDownloaded) {
								mContentDecryptedFilePath.add(Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath.get(position))));
								if (!TextUtils.isEmpty(mContentDecryptedFilePath.get(position))) {
									if(mContentFilePath.size()-1 == position){
										setImageViewPager(mContentDecryptedFilePath);
										downloadThumbnailInBackground();
									}
								}
							}
						}
					});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void downloadThumbnailInBackground(){
		Utilities.downloadQueue.postRunnable(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Utilities.downloadFile(AppConstants.TYPE.IMAGE, true, false, mContentFileThumbLink, Utilities.getFileName(mContentFileThumbLink));				
			}
		});
	}

	private void updateReadInDb() {
		ContentValues values = new ContentValues();
		if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)) {
			values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ,
					"true");
			getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI,
					values,
					DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?",
					new String[] { mId });
		} else if (mCategory
				.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)) {
			values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ,
					"true");
			getContentResolver().update(
					DBConstant.Training_Columns.CONTENT_URI, values,
					DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?",
					new String[] { mId });
		}
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.ImageDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
		setSwipeRefreshListener();
	}

	private void setOnClickListener() {
		mImageNewsLinkLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntentWebView = new Intent(ImageDetailActivity.this, WebViewActivity.class);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE, AppConstants.INTENTCONSTANTS.OPEN);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.LINK, mContentLink);
				startActivity(mIntentWebView);
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LINK, "");
			}
		});
		
		mImageLikeTv.setOnClickListener(new View.OnClickListener() {
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
						mImageLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
						mImageLikeTv.setText(String.valueOf(Integer.parseInt(mContentLikeCount)+1));
						mImageLikeTv.setTextColor(getResources().getColor(R.color.red));
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
			FetchActionAsyncTask mFetchActionAsyncTask = new FetchActionAsyncTask(ImageDetailActivity.this, mId, mCategory, TAG);
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
			mImageViewTv.setText(mViewCount);
		}
		
		if(mLikeCount!=null){
			ContentValues mValues = new ContentValues();
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mViewCount);
				getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
			}
			mImageLikeTv.setText(mLikeCount);
		}
	}

	private void setImageViewPager(ArrayList<String> mContentFilePath) {
		mArrayListString = new ArrayList<String>();
		for (int i = 0; i < mContentFilePath.size(); i++) {
			mArrayListString.add(mContentFilePath.get(i));
		}
		mAdapter = new ImageDetailPagerAdapter(getSupportFragmentManager(),
				mArrayListString);
		mImageViewPager.setAdapter(mAdapter);
		mImageCirclePageIndicator.setViewPager(mImageViewPager);
	}

	private void setLanguageChipsLayout(
			final ArrayList<String> mContentLanguageList) {
		mLanguageLinearLayout.setVisibility(View.VISIBLE);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(2, 2, 2, 2);
		for (int i = 0; i < mContentLanguageList.size(); i++) {
			ChipsLayout mChip = new ChipsLayout(this);
			mChip.setDrawable(R.drawable.ic_chips_download);
			mChip.setText(mContentLanguageList.get(i));
			mChip.setLayoutParams(params);
			final int j = i;
			mChip.getChipLayout().setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
						}
					});
			mLanguageFlowLayout.addView(mChip);
		}
	}

	private void decryptFileOnResume(){
		try{
			if(mContentFilePathList!=null && mContentFilePathList.size() > 0){
				mContentDecryptedFilePath.clear();
				for(int i = 0; i < mContentFilePathList.size();i++){
					String mPath = mContentFilePathList.get(i);
					if(checkIfFileExists(mPath)){
						mContentDecryptedFilePath.add(Utilities.fbConcealDecryptFile(TAG, new File(mPath)));
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void deleteDecryptedFile(){
		try{
			if(mContentDecryptedFilePath!=null && mContentDecryptedFilePath.size() > 0){
				for(int i = 0 ;i < mContentDecryptedFilePath.size();i++){
					if(Utilities.isContainsDecrypted(mContentDecryptedFilePath.get(i))){
						new File(mContentDecryptedFilePath.get(i)).delete();
					}
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

	protected BottomSheet getShareAction() {
		String mShareContent = mContentTitle + "\n\n" + mContentDesc + "\n\n\n"+ getResources().getString(R.string.share_advertisement);
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				mShareContent).limit(R.integer.bs_initial_grid_row).build();
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
