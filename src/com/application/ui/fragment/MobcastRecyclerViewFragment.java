package com.application.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

import com.application.beans.Mobcast;
import com.application.beans.MobcastFileInfo;
import com.application.sqlite.DBConstant;
import com.application.ui.activity.AudioDetailActivity;
import com.application.ui.activity.DocDetailActivity;
import com.application.ui.activity.FeedbackActivity;
import com.application.ui.activity.ImageDetailActivity;
import com.application.ui.activity.MotherActivity;
import com.application.ui.activity.PdfDetailActivity;
import com.application.ui.activity.PptDetailActivity;
import com.application.ui.activity.TextDetailActivity;
import com.application.ui.activity.VideoDetailActivity;
import com.application.ui.activity.XlsDetailActivity;
import com.application.ui.activity.YouTubeLiveStreamActivity;
import com.application.ui.adapter.MobcastRecyclerAdapter;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemClickListener;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.VerticalDividerItemDecoration;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FetchFeedActionAsyncTask;
import com.application.utils.FetchFeedActionAsyncTask.OnPostExecuteFeedActionTaskListener;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.ScrollUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mobcast.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.squareup.okhttp.OkHttpClient;

public class MobcastRecyclerViewFragment extends BaseFragment implements IFragmentCommunicator{
	private static final String TAG = MobcastRecyclerViewFragment.class
			.getSimpleName();
	public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

	private IActivityCommunicator mActivityCommunicator;
	
	private Activity mParentActivity;

	private View headerView;

	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;
	
	private FrameLayout mNewAvailFrameLayout;
	private AppCompatTextView mNewAvailBubbleTextView;
	
	private Animation mAnimSlideInUp;
	private Animation mAnimSlideOutUp;

	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private AppCompatButton mEmptyRefreshBtn;

	private ObservableRecyclerView mRecyclerView;
	private MobcastRecyclerAdapter mAdapter;
	
	private ArrayList<Mobcast> mArrayListMobcast;
	
	private Context mContext;
	
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
       ((MotherActivity)mContext).mFragmentCommunicator = this;
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
		

		mNewAvailFrameLayout = (FrameLayout) view.findViewById(R.id.fragmentMobcastNewAvailLayout);

		mNewAvailBubbleTextView = (AppCompatTextView) view.findViewById(R.id.layoutNewBroadcastTv);

		
		isToApplyGridOrNot();
		
		mGridLayoutManager = new GridLayoutManager(mParentActivity, mGridColumn);
		mRecyclerView.setLayoutManager(mGridLayoutManager);
		
		ApplicationLoader.getPreferences().setViewIdMobcast("-1");
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
		checkDataInAdapter();
//		setRecyclerAdapter();

		setSwipeRefreshLayoutCustomisation();
		setMaterialRippleView();
		syncDataWithApi();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		ApplicationLoader.trackScreenViewV3(ApplicationLoader.getApplication().getResources().getString(R.string.com_application_ui_fragment_MobcastRecyclerViewFragment));
		checkForNewMobcastDataAvail();
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setClickListener();
	}

	/**
	 * <b>Description: </b></br>Api : Sync Data - Calls on App Opens</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void syncDataWithApi(){
		try{
			if (mArrayListMobcast != null && mArrayListMobcast.size() > 0) {
				refreshFeedFromApi(true, true, 0, true);	
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

	/**
	 * <b>Description: </b></br>Adapter : Check Data available from DB and feed to Adapter</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void checkDataInAdapter() {
		checkExpiredMobcastAndDeleteFromDB();
		Cursor mCursor = getActivity().getContentResolver().query(
				DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null,
				DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED + " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addMobcastObjectListFromDBToBeans(mCursor, false, false);
			if (mArrayListMobcast.size() > 0) {
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

	private void setEmptyData() {
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * <b>Description: </b></br>Bubble : New Mobcast Available bubble</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setNewAvailBubbleLayout(){
		try{
			mAnimSlideInUp = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_down);
			mAnimSlideOutUp = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_up);
			
			mNewAvailBubbleTextView.setText(getResources().getString(R.string.new_mobcast_avail));
			
			mAnimSlideOutUp.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					mNewAvailFrameLayout.setVisibility(View.GONE);
				}
			});
			
			mAnimSlideInUp.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					mNewAvailFrameLayout.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mNewAvailFrameLayout.startAnimation(mAnimSlideOutUp);
						}
					}, AppConstants.BUBBLESTAY);
				}
			});
			
			mNewAvailFrameLayout.startAnimation(mAnimSlideInUp);
			
			mNewAvailFrameLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					try{
						mNewAvailFrameLayout.startAnimation(mAnimSlideOutUp);
						mRecyclerView.smoothScrollToPosition(1);
					}catch(Exception e){
						FileLog.e(TAG, e.toString());
					}
				}
			});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	

	@SuppressLint("NewApi") 
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (mArrayListMobcast != null && mArrayListMobcast.size() > 0) {
					refreshFeedFromApi(true, true, 0, false);	
				}
			}
		});
	}
	
	/**
	 * <b>Description: </b></br>Api : Refresh Feed</br></br>
	 * <b>Parameters : </b></br>//sortByAsc:true-> new data //sortByAsc:false->Old Data //isAutoRefresh: true-> onResume ? false-> onPullToRefresh
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	@SuppressLint("NewApi") 
	private void refreshFeedFromApi(boolean isRefreshFeed, boolean sortByAsc, int limit, boolean isAutoRefresh){
		if(Utilities.isInternetConnected()){
			if(AndroidUtilities.isAboveIceCreamSandWich()){
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit, isAutoRefresh).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			}else{
				new AsyncRefreshTask(isRefreshFeed,sortByAsc,limit,isAutoRefresh).execute();
			}
		}
	}
	
	/**
	 * <b>Description: </b></br>Sort : Mobcast Sort by Desc - mMobcastId</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
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
	
	/**
	 * <b>Description: </b></br>Expiry : Check Expired Mobcast and delete</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void checkExpiredMobcastAndDeleteFromDB(){
		long mCurrentTimeMillis = System.currentTimeMillis();
		getActivity().getContentResolver().delete(DBConstant.Mobcast_Columns.CONTENT_URI, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED + "<?", new String[]{String.valueOf(mCurrentTimeMillis)});
	}
	
	/**
	 * <b>Description: </b></br>ObjectList : Add mobcast object list from DB</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
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
		
		Cursor mCursorFile = getActivity().getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, null, null, null);
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
			mArrayListMobcast = new ArrayList<Mobcast>();
		}else{
			if(mArrayListMobcast==null){
				mArrayListMobcast = new ArrayList<Mobcast>();	
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
				Cursor mCursorFileInfo = getActivity().getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
				
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
				Cursor mCursorFeedbackInfo = getActivity().getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?", new String[]{mMobcastId}, DBConstant.Mobcast_Feedback_Columns.COLUMN_ID + " ASC");
				
				if(mCursorFeedbackInfo!=null && mCursorFeedbackInfo.getCount() > 0){
					mCursorFeedbackInfo.moveToFirst();
					ArrayList<MobcastFileInfo> mMobcastFeedbackInfoList = new ArrayList<>();
					MobcastFileInfo feedbackObj = new MobcastFileInfo();
					feedbackObj.setmFileIsDefault(mCursorFeedbackInfo.getString(mCursorFeedbackInfo.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ATTEMPT)));
					if(mCursorFeedbackInfo.getCount() ==1){
						feedbackObj.setmPages(mCursorFeedbackInfo.getCount() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));						
					}else{
						feedbackObj.setmPages(mCursorFeedbackInfo.getCount() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_questions));	
					}
					mMobcastFeedbackInfoList.add(feedbackObj);
					Obj.setmFileInfo(mMobcastFeedbackInfoList);
				}
				
				if(mCursorFeedbackInfo!=null){
					mCursorFeedbackInfo.close();
				}
			}
			
			
			if(!isFromBroadCastReceiver){
				mArrayListMobcast.add(Obj);	
			}else{
				mArrayListMobcast.add(0,Obj);
			}
		} while (mCursor.moveToNext());
	
		Collections.sort(mArrayListMobcast, new MobcastSort());
	}

	private void setRecyclerAdapter() {
		mAdapter = new MobcastRecyclerAdapter(getActivity(),
				mArrayListMobcast, headerView, isGrid);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(false);
		if (AndroidUtilities.isAboveIceCreamSandWich()) {
			AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(
					mAdapter);
			ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(
					mAlphaAdapter);
			mRecyclerView.setAdapter(mScaleInAdapter);
		} else {
			mRecyclerView.setAdapter(mAdapter);
		}
		mRecyclerView
				.addItemDecoration(new HorizontalDividerItemDecoration.Builder(
						getActivity()).color(Utilities.getDividerColor())
						.sizeResId(R.dimen.fragment_recyclerview_divider)
						.visibilityProvider(mAdapter).build());
		
		if(isGrid){
			mRecyclerView
			.addItemDecoration(new VerticalDividerItemDecoration.Builder(
					getActivity()).color(Utilities.getDividerColor())
					.sizeResId(R.dimen.fragment_recyclerview_divider)
					.visibilityProvider(mAdapter).build());	
		}

		if (mParentActivity instanceof ObservableScrollViewCallbacks) {
			// Scroll to the specified offset after layout
			Bundle args = getArguments();
			if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
				final int initialPosition = args
						.getInt(ARG_INITIAL_POSITION, 0);
				ScrollUtils.addOnGlobalLayoutListener(mRecyclerView,
						new Runnable() {
							@Override
							public void run() {
								mRecyclerView
										.scrollVerticallyToPosition(initialPosition);
							}
						});
			}
			mRecyclerView
					.setScrollViewCallbacks((ObservableScrollViewCallbacks) mParentActivity);
		}
	}

	private void setRecyclerAdapterListener() {
		if(mAdapter!=null){
			mAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					position = isGrid ? position - 2 : position - 1;
					switch (view.getId()) {
					case R.id.itemRecyclerMobcastVideoRootLayout:
						Intent mIntentVideo = new Intent(mParentActivity,
								VideoDetailActivity.class);
						mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentVideo);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastLiveRootLayout:
						Intent mIntentLive = new Intent(mParentActivity,
								YouTubeLiveStreamActivity.class);
						mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentLive);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastImageRootLayout:
						Intent mIntentImage = new Intent(mParentActivity,ImageDetailActivity.class);
						mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentImage);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastFeedbackRootLayout:
						boolean feedbackSingleAttemptAndRead = false;
						if(Boolean.parseBoolean(mArrayListMobcast.get(position).getmFileInfo().get(0).getmFileIsDefault())){
								if(mArrayListMobcast.get(position).isRead()){
									feedbackSingleAttemptAndRead = true;
								}
						}
						if(!feedbackSingleAttemptAndRead){
							Intent mIntentFeedback = new Intent(mParentActivity,FeedbackActivity.class);
							mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
							mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
							saveViewPosition(position);
							startActivity(mIntentFeedback);
							AndroidUtilities.enterWindowAnimation(mParentActivity);
						}else{
							AndroidUtilities.showSnackBar(getActivity(), getActivity().getResources().getString(R.string.feedback_attempted));
						}
						break;
					case R.id.itemRecyclerMobcastAudioRootLayout:
						Intent mIntentAudio = new Intent(mParentActivity,AudioDetailActivity.class);
						mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentAudio);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastTextRootLayout:
						Intent mIntentText = new Intent(mParentActivity,TextDetailActivity.class);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentText);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastPdfRootLayout:
						Intent mIntentPdf = new Intent(mParentActivity,PdfDetailActivity.class);
						mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentPdf);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastXlsRootLayout:
						Intent mIntentXls = new Intent(mParentActivity,XlsDetailActivity.class);
						mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentXls);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastDocRootLayout:
						Intent mIntentDoc = new Intent(mParentActivity,DocDetailActivity.class);
						mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentDoc);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastPptRootLayout:
						Intent mIntentPpt = new Intent(mParentActivity,PptDetailActivity.class);
						mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentPpt);
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
//					if(isGrid){
//						position+=1;
//					}
					showContextMenu(position, view);
				}
			});
		}
	}
	
	private void saveViewPosition(int position){
		ApplicationLoader.getPreferences().setViewIdMobcast(String.valueOf(position));
	}
	
	private void checkForNewMobcastDataAvail(){
		try{
			if(ApplicationLoader.getPreferences().isRefreshMobcastWithNewDataAvail()){
				ApplicationLoader.getPreferences().setRefreshMobcastWithNewDataAvail(false);
				passDataToFragment(-1, AppConstants.INTENTCONSTANTS.MOBCAST);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void checkReadFromDBAndUpdateToObj(){
		int position = Integer.parseInt(ApplicationLoader.getPreferences().getViewIdMobcast());
//		position-=1;
		if (position >= 0 && position < mArrayListMobcast.size()) {
			if(mArrayListMobcast!=null && mArrayListMobcast.size() > 0){
				Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO,DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT}, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mArrayListMobcast.get(position).getmId()}, null);
				
				if(mCursor!=null && mCursor.getCount() >0){
					mCursor.moveToFirst();
					boolean isToNotify = false;
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)))){
						mArrayListMobcast.get(position).setRead(true);
						mArrayListMobcast.get(position).setmViewCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
						isToNotify = true;
					}
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)))){
						mArrayListMobcast.get(position).setLike(true);
						isToNotify = true;
					}else{
						mArrayListMobcast.get(position).setLike(false);
						isToNotify = true;
					}
					
					mArrayListMobcast.get(position).setmLikeCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
					mArrayListMobcast.get(position).setmViewCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
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

	/* (non-Javadoc)
	 * @see com.application.ui.fragment.IFragmentCommunicator#passDataToFragment(int, java.lang.String)
	 */
	@Override
	public void passDataToFragment(int mId, String mCategory) {
		try{
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				Cursor mCursor = null;
				boolean isToAddOldData = false;
				boolean isFromBroadCastReceiver = false;
				if(mId == -786){
					mCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "<?", new String[]{mArrayListMobcast.get(mArrayListMobcast.size()-1).getmId()}, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " DESC");
					isToAddOldData = true;
				}else{
					mCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " DESC");
					if(mId ==-1){
						isFromBroadCastReceiver = true;
						setNewAvailBubbleLayout();
					}
				}
				if(mCursor!=null && mCursor.getCount() > 0){
					mSwipeRefreshLayout.setEnabled(true);
					mSwipeRefreshLayout.setRefreshing(true);
					mCursor.moveToFirst();
					addMobcastObjectListFromDBToBeans(mCursor, isFromBroadCastReceiver, isToAddOldData);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(mAdapter!=null){
								mAdapter.addMobcastObjList(mArrayListMobcast);
								mRecyclerView.getAdapter().notifyDataSetChanged();
							}else{
								setRecyclerAdapter();
								setUiListener();
							}
							mSwipeRefreshLayout.setRefreshing(false);
							
							mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.MOBCAST);
							
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
	
	
	/**
	 * ContextMenu
	 */
	private void showContextMenu(int mPosition, View mView){
		try{
			mPosition =  mPosition - mGridColumn;
			if(mPosition!= -1){
				int mType = Utilities.getMediaType(mArrayListMobcast.get(mPosition).getmFileType());
				String mTitle = mArrayListMobcast.get(mPosition).getmTitle();
				boolean isRead = mArrayListMobcast.get(mPosition).isRead();
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
		mBottomSheet = new BottomSheet.Builder(getActivity()).icon(Utilities.getRoundedBitmapFromSVGForContextMenu(mType, whichTheme)).title(mTitle).sheet(R.menu.context_menu_mobcast).build();
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
		 if(!mArrayListMobcast.get(mPosition).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FEEDBACK)){
			 String mMobcastId = mArrayListMobcast.get(mPosition).getmId();
			 values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, "true");
			 getActivity().getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId});	
	       	 mArrayListMobcast.get(mPosition).setRead(true);
	       	 mRecyclerView.getAdapter().notifyItemChanged(isGrid ? mPosition+2 : mPosition+1);
	       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.MOBCAST);
	       	 UserReport.updateUserReportApi(mMobcastId, AppConstants.INTENTCONSTANTS.MOBCAST, AppConstants.REPORT.READ, "");
	       	 Utilities.showBadgeNotification(getActivity());
		 }else{
			 AndroidUtilities.showSnackBar(getActivity(), getActivity().getResources().getString(R.string.feedback_read));
		 }
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuMarkAsUnRead(int mPosition){
		try{
		 if(!mArrayListMobcast.get(mPosition).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FEEDBACK)){
			 ContentValues values = new ContentValues();
		     values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, "false");
			 getActivity().getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mArrayListMobcast.get(mPosition).getmId()});
			 mArrayListMobcast.get(mPosition).setRead(false);
			 mRecyclerView.getAdapter().notifyItemChanged(isGrid ? mPosition+2 : mPosition+1);
	       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.MOBCAST);
	       	Utilities.showBadgeNotification(getActivity());
		 }
       	else{
			 AndroidUtilities.showSnackBar(getActivity(), getActivity().getResources().getString(R.string.feedback_unread));
		 }
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuDelete(int mPosition){
		try{
		 getActivity().getContentResolver().delete(DBConstant.Mobcast_Columns.CONTENT_URI, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mArrayListMobcast.get(mPosition).getmId()});
       	 getActivity().getContentResolver().delete(DBConstant.Mobcast_File_Columns.CONTENT_URI, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mArrayListMobcast.get(mPosition).getmId()});
       	 mArrayListMobcast.remove(mPosition);
       	 if(mArrayListMobcast.size() == 0){
       		 checkDataInAdapter();
       	 }else{
       		mRecyclerView.getAdapter().notifyDataSetChanged();
       	 }
       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.MOBCAST);		
       	Utilities.showBadgeNotification(getActivity());
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
			 if(!mArrayListMobcast.get(mPosition).isLike()){
				 String mMobcastId = mArrayListMobcast.get(mPosition).getmId();
				 ContentValues values = new ContentValues();
			     values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, "true");
				 getActivity().getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId});
				 mArrayListMobcast.get(mPosition).setLike(true);
				 mArrayListMobcast.get(mPosition).setmLikeCount(String.valueOf(Integer.parseInt(mArrayListMobcast.get(mPosition).getmLikeCount())+1));
				 mRecyclerView.getAdapter().notifyItemChanged(isGrid ? mPosition+2 : mPosition+1);
		       	UserReport.updateUserReportApi(mMobcastId, AppConstants.INTENTCONSTANTS.MOBCAST, AppConstants.REPORT.LIKE, "");
		       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.MOBCAST);
			 }
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void showDeleteConfirmationDialog(final int mPosition, String mTitle){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(getActivity())
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
	
	/**
	 * AsyncTask To Refresh
	 */
	public String apiRefreshFeedMobcast(boolean sortByAsc, int limit){
		try {
			JSONObject jsonObj =null;
			 if(sortByAsc){
				jsonObj = JSONRequestBuilder.getPostFetchFeedMobcast(sortByAsc,limit, mArrayListMobcast != null ? mArrayListMobcast.get(0).getmId() : String.valueOf("0"));
			 }else{
				 jsonObj= JSONRequestBuilder.getPostFetchFeedMobcast(sortByAsc,limit, isGrid?mArrayListMobcast.get(mArrayListMobcast.size()-3).getmId():mArrayListMobcast.get(mArrayListMobcast.size()-2).getmId());
			 }
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_MOBCAST, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_MOBCAST, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public String apiRefreshFeedAction(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.MOBCAST);
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
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcast);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					String mMobcastId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastId);
					String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastType);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastTime);
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
					
					Uri isInsertUri = getActivity().getContentResolver().insert(DBConstant.Mobcast_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					/*if(isInsertedInDB && !isToAddOldData){
						ApplicationLoader.getPreferences().setLastIdMobcast(mMobcastId);
					}*/
					final int mIntType = Utilities.getMediaType(mType);
					
					if (mIntType != AppConstants.TYPE.FEEDBACK && mIntType != AppConstants.TYPE.TEXT) {
						JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFileInfo);
						
						for (int j = 0; j < mJSONArrMobFileObj.length(); j++) {
							JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(j);
							ContentValues valuesFileInfo = new ContentValues();
							final String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastThumbnail);
							String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLink);
							final String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileName);
							String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastisDefault);
							final String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID, mMobcastId);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK, mFileLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileId));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLang));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileSize));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileDuration));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFilePages));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, mIsDefault);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLiveStream));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLiveStreamYouTube));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastReadDuration));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME, mFileName);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK, mThumbnailLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH, mThumbnailName);
							
							getActivity().getContentResolver().insert(DBConstant.Mobcast_File_Columns.CONTENT_URI, valuesFileInfo);
							
						}
					}else if(mIntType == AppConstants.TYPE.FEEDBACK){
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
								
								Uri isInsertFeedbackInfoUri = getActivity().getContentResolver().insert(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, valuesFeedbackInfo);
								if(BuildVars.DEBUG){
									Utilities.checkWhetherInsertedOrNot(TAG,isInsertFeedbackInfoUri);
								}
						}
					}
				}
				
				if(isToAddOldData){
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.MOBCAST);					
				}else{
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.MOBCAST);	
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
			if (mArrayListMobcast == null) {
				mProgressDialog = new MobcastProgressDialog(getActivity());
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
			
			if(!sortByAsc){
				for(int i = 0 ; i < mGridColumn ; i++){
					Mobcast Obj = new Mobcast();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					mArrayListMobcast.add(Obj);
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
						mArrayListMobcast.remove(mArrayListMobcast.size()-1);
					}
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}
				
				if (isSuccess) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
					if(sortByAsc){
						if(isAutoRefresh){
							setNewAvailBubbleLayout();
						}
					}
				}else{
					if(sortByAsc){
						if(!isAutoRefresh){
							AndroidUtilities.showSnackBar(getActivity(), Utilities.getErrorMessageFromApi(mResponseFromApi));	
						}
					}
				}
				
				if(sortByAsc){
					refreshFeedActionFromApi(isAutoRefresh);
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
	
	/**
	 * Async : Refresh Feed Like + Read count
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	private void refreshFeedActionFromApi(boolean isAutoRefresh){
		try{
			if(Utilities.isInternetConnected()){
				if(!isAutoRefresh){
					if(!mSwipeRefreshLayout.isRefreshing()){
						mSwipeRefreshLayout.setRefreshing(true);
					}
				}
				FetchFeedActionAsyncTask mFetchFeedActionAsyncTask = new FetchFeedActionAsyncTask(mParentActivity, AppConstants.INTENTCONSTANTS.MOBCAST, JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.MOBCAST), TAG);
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
							updateArrayListMobcastObjectWithLatestFeedActionCount();
						}
						mSwipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateArrayListMobcastObjectWithLatestFeedActionCount(){
		try{
			Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT},  null, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED + " DESC");
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				int mIntMobcastId = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
				int mIntMobcastLikeCount = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO);
				int mIntMobcastViewCount = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT);
				int i = 0;
				do{
					Mobcast Obj = mArrayListMobcast.get(i);
					if(!Obj.getmFileType().toString().equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)){
						if(Obj.getmId().equalsIgnoreCase(mCursor.getString(mIntMobcastId))){
							Obj.setmLikeCount(mCursor.getString(mIntMobcastLikeCount));
							Obj.setmViewCount(mCursor.getString(mIntMobcastViewCount));
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
