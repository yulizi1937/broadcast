/**
 * 
 */
package com.application.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.sms.Sms;
import com.application.sms.SmsListener;
import com.application.sms.SmsRadar;
import com.application.ui.activity.LoginActivity.AsyncLoginTask;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.MobcastConfig;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.facebook.stetho.common.Util;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class VerificationActivity extends AppCompatActivity {
	private static final String TAG = VerificationActivity.class
			.getSimpleName();

	private AppCompatEditText mVerificationCode1;
	private AppCompatEditText mVerificationCode2;
	private AppCompatEditText mVerificationCode3;
	private AppCompatEditText mVerificationCode4;

	private AppCompatTextView mVerificationTv;
	private AppCompatTextView mTimerTv;
	private AppCompatTextView mEnteredAgainTv;
	
	private Toolbar mToolbar;

	private TextView mToolBarTitleTv;

	private AppCompatButton mNextBtn;
	private AppCompatButton mReSendBtn;
	private AppCompatButton mRaisedTicket;

	private ImageView mToolBarDrawer;

	private FrameLayout mCroutonViewGroup;

	private boolean isValidVerificationCode = false;
	private boolean isResendClicked = false;

	private String OTP;
	private String mUserName;
	private String mCountryCode;
	private String mOTP;

	/*
	 * private long mStartTime = 0L; private long mTimeInMilliSeconds = 0L;
	 * private long mTimeSwapBuff = 0L; private long mUpdatedTime = 0L;
	 */

	private Handler mHandler = new Handler();
	private long mTimerStartFrom;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		setSecurity();
		initUi();
		initToolBar();
		applyTheme();
		setUiListener();
		initTimer();
		getIntentData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setSMSRadarService();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SmsRadar.stopSmsRadarService(ApplicationLoader.getApplication());
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
		mVerificationCode1 = (AppCompatEditText) findViewById(R.id.activityVerificationCode1);
		mVerificationCode2 = (AppCompatEditText) findViewById(R.id.activityVerificationCode2);
		mVerificationCode3 = (AppCompatEditText) findViewById(R.id.activityVerificationCode3);
		mVerificationCode4 = (AppCompatEditText) findViewById(R.id.activityVerificationCode4);

		
		
		mNextBtn = (AppCompatButton) findViewById(R.id.activityVerificationBtn);
		mReSendBtn = (AppCompatButton) findViewById(R.id.activityVerificationResendBtn);
		mRaisedTicket = (AppCompatButton) findViewById(R.id.activityVerificationRaiseTickedBtn);

		mVerificationTv = (AppCompatTextView) findViewById(R.id.activityVerificationTv);
		mTimerTv = (AppCompatTextView) findViewById(R.id.activityVerificationRetryTv);
		mEnteredAgainTv = (AppCompatTextView) findViewById(R.id.activityVerificationAdminstrationTryAgain);

		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
	}

	private void initToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbarLayout);
		setSupportActionBar(mToolbar);
		mToolBarTitleTv = (TextView) findViewById(R.id.toolbarTitleTv);
		mToolBarDrawer = (ImageView) findViewById(R.id.toolbarBackIv);

		mToolBarDrawer.setVisibility(View.GONE);
		mToolBarTitleTv.setText(getResources().getString(
				R.string.VerificationActivityTitle));
	}
	
	private void applyTheme() {
		try {
			ThemeUtils.getInstance(VerificationActivity.this)
					.applyThemeVerification(VerificationActivity.this,
							VerificationActivity.this, mToolbar,
							mVerificationTv);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void initTimer() {
		// mStartTime = SystemClock.uptimeMillis();
		// if(!isResendClicked){
		mTimerStartFrom = MobcastConfig.BUILD.VERIFICATION_TIMER_OUT;
		mHandler.postDelayed(updateTimerThread, 1000);
		// }
	}
	
	private void getIntentData(){
		try{
			mUserName = getIntent().getStringExtra(AppConstants.INTENTCONSTANTS.USERNAME);
			if(mUserName==null){
				mUserName = ApplicationLoader.getPreferences().getUserName();
			}
			mCountryCode = getIntent().getStringExtra(AppConstants.INTENTCONSTANTS.COUNTRYCODE);
			mOTP = getIntent().getStringExtra(AppConstants.INTENTCONSTANTS.OTP);
			if(BuildVars.DEBUG_OTP){
//				Utilities.showCrouton(VerificationActivity.this	, mCroutonViewGroup, mOTP, Style.INFO);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(VerificationActivity.this);
		}
	}

	private void setUiListener() {
		setFocusListener();
		setTextWatcher();
		setMaterialRippleView();
		setClickListener();
	}

	private void setFocusListener() {
		onFocusListener(mVerificationCode1);
		onFocusListener(mVerificationCode2);
		onFocusListener(mVerificationCode3);
		onFocusListener(mVerificationCode4);
	}

	private void setTextWatcher() {
		onTextWatcher(mVerificationCode1);
		onTextWatcher(mVerificationCode2);
		onTextWatcher(mVerificationCode3);
		onTextWatcher(mVerificationCode4);
	}

	private void onTextWatcher(final AppCompatEditText mAppCompatEditText) {
		mAppCompatEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateVerificationCodeBoxes(mAppCompatEditText);
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

	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			/*
			 * mTimeInMilliSeconds = SystemClock.uptimeMillis() - mStartTime;
			 * mUpdatedTime = mTimeSwapBuff + mTimeInMilliSeconds; int secs =
			 * (int)mUpdatedTime/1000; if(secs >
			 * MobcastConfig.BUILD.VERIFICATION_TIMER_OUT){
			 * mHandler.removeCallbacks(updateTimerThread);
			 * mTimerTv.setVisibility(View.GONE);
			 * mReSendBtn.setVisibility(View.VISIBLE); } int mins = secs/60;
			 * secs %=60;
			 */

			mTimerStartFrom--;
			int mins = (int) mTimerStartFrom / 60;
			int secs = (int) mTimerStartFrom % 60;
			if (secs < 1 && mins == 0) {
				mHandler.removeCallbacks(updateTimerThread);
				mTimerTv.setVisibility(View.GONE);
				if (isResendClicked) {
					mRaisedTicket.setVisibility(View.VISIBLE);
					mReSendBtn.setVisibility(View.GONE);
				}else{
					mReSendBtn.setVisibility(View.VISIBLE);					
				}
			}
			mTimerTv.setText("" + mins + ":" + String.format("%02d", secs));
			mHandler.postDelayed(this, 1000);
		}
	};

	private void validateVerificationCodeBoxes(
			AppCompatEditText mAppCompatEditText) {
		if (!TextUtils.isEmpty(mVerificationCode1.getText().toString())
				&& !TextUtils.isEmpty(mVerificationCode2.getText().toString())
				&& !TextUtils.isEmpty(mVerificationCode3.getText().toString())
				&& !TextUtils.isEmpty(mVerificationCode4.getText().toString())) {
			isValidVerificationCode = true;
			setUiOfVerifyAccordingly();
		}
		if (mAppCompatEditText.getId() == R.id.activityVerificationCode1) {
			mVerificationCode2.requestFocus();
		} else if (mAppCompatEditText.getId() == R.id.activityVerificationCode2) {
			mVerificationCode3.requestFocus();
		} else if (mAppCompatEditText.getId() == R.id.activityVerificationCode3) {
			mVerificationCode4.requestFocus();
		} else if (mAppCompatEditText.getId() == R.id.activityVerificationCode4) {
			mNextBtn.performClick();
		}
	}

	private void setSMSRadarService() {
		SmsRadar.initializeSmsRadarService(ApplicationLoader.getApplication(),
				new SmsListener() {
					@Override
					public void onSmsSent(Sms sms) {
						showSmsToast(sms);
					}

					@Override
					public void onSmsReceived(Sms sms) {
						showSmsToast(sms);
					}
				});
	}

	private void showSmsToast(Sms sms) {
		// Utilities.showCrouton(VerificationActivity.this, mCroutonViewGroup,
		// sms.toString(), Style.INFO);
		if (!TextUtils.isEmpty(sms.toString())) {
			OTP = sms.getMsg();
			if (!TextUtils.isEmpty(OTP)) {
				if (OTP.contains("mobcast")) {
					OTP = OTP.substring(OTP.length() - 4, OTP.length());
					mVerificationCode1.setText(String.valueOf(OTP.charAt(0)));
					mVerificationCode2.setText(String.valueOf(OTP.charAt(1)));
					mVerificationCode3.setText(String.valueOf(OTP.charAt(2)));
					mVerificationCode4.setText(String.valueOf(OTP.charAt(3)));
					if (!TextUtils.isEmpty(mVerificationCode1.getText()
							.toString())
							&& !TextUtils.isEmpty(mVerificationCode2.getText()
									.toString())
							&& !TextUtils.isEmpty(mVerificationCode3.getText()
									.toString())
							&& !TextUtils.isEmpty(mVerificationCode4.getText()
									.toString())) {
						isValidVerificationCode = true;
						setUiOfVerifyAccordingly();
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void setUiOfVerifyAccordingly() {
		if (isValidVerificationCode) {
			ThemeUtils.getInstance(VerificationActivity.this).applyThemeButton(VerificationActivity.this, VerificationActivity.this, mNextBtn);
		} else {
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_button_normal));
		}
	}

	private void onFocusListener(final View view) {
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					view.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.shape_editbox_selected));
				} else {
					view.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.shape_editbox_normal));
				}
			}
		});
	}

	@SuppressLint("NewApi") private void setClickListener() {
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!BuildVars.DEBUG_DESIGN){
					if (!TextUtils.isEmpty(mVerificationCode1.getText().toString())
							&& !TextUtils.isEmpty(mVerificationCode2.getText()
									.toString())
							&& !TextUtils.isEmpty(mVerificationCode3.getText()
									.toString())
							&& !TextUtils.isEmpty(mVerificationCode4.getText()
									.toString())) {
						if (Utilities.isInternetConnected()) {
							if (AndroidUtilities.isAboveIceCreamSandWich()) {
								new AsyncVerifyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
							} else {
								new AsyncVerifyTask().execute();
							}
						} else {
							Utilities.showCrouton(
									VerificationActivity.this,
									mCroutonViewGroup,
									getResources().getString(
											R.string.internet_unavailable),
									Style.ALERT);
						}
					} else {
						Utilities.showCrouton(
								VerificationActivity.this,
								mCroutonViewGroup,
								getResources().getString(
										R.string.validate_verfication_code_empty),
								Style.ALERT);
					}
				}else{
					Intent mIntent = new Intent(VerificationActivity.this, SetProfileActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(VerificationActivity.this);
					finish();
				}
			}
		});

		mEnteredAgainTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ApplicationLoader.getPreferences().setUserName(null);
				ApplicationLoader.getPreferences().setAttemptedToLoginDidntReceiveOTP(false);
				Intent mIntent = new Intent(VerificationActivity.this,
						LoginActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
				finish();
			}
		});

		mReSendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTimerStartFrom = MobcastConfig.BUILD.VERIFICATION_TIMER_OUT;
				mReSendBtn.setVisibility(View.GONE);
				mTimerTv.setVisibility(View.VISIBLE);
				isResendClicked = true;
				initTimer();
				if (Utilities.isInternetConnected()) {
					if (AndroidUtilities.isAboveIceCreamSandWich()) {
						new AsyncVerifyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
					} else {
						new AsyncVerifyTask().execute();
					}
				} else {
					Utilities.showCrouton(
							VerificationActivity.this,
							mCroutonViewGroup,
							getResources().getString(
									R.string.internet_unavailable),
							Style.ALERT);
				}
			}
		});

		mRaisedTicket.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					if(!TextUtils.isEmpty(mUserName)){
						UserReport.updateUserIssueApi(AppConstants.INTENTCONSTANTS.OTP, mUserName);
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",AppConstants.mSupportEmail, null));
						emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suport : Unable to Login | " + getResources().getString(R.string.app_name));
						emailIntent.putExtra(Intent.EXTRA_TEXT, "Please help me to login \n User Credentails: \n" + mUserName + "\n" +  Utilities.getDeviceMfg() + "\n" + Utilities.getDeviceName());
						startActivity(Intent.createChooser(emailIntent, "Send email"));
					}else{
						AndroidUtilities.showSnackBar(VerificationActivity.this, "Please enter your username!");
					}
				}catch(Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
		});
		
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

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mNextBtn);
			setMaterialRippleOnView(mReSendBtn);
			setMaterialRippleOnView(mRaisedTicket);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private String apiVerifyUser() {
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostVerifyData(
					mUserName, mVerificationCode1.getText().toString()
							+ mVerificationCode2.getText().toString()
							+ mVerificationCode3.getText().toString()
							+ mVerificationCode4.getText().toString());
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient
						.postJSON(new OkHttpClient(),AppConstants.API.API_VERIFY, jsonObj.toString(), TAG);
			}else{
				return RestClient
						.postJSON(AppConstants.API.API_VERIFY, jsonObj, TAG);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	private String apiRequestOTPAgain() {
			try {
					JSONObject jsonObj = JSONRequestBuilder.getPostLoginData(mUserName, mCountryCode);
					if(BuildVars.USE_OKHTTP){
						return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_LOGIN, jsonObj.toString(), TAG);	
					}else{
						return RestClient.postJSON(AppConstants.API.API_LOGIN, jsonObj, TAG);	
					}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FileLog.e(TAG, e.toString());
			}
			return null;
	}
	
	private void parseDataFromOTP(String mResponseFromApi){
		try{
			JSONObject mJSONObj = new JSONObject(mResponseFromApi);
			mJSONObj.getString("otp");
			AndroidUtilities.showSnackBar(VerificationActivity.this, getResources().getString(R.string.resend_otp));
			isResendClicked = true;
		}catch(Exception e){
		}
	}
	
	private void parseDataFromApi(String mResponseFromApi){
		try{
			JSONObject  mJSONObject = new JSONObject(mResponseFromApi);
			ApplicationLoader.getPreferences().setAccessToken(mJSONObject.getString(AppConstants.API_KEY_PARAMETER.accessToken));
			ApplicationLoader.getPreferences().setUserName(mUserName);
			JSONObject mJSONObjectUser =  mJSONObject.getJSONObject(AppConstants.API_KEY_PARAMETER.user);
			String name = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.name);
			String emailAddress = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.emailAddress);
			String employeeId =  mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.employeeId);
			String mProfileImage = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.profileImage);
			String mFavouriteQuestion = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.favouriteQuestion);
			String mFavouriteAnswer = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.favouriteAnswer);
			String mBirthDate = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.birthdate);
			
			if(!TextUtils.isEmpty(name)){
				ApplicationLoader.getPreferences().setUserDisplayName(name);
			}
			
			if(!TextUtils.isEmpty(emailAddress)){
				ApplicationLoader.getPreferences().setUserEmailAddress(emailAddress);
			}
			
			if(!TextUtils.isEmpty(employeeId)){
				ApplicationLoader.getPreferences().setUserEmployeeId(employeeId);
			}
			
			if(!TextUtils.isEmpty(mProfileImage)){
				ApplicationLoader.getPreferences().setUserProfileImageLink(mProfileImage);
			}
			
			if(!TextUtils.isEmpty(mFavouriteAnswer)){
				ApplicationLoader.getPreferences().setUserFavouriteAnswer(mFavouriteAnswer);
			}
			
			if(!TextUtils.isEmpty(mFavouriteQuestion)){
				ApplicationLoader.getPreferences().setUserFavouriteQuestion(mFavouriteQuestion);
			}
			
			if(!TextUtils.isEmpty(mBirthDate)){
				ApplicationLoader.getPreferences().setUserBirthdate(mBirthDate);
			}
			
			if(BuildVars.IS_AUTOSYNC){
				ApplicationLoader.setSyncServiceAlarm();
			}
			
			ApplicationLoader.getPreferences().setAttemptedToLoginDidntReceiveOTP(true);
			Intent mIntent = new Intent(VerificationActivity.this, SetProfileActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(VerificationActivity.this);
			finish();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setMaterialRippleOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#FFFFFF"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}
	
	public class AsyncVerifyTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;
		private boolean isResendOTP = false;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(VerificationActivity.this);
			if(!isResendClicked){
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingVerify));	
			}else{
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRequest));				
			}
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				isResendOTP = isResendClicked; 
				mResponseFromApi = !isResendClicked ? apiVerifyUser()
						: apiRequestOTPAgain();
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
				if(isResendOTP){
					parseDataFromOTP(mResponseFromApi);
				}else{
					parseDataFromApi(mResponseFromApi);
				}
				
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(VerificationActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
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
