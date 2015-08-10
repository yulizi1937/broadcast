/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.FileLog;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.mobcast.R;

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
	
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsGoing;
	private boolean mContentIsRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		initToolBar();
		initUi();
		getIntentData();
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
		// TODO Auto-generated method stub
		super.onPause();
		deleteDecryptedFile();
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
			AndroidUtilities.exitWindowAnimation(EventDetailActivity.this);
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

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
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
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		try{
			if(mIntent!=null){
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).toString();
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
			
			mEventAttendInvitedNumberTv.setText(mContentEventInvited);
			mEventAttendGoingNumberTv.setText(mContentEventGoing);
			
			if (checkIfFileExists(mContentFilePath)) {
				mContentFilePath = Utilities.fbConcealDecryptFile(TAG,new File(mContentFilePath));
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
					EventDetailActivity.this, false, true, mContentFileLink,
					mContentFilePath, AppConstants.TYPE.IMAGE,
					Long.parseLong(mContentFileSize), TAG);
			mDownloadAsyncTask.execute();
			mDownloadAsyncTask
					.setOnPostExecuteListener(new OnPostExecuteListener() {
						@Override
						public void onPostExecute(boolean isDownloaded) {
							// TODO Auto-generated method stub
							if (isDownloaded) {
								mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
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
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				"Hello ").limit(R.integer.bs_initial_grid_row).build();
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
	
}
