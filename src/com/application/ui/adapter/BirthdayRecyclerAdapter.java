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

import com.application.beans.Birthday;
import com.application.ui.view.CircleImageView;
import com.application.utils.ApplicationLoader;
import com.mobcast.R;

public class BirthdayRecyclerAdapter extends
		RecyclerView.Adapter<BirthdayRecyclerAdapter.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = BirthdayRecyclerAdapter.class
			.getSimpleName();
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Birthday> mArrayListBirthday;
	OnItemClickListener mItemClickListener;

	public BirthdayRecyclerAdapter(Context mContext,
			ArrayList<Birthday> mArrayListBirthday) {
		this.mContext = mContext;
		this.mArrayListBirthday = mArrayListBirthday;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_recycler_birthday,
				parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.birthdayDepartmentTv.setText(mArrayListBirthday.get(position)
				.getmBirthdayUserDep());
		viewHolder.birthdayNameTv.setText(mArrayListBirthday.get(position).getmBirthdayUserName());
		viewHolder.sendMessageIv.setTag(R.id.TAG_BIRTHDAYUSERNAME, "ABC");
		if(position == 0){
		}else if(position == 1){
		}else {
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListBirthday.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		AppCompatTextView birthdayNameTv;
		AppCompatTextView birthdayDepartmentTv;
		CircleImageView birthdayUserIv;
		ImageView sendWishIv;
		ImageView sendMessageIv;
		FrameLayout rootLayout;
		View readStripView;

		public ViewHolder(View view) {
			super(view);
			birthdayNameTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerBirthdayName);
			birthdayDepartmentTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerBirthdayDepartment);

			birthdayUserIv = (CircleImageView) view
					.findViewById(R.id.itemRecyclerBirthdayIv);

			sendMessageIv = (ImageView) view
					.findViewById(R.id.itemRecyclerBirthdayMessageIv);
			
			sendWishIv = (ImageView) view
					.findViewById(R.id.itemRecyclerBirthdayWishIv);

			rootLayout = (FrameLayout) view
					.findViewById(R.id.itemRecyclerBirthdayRootLayout);
			
			readStripView = (View) view.findViewById(R.id.itemRecyclerReadView);
			
			sendMessageIv.setOnClickListener(this);
			sendWishIv.setOnClickListener(this);
			rootLayout.setOnClickListener(this);
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
