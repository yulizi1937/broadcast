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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.application.beans.FeedbackPagerInfo;
import com.application.ui.adapter.FeedbackViewPagerAdapter;
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
public class FeedbackActivity extends SwipeBackBaseActivity {
	private static final String TAG = FeedbackActivity.class.getSimpleName();
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mFeedbackQuestionHeaderTv;
	private AppCompatTextView mFeedbackQuestionPagerCounterTv;

	private AppCompatButton mFeedbackNavigationNextBtn;
	private AppCompatButton mFeedbackNavigationPrevBtn;

	private CirclePageIndicator mFeedbackCirclePageIndicator;

	private ViewPager mFeedbackViewPager;

	private FeedbackViewPagerAdapter mAdapter;

	private ViewPager.OnPageChangeListener mPagerListener;

	private ArrayList<FeedbackPagerInfo> mArrayListFeedbackPagerInfo;

	private boolean isCirclePagerIndicatorEnable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initToolBar();
		initUi();
		setUiListener();
		setFeedbackViewPager();
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
			AndroidUtilities.exitWindowAnimation(FeedbackActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(FeedbackActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Feedback");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(FeedbackActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mFeedbackQuestionHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentFeedbackQuestionNumberTextTv);
		mFeedbackQuestionPagerCounterTv = (AppCompatTextView) findViewById(R.id.fragmentFeedbackQuestionPageCountTv);

		mFeedbackNavigationNextBtn = (AppCompatButton) findViewById(R.id.fragmentFeedbackNextBtn);
		mFeedbackNavigationPrevBtn = (AppCompatButton) findViewById(R.id.fragmentFeedbackPreviousBtn);

		mFeedbackViewPager = (ViewPager) findViewById(R.id.fragmentFeedbackQuestionViewPager);

		mFeedbackCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentFeedbackQuestionCirclePageIndicator);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.FeedbackActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}

	private void setFeedbackViewPager() {
		mArrayListFeedbackPagerInfo = new ArrayList<FeedbackPagerInfo>();
		for (int i = 0; i < 5; i++) {
			FeedbackPagerInfo Obj = new FeedbackPagerInfo();
			Obj.setmFeedbackId("1");
			if (i == 0) {
				Obj.setmFeedbackType("subjective");
			} else if (i == 1) {
				Obj.setmFeedbackType("selective");
			} else if (i == 2) {
				Obj.setmFeedbackType("multiple");
			} else if (i == 3) {
				Obj.setmFeedbackType("selective");
			} else {
				Obj.setmFeedbackType("rating");
			}
			mArrayListFeedbackPagerInfo.add(Obj);
		}
		if (mArrayListFeedbackPagerInfo.size() < 7) {
			isCirclePagerIndicatorEnable = true;
		}
		mAdapter = new FeedbackViewPagerAdapter(getSupportFragmentManager(),
				mArrayListFeedbackPagerInfo);
		setViewPagerListener();
		mFeedbackViewPager.setAdapter(mAdapter);
		if (isCirclePagerIndicatorEnable) {
			mFeedbackCirclePageIndicator.setViewPager(mFeedbackViewPager);
			mFeedbackCirclePageIndicator
					.setOnPageChangeListener(mPagerListener);
		} else {
			mFeedbackQuestionPagerCounterTv.setVisibility(View.VISIBLE);
			mFeedbackCirclePageIndicator.setVisibility(View.GONE);
			mFeedbackViewPager.setOnPageChangeListener(mPagerListener);
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
		mFeedbackQuestionHeaderTv.setText(getResources().getString(
				R.string.sample_question_header_box)
				+ " " + mNumberQuestion);
		if (!isCirclePagerIndicatorEnable) {
			mFeedbackQuestionPagerCounterTv.setText(mNumberQuestion + " / "
					+ mArrayListFeedbackPagerInfo.size());
		}

		if (mNumberQuestion == mArrayListFeedbackPagerInfo.size()) {
			mFeedbackNavigationNextBtn.setText(getResources().getString(
					R.string.button_submit));
		} else {
			mFeedbackNavigationNextBtn.setText(getResources().getString(
					R.string.button_next));
		}

		if (mNumberQuestion == 1) {
			mFeedbackNavigationPrevBtn.setEnabled(false);
		} else {
			mFeedbackNavigationPrevBtn.setEnabled(true);
		}

		if (mArrayListFeedbackPagerInfo.size() == 1) {
			mFeedbackNavigationPrevBtn.setVisibility(View.INVISIBLE);
			mFeedbackNavigationNextBtn.setText(getResources().getString(
					R.string.button_submit));
		}
	}

	private void setOnClickListener() {
		mFeedbackNavigationNextBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if (mFeedbackNavigationNextBtn
								.getText()
								.toString()
								.equalsIgnoreCase(
										getResources().getString(
												R.string.button_next))) {
							changePagerTo(mFeedbackViewPager.getCurrentItem() + 1);
						} else if (mFeedbackNavigationNextBtn
								.getText()
								.toString()
								.equalsIgnoreCase(
										getResources().getString(
												R.string.button_submit))) {
							showFeedbackThankYouDialog();
						}
					}
				});

		mFeedbackNavigationPrevBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						changePagerTo(mFeedbackViewPager.getCurrentItem() - 1);
					}
				});
	}

	private void changePagerTo(int position) {
		mFeedbackViewPager.setCurrentItem(position, true);
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mFeedbackNavigationNextBtn);
			setMaterialRippleOnView(mFeedbackNavigationPrevBtn);
			setMaterialRippleOnView(mFeedbackQuestionHeaderTv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void showFeedbackThankYouDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(FeedbackActivity.this)
        .title(getResources().getString(R.string.fragment_feedback_thank_you_header))
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
}
