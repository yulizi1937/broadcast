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
import android.widget.RelativeLayout;

import com.mobcast.R;

public class SimpleItemRecyclerAdapter extends
		RecyclerView.Adapter<SimpleItemRecyclerAdapter.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = SimpleItemRecyclerAdapter.class
			.getSimpleName();
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<String> mArrayList;
	OnItemClickListener mItemClickListener;

	public SimpleItemRecyclerAdapter(Context mContext,
			ArrayList<String> mArrayList) {
		this.mContext = mContext;
		this.mArrayList = mArrayList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_simple, parent,
				false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.mTextView.setText(mArrayList.get(position).toString());
	}

	@Override
	public int getItemCount() {
		return mArrayList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		AppCompatTextView mTextView;

		RelativeLayout mLayout;

		public ViewHolder(View view) {
			super(view);
			mTextView = (AppCompatTextView) view
					.findViewById(R.id.itemSimpleTextView);

			mLayout = (RelativeLayout) view.findViewById(R.id.itemSimpleLayout);

			mLayout.setOnClickListener(this);
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
}
