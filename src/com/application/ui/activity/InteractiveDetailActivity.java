/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.sqlite.DBConstant;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.SmoothProgressBar;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class InteractiveDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = InteractiveDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mInteractiveTitleTv;
	private AppCompatTextView mInteractiveByTv;
	private AppCompatTextView mInteractiveViewTv;
	private AppCompatTextView mInteractiveSummaryTextTv;

	private AppCompatTextView mInteractiveNewsLinkTv;
	
	private AppCompatTextView mInteractiveLikeTv;
	
	private WebView mWebView;
	
	private SmoothProgressBar mSmoothProgressBar;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

	private LinearLayout mInteractiveNewsLinkLayout;

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
	private String mContentDate;
	private String mContentTime;
	private String mUrl;
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsRead;
	
	private int whichTheme = 0;
	
	private boolean isFromNotification = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interactive_detail);
		setSecurity();
		initToolBar();
		initUi();
		getIntentData();
		initUiWithData();
		setUiListener();
		setAnimation();
		setSwipeRefreshLayoutCustomisation();
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
			finish();
			AndroidUtilities.exitWindowAnimation(InteractiveDetailActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(InteractiveDetailActivity.this, MotherActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, isFromTraining(mCategory));
				startActivity(mIntent);
			}
			return true;
		case R.id.action_like:
			mInteractiveLikeTv.performClick();
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(InteractiveDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Text");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(InteractiveDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(isFromNotification){
			Intent mIntent = new Intent(InteractiveDetailActivity.this, MotherActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, isFromTraining(mCategory));
			startActivity(mIntent);
		}
	}

	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		refreshFeedActionFromApi();
		if(!TextUtils.isEmpty(mUrl) && Utilities.isInternetConnected()){
			mWebView.loadUrl(mUrl);	
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mSwipeRefreshLayout  = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

		mInteractiveTitleTv = (AppCompatTextView) findViewById(R.id.fragmentInteractiveDetailTitleTv);

		mInteractiveByTv = (AppCompatTextView) findViewById(R.id.fragmentInteractiveDetailByTv);
		mInteractiveSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentInteractiveDetailSummaryTv);
		mInteractiveViewTv = (AppCompatTextView) findViewById(R.id.fragmentInteractiveDetailViewTv);
		mInteractiveLikeTv = (AppCompatTextView) findViewById(R.id.fragmentInteractiveDetailLikeTv);
		
		mInteractiveNewsLinkTv = (AppCompatTextView)findViewById(R.id.fragmentInteractiveDetailLinkTv);
		
		mInteractiveNewsLinkLayout = (LinearLayout)findViewById(R.id.fragmentInteractiveDetailViewSourceLayout);
		
		mInteractiveNewsLinkLayout.setVisibility(View.GONE);
		
		mWebView = (WebView)findViewById(R.id.fragmentWebViewWebView);
		mSmoothProgressBar = (SmoothProgressBar)findViewById(R.id.fragmentWebViewProgressBar);
	}
	
	@SuppressLint("NewApi") 
	private void initWebViewClient(){
		mWebView.setWebChromeClient(new MyWebViewChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());
		try{
			if(AndroidUtilities.isAboveHoneyComb()){
				mWebView.getSettings().setBuiltInZoomControls(true);
				mWebView.getSettings().setDisplayZoomControls(false);
				mWebView.getSettings().setJavaScriptEnabled(true);
				mWebView.getSettings().setSupportZoom(true);
				mWebView.getSettings().setDomStorageEnabled(true);
				mWebView.getSettings().setSaveFormData(true);
				mWebView.getSettings().setSavePassword(true);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		if(!TextUtils.isEmpty(mUrl) && Utilities.isInternetConnected()){
			mWebView.loadUrl(mUrl);	
		}else{
			AndroidUtilities.showSnackBar(InteractiveDetailActivity.this, getResources().getString(R.string.internet_unavailable));
		}
	}
	
	private void applyTheme(){
		try{
			whichTheme = ApplicationLoader.getPreferences().getAppTheme();
			ThemeUtils.getInstance(InteractiveDetailActivity.this).applyThemeWithBy(InteractiveDetailActivity.this, InteractiveDetailActivity.this, mToolBar, mInteractiveByTv);
			ThemeUtils.applyThemeSmoothProgressBar(whichTheme, mSmoothProgressBar);
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
					AndroidUtilities.exitWindowAnimation(InteractiveDetailActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(InteractiveDetailActivity.this);
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
			isShareOptionEnable = mContentIsSharing;
			mUrl = mContentLink;
			setIntentDataToUi();
			initWebViewClient();
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
			isShareOptionEnable = mContentIsSharing;
			mUrl = mContentLink;
			setIntentDataToUi();
			initWebViewClient();
		}
	}
	
	private void setIntentDataToUi(){
		try{
			mInteractiveTitleTv.setText(mContentTitle);
			mInteractiveSummaryTextTv.setText(mContentDesc);
			if(!TextUtils.isEmpty(mContentLikeCount)){
				mInteractiveLikeTv.setText(mContentLikeCount);	
			}
			
			if(!TextUtils.isEmpty(mContentViewCount)){
				mInteractiveViewTv.setText(mContentViewCount);
			}
			
			mInteractiveByTv.setText(Utilities.formatBy(mContentBy, mContentDate, mContentTime));
			
			if(TextUtils.isEmpty(mContentLink)){
				mInteractiveNewsLinkLayout.setVisibility(View.GONE);
			}
			
			if(mContentIsLike){
				mInteractiveLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
				mInteractiveLikeTv.setTextColor(getResources().getColor(R.color.red));
				isContentLiked = true;
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
	
	private void initUiWithData(){
		mInteractiveNewsLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
	}
	
	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.InteractiveDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.ZoomIn).duration(500).playOn(mInteractiveTitleTv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
		setSwipeRefreshListener();
	}

	private void setOnClickListener() {
		mInteractiveNewsLinkLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntentWebView = new Intent(InteractiveDetailActivity.this, WebViewActivity.class);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE, AppConstants.INTENTCONSTANTS.OPEN);
				mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.LINK, mContentLink);
				startActivity(mIntentWebView);
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LINK, "");
			}
		});
		
		mInteractiveLikeTv.setOnClickListener(new View.OnClickListener() {
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
						mInteractiveLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
						mInteractiveLikeTv.setText(mContentLikeCount);
						mInteractiveLikeTv.setTextColor(getResources().getColor(R.color.red));
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
							mInteractiveLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
							mInteractiveLikeTv.setText(mContentLikeCount);
							mInteractiveLikeTv.setTextColor(getResources().getColor(R.color.item_activity_color));
							UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.UNLIKE, "");
						}
					}
					supportInvalidateOptionsMenu();
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
			FetchActionAsyncTask mFetchActionAsyncTask = new FetchActionAsyncTask(InteractiveDetailActivity.this, mId, mCategory, TAG);
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
			mInteractiveViewTv.setText(mViewCount);
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
			mInteractiveLikeTv.setText(mLikeCount);
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.SHARE, "");
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		String mShareContent = mContentTitle + "\n\n" + mContentDesc + "\n\nLink : " + mContentLink + "\n\n\n"+ getResources().getString(R.string.share_advertisement); 
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
	
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		}

		@Override
		public void onPageFinished(WebView view, String url) {
		}

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class MyWebViewChromeClient extends WebChromeClient{
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if(newProgress == 100){
				mSmoothProgressBar.setVisibility(View.GONE);
			}else{
				mSmoothProgressBar.setVisibility(View.VISIBLE);
			}
		}
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
