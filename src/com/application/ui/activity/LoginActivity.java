/**
 * 
 */
package com.application.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ProgressMaterialDialogWord;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.Utilities;
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

	private AppCompatTextView mLoginTitleTv;
	private AppCompatTextView mCountryCodeTv;
	private AppCompatTextView mAdminTv;

	private TextView mToolBarTitleTv;

	private AppCompatEditText mLoginIdEt;

	private AppCompatButton mNextBtn;

	private Toolbar mToolBar;

	private ImageView mLoginIdIv;
	private ImageView mToolBarDrawer;

	private LinearLayout mLoginLayout;
	private FrameLayout mCroutonViewGroup;

	private boolean isValidLoginId = false;

	private String userId;
	private String countryCodeValue;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setSecurity();
		initUi();
		initToolBar();
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

		mLoginIdEt = (AppCompatEditText) findViewById(R.id.activityLoginIdEv);

		mNextBtn = (AppCompatButton) findViewById(R.id.activityLoginNextBtn);

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

	private void setUiListener() {
		mLoginIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					mLoginLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_selected));
				} else {
					mLoginLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
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

	private void setClickListener() {
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(mLoginIdEt.getText().toString())) {
					if (Utilities.isInternetConnected()) {
						new AsyncLoginTask().execute();
					} else {
						Utilities.showCrouton(
								LoginActivity.this,
								mCroutonViewGroup,
								getResources().getString(
										R.string.internet_unavailable),
								Style.ALERT);
					}
				}
				
				/*Intent mIntent = new Intent(LoginActivity.this,
						VerificationActivity.class);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(LoginActivity.this);*/
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
		} else {
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_button_normal));
		}
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mNextBtn);
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

	private void tryToGetUserId() {
		if (BuildVars.IS_MOBILENUMBER_PRIMARY) {
			try {
				userId = getUserMobileFromImPrintedSIM();
				if (TextUtils.isEmpty(userId)) {
					userId = getUserMobileFromWhatsAppAPI();
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
			JSONObject jsonObj = JSONRequestBuilder.getPostLoginData(mLoginIdEt
					.getText().toString());
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

	private void parseDataFromApi(String mResponseFromApi) {
		ApplicationLoader.getPreferences().setUserName(
				mLoginIdEt.getText().toString());
		Intent mIntent = new Intent(LoginActivity.this,
				VerificationActivity.class);
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.USERNAME, mLoginIdEt.getText().toString());
		startActivity(mIntent);
		AndroidUtilities.enterWindowAnimation(LoginActivity.this);
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
}
