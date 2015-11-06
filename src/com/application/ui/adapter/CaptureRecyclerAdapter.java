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
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.Capture;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.DontPressWithParentImageView;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CaptureRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = CaptureRecyclerAdapter.class
			.getSimpleName();

	private static final int VIEW_TYPE_CAPTURE = 0;

	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Capture> mArrayListCapture;
	public OnItemClickListener mItemClickListener;
	private ImageLoader mImageLoader;

	public CaptureRecyclerAdapter(Context mContext,
			ArrayList<Capture> mArrayListCapture) {
		this.mContext = mContext;
		this.mArrayListCapture = mArrayListCapture;
		mInflater = LayoutInflater.from(mContext);
		mImageLoader = ApplicationLoader.getUILImageLoader();
	}

	public void addCaptureObjList(ArrayList<Capture> mListCapture) {
		mArrayListCapture = mListCapture;
		notifyDataSetChanged();
	}

	public void updateEventObj(int position, ArrayList<Capture> mListCapture) {
		mArrayListCapture = mListCapture;
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
		return VIEW_TYPE_CAPTURE;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_CAPTURE:
			return new CaptureViewHolder(mInflater.inflate(
					R.layout.item_recycler_capture, parent, false));
		default:
			return new CaptureViewHolder(mInflater.inflate(
					R.layout.item_recycler_capture, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof CaptureViewHolder){
			processCaptureViewHolder(viewHolder, position);
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListCapture.size();
	}

	public class CaptureViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		AppCompatTextView mNameTv;
		AppCompatTextView mSizeTv;
		
		CircleImageView mCircleIv;
		DontPressWithParentImageView mDeleteIv;
		
		FrameLayout mRootLayout;

		public CaptureViewHolder(View view) {
			super(view);
			mNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerCaptureNameTv);
			mSizeTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerCaptureSizeTv);
			
			mCircleIv = (CircleImageView)view.findViewById(R.id.itemRecyclerCaptureThumbnailIv);
			mDeleteIv = (DontPressWithParentImageView)view.findViewById(R.id.itemRecyclerCaptureDeleteIv);
			
			mRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerCaptureRootLayout);

			mDeleteIv.setOnClickListener(this);
			mRootLayout.setOnClickListener(this);
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
	
	@SuppressWarnings("deprecation")
	private void processCaptureViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Capture mObj = mArrayListCapture.get(position);
			((CaptureViewHolder)viewHolder).mNameTv.setText(Utilities.getFileName(mObj.getmFilePath()));
			((CaptureViewHolder)viewHolder).mSizeTv.setText(mObj.getmFileSize());
			String mPicturePath = mObj.getmFilePath();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inSampleSize = 8;
			Bitmap mBitmap = BitmapFactory.decodeFile(mPicturePath, options);
			((CaptureViewHolder)viewHolder).mCircleIv.setImageBitmap(mBitmap);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
