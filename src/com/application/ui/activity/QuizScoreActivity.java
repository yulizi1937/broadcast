/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.application.beans.QuizScorePagerInfo;
import com.application.ui.adapter.QuizScoreViewPagerAdapter;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.CirclePageIndicator;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class QuizScoreActivity extends SwipeBackBaseActivity {
	private static final String TAG = QuizScoreActivity.class.getSimpleName();
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private FrameLayout mQuizScoreLayout;

	private AppCompatButton mQuizScoreNavigationNextBtn;
	private AppCompatButton mQuizScoreNavigationPrevBtn;

	private AppCompatTextView mQuizScoreTimeTakenTv;
	private AppCompatTextView mQuizScoreTimeTakenTextTv;
	private AppCompatTextView mQuizScoreTv;
	private AppCompatTextView mQuizScoreStatementTv;
	private AppCompatTextView mQuizScoreStatementCorrectAnswerTv;
	private AppCompatTextView mQuizScoreCorrectAnswerTv;
	private AppCompatTextView mQuizScoreGoofedUpAtTv;

	private AppCompatTextView mQuizScoreQuestionPagerCounterTv;

	private CirclePageIndicator mQuestionScoreCirclePageIndicator;

	private ViewPager mQuestionScoreViewPager;

	private QuizScoreViewPagerAdapter mAdapter;

	private ViewPager.OnPageChangeListener mPagerListener;

	private ArrayList<QuizScorePagerInfo> mArrayListQuizScorePagerInfo;

	private boolean isCirclePagerIndicatorEnable = false;
	
	private Intent mIntent;
	
	private String mTimeTaken;
	private String mTotalPoints;
	private String mOverallPoints;
	private String mTotalQuestions = "0";
	private String mIncorrectQuestions = "0";
	
	private boolean isFromNotification = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_score);
		setSecurity();
		initToolBar();
		initUi();
		setUiListener();
		getIntentData();
		applyTheme();
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
		inflater.inflate(R.menu.menu_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(QuizScoreActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(QuizScoreActivity.this, MotherActivity.class);
				startActivity(mIntent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mQuizScoreLayout = (FrameLayout) findViewById(R.id.fragmentQuizScoreRootLayout);

		mQuizScoreNavigationNextBtn = (AppCompatButton) findViewById(R.id.fragmentQuizScoreNextBtn);
		mQuizScoreNavigationPrevBtn = (AppCompatButton) findViewById(R.id.fragmentQuizScorePreviousBtn);

		mQuizScoreTimeTakenTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreTimeTakenTv);
		mQuizScoreTimeTakenTextTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreTimeTakenInTv);
		mQuizScoreTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreTotalScoreTv);
		mQuizScoreStatementTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreTotalScoreTextTv);
		mQuizScoreCorrectAnswerTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreCorrectAnswerTv);
		mQuizScoreStatementCorrectAnswerTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreCorrectAnswerTextTv);
		mQuizScoreGoofedUpAtTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreGoofedUpAtTv);

		mQuestionScoreViewPager = (ViewPager) findViewById(R.id.fragmentQuizScoreQuestionViewPager);

		mQuestionScoreCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentQuizScoreQuestionCirclePageIndicator);
		mQuizScoreQuestionPagerCounterTv = (AppCompatTextView) findViewById(R.id.fragmentQuizScoreQuestionPageCountTv);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(R.string.QuizActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(isFromNotification){
			Intent mIntent = new Intent(QuizScoreActivity.this, MotherActivity.class);
			startActivity(mIntent);
		}
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(QuizScoreActivity.this).applyThemeCountrySelect(QuizScoreActivity.this, QuizScoreActivity.this, mToolBar);
			ThemeUtils.getInstance(QuizScoreActivity.this).applyThemeSplash(QuizScoreActivity.this, QuizScoreActivity.this, mQuizScoreLayout);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		mTimeTaken = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.TIMETAKEN);
		mTotalPoints = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.POINTS);
		mOverallPoints = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.TOTALPOINTS);
		mTotalQuestions = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.TOTAL);
		mArrayListQuizScorePagerInfo = mIntent.getParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.QUIZINCORRECT);
		isFromNotification = mIntent.getBooleanExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, false);
		if(mArrayListQuizScorePagerInfo!=null && mArrayListQuizScorePagerInfo.size() > 0){
			mIncorrectQuestions = String.valueOf(Integer.parseInt(mTotalQuestions) - mArrayListQuizScorePagerInfo.size());	
		}
		setIntentDataToUi();
	}
	
	private void setIntentDataToUi(){
		try{
			if(!TextUtils.isEmpty(mTotalPoints)){
				mQuizScoreTv.setText(mTotalPoints + "/" + mOverallPoints);
			}
			
			if(!TextUtils.isEmpty(mTimeTaken)){
				String[] mTime = Utilities.convertTimeFromSecsTo(Long.parseLong(mTimeTaken)).split(" ");
				mQuizScoreTimeTakenTv.setText(mTime[0]);
				mQuizScoreTimeTakenTextTv.setText(mTime[1]);
			}
			
			if(mArrayListQuizScorePagerInfo!=null && mArrayListQuizScorePagerInfo.size() > 0){
				mQuizScoreCorrectAnswerTv.setText(mIncorrectQuestions + "/" + mTotalQuestions);
			}else{
				mQuizScoreCorrectAnswerTv.setText(mTotalQuestions+ "/" + mTotalQuestions);
			}
			
			
			if(mArrayListQuizScorePagerInfo!=null && mArrayListQuizScorePagerInfo.size() > 0){
				setQuizViewPager();	
			}else{
				mQuestionScoreViewPager.setVisibility(View.GONE);
				mQuizScoreNavigationPrevBtn.setVisibility(View.GONE);
				mQuizScoreGoofedUpAtTv.setVisibility(View.GONE);
				mQuizScoreNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@SuppressWarnings("deprecation")
	private void setQuizViewPager() {
		try{
			if (mArrayListQuizScorePagerInfo.size() < 7) {
				isCirclePagerIndicatorEnable = true;
			}
			mAdapter = new QuizScoreViewPagerAdapter(getSupportFragmentManager(),
					mArrayListQuizScorePagerInfo);
			setViewPagerListener();
			mQuestionScoreViewPager.setAdapter(mAdapter);
			if (isCirclePagerIndicatorEnable) {
				mQuestionScoreCirclePageIndicator
						.setViewPager(mQuestionScoreViewPager);
				mQuestionScoreCirclePageIndicator
						.setOnPageChangeListener(mPagerListener);
			} else {
				mQuizScoreQuestionPagerCounterTv.setVisibility(View.VISIBLE);
				mQuestionScoreCirclePageIndicator.setVisibility(View.GONE);
				mQuestionScoreViewPager.setOnPageChangeListener(mPagerListener);
				mQuizScoreQuestionPagerCounterTv.setText("1" + " / "+ mArrayListQuizScorePagerInfo.size());
			}
			
			if (mArrayListQuizScorePagerInfo.size() == 1) {
				mQuizScoreNavigationPrevBtn.setVisibility(View.GONE);
				mQuizScoreNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			}

		}catch(Exception e){
			FileLog.e(TAG, e.toString());	
		}
	}

	private void setViewPagerListener() {
		mPagerListener = new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				int mNumberQuestion = position + 1;
				uiOnChangeOfPagerListener(mNumberQuestion);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		};
	}

	private void uiOnChangeOfPagerListener(int mNumberQuestion) {
		try{
			if (!isCirclePagerIndicatorEnable) {
				mQuizScoreQuestionPagerCounterTv.setText(mNumberQuestion + " / "
						+ mArrayListQuizScorePagerInfo.size());
			}

			if (mNumberQuestion == mArrayListQuizScorePagerInfo.size()) {
				mQuizScoreNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			} else {
				mQuizScoreNavigationNextBtn.setText(getResources().getString(
						R.string.button_next));
			}

			if (mNumberQuestion == 1) {
				mQuizScoreNavigationPrevBtn.setEnabled(false);
			} else {
				mQuizScoreNavigationPrevBtn.setEnabled(true);
			}

			if (mArrayListQuizScorePagerInfo.size() == 1) {
				mQuizScoreNavigationPrevBtn.setVisibility(View.INVISIBLE);
				mQuizScoreNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void showQuizThankYouDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(QuizScoreActivity.this)
        .title(getResources().getString(R.string.fragment_quizscore_thank_you_header))
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            	finish();
            	AndroidUtilities.exitWindowAnimation(QuizScoreActivity.this);
            	if(isFromNotification){
        			Intent mIntent = new Intent(QuizScoreActivity.this, MotherActivity.class);
        			startActivity(mIntent);
        		}
            }
        })
        .show();
	}

	private void setOnClickListener() {
		mQuizScoreNavigationNextBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if (mQuizScoreNavigationNextBtn
								.getText()
								.toString()
								.equalsIgnoreCase(
										getResources().getString(
												R.string.button_next))) {
							changePagerTo(mQuestionScoreViewPager
									.getCurrentItem() + 1);
						} else if (mQuizScoreNavigationNextBtn
								.getText()
								.toString()
								.equalsIgnoreCase(
										getResources().getString(
												R.string.button_submit))) {
							showQuizThankYouDialog();
						}
					}
				});

		mQuizScoreNavigationPrevBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						changePagerTo(mQuestionScoreViewPager.getCurrentItem() - 1);
					}
				});
	}

	private void changePagerTo(int position) {
		mQuestionScoreViewPager.setCurrentItem(position, true);
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mQuizScoreNavigationNextBtn);
			setMaterialRippleOnView(mQuizScoreNavigationPrevBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
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
