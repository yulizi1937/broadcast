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

import com.application.beans.Recruitment;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.mobcast.R;

public class RecruitmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = RecruitmentRecyclerAdapter.class
			.getSimpleName();

	private static final int VIEW_TYPE_RECRUITMENT = 0;
	private static final int VIEW_TYPE_FOOTER = 1;

	private LayoutInflater mInflater;
	private Context mContext;
	private int whichTheme = 0;
	private ArrayList<Recruitment> mArrayListRecruitment;
	public OnItemClickListener mItemClickListener;
	public OnItemLongClickListener mItemLongClickListener;

	public RecruitmentRecyclerAdapter(Context mContext,
			ArrayList<Recruitment> mArrayListRecruitment) {
		this.mContext = mContext;
		this.mArrayListRecruitment = mArrayListRecruitment;
		mInflater = LayoutInflater.from(mContext);
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
	}

	public void addRecruitmentObjList(ArrayList<Recruitment> mListRecruitment) {
		mArrayListRecruitment = mListRecruitment;
		notifyDataSetChanged();
	}

	public void updateEventObj(int position, ArrayList<Recruitment> mListRecruitment) {
		mArrayListRecruitment = mListRecruitment;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
		default:
			return getItemTypeFromObject(position);
		}
	}

	public int getItemTypeFromObject(int position) {
		if (mArrayListRecruitment.get(position).getmFileType()
				.equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)) {
			return VIEW_TYPE_FOOTER;
		} else {
			return VIEW_TYPE_RECRUITMENT;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_FOOTER:
			return new FooterViewHolder(mInflater.inflate(
					R.layout.layout_footerview, parent, false));
		default:
			return new RecruitmentViewHolder(mInflater.inflate(
					R.layout.item_recycler_recruitment, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof RecruitmentViewHolder){
			processRecruitmentViewHolder(viewHolder, position);
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListRecruitment.size();
	}

	public class RecruitmentViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {
		AppCompatTextView mDateTv;
		AppCompatTextView mMonthTv;
		AppCompatTextView mTitleTv;
		AppCompatTextView mDescTv;
		
		FrameLayout mRootLayout;
		FrameLayout mIndicatorLayout;
		View mReadStripView;
		View mTimeLineView;

		public RecruitmentViewHolder(View view) {
			super(view);
			mDateTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerRecruitmentIndicatorDateTv);
			mMonthTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerRecruitmentIndicatorMonthTv);
			mTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerRecruitmentTitleTv);
			mDescTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerRecruitmentSummaryTv);
			
			mRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerRecruitmentRootLayout);
			mIndicatorLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerRecruitmentIndicatorImageView);
			
			mReadStripView = (View)view.findViewById(R.id.itemRecyclerRecruitmentReadView);
			mTimeLineView = (View)view.findViewById(R.id.itemRecyclerRecruitmentLineView);

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
	
	private void processRecruitmentViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Recruitment mObj = mArrayListRecruitment.get(position);
			((RecruitmentViewHolder)viewHolder).mDateTv.setText(mObj.getmDate());
			((RecruitmentViewHolder)viewHolder).mMonthTv.setText(mObj.getmMonth());
			((RecruitmentViewHolder)viewHolder).mTitleTv.setText(mObj.getmJobTitle());
			((RecruitmentViewHolder)viewHolder).mDescTv.setText(mObj.getmJobDesc());
			
			ThemeUtils.applyThemeItemRecruitment(mContext, whichTheme,
					((RecruitmentViewHolder)viewHolder).mReadStripView, ((RecruitmentViewHolder)viewHolder).mTimeLineView, ((RecruitmentViewHolder)viewHolder).mIndicatorLayout, ((RecruitmentViewHolder)viewHolder).mRootLayout,
					((RecruitmentViewHolder)viewHolder).mTitleTv, ((RecruitmentViewHolder)viewHolder).mDateTv, ((RecruitmentViewHolder)viewHolder).mMonthTv, mObj.isRead());
//			if(!mObj.isRead()){
//				((RecruitmentViewHolder)viewHolder).mTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
//				((RecruitmentViewHolder)viewHolder).mReadStripView.setVisibility(View.VISIBLE);
//				((RecruitmentViewHolder)viewHolder).mRootLayout.setBackgroundResource(R.color.unread_background);
//				((RecruitmentViewHolder)viewHolder).mIndicatorLayout.setBackgroundResource(R.drawable.shape_round_orange_fill);
//			}else{
//				((RecruitmentViewHolder)viewHolder).mTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
//				((RecruitmentViewHolder)viewHolder).mReadStripView.setVisibility(View.GONE);
//				((RecruitmentViewHolder)viewHolder).mRootLayout.setBackgroundResource(R.color.white);
//				((RecruitmentViewHolder)viewHolder).mIndicatorLayout.setBackgroundResource(R.drawable.shape_round_orange_border);
//			}
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
