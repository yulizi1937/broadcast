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
import android.widget.ImageView;

import com.application.beans.Event;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.mobcast.R;

public class EventRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider {
	private static final String TAG = EventRecyclerAdapter.class
			.getSimpleName();
	
	private static final int VIEW_TYPE_EVENT      = 0;
    private static final int VIEW_TYPE_FOOTER     = 1;
    
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<Event> mArrayListEvent;
	public OnItemClickListenerE mItemClickListener;
	public OnItemLongClickListenerE mItemLongClickListener;
	private int mWhichTheme = 0;

	public EventRecyclerAdapter(Context mContext,
			ArrayList<Event> mArrayListEvent) {
		this.mContext = mContext;
		this.mArrayListEvent = mArrayListEvent;
		mInflater = LayoutInflater.from(mContext);
		mWhichTheme = ApplicationLoader.getPreferences().getAppTheme();
	}
	
	 public void addEventObjList(ArrayList<Event> mListEvent){
	    	mArrayListEvent = mListEvent;
	    	notifyDataSetChanged();
	    }
	    
	    public void updateEventObj(int position, ArrayList<Event> mListEvent){
	    	mArrayListEvent = mListEvent;
	    	notifyDataSetChanged();
	    }
	
	 @Override
	    public int getItemViewType(int position) {
	    	switch(position){
	    	default:
	    		return getItemTypeFromObject(position);
	    	}
	    }
	    
	    public int getItemTypeFromObject(int position){
	    	if(mArrayListEvent.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)){
	    		return VIEW_TYPE_FOOTER;
	    	}else{
	    		return VIEW_TYPE_EVENT;
	    	}
	    }

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_FOOTER:
			return new FooterViewHolder(mInflater.inflate(
					R.layout.layout_footerview, parent, false));
		default:
			return new EventViewHolder(mInflater.inflate(
					R.layout.item_recycler_event, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if (viewHolder instanceof EventViewHolder) {
			processEventViewHolder(viewHolder, position);
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListEvent.size();
	}

	public class EventViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {
		FrameLayout mEventRootLayout;

		View mReadStripView;
		View mTimeLineView;

		AppCompatTextView mEventTitleTv;
		AppCompatTextView mEventViewCountTv;
		AppCompatTextView mEventLikeCountTv;
		AppCompatTextView mEventByTv;
		AppCompatTextView mEventDaysHoursLeftTextTv;
		AppCompatTextView mEventAttendanceTextTv;
		AppCompatTextView mEventIsGoingTextTv;
		
		ImageView mEventTextIndicatorIv;
		ImageView mEventIsGoingTextIv;

		public EventViewHolder(View view) {
			super(view);
			mEventTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerEventTitleTv);
			mEventByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerEventByTv);
			mEventViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerEventViewCountTv);
			mEventLikeCountTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerEventLikeCountTv);
			mEventDaysHoursLeftTextTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerEventDetailDaysLeftTv);
			mEventAttendanceTextTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerEventDetailAttendanceTv);
			mEventIsGoingTextTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerEventDetailIsGoingTv);
			
			mEventTextIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerEventIndicatorImageView);
			mEventIsGoingTextIv = (ImageView)view.findViewById(R.id.itemRecyclerEventDetailIsGoingIv);

			mEventRootLayout = (FrameLayout) view.findViewById(R.id.itemRecyclerEventRootLayout);

			mReadStripView = (View) view.findViewById(R.id.itemRecyclerEventReadView);
			mTimeLineView = (View) view.findViewById(R.id.itemRecyclerEventLineView);

			mEventRootLayout.setOnClickListener(this);
			mEventRootLayout.setOnLongClickListener(this);
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

	public interface OnItemClickListenerE {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListenerE mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	public interface OnItemLongClickListenerE {
		public void onItemLongClick(View view, int position);
	}

	public void setOnItemLongClickListener(
			final OnItemLongClickListenerE mItemLongClickListener) {
		this.mItemLongClickListener = mItemLongClickListener;
	}
	
	@SuppressWarnings("deprecation")
	private void processEventViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Event mObj = mArrayListEvent.get(position);
			((EventViewHolder)viewHolder).mEventTitleTv.setText(mObj.getmTitle());
			((EventViewHolder)viewHolder).mEventByTv.setText(mObj.getmBy());
			((EventViewHolder)viewHolder).mEventViewCountTv.setText(mObj.getmViewCount());
			((EventViewHolder)viewHolder).mEventLikeCountTv.setText(mObj.getmLikeCount());
			((EventViewHolder)viewHolder).mEventAttendanceTextTv.setText(mObj.getmGoingCount() + " " + mContext.getResources().getString(R.string.going));
			((EventViewHolder)viewHolder).mEventDaysHoursLeftTextTv.setText(mObj.getmDaysLeft());
			if(!mObj.isRead()){
				((EventViewHolder)viewHolder).mEventTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_event_focused));
			}else{
				((EventViewHolder)viewHolder).mEventTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_event_normal));
			}
			
			if(!mObj.isLike()){
				((EventViewHolder)viewHolder).mEventLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((EventViewHolder)viewHolder).mEventLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((EventViewHolder)viewHolder).mEventLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((EventViewHolder)viewHolder).mEventLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(mObj.getIsGoingToAttend().equalsIgnoreCase("0")){
				((EventViewHolder)viewHolder).mEventIsGoingTextTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((EventViewHolder)viewHolder).mEventIsGoingTextTv.setText(mContext.getResources().getString(R.string.notgoing));
				((EventViewHolder)viewHolder).mEventIsGoingTextIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_event_declined));
			}else if(mObj.getIsGoingToAttend().equalsIgnoreCase("1")){
				((EventViewHolder)viewHolder).mEventIsGoingTextTv.setTextColor(mContext.getResources().getColor(R.color.green));
				((EventViewHolder)viewHolder).mEventIsGoingTextTv.setText(mContext.getResources().getString(R.string.accepted));
				((EventViewHolder)viewHolder).mEventIsGoingTextIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_event_accepted));				
			}else{
				((EventViewHolder)viewHolder).mEventIsGoingTextTv.setTextColor(mContext.getResources().getColor(R.color.drawer_item_title));
				((EventViewHolder)viewHolder).mEventIsGoingTextTv.setText(mContext.getResources().getString(R.string.maybe));
				((EventViewHolder)viewHolder).mEventIsGoingTextIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_feedback_question));
			}
			
			ThemeUtils.applyThemeItemEvent(mContext, mWhichTheme, ((EventViewHolder)viewHolder).mReadStripView, ((EventViewHolder)viewHolder).mTimeLineView, ((EventViewHolder)viewHolder).mEventRootLayout, ((EventViewHolder)viewHolder).mEventTitleTv, ((EventViewHolder)viewHolder).mEventByTv, ((EventViewHolder)viewHolder).mEventTextIndicatorIv, mObj.isRead());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.application.ui.view.FlexibleDividerDecoration.VisibilityProvider#
	 * shouldHideDivider(int, android.support.v7.widget.RecyclerView)
	 */
	@Override
	public boolean shouldHideDivider(int position, RecyclerView parent) {
		// TODO Auto-generated method stub
		return false;
	}
}
