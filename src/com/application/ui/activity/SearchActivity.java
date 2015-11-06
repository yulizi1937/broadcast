package com.application.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.application.beans.Award;
import com.application.beans.AwardSave;
import com.application.beans.Event;
import com.application.beans.EventSave;
import com.application.beans.Mobcast;
import com.application.beans.MobcastFileInfo;
import com.application.beans.Training;
import com.application.beans.TrainingFileInfo;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.AwardRecyclerAdapter;
import com.application.ui.adapter.AwardRecyclerAdapter.OnItemClickListenerA;
import com.application.ui.adapter.AwardRecyclerAdapter.OnItemLongClickListenerA;
import com.application.ui.adapter.EventRecyclerAdapter;
import com.application.ui.adapter.EventRecyclerAdapter.OnItemClickListenerE;
import com.application.ui.adapter.EventRecyclerAdapter.OnItemLongClickListenerE;
import com.application.ui.adapter.MobcastRecyclerAdapter;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemClickListener;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.adapter.TrainingRecyclerAdapter;
import com.application.ui.adapter.TrainingRecyclerAdapter.OnItemClickListenerT;
import com.application.ui.adapter.TrainingRecyclerAdapter.OnItemLongClickListenerT;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.DrawerArrowDrawable;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.ScrimInsetsFrameLayout;
import com.application.ui.view.SearchBox;
import com.application.ui.view.SearchBox.MenuListener;
import com.application.ui.view.SearchBox.SearchListener;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * 
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class SearchActivity extends BaseActivity{
	/*
	 * Drawer
	 */
	private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
	private DrawerLayout mDrawerLayout;

	private ImageView mDrawerFilterIv;

	private DrawerArrowDrawable drawerArrowDrawable;
	private float offset;
	private boolean flipped;
	private Resources mResources;

	private Toolbar mToolBar;
	private View mHeaderView;
	private View headerView;
	

	private SearchBox mGooglePlaySearchBox;
	private ImageView mGooglePlaySearchBoxClear;
	
	private AppCompatEditText mGooglePlaySearchEditText;
	
	private AppCompatCheckBox mFilterDevice;
	private AppCompatCheckBox mFilterWeb;
	private AppCompatCheckBox mFilterAll;
	private AppCompatCheckBox mFilterAudio;
	private AppCompatCheckBox mFilterBy;
	private AppCompatCheckBox mFilterDoc;
	private AppCompatCheckBox mFilterFeedback;
	private AppCompatCheckBox mFilterImage;
	private AppCompatCheckBox mFilterLiveStream;
	private AppCompatCheckBox mFilterText;
	private AppCompatCheckBox mFilterVideo;
	private AppCompatCheckBox mFilterQuiz;
	
	private RelativeLayout mFilterForLayout;
	
	private ProgressWheel mGooglePlaySearchProgress;
	
	private FrameLayout mEmptyFrameLayout;
	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;
	private AppCompatButton  mEmptySearchButton;
	
	private FrameLayout mProgressFrameLayout;
	private AppCompatTextView mProgressSearchTextView;
	
	private ObservableRecyclerView mRecyclerView;
	
	private MobcastRecyclerAdapter mMobcastAdapter;
	private TrainingRecyclerAdapter mTrainingAdapter;
	private AwardRecyclerAdapter mAwardAdapter;
	private EventRecyclerAdapter mEventAdapter;
	
	private Context mContext;
	
	private LinearLayoutManager mLinearLayoutManager;
	
    private boolean mLoadMore = false; 
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

	private Intent mIntent;
	private String mCategory;
	private boolean isDrawerEnable = false;
	
	private ArrayList<Mobcast> mArrayListMobcastDB = new ArrayList<>();
	private ArrayList<Mobcast> mArrayListMobcastFilter = new ArrayList<>();
	private ArrayList<Mobcast> mArrayListMobcastWeb = new ArrayList<>();
	
	private ArrayList<Training> mArrayListTrainingDB = new ArrayList<>();
	private ArrayList<Training> mArrayListTrainingFilter = new ArrayList<>();
	private ArrayList<Training> mArrayListTrainingWeb = new ArrayList<>();
	
	private ArrayList<Award> mArrayListAwardDB = new ArrayList<>();
	private ArrayList<Award> mArrayListAwardFilter = new ArrayList<>();
	private ArrayList<Award> mArrayListAwardWeb = new ArrayList<>();
	private ArrayList<AwardSave> mArrayListAwardWebSave = new ArrayList<>();
	
	private ArrayList<Event> mArrayListEventDB = new ArrayList<>();
	private ArrayList<Event> mArrayListEventFilter = new ArrayList<>();
	private ArrayList<Event> mArrayListEventWeb = new ArrayList<>();
	private ArrayList<EventSave> mArrayListEventWebSave = new ArrayList<>();

	private boolean isFilterDevice = true;
	private boolean isFilterWeb = false;
	private boolean isFilterAll = true;
	private boolean isFilterAudio = false;
	private boolean isFilterBy = false;
	private boolean isFilterDoc = false;
	private boolean isFilterFeedback = false;
	private boolean isFilterImage = false;
	private boolean isFilterLiveStream = false;
	private boolean isFilterText = false;
	private boolean isFilterVideo = false;
	private boolean isFilterQuiz = false;
	
	private boolean isMobcast = false;
	private boolean isChat = false;
	private boolean isTraining = false;
	private boolean isAward = false;
	private boolean isEvent = false;
	
	private String mSearchTerm;
	
	private static final String TAG = SearchActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_mother);
		setSecurity();
		initToolBar();
		initUi();
		setDrawerLayout();
		runGooglePlaySearch();
		setUiListener();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(SearchActivity.this);
			return true;
		case R.id.action_search:
			openGooglePlaySearch();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		try {
			if (AndroidUtilities.isAppLanguageIsEnglish()) {
				super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
			} else {
				super.attachBaseContext(newBase);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		AndroidUtilities.exitWindowAnimation(SearchActivity.this);
	}

	private void runGooglePlaySearch() {
		AndroidUtilities.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				openGooglePlaySearch();
				checkDataInAdapter();
			}
		}, 500);
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		if(mIntent.getExtras()!=null){
			mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.CHAT)){
				mGooglePlaySearchEditText.setHint("Search chat");
				isDrawerEnable = false;
				isChat = true;
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mGooglePlaySearchEditText.setHint("Search mobcast");
				isDrawerEnable = true;
				isMobcast = true;
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				if(isMobcast){
					mFilterFeedback.setVisibility(View.VISIBLE);
				}
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				mGooglePlaySearchEditText.setHint("Search training");
				isDrawerEnable = true;
				isTraining = true;
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				if(isTraining){
					mFilterQuiz.setVisibility(View.VISIBLE);
				}
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
				mGooglePlaySearchEditText.setHint("Search award");
				isDrawerEnable = true;
				isAward = true;
//				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				if(isAward){
					mFilterForLayout.setVisibility(View.GONE);
				}
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
				mGooglePlaySearchEditText.setHint("Search event");
				isDrawerEnable = true;
				isEvent = true;
//				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				if(isEvent){
					mFilterForLayout.setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * <b>Description:</b></br> Initialise Ui Elements from XML
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initUi() {
		mHeaderView = findViewById(R.id.header);

		ViewCompat.setElevation(mHeaderView,getResources().getDimension(R.dimen.toolbar_elevation));

		mGooglePlaySearchBox = (SearchBox) findViewById(R.id.searchbox);
		
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll);
		
		mEmptyFrameLayout = (FrameLayout)findViewById(R.id.searchEmptyLayout);
		mEmptyTitleTextView = (AppCompatTextView)findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView)findViewById(R.id.layoutEmptyMessageTv);
		mEmptySearchButton = (AppCompatButton)findViewById(R.id.layoutEmptyRefreshBtn);
		
		mProgressSearchTextView = (AppCompatTextView)findViewById(R.id.searchProgressLayoutTv);
		mProgressFrameLayout = (FrameLayout)findViewById(R.id.searchProgressLayout);
		
		mContext =  SearchActivity.this;
		
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_headview, null);
		
		mLinearLayoutManager = new LinearLayoutManager(mContext);
		
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
	}
	
	private void initDrawerLayout(){
		mFilterDevice = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchLocalCheckbox);
		mFilterWeb = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchWebCheckbox);
		mFilterAll = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchAllCheckbox);
		mFilterAudio = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchAudioCheckbox);
		mFilterBy = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchByCheckbox);
		mFilterDoc = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchDocCheckbox);
		mFilterFeedback = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchFeedbackCheckbox);
		mFilterImage = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchImageCheckbox);
		mFilterLiveStream = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchLiveStreamCheckbox);
		mFilterText = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchTextCheckbox);
		mFilterVideo = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchVideoCheckbox);
		mFilterQuiz = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchMotherSearchQuizCheckbox);
		
		mFilterForLayout  = (RelativeLayout)findViewById(R.id.layoutDrawerSearchMotherSearchForLayout);
	}
	
	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle("");
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		mToolBar.setVisibility(View.GONE);
		setSupportActionBar(mToolBar);
	}

	/**
	 * <b>Description:</b></br> Sets Listener on Ui Elements</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setUiListener() {
		setOnCheckChangeListener();
		setMaterialRippleView();
		setOnClickListener();
	}
	
	private void setMaterialRippleView(){
		try{
			setMaterialRippleOnView(mEmptySearchButton);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setOnCheckChangeListener(){
		mFilterDevice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterDevice = true;
					isFilterWeb= false;
					mFilterWeb.setChecked(false);
					if(isMobcast){
						if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mMobcastAdapter.addMobcastObjList(mArrayListMobcastDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					}else if(isTraining){
						if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mTrainingAdapter.addTrainingObjList(mArrayListTrainingDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					}else if(isAward){
						if(mArrayListAwardDB!=null && mArrayListAwardDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mAwardAdapter.addAwardObjList(mArrayListAwardDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					}else if(isEvent){
						if(mArrayListEventDB!=null && mArrayListEventDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mEventAdapter.addEventObjList(mArrayListEventDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					}
				}else{
					isFilterDevice = false;
					isFilterWeb= true;
					mFilterWeb.setChecked(true);
					if(isMobcast){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}else if(isTraining){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}else if(isAward){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}else if(isEvent){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}
				}
			}
		});
		
		mFilterWeb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterWeb = true;
					isFilterDevice= false;
					mFilterDevice.setChecked(false);
					if(isMobcast){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}else if(isTraining){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}else if(isAward){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}else if(isEvent){
						setEmptyData(true, AppConstants.SEARCH_WEB);
					}
				}else{
					isFilterWeb = false;
					isFilterDevice= true;
					mFilterDevice.setChecked(true);
					if(isMobcast){
						if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mMobcastAdapter.addMobcastObjList(mArrayListMobcastDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					} else if(isTraining){
						if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mTrainingAdapter.addTrainingObjList(mArrayListTrainingDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					} else if(isAward){
						if(mArrayListAwardDB!=null && mArrayListAwardDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mAwardAdapter.addAwardObjList(mArrayListAwardDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					}else if(isEvent){
						if(mArrayListEventDB!=null && mArrayListEventDB.size()>0){
							mEmptyFrameLayout.setVisibility(View.GONE);
							mEventAdapter.addEventObjList(mArrayListEventDB);
							mRecyclerView.getAdapter().notifyDataSetChanged();
							mRecyclerView.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});
		
		mFilterAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterAll = true;
					disableAllOtherFilters();
				}else{
					isFilterAll = false;
					enableAllOtherFilters();
				}
			}
		});
		
		mFilterAudio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterAudio = true;
				}else{
					isFilterAudio = false;
				}
				applyFilter();
			}
		});
		
		mFilterBy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterBy = true;
				}else{
					isFilterBy = false;
				}
				applyFilter();
			}
		});
		
		mFilterDoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterDoc = true;
				}else{
					isFilterDoc = false;
				}
				applyFilter();
			}
		});
		
		mFilterFeedback.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterFeedback = true;
				}else{
					isFilterFeedback = false;
				}
				applyFilter();
			}
		});
		
		mFilterImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterImage = true;
				}else{
					isFilterImage = false;
				}
				applyFilter();
			}
		});
		

		mFilterLiveStream.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterLiveStream = true;
				}else{
					isFilterLiveStream = false;
				}
				applyFilter();
			}
		});
		
		mFilterText.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterText = true;
				}else{
					isFilterText = false;
				}
				applyFilter();
			}
		});
		
		mFilterVideo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterVideo = true;
				}else{
					isFilterVideo = false;
				}
				applyFilter();
			}
		});
		
		mFilterQuiz.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterQuiz = true;
				}else{
					isFilterQuiz = false;
				}
				applyFilter();
			}
		});
	}
	
	private void setOnClickListener(){
		mEmptySearchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				isFilterDevice = false;
				isFilterWeb = true;
				mFilterWeb.setChecked(true);
				mFilterDevice.setChecked(false);
				searchQuery(mSearchTerm);
			}
		});
	}
	
	private void disableAllOtherFilters(){
		if(isFilterAll){
			mFilterAudio.setEnabled(false);
			mFilterBy.setEnabled(false);
			mFilterDoc.setEnabled(false);
			mFilterFeedback.setEnabled(false);
			mFilterImage.setEnabled(false);
			mFilterLiveStream.setEnabled(false);
			mFilterText.setEnabled(false);
			mFilterVideo.setEnabled(false);
			mFilterQuiz.setEnabled(false);
			
			mFilterAudio.setChecked(false);
			mFilterBy.setChecked(false);
			mFilterDoc.setChecked(false);
			mFilterFeedback.setChecked(false);
			mFilterImage.setChecked(false);
			mFilterLiveStream.setChecked(false);
			mFilterText.setChecked(false);
			mFilterVideo.setChecked(false);
			mFilterQuiz.setChecked(false);
		}
	}
	
	private void enableAllOtherFilters(){
		if(!isFilterAll){
			mFilterAudio.setEnabled(true);
			mFilterBy.setEnabled(true);
			mFilterDoc.setEnabled(true);
			mFilterFeedback.setEnabled(true);
			mFilterImage.setEnabled(true);
			mFilterLiveStream.setEnabled(true);
			mFilterText.setEnabled(true);
			mFilterVideo.setEnabled(true);
			mFilterQuiz.setEnabled(true);
		}
	}

	/*
	 * Search Intilization
	 */

	@SuppressWarnings("deprecation")
	public void openGooglePlaySearch() {
		mToolBar.setTitle("");
		mToolBar.setVisibility(View.GONE);
		mGooglePlaySearchBox.revealFromMenuItem(R.id.action_search, this);
		mGooglePlaySearchBox.enableVoiceRecognition(SearchActivity.this);
		mGooglePlaySearchProgress = mGooglePlaySearchBox.getProgressWheel();
		mGooglePlaySearchEditText = mGooglePlaySearchBox.getSearchEditText();
		mGooglePlaySearchBoxClear = mGooglePlaySearchBox.getClearButton();
		
		/*for (int x = 0; x < 10; x++) {
			SearchResult option = new SearchResult("Result "
					+ Integer.toString(x), getResources().getDrawable(
					R.drawable.ic_history));
			mGooglePlaySearchBox.addSearchable(option);
		}*/
		 
		mGooglePlaySearchBox.setMenuListener(new MenuListener() {

			@Override
			public void onMenuClick() {
				// Hamburger has been clicked
			}

		});
		mGooglePlaySearchBox.setSearchListener(new SearchListener() {

			@Override
			public void onSearchOpened() {
				// Use this to tint the screen

			}

			@Override
			public void onSearchClosed() {
				// Use this to un-tint the screen
				closeGooglePlaySearch();
			}

			@Override
			public void onSearchTermChanged() {
				// React to the search term changing
				// Called after it has updated results
			}

			@Override
			public void onSearch(String searchTerm) {
				mToolBar.setTitle(searchTerm);
				mGooglePlaySearchProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSearchCleared() {
				mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
			}

		});

		mDrawerFilterIv = mGooglePlaySearchBox.getDrawerLogo();
		mDrawerFilterIv.setVisibility(View.VISIBLE);
		getIntentData();
		if(!isDrawerEnable){
			mDrawerFilterIv.setImageResource(R.drawable.ic_back_gray);
		}
		
		if (mDrawerFilterIv != null) {
			mDrawerFilterIv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isDrawerEnable){
						if (mDrawerLayout.isDrawerVisible(Gravity.START)) {
							mDrawerLayout.closeDrawer(Gravity.START);
						} else {
							mDrawerLayout.openDrawer(Gravity.START);
						}	
					}else{
						onBackPressed();
					}
				}
			});
		}
		
		mGooglePlaySearchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				searchQuery(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s){
				// TODO Auto-generated method stub
				
			}
		});
		
		mGooglePlaySearchBoxClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
					try{
						if(mGooglePlaySearchBox.isMicEnabled() && mGooglePlaySearchEditText.getEditableText().toString().length() > 0){
							mGooglePlaySearchEditText.setText("");
							if(isFilterDevice){
								resetAdapterWithArrayList();
							}
						}else{
							mGooglePlaySearchBox.micClick();
						}
					}catch(Exception e){
						FileLog.e(TAG, e.toString());
					}
			}
		});
	}

	protected void closeGooglePlaySearch() {
		mGooglePlaySearchBox.hideCircularly(SearchActivity.this);
		if (mGooglePlaySearchBox.getSearchText().isEmpty())
			mToolBar.setTitle("");
	}
	
	@SuppressLint("DefaultLocale") private void searchQuery(String mSearchQuery){
		if(!TextUtils.isEmpty(mSearchQuery)){
			if(mSearchQuery.length() > 2){
				mSearchTerm = mSearchQuery;
				mGooglePlaySearchProgress.setVisibility(View.VISIBLE);
				if(isMobcast){
					if(isFilterDevice){
						searchQueryMobcast(mSearchQuery.toLowerCase());	
					}else if(isFilterWeb){
						searchFromApi(mSearchQuery, true);
					}
				}else if(isChat){
					
				}else if(isTraining){
					if(isFilterDevice){
						searchQueryTraining(mSearchQuery.toLowerCase());	
					}else if(isFilterWeb){
						searchFromApi(mSearchQuery, true);
					}
				}else if(isAward){
					if(isFilterDevice){
						searchQueryAward(mSearchQuery.toLowerCase());	
					}else if(isFilterWeb){
						searchFromApi(mSearchQuery, true);
					}
				}else if(isEvent){
					if(isFilterDevice){
						searchQueryEvent(mSearchQuery.toLowerCase());	
					}else if(isFilterWeb){
						searchFromApi(mSearchQuery, true);
					}
				}
			}else{
				mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
				if(isFilterDevice){
					resetAdapterWithArrayList();
				}
			}
		}
	}
	
	
	private void searchQueryMobcast(String mSearchQuery){
		try{
			ArrayList<Mobcast> mFilter = filterListMobcast(mSearchQuery);
			if (mFilter != null && mFilter.size() > 0) {
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mMobcastAdapter.addMobcastObjList(mArrayListMobcastFilter);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}else{
				setEmptyData(true, mSearchQuery);
			}
			mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void searchQueryTraining(String mSearchQuery){
		try{
			ArrayList<Training> mFilter = filterListTraining(mSearchQuery);
			if (mFilter != null && mFilter.size() > 0) {
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mTrainingAdapter.addTrainingObjList(mArrayListTrainingFilter);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}else{
				setEmptyData(true, mSearchQuery);
			}
			mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void searchQueryAward(String mSearchQuery){
		try{
			ArrayList<Award> mFilter = filterListAward(mSearchQuery);
			if (mFilter != null && mFilter.size() > 0) {
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mAwardAdapter.addAwardObjList(mArrayListAwardFilter);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}else{
				setEmptyData(true, mSearchQuery);
			}
			mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void searchQueryEvent(String mSearchQuery){
		try{
			ArrayList<Event> mFilter = filterListEvent(mSearchQuery);
			if (mFilter != null && mFilter.size() > 0) {
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mEventAdapter.addEventObjList(mArrayListEventFilter);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}else{
				setEmptyData(true, mSearchQuery);
			}
			mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void applyFilter(){
		if(isMobcast){
			if(isFilterDevice){
				if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size() > 0){
					searchQueryMobcast(AppConstants.SEARCH_FILTER);	
				}
			}
		}else if(isTraining){
			if(isFilterDevice){
				if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size() > 0){
					searchQueryTraining(AppConstants.SEARCH_FILTER);	
				}
			}
		}else if(isAward){
			if(isFilterDevice){
				if(mArrayListAwardDB!=null && mArrayListAwardDB.size() > 0){
					searchQueryAward(AppConstants.SEARCH_FILTER);	
				}
			}
		}else if(isEvent){
			if(isFilterDevice){
				if(mArrayListEventDB!=null && mArrayListEventDB.size() > 0){
					searchQueryEvent(AppConstants.SEARCH_FILTER);	
				}
			}
		}
		
	}
	
	private void resetAdapterWithArrayList(){
		if(isMobcast){
			if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size() > 0){
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mMobcastAdapter.addMobcastObjList(mArrayListMobcastDB);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}else if(isTraining){
			if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size() > 0){
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mTrainingAdapter.addTrainingObjList(mArrayListTrainingDB);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}else if(isAward){
			if(mArrayListAwardDB!=null && mArrayListAwardDB.size() > 0){
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mAwardAdapter.addAwardObjList(mArrayListAwardDB);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}else if(isEvent){
			if(mArrayListEventDB!=null && mArrayListEventDB.size() > 0){
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mEventAdapter.addEventObjList(mArrayListEventDB);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * RecyclerView
	 */
	
	private void checkDataInAdapter(){
		if(isMobcast){
			checkDataInMobcastAdapter();
		}else if(isChat){
			
		}else if(isTraining){
			checkDataInTrainingAdapter();			
		}else if(isAward){
			checkDataInAwardAdapter();
		}else if(isEvent){
			checkDataInEventAdapter();
		}
	}
	
	//Mobcast
	private void checkDataInMobcastAdapter() {
		Cursor mCursor = getContentResolver().query(
				DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null,
				DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED + " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addMobcastObjectListFromDBToBeans(mCursor, false, false);
			if (mArrayListMobcastDB.size() > 0) {
				setRecyclerMobcastAdapter(mArrayListMobcastDB);
				setRecyclerMobcastAdapterListener();
			} else {
				setEmptyData(true, null);
			}
		} else {
			setEmptyData(true, null);
		}

		if (mCursor != null) {
			mCursor.close();
		}
	}
	
	private void setRecyclerMobcastAdapter(ArrayList<Mobcast> mListMobcast) {
		mMobcastAdapter = new MobcastRecyclerAdapter(mContext,
				mListMobcast, headerView, false);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(false);
		if (AndroidUtilities.isAboveIceCreamSandWich()) {
			AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(
					mMobcastAdapter);
			ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(
					mAlphaAdapter);
			mRecyclerView.setAdapter(mScaleInAdapter);
		} else {
			mRecyclerView.setAdapter(mMobcastAdapter);
		}
		mRecyclerView
				.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).color(Utilities.getDividerColor())
						.sizeResId(R.dimen.fragment_recyclerview_divider)
						.visibilityProvider(mMobcastAdapter).build());
	}
	
	private void setRecyclerMobcastAdapterListener() {
		if(mMobcastAdapter!=null){
			mMobcastAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					String mId = null;
					position-=1;
					if(isFilterDevice){
						if(mArrayListMobcastFilter!=null && mArrayListMobcastFilter.size() > 0){
							mId = mArrayListMobcastFilter.get(position).getmId();
						}else if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size() > 0){
							mId = mArrayListMobcastDB.get(position).getmId();
						}else{
							return;
						}
					}
					switch (view.getId()) {
					case R.id.itemRecyclerMobcastVideoRootLayout:
						Intent mIntentVideo = new Intent(SearchActivity.this,VideoDetailActivity.class);
						mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentVideo);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastLiveRootLayout:
						Intent mIntentLive = new Intent(SearchActivity.this,
								YouTubeLiveStreamActivity.class);
						mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentLive);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastImageRootLayout:
						Intent mIntentImage = new Intent(SearchActivity.this,ImageDetailActivity.class);
						mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.ID,mId);
						startActivity(mIntentImage);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastFeedbackRootLayout:
						boolean feedbackSingleAttemptAndRead = false;
						boolean isSingleAttempt = false;
						if(isFilterDevice){
							if(mArrayListMobcastFilter!=null && mArrayListMobcastFilter.size() > 0){
								isSingleAttempt = Boolean.parseBoolean(mArrayListMobcastFilter.get(position).getmFileInfo().get(0).getmFileIsDefault());
							}else if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size() > 0){
								isSingleAttempt = Boolean.parseBoolean(mArrayListMobcastDB.get(position).getmFileInfo().get(0).getmFileIsDefault());
							}else{
								return;
							}
						}
						
						if(isSingleAttempt){
							boolean isRead = false;
							if(isFilterDevice){
								if(mArrayListMobcastFilter!=null && mArrayListMobcastFilter.size() > 0){
									isRead = mArrayListMobcastFilter.get(position).isRead();
								}else if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size() > 0){
									Cursor mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId}, null);
									if(mCursor!=null && mCursor.getCount() > 0){
										mCursor.moveToFirst();
										isRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)));	
									}
									
									if(mCursor!=null){
										mCursor.close();
									}
								}else{
									return;
								}
							}
								if(isRead){
									feedbackSingleAttemptAndRead = true;
								}
						}
						
						if(!feedbackSingleAttemptAndRead){
							Intent mIntentFeedback = new Intent(SearchActivity.this,FeedbackActivity.class);
							mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
							mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentFeedback);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						}else{
							AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.feedback_attempted));
						}
						break;
					case R.id.itemRecyclerMobcastAudioRootLayout:
						Intent mIntentAudio = new Intent(SearchActivity.this,AudioDetailActivity.class);
						mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentAudio);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastTextRootLayout:
						Intent mIntentText = new Intent(SearchActivity.this,TextDetailActivity.class);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentText);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastPdfRootLayout:
						Intent mIntentPdf = new Intent(SearchActivity.this,PdfDetailActivity.class);
						mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentPdf);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastXlsRootLayout:
						Intent mIntentXls = new Intent(SearchActivity.this,XlsDetailActivity.class);
						mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentXls);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastDocRootLayout:
						Intent mIntentDoc = new Intent(SearchActivity.this,DocDetailActivity.class);
						mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					case R.id.itemRecyclerMobcastPptRootLayout:
						Intent mIntentPpt = new Intent(SearchActivity.this,PptDetailActivity.class);
						mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
						startActivity(mIntentPpt);
						AndroidUtilities.enterWindowAnimation(SearchActivity.this);
						break;
					}
				}
			});
		}
	}
	
	private void setRecyclerMobcastScrollListener() {
		if(mMobcastAdapter!=null){
			mRecyclerView.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView,
						int newState) {
					// TODO Auto-generated method stub
					super.onScrollStateChanged(recyclerView, newState);
					int topRowVerticalPosition = (recyclerView == null || recyclerView
							.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0)
							.getTop();
				}

				
				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					// TODO Auto-generated method stub
					super.onScrolled(recyclerView, dx, dy);
			        mVisibleItemCount = mLinearLayoutManager.getChildCount();
			        mTotalItemCount = mLinearLayoutManager.getItemCount();
			        mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
			 
			        if (!mLoadMore) {
						if (mVisibleItemCount + mFirstVisibleItem >= mTotalItemCount) {
			            	mLoadMore = true;
				            if(!TextUtils.isEmpty(mSearchTerm)){
				            	searchFromApi(mSearchTerm, false);
				            }
			            }
			        }
				}
			});
		}
	}
	
	private void setRecyclerMobcastAdapterLongPressListener(){
		if(mArrayListMobcastWeb!=null && mArrayListMobcastWeb.size()>0){
			if(mMobcastAdapter!=null){
				mMobcastAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public void onItemLongClick(View view, int position) {
						// TODO Auto-generated method stub
						position-=1;
						showContextMenu(position, view);
					}
				});
			}
		}
	}
	
	private void addMobcastObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver, boolean isToAddOldData){
		int mIntMobcastId = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
		int mIntMobcastTitle = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE);
		int mIntMobcastBy = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY);
		int mIntMobcastDesc = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC);
		int mIntMobcastDate = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE);
		int mIntMobcastTime = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME);
		int mIntMobcastLikeCount = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO);
		int mIntMobcastViewCount = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT);
		int mIntMobcastLink = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK);
		int mIntMobcastIsLike = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE);
		int mIntMobcastIsRead = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ);
		int mIntMobcastIsSharing = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING);
		int mIntMobcastIsDown = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE);
		int mIntMobcastType = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE);
		int mIntMobcastExpiryDate = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE);
		int mIntMobcastExpiryTime = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME);
		
		Cursor mCursorFile = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, null, null, null);
		int mIntMobcastFileInfoLink = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK);
		int mIntMobcastFileInfoPath = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH);
		int mIntMobcastFileInfoLang = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG);
		int mIntMobcastFileInfoPage = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES);
		int mIntMobcastFileInfoName = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME);
		int mIntMobcastFileInfoThumbLink = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK);
		int mIntMobcastFileInfoThumbPath = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH);
		int mIntMobcastFileInfoSize = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE);
		int mIntMobcastFileInfoDuration = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION);
		int mIntMobcastFileInfoIsDefault = mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT);
		
		if(mCursorFile!=null){
			mCursorFile.close();
		}
		
		if(!isToAddOldData){
			mArrayListMobcastDB = new ArrayList<Mobcast>();
		}else{
			if(mArrayListMobcastDB==null){
				mArrayListMobcastDB = new ArrayList<Mobcast>();	
			}
		}
		
		mCursor.moveToFirst();
		do {
			Mobcast  Obj = new Mobcast();
			String mMobcastId = mCursor.getString(mIntMobcastId);
			String mMobcastDate = mCursor.getString(mIntMobcastDate);
			String mMobcastTime = mCursor.getString(mIntMobcastTime);
			Obj.setmId(mMobcastId);
			Obj.setmTitle(mCursor.getString(mIntMobcastTitle));
			Obj.setmDescription(mCursor.getString(mIntMobcastDesc));
			Obj.setmBy(Utilities.formatBy(mCursor.getString(mIntMobcastBy), mMobcastDate, mMobcastTime));
			Obj.setmDate(mMobcastDate);
			Obj.setmTime(mMobcastTime);
			Obj.setmLikeCount(mCursor.getString(mIntMobcastLikeCount));
			Obj.setmViewCount(mCursor.getString(mIntMobcastViewCount));
			Obj.setLike(Boolean.parseBoolean(mCursor.getString(mIntMobcastIsLike)));
			Obj.setRead(Boolean.parseBoolean(mCursor.getString(mIntMobcastIsRead)));
			Obj.setDownloadable(Boolean.parseBoolean(mCursor.getString(mIntMobcastIsDown)));
			Obj.setSharing(Boolean.parseBoolean(mCursor.getString(mIntMobcastIsSharing)));
			Obj.setmLink(mCursor.getString(mIntMobcastLink));
			Obj.setmExpiryDate(mCursor.getString(mIntMobcastExpiryDate));
			Obj.setmExpiryTime(mCursor.getString(mIntMobcastExpiryTime));
			Obj.setmFileType(mCursor.getString(mIntMobcastType));

			if(Utilities.getMediaType(mCursor.getString(mIntMobcastType)) != AppConstants.TYPE.FEEDBACK && Utilities.getMediaType(mCursor.getString(mIntMobcastType)) != AppConstants.TYPE.TEXT){
				Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
				
				if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
					mCursorFileInfo.moveToFirst();
					ArrayList<MobcastFileInfo> mMobcastFileInfoList = new ArrayList<MobcastFileInfo>();
					do{
						MobcastFileInfo fileObj = new MobcastFileInfo();
						fileObj.setmDuration(mCursorFileInfo.getString(mIntMobcastFileInfoDuration));
						fileObj.setmFileIsDefault(mCursorFileInfo.getString(mIntMobcastFileInfoIsDefault));
						fileObj.setmFileLanguages(mCursorFileInfo.getString(mIntMobcastFileInfoLang));
						fileObj.setmFileLink(mCursorFileInfo.getString(mIntMobcastFileInfoLink));
						fileObj.setmFileName(mCursorFileInfo.getString(mIntMobcastFileInfoName));
						fileObj.setmFilePath(mCursorFileInfo.getString(mIntMobcastFileInfoPath));
						fileObj.setmFileSize(mCursorFileInfo.getString(mIntMobcastFileInfoSize));
						fileObj.setmPages(mCursorFileInfo.getString(mIntMobcastFileInfoPage));
						fileObj.setmThumbnailLink(mCursorFileInfo.getString(mIntMobcastFileInfoThumbLink));
						fileObj.setmThumbnailPath(mCursorFileInfo.getString(mIntMobcastFileInfoThumbPath));
						
						mMobcastFileInfoList.add(fileObj);
					}while(mCursorFileInfo.moveToNext());
					
					Obj.setmFileInfo(mMobcastFileInfoList);
				}
				
				if(mCursorFileInfo!=null){
					mCursorFileInfo.close();
				}
			}else if(Utilities.getMediaType(mCursor.getString(mIntMobcastType)) == AppConstants.TYPE.FEEDBACK){
				Cursor mCursorFeedbackInfo = getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?", new String[]{mMobcastId}, DBConstant.Mobcast_Feedback_Columns.COLUMN_ID + " ASC");
				
				if(mCursorFeedbackInfo!=null && mCursorFeedbackInfo.getCount() > 0){
					mCursorFeedbackInfo.moveToFirst();
					ArrayList<MobcastFileInfo> mMobcastFeedbackInfoList = new ArrayList<>();
					MobcastFileInfo feedbackObj = new MobcastFileInfo();
					feedbackObj.setmFileIsDefault(mCursorFeedbackInfo.getString(mCursorFeedbackInfo.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ATTEMPT)));
					feedbackObj.setmPages(mCursorFeedbackInfo.getCount() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));
					mMobcastFeedbackInfoList.add(feedbackObj);
					Obj.setmFileInfo(mMobcastFeedbackInfoList);
				}
				
				if(mCursorFeedbackInfo!=null){
					mCursorFeedbackInfo.close();
				}
			}
			
			
			if(!isFromBroadCastReceiver){
				mArrayListMobcastDB.add(Obj);	
			}else{
				mArrayListMobcastDB.add(0,Obj);
			}
		} while (mCursor.moveToNext());
	
		Collections.sort(mArrayListMobcastDB, new MobcastSort());
	}
	
	public class MobcastSort implements Comparator<Mobcast>{
	    @Override
	    public int compare(Mobcast Obj1, Mobcast Obj2) {
	        try{
	        	if(Integer.parseInt(Obj1.getmId()) < Integer.parseInt(Obj2.getmId())){
		            return 1;
		        } else {
		            return -1;
		        }
	        }catch(Exception e){
	        	FileLog.e(TAG, e.toString());
	        	return -1;
	        }
	    }
	}
	
	@SuppressLint("DefaultLocale") private ArrayList<Mobcast> filterListMobcast(String mSearchQuery){
		try{
			if(mArrayListMobcastFilter!=null){
				mArrayListMobcastFilter.clear();
			}else{
				mArrayListMobcastFilter  = new ArrayList<>();
			}
			if(mArrayListMobcastDB!=null && mArrayListMobcastDB.size() > 0){
				for(int i = 0; i < mArrayListMobcastDB.size() ; i++){
					Mobcast Obj = mArrayListMobcastDB.get(i);
					if(isFilterAll){
						if(Obj.getmTitle().toLowerCase().contains(mSearchQuery) || Obj.getmDescription().toLowerCase().contains(mSearchQuery) || Obj.getmBy().toLowerCase().contains(mSearchQuery)){
							mArrayListMobcastFilter.add(Obj);
						}
					}else {
						boolean isAlreadyAdded = false;
						if(isFilterBy){
							if ( Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterAudio && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.AUDIO)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterDoc && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.PDF)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.PPT)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.XLS)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.DOC)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterFeedback && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.FEEDBACK)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterImage && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.IMAGE)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterLiveStream && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.STREAM)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterText && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.TEXT)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterVideo && !isAlreadyAdded){
							if (Obj.getmTitle().toLowerCase()
									.contains(mSearchQuery)
									|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
									|| Obj.getmFileType().toLowerCase().contains(AppConstants.MOBCAST.VIDEO)
									|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
								mArrayListMobcastFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
					}
				}
			}
			
			if(mArrayListMobcastFilter!=null && mArrayListMobcastFilter.size() > 0){
				return mArrayListMobcastFilter;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	//Training
		private void checkDataInTrainingAdapter() {
			Cursor mCursor = getContentResolver().query(
					DBConstant.Training_Columns.CONTENT_URI, null, null, null,
					DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED + " DESC");
			if (mCursor != null && mCursor.getCount() > 0) {
				addTrainingObjectListFromDBToBeans(mCursor, false, false);
				if (mArrayListTrainingDB.size() > 0) {
					setRecyclerTrainingAdapter(mArrayListTrainingDB);
					setRecyclerTrainingAdapterListener();
				} else {
					setEmptyData(true, null);
				}
			} else {
				setEmptyData(true, null);
			}

			if (mCursor != null) {
				mCursor.close();
			}
		}
		
		private void setRecyclerTrainingAdapter(ArrayList<Training> mArrayListTraining) {
			mTrainingAdapter = new TrainingRecyclerAdapter(mContext,
					mArrayListTraining, headerView, false);
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			mRecyclerView.setHasFixedSize(false);
			if (AndroidUtilities.isAboveIceCreamSandWich()) {
				AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(
						mTrainingAdapter);
				ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(
						mAlphaAdapter);
				mRecyclerView.setAdapter(mScaleInAdapter);
			} else {
				mRecyclerView.setAdapter(mTrainingAdapter);
			}
			mRecyclerView
					.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).color(Utilities.getDividerColor())
							.sizeResId(R.dimen.fragment_recyclerview_divider)
							.visibilityProvider(mTrainingAdapter).build());
		}
		
		private void setRecyclerTrainingAdapterListener() {
			if(mTrainingAdapter!=null){
				mTrainingAdapter.setOnItemClickListener(new OnItemClickListenerT() {
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
						String mId = null;
						position-=1;
						if(isFilterDevice){
							if(mArrayListTrainingFilter!=null && mArrayListTrainingFilter.size() > 0){
								mId = mArrayListTrainingFilter.get(position).getmId();
							}else if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size() > 0){
								mId = mArrayListTrainingDB.get(position).getmId();
							}else{
								return;
							}
						}
						switch (view.getId()) {
						case R.id.itemRecyclerTrainingVideoRootLayout:
							Intent mIntentVideo = new Intent(SearchActivity.this,VideoDetailActivity.class);
							mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentVideo);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerMobcastLiveRootLayout:
							Intent mIntentLive = new Intent(SearchActivity.this,
									YouTubeLiveStreamActivity.class);
							mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentLive);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingImageRootLayout:
							Intent mIntentImage = new Intent(SearchActivity.this,ImageDetailActivity.class);
							mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.ID,mId);
							startActivity(mIntentImage);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingQuizRootLayout:
							boolean quizSingleAttemptAndRead = false;
							boolean isSingleAttempt = false;
							if(isFilterDevice){
								if(mArrayListTrainingFilter!=null && mArrayListTrainingFilter.size() > 0){
									isSingleAttempt = Boolean.parseBoolean(mArrayListTrainingFilter.get(position).getmFileInfo().get(0).getmFileIsDefault());
								}else if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size() > 0){
									isSingleAttempt = Boolean.parseBoolean(mArrayListTrainingDB.get(position).getmFileInfo().get(0).getmFileIsDefault());
								}else{
									return;
								}
							}
							
							if(isSingleAttempt){
								boolean isRead = false;
								if(isFilterDevice){
									if(mArrayListTrainingFilter!=null && mArrayListTrainingFilter.size() > 0){
										isRead = mArrayListTrainingFilter.get(position).isRead();
									}else if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size() > 0){
										Cursor mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId}, null);
										if(mCursor!=null && mCursor.getCount() > 0){
											mCursor.moveToFirst();
											isRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ)));	
										}
										
										if(mCursor!=null){
											mCursor.close();
										}
										
									}else{
										return;
									}
								}
									if(isRead){
										quizSingleAttemptAndRead = true;
									}
							}
							
							if(!quizSingleAttemptAndRead){
								Intent mIntentFeedback = new Intent(SearchActivity.this,QuizActivity.class);
								mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
								mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
								startActivity(mIntentFeedback);
								AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							}else{
								AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.quiz_attempted));
							}
							break;
						case R.id.itemRecyclerTrainingAudioRootLayout:
							Intent mIntentAudio = new Intent(SearchActivity.this,AudioDetailActivity.class);
							mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentAudio);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingTextRootLayout:
							Intent mIntentText = new Intent(SearchActivity.this,TextDetailActivity.class);
							mIntentText.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentText.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentText);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingPdfRootLayout:
							Intent mIntentPdf = new Intent(SearchActivity.this,PdfDetailActivity.class);
							mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentPdf);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingXlsRootLayout:
							Intent mIntentXls = new Intent(SearchActivity.this,XlsDetailActivity.class);
							mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentXls);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingDocRootLayout:
							Intent mIntentDoc = new Intent(SearchActivity.this,DocDetailActivity.class);
							mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						case R.id.itemRecyclerTrainingPptRootLayout:
							Intent mIntentPpt = new Intent(SearchActivity.this,PptDetailActivity.class);
							mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
							mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentPpt);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						}
					}
				});
			}
		}
		
		private void setRecyclerTrainingScrollListener() {
			if(mTrainingAdapter!=null){
				mRecyclerView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(RecyclerView recyclerView,
							int newState) {
						// TODO Auto-generated method stub
						super.onScrollStateChanged(recyclerView, newState);
						int topRowVerticalPosition = (recyclerView == null || recyclerView
								.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0)
								.getTop();
					}

					
					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						// TODO Auto-generated method stub
						super.onScrolled(recyclerView, dx, dy);
				        mVisibleItemCount = mLinearLayoutManager.getChildCount();
				        mTotalItemCount = mLinearLayoutManager.getItemCount();
				        mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
				 
				        if (!mLoadMore) {
							if (mVisibleItemCount + mFirstVisibleItem >= mTotalItemCount) {
				            	mLoadMore = true;
					            if(!TextUtils.isEmpty(mSearchTerm)){
					            	searchFromApi(mSearchTerm, false);
					            }
				            }
				        }
					}
				});
			}
		}
		
		private void setRecyclerTrainingAdapterLongPressListener(){
			if(mArrayListTrainingWeb!=null && mArrayListTrainingWeb.size()>0){
				if(mTrainingAdapter!=null){
					mTrainingAdapter.setOnItemLongClickListener(new OnItemLongClickListenerT() {
						@Override
						public void onItemLongClick(View view, int position) {
							// TODO Auto-generated method stub
							position-=1;
							showContextMenu(position, view);
						}
					});
				}
			}
		}
		
		private void addTrainingObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver, boolean isToAddOldData){
			int mIntTrainingId = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_ID);
			int mIntTrainingTitle = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE);
			int mIntTrainingBy = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY);
			int mIntTrainingDesc = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC);
			int mIntTrainingDate = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE);
			int mIntTrainingTime = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME);
			int mIntTrainingLikeCount = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO);
			int mIntTrainingViewCount = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT);
			int mIntTrainingLink = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LINK);
			int mIntTrainingIsLike = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE);
			int mIntTrainingIsRead = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ);
			int mIntTrainingIsSharing = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING);
			int mIntTrainingIsDown = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE);
			int mIntTrainingType = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE);
			int mIntTrainingExpiryDate = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE);
			int mIntTrainingExpiryTime = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME);
			
			Cursor mCursorFile = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, null, null, null);
			int mIntTrainingFileInfoLink = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK);
			int mIntTrainingFileInfoPath = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH);
			int mIntTrainingFileInfoLang = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG);
			int mIntTrainingFileInfoPage = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES);
			int mIntTrainingFileInfoName = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME);
			int mIntTrainingFileInfoThumbLink = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK);
			int mIntTrainingFileInfoThumbPath = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH);
			int mIntTrainingFileInfoSize = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE);
			int mIntTrainingFileInfoDuration = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION);
			int mIntTrainingFileInfoIsDefault = mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT);
			
			if(mCursorFile!=null){
				mCursorFile.close();
			}
			
			if(!isToAddOldData){
				mArrayListTrainingDB = new ArrayList<Training>();
			}else{
				if(mArrayListTrainingDB==null){
					mArrayListTrainingDB = new ArrayList<Training>();	
				}
			}
			
			mCursor.moveToFirst();
			do {
				Training  Obj = new Training();
				String mTrainingId = mCursor.getString(mIntTrainingId);
				String mTrainingDate = mCursor.getString(mIntTrainingDate);
				String mTrainingTime = mCursor.getString(mIntTrainingTime);
				Obj.setmId(mTrainingId);
				Obj.setmTitle(mCursor.getString(mIntTrainingTitle));
				Obj.setmDescription(mCursor.getString(mIntTrainingDesc));
				Obj.setmBy(Utilities.formatBy(mCursor.getString(mIntTrainingBy), mTrainingDate, mTrainingTime));
				Obj.setmDate(mTrainingDate);
				Obj.setmTime(mTrainingTime);
				Obj.setmLikeCount(mCursor.getString(mIntTrainingLikeCount));
				Obj.setmViewCount(mCursor.getString(mIntTrainingViewCount));
				Obj.setLike(Boolean.parseBoolean(mCursor.getString(mIntTrainingIsLike)));
				Obj.setRead(Boolean.parseBoolean(mCursor.getString(mIntTrainingIsRead)));
				Obj.setDownloadable(Boolean.parseBoolean(mCursor.getString(mIntTrainingIsDown)));
				Obj.setSharing(Boolean.parseBoolean(mCursor.getString(mIntTrainingIsSharing)));
				Obj.setmLink(mCursor.getString(mIntTrainingLink));
				Obj.setmExpiryDate(mCursor.getString(mIntTrainingExpiryDate));
				Obj.setmExpiryTime(mCursor.getString(mIntTrainingExpiryTime));
				Obj.setmFileType(mCursor.getString(mIntTrainingType));

				if(Utilities.getMediaType(mCursor.getString(mIntTrainingType)) != AppConstants.TYPE.QUIZ && Utilities.getMediaType(mCursor.getString(mIntTrainingType)) != AppConstants.TYPE.TEXT){
					Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
					
					if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
						mCursorFileInfo.moveToFirst();
						ArrayList<TrainingFileInfo> mTrainingFileInfoList = new ArrayList<TrainingFileInfo>();
						do{
							TrainingFileInfo fileObj = new TrainingFileInfo();
							fileObj.setmDuration(mCursorFileInfo.getString(mIntTrainingFileInfoDuration));
							fileObj.setmFileIsDefault(mCursorFileInfo.getString(mIntTrainingFileInfoIsDefault));
							fileObj.setmFileLanguages(mCursorFileInfo.getString(mIntTrainingFileInfoLang));
							fileObj.setmFileLink(mCursorFileInfo.getString(mIntTrainingFileInfoLink));
							fileObj.setmFileName(mCursorFileInfo.getString(mIntTrainingFileInfoName));
							fileObj.setmFilePath(mCursorFileInfo.getString(mIntTrainingFileInfoPath));
							fileObj.setmFileSize(mCursorFileInfo.getString(mIntTrainingFileInfoSize));
							fileObj.setmPages(mCursorFileInfo.getString(mIntTrainingFileInfoPage));
							fileObj.setmThumbnailLink(mCursorFileInfo.getString(mIntTrainingFileInfoThumbLink));
							fileObj.setmThumbnailPath(mCursorFileInfo.getString(mIntTrainingFileInfoThumbPath));
							
							mTrainingFileInfoList.add(fileObj);
						}while(mCursorFileInfo.moveToNext());
						
						Obj.setmFileInfo(mTrainingFileInfoList);
					}
					
					if(mCursorFileInfo!=null){
						mCursorFileInfo.close();
					}
				}else if(Utilities.getMediaType(mCursor.getString(mIntTrainingType)) == AppConstants.TYPE.QUIZ){
					Cursor mCursorQuizInfo = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?", new String[]{mTrainingId}, DBConstant.Training_Quiz_Columns.COLUMN_ID + " ASC");
					
					if(mCursorQuizInfo!=null && mCursorQuizInfo.getCount() > 0){
						mCursorQuizInfo.moveToFirst();
						int mTotalPoints = 0;
						ArrayList<TrainingFileInfo> mTrainingQuizInfoList = new ArrayList<>();
						TrainingFileInfo quizObj = new TrainingFileInfo();
						quizObj.setmPages(mCursorQuizInfo.getCount() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));
						try{
							String []mTime = Utilities.convertTimeFromSecsTo(Long.parseLong(mCursorQuizInfo.getString(mCursorQuizInfo.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION)))).split(" ");
							quizObj.setmDuration(mTime[0]+" "+ mTime[1]);
							quizObj.setmFileIsDefault(mCursorQuizInfo.getString(mCursorQuizInfo.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT)));
							quizObj.setmAttempts(mCursorQuizInfo.getString(mCursorQuizInfo.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT_COUNT)));
							do{
								mTotalPoints+=Integer.parseInt(mCursorQuizInfo.getString(mCursorQuizInfo.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS)));
							}while(mCursorQuizInfo.moveToNext());
							quizObj.setmQuestions(mTotalPoints + " "+getResources().getString(R.string.item_recycler_training_quiz_points));
						}catch(Exception e){
							FileLog.e(TAG, e.toString());
						}
						mTrainingQuizInfoList.add(quizObj);
						Obj.setmFileInfo(mTrainingQuizInfoList);
					}
					
					if(mCursorQuizInfo!=null){
						mCursorQuizInfo.close();
					}
				}
				
				
				if(!isFromBroadCastReceiver){
					mArrayListTrainingDB.add(Obj);	
				}else{
					mArrayListTrainingDB.add(0,Obj);
				}
			} while (mCursor.moveToNext());
		
			Collections.sort(mArrayListTrainingDB, new TrainingSort());
		}
		
		public class TrainingSort implements Comparator<Training>{
		    @Override
		    public int compare(Training Obj1, Training Obj2) {
		        try{
		        	if(Integer.parseInt(Obj1.getmId()) < Integer.parseInt(Obj2.getmId())){
			            return 1;
			        } else {
			            return -1;
			        }
		        }catch(Exception e){
		        	FileLog.e(TAG, e.toString());
		        	return -1;
		        }
		    }
		}
		
		@SuppressLint("DefaultLocale") private ArrayList<Training> filterListTraining(String mSearchQuery){
			try{
				if(mArrayListTrainingFilter!=null){
					mArrayListTrainingFilter.clear();
				}else{
					mArrayListTrainingFilter  = new ArrayList<>();
				}
				if(mArrayListTrainingDB!=null && mArrayListTrainingDB.size() > 0){
					for(int i = 0; i < mArrayListTrainingDB.size() ; i++){
						Training Obj = mArrayListTrainingDB.get(i);
						if(isFilterAll){
							if(Obj.getmTitle().toLowerCase().contains(mSearchQuery) || Obj.getmDescription().toLowerCase().contains(mSearchQuery) || Obj.getmBy().toLowerCase().contains(mSearchQuery)){
								mArrayListTrainingFilter.add(Obj);
							}
						}else {
							boolean isAlreadyAdded = false;
							if(isFilterBy){
								if ( Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterAudio && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.AUDIO)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterDoc && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.PDF)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.PPT)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.XLS)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.DOC)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterQuiz && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.QUIZ)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterImage && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.IMAGE)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterLiveStream && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.STREAM)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterText && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.TEXT)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
							
							if(isFilterVideo && !isAlreadyAdded){
								if (Obj.getmTitle().toLowerCase()
										.contains(mSearchQuery)
										|| Obj.getmDescription().toLowerCase().contains(mSearchQuery)
										|| Obj.getmFileType().toLowerCase().contains(AppConstants.TRAINING.VIDEO)
										|| Obj.getmBy().toLowerCase().contains(mSearchQuery)) {
									mArrayListTrainingFilter.add(Obj);
									isAlreadyAdded = true;
								}
							}
						}
					}
				}
				
				if(mArrayListTrainingFilter!=null && mArrayListTrainingFilter.size() > 0){
					return mArrayListTrainingFilter;
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
			return null;
		}
		
	//Award	
		private void checkDataInAwardAdapter() {
			Cursor mCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI,null,null,null,DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED+ " DESC");
			if (mCursor != null && mCursor.getCount() > 0) {
				addAwardObjectListFromDBToBeans(mCursor, false, false);
				if (mArrayListAwardDB.size() > 0) {
					setRecyclerAwardAdapter(mArrayListAwardDB);
					setRecyclerAwardAdapterListener();
				}else {
					setEmptyData(true, null);
				}
			} else {
				setEmptyData(true, null);
			}

			if (mCursor != null) {
				mCursor.close();
			}
		}
		
		private void setRecyclerAwardAdapter(ArrayList<Award>  mArrayListAward) {
			mAwardAdapter = new AwardRecyclerAdapter(mContext, mArrayListAward);
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			mRecyclerView.setHasFixedSize(false);
			if (AndroidUtilities.isAboveIceCreamSandWich()) {
				AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(
						mAwardAdapter);
				ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(
						mAlphaAdapter);
				mRecyclerView.setAdapter(mScaleInAdapter);
			} else {
				mRecyclerView.setAdapter(mAwardAdapter);
			}
			mRecyclerView
					.addItemDecoration(new HorizontalDividerItemDecoration.Builder(
							this)
							.color(Utilities.getDividerColor())
							.marginResId(
									R.dimen.fragment_recyclerview_award_left_margin,
									R.dimen.fragment_recyclerview_award_right_margin)
							.build()/*
							new VerticalDividerItemDecoration.Builder(this).color(Utilities.getDividerColor())
									.sizeResId(R.dimen.fragment_recyclerview_divider)
									.visibilityProvider(mAwardAdapter).build()*/);
		}		
		
		private void setRecyclerAwardAdapterListener() {
			if (mAwardAdapter != null) {
				mAwardAdapter.setOnItemClickListener(new OnItemClickListenerA() {
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
						String mId = null;
						if(isFilterDevice){
							if(mArrayListAwardFilter!=null && mArrayListAwardFilter.size() > 0){
								mId = mArrayListAwardFilter.get(position).getmId();
							}else if(mArrayListAwardDB!=null && mArrayListAwardDB.size() > 0){
								mId = mArrayListAwardDB.get(position).getmId();
							}else{
								return;
							}
						}
						switch (view.getId()) {
						case R.id.itemRecyclerAwardRootLayout:
							Intent mIntentEvent = new Intent(SearchActivity.this,AwardProfileActivity.class);
							mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.AWARD);
							mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.ID,mId);
							startActivity(mIntentEvent);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;						
							}
					}
				});
			}
	}
		
		private void setRecyclerAwardScrollListener() {
			if (mAwardAdapter != null) {
				mRecyclerView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(RecyclerView recyclerView,
							int newState) {
						// TODO Auto-generated method stub
						super.onScrollStateChanged(recyclerView, newState);
						int topRowVerticalPosition = (recyclerView == null || recyclerView
								.getChildCount() == 0) ? 0 : recyclerView
								.getChildAt(0).getTop();
					}

					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						// TODO Auto-generated method stub
						super.onScrolled(recyclerView, dx, dy);
						mVisibleItemCount = mLinearLayoutManager.getChildCount();
						mTotalItemCount = mLinearLayoutManager.getItemCount();
						mFirstVisibleItem = mLinearLayoutManager
								.findFirstVisibleItemPosition();

						if (!mLoadMore) {
							if (mVisibleItemCount + mFirstVisibleItem >= mTotalItemCount) {
								mLoadMore = true;
								 if(!TextUtils.isEmpty(mSearchTerm)){
						            	searchFromApi(mSearchTerm, false);
						          }
							}
						}
					}
				});
			}
		}
		
		private void setRecyclerAwardAdapterLongPressListener(){
			if(mArrayListAwardWeb!=null && mArrayListAwardWeb.size()>0){
				if(mAwardAdapter!=null){
					mAwardAdapter.setOnItemLongClickListener(new OnItemLongClickListenerA() {
						@Override
						public void onItemLongClick(View view, int position) {
							// TODO Auto-generated method stub
							showContextMenu(position, view);	
						}
					});
				}
			}
		}	
		
		
		private void addAwardObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver, boolean isToAddOldData) {
			int mIntAwardId = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_ID);
			int mIntAwardName = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME);
			int mIntAwardRecognition = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION);
			int mIntAwardThumbLink = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK);
			int mIntAwardThumbPath = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH);
			int mIntAwardReceivedDate = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DATE);
			int mIntAwardReceivedTime = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_TIME);
			int mIntAwardDate = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE);
			int mIntAwardViewCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO);
			int mIntAwardLikeCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO);
			int mIntAwardCongratulatedCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO);
			int mIntAwardIsRead = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ);
			int mIntAwardIsLike = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE);
			int mIntAwardIsCongratulated = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE);

			if (!isToAddOldData) {
				mArrayListAwardDB = new ArrayList<Award>();
			} else {
				if (mArrayListAwardDB == null) {
					mArrayListAwardDB = new ArrayList<Award>();
				}
			}
			mCursor.moveToFirst();
			do {
				Award Obj = new Award();
				String mAwardId = mCursor.getString(mIntAwardId);
				String mAwardReceivedDate = mCursor.getString(mIntAwardReceivedDate);
				String mAwardReceivedTime = mCursor.getString(mIntAwardReceivedTime);
				Obj.setmId(mAwardId);
				Obj.setmName(mCursor.getString(mIntAwardName));
				Obj.setmRecognition(mCursor.getString(mIntAwardRecognition));
				Obj.setmThumbLink(mCursor.getString(mIntAwardThumbLink));
				Obj.setmThumbPath(mCursor.getString(mIntAwardThumbPath));
				Obj.setmReceivedDate(mAwardReceivedDate);
				Obj.setmReceivedTime(mAwardReceivedTime);
				Obj.setmAwardDate(mCursor.getString(mIntAwardDate));
				Obj.setmLikeCount(mCursor.getString(mIntAwardLikeCount));
				Obj.setmViewCount(mCursor.getString(mIntAwardViewCount));
				Obj.setmCongratulatedCount(mCursor.getString(mIntAwardCongratulatedCount));
				Obj.setRead(Boolean.parseBoolean(mCursor.getString(mIntAwardIsRead)));
				Obj.setLike(Boolean.parseBoolean(mCursor.getString(mIntAwardIsLike)));
				Obj.setCongratulated(Boolean.parseBoolean(mCursor.getString(mIntAwardIsCongratulated)));
				Obj.setmFileType(AppConstants.INTENTCONSTANTS.AWARD);

				if (!isFromBroadCastReceiver) {
					mArrayListAwardDB.add(Obj);
				} else {
					mArrayListAwardDB.add(0, Obj);
				}
			} while (mCursor.moveToNext());

			Collections.sort(mArrayListAwardDB, new AwardSort());
		}	
		
		public class AwardSort implements Comparator<Award> {
			@Override
			public int compare(Award Obj1, Award Obj2) {
				try {
					if (Integer.parseInt(Obj1.getmId()) < Integer.parseInt(Obj2
							.getmId())) {
						return 1;
					} else {
						return -1;
					}
				} catch (Exception e) {
					FileLog.e(TAG, e.toString());
					return -1;
				}
			}
		}
		
		@SuppressLint("DefaultLocale") private ArrayList<Award> filterListAward(String mSearchQuery){
			try{
				if(mArrayListAwardFilter!=null){
					mArrayListAwardFilter.clear();
				}else{
					mArrayListAwardFilter  = new ArrayList<>();
				}
				if(mArrayListAwardDB!=null && mArrayListAwardDB.size() > 0){
					for(int i = 0; i < mArrayListAwardDB.size() ; i++){
						Award Obj = mArrayListAwardDB.get(i);
						if(isFilterAll){
							if(Obj.getmName().toLowerCase().contains(mSearchQuery) || Obj.getmRecognition().toLowerCase().contains(mSearchQuery)){
								mArrayListAwardFilter.add(Obj);
							}
						}
					}
				}
				
				if(mArrayListAwardFilter!=null && mArrayListAwardFilter.size() > 0){
					return mArrayListAwardFilter;
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
			return null;
		}
		
		//Event
		private void checkDataInEventAdapter() {
			Cursor mCursor = getContentResolver().query(
					DBConstant.Event_Columns.CONTENT_URI, null, null, null,
					DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED + " DESC");
			if (mCursor != null && mCursor.getCount() > 0) {
				addEventObjectListFromDBToBeans(mCursor, false, false);
				if (mArrayListEventDB.size() > 0) {
					setRecyclerEventAdapter(mArrayListEventDB);
					setRecyclerEventAdapterListener();
				} else {
					setEmptyData(true, null);
				}
			} else {
				setEmptyData(true,null);
			}

			if (mCursor != null) {
				mCursor.close();
			}
		}
		
		private void setRecyclerEventAdapter(ArrayList<Event> mArrayListEvent){
		    mEventAdapter = new EventRecyclerAdapter(SearchActivity.this, mArrayListEvent);
		    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			mRecyclerView.setHasFixedSize(false);
		    if(AndroidUtilities.isAboveIceCreamSandWich()){
	        	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mEventAdapter);
	            ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
	            mRecyclerView.setAdapter(mScaleInAdapter);
	        }else{
	        	mRecyclerView.setAdapter(mEventAdapter);
	        }
			 mRecyclerView.addItemDecoration(
				        new HorizontalDividerItemDecoration.Builder(this)
				                .color(Utilities.getDividerColor())
				                .sizeResId(R.dimen.fragment_recyclerview_divider)
				                .visibilityProvider(mEventAdapter)
				                .build());
		}
		
		private void setRecyclerEventAdapterListener() {
			if(mEventAdapter!=null){
				mEventAdapter.setOnItemClickListener(new OnItemClickListenerE() {
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
//						position-=1;
						String mId = null;
						if(isFilterDevice){
							if(mArrayListEventFilter!=null && mArrayListEventFilter.size() > 0){
								mId = mArrayListEventFilter.get(position).getmId();
							}else if(mArrayListEventDB!=null && mArrayListEventDB.size() > 0){
								mId = mArrayListEventDB.get(position).getmId();
							}else{
								return;
							}
						}
						switch (view.getId()) {
						case R.id.itemRecyclerEventRootLayout:
							Intent mIntentEvent = new Intent(SearchActivity.this,EventDetailActivity.class);
							mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.EVENT);
							mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
							startActivity(mIntentEvent);
							AndroidUtilities.enterWindowAnimation(SearchActivity.this);
							break;
						}

					}
				});
			}
		}
		
		private void setRecyclerEventScrollListener() {
			if(mEventAdapter!=null){
				mRecyclerView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(RecyclerView recyclerView,
							int newState) {
						// TODO Auto-generated method stub
						super.onScrollStateChanged(recyclerView, newState);
						int topRowVerticalPosition = (recyclerView == null || recyclerView
								.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0)
								.getTop();
					}

					
					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						// TODO Auto-generated method stub
						super.onScrolled(recyclerView, dx, dy);
				        mVisibleItemCount = mLinearLayoutManager.getChildCount();
				        mTotalItemCount = mLinearLayoutManager.getItemCount();
				        mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
				 
				        if (!mLoadMore) {
							if (mVisibleItemCount + mFirstVisibleItem >= mTotalItemCount) {
				            	mLoadMore = true;
				            	 if(!TextUtils.isEmpty(mSearchTerm)){
						            	searchFromApi(mSearchTerm, false);
						            }
				            }
				        }
					}
				});
			}
		}
		
		private void setRecyclerEventAdapterLongPressListener(){
			if(mArrayListEventWeb!=null && mArrayListEventWeb.size()>0){
				if(mEventAdapter!=null){
					mEventAdapter.setOnItemLongClickListener(new OnItemLongClickListenerE() {
						@Override
						public void onItemLongClick(View view, int position) {
							// TODO Auto-generated method stub
							showContextMenu(position, view);
						}
					});
				}
			}
		}
		
		private void addEventObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver, boolean isToAddOldData){
			int mIntEventId = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_ID);
			int mIntEventTitle = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE);
			int mIntEventViewCount = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO);
			int mIntEventLikeCount = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO);
			int mIntEventGoingCount = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO);
			int mIntEventBy = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_BY);
			int mIntEventStartDate = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE);
			int mIntEventStartTime = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME);
			int mIntEventReceivedDate = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE);
			int mIntEventReceivedTime = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME);
			int mIntEventIsRead = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ);
			int mIntEventIsLike = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE);
			int mIntEventIsGoing = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN);
			
			
			if(!isToAddOldData){
				mArrayListEventDB = new ArrayList<Event>();
			}else{
				if(mArrayListEventDB==null){
					mArrayListEventDB = new ArrayList<Event>();	
				}
			}
			mCursor.moveToFirst();
			do {
				Event Obj = new Event();
				String mEventId = mCursor.getString(mIntEventId);
				String mEventReceivedDate = mCursor.getString(mIntEventReceivedDate);
				String mEventReceivedTime = mCursor.getString(mIntEventReceivedTime);
				Obj.setmId(mEventId);
				Obj.setmTitle(mCursor.getString(mIntEventTitle));
				Obj.setmBy(Utilities.formatBy(mCursor.getString(mIntEventBy), mEventReceivedDate, mEventReceivedTime));
				Obj.setmDaysLeft(Utilities.formatDaysLeft(mCursor.getString(mIntEventStartDate), mCursor.getString(mIntEventStartTime)));
				Obj.setmLikeCount(mCursor.getString(mIntEventLikeCount));
				Obj.setmViewCount(mCursor.getString(mIntEventViewCount));
				Obj.setmGoingCount(mCursor.getString(mIntEventGoingCount));
				Obj.setIsGoingToAttend(mCursor.getString(mIntEventIsGoing));
				Obj.setRead(Boolean.parseBoolean(mCursor.getString(mIntEventIsRead)));
				Obj.setLike(Boolean.parseBoolean(mCursor.getString(mIntEventIsLike)));
				Obj.setmFileType(AppConstants.INTENTCONSTANTS.EVENT);
				
				if(!isFromBroadCastReceiver){
					mArrayListEventDB.add(Obj);	
				}else{
					mArrayListEventDB.add(0,Obj);
				}
			} while (mCursor.moveToNext());
		
			Collections.sort(mArrayListEventDB, new EventSort());
		}
		
		public class EventSort implements Comparator<Event>{
		    @Override
		    public int compare(Event Obj1, Event Obj2) {
		        try{
		        	if(Integer.parseInt(Obj1.getmId()) < Integer.parseInt(Obj2.getmId())){
			            return 1;
			        } else {
			            return -1;
			        }
		        }catch(Exception e){
		        	FileLog.e(TAG, e.toString());
		        	return -1;
		        }
		    }
		}
		
		@SuppressLint("DefaultLocale") private ArrayList<Event> filterListEvent(String mSearchQuery){
			try{
				if(mArrayListEventFilter!=null){
					mArrayListEventFilter.clear();
				}else{
					mArrayListEventFilter  = new ArrayList<>();
				}
				if(mArrayListEventDB!=null && mArrayListEventDB.size() > 0){
					for(int i = 0; i < mArrayListEventDB.size() ; i++){
						Event Obj = mArrayListEventDB.get(i);
						if(isFilterAll){
							if(Obj.getmTitle().toLowerCase().contains(mSearchQuery) || Obj.getmBy().toLowerCase().contains(mSearchQuery)){
								mArrayListEventFilter.add(Obj);
							}
						}
					}
				}
				
				if(mArrayListEventFilter!=null && mArrayListEventFilter.size() > 0){
					return mArrayListEventFilter;
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
			return null;
		}
		
	private void setEmptyData(boolean isToSearchOnWeb, String mSearchQuery) {
		if(!TextUtils.isEmpty(mSearchQuery) && mSearchQuery.contains(AppConstants.SEARCH_FILTER)){
			mEmptyMessageTextView.setText("No results found!");
		}else if(!TextUtils.isEmpty(mSearchQuery)){
			mEmptyMessageTextView.setText("No results found for "+mSearchQuery);
		}if(!TextUtils.isEmpty(mSearchQuery) && mSearchQuery.contains(AppConstants.SEARCH_WEB)){
			mEmptyMessageTextView.setText("Type keyword you want to search for");
			mEmptySearchButton.setText(getResources().getString(R.string.emptyRefreshOnSearch));
		}
		
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
		mEmptyTitleTextView.setVisibility(View.GONE);
		if(isToSearchOnWeb){
			mEmptySearchButton.setVisibility(View.VISIBLE);
			mEmptySearchButton.setText(getResources().getString(R.string.emptyRefreshOnSearch));
		}else{
			mEmptySearchButton.setVisibility(View.GONE);
		}
	}
	
	
	private void logFilters(){
		Log.i(TAG, "isFilterDevice : "+String.valueOf(isFilterDevice));
		Log.i(TAG, "isFilterWeb : "+String.valueOf(isFilterWeb));
		Log.i(TAG, "isFilterAll : "+String.valueOf(isFilterAll));
		Log.i(TAG, "isFilterAudio : "+String.valueOf(isFilterAudio));
		Log.i(TAG, "isFilterBy : "+String.valueOf(isFilterBy));
		Log.i(TAG, "isFilterDoc : "+String.valueOf(isFilterDoc));
		Log.i(TAG, "isFilterFeed : "+String.valueOf(isFilterFeedback));
		Log.i(TAG, "isFilterImage : "+String.valueOf(isFilterImage));
		Log.i(TAG, "isFilterStream : "+String.valueOf(isFilterLiveStream));
		Log.i(TAG, "isFilterQuiz : "+String.valueOf(isFilterQuiz));
		Log.i(TAG, "isFilterText : "+String.valueOf(isFilterText));
		Log.i(TAG, "isFilterVideo : "+String.valueOf(isFilterVideo));
	}

	/*
	 * Drawer Initilization
	 */

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void setDrawerLayout() {
		mResources = getResources();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.drawerRootLayout);
		
		initDrawerLayout();
		disableAllOtherFilters();
		
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
				if(isDrawerEnable){
					if (mDrawerLayout.isDrawerVisible(Gravity.START)) {
						mDrawerLayout.closeDrawer(Gravity.START);
					} else {
						mDrawerLayout.openDrawer(Gravity.START);
					}	
				}else{
					onBackPressed();
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
		} catch (Exception e) {
		}
	}
	
	/*
	 * ContextMenu
	 */
	private void showContextMenu(int mPosition, View mView) {
		try {
			if (mPosition != -1) {
				int mType = 0;;
				String mTitle = " ";
				boolean isRead = false; 
				if(isMobcast){
					if(mArrayListMobcastWeb!=null && mArrayListMobcastWeb.size() > 0){
						mTitle = mArrayListMobcastWeb.get(mPosition).getmTitle();
						isRead = mArrayListMobcastWeb.get(mPosition).isRead();
						mType = Utilities.getMediaType(mArrayListMobcastWeb.get(mPosition).getmFileType());
					}
				}else if(isTraining){
					if(mArrayListTrainingWeb!=null && mArrayListTrainingWeb.size() > 0){
						mTitle = mArrayListTrainingWeb.get(mPosition).getmTitle();
						isRead = mArrayListTrainingWeb.get(mPosition).isRead();
						mType = Utilities.getMediaType(mArrayListTrainingWeb.get(mPosition).getmFileType());
					}
				}else if(isAward){
					if(mArrayListAwardWeb!=null && mArrayListAwardWeb.size() > 0){
						mTitle = mArrayListAwardWeb.get(mPosition).getmName();
						isRead = mArrayListAwardWeb.get(mPosition).isRead();
						mType = AppConstants.TYPE.AWARD;
					}
				}else if(isEvent){
					if(mArrayListEventWeb!=null && mArrayListEventWeb.size() > 0){
						mTitle = mArrayListEventWeb.get(mPosition).getmTitle();
						isRead = mArrayListEventWeb.get(mPosition).isRead();
						mType = AppConstants.TYPE.EVENT;
					}
				} 
				
				ContextMenuFragment newFragment = new ContextMenuFragment(mPosition, mType, mTitle, isRead, mView);
				newFragment.show(getSupportFragmentManager(), "dialog");
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	public class ContextMenuFragment extends DialogFragment {
		int mPosition;
		int mType;
		String mTitle;
		boolean isRead;
		View mView;

		public ContextMenuFragment(int mPosition, int mType, String mTitle,
				boolean isRead, View mView) {
			this.mPosition = mPosition;
			this.mTitle = mTitle;
			this.mType = mType;
			this.isRead = isRead;
			this.mView = mView;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return getContextMenu(mPosition, mType, mTitle, isRead, mView);
		}
	}

	private BottomSheet getContextMenu(final int mPosition, int mType,
			final String mTitle, boolean isRead, final View mView) {
		BottomSheet mBottomSheet;
		mBottomSheet = new BottomSheet.Builder(SearchActivity.this)
				.icon(Utilities.getRoundedBitmapForContextMenu(mType))
				.title(mTitle).sheet(R.menu.context_menu_search).build();
		final Menu menu = mBottomSheet.getMenu();

		SpannableString mSpannabledSave = new SpannableString(getResources()
				.getString(R.string.context_menu_save));

		mSpannabledSave.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
				mSpannabledSave.length(), 0);

		menu.getItem(0).setTitle(mSpannabledSave);

		/**
		 * Save
		 */
		menu.getItem(0).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						contextMenuSave(mPosition, mView);
						return true;
					}
				});

		return mBottomSheet;
	}

	private void contextMenuSave(int mPosition, View mView) {
		try {
			saveInDB(mPosition);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void saveInDB(int mPosition){
		if(isMobcast){
			saveMobcastInDB(mPosition);	
		}else if(isTraining){
			saveTrainingInDB(mPosition);
		}else if(isAward){
			saveAwardIdDB(mPosition);
		}else if(isEvent){
			saveEventIdDB(mPosition);
		}
	}
	
	private void saveMobcastInDB(int mPosition){
		try{
			if(mArrayListMobcastWeb!=null && mArrayListMobcastWeb.size()>0){
				Mobcast Obj = mArrayListMobcastWeb.get(mPosition);
				if(!Obj.getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FEEDBACK)){
					ContentValues values = new ContentValues();
					
					String mMobcastId = Obj.getmId();
//					String mDate = Obj.getmDate();
//					String mTime = Obj.getmTime();
					String mDate = Utilities.getTodayDate();
					String mTime = Utilities.getTodayTime();
					String mExpiryDate = Obj.getmExpiryDate();
					String mExpiryTime = Obj.getmExpiryTime();
					String mType = Obj.getmFileType();
					
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, mMobcastId);
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE, Obj.getmTitle());
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY, Obj.getmBy().substring(Obj.getmBy().indexOf(":")+1, Obj.getmBy().indexOf("-")));
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC, Obj.getmDescription());
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, Obj.getmViewCount());
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, Obj.getmLikeCount());
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE, mDate);
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME, mTime);
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE, mType);
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, String.valueOf(Obj.isRead()));
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, String.valueOf(Obj.isLike()));
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING, String.valueOf(Obj.isSharing()));
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE, String.valueOf(Obj.isDownloadable()));
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK, Obj.getmLink());
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE, mExpiryDate);
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME, mExpiryTime);
					values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
					
					Cursor mIsExistCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID},  DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						return;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					
					Uri isInsertUri = getContentResolver().insert(DBConstant.Mobcast_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					int mIntType = Utilities.getMediaType(mType);
					
					if(mIntType!= AppConstants.TYPE.FEEDBACK && mIntType!= AppConstants.TYPE.TEXT){
						ArrayList<MobcastFileInfo> mListFileInfo = Obj.getmFileInfo();
						for (int i = 0; i < mListFileInfo.size(); i++) {
							MobcastFileInfo fileInfoObj= mListFileInfo.get(i);
							ContentValues valuesFileInfo = new ContentValues();
							String mThumbnailLink = fileInfoObj.getmThumbnailLink();
							String mFileLink = fileInfoObj.getmFileLink();
							String mFileName = fileInfoObj.getmFileName();
							String mIsDefault = fileInfoObj.getmFileIsDefault();
							String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID, mMobcastId);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK, fileInfoObj.getmFileLink());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID, fileInfoObj.getmFileId());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG, fileInfoObj.getmFileLanguages());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE, fileInfoObj.getmFileSize());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION, fileInfoObj.getmDuration());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES, fileInfoObj.getmPages());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, mIsDefault);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM, mFileLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE, mFileLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION, fileInfoObj.getmDuration());
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME, mFileName);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK, mThumbnailLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH, mThumbnailName);
							
							getContentResolver().insert(DBConstant.Mobcast_File_Columns.CONTENT_URI, valuesFileInfo);
						}
					}
					
					if(isInsertedInDB){
						AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.save_message));
					}
				}else{
					saveFeedback(Obj.getmId());
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void saveFeedback(String mId){
		Cursor mIsExistCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID},  DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId}, null);
		if(mIsExistCursor!=null && mIsExistCursor.getCount() == 0){
			fetchDataFromApi(mId, AppConstants.INTENTCONSTANTS.MOBCAST);
		}
		if(mIsExistCursor!=null)
			mIsExistCursor.close();	
	}
	
	private void saveTrainingInDB(int mPosition){
		try{
			if(mArrayListTrainingWeb!=null && mArrayListTrainingWeb.size()>0){
				Training Obj = mArrayListTrainingWeb.get(mPosition);
				if(!Obj.getmFileType().equalsIgnoreCase(AppConstants.TRAINING.QUIZ)){
					ContentValues values = new ContentValues();
					
					String mTrainingId = Obj.getmId();
//					String mDate = Obj.getmDate();
//					String mTime = Obj.getmTime();
					String mDate = Utilities.getTodayDate();
					String mTime = Utilities.getTodayTime();
					String mExpiryDate = Obj.getmExpiryDate();
					String mExpiryTime = Obj.getmExpiryTime();
					String mType = Obj.getmFileType();
					
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_ID, mTrainingId);
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE, Obj.getmTitle());
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_BY, Obj.getmBy().substring(Obj.getmBy().indexOf(":")+1, Obj.getmBy().indexOf("-")));
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DESC, Obj.getmDescription());
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, Obj.getmViewCount());
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, Obj.getmLikeCount());
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE, mDate);
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME, mTime);
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE, mType);
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, String.valueOf(Obj.isRead()));
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, String.valueOf(Obj.isLike()));
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING, String.valueOf(Obj.isSharing()));
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE, String.valueOf(Obj.isDownloadable()));
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LINK, Obj.getmLink());
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE, mExpiryDate);
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME, mExpiryTime);
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
					
					Cursor mIsExistCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID},  DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						return;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					
					Uri isInsertUri = getContentResolver().insert(DBConstant.Training_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					int mIntType = Utilities.getMediaType(mType);
					
					if(mIntType!= AppConstants.TYPE.QUIZ && mIntType!= AppConstants.TYPE.TEXT){
						ArrayList<TrainingFileInfo> mListFileInfo = Obj.getmFileInfo();
						for (int i = 0; i < mListFileInfo.size(); i++) {
							TrainingFileInfo fileInfoObj= mListFileInfo.get(i);
							ContentValues valuesFileInfo = new ContentValues();
							String mThumbnailLink = fileInfoObj.getmThumbnailLink();
							String mFileLink = fileInfoObj.getmFileLink();
							String mFileName = fileInfoObj.getmFileName();
							String mIsDefault = fileInfoObj.getmFileIsDefault();
							String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_ID, mTrainingId);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK, fileInfoObj.getmFileLink());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID, fileInfoObj.getmFileId());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG, fileInfoObj.getmFileLanguages());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE, fileInfoObj.getmFileSize());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION, fileInfoObj.getmDuration());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES, fileInfoObj.getmPages());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, mIsDefault);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM, mFileLink);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE, mFileLink);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION, fileInfoObj.getmDuration());
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME, mFileName);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK, mThumbnailLink);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH, mThumbnailName);
							
							getContentResolver().insert(DBConstant.Training_File_Columns.CONTENT_URI, valuesFileInfo);
						}
					}
					
					if(isInsertedInDB){
						AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.save_message));
					}
				}else{
					saveQuiz(Obj.getmId());	
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void saveQuiz(String mId){
		Cursor mIsExistCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID},  DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId}, null);
		if(mIsExistCursor!=null && mIsExistCursor.getCount() == 0){
			fetchDataFromApi(mId, AppConstants.INTENTCONSTANTS.TRAINING);
		}
		if(mIsExistCursor!=null)
			mIsExistCursor.close();	
	}
	
	private void saveAwardIdDB(int mPosition){
		try{
			if(mArrayListAwardWeb!=null && mArrayListAwardWeb.size()>0){
				if(mArrayListAwardDB.get(mPosition).getmId().equalsIgnoreCase(mArrayListAwardWebSave.get(mPosition).getmAwardId())){
					AwardSave Obj = mArrayListAwardWebSave.get(mPosition);
					String mAwardId = Obj.getmAwardId();
					
					Cursor mIsExistCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, new String[]{DBConstant.Award_Columns.COLUMN_ID},  DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mAwardId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						return;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					
					String mDate = Utilities.getTodayDate();
					String mTime = Utilities.getTodayTime();
					String mExpiryDate = Obj.getmExpiryDate();
					String mExpiryTime = Obj.getmExpiryTime();
					String mThumbnailPath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, true,Utilities.getFileName(Obj.getmThumbnailLink()));
					String mFilePath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, false,Utilities.getFileName(Obj.getmFileLink()));
					ContentValues values = new ContentValues();
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_ID,mAwardId);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION,Obj.getmAwardTitle());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_NAME,Obj.getmAwardName());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE,Obj.getmAwardMobile());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL,Obj.getmAwardEmail());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE,Obj.getmAwardDate());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE,mDate);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_TIME,mTime);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED,Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION,Obj.getmAwardDesc());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_CITY,Obj.getmAwardCity());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT,Obj.getmAwardDep());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO,Obj.getmAwardViewCount());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO,Obj.getmAwardLikeCount());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO,Obj.getmAwardCongratulateCount());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ,Obj.ismIsRead());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE,Obj.ismIsLike());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE,Obj.ismIsCongratulate());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING,Obj.ismIsSharing());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK,Obj.getmFileLink());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH,mFilePath);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE,Obj.getmFileSize());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK,Obj.getmThumbnailLink());
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH,mThumbnailPath);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE,mExpiryDate);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_TIME,mExpiryTime);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED,Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
					
					Uri isInsertUri = getContentResolver().insert(DBConstant.Award_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					if(isInsertedInDB){
						AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.save_message));
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void saveEventIdDB(int mPosition){
		try{
			if(mArrayListEventWeb!=null && mArrayListEventWeb.size()>0){
				if(mArrayListEventDB.get(mPosition).getmId().equalsIgnoreCase(mArrayListEventWebSave.get(mPosition).getmEventId())){
					EventSave Obj = mArrayListEventWebSave.get(mPosition);
					String mEventId = Obj.getmEventId();
					
					Cursor mIsExistCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, new String[]{DBConstant.Event_Columns.COLUMN_ID},  DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mEventId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						return;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					
					String mDate = Utilities.getTodayDate();
					String mTime = Utilities.getTodayTime();
					String mExpiryDate = Obj.getmEventExpiryDate();
					String mExpiryTime = "00:00:00";
					String mFilePath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, false,Utilities.getFileName(Obj.getmFileLink()));
					ContentValues values = new ContentValues();
					
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_ID, mEventId);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_TITLE, Obj.getmEventTitle());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_BY, Obj.getmEventBy());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_DESC, Obj.getmEventDesc());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, Obj.getmEventGoingCount());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, Obj.getmEventLikeCount());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO, Obj.getmEventReadCount());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, Obj.getmEventInvitedCount());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE, mDate);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME, mTime);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, Obj.isRead());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, Obj.isLike());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING, Obj.isSharing());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, Obj.isJoin());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK, Obj.getmFileLink());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH, mFilePath);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE, Obj.getmFileSize());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE, Obj.getmEventStartDate());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE, Obj.getmEventEndDate());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME, Obj.getmEventStartTime());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME, Obj.getmEventEndTime());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_VENUE, Obj.getmEventVenue());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_MAP, Obj.getmEventMap());
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE, mExpiryDate);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_TIME, mExpiryTime);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
					
					
					Uri isInsertUri = getContentResolver().insert(DBConstant.Event_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					if(isInsertedInDB){
						AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.save_message));
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * AsyncTask : Search Online
	 */
	@SuppressLint("NewApi") 
	private void searchFromApi(String mSearchTerm ,boolean sortByAsc){//sortByAsc:true-> new data //sortByAsc:false->Old Data
		if(isFilterWeb){
			if(Utilities.isInternetConnected()){
				if(AndroidUtilities.isAboveIceCreamSandWich()){
					new AsyncSearchTask(mSearchTerm,sortByAsc).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				}else{
					new AsyncSearchTask(mSearchTerm,sortByAsc).execute();
				}
			}else{
				Toast.makeText(SearchActivity.this, getResources().getString(R.string.internet_unavailable), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private String getFiltersForApi(){
		String mFilter = "";
		boolean isAdded = false;
		if(isFilterAudio){
			mFilter = AppConstants.MOBCAST.AUDIO;
			isAdded = true;
		}
		
		if(isFilterDoc){
			if(!isAdded){
				mFilter+=AppConstants.MOBCAST.PDF + ","+AppConstants.MOBCAST.PPT + ","+AppConstants.MOBCAST.DOC + ","+AppConstants.MOBCAST.XLS;	
			}else{
				mFilter+=","+AppConstants.MOBCAST.PDF + ","+AppConstants.MOBCAST.PPT + ","+AppConstants.MOBCAST.DOC + ","+AppConstants.MOBCAST.XLS;				
			}
		}
		
		if(isFilterFeedback){
			if(!isAdded){
				mFilter+=AppConstants.MOBCAST.FEEDBACK;	
			}else{
				mFilter+=","+AppConstants.MOBCAST.FEEDBACK;				
			}
		}
		
		if(isFilterImage){
			if(!isAdded){
				mFilter+=AppConstants.MOBCAST.IMAGE;	
			}else{
				mFilter+=","+AppConstants.MOBCAST.IMAGE;				
			}
		}
		
		if(isFilterLiveStream){
			if(!isAdded){
				mFilter+=AppConstants.MOBCAST.STREAM;
			}else{
				mFilter+=","+AppConstants.MOBCAST.STREAM;				
			}
		}
		
		if(isFilterQuiz){
			if(!isAdded){
				mFilter+=AppConstants.TRAINING.QUIZ;
			}else{
				mFilter+=","+AppConstants.TRAINING.QUIZ;				
			}
		}
		
		if(isFilterText){
			if(!isAdded){
				mFilter+=AppConstants.MOBCAST.TEXT;
			}else{
				mFilter+=","+AppConstants.MOBCAST.TEXT;				
			}
		}
		
		if(isFilterVideo){
			if(!isAdded){
				mFilter+=AppConstants.MOBCAST.VIDEO;
			}else{
				mFilter+=","+AppConstants.MOBCAST.VIDEO;				
			}
		}
		
		if(isFilterAll){
			mFilter = "all";
		}
		return mFilter;
	}
	
	private String getFilterSearchBy(){
		return mSearchTerm;
	}
	
	private String getIdForWebSearch(boolean sortByAsc){
		String mLastId = "0";
		if(!sortByAsc){
			if(isMobcast){
				mLastId = mArrayListMobcastWeb.get(mArrayListMobcastWeb.size() - 2)
						.getmId();
			}else if(isTraining){
				mLastId = mArrayListTrainingWeb.get(mArrayListTrainingWeb.size() - 2)
						.getmId();
			}else if(isAward){
				mLastId = mArrayListAwardWeb.get(mArrayListAwardWeb.size() - 1)
						.getmId();
			}else if(isEvent){
				mLastId = mArrayListEventWeb.get(mArrayListEventWeb.size() - 1)
						.getmId();
			}
		 }
		return mLastId;
	}
	
	private String apiSearch(String mSearchTerm, boolean sortByAsc) {
		try {
				String mFilter = getFiltersForApi();
				String mSearchBy = getFilterSearchBy();
				String mLastId=getIdForWebSearch(sortByAsc); 
				JSONObject jsonObj = JSONRequestBuilder.getPostSearchData(mCategory, mLastId, mSearchTerm, mSearchBy!=null?mSearchBy:" ", mFilter!=null?mFilter :"", AppConstants.BULK, sortByAsc);
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SEARCH, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_SEARCH, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi, boolean sortByAsc) {
		try{
			if(!TextUtils.isEmpty(mResponseFromApi)){
				if(isMobcast){
					parseDataForMobcast(mResponseFromApi, sortByAsc);
				}else if(isTraining){
					parseDataForTraining(mResponseFromApi, sortByAsc);
				}else if(isAward){
					parseDataForAward(mResponseFromApi, sortByAsc);
				}else if(isEvent){
					parseDataForEvent(mResponseFromApi, sortByAsc);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForMobcast(String mResponseFromApi, boolean isNewData){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcast);
				
				if(mArrayListMobcastWeb!=null){
					if (mArrayListMobcastWeb.size() > 0 && isNewData) {
						mArrayListMobcastWeb.clear();
					}
				}
				
				if(mArrayListMobcastWeb==null){
					mArrayListMobcastWeb = new ArrayList<>();
				}
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					Mobcast mMobcastObj = new Mobcast();
					String mMobcastId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastId);
					String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastType);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastTime);
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastExpiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastExpiryTime);
					mMobcastObj.setmId(mMobcastId);
					mMobcastObj.setmTitle(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastTitle));
					mMobcastObj.setmBy(Utilities.formatBy(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastBy), mDate, mTime));
					mMobcastObj.setmDescription(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastDescription));
					mMobcastObj.setmViewCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastViewCount));
					mMobcastObj.setmLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLikeCount));
					mMobcastObj.setmFileType(mType);
					mMobcastObj.setmDate(mDate);
					mMobcastObj.setmTime(mTime);
					mMobcastObj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsRead)));
					mMobcastObj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsLiked)));
					mMobcastObj.setSharing(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsSharing)));
					mMobcastObj.setDownloadable(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsDownloadable)));
					mMobcastObj.setmLink(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLink));
					mMobcastObj.setmExpiryDate(mExpiryDate);
					mMobcastObj.setmExpiryTime(mExpiryTime);
					
					final int mIntType = Utilities.getMediaType(mType);
					
					if (mIntType != AppConstants.TYPE.FEEDBACK && mIntType != AppConstants.TYPE.TEXT) {
						JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFileInfo);
						ArrayList<MobcastFileInfo> mMobcastFileInfoList = new ArrayList<MobcastFileInfo>();
						for (int j = 0; j < mJSONArrMobFileObj.length(); j++) {
							JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(j);
							MobcastFileInfo mMobcastFileInfoObj = new MobcastFileInfo();
							final String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastThumbnail);
							String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLink);
							final String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileName);
							String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastisDefault);
							final String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							mMobcastFileInfoObj.setmFileId(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileId));
							mMobcastFileInfoObj.setmFileLink(mFileLink);
							mMobcastFileInfoObj.setmFilePath(Utilities.getFilePath(mIntType, false, mFileName));
							mMobcastFileInfoObj.setmFileLanguages(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLang));
							mMobcastFileInfoObj.setmFileSize(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileSize));
							mMobcastFileInfoObj.setmDuration(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileDuration));
							mMobcastFileInfoObj.setmPages(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFilePages));
							mMobcastFileInfoObj.setmFileIsDefault(mIsDefault);
							mMobcastFileInfoObj.setmFileName(mFileName);
							mMobcastFileInfoObj.setmThumbnailLink(mThumbnailLink);
							mMobcastFileInfoObj.setmThumbnailPath(mThumbnailName);
							mMobcastFileInfoList.add(mMobcastFileInfoObj);
						}
						mMobcastObj.setmFileInfo(mMobcastFileInfoList);
					}else if(mIntType == AppConstants.TYPE.FEEDBACK){
						JSONArray mJSONArrMobFeedObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFeedbackInfo);
						JSONObject mJSONObjMobFeedObj = mJSONArrMobFeedObj.getJSONObject(0);
						ArrayList<MobcastFileInfo> mMobcastFeedbackInfoList = new ArrayList<>();
						MobcastFileInfo feedbackObj = new MobcastFileInfo();
						feedbackObj.setmFileIsDefault(mJSONObjMobFeedObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
						feedbackObj.setmPages(mJSONArrMobFeedObj.length() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));
						mMobcastFeedbackInfoList.add(feedbackObj);
						mMobcastObj.setmFileInfo(mMobcastFeedbackInfoList);
					}
					
					mArrayListMobcastWeb.add(mMobcastObj);
				}
				
				if(mArrayListMobcastWeb!=null && mArrayListMobcastWeb.size()>0){
					if(mMobcastAdapter==null){
						setRecyclerMobcastAdapter(mArrayListMobcastWeb);
					}
					mMobcastAdapter.addMobcastObjList(mArrayListMobcastWeb);
					mRecyclerView.setVisibility(View.VISIBLE);
					mEmptyFrameLayout.setVisibility(View.GONE);
					mRecyclerView.getAdapter().notifyDataSetChanged();
					setRecyclerMobcastScrollListener();
					setRecyclerMobcastAdapterLongPressListener();
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForTraining(String mResponseFromApi, boolean isNewData){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.training);
				
				if(mArrayListTrainingWeb!=null){
					if (mArrayListTrainingWeb.size() > 0 && isNewData) {
						mArrayListTrainingWeb.clear();
					}
				}
				
				if(mArrayListTrainingWeb==null){
					mArrayListTrainingWeb = new ArrayList<>();
				}
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					Training mTrainingObj = new Training();
					String mTrainingId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingId);
					String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingType);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingTime);
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingExpiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingExpiryTime);
					mTrainingObj.setmId(mTrainingId);
					mTrainingObj.setmTitle(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingTitle));
					mTrainingObj.setmBy(Utilities.formatBy(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingBy), mDate, mTime));
					mTrainingObj.setmDescription(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingDescription));
					mTrainingObj.setmViewCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingViewCount));
					mTrainingObj.setmLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLikeCount));
					mTrainingObj.setmFileType(mType);
					mTrainingObj.setmDate(mDate);
					mTrainingObj.setmTime(mTime);
					mTrainingObj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsRead)));
					mTrainingObj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsLiked)));
					mTrainingObj.setSharing(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsSharing)));
					mTrainingObj.setDownloadable(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsDownloadable)));
					mTrainingObj.setmLink(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLink));
					mTrainingObj.setmExpiryDate(mExpiryDate);
					mTrainingObj.setmExpiryTime(mExpiryTime);
					
					final int mIntType = Utilities.getMediaType(mType);
					
					if (mIntType != AppConstants.TYPE.QUIZ && mIntType != AppConstants.TYPE.TEXT) {
						JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingFileInfo);
						ArrayList<TrainingFileInfo> mTrainingFileInfoList = new ArrayList<TrainingFileInfo>();
						for (int j = 0; j < mJSONArrMobFileObj.length(); j++) {
							JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(j);
							TrainingFileInfo mTrainingFileInfoObj = new TrainingFileInfo();
							final String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingThumbnail);
							String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileLink);
							final String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileName);
							String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingisDefault);
							final String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							mTrainingFileInfoObj.setmFileLink(mFileLink);
							mTrainingFileInfoObj.setmFilePath(Utilities.getFilePath(mIntType, false, mFileName));
							mTrainingFileInfoObj.setmFileLanguages(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileLang));
							mTrainingFileInfoObj.setmFileSize(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileSize));
							mTrainingFileInfoObj.setmDuration(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileDuration));
							mTrainingFileInfoObj.setmPages(mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFilePages));
							mTrainingFileInfoObj.setmFileIsDefault(mIsDefault);
							mTrainingFileInfoObj.setmFileName(mFileName);
							mTrainingFileInfoObj.setmThumbnailLink(mThumbnailLink);
							mTrainingFileInfoObj.setmThumbnailPath(mThumbnailName);
							mTrainingFileInfoList.add(mTrainingFileInfoObj);
						}
						mTrainingObj.setmFileInfo(mTrainingFileInfoList);
					}else if(mIntType == AppConstants.TYPE.QUIZ){
						JSONArray mJSONArrMobFeedObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingQuizInfo);
						JSONObject mJSONObjMobFeedObj = mJSONArrMobFeedObj.getJSONObject(0);
						ArrayList<TrainingFileInfo> mTrainingFeedbackInfoList = new ArrayList<>();
						TrainingFileInfo quizObj = new TrainingFileInfo();
						quizObj.setmFileIsDefault(mJSONObjMobFeedObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
						quizObj.setmPages(mJSONArrMobFeedObj.length() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));
						mTrainingFeedbackInfoList.add(quizObj);
						mTrainingObj.setmFileInfo(mTrainingFeedbackInfoList);
					}
					
					mArrayListTrainingWeb.add(mTrainingObj);
				}
				
				if(mArrayListTrainingWeb!=null && mArrayListTrainingWeb.size()>0){
					if(mTrainingAdapter==null){
						if(mMobcastAdapter==null){
							setRecyclerTrainingAdapter(mArrayListTrainingWeb);
						}
					}
					mTrainingAdapter.addTrainingObjList(mArrayListTrainingWeb);
					mRecyclerView.setVisibility(View.VISIBLE);
					mEmptyFrameLayout.setVisibility(View.GONE);
					mRecyclerView.getAdapter().notifyDataSetChanged();
					setRecyclerTrainingScrollListener();
					setRecyclerTrainingAdapterLongPressListener();
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForAward(String mResponseFromApi, boolean isNewData) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.award);

				if(mArrayListAwardWeb!=null){
					if (mArrayListAwardWeb.size() > 0 && isNewData) {
						mArrayListAwardWeb.clear();
						mArrayListAwardWebSave.clear();
					}
				}
				
				if(mArrayListAwardWeb==null){
					mArrayListAwardWeb = new ArrayList<>();
					mArrayListAwardWebSave = new ArrayList<>();
				}
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					Award Obj = new Award();
					String mAwardId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardId);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardSentDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardSentTime);
					String mThumbnailLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCoverThumbnail);
					String mThumbnailPath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, true,Utilities.getFileName(mThumbnailLink));
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardExpiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardExpiryTime);
					String mFileLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCoverLink);
					String mFilePath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, false,Utilities.getFileName(mFileLink));
					
					Obj.setmId(mAwardId);
					Obj.setmName(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverName));
					Obj.setmRecognition(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardTitle));
					Obj.setmThumbLink(mThumbnailLink);
					Obj.setmThumbPath(mThumbnailPath);
					Obj.setmReceivedDate(mDate);
					Obj.setmReceivedTime(mTime);
					Obj.setmAwardDate(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDate));
					Obj.setmLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardLikeCount));
					Obj.setmViewCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReadCount));
					Obj.setmCongratulatedCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCongratulatedCount));
					Obj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsRead)));
					Obj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsLike)));
					Obj.setCongratulated(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsCongratulated)));
					Obj.setmFileType(AppConstants.INTENTCONSTANTS.AWARD);
					
					mArrayListAwardWeb.add(Obj);
					
					AwardSave saveObj = new AwardSave();
					
					saveObj.setmAwardId(mAwardId);
					saveObj.setmAwardName(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverName));
					saveObj.setmAwardTitle(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardTitle));
					saveObj.setmAwardMobile(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverMobile));
					saveObj.setmAwardEmail(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverEmail));
					saveObj.setmAwardReceivedDate(mDate);
					saveObj.setmAwardReceivedTime(mTime);
					saveObj.setmAwardDesc(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDescription));
					saveObj.setmAwardCity(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCity));
					saveObj.setmAwardDep(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDepartment));
					saveObj.setmAwardViewCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReadCount));
					saveObj.setmAwardLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardLikeCount));
					saveObj.setmAwardCongratulateCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCongratulatedCount));
					saveObj.setmIsRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsRead)));
					saveObj.setmIsLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsLike)));
					saveObj.setmIsCongratulate(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsCongratulated)));
					saveObj.setmIsSharing(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsSharing)));
					saveObj.setmFileLink(mFileLink);
					saveObj.setmFileSize(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardFileSize));
					saveObj.setmThumbnailLink(mThumbnailLink);
					//saveObj.setmFilemFilePath);
					//saveObj.setmTHmThumbnailPath);
					//values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED,Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
					//values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED,Utilities.getMilliSecond(mDate, mTime));
					saveObj.setmExpiryDate(mExpiryDate);
					saveObj.setmExpiryTime(mExpiryTime);

					
					mArrayListAwardWebSave.add(saveObj);
				}

				if(mArrayListAwardWeb!=null && mArrayListAwardWeb.size()>0){
					if(mAwardAdapter==null){
						setRecyclerAwardAdapter(mArrayListAwardWeb);
					}
					mAwardAdapter.addAwardObjList(mArrayListAwardWeb);
					mRecyclerView.setVisibility(View.VISIBLE);
					mEmptyFrameLayout.setVisibility(View.GONE);
					mRecyclerView.getAdapter().notifyDataSetChanged();
					setRecyclerAwardScrollListener();
					setRecyclerAwardAdapterLongPressListener();
				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForEvent(String mResponseFromApi, boolean isNewData){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.event);
				
				if(mArrayListEventWeb!=null){
					if (mArrayListEventWeb.size() > 0 && isNewData) {
						mArrayListEventWeb.clear();
						mArrayListEventWebSave.clear();
					}
				}
				
				if(mArrayListEventWeb==null){
					mArrayListEventWeb = new ArrayList<>();
					mArrayListEventWebSave = new ArrayList<>();
				}
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					Event Obj = new Event();
					String mEventId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventId);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventSentDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventSentTime);
					
					Obj.setmId(mEventId);
					Obj.setmTitle(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventTitle));
					Obj.setmBy(Utilities.formatBy(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventBy), mDate, mTime));
					Obj.setmDaysLeft(Utilities.formatDaysLeft(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventStartDate), mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFromTime)));
					Obj.setmLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventLikeCount));
					Obj.setmViewCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventReadCount));
					Obj.setmGoingCount( mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsGoing));
					Obj.setIsGoingToAttend(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventInvitedCount));
					Obj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsRead)));
					Obj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsLiked)));
					Obj.setmFileType(AppConstants.INTENTCONSTANTS.EVENT);
					
					mArrayListEventWeb.add(Obj);
					EventSave saveObj = new EventSave();
					saveObj.setmEventId(mEventId);
					saveObj.setmEventTitle(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventTitle));
					saveObj.setmEventBy(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventBy));
					saveObj.setmEventDesc(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventDescription));
					saveObj.setmEventGoingCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventGoingCount));
					saveObj.setmEventLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventLikeCount));
					saveObj.setmEventReadCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventReadCount));
					saveObj.setmEventInvitedCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventInvitedCount));
					saveObj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsRead)));
					saveObj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsLiked)));
					saveObj.setSharing(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsSharing)));
					saveObj.setJoin(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsGoing)));
					saveObj.setmFileLink(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventCoverLink));
					saveObj.setmFileSize(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFileSize));
					saveObj.setmEventStartDate(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventStartDate));
					saveObj.setmEventEndDate(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventEndDate));
					saveObj.setmEventStartTime(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFromTime));
					saveObj.setmEventEndTime(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventToTime));
					saveObj.setmEventVenue(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventVenue));
					saveObj.setmEventMap(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventMap));
					saveObj.setmEventExpiryDate(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventExpiryDate));
					saveObj.setmEventExpriyTime(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventExpiryTime));
					
					mArrayListEventWebSave.add(saveObj);
				}
				
				if(mArrayListEventWeb!=null && mArrayListEventWeb.size()>0){
					if(mEventAdapter==null){
						setRecyclerEventAdapter(mArrayListEventWeb);
					}
					mEventAdapter.addEventObjList(mArrayListEventWeb);
					mRecyclerView.setVisibility(View.VISIBLE);
					mEmptyFrameLayout.setVisibility(View.GONE);
					mRecyclerView.getAdapter().notifyDataSetChanged();
					setRecyclerEventScrollListener();
					setRecyclerEventAdapterLongPressListener();
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	

	public class AsyncSearchTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private String mSearchTerm = "";
		private boolean sortByAsc = false;

		public AsyncSearchTask(String mSearchTerm, boolean sortByAsc){
			this.mSearchTerm = mSearchTerm;
			this.sortByAsc = sortByAsc;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(sortByAsc){
				mRecyclerView.setVisibility(View.GONE);
				mEmptyFrameLayout.setVisibility(View.GONE);
				mProgressFrameLayout.setVisibility(View.VISIBLE);
				mGooglePlaySearchProgress.setVisibility(View.VISIBLE);
				mProgressSearchTextView.setText(mSearchTerm);
			}else{
				if(isMobcast){
					Mobcast Obj = new Mobcast();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					if(mArrayListMobcastWeb!=null && mArrayListMobcastWeb.size()>0){
						mArrayListMobcastWeb.add(Obj);
					}
				}else if(isTraining){
					Training Obj = new Training();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					if(mArrayListTrainingWeb!=null && mArrayListTrainingWeb.size()>0){
						mArrayListTrainingWeb.add(Obj);
					}
				}else if(isAward){
					Award Obj = new Award();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					if(mArrayListAwardWeb!=null && mArrayListAwardWeb.size()>0){
						mArrayListAwardWeb.add(Obj);
					}
				}else if(isEvent){
					Event Obj = new Event();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					if(mArrayListEventWeb!=null && mArrayListEventWeb.size()>0){
						mArrayListEventWeb.add(Obj);
					}
				}  
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiSearch(mSearchTerm,sortByAsc);
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
				if(mProgressFrameLayout!=null){
					mProgressFrameLayout.setVisibility(View.GONE);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if(sortByAsc){
					mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
				}
				
				if(!sortByAsc){
					mLoadMore = false;
					if(isMobcast){
						mArrayListMobcastWeb.remove(mArrayListMobcastWeb.size()-1);
					}else if(isTraining){
						mArrayListTrainingWeb.remove(mArrayListTrainingWeb.size()-1);
					}else if(isAward){
						mArrayListAwardWeb.remove(mArrayListAwardWeb.size()-1);
					}else if(isEvent){
						mArrayListEventWeb.remove(mArrayListEventWeb.size()-1);
					}
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}
				
				mProgressFrameLayout.setVisibility(View.GONE);
				if (isSuccess) {
					parseDataFromApi(mResponseFromApi,sortByAsc);
				} else {
					if(sortByAsc){
						if(isFilterDevice){
							setEmptyData(true, mSearchTerm);
						}else{
							setEmptyData(false, mSearchTerm);
						}
					}
					
					if(!sortByAsc){
						mErrorMessage = Utilities.getErrorMessageFromApi(mResponseFromApi);
						AndroidUtilities.showSnackBar(SearchActivity.this, mErrorMessage != null ? mErrorMessage : " ");
					}
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}
	
	/**
	 * Fetch Data From Api
	 * @author Vikalp Patel(VikalpPatelCE)
	 *
	 */
	
	@SuppressLint("NewApi") private void fetchDataFromApi(String _id, String mBroadcastType){
		if(Utilities.isInternetConnected()){
			if(AndroidUtilities.isAboveIceCreamSandWich()){
				new AsyncFetchData(_id, mBroadcastType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			}else{
				new AsyncFetchData(_id, mBroadcastType).execute();
			}
		}
	}
	
	private String apiFetchPushData(String _id, String mBroadcastType) {
		try {
			if (!TextUtils.isEmpty(_id) && !TextUtils.isEmpty(mBroadcastType)) {
				JSONObject jsonObj = JSONRequestBuilder.getPostFetchPushData(
						mBroadcastType, _id);
				if (BuildVars.USE_OKHTTP) {
					return RetroFitClient.postJSON(new OkHttpClient(),
							AppConstants.API.API_FETCH_PUSH_DATA,
							jsonObj.toString(), TAG);
				} else {
					return RestClient.postJSON(
							AppConstants.API.API_FETCH_PUSH_DATA, jsonObj, TAG);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public class AsyncFetchData extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private String _id;
		private String mBroadcastType;
		private MobcastProgressDialog mProgressDialog;

		public AsyncFetchData(String _id, String mBroadcastType){
			this._id = _id;
			this.mBroadcastType = mBroadcastType;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(SearchActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiFetchPushData(_id, mBroadcastType);
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
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
				
				if (isSuccess) {
					parseDataFromApiToSaveInDB(mResponseFromApi, mBroadcastType);
				} else {
					mErrorMessage = Utilities.getErrorMessageFromApi(mResponseFromApi);
					AndroidUtilities.showSnackBar(SearchActivity.this, mErrorMessage != null ? mErrorMessage : " ");
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}
	
	private void parseDataFromApiToSaveInDB(String mResponseFromApi, String mBroadcastType){
		try{
			if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONObject mJSONMobObj = mJSONObj.getJSONObject(AppConstants.API_KEY_PARAMETER.mobcast);
				String mMobcastId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastId);
				String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastType);
				String mDate = Utilities.getTodayDate();
				String mTime = Utilities.getTodayTime();
				String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastExpiryDate);
				String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastExpiryTime);
				ContentValues values = new ContentValues();
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, mMobcastId);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastTitle));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastBy));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastDescription));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastViewCount));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLikeCount));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE, mDate);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME, mTime);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE, mType);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsRead));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsLiked));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsSharing));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsDownloadable));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLink));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE, mExpiryDate);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME, mExpiryTime);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
				
				Uri isInsertUri = getContentResolver().insert(DBConstant.Mobcast_Columns.CONTENT_URI, values);
				boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
				
				if(!isInsertedInDB){
					return;
				}
				
				int mIntType = Utilities.getMediaType(mType);
				
				if(mIntType == AppConstants.TYPE.FEEDBACK){
					JSONArray mJSONArrMobFeedObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFeedbackInfo);
					
					for (int k = 0; k < mJSONArrMobFeedObj.length(); k++) {
							JSONObject mJSONFeedbackObj = mJSONArrMobFeedObj.getJSONObject(k);
							ContentValues valuesFeedbackInfo = new ContentValues();
							String mFeedbackId = mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackId);
							String mFeedbackQid = mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueId);
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID, mFeedbackId);
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID, mFeedbackQid);
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_TYPE, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueType));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueTitle));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ATTEMPT, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption1));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption2));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption3));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption4));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption5));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption6));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption7));
							
							Uri isInsertFeedbackInfoUri = getContentResolver().insert(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, valuesFeedbackInfo);
							Utilities.checkWhetherInsertedOrNot(TAG,isInsertFeedbackInfoUri);
					}
					
					if(isInsertedInDB){
						AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.save_message));
					}
				}
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONObject mJSONMobObj = mJSONObj.getJSONObject(AppConstants.API_KEY_PARAMETER.training);
				String mTrainingId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingId);
				String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingType);
				String mDate = Utilities.getTodayDate();
				String mTime = Utilities.getTodayTime();
				String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingExpiryDate);
				String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingExpiryTime);
				ContentValues values = new ContentValues();
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_ID, mTrainingId);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingTitle));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_BY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingBy));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DESC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingDescription));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingViewCount));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLikeCount));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE, mDate);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME, mTime);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE, mType);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsRead));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsLiked));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsSharing));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsDownloadable));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LINK, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLink));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE, mExpiryDate);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME, mExpiryTime);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
				
				Cursor mIsExistCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID},  DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, null);
				if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
					mIsExistCursor.close();
					return;
				}
				if(mIsExistCursor!=null)
					mIsExistCursor.close();
				
				
				Uri isInsertUri = getContentResolver().insert(DBConstant.Training_Columns.CONTENT_URI, values);
				boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
				
				if(isInsertedInDB){
					return;
				}
				
				int mIntType = Utilities.getMediaType(mType);
				
				if(mIntType == AppConstants.TYPE.QUIZ){
					JSONArray mJSONArrMobFeedObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingQuizInfo);
					
					for (int k = 0; k < mJSONArrMobFeedObj.length(); k++) {
							JSONObject mJSONQuizObj = mJSONArrMobFeedObj.getJSONObject(k);
							ContentValues valuesQuizInfo = new ContentValues();
							String mQuizId = mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizId);
							String mQuizQid = mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizQueId);
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID, mQuizId);
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID, mQuizQid);
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizQueType));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizQueTitle));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizCorrectAnswer));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizScore));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizDuration));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption1));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption2));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption3));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption4));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption5));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption6));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption7));
							
							Uri isInsertQuizInfoUri = getContentResolver().insert(DBConstant.Training_Quiz_Columns.CONTENT_URI, valuesQuizInfo);
							Utilities.checkWhetherInsertedOrNot(TAG,isInsertQuizInfoUri);
					}
					
					if(isInsertedInDB){
						AndroidUtilities.showSnackBar(SearchActivity.this, getResources().getString(R.string.save_message));
					}
				}
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
