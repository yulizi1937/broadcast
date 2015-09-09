/**
 * 
 */
package com.application.ui.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.application.ui.activity.LoginActivity.AsyncLoginTask;
import com.application.ui.view.CircleImageView;
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
import com.application.utils.Utilities;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
public class FeedbackAppActivity extends SwipeBackBaseActivity {
	private static final String TAG = FeedbackAppActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mAppFeedbackCategoryTitleTv;
	private AppCompatTextView mAppFeedbackCategorySelectedTv;
	
	private LinearLayout mAppFeedbackCategoryLayout;

	private CircleImageView mAppFeedbackProfileCircleImageView;

	private AppCompatEditText mAppFeedbackEditText;

	private AppCompatCheckBox mAppFeedbackIncludeLogsCheckBox;

	private AppCompatButton mAppFeedbackSubmitBtn;

	private ImageLoader mImageLoader;
	
	private boolean isValidSubmit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_feedback);
		setSecurity();
		initToolBar();
		initUi();
		setUiListener();
		setDataFromPreferences();
		setAnimation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(FeedbackAppActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mAppFeedbackCategoryTitleTv = (AppCompatTextView) findViewById(R.id.fragmentAppFeedbackCategoryTv);
		mAppFeedbackCategorySelectedTv = (AppCompatTextView) findViewById(R.id.fragmentAppFeedbackCategorySelectedTv);
		
		mAppFeedbackCategoryLayout = (LinearLayout)findViewById(R.id.fragmentAppFeedbackCategoryLayout);
		
		mAppFeedbackProfileCircleImageView = (CircleImageView) findViewById(R.id.fragmentAppFeedbackTextProfileIv);

		mAppFeedbackEditText = (AppCompatEditText) findViewById(R.id.fragmentAppFeedbackTextEt);

		mAppFeedbackIncludeLogsCheckBox = (AppCompatCheckBox) findViewById(R.id.fragmentAppFeedbackIncludeLogsCheckBox);

		mAppFeedbackSubmitBtn = (AppCompatButton) findViewById(R.id.fragmentAppFeedbackSubmitBtn);

	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources()
				.getString(R.string.FeedbackAppActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.ZoomIn).duration(500).playOn(mAppFeedbackProfileCircleImageView);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setOnClickListener();
		setTextWatcher();
		setMaterialRippleView();
	}

	@SuppressLint("NewApi") private void setOnClickListener() {
		mAppFeedbackCategoryLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(FeedbackAppActivity.this, SimpleRecyclerItemActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORYARRAY, getResources().getStringArray(
						R.array.feedback_app_array));
				startActivityForResult(mIntent, AppConstants.INTENT.INTENT_CATEGORY);
				AndroidUtilities.enterWindowAnimation(FeedbackAppActivity.this);
			}
		});
		
		mAppFeedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
					if (Utilities.isInternetConnected()) {
						if(isValidSubmit){
							if (AndroidUtilities.isAboveIceCreamSandWich()) {
								new AsyncAppFeedbackTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
							} else {
								new AsyncAppFeedbackTask().execute();
							}
						}
					} else {
						Utilities.showCrouton(FeedbackAppActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
					}
				}
		});
	}
	
	private void setDataFromPreferences(){
		try{
			final String mProfileImagePath = Utilities.getFilePath(AppConstants.TYPE.PROFILE, false, Utilities.getFileName(ApplicationLoader.getPreferences().getUserProfileImageLink()));
			if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getUserProfileImageLink())){
				ApplicationLoader.getPreferences().setUserProfileImagePath(mProfileImagePath);
				if(Utilities.checkIfFileExists(mProfileImagePath)){
					mAppFeedbackProfileCircleImageView.setImageURI(Uri.parse(mProfileImagePath));
				}else{
					mImageLoader = ApplicationLoader.getUILImageLoader();
					mImageLoader.displayImage(ApplicationLoader.getPreferences().getUserProfileImageLink(), mAppFeedbackProfileCircleImageView, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							mAppFeedbackProfileCircleImageView.setVisibility(View.GONE);
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							mAppFeedbackProfileCircleImageView.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							mAppFeedbackProfileCircleImageView.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							mAppFeedbackProfileCircleImageView.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppConstants.INTENT.INTENT_CATEGORY && resultCode == Activity.RESULT_OK){
			mAppFeedbackCategorySelectedTv.setText(data.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY));
		}
	}
	
	private void setTextWatcher() {
		mAppFeedbackEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				validateSubmit(mCharsequence);
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

	private void validateSubmit(CharSequence mCharsequence) {
		if (TextUtils.isEmpty(mCharsequence.toString())) {
			isValidSubmit = false;
		} else {
			isValidSubmit = true;
		}
		setUiOfSubmitAccordingly();
	}

	@SuppressWarnings("deprecation")
	private void setUiOfSubmitAccordingly() {
		if (isValidSubmit) {
			mAppFeedbackSubmitBtn.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.shape_button_pressed));
		} else {
			mAppFeedbackSubmitBtn.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.shape_button_normal));
		}
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mAppFeedbackSubmitBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private String apiSubmitAppFeedback() {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostAppFeedbackData(mAppFeedbackCategorySelectedTv.getText().toString(), mAppFeedbackEditText.getText().toString());
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_APP_FEEDBACK, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_APP_FEEDBACK, jsonObj, TAG);	
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
				if(Utilities.isSuccessFromApi(mResponseFromApi)){
					Utilities.showCrouton(FeedbackAppActivity.this, mCroutonViewGroup, Utilities.getSuccessMessageFromApi(mResponseFromApi), Style.CONFIRM);
					mAppFeedbackEditText.setText("");
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public class AsyncAppFeedbackTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(FeedbackAppActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiSubmitAppFeedback();
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
				Utilities.showCrouton(FeedbackAppActivity.this, mCroutonViewGroup,
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
