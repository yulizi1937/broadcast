/**
 * 
 */
package com.application.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.application.ui.view.CircleImageView;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ProgressWheel;
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
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class SetProfileActivity extends AppCompatActivity{
	private static final String TAG = SetProfileActivity.class.getSimpleName();
	
	private static final int INTENT_PHOTO = 1001;
	
	private AppCompatTextView mRemoveTv;
	private AppCompatTextView mSkipTv;
	
	private TextView mToolBarTitleTv;
	
	private AppCompatEditText mNameEv;
	private AppCompatEditText mEmailEv;
	private AppCompatEditText mEmployeeIdEv;
	
	private AppCompatButton mNextBtn;
	
	private ImageView mNameValidateIv;
	private ImageView mEmailValidateIv;
	private ImageView mEmployeeIdIv;
	private ImageView mToolBarDrawer;
	private ImageView mUploadAnotherIv;
	
	private LinearLayout mNameLayout;
	private LinearLayout mEmailLayout;
	private LinearLayout mEmployeeIdLayout;
	
	private FrameLayout mCroutonViewGroup;
	
	private ProgressWheel mProgressWheel;
	private CircleImageView mProfileCirleIv;

	private String mPicturePath;
	
	private ImageLoader mImageLoader;
	
	private boolean isValidName = false;
	private boolean isValidEmail = false;
	private boolean isValidEmployeeId = false;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_profile);
		setSecurity();
		initUi();
		initToolBar();
		setUiListener();
		setDataFromPreferences();
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

	private void initUi(){
		mUploadAnotherIv = (ImageView)findViewById(R.id.activitySetProfileUploadAnotherIv);
		mRemoveTv = (AppCompatTextView)findViewById(R.id.activitySetProfileRemoveTv);
		mSkipTv = (AppCompatTextView)findViewById(R.id.activitySetProfileSkipBtn);
		
		mNameEv = (AppCompatEditText)findViewById(R.id.activitySetProfileNameEv);
		mEmailEv = (AppCompatEditText)findViewById(R.id.activitySetProfileEmailEv);
		mEmployeeIdEv = (AppCompatEditText)findViewById(R.id.activitySetProfileEmployeeIdEv);
		
		mNextBtn = (AppCompatButton)findViewById(R.id.activitySetProfileNextBtn);
		
		mNameValidateIv = (ImageView)findViewById(R.id.activitySetProfileNameValidateIv);
		mEmailValidateIv = (ImageView)findViewById(R.id.activitySetProfileEmailValidateIv);
		mEmployeeIdIv = (ImageView)findViewById(R.id.activitySetProfileEmployeeIdValidateIv);
		
		mProgressWheel = (ProgressWheel)findViewById(R.id.activitySetProfileProgressWheel);
		
		mProfileCirleIv = (CircleImageView)findViewById(R.id.activitySetProfileCircleImageView);
		
		mNameLayout = (LinearLayout)findViewById(R.id.activitySetProfileNameLayout);
		mEmailLayout = (LinearLayout)findViewById(R.id.activitySetProfileEmailLayout);
		mEmployeeIdLayout= (LinearLayout)findViewById(R.id.activitySetProfileEmployeeIdLayout);
		
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
	}
	
	private void initToolBar() {
		setSupportActionBar((Toolbar) findViewById(R.id.toolbarLayout));
		mToolBarTitleTv = (TextView)findViewById(R.id.toolbarTitleTv);
    	mToolBarDrawer = (ImageView)findViewById(R.id.toolbarBackIv);
    	
    	mToolBarDrawer.setVisibility(View.GONE);
    	mToolBarTitleTv.setText(getResources().getString(R.string.SetProfileActivityTitle));
	}
	
	private void setUiListener(){
		setFocusListener();
		setTextWatcher();
		setMaterialRippleView();
		setClickListener();
	}
	
	private void setDataFromPreferences(){
		mNameEv.setText(ApplicationLoader.getPreferences().getUserDisplayName());
		mEmailEv.setText(ApplicationLoader.getPreferences().getUserEmailAddress());
		mEmployeeIdEv.setText(ApplicationLoader.getPreferences().getUserEmployeeId());
		
		mImageLoader = ApplicationLoader.getUILImageLoader();
		mImageLoader.displayImage(ApplicationLoader.getPreferences().getUserProfileImageLink(), mProfileCirleIv);
	}
	
	private void setClickListener(){
		mUploadAnotherIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPhotoFromGallery();
			}
		});
		
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!BuildVars.DEBUG_DESIGN){
					if (Utilities.isInternetConnected()) {
						if(isValidName && isValidEmail){
							new AsyncUpdateProfileTask().execute();
						}
					} else {
						Utilities.showCrouton(
								SetProfileActivity.this,
								mCroutonViewGroup,
								getResources().getString(
										R.string.internet_unavailable),
								Style.ALERT);
					}
				}else{
					Intent mIntent = new Intent(SetProfileActivity.this, MotherActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(SetProfileActivity.this);
					finish();
				}
			}
		});
		
		mSkipTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(SetProfileActivity.this, MotherActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(SetProfileActivity.this);
				finish();
			}
		});
	}
	
	private void setFocusListener(){
		mNameEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					mNameLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_selected));
				} else {
					mNameLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mEmployeeIdEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					mEmployeeIdLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_selected));
				} else {
					mEmployeeIdLayout.setBackgroundDrawable(getResources()
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
					mEmailLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_selected));
				} else {
					mEmailLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
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
		
		mEmployeeIdEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateEmployeeId(mCharsequence);
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
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			Pattern p = Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}");
			Matcher matcher = p.matcher(mCharsequence.toString());
			if (matcher.matches()) {
				isValidName =  true;
			}else{
				isValidName = false;
			}
		}else{
			isValidName = false;
		}
		setUiOfNextAccordingly();
	}
	
	private void validateEmail(CharSequence mCharsequence){
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
					mCharsequence.toString()).matches()) {
				isValidEmail = false;
			}else{
				isValidEmail = true;
			}
		}else{
			isValidEmail = false;
		}
		setUiOfNextAccordingly();
	}
	
	
	private void validateEmployeeId(CharSequence mCharsequence){
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher matcher = p.matcher(mCharsequence.toString());
			if (matcher.matches()) {
				isValidEmployeeId =  true;
			}else{
				isValidEmployeeId = false;
			}
		}else{
			isValidEmployeeId = false;
		}
		setUiOfNextAccordingly();
	}
	
	@SuppressWarnings("deprecation")
	private void setUiOfNextAccordingly(){
		if(isValidName && isValidEmail && isValidEmployeeId){
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_pressed));
		}else{
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_normal));
		}
	}
	
	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mNextBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	private void setMaterialRippleOnView(View mView){
		MaterialRippleLayout.on(mView)
        .rippleColor(Color.parseColor("#FFFFFF"))
        .rippleAlpha(0.2f)
        .rippleHover(true)
        .rippleOverlay(true)
        .rippleBackground(Color.parseColor(getString(R.color.toolbar_background)))
        .create();
	}
	
	private void getPhotoFromGallery(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, INTENT_PHOTO);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		if(resultCode == Activity.RESULT_OK){
			Uri selectedImage = mIntent.getData();
			mPicturePath = Utilities.getPath(selectedImage);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap mBitmap = BitmapFactory.decodeFile(mPicturePath, options);
			mProfileCirleIv.setImageBitmap(mBitmap);
			
//			mProfileCirleIv.setVisibility(View.GONE);
//			mProgressWheel.setVisibility(View.VISIBLE);
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
	
	private String apiUpdateProfile(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostUserProfile(mNameEv
					.getText().toString(), mEmailEv.getText().toString(),
					mEmployeeIdEv.getText().toString(), mPicturePath);
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient
						.postJSON(new OkHttpClient(),AppConstants.API.API_UPDATE_USER, jsonObj.toString(), TAG);
			}else{
				return RestClient
						.postJSON(AppConstants.API.API_UPDATE_USER, jsonObj, TAG);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	private void parseDataFromApi(String mResponseFromApi){
		try{
			JSONObject  mJSONObject = new JSONObject(mResponseFromApi);
			JSONObject mJSONObjectUser =  mJSONObject.getJSONObject(AppConstants.API_KEY_PARAMETER.user);
			String mProfileImage = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.profileImage);
			String name = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.name);
			String emailAddress = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.emailAddress);
			String employeeId =  mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.employeeId);
			if(!TextUtils.isEmpty(mProfileImage)){
				ApplicationLoader.getPreferences().setUserProfileImageLink(mProfileImage);
			}
			
			if(!TextUtils.isEmpty(name)){
				ApplicationLoader.getPreferences().setUserDisplayName(name);
			}
			
			if(!TextUtils.isEmpty(emailAddress)){
				ApplicationLoader.getPreferences().setUserEmailAddress(emailAddress);
			}
			
			if(!TextUtils.isEmpty(employeeId)){
				ApplicationLoader.getPreferences().setUserEmployeeId(employeeId);
			}
			
			Intent mIntent = new Intent(SetProfileActivity.this, MotherActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(SetProfileActivity.this);
			finish();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public class AsyncUpdateProfileTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(SetProfileActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingUpdate));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiUpdateProfile();
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
				Utilities.showCrouton(SetProfileActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
	}
}
