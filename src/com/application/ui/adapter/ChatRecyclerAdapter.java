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
//https://code.google.com/p/android/issues/detail?id=28057

package com.application.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.application.beans.Chat;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = ChatRecyclerAdapter.class.getSimpleName(); 
			
    private static final int VIEW_TYPE_HEADER     = 0;
    private static final int VIEW_TYPE_TEXT       = 1;

    private LayoutInflater mInflater;
    private ArrayList<Chat> mArrayListChat;
    private View mHeaderView;
    public OnItemClickListener mItemClickListener;
    private Context mContext;
    private ImageLoader mImageLoader;

    public ChatRecyclerAdapter(Context context, ArrayList<Chat> mArrayListChat, View headerView) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mArrayListChat = mArrayListChat;
        mHeaderView = headerView;
        mImageLoader = ApplicationLoader.getUILImageLoader();
    }
    
    
	@Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mArrayListChat.size();
        } else {
            return mArrayListChat.size() + 1;
        }
    }
    
    public void addChatObjList(ArrayList<Chat> mListChat){
    	mArrayListChat= mListChat;
    	notifyDataSetChanged();
    }
    
    public void updateChatObj(int position, ArrayList<Chat> mListChat){
    	mArrayListChat = mListChat;
    	notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
    	switch(position){
    	case 0:
    		return VIEW_TYPE_HEADER;
    	default:
    		return getItemTypeFromObject(position-1);
    	}
    }
    
    public int getItemTypeFromObject(int position){
    	return VIEW_TYPE_TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	switch(viewType){
    	case VIEW_TYPE_TEXT:
    		return  new ChatTextViewHolder(mInflater.inflate(R.layout.item_recycler_chat, parent, false));
    	default:
    		return new HeaderViewHolder(mHeaderView);
    	}
    }

	@Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		position-=1;
		if (viewHolder instanceof ChatTextViewHolder) {
			processChatTextViewHolder(viewHolder, position);
		}
    }
    
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    
    public class ChatTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mChatTextRootLayout;
    	
    	View mChatTextReadView;
        CircleImageView mChatTextIv;
        
        AppCompatTextView mChatTextTitleTv;
        AppCompatTextView mChatTextMessageTv;
        
        public ChatTextViewHolder(View view) {
            super(view);
            mChatTextRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerChatRootLayout);
            
            mChatTextReadView = (View)view.findViewById(R.id.itemRecyclerChatReadView);
            mChatTextTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerChatTitleTv);
            mChatTextMessageTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerChatMessageTv);
            mChatTextIv = (CircleImageView) view.findViewById(R.id.itemRecyclerChatIv);
            
            mChatTextReadView.setVisibility(View.INVISIBLE);
            
            mChatTextRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}

    }
    

	/* (non-Javadoc)
	 * @see com.application.ui.view.FlexibleDividerDecoration.VisibilityProvider#shouldHideDivider(int, android.support.v7.widget.RecyclerView)
	 */
	@Override
	public boolean shouldHideDivider(int position, RecyclerView parent) {
		// TODO Auto-generated method stub
		if (position == 0) {
	            return true;
	        }
	        return false;
	}
	
	
	@SuppressWarnings("deprecation")
	private void processChatTextViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Chat mObj = mArrayListChat.get(position);
			((ChatTextViewHolder)viewHolder).mChatTextTitleTv.setText(mObj.getmName());
			mImageLoader.displayImage(mObj.getmUserDpLink(), ((ChatTextViewHolder)viewHolder).mChatTextIv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
}
