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
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Mobcast;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MobcastRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = MobcastRecyclerAdapter.class.getSimpleName(); 
			
    private static final int VIEW_TYPE_HEADER     = 0;
    private static final int VIEW_TYPE_TEXT       = 1;
    private static final int VIEW_TYPE_NEWS       = 2;
    private static final int VIEW_TYPE_IMAGE      = 3;
    private static final int VIEW_TYPE_VIDEO      = 4;
    private static final int VIEW_TYPE_AUDIO      = 5;
    private static final int VIEW_TYPE_DOC        = 6;
    private static final int VIEW_TYPE_PDF        = 7;
    private static final int VIEW_TYPE_PPT        = 8;
    private static final int VIEW_TYPE_XLS        = 9;
    private static final int VIEW_TYPE_FEEDBACK   = 10;
    private static final int VIEW_TYPE_STREAM     = 11;
    private static final int VIEW_TYPE_FOOTER     = 12;

    private LayoutInflater mInflater;
    private ArrayList<Mobcast> mArrayListMobcast;
    private View mHeaderView;
    public OnItemClickListener mItemClickListener;
    private Context mContext;
    private ImageLoader mImageLoader;

    public MobcastRecyclerAdapter(Context context, ArrayList<Mobcast> mArrayListMobcast, View headerView) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mArrayListMobcast = mArrayListMobcast;
        mHeaderView = headerView;
        mImageLoader = ApplicationLoader.getUILImageLoader();
    }
    
    
	@Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mArrayListMobcast.size();
        } else {
            return mArrayListMobcast.size() + 1;
        }
    }
    
    public void addMobcastObjList(ArrayList<Mobcast> mListMobcast){
    	mArrayListMobcast = mListMobcast;
    	notifyDataSetChanged();
    }
    
    public void updateMobcastObj(int position, ArrayList<Mobcast> mListMobcast){
    	mArrayListMobcast = mListMobcast;
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
//        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }
    
    public int getItemTypeFromObject(int position){
    	if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.TEXT)){
    		return VIEW_TYPE_TEXT;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.NEWS)){
    		return VIEW_TYPE_NEWS;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.IMAGE)){
    		return VIEW_TYPE_IMAGE;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.VIDEO)){
    		return VIEW_TYPE_VIDEO;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.AUDIO)){
    		return VIEW_TYPE_AUDIO;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.DOC)){
    		return VIEW_TYPE_DOC;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.PDF)){
    		return VIEW_TYPE_PDF;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.PPT)){
    		return VIEW_TYPE_PPT;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FEEDBACK)){
    		return VIEW_TYPE_FEEDBACK;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.STREAM)){
    		return VIEW_TYPE_STREAM;
    	}else if(mArrayListMobcast.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)){
    		return VIEW_TYPE_FOOTER;
    	}{
    		return VIEW_TYPE_XLS;
    	}
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	switch(viewType){
    	case VIEW_TYPE_TEXT:
    		return  new TextViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_text, parent, false));
    	case VIEW_TYPE_IMAGE:
    		return  new ImageViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_image, parent, false));
    	case VIEW_TYPE_VIDEO:
    		return  new VideoViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_video, parent, false));
    	case VIEW_TYPE_AUDIO:
    		return  new AudioViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_audio, parent, false));
    	case VIEW_TYPE_PDF:
    		return  new PdfViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_pdf, parent, false));
    	case VIEW_TYPE_DOC:
    		return  new DocViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_doc, parent, false));
    	case VIEW_TYPE_PPT:
    		return  new PptViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_ppt, parent, false));
    	case VIEW_TYPE_XLS:
    		return  new XlsViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_xls, parent, false));
    	case VIEW_TYPE_FEEDBACK:
    		return  new FeedbackViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_feedback, parent, false));
    	case VIEW_TYPE_NEWS:
    		return  new NewsViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_news, parent, false));
    	case VIEW_TYPE_STREAM:
    		return  new YoutubeLiveStreamViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_livestream, parent, false));
    	case VIEW_TYPE_FOOTER:
    		return  new FooterViewHolder(mInflater.inflate(R.layout.layout_footerview, parent, false));
    	default:
    		return new HeaderViewHolder(mHeaderView);
    	}
    }

	@Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		position-=1;
		if (viewHolder instanceof TextViewHolder) {
			processTextViewHolder(viewHolder, position);
		} else if (viewHolder instanceof ImageViewHolder) {
			processImageViewHolder(viewHolder, position);
		} else if (viewHolder instanceof VideoViewHolder) {
			processVideoViewHolder(viewHolder, position);
		} else if (viewHolder instanceof AudioViewHolder) {
			processAudioViewHolder(viewHolder, position);
		} else if (viewHolder instanceof PdfViewHolder) {
			processPdfViewHolder(viewHolder, position);
		} else if (viewHolder instanceof DocViewHolder) {
			processDocViewHolder(viewHolder, position);
		} else if (viewHolder instanceof PptViewHolder) {
			processPptViewHolder(viewHolder, position);
		} else if (viewHolder instanceof XlsViewHolder) {
			processXlsViewHolder(viewHolder, position);
		} else if (viewHolder instanceof FeedbackViewHolder) {
			processFeedbackViewHolder(viewHolder, position);
		} else if (viewHolder instanceof YoutubeLiveStreamViewHolder) {
			processLiveStreamViewHolder(viewHolder, position);
		}else if (viewHolder instanceof FooterViewHolder) {
		}else if (viewHolder instanceof NewsViewHolder) {
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

    
    public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastTextRootLayout;
    	
    	View mMobcastTextReadView;
        ImageView mMobcastTextIndicatorIv;
        
        AppCompatTextView mMobcastTextTitleTv;
        AppCompatTextView mMobcastTextByTv;
        AppCompatTextView mMobcastTextViewCountTv;
        AppCompatTextView mMobcastTextLikeCountTv;
        ImageView mMobcastTextLinkTv;
        AppCompatTextView mMobcastTextSummaryTv;
        
        public TextViewHolder(View view) {
            super(view);
            mMobcastTextRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastTextRootLayout);
            
            mMobcastTextReadView = (View)view.findViewById(R.id.itemRecyclerMobcastTextReadView);
            
            mMobcastTextIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastTextIndicatorImageView);
            
            mMobcastTextTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextTitleTv);
            mMobcastTextByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextByTv);
            mMobcastTextViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextViewCountTv);
            mMobcastTextLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextLikeCountTv);
            mMobcastTextLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastTextLinkTv);
            mMobcastTextSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextSummaryTv);
            
            mMobcastTextReadView.setVisibility(View.INVISIBLE);
            
            mMobcastTextRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    
    
	public class ImageViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
    	FrameLayout mMobcastImageRootLayout;
    	
    	View mMobcastImageReadView;
        ImageView mMobcastImageIndicatorIv;
        ImageView mMobcastImageMainImageViewIv;
        
        AppCompatTextView mMobcastImageTitleTv;
        AppCompatTextView mMobcastImageByTv;
        AppCompatTextView mMobcastImageViewCountTv;
        AppCompatTextView mMobcastImageLikeCountTv;
        ImageView mMobcastImageLinkTv;
        
        ProgressWheel mMobcastImageProgressWheel;
        
        
        public ImageViewHolder(View view) {
            super(view);
            mMobcastImageRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastImageRootLayout);
            
            mMobcastImageReadView = (View)view.findViewById(R.id.itemRecyclerMobcastImageReadView);
            
            mMobcastImageIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastImageIndicatorImageView);
            
            mMobcastImageMainImageViewIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastImageMainImageIv);
            
            mMobcastImageTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageTitleTv);
            mMobcastImageByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageByTv);
            mMobcastImageViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageViewCountTv);
            mMobcastImageLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageLikeCountTv);
            mMobcastImageLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastImageLinkTv);
            
            mMobcastImageProgressWheel = (ProgressWheel)view.findViewById(R.id.itemRecyclerMobcastImageLoadingProgress);
            mMobcastImageReadView.setVisibility(View.VISIBLE);
            
            mMobcastImageRootLayout.setOnClickListener(this);
            
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
	public class VideoViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
    	FrameLayout mMobcastVideoRootLayout;
    	
    	View mMobcastVideoReadView;
        ImageView mMobcastVideoIndicatorIv;
        ImageView mMobcastVideoThumbnailIv;
        ImageView mMobcastVideoPlayIv;
        
        AppCompatTextView mMobcastVideoTitleTv;
        AppCompatTextView mMobcastVideoByTv;
        AppCompatTextView mMobcastVideoViewCountTv;
        AppCompatTextView mMobcastVideoLikeCountTv;
        ImageView mMobcastVideoLinkTv;
        AppCompatTextView mMobcastVideoViewDurationTv;
        ProgressWheel mMobcastVideoLoadingProgress;
        
        
        
        public VideoViewHolder(View view) {
            super(view);
            mMobcastVideoRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastVideoRootLayout);
            
            mMobcastVideoReadView = (View)view.findViewById(R.id.itemRecyclerMobcastVideoReadView);
            
            mMobcastVideoIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastVideoIndicatorImageView);
            
            mMobcastVideoThumbnailIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastVideoThumbnailImageIv);
            mMobcastVideoPlayIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastVideoPlayImageIv);
            
            mMobcastVideoTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastVideoTitleTv);
            mMobcastVideoByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastVideoByTv);
            mMobcastVideoViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastVideoViewCountTv);
            mMobcastVideoLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastVideoLikeCountTv);
            mMobcastVideoLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastVideoLinkTv);
            mMobcastVideoViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastVideoDurationTv);
            mMobcastVideoLoadingProgress = (ProgressWheel)view.findViewById(R.id.itemRecyclerMobcastVideoLoadingProgress);
            
            mMobcastVideoReadView.setVisibility(View.INVISIBLE);
            
            mMobcastVideoRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
	
	public class YoutubeLiveStreamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		FrameLayout mMobcastLiveRootLayout;

		View mMobcastLiveReadView;
		ImageView mMobcastLiveIndicatorIv;
		ImageView mMobcastLiveThumbnailIv;
		ImageView mMobcastLivePlayIv;

		AppCompatTextView mMobcastLiveTitleTv;
		AppCompatTextView mMobcastLiveByTv;
		AppCompatTextView mMobcastLiveViewCountTv;
		AppCompatTextView mMobcastLiveLikeCountTv;
		ImageView mMobcastLiveLinkTv;
		AppCompatTextView mMobcastLiveViewDurationTv;


		public YoutubeLiveStreamViewHolder(View view) {
			super(view);
			mMobcastLiveRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastLiveRootLayout);
    
			mMobcastLiveReadView = (View)view.findViewById(R.id.itemRecyclerMobcastLiveReadView);
    
			mMobcastLiveIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastLiveIndicatorImageView);
    
			mMobcastLiveThumbnailIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastLiveThumbnailImageIv);
			mMobcastLivePlayIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastLivePlayImageIv);
    
			mMobcastLiveTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastLiveTitleTv);
			mMobcastLiveByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastLiveByTv);
			mMobcastLiveViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastLiveViewCountTv);
			mMobcastLiveLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastLiveLikeCountTv);
			mMobcastLiveLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastLiveLinkTv);
			mMobcastLiveViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastLiveDurationTv);
			
			mMobcastLiveReadView.setVisibility(View.INVISIBLE);
    
			mMobcastLiveRootLayout.setOnClickListener(this);
		}

		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
	}
    
    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastAudioRootLayout;
    	
    	View mMobcastAudioReadView;
        ImageView mMobcastAudioIndicatorIv;
        ImageView mMobcastAudioFileTypeIv;
        
        AppCompatTextView mMobcastAudioTitleTv;
        AppCompatTextView mMobcastAudioByTv;
        AppCompatTextView mMobcastAudioViewCountTv;
        AppCompatTextView mMobcastAudioLikeCountTv;
        ImageView mMobcastAudioLinkTv;
        AppCompatTextView mMobcastAudioViewDurationTv;
        AppCompatTextView mMobcastAudioViewFileNameTv;
        AppCompatTextView mMobcastAudioSummaryTv;
        
        
        public AudioViewHolder(View view) {
            super(view);
            mMobcastAudioRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastAudioRootLayout);
            
            mMobcastAudioReadView = (View)view.findViewById(R.id.itemRecyclerMobcastAudioReadView);
            
            mMobcastAudioIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastAudioIndicatorImageView);
            mMobcastAudioFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastAudioDetailFileTypeIv);
            
            mMobcastAudioTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioTitleTv);
            mMobcastAudioByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioByTv);
            mMobcastAudioViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioViewCountTv);
            mMobcastAudioLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioLikeCountTv);
            mMobcastAudioLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastAudioLinkTv);
            mMobcastAudioViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioDetailFileInfoDetailTv);
            mMobcastAudioViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioDetailFileInfoNameTv);
            mMobcastAudioSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastAudioSummaryTv);
            
            mMobcastAudioReadView.setVisibility(View.INVISIBLE);
            
            mMobcastAudioRootLayout.setOnClickListener(this);
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public  class PdfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastPdfRootLayout;
    	
    	View mMobcastPdfReadView;
        ImageView mMobcastPdfIndicatorIv;
        ImageView mMobcastPdfFileTypeIv;
        
        AppCompatTextView mMobcastPdfTitleTv;
        AppCompatTextView mMobcastPdfByTv;
        AppCompatTextView mMobcastPdfViewCountTv;
        AppCompatTextView mMobcastPdfLikeCountTv;
        ImageView mMobcastPdfLinkTv;
        AppCompatTextView mMobcastPdfViewFileMetaTv;
        AppCompatTextView mMobcastPdfViewFileNameTv;
        
        
        public PdfViewHolder(View view) {
            super(view);
            mMobcastPdfRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastPdfRootLayout);
            
            mMobcastPdfReadView = (View)view.findViewById(R.id.itemRecyclerMobcastPdfReadView);
            
            mMobcastPdfIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastPdfIndicatorImageView);
            mMobcastPdfFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastPdfDetailFileTypeIv);
            
            mMobcastPdfTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPdfTitleTv);
            mMobcastPdfByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPdfByTv);
            mMobcastPdfViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPdfViewCountTv);
            mMobcastPdfLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPdfLikeCountTv);
            mMobcastPdfLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastPdfLinkTv);
            mMobcastPdfViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPdfDetailFileInfoDetailTv);
            mMobcastPdfViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPdfDetailFileInfoNameTv);
            
            mMobcastPdfReadView.setVisibility(View.INVISIBLE);
            
            mMobcastPdfRootLayout.setOnClickListener(this);
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastDocRootLayout;
    	
    	View mMobcastDocReadView;
        ImageView mMobcastDocIndicatorIv;
        ImageView mMobcastDocFileTypeIv;
        
        AppCompatTextView mMobcastDocTitleTv;
        AppCompatTextView mMobcastDocByTv;
        AppCompatTextView mMobcastDocViewCountTv;
        AppCompatTextView mMobcastDocLikeCountTv;
        ImageView mMobcastDocLinkTv;
        AppCompatTextView mMobcastDocViewFileMetaTv;
        AppCompatTextView mMobcastDocViewFileNameTv;
        
        
        public DocViewHolder(View view) {
            super(view);
            mMobcastDocRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastDocRootLayout);
            
            mMobcastDocReadView = (View)view.findViewById(R.id.itemRecyclerMobcastDocReadView);
            
            mMobcastDocIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastDocIndicatorImageView);
            mMobcastDocFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastDocDetailFileTypeIv);
            
            mMobcastDocTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastDocTitleTv);
            mMobcastDocByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastDocByTv);
            mMobcastDocViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastDocViewCountTv);
            mMobcastDocLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastDocLikeCountTv);
            mMobcastDocLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastDocLinkTv);
            mMobcastDocViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastDocDetailFileInfoDetailTv);
            mMobcastDocViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastDocDetailFileInfoNameTv);
            
            mMobcastDocReadView.setVisibility(View.INVISIBLE);
            
            mMobcastDocRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
   public class PptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastPptRootLayout;
    	
    	View mMobcastPptReadView;
        ImageView mMobcastPptIndicatorIv;
        ImageView mMobcastPptFileTypeIv;
        
        AppCompatTextView mMobcastPptTitleTv;
        AppCompatTextView mMobcastPptByTv;
        AppCompatTextView mMobcastPptViewCountTv;
        AppCompatTextView mMobcastPptLikeCountTv;
        ImageView mMobcastPptLinkTv;
        AppCompatTextView mMobcastPptViewFileMetaTv;
        AppCompatTextView mMobcastPptViewFileNameTv;
        
        
        public PptViewHolder(View view) {
            super(view);
            mMobcastPptRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastPptRootLayout);
            
            mMobcastPptReadView = (View)view.findViewById(R.id.itemRecyclerMobcastPptReadView);
            
            mMobcastPptIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastPptIndicatorImageView);
            mMobcastPptFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastPptDetailFileTypeIv);
            
            mMobcastPptTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPptTitleTv);
            mMobcastPptByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPptByTv);
            mMobcastPptViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPptViewCountTv);
            mMobcastPptLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPptLikeCountTv);
            mMobcastPptLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastPptLinkTv);
            mMobcastPptViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPptDetailFileInfoDetailTv);
            mMobcastPptViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastPptDetailFileInfoNameTv);
            
            mMobcastPptReadView.setVisibility(View.INVISIBLE);
            
            mMobcastPptRootLayout.setOnClickListener(this);
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public class XlsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastXlsRootLayout;
    	
    	View mMobcastXlsReadView;
        ImageView mMobcastXlsIndicatorIv;
        ImageView mMobcastXlsFileTypeIv;
        
        AppCompatTextView mMobcastXlsTitleTv;
        AppCompatTextView mMobcastXlsByTv;
        AppCompatTextView mMobcastXlsViewCountTv;
        AppCompatTextView mMobcastXlsLikeCountTv;
        ImageView mMobcastXlsLinkTv;
        AppCompatTextView mMobcastXlsViewFileMetaTv;
        AppCompatTextView mMobcastXlsViewFileNameTv;
        
        
        public XlsViewHolder(View view) {
            super(view);
            mMobcastXlsRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastXlsRootLayout);
            
            mMobcastXlsReadView = (View)view.findViewById(R.id.itemRecyclerMobcastXlsReadView);
            
            mMobcastXlsIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastXlsIndicatorImageView);
            mMobcastXlsFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastXlsDetailFileTypeIv);
            
            mMobcastXlsTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastXlsTitleTv);
            mMobcastXlsByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastXlsByTv);
            mMobcastXlsViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastXlsViewCountTv);
            mMobcastXlsLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastXlsLikeCountTv);
            mMobcastXlsLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastXlsLinkTv);
            mMobcastXlsViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastXlsDetailFileInfoDetailTv);
            mMobcastXlsViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastXlsDetailFileInfoNameTv);
            
            mMobcastXlsReadView.setVisibility(View.INVISIBLE);
            
            mMobcastXlsRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    
    public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastFeedbackRootLayout;
    	
    	View mMobcastFeedbackReadView;
        ImageView mMobcastFeedbackIndicatorIv;
        ImageView mMobcastFeedbackLinkIv;
        
        AppCompatTextView mMobcastFeedbackTitleTv;
        AppCompatTextView mMobcastFeedbackByTv;
        AppCompatTextView mMobcastFeedbackLikeCountTv;
        AppCompatTextView mMobcastFeedbackViewCountTv;
        AppCompatTextView mMobcastFeedbackDetailQuestionCountTv;
        
        
        public FeedbackViewHolder(View view) {
            super(view);
            mMobcastFeedbackRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastFeedbackRootLayout);
            
            mMobcastFeedbackReadView = (View)view.findViewById(R.id.itemRecyclerMobcastFeedbackReadView);
            
            mMobcastFeedbackIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastFeedbackIndicatorImageView);
            mMobcastFeedbackLinkIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastFeedbackLinkTv);
            
            mMobcastFeedbackTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackTitleTv);
            mMobcastFeedbackByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackByTv);
            mMobcastFeedbackViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackViewCountTv);
            mMobcastFeedbackLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackLikeCountTv);
            mMobcastFeedbackDetailQuestionCountTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackDetailQuestionTv);
            
            mMobcastFeedbackReadView.setVisibility(View.INVISIBLE);
            
            mMobcastFeedbackRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
	public class FooterViewHolder extends RecyclerView.ViewHolder {
		ProgressWheel mFooterProgressWheel;

		public FooterViewHolder(View view) {
			super(view);
			mFooterProgressWheel = (ProgressWheel)view.findViewById(R.id.itemFooterProgressWheel);
		}
	}
    
    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mMobcastNewsRootLayout;
    	
    	View mMobcastNewsReadView;
        ImageView mMobcastNewsIndicatorIv;
        
        AppCompatTextView mMobcastNewsTitleTv;
        AppCompatTextView mMobcastNewsByTv;
        AppCompatTextView mMobcastNewsViewCountTv;
        AppCompatTextView mMobcastNewsDetailLinkTv;
        
        
        public NewsViewHolder(View view) {
            super(view);
            mMobcastNewsRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastNewsRootLayout);
            
            mMobcastNewsReadView = (View)view.findViewById(R.id.itemRecyclerMobcastNewsReadView);
            
            mMobcastNewsIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastNewsIndicatorImageView);
            
            mMobcastNewsTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastNewsTitleTv);
            mMobcastNewsByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastNewsByTv);
            mMobcastNewsViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastNewsViewCountTv);
            mMobcastNewsDetailLinkTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastNewsDetailLinkTv);
            
            mMobcastNewsReadView.setVisibility(View.INVISIBLE);
            
            mMobcastNewsRootLayout.setOnClickListener(this);
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
	private void processTextViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((TextViewHolder)viewHolder).mMobcastTextTitleTv.setText(mObj.getmTitle());
			((TextViewHolder)viewHolder).mMobcastTextByTv.setText(mObj.getmBy());
			((TextViewHolder)viewHolder).mMobcastTextViewCountTv.setText(mObj.getmViewCount());
			((TextViewHolder)viewHolder).mMobcastTextLikeCountTv.setText(mObj.getmLikeCount());
			((TextViewHolder)viewHolder).mMobcastTextSummaryTv.setText(mObj.getmDescription());
			if(!mObj.isRead()){
				((TextViewHolder)viewHolder).mMobcastTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_text_focused));
				((TextViewHolder)viewHolder).mMobcastTextIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((TextViewHolder)viewHolder).mMobcastTextTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((TextViewHolder)viewHolder).mMobcastTextReadView.setVisibility(View.VISIBLE);
				((TextViewHolder)viewHolder).mMobcastTextRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((TextViewHolder)viewHolder).mMobcastTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_text_normal));
				((TextViewHolder)viewHolder).mMobcastTextIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((TextViewHolder)viewHolder).mMobcastTextTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((TextViewHolder)viewHolder).mMobcastTextReadView.setVisibility(View.GONE);
				((TextViewHolder)viewHolder).mMobcastTextRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((TextViewHolder)viewHolder).mMobcastTextLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((TextViewHolder)viewHolder).mMobcastTextLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((TextViewHolder)viewHolder).mMobcastTextLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((TextViewHolder)viewHolder).mMobcastTextLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((TextViewHolder)viewHolder).mMobcastTextLinkTv.setVisibility(View.GONE);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processVideoViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((VideoViewHolder)viewHolder).mMobcastVideoTitleTv.setText(mObj.getmTitle());
			((VideoViewHolder)viewHolder).mMobcastVideoByTv.setText(mObj.getmBy());
			((VideoViewHolder)viewHolder).mMobcastVideoViewCountTv.setText(mObj.getmViewCount());
			((VideoViewHolder)viewHolder).mMobcastVideoLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((VideoViewHolder)viewHolder).mMobcastVideoIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_focused));
				((VideoViewHolder)viewHolder).mMobcastVideoIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((VideoViewHolder)viewHolder).mMobcastVideoTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((VideoViewHolder)viewHolder).mMobcastVideoReadView.setVisibility(View.VISIBLE);
				((VideoViewHolder)viewHolder).mMobcastVideoRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((VideoViewHolder)viewHolder).mMobcastVideoIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_normal));
				((VideoViewHolder)viewHolder).mMobcastVideoIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((VideoViewHolder)viewHolder).mMobcastVideoTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((VideoViewHolder)viewHolder).mMobcastVideoReadView.setVisibility(View.GONE);
				((VideoViewHolder)viewHolder).mMobcastVideoRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((VideoViewHolder)viewHolder).mMobcastVideoLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((VideoViewHolder)viewHolder).mMobcastVideoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((VideoViewHolder)viewHolder).mMobcastVideoLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((VideoViewHolder)viewHolder).mMobcastVideoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((VideoViewHolder)viewHolder).mMobcastVideoLinkTv.setVisibility(View.GONE);
			}
			
			final String mThumbnailPath = mObj.getmFileInfo().get(0).getmThumbnailPath();
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				((VideoViewHolder)viewHolder).mMobcastVideoThumbnailIv.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mObj.getmFileInfo().get(0).getmThumbnailLink(), ((VideoViewHolder)viewHolder).mMobcastVideoThumbnailIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mMobcastVideoLoadingProgress.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mMobcastVideoThumbnailIv.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mMobcastVideoPlayIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mMobcastVideoLoadingProgress.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mMobcastVideoThumbnailIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mMobcastVideoPlayIv.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mMobcastVideoLoadingProgress.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mMobcastVideoThumbnailIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mMobcastVideoPlayIv.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mMobcastVideoLoadingProgress.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mMobcastVideoThumbnailIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mMobcastVideoPlayIv.setVisibility(View.VISIBLE);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processLiveStreamViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveTitleTv.setText(mObj.getmTitle());
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveByTv.setText(mObj.getmBy());
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveViewCountTv.setText(mObj.getmViewCount());
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_focused));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveReadView.setVisibility(View.VISIBLE);
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_normal));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveReadView.setVisibility(View.GONE);
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLinkTv.setVisibility(View.GONE);
			}
			
//			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setImageURI(Uri.parse(mObj.getmFileInfo().get(0).getmThumbnailPath()));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processImageViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((ImageViewHolder)viewHolder).mMobcastImageTitleTv.setText(mObj.getmTitle());
			((ImageViewHolder)viewHolder).mMobcastImageByTv.setText(mObj.getmBy());
			((ImageViewHolder)viewHolder).mMobcastImageViewCountTv.setText(mObj.getmViewCount());
			((ImageViewHolder)viewHolder).mMobcastImageLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((ImageViewHolder)viewHolder).mMobcastImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_focus));
				((ImageViewHolder)viewHolder).mMobcastImageIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((ImageViewHolder)viewHolder).mMobcastImageTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((ImageViewHolder)viewHolder).mMobcastImageReadView.setVisibility(View.VISIBLE);
				((ImageViewHolder)viewHolder).mMobcastImageRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((ImageViewHolder)viewHolder).mMobcastImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_normal));
				((ImageViewHolder)viewHolder).mMobcastImageIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((ImageViewHolder)viewHolder).mMobcastImageTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((ImageViewHolder)viewHolder).mMobcastImageReadView.setVisibility(View.GONE);
				((ImageViewHolder)viewHolder).mMobcastImageRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((ImageViewHolder)viewHolder).mMobcastImageLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((ImageViewHolder)viewHolder).mMobcastImageLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((ImageViewHolder)viewHolder).mMobcastImageLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((ImageViewHolder)viewHolder).mMobcastImageLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((ImageViewHolder)viewHolder).mMobcastImageLinkTv.setVisibility(View.GONE);
			}
			
			final String mThumbnailPath = mObj.getmFileInfo().get(0).getmThumbnailPath();
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				((ImageViewHolder)viewHolder).mMobcastImageMainImageViewIv.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mObj.getmFileInfo().get(0).getmThumbnailLink(), ((ImageViewHolder)viewHolder).mMobcastImageMainImageViewIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mMobcastImageProgressWheel.setVisibility(View.VISIBLE);
						((ImageViewHolder)viewHolder).mMobcastImageMainImageViewIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mMobcastImageProgressWheel.setVisibility(View.GONE);
						((ImageViewHolder)viewHolder).mMobcastImageMainImageViewIv.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mMobcastImageProgressWheel.setVisibility(View.GONE);
						((ImageViewHolder)viewHolder).mMobcastImageMainImageViewIv.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mMobcastImageProgressWheel.setVisibility(View.GONE);
						((ImageViewHolder)viewHolder).mMobcastImageMainImageViewIv.setVisibility(View.VISIBLE);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processAudioViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((AudioViewHolder)viewHolder).mMobcastAudioTitleTv.setText(mObj.getmTitle());
			((AudioViewHolder)viewHolder).mMobcastAudioByTv.setText(mObj.getmBy());
			((AudioViewHolder)viewHolder).mMobcastAudioViewCountTv.setText(mObj.getmViewCount());
			((AudioViewHolder)viewHolder).mMobcastAudioLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((AudioViewHolder)viewHolder).mMobcastAudioIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_audio_focused));
				((AudioViewHolder)viewHolder).mMobcastAudioIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((AudioViewHolder)viewHolder).mMobcastAudioTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((AudioViewHolder)viewHolder).mMobcastAudioReadView.setVisibility(View.VISIBLE);
				((AudioViewHolder)viewHolder).mMobcastAudioRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((AudioViewHolder)viewHolder).mMobcastAudioIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_audio_normal));
				((AudioViewHolder)viewHolder).mMobcastAudioIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((AudioViewHolder)viewHolder).mMobcastAudioTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((AudioViewHolder)viewHolder).mMobcastAudioReadView.setVisibility(View.GONE);
				((AudioViewHolder)viewHolder).mMobcastAudioRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((AudioViewHolder)viewHolder).mMobcastAudioLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((AudioViewHolder)viewHolder).mMobcastAudioLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((AudioViewHolder)viewHolder).mMobcastAudioLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((AudioViewHolder)viewHolder).mMobcastAudioLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((AudioViewHolder)viewHolder).mMobcastAudioLinkTv.setVisibility(View.GONE);
			}
			
			((AudioViewHolder)viewHolder).mMobcastAudioSummaryTv.setText(mObj.getmDescription());
			((AudioViewHolder)viewHolder).mMobcastAudioViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
//			((AudioViewHolder)viewHolder).mMobcastAudioViewDurationTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processPdfViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((PdfViewHolder)viewHolder).mMobcastPdfTitleTv.setText(mObj.getmTitle());
			((PdfViewHolder)viewHolder).mMobcastPdfByTv.setText(mObj.getmBy());
			((PdfViewHolder)viewHolder).mMobcastPdfViewCountTv.setText(mObj.getmViewCount());
			((PdfViewHolder)viewHolder).mMobcastPdfLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((PdfViewHolder)viewHolder).mMobcastPdfIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_focused));
				((PdfViewHolder)viewHolder).mMobcastPdfIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((PdfViewHolder)viewHolder).mMobcastPdfTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((PdfViewHolder)viewHolder).mMobcastPdfReadView.setVisibility(View.VISIBLE);
				((PdfViewHolder)viewHolder).mMobcastPdfRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((PdfViewHolder)viewHolder).mMobcastPdfIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_normal));
				((PdfViewHolder)viewHolder).mMobcastPdfIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((PdfViewHolder)viewHolder).mMobcastPdfTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PdfViewHolder)viewHolder).mMobcastPdfReadView.setVisibility(View.GONE);
				((PdfViewHolder)viewHolder).mMobcastPdfRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((PdfViewHolder)viewHolder).mMobcastPdfLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PdfViewHolder)viewHolder).mMobcastPdfLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((PdfViewHolder)viewHolder).mMobcastPdfLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((PdfViewHolder)viewHolder).mMobcastPdfLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((PdfViewHolder)viewHolder).mMobcastPdfLinkTv.setVisibility(View.GONE);
			}
			
			((PdfViewHolder)viewHolder).mMobcastPdfViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((PdfViewHolder)viewHolder).mMobcastPdfViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processPptViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((PptViewHolder)viewHolder).mMobcastPptTitleTv.setText(mObj.getmTitle());
			((PptViewHolder)viewHolder).mMobcastPptByTv.setText(mObj.getmBy());
			((PptViewHolder)viewHolder).mMobcastPptViewCountTv.setText(mObj.getmViewCount());
			((PptViewHolder)viewHolder).mMobcastPptLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((PptViewHolder)viewHolder).mMobcastPptIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_focused));
				((PptViewHolder)viewHolder).mMobcastPptIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((PptViewHolder)viewHolder).mMobcastPptTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((PptViewHolder)viewHolder).mMobcastPptReadView.setVisibility(View.VISIBLE);
				((PptViewHolder)viewHolder).mMobcastPptRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((PptViewHolder)viewHolder).mMobcastPptIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_normal));
				((PptViewHolder)viewHolder).mMobcastPptIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((PptViewHolder)viewHolder).mMobcastPptTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PptViewHolder)viewHolder).mMobcastPptReadView.setVisibility(View.GONE);
				((PptViewHolder)viewHolder).mMobcastPptRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((PptViewHolder)viewHolder).mMobcastPptLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PptViewHolder)viewHolder).mMobcastPptLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((PptViewHolder)viewHolder).mMobcastPptLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((PptViewHolder)viewHolder).mMobcastPptLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((PptViewHolder)viewHolder).mMobcastPptLinkTv.setVisibility(View.GONE);
			}
			
			((PptViewHolder)viewHolder).mMobcastPptViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((PptViewHolder)viewHolder).mMobcastPptViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processDocViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((DocViewHolder)viewHolder).mMobcastDocTitleTv.setText(mObj.getmTitle());
			((DocViewHolder)viewHolder).mMobcastDocByTv.setText(mObj.getmBy());
			((DocViewHolder)viewHolder).mMobcastDocViewCountTv.setText(mObj.getmViewCount());
			((DocViewHolder)viewHolder).mMobcastDocLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((DocViewHolder)viewHolder).mMobcastDocIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_doc_focus));
				((DocViewHolder)viewHolder).mMobcastDocIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((DocViewHolder)viewHolder).mMobcastDocTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((DocViewHolder)viewHolder).mMobcastDocReadView.setVisibility(View.VISIBLE);
				((DocViewHolder)viewHolder).mMobcastDocRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((DocViewHolder)viewHolder).mMobcastDocIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_doc_normal));
				((DocViewHolder)viewHolder).mMobcastDocIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((DocViewHolder)viewHolder).mMobcastDocTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((DocViewHolder)viewHolder).mMobcastDocReadView.setVisibility(View.GONE);
				((DocViewHolder)viewHolder).mMobcastDocRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((DocViewHolder)viewHolder).mMobcastDocLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((DocViewHolder)viewHolder).mMobcastDocLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((DocViewHolder)viewHolder).mMobcastDocLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((DocViewHolder)viewHolder).mMobcastDocLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((DocViewHolder)viewHolder).mMobcastDocLinkTv.setVisibility(View.GONE);
			}
			
			((DocViewHolder)viewHolder).mMobcastDocViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((DocViewHolder)viewHolder).mMobcastDocViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processXlsViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((XlsViewHolder)viewHolder).mMobcastXlsTitleTv.setText(mObj.getmTitle());
			((XlsViewHolder)viewHolder).mMobcastXlsByTv.setText(mObj.getmBy());
			((XlsViewHolder)viewHolder).mMobcastXlsViewCountTv.setText(mObj.getmViewCount());
			((XlsViewHolder)viewHolder).mMobcastXlsLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((XlsViewHolder)viewHolder).mMobcastXlsIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_xls_focused));
				((XlsViewHolder)viewHolder).mMobcastXlsIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((XlsViewHolder)viewHolder).mMobcastXlsTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((XlsViewHolder)viewHolder).mMobcastXlsReadView.setVisibility(View.VISIBLE);
				((XlsViewHolder)viewHolder).mMobcastXlsRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((XlsViewHolder)viewHolder).mMobcastXlsIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_xls_normal));
				((XlsViewHolder)viewHolder).mMobcastXlsIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((XlsViewHolder)viewHolder).mMobcastXlsTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((XlsViewHolder)viewHolder).mMobcastXlsReadView.setVisibility(View.GONE);
				((XlsViewHolder)viewHolder).mMobcastXlsRootLayout.setBackgroundResource(R.color.white);
			}
			
			if(!mObj.isLike()){
				((XlsViewHolder)viewHolder).mMobcastXlsLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((XlsViewHolder)viewHolder).mMobcastXlsLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((XlsViewHolder)viewHolder).mMobcastXlsLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((XlsViewHolder)viewHolder).mMobcastXlsLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((XlsViewHolder)viewHolder).mMobcastXlsLinkTv.setVisibility(View.GONE);
			}
			
			((XlsViewHolder)viewHolder).mMobcastXlsViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((XlsViewHolder)viewHolder).mMobcastXlsViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processFeedbackViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Mobcast mObj = mArrayListMobcast.get(position);
			((FeedbackViewHolder)viewHolder).mMobcastFeedbackTitleTv.setText(mObj.getmTitle());
			((FeedbackViewHolder)viewHolder).mMobcastFeedbackByTv.setText(mObj.getmBy());
			((FeedbackViewHolder)viewHolder).mMobcastFeedbackLinkIv.setVisibility(View.GONE);
			((FeedbackViewHolder)viewHolder).mMobcastFeedbackViewCountTv.setText(mObj.getmViewCount());
			((FeedbackViewHolder)viewHolder).mMobcastFeedbackLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_feedback_focused));
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackReadView.setVisibility(View.VISIBLE);
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_feedback_normal));
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackReadView.setVisibility(View.GONE);
				((FeedbackViewHolder)viewHolder).mMobcastFeedbackRootLayout.setBackgroundResource(R.color.white);
			}
			((FeedbackViewHolder)viewHolder).mMobcastFeedbackDetailQuestionCountTv.setText(mObj.getmFileInfo().get(0).getmPages());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
