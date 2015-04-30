package com.application.ui.activity;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.beans.MotherHeader;
import com.application.ui.fragment.MotherPagerAdapter;
import com.application.ui.view.DrawerArrowDrawable;
import com.application.ui.view.SlidingTabLayout;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.ScrollState;
import com.application.utils.ScrollUtils;
import com.application.utils.Scrollable;
import com.mobcast.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class MotherActivity extends BaseActivity implements
		ObservableScrollViewCallbacks {
	/*
	 * Drawer
	 */
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private DrawerArrowDrawable drawerArrowDrawable;
	private float offset;
	private boolean flipped;
	private Resources mResources;
	
	/*
	 * 
	 */
	private View mHeaderView;
	private View mToolbarView;
	
	private ImageView mToolBarDrawer;
	
	private TextView mToolBarTitleTv;
	
	private SlidingTabLayout mSlidingTabLayout;
	private ViewPager mPager;

	private int mBaseTranslationY;

	private MotherPagerAdapter mPagerAdapter;
	
	private ArrayList<MotherHeader> mArrayListMotherHeader;
	
	private static final String TAG = MotherActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mother);
		initToolBar();
		initUi();
		setSlidingTabPagerAdapter();
		setUiListener();
		propagateToolbarState(toolbarIsShown());
		setDrawerLayout();
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
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		setSupportActionBar((Toolbar) findViewById(R.id.toolbarLayout));
		mToolBarTitleTv = (TextView)findViewById(R.id.toolbarTitleTv);
    	mToolBarDrawer = (ImageView)findViewById(R.id.toolbarBackIv);
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

	private void setSlidingTabPagerAdapter() {
		mArrayListMotherHeader = getMotherPagerHeader();
		// mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
		mPagerAdapter = new MotherPagerAdapter(getSupportFragmentManager(),
				mArrayListMotherHeader);
		mPager.setAdapter(mPagerAdapter);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mPager);
	}

	private ArrayList<MotherHeader> getMotherPagerHeader() {
		mArrayListMotherHeader = new ArrayList<MotherHeader>();
		
		MotherHeader obj1 = new MotherHeader();
		obj1.setmIsUnread(true);
		obj1.setmTitle("MOBCAST");
		obj1.setmUnreadCount("99");
		mArrayListMotherHeader.add(obj1);
		
		MotherHeader obj2 = new MotherHeader();
		obj2.setmIsUnread(false);
		obj2.setmTitle("CHAT");
		obj2.setmUnreadCount("0");
		mArrayListMotherHeader.add(obj2);
		
		MotherHeader obj3 = new MotherHeader();
		obj3.setmIsUnread(true);
		obj3.setmTitle("TRAINING");
		obj3.setmUnreadCount("90");
		mArrayListMotherHeader.add(obj3);

		return mArrayListMotherHeader;
	}

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
	
	/*
	 * Drawer Initilization
	 */
	
	private void setDrawerLayout() {
		mResources = getResources();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_listview);
		
		drawerArrowDrawable = new DrawerArrowDrawable(mResources);
		drawerArrowDrawable.setStrokeColor(mResources
				.getColor(android.R.color.white));
		// mImageViewActionBar.setImageDrawable(drawerArrowDrawable);
		mToolBarDrawer.setImageDrawable(drawerArrowDrawable);
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mToolBarDrawer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDrawerLayout.isDrawerVisible(Gravity.START)) {
					mDrawerLayout.closeDrawer(Gravity.START);
				} else {
					mDrawerLayout.openDrawer(Gravity.START);
				}
			}
		});

		mDrawerLayout
				.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
					@Override
					public void onDrawerSlide(View drawerView, float slideOffset) {
						offset = slideOffset;

						// Sometimes slideOffset ends up so close to but not
						// quite 1 or 0.
						if (slideOffset >= .995) {
							flipped = true;
							drawerArrowDrawable.setFlip(flipped);
						} else if (slideOffset <= .005) {
							flipped = false;
							drawerArrowDrawable.setFlip(flipped);
						}

						try{
							drawerArrowDrawable.setParameter(offset);	
						}catch(IllegalArgumentException e){
							Log.i(TAG, e.toString());
						}
					}
				});
		try{
			mDrawerLayout.setStatusBarBackgroundColor(Color.parseColor(getResources().getString(R.color.toolbar_background)));
		}catch(Exception e){
			
		}
	}
}
