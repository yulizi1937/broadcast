package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.application.beans.MotherHeader;
import com.application.ui.adapter.MotherPagerAdapter;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.fragment.IActivityCommunicator;
import com.application.ui.fragment.IFragmentCommunicator;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.service.SyncService;
import com.application.ui.showcaseview.MaterialShowcaseView;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.DrawerArrowDrawable;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ScrimInsetsFrameLayout;
import com.application.ui.view.SlidingTabLayout;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.CheckVersionUpdateAsyncTask;
import com.application.utils.CheckVersionUpdateAsyncTask.OnPostExecuteListener;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.NotificationsController;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.ScrollState;
import com.application.utils.ScrollUtils;
import com.application.utils.Scrollable;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.apprater.AppRater;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.permission.OnPermissionCallback;
import com.permission.PermissionHelper;
import com.squareup.okhttp.OkHttpClient;

/**
 * 
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
@SuppressLint("InlinedApi") 
public class MotherActivity extends BaseActivity implements ObservableScrollViewCallbacks,IActivityCommunicator, OnPermissionCallback {
	public  static final String SLIDINGTABACTION = "com.application.ui.activity.MotherActivity";
	/*
	 * Drawer
	 */
	private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private FrameLayout mDrawerProfileBgLayout;
	private CircleImageView mDrawerProfileIv;
	private ImageView mDrawerProfileCoverIv;
	private AppCompatTextView mDrawerUserNameTv;
	private AppCompatTextView mDrawerUserEmailTv;
	private AppCompatTextView mDrawerSyncTv;

	private ImageView mDrawerSyncIv;

	private LinearLayout mDrawerSyncLayout;

	private DrawerArrowDrawable drawerArrowDrawable;
	private DrawerArrayAdapter mDrawerAdapter;
	private float offset;
	private boolean flipped;
	private Resources mResources;
	private RotateAnimation animSyncDrawer;

	private Toolbar mToolBar;
	private View mHeaderView;
	private View mToolbarView;
	
	private View mMenuAwardView;
	private View mMenuBirthdayView;
	private View mMenuEventView;
	private View mMenuSearchView;

	private FrameLayout mCroutonViewGroup;

	private SlidingTabLayout mSlidingTabLayout;
	private ViewPager mPager;

	private int mBaseTranslationY;

	private MotherPagerAdapter mPagerAdapter;

	private ArrayList<MotherHeader> mArrayListMotherHeader;
	
	public IFragmentCommunicator mFragmentCommunicator;
	
	private ImageLoader mImageLoader;
	
	private int whichTheme = 0;

	private static final String TAG = MotherActivity.class.getSimpleName();
	
	private PermissionHelper mPermissionHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mother);
		setSecurity();
		initToolBar();
		initUi();
		checkPermissionModel();
		setSlidingTabPagerAdapter();
		setUiListener();
		propagateToolbarState(toolbarIsShown());
		setDrawerLayout();
		applyTheme();
		getIntentData();
		apiCheckVersionUpdate();
		showCaseView();
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
		showAppRate();
		checkVideoEncrypted();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mBroadCastReceiver);
		unregisterReceiver(mSyncBroadCastReceiver);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(mBroadCastReceiver, new IntentFilter(NotificationsController.BROADCAST_ACTION));
		registerReceiver(mSyncBroadCastReceiver, new IntentFilter(SyncService.BROADCAST_ACTION));
		notifySlidingTabLayoutChange();
		supportInvalidateOptionsMenu();
		Utilities.showBadgeNotification(MotherActivity.this);
		checkAppVersionOnResume();
		checkMobcastForNewDataAvail();
	}


	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_mother, menu);
		MenuItem menuItemEvent = menu.findItem(R.id.action_event);
		MenuItem menuItemAward = menu.findItem(R.id.action_award);
		MenuItem menuItemBirthday = menu.findItem(R.id.action_birthday);
		
		try{
			if(Utilities.getUnreadOfEvent(MotherActivity.this) != 0){
				menuItemEvent.setIcon(buildCounterDrawableWithPNG(AppConstants.TYPE.EVENT, whichTheme));
			}
			if(Utilities.getUnreadOfAward(MotherActivity.this) != 0){
				menuItemAward.setIcon(buildCounterDrawableWithPNG(AppConstants.TYPE.AWARD, whichTheme));
			}
			if(Utilities.getUnreadOfBirthday(MotherActivity.this) != 0){
				menuItemBirthday.setIcon(buildCounterDrawableWithPNG(AppConstants.TYPE.BIRTHDAY, whichTheme));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			menuItemEvent.setIcon(buildCounterDrawable(Utilities.getUnreadOfEvent(MotherActivity.this),R.drawable.ic_toolbar_event));
			menuItemAward.setIcon(buildCounterDrawable(Utilities.getUnreadOfAward(MotherActivity.this),R.drawable.ic_toolbar_award));
			menuItemBirthday.setIcon(buildCounterDrawable(Utilities.getUnreadOfBirthday(MotherActivity.this),R.drawable.ic_toolbar_birthday));
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_search:
			Intent mIntentSearch = new Intent(MotherActivity.this, SearchActivity.class);
			switch(mPager.getCurrentItem()){
			case 0:
				mIntentSearch.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.MOBCAST);
				break;
			/*case 2:
				mIntentSearch.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.CHAT);
				break;*/
			case 1:
				mIntentSearch.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.TRAINING);
				break;
			}
			startActivity(mIntentSearch);
			AndroidUtilities.enterWindowAnimation(MotherActivity.this);
			return true;
		case R.id.action_award:
			Intent mIntent = new Intent(MotherActivity.this,
					AwardRecyclerActivity.class);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(MotherActivity.this);
			return true;
		case R.id.action_birthday:
			Intent mIntentBirthday = new Intent(MotherActivity.this,
					BirthdayRecyclerActivity.class);
			startActivity(mIntentBirthday);
			AndroidUtilities.enterWindowAnimation(MotherActivity.this);
			return true;
		case R.id.action_event:
			Intent mIntentEvent = new Intent(MotherActivity.this,
					EventRecyclerActivity.class);
			startActivity(mIntentEvent);
			AndroidUtilities.enterWindowAnimation(MotherActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntentReport = new Intent(MotherActivity.this,
					ReportActivity.class);
			startActivity(mIntentReport);
			AndroidUtilities.enterWindowAnimation(MotherActivity.this);
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
		mToolBar.setTitle("");
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
	
	/**
	 * <b>Description: </b></br>Apply Theme</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(MotherActivity.this).applyThemeMother(MotherActivity.this, MotherActivity.this, mToolBar, mSlidingTabLayout);
			ThemeUtils.getInstance(MotherActivity.this).applyThemeDrawer(MotherActivity.this, MotherActivity.this, mDrawerProfileBgLayout, mDrawerUserNameTv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getIntentData(){
		try{
			int isTrainingTab = getIntent().getIntExtra(AppConstants.INTENTCONSTANTS.CATEGORY, 0);
			if(isTrainingTab == 1){
				mPager.setCurrentItem(1, true);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>Notify PagerSlidingTabStrip on New Data Added</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent mIntent) {
				refreshMotherActivity(mIntent);
		}
	};
	
	/**
	 * <b>Description: </b></br>Notify PagerSlidingTabStrip, Fragments & MenuItem Counts</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void refreshMotherActivity(Intent mIntent){
		try{
			String mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
			if (mCategory
					.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)
					|| mCategory
							.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)) {
					notifySlidingTabLayoutChange();
			notifyFragmentWithIdAndCategory(mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.ID, -1), mCategory);
				}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>Broadcast Receiver : Syncing</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private BroadcastReceiver mSyncBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent mIntent) {
			try{
				mDrawerSyncLayout.setEnabled(true);
				mDrawerSyncLayout.setClickable(true);
				mDrawerSyncIv.clearAnimation();
				mDrawerSyncTv.setText(ApplicationLoader.getPreferences().getLastSyncTimeStampMessage());
				onResume();
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	};
	
	/**
	 * <b>Description: </b></br>Notify MotherPagerAdapter : PagerSlidingTabStrip</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void notifySlidingTabLayoutChange(){
		
		mArrayListMotherHeader.get(0).setmIsUnread(Utilities.getUnreadOfMobcast(MotherActivity.this) > 0 ? true : false);
		mArrayListMotherHeader.get(1).setmIsUnread(Utilities.getUnreadOfTraining(MotherActivity.this) > 0 ? true : false);
		
		mArrayListMotherHeader.get(0).setmUnreadCount(String.valueOf(Utilities.getUnreadOfMobcast(MotherActivity.this)));
		mArrayListMotherHeader.get(1).setmUnreadCount(String.valueOf(Utilities.getUnreadOfTraining(MotherActivity.this)));
		
		mPagerAdapter.notifyDataSetChanged(mArrayListMotherHeader);
		
		mSlidingTabLayout.notifyDataSetChanged(mPager);
		notifyDrawerUnreadCount();
	}
	
	/**
	 * <b>Description: </b></br>Notify Drawer List Item and ProfileImage</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void notifyDrawerUnreadCount(){
		try{
			mDrawerAdapter.notifyDataSetChanged();
			setDrawerProfileInfo(mDrawerUserNameTv, mDrawerUserEmailTv, mDrawerProfileIv, mDrawerProfileCoverIv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
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
	 * <b>Description: </b></br>Set MenuIcon According to Theme</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@SuppressWarnings("deprecation")
	private Drawable buildCounterDrawableWithPNG(int mType, int whichTheme){
		Drawable mDrawable = null;
		try{
			Resources mResources = getResources();
			switch(mType){
			case AppConstants.TYPE.BIRTHDAY:
				switch (whichTheme) {
				case 0:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_dblue);
					break;
				case 1:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_purple);
					break;
				case 2:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_green);
					break;
				case 3:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_pink);
					break;
				case 4:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_dblue);
					break;
				case 5:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_brown);
					break;
				case 6:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_purple);
					break;
				case 7:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_purple);
					break;
				case 8:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_pink);
					break;
				case 9:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_dblue);
					break;
				case 10:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_pink);
					break;
				case 11:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_purple);
					break;
				default:
					mDrawable =mResources.getDrawable(R.drawable.ic_toolbar_birthday_unread_dblue);
					break;
				}

				break;
			case AppConstants.TYPE.AWARD:
				switch (whichTheme) {
				case 0:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_dblue);
					break;
				case 1:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_purple);
					break;
				case 2:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_green);
					break;
				case 3:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_pink);
					break;
				case 4:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_dblue);
					break;
				case 5:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_brown);
					break;
				case 6:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_purple);
					break;
				case 7:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_purple);
					break;
				case 8:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_pink);
					break;
				case 9:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_dblue);
					break;
				case 10:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_pink);
					break;
				case 11:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_purple);
					break;
				default:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_award_unread_dblue);
					break;
				}
				break;
			case AppConstants.TYPE.EVENT:
				switch (whichTheme) {
				case 0:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_dblue);
					break;
				case 1:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_purple);
					break;
				case 2:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_green);
					break;
				case 3:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_pink);
					break;
				case 4:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_dblue);
					break;
				case 5:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_brown);
					break;
				case 6:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_purple);
					break;
				case 7:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_purple);
					break;
				case 8:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_pink);
					break;
				case 9:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_dblue);
					break;
				case 10:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_pink);
					break;
				case 11:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_purple);
					break;
				default:
					mDrawable = mResources.getDrawable(R.drawable.ic_toolbar_event_unread_dblue);
					break;
				}

				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return mDrawable;
	}
	
	/**
	 * <b>Description: </b></br>Set Counter to MenuItem</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@SuppressLint("InflateParams")
	private Drawable buildCounterDrawable(int count, int backgroundImageId) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.include_toolbar_action_layout,
				null);
		view.setBackgroundResource(backgroundImageId);

		if (count == 0) {
			View counterTextPanel = view
					.findViewById(R.id.actionItemCounterPanelLayout);
			counterTextPanel.setVisibility(View.GONE);
		} else {
			AppCompatTextView textView = (AppCompatTextView) view
					.findViewById(R.id.actionItemCounter);
			textView.setText("" + count);
		}

		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

		view.setDrawingCacheEnabled(true);
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);

		return new BitmapDrawable(getResources(), bitmap);
	}

	/**
	 * <b>Description: </b></br>Set Adapter to PagerSlidingTabStrip</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setSlidingTabPagerAdapter() {
		mArrayListMotherHeader = getMotherPagerHeader();
		// mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
		mPagerAdapter = new MotherPagerAdapter(getSupportFragmentManager(),
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
		int mUnreadCountMobcast = Utilities.getUnreadOfMobcast(MotherActivity.this);
		if(mUnreadCountMobcast > 0){
			obj1.setmIsUnread(true);
		}else{
			obj1.setmIsUnread(false);
		}
		obj1.setmTitle(getResources().getString(R.string.layout_mother_mobcast));
		obj1.setmUnreadCount(String.valueOf(mUnreadCountMobcast));
		mArrayListMotherHeader.add(obj1);

/*		MotherHeader obj2 = new MotherHeader();
		obj2.setmIsUnread(false);
		obj2.setmTitle(getResources().getString(R.string.layout_mother_chat));
		obj2.setmUnreadCount("0");
		mArrayListMotherHeader.add(obj2);*/

		MotherHeader obj3 = new MotherHeader();
		int mUnreadCountTraining = Utilities.getUnreadOfTraining(MotherActivity.this);
		if(mUnreadCountTraining > 0){
			obj3.setmIsUnread(true);
		}else{
			obj3.setmIsUnread(false);	
		}
		obj3.setmTitle(getResources().getString(R.string.layout_mother_training));
		obj3.setmUnreadCount(String.valueOf(mUnreadCountTraining));
		mArrayListMotherHeader.add(obj3);

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
	 * <b>Description: </b></br>AppRater : Show App Rate</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void showAppRate(){
		try{
			AppRater.app_launched(MotherActivity.this, 7, 20, 21, 40);	
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void checkVideoEncrypted(){
		try{
			if(!ApplicationLoader.getPreferences().isEncryptedVideoDeleted()){
				Utilities.deleteAppFolder(new File(AppConstants.FOLDER.VIDEO_FOLDER));
				ApplicationLoader.getPreferences().setEncryptedVideoDeleted(true);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>ShowCaseView : Starts for the first time</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void showCaseView(){
		if(!ApplicationLoader.getPreferences().isAppShowCaseViewMother()){
			AndroidUtilities.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mMenuAwardView = getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.action_award);
					mMenuSearchView = getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.action_search);
					mMenuBirthdayView = getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.action_birthday);
					mMenuEventView = getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.action_event);
					ApplicationLoader.getPreferences().setAppShowCaseViewMother(true);
					if (mMenuAwardView != null && mMenuBirthdayView != null
							&& mMenuEventView != null && mMenuSearchView != null) {
						startSequenceShowCaseView(2000, 250);
					}
				}
			}, 1000);
		}
	}
	
	/**
	 * <b>Description: </b></br>Sequence ShowCaseView</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void startSequenceShowCaseView(int mDelay, int mSequenceDelay){
		try{
	        MaterialShowcaseView.Builder mMaterialShowcaseViewBuilder1 = new MaterialShowcaseView.Builder(MotherActivity.this);
	        final MaterialShowcaseView mShowCaseView1 = mMaterialShowcaseViewBuilder1.setTarget(mToolBar.getChildAt(0))
	        .setDismissText(getResources().getString(R.string.showcase_next))
	        .setContentText(getResources().getString(R.string.showcase_content1))
	        .setContentTextColor(getResources().getColor(R.color.white))
	        .setMaskColour(getResources().getColor(R.color.mobcast_red))
	        .setDelay(mSequenceDelay) // optional but starting animations immediately in onCreate can make them choppy
	        .show();
	        
	        mMaterialShowcaseViewBuilder1.getDismissTextView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					mShowCaseView1.hide();
					 MaterialShowcaseView.Builder mMaterialShowcaseViewBuilder2 = new MaterialShowcaseView.Builder(MotherActivity.this);
				        final MaterialShowcaseView mShowCaseView2 = mMaterialShowcaseViewBuilder2.setTarget(mMenuEventView)
				        .setDismissText(getResources().getString(R.string.showcase_next))
				        .setContentText(getResources().getString(R.string.showcase_content2))
				        .setContentTextColor(getResources().getColor(R.color.black_shade_light))
				        .setMaskColour(getResources().getColor(R.color.mobcast_yellow))
				        .setDelay(250) // optional but starting animations immediately in onCreate can make them choppy
				        .show();
				        
				        mMaterialShowcaseViewBuilder2.getDismissTextView().setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								mShowCaseView2.hide();
								 MaterialShowcaseView.Builder mMaterialShowcaseViewBuilder3 = new MaterialShowcaseView.Builder(MotherActivity.this);
							        final MaterialShowcaseView mShowCaseView3 = mMaterialShowcaseViewBuilder3.setTarget(mMenuAwardView)
							        .setDismissText(getResources().getString(R.string.showcase_next))
							        .setContentText(getResources().getString(R.string.showcase_content3))
							        .setContentTextColor(getResources().getColor(R.color.white))
							        .setMaskColour(getResources().getColor(R.color.mobcast_purple))
							        .setDelay(250) // optional but starting animations immediately in onCreate can make them choppy
							        .show();
							        
							        mMaterialShowcaseViewBuilder3.getDismissTextView().setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											mShowCaseView3.hide();
											MaterialShowcaseView.Builder mMaterialShowcaseViewBuilder4 = new MaterialShowcaseView.Builder(MotherActivity.this);
									        final MaterialShowcaseView mShowCaseView4 = mMaterialShowcaseViewBuilder4.setTarget(mMenuBirthdayView)
									        .setDismissText(getResources().getString(R.string.showcase_next))
									        .setContentText(getResources().getString(R.string.showcase_content4))
									        .setContentTextColor(getResources().getColor(R.color.white))
									        .setMaskColour(getResources().getColor(R.color.mobcast_green))
									        .setDelay(250) // optional but starting animations immediately in onCreate can make them choppy
									        .show();
									        
									        mMaterialShowcaseViewBuilder4.getDismissTextView().setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													// TODO Auto-generated method stub
													mShowCaseView4.hide();
													MaterialShowcaseView.Builder mMaterialShowcaseViewBuilder5 = new MaterialShowcaseView.Builder(MotherActivity.this);
											        final MaterialShowcaseView mShowCaseView5 = mMaterialShowcaseViewBuilder5.setTarget(mMenuSearchView)
											        .setDismissText(getResources().getString(R.string.showcase_gotit))
											        .setContentText(getResources().getString(R.string.showcase_content5))
											        .setContentTextColor(getResources().getColor(R.color.white))
											        .setMaskColour(getResources().getColor(R.color.mobcast_blue))
											        .setDelay(250) // optional but starting animations immediately in onCreate can make them choppy
											        .show();
											        
											        mMaterialShowcaseViewBuilder5.getDismissTextView().setOnClickListener(new View.OnClickListener() {
														@Override
														public void onClick(View v) {
															// TODO Auto-generated method stub
															mShowCaseView5.hide();
														}
													});
												}
											});
										}
									});
							}
						});
				}
			});
		}catch(Exception e){
			FileLog.e("showCaseView", e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>LogOut : Clear Preferences, Truncate Database, Delete Files, Update Api</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	private void logOutFromApp(){
		if(Utilities.isInternetConnected()){
			if (AndroidUtilities.isAboveHoneyComb()) {
				new AsyncLogOutTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			} else {
				new AsyncLogOutTask().execute();
			}
		}else{
			Utilities.showCrouton(MotherActivity.this, mCroutonViewGroup, getResources().getString(R.string.internet_unavailable), Style.ALERT);
		}
	}
	
	/**
	 * <b>Description: </b></br>AsyncTask : LogOut</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	public class AsyncLogOutTask extends AsyncTask<Void, Void, Void>{
		private MobcastProgressDialog mProgressDialog;
		private String mResponseFromApi;
		private boolean isSuccess;
		private String mErrorMessage;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(MotherActivity.this);
			mProgressDialog.setMessage(getResources().getString(R.string.logging_out));
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiUserAppLogOut();
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
				if(isSuccess){
					ApplicationLoader.getPreferences().clearPreferences();
					Utilities.deleteTables();
					Utilities.deleteAppFolder(new File(AppConstants.FOLDER.BUILD_FOLDER));
					ApplicationLoader.cancelSyncServiceAlarm();
					ApplicationLoader.cancelAppOpenRemindServiceAlarm();
					Utilities.showBadgeNotification(MotherActivity.this);
				}else{
					mErrorMessage = Utilities.getErrorMessageFromApi(mResponseFromApi);
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
				if(isSuccess){
					Toast.makeText(MotherActivity.this, Utilities.getSuccessMessageFromApi(mResponseFromApi), Toast.LENGTH_SHORT).show();
					MotherActivity.this.finish();	
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}
	
	/**
	 * <b>Description: </b></br>Api : LogOut</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private String apiUserAppLogOut() {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostAppLogOutData();
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_LOGOUT_USER, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_LOGOUT_USER, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	/**
	 * <b>Description: </b></br>Dialog : Logout Confirmation</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void showLogOutConfirmationMaterialDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(MotherActivity.this)
        .title(getResources().getString(R.string.logout_title_message))
        .titleColor(Utilities.getAppColor())
        .content(getResources().getString(R.string.logout_content_message))
        .contentColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.sample_fragment_settings_dialog_language_negative))
        .negativeColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	logOutFromApp();
            	dialog.dismiss();
            }
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onNegative(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
	
	
	/**
	 * <b>Description: </b></br>Api : Check Version Update + Update Reg Id</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	private void apiCheckVersionUpdate(){
		try{
			ApplicationLoader.getPreferences().setAppUpdateAvail(false);
			if(Utilities.isInternetConnected()){
				CheckVersionUpdateAsyncTask mCheckVersionUpdateAsyncTask = new CheckVersionUpdateAsyncTask();
				if (AndroidUtilities.isAboveHoneyComb()) {
					mCheckVersionUpdateAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				} else {
					mCheckVersionUpdateAsyncTask.execute();
				}
				mCheckVersionUpdateAsyncTask.setOnPostExecuteListener(new OnPostExecuteListener() {
					@Override
					public void onPostExecute(String mResponseFromApi) {
						// TODO Auto-generated method stub
						try{
							if(Utilities.isSuccessFromApi(mResponseFromApi)){
								try {
									JSONObject mJSONObj = new JSONObject(mResponseFromApi);
									if(mJSONObj.getBoolean(AppConstants.API_KEY_PARAMETER.updateAvailable)){
										ApplicationLoader.getPreferences().setAppUpdateAvail(true);
										showUpdateAvailConfirmationMaterialDialog();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									FileLog.e(TAG, e.toString());
								}catch(Exception e){
									FileLog.e(TAG, e.toString());
								}
							}
						}catch(Exception e){
							FileLog.e(TAG, e.toString());
						}
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>New Data Bubble : Like Facebook - New Stores</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void checkMobcastForNewDataAvail(){
		try{
			if(ApplicationLoader.getPreferences().isRefreshMobcastWithNewDataAvail()){
				Intent mIntent = new Intent();
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.MOBCAST);
				refreshMotherActivity(mIntent);
			}
			
			if(ApplicationLoader.getPreferences().isRefreshTrainingWithNewDataAvail()){
				Intent mTrainIntent = new Intent();
				mTrainIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.TRAINING);
				refreshMotherActivity(mTrainIntent);				
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * <b>Description: </b></br>onResume : CheckAppVersion - ForceUpdate</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void checkAppVersionOnResume(){
		if(ApplicationLoader.getPreferences().isAppUpdateAvail()){
			showUpdateAvailConfirmationMaterialDialog();
		}
	}
	
	
	/**
	 * <b>Description: </b></br>Dialog : Update Available</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void showUpdateAvailConfirmationMaterialDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(MotherActivity.this)
        .title(getResources().getString(R.string.update_title_message))
        .titleColor(Utilities.getAppColor())
        .content(getResources().getString(R.string.update_delete_message))
        .contentColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.update))
        .positiveColor(Utilities.getAppColor())
        .cancelable(false)
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
				Intent mIntent = new Intent(Intent.ACTION_VIEW);
				mIntent.setData(Uri.parse(AppConstants.mStoreLink));
				startActivity(mIntent);
            }
        })
        .show();
	}
	
	/**
	 * Drawer Initilization
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void setDrawerLayout() {
		mResources = getResources();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_listview);

		mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.drawerRootLayout);
		
		mDrawerProfileBgLayout = (FrameLayout)findViewById(R.id.drawerProfileBgLayout);

		mDrawerProfileIv = (CircleImageView) findViewById(R.id.drawerProfileIv);
		mDrawerProfileCoverIv = (ImageView) findViewById(R.id.drawerProfileCoverIv);

		mDrawerUserNameTv = (AppCompatTextView) findViewById(R.id.drawerUserNameTv);
		mDrawerUserEmailTv = (AppCompatTextView) findViewById(R.id.drawerUserEmailTv);
		mDrawerSyncTv = (AppCompatTextView) findViewById(R.id.drawerSyncContentTv);

		mDrawerSyncIv = (ImageView) findViewById(R.id.drawerSyncIv);

		mDrawerSyncLayout = (LinearLayout) findViewById(R.id.drawerSyncLayout);
		
		String[] mDrawerItemArray = getResources().getStringArray(
				R.array.drawer_array);
		int[] mDrawableResId = new int[] { R.drawable.ic_drawer_profile,
				R.drawable.ic_drawer_capture, R.drawable.ic_drawer_recruitment,R.drawable.ic_drawer_recruitment,
				R.drawable.ic_drawer_settings, R.drawable.ic_drawer_help,
				R.drawable.ic_drawer_report, R.drawable.ic_drawer_feedback,
				R.drawable.ic_drawer_logout, R.drawable.ic_drawer_about};

		mDrawerAdapter = new DrawerArrayAdapter(MotherActivity.this,
				mDrawerItemArray, mDrawableResId);
		mDrawerList.setAdapter(mDrawerAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		setDrawerProfileInfo(mDrawerUserNameTv, mDrawerUserEmailTv, mDrawerProfileIv, mDrawerProfileCoverIv);
		
		drawerArrowDrawable = new DrawerArrowDrawable(mResources);
		drawerArrowDrawable.setStrokeColor(mResources
				.getColor(android.R.color.white));
		mToolBar.setNavigationIcon(drawerArrowDrawable);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mToolBar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
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

						try {
							drawerArrowDrawable.setParameter(offset);
						} catch (IllegalArgumentException e) {
							Log.i(TAG, e.toString());
						}
					}
				});

		mDrawerSyncLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				animSyncDrawer = (RotateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_sync);
				mDrawerSyncIv.startAnimation(animSyncDrawer);
				mDrawerSyncLayout.setClickable(false);
				mDrawerSyncLayout.setEnabled(false);
				Intent mSyncServiceIntent = new Intent(MotherActivity.this, SyncService.class);
				mSyncServiceIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMAPP, true);
				startService(mSyncServiceIntent);
			}
		});

		try {
			if (AndroidUtilities.isAboveLollyPop()) {
				mDrawerLayout.setStatusBarBackgroundColor(getResources()
						.getColor(R.color.toolbar_background_statusbar));
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(getResources().getColor(
						android.R.color.transparent));
			}
			mDrawerSyncTv.setText(ApplicationLoader.getPreferences().getLastSyncTimeStampMessage());
		} catch (Exception e) {
		}
		
		try{
			mDrawerProfileBgLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent drawerIntent = new Intent(MotherActivity.this, EditProfileActivity.class);
					mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);
					if (drawerIntent != null){
						startActivity(drawerIntent);
						AndroidUtilities.enterWindowAnimation(MotherActivity.this);	
					}
				}
			});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		try{
			Fragment fragment = null;
			Bundle args = new Bundle();
			Intent drawerIntent = null;
			switch (position) {
			case 0:
				drawerIntent = new Intent(MotherActivity.this, EditProfileActivity.class);
				break;
			/*case 1:
				if(!Utilities.openApp(MotherActivity.this, "in.jts.myapp")){
					Intent mBrowserIntent = new Intent(Intent.ACTION_VIEW);
					mBrowserIntent.setData(Uri.parse("http://www.jajo.in/myovp11.apk"));
					startActivity(mBrowserIntent);
					AndroidUtilities.enterWindowAnimation(MotherActivity.this);	
				}
				return;*/
			case 1:
				drawerIntent = new Intent(MotherActivity.this, CaptureActivity.class);
				break;
			case 2:
				drawerIntent = new Intent(MotherActivity.this, ParichayActivity.class);
				break;
			case 3:
				drawerIntent = new Intent(MotherActivity.this, RecruitmentRecyclerActivity.class);
				break;
			case 4:
				drawerIntent = new Intent(MotherActivity.this, SettingsActivity.class);
				break;
			case 5:
				drawerIntent = new Intent(MotherActivity.this, TutorialActivity.class);
				drawerIntent.putExtra(AppConstants.INTENTCONSTANTS.HELP, true);
				break;
			case 6:
				drawerIntent = new Intent(MotherActivity.this, ReportActivity.class);
				break;
			case 7:
				drawerIntent = new Intent(MotherActivity.this, FeedbackAppActivity.class);
				break;
			case 8:
				showLogOutConfirmationMaterialDialog();
				break;
			case 9:
				drawerIntent = new Intent(MotherActivity.this, AboutActivity.class);
				break;
			default:
				break;
			}
			mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);
			
			if (drawerIntent != null){
				startActivity(drawerIntent);
				AndroidUtilities.enterWindowAnimation(MotherActivity.this);	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	public class DrawerArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
		private final int[] drawableResId;

		public DrawerArrayAdapter(Context context, String[] values,
				int[] drawableResId) {
			super(context, R.layout.include_layout_drawer, values);
			this.context = context;
			this.values = values;
			this.drawableResId = drawableResId;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater
					.inflate(R.layout.item_drawer, parent, false);

			RelativeLayout mDrawerItemLayout = (RelativeLayout) rowView
					.findViewById(R.id.drawerItemLayout);

			AppCompatTextView mDrawerItemTextView = (AppCompatTextView) rowView
					.findViewById(R.id.drawerItemTitleTv);

			ImageView mDrawerItemImageView = (ImageView) rowView
					.findViewById(R.id.drawerItemIv);
			AppCompatTextView mDrawerItemUnreadTextView = (AppCompatTextView)rowView.findViewById(R.id.drawerItemReadCountTv);

			mDrawerItemTextView.setText(values[position]);
			mDrawerItemImageView.setImageDrawable(getResources().getDrawable(
					drawableResId[position]));
			
			if (position != AppConstants.DRAWER_RECRUITMENT_POS) {
				mDrawerItemUnreadTextView.setVisibility(View.GONE);
			}else{
				if(!(ApplicationLoader.getPreferences().getUnreadRecruitment()<=0)){
					mDrawerItemUnreadTextView.setVisibility(View.VISIBLE);
					mDrawerItemUnreadTextView.setText(String.valueOf(ApplicationLoader.getPreferences().getUnreadRecruitment()));
				}else{
					mDrawerItemUnreadTextView.setVisibility(View.GONE);
				}
			}

			return rowView;
		}
	}
	
	/**
	 * <b>Description: </b></br>Drawer : Set Profile</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setDrawerProfileInfo(AppCompatTextView mNameTextView, AppCompatTextView mEmailTextView, CircleImageView mCircleImageView, final ImageView mImageView){
		try{
			if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getUserDisplayName())){
				mNameTextView.setText(ApplicationLoader.getPreferences().getUserDisplayName());
			}
			
			if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getUserEmailAddress())){
				mEmailTextView.setText(ApplicationLoader.getPreferences().getUserEmailAddress());
			}
			
			final String mProfileImagePath = Utilities.getFilePath(AppConstants.TYPE.PROFILE, false, Utilities.getFileName(ApplicationLoader.getPreferences().getUserProfileImageLink()));
			if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getUserProfileImageLink())){
				ApplicationLoader.getPreferences().setUserProfileImagePath(mProfileImagePath);
				if(Utilities.checkIfFileExists(mProfileImagePath)){
					mCircleImageView.setImageURI(Uri.parse(mProfileImagePath));
					/*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
					Bitmap bitmap = BitmapFactory.decodeFile(mProfileImagePath,bmOptions);
					bitmap = Bitmap.createScaledBitmap(bitmap,Utilities.dpToPx(240, getResources()), Utilities.dpToPx(240, getResources()), true);
					mImageView.setImageBitmap(Utilities.blurBitmap(bitmap));*/
				}else{
					mImageLoader = ApplicationLoader.getUILImageLoader();
					mImageLoader.displayImage(ApplicationLoader.getPreferences().getUserProfileImageLink(), mCircleImageView, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
							// TODO Auto-generated method stub
							Utilities.writeBitmapToSDCard(mBitmap, mProfileImagePath);
							/*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
							Bitmap bitmap = BitmapFactory.decodeFile(mProfileImagePath,bmOptions);
							bitmap = Bitmap.createScaledBitmap(bitmap,Utilities.dpToPx(240, getResources()), Utilities.dpToPx(240, getResources()), true);
							mImageView.setImageBitmap(Utilities.blurBitmap(bitmap));*/
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}
					});
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * AndroidM: Permission Model
	 */
	private void checkPermissionModel() {
		try{
			if (AndroidUtilities.isAboveMarshMallow()) {
				mPermissionHelper = PermissionHelper.getInstance(this);
				mPermissionHelper.setForceAccepting(false)
						.request(AppConstants.PERMISSION.STORAGE);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mPermissionHelper.onActivityForResult(requestCode);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			@NonNull String[] permissions, @NonNull int[] grantResults) {
		mPermissionHelper.onRequestPermissionsResult(requestCode, permissions,
				grantResults);
	}

	@SuppressLint("NewApi") @Override
	public void onPermissionGranted(String[] permissionName) {
		try{
			recreate();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	public void onPermissionDeclined(String[] permissionName) {
		AndroidUtilities.showSnackBar(this, getString(R.string.permission_message_denied));
	}

	@Override
	public void onPermissionPreGranted(String permissionName) {
	}

	@Override
	public void onPermissionNeedExplanation(String permissionName) {
		getAlertDialog(permissionName).show();
	}

	@Override
	public void onPermissionReallyDeclined(String permissionName) {
		AndroidUtilities.showSnackBar(this, getString(R.string.permission_message_denied));
	}

	@Override
	public void onNoPermissionNeeded() {
	}

	public AlertDialog getAlertDialog(final String permission) {
		AlertDialog builder = null;
		if (builder == null) {
			builder = new AlertDialog.Builder(this).setTitle(
					getResources().getString(R.string.app_name)+ " requires " + permission + " permission").create();
		}
		builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPermissionHelper.requestAfterExplanation(permission);
					}
				});
		builder.setMessage(getResources().getString(R.string.permission_message_externalstorage));
		return builder;
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
