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
import android.content.res.Resources;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.NotifRemind;
import com.application.ui.adapter.AwardRecyclerAdapter.OnItemClickListenerA;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.mobcast.R;

public class NotifRemindRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
	@SuppressWarnings("unused")
	private static final String TAG = NotifRemindRecyclerAdapter.class
			.getSimpleName();

	private static final int VIEW_TYPE_NOTIF_REMIND = 0;

	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private Resources mResources;
	public OnItemClickListenerN mItemClickListener;
	private ArrayList<NotifRemind> mArrayListNotifRemind;
	private int mWhichTheme = 0;
	
	public interface OnItemClickListenerN {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListenerN mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}

	public NotifRemindRecyclerAdapter(Context mContext,
			ArrayList<NotifRemind> mArrayListNotifRemind) {
		this.mContext = mContext;
		this.mArrayListNotifRemind = mArrayListNotifRemind;
		mInflater = LayoutInflater.from(mContext);
		mWhichTheme = ApplicationLoader.getPreferences().getAppTheme();
		mResources = mContext.getResources();
	}

	public void addNotifRemindObjList(ArrayList<NotifRemind> mListNotifRemind) {
		mArrayListNotifRemind = mListNotifRemind;
		notifyDataSetChanged();
	}

	public void updateNotifRemindObj(int position, ArrayList<NotifRemind> mListNotifRemind) {
		mArrayListNotifRemind = mListNotifRemind;
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
		return VIEW_TYPE_NOTIF_REMIND;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new NotifRemindViewHolder(mInflater.inflate(R.layout.item_recycler_notifremind, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof NotifRemindViewHolder){
			processNotifRemindViewHolder(viewHolder, position);
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListNotifRemind.size();
	}

	public class NotifRemindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		AppCompatTextView mTextView;
		
		ImageView mImageView;
		
		FrameLayout mRootLayout;

		public NotifRemindViewHolder(View view) {
			super(view);
			mRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerNotifRemindRootLayout);
			mImageView = (ImageView)view.findViewById(R.id.itemRecyclerNotifRemindIndicatorImageView);
			mTextView = (AppCompatTextView)view.findViewById(R.id.itemRecyclerNotifRemindTitleTv);
			mRootLayout.setOnClickListener(this);
		}
		
		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void processNotifRemindViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			NotifRemind mObj = mArrayListNotifRemind.get(position);
			((NotifRemindViewHolder)viewHolder).mTextView.setText(mObj.getmTitle());
			ThemeUtils.applyThemeToTimeLineIconsWithSVG(((NotifRemindViewHolder)viewHolder).mImageView, mResources,true, mObj.getmType(), mWhichTheme);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
