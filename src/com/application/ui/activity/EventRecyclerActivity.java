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
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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
import android.support.v7.widget.GridLayoutManager;
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

import com.application.beans.Award;
import com.application.beans.Event;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.EventRecyclerAdapter;
import com.application.ui.adapter.EventRecyclerAdapter.OnItemClickListenerE;
import com.application.ui.adapter.EventRecyclerAdapter.OnItemLongClickListenerE;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.VerticalDividerItemDecoration;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FetchFeedActionAsyncTask;
import com.application.utils.FetchFeedActionAsyncTask.OnPostExecuteFeedActionTaskListener;
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
public class EventRecyclerActivity extends SwipeBackBaseActivity {
	@SuppressWarnings("unused")
	private static final String TAG = EventRecyclerActivity.class.getSimpleName();
	
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
	
	private EventRecyclerAdapter mAdapter;
	
	private ArrayList<Event> mArrayListEvent;
	
	private Context mContext;
	
	private GridLayoutManager mGridLayoutManager;
	
	private int whichTheme;
	
	private boolean isGrid = false;
	private int mGridColumn = 1;
	
    private boolean mLoadMore = false; 
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_event);
		setSecurity();
		initToolBar();
		initUi();
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
		setMaterialRippleView();
		syncDataWithApi();
		applyTheme();
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		Utilities.showBadgeNotification(EventRecyclerActivity.this);
	}
	
	private void syncDataWithApi(){
		try{
			if (mArrayListEvent != null && mArrayListEvent.size() > 0) {
				refreshFeedFromApi(true, true, 0,true);	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressLint("NewApi") @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_event, menu);
	    if(AndroidUtilities.isAboveGingerBread()){
		    MenuItem refreshItem = menu.findItem(R.id.action_refresh_actionable);
		    if(refreshItem!=null){
		    	View mView = MenuItemCompat.getActionView(refreshItem);
		    	MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout)mView.findViewById(R.id.toolBarActionItemRefresh);
		    	mToolBarMenuRefreshProgress = (ProgressWheel)mView.findViewById(R.id.toolBarActionItemProgressWheel);
		    	mToolBarMenuRefresh = (ImageView)mView.findViewById(R.id.toolBarActionItemImageView);
		    	mToolBarMenuRefreshLayout.setOnClickListener(new View.OnClickListener() {
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
	        	AndroidUtilities.exitWindowAnimation(EventRecyclerActivity.this);
	        	return true;
	        case R.id.action_search:
	        	Intent mIntent = new Intent(EventRecyclerActivity.this, SearchActivity.class);
	        	mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.EVENT);
	        	startActivity(mIntent);
	        	AndroidUtilities.enterWindowAnimation(EventRecyclerActivity.this);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void toolBarRefresh(){
		if(!mSwipeRefreshLayout.isRefreshing()){
			mSwipeRefreshLayout.setRefreshing(true);
		}
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		if (!mLoadMore) {
            	mLoadMore = true;
            	refreshFeedFromApi(true, true, 0,false);
        }
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
//		setMaterialRippleView();
		setClickListener();
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
			ThemeUtils.getInstance(EventRecyclerActivity.this).applyThemeCapture(EventRecyclerActivity.this, EventRecyclerActivity.this, mToolBar);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setClickListener(){
		mEmptyRefreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				refreshFeedFromApi(true, true, AppConstants.BULK,true);
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mToolBarMenuRefresh.setVisibility(View.GONE);
				mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
				refreshFeedFromApi(true, true, 0,false);
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void refreshFeedFromApi(boolean isRefreshFeed, boolean sortByAsc, int limit, boolean isAutoRefresh){//sortByAsc:true-> new data //sortByAsc:false->Old Data //isAutoRefresh : true->onResume ? false -> onPullToRefresh;
		if(Utilities.isInternetConnected()){
			if(AndroidUtilities.isAboveIceCreamSandWich()){
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit, isAutoRefresh).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			}else{
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit, isAutoRefresh).execute();
			}
		}
	}


	private void initUi(){
		mContext = EventRecyclerActivity.this;
		
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll_wo);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

		mEmptyFrameLayout = (FrameLayout)findViewById(R.id.fragmentEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView)findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView)findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton)findViewById(R.id.layoutEmptyRefreshBtn);
		
		isToApplyGridOrNot();
		
		mGridLayoutManager = new GridLayoutManager(mContext, mGridColumn);
		mRecyclerView.setLayoutManager(mGridLayoutManager);
		
		ApplicationLoader.getPreferences().setViewIdEvent("-1");
		
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
		mToolBar.setTitle(getResources().getString(R.string.EventRecyclerActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void setRecyclerAdapter(){
	    mAdapter = new EventRecyclerAdapter(mContext, mArrayListEvent);
	    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(false);
	    if(AndroidUtilities.isAboveIceCreamSandWich()){
        	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
            mRecyclerView.setAdapter(mScaleInAdapter);
        }else{
        	mRecyclerView.setAdapter(mAdapter);
        }
		 mRecyclerView.addItemDecoration(
			        new HorizontalDividerItemDecoration.Builder(this)
			                .color(Utilities.getDividerColor())
			                .sizeResId(R.dimen.fragment_recyclerview_divider)
			                .visibilityProvider(mAdapter)
			                .build());
		 
		 if(isGrid){
			 mRecyclerView.addItemDecoration(
				        new VerticalDividerItemDecoration.Builder(this)
				                .color(Utilities.getDividerColor())
				                .sizeResId(R.dimen.fragment_recyclerview_divider)
				                .visibilityProvider(mAdapter)
				                .build());	 
		 }
	}
	
	
	private void setEmptyData() {
		mEmptyTitleTextView.setText(getResources().getString(
				R.string.emptyEventTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyEventMessage));
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
			mAdapter.setOnItemClickListener(new OnItemClickListenerE() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
//					position-=1;
					switch (view.getId()) {
					case R.id.itemRecyclerEventRootLayout:
						Intent mIntentEvent = new Intent(EventRecyclerActivity.this,EventDetailActivity.class);
						mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.EVENT);
						mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListEvent.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentEvent);
						AndroidUtilities.enterWindowAnimation(EventRecyclerActivity.this);
						break;
					}

				}
			});
		}
		
		if(mAdapter!=null){
			mAdapter.setOnItemLongClickListener(new OnItemLongClickListenerE() {
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
				            refreshFeedFromApi(true, false, AppConstants.BULK,false);
			            }
			        }
				}
			});
		}
	}
	
	private void checkDataInAdapter() {
		checkExpiredEventAndDeleteFromDB();
		Cursor mCursor = getContentResolver().query(
				DBConstant.Event_Columns.CONTENT_URI, null, null, null,
				DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED + " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addEventObjectListFromDBToBeans(mCursor, false, false);
			if (mArrayListEvent.size() > 0) {
				setRecyclerAdapter();
			}
		} else {
			setEmptyData();
		}

		if (mCursor != null) {
			mCursor.close();
		}
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
	
	private void checkExpiredEventAndDeleteFromDB(){
		long mCurrentTimeMillis = System.currentTimeMillis();
		getContentResolver().delete(DBConstant.Event_Columns.CONTENT_URI, DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED + "<?", new String[]{String.valueOf(mCurrentTimeMillis)});
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
			mArrayListEvent = new ArrayList<Event>();
		}else{
			if(mArrayListEvent==null){
				mArrayListEvent = new ArrayList<Event>();	
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
				mArrayListEvent.add(Obj);	
			}else{
				mArrayListEvent.add(0,Obj);
			}
		} while (mCursor.moveToNext());
	
		Collections.sort(mArrayListEvent, new EventSort());
	}
	
	private void saveViewPosition(int position){
		ApplicationLoader.getPreferences().setViewIdEvent(String.valueOf(position));
	}
	
	private void checkReadFromDBAndUpdateToObj(){
		int position = Integer.parseInt(ApplicationLoader.getPreferences().getViewIdEvent());
//		position-=1;
		if (position >= 0 && position < mArrayListEvent.size()) {
			if(mArrayListEvent!=null && mArrayListEvent.size() > 0){
				Cursor mCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, new String[]{DBConstant.Event_Columns.COLUMN_EVENT_ID, DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, DBConstant.Event_Columns.COLUMN_EVENT_READ_NO,DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN}, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mArrayListEvent.get(position).getmId()}, null);
				
				if(mCursor!=null && mCursor.getCount() >0){
					mCursor.moveToFirst();
					boolean isToNotify = false;
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ)))){
						mArrayListEvent.get(position).setRead(true);
						isToNotify = true;
					}
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE)))){
						mArrayListEvent.get(position).setLike(true);
						isToNotify = true;
					}else{
						mArrayListEvent.get(position).setLike(false);
						isToNotify = true;
					}
					
					mArrayListEvent.get(position).setmLikeCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO)));
					mArrayListEvent.get(position).setmViewCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO)));
					mArrayListEvent.get(position).setIsGoingToAttend(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN)));
					isToNotify = true;
					
					if(isToNotify){
						mRecyclerView.getAdapter().notifyDataSetChanged();
					}
				}
				
				if(mCursor!=null)
					mCursor.close();
			}
		}
	}
	
	public void passDataToFragment(int mId, String mCategory) {
		try{
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
				Cursor mCursor = null;
				boolean isToAddOldData = false;
				if(mId == -786){
					mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + "<?", new String[]{mArrayListEvent.get(mArrayListEvent.size()-1).getmId()}, DBConstant.Event_Columns.COLUMN_EVENT_ID + " DESC");
					isToAddOldData = true;
				}else{
					mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, null, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + " DESC");
				}
				if(mCursor!=null && mCursor.getCount() > 0){
					mSwipeRefreshLayout.setEnabled(true);
					mSwipeRefreshLayout.setRefreshing(true);
					mCursor.moveToFirst();
					addEventObjectListFromDBToBeans(mCursor, false, isToAddOldData);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(mAdapter!=null){
								mAdapter.addEventObjList(mArrayListEvent);
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
				
				if(mCursor!=null)
					mCursor.close();
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
				int mType = AppConstants.TYPE.EVENT;
				String mTitle = mArrayListEvent.get(mPosition).getmTitle();
				boolean isRead = mArrayListEvent.get(mPosition).isRead();
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
		mBottomSheet = new BottomSheet.Builder(EventRecyclerActivity.this).icon(Utilities.getRoundedBitmapFromSVGForContextMenu(mType, whichTheme)).title(mTitle).sheet(R.menu.context_menu_mobcast).build();
         final Menu menu = mBottomSheet.getMenu();
         
         SpannableString mSpannabledRead = new SpannableString(getResources().getString(R.string.context_menu_read));
         SpannableString mSpannabledUnRead = new SpannableString(getResources().getString(R.string.context_menu_unread));
         SpannableString mSpannabledDelete = new SpannableString(getResources().getString(R.string.context_menu_delete));
         SpannableString mSpannabledView = new SpannableString(getResources().getString(R.string.context_menu_view));
         SpannableString mSpannabledLike = new SpannableString(getResources().getString(R.string.context_menu_like));
         
         mSpannabledRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledRead.length(), 0);
         mSpannabledUnRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledUnRead.length(), 0);
         mSpannabledDelete.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledDelete.length(), 0);
         mSpannabledView.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledView.length(), 0);
         mSpannabledLike.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledLike.length(), 0);
         
         menu.getItem(0).setTitle(mSpannabledRead);
         menu.getItem(1).setTitle(mSpannabledUnRead);
         menu.getItem(2).setTitle(mSpannabledLike);
         menu.getItem(3).setTitle(mSpannabledDelete);
         menu.getItem(4).setTitle(mSpannabledView);
         
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
          * Like
          */
         menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
            	 contextMenuLike(mPosition);
                 return true;
             }
         });
         
         /**
          * Delete
          */
         menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {
            	 showDeleteConfirmationDialog(mPosition, mTitle);
//            	 contextMenuDelete(mPosition);
                 return true;
             }
         });
         
         /**
          * View
          */
         menu.getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
		 ContentValues values = new ContentValues();
		 String mEventId = mArrayListEvent.get(mPosition).getmId();
		 values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, "true");
		 getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, values, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mEventId});	
       	 mArrayListEvent.get(mPosition).setRead(true);
       	 mRecyclerView.getAdapter().notifyItemChanged(mPosition);
       	 UserReport.updateUserReportApi(mEventId, AppConstants.INTENTCONSTANTS.EVENT, AppConstants.REPORT.READ, "");
       	Utilities.showBadgeNotification(EventRecyclerActivity.this);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuMarkAsUnRead(int mPosition){
		try{
		 ContentValues values = new ContentValues();
	     values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, "false");
		 getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, values, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mArrayListEvent.get(mPosition).getmId()});
		 mArrayListEvent.get(mPosition).setRead(false);
       	 mRecyclerView.getAdapter().notifyItemChanged(mPosition);
       	Utilities.showBadgeNotification(EventRecyclerActivity.this);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuDelete(int mPosition){
		try{
		 getContentResolver().delete(DBConstant.Event_Columns.CONTENT_URI, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mArrayListEvent.get(mPosition).getmId()});
       	 mArrayListEvent.remove(mPosition);
       	 if(mArrayListEvent.size() == 0){
       		 checkDataInAdapter();
       	 }else{
       		mRecyclerView.getAdapter().notifyDataSetChanged();
       	 }
       	Utilities.showBadgeNotification(EventRecyclerActivity.this);
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
	
	private void contextMenuLike(int mPosition){
		try{
			 if(!mArrayListEvent.get(mPosition).isLike()){
				 String mEventId = mArrayListEvent.get(mPosition).getmId();
				 ContentValues values = new ContentValues();
			     values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, "true");
				 getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, values, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mEventId});
				 mArrayListEvent.get(mPosition).setLike(true);
				 mArrayListEvent.get(mPosition).setmLikeCount(String.valueOf(Integer.parseInt(mArrayListEvent.get(mPosition).getmLikeCount())+1));
		       	 mRecyclerView.getAdapter().notifyItemChanged(mPosition);
		       	 UserReport.updateUserReportApi(mEventId, AppConstants.INTENTCONSTANTS.EVENT, AppConstants.REPORT.LIKE, "");
			 }
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	private void showDeleteConfirmationDialog(final int mPosition, String mTitle){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(EventRecyclerActivity.this)
        .title(getResources().getString(R.string.content_delete_message) + " " + mTitle + "?")
        .iconRes(R.drawable.context_menu_delete)
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.button_delete))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.sample_fragment_settings_dialog_language_negative))
        .negativeColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            	contextMenuDelete(mPosition);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
	/*
	 * AsyncTask To Refresh
	 */
	
	public String apiRefreshFeedMobcast(boolean sortByAsc, int limit){
		try {
			JSONObject jsonObj =null;
			 if(sortByAsc){
				jsonObj = JSONRequestBuilder.getPostFetchFeedEvent(sortByAsc,limit, mArrayListEvent != null ? mArrayListEvent.get(0).getmId() : String.valueOf("0"));
			 }else{
//				 jsonObj= JSONRequestBuilder.getPostFetchFeedEvent(sortByAsc,limit, mArrayListEvent.get(mArrayListEvent.size()-2).getmId());
				 jsonObj= JSONRequestBuilder.getPostFetchFeedEvent(sortByAsc,limit, isGrid?mArrayListEvent.get(mArrayListEvent.size()-3).getmId():mArrayListEvent.get(mArrayListEvent.size()-2).getmId());
			 }
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_EVENT, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_EVENT, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public String apiRefreshFeedAction(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.EVENT);
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
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.event);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					String mEventId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventId);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventSentDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventSentTime);
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventExpiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventExpiryTime);
					String mFileLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventCoverLink);
					String mFileName = Utilities.getFileName(mFileLink);
					ContentValues values = new ContentValues();
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_ID, mEventId);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_TITLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventTitle));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_BY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventBy));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_DESC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventDescription));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventGoingCount));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventLikeCount));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventReadCount));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventInvitedCount));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE, mDate);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME, mTime);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsRead));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsLiked));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsSharing));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsGoing));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK, mFileLink);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH, Utilities.getFilePath(AppConstants.TYPE.IMAGE, false, mFileName));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFileSize));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventStartDate));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventEndDate));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFromTime));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventToTime));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_VENUE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventVenue));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_MAP, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventMap));
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE, mExpiryDate);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_TIME, mExpiryTime);
					values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
					
					
					Uri isInsertUri = getContentResolver().insert(DBConstant.Event_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					/*if(isInsertedInDB){
						ApplicationLoader.getPreferences().setLastIdEvent(mEventId);
					}*/
				}
				
				if(isToAddOldData){
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.EVENT);					
				}else{
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.EVENT);	
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
			if (mArrayListEvent == null) {
				mProgressDialog = new MobcastProgressDialog(EventRecyclerActivity.this);
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
			
			if(!sortByAsc){
				for(int i = 0 ; i < mGridColumn ; i++){
					Event Obj = new Event();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					mArrayListEvent.add(Obj);
				}
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = isRefreshFeed ? apiRefreshFeedMobcast(sortByAsc,limit) : apiRefreshFeedAction();
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
						mArrayListEvent.remove(mArrayListEvent.size()-1);	
					}
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}
				
				if (isSuccess) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
				}else{
					if(sortByAsc){
						if(!isAutoRefresh){
							AndroidUtilities.showSnackBar(EventRecyclerActivity.this, Utilities.getErrorMessageFromApi(mResponseFromApi));
						}else{//show new events available
							
						}
					}
				}
				
				if(sortByAsc){
					refreshFeedActionFromApi(isAutoRefresh);
				}

				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
				
//				if(mSwipeRefreshLayout.isRefreshing()){
					mToolBarMenuRefresh.setVisibility(View.VISIBLE);
					mToolBarMenuRefreshProgress.setVisibility(View.GONE);
					mSwipeRefreshLayout.setRefreshing(false);
//				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}
	
	/**
	 * Async : Refresh Feed Like + Read count
	 */
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void refreshFeedActionFromApi(boolean isAutoRefresh){
		try{
			if(Utilities.isInternetConnected()){
				if(!isAutoRefresh){
					if(!mSwipeRefreshLayout.isRefreshing()){
						mSwipeRefreshLayout.setRefreshing(true);
					}
				}
				FetchFeedActionAsyncTask mFetchFeedActionAsyncTask = new FetchFeedActionAsyncTask(EventRecyclerActivity.this, AppConstants.INTENTCONSTANTS.EVENT, JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.EVENT), TAG);
				if(AndroidUtilities.isAboveIceCreamSandWich()){
					mFetchFeedActionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				}else{
					mFetchFeedActionAsyncTask.execute();
				}
				mFetchFeedActionAsyncTask.setOnPostExecuteFeedActionTaskListener(new OnPostExecuteFeedActionTaskListener() {
					@Override
					public void onPostExecute(String mResponseFromApi, boolean isSuccess) {
						// TODO Auto-generated method stub
						if(isSuccess){
							updateArrayListEventObjectWithLatestFeedActionCount();
						}
						mSwipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateArrayListEventObjectWithLatestFeedActionCount(){
		try{
			Cursor mCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, new String[]{DBConstant.Event_Columns.COLUMN_ID, DBConstant.Event_Columns.COLUMN_EVENT_ID, DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, DBConstant.Event_Columns.COLUMN_EVENT_READ_NO},  null, null, DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED + " DESC");
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				int mIntEventId = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_ID);
				int mIntEventLikeCount = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO);
				int mIntEventViewCount = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO);
				int mIntEventGoingCount = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO);
				int i = 0;
				do{
					Event Obj = mArrayListEvent.get(i);
					if(!Obj.getmFileType().toString().equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)){
						if(Obj.getmId().equalsIgnoreCase(mCursor.getString(mIntEventId))){
							Obj.setmLikeCount(mCursor.getString(mIntEventLikeCount));
							Obj.setmViewCount(mCursor.getString(mIntEventViewCount));
							Obj.setmGoingCount(mCursor.getString(mIntEventGoingCount));
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
	
	private void isToApplyGridOrNot(){
		try{
			if(BuildVars.IS_GRID){
				if(AndroidUtilities.getScreenSizeInInches() >= 7.0 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					isGrid = true;
					mGridColumn = 2;
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
