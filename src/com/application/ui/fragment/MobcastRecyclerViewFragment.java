/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.application.ui.fragment;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.Mobcast;
import com.application.beans.MobcastFileInfo;
import com.application.sqlite.DBConstant;
import com.application.ui.activity.AudioDetailActivity;
import com.application.ui.activity.DocDetailActivity;
import com.application.ui.activity.FeedbackActivity;
import com.application.ui.activity.ImageDetailActivity;
import com.application.ui.activity.MotherActivity;
import com.application.ui.activity.NewsDetailActivity;
import com.application.ui.activity.PdfDetailActivity;
import com.application.ui.activity.PptDetailActivity;
import com.application.ui.activity.TextDetailActivity;
import com.application.ui.activity.VideoDetailActivity;
import com.application.ui.activity.XlsDetailActivity;
import com.application.ui.activity.YouTubeLiveStreamActivity;
import com.application.ui.adapter.MobcastRecyclerAdapter;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemClickListener;
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
import com.application.utils.Utilities;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
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

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private AppCompatButton mEmptyRefreshBtn;

	private ObservableRecyclerView mRecyclerView;
	private MobcastRecyclerAdapter mAdapter;
	
	private ArrayList<Mobcast> mArrayListMobcast;
	
	private Context mContext;
	
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

		mEmptyFrameLayout = (FrameLayout) view
				.findViewById(R.id.fragmentMobcastEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) view
				.findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) view
				.findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton) view
				.findViewById(R.id.layoutEmptyRefreshBtn);

		mRecyclerView
		.setLayoutManager(new LinearLayoutManager(mParentActivity));
		
		ApplicationLoader.getPreferences().setViewIdMobcast("-1");
		
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
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setMaterialRippleView();
		setClickListener();
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
				refreshFeedFromApi(true);
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
				}
			});
		}
	}

	private void checkDataInAdapter() {
		Cursor mCursor = getActivity().getContentResolver().query(
				DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null,
				DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED + " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addMobcastObjectListFromDBToBeans(mCursor, false);
			if (mArrayListMobcast.size() > 0) {
				setRecyclerAdapter();
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

	@SuppressLint("NewApi") 
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				refreshFeedFromApi(true);
			}
		});
	}
	
	@SuppressLint("NewApi") 
	private void refreshFeedFromApi(boolean isRefreshFeed){
		if(AndroidUtilities.isAboveIceCreamSandWich()){
			new AsyncRefreshTask(isRefreshFeed).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
		}else{
			new AsyncRefreshTask(isRefreshFeed).execute();
		}
	}
	
	private void addMobcastObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver){
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
		
		mArrayListMobcast = new ArrayList<Mobcast>();
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

			if(Utilities.getMediaType(mCursor.getString(mIntMobcastType)) != AppConstants.TYPE.FEEDBACK){
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
					feedbackObj.setmPages(mCursorFeedbackInfo.getCount() + " " + getResources().getString(R.string.item_recycler_mobcast_feedback_question));
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
		
	}

	private void setRecyclerAdapter() {
//		mArrayListMobcast = getDummyMobcastData();
//		mAdapter = new MobcastRecyclerAdapter(getActivity(),
//				mArrayListMobcast, headerView);
		
		mAdapter = new MobcastRecyclerAdapter(getActivity(),
				mArrayListMobcast, headerView);
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
						Intent mIntentFeedback = new Intent(mParentActivity,FeedbackActivity.class);
						mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,AppConstants.INTENTCONSTANTS.MOBCAST);
						mIntentFeedback.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListMobcast.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentFeedback);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
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
	}
	
	private void saveViewPosition(int position){
		ApplicationLoader.getPreferences().setViewIdMobcast(String.valueOf(position));
	}
	
	private void checkReadFromDBAndUpdateToObj(){
		int position = Integer.parseInt(ApplicationLoader.getPreferences().getViewIdMobcast());
//		position-=1;
		if (position >= 0 && position < mArrayListMobcast.size()) {
			if(mArrayListMobcast!=null && mArrayListMobcast.size() > 0){
				Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO}, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mArrayListMobcast.get(position).getmId()}, null);
				
				if(mCursor!=null && mCursor.getCount() >0){
					mCursor.moveToFirst();
					boolean isToNotify = false;
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)))){
						mArrayListMobcast.get(position).setRead(true);
						isToNotify = true;
					}
					if(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)))){
						mArrayListMobcast.get(position).setLike(true);
						mArrayListMobcast.get(position).setmLikeCount(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
						isToNotify = true;
					}
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
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			Cursor mCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " DESC");
			if(mCursor!=null && mCursor.getCount() > 0){
				mSwipeRefreshLayout.setEnabled(true);
				mSwipeRefreshLayout.setRefreshing(true);
				mCursor.moveToFirst();
				addMobcastObjectListFromDBToBeans(mCursor, false);
				AndroidUtilities.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(mAdapter!=null){
							mAdapter.addMobcastObjList(mArrayListMobcast);
							mRecyclerView.getAdapter().notifyDataSetChanged();
						}else{
							setRecyclerAdapter();
							setRecyclerAdapterListener();
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
	}
	
	/*
	 * AsyncTask To Refresh
	 */
	
	public String apiRefreshFeedMobcast(){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedMobcast();
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
	
	public void parseDataFromApi(String mResponseFromApi){
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
					
					if(isInsertedInDB){
						ApplicationLoader.getPreferences().setLastIdMobcast(mMobcastId);
					}
					final int mIntType = Utilities.getMediaType(mType);
					
					if(mIntType != AppConstants.TYPE.FEEDBACK){
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
							
							/*if(!TextUtils.isEmpty(mThumbnailLink)){
								ConnectionQuality mConnectionQuality = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
								if(mConnectionQuality.compareTo(ConnectionQuality.EXCELLENT) == 1){
									Utilities.downloadQueue.postRunnable(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											Utilities.downloadFile(mIntType, true,false, mThumbnailLink, Utilities.getFileName(mThumbnailName));//thumbnail, isEncrypt		
										}
									});
								}
							}*/
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
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption1));
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption2));
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption3));
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption4));
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption5));
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption6));
								valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption7));
								
								Uri isInsertFeedbackInfoUri = getActivity().getContentResolver().insert(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, valuesFeedbackInfo);
								Utilities.checkWhetherInsertedOrNot(TAG,isInsertFeedbackInfoUri);
						}
					}
				}
				
				passDataToFragment(0, "mobcast");
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

		public AsyncRefreshTask(boolean isRefreshFeed){
			this.isRefreshFeed = isRefreshFeed;
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
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = isRefreshFeed ? apiRefreshFeedMobcast() : apiRefreshFeedAction();
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
			
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			
			if(mSwipeRefreshLayout.isRefreshing()){
				mSwipeRefreshLayout.setRefreshing(false);
			}
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				/*Utilities.showCrouton(getActivity(), mCroutonViewGroup,
						mErrorMessage, Style.ALERT);*/
			}
		}
	}
}
