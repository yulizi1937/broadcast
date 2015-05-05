/**
 * 
 */
package com.application.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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

import com.application.ui.view.CircleImageView;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.BuildVars;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class SetProfileActivity extends AppCompatActivity{
	private static final String TAG = SetProfileActivity.class.getSimpleName();
	
	private static final int INTENT_PHOTO = 1001;
	
	private AppCompatTextView mUploadAnotherTv;
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
	
	private LinearLayout mNameLayout;
	private LinearLayout mEmailLayout;
	private LinearLayout mEmployeeIdLayout;
	
	private FrameLayout mCroutonViewGroup;
	
	private ProgressWheel mProgressWheel;
	private CircleImageView mProfileCirleIv;
	
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
	}

	private void initUi(){
		mUploadAnotherTv = (AppCompatTextView)findViewById(R.id.activitySetProfileUploadAnotherTv);
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
	
	private void setClickListener(){
		mUploadAnotherTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPhotoFromGallery();
			}
		});
		
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utilities.showCrouton(SetProfileActivity.this,mCroutonViewGroup, "Crouton", Style.CONFIRM);
				Intent mIntent = new Intent(SetProfileActivity.this, MotherActivity.class);
				startActivity(mIntent);
			}
		});
		
		mSkipTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(SetProfileActivity.this, FacebookConcealActivity.class);
				startActivity(mIntent);
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
			Pattern p = Pattern.compile("[A-Za-z]+");
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
			final String mPicturePath = Utilities.getPath(selectedImage);
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
}
