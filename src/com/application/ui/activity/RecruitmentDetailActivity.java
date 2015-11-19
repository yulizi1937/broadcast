/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import com.application.beans.Recruitment;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.MobcastProgressDialog;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
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
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.permission.PermissionHelper;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class RecruitmentDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = RecruitmentDetailActivity.class.getSimpleName();
	
	private static final int CONTACT_PICK = 10001;

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private AppCompatTextView mJobTitleTv;
	private AppCompatTextView mJobDesignationTv;
	private AppCompatTextView mJobLocationTv;
	private AppCompatTextView mJobExperienceTv;
	private AppCompatTextView mJobSkillsTv;
	private AppCompatTextView mJobDescriptionTv;
	private AppCompatTextView mJobQualificationTv;
	private AppCompatTextView mJobCTCTv;
	private AppCompatTextView mContactNameTv;
	private AppCompatTextView mContactDesignationTv;
	private AppCompatTextView mContactMobileTv;
	private AppCompatTextView mContactEmailTv;
	
	private ImageView mJobTitleIv;
	private ImageView mJobSkillsIv;
	private ImageView mJobDescriptionIv;
	private ImageView mJobQualificationIv;
	private ImageView mJobCTCIv;
	private ImageView mJobContactIv;
	
	private FrameLayout mJobTitleLayout;
	private FrameLayout mJobSkillsLayout;
	private FrameLayout mJobDescriptionLayout;
	private FrameLayout mJobQualificationLayout;
	private FrameLayout mJobCTCLayout;
	private FrameLayout mContactLayout;
	
	private LinearLayout mJobTitleDetailLayout;
	private LinearLayout mContactDetailLayout;
	
	private LinearLayout mContactEmailLayout;
	private LinearLayout mContactMobileLayout;
	
	private boolean isShareOptionEnable = true;
	
	private Intent mIntent;
	private String mId;
	private String mCategory = AppConstants.INTENTCONSTANTS.RECRUITMENT;
	private String mJobTitle;
	private String mJobDesignation;
	private String mJobLocation;
	private String mJobExperience;
	private String mJobSkills;
	private String mJobDescription;
	private String mJobCTC;
	private String mJobQualification;
	private String mJobReceivedDate;
	private String mJobReceivedTime;
	private String mContactName;
	private String mContactEmail;
	private String mContactMobile;
	private String mContactDesignation;
	private boolean isContactSharing = false;
	
	private Recruitment mRecruitment;
	
	private boolean isJobTitleExpanded = true;
	private boolean isJobSkillsExpanded = true;
	private boolean isJobDescExpanded = false;
	private boolean isJobQualificationExpanded = false;
	private boolean isJobCTCExpanded = false;
	private boolean isContactExpanded = true;
	
	private PermissionHelper mPermissionHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruitment_detail);
		setSecurity();
		initToolBar();
		initUi();
		getIntentData();
		setUiListener();
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
			
			if (isContactSharing) {
				menu.findItem(R.id.action_contact_share).setVisible(true);
			} else {
				menu.findItem(R.id.action_contact_share).setVisible(false);
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
		inflater.inflate(R.menu.menu_recruitment_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(RecruitmentDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_contact_share:
			shareContactFromPhoneBook();
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(RecruitmentDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Recruitment");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(RecruitmentDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mJobTitleLayout = (FrameLayout)findViewById(R.id.activityRecruitmentDetailExpandLayout);
		mJobSkillsLayout = (FrameLayout)findViewById(R.id.activityRecruitmentDetailSkillsExpandLayout);
		mJobDescriptionLayout = (FrameLayout)findViewById(R.id.activityRecruitmentDetailJobDescriptionExpandLayout);
		mJobQualificationLayout = (FrameLayout)findViewById(R.id.activityRecruitmentDetailQualificationExpandLayout);
		mJobCTCLayout = (FrameLayout)findViewById(R.id.activityRecruitmentDetailCTCExpandLayout);
		mContactLayout = (FrameLayout)findViewById(R.id.activityRecruitmentDetailContactExpandLayout);
		
		mJobTitleDetailLayout = (LinearLayout)findViewById(R.id.activityRecruitmentDetailLayout);
		mContactDetailLayout = (LinearLayout)findViewById(R.id.activityRecruitmentDetailContactLayout);
		mContactEmailLayout = (LinearLayout)findViewById(R.id.activityRecruitmentContactEmailLayout);
		mContactMobileLayout = (LinearLayout)findViewById(R.id.activityRecruitmentContactMobileLayout);
		
		mJobTitleTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailTitleTv);
		mJobDesignationTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailDesignationTv);
	    mJobLocationTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailLocationTv);
	    mJobExperienceTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailExperienceTv);
	    mJobSkillsTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailSkillsTv);
	    mJobDescriptionTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailJobDescriptionTv);
	    mJobQualificationTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailQualificationTv);
	    mJobCTCTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailCTCTv);
	    mContactNameTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailContactNameTv);
	    mContactDesignationTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailContactDesignationTv);
	    mContactMobileTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailContactMobileTv);
	    mContactEmailTv = (AppCompatTextView)findViewById(R.id.activityRecruitmentDetailContactEmailTv);
				
		mJobTitleIv = (ImageView)findViewById(R.id.activityRecruitmentDetailIv);
		mJobSkillsIv = (ImageView)findViewById(R.id.activityRecruitmentDetailSkillsIv);
		mJobDescriptionIv = (ImageView)findViewById(R.id.activityRecruitmentDetailJobDescriptionIv);
		mJobQualificationIv = (ImageView)findViewById(R.id.activityRecruitmentDetailQualificationIv);
		mJobCTCIv = (ImageView)findViewById(R.id.activityRecruitmentDetailCTCIv);
		mJobContactIv = (ImageView)findViewById(R.id.activityRecruitmentDetailContactIv);
		
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		try{
			if(mIntent!=null){
				int mPosition = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.POSITION, 0);
				ArrayList<Recruitment> mList = mIntent.getParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT);
				mRecruitment = mList.get(mPosition);
				if(mRecruitment!=null){
						getDataFromIntentObject();
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(RecruitmentDetailActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(RecruitmentDetailActivity.this);
		}
	}
	
	private void getDataFromIntentObject(){
		try{
			mId = mRecruitment.getmId();
			mJobTitle = mRecruitment.getmJobTitle();
			mJobDesignation = mRecruitment.getmJobDesig();
			mJobLocation = mRecruitment.getmJobLoc();
			mJobExperience = mRecruitment.getmJobExp();
			mJobSkills = mRecruitment.getmJobSkills();
			mJobDescription =  mRecruitment.getmJobDesc();
			mJobQualification = mRecruitment.getmJobQualif();
			mJobCTC = mRecruitment.getmJobCTC();
			mContactName = mRecruitment.getmContactName();
			mContactDesignation = mRecruitment.getmContactDesig();
			mContactEmail = mRecruitment.getmContactEmail();
			mContactMobile = mRecruitment.getmContactMobile();
			isShareOptionEnable = mRecruitment.isSocialSharing();
			isContactSharing = mRecruitment.isContactSharing();
			setIntentDataToUi();			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setIntentDataToUi(){
		try{
			mJobTitleTv.setText(mJobTitle);
			mJobDesignationTv.setText(mJobDesignation);
			if(!TextUtils.isEmpty(mJobDescription)){
				mJobDescriptionTv.setText(mJobDescription);	
			}
			if(!TextUtils.isEmpty(mJobLocation)){
				mJobLocationTv.setText(mJobLocation);
			}
			if(!TextUtils.isEmpty(mJobExperience)){
				mJobExperienceTv.setText(mJobExperience);
			}
			if(!TextUtils.isEmpty(mJobSkills)){
				mJobSkillsTv.setText(mJobSkills);
			}
			if(!TextUtils.isEmpty(mJobQualification)){
				mJobQualificationTv.setText(mJobQualification);
			}
			if(!TextUtils.isEmpty(mJobCTC)){
				mJobCTCTv.setText(mJobCTC);
			}
			if(!TextUtils.isEmpty(mContactName)){
				mContactNameTv.setText(mContactName);
			}
			if(!TextUtils.isEmpty(mContactDesignation)){
				mContactDesignationTv.setText(mContactDesignation);
			}
			if(!TextUtils.isEmpty(mContactMobile)){
				mContactMobileTv.setText(mContactMobile);
			}
			if(!TextUtils.isEmpty(mContactEmail)){
				mContactEmailTv.setText(mContactEmail);
			}
			supportInvalidateOptionsMenu();
			mRecruitment.setRead(true);
			UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
				R.string.RecruitmentRecyclerActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setToolBarOption();
		setClickListener();
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(RecruitmentDetailActivity.this).applyThemeWithBy(RecruitmentDetailActivity.this, RecruitmentDetailActivity.this, mToolBar,mJobDesignationTv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setClickListener(){
		mJobTitleLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobTitleExpanded){
					AndroidUtilities.expand(mJobTitleDetailLayout);
					mJobTitleIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobTitleDetailLayout);
					mJobTitleIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobTitleExpanded = !isJobTitleExpanded;
			}
		});
		
		mJobSkillsLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobSkillsExpanded){
					AndroidUtilities.expand(mJobSkillsTv);				
					mJobSkillsIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobSkillsTv);
					mJobSkillsIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobSkillsExpanded = !isJobSkillsExpanded;
			}
		});
		
		mJobDescriptionLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobDescExpanded){
					AndroidUtilities.expand(mJobDescriptionTv);
					mJobDescriptionIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobDescriptionTv);
					mJobDescriptionIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobDescExpanded = !isJobDescExpanded;
			}
		});
		
		mJobQualificationLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobQualificationExpanded){
					AndroidUtilities.expand(mJobQualificationTv);
					mJobQualificationIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobQualificationTv);
					mJobQualificationIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobQualificationExpanded = !isJobQualificationExpanded;
			}
		});
		
		mJobCTCLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobCTCExpanded){
					AndroidUtilities.expand(mJobCTCTv);
					mJobCTCIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobCTCTv);
					mJobCTCIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobCTCExpanded = !isJobCTCExpanded;
			}
		});
		
		mContactLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isContactExpanded){
					AndroidUtilities.expand(mContactDetailLayout);
					mJobContactIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mContactDetailLayout);
					mJobContactIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isContactExpanded = !isContactExpanded;
			}
		});
		
		
		mContactEmailLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mContactEmail)){
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mContactEmail, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reference to Job Id " +mId);
				emailIntent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(emailIntent, "Send email"));
				}
			}
		});
		
		mContactMobileLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mContactMobile)){
					Intent mIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mContactMobile, null));
					startActivity(mIntent);
				}
			}
		});
		
	}

	private void shareContactFromPhoneBook(){
		try{
			if(AndroidUtilities.isAboveMarshMallow()){
				if(mPermissionHelper.isPermissionDeclined(AppConstants.PERMISSION.CONTACTS)){
					startActivityForResult(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI), CONTACT_PICK);
				}else{
					checkPermissionModel();
				}
			}else{
				startActivityForResult(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI), CONTACT_PICK);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// log.v("OnactivityResult", "reached");
		mPermissionHelper.onActivityForResult(requestCode);
		if (requestCode == CONTACT_PICK) {
			if (resultCode == RESULT_OK) {
				String mPhoneNumber = "";
				String mEmailAddress = "";
				String mName = "";
				String mStringEmailCompose = "";
				String id = "";
				Uri uri = Uri.parse(data.getDataString());
				Cursor mCursor = managedQuery(uri, null, null, null, null);
				if (mCursor.moveToFirst()) {
					mName = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
					id = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Data._ID));
					mStringEmailCompose = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));
					if (Integer.parseInt(mStringEmailCompose) == 1) {
						Cursor mCursorPhone = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ "=" + id, null, null);
						if (mCursorPhone.moveToFirst()){
							mPhoneNumber = mCursorPhone.getString(mCursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						}
						if(mCursorPhone!=null){
							mCursorPhone.close();
						}
					}
					Cursor mCursorEmail = managedQuery(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ "=" + id, null, null);
					if (mCursorEmail.moveToFirst()) {
						mEmailAddress = mCursorEmail.getString(mCursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
					}
					
					if(mCursorEmail!=null){
						mCursorEmail.close();
					}
				}
				mStringEmailCompose = "Name: " + mName;

				if (!TextUtils.isEmpty(mPhoneNumber)) {
					mStringEmailCompose += "\nContact Number: " + mPhoneNumber;
				}
				if (!TextUtils.isEmpty(mEmailAddress)) {
					mStringEmailCompose += "\nEmail: " + mEmailAddress;
				}
				
				submitContactDetailsToApi(mName, mPhoneNumber, mEmailAddress, mStringEmailCompose);
			}
		}else{
			if(AndroidUtilities.isAboveMarshMallow()){
        		mPermissionHelper.onActivityForResult(requestCode);
        	}
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void submitContactDetailsToApi(String mReferenceName, String mReferencePhoneNumber, String mReferenceEmailAddress, String mStringEmailCompose){
		if (Utilities.isInternetConnected()) {
				if (AndroidUtilities.isAboveIceCreamSandWich()) {
					new AsyncContactTask(mReferenceName, mReferencePhoneNumber, mReferenceEmailAddress, mStringEmailCompose).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				} else {
					new AsyncContactTask(mReferenceName, mReferencePhoneNumber, mReferenceEmailAddress, mStringEmailCompose).execute();
				}
		} else {
			Utilities.showCrouton(
					RecruitmentDetailActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
		}
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		String mShareContent = mJobTitle + "\n\n" + "Description\n\n"
				+ mJobDescription + "\nSkills\n\n" + mJobSkills
				+ "\nQualification\n\n" + mJobQualification + "\nCTC\n\n" + mJobCTC
				+ "\nLocation\n\n" + mJobLocation + "\nExperience\n\n"
				+ mJobExperience + "\nContact\n\n" + "\n" + mContactName + "\n"
				+ mContactEmail + "\n" + mContactMobile + "\n\n\n"
				+ getResources().getString(R.string.share_advertisement); 
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				mShareContent).limit(R.integer.bs_initial_grid_row).build();
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleWithGrayOnView(mJobTitleLayout);
			setMaterialRippleWithGrayOnView(mJobSkillsLayout);
			setMaterialRippleWithGrayOnView(mJobDescriptionLayout);
			setMaterialRippleWithGrayOnView(mJobQualificationLayout);
			setMaterialRippleWithGrayOnView(mJobCTCLayout);
			setMaterialRippleWithGrayOnView(mContactLayout);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	/**
	 * Async : Contact Share
	 */
	
	private String apiSubmitContactDetails(String mReferenceName, String mReferencePhoneNumber, String mReferenceEmailAddress) {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostRecruitmentContactShareDate(mId, mReferenceName, mReferencePhoneNumber, mReferenceEmailAddress);
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SUBMIT_REF, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_SUBMIT_REF, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi, String mStringEmailCompose) {
		try{
			if(!TextUtils.isEmpty(mResponseFromApi)){
				Utilities.showCrouton(RecruitmentDetailActivity.this, mCroutonViewGroup, Utilities.getSuccessMessageFromApi(mResponseFromApi), Style.CONFIRM);
				Intent mIntentEmail = new Intent(Intent.ACTION_SEND);
				mIntentEmail.setType("message/rfc822");
				mIntentEmail.putExtra(Intent.EXTRA_SUBJECT, "Reference to the recruitment Job ID " + mId);
				mIntentEmail.putExtra(Intent.EXTRA_TEXT, mStringEmailCompose);
				mIntentEmail.putExtra(Intent.EXTRA_EMAIL,  new String[]{mContactEmail});
				try {
					startActivity(Intent.createChooser(mIntentEmail, "Send mail"));
				} catch (android.content.ActivityNotFoundException ex) {
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	

	public class AsyncContactTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private String mReferenceName;
		private String mReferenceEmailAddress;
		private String mReferencePhoneNumber;
		private String mStringEmailCompose;
		private MobcastProgressDialog mProgressDialog;

		public AsyncContactTask(String mReferenceName, String mReferencePhoneNumber, String mReferenceEmailAddress, String mStringEmailCompose){
			this.mReferenceName = mReferenceName;
			this.mReferenceEmailAddress = mReferenceEmailAddress;
			this.mReferencePhoneNumber = mReferencePhoneNumber;
			this.mStringEmailCompose = mStringEmailCompose;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(RecruitmentDetailActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiSubmitContactDetails(mReferenceName, mReferencePhoneNumber, mReferenceEmailAddress);
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi,mStringEmailCompose);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(RecruitmentDetailActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
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
						.request(AppConstants.PERMISSION.CONTACTS);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
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
		if(mPermissionHelper.isPermissionDeclined(AppConstants.PERMISSION.CONTACTS)){
			startActivityForResult(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI), CONTACT_PICK);
		}
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
