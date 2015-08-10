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
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class QuizScoreActivity extends SwipeBackBaseActivity {
	private static final String TAG = QuizScoreActivity.class.getSimpleName();
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_score);
		initToolBar();
		initUi();
		setUiListener();
		getIntentData();
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
		inflater.inflate(R.menu.menu_quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(QuizScoreActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

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
	
	private void getIntentData(){
		mIntent = getIntent();
		mTimeTaken = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.TIMETAKEN);
		mTotalPoints = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.POINTS);
		mArrayListQuizScorePagerInfo = mIntent.getParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.QUIZINCORRECT);
		setIntentDataToUi();
	}
	
	private void setIntentDataToUi(){
		
		if(!TextUtils.isEmpty(mTotalPoints)){
			mQuizScoreTv.setText(mTotalPoints);
		}
		
		if(!TextUtils.isEmpty(mTimeTaken)){
			String[] mTime = Utilities.convertTimeFromSecsTo(Long.parseLong(mTimeTaken)).split(" ");
			mQuizScoreTimeTakenTv.setText(mTime[0]);
			mQuizScoreTimeTakenTextTv.setText(mTime[1]);
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
	}

	@SuppressWarnings("deprecation")
	private void setQuizViewPager() {
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

}
