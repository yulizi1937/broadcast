/**
 * 
 */
package com.application.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import com.application.ui.view.MaterialRippleLayout;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.MobcastConfig;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class VerificationActivity extends AppCompatActivity {
	private static final String TAG = VerificationActivity.class.getSimpleName();

	private AppCompatEditText mVerificationCode1;
	private AppCompatEditText mVerificationCode2;
	private AppCompatEditText mVerificationCode3;
	private AppCompatEditText mVerificationCode4;
	
	private AppCompatTextView mTimerTv;
	
	private TextView mToolBarTitleTv;
	
	private AppCompatButton mNextBtn;
	private AppCompatButton mReSendBtn;
	
	
	private ImageView mToolBarDrawer;

	private FrameLayout mCroutonViewGroup;
	
	private boolean isValidVerificationCode = false;
	
	private String OTP;
	
	private long mStartTime = 0L;
	private long mTimeInMilliSeconds = 0L;
	private long mTimeSwapBuff = 0L;
	private long mUpdatedTime = 0L;
	
	private Handler mHandler = new Handler();
	

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		setSecurity();
		initUi();
		initToolBar();
		setUiListener();
		initTimer();
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


	private void initUi() {
		mVerificationCode1 = (AppCompatEditText) findViewById(R.id.activityVerificationCode1);
		mVerificationCode2 = (AppCompatEditText) findViewById(R.id.activityVerificationCode2);
		mVerificationCode3 = (AppCompatEditText) findViewById(R.id.activityVerificationCode3);
		mVerificationCode4 = (AppCompatEditText) findViewById(R.id.activityVerificationCode4);

		mNextBtn = (AppCompatButton) findViewById(R.id.activityVerificationBtn);
		mReSendBtn = (AppCompatButton)findViewById(R.id.activityVerificationResendBtn);
		
		mTimerTv = (AppCompatTextView)findViewById(R.id.activityVerificationRetryTv);

		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
	}

	private void initToolBar() {
		setSupportActionBar((Toolbar) findViewById(R.id.toolbarLayout));
		mToolBarTitleTv = (TextView)findViewById(R.id.toolbarTitleTv);
    	mToolBarDrawer = (ImageView)findViewById(R.id.toolbarBackIv);
    	
    	mToolBarDrawer.setVisibility(View.GONE);
    	mToolBarTitleTv.setText(getResources().getString(R.string.VerificationActivityTitle));
	}
	
	private void initTimer(){
		mStartTime = SystemClock.uptimeMillis();
		mHandler.postDelayed(updateTimerThread, 1000);
	}
	
	private void setUiListener() {
		setFocusListener();
		setTextWatcher();
		setMaterialRippleView();
		setClickListener();
	}
	
	private void setFocusListener(){
		onFocusListener(mVerificationCode1);
		onFocusListener(mVerificationCode2);
		onFocusListener(mVerificationCode3);
		onFocusListener(mVerificationCode4);
	}
	
	private void setTextWatcher(){
		onTextWatcher(mVerificationCode1);
		onTextWatcher(mVerificationCode2);
		onTextWatcher(mVerificationCode3);
		onTextWatcher(mVerificationCode4);
	}
	
	private void onTextWatcher(final AppCompatEditText mAppCompatEditText){
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
			mTimeInMilliSeconds = SystemClock.uptimeMillis() - mStartTime;
			mUpdatedTime = mTimeSwapBuff + mTimeInMilliSeconds;
			int secs = (int)mUpdatedTime/1000;
			Log.i(TAG, String.valueOf(secs));
			if(secs > MobcastConfig.BUILD.VERIFICATION_TIMER_OUT){
				mHandler.removeCallbacks(updateTimerThread);
				mTimerTv.setVisibility(View.GONE);
				mReSendBtn.setVisibility(View.VISIBLE);
			}
			int mins = secs/60;
			secs %=60;
			mTimerTv.setText(""+mins+":"+String.format("%02d", secs));
			mHandler.postDelayed(this, 1000);
		}
	};
	
	
	private void validateVerificationCodeBoxes(AppCompatEditText mAppCompatEditText){
		if(!TextUtils.isEmpty(mVerificationCode1.getText().toString())
				&&!TextUtils.isEmpty(mVerificationCode2.getText().toString())
				&&!TextUtils.isEmpty(mVerificationCode3.getText().toString())
				&&!TextUtils.isEmpty(mVerificationCode4.getText().toString())
				){
			isValidVerificationCode = true;
			setUiOfVerifyAccordingly();
		}
		if(mAppCompatEditText.getId() == R.id.activityVerificationCode1){
			mVerificationCode2.requestFocus();
		}else if(mAppCompatEditText.getId() == R.id.activityVerificationCode2){
			mVerificationCode3.requestFocus();
		}else if(mAppCompatEditText.getId() == R.id.activityVerificationCode3){
			mVerificationCode4.requestFocus();
		}else if(mAppCompatEditText.getId() == R.id.activityVerificationCode4){
			mNextBtn.performClick();
		}
	}
	
	private void setSMSRadarService(){
		SmsRadar.initializeSmsRadarService(ApplicationLoader.getApplication(), new SmsListener() {
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
	
	private void showSmsToast(Sms sms){
//		Utilities.showCrouton(VerificationActivity.this, mCroutonViewGroup, sms.toString(), Style.INFO);
		if(!TextUtils.isEmpty(sms.toString())){
			OTP = sms.getMsg();
			if(!TextUtils.isEmpty(OTP)){
				if(OTP.contains("mobcast")){
					OTP = OTP.substring(OTP.length()-5, OTP.length()-1);
					mVerificationCode1.setText(String.valueOf(OTP.charAt(0)));
					mVerificationCode2.setText(String.valueOf(OTP.charAt(1)));
					mVerificationCode3.setText(String.valueOf(OTP.charAt(2)));
					mVerificationCode4.setText(String.valueOf(OTP.charAt(3)));
					if(!TextUtils.isEmpty(mVerificationCode1.getText().toString())
							&& !TextUtils.isEmpty(mVerificationCode2.getText().toString())
							&& !TextUtils.isEmpty(mVerificationCode3.getText().toString())
							&& !TextUtils.isEmpty(mVerificationCode4.getText().toString())){
						isValidVerificationCode = true;
						setUiOfVerifyAccordingly();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setUiOfVerifyAccordingly(){
		if(isValidVerificationCode){
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_pressed));
		}else{
			mNextBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_normal));
		}
	}
	
	private void onFocusListener(final View view){
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					view.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_selected));
				} else {
					view.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.shape_editbox_normal));
				}
			}
		});
	}

	
	private void setClickListener(){
		mNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mVerificationCode1.getText().toString())
						&& !TextUtils.isEmpty(mVerificationCode2.getText().toString())
						&& !TextUtils.isEmpty(mVerificationCode3.getText().toString())
						&& !TextUtils.isEmpty(mVerificationCode4.getText().toString())
						){
					Intent mIntent = new Intent(VerificationActivity.this, SetProfileActivity.class);
					startActivity(mIntent);	
				}else{
					Utilities.showCrouton(VerificationActivity.this, mCroutonViewGroup, getResources().getString(R.string.validate_verfication_code_empty), Style.ALERT);
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
	
}
