package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.application.beans.MotherHeader;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.ParichayPagerAdapter;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.fragment.IActivityCommunicator;
import com.application.ui.fragment.IFragmentCommunicator;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.SlidingTabLayout;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.ScrollState;
import com.application.utils.ScrollUtils;
import com.application.utils.Scrollable;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
@SuppressLint("InlinedApi") 
public class ParichayActivity extends BaseActivity implements ObservableScrollViewCallbacks,IActivityCommunicator {
	private Toolbar mToolBar;
	private View mHeaderView;
	private View mToolbarView;
	
	private FrameLayout mCroutonViewGroup;

	private SlidingTabLayout mSlidingTabLayout;
	private ViewPager mPager;

	private int mBaseTranslationY;

	private ParichayPagerAdapter mPagerAdapter;

	private ArrayList<MotherHeader> mArrayListMotherHeader;
	
	public IFragmentCommunicator mFragmentCommunicator;
	
	private int whichTheme = 0;
	
	private boolean isFromNotification = false;
	private String mId;
	private String mCategory;
	
	private Intent mIntent;

	private static final String TAG = ParichayActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mother);
		setSecurity();
		initToolBar();
		initUi();
		ApplicationLoader.getPreferences().setUnreadParichay(0);
		setSlidingTabPagerAdapter();
		setUiListener();
		propagateToolbarState(toolbarIsShown());
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
		applyTheme();
		getIntentData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		notifySlidingTabLayoutChange();
	}


	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_recruitment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(ParichayActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(ParichayActivity.this, MotherActivity.class);
				startActivity(mIntent);
			}
			return true;
		case R.id.action_report:
			Intent mIntentReport = new Intent(ParichayActivity.this,
					ReportActivity.class);
			startActivity(mIntentReport);
			AndroidUtilities.enterWindowAnimation(ParichayActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
	 * <b>Description:</b></br> Initialise Ui Elements from XML
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initUi() {
		mHeaderView = findViewById(R.id.header);

		ViewCompat.setElevation(mHeaderView,
				getResources().getDimension(R.dimen.toolbar_elevation));

		mToolbarView = findViewById(R.id.toolbarLayout);
		mPager = (ViewPager) findViewById(R.id.pager);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		mToolBar.setTitle(getResources().getString(R.string.ParichayActivityTitle));
		setSupportActionBar(mToolBar);
	}

	/**
	 * <b>Description:</b></br> Sets Listener on Ui Elements</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setUiListener() {
		mSlidingTabLayout
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int i, float v, int i2) {
					}

					@Override
					public void onPageSelected(int i) {
						propagateToolbarState(toolbarIsShown());
					}

					@Override
					public void onPageScrollStateChanged(int i) {
					}
				});
	}
	
	private void getIntentData(){
		try{
			mIntent = getIntent();
			if(mIntent!=null){
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).toString();
				isFromNotification = mIntent.getBooleanExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, false);
				if(!TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mCategory)){
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REFERRAL)){
						mCursor = getContentResolver().query(DBConstant.Parichay_Referral_Columns.CONTENT_URI, null, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + "=?", new String[]{mId}, null);
						if(mCursor!=null && mCursor.getCount() > 0){
							mCursor.moveToFirst();
							showReferralUpdateDialog(mCursor);	
						}
					}
					if(mCursor!=null){
						mCursor.close();
					}
					mPager.setCurrentItem(1, true);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void showReferralUpdateDialog(Cursor mCursor){
		try{
			String mTrafficStatics = Utilities.getStatusMessageForParichayReferral(mCursor);
			MaterialDialog mMaterialDialog = new MaterialDialog.Builder(ParichayActivity.this)
					.title(mCursor.getString(mCursor
							.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_FOR))
							+ ": "
							+ mCursor.getString(mCursor
									.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_NAME)))
	        .content(mTrafficStatics)
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
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>Apply Theme</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(ParichayActivity.this).applyThemeMother(ParichayActivity.this, ParichayActivity.this, mToolBar, mSlidingTabLayout);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	/**
	 * <b>Description: </b></br>Notify MotherPagerAdapter : PagerSlidingTabStrip</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void notifySlidingTabLayoutChange(){
		
		mArrayListMotherHeader.get(0).setmIsUnread(ApplicationLoader.getPreferences().getUnreadParichay() > 0 ? true : false);
		mArrayListMotherHeader.get(1).setmIsUnread(false);
		
		mArrayListMotherHeader.get(0).setmUnreadCount(String.valueOf(ApplicationLoader.getPreferences().getUnreadParichay()));
		mArrayListMotherHeader.get(1).setmUnreadCount(String.valueOf(0));
		
		mPagerAdapter.notifyDataSetChanged(mArrayListMotherHeader);
		
		mSlidingTabLayout.notifyDataSetChanged(mPager);
	}
	
	/**
	 * <b>Description: </b></br>Notify Fragment</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void notifyFragmentWithIdAndCategory(int mId, String mCategory){
		if (mId != -1) {
			mFragmentCommunicator.passDataToFragment(mId, mCategory);
		}
	}
	
	/**
	 * <b>Description: </b></br>Set Adapter to PagerSlidingTabStrip</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setSlidingTabPagerAdapter() {
		mArrayListMotherHeader = getMotherPagerHeader();
		mPagerAdapter = new ParichayPagerAdapter(getSupportFragmentManager(),
				mArrayListMotherHeader);
		mPager.setAdapter(mPagerAdapter);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mPager);
	}

	/**
	 * <b>Description: </b></br>Set No of Tabs to PagerSlidingTabStrip</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private ArrayList<MotherHeader> getMotherPagerHeader() {
		mArrayListMotherHeader = new ArrayList<MotherHeader>();

		MotherHeader obj1 = new MotherHeader();
		obj1.setmIsUnread(false);
		obj1.setmTitle(getResources().getString(R.string.layout_mother_parichay));
		obj1.setmUnreadCount("0");
		mArrayListMotherHeader.add(obj1);

		MotherHeader obj2 = new MotherHeader();
		obj2.setmIsUnread(false);	
		obj2.setmTitle(getResources().getString(R.string.layout_mother_referred));
		obj2.setmUnreadCount("0");
		mArrayListMotherHeader.add(obj2);

		return mArrayListMotherHeader;
	}
	
	/**
	 * <b>Description: </b></br>ObservableView</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll,
			boolean dragging) {
		if (dragging) {
			int toolbarHeight = mToolbarView.getHeight();
			float currentHeaderTranslationY = ViewHelper
					.getTranslationY(mHeaderView);
			if (firstScroll) {
				if (-toolbarHeight < currentHeaderTranslationY) {
					mBaseTranslationY = scrollY;
				}
			}
			float headerTranslationY = ScrollUtils.getFloat(
					-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
			ViewPropertyAnimator.animate(mHeaderView).cancel();
			ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
		}
	}

	@Override
	public void onDownMotionEvent() {
	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		mBaseTranslationY = 0;

		Fragment fragment = getCurrentFragment();
		if (fragment == null) {
			return;
		}
		View view = fragment.getView();
		if (view == null) {
			return;
		}

		// ObservableXxxViews have same API
		// but currently they don't have any common interfaces.
		adjustToolbar(scrollState, view);
	}

	private void adjustToolbar(ScrollState scrollState, View view) {
		int toolbarHeight = mToolbarView.getHeight();
		final Scrollable scrollView = (Scrollable) view
				.findViewById(R.id.scroll);
		if (scrollView == null) {
			return;
		}
		int scrollY = scrollView.getCurrentScrollY();
		if (scrollState == ScrollState.DOWN) {
			showToolbar();
		} else if (scrollState == ScrollState.UP) {
			if (toolbarHeight <= scrollY) {
				hideToolbar();
			} else {
				showToolbar();
			}
		} else {
			// Even if onScrollChanged occurs without scrollY changing, toolbar
			// should be adjusted
			if (toolbarIsShown() || toolbarIsHidden()) {
				// Toolbar is completely moved, so just keep its state
				// and propagate it to other pages
				propagateToolbarState(toolbarIsShown());
			} else {
				// Toolbar is moving but doesn't know which to move:
				// you can change this to hideToolbar()
				showToolbar();
			}
		}
	}

	private Fragment getCurrentFragment() {
		return mPagerAdapter.getItemAt(mPager.getCurrentItem());
	}

	/**
	 * <b>Description: </b></br>When the page is selected, other fragments'
	 * scrollY should be adjusted according to the toolbar
	 * status(shown/hidden)</br></br> <b>Referenced
	 * :</b></br>com.github.ksoichiro
	 * .android.observablescrollview.samples</br></br>
	 * 
	 * @param isShown
	 */
	private void propagateToolbarState(boolean isShown) {
		int toolbarHeight = mToolbarView.getHeight();

		// Set scrollY for the fragments that are not created yet
		mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

		// Set scrollY for the active fragments
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			// Skip current item
			if (i == mPager.getCurrentItem()) {
				continue;
			}

			// Skip destroyed or not created item
			Fragment f = mPagerAdapter.getItemAt(i);
			if (f == null) {
				continue;
			}

			View view = f.getView();
			if (view == null) {
				continue;
			}
			propagateToolbarState(isShown, view, toolbarHeight);
		}
	}

	/**
	 * <b>Description:</b></br>propogateToolbarState
	 * 
	 * @param isShown
	 * @param view
	 * @param toolbarHeight
	 */
	private void propagateToolbarState(boolean isShown, View view,
			int toolbarHeight) {
		Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
		if (scrollView == null) {
			return;
		}
		if (isShown) {
			// Scroll up
			if (0 < scrollView.getCurrentScrollY()) {
				scrollView.scrollVerticallyTo(0);
			}
		} else {
			// Scroll down (to hide padding)
			if (scrollView.getCurrentScrollY() < toolbarHeight) {
				scrollView.scrollVerticallyTo(toolbarHeight);
			}
		}
	}

	private boolean toolbarIsShown() {
		return ViewHelper.getTranslationY(mHeaderView) == 0;
	}

	private boolean toolbarIsHidden() {
		return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView
				.getHeight();
	}

	private void showToolbar() {
		float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
		if (headerTranslationY != 0) {
			ViewPropertyAnimator.animate(mHeaderView).cancel();
			ViewPropertyAnimator.animate(mHeaderView).translationY(0)
					.setDuration(200).start();
		}
		propagateToolbarState(true);
	}

	private void hideToolbar() {
		float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
		int toolbarHeight = mToolbarView.getHeight();
		if (headerTranslationY != -toolbarHeight) {
			ViewPropertyAnimator.animate(mHeaderView).cancel();
			ViewPropertyAnimator.animate(mHeaderView)
					.translationY(-toolbarHeight).setDuration(200).start();
		}
		propagateToolbarState(false);
	}
	
	/**
	 * <b>Description: </b></br>Activity : Fragment Communicator</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@Override
	public void passDataToActivity(int mId, String mCategory) {
		// TODO Auto-generated method stub
		notifySlidingTabLayoutChange();
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
