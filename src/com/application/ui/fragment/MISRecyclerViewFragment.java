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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.MIS;
import com.application.ui.activity.WebViewActivity;
import com.application.ui.adapter.MISRecyclerAdapter;
import com.application.ui.adapter.MISRecyclerAdapter.OnItemClickListener;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.ObservableScrollViewCallbacks;
import com.application.utils.ScrollUtils;
import com.application.utils.Utilities;
import com.mobcast.R;

public class MISRecyclerViewFragment extends BaseFragment {
	private static final String TAG = MISRecyclerViewFragment.class.getSimpleName();
    public  static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    
	
	private Activity mParentActivity;
    
	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatButton mEmptyRefreshBtn;
	
	private View headerView;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
    private ObservableRecyclerView mRecyclerView;
    private MISRecyclerAdapter mAdapter;
    
    private Context mContext;
	
	private LinearLayoutManager mLinearLayoutManager;
	
	private ArrayList<MIS> mArrayListMIS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mParentActivity = getActivity();
        mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        
        headerView = LayoutInflater.from(mParentActivity).inflate(R.layout.padding, null);
        
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        
        mEmptyFrameLayout = (FrameLayout) view.findViewById(R.id.fragmentChatEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) view.findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) view.findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton) view.findViewById(R.id.layoutEmptyRefreshBtn);
		
		mLinearLayoutManager = new LinearLayoutManager(mParentActivity);
		mRecyclerView
		.setLayoutManager(mLinearLayoutManager);
		
		setEmptyData();
		mArrayListMIS = getDummyMISData();
		setRecyclerAdapter();
		setMaterialRippleView();
        return view;
    }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
	}
	
	private void setUiListener(){
		setRecyclerAdapterListener();
//		setMaterialRippleView();
	}
	
	private void setEmptyData(){
		mEmptyTitleTextView.setText(getResources().getString(R.string.emptyChatTitle));
		mEmptyMessageTextView.setText(getResources().getString(R.string.emptyChatMessage));
		mSwipeRefreshLayout.setProgressViewOffset(true, 80, 140);
	}
	
	private void setMaterialRippleView(){
		try{
			setMaterialRippleOnView(mEmptyRefreshBtn);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setRecyclerAdapter(){
        if(mArrayListMIS!=null && mArrayListMIS.size() > 0){
        	mEmptyFrameLayout.setVisibility(View.GONE);
        	mRecyclerView.setVisibility(View.VISIBLE);
        	
        	mRecyclerView.setHasFixedSize(false);
            mAdapter= new MISRecyclerAdapter(getActivity(), mArrayListMIS, headerView);
            if(AndroidUtilities.isAboveIceCreamSandWich()){
            	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
                ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
                mRecyclerView.setAdapter(mScaleInAdapter);
            }else{
            	mRecyclerView.setAdapter(mAdapter);
            }
            mRecyclerView.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(getActivity())
                            .color(Utilities.getDividerColor())
                            .sizeResId(R.dimen.fragment_recyclerview_divider)
                            .visibilityProvider(mAdapter)
                            .build());

            if (mParentActivity instanceof ObservableScrollViewCallbacks) {
                // Scroll to the specified offset after layout
                Bundle args = getArguments();
                if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                    final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                    ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.scrollVerticallyToPosition(initialPosition);
                        }
                    });
                }
                mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) mParentActivity);
            }
        }else{
        	mEmptyFrameLayout.setVisibility(View.VISIBLE);
        	mRecyclerView.setVisibility(View.GONE);
        }
	}
	
	private void setRecyclerAdapterListener(){
		if(mAdapter!=null){
			mAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					position-=1;
					switch(view.getId()){
					case R.id.itemRecyclerMISTextRootLayout:
						String mLink = mArrayListMIS.get(position).getmLink();
						if(mLink.contains("http")){
							Intent mIntentWebView = new Intent(getActivity(), WebViewActivity.class);
							mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE, AppConstants.INTENTCONSTANTS.OPEN);
							mIntentWebView.putExtra(AppConstants.INTENTCONSTANTS.LINK, mLink);
							startActivity(mIntentWebView);
						}else{
							if(!Utilities.openApp(getActivity(), mLink)){
								Intent mBrowserIntent = new Intent(Intent.ACTION_VIEW);
								mBrowserIntent.setData(Uri.parse("http://www.jajo.in/myovp11.apk"));
								startActivity(mBrowserIntent);
							}
						}
						AndroidUtilities.enterWindowAnimation(mParentActivity);
						break;
					}
				}
			});
		}
	}
}
