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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Award;
import com.application.beans.Birthday;
import com.application.ui.adapter.AwardRecyclerAdapter.AwardViewHolder;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BirthdayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	private static final int VIEW_TYPE_BIRTHDAY = 0;
	private static final int VIEW_TYPE_HEADER = 1;
	
	private static final String TAG = BirthdayRecyclerAdapter.class.getSimpleName();
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Birthday> mArrayListBirthday;
	OnItemClickListener mItemClickListener;
	private ImageLoader mImageLoader;

	public BirthdayRecyclerAdapter(Context mContext,
			ArrayList<Birthday> mArrayListBirthday) {
		this.mContext = mContext;
		this.mArrayListBirthday = mArrayListBirthday;
		mInflater = LayoutInflater.from(mContext);
		mImageLoader = ApplicationLoader.getUILImageLoader();
	}
	
	public void addBirthdayObjList(ArrayList<Birthday> mListBirthday) {
		mArrayListBirthday = mListBirthday;
		notifyDataSetChanged();
	}

	public void updateEventObj(int position, ArrayList<Birthday> mListBirthday) {
		mArrayListBirthday = mListBirthday;
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
		if (mArrayListBirthday.get(position).getmFileType().equalsIgnoreCase(AppConstants.BIRTHDAY.DETAIL)) {
			return VIEW_TYPE_BIRTHDAY;
		} else {
			return VIEW_TYPE_HEADER;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_BIRTHDAY:
			return new BirthdayViewHolder(mInflater.inflate(R.layout.item_recycler_birthday,parent, false));
		default:
			return new SectionViewHolder(mInflater.inflate(R.layout.item_recycler_birthday_sections, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof BirthdayViewHolder){
			processBirthdayViewHolder(viewHolder, position);
		}else if(viewHolder instanceof SectionViewHolder){
			processSectionViewHolder(viewHolder, position);
		} 
	}

	@Override
	public int getItemCount() {
		return mArrayListBirthday.size();
	}

	public class BirthdayViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		AppCompatTextView birthdayNameTv;
		AppCompatTextView birthdayDepartmentTv;
		CircleImageView birthdayUserIv;
		ImageView sendWishIv;
		ImageView sendMessageIv;
		FrameLayout rootLayout;
		ProgressWheel mProgressWheel;
		View readStripView;

		public BirthdayViewHolder(View view) {
			super(view);
			birthdayNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerBirthdayName);
			birthdayDepartmentTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerBirthdayDepartment);

			birthdayUserIv = (CircleImageView) view.findViewById(R.id.itemRecyclerBirthdayIv);
			
			mProgressWheel = (ProgressWheel) view.findViewById(R.id.itemRecyclerBirthdayImageLoadingProgress);

			sendMessageIv = (ImageView) view.findViewById(R.id.itemRecyclerBirthdayMessageIv);
			
			sendWishIv = (ImageView) view.findViewById(R.id.itemRecyclerBirthdayWishIv);

			rootLayout = (FrameLayout) view.findViewById(R.id.itemRecyclerBirthdayRootLayout);
			
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
	
	public class SectionViewHolder extends RecyclerView.ViewHolder {
	    AppCompatTextView headerTv;

		public SectionViewHolder(View view) {
			super(view);
			headerTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerBirthdaySectionTv);
	     }
	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	@SuppressWarnings("deprecation")
	private void processBirthdayViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Birthday mObj = mArrayListBirthday.get(position);
			((BirthdayViewHolder)viewHolder).birthdayNameTv.setText(mObj.getmBirthdayUserName());
			((BirthdayViewHolder)viewHolder).birthdayDepartmentTv.setText(mObj.getmBirthdayUserDep());
			if(!mObj.isRead()){
				((BirthdayViewHolder)viewHolder).birthdayNameTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((BirthdayViewHolder)viewHolder).readStripView.setVisibility(View.VISIBLE);
				((BirthdayViewHolder)viewHolder).rootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((BirthdayViewHolder)viewHolder).birthdayNameTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((BirthdayViewHolder)viewHolder).readStripView.setVisibility(View.GONE);
				((BirthdayViewHolder)viewHolder).rootLayout.setBackgroundResource(R.color.white);
			}
			
			if(mObj.isWished()){
				((BirthdayViewHolder)viewHolder).sendWishIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_birthday_wish_focused));
			}else{
				((BirthdayViewHolder)viewHolder).sendWishIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_birthday_wish_normal));
			}
			
			if(mObj.isMessaged()){
				((BirthdayViewHolder)viewHolder).sendMessageIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_birthday_message_focused));
			}else{
				((BirthdayViewHolder)viewHolder).sendMessageIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_birthday_message_normal));
			}
			
			
			try{
			final String mThumbnailPath = mObj.getmBirthdayUserImage();
				mImageLoader.displayImage(mThumbnailPath, ((BirthdayViewHolder)viewHolder).birthdayUserIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((BirthdayViewHolder)viewHolder).mProgressWheel.setVisibility(View.VISIBLE);
						((BirthdayViewHolder)viewHolder).birthdayUserIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((BirthdayViewHolder)viewHolder).mProgressWheel.setVisibility(View.GONE);
						((BirthdayViewHolder)viewHolder).birthdayUserIv.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((BirthdayViewHolder)viewHolder).mProgressWheel.setVisibility(View.GONE);
						((BirthdayViewHolder)viewHolder).birthdayUserIv.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((BirthdayViewHolder)viewHolder).mProgressWheel.setVisibility(View.GONE);
						((BirthdayViewHolder)viewHolder).birthdayUserIv.setVisibility(View.VISIBLE);
					}
				});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private void processSectionViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Birthday mObj = mArrayListBirthday.get(position);
			((SectionViewHolder)viewHolder).headerTv.setText(mObj.getmEmployeeId());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
