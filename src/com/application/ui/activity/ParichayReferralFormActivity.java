/**
 * 
 */
package com.application.ui.activity;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.sqlite.DBConstant;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.DownloadProgressDialog;
import com.application.ui.view.NumberProgressBar;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.ProgressMutliPartEntity;
import com.application.utils.ProgressMutliPartEntity.ProgressListener;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.permission.PermissionHelper;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
@SuppressWarnings("deprecation")
public class ParichayReferralFormActivity extends SwipeBackBaseActivity{
	private static final String TAG = ParichayReferralFormActivity.class.getSimpleName();
	
	private static final int INTENT_PICK_FILE_SYSTEM = 1001;
	private static final int INTENT_PICK_FILE_MOBCAST = 1002;
	
	private Toolbar mToolBar;
	
	private AppCompatTextView mReferredForTv;
	
	private TextView mToolBarTitleTv;
	
	private AppCompatEditText mNameEv;
	private AppCompatEditText mPhoneEv;
	private AppCompatEditText mQualificationEv;
	private AppCompatEditText mEmailEv;
	private AppCompatEditText mOrganizationEv;
	private AppCompatEditText mDesignationEv;
	private AppCompatEditText mExperienceEv;
	private AppCompatEditText mRelationEv;
	private AppCompatEditText mFileEv;
	private AppCompatEditText mAgeEv;
	private AppCompatEditText mDivisonEv;
	private AppCompatEditText mHeadquarterEv;
	private AppCompatEditText mRegionEv;
	
	private AppCompatButton mSubmitBtn;
	private AppCompatButton mBrowseBtn;

	private ImageView mNameValidateIv;
	private ImageView mPhoneValidateIv;
	private ImageView mQualificationValidateIv;
	private ImageView mEmailValidateIv;
	private ImageView mOrganizationValidateIv;
	private ImageView mDesignationValidateIv;
	private ImageView mExperienceValidateIv;
	private ImageView mRelationValidateIv;
	private ImageView mFileValidateIv;
	private ImageView mAgeValidateIv;
	private ImageView mDivisonValidateIv;
	private ImageView mHeadquarterValidateIv;
	private ImageView mRegionValidateIv;
	
	private LinearLayout mNameLayout;
	private LinearLayout mPhoneLayout;
	private LinearLayout mQualificationLayout;
	private LinearLayout mEmailLayout;
	private LinearLayout mOrganizationLayout;
	private LinearLayout mDesignationLayout;
	private LinearLayout mExperienceLayout;
	private LinearLayout mRelationLayout;
	private LinearLayout mFileLayout;
	private LinearLayout mAgeLayout;
	private LinearLayout mDivisonLayout;
	private LinearLayout mHeadquarterLayout;
	private LinearLayout mRegionLayout;
	private LinearLayout mIRFLayout;
	
	private AppCompatCheckBox mAppCompatCheckBoxMedicalTrainee;
	private AppCompatCheckBox mAppCompatCheckBoxMedicalExecutive;
	private AppCompatCheckBox mAppCompatCheckBoxMedicalRepresentative;
	
	
	private FrameLayout mCroutonViewGroup;
	private FrameLayout mCandidateDetailsFrameLayout;
	
	private String mPicturePath;
	
	private boolean isValidName = false;
	private boolean isValidMobile = false;
	private boolean isValidEmail = false;
	private boolean isValidQualification = false;
	private boolean isValidAge = false;
	private boolean isValidExperience = false;
	private boolean isValidOrg = false;
	private boolean isValidDes = false;
	private boolean isValidRel = false;
	private boolean isValidEmployeeId = false;
	private boolean isValid = false;
	
	private boolean isValidFile = false;
	private boolean isReferralSucess = false;
	
	private String mId;
	private String mReferredFor;
	private String mJobUnit;
	private String mJobReferredFor;
	private String mJobHQ;
	private String mJobDivision;
	private String mJobRegion;
	private String mInstallment = "2";
	
	private Intent mIntent;
	
	private Context mContext;
	
	private String mFilePath;
	private int whichTheme = 0;
	
	private PermissionHelper mPermissionHelper;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parichay_referral_form);
		setSecurity();
		initUi();
		initToolBar();
		checkPermissionModel();
		getIntentData();
		applyTheme();
		setUiListener();
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        try{
        	if(AndroidUtilities.isAppLanguageIsEnglish()){
        		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        	}else{
        		super.attachBaseContext(newBase);
        	}
        }catch(Exception e){
        	FileLog.e(TAG, e.toString());
        }
    }
	
	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
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

	@SuppressLint("InlinedApi") @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			if(isReferralSucess){
				Intent mIntent = new Intent(ParichayReferralFormActivity.this, ParichayActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(ParichayReferralFormActivity.this);
				finishParentActivity();	
			}else{
				finish();
				AndroidUtilities.exitWindowAnimation(ParichayReferralFormActivity.this);	
			}
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(ParichayReferralFormActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Profile");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ParichayReferralFormActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@SuppressLint("InlinedApi") @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(isReferralSucess){
			Intent mIntent = new Intent(ParichayReferralFormActivity.this, ParichayActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ParichayReferralFormActivity.this);
			finishParentActivity();
		}else{
			finish();
			AndroidUtilities.exitWindowAnimation(ParichayReferralFormActivity.this);
		}
	}
	
	public void finishParentActivity(){
		try{
			finish();
			AndroidUtilities.exitWindowAnimation(ParichayReferralFormActivity.this);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void initUi(){
		mReferredForTv = (AppCompatTextView)findViewById(R.id.layoutReferralFormCandidatesReferredTv);
		mCandidateDetailsFrameLayout = (FrameLayout)findViewById(R.id.layoutReferralFormCandidateLayout);
		
		mNameEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormNameEv);
		mPhoneEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormMobileEv);
		mQualificationEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormQualificationEv);
		mEmailEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormEmailEv);
		mOrganizationEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormOrganizationEv);
		mDesignationEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormDesignationEv);
		mExperienceEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormExperienceEv);
		mRelationEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormRelationEv);
		mFileEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormBrowseEv);
		mAgeEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormAgeEv);
		mDivisonEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormDivisonEv);
		mHeadquarterEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormHeadQuarterEv);
		mRegionEv = (AppCompatEditText)findViewById(R.id.layoutReferralFormRegionEv);
		
		mSubmitBtn = (AppCompatButton)findViewById(R.id.layoutReferralFormSubmitBtn);
		mBrowseBtn = (AppCompatButton)findViewById(R.id.layoutReferralFormBrowseBtn);
		
		mNameValidateIv = (ImageView)findViewById(R.id.layoutReferralFormNameValidateIv);
		mPhoneValidateIv = (ImageView)findViewById(R.id.layoutReferralFormMobileValidateIv);
		mQualificationValidateIv = (ImageView)findViewById(R.id.layoutReferralFormQualificationValidateIv);
		mEmailValidateIv = (ImageView)findViewById(R.id.layoutReferralFormEmailValidateIv);
		mOrganizationValidateIv = (ImageView)findViewById(R.id.layoutReferralFormOrganizationValidateIv);
		mDesignationValidateIv = (ImageView)findViewById(R.id.layoutReferralFormDesignationValidateIv);
		mExperienceValidateIv = (ImageView)findViewById(R.id.layoutReferralFormExperienceValidateIv);
		mRelationValidateIv = (ImageView)findViewById(R.id.layoutReferralFormRelationValidateIv);
		mFileValidateIv = (ImageView)findViewById(R.id.layoutReferralFormBrowseValidateIv);
		mAgeValidateIv = (ImageView)findViewById(R.id.layoutReferralFormAgeValidateIv);
		mDivisonValidateIv = (ImageView)findViewById(R.id.layoutReferralFormDivisonValidateIv);
		mHeadquarterValidateIv = (ImageView)findViewById(R.id.layoutReferralFormHeadQuarterValidateIv);
		mRegionValidateIv = (ImageView)findViewById(R.id.layoutReferralFormRegionValidateIv);
		
		mNameLayout = (LinearLayout)findViewById(R.id.layoutReferralFormNameLayout);
		mPhoneLayout = (LinearLayout)findViewById(R.id.layoutReferralFormMobileLayout);
		mQualificationLayout = (LinearLayout)findViewById(R.id.layoutReferralFormQualificationLayout);
		mEmailLayout = (LinearLayout)findViewById(R.id.layoutReferralFormEmailLayout);
		mOrganizationLayout = (LinearLayout)findViewById(R.id.layoutReferralFormOrganizationLayout);
		mDesignationLayout = (LinearLayout)findViewById(R.id.layoutReferralFormDesignationLayout);
		mExperienceLayout = (LinearLayout)findViewById(R.id.layoutReferralFormExperienceLayout);
		mRelationLayout = (LinearLayout)findViewById(R.id.layoutReferralFormRelationLayout);
		mFileLayout = (LinearLayout)findViewById(R.id.layoutReferralFormBrowseLayout);
		mAgeLayout = (LinearLayout)findViewById(R.id.layoutReferralFormAgeLayout);
		mDivisonLayout = (LinearLayout)findViewById(R.id.layoutReferralFormDivisonLayout);
		mHeadquarterLayout = (LinearLayout)findViewById(R.id.layoutReferralFormHeadQuarterLayout);
		mRegionLayout = (LinearLayout)findViewById(R.id.layoutReferralFormRegionLayout);
		mIRFLayout = (LinearLayout)findViewById(R.id.layoutReferralFormIRFLayout);
		
		mAppCompatCheckBoxMedicalTrainee = (AppCompatCheckBox)findViewById(R.id.layoutReferralFormCheckBox1);
		mAppCompatCheckBoxMedicalExecutive = (AppCompatCheckBox)findViewById(R.id.layoutReferralFormCheckBox2);
		mAppCompatCheckBoxMedicalRepresentative = (AppCompatCheckBox)findViewById(R.id.layoutReferralFormCheckBox3);
		
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
		
		mContext = ParichayReferralFormActivity.this;
	}
	
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.ParichayReferralFormActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void getIntentData(){
		try{
			mIntent = getIntent();
			mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
			mReferredFor = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE);
			mJobUnit = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
			mJobHQ = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.HQ);
			mJobRegion = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.REGION);
			mJobDivision = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.DIVISION);
			mInstallment = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.INSTALLMENT);
			mReferredForTv.setText(mReferredFor);
			mJobReferredFor = mReferredFor;
			
			mJobUnit = mJobUnit.substring(mJobUnit.lastIndexOf(":")+2, mJobUnit.length()).trim();
			if(mJobUnit.equalsIgnoreCase("irf")){
//				mIRFLayout.setVisibility(View.VISIBLE);
				mDivisonLayout.setVisibility(View.VISIBLE);
				mRegionLayout.setVisibility(View.VISIBLE);
				mHeadquarterLayout.setVisibility(View.VISIBLE);
				if(!TextUtils.isEmpty(mJobRegion) && !mJobRegion.equalsIgnoreCase("-1")){
					mRegionEv.setText(mJobRegion);
				}
				if(!TextUtils.isEmpty(mJobHQ) && !mJobHQ.equalsIgnoreCase("-1")){
					mHeadquarterEv.setText(mJobHQ);
				}
				if(!TextUtils.isEmpty(mJobDivision) && !mJobDivision.equalsIgnoreCase("-1")){
					mDivisonEv.setText(mJobDivision);
				}
			}else{
				mIRFLayout.setVisibility(View.GONE);
				mDivisonLayout.setVisibility(View.GONE);
				mRegionLayout.setVisibility(View.GONE);
				mHeadquarterLayout.setVisibility(View.GONE);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setUiListener(){
		setFocusListener();
		setTextWatcher();
		setMaterialRippleView();
		setClickListener();
	}
	
	private void applyTheme() {
		try {
			whichTheme = ApplicationLoader.getPreferences().getAppTheme();
			ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeCountrySelect(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mToolBar);
			ThemeUtils.applyThemeFrameLayout(whichTheme, mCandidateDetailsFrameLayout);
			ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeButton(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mBrowseBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void setClickListener(){
		mSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Utilities.isInternetConnected()) {
					if(isValid){
						checkJobReferredFor();
						if (AndroidUtilities.isAboveIceCreamSandWich()) {
							new AsyncUpdateReferralTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
						} else {
							new AsyncUpdateReferralTask().execute();
						}
					}else{
						showValidationPopUp();
					}
				} else {
					Utilities.showCrouton(ParichayReferralFormActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
				}
			}
		});
		
		mBrowseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFileManagerDialog();
			}
		});
	}
	
	private void checkJobReferredFor(){
		try{
			if(mJobUnit.equalsIgnoreCase("irf")){
				ArrayList<String> mList = new ArrayList<String>();
				if(mAppCompatCheckBoxMedicalTrainee.isChecked()){
					mList.add("Medical Trainee");
				}
				if(mAppCompatCheckBoxMedicalExecutive.isChecked()){
					mList.add("Medial Executive");
				}
				
				if(mAppCompatCheckBoxMedicalRepresentative.isChecked()){
					mList.add("Medial Representative");
				}
				if(mList!=null && mList.size() > 0){
					mJobReferredFor = StringUtils.join(mList, ",");	
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setFocusListener(){
		mNameEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mNameLayout);
				} else {
					mNameLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		
		mEmailEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mEmailLayout);

				} else {
					mEmailLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mPhoneEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mPhoneLayout);
				} else {
					mPhoneLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mQualificationEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mQualificationLayout);
				} else {
					mQualificationLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mOrganizationEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mOrganizationLayout);
				} else {
					mOrganizationLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mDesignationEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mDesignationLayout);
				} else {
					mDesignationLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mExperienceEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mExperienceLayout);
				} else {
					mExperienceLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mRelationEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mRelationLayout);
				} else {
					mRelationLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mAgeEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mAgeLayout);
				} else {
					mAgeLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mDivisonEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mDivisonLayout);
				} else {
					mDivisonLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mHeadquarterEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mHeadquarterLayout);
				} else {
					mHeadquarterLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mRegionEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mRegionLayout);
				} else {
					mRegionLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mFileEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeEditText(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mFileLayout);
				} else {
					mFileLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mFileEv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showFileManagerDialog();
			}
		});
	}
	
	private void setTextWatcher(){
		mNameEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateName(mCharsequence);
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
		
		mEmailEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateEmail(mCharsequence);
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
		
		mPhoneEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validatePhone(mCharsequence);
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
		
		mQualificationEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateQualification(mCharsequence);
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
		
		mOrganizationEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateOrganization(mCharsequence);
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
		
		mDesignationEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateDesignation(mCharsequence);
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
		
		mExperienceEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateExperience(mCharsequence);
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
		
		mRelationEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateRelation(mCharsequence);
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
		
		mAgeEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateAge(mCharsequence);
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
		
		mDivisonEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateDivison(mCharsequence);
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
		
		mHeadquarterEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateHeadquarter(mCharsequence);
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
		
		mRegionEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateRegion(mCharsequence);
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
	}
	
	
	private void validateName(CharSequence mCharsequence){
		mNameValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			Pattern p = Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}");
			Matcher matcher = p.matcher(mCharsequence.toString());
			if (matcher.matches()) {
				isValidName =  true;
				mNameValidateIv.setImageResource(R.drawable.ic_text_correct);
			}else{
				isValidName = false;
				mNameValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			}
		}else{
			isValidName = false;
			mNameValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validateEmail(CharSequence mCharsequence){
		mEmailValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
					mCharsequence.toString()).matches()) {
				isValidEmail = false;
				mEmailValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			}else{
				isValidEmail = true;
				mEmailValidateIv.setImageResource(R.drawable.ic_text_correct);
			}
		}else{
			isValidEmail = false;
			mEmailValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validatePhone(CharSequence mCharsequence){
		mPhoneValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher matcher = p.matcher(mCharsequence.toString());
			if (matcher.matches()) {
				isValidMobile =  true;
				mPhoneValidateIv.setImageResource(R.drawable.ic_text_correct);
			}else{
				isValidMobile = false;
				mPhoneValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			}
		}else{
			isValidMobile = false;
			mPhoneValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validateQualification(CharSequence mCharsequence){
		mQualificationValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mQualificationValidateIv.setImageResource(R.drawable.ic_text_correct);
			isValidQualification = true;
		}else{
			mQualificationValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			isValidQualification = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateOrganization(CharSequence mCharsequence){
		mOrganizationValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mOrganizationValidateIv.setImageResource(R.drawable.ic_text_correct);
			isValidOrg = true;
		}else{
			mOrganizationValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			isValidOrg = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateDesignation(CharSequence mCharsequence){
		mDesignationValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mDesignationValidateIv.setImageResource(R.drawable.ic_text_correct);
			isValidDes = true;
		}else{
			mDesignationValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			isValidDes = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateExperience(CharSequence mCharsequence){
		mExperienceValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mExperienceValidateIv.setImageResource(R.drawable.ic_text_correct);
			isValidExperience = true;
		}else{
			mExperienceValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			isValidExperience = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateRelation(CharSequence mCharsequence){
		mRelationValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mRelationValidateIv.setImageResource(R.drawable.ic_text_correct);
			isValidRel = true;
		}else{
			mRelationValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			isValidRel = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateAge(CharSequence mCharsequence){
		mAgeValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mAgeValidateIv.setImageResource(R.drawable.ic_text_correct);
			isValidAge = true;
		}else{
			mAgeValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			isValidAge = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateDivison(CharSequence mCharsequence){
		mDivisonValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mDivisonValidateIv.setImageResource(R.drawable.ic_text_correct);
		}else{
			mDivisonValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validateHeadquarter(CharSequence mCharsequence){
		mHeadquarterValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mHeadquarterValidateIv.setImageResource(R.drawable.ic_text_correct);
		}else{
			mHeadquarterValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validateRegion(CharSequence mCharsequence){
		mRegionValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mRegionValidateIv.setImageResource(R.drawable.ic_text_correct);
		}else{
			mRegionValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	@SuppressWarnings("deprecation")
	private void setUiOfNextAccordingly(){
		if(isValidName && isValidEmail && isValidMobile && isValidQualification && isValidAge && isValidExperience && isValidOrg && isValidRel && isValidDes){
			isValid = true;
			ThemeUtils.getInstance(ParichayReferralFormActivity.this).applyThemeButton(ParichayReferralFormActivity.this, ParichayReferralFormActivity.this, mSubmitBtn);
		}else{
			isValid = false;
			mSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_normal));
		}
	}
	
	private void showValidationPopUp(){
		try{
			if(!isValidName){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid name!", Style.ALERT);
				mNameLayout.requestFocus();
			}else if(!isValidMobile){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid mobile!", Style.ALERT);
				mPhoneLayout.requestFocus();
			}else if(!isValidEmail){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid emailaddress!", Style.ALERT);
				mEmailLayout.requestFocus();
			}else if(!isValidQualification){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid qualification!", Style.ALERT);
				mQualificationLayout.requestFocus();
			}else if(!isValidAge){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid age!", Style.ALERT);
				mAgeLayout.requestFocus();
			}else if(!isValidExperience){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid experience!", Style.ALERT);
				mExperienceLayout.requestFocus();
			}else if(!isValidOrg){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid organization!", Style.ALERT);
				mOrganizationLayout.requestFocus();
			}else if(!isValidDes){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid designation!", Style.ALERT);
				mDesignationLayout.requestFocus();
			}else if(!isValidRel){
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, "Please enter valid relationship!", Style.ALERT);
				mRelationLayout.requestFocus();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mSubmitBtn);
			setMaterialRippleOnView(mBrowseBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void getFileFromDevice(boolean isSystemFileManager){
		if(AndroidUtilities.isAboveMarshMallow()){
				if(mPermissionHelper.isPermissionGranted(AppConstants.PERMISSION.STORAGE)){
					if(isSystemFileManager){
						Intent filePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
						filePickerIntent.setType("*/*");
				        startActivityForResult(filePickerIntent, INTENT_PICK_FILE_SYSTEM);		
					}else{
						Intent filePickerIntent = new Intent(ParichayReferralFormActivity.this, FileManagerActivity.class);
						startActivityForResult(filePickerIntent, INTENT_PICK_FILE_MOBCAST);
						AndroidUtilities.enterWindowAnimation(ParichayReferralFormActivity.this);
					}
				}else{
					checkPermissionModel();
				}
		}else{
			if(isSystemFileManager){
				Intent filePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				filePickerIntent.setType("*/*");
		        startActivityForResult(filePickerIntent, INTENT_PICK_FILE_SYSTEM);		
			}else{
				Intent filePickerIntent = new Intent(ParichayReferralFormActivity.this, FileManagerActivity.class);
				startActivityForResult(filePickerIntent, INTENT_PICK_FILE_MOBCAST);
				AndroidUtilities.enterWindowAnimation(ParichayReferralFormActivity.this);
			}
		}
		
	}
	
	
	private void showFileManagerDialog() {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(ParichayReferralFormActivity.this)
				.title(getResources().getString(R.string.select_filemanger_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_select_filemanager, true)
				.cancelable(true).show();

		View mView = mMaterialDialog.getCustomView();
		final AppCompatTextView mMobcastFileManager = (AppCompatTextView) mView.findViewById(R.id.dialogSelectMobcastFileManagerTv);
		final AppCompatTextView mSystemFileManager = (AppCompatTextView) mView.findViewById(R.id.dialogSelectSystemFileManagerTv);
		
		try {
			setMaterialRippleWithGrayOnView(mMobcastFileManager);
			setMaterialRippleWithGrayOnView(mSystemFileManager);
			mMobcastFileManager.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getFileFromDevice(false);
					mMaterialDialog.dismiss();
				}
			});
			
			mSystemFileManager.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getFileFromDevice(true);
					mMaterialDialog.dismiss();	
				}
			});
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		try{
			if((requestCode == INTENT_PICK_FILE_SYSTEM || requestCode == INTENT_PICK_FILE_MOBCAST ) && resultCode == Activity.RESULT_OK){
//				mFilePath = mIntent.getData().getPath();
				mFilePath = Utilities.getPath(mIntent.getData());
				if(TextUtils.isEmpty(mFilePath)){
					mFilePath = mIntent.getExtras().getStringArrayList("SelectedFiles").get(0);
				}
				if(!TextUtils.isEmpty(mFilePath)){
					String mExt = mFilePath.substring(mFilePath.lastIndexOf(".")+1, mFilePath.length());
					if(mExt.equalsIgnoreCase("doc") || mExt.equalsIgnoreCase("docx") || mExt.equalsIgnoreCase("pdf") || mExt.equalsIgnoreCase("rtf") || mExt.equalsIgnoreCase("png") || mExt.equalsIgnoreCase("jpeg")|| mExt.equalsIgnoreCase("jpg")){
						isValidFile = true;
						mFileEv.append(mFilePath);
						mFileValidateIv.setImageResource(R.drawable.ic_text_correct);	
						setUiOfNextAccordingly();
					}else{
						AndroidUtilities.showSnackBar(ParichayReferralFormActivity.this, "Only .doc, .docx, pdf, jpeg, png or rtf is allowed!");
					}
				}else{
					isValidFile = false;
					mFileValidateIv.setImageResource(R.drawable.ic_text_incorrect);
					AndroidUtilities.showSnackBar(ParichayReferralFormActivity.this, "Only .doc, .docx, pdf, jpeg, png or rtf is allowed!");
				}
			}else{
				mPermissionHelper.onActivityForResult(requestCode);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void parseDataFromApi(String mResponseFromApi){
		try{
			addReferredDataInDB(mResponseFromApi);
			Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup, Utilities.getSuccessMessageFromApi(mResponseFromApi), Style.CONFIRM);
			mNameEv.setText("");
			mEmailEv.setText("");
			mOrganizationEv.setText("");
			mDesignationEv.setText("");
			mExperienceEv.setText("");
			mAgeEv.setText("");
			mRelationEv.setText("");
			mQualificationEv.setText("");
			mPhoneEv.setText("");
			mFileEv.setText("");
			mFilePath = null;
			isValidEmail = false;
			isValidFile = false;
			isValidName = false;
			isValidMobile = false;
			isValid = false;
			AndroidUtilities.showSnackBar(ParichayReferralFormActivity.this, Utilities.getSuccessMessageFromApi(mResponseFromApi));
			isReferralSucess = true;
		}catch(Exception e){
			isReferralSucess = false;
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void addReferredDataInDB(String mResponseFromApi){
		try{
			JSONObject mJSONObj = new JSONObject(mResponseFromApi);
			ContentValues mValues = new ContentValues();
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID, mJSONObj.getString(AppConstants.API_KEY_PARAMETER.referredId));
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_JOB_ID, mId);
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_DATE, String.valueOf(System.currentTimeMillis()));
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_FOR, mReferredFor);
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_NAME, mNameEv.getText().toString());
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_TYPE, Utilities.formatUnit(mJobUnit));
			mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL, mInstallment);
			getContentResolver().insert(DBConstant.Parichay_Referral_Columns.CONTENT_URI, mValues);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public class AsyncUpdateReferralTask extends AsyncTask<Void, Integer, String> {
		private PowerManager.WakeLock mWakeLock;

		private NumberProgressBar mNumberProgressBar;

		private DownloadProgressDialog mDownloadProgressDialog;

		private AppCompatTextView mNameTextView;
		private AppCompatTextView mFactTextView;
		private AppCompatTextView mDownloadProgressTextView;
		
		private ImageView mImageView;

		private AppCompatButton mCancelBtn;
		
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private long totalSize = 0;
		
		private Animation mAnimationRotate;


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			acquirePowerLock();
			initUi();
			if(mDownloadProgressDialog!=null){
				mDownloadProgressDialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// updating progress bar value
			mNumberProgressBar.setProgress(progress[0]);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = apiUploadFileWithParameters();
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
				return mResponseFromApi;
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
				if(mDownloadProgressDialog!=null){
					mDownloadProgressDialog.dismiss();
				}
				releasePowerLock();
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		private String apiUploadFileWithParameters() {
			String responseString = null;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 700000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpPost httppost = new HttpPost(
					AppConstants.API.API_SUBMIT_PARICHAY);
			try {
				ProgressMutliPartEntity entity = new ProgressMutliPartEntity(
						new ProgressListener() {
							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});
				if(!TextUtils.isEmpty(mFilePath)){
					File mUploadFilePath = new File(mFilePath);
					entity.addPart(AppConstants.API_KEY_PARAMETER.captureFile, new FileBody(mUploadFilePath));	
				}
				entity.addPart(AppConstants.API_KEY_PARAMETER.accessToken,new StringBody(ApplicationLoader.getPreferences().getAccessToken()));
				entity.addPart(AppConstants.API_KEY_PARAMETER.userName, new StringBody(String.valueOf(TextUtils.isEmpty(mPhoneEv.getText().toString())? "":mPhoneEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.jobId, new StringBody(mId));
				entity.addPart(AppConstants.API_KEY_PARAMETER.jobUnit, new StringBody(mJobUnit));
				entity.addPart(AppConstants.API_KEY_PARAMETER.name, new StringBody(String.valueOf(TextUtils.isEmpty(mNameEv.getText().toString())? "":mNameEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.qualification, new StringBody(String.valueOf(TextUtils.isEmpty(mQualificationEv.getText().toString())? "":mQualificationEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.emailAddress, new StringBody(String.valueOf(TextUtils.isEmpty(mEmailEv.getText().toString())? "":mEmailEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.organization, new StringBody(String.valueOf(TextUtils.isEmpty(mOrganizationEv.getText().toString())? "":mOrganizationEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.designation, new StringBody(String.valueOf(TextUtils.isEmpty(mDesignationEv.getText().toString())? "":mDesignationEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.experience, new StringBody(String.valueOf(TextUtils.isEmpty(mExperienceEv.getText().toString())? "":mExperienceEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.relationship, new StringBody(String.valueOf(TextUtils.isEmpty(mRelationEv.getText().toString())? "":mRelationEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.age, new StringBody(String.valueOf(TextUtils.isEmpty(mAgeEv.getText().toString())? "":mAgeEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.region, new StringBody(String.valueOf(TextUtils.isEmpty(mRegionEv.getText().toString())? "":mRegionEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.headquarter, new StringBody(String.valueOf(TextUtils.isEmpty(mHeadquarterEv.getText().toString())? "":mHeadquarterEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.divison, new StringBody(String.valueOf(TextUtils.isEmpty(mDivisonEv.getText().toString())? "":mDivisonEv.getText().toString())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.jobReferredFor, new StringBody(String.valueOf(TextUtils.isEmpty(mJobReferredFor)? "":mJobReferredFor)));
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = JSONRequestBuilder
							.getErrorMessageFromStatusCode(
									String.valueOf(statusCode),
									getResources().getString(
											R.string.api_unknown_error))
							.toString();
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}catch(Exception e){
				responseString = JSONRequestBuilder.getErrorMessageFromStatusCode("500", "Please try again!").toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			releasePowerLock();
			if(mDownloadProgressDialog!=null){
				mDownloadProgressDialog.dismiss();
			}
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(ParichayReferralFormActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
		
		/*
		 * Power Lock
		 */
		@SuppressLint("Wakelock")
		private void acquirePowerLock() {
			try {
				PowerManager pm = (PowerManager) mContext
						.getSystemService(Context.POWER_SERVICE);
				mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						getClass().getName());
				mWakeLock.acquire();
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
			}
		}

		private void releasePowerLock() {
			try {
				mWakeLock.release();
				mWakeLock = null;
			} catch (Exception e) {

			}
		}
		
		@SuppressWarnings("deprecation")
		private void initUi() {
			try{
				mDownloadProgressDialog = new DownloadProgressDialog(mContext);
				mFactTextView = mDownloadProgressDialog.getFactTextView();
				mNameTextView = mDownloadProgressDialog.getFileNameTextView();
				mCancelBtn = mDownloadProgressDialog.getCancelButton();
				mNumberProgressBar = mDownloadProgressDialog.getNumberProgressBar();
				mDownloadProgressTextView = mDownloadProgressDialog.getDownloadProgressTextView();
				mImageView = mDownloadProgressDialog.getImageView();
				
				mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.splash_logo));
				mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_image));
				mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_image));
				mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_image));
				mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_image));
				
				mNameTextView.setVisibility(View.VISIBLE);
				mCancelBtn.setVisibility(View.VISIBLE);
				mDownloadProgressTextView.setVisibility(View.INVISIBLE);
				
				mNameTextView.setText(mContext.getResources().getString(R.string.loadingSubmit));
				mAnimationRotate = AnimationUtils.loadAnimation(mContext, R.anim.rotate_infinite);
				
				mImageView.startAnimation(mAnimationRotate);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
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
						.request(AppConstants.PERMISSION.STORAGE);
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
			showFileManagerDialog();
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
	 * Google Analytics
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

