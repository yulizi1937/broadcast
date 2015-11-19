/**
 * 
 */
package com.application.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Parichay;
import com.application.ui.activity.ParichayActivity;
import com.application.ui.activity.ParichayDetailActivity;
import com.application.ui.adapter.ParichayRecyclerAdapter;
import com.application.ui.adapter.ParichayRecyclerAdapter.OnItemClickListener;
import com.application.ui.adapter.ParichayRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.HorizontalDividerItemDecoration;
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
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ParichayFragment extends BaseFragment implements IFragmentCommunicator {
	@SuppressWarnings("unused")
	
	private static final String TAG = ParichayFragment.class.getSimpleName();
	
	public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

	private IActivityCommunicator mActivityCommunicator;
	
	private Activity mParentActivity;

	private View headerView;
	
	private Toolbar mToolBar;

	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatButton mEmptyRefreshBtn;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ObservableRecyclerView mRecyclerView;

	private FrameLayout mCroutonViewGroup;

	private ParichayRecyclerAdapter mAdapter;
	
	private ArrayList<Parichay> mArrayListParichay = new ArrayList<>();
	
	private Context mContext;
	
	private int UnreadCount = 0;
	
	private GridLayoutManager mGridLayoutManager;
	
	private boolean isGrid = false;
	private int mGridColumn = 1;
	
	private int whichTheme = 0;
	
    private boolean mLoadMore = false; 
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    @Override
    public void onAttach(Activity activity){
      super.onAttach(activity);
      mContext = getActivity();
      mActivityCommunicator =(IActivityCommunicator)mContext;//NIELSEN
      ((ParichayActivity)mContext).mFragmentCommunicator = this;
    }
	 
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mobcast, container,
				false);

		mParentActivity = getActivity();
		mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
		headerView = LayoutInflater.from(mParentActivity).inflate(
				R.layout.padding, null);

		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipeRefreshLayout);

		mEmptyFrameLayout = (FrameLayout) view.findViewById(R.id.fragmentMobcastEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) view.findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) view.findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton) view.findViewById(R.id.layoutEmptyRefreshBtn);
		

		
		isToApplyGridOrNot();
		
		mGridLayoutManager = new GridLayoutManager(mParentActivity, mGridColumn);
		mRecyclerView.setLayoutManager(mGridLayoutManager);
		
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
		ApplicationLoader.getPreferences().setViewIdParichay("-1");
		
		checkDataInAdapter();
		setSwipeRefreshLayoutCustomisation();
		setMaterialRippleView();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		fetchDataFromApi();
		ApplicationLoader.trackScreenViewV3(ApplicationLoader.getApplication().getResources().getString(R.string.com_application_ui_fragment_ParichayFragment));
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setClickListener();
	}
	
	private void fetchDataFromApi(){
		if(mArrayListParichay.size() == 0){
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

	private void setRecyclerAdapter(){
	    mAdapter = new ParichayRecyclerAdapter(mContext, mArrayListParichay,headerView, isGrid);
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
				R.string.emptyParichayTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyParichayMessage));
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
		mSwipeRefreshLayout.setProgressViewOffset(false, 100, 180);
	}
	
	
	private void setRecyclerAdapterListener() {
		if(mAdapter!=null){
			mAdapter.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					position = isGrid ? position - 2 : position - 1;
					switch(view.getId()){
					case R.id.itemRecyclerParichayRootLayout:
						Intent mIntentParichay = new Intent(mParentActivity,ParichayDetailActivity.class);
						mIntentParichay.putExtra(AppConstants.INTENTCONSTANTS.POSITION,position);
						mIntentParichay.putParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.OBJECT, mArrayListParichay);
						saveViewPosition(position);
						startActivity(mIntentParichay);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
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
			        mVisibleItemCount = mGridLayoutManager.getChildCount();
			        mTotalItemCount = mGridLayoutManager.getItemCount();
			        mFirstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
			 
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
		if (mArrayListParichay.size() > 0) {
			setRecyclerAdapter();
		} else {
			setEmptyData();
		}
	}
	
	public class ParichaySort implements Comparator<Parichay>{
	    @Override
	    public int compare(Parichay Obj1, Parichay Obj2) {
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
		ApplicationLoader.getPreferences().setViewIdParichay(String.valueOf(position));
	}
	
	private void checkReadFromDBAndUpdateToObj(){
		int position = Integer.parseInt(ApplicationLoader.getPreferences().getViewIdParichay());
		if (position >= 0 && position < mArrayListParichay.size()) {
			if(mArrayListParichay!=null && mArrayListParichay.size() > 0){
					boolean isToNotify = false;
					boolean isRead = false;
					if(!mArrayListParichay.get(position).isRead()){
						mArrayListParichay.get(position).setRead(true);
						mArrayListParichay.get(position).setmReadCount(String.valueOf(Integer.parseInt(mArrayListParichay.get(position).getmReadCount())+1));
						isToNotify = true;
						isRead = true;
					}
					if(mArrayListParichay.get(position).isLike()){
						if(Integer.parseInt(ApplicationLoader.getPreferences().getLikeIdParichay())!=-1){
							mArrayListParichay.get(position).setLike(true);	
						}else{
							mArrayListParichay.get(position).setLike(false);
							mArrayListParichay.get(position).setmLikeCount(String.valueOf(Integer.parseInt(mArrayListParichay.get(position).getmLikeCount())-1));
						}
						isToNotify = true;
					}else{
						if(Integer.parseInt(ApplicationLoader.getPreferences().getLikeIdParichay())!=-1){
							mArrayListParichay.get(position).setLike(true);
							mArrayListParichay.get(position).setmLikeCount(String.valueOf(Integer.parseInt(mArrayListParichay.get(position).getmLikeCount())+1));	
						}else{
							mArrayListParichay.get(position).setLike(false);
						}
						isToNotify = true;
					}
					
					if(isToNotify){
						mRecyclerView.getAdapter().notifyDataSetChanged();
						if(isRead){
							--UnreadCount;
							if (!(UnreadCount <= 0)) {
								ApplicationLoader.getPreferences().setUnreadParichay(UnreadCount);
							}else{
								ApplicationLoader.getPreferences().setUnreadParichay(0);
							}
							mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.PARICHAY);
						}
					}
			}
		}
	}
	
	public void passDataToFragment(int mId, String mCategory) {
		try{
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.PARICHAY)){
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
							
							mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.PARICHAY);
							
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
			mPosition =  mPosition - mGridColumn;
			if(mPosition!= -1){
				int mType = AppConstants.TYPE.PARICHAY;
				String mTitle = mArrayListParichay.get(mPosition).getmJobPosition();
				boolean isRead = mArrayListParichay.get(mPosition).isRead();
				ContextMenuFragment newFragment = new ContextMenuFragment(mPosition, mType, mTitle, isRead , mView);
		        newFragment.show(getFragmentManager(), "dialog");
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
		mBottomSheet = new BottomSheet.Builder(mParentActivity).icon(Utilities.getRoundedBitmapForContextMenu(mType)).title(mTitle).sheet(R.menu.context_menu_recruitment).build();
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
		 String mParichayId = mArrayListParichay.get(mPosition).getmId();
       	 mArrayListParichay.get(mPosition).setRead(true);
       	 mRecyclerView.getAdapter().notifyItemChanged(isGrid ? mPosition+2 : mPosition+1);
       	 UserReport.updateUserReportApi(mParichayId, AppConstants.INTENTCONSTANTS.PARICHAY, AppConstants.REPORT.READ, "");
       	 --UnreadCount;
		if (!(UnreadCount <= 0)) {
			ApplicationLoader.getPreferences().setUnreadParichay(UnreadCount);
		}else{
			ApplicationLoader.getPreferences().setUnreadParichay(0);
		}
		mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.PARICHAY);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuMarkAsUnRead(int mPosition){
		try{
	       mArrayListParichay.get(mPosition).setRead(false);
           mRecyclerView.getAdapter().notifyItemChanged(isGrid ? mPosition+2 : mPosition+1);
           ++UnreadCount;
   		   if (!(UnreadCount <= 0)) {
   			ApplicationLoader.getPreferences().setUnreadParichay(UnreadCount);
   		   }else{
   			ApplicationLoader.getPreferences().setUnreadParichay(0);
   		   }
   		mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.PARICHAY);
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
	
	public String apiRefreshFeedParichay(boolean sortByAsc, int limit){
		try {
			JSONObject jsonObj =null;
			 if(sortByAsc){
				jsonObj = JSONRequestBuilder.getPostFetchFeedParichay(sortByAsc,limit, mArrayListParichay.size()!=0 ? mArrayListParichay.get(0).getmId() : String.valueOf("0"));
			 }else{
				 jsonObj= JSONRequestBuilder.getPostFetchFeedParichay(sortByAsc,limit, mArrayListParichay.get(mArrayListParichay.size()-2).getmId());
			 }
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_PARICHAY, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_PARICHAY, jsonObj, TAG);	
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
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.parichay);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					Parichay Obj = new Parichay();
					String mParichayId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobId);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.sentDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.sentTime);
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.expiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.expiryTime);
					Obj.setmId(mParichayId);
					Obj.setmDate(Utilities.getDate(mDate));
					Obj.setmMonth(Utilities.getMonth(mDate));
					Obj.setmExpiryDate(mExpiryDate);
					Obj.setmExpiryTime(mExpiryTime);
					Obj.setmFileType(AppConstants.INTENTCONSTANTS.PARICHAY);
					Obj.setmJobUnit(Utilities.formatUnit(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobTitle)));
					Obj.setmJobPosition(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobPosition));
					Obj.setmJobLoc(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobLocation));
					Obj.setmJobAgeLimit(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobAgeLimit));
					Obj.setmJobDesc(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobDescription));
					Obj.setmJobQualif(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobQualification));
					Obj.setmJobDesiredProfile(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobDesiredProfile));
					Obj.setLike(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isLiked)));
					Obj.setRead(Boolean.parseBoolean(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isRead)));
					Obj.setmLikeCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.likeCount));
					Obj.setmReadCount(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.viewCount));
					String mYear = " yrs";
					try {
						if(Integer.parseInt(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobExperienceTo)) <= 0){
							mYear = " yr";
						}
					} catch (Exception e) {
						FileLog.e(TAG, e.toString());
					}
					Obj.setmJobExp(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobExperienceFrom) + " to " + mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobExperienceTo) + mYear);
					
					if(!Obj.isRead()){
						UnreadCount++;
					}
					
					mArrayListParichay.add(Obj);
				}
				
				if (!(UnreadCount <= 0)) {
					ApplicationLoader.getPreferences().setUnreadParichay(UnreadCount);
				}else{
					ApplicationLoader.getPreferences().setUnreadParichay(0);
				}
				
				Collections.sort(mArrayListParichay, new ParichaySort());
				if(isToAddOldData){
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.PARICHAY);					
				}else{
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.PARICHAY);	
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
			if (mArrayListParichay.size() == 0) {
				mProgressDialog = new MobcastProgressDialog(getActivity());
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
			
			if(!sortByAsc){
				Parichay Obj = new Parichay();
				Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
				mArrayListParichay.add(Obj);
				if(mAdapter!=null)
					mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiRefreshFeedParichay(sortByAsc,limit);
//				mResponseFromApi = Utilities.readFile("json/parichay.json");
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
					mArrayListParichay.remove(mArrayListParichay.size()-1);
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}
				
				if (isSuccess) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
				}else{
					if(sortByAsc){
						AndroidUtilities.showSnackBar(getActivity(), Utilities.getErrorMessageFromApi(mResponseFromApi));	
					}
				}

				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
				
				if(mSwipeRefreshLayout.isRefreshing()){
					mSwipeRefreshLayout.setRefreshing(false);
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}
	
	private void isToApplyGridOrNot(){
		try{
			if(BuildVars.IS_GRID){
				if(AndroidUtilities.getScreenSizeInInches() >= 7.0 && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					isGrid = true;
					mGridColumn = 2;
				}	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
