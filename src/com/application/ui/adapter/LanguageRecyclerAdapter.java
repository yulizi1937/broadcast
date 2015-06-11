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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.application.beans.Language;
import com.application.utils.ApplicationLoader;
import com.mobcast.R;

public class LanguageRecyclerAdapter extends
		RecyclerView.Adapter<LanguageRecyclerAdapter.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = LanguageRecyclerAdapter.class
			.getSimpleName();
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Language> mArrayListLanguage;
	OnItemClickListener mItemClickListener;

	public LanguageRecyclerAdapter(Context mContext,
			ArrayList<Language> mArrayListLanguage) {
		this.mContext = mContext;
		this.mArrayListLanguage = mArrayListLanguage;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_language,
				parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.mLanguage.setText(mArrayListLanguage.get(position)
				.getmLanguage());
		if(ApplicationLoader.getPreferences().getAppLanguage().equalsIgnoreCase(mArrayListLanguage.get(position).getmLanguageCode())){
			viewHolder.mIsSelected.setVisibility(View.VISIBLE);	
		}else{
			viewHolder.mIsSelected.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return mArrayListLanguage.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		AppCompatTextView mLanguage;
		
		ImageView mIsSelected;
		
		RelativeLayout mLanguageLayout;

		public ViewHolder(View view) {
			super(view);
			mLanguage = (AppCompatTextView) view
					.findViewById(R.id.itemLanguageTextView);
			
			mLanguageLayout = (RelativeLayout) view
					.findViewById(R.id.itemLanugageLayout);
			
			mIsSelected = (ImageView)view.findViewById(R.id.itemLanguageImageView);
			
			mLanguageLayout.setOnClickListener(this);
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
