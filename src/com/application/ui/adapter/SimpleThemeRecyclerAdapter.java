/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.application.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Theme;
import com.application.utils.FileLog;
import com.mobcast.R;

public class SimpleThemeRecyclerAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private LayoutInflater mInflater;
	private ArrayList<Theme> mListTheme;

	public OnItemClickListener mItemClickListener;

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}

	private static final String TAG = SimpleThemeRecyclerAdapter.class
			.getSimpleName();

	public SimpleThemeRecyclerAdapter(Context context,
			ArrayList<Theme> mListTheme) {
		mInflater = LayoutInflater.from(context);
		this.mListTheme = mListTheme;
	}

	@Override
	public int getItemCount() {
		return mListTheme.size();
	}
	
	public int selectedTheme(){
		int mSelected = 0;
		for(int i = 0 ; i< mListTheme.size();i++){
			if(mListTheme.get(i).isSelected()){
				mSelected = i;
			}
		}
		return mSelected;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		return new ThemeViewHolder(mInflater.inflate(R.layout.item_theme,
				parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if (viewHolder instanceof ThemeViewHolder) {
			processThemeViewHolder(viewHolder, position);
		}
	}

	public class ThemeViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
		FrameLayout mLayout;
		FrameLayout mFrameLayout;
		ImageView mIsCheckedIv;

		public ThemeViewHolder(View view) {
			super(view);
			mLayout = (FrameLayout) view.findViewById(R.id.itemThemeLayout);
			mFrameLayout = (FrameLayout) view
					.findViewById(R.id.itemThemeAppLayout);
			mIsCheckedIv = (ImageView) view.findViewById(R.id.itemThemeChecked);

			mLayout.setOnClickListener(this);
		}

		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void processThemeViewHolder(RecyclerView.ViewHolder viewHolder,
			int position) {
		try {
			((ThemeViewHolder) viewHolder).mFrameLayout
					.setBackgroundColor(Color.parseColor(mListTheme.get(
							position).getmColor()));
			if (mListTheme.get(position).isSelected()) {
				((ThemeViewHolder) viewHolder).mIsCheckedIv
						.setVisibility(View.VISIBLE);
			} else {
				((ThemeViewHolder) viewHolder).mIsCheckedIv
						.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
}
