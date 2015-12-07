/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.sqlite.DBConstant;
import com.application.ui.activity.AwardRecyclerActivity.AsyncSendMessageTask;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.LoadToast;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.DownloadAsyncTask;
import com.application.utils.DownloadAsyncTask.OnPostExecuteListener;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.permission.PermissionHelper;
import com.squareup.okhttp.OkHttpClient;

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

	private ImageView mAwardProfileActionCongIv;
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
	private boolean mContentIsCongratulated;
	private boolean mContentIsRead;
	
	private boolean isFromNotification = false;
	
	private PermissionHelper mPermissionHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_award_profile);
		setSecurity();
		initToolBar();
		initUi();
		checkPermissionModel();
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
		mAwardProfileActionCongIv = (ImageView) findViewById(R.id.fragmentAwardProfileCongratulateIv);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.AwardProfileActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.ZoomIn).duration(1000)
					.playOn(mAwardProfileCircleImageView);
			YoYo.with(Techniques.BounceInUp).delay(1000).duration(500)
					.playOn(mAwardProfileActionCongIv);
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
			mContentIsCongratulated = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE)));
			isShareOptionEnable = mContentIsSharing;
			setIntentDataToUi();
		}
	}
	
	
	@SuppressWarnings("deprecation")
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
			
			if(mContentIsCongratulated){
				mAwardProfileActionCongIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_award_cong_selected));
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
		mAwardProfileActionCongIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showAwardCongratulateDialog();
			}
		});
		
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
					/*Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+mContentMobile));
					smsIntent.putExtra("sms_body", "Congrats " + mContentTitle);
					startActivity(smsIntent);*/
					showAwardMessageDialog();
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
	
	
	private void showAwardCongratulateDialog() {
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(
				AwardProfileActivity.this)
				.title(getResources().getString(
						R.string.dialog_birthday_award_title)
						+ " ?")
				.titleColor(Utilities.getAppColor())
				.positiveText(
						getResources()
								.getString(
										R.string.sample_fragment_settings_dialog_language_positive))
				.positiveColor(Utilities.getAppColor())
				.negativeText(
						getResources()
								.getString(
										R.string.sample_fragment_settings_dialog_language_negative))
				.negativeColor(Utilities.getAppColor())
				.callback(new MaterialDialog.ButtonCallback() {
					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onPositive(MaterialDialog dialog) {
						dialog.dismiss();
						if (!mContentIsCongratulated) {
							UserReport.updateUserReportApi(
									mId,
									AppConstants.INTENTCONSTANTS.AWARD,
									AppConstants.REPORT.CONG, "");
							Utilities.showCrouton(
									AwardProfileActivity.this,
									mCroutonViewGroup,
									getResources().getString(
											R.string.congratulate_message)
											+ " "
											+ mContentTitle, Style.CONFIRM);
							ContentValues mValues = new ContentValues();
							mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE,"true");
							getContentResolver().update(DBConstant.Award_Columns.CONTENT_URI,mValues,DBConstant.Award_Columns.COLUMN_AWARD_ID+ "=?",new String[] { mId});
							mAwardProfileActionCongIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_award_cong_selected));
						}
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						dialog.dismiss();
					}
				}).show();
	}

	private void showAwardMessageDialog() {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(AwardProfileActivity.this)
				.title(getResources().getString(R.string.dialog_birthday_message_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_birthday_message, true)
				.cancelable(true).show();

		View mView = mMaterialDialog.getCustomView();
		final AppCompatEditText mMessageEd = (AppCompatEditText) mView
				.findViewById(R.id.dialogBirthdayMessageEd);
		AppCompatButton mSendBtn = (AppCompatButton) mView
				.findViewById(R.id.dialogBirthdayMessageSendBtn);
		final AppCompatTextView mMessageEdCounterTv = (AppCompatTextView) mView
				.findViewById(R.id.dialogBirthdayMessageTv);

		mMessageEd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				if (mCharsequence.length() > 140) {
					mMessageEdCounterTv.setTextColor(Color.RED);
				} else {
					mMessageEdCounterTv.setTextColor(Utilities.getAppColor());
				}
				mMessageEdCounterTv.setText(mCharsequence.length() + "/140");

			}

			@Override
			public void beforeTextChanged(CharSequence mCharsequence,
					int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable mEditable) {
				// TODO Auto-generated method stub
			}
		});

		try {
			setMaterialRippleWithGrayOnView(mSendBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		mSendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mMessageEd.getText().toString())){
					sendMessage(mMessageEd.getText().toString(), mId);
					mMaterialDialog.dismiss();
				}
			}
		});
	}
	/**
	 * AsyncTask : Sent Chat to Server
	 */

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void sendMessage(String mMessage, String mId) {
		if (!TextUtils.isEmpty(mMessage)) {
			if (Utilities.isInternetConnected()) {
				if (AndroidUtilities.isAboveIceCreamSandWich()) {
					new AsyncSendMessageTask(mMessage, mId).executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				} else {
					new AsyncSendMessageTask(mMessage, mId).execute();
				}
			} else {
				Utilities
						.showCrouton(
								AwardProfileActivity.this,
								mCroutonViewGroup,
								getResources().getString(
										R.string.internet_unavailable),
								Style.ALERT);
			}
		}
	}

	private String apiSendMessage(String mMessage, String mCategory, String mId) {
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostMessage(mMessage,
					mId, mCategory);
			if (BuildVars.USE_OKHTTP) {
				return RetroFitClient.postJSON(new OkHttpClient(),
						AppConstants.API.API_SUBMIT_AWARD_MESSAGE,
						jsonObj.toString(), TAG);
			} else {
				return RestClient.postJSON(AppConstants.API.API_SUBMIT_AWARD_MESSAGE,
						jsonObj, TAG);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		try {
			if (!TextUtils.isEmpty(mResponseFromApi)) {
//				Utilities.showCrouton(AwardRecyclerActivity.this,mCroutonViewGroup,Utilities.getSuccessMessageFromApi(mResponseFromApi),Style.CONFIRM);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	public class AsyncSendMessageTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private String mMessage;
		private String mId;
		private MobcastProgressDialog mProgressDialog;
		private LoadToast mLoadToast;

		public AsyncSendMessageTask(String mMessage, String mId) {
			this.mMessage = mMessage;
			this.mId = mId;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*mProgressDialog = new MobcastProgressDialog(
					AwardRecyclerActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication()
					.getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();*/
			mLoadToast = new LoadToast(AwardProfileActivity.this);
			mLoadToast.setText(ApplicationLoader.getApplication()
					.getResources().getString(R.string.loadingMessage));
			mLoadToast.setProgressColor(ApplicationLoader.getApplication().getResources().getColor(R.color.toolbar_background));
			mLoadToast.setTranslationY(170);
			mLoadToast.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = apiSendMessage(mMessage,
						AppConstants.INTENTCONSTANTS.AWARD, mId);
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				
				if(mLoadToast!=null){
					mLoadToast.error();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				if (isSuccess) {
					if(mLoadToast!=null){
						mLoadToast.success();
					}
					parseDataFromApi(mResponseFromApi);
				} else {
					mErrorMessage = Utilities.getErrorMessageFromApi(mResponseFromApi);
//					Utilities.showCrouton(AwardRecyclerActivity.this,mCroutonViewGroup, mErrorMessage, Style.ALERT);
					if(mLoadToast!=null){
						mLoadToast.error();
					}
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
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
