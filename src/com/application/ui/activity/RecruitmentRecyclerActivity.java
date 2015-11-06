/**
 * 
 */
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Recruitment;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.RecruitmentRecyclerAdapter;
import com.application.ui.adapter.RecruitmentRecyclerAdapter.OnItemClickListener;
import com.application.ui.adapter.RecruitmentRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class RecruitmentRecyclerActivity extends SwipeBackBaseActivity {
	@SuppressWarnings("unused")
	private static final String TAG = RecruitmentRecyclerActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;
	
	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatButton mEmptyRefreshBtn;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ObservableRecyclerView mRecyclerView;

	private FrameLayout mCroutonViewGroup;

	private RecruitmentRecyclerAdapter mAdapter;
	
	private ArrayList<Recruitment> mArrayListRecruitment = new ArrayList<>();
	
	private Context mContext;
	
	private LinearLayoutManager mLinearLayoutManager;
	
	private int UnreadCount = 0;
	
    private boolean mLoadMore = false; 
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_recruitment);
		setSecurity();
		initToolBar();
		initUi();
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		fetchDataFromApi();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_recruitment, menu);
		if (AndroidUtilities.isAboveGingerBread()) {
			MenuItem refreshItem = menu
					.findItem(R.id.action_refresh_actionable);
			if (refreshItem != null) {
				View mView = MenuItemCompat.getActionView(refreshItem);
				MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout) mView
						.findViewById(R.id.toolBarActionItemRefresh);
				mToolBarMenuRefreshProgress = (ProgressWheel) mView
						.findViewById(R.id.toolBarActionItemProgressWheel);
				mToolBarMenuRefresh = (ImageView) mView
						.findViewById(R.id.toolBarActionItemImageView);
				mToolBarMenuRefreshLayout
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View mView) {
								// TODO Auto-generated method stub
								toolBarRefresh();
							}
						});
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_refresh_actionable:
			toolBarRefresh();
			return true;
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(RecruitmentRecyclerActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(RecruitmentRecyclerActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Recruitment");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(RecruitmentRecyclerActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		if (!mLoadMore) {
        	mLoadMore = true;
            refreshFeedFromApi(true, false, AppConstants.BULK);
		}
	}
	
	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setMaterialRippleView();
		setClickListener();
	}
	
	private void fetchDataFromApi(){
		if(mArrayListRecruitment.size() == 0){
			mEmptyRefreshBtn.performClick();
		}
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mEmptyRefreshBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(RecruitmentRecyclerActivity.this).applyThemeCountrySelect(RecruitmentRecyclerActivity.this, RecruitmentRecyclerActivity.this, mToolBar);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setClickListener(){
		mEmptyRefreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				refreshFeedFromApi(true, true, AppConstants.BULK);
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				refreshFeedFromApi(true, true, 0);
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void refreshFeedFromApi(boolean isRefreshFeed, boolean sortByAsc, int limit){//sortByAsc:true-> new data //sortByAsc:false->Old Data
		if(Utilities.isInternetConnected()){
			if(AndroidUtilities.isAboveIceCreamSandWich()){
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			}else{
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit).execute();
			}
		}
	}

	private void initUi() {
		mContext = RecruitmentRecyclerActivity.this;
		
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll_wo);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

		mEmptyFrameLayout = (FrameLayout)findViewById(R.id.fragmentEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView)findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView)findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton)findViewById(R.id.layoutEmptyRefreshBtn);
		
		mLinearLayoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		
		ApplicationLoader.getPreferences().setViewIdRecruitment("-1");
		
		checkDataInAdapter();

		setSwipeRefreshLayoutCustomisation();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.RecruitmentRecyclerActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void setRecyclerAdapter(){
	    mAdapter = new RecruitmentRecyclerAdapter(mContext, mArrayListRecruitment);
	    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(false);
	    if(AndroidUtilities.isAboveIceCreamSandWich()){
        	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
            mRecyclerView.setAdapter(mScaleInAdapter);
        }else{
        	mRecyclerView.setAdapter(mAdapter);
        }
	    mRecyclerView
		.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).color(Utilities.getDividerColor())
				.sizeResId(R.dimen.fragment_recyclerview_divider)
				.visibilityProvider(mAdapter).build());
	}

	private void setEmptyData() {
		mEmptyTitleTextView.setText(getResources().getString(
				R.string.emptyRecruitmentTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyRecruitmentMessage));
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
	}
	
	private void setSwipeRefreshLayoutCustomisation() {
		mSwipeRefreshLayout.setColorSchemeColors(
				Color.parseColor(AppConstants.COLOR.MOBCAST_RED),
				Color.parseColor(AppConstants.COLOR.MOBCAST_YELLOW),
				Color.parseColor(AppConstants.COLOR.MOBCAST_PURPLE),
				Color.parseColor(AppConstants.COLOR.MOBCAST_GREEN),
				Color.parseColor(AppConstants.COLOR.MOBCAST_BLUE));
	}
	
	
	private void setRecyclerAdapterListener() {
		if(mAdapter!=null){
			mAdapter.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					switch(view.getId()){
					case R.id.itemRecyclerRecruitmentRootLayout:
						Intent mIntentRecruitment = new Intent(RecruitmentRecyclerActivity.this,RecruitmentDetailActivity.class);
						mIntentRecruitment.putExtra(AppConstants.INTENTCONSTANTS.POSITION,position);
						mIntentRecruitment.putParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT, mArrayListRecruitment);
						saveViewPosition(position);
						startActivity(mIntentRecruitment);
						AndroidUtilities.enterWindowAnimation(RecruitmentRecyclerActivity.this);
						break;
					}
				}
			});
		}
		
		if(mAdapter!=null){
			mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
//					position=-1;
					showContextMenu(position, view);
				}
			});
		}
	}
	
	private void setRecyclerScrollListener() {
		if(mAdapter!=null){
			mRecyclerView.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView,
						int newState) {
					// TODO Auto-generated method stub
					super.onScrollStateChanged(recyclerView, newState);
					int topRowVerticalPosition = (recyclerView == null || recyclerView
							.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0)
							.getTop();
					mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
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
				            refreshFeedFromApi(true, false, AppConstants.BULK);
			            }
			        }
				}
			});
		}
	}
	
	private void checkDataInAdapter() {
		if (mArrayListRecruitment.size() > 0) {
			setRecyclerAdapter();
		} else {
			setEmptyData();
		}
	}
	
	public class RecruitmentSort implements Comparator<Recruitment>{
	    @Override
	    public int compare(Recruitment Obj1, Recruitment Obj2) {
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
	
	private void saveViewPosition(int position){
		ApplicationLoader.getPreferences().setViewIdRecruitment(String.valueOf(position));
		mArrayListRecruitment.get(position).setRead(true);
	}
	
	private void checkReadFromDBAndUpdateToObj(){
		int position = Integer.parseInt(ApplicationLoader.getPreferences().getViewIdRecruitment());
		if (position >= 0 && position < mArrayListRecruitment.size()) {
			if(mArrayListRecruitment!=null && mArrayListRecruitment.size() > 0){
					boolean isToNotify = false;
					if(mArrayListRecruitment.get(position).isRead()){
						mArrayListRecruitment.get(position).setRead(true);
						isToNotify = true;
					}
					if(mArrayListRecruitment.get(position).isLike()){
						mArrayListRecruitment.get(position).setLike(true);
						isToNotify = true;
					}
					if(isToNotify){
						mRecyclerView.getAdapter().notifyDataSetChanged();
						UnreadCount--;
						if (!(UnreadCount <= 0)) {
							ApplicationLoader.getPreferences().setUnreadRecruitment(UnreadCount);
						}else{
							ApplicationLoader.getPreferences().setUnreadRecruitment(0);
						}
					}
			}
		}
	}
	
	public void passDataToFragment(int mId, String mCategory) {
		try{
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.RECRUITMENT)){
					mSwipeRefreshLayout.setEnabled(true);
					mSwipeRefreshLayout.setRefreshing(true);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(mAdapter!=null){
								mRecyclerView.getAdapter().notifyDataSetChanged();
							}else{
								setRecyclerAdapter();
								setUiListener();
							}
							mSwipeRefreshLayout.setRefreshing(false);
							
							if(mRecyclerView.getVisibility() == View.GONE){
								mEmptyFrameLayout.setVisibility(View.GONE);
								mRecyclerView.setVisibility(View.VISIBLE);
							}
						}
					}, 1000);
					
			}			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/*
	 * ContextMenu
	 */
	private void showContextMenu(int mPosition, View mView){
		try{
//			mPosition =  mPosition - 1;
			if(mPosition!= -1){
				int mType = AppConstants.TYPE.RECRUITMENT;
				String mTitle = mArrayListRecruitment.get(mPosition).getmJobTitle();
				boolean isRead = mArrayListRecruitment.get(mPosition).isRead();
				ContextMenuFragment newFragment = new ContextMenuFragment(mPosition, mType, mTitle, isRead , mView);
		        newFragment.show(getSupportFragmentManager(), "dialog");
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public class ContextMenuFragment extends DialogFragment {
		int mPosition;
		int mType;
		String mTitle;
		boolean isRead;
		View mView;
	    public ContextMenuFragment (int mPosition, int mType, String mTitle, boolean isRead, View mView) {
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
	
	private BottomSheet getContextMenu(final int mPosition, int mType, final String mTitle, boolean isRead, final View mView){
		BottomSheet mBottomSheet;
		mBottomSheet = new BottomSheet.Builder(RecruitmentRecyclerActivity.this).icon(Utilities.getRoundedBitmapForContextMenu(mType)).title(mTitle).sheet(R.menu.context_menu_recruitment).build();
         final Menu menu = mBottomSheet.getMenu();
         
         SpannableString mSpannabledRead = new SpannableString(getResources().getString(R.string.context_menu_read));
         SpannableString mSpannabledUnRead = new SpannableString(getResources().getString(R.string.context_menu_unread));
         SpannableString mSpannabledView = new SpannableString(getResources().getString(R.string.context_menu_view));
         
         mSpannabledRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledRead.length(), 0);
         mSpannabledUnRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledUnRead.length(), 0);
         mSpannabledView.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledView.length(), 0);
         
         menu.getItem(0).setTitle(mSpannabledRead);
         menu.getItem(1).setTitle(mSpannabledUnRead);
         menu.getItem(2).setTitle(mSpannabledView);
         
         /**
          * Read
          */
         menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
            	 contextMenuMarkAsRead(mPosition);
                 return true;
             }
         });
         
         /**
          * UnRead
          */
         menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
            	 contextMenuMarkAsUnRead(mPosition);
                 return true;
             }
         });
         
         /**
          * View
          */
         menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
            	 contextMenuView(mPosition, mView);
                 return true;
             }
         });
         
         return mBottomSheet;
	}
	
	private void contextMenuMarkAsRead(int mPosition){
		try{
		 String mRecruitmentId = mArrayListRecruitment.get(mPosition).getmId();
       	 mArrayListRecruitment.get(mPosition).setRead(true);
       	 mRecyclerView.getAdapter().notifyItemChanged(mPosition);
       	 UserReport.updateUserReportApi(mRecruitmentId, AppConstants.INTENTCONSTANTS.RECRUITMENT, AppConstants.REPORT.READ, "");
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuMarkAsUnRead(int mPosition){
		try{
	       mArrayListRecruitment.get(mPosition).setRead(false);
           mRecyclerView.getAdapter().notifyItemChanged(mPosition);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuView(int mPosition , View mView){
		try{
			mView.performClick();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/*
	 * AsyncTask To Refresh
	 */
	
	public String apiRefreshFeedRecruitment(boolean sortByAsc, int limit){
		try {
			JSONObject jsonObj =null;
			 if(sortByAsc){
				jsonObj = JSONRequestBuilder.getPostFetchFeedRecruitment(sortByAsc,limit, mArrayListRecruitment.size()!=0 ? mArrayListRecruitment.get(0).getmId() : String.valueOf("0"));
			 }else{
				 jsonObj= JSONRequestBuilder.getPostFetchFeedRecruitment(sortByAsc,limit, mArrayListRecruitment.get(mArrayListRecruitment.size()-2).getmId());
			 }
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_RECRUITMENT, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_RECRUITMENT, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public String apiRefreshFeedAction(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.RECRUITMENT);
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_ACTION, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_ACTION, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public void parseDataFromApi(String mResponseFromApi, boolean isToAddOldData){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.recruitment);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					Recruitment Obj = new Recruitment();
					String mRecruitmentId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER._id);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.sentDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.sentTime);
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.expiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.expiryTime);
					Obj.setmId(mRecruitmentId);
					Obj.setmDate(Utilities.getDate(mDate));
					Obj.setmMonth(Utilities.getMonth(mDate));
					Obj.setmExpiryDate(mExpiryDate);
					Obj.setmExpiryTime(mExpiryTime);
					Obj.setmFileType(AppConstants.INTENTCONSTANTS.RECRUITMENT);
					Obj.setmJobTitle(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobTitle));
					Obj.setmJobDesig(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobDesignation));
					Obj.setmJobLoc(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobLocation));
					Obj.setmJobExp(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobExperience));
					Obj.setmJobSkills(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobSkills));
					Obj.setmJobDesc(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobDescription));
					Obj.setmJobQualif(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobQualification));
					Obj.setmJobCTC(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobCTC));
					Obj.setmContactName(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.contactName));
					Obj.setmContactEmail(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.contactEmail));
					Obj.setmContactDesig(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.contactDesignation));
					Obj.setmContactMobile(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.contactMobile));
					Obj.setContactSharing(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isContactSharing)));
					Obj.setSocialSharing(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isSharing)));
					Obj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isLiked)));
					Obj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isRead)));
					Obj.setmLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.likeCount));
					Obj.setmReadCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.viewCount));
					Obj.setmShareCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.shareCount));
					
					if(!Obj.isRead()){
						UnreadCount++;
					}
					
					mArrayListRecruitment.add(Obj);
				}
				
				if (!(UnreadCount <= 0)) {
					ApplicationLoader.getPreferences().setUnreadRecruitment(UnreadCount);
				}else{
					ApplicationLoader.getPreferences().setUnreadRecruitment(0);
				}
				
				Collections.sort(mArrayListRecruitment, new RecruitmentSort());
				if(isToAddOldData){
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.RECRUITMENT);					
				}else{
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.RECRUITMENT);	
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public class AsyncRefreshTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;
		private boolean isRefreshFeed = true;
		private boolean sortByAsc =false;
		private int limit;

		public AsyncRefreshTask(boolean isRefreshFeed, boolean sortByAsc,int limit){
			this.isRefreshFeed = isRefreshFeed;
			this.sortByAsc = sortByAsc;
			this.limit = limit;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mArrayListRecruitment.size() == 0) {
				mProgressDialog = new MobcastProgressDialog(RecruitmentRecyclerActivity.this);
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
			
			if(!sortByAsc){
				Recruitment Obj = new Recruitment();
				Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
				mArrayListRecruitment.add(Obj);
				if(mAdapter!=null)
					mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = isRefreshFeed ? apiRefreshFeedRecruitment(sortByAsc,limit) : apiRefreshFeedAction();
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
				if(!sortByAsc){
					mLoadMore = false;
					mArrayListRecruitment.remove(mArrayListRecruitment.size()-1);
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}
				
				if (isSuccess) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
				}else{
					AndroidUtilities.showSnackBar(RecruitmentRecyclerActivity.this, Utilities.getErrorMessageFromApi(mResponseFromApi));	
				}

				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
				
				if(mSwipeRefreshLayout.isRefreshing()){
					mToolBarMenuRefresh.setVisibility(View.VISIBLE);
					mToolBarMenuRefreshProgress.setVisibility(View.GONE);
					mSwipeRefreshLayout.setRefreshing(false);
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
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
