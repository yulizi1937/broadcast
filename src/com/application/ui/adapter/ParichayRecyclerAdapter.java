/**
 * 
 */
package com.application.ui.adapter;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.Parichay;
import com.application.ui.adapter.MobcastRecyclerAdapter.HeaderViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.TextViewHolder;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.mobcast.R;

public class ParichayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = ParichayRecyclerAdapter.class
			.getSimpleName();

	private static final int VIEW_TYPE_PARICHAY = 0;
	private static final int VIEW_TYPE_FOOTER = 1;
	private static final int VIEW_TYPE_HEADER = 2;

	private LayoutInflater mInflater;
	private Context mContext;
	private int whichTheme = 0;
	private View mHeaderView;
	private boolean isGrid = false;
	private ArrayList<Parichay> mArrayListParichay;
	public OnItemClickListener mItemClickListener;
	public OnItemLongClickListener mItemLongClickListener;

	public ParichayRecyclerAdapter(Context mContext,
			ArrayList<Parichay> mArrayListParichay,View headerView, boolean isGrid) {
		this.mContext = mContext;
		this.mArrayListParichay = mArrayListParichay;
		mHeaderView = headerView;
		this.isGrid = isGrid;
		mInflater = LayoutInflater.from(mContext);
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
	}

	public void addParichayObjList(ArrayList<Parichay> mListParichay) {
		mArrayListParichay = mListParichay;
		notifyDataSetChanged();
	}

	public void updateEventObj(int position, ArrayList<Parichay> mListParichay) {
		mArrayListParichay = mListParichay;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if(!isGrid){
    		switch(position){
        	case 0:
        		return VIEW_TYPE_HEADER;
        	default:
        		return getItemTypeFromObject(position-1);
        	}
    	}else{
    		switch(position){
        	case 0:
        		return VIEW_TYPE_HEADER;
        	case 1:
        		return VIEW_TYPE_HEADER;
        	default:
        		return getItemTypeFromObject(position-2);
        	}
    	}
	}

	public int getItemTypeFromObject(int position) {
		if (mArrayListParichay.get(position).getmFileType()
				.equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)) {
			return VIEW_TYPE_FOOTER;
		} else {
			return VIEW_TYPE_PARICHAY;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_FOOTER:
			return new FooterViewHolder(mInflater.inflate(
					R.layout.layout_footerview, parent, false));
		case VIEW_TYPE_PARICHAY:
			return new ParichayViewHolder(mInflater.inflate(
					R.layout.item_recycler_parichay, parent, false));
		default:
			return new HeaderViewHolder(mHeaderView);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof ParichayViewHolder){
			processParichayViewHolder(viewHolder, position);
		}
	}

	@Override
	public int getItemCount() {
		if (mHeaderView == null) {
            return mArrayListParichay.size();
        } else {
        	if(!isGrid){
        		return mArrayListParichay.size() + 1;	
        	}else{
        		return mArrayListParichay.size() + 2;
        	}
            
        }
	}

	public class ParichayViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {
		AppCompatTextView mDateTv;
		AppCompatTextView mMonthTv;
		AppCompatTextView mTitleTv;
		AppCompatTextView mDescTv;
		AppCompatTextView mByTv;
		AppCompatTextView mLikeCountTv;
		AppCompatTextView mViewCountTv;
		
		FrameLayout mRootLayout;
		FrameLayout mIndicatorLayout;
		View mReadStripView;
		View mTimeLineView;

		public ParichayViewHolder(View view) {
			super(view);
			mDateTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayIndicatorDateTv);
			mMonthTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayIndicatorMonthTv);
			mTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayTitleTv);
			mDescTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichaySummaryTv);
			mByTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayByTv);
			mLikeCountTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayLikeCountTv);
			mViewCountTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayViewCountTv);
			
			mRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerParichayRootLayout);
			mIndicatorLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerParichayIndicatorImageView);
			
			mReadStripView = (View)view.findViewById(R.id.itemRecyclerParichayReadView);
			mTimeLineView = (View)view.findViewById(R.id.itemRecyclerParichayLineView);

			mRootLayout.setOnClickListener(this);
			mRootLayout.setOnLongClickListener(this);
		}

		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
		
		/* (non-Javadoc)
		 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
		 */
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if (mItemLongClickListener != null) {
				mItemLongClickListener.onItemLongClick(v, getLayoutPosition());
			}
			return true;
		}
	}
	
	 static class HeaderViewHolder extends RecyclerView.ViewHolder {
	        public HeaderViewHolder(View view) {
	            super(view);
	        }
	    }
	
	public class FooterViewHolder extends RecyclerView.ViewHolder {
		ProgressWheel mFooterProgressWheel;

		public FooterViewHolder(View view) {
			super(view);
			mFooterProgressWheel = (ProgressWheel)view.findViewById(R.id.itemFooterProgressWheel);
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	public interface OnItemLongClickListener {
		public void onItemLongClick(View view, int position);
	}

	public void setOnItemLongClickListener(
			final OnItemLongClickListener mItemLongClickListener) {
		this.mItemLongClickListener = mItemLongClickListener;
	}
	
	private void processParichayViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Parichay mObj = mArrayListParichay.get(position-1);
			((ParichayViewHolder)viewHolder).mDateTv.setText(mObj.getmDate());
			((ParichayViewHolder)viewHolder).mMonthTv.setText(mObj.getmMonth());
			((ParichayViewHolder)viewHolder).mTitleTv.setText(mObj.getmJobPosition());
			((ParichayViewHolder)viewHolder).mDescTv.setText(mObj.getmJobDesc());
			((ParichayViewHolder)viewHolder).mByTv.setText(mObj.getmJobUnit());
			((ParichayViewHolder)viewHolder).mLikeCountTv.setText(mObj.getmLikeCount());
			((ParichayViewHolder)viewHolder).mViewCountTv.setText(mObj.getmReadCount());
			

			if(!mObj.isLike()){
				((ParichayViewHolder)viewHolder).mLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((ParichayViewHolder)viewHolder).mLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((ParichayViewHolder)viewHolder).mLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((ParichayViewHolder)viewHolder).mLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			ThemeUtils.applyThemeItemParichay(mContext, whichTheme,
					((ParichayViewHolder)viewHolder).mReadStripView, ((ParichayViewHolder)viewHolder).mTimeLineView, ((ParichayViewHolder)viewHolder).mIndicatorLayout, ((ParichayViewHolder)viewHolder).mRootLayout,
					((ParichayViewHolder)viewHolder).mTitleTv, ((ParichayViewHolder)viewHolder).mDateTv, ((ParichayViewHolder)viewHolder).mMonthTv, ((ParichayViewHolder)viewHolder).mByTv,mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
}
