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

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.ui.activity.AudioDetailActivity;
import com.application.ui.activity.DocDetailActivity;
import com.application.ui.activity.ImageDetailActivity;
import com.application.ui.activity.InteractiveDetailActivity;
import com.application.ui.activity.MotherActivity;
import com.application.ui.activity.PdfDetailActivity;
import com.application.ui.activity.PptDetailActivity;
import com.application.ui.activity.QuizActivity;
import com.application.ui.activity.TextDetailActivity;
import com.application.ui.activity.VideoDetailActivity;
import com.application.ui.activity.XlsDetailActivity;
import com.application.ui.adapter.TrainingRecyclerAdapter;
import com.application.ui.adapter.TrainingRecyclerAdapter.OnItemClickListener;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ObservableScrollView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.ScrollUtils;
import com.application.utils.Utilities;
import com.mobcast.R;

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
		View view = inflater.inflate(R.layout.fragment_training, container,
				false);

		mParentActivity = getActivity();
		mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);

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

		setRecyclerAdapter();
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setEmptyData();
		setUiListener();
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setMaterialRippleView();
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
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mEmptyRefreshBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void setRecyclerScrollListener() {
		mRecyclerView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				// TODO Auto-generated method stub
				super.onScrollStateChanged(recyclerView, newState);
				int topRowVerticalPosition =
	                    (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
	            mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				// TODO Auto-generated method stub
				super.onScrolled(recyclerView, dx, dy);
			}
		});
	}

	private void setRecyclerAdapter() {
		mRecyclerView
				.setLayoutManager(new LinearLayoutManager(mParentActivity));
		mRecyclerView.setHasFixedSize(false);
		headerView = LayoutInflater.from(mParentActivity).inflate(
				R.layout.padding, null);
		// setDummyDataWithHeader(mRecyclerView, headerView);
		mAdapter = new TrainingRecyclerAdapter(getActivity(),
				getDummyTrainingData(), headerView);
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
		mAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				switch (view.getId()) {
				case R.id.itemRecyclerTrainingVideoRootLayout:
					Intent mIntentVideo = new Intent(mParentActivity,
							VideoDetailActivity.class);
					startActivity(mIntentVideo);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingImageRootLayout:
					Intent mIntentImage = new Intent(mParentActivity,
							ImageDetailActivity.class);
					mIntentImage.putExtra(
							AppConstants.INTENTCONSTANTS.CATEGORY,
							AppConstants.INTENTCONSTANTS.TRAINING);
					startActivity(mIntentImage);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingQuizRootLayout:
					Intent mIntentFeedback = new Intent(mParentActivity,
							QuizActivity.class);
					startActivity(mIntentFeedback);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingAudioRootLayout:
					Intent mIntentAudio = new Intent(mParentActivity,
							AudioDetailActivity.class);
					mIntentAudio.putExtra(
							AppConstants.INTENTCONSTANTS.CATEGORY,
							AppConstants.INTENTCONSTANTS.TRAINING);
					startActivity(mIntentAudio);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingTextRootLayout:
					Intent mIntentText = new Intent(mParentActivity,
							TextDetailActivity.class);
					mIntentText.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
							AppConstants.INTENTCONSTANTS.TRAINING);
					startActivity(mIntentText);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingPdfRootLayout:
					Intent mIntentPdf = new Intent(mParentActivity,
							PdfDetailActivity.class);
					startActivity(mIntentPdf);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingXlsRootLayout:
					Intent mIntentXls = new Intent(mParentActivity,
							XlsDetailActivity.class);
					startActivity(mIntentXls);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingDocRootLayout:
					Intent mIntentDoc = new Intent(mParentActivity,
							DocDetailActivity.class);
					startActivity(mIntentDoc);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingPptRootLayout:
					Intent mIntentPpt = new Intent(mParentActivity,
							PptDetailActivity.class);
					startActivity(mIntentPpt);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				case R.id.itemRecyclerTrainingInteractiveRootLayout:
					Intent mIntentInteractive = new Intent(mParentActivity,
							InteractiveDetailActivity.class);
					startActivity(mIntentInteractive);
					AndroidUtilities.enterWindowAnimation(mParentActivity);
					break;
				}

			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.application.ui.fragment.IFragmentCommunicator#passDataToFragment(int, java.lang.String)
	 */
	@Override
	public void passDataToFragment(int mId, String mCategory) {
		// TODO Auto-generated method stub
		
	}
}
