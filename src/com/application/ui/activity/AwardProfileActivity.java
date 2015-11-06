/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
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
import com.application.ui.view.CircleImageView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.application.utils.FileLog;
import com.application.utils.Style;
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
public class AwardProfileActivity extends SwipeBackBaseActivity {
	private static final String TAG = AwardProfileActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;

	private FrameLayout mAwardProfileFrameLayout;
	
	private LinearLayout mAwardProfileLinearLayout;

	private CircleImageView mAwardProfileCircleImageView;

	private ImageView mAwardProfileActionCallIv;
	private ImageView mAwardProfileActionChatIv;
	private ImageView mAwardProfileActionMsgIv;
	private ImageView mAwardProfileActionEmailIv;

	private AppCompatTextView mAwardProfileDescTv;
	private AppCompatTextView mAwardProfileNameTv;
	private AppCompatTextView mAwardProfileDateTv;
	private AppCompatTextView mAwardProfileDepTv;
	private AppCompatTextView mAwardProfileCityTv;
	
	private boolean isShareOptionEnable = true;
	
	private Intent mIntent;
	private String mId;
	private String mCategory;
	private String mContentTitle;
	private String mContentDesc;
	private String mContentLikeCount;
	private String mContentViewCount;
	private String mContentCongratulateCount;
	private String mContentFileLink;
	private String mContentFilePath;
	private String mContentFileSize;
	private String mContentReceivedDate;
	private String mContentReceivedTime;
	private String mContentCity;
	private String mContentDepartment;
	private String mContentEmail;
	private String mContentMobile;
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsRead;
	
	private boolean isFromNotification = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_award_profile);
		setSecurity();
		initToolBar();
		initUi();
		getIntentData();
		setUiListener();
		setAnimation();
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
		inflater.inflate(R.menu.menu_profile, menu);
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
			AndroidUtilities.exitWindowAnimation(AwardProfileActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(AwardProfileActivity.this, MotherActivity.class);
				startActivity(mIntent);
			}
			return true;
		case R.id.action_share:
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(AwardProfileActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Profile");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(AwardProfileActivity.this);
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
			Intent mIntent = new Intent(AwardProfileActivity.this, MotherActivity.class);
			startActivity(mIntent);
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

		mAwardProfileFrameLayout = (FrameLayout) findViewById(R.id.fragmentAwardProfileRootLayout);
		
		mAwardProfileLinearLayout = (LinearLayout) findViewById(R.id.fragmentAwardProfileLayout);

		mAwardProfileCircleImageView = (CircleImageView) findViewById(R.id.fragmentAwardProfileImageIv);

		mAwardProfileNameTv = (AppCompatTextView) findViewById(R.id.fragmentAwardProfileNameTv);
		mAwardProfileDateTv = (AppCompatTextView) findViewById(R.id.fragmentAwardProfileDateTv);
		mAwardProfileDepTv = (AppCompatTextView) findViewById(R.id.fragmentAwardProfileDepartmentTv);
		mAwardProfileCityTv = (AppCompatTextView)findViewById(R.id.fragmentAwardProfileCityTv);
		
		mAwardProfileDescTv = (AppCompatTextView)findViewById(R.id.fragmentAwardProfileDescriptionTv);

		mAwardProfileActionCallIv = (ImageView) findViewById(R.id.fragmentAwardProfileCallIv);
		mAwardProfileActionChatIv = (ImageView) findViewById(R.id.fragmentAwardProfileChatIv);
		mAwardProfileActionMsgIv = (ImageView) findViewById(R.id.fragmentAwardProfileMessageIv);
		mAwardProfileActionEmailIv = (ImageView) findViewById(R.id.fragmentAwardProfileEmailIv);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.BirthdayProfileActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.ZoomIn).duration(1000)
					.playOn(mAwardProfileCircleImageView);
			YoYo.with(Techniques.BounceInUp).delay(1000).duration(500)
					.playOn(mAwardProfileActionChatIv);
			YoYo.with(Techniques.BounceInUp).delay(1500).duration(500)
					.playOn(mAwardProfileActionCallIv);
			YoYo.with(Techniques.BounceInUp).delay(2000).duration(500)
					.playOn(mAwardProfileActionMsgIv);
			YoYo.with(Techniques.BounceInUp).delay(2500).duration(500)
					.playOn(mAwardProfileActionEmailIv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	public void applyTheme(){
		try{
			ThemeUtils.getInstance(AwardProfileActivity.this).applyThemeAwardProfile(AwardProfileActivity.this, AwardProfileActivity.this, mToolBar, mAwardProfileLinearLayout, mAwardProfileNameTv, mAwardProfileCityTv, mAwardProfileDepTv);
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
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
						mCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, null, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mId}, DBConstant.Award_Columns.COLUMN_AWARD_ID + " DESC");
						getDataFromDBForAward(mCursor);
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(AwardProfileActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(AwardProfileActivity.this);
		}
	}
	
	
	private void getDataFromDBForAward(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING)));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO));
			mContentCongratulateCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO));
			mContentFileLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK));
			mContentFilePath = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH));
			mContentFileSize = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE));
			mContentCity = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CITY));
			mContentDepartment = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT));
			mContentMobile = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE));
			mContentEmail = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL));
			mContentReceivedDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE));
			
			isShareOptionEnable = mContentIsSharing;
			setIntentDataToUi();
		}
	}
	
	
	private void setIntentDataToUi(){
		try{
			mAwardProfileNameTv.setText(mContentTitle);
			
			if(!TextUtils.isEmpty(mContentDepartment)){
				mAwardProfileDepTv.setText(mContentDepartment);
			}
						
			if(!TextUtils.isEmpty(mContentReceivedDate)){
				setDateToUi();
			}
			
			if(!TextUtils.isEmpty(mContentDesc)){
				mAwardProfileDescTv.setText(mContentDesc);
			}
			
			mAwardProfileCityTv.setText(mContentCity);
			
			if(checkIfFileExists(mContentFilePath)){
//				mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
				if(!TextUtils.isEmpty(mContentFilePath)){
					mAwardProfileCircleImageView.setImageURI(Uri.parse(mContentFilePath));
				}
			}else{
				downloadFileInBackground();
			}
			
			mAwardProfileActionChatIv.setVisibility(View.GONE);
			
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
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
			values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, "true");
			getContentResolver().update(DBConstant.Award_Columns.CONTENT_URI, values, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mId});	
		}
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) private void setDateToUi(){
		try{
			Date mDate = new SimpleDateFormat("yyyy-MM-dd").parse(mContentReceivedDate);
			mAwardProfileDateTv.setText(String.valueOf(new SimpleDateFormat("MMM").format(mDate)).toUpperCase() +" "+  mDate.getDate() + " " +(mDate.getYear() + 1900));
		}catch(Exception e){
			
		}
	}
	
	private void downloadFileInBackground(){
		try{
			if(Utilities.isInternetConnected()){
				DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(
						AwardProfileActivity.this, false, false,
						mContentFileLink, mContentFilePath,
						AppConstants.TYPE.IMAGE, Long.parseLong(mContentFileSize), TAG);
				mDownloadAsyncTask.execute();
				mDownloadAsyncTask.setOnPostExecuteListener(new OnPostExecuteListener() {
					@Override
					public void onPostExecute(boolean isDownloaded) {
						// TODO Auto-generated method stub
						if(isDownloaded){
//							mContentFilePath = Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath));
							if(!TextUtils.isEmpty(mContentFilePath)){
								mAwardProfileCircleImageView.setImageURI(Uri.parse(mContentFilePath));		
							}	
						}
					}
				});
			}else{
				Utilities.showCrouton(AwardProfileActivity.this, mCroutonViewGroup, getResources().getString(R.string.internet_unavailable), Style.ALERT);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}

	private void setOnClickListener() {
		mAwardProfileActionCallIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mContentMobile)){
					Intent mIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mContentMobile, null));
					startActivity(mIntent);
				}
			}
		});
		
		mAwardProfileActionEmailIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mContentEmail)){
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mContentEmail, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Congrats " +mContentTitle);
				emailIntent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(emailIntent, "Send email"));
				}
			}
		});
		
		mAwardProfileActionMsgIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mContentMobile)){
					Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+mContentMobile));
					smsIntent.putExtra("sms_body", "Congrats " + mContentTitle);
					startActivity(smsIntent);
				}
			}
		});
		
		mAwardProfileCircleImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(AwardProfileActivity.this, ImageFullScreenActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.OBJECT, new String[]{mContentFilePath});
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.POSITION, 0);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(AwardProfileActivity.this);	
			}
		});
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
//			setMaterialRippleOnView(mAwardProfileFrameLayout);
			setMaterialRippleOnView(mAwardProfileCircleImageView);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
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
