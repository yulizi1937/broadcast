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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.ui.view.FlexibleDividerDecoration;
import com.application.beans.Event;
import com.mobcast.R;

public class EventRecyclerAdapter extends
		RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> implements
		FlexibleDividerDecoration.VisibilityProvider {
	@SuppressWarnings("unused")
	private static final String TAG = EventRecyclerAdapter.class
			.getSimpleName();
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Event> mArrayListEvent;
	OnItemClickListener mItemClickListener;

	public EventRecyclerAdapter(Context mContext,
			ArrayList<Event> mArrayListEvent) {
		this.mContext = mContext;
		this.mArrayListEvent = mArrayListEvent;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_recycler_event,
				parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
	}

	@Override
	public int getItemCount() {
		return mArrayListEvent.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		FrameLayout mEventRootLayout;

		View mReadStripView;

		AppCompatTextView mEventTitleTv;
		AppCompatTextView mEventViewCountTv;
		AppCompatTextView mEventByTv;
		AppCompatTextView mEventDaysHoursLeftTextTv;
		AppCompatTextView mEventAttendanceTextTv;
		AppCompatTextView mEventIsGoingTextTv;

		public ViewHolder(View view) {
			super(view);
			mEventTitleTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerEventTitleTv);
			mEventByTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerEventByTv);
			mEventViewCountTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerEventViewCountTv);
			mEventDaysHoursLeftTextTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerEventDetailDaysLeftTv);
			mEventAttendanceTextTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerEventDetailAttendanceTv);
			mEventIsGoingTextTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerEventDetailIsGoingTv);

			mEventRootLayout = (FrameLayout) view
					.findViewById(R.id.itemRecyclerEventRootLayout);

			mReadStripView = (View) view
					.findViewById(R.id.itemRecyclerReadView);

			mEventRootLayout.setOnClickListener(this);
		}

		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
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
