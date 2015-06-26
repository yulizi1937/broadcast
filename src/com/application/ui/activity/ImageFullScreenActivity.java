/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.adapter.ImageFullScreenPagerAdapter;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.CirclePageIndicator;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.mobcast.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageFullScreenActivity extends SwipeBackBaseActivity {
	private static final String TAG = ImageFullScreenActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private ViewPager mImageViewPager;
	private CirclePageIndicator mImageCirclePageIndicator;

	private ImageView mImageNextIv;
	private ImageView mImagePrevIv;

	private ImageFullScreenPagerAdapter mAdapter;

	private ArrayList<String> mArrayListString;

	private SystemBarTintManager mTintManager;

	private boolean isTraining = false; //HDFC
	private boolean isShareOptionEnable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_fullscreen);
		setSystemBarTint();
		initToolBar();
		initUi();
		setUiListener();
		setImageViewPager();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		if(isShareOptionEnable){
			menu.findItem(R.id.action_share).setVisible(true);
			menu.findItem(R.id.action_refresh_actionable).setVisible(true);
		}else{
			menu.findItem(R.id.action_share).setVisible(false);
			menu.findItem(R.id.action_refresh_actionable).setVisible(false);
		}
		return super.onPrepareOptionsPanel(view, menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_text_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_refresh_actionable:
			return true;
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(ImageFullScreenActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(ImageFullScreenActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Image");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ImageFullScreenActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mImageNextIv = (ImageView) findViewById(R.id.fragmentImageFullScreenNextIv);
		mImagePrevIv = (ImageView) findViewById(R.id.fragmentImageFullScreenPreviousIv);

		mImageViewPager = (ViewPager) findViewById(R.id.fragmentImageFullScreenViewPager);
		mImageCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentImageFullScreenCirclePageIndicator);
		
		initUiWithDataForWastingTime();//HDFC
	}

	private void initUiWithDataForWastingTime(){
		if(getIntent().getBooleanExtra(AppConstants.INTENTCONSTANTS.TRAINING, false)){
			isTraining = true;
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
				R.string.ImageDetailActivityTitle));
		mToolBar.setBackgroundColor(getResources().getColor(
				R.color.black_shade_light));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
	}

	private void setOnClickListener() {
	}

	private void setImageViewPager() {
		mArrayListString = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			mArrayListString.add("1");
		}
		mAdapter = new ImageFullScreenPagerAdapter(getSupportFragmentManager(),
				mArrayListString, isTraining);
		mImageViewPager.setAdapter(mAdapter);
		mImageCirclePageIndicator.setViewPager(mImageViewPager);
	}

	private void setMaterialRippleView() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	@SuppressLint("NewApi")
	private void setSystemBarTint() {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(Color.BLACK);
			} else {
				mTintManager = new SystemBarTintManager(
						ImageFullScreenActivity.this);
				// enable status bar tint
				mTintManager.setStatusBarTintEnabled(true);
				mTintManager.setStatusBarTintColor(getResources().getColor(
						android.R.color.black));
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				"Hello ").limit(R.integer.bs_initial_grid_row).build();
	}
}
