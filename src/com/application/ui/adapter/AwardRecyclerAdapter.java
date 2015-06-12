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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.application.beans.Award;
import com.application.ui.view.CircleImageView;
import com.mobcast.R;

public class AwardRecyclerAdapter extends
		RecyclerView.Adapter<AwardRecyclerAdapter.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = AwardRecyclerAdapter.class
			.getSimpleName();
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Award> mArrayListAward;
	OnItemClickListener mItemClickListener;

	public AwardRecyclerAdapter(Context mContext,
			ArrayList<Award> mArrayListAward) {
		this.mContext = mContext;
		this.mArrayListAward = mArrayListAward;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_recycler_award,
				parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.awardNameTv.setText(mArrayListAward.get(position)
				.getmAwardName());
	}

	@Override
	public int getItemCount() {
		return mArrayListAward.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		AppCompatTextView winnerNameTv;
		AppCompatTextView awardNameTv;
		CircleImageView winnerIv;
		ImageView congratulateIv;
		RelativeLayout rootLayout;
		View readStripView;

		public ViewHolder(View view) {
			super(view);
			awardNameTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerAwardName);
			winnerNameTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerAwardWinnerName);

			winnerIv = (CircleImageView) view
					.findViewById(R.id.itemRecyclerAwardIv);

			congratulateIv = (ImageView) view
					.findViewById(R.id.itemRecyclerCongratulateIv);

			rootLayout = (RelativeLayout) view
					.findViewById(R.id.itemRecyclerAwardRootLayout);
			
			readStripView = (View) view.findViewById(R.id.itemRecyclerReadView);
			
			congratulateIv.setOnClickListener(this);
			rootLayout.setOnClickListener(this);
			readStripView.setOnClickListener(this);
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
