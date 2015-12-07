/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.beans.Parichay;
import com.application.sqlite.DBConstant;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.FABObservableScrollView;
import com.application.ui.view.FloatingActionButton;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.AppPreferences;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ParichayDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = ParichayDetailActivity.class.getSimpleName();
	
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private FloatingActionButton mFloatActionButton;
	private FABObservableScrollView mObservableScrollView;
	
	private AppCompatTextView mJobTitleTv;
	private AppCompatTextView mJobDesignationTv;
	private AppCompatTextView mJobLocationTv;
	private AppCompatTextView mJobExperienceTv;
	private AppCompatTextView mJobSkillsTv;
	private AppCompatTextView mJobDescriptionTv;
	private AppCompatTextView mJobDesiredProfileTv;
	private AppCompatTextView mJobQualificationTv;
	private AppCompatTextView mJobCTCTv;
	
	private ImageView mJobTitleIv;
	private ImageView mJobSkillsIv;
	private ImageView mJobDescriptionIv;
	private ImageView mJobQualificationIv;
	private ImageView mJobCTCIv;
	private ImageView mJobDesiredProfileIv;
	private ImageView mJobExperienceIv;
	
	private FrameLayout mJobTitleLayout;
	private FrameLayout mJobSkillsLayout;
	private FrameLayout mJobDescriptionLayout;
	private FrameLayout mJobQualificationLayout;
	private FrameLayout mJobCTCLayout;
	private FrameLayout mJobDesiredProfileLayout;
	private FrameLayout mJobExperienceLayout;
	
	private LinearLayout mJobTitleDetailLayout;
	
	private Intent mIntent;
	private String mId;
	private String mCategory = AppConstants.INTENTCONSTANTS.PARICHAY;
	private String mJobTitle;
	private String mJobDesiredProfile;
	private String mJobLocation;
	private String mJobUnit;
	private String mJobExperience;
	private String mJobSkills;
	private String mJobDescription;
	private String mJobCTC;
	private String mJobQualification;
	private String mJobHQ;
	private String mJobRegion;
	private String mJobDivison;
	private String mInstallment;
	
	private String mContentLikeCount;
	private boolean isContentLiked = false;
	
	private Parichay mParichay;
	
	private boolean isJobTitleExpanded = true;
	private boolean isJobSkillsExpanded = true;
	private boolean isDesiredProfileExpanded = true;
	private boolean isJobDescExpanded = false;
	private boolean isJobQualificationExpanded = false;
	private boolean isJobCTCExpanded = false;
	private boolean isJobExperienceExpanded = false;
	
	private int whichTheme = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parichay_detail);
		setSecurity();
		initToolBar();
		initUi();
		getIntentData();
		setUiListener();
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		try{
			if (isContentLiked) {
				menu.findItem(R.id.action_like).setIcon(R.drawable.ic_liked);
			} else {
				menu.findItem(R.id.action_like).setIcon(R.drawable.ic_like);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return super.onPrepareOptionsPanel(view, menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_parichay_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(ParichayDetailActivity.this);
			return true;
		case R.id.action_like:
			actionLike();
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(ParichayDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Parichay");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ParichayDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mJobTitleLayout = (FrameLayout)findViewById(R.id.activityParichayDetailExpandLayout);
		mJobSkillsLayout = (FrameLayout)findViewById(R.id.activityParichayDetailSkillsExpandLayout);
		mJobDescriptionLayout = (FrameLayout)findViewById(R.id.activityParichayDetailJobDescriptionExpandLayout);
		mJobQualificationLayout = (FrameLayout)findViewById(R.id.activityParichayDetailQualificationExpandLayout);
		mJobCTCLayout = (FrameLayout)findViewById(R.id.activityParichayDetailCTCExpandLayout);
		mJobDesiredProfileLayout = (FrameLayout)findViewById(R.id.activityParichayDetailDesiredProfileExpandLayout);
		mJobExperienceLayout = (FrameLayout)findViewById(R.id.activityParichayDetailMinExperienceExpandLayout);
		
		mJobTitleDetailLayout = (LinearLayout)findViewById(R.id.activityParichayDetailLayout);
		
		mJobTitleTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailTitleTv);
		mJobDesignationTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailDesignationTv);
	    mJobLocationTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailLocationTv);
	    mJobExperienceTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailExperienceTv);
	    mJobSkillsTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailSkillsTv);
	    mJobDescriptionTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailJobDescriptionTv);
	    mJobQualificationTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailQualificationTv);
	    mJobCTCTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailCTCTv);
	    mJobDesiredProfileTv = (AppCompatTextView)findViewById(R.id.activityParichayDetailDesiredProfileTv);
				
		mJobTitleIv = (ImageView)findViewById(R.id.activityParichayDetailIv);
		mJobSkillsIv = (ImageView)findViewById(R.id.activityParichayDetailSkillsIv);
		mJobDescriptionIv = (ImageView)findViewById(R.id.activityParichayDetailJobDescriptionIv);
		mJobQualificationIv = (ImageView)findViewById(R.id.activityParichayDetailQualificationIv);
		mJobCTCIv = (ImageView)findViewById(R.id.activityParichayDetailCTCIv);
		mJobDesiredProfileIv = (ImageView)findViewById(R.id.activityParichayDetailDesiredProfileIv);
		mJobExperienceIv = (ImageView)findViewById(R.id.activityParichayDetailMinExperienceIv);
		
		mFloatActionButton = (FloatingActionButton) findViewById(R.id.activityParichayDetailFAB);
		mObservableScrollView = (FABObservableScrollView)findViewById(R.id.fragmentParichayDetailObservableScrollView);
	}
	
	private void attachFABToScrollView(){
		try{
			mFloatActionButton.attachToScrollView(mObservableScrollView);
			ThemeUtils.applyThemeFAB(whichTheme, mFloatActionButton);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		try{
			if(mIntent!=null){
				int mPosition = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.POSITION, 0);
				ArrayList<Parichay> mList = mIntent.getParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT);
				mParichay = mList.get(mPosition);
				if(mParichay!=null){
						getDataFromIntentObject();
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(ParichayDetailActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(ParichayDetailActivity.this);
		}
	}
	
	private void getDataFromIntentObject(){
		try{
			mId = mParichay.getmId();
			mJobTitle = mParichay.getmJobPosition();
			mJobDesiredProfile = mParichay.getmJobDesiredProfile();
			mJobUnit = mParichay.getmJobUnit();
			mJobLocation = mParichay.getmJobLoc();
			mJobExperience = mParichay.getmJobExp();
			mJobSkills = mParichay.getmJobAgeLimit();
			mJobDescription =  mParichay.getmJobDesc();
			mJobQualification = mParichay.getmJobQualif();
			isContentLiked = mParichay.isLike();
			mContentLikeCount = mParichay.getmLikeCount();
			mJobHQ = mParichay.getmJobHQ();
			mJobDivison = mParichay.getmJobDivision();
			mJobRegion = mParichay.getmJobRegion();
			mInstallment = mParichay.getmInstall();
			setIntentDataToUi();			
			
			if(isContentLiked){
				ApplicationLoader.getPreferences().setLikeIdParichay(mId);
			}else{
				ApplicationLoader.getPreferences().setLikeIdParichay("-1");
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setIntentDataToUi(){
		try{
			mJobTitleTv.setText(mJobTitle);
			mJobDesignationTv.setText(mJobUnit);
			if(!TextUtils.isEmpty(mJobDescription)){
				mJobDescriptionTv.setText(mJobDescription);	
			}
			if(!TextUtils.isEmpty(mJobLocation)){
				mJobLocationTv.setText(mJobLocation);
			}
			if(!TextUtils.isEmpty(mJobExperience)){
				mJobExperienceTv.setText(mJobExperience);
			}
			if(!TextUtils.isEmpty(mJobSkills)){
				mJobSkillsTv.setText(mJobSkills);
			}
			if(!TextUtils.isEmpty(mJobQualification)){
				mJobQualificationTv.setText(mJobQualification);
			}
			if(!TextUtils.isEmpty(mJobCTC)){
				mJobCTCTv.setText(mJobCTC);
			}
			
			if(!TextUtils.isEmpty(mJobDesiredProfile)){
				mJobDesiredProfileTv.setText(mJobDesiredProfile);
			}
			supportInvalidateOptionsMenu();
			mParichay.setRead(true);
			UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.ParichayDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setToolBarOption();
		setClickListener();
	}
	
	private void applyTheme(){
		try{
			whichTheme = ApplicationLoader.getPreferences().getAppTheme();
			ThemeUtils.getInstance(ParichayDetailActivity.this).applyThemeWithBy(ParichayDetailActivity.this, ParichayDetailActivity.this, mToolBar,mJobDesignationTv);
			attachFABToScrollView();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setClickListener(){
		mJobTitleLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobTitleExpanded){
					AndroidUtilities.expand(mJobTitleDetailLayout);
					mJobTitleIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobTitleDetailLayout);
					mJobTitleIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobTitleExpanded = !isJobTitleExpanded;
			}
		});
		
		mJobSkillsLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobSkillsExpanded){
					AndroidUtilities.expand(mJobSkillsTv);
					mJobSkillsIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobSkillsTv);
					mJobSkillsIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobSkillsExpanded = !isJobSkillsExpanded;
			}
		});
		
		mJobDescriptionLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobDescExpanded){
					AndroidUtilities.expand(mJobDescriptionTv);
					mJobDescriptionIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobDescriptionTv);
					mJobDescriptionIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobDescExpanded = !isJobDescExpanded;
			}
		});
		
		mJobQualificationLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobQualificationExpanded){
					AndroidUtilities.expand(mJobQualificationTv);
					mJobQualificationIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobQualificationTv);
					mJobQualificationIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobQualificationExpanded = !isJobQualificationExpanded;
			}
		});
		
		mJobCTCLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobCTCExpanded){
					AndroidUtilities.expand(mJobCTCTv);
					mJobCTCIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobCTCTv);
					mJobCTCIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobCTCExpanded = !isJobCTCExpanded;
			}
		});
		
		mJobDesiredProfileLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(isDesiredProfileExpanded){
					AndroidUtilities.expand(mJobDesiredProfileTv);
					mJobDesiredProfileIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobDesiredProfileTv);
					mJobDesiredProfileIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isDesiredProfileExpanded = !isDesiredProfileExpanded;
			}
		});
		
		mJobExperienceLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!isJobExperienceExpanded){
					AndroidUtilities.expand(mJobExperienceTv);
					mJobExperienceIv.setImageResource(R.drawable.ic_recruitment_collapse);
				}else{
					AndroidUtilities.collapse(mJobExperienceTv);
					mJobExperienceIv.setImageResource(R.drawable.ic_recruitment_expand);
				}
				isJobExperienceExpanded = !isJobExperienceExpanded;
			}
		});
		
		mFloatActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(ParichayDetailActivity.this, ParichayReferralFormActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE, mJobTitle);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mJobUnit);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.HQ, mJobHQ);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.REGION, mJobRegion);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.DIVISION, mJobDivison);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.INSTALLMENT, mInstallment);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(ParichayDetailActivity.this);
			}
		});
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleWithGrayOnView(mJobTitleLayout);
			setMaterialRippleWithGrayOnView(mJobSkillsLayout);
			setMaterialRippleWithGrayOnView(mJobDescriptionLayout);
			setMaterialRippleWithGrayOnView(mJobQualificationLayout);
			setMaterialRippleWithGrayOnView(mJobCTCLayout);
			setMaterialRippleWithGrayOnView(mJobExperienceLayout);
			setMaterialRippleWithGrayOnView(mJobDesiredProfileLayout);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void actionLike(){
		try{
			if(!isContentLiked){
				mContentLikeCount  = String.valueOf(Integer.parseInt(mContentLikeCount)+1);
				if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
					ContentValues values = new ContentValues();
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, "true");
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mContentLikeCount);
					getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
				}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
					ContentValues values = new ContentValues();
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, "true");
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mContentLikeCount);
					getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
				}
				isContentLiked = true;
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LIKE, "");
				ApplicationLoader.getPreferences().setLikeIdParichay(mId);
			}else{
				if(isContentLiked){
					mContentLikeCount = String.valueOf(Integer.parseInt(mContentLikeCount)-1);
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
						ContentValues values = new ContentValues();
						values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, "false");
						values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mContentLikeCount);
						getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});
					}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
						ContentValues values = new ContentValues();
						values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, "false");
						values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mContentLikeCount);
						getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
					}
					isContentLiked = false;
					UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.UNLIKE, "");
					ApplicationLoader.getPreferences().setLikeIdParichay("-1");
				}
			}
			supportInvalidateOptionsMenu();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
