package com.application.ui.activity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.application.beans.Parichay;
import com.application.ui.adapter.ParichayRecyclerAdapter;
import com.application.ui.adapter.ParichayRecyclerAdapter.OnItemClickListener;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.DrawerArrowDrawable;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.ScrimInsetsFrameLayout;
import com.application.ui.view.SearchBox;
import com.application.ui.view.SearchBox.MenuListener;
import com.application.ui.view.SearchBox.SearchListener;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * 
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ParichaySearchActivity extends BaseActivity{
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
	
	private AppCompatCheckBox mFilterAll;
	private AppCompatCheckBox mFilterUnit;
	private AppCompatCheckBox mFilterPosition;
	private AppCompatCheckBox mFilterLocation;
	private AppCompatCheckBox mFilterQualif;
	private AppCompatCheckBox mFilterDesc;
	private AppCompatCheckBox mFilterAge;
	private AppCompatCheckBox mFilterExp;
	
	private RelativeLayout mFilterForLayout;
	
	private ProgressWheel mGooglePlaySearchProgress;
	
	private FrameLayout mEmptyFrameLayout;
	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;
	private AppCompatButton  mEmptySearchButton;
	
	private FrameLayout mProgressFrameLayout;
	private AppCompatTextView mProgressSearchTextView;
	
	private ObservableRecyclerView mRecyclerView;
	
	private ParichayRecyclerAdapter mParichayAdapter;
	
	private Context mContext;
	
	private LinearLayoutManager mLinearLayoutManager;
	
    private boolean mLoadMore = false; 
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

	private Intent mIntent;
	private String mCategory;
	private boolean isDrawerEnable = false;
	
	private ArrayList<Parichay> mArrayListParichayDB = new ArrayList<>();
	private ArrayList<Parichay> mArrayListParichayFilter = new ArrayList<>();

	private boolean isFilterAll = true;
	private boolean isFilterUnit = false;
	private boolean isFilterPosition = false;
	private boolean isFilterLocation = false;
	private boolean isFilterQualif = false;
	private boolean isFilterDesc = false;
	private boolean isFilterAge = false;
	private boolean isFilterExp = false;
	
	private boolean isParichay = false;
	
	private String mSearchTerm;
	
	private static final String TAG = ParichaySearchActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_parichay);
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
			AndroidUtilities.exitWindowAnimation(ParichaySearchActivity.this);
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
		AndroidUtilities.exitWindowAnimation(ParichaySearchActivity.this);
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
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.PARICHAY)){
				mGooglePlaySearchEditText.setHint("Search parichay");
				isDrawerEnable = true;
				mArrayListParichayDB = mIntent.getParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT);
				isParichay = true;
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
		
		mContext =  ParichaySearchActivity.this;
		
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_headview, null);
		
		mLinearLayoutManager = new LinearLayoutManager(mContext);
		
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
	}
	
	private void initDrawerLayout(){
		mFilterAll = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchAllCheckbox);
		mFilterUnit = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchUnitCheckbox);
		mFilterPosition = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchPositionCheckbox);
		mFilterLocation = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchLocationCheckbox);
		mFilterQualif = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchQualifCheckbox);
		mFilterDesc = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchDescCheckbox);
		mFilterAge = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchAgeCheckbox);
		mFilterExp = (AppCompatCheckBox)findViewById(R.id.layoutDrawerSearchParichaySearchExpCheckbox);
		
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
		
		mFilterUnit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterUnit = true;
				}else{
					isFilterUnit = false;
				}
				applyFilter();
			}
		});
		
		mFilterPosition.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterPosition = true;
				}else{
					isFilterPosition = false;
				}
				applyFilter();
			}
		});
		
		mFilterLocation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterLocation = true;
				}else{
					isFilterLocation = false;
				}
				applyFilter();
			}
		});
		
		mFilterQualif.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterQualif = true;
				}else{
					isFilterQualif = false;
				}
				applyFilter();
			}
		});
		
		mFilterDesc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterDesc = true;
				}else{
					isFilterDesc = false;
				}
				applyFilter();
			}
		});
		

		mFilterAge.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterAge = true;
				}else{
					isFilterAge = false;
				}
				applyFilter();
			}
		});
		
		mFilterExp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isFilterExp = true;
				}else{
					isFilterExp = false;
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
				searchQuery(mSearchTerm);
			}
		});
	}
	
	private void disableAllOtherFilters(){
		if(isFilterAll){
			mFilterUnit.setEnabled(false);
			mFilterPosition.setEnabled(false);
			mFilterLocation.setEnabled(false);
			mFilterQualif.setEnabled(false);
			mFilterDesc.setEnabled(false);
			mFilterAge.setEnabled(false);
			mFilterExp.setEnabled(false);
			
			mFilterUnit.setChecked(false);
			mFilterPosition.setChecked(false);
			mFilterLocation.setChecked(false);
			mFilterQualif.setChecked(false);
			mFilterDesc.setChecked(false);
			mFilterAge.setChecked(false);
			mFilterExp.setChecked(false);
		}
	}
	
	private void enableAllOtherFilters(){
		if(!isFilterAll){
			mFilterUnit.setEnabled(true);
			mFilterPosition.setEnabled(true);
			mFilterLocation.setEnabled(true);
			mFilterQualif.setEnabled(true);
			mFilterDesc.setEnabled(true);
			mFilterAge.setEnabled(true);
			mFilterExp.setEnabled(true);
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
		mGooglePlaySearchBox.enableVoiceRecognition(ParichaySearchActivity.this);
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
								resetAdapterWithArrayList();
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
		mGooglePlaySearchBox.hideCircularly(ParichaySearchActivity.this);
		if (mGooglePlaySearchBox.getSearchText().isEmpty())
			mToolBar.setTitle("");
	}
	
	@SuppressLint("DefaultLocale") private void searchQuery(String mSearchQuery){
		if(!TextUtils.isEmpty(mSearchQuery)){
			if(mSearchQuery.length() > 1){
				mSearchTerm = mSearchQuery;
				mGooglePlaySearchProgress.setVisibility(View.VISIBLE);
				if(isParichay){
					searchQueryParichay(mSearchQuery.toLowerCase());	
				}
			}else{
				mGooglePlaySearchProgress.setVisibility(View.INVISIBLE);
				resetAdapterWithArrayList();
			}
		}
	}
	
	
	private void searchQueryParichay(String mSearchQuery){
		try{
			ArrayList<Parichay> mFilter = filterListParichay(mSearchQuery);
			if (mFilter != null && mFilter.size() > 0) {
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mParichayAdapter.addParichayObjList(mArrayListParichayFilter);
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
		if(isParichay){
				if(mArrayListParichayDB!=null && mArrayListParichayDB.size() > 0){
					searchQueryParichay(AppConstants.SEARCH_FILTER);	
			}
		}
	}
	
	private void resetAdapterWithArrayList(){
		if(isParichay){
			if(mArrayListParichayDB!=null && mArrayListParichayDB.size() > 0){
				mEmptyFrameLayout.setVisibility(View.GONE);
				mRecyclerView.setVisibility(View.VISIBLE);
				mParichayAdapter.addParichayObjList(mArrayListParichayDB);
				mRecyclerView.getAdapter().notifyDataSetChanged();
				mArrayListParichayFilter.clear();
			}
		}
	}
	
	/**
	 * RecyclerView
	 */
	
	private void checkDataInAdapter(){
		if(isParichay){
			checkDataInParichayAdapter();
		}
	}
	
	//Parichay
	private void checkDataInParichayAdapter() {
		if (mArrayListParichayDB.size() > 0) {
			setRecyclerParichayAdapter();
			setRecyclerParichayAdapterListener();
		} else {
			setEmptyData(true, null);
		}
	}
	
	private void setRecyclerParichayAdapter(){
		mParichayAdapter = new ParichayRecyclerAdapter(mContext, mArrayListParichayDB,headerView, false);
	    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(false);
	    if(AndroidUtilities.isAboveIceCreamSandWich()){
        	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mParichayAdapter);
            ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
            mRecyclerView.setAdapter(mScaleInAdapter);
        }else{
        	mRecyclerView.setAdapter(mParichayAdapter);
        }
	    mRecyclerView
		.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).color(Utilities.getDividerColor())
				.sizeResId(R.dimen.fragment_recyclerview_divider)
				.visibilityProvider(mParichayAdapter).build());
	}
	
	private void setRecyclerParichayAdapterListener() {
		if(mParichayAdapter!=null){
			mParichayAdapter.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					position-=1;
					switch(view.getId()){
					case R.id.itemRecyclerParichayRootLayout:
						Intent mIntentParichay = new Intent(ParichaySearchActivity.this,ParichayDetailActivity.class);
						mIntentParichay.putExtra(AppConstants.INTENTCONSTANTS.POSITION,position);
						if(mArrayListParichayFilter!=null && mArrayListParichayFilter.size() > 0){
							mIntentParichay.putParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT, mArrayListParichayFilter);
						}else if(mArrayListParichayDB!=null && mArrayListParichayDB.size() > 0){
							mIntentParichay.putParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT, mArrayListParichayDB);
						}						
						startActivity(mIntentParichay);
						AndroidUtilities.enterWindowAnimation(ParichaySearchActivity.this);
						break;
					}
				}
			});
		}
	}
	
	@SuppressLint("DefaultLocale") private ArrayList<Parichay> filterListParichay(String mSearchQuery){
		try{
			if(mArrayListParichayFilter!=null){
				mArrayListParichayFilter.clear();
			}else{
				mArrayListParichayFilter  = new ArrayList<>();
			}
			if(mArrayListParichayDB!=null && mArrayListParichayDB.size() > 0){
				for(int i = 0; i < mArrayListParichayDB.size() ; i++){
					Parichay Obj = mArrayListParichayDB.get(i);
					if(isFilterAll){
						if(Obj.getmJobUnit().toLowerCase().contains(mSearchQuery) || Obj.getmJobPosition().toLowerCase().contains(mSearchQuery) || Obj.getmJobDesc().toLowerCase().contains(mSearchQuery)
								|| Obj.getmJobDesiredProfile().toLowerCase().contains(mSearchQuery)){
							mArrayListParichayFilter.add(Obj);
						}
					}else {
						boolean isAlreadyAdded = false;
						if(isFilterUnit){
							if ( Obj.getmJobUnit().toLowerCase().contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterPosition && !isAlreadyAdded){
							if (Obj.getmJobPosition().toLowerCase()
									.contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterLocation && !isAlreadyAdded){
							if (Obj.getmJobLoc().toLowerCase()
									.contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterQualif && !isAlreadyAdded){
							if (Obj.getmJobQualif().toLowerCase()
									.contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterDesc && !isAlreadyAdded){
							if (Obj.getmJobDesc().toLowerCase()
									.contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterAge && !isAlreadyAdded){
							if (Obj.getmJobAgeLimit().toLowerCase()
									.contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
						
						if(isFilterExp && !isAlreadyAdded){
							if (Obj.getmJobExp().toLowerCase()
									.contains(mSearchQuery)) {
								mArrayListParichayFilter.add(Obj);
								isAlreadyAdded = true;
							}
						}
					}
				}
			}
			
			if(mArrayListParichayFilter!=null && mArrayListParichayFilter.size() > 0){
				return mArrayListParichayFilter;
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
