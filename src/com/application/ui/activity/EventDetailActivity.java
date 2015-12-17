/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.sqlite.DBConstant;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.ThemeUtils;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
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
public class EventDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = EventDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

	private AppCompatTextView mEventCoverLeftNumberTv;
	private AppCompatTextView mEventCoverLeftTextTv;
	private AppCompatTextView mEventTitleTv;
	private AppCompatTextView mEventByTv;
	private AppCompatTextView mEventViewTv;
	private AppCompatTextView mEventLikeTv;
	private AppCompatTextView mEventTimeDateNumberTv;
	private AppCompatTextView mEventTimeDateTextTv;
	private AppCompatTextView mEventTimeTillDayTv;
	private AppCompatTextView mEventTimeTillTv;
	private AppCompatTextView mEventTimeHoursNumberTv;
	private AppCompatTextView mEventTImeHoursTextTv;
	private AppCompatTextView mEventLocationTextTv;
	private AppCompatTextView mEventLocationViewMapTv;
	private AppCompatTextView mEventAttendInvitedNumberTv;
	private AppCompatTextView mEventAttendGoingNumberTv;
	private AppCompatTextView mEventSummaryTextTv;
	

	private AppCompatButton mEventAttendJoinBtn;
	private AppCompatButton mEventAttendDeclineBtn;

	private ImageView mEventShareIv;
	private ImageView mEventAddToCalIv;

	private ImageView mEventCoverIv;
	
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
	private String mContentDate;
	private String mContentTime;
	private String mContentEventFromTime;
	private String mContentEventDate;
	private String mContentEventToTime;
	private String mContentEventVenue;
	private String mContentEventInvited;
	private String mContentEventGoing;
	private String mContentEventMap;
	private String mContentFileLink;
	private String mContentFilePath;
	private String mContentFileSize;
	private String mContentIsGoing;
	
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsRead;

	private boolean isFromNotification = false;
	
	private PermissionHelper mPermissionHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		setSecurity();
		initToolBar();
		initUi();
		checkPermissionModel();
		getIntentData();
		setUiListener();
		setSwipeRefreshLayoutCustomisation();
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		decryptFileOnResume();
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		deleteDecryptedFile();
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
		inflater.inflate(R.menu.menu_event_detail, menu);
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
			AndroidUtilities.exitWindowAnimation(EventDetailActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(EventDetailActivity.this, MotherActivity.class);
				startActivity(mIntent);
			}
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_like:
			mEventLikeTv.performClick();
			return true;
		case R.id.action_add_cal:
			addEventToCalendar();
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
			Intent mIntent = new Intent(EventDetailActivity.this, MotherActivity.class);
			startActivity(mIntent);
		}
	}

	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		refreshFeedActionFromApi();
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mSwipeRefreshLayout  = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		
		mEventCoverLeftNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailLeftNumberTv);
		mEventCoverLeftTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailLeftTextTv);

		mEventTitleTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailTitleTv);

		mEventByTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailByTv);
		mEventViewTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailViewTv);
		mEventLikeTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailLikeTv);

		mEventTimeDateNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDateTv);
		mEventTimeDateTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDateTextTv);
		mEventTimeTillDayTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDayTv);
		mEventTimeTillTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDayTillTv);
		mEventTimeHoursNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventHoursTv);
		mEventTImeHoursTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventHoursTextTv);

		mEventLocationTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventLocationTv);
		mEventLocationViewMapTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventLocationViewMapTv);

		mEventAttendInvitedNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetaiTotalAttendTv);
		mEventAttendGoingNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetaiTotalGoingTv);

		mEventSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailSummaryTv);

		mEventAttendJoinBtn = (AppCompatButton) findViewById(R.id.fragmentEventDetailJoinBtn);
		mEventAttendDeclineBtn = (AppCompatButton) findViewById(R.id.fragmentEventDetailDeclineBtn);

		mEventShareIv = (ImageView) findViewById(R.id.fragmentEventDetailEventShareIv);
		mEventAddToCalIv = (ImageView) findViewById(R.id.fragmentEventDetailEventAddToCalendarIv);
		mEventCoverIv = (ImageView) findViewById(R.id.fragmentEventDetailEventCoverIv);

		setHtmlData();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.EventDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(EventDetailActivity.this).applyThemeWithBy(EventDetailActivity.this, EventDetailActivity.this, mToolBar, mEventByTv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setSwipeRefreshListener();
	}

	private void setHtmlData() {
		mEventLocationViewMapTv.setText(Html.fromHtml(getResources().getString(
				R.string.sample_event_view_map)));
	}

	private void setOnClickListener() {
		mEventShareIv.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showShareDialog();
			}
		});
		
		mEventLocationViewMapTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				try {
					String[] mLatitude = mContentEventMap.split(",");
					String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
							Float.parseFloat(mLatitude[0]), Float.parseFloat(mLatitude[1]));
					Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(uri));
					mapIntent.setPackage("com.google.android.apps.maps");
					if (mapIntent.resolveActivity(getPackageManager()) != null) {
					    startActivity(mapIntent);
					}else{
						String mURL = String.format(Locale.ENGLISH, "http://www.google.com/maps/place/%f,%f",Float.parseFloat(mLatitude[0]), Float.parseFloat(mLatitude[1]));
						Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
						startActivity(mIntent);
					}
				} catch (Exception e) {
					FileLog.e(TAG, e.toString());
				}
			}
		});
		
		mEventLikeTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				try{
					if(!mContentIsLike){
						mContentLikeCount  = String.valueOf(Integer.parseInt(mContentLikeCount)+1);
						if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
							ContentValues values = new ContentValues();
							values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, "true");
							values.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, mContentLikeCount);
							getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, values, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
						}
						isContentLiked = true;
						mContentIsLike = true;
						mEventLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
						mEventLikeTv.setText(mContentLikeCount);
						mEventLikeTv.setTextColor(getResources().getColor(R.color.red));
						UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LIKE, "");
					}else{
						if(isContentLiked){
							mContentLikeCount = String.valueOf(Integer.parseInt(mContentLikeCount)-1);
							if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
								ContentValues values = new ContentValues();
								values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, "false");
								values.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, mContentLikeCount);
								getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, values, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
							}
							isContentLiked = false;
							mContentIsLike =false;
							mEventLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
							mEventLikeTv.setText(mContentLikeCount);
							mEventLikeTv.setTextColor(getResources().getColor(R.color.item_activity_color));
							UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.UNLIKE, "");
						}
					}
					supportInvalidateOptionsMenu();
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
		});
		
		mEventAttendJoinBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.JOIN, "");
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, "1");
				getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
				mEventAttendJoinBtn.setText(getResources().getString(R.string.going));
				mEventAttendJoinBtn.setBackgroundResource(R.drawable.shape_button_green_fill);
				mEventAttendDeclineBtn.setText(getResources().getString(R.string.fragment_event_detail_button_decline));
				mEventAttendDeclineBtn.setBackgroundResource(R.drawable.shape_button_orange_border);
				mEventAttendDeclineBtn.setTextColor(getResources().getColor(R.color.orange));
				addEventToCalendarDirectly();
				toolBarRefresh();
			}
		});
		
		mEventAttendDeclineBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.DECL, "");
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, "0");
				getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
				mEventAttendDeclineBtn.setText(getResources().getString(R.string.notgoing));
				mEventAttendDeclineBtn.setBackgroundResource(R.drawable.shape_button_red_fill);
				mEventAttendDeclineBtn.setTextColor(getResources().getColor(R.color.white));
				mEventAttendJoinBtn.setText(getResources().getString(R.string.fragment_event_detail_button_join));
				mEventAttendJoinBtn.setBackgroundResource(R.drawable.shape_button_orange_fill);
				toolBarRefresh();
			}
		});
		
		mEventCoverIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(EventDetailActivity.this, ImageFullScreenActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.OBJECT, new String[]{mContentFilePath});
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.POSITION, 0);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(EventDetailActivity.this);
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
			FetchActionAsyncTask mFetchActionAsyncTask = new FetchActionAsyncTask(EventDetailActivity.this, mId, mCategory, TAG);
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
		try{
			if(mViewCount!=null){
				String[] mViewInvited = mViewCount.split(",");
				String mView = mViewInvited[0];
				String mInvited = mViewInvited[1];
				if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
					ContentValues mValues = new ContentValues();
					mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO, mView);
					getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
					
					ContentValues mValues1 = new ContentValues();
					mValues1.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, mInvited);
					getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues1, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
				}
				
				mEventViewTv.setText(mView);
				mEventAttendInvitedNumberTv.setText(mInvited);
			}
			
			if(mLikeCount!=null){
				String[] mLikeGoing = mLikeCount.split(",");
				String mLike = mLikeGoing[0];
				String mGoing = mLikeGoing[1];
				if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
					ContentValues mValues = new ContentValues();
					mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, mLike);
					getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
					
					ContentValues mValues1 = new ContentValues();
					mValues1.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, mGoing);
					getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues1, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});
				}
				mEventLikeTv.setText(mLike);
				mEventAttendGoingNumberTv.setText(mGoing);
			}
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
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
						mCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId}, DBConstant.Event_Columns.COLUMN_EVENT_ID + " DESC");
						getDataFromDBForEvent(mCursor);
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(EventDetailActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(EventDetailActivity.this);
		}
	}
	
	private void getDataFromDBForEvent(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING)));
			mContentIsGoing =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME));
			mContentEventDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE));
			mContentEventFromTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME));
			mContentEventToTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME));
			mContentEventVenue = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_VENUE));
			mContentEventInvited =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO));
			mContentEventGoing =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO));
			mContentEventMap =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_MAP));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ)));
			mContentFileLink =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK));
			mContentFilePath =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH));
			mContentFileSize =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE));
			isShareOptionEnable = mContentIsSharing;
			isContentLiked = mContentIsLike;
			setIntentDataToUi();
		}
	}
	
	private void setIntentDataToUi(){
		try{
			mEventTitleTv.setText(mContentTitle);
			mEventSummaryTextTv.setText(mContentDesc);
			if(!TextUtils.isEmpty(mContentLikeCount)){
				mEventLikeTv.setText(mContentLikeCount);	
			}
			
			if(!TextUtils.isEmpty(mContentViewCount)){
				mEventViewTv.setText(mContentViewCount);
			}
			
			mEventByTv.setText(Utilities.formatBy(mContentBy, mContentDate, mContentTime));
			
			if(!TextUtils.isEmpty(mContentEventDate)){
				setDataToUiAccCurrentDate();
			}
			
			if(!TextUtils.isEmpty(mContentEventVenue)){
				mEventLocationTextTv.setText(mContentEventVenue);
			}
			
			if(mContentIsLike){
				mEventLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
				mEventLikeTv.setText(mContentLikeCount);
				mEventLikeTv.setTextColor(getResources().getColor(R.color.red));
			}
			
			if(mContentIsGoing.equalsIgnoreCase("1")){
				mEventAttendJoinBtn.setText(getResources().getString(R.string.going));
				mEventAttendJoinBtn.setBackgroundResource(R.drawable.shape_button_green_fill);
			}else if(mContentIsGoing.equalsIgnoreCase("0")){
				mEventAttendDeclineBtn.setText(getResources().getString(R.string.notgoing));
				mEventAttendDeclineBtn.setBackgroundResource(R.drawable.shape_button_red_fill);
				mEventAttendDeclineBtn.setTextColor(getResources().getColor(R.color.white));
			}
			
			
			mEventAttendInvitedNumberTv.setText(mContentEventInvited);
			mEventAttendGoingNumberTv.setText(mContentEventGoing);
			
			if (checkIfFileExists(mContentFilePath)) {
//				mContentFilePath = Utilities.fbConcealDecryptFile(TAG,new File(mContentFilePath));
				mEventCoverIv.setImageURI(Uri.parse(mContentFilePath));
			} else {
				downloadFileInBackground();
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
	
	@SuppressLint({ "InlinedApi", "SimpleDateFormat" }) private void addEventToCalendar() {
		// TODO Auto-generated method stub
		Intent mCalIntent = new Intent(Intent.ACTION_EDIT);
		mCalIntent.setType("vnd.android.cursor.item/event");
		Date mFromTime = null;
		Date mToTime = null;
		try {
			mFromTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mContentEventDate + " " +mContentEventFromTime);
			mToTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mContentEventDate + " "+mContentEventToTime);
			mCalIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,mFromTime.getTime());
			mCalIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,mToTime.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mCalIntent.putExtra(Events.TITLE, mContentTitle);
		mCalIntent.putExtra(Events.DESCRIPTION, mContentDesc);
		mCalIntent.putExtra(Events.EVENT_LOCATION, mContentEventVenue);
		mCalIntent.putExtra(Events.ALL_DAY, false);
		mCalIntent.putExtra(Events.STATUS, 1);
		mCalIntent.putExtra(Events.VISIBLE, 0);
		mCalIntent.putExtra(Events.HAS_ALARM, 1);
		startActivity(mCalIntent);
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
	private void addEventToCalendarDirectly(){
		 try{
			 ContentValues values = new ContentValues();
			 Date mFromTime = null;
				Date mToTime = null;
				try {
					mFromTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mContentEventDate + " " +mContentEventFromTime);
					mToTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mContentEventDate + " "+mContentEventToTime);
					values.put(CalendarContract.Events.DTSTART, mFromTime.getTime());
					values.put(CalendarContract.Events.DTEND,mToTime.getTime());
					values.put(CalendarContract.Events.EVENT_LOCATION,mContentEventVenue);
					values.put(CalendarContract.Events.ALL_DAY, false);
					values.put(CalendarContract.Events.STATUS, 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     values.put(CalendarContract.Events.TITLE, mContentTitle);
		     values.put(CalendarContract.Events.DESCRIPTION, mContentDesc);
		     TimeZone timeZone = TimeZone.getDefault();
		     values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		        // default calendar
		     values.put(CalendarContract.Events.CALENDAR_ID, 1);
//		     values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;");
		        //for one hour
//		     values.put(CalendarContract.Events.DURATION, "+P1H");
		     
		     values.put(CalendarContract.Events.HAS_ALARM, 1);
		        // insert event to calendar
		     Uri mUri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
		     if(Utilities.checkWhetherInsertedOrNot(TAG, mUri)){
		    	 AndroidUtilities.showSnackBar(EventDetailActivity.this, getResources().getString(R.string.fragment_event_added));
		     }else{
		    	 AndroidUtilities.showSnackBar(EventDetailActivity.this, getResources().getString(R.string.fragment_event_add_error));
		     }
		 }catch(Exception e){
			 FileLog.e(TAG, e.toString());
		 }
	}
	
	
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) private void setDataToUiAccCurrentDate(){
		try{
			Date mDate = new SimpleDateFormat("yyyy-MM-dd").parse(mContentEventDate);
			mEventTimeTillDayTv.setText(String.valueOf(new SimpleDateFormat("EEEE").format(mDate).toUpperCase()));
			mEventTimeDateNumberTv.setText(String.valueOf(mDate.getDate()));
			mEventTimeDateTextTv.setText(String.valueOf(new SimpleDateFormat("MMMM").format(mDate)).toUpperCase());
			mEventTimeTillTv.setText(mContentEventFromTime + "-" +mContentEventToTime);
			String mDaysLeft = Utilities.formatDaysLeft(mContentEventDate, mContentEventFromTime);
			if(!TextUtils.isEmpty(mDaysLeft)){
				mEventTimeHoursNumberTv.setText(mDaysLeft.subSequence(0, 2));
				mEventCoverLeftNumberTv.setText(mDaysLeft.subSequence(0, 2));
				mEventCoverLeftTextTv.setText(mDaysLeft.subSequence(2, mDaysLeft.length()));
				mEventTImeHoursTextTv.setText(mDaysLeft.subSequence(2, mDaysLeft.length()));
			}
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void downloadFileInBackground() {
		try{
			DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
					EventDetailActivity.this, false, false, mContentFileLink,
					mContentFilePath, AppConstants.TYPE.IMAGE,
					Long.parseLong(mContentFileSize), TAG);
			mDownloadAsyncTask.execute();
			mDownloadAsyncTask
					.setOnPostExecuteListener(new OnPostExecuteListener() {
						@Override
						public void onPostExecute(boolean isDownloaded) {
							// TODO Auto-generated method stub
							if (isDownloaded) {
//								mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
								if(checkIfFileExists(mContentFilePath)){
									mEventCoverIv.setImageURI(Uri.parse(mContentFilePath));
								}
							}
						}
					});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateReadInDb(){
		ContentValues values = new ContentValues();
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
			values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, "true");
			getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, values, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId});	
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
		String mShareContent = mContentTitle + "\n\n" + mContentDesc + "\n\n\n on"
				+ mContentEventDate+" at "+ mContentEventFromTime + "\n\n" + mContentEventVenue + "\n\n"
				+ getResources().getString(R.string.share_advertisement); 
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				mShareContent).limit(R.integer.bs_initial_grid_row).build();
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mEventAddToCalIv);
			setMaterialRippleOnView(mEventShareIv);
			setMaterialRippleOnView(mEventAttendJoinBtn);
			setMaterialRippleOnView(mEventCoverIv);
			setMaterialRippleWithGrayOnView(mEventAttendDeclineBtn);
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
				final String STORAGE_CALENDAR[] = new String[]{AppConstants.PERMISSION.CALENDAR, AppConstants.PERMISSION.STORAGE};
				mPermissionHelper = PermissionHelper.getInstance(this);
				mPermissionHelper.setForceAccepting(false)
						.request(STORAGE_CALENDAR);
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
