/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import com.application.ui.view.CircleImageView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mobcast.R;

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

	private boolean isValidSubmit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_feedback);
		initToolBar();
		initUi();
		setUiListener();
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
			YoYo.with(Techniques.ZoomIn).duration(500)
					.playOn(mAppFeedbackProfileCircleImageView);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setOnClickListener();
		setTextWatcher();
		setMaterialRippleView();
	}

	private void setOnClickListener() {
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
}
