/**
 * 
 */
package com.application.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.ui.adapter.WhatWeDoPagerAdapter;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.MaterialRippleLayout;
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
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class LoginActivity extends AppCompatActivity {
	private static final String TAG = LoginActivity.class.getSimpleName();
	private static final int INTENT_COUNTRY_CODE = 1001;

	private ViewPager mViewPager;
	
	private CirclePageIndicator mCirclerPagerIndicator;
	
	private WhatWeDoPagerAdapter mAdapter;
	
	private AppCompatTextView mLoginTitleTv;
	private AppCompatTextView mCountryCodeTv;
	private AppCompatTextView mAdminTv;

	private TextView mToolBarTitleTv;

	private AppCompatEditText mLoginIdEt;

	private AppCompatButton mNextBtn;
	private AppCompatButton mNextBtn1;

	private Toolbar mToolBar;

	private ImageView mLoginIdIv;
	private ImageView mToolBarDrawer;

	private LinearLayout mLoginLayout;
	private FrameLayout mCroutonViewGroup;

	private boolean isValidLoginId = false;

	private String userId;
	private String countryCodeValue = "IN";
	
	private Handler mHandler = new Handler();
	private changePagerRunnable mRunnable = new changePagerRunnable();
	private int mRunnableTimer = 4000;
	
	private GoogleCloudMessaging gcm;
	private String regid;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private Context applicationContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setFullScreen();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setSecurity();
		initUi();
//		initToolBar();
		initViewPager();
		setAutomatePager();
		setUiListener();
		tryToGetUserId();
		tryToGetUserCountryCode();
		isViaOTPSupport();
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		try {
			if (AndroidUtilities.isAppLanguageIsEnglish()) {
				super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
			} else {
				super.attachBaseContext(newBase);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void initUi() {
		mAdminTv = (AppCompatTextView) findViewById(R.id.activityLoginAdminstration);
		mCountryCodeTv = (AppCompatTextView) findViewById(R.id.activityLoginCountryCodeTv);
		mLoginTitleTv = (AppCompatTextView) findViewById(R.id.activityLoginTv);
		
		mViewPager = (ViewPager)findViewById(R.id.activityLoginViewPager);
		mCirclerPagerIndicator = (CirclePageIndicator)findViewById(R.id.activityLoginCirclePageIndicator);

		mLoginIdEt = (AppCompatEditText) findViewById(R.id.activityLoginIdEv);

		mNextBtn = (AppCompatButton) findViewById(R.id.activityLoginNextBtn);
		mNextBtn1 = (AppCompatButton) findViewById(R.id.activityLoginNextBtn1);

		mLoginIdIv = (ImageView) findViewById(R.id.activityLoginValidateIv);

		mLoginLayout = (LinearLayout) findViewById(R.id.activityLoginLayout);
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
	}

	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		setSupportActionBar(mToolBar);
		mToolBarTitleTv = (TextView) findViewById(R.id.toolbarTitleTv);
		mToolBarDrawer = (ImageView) findViewById(R.id.toolbarBackIv);

		mToolBarDrawer.setVisibility(View.GONE);
		mToolBarTitleTv.setText(getResources().getString(
				R.string.LoginActivityTitle));
	}
	
	private void initViewPager(){
		mAdapter = new WhatWeDoPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mCirclerPagerIndicator.setViewPager(mViewPager);
	}
	
	private void setAutomatePager(){
		mHandler.postDelayed(mRunnable, mRunnableTimer);
	}
	
	private void changeCurrentPager(){
		int mPosition = mViewPager.getCurrentItem();
		mPosition++;
		if (mPosition < 4) {
			mViewPager.setCurrentItem(mPosition,true);
		}else{
			mPosition = 0;
			mViewPager.setCurrentItem(mPosition,true);
			mHandler.removeCallbacks(mRunnable);
		}
	}
	

	private void setUiListener() {
		mLoginIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					mLoginLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_selected));
					mNextBtn.setVisibility(View.GONE);
					mNextBtn1.setVisibility(View.VISIBLE);
				} else {
					mLoginLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
					mNextBtn.setVisibility(View.VISIBLE);
					mNextBtn1.setVisibility(View.GONE);
				}
			}
		});

		mLoginIdEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateLoginId(mCharsequence);
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

		setMaterialRippleView();
		setClickListener();
	}

	@SuppressLint("NewApi") private void setClickListener() {
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				doCheckApiUserExists();
			}
		});
		
		mNextBtn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doCheckApiUserExists();	
			}
		});

		mCountryCodeTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(LoginActivity.this,
						CountrySelectActivity.class);
				startActivityForResult(mIntent, INTENT_COUNTRY_CODE);
				AndroidUtilities.enterWindowAnimation(LoginActivity.this);
			}
		});
		
		mAdminTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					if(!TextUtils.isEmpty(mLoginIdEt.getText().toString())){
						UserReport.updateUserIssueApi(AppConstants.INTENTCONSTANTS.LOGIN, mLoginIdEt.getText().toString());
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",AppConstants.mSupportEmail, null));
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suport : Unable to Login | " + getResources().getString(R.string.app_name));
						emailIntent.putExtra(Intent.EXTRA_TEXT, "Please help me to login \n User Credentails: \n" + mLoginIdEt.getText().toString() + "\n" +  Utilities.getDeviceMfg() + "\n" + Utilities.getDeviceName());
						startActivity(Intent.createChooser(emailIntent, "Send email"));
					}else{
						AndroidUtilities.showSnackBar(LoginActivity.this, "Please enter your username!");
					}
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void doCheckApiUserExists(){
		if(!BuildVars.DEBUG_DESIGN){
			if (!TextUtils.isEmpty(mLoginIdEt.getText().toString())) {
				if (Utilities.isInternetConnected()) {
					if(isValidLoginId){
						mHandler.removeCallbacks(mRunnable);
						if (AndroidUtilities.isAboveIceCreamSandWich()) {
							new AsyncLoginTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
						} else {
							new AsyncLoginTask().execute();
						}
					}
				} else {
					Utilities.showCrouton(
							LoginActivity.this,
							mCroutonViewGroup,
							getResources().getString(
									R.string.internet_unavailable),
							Style.ALERT);
				}
			}	
		}else{
			Intent mIntent = new Intent(LoginActivity.this,
					VerificationActivity.class);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(LoginActivity.this);	
		}
	}
	
	private class changePagerRunnable implements Runnable{
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			changeCurrentPager();
			mHandler.postDelayed(mRunnable, mRunnableTimer);
		}
		
	}

	/**
	 * Security : Couldn't capture ScreenShot
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setSecurity() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (!BuildVars.DEBUG_SCREENSHOT) {
				getWindow().setFlags(LayoutParams.FLAG_SECURE,
						LayoutParams.FLAG_SECURE);
			}
		}
	}

	private void validateLoginId(CharSequence mCharsequence) {
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher matcher = p.matcher(mCharsequence.toString());
			if (matcher.matches()) {
				Phonenumber.PhoneNumber mPhoneNumber;
				try {
					mPhoneNumber = PhoneNumberUtil.getInstance().parse(
							mCharsequence.toString(), countryCodeValue);
					if (PhoneNumberUtil.getInstance().isValidNumber(
							mPhoneNumber)) {
						if (PhoneNumberUtil.getInstance()
								.isValidNumberForRegion(mPhoneNumber,
										countryCodeValue)) {
							isValidLoginId = true;
						} else {
							isValidLoginId = false;
						}
					} else {
						isValidLoginId = false;
					}
				} catch (NumberParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					countryCodeValue = "IN";
					isValidLoginId = false;
				}

			} else {
				isValidLoginId = false;
			}
		}
		setUiOfNextAccordingly();
	}

	@SuppressWarnings("deprecation")
	private void setUiOfNextAccordingly() {
		if (isValidLoginId) {
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_button_pressed));
			mNextBtn1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_button_pressed));
		} else {
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_button_normal));
			mNextBtn1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_button_normal));
		}
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mNextBtn);
			setMaterialRippleOnView(mNextBtn1);
			setMaterialRippleWithGrayOnView(mCountryCodeTv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setMaterialRippleOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#FFFFFF"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}

	private void setMaterialRippleWithGrayOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#aaaaaa"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}
	
	private void setFullScreen() {
		try {
					requestWindowFeature(Window.FEATURE_NO_TITLE);
					getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void tryToGetUserId() {
		if (BuildVars.IS_MOBILENUMBER_PRIMARY) {
			try {
				userId = getUserMobileFromImPrintedSIM();
				if (TextUtils.isEmpty(userId)) {
//					userId = getUserMobileFromWhatsAppAPI();
				}
				if (!TextUtils.isEmpty(userId)) {
					mLoginIdEt.setText(userId.subSequence(userId.length() - 10,
							userId.length()));
				}
				// getUserMobileFromMessages();
			} catch (Exception e) {
				FileLog.e(TAG, true, e.toString());
			}
		}
	}

	private void tryToGetUserCountryCode() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		countryCodeValue = tm.getNetworkCountryIso();
		if (TextUtils.isEmpty(countryCodeValue)) {
			countryCodeValue = "IN";
		}
	}

	@SuppressWarnings("unused")
	private void getUserMobileFromMessages() {
		try {
			// Create Sent box URI
			Uri sentURI = Uri.parse("content://sms/sent");
			// List required columns
			String[] reqCols = new String[] { "_id", "address", "creator",
					"body" };
			// Get Content Resolver object, which will deal with Content
			// Provider
			ContentResolver cr = getContentResolver();
			// Fetch Sent SMS Message from Built-in Content Provider
			Cursor c = cr.query(sentURI, reqCols, null, null, null);
			if (c != null && c.getCount() > 0) {

			}
		} catch (Exception e) {
			FileLog.e(TAG, true, e.toString());
		}
	}

	private String getUserMobileFromImPrintedSIM() {
		try {
			TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			userId = tm.getLine1Number();
			return userId;
		} catch (Exception e) {
			FileLog.e(TAG, true, e.toString());
			return null;
		}
	}

	private String getUserMobileFromWhatsAppAPI() {
		try {
			AccountManager am = AccountManager.get(this);
			Account[] accounts = am.getAccounts();
			for (Account ac : accounts) {
				if (!ac.name.contains("@")) {
					userId = ac.name;
				}
			}
			return userId;
		} catch (Exception e) {
			FileLog.e(TAG, true, e.toString());
			return null;
		}
	}

	private void isViaOTPSupport() {
		if (ApplicationLoader.getPreferences().isToByPassOTP()) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == INTENT_COUNTRY_CODE) {
				mCountryCodeTv.setText(mIntent.getStringExtra("country_code"));
				countryCodeValue = mIntent.getStringExtra("country_code_");
			}
		}
	}

	private String apiCheckUserExists() {
		try {
			if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getRegId())){
				JSONObject jsonObj = JSONRequestBuilder.getPostLoginData(mLoginIdEt
						.getText().toString(), mCountryCodeTv.getText().toString());
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_LOGIN, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_LOGIN, jsonObj, TAG);	
				}	
			}else{
				applicationContext = ApplicationLoader.getApplication().getApplicationContext();
				initPlayServices();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		try{
			if(!TextUtils.isEmpty(mResponseFromApi)){
				ApplicationLoader.getPreferences().setUserName(
						mLoginIdEt.getText().toString());
				Intent mIntent = new Intent(LoginActivity.this,
						VerificationActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.USERNAME, mLoginIdEt.getText().toString());
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.OTP, parseDataFromOTP(mResponseFromApi));
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.COUNTRYCODE, mCountryCodeTv.getText().toString());
				startActivity(mIntent);
				ApplicationLoader.getPreferences().setAttemptedToLoginDidntReceiveOTP(true);
				ApplicationLoader.getPreferences().setUserName(mLoginIdEt.getText().toString());
				AndroidUtilities.enterWindowAnimation(LoginActivity.this);
				finish();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private String parseDataFromOTP(String mResponseFromApi){
		try{
			JSONObject mJSONObj = new JSONObject(mResponseFromApi);
			return mJSONObj.getString("otp");
		}catch(Exception e){
			return "Sorry!";
		}
	}

	public class AsyncLoginTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(LoginActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingLogin));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiCheckUserExists();
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
				parseDataFromApi(mResponseFromApi);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(LoginActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
	}
	
	
	/**
	 * GCM API
	 */
	
	/*
	 * GCM API
	 */
	private void initPlayServices() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(applicationContext);
			regid = getRegistrationId();

			if (regid.length() == 0) {
				registerInBackground();
			}else{
				ApplicationLoader.getPreferences().setRegId(regid);
			}
		} else {
			FileLog.d("tmessages", "No valid Google Play Services APK found.");
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		return resultCode == ConnectionResult.SUCCESS;
	}

	private String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences(applicationContext);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			FileLog.d("tmessages", "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = ApplicationLoader.getAppVersion();
		if (registeredVersion != currentVersion) {
			FileLog.d("tmessages", "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(ApplicationLoader.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}


	@SuppressLint("NewApi") private void registerInBackground() {
		AsyncTask<String, String, Boolean> task = new AsyncTask<String, String, Boolean>() {
			@Override
			protected Boolean doInBackground(String... objects) {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(applicationContext);
				}
				int count = 0;
				while (count < 1000) {
					try {
						count++;
						regid = gcm.register(AppConstants.PUSH.PROJECT_ID);
						storeRegistrationId(applicationContext, regid);
						ApplicationLoader.getPreferences().setRegId(regid);
						if(isValidLoginId){
							mNextBtn.performClick();
						}
						return true;
					} catch (Exception e) {
						FileLog.e("tmessages", e);
					}
					try {
						if (count % 20 == 0) {
							Thread.sleep(60000 * 30);
						} else {
							Thread.sleep(5000);
						}
					} catch (InterruptedException e) {
						FileLog.e("tmessages", e);
					}
				}
				return false;
			}
		};

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null,
					null);
		} else {
			task.execute(null, null, null);
		}
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = ApplicationLoader.getAppVersion();
		FileLog.e("tmessages", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
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
