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
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.ParichayReferral;
import com.application.sqlite.DBConstant;
import com.application.ui.activity.ParichayActivity;
import com.application.ui.adapter.ParichayReferralRecyclerAdapter;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ParichayReferralFragment extends BaseFragment implements IFragmentCommunicator {
	@SuppressWarnings("unused")
	
	private static final String TAG = ParichayReferralFragment.class.getSimpleName();
	
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

	private ParichayReferralRecyclerAdapter mAdapter;
	
	private ArrayList<ParichayReferral> mArrayListParichayReferral;
	
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
		checkDataInAdapter();
		setSwipeRefreshLayoutCustomisation();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		syncDataWithApi();
		ApplicationLoader.trackScreenViewV3(ApplicationLoader.getApplication().getResources().getString(R.string.com_application_ui_fragment_ParichayReferralFragment));
	}

	private void setUiListener() {
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setMaterialRippleView();
		setClickListener();
	}
	
	/**
	 * <b>Description: </b></br>Api : Sync Data - Calls on App Opens</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void syncDataWithApi(){
		try{
			if (mArrayListParichayReferral != null && mArrayListParichayReferral.size() > 0) {
				refreshFeedFromApi(false, true, 0, true);	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
				refreshFeedFromApi(true, true, AppConstants.BULK,false);
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				try{
					if(mArrayListParichayReferral!=null && mArrayListParichayReferral.size() > 0){
						refreshFeedFromApi(true, true, 0, false);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								refreshFeedFromApi(false, true, 0, true);						
							}
						}, 4000);
					}
				}catch(Exception e){
					
				}
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void refreshFeedFromApi(boolean isRefreshFeed, boolean sortByAsc, int limit, boolean isAutoRefresh){//sortByAsc:true-> new data //sortByAsc:false->Old Data
		if(Utilities.isInternetConnected()){
			if(AndroidUtilities.isAboveIceCreamSandWich()){
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit, isAutoRefresh).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			}else{
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit,isAutoRefresh).execute();
			}
		}
	}

	private void setRecyclerAdapter(){
	    mAdapter = new ParichayReferralRecyclerAdapter(mContext, mArrayListParichayReferral,headerView, isGrid);
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
				R.string.emptyParichayReferredTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyParichayReferredMessage));
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
			            	refreshFeedFromApi(true, false, AppConstants.BULK, false);
			            }
			        }
				}
			});
		}
	}
	
	private void checkDataInAdapter() {
		Cursor mCursor = getActivity().getContentResolver().query(
				DBConstant.Parichay_Referral_Columns.CONTENT_URI, null, null, null,
				DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addParichayReferralObjectListFromDBToBeans(mCursor, false, false);
			if (mArrayListParichayReferral.size() > 0) {
				setRecyclerAdapter();
			} else {
				setEmptyData();
			}
		} else {
			setEmptyData();
		}

		if (mCursor != null) {
			mCursor.close();
		}
	}
	
	public class ParichayReferralSort implements Comparator<ParichayReferral>{
	    @Override
	    public int compare(ParichayReferral Obj1, ParichayReferral Obj2) {
	        try{
	        	if(Integer.parseInt(Obj1.getmReferredId()) < Integer.parseInt(Obj2.getmReferredId())){
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
	
	private void addParichayReferralObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver, boolean isToAddOldData){
		int mIntJobId = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_JOB_ID);
		int mIntJobUnit = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_TYPE);
		int mIntReferredId = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID);
		int mIntReferredDate = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_DATE);
		int mIntReferredFor = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_FOR);
		int mIntReferredName = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_NAME);
		int mIntReason = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REASON);
		int mIntTelephonic = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_TELEPHONIC);
		int mIntOnlineWritten = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_ONLINE);
		int mIntPR1 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR1);
		int mIntPR2 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR2);
		int mIntHR = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_HR);
		int mIntInstall1 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL1);
		int mIntInstall2 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL2);
		int mIntInstall3 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL3);
		
		if(!isToAddOldData){
			mArrayListParichayReferral = new ArrayList<ParichayReferral>();
		}else{
			if(mArrayListParichayReferral==null){
				mArrayListParichayReferral = new ArrayList<ParichayReferral>();	
			}
		}
		
		mCursor.moveToFirst();
		do {
			ParichayReferral  Obj = new ParichayReferral();
			Obj.setmJobId(mCursor.getString(mIntJobId));
			Obj.setmJobUnit(mCursor.getString(mIntJobUnit));
			Obj.setmDate(mCursor.getString(mIntReferredDate));
			Obj.setmReferredId(mCursor.getString(mIntReferredId));
			Obj.setmReferredFor(mCursor.getString(mIntReferredFor));
			Obj.setmName(mCursor.getString(mIntReferredName));
			Obj.setmReason(mCursor.getString(mIntReason));
			Obj.setIsTelephone(Integer.parseInt(mCursor.getString(mIntTelephonic)));
			Obj.setIsOnlineWritten(Integer.parseInt(mCursor.getString(mIntOnlineWritten)));
			Obj.setIsPR1(Integer.parseInt(mCursor.getString(mIntPR1)));
			Obj.setIsPR2(Integer.parseInt(mCursor.getString(mIntPR2)));
			Obj.setIsHR(Integer.parseInt(mCursor.getString(mIntHR)));
			Obj.setIsInstallment1(Integer.parseInt(mCursor.getString(mIntInstall1)));
			Obj.setIsInstallment2(Integer.parseInt(mCursor.getString(mIntInstall2)));
			Obj.setIsInstallment3(Integer.parseInt(mCursor.getString(mIntInstall3)));
			
			if(Obj.getmJobUnit().substring(Obj.getmJobUnit().lastIndexOf(":")+1, Obj.getmJobUnit().length()).trim().contains("IRF")){
				Obj.setmType(AppConstants.PARICHAY.IRF);
			}else{
				Obj.setmType(AppConstants.PARICHAY.FMCG);
			}

			if(!isFromBroadCastReceiver){
				mArrayListParichayReferral.add(Obj);	
			}else{
				mArrayListParichayReferral.add(0,Obj);
			}
		} while (mCursor.moveToNext());
	
		Collections.sort(mArrayListParichayReferral, new ParichayReferralSort());
	}
	
	
	public void passDataToFragment(int mId, String mCategory) {
		try{
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.PARICHAYREF)){
				Cursor mCursor = null;
				boolean isToAddOldData = false;
				boolean isFromBroadCastReceiver = false;
				if(mId == -786){
					mCursor = mContext.getContentResolver().query(DBConstant.Parichay_Referral_Columns.CONTENT_URI, null, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + "<?", new String[]{mArrayListParichayReferral.get(mArrayListParichayReferral.size()-1).getmReferredId()}, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + " DESC");
					isToAddOldData = true;
				}else{
					mCursor = mContext.getContentResolver().query(DBConstant.Parichay_Referral_Columns.CONTENT_URI, null, null, null,DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + " DESC");
				}
					mSwipeRefreshLayout.setEnabled(true);
					mSwipeRefreshLayout.setRefreshing(true);
					mCursor.moveToFirst();
					addParichayReferralObjectListFromDBToBeans(mCursor, isFromBroadCastReceiver, isToAddOldData);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(mAdapter!=null){
								mAdapter.addParichayObjList(mArrayListParichayReferral);
								mRecyclerView.getAdapter().notifyDataSetChanged();
							}else{
								setRecyclerAdapter();
								setUiListener();
							}
							mSwipeRefreshLayout.setRefreshing(false);
							
							mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.REFERRAL);
							
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
	 * AsyncTask To Refresh
	 */
	
	public String apiRefreshFeedParichay(boolean sortByAsc, int limit){
		try {
			JSONObject jsonObj =null;
			 if(sortByAsc){
				jsonObj = JSONRequestBuilder.getPostFetchFeedParichayReferral(sortByAsc,limit, mArrayListParichayReferral!=null ? mArrayListParichayReferral.get(0).getmReferredId() : String.valueOf("0"));
			 }else{
				 jsonObj= JSONRequestBuilder.getPostFetchFeedParichayReferral(sortByAsc,limit, mArrayListParichayReferral.get(mArrayListParichayReferral.size()-2).getmReferredId());
			 }
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_PARICHAYREF, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_PARICHAYREF, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public String apiRefreshFeedAction(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedActionParichayReferred(AppConstants.INTENTCONSTANTS.PARICHAYREF);
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_ACTION_PAR, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_ACTION_PAR, jsonObj, TAG);	
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
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.parichayReferral);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					ContentValues mValues = new ContentValues();
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_JOB_ID, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobId));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.referredId));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_FOR, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobReferredFor));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_DATE, Utilities.getMilliSecond(mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.referredDate)));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_NAME, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.name));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_TYPE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobUnit));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_TELEPHONIC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isTelephonic));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_ONLINE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isOnlineWritten));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR1, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isPR1));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR2, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isPR2));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_HR, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isHR));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_DUPLICATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isDuplicate));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REASON, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.reason));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL1, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment1));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL2, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment2));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL3, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment3));
					
					getActivity().getContentResolver().insert(DBConstant.Parichay_Referral_Columns.CONTENT_URI, mValues);
				}
				
				if(isToAddOldData){
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.PARICHAYREF);					
				}else{
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.PARICHAYREF);	
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataFromApiAction(String mResponseFromApi, boolean isToAddOldData){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.parichayReferral);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					ContentValues mValues = new ContentValues();
					String mReferredId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.referredId);
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_JOB_ID, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobId));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID, mReferredId);
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_TYPE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobUnit));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_TELEPHONIC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isTelephonic));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_ONLINE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isOnlineWritten));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR1, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isPR1));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR2, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isPR2));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_HR, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isHR));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_DUPLICATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isDuplicate));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REASON, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.reason));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL1, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment1));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL2, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment2));
					mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL3, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment3));
					
					getActivity().getContentResolver().update(DBConstant.Parichay_Referral_Columns.CONTENT_URI, mValues, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + "=?", new String[]{mReferredId});
				}
				updateArrayListParichayReferralObjectWithLatestFeedActionCount();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateArrayListParichayReferralObjectWithLatestFeedActionCount(){
		try{
			Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Parichay_Referral_Columns.CONTENT_URI, null,  null, null, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_DATE + " DESC");
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				int mIntReferralId = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID);
				int mIntReason = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REASON);
				int mIntTelephonic = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_TELEPHONIC);
				int mIntOnlineWritten = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_ONLINE);
				int mIntPR1 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR1);
				int mIntPR2 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR2);
				int mIntHR = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_HR);
				int mIntDuplicate = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_DUPLICATE);
				int mIntInstall1 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL1);
				int mIntInstall2 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL2);
				int mIntInstall3 = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL3);
				int i = 0;
				do{
					ParichayReferral Obj = mArrayListParichayReferral.get(i);
					if(!Obj.getmType().toString().equalsIgnoreCase(AppConstants.PARICHAY.FOOTER)
							|| !Obj.getmType().toString().equalsIgnoreCase(AppConstants.PARICHAY.BANNER) 
							){
						if(Obj.getmReferredId().equalsIgnoreCase(mCursor.getString(mIntReferralId))){
							Obj.setmReason(mCursor.getString(mIntReason));
							Obj.setIsTelephone(Integer.parseInt(mCursor.getString(mIntTelephonic)));
							Obj.setIsOnlineWritten(Integer.parseInt(mCursor.getString(mIntOnlineWritten)));
							Obj.setIsPR1(Integer.parseInt(mCursor.getString(mIntPR1)));
							Obj.setIsPR2(Integer.parseInt(mCursor.getString(mIntPR2)));
							Obj.setIsHR(Integer.parseInt(mCursor.getString(mIntHR)));
							Obj.setIsInstallment1(Integer.parseInt(mCursor.getString(mIntInstall1)));
							Obj.setIsInstallment2(Integer.parseInt(mCursor.getString(mIntInstall2)));
							Obj.setIsInstallment3(Integer.parseInt(mCursor.getString(mIntInstall3)));
							Obj.setIsDuplicate(Integer.parseInt(mCursor.getString(mIntDuplicate)));
						}
					}
					i++;
				}while(mCursor.moveToNext());
			}
			
			if(mCursor!=null){
				mCursor.close();
			}
			mRecyclerView.getAdapter().notifyDataSetChanged();
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
		private boolean isAutoRefresh = false;
		private int limit;

		public AsyncRefreshTask(boolean isRefreshFeed, boolean sortByAsc,int limit, boolean isAutoRefresh){
			this.isRefreshFeed = isRefreshFeed;
			this.sortByAsc = sortByAsc;
			this.limit = limit;
			this.isAutoRefresh = isAutoRefresh;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mArrayListParichayReferral == null) {
				mProgressDialog = new MobcastProgressDialog(getActivity());
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
			
			if(!sortByAsc){
				for(int i = 0 ; i < mGridColumn ; i++){
					ParichayReferral Obj = new ParichayReferral();
					Obj.setmType(AppConstants.PARICHAY.FOOTER);
					mArrayListParichayReferral.add(Obj);
				}
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = isRefreshFeed ? apiRefreshFeedParichay(sortByAsc,limit) : apiRefreshFeedAction();
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
					for(int i = 0; i < mGridColumn;i++){
						mArrayListParichayReferral.remove(mArrayListParichayReferral.size()-1);
					}
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}
				
				if (isSuccess && isRefreshFeed) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
				}if (isSuccess && !isRefreshFeed) {
					parseDataFromApiAction(mResponseFromApi, !sortByAsc);
				}else if(!isSuccess){
					if(sortByAsc){
						if(!isAutoRefresh && isRefreshFeed){
							AndroidUtilities.showSnackBar(getActivity(), Utilities.getErrorMessageFromApi(mResponseFromApi));	
						}
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
