/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.application.ui.adapter.TutorialPagerAdapter;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.MaterialRippleLayout;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class TutorialActivity extends AppCompatActivity {
	private static final String TAG = TutorialActivity.class.getSimpleName();

	private ViewPager mViewPager;

	private Toolbar mToolBar;

	private CirclePageIndicator mCirclePageIndicator;

	private TutorialPagerAdapter mPagerAdapter;

	private SystemBarTintManager mTintManager;

	private ViewPager.OnPageChangeListener mPagerListener;

	private ImageView mNextIv;
	private ImageView mPrevIv;

	private boolean isLastPage = false;

	private boolean isFromHelp = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getIntentData();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		setSecurity();
		setSystemBarTint(0);
		initToolBar();
		initUi();
		setViewPager();
		setUiListener();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_help_mobcast:
			Intent mIntent = new Intent(TutorialActivity.this, YouTubeLiveFullScreenActivity.class);
			startActivity(mIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void getIntentData() {
		try {
			if (!getIntent().getBooleanExtra(AppConstants.INTENTCONSTANTS.HELP,
					false)) {
				if (!AndroidUtilities.isAboveLollyPop()) {
					requestWindowFeature(Window.FEATURE_NO_TITLE);
					getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
				}
				isFromHelp = true;
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle("");
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
		if (!isFromHelp) {
			mToolBar.setVisibility(View.VISIBLE);
		}else{
			mToolBar.setVisibility(View.GONE);
		}
	}

	private void initUi() {
		mViewPager = (ViewPager) findViewById(R.id.activityTutorialViewPager);
		mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.activityTutorialCirclePageIndicator);

		mNextIv = (ImageView) findViewById(R.id.activityTutorialNextIv);
		mPrevIv = (ImageView) findViewById(R.id.activityTutorialPreviousIv);
	}

	private void setViewPager() {
		mPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mCirclePageIndicator.setViewPager(mViewPager);
		setViewPagerListener();
		mViewPager.setCurrentItem(0);
		mPrevIv.setVisibility(View.GONE);
		mCirclePageIndicator.setOnPageChangeListener(mPagerListener);
	}

	private void setViewPagerListener() {
		mPagerListener = new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				uiOnChangeOfPagerListener(position);
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

	private void uiOnChangeOfPagerListener(int mCurrentPage) {
		switch (mCurrentPage) {
		case 0:
			mPrevIv.setVisibility(View.GONE);
			isLastPage = false;
			setSystemBarTint(0);
			break;
		case 4:
			if(isFromHelp){
				mNextIv.setImageResource(R.drawable.ic_done);
			}else{
				mNextIv.setVisibility(View.GONE);
			}
			isLastPage = true;
			setSystemBarTint(4);
			break;
		default:
			mPrevIv.setVisibility(View.VISIBLE);
			mNextIv.setVisibility(View.VISIBLE);
			mNextIv.setImageResource(R.drawable.ic_tutorial_next);
			isLastPage = false;
			setSystemBarTint(mCurrentPage);
			break;
		}
	}

	private void setUiListener() {
		setClickListener();
	}

	private void setClickListener() {
		mNextIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (!isLastPage) {
					mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1,
							true);
				} else {
					Intent mIntent = new Intent(TutorialActivity.this, LoginActivity.class);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(TutorialActivity.this);
					finish();
				}
			}
		});

		mPrevIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
			}
		});
	}

	@SuppressLint("NewApi")
	private void setSystemBarTint(int position) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				switch (position) {
				case 0:
					window.setStatusBarColor(Color.parseColor("#FF4521"));
					break;
				case 1:
					window.setStatusBarColor(Color.parseColor("#FECC00"));
					break;
				case 2:
					window.setStatusBarColor(Color.parseColor("#8E44AD"));
					break;
				case 3:
					window.setStatusBarColor(Color.parseColor("#06AE66"));
					break;
				case 4:
					window.setStatusBarColor(Color.parseColor("#048EEE"));
					break;
				}
			} else {
				mTintManager = new SystemBarTintManager(TutorialActivity.this);
				// enable status bar tint
				mTintManager.setStatusBarTintEnabled(true);
				switch (position) {
				case 0:
					mTintManager.setStatusBarTintColor(Color.parseColor("#FF4521"));
					break;
				case 1:
					mTintManager.setStatusBarTintColor(Color.parseColor("#FECC00"));
					break;
				case 2:
					mTintManager.setStatusBarTintColor(Color.parseColor("#8E44AD"));
					break;
				case 3:
					mTintManager.setStatusBarTintColor(Color.parseColor("#06AE66"));
					break;
				case 4:
					mTintManager.setStatusBarTintColor(Color.parseColor("#048EEE"));
					break;
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setMaterialRippleOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#ffffff"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}

	private void setSecurity() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (!BuildVars.DEBUG_SCREENSHOT) {
				getWindow().setFlags(LayoutParams.FLAG_SECURE,
						LayoutParams.FLAG_SECURE);
			}
		}
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        try{
        	if(AndroidUtilities.isAppLanguageIsEnglish()){
        		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        	}else{
        		super.attachBaseContext(newBase);
        	}
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
