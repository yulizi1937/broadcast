/**
 * 
 */
package com.application.ui.activity;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.ui.activity.SetProfileActivity.AsyncUpdateProfileTask;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.CircleImageView;
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
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class EditProfileActivity extends SwipeBackBaseActivity{
	private static final String TAG = EditProfileActivity.class.getSimpleName();
	
	private static final int INTENT_PHOTO = 1001;
	
	private Toolbar mToolBar;
	
	private AppCompatTextView mRemoveTv;
	
	private TextView mToolBarTitleTv;
	
	private AppCompatEditText mNameEv;
	private AppCompatEditText mEmailEv;
	private AppCompatEditText mEmployeeIdEv;
	private AppCompatEditText mQuestionEv;
	private AppCompatEditText mAnswerEv;
	private AppCompatEditText mDOBEv;
	
	private AppCompatButton mUpdateBtn;

	private ImageView mUploadAnotherIv;
	private ImageView mNameValidateIv;
	private ImageView mEmailValidateIv;
	private ImageView mEmployeeValidateIv;
	private ImageView mQuestionValidateIv;
	private ImageView mAnswerValidateIv;
	private ImageView mDOBIv;
	private ImageView mToolBarDrawer;
	
	private LinearLayout mNameLayout;
	private LinearLayout mEmailLayout;
	private LinearLayout mEmployeeIdLayout;
	private LinearLayout mQuestionLayout;
	private LinearLayout mAnswerLayout;
	private LinearLayout mDOBLayout;
	
	private FrameLayout mCroutonViewGroup;
	private FrameLayout mProfileImageLayout;
	
	private ProgressWheel mProgressWheel;
	private CircleImageView mProfileCirleIv;
	
	private ImageLoader mImageLoader;
	
	private int mDay;
	private int mMonth;
	private int mYear;
	
	private String mPicturePath;
	
	private boolean isValidName = false;
	private boolean isValidEmail = false;
	private boolean isValidEmployeeId = false;
	private boolean isValid = false;
	
	private boolean isShareOptionEnable = false;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		setSecurity();
		initUi();
		initToolBar();
		applyTheme();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(EditProfileActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(EditProfileActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Profile");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(EditProfileActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi(){
		mUploadAnotherIv = (ImageView)findViewById(R.id.activityEditProfileUploadAnotherIv);
		mRemoveTv = (AppCompatTextView)findViewById(R.id.activityEditProfileRemoveTv);
		
		mNameEv = (AppCompatEditText)findViewById(R.id.activityEditProfileNameEv);
		mEmailEv = (AppCompatEditText)findViewById(R.id.activityEditProfileEmailEv);
		mEmployeeIdEv = (AppCompatEditText)findViewById(R.id.activityEditProfileEmployeeIdEv);
		mQuestionEv = (AppCompatEditText)findViewById(R.id.activityEditProfileEmployeeQuestionEv);
		mAnswerEv = (AppCompatEditText)findViewById(R.id.activityEditProfileAnswerEv);
		mDOBEv= (AppCompatEditText)findViewById(R.id.activityEditProfileBirthdayEv);
		
		mUpdateBtn = (AppCompatButton)findViewById(R.id.activityEditProfileNextBtn);
		
		mNameValidateIv = (ImageView)findViewById(R.id.activityEditProfileNameValidateIv);
		mEmailValidateIv = (ImageView)findViewById(R.id.activityEditProfileEmailValidateIv);
		mEmployeeValidateIv = (ImageView)findViewById(R.id.activityEditProfileEmployeeIdValidateIv);
		mQuestionValidateIv = (ImageView)findViewById(R.id.activityEditProfileEmployeeQuestionValidateIv);
		mAnswerValidateIv= (ImageView)findViewById(R.id.activityEditProfileAnswerValidateIv);
		mDOBIv = (ImageView)findViewById(R.id.activityEditProfileBirthdayValidateIv);
		
		mProgressWheel = (ProgressWheel)findViewById(R.id.activityEditProfileProgressWheel);
		
		mProfileCirleIv = (CircleImageView)findViewById(R.id.activityEditProfileCircleImageView);
		
		mNameLayout = (LinearLayout)findViewById(R.id.activityEditProfileNameLayout);
		mEmailLayout = (LinearLayout)findViewById(R.id.activityEditProfileEmailLayout);
		mEmployeeIdLayout = (LinearLayout)findViewById(R.id.activityEditProfileEmployeeIdLayout);
		mQuestionLayout = (LinearLayout)findViewById(R.id.activityEditProfileEmployeeQuestionLayout);
		mAnswerLayout = (LinearLayout)findViewById(R.id.activityEditProfileAnswerLayout);
		mDOBLayout= (LinearLayout)findViewById(R.id.activityEditProfileBirthdayLayout);
		
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
		mProfileImageLayout = (FrameLayout)findViewById(R.id.activityEditProfileImageLayout);
	}
	
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.EditProfileActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void setUiListener(){
		setFocusListener();
		setTextWatcher();
		setMaterialRippleView();
		setClickListener();
	}
	
	private void applyTheme() {
		try {
			ThemeUtils.getInstance(EditProfileActivity.this)
					.applyThemeSetProfile(EditProfileActivity.this,
							EditProfileActivity.this, mToolBar,
							mProfileImageLayout);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	

	private void setDataFromPreferences(){
		try{
			mNameEv.setText(ApplicationLoader.getPreferences().getUserDisplayName());
			mEmailEv.setText(ApplicationLoader.getPreferences().getUserEmailAddress());
			mEmployeeIdEv.setText(ApplicationLoader.getPreferences().getUserEmployeeId());
			mDOBEv.setText(ApplicationLoader.getPreferences().getUserBirthdate());
			mQuestionEv.setText(ApplicationLoader.getPreferences().getUserFavouriteQuestion());
			mAnswerEv.setText(ApplicationLoader.getPreferences().getUserFavouriteAnswer());
			
			final String mProfileImagePath = Utilities.getFilePath(AppConstants.TYPE.PROFILE, false, Utilities.getFileName(ApplicationLoader.getPreferences().getUserProfileImageLink()));
			if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getUserProfileImageLink())){
				ApplicationLoader.getPreferences().setUserProfileImagePath(mProfileImagePath);
				if(Utilities.checkIfFileExists(mProfileImagePath)){
					mProfileCirleIv.setImageURI(Uri.parse(mProfileImagePath));
				}else{
					mImageLoader = ApplicationLoader.getUILImageLoader();
					mImageLoader.displayImage(ApplicationLoader.getPreferences().getUserProfileImageLink(), mProfileCirleIv, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							mProgressWheel.setVisibility(View.VISIBLE);
							mProfileCirleIv.setVisibility(View.GONE);
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							mProgressWheel.setVisibility(View.GONE);
							mProfileCirleIv.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							mProgressWheel.setVisibility(View.GONE);
							mProfileCirleIv.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							mProgressWheel.setVisibility(View.GONE);
							mProfileCirleIv.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void setClickListener(){
		mUploadAnotherIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPhotoFromGallery();
			}
		});
		
		mDOBEv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				showDialog(0);
			}
		});
		
		mUpdateBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Utilities.isInternetConnected()) {
					if(isValid){
						if (AndroidUtilities.isAboveIceCreamSandWich()) {
							new AsyncUpdateProfileTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
						} else {
							new AsyncUpdateProfileTask().execute();
						}
					}
				} else {
					Utilities.showCrouton(EditProfileActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
				}
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
					ThemeUtils.getInstance(EditProfileActivity.this).applyThemeEditText(EditProfileActivity.this, EditProfileActivity.this, mNameLayout);
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
					ThemeUtils.getInstance(EditProfileActivity.this).applyThemeEditText(EditProfileActivity.this, EditProfileActivity.this, mEmailLayout);

				} else {
					mEmailLayout.setBackgroundDrawable(getResources()
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
					ThemeUtils.getInstance(EditProfileActivity.this).applyThemeEditText(EditProfileActivity.this, EditProfileActivity.this, mEmployeeIdLayout);
				} else {
					mEmployeeIdLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mQuestionEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(EditProfileActivity.this).applyThemeEditText(EditProfileActivity.this, EditProfileActivity.this, mQuestionLayout);
				} else {
					mQuestionLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mAnswerEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(EditProfileActivity.this).applyThemeEditText(EditProfileActivity.this, EditProfileActivity.this, mAnswerLayout);
				} else {
					mAnswerLayout.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
		
		mDOBEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ThemeUtils.getInstance(EditProfileActivity.this).applyThemeEditText(EditProfileActivity.this, EditProfileActivity.this, mDOBLayout);
					final Calendar c = Calendar.getInstance();
					mYear = c.get(Calendar.YEAR);
					mMonth = c.get(Calendar.MONTH);
					mDay = c.get(Calendar.DAY_OF_MONTH);
					showDialog(0);
				} else {
					mDOBLayout.setBackgroundDrawable(getResources()
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
		
		mQuestionEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateFavouriteQuestion(mCharsequence);
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
		
		mAnswerEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateFavouriteAnswer(mCharsequence);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
		   // set date picker as current date
			return new DatePickerDialog(this, datePickerListener, mYear,
					mMonth, mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener 
                = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;
			// set selected date into textview
			mDOBEv.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
			// set selected date into datepicker also

		}
	};
	
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
	
	private void validateEmployeeId(CharSequence mCharsequence){
		mEmployeeValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher matcher = p.matcher(mCharsequence.toString());
			if (matcher.matches()) {
				isValidEmployeeId =  true;
				mEmployeeValidateIv.setImageResource(R.drawable.ic_text_correct);
			}else{
				isValidEmployeeId = false;
				mEmployeeValidateIv.setImageResource(R.drawable.ic_text_incorrect);
			}
		}else{
			isValidEmployeeId = false;
			mEmployeeValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validateFavouriteQuestion(CharSequence mCharsequence){
		mQuestionValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mQuestionValidateIv.setImageResource(R.drawable.ic_text_correct);
		}else{
			mQuestionValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	private void validateFavouriteAnswer(CharSequence mCharsequence){
		mAnswerValidateIv.setImageResource(R.drawable.ic_text_process);
		if (!TextUtils.isEmpty(mCharsequence.toString())) {
			mAnswerValidateIv.setImageResource(R.drawable.ic_text_correct);
		}else{
			mAnswerValidateIv.setImageResource(R.drawable.ic_text_incorrect);
		}
		setUiOfNextAccordingly();
	}
	
	
	@SuppressWarnings("deprecation")
	private void setUiOfNextAccordingly(){
		if(isValidName && isValidEmail){
			isValid = true;
			ThemeUtils.getInstance(EditProfileActivity.this).applyThemeButton(EditProfileActivity.this, EditProfileActivity.this, mUpdateBtn);
		}else{
			isValid = false;
			mUpdateBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_normal));
		}
	}
	
	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mUpdateBtn);
			setMaterialRippleWithGrayOnView(mUploadAnotherIv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
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
	
	private String apiUpdateProfile(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostUserProfile(mNameEv
					.getText().toString(), mEmailEv.getText().toString(),
					mEmployeeIdEv.getText().toString(), mPicturePath, mQuestionEv.getText().toString(), mAnswerEv.getText().toString()," ");
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
			String mFavouriteQuestion = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.favouriteQuestion);
			String mFavouriteAnswer = mJSONObjectUser.getString(AppConstants.API_KEY_PARAMETER.favouriteAnswer);
			
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
			

			if(!TextUtils.isEmpty(mFavouriteAnswer)){
				ApplicationLoader.getPreferences().setUserFavouriteAnswer(mFavouriteAnswer);
			}
			
			if(!TextUtils.isEmpty(mFavouriteQuestion)){
				ApplicationLoader.getPreferences().setUserFavouriteQuestion(mFavouriteQuestion);
			}
			
			Utilities.showCrouton(EditProfileActivity.this, mCroutonViewGroup, Utilities.getSuccessMessageFromApi(mResponseFromApi), Style.CONFIRM);
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
			mProgressDialog = new MobcastProgressDialog(EditProfileActivity.this);
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
				Utilities.showCrouton(EditProfileActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
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

