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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Mobcast;
import com.application.beans.Training;
import com.application.ui.adapter.MobcastRecyclerAdapter.AudioViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.ImageViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.InteractiveViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.TextViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.VideoViewHolder;
import com.application.ui.adapter.MobcastRecyclerAdapter.YoutubeLiveStreamViewHolder;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TrainingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = TrainingRecyclerAdapter.class.getSimpleName();
	
    private static final int VIEW_TYPE_HEADER     = 0;
    private static final int VIEW_TYPE_TEXT       = 1;
    private static final int VIEW_TYPE_INTERACTIVE= 2;
    private static final int VIEW_TYPE_IMAGE      = 3;
    private static final int VIEW_TYPE_VIDEO      = 4;
    private static final int VIEW_TYPE_AUDIO      = 5;
    private static final int VIEW_TYPE_DOC        = 6;
    private static final int VIEW_TYPE_PDF        = 7;
    private static final int VIEW_TYPE_PPT        = 8;
    private static final int VIEW_TYPE_XLS        = 9;
    private static final int VIEW_TYPE_QUIZ       = 10;
    private static final int VIEW_TYPE_STREAM     = 11;
    private static final int VIEW_TYPE_FOOTER     = 12;

    private LayoutInflater mInflater;
    private ArrayList<Training> mArrayListTraining;
    private View mHeaderView;
    private boolean isGrid = false;
    public OnItemClickListenerT mItemClickListener;
    public OnItemLongClickListenerT mItemLongClickListener;
    private Context mContext;
    private ImageLoader mImageLoader;
    private int whichTheme = 0;
    
    public TrainingRecyclerAdapter(Context context, ArrayList<Training> mArrayListTraining, View headerView,boolean isGrid) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mArrayListTraining = mArrayListTraining;
        this.isGrid = isGrid;
        mHeaderView = headerView;
        mImageLoader = ApplicationLoader.getUILImageLoader();
        whichTheme = ApplicationLoader.getPreferences().getAppTheme();
    }

    @Override
    public int getItemCount() {
    	 if (mHeaderView == null) {
             return mArrayListTraining.size();
         } else {
         	if(!isGrid){
         		return mArrayListTraining.size() + 1;	
         	}else{
         		return mArrayListTraining.size() + 2;
         	}
             
         }
    }
    
    public void addTrainingObjList(ArrayList<Training> mListTraining){
    	mArrayListTraining = mListTraining;
    	notifyDataSetChanged();
    }
    
    public void updateTrainingObj(int position, ArrayList<Training> mListTraining){
    	mArrayListTraining = mListTraining;
    	notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
    	if(!isGrid){
    		switch(position){
        	case 0:
        		return VIEW_TYPE_HEADER;
        	default:
        		return getItemTypeFromObject(position-1);
        	}
    	}else{
    		switch(position){
        	case 0:
        		return VIEW_TYPE_HEADER;
        	case 1:
        		return VIEW_TYPE_HEADER;
        	default:
        		return getItemTypeFromObject(position-2);
        	}
    	}
//        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }
    
    public int getItemTypeFromObject(int position){
    	if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.TEXT)){
    		return VIEW_TYPE_TEXT;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.INTERACTIVE)){
    		return VIEW_TYPE_INTERACTIVE;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.IMAGE)){
    		return VIEW_TYPE_IMAGE;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.VIDEO)){
    		return VIEW_TYPE_VIDEO;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.AUDIO)){
    		return VIEW_TYPE_AUDIO;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.DOC)){
    		return VIEW_TYPE_DOC;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.PDF)){
    		return VIEW_TYPE_PDF;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.PPT)){
    		return VIEW_TYPE_PPT;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.QUIZ)){
    		return VIEW_TYPE_QUIZ;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.TRAINING.STREAM)){
    		return VIEW_TYPE_STREAM;
    	}else if(mArrayListTraining.get(position).getmFileType().equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)){
    		return VIEW_TYPE_FOOTER;
    	}else{
    		return VIEW_TYPE_XLS;
    	}
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	switch(viewType){
    	case VIEW_TYPE_TEXT:
    		return  new TextViewHolder(mInflater.inflate(R.layout.item_recycler_training_text, parent, false));
    	case VIEW_TYPE_IMAGE:
    		return  new ImageViewHolder(mInflater.inflate(R.layout.item_recycler_training_image, parent, false));
    	case VIEW_TYPE_VIDEO:
    		return  new VideoViewHolder(mInflater.inflate(R.layout.item_recycler_training_video, parent, false));
    	case VIEW_TYPE_AUDIO:
    		return  new AudioViewHolder(mInflater.inflate(R.layout.item_recycler_training_audio, parent, false));
    	case VIEW_TYPE_PDF:
    		return  new PdfViewHolder(mInflater.inflate(R.layout.item_recycler_training_pdf, parent, false));
    	case VIEW_TYPE_DOC:
    		return  new DocViewHolder(mInflater.inflate(R.layout.item_recycler_training_doc, parent, false));
    	case VIEW_TYPE_PPT:
    		return  new PptViewHolder(mInflater.inflate(R.layout.item_recycler_training_ppt, parent, false));
    	case VIEW_TYPE_XLS:
    		return  new XlsViewHolder(mInflater.inflate(R.layout.item_recycler_training_xls, parent, false));
    	case VIEW_TYPE_QUIZ:
    		return  new QuizViewHolder(mInflater.inflate(R.layout.item_recycler_training_quiz, parent, false));
    	case VIEW_TYPE_INTERACTIVE:
    		return  new InteractiveViewHolder(mInflater.inflate(R.layout.item_recycler_mobcast_interactive, parent, false));
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
    	position = isGrid ? position - 2 : position - 1;
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
		} else if (viewHolder instanceof QuizViewHolder) {
			processQuizViewHolder(viewHolder, position);
		} else if (viewHolder instanceof YoutubeLiveStreamViewHolder) {
			processLiveStreamViewHolder(viewHolder, position);
		}else if (viewHolder instanceof InteractiveViewHolder) {
			processInteractiveViewHolder(viewHolder, position);
		}
    }
    
	public interface OnItemClickListenerT {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListenerT mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	public interface OnItemLongClickListenerT {
		public void onItemLongClick(View view, int position);
	}

	public void setOnItemLongClickListener(
			final OnItemLongClickListenerT mItemLongClickListener) {
		this.mItemLongClickListener = mItemLongClickListener;
	}

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    
    public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingTextRootLayout;
    	
    	View mTrainingTextReadView;
    	View mTrainingTextLineView;
        ImageView mTrainingTextIndicatorIv;
        
        AppCompatTextView mTrainingTextTitleTv;
        AppCompatTextView mTrainingTextByTv;
        AppCompatTextView mTrainingTextViewCountTv;
        AppCompatTextView mTrainingTextLikeCountTv;
        ImageView mTrainingTextLinkTv;
        AppCompatTextView mTrainingTextSummaryTv;
        
        public TextViewHolder(View view) {
            super(view);
            mTrainingTextRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingTextRootLayout);
            
            mTrainingTextReadView = (View)view.findViewById(R.id.itemRecyclerTrainingTextReadView);
            mTrainingTextLineView = (View)view.findViewById(R.id.itemRecyclerTrainingTextLineView);
            
            mTrainingTextIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingTextIndicatorImageView);
            
            mTrainingTextTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextTitleTv);
            mTrainingTextByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextByTv);
            mTrainingTextViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextViewCountTv);
            mTrainingTextLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextLikeCountTv);
            mTrainingTextLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingTextLinkTv);
            mTrainingTextSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextSummaryTv);
            
            mTrainingTextRootLayout.setOnClickListener(this);
            mTrainingTextRootLayout.setOnLongClickListener(this);
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
    
    
    
	public class ImageViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {
    	FrameLayout mTrainingImageRootLayout;
    	
    	View mTrainingImageReadView;
    	View mTrainingImageLineView;
        ImageView mTrainingImageIndicatorIv;
        ImageView mTrainingImageMainImageViewIv;
        
        AppCompatTextView mTrainingImageTitleTv;
        AppCompatTextView mTrainingImageByTv;
        AppCompatTextView mTrainingImageViewCountTv;
        AppCompatTextView mTrainingImageLikeCountTv;
        ImageView mTrainingImageLinkTv;
        
        ProgressWheel mTrainingImageProgressWheel;
        
        
        public ImageViewHolder(View view) {
            super(view);
            Log.i(TAG,"ImageViewHolder");
            mTrainingImageRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingImageRootLayout);
            
            mTrainingImageReadView = (View)view.findViewById(R.id.itemRecyclerTrainingImageReadView);
            mTrainingImageLineView = (View)view.findViewById(R.id.itemRecyclerTrainingImageLineView);
            
            mTrainingImageIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingImageIndicatorImageView);
            
            mTrainingImageMainImageViewIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingImageMainImageIv);
            
            mTrainingImageTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageTitleTv);
            mTrainingImageByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageByTv);
            mTrainingImageViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageViewCountTv);
            mTrainingImageLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageLikeCountTv);
            mTrainingImageLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingImageLinkTv);
            
            mTrainingImageProgressWheel = (ProgressWheel)view.findViewById(R.id.itemRecyclerTrainingImageLoadingProgress);
            
            mTrainingImageReadView.setVisibility(View.INVISIBLE);
            
            mTrainingImageRootLayout.setOnClickListener(this);
            mTrainingImageRootLayout.setOnLongClickListener(this);
            
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
    
	public class VideoViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener, View.OnLongClickListener {
    	FrameLayout mTrainingVideoRootLayout;
    	
    	View mTrainingVideoReadView;
    	View mTrainingVideoLineView;
        ImageView mTrainingVideoIndicatorIv;
        ImageView mTrainingVideoThumbnailIv;
        ImageView mTrainingVideoPlayIv;
        
        AppCompatTextView mTrainingVideoTitleTv;
        AppCompatTextView mTrainingVideoByTv;
        AppCompatTextView mTrainingVideoViewCountTv;
        AppCompatTextView mTrainingVideoLikeCountTv;
        ImageView mTrainingVideoLinkTv;
        AppCompatTextView mTrainingVideoViewDurationTv;
        ProgressWheel mTrainingVideoLoadingProgress;
        
        
        public VideoViewHolder(View view) {
            super(view);
            mTrainingVideoRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingVideoRootLayout);
            
            mTrainingVideoReadView = (View)view.findViewById(R.id.itemRecyclerTrainingVideoReadView);
            mTrainingVideoLineView = (View)view.findViewById(R.id.itemRecyclerTrainingVideoLineView);
            
            mTrainingVideoIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingVideoIndicatorImageView);
            
            mTrainingVideoThumbnailIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingVideoThumbnailImageIv);
            mTrainingVideoPlayIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingVideoPlayImageIv);
            
            mTrainingVideoTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingVideoTitleTv);
            mTrainingVideoByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingVideoByTv);
            mTrainingVideoViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingVideoViewCountTv);
            mTrainingVideoLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingVideoLikeCountTv);
            mTrainingVideoLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingVideoLinkTv);
            mTrainingVideoViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingVideoDurationTv);
            mTrainingVideoLoadingProgress = (ProgressWheel)view.findViewById(R.id.itemRecyclerTrainingVideoLoadingProgress);
            
            mTrainingVideoRootLayout.setOnClickListener(this);
            mTrainingVideoRootLayout.setOnLongClickListener(this);
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
    
    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingAudioRootLayout;
    	
    	View mTrainingAudioReadView;
    	View mTrainingAudioLineView;
        ImageView mTrainingAudioIndicatorIv;
        ImageView mTrainingAudioFileTypeIv;
        
        AppCompatTextView mTrainingAudioTitleTv;
        AppCompatTextView mTrainingAudioByTv;
        AppCompatTextView mTrainingAudioViewCountTv;
        AppCompatTextView mTrainingAudioLikeCountTv;
        ImageView mTrainingAudioLinkTv;
        AppCompatTextView mTrainingAudioViewDurationTv;
        AppCompatTextView mTrainingAudioViewFileNameTv;
        AppCompatTextView mTrainingAudioSummaryTv;
        
        
        public AudioViewHolder(View view) {
            super(view);
            mTrainingAudioRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingAudioRootLayout);
            
            mTrainingAudioReadView = (View)view.findViewById(R.id.itemRecyclerTrainingAudioReadView);
            mTrainingAudioLineView = (View)view.findViewById(R.id.itemRecyclerTrainingAudioLineView);
            
            mTrainingAudioIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingAudioIndicatorImageView);
            mTrainingAudioFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingAudioDetailFileTypeIv);
            
            mTrainingAudioTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioTitleTv);
            mTrainingAudioByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioByTv);
            mTrainingAudioViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioViewCountTv);
            mTrainingAudioLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioLikeCountTv);
            mTrainingAudioLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingAudioLinkTv);
            mTrainingAudioViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioDetailFileInfoDetailTv);
            mTrainingAudioViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioDetailFileInfoNameTv);
            mTrainingAudioSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioSummaryTv);
            
            mTrainingAudioRootLayout.setOnClickListener(this);
            mTrainingAudioRootLayout.setOnLongClickListener(this);
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
    
    public  class PdfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingPdfRootLayout;
    	
    	View mTrainingPdfReadView;
    	View mTrainingPdfLineView;
        ImageView mTrainingPdfIndicatorIv;
        ImageView mTrainingPdfFileTypeIv;
        
        AppCompatTextView mTrainingPdfTitleTv;
        AppCompatTextView mTrainingPdfByTv;
        AppCompatTextView mTrainingPdfViewCountTv;
        AppCompatTextView mTrainingPdfLikeCountTv;
        ImageView mTrainingPdfLinkTv;
        AppCompatTextView mTrainingPdfViewFileMetaTv;
        AppCompatTextView mTrainingPdfViewFileNameTv;
        
        
        public PdfViewHolder(View view) {
            super(view);
            mTrainingPdfRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingPdfRootLayout);
            
            mTrainingPdfReadView = (View)view.findViewById(R.id.itemRecyclerTrainingPdfReadView);
            mTrainingPdfLineView = (View)view.findViewById(R.id.itemRecyclerTrainingPdfLineView);
            
            mTrainingPdfIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingPdfIndicatorImageView);
            mTrainingPdfFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingPdfDetailFileTypeIv);
            
            mTrainingPdfTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfTitleTv);
            mTrainingPdfByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfByTv);
            mTrainingPdfViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfViewCountTv);
            mTrainingPdfLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfLikeCountTv);
            mTrainingPdfLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingPdfLinkTv);
            mTrainingPdfViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfDetailFileInfoDetailTv);
            mTrainingPdfViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfDetailFileInfoNameTv);
            
            mTrainingPdfRootLayout.setOnClickListener(this);
            mTrainingPdfRootLayout.setOnLongClickListener(this);
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
    
    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingDocRootLayout;
    	
    	View mTrainingDocReadView;
    	View mTrainingDocLineView;
        ImageView mTrainingDocIndicatorIv;
        ImageView mTrainingDocFileTypeIv;
        
        AppCompatTextView mTrainingDocTitleTv;
        AppCompatTextView mTrainingDocByTv;
        AppCompatTextView mTrainingDocViewCountTv;
        AppCompatTextView mTrainingDocLikeCountTv;
        ImageView mTrainingDocLinkTv;
        AppCompatTextView mTrainingDocViewFileMetaTv;
        AppCompatTextView mTrainingDocViewFileNameTv;
        
        
        public DocViewHolder(View view) {
            super(view);
            mTrainingDocRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingDocRootLayout);
            
            mTrainingDocReadView = (View)view.findViewById(R.id.itemRecyclerTrainingDocReadView);
            mTrainingDocLineView = (View)view.findViewById(R.id.itemRecyclerTrainingDocLineView);
            
            mTrainingDocIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingDocIndicatorImageView);
            mTrainingDocFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingDocDetailFileTypeIv);
            
            mTrainingDocTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocTitleTv);
            mTrainingDocByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocByTv);
            mTrainingDocViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocViewCountTv);
            mTrainingDocLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocLikeCountTv);
            mTrainingDocLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingDocLinkTv);
            mTrainingDocViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocDetailFileInfoDetailTv);
            mTrainingDocViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocDetailFileInfoNameTv);
            
            mTrainingDocRootLayout.setOnClickListener(this);
            mTrainingDocRootLayout.setOnLongClickListener(this);
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
    
   public class PptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingPptRootLayout;
    	
    	View mTrainingPptReadView;
    	View mTrainingPptLineView;
        ImageView mTrainingPptIndicatorIv;
        ImageView mTrainingPptFileTypeIv;
        
        AppCompatTextView mTrainingPptTitleTv;
        AppCompatTextView mTrainingPptByTv;
        AppCompatTextView mTrainingPptViewCountTv;
        AppCompatTextView mTrainingPptLikeCountTv;
        ImageView mTrainingPptLinkTv;
        AppCompatTextView mTrainingPptViewFileMetaTv;
        AppCompatTextView mTrainingPptViewFileNameTv;
        
        
        public PptViewHolder(View view) {
            super(view);
            mTrainingPptRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingPptRootLayout);
            
            mTrainingPptReadView = (View)view.findViewById(R.id.itemRecyclerTrainingPptReadView);
            mTrainingPptLineView = (View)view.findViewById(R.id.itemRecyclerTrainingPptLineView);
            
            mTrainingPptIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingPptIndicatorImageView);
            mTrainingPptFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingPptDetailFileTypeIv);
            
            mTrainingPptTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptTitleTv);
            mTrainingPptByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptByTv);
            mTrainingPptViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptViewCountTv);
            mTrainingPptLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptLikeCountTv);
            mTrainingPptLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingPptLinkTv);
            mTrainingPptViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptDetailFileInfoDetailTv);
            mTrainingPptViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptDetailFileInfoNameTv);
            
            mTrainingPptRootLayout.setOnClickListener(this);
            mTrainingPptRootLayout.setOnLongClickListener(this);
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
    
    public class XlsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingXlsRootLayout;
    	
    	View mTrainingXlsReadView;
    	View mTrainingXlsLineView;
        ImageView mTrainingXlsIndicatorIv;
        ImageView mTrainingXlsFileTypeIv;
        
        AppCompatTextView mTrainingXlsTitleTv;
        AppCompatTextView mTrainingXlsByTv;
        AppCompatTextView mTrainingXlsViewCountTv;
        AppCompatTextView mTrainingXlsLikeCountTv;
        ImageView mTrainingXlsLinkTv;
        AppCompatTextView mTrainingXlsViewFileMetaTv;
        AppCompatTextView mTrainingXlsViewFileNameTv;
        
        
        public XlsViewHolder(View view) {
            super(view);
            mTrainingXlsRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingXlsRootLayout);
            
            mTrainingXlsReadView = (View)view.findViewById(R.id.itemRecyclerTrainingXlsReadView);
            mTrainingXlsLineView = (View)view.findViewById(R.id.itemRecyclerTrainingXlsLineView);
            
            mTrainingXlsIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingXlsIndicatorImageView);
            mTrainingXlsFileTypeIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingXlsDetailFileTypeIv);
            
            mTrainingXlsTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsTitleTv);
            mTrainingXlsByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsByTv);
            mTrainingXlsViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsViewCountTv);
            mTrainingXlsLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsLikeCountTv);
            mTrainingXlsLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerTrainingXlsLinkTv);
            mTrainingXlsViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsDetailFileInfoDetailTv);
            mTrainingXlsViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsDetailFileInfoNameTv);
            
            mTrainingXlsRootLayout.setOnClickListener(this);
            mTrainingXlsRootLayout.setOnLongClickListener(this);
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
    
    
    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mTrainingQuizRootLayout;
    	
    	View mTrainingQuizReadView;
    	View mTrainingQuizLineView;	
        ImageView mTrainingQuizIndicatorIv;
        
        AppCompatTextView mTrainingQuizTitleTv;
        AppCompatTextView mTrainingQuizByTv;
        AppCompatTextView mTrainingQuizViewCountTv;
        AppCompatTextView mTrainingQuizLikeCountTv;
        AppCompatTextView mTrainingQuizDetailQuestionCountTv;
        AppCompatTextView mTrainingQuizDetailTimerTv;
        AppCompatTextView mTrainingQuizDetailAttemptedTv;
        AppCompatTextView mTrainingQuizDetailScoreTv;
        
        
        public QuizViewHolder(View view) {
            super(view);
            mTrainingQuizRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingQuizRootLayout);
            
            mTrainingQuizReadView = (View)view.findViewById(R.id.itemRecyclerTrainingQuizReadView);
            mTrainingQuizLineView = (View)view.findViewById(R.id.itemRecyclerTrainingQuizLineView);
            
            mTrainingQuizIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingQuizIndicatorImageView);
            
            mTrainingQuizTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizTitleTv);
            mTrainingQuizByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizByTv);
            mTrainingQuizViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizViewCountTv);
            mTrainingQuizLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizLikeCountTv);
            mTrainingQuizDetailQuestionCountTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizDetailQuestionTv);
            mTrainingQuizDetailTimerTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizDetailTimeTv);
            mTrainingQuizDetailAttemptedTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizDetailAttemptedTv);
            mTrainingQuizDetailScoreTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizDetailPointsTv);
            
            mTrainingQuizRootLayout.setOnClickListener(this);
            mTrainingQuizRootLayout.setOnLongClickListener(this);
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
    
    public class InteractiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    	FrameLayout mMobcastInteractiveRootLayout;
    	
    	View mMobcastInteractiveReadView;
    	View mMobcastInteractiveLineView;
        ImageView mMobcastInteractiveIndicatorIv;
        
        AppCompatTextView mMobcastInteractiveTitleTv;
        AppCompatTextView mMobcastInteractiveByTv;
        AppCompatTextView mMobcastInteractiveViewCountTv;
        AppCompatTextView mMobcastInteractiveLikeCountTv;
        ImageView mMobcastInteractiveLinkTv;
        AppCompatTextView mMobcastInteractiveSummaryTv;
        
        public InteractiveViewHolder(View view) {
            super(view);
            mMobcastInteractiveRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastInteractiveRootLayout);
            
            mMobcastInteractiveReadView = (View)view.findViewById(R.id.itemRecyclerMobcastInteractiveReadView);
            mMobcastInteractiveLineView = (View)view.findViewById(R.id.itemRecyclerMobcastInteractiveLineView);
            
            mMobcastInteractiveIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastInteractiveIndicatorImageView);
            
            mMobcastInteractiveTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastInteractiveTitleTv);
            mMobcastInteractiveByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastInteractiveByTv);
            mMobcastInteractiveViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastInteractiveViewCountTv);
            mMobcastInteractiveLikeCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastInteractiveLikeCountTv);
            mMobcastInteractiveLinkTv = (ImageView) view.findViewById(R.id.itemRecyclerMobcastInteractiveLinkTv);
            mMobcastInteractiveSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastInteractiveSummaryTv);
            
            mMobcastInteractiveReadView.setVisibility(View.INVISIBLE);
            
            mMobcastInteractiveRootLayout.setOnClickListener(this);
            mMobcastInteractiveRootLayout.setOnLongClickListener(this);
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
    
	public class YoutubeLiveStreamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		FrameLayout mMobcastLiveRootLayout;

		View mMobcastLiveReadView;
		View mMobcastLiveLineView;
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
			mMobcastLiveLineView = (View)view.findViewById(R.id.itemRecyclerMobcastLiveLineView);
    
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
			mMobcastLiveRootLayout.setOnLongClickListener(this);
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
			Training mObj = mArrayListTraining.get(position);
			((TextViewHolder)viewHolder).mTrainingTextTitleTv.setText(mObj.getmTitle());
			((TextViewHolder)viewHolder).mTrainingTextByTv.setText(mObj.getmBy());
			((TextViewHolder)viewHolder).mTrainingTextViewCountTv.setText(mObj.getmViewCount());
			((TextViewHolder)viewHolder).mTrainingTextLikeCountTv.setText(mObj.getmLikeCount());
			((TextViewHolder)viewHolder).mTrainingTextSummaryTv.setText(mObj.getmDescription());
			if(!mObj.isRead()){
				((TextViewHolder)viewHolder).mTrainingTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_text_focused));
			}else{
				((TextViewHolder)viewHolder).mTrainingTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_text_normal));
			}
			
			if(!mObj.isLike()){
				((TextViewHolder)viewHolder).mTrainingTextLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((TextViewHolder)viewHolder).mTrainingTextLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((TextViewHolder)viewHolder).mTrainingTextLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((TextViewHolder)viewHolder).mTrainingTextLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((TextViewHolder)viewHolder).mTrainingTextLinkTv.setVisibility(View.GONE);
			}else{
				((TextViewHolder)viewHolder).mTrainingTextLinkTv.setVisibility(View.VISIBLE);
			}
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((TextViewHolder) viewHolder).mTrainingTextReadView,
					((TextViewHolder) viewHolder).mTrainingTextLineView,
					((TextViewHolder) viewHolder).mTrainingTextRootLayout,
					((TextViewHolder) viewHolder).mTrainingTextTitleTv,
					((TextViewHolder) viewHolder).mTrainingTextByTv,
					((TextViewHolder) viewHolder).mTrainingTextIndicatorIv,
					AppConstants.TYPE.TEXT, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processVideoViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((VideoViewHolder)viewHolder).mTrainingVideoTitleTv.setText(mObj.getmTitle());
			((VideoViewHolder)viewHolder).mTrainingVideoByTv.setText(mObj.getmBy());
			((VideoViewHolder)viewHolder).mTrainingVideoViewCountTv.setText(mObj.getmViewCount());
			((VideoViewHolder)viewHolder).mTrainingVideoLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((VideoViewHolder)viewHolder).mTrainingVideoIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_live_stream_focused));
			}else{
				((VideoViewHolder)viewHolder).mTrainingVideoIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_live_stream_normal));
			}
			
			if(!mObj.isLike()){
				((VideoViewHolder)viewHolder).mTrainingVideoLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((VideoViewHolder)viewHolder).mTrainingVideoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((VideoViewHolder)viewHolder).mTrainingVideoLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((VideoViewHolder)viewHolder).mTrainingVideoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((VideoViewHolder)viewHolder).mTrainingVideoLinkTv.setVisibility(View.GONE);
			}else{
				((VideoViewHolder)viewHolder).mTrainingVideoLinkTv.setVisibility(View.VISIBLE);
			}
			
			if(!TextUtils.isEmpty(mObj.getmFileInfo().get(0).getmDuration())){
				((VideoViewHolder)viewHolder).mTrainingVideoViewDurationTv.setText(mObj.getmFileInfo().get(0).getmDuration());
			}
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((VideoViewHolder) viewHolder).mTrainingVideoReadView,
					((VideoViewHolder) viewHolder).mTrainingVideoLineView,
					((VideoViewHolder) viewHolder).mTrainingVideoRootLayout,
					((VideoViewHolder) viewHolder).mTrainingVideoTitleTv,
					((VideoViewHolder) viewHolder).mTrainingVideoByTv,
					((VideoViewHolder) viewHolder).mTrainingVideoIndicatorIv,
					AppConstants.TYPE.VIDEO, mObj.isRead());
			
			final String mThumbnailPath = mObj.getmFileInfo().get(0).getmThumbnailPath();
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mObj.getmFileInfo().get(0).getmThumbnailLink(), ((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mTrainingVideoLoadingProgress.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mTrainingVideoPlayIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mTrainingVideoLoadingProgress.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mTrainingVideoPlayIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setImageResource(R.drawable.placeholder_icon);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mTrainingVideoLoadingProgress.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mTrainingVideoPlayIv.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((VideoViewHolder)viewHolder).mTrainingVideoLoadingProgress.setVisibility(View.GONE);
						((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mTrainingVideoPlayIv.setVisibility(View.VISIBLE);
						((VideoViewHolder)viewHolder).mTrainingVideoThumbnailIv.setImageResource(R.drawable.placeholder_icon);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processLiveStreamViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveTitleTv.setText(mObj.getmTitle());
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveByTv.setText(mObj.getmBy());
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveViewCountTv.setText(mObj.getmViewCount());
			((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_focused));
			}else{
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_normal));
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
			}else{
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveLinkTv.setVisibility(View.VISIBLE);
			}
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((YoutubeLiveStreamViewHolder) viewHolder).mMobcastLiveReadView,
					((YoutubeLiveStreamViewHolder) viewHolder).mMobcastLiveLineView,
					((YoutubeLiveStreamViewHolder) viewHolder).mMobcastLiveRootLayout,
					((YoutubeLiveStreamViewHolder) viewHolder).mMobcastLiveTitleTv,
					((YoutubeLiveStreamViewHolder) viewHolder).mMobcastLiveByTv,
					((YoutubeLiveStreamViewHolder) viewHolder).mMobcastLiveIndicatorIv,
					AppConstants.TYPE.STREAM, mObj.isRead());
			
			final String mThumbnailPath = mObj.getmFileInfo().get(0).getmThumbnailPath();
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mObj.getmFileInfo().get(0).getmThumbnailLink(),((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setVisibility(View.VISIBLE);
						((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setImageResource(R.drawable.livestream_thumbnail);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setVisibility(View.VISIBLE);
						((YoutubeLiveStreamViewHolder)viewHolder).mMobcastLiveThumbnailIv.setImageResource(R.drawable.livestream_thumbnail);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processImageViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((ImageViewHolder)viewHolder).mTrainingImageTitleTv.setText(mObj.getmTitle());
			((ImageViewHolder)viewHolder).mTrainingImageByTv.setText(mObj.getmBy());
			((ImageViewHolder)viewHolder).mTrainingImageViewCountTv.setText(mObj.getmViewCount());
			((ImageViewHolder)viewHolder).mTrainingImageLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((ImageViewHolder)viewHolder).mTrainingImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_focus));
			}else{
				((ImageViewHolder)viewHolder).mTrainingImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_normal));
			}
			
			if(!mObj.isLike()){
				((ImageViewHolder)viewHolder).mTrainingImageLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((ImageViewHolder)viewHolder).mTrainingImageLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((ImageViewHolder)viewHolder).mTrainingImageLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((ImageViewHolder)viewHolder).mTrainingImageLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((ImageViewHolder)viewHolder).mTrainingImageLinkTv.setVisibility(View.GONE);
			}else{
				((ImageViewHolder)viewHolder).mTrainingImageLinkTv.setVisibility(View.VISIBLE);
			}
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((ImageViewHolder) viewHolder).mTrainingImageReadView,
					((ImageViewHolder) viewHolder).mTrainingImageLineView,
					((ImageViewHolder) viewHolder).mTrainingImageRootLayout,
					((ImageViewHolder) viewHolder).mTrainingImageTitleTv,
					((ImageViewHolder) viewHolder).mTrainingImageByTv,
					((ImageViewHolder) viewHolder).mTrainingImageIndicatorIv,
					AppConstants.TYPE.IMAGE, mObj.isRead());
			
			final String mThumbnailPath = mObj.getmFileInfo().get(0).getmThumbnailPath();
			if(Utilities.checkIfFileExists(mThumbnailPath)){
//				mImageLoader.displayImage("file:///"+mThumbnailPath, ((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv);
				((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mObj.getmFileInfo().get(0).getmThumbnailLink(), ((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mTrainingImageProgressWheel.setVisibility(View.VISIBLE);
						((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mTrainingImageProgressWheel.setVisibility(View.GONE);
						((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setVisibility(View.VISIBLE);
						((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setImageResource(R.drawable.placeholder_icon);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mTrainingImageProgressWheel.setVisibility(View.GONE);
						((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						((ImageViewHolder)viewHolder).mTrainingImageProgressWheel.setVisibility(View.GONE);
						((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setVisibility(View.VISIBLE);
						((ImageViewHolder)viewHolder).mTrainingImageMainImageViewIv.setImageResource(R.drawable.placeholder_icon);
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
			Training mObj = mArrayListTraining.get(position);
			((AudioViewHolder)viewHolder).mTrainingAudioTitleTv.setText(mObj.getmTitle());
			((AudioViewHolder)viewHolder).mTrainingAudioByTv.setText(mObj.getmBy());
			((AudioViewHolder)viewHolder).mTrainingAudioViewCountTv.setText(mObj.getmViewCount());
			((AudioViewHolder)viewHolder).mTrainingAudioLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((AudioViewHolder)viewHolder).mTrainingAudioIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_audio_focused));
			}else{
				((AudioViewHolder)viewHolder).mTrainingAudioIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_audio_normal));
			}
			
			if(!mObj.isLike()){
				((AudioViewHolder)viewHolder).mTrainingAudioLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((AudioViewHolder)viewHolder).mTrainingAudioLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((AudioViewHolder)viewHolder).mTrainingAudioLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((AudioViewHolder)viewHolder).mTrainingAudioLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((AudioViewHolder)viewHolder).mTrainingAudioLinkTv.setVisibility(View.GONE);
			}else{
				((AudioViewHolder)viewHolder).mTrainingAudioLinkTv.setVisibility(View.VISIBLE);
			}
			
			if(!TextUtils.isEmpty(mObj.getmFileInfo().get(0).getmDuration())){
				((AudioViewHolder)viewHolder).mTrainingAudioViewDurationTv.setText(mObj.getmFileInfo().get(0).getmDuration());
			}
			
			((AudioViewHolder)viewHolder).mTrainingAudioSummaryTv.setText(mObj.getmDescription());
			((AudioViewHolder)viewHolder).mTrainingAudioViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
//			((AudioViewHolder)viewHolder).mTrainingAudioViewDurationTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((AudioViewHolder) viewHolder).mTrainingAudioReadView,
					((AudioViewHolder) viewHolder).mTrainingAudioLineView,
					((AudioViewHolder) viewHolder).mTrainingAudioRootLayout,
					((AudioViewHolder) viewHolder).mTrainingAudioTitleTv,
					((AudioViewHolder) viewHolder).mTrainingAudioByTv,
					((AudioViewHolder) viewHolder).mTrainingAudioIndicatorIv,
					AppConstants.TYPE.AUDIO, mObj.isRead());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processPdfViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((PdfViewHolder)viewHolder).mTrainingPdfTitleTv.setText(mObj.getmTitle());
			((PdfViewHolder)viewHolder).mTrainingPdfByTv.setText(mObj.getmBy());
			((PdfViewHolder)viewHolder).mTrainingPdfViewCountTv.setText(mObj.getmViewCount());
			((PdfViewHolder)viewHolder).mTrainingPdfLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((PdfViewHolder)viewHolder).mTrainingPdfIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_pdf_focused));
			}else{
				((PdfViewHolder)viewHolder).mTrainingPdfIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_pdf_normal));
			}
			
			if(!mObj.isLike()){
				((PdfViewHolder)viewHolder).mTrainingPdfLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PdfViewHolder)viewHolder).mTrainingPdfLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((PdfViewHolder)viewHolder).mTrainingPdfLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((PdfViewHolder)viewHolder).mTrainingPdfLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((PdfViewHolder)viewHolder).mTrainingPdfLinkTv.setVisibility(View.GONE);
			}else{
				((PdfViewHolder)viewHolder).mTrainingPdfLinkTv.setVisibility(View.VISIBLE);
			}
			
			((PdfViewHolder)viewHolder).mTrainingPdfViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((PdfViewHolder)viewHolder).mTrainingPdfViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((PdfViewHolder) viewHolder).mTrainingPdfReadView,
					((PdfViewHolder) viewHolder).mTrainingPdfLineView,
					((PdfViewHolder) viewHolder).mTrainingPdfRootLayout,
					((PdfViewHolder) viewHolder).mTrainingPdfTitleTv,
					((PdfViewHolder) viewHolder).mTrainingPdfByTv,
					((PdfViewHolder) viewHolder).mTrainingPdfIndicatorIv,
					AppConstants.TYPE.PDF, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processPptViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((PptViewHolder)viewHolder).mTrainingPptTitleTv.setText(mObj.getmTitle());
			((PptViewHolder)viewHolder).mTrainingPptByTv.setText(mObj.getmBy());
			((PptViewHolder)viewHolder).mTrainingPptViewCountTv.setText(mObj.getmViewCount());
			((PptViewHolder)viewHolder).mTrainingPptLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((PptViewHolder)viewHolder).mTrainingPptIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_focused));
			}else{
				((PptViewHolder)viewHolder).mTrainingPptIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_normal));
			}
			
			if(!mObj.isLike()){
				((PptViewHolder)viewHolder).mTrainingPptLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PptViewHolder)viewHolder).mTrainingPptLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((PptViewHolder)viewHolder).mTrainingPptLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((PptViewHolder)viewHolder).mTrainingPptLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((PptViewHolder)viewHolder).mTrainingPptLinkTv.setVisibility(View.GONE);
			}else{
				((PptViewHolder)viewHolder).mTrainingPptLinkTv.setVisibility(View.VISIBLE);
			}
			
			((PptViewHolder)viewHolder).mTrainingPptViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((PptViewHolder)viewHolder).mTrainingPptViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((PptViewHolder) viewHolder).mTrainingPptReadView,
					((PptViewHolder) viewHolder).mTrainingPptLineView,
					((PptViewHolder) viewHolder).mTrainingPptRootLayout,
					((PptViewHolder) viewHolder).mTrainingPptTitleTv,
					((PptViewHolder) viewHolder).mTrainingPptByTv,
					((PptViewHolder) viewHolder).mTrainingPptIndicatorIv,
					AppConstants.TYPE.PPT, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processDocViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((DocViewHolder)viewHolder).mTrainingDocTitleTv.setText(mObj.getmTitle());
			((DocViewHolder)viewHolder).mTrainingDocByTv.setText(mObj.getmBy());
			((DocViewHolder)viewHolder).mTrainingDocViewCountTv.setText(mObj.getmViewCount());
			((DocViewHolder)viewHolder).mTrainingDocLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((DocViewHolder)viewHolder).mTrainingDocIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_doc_focus));
			}else{
				((DocViewHolder)viewHolder).mTrainingDocIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_doc_normal));
			}
			
			if(!mObj.isLike()){
				((DocViewHolder)viewHolder).mTrainingDocLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((DocViewHolder)viewHolder).mTrainingDocLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((DocViewHolder)viewHolder).mTrainingDocLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((DocViewHolder)viewHolder).mTrainingDocLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((DocViewHolder)viewHolder).mTrainingDocLinkTv.setVisibility(View.GONE);
			}else{
				((DocViewHolder)viewHolder).mTrainingDocLinkTv.setVisibility(View.VISIBLE);
			}
			
			((DocViewHolder)viewHolder).mTrainingDocViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((DocViewHolder)viewHolder).mTrainingDocViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((DocViewHolder) viewHolder).mTrainingDocReadView,
					((DocViewHolder) viewHolder).mTrainingDocLineView,
					((DocViewHolder) viewHolder).mTrainingDocRootLayout,
					((DocViewHolder) viewHolder).mTrainingDocTitleTv,
					((DocViewHolder) viewHolder).mTrainingDocByTv,
					((DocViewHolder) viewHolder).mTrainingDocIndicatorIv,
					AppConstants.TYPE.DOC, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processXlsViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((XlsViewHolder)viewHolder).mTrainingXlsTitleTv.setText(mObj.getmTitle());
			((XlsViewHolder)viewHolder).mTrainingXlsByTv.setText(mObj.getmBy());
			((XlsViewHolder)viewHolder).mTrainingXlsViewCountTv.setText(mObj.getmViewCount());
			((XlsViewHolder)viewHolder).mTrainingXlsLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((XlsViewHolder)viewHolder).mTrainingXlsIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_xls_focused));
			}else{
				((XlsViewHolder)viewHolder).mTrainingXlsIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_xls_normal));
			}
			
			if(!mObj.isLike()){
				((XlsViewHolder)viewHolder).mTrainingXlsLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((XlsViewHolder)viewHolder).mTrainingXlsLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((XlsViewHolder)viewHolder).mTrainingXlsLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((XlsViewHolder)viewHolder).mTrainingXlsLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((XlsViewHolder)viewHolder).mTrainingXlsLinkTv.setVisibility(View.GONE);
			}else{
				((XlsViewHolder)viewHolder).mTrainingXlsLinkTv.setVisibility(View.VISIBLE);
			}
			
			((XlsViewHolder)viewHolder).mTrainingXlsViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((XlsViewHolder)viewHolder).mTrainingXlsViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((XlsViewHolder) viewHolder).mTrainingXlsReadView,
					((XlsViewHolder) viewHolder).mTrainingXlsLineView,
					((XlsViewHolder) viewHolder).mTrainingXlsRootLayout,
					((XlsViewHolder) viewHolder).mTrainingXlsTitleTv,
					((XlsViewHolder) viewHolder).mTrainingXlsByTv,
					((XlsViewHolder) viewHolder).mTrainingXlsIndicatorIv,
					AppConstants.TYPE.XLS, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processInteractiveViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((InteractiveViewHolder)viewHolder).mMobcastInteractiveTitleTv.setText(mObj.getmTitle());
			((InteractiveViewHolder)viewHolder).mMobcastInteractiveByTv.setText(mObj.getmBy());
			((InteractiveViewHolder)viewHolder).mMobcastInteractiveViewCountTv.setText(mObj.getmViewCount());
			((InteractiveViewHolder)viewHolder).mMobcastInteractiveLikeCountTv.setText(mObj.getmLikeCount());
			((InteractiveViewHolder)viewHolder).mMobcastInteractiveSummaryTv.setText(mObj.getmDescription());
			if(!mObj.isRead()){
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_interactive_read_focused));
			}else{
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_interactive_read_normal));
			}
			
			if(!mObj.isLike()){
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			if(TextUtils.isEmpty(mObj.getmLink())){
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveLinkTv.setVisibility(View.GONE);
			}else{
				((InteractiveViewHolder)viewHolder).mMobcastInteractiveLinkTv.setVisibility(View.VISIBLE);
			}
			
			ThemeUtils.applyThemeItemMobcast(mContext, whichTheme,
					((InteractiveViewHolder) viewHolder).mMobcastInteractiveReadView,
					((InteractiveViewHolder) viewHolder).mMobcastInteractiveLineView,
					((InteractiveViewHolder) viewHolder).mMobcastInteractiveRootLayout,
					((InteractiveViewHolder) viewHolder).mMobcastInteractiveTitleTv,
					((InteractiveViewHolder) viewHolder).mMobcastInteractiveByTv,
					((InteractiveViewHolder) viewHolder).mMobcastInteractiveIndicatorIv,
					AppConstants.TYPE.INTERACTIVE, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processQuizViewHolder(RecyclerView.ViewHolder viewHolder, int position){
		try{
			Training mObj = mArrayListTraining.get(position);
			((QuizViewHolder)viewHolder).mTrainingQuizTitleTv.setText(mObj.getmTitle());
			((QuizViewHolder)viewHolder).mTrainingQuizByTv.setText(mObj.getmBy());
			((QuizViewHolder)viewHolder).mTrainingQuizViewCountTv.setText(mObj.getmViewCount());
			((QuizViewHolder)viewHolder).mTrainingQuizLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((QuizViewHolder)viewHolder).mTrainingQuizIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_quiz_focused));
			}else{
				((QuizViewHolder)viewHolder).mTrainingQuizIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_quiz_normal));
			}
			((QuizViewHolder)viewHolder).mTrainingQuizDetailQuestionCountTv.setText(mObj.getmFileInfo().get(0).getmPages());
			((QuizViewHolder)viewHolder).mTrainingQuizDetailTimerTv.setText(mObj.getmFileInfo().get(0).getmDuration());
			((QuizViewHolder)viewHolder).mTrainingQuizDetailScoreTv.setText(mObj.getmFileInfo().get(0).getmQuestions());
			if(Integer.parseInt(mObj.getmFileInfo().get(0).getmAttempts()) > 1){
				((QuizViewHolder)viewHolder).mTrainingQuizDetailAttemptedTv.setText(mObj.getmFileInfo().get(0).getmAttempts() +" "+ mContext.getResources().getString(R.string.item_recycler_training_quiz_attempts));
			}else{
				((QuizViewHolder)viewHolder).mTrainingQuizDetailAttemptedTv.setText(mObj.getmFileInfo().get(0).getmAttempts() +" "+ mContext.getResources().getString(R.string.item_recycler_training_quiz_attempt));	
			}
			
			
			if(!mObj.isLike()){
				((QuizViewHolder)viewHolder).mTrainingQuizLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((QuizViewHolder)viewHolder).mTrainingQuizLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like, 0, 0, 0);
			}else{
				((QuizViewHolder)viewHolder).mTrainingQuizLikeCountTv.setTextColor(mContext.getResources().getColor(R.color.red));
				((QuizViewHolder)viewHolder).mTrainingQuizLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmap_item_like_done, 0, 0, 0);
			}
			
			ThemeUtils.applyThemeItemTraining(mContext, whichTheme,
					((QuizViewHolder) viewHolder).mTrainingQuizReadView,
					((QuizViewHolder) viewHolder).mTrainingQuizLineView,
					((QuizViewHolder) viewHolder).mTrainingQuizRootLayout,
					((QuizViewHolder) viewHolder).mTrainingQuizTitleTv,
					((QuizViewHolder) viewHolder).mTrainingQuizByTv,
					((QuizViewHolder) viewHolder).mTrainingQuizIndicatorIv,
					AppConstants.TYPE.QUIZ, mObj.isRead());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
    
}
