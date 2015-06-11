/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;

import com.application.beans.QuizPagerInfo;
import com.application.ui.adapter.QuizViewPagerAdapter;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.ProgressTimerWheel;
import com.application.utils.AndroidUtilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class QuizActivity extends SwipeBackBaseActivity {
	private static final String TAG = QuizActivity.class.getSimpleName();
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private ImageView mQuizCoverIv;

	private ProgressTimerWheel mQuizProgressTimerWheel;

	private AppCompatTextView mQuizProgressTimerNumberTv;
	private AppCompatTextView mQuizProgressTimerTextTv;

	private AppCompatTextView mQuizQuestionHeaderTv;
	private AppCompatTextView mQuizQuestionPagerCounterTv;

	private AppCompatButton mQuizNavigationNextBtn;
	private AppCompatButton mQuizNavigationPrevBtn;

	private CirclePageIndicator mQuestionCirclePageIndicator;

	private ViewPager mQuestionViewPager;

	private QuizViewPagerAdapter mAdapter;

	private ViewPager.OnPageChangeListener mPagerListener;

	private int mTimerProgress = 0;
	private boolean isTimerRunning;
	private int mTimerProgressMax = 360;

	private Handler mHandler;

	private ArrayList<QuizPagerInfo> mArrayListQuizPagerInfo;

	private boolean isCirclePagerIndicatorEnable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		initToolBar();
		initUi();
		setUiListener();
		setQuizTimerStart();
		setQuizViewPager();
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
			AndroidUtilities.exitWindowAnimation(QuizActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mQuizCoverIv = (ImageView) findViewById(R.id.fragmentQuizTimerCoverIv);

		mQuizProgressTimerNumberTv = (AppCompatTextView) findViewById(R.id.fragmentQuizTimerSecondTv);
		mQuizProgressTimerTextTv = (AppCompatTextView) findViewById(R.id.fragmentQuizTimerSecondTextTv);
		mQuizQuestionHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentQuizQuestionNumberTextTv);
		mQuizQuestionPagerCounterTv = (AppCompatTextView) findViewById(R.id.fragmentQuizQuestionPageCountTv);

		mQuizProgressTimerWheel = (ProgressTimerWheel) findViewById(R.id.fragmentQuizTimerProgressWheel);

		mQuizNavigationNextBtn = (AppCompatButton) findViewById(R.id.fragmentQuizNextBtn);
		mQuizNavigationPrevBtn = (AppCompatButton) findViewById(R.id.fragmentQuizPreviousBtn);

		mQuestionViewPager = (ViewPager) findViewById(R.id.fragmentQuizQuestionViewPager);

		mQuestionCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentQuizQuestionCirclePageIndicator);
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

	private void setQuizTimerStart() {
		mHandler = new Handler();
		final Runnable mQuizTimerTextRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mQuizProgressTimerNumberTv.setText(String
						.valueOf(mTimerProgressMax - mTimerProgress));
			}
		};

		final Runnable mQuizTimerRunnable = new Runnable() {
			public void run() {
				isTimerRunning = true;
				while (mTimerProgress <= mTimerProgressMax) {
					mQuizProgressTimerWheel.setProgress(getTimerProgress());
					mHandler.post(mQuizTimerTextRunnable);
					mTimerProgress++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				isTimerRunning = false;
			}
		};

		Thread mQuizTimerThread = new Thread(mQuizTimerRunnable);
		mQuizTimerThread.start();
	}

	private int getTimerProgress() {
		return Math.round((mTimerProgress / (float) mTimerProgressMax) * 100
				* (360 / (float) 100));
	}

	private void setQuizViewPager() {
		mArrayListQuizPagerInfo = new ArrayList<QuizPagerInfo>();
		for (int i = 0; i < 5; i++) {
			QuizPagerInfo Obj = new QuizPagerInfo();
			Obj.setmQuizId("1");
			if (i % 2 == 0) {
				Obj.setmQuizType("selective");
			} else {
				Obj.setmQuizType("multiple");
			}
			mArrayListQuizPagerInfo.add(Obj);
		}
		if (mArrayListQuizPagerInfo.size() < 7) {
			isCirclePagerIndicatorEnable = true;
		}
		mAdapter = new QuizViewPagerAdapter(getSupportFragmentManager(),
				mArrayListQuizPagerInfo);
		setViewPagerListener();
		mQuestionViewPager.setAdapter(mAdapter);
		if (isCirclePagerIndicatorEnable) {
			mQuestionCirclePageIndicator.setViewPager(mQuestionViewPager);
			mQuestionCirclePageIndicator
					.setOnPageChangeListener(mPagerListener);
		} else {
			mQuizQuestionPagerCounterTv.setVisibility(View.VISIBLE);
			mQuestionCirclePageIndicator.setVisibility(View.GONE);
			mQuestionViewPager.setOnPageChangeListener(mPagerListener);
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
		mQuizQuestionHeaderTv.setText(getResources().getString(
				R.string.sample_question_header_box)
				+ " " + mNumberQuestion);
		if (!isCirclePagerIndicatorEnable) {
			mQuizQuestionPagerCounterTv.setText(mNumberQuestion + " / "
					+ mArrayListQuizPagerInfo.size());
		}

		if (mNumberQuestion == mArrayListQuizPagerInfo.size()) {
			mQuizNavigationNextBtn.setText(getResources().getString(
					R.string.button_submit));
		} else {
			mQuizNavigationNextBtn.setText(getResources().getString(
					R.string.button_next));
		}

		if (mNumberQuestion == 1) {
			mQuizNavigationPrevBtn.setEnabled(false);
		} else {
			mQuizNavigationPrevBtn.setEnabled(true);
		}

		if (mArrayListQuizPagerInfo.size() == 1) {
			mQuizNavigationPrevBtn.setVisibility(View.INVISIBLE);
			mQuizNavigationNextBtn.setText(getResources().getString(
					R.string.button_submit));
		}
	}

	private void setOnClickListener() {
		mQuizNavigationNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (mQuizNavigationNextBtn
						.getText()
						.toString()
						.equalsIgnoreCase(
								getResources().getString(R.string.button_next))) {
					changePagerTo(mQuestionViewPager.getCurrentItem() + 1);
				} else if (mQuizNavigationNextBtn
						.getText()
						.toString()
						.equalsIgnoreCase(
								getResources()
										.getString(R.string.button_submit))) {
					Intent mIntent = new Intent(QuizActivity.this,
							QuizScoreActivity.class);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(QuizActivity.this);
				}
			}
		});

		mQuizNavigationPrevBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changePagerTo(mQuestionViewPager.getCurrentItem() - 1);
			}
		});
	}

	private void changePagerTo(int position) {
		mQuestionViewPager.setCurrentItem(position, true);
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mQuizNavigationNextBtn);
			setMaterialRippleOnView(mQuizNavigationPrevBtn);
			setMaterialRippleWithGrayOnView(mQuizCoverIv);
			setMaterialRippleOnView(mQuizQuestionHeaderTv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
