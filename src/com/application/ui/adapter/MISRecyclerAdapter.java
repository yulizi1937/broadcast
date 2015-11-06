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
//https://code.google.com/p/android/issues/detail?id=28057

package com.application.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.MIS;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MISRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = MISRecyclerAdapter.class.getSimpleName(); 
			
    private static final int VIEW_TYPE_HEADER     = 0;
    private static final int VIEW_TYPE_TEXT       = 1;

    private LayoutInflater mInflater;
    private ArrayList<MIS> mArrayListMIS;
    private View mHeaderView;
    public OnItemClickListener mItemClickListener;
    private Context mContext;
    private ImageLoader mImageLoader;

    public MISRecyclerAdapter(Context context, ArrayList<MIS> mArrayListMIS, View headerView) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mArrayListMIS = mArrayListMIS;
        mHeaderView = headerView;
        mImageLoader = ApplicationLoader.getUILImageLoader();
    }
    
    
	@Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mArrayListMIS.size();
        } else {
            return mArrayListMIS.size() + 1;
        }
    }
    
    public void addMISObjList(ArrayList<MIS> mListMIS){
    	mArrayListMIS = mListMIS;
    	notifyDataSetChanged();
    }
    
    public void updateMISObj(int position, ArrayList<MIS> mListMIS){
    	mArrayListMIS = mListMIS;
    	notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
    	switch(position){
    	case 0:
    		return VIEW_TYPE_HEADER;
    	default:
    		return getItemTypeFromObject(position-1);
    	}
//        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }
    
    public int getItemTypeFromObject(int position){
    		return VIEW_TYPE_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	switch(viewType){
    	case VIEW_TYPE_TEXT:
    		return  new TextViewHolder(mInflater.inflate(R.layout.item_recycler_mis_text, parent, false));
    	default:
    		return new HeaderViewHolder(mHeaderView);
    	}
    }

	@Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		position-=1;
		if (viewHolder instanceof TextViewHolder) {
			processTextViewHolder(viewHolder, position);
		}
    }
    
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    
    public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMISTextRootLayout;
    	
    	View mMISTextReadView;
        ImageView mMISTextIndicatorIv;
        
        AppCompatTextView mMISTextTitleTv;
        AppCompatTextView mMISTextSummaryTv;
        
        public TextViewHolder(View view) {
            super(view);
            mMISTextRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMISTextRootLayout);
            
            mMISTextReadView = (View)view.findViewById(R.id.itemRecyclerMISTextReadView);
            
            mMISTextIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMISTextIndicatorImageView);
            
            mMISTextTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMISTextTitleTv);
            mMISTextSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMISTextSummaryTv);
            
            mMISTextReadView.setVisibility(View.INVISIBLE);
            
            mMISTextRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    
	public class FooterViewHolder extends RecyclerView.ViewHolder {
		ProgressWheel mFooterProgressWheel;

		public FooterViewHolder(View view) {
			super(view);
			mFooterProgressWheel = (ProgressWheel)view.findViewById(R.id.itemFooterProgressWheel);
		}
	}
    


	/* (non-Javadoc)
	 * @see com.application.ui.view.FlexibleDividerDecoration.VisibilityProvider#shouldHideDivider(int, android.support.v7.widget.RecyclerView)
	 */
	@Override
	public boolean shouldHideDivider(int position, RecyclerView parent) {
		// TODO Auto-generated method stub
		if (position == 0) {
	            return true;
	        }
	        return false;
	}
	
	
	@SuppressWarnings("deprecation")
	private void processTextViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			MIS mObj = mArrayListMIS.get(position);
			((TextViewHolder)viewHolder).mMISTextTitleTv.setText(mObj.getmTitle());
			((TextViewHolder)viewHolder).mMISTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mis_text_normal));
			((TextViewHolder)viewHolder).mMISTextIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
			((TextViewHolder)viewHolder).mMISTextTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
			((TextViewHolder)viewHolder).mMISTextReadView.setVisibility(View.GONE);
			((TextViewHolder)viewHolder).mMISTextRootLayout.setBackgroundResource(R.color.white);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
