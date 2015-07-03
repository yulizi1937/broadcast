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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
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
import com.application.ui.adapter.MobcastRecyclerAdapter;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemClickListener;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.ScrollUtils;
import com.application.utils.Utilities;
import com.mobcast.R;

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
       mActivityCommunicator =(IActivityCommunicator)mContext;
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
		
		checkDataInAdapter();

		setSwipeRefreshLayoutCustomisation();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
		setMaterialRippleView();
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mEmptyRefreshBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
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
				DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " DESC");
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

	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void addMobcastObjectListFromDBToBeans(Cursor mCursor, boolean isFromBroadCastReceiver){
		int mIntMobcastId = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
		int mIntMobcastTitle = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE);
		int mIntMobcastBy = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY);
		int mIntMobcastDesc = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC);
		int mIntMobcastDate = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE);
		int mIntMobcastTime = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME);
		int mIntMobcastLikeCount = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO);
		int mIntMobcastViewCount = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_READ_NO);
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
			
			if(!isFromBroadCastReceiver){
				mArrayListMobcast.add(Obj);	
			}else{
				mArrayListMobcast.add(0,Obj);
			}
		} while (mCursor.moveToNext());
		
	}

	private void setRecyclerAdapter() {
//		mAdapter = new MobcastRecyclerAdapter(getActivity(),
//				getDummyMobcastData(), headerView);
		
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
					switch (view.getId()) {
					case R.id.itemRecyclerMobcastVideoRootLayout:
						Intent mIntentVideo = new Intent(mParentActivity,
								VideoDetailActivity.class);
						startActivity(mIntentVideo);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastImageRootLayout:
						Intent mIntentImage = new Intent(mParentActivity,
								ImageDetailActivity.class);
						mIntentImage.putExtra(
								AppConstants.INTENTCONSTANTS.CATEGORY,
								AppConstants.INTENTCONSTANTS.MOBCAST);
						startActivity(mIntentImage);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastFeedbackRootLayout:
						Intent mIntentFeedback = new Intent(mParentActivity,
								FeedbackActivity.class);
						startActivity(mIntentFeedback);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastAudioRootLayout:
						Intent mIntentAudio = new Intent(mParentActivity,
								AudioDetailActivity.class);
						mIntentAudio.putExtra(
								AppConstants.INTENTCONSTANTS.CATEGORY,
								AppConstants.INTENTCONSTANTS.MOBCAST);
						startActivity(mIntentAudio);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastTextRootLayout:
						Intent mIntentText = new Intent(mParentActivity,
								TextDetailActivity.class);
						mIntentText.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
								AppConstants.INTENTCONSTANTS.MOBCAST);
						startActivity(mIntentText);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastPdfRootLayout:
						Intent mIntentPdf = new Intent(mParentActivity,
								PdfDetailActivity.class);
						startActivity(mIntentPdf);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastXlsRootLayout:
						Intent mIntentXls = new Intent(mParentActivity,
								XlsDetailActivity.class);
						startActivity(mIntentXls);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastDocRootLayout:
						Intent mIntentDoc = new Intent(mParentActivity,
								DocDetailActivity.class);
						startActivity(mIntentDoc);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastPptRootLayout:
						Intent mIntentPpt = new Intent(mParentActivity,
								PptDetailActivity.class);
						startActivity(mIntentPpt);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					case R.id.itemRecyclerMobcastNewsRootLayout:
						Intent mIntentNews = new Intent(mParentActivity,
								NewsDetailActivity.class);
						startActivity(mIntentNews);
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					}

				}
			});
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
				mAdapter.addMobcastObjList(mArrayListMobcast);
				mSwipeRefreshLayout.setRefreshing(false);
			}
			
			if(mCursor!=null)
				mCursor.close();
		}
	}
}
