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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.Training;
import com.application.beans.TrainingFileInfo;
import com.application.sqlite.DBConstant;
import com.application.ui.activity.AudioDetailActivity;
import com.application.ui.activity.DocDetailActivity;
import com.application.ui.activity.ImageDetailActivity;
import com.application.ui.activity.MotherActivity;
import com.application.ui.activity.PdfDetailActivity;
import com.application.ui.activity.PptDetailActivity;
import com.application.ui.activity.QuizActivity;
import com.application.ui.activity.TextDetailActivity;
import com.application.ui.activity.VideoDetailActivity;
import com.application.ui.activity.XlsDetailActivity;
import com.application.ui.activity.YouTubeLiveStreamActivity;
import com.application.ui.adapter.TrainingRecyclerAdapter;
import com.application.ui.adapter.TrainingRecyclerAdapter.OnItemClickListener;
import com.application.ui.adapter.TrainingRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.ScrollUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

public class TrainingRecyclerViewFragment extends BaseFragment implements IFragmentCommunicator{
	private static final String TAG = TrainingRecyclerViewFragment.class
			.getSimpleName();
	public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

	private IActivityCommunicator mActivityCommunicator;
	
	private Activity mParentActivity;

	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatButton mEmptyRefreshBtn;

	private View headerView;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ObservableRecyclerView mRecyclerView;
	private TrainingRecyclerAdapter mAdapter;
	
	private ArrayList<Training> mArrayListTraining;
	
	private Context mContext;
	
	private LinearLayoutManager mLinearLayoutManager;
	
    private boolean mLoadMore = false; 
    int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
	 @Override
    public void onAttach(Activity activity){
      super.onAttach(activity);
      mContext = getActivity();
      mActivityCommunicator =(IActivityCommunicator)mContext; //NIELSEN
      ((MotherActivity)mContext).mFragmentCommunicator = this;
    }
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_training, container,
				false);

		mParentActivity = getActivity();
		mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
		headerView = LayoutInflater.from(mParentActivity).inflate(
				R.layout.padding, null);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipeRefreshLayout);

		mEmptyFrameLayout = (FrameLayout) view
				.findViewById(R.id.fragmentTrainingEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) view
				.findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) view
				.findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton) view
				.findViewById(R.id.layoutEmptyRefreshBtn);
		
		mLinearLayoutManager = new LinearLayoutManager(mParentActivity);
		mRecyclerView
		.setLayoutManager(mLinearLayoutManager);
		
		ApplicationLoader.getPreferences().setViewIdTraining("-1");

		checkDataInAdapter();
//		setRecyclerAdapter();

		setSwipeRefreshLayoutCustomisation();
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		ApplicationLoader.trackScreenViewV3(ApplicationLoader.getApplication().getResources().getString(R.string.com_application_ui_fragment_TrainingRecyclerViewFragment));
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setMaterialRippleView();
		setClickListener();
	}

	private void setEmptyData() {
		mEmptyTitleTextView.setText(getResources().getString(
				R.string.emptyTrainingTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyTrainingMessage));
		mSwipeRefreshLayout.setColorSchemeColors(
				Color.parseColor(AppConstants.COLOR.MOBCAST_RED),
				Color.parseColor(AppConstants.COLOR.MOBCAST_YELLOW),
				Color.parseColor(AppConstants.COLOR.MOBCAST_PURPLE),
				Color.parseColor(AppConstants.COLOR.MOBCAST_GREEN),
				Color.parseColor(AppConstants.COLOR.MOBCAST_BLUE));
		mSwipeRefreshLayout.setProgressViewOffset(false, 75, 120);
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
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
	
	private void setSwipeRefreshLayoutCustomisation() {
		mSwipeRefreshLayout.setColorSchemeColors(
				Color.parseColor(AppConstants.COLOR.MOBCAST_RED),
				Color.parseColor(AppConstants.COLOR.MOBCAST_YELLOW),
				Color.parseColor(AppConstants.COLOR.MOBCAST_PURPLE),
				Color.parseColor(AppConstants.COLOR.MOBCAST_GREEN),
				Color.parseColor(AppConstants.COLOR.MOBCAST_BLUE));
		mSwipeRefreshLayout.setProgressViewOffset(false, 80, 140);
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
		checkExpiredTrainingAndDeleteFromDB();
		Cursor mCursor = getActivity().getContentResolver().query(
				DBConstant.Training_Columns.CONTENT_URI, null, null, null,
				DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED + " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addTrainingObjectListFromDBToBeans(mCursor, false,false);
			if (mArrayListTraining.size() > 0) {
				setRecyclerAdapter();
			}else {
				setEmptyData();
			}
		} else {
			setEmptyData();
		}

		if (mCursor != null) {
			mCursor.close();
		}
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
	
	private void checkExpiredTrainingAndDeleteFromDB(){
		long mCurrentTimeMillis = System.currentTimeMillis();
		getActivity().getContentResolver().delete(DBConstant.Training_Columns.CONTENT_URI, DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED + "<?", new String[]{String.valueOf(mCurrentTimeMillis)});
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
		
		Cursor mCursorFile = getActivity().getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, null, null, null);
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
			mArrayListTraining = new ArrayList<Training>();
		}else{
			if(mArrayListTraining==null){
				mArrayListTraining = new ArrayList<Training>();	
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
				Cursor mCursorFileInfo = getActivity().getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
				
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
				Cursor mCursorQuizInfo = getActivity().getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?", new String[]{mTrainingId}, DBConstant.Training_Quiz_Columns.COLUMN_ID + " ASC");
				
				if(mCursorQuizInfo!=null && mCursorQuizInfo.getCount() > 0){
					mCursorQuizInfo.moveToFirst();
					int mTotalPoints = 0;
					ArrayList<TrainingFileInfo> mTrainingQuizInfoList = new ArrayList<>();
					TrainingFileInfo quizObj = new TrainingFileInfo();
					quizObj.setmPages(mCursorQuizInfo.getCount() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));
					try{
						String []mTime = Utilities.convertTimeFromSecsTo(Long.parseLong(mCursorQuizInfo.getString(mCursorQuizInfo.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION)))).split(" ");
						quizObj.setmDuration(mTime[0]+" "+ mTime[1]);
						
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
				mArrayListTraining.add(Obj);	
			}else{
				mArrayListTraining.add(0,Obj);
			}
		} while (mCursor.moveToNext());
		
		Collections.sort(mArrayListTraining, new TrainingSort());
	}

	private void setRecyclerAdapter() {
//		mArrayListTraining = getDummyTrainingData();
		// setDummyDataWithHeader(mRecyclerView, headerView);
		mAdapter = new TrainingRecyclerAdapter(getActivity(),
				mArrayListTraining, headerView);
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
					position-=1;
					switch (view.getId()) {
					case R.id.itemRecyclerTrainingVideoRootLayout:
						Intent mIntentVideo = new Intent(mParentActivity,
								VideoDetailActivity.class);
						mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentVideo.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentVideo);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastLiveRootLayout:
						Intent mIntentLive = new Intent(mParentActivity,
								YouTubeLiveStreamActivity.class);
						mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentLive.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentLive);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingImageRootLayout:
						Intent mIntentImage = new Intent(mParentActivity,ImageDetailActivity.class);
						mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentImage.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentImage);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingQuizRootLayout:
						Intent mIntentFeedback = new Intent(mParentActivity,QuizActivity.class);
						mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentFeedback);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingAudioRootLayout:
						Intent mIntentAudio = new Intent(mParentActivity,AudioDetailActivity.class);
						mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentAudio.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentAudio);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingTextRootLayout:
						Intent mIntentText = new Intent(mParentActivity,TextDetailActivity.class);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentText);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingPdfRootLayout:
						Intent mIntentPdf = new Intent(mParentActivity,PdfDetailActivity.class);
						mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentPdf.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentPdf);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingXlsRootLayout:
						Intent mIntentXls = new Intent(mParentActivity,XlsDetailActivity.class);
						mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentXls.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentXls);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingDocRootLayout:
						Intent mIntentDoc = new Intent(mParentActivity,DocDetailActivity.class);
						mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentDoc.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
						startActivity(mIntentDoc);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerTrainingPptRootLayout:
						Intent mIntentPpt = new Intent(mParentActivity,PptDetailActivity.class);
						mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.TRAINING);
						mIntentPpt.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListTraining.get(position).getmId());
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
					showContextMenu(position, view);
				}
			});
		}
	}
	
	private void saveViewPosition(int position){
		ApplicationLoader.getPreferences().setViewIdTraining(String.valueOf(position));
	}
	
	private void checkReadFromDBAndUpdateToObj(){
		int position = Integer.parseInt(ApplicationLoader.getPreferences().getViewIdTraining());
//		position-=1;
		if (position >= 0 && position < mArrayListTraining.size()) {
			if(mArrayListTraining!=null && mArrayListTraining.size() > 0){
				Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_TRAINING_ID, DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO}, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mArrayListTraining.get(position).getmId()}, null);
				
				if(mCursor!=null && mCursor.getCount() >0){
					mCursor.moveToFirst();
					boolean isToNotify = false;
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ)))){
						mArrayListTraining.get(position).setRead(true);
						isToNotify = true;
					}
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE)))){
						mArrayListTraining.get(position).setLike(true);
						mArrayListTraining.get(position).setmLikeCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
						isToNotify = true;
					}
					
					mArrayListTraining.get(position).setmLikeCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
					mArrayListTraining.get(position).setmViewCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO)));
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
		// TODO Auto-generated method stub
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			Cursor mCursor = null;
			boolean isToAddOldData = false;
			if(mId == -786){
				mCursor = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "<?", new String[]{mArrayListTraining.get(mArrayListTraining.size()-1).getmId()}, DBConstant.Training_Columns.COLUMN_TRAINING_ID + " DESC");
				isToAddOldData = true;
			}else{
				mCursor = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, null, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + " DESC");
			}
			if(mCursor!=null && mCursor.getCount() > 0){
				mSwipeRefreshLayout.setEnabled(true);
				mSwipeRefreshLayout.setRefreshing(true);
				mCursor.moveToFirst();
				addTrainingObjectListFromDBToBeans(mCursor, false, isToAddOldData);
				AndroidUtilities.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(mAdapter!=null){
							mAdapter.addTrainingObjList(mArrayListTraining);
							mRecyclerView.getAdapter().notifyDataSetChanged();
						}else{
							setRecyclerAdapter();
							setUiListener();
						}
						mSwipeRefreshLayout.setRefreshing(false);
						
						mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.TRAINING);
						
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
	}
	
	/*
	 * ContextMenu
	 */
	private void showContextMenu(int mPosition, View mView){
		try{
			mPosition =  mPosition - 1;
			if(mPosition!= -1){
				int mType = Utilities.getMediaType(mArrayListTraining.get(mPosition).getmFileType());
				String mTitle = mArrayListTraining.get(mPosition).getmTitle();
				boolean isRead = mArrayListTraining.get(mPosition).isRead();
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
		mBottomSheet = new BottomSheet.Builder(getActivity()).icon(Utilities.getRoundedBitmapForContextMenu(mType)).title(mTitle).sheet(R.menu.context_menu_mobcast).build();
         final Menu menu = mBottomSheet.getMenu();
         
         SpannableString mSpannabledRead = new SpannableString(getResources().getString(R.string.context_menu_read));
         SpannableString mSpannabledUnRead = new SpannableString(getResources().getString(R.string.context_menu_unread));
         SpannableString mSpannabledDelete = new SpannableString(getResources().getString(R.string.context_menu_delete));
         SpannableString mSpannabledView = new SpannableString(getResources().getString(R.string.context_menu_view));
         
         mSpannabledRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledRead.length(), 0);
         mSpannabledUnRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledUnRead.length(), 0);
         mSpannabledDelete.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledDelete.length(), 0);
         mSpannabledView.setSpan(new ForegroundColorSpan(Color.GRAY), 0, mSpannabledView.length(), 0);
         
         menu.getItem(0).setTitle(mSpannabledRead);
         menu.getItem(1).setTitle(mSpannabledUnRead);
         menu.getItem(2).setTitle(mSpannabledDelete);
         menu.getItem(3).setTitle(mSpannabledView);
         
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
          * Delete
          */
         menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
         menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
		 String mTrainingId = mArrayListTraining.get(mPosition).getmId();
		 values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, "true");
		 getActivity().getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId});	
       	 mArrayListTraining.get(mPosition).setRead(true);
       	 mRecyclerView.getAdapter().notifyItemChanged(mPosition+1);
       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.TRAINING);
       	 UserReport.updateUserReportApi(mTrainingId, AppConstants.INTENTCONSTANTS.TRAINING, AppConstants.REPORT.READ, "");
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuMarkAsUnRead(int mPosition){
		try{
		 ContentValues values = new ContentValues();
	     values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, "false");
		 getActivity().getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mArrayListTraining.get(mPosition).getmId()});
		 mArrayListTraining.get(mPosition).setRead(false);
       	 mRecyclerView.getAdapter().notifyItemChanged(mPosition+1);
       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.TRAINING);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void contextMenuDelete(int mPosition){
		try{
		 getActivity().getContentResolver().delete(DBConstant.Training_Columns.CONTENT_URI, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mArrayListTraining.get(mPosition).getmId()});
       	 getActivity().getContentResolver().delete(DBConstant.Training_File_Columns.CONTENT_URI, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mArrayListTraining.get(mPosition).getmId()});
       	 mArrayListTraining.remove(mPosition);
       	 if(mArrayListTraining.size() == 0){
       		 checkDataInAdapter();
       	 }else{
       		mRecyclerView.getAdapter().notifyDataSetChanged();
       	 }
       	 mActivityCommunicator.passDataToActivity(0, AppConstants.INTENTCONSTANTS.TRAINING);			
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

	
	/*
	 * AsyncTask To Refresh
	 */
	
	public String apiRefreshFeedTraining(boolean sortByAsc, int limit){
		try {
			JSONObject jsonObj =null;
			 if(sortByAsc){
				jsonObj = JSONRequestBuilder.getPostFetchFeedTraining(sortByAsc,limit, mArrayListTraining != null ? mArrayListTraining.get(0).getmId() : String.valueOf("0"));
			 }else{
				 jsonObj= JSONRequestBuilder.getPostFetchFeedTraining(sortByAsc,limit, mArrayListTraining.get(mArrayListTraining.size()-2).getmId());
			 }
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_TRAINING, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_TRAINING, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	public String apiRefreshFeedAction(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.TRAINING);
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
	
	private void parseDataFromApi(String mResponseFromApi, boolean isToAddOldData) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.training);
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					String mTrainingId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingId);
					String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingType);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingTime);
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
					
					Cursor mIsExistCursor = getActivity().getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID},  DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						return;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					
					Uri isInsertUri = getActivity().getContentResolver().insert(DBConstant.Training_Columns.CONTENT_URI, values);
					boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
					
					/*if(isInsertedInDB){
						ApplicationLoader.getPreferences().setLastIdTraining(mTrainingId);
					}*/
					
					int mIntType = Utilities.getMediaType(mType);
					
					if(mIntType!= AppConstants.TYPE.QUIZ && mIntType!= AppConstants.TYPE.TEXT){
						JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingFileInfo);
						
						for (int j = 0; j < mJSONArrMobFileObj.length(); j++) {
							JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(j);
							ContentValues valuesFileInfo = new ContentValues();
							String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingThumbnail);
							String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileLink);
							String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileName);
							String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingisDefault);
							String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_ID, mTrainingId);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK, mFileLink);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileId));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileLang));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileSize));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileDuration));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFilePages));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, mIsDefault);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingLiveStream));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingLiveStreamYouTube));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingReadDuration));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME, mFileName);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK, mThumbnailLink);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH, mThumbnailName);
							
							
							getActivity().getContentResolver().insert(DBConstant.Training_File_Columns.CONTENT_URI, valuesFileInfo);
						}
					}else if(mIntType == AppConstants.TYPE.QUIZ){
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
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption1));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption2));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption3));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption4));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption5));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption6));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption7));
								
								Uri isInsertQuizInfoUri = getActivity().getContentResolver().insert(DBConstant.Training_Quiz_Columns.CONTENT_URI, valuesQuizInfo);
								Utilities.checkWhetherInsertedOrNot(TAG,isInsertQuizInfoUri);
						}
					}
				}
				
				if(isToAddOldData){
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.TRAINING);					
				}else{
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.TRAINING);	
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
			if (mArrayListTraining == null) {
				mProgressDialog = new MobcastProgressDialog(getActivity());
				mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
			
			if(!sortByAsc){
				Training Obj = new Training();
				Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
				mArrayListTraining.add(Obj);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = isRefreshFeed ? apiRefreshFeedTraining(sortByAsc,limit) : apiRefreshFeedAction();
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
			
			if(!sortByAsc){
				mLoadMore = false;
				mArrayListTraining.remove(mArrayListTraining.size()-1);
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
			
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi, !sortByAsc);
			}

			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			
			if(mSwipeRefreshLayout.isRefreshing()){
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}
	}
}
