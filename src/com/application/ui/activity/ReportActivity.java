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
public class ReportActivity extends SwipeBackBaseActivity {
	private static final String TAG = ReportActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private LinearLayout mReportCategoryLayout;
	
	private AppCompatTextView mReportCategoryTitleTv;
	private AppCompatTextView mReportCategorySelectedTv;
	
	private CircleImageView mReportProfileCircleImageView;
	
	private AppCompatEditText mReportEditText;
	
	private AppCompatCheckBox mReportIncludeLogsCheckBox;
	
	private AppCompatButton mReportSubmitBtn;

	
	private boolean isValidSubmit = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
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

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_event_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(ReportActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mReportCategoryTitleTv =(AppCompatTextView)findViewById(R.id.fragmentReportCategoryTv);
		mReportCategorySelectedTv =(AppCompatTextView)findViewById(R.id.fragmentReportCategorySelectedTv);
		
		mReportProfileCircleImageView = (CircleImageView)findViewById(R.id.fragmentReportTextProfileIv);
		
		mReportEditText = (AppCompatEditText)findViewById(R.id.fragmentReportTextEt);
		
		mReportIncludeLogsCheckBox = (AppCompatCheckBox)findViewById(R.id.fragmentReportIncludeLogsCheckBox);
		
		mReportSubmitBtn = (AppCompatButton)findViewById(R.id.fragmentReportSubmitBtn);
		
		mReportCategoryLayout = (LinearLayout)findViewById(R.id.fragmentReportCategoryLayout);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.ReportActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation(){
		try{
			YoYo.with(Techniques.ZoomIn).duration(500).playOn(mReportProfileCircleImageView);
		}catch(Exception e){
			Log.i(TAG, e.toString());
		}
	}
	
	private void setUiListener() {
		setOnClickListener();
		setTextWatcher();
		setMaterialRippleView();
	}
	
	private void setOnClickListener(){
		mReportCategoryLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(ReportActivity.this, SimpleRecyclerItemActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORYARRAY, getResources().getStringArray(R.array.report_category_array));
				startActivityForResult(mIntent, AppConstants.INTENT.INTENT_CATEGORY);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppConstants.INTENT.INTENT_CATEGORY && resultCode == Activity.RESULT_OK){
			mReportCategorySelectedTv.setText(data.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY));
		}
	}
	
	private void setTextWatcher(){
		mReportEditText.addTextChangedListener(new TextWatcher() {
			
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
	
	private void validateSubmit(CharSequence mCharsequence){
		if(TextUtils.isEmpty(mCharsequence.toString())){
			isValidSubmit = false;
		}else{
			isValidSubmit = true;
		}
		setUiOfSubmitAccordingly();
	}
	
	@SuppressWarnings("deprecation")
	private void setUiOfSubmitAccordingly(){
		if(isValidSubmit){
			mReportSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_pressed));
		}else{
			mReportSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_button_normal));
		}
	}
	

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mReportSubmitBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
