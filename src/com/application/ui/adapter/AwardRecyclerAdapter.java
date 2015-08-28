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
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.application.beans.Award;
import com.application.ui.adapter.MobcastRecyclerAdapter.ImageViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.OnItemLongClickListener;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.DontPressWithParentImageView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AwardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = AwardRecyclerAdapter.class
			.getSimpleName();

	private static final int VIEW_TYPE_AWARD = 0;
	private static final int VIEW_TYPE_FOOTER = 1;

	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Award> mArrayListAward;
	public OnItemClickListenerA mItemClickListener;
	public OnItemLongClickListenerA mItemLongClickListener;
	private ImageLoader mImageLoader;

	public AwardRecyclerAdapter(Context mContext,
			ArrayList<Award> mArrayListAward) {
		this.mContext = mContext;
		this.mArrayListAward = mArrayListAward;
		mInflater = LayoutInflater.from(mContext);
		mImageLoader = ApplicationLoader.getUILImageLoader();
	}

	public void addAwardObjList(ArrayList<Award> mListAward) {
		mArrayListAward = mListAward;
		notifyDataSetChanged();
	}

	public void updateEventObj(int position, ArrayList<Award> mListAward) {
		mArrayListAward = mListAward;
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
		if (mArrayListAward.get(position).getmFileType()
				.equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)) {
			return VIEW_TYPE_FOOTER;
		} else {
			return VIEW_TYPE_AWARD;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_FOOTER:
			return new FooterViewHolder(mInflater.inflate(
					R.layout.layout_footerview, parent, false));
		default:
			return new AwardViewHolder(mInflater.inflate(
					R.layout.item_recycler_award, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof AwardViewHolder){
			processAwardViewHolder(viewHolder, position);
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListAward.size();
	}

	public class AwardViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {
		AppCompatTextView mWinnerNameTv;
		AppCompatTextView mAwardNameTv;
		AppCompatTextView mCongratulatedTv;
		
		CircleImageView mWinnerIv;
		ProgressWheel mProgressWheel;
		DontPressWithParentImageView mCongratulateIv;
		DontPressWithParentImageView mMessageIv;
		RelativeLayout mRootLayout;
		View mReadStripView;

		public AwardViewHolder(View view) {
			super(view);
			mAwardNameTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerAwardName);
			mWinnerNameTv = (AppCompatTextView) view
					.findViewById(R.id.itemRecyclerAwardWinnerName);
			
			mProgressWheel = (ProgressWheel)view.findViewById(R.id.itemRecyclerAwardImageLoadingProgress);
			mCongratulatedTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerAwardCongratulatedTv);

			mWinnerIv = (CircleImageView) view
					.findViewById(R.id.itemRecyclerAwardIv);

			mCongratulateIv = (DontPressWithParentImageView) view
					.findViewById(R.id.itemRecyclerCongratulateIv);

			mMessageIv = (DontPressWithParentImageView) view
					.findViewById(R.id.itemRecyclerAwardMessageIv);

			mRootLayout = (RelativeLayout) view
					.findViewById(R.id.itemRecyclerAwardRootLayout);

			mReadStripView = (View) view.findViewById(R.id.itemRecyclerReadView);

			mCongratulateIv.setOnClickListener(this);
			mMessageIv.setOnClickListener(this);
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

	public interface OnItemClickListenerA {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListenerA mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	public interface OnItemLongClickListenerA {
		public void onItemLongClick(View view, int position);
	}

	public void setOnItemLongClickListener(
			final OnItemLongClickListenerA mItemLongClickListener) {
		this.mItemLongClickListener = mItemLongClickListener;
	}
	
	@SuppressWarnings("deprecation")
	private void processAwardViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Award mObj = mArrayListAward.get(position);
			((AwardViewHolder)viewHolder).mAwardNameTv.setText(mObj.getmRecognition());
			((AwardViewHolder)viewHolder).mWinnerNameTv.setText(mObj.getmName());
			if(!mObj.isRead()){
				((AwardViewHolder)viewHolder).mWinnerNameTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((AwardViewHolder)viewHolder).mReadStripView.setVisibility(View.VISIBLE);
				((AwardViewHolder)viewHolder).mRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((AwardViewHolder)viewHolder).mWinnerNameTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((AwardViewHolder)viewHolder).mReadStripView.setVisibility(View.GONE);
				((AwardViewHolder)viewHolder).mRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(mObj.isCongratulated()){
				((AwardViewHolder)viewHolder).mCongratulateIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_award_cong_selected));
			}else{
				((AwardViewHolder)viewHolder).mCongratulateIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_award_cong_normal));
			}
			
			if(!TextUtils.isEmpty(mObj.getmCongratulatedCount())){
				((AwardViewHolder)viewHolder).mCongratulatedTv.setText(mObj.getmCongratulatedCount() + " "+mContext.getResources().getString(R.string.congratulated_text));
			}
			
			try{
			final String mThumbnailPath = mObj.getmThumbPath();
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				((AwardViewHolder)viewHolder).mWinnerIv.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mObj.getmThumbLink(), ((AwardViewHolder)viewHolder).mWinnerIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((AwardViewHolder)viewHolder).mProgressWheel.setVisibility(View.VISIBLE);
						((AwardViewHolder)viewHolder).mWinnerIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((AwardViewHolder)viewHolder).mProgressWheel.setVisibility(View.GONE);
						((AwardViewHolder)viewHolder).mWinnerIv.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((AwardViewHolder)viewHolder).mProgressWheel.setVisibility(View.GONE);
						((AwardViewHolder)viewHolder).mWinnerIv.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((AwardViewHolder)viewHolder).mProgressWheel.setVisibility(View.GONE);
						((AwardViewHolder)viewHolder).mWinnerIv.setVisibility(View.VISIBLE);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
