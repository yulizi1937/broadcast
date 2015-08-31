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

import com.application.beans.Training;
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

    public TrainingRecyclerAdapter(Context context, ArrayList<Training> mArrayListTraining, View headerView,boolean isGrid) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mArrayListTraining = mArrayListTraining;
        this.isGrid = isGrid;
        mHeaderView = headerView;
        mImageLoader = ApplicationLoader.getUILImageLoader();
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
    		return  new InteractiveViewHolder(mInflater.inflate(R.layout.item_recycler_training_interactive, parent, false));
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
		} else if (viewHolder instanceof QuizViewHolder) {
			processQuizViewHolder(viewHolder, position);
		} else if (viewHolder instanceof YoutubeLiveStreamViewHolder) {
			processLiveStreamViewHolder(viewHolder, position);
		}else if (viewHolder instanceof InteractiveViewHolder) {
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
        ImageView mTrainingQuizIndicatorIv;
        
        AppCompatTextView mTrainingQuizTitleTv;
        AppCompatTextView mTrainingQuizByTv;
        AppCompatTextView mTrainingQuizViewCountTv;
        AppCompatTextView mTrainingQuizDetailQuestionCountTv;
        AppCompatTextView mTrainingQuizDetailTimerTv;
        AppCompatTextView mTrainingQuizDetailAttemptedTv;
        AppCompatTextView mTrainingQuizDetailScoreTv;
        
        
        public QuizViewHolder(View view) {
            super(view);
            mTrainingQuizRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingQuizRootLayout);
            
            mTrainingQuizReadView = (View)view.findViewById(R.id.itemRecyclerTrainingQuizReadView);
            
            mTrainingQuizIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingQuizIndicatorImageView);
            
            mTrainingQuizTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizTitleTv);
            mTrainingQuizByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizByTv);
            mTrainingQuizViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingQuizViewCountTv);
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
    	FrameLayout mTrainingInteractiveRootLayout;
    	
    	View mTrainingInteractiveReadView;
        ImageView mTrainingInteractiveIndicatorIv;
        
        AppCompatTextView mTrainingInteractiveTitleTv;
        AppCompatTextView mTrainingInteractiveByTv;
        AppCompatTextView mTrainingInteractiveViewCountTv;
        AppCompatTextView mTrainingInteractiveDetailLinkTv;
        
        
        public InteractiveViewHolder(View view) {
            super(view);
            mTrainingInteractiveRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingInteractiveRootLayout);
            
            mTrainingInteractiveReadView = (View)view.findViewById(R.id.itemRecyclerTrainingInteractiveReadView);
            
            mTrainingInteractiveIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingInteractiveIndicatorImageView);
            
            mTrainingInteractiveTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingInteractiveTitleTv);
            mTrainingInteractiveByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingInteractiveByTv);
            mTrainingInteractiveViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingInteractiveViewCountTv);
            mTrainingInteractiveDetailLinkTv= (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingInteractiveDetailLinkTv);
            
            mTrainingInteractiveRootLayout.setOnClickListener(this);
            mTrainingInteractiveRootLayout.setOnLongClickListener(this);
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
				((TextViewHolder)viewHolder).mTrainingTextIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((TextViewHolder)viewHolder).mTrainingTextTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((TextViewHolder)viewHolder).mTrainingTextReadView.setVisibility(View.VISIBLE);
				((TextViewHolder)viewHolder).mTrainingTextRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((TextViewHolder)viewHolder).mTrainingTextIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_text_normal));
				((TextViewHolder)viewHolder).mTrainingTextIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((TextViewHolder)viewHolder).mTrainingTextTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((TextViewHolder)viewHolder).mTrainingTextReadView.setVisibility(View.GONE);
				((TextViewHolder)viewHolder).mTrainingTextRootLayout.setBackgroundResource(R.color.white);
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
			}
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
				((VideoViewHolder)viewHolder).mTrainingVideoIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_focused));
				((VideoViewHolder)viewHolder).mTrainingVideoIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((VideoViewHolder)viewHolder).mTrainingVideoTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((VideoViewHolder)viewHolder).mTrainingVideoReadView.setVisibility(View.VISIBLE);
				((VideoViewHolder)viewHolder).mTrainingVideoRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((VideoViewHolder)viewHolder).mTrainingVideoIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_video_normal));
				((VideoViewHolder)viewHolder).mTrainingVideoIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((VideoViewHolder)viewHolder).mTrainingVideoTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((VideoViewHolder)viewHolder).mTrainingVideoReadView.setVisibility(View.GONE);
				((VideoViewHolder)viewHolder).mTrainingVideoRootLayout.setBackgroundResource(R.color.white);
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
			}
			
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
			Training mObj = mArrayListTraining.get(position);
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
			Training mObj = mArrayListTraining.get(position);
			((ImageViewHolder)viewHolder).mTrainingImageTitleTv.setText(mObj.getmTitle());
			((ImageViewHolder)viewHolder).mTrainingImageByTv.setText(mObj.getmBy());
			((ImageViewHolder)viewHolder).mTrainingImageViewCountTv.setText(mObj.getmViewCount());
			((ImageViewHolder)viewHolder).mTrainingImageLikeCountTv.setText(mObj.getmLikeCount());
			if(!mObj.isRead()){
				((ImageViewHolder)viewHolder).mTrainingImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_focus));
				((ImageViewHolder)viewHolder).mTrainingImageIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((ImageViewHolder)viewHolder).mTrainingImageTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((ImageViewHolder)viewHolder).mTrainingImageReadView.setVisibility(View.VISIBLE);
				((ImageViewHolder)viewHolder).mTrainingImageRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((ImageViewHolder)viewHolder).mTrainingImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_normal));
				((ImageViewHolder)viewHolder).mTrainingImageIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((ImageViewHolder)viewHolder).mTrainingImageTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((ImageViewHolder)viewHolder).mTrainingImageReadView.setVisibility(View.GONE);
				((ImageViewHolder)viewHolder).mTrainingImageRootLayout.setBackgroundResource(R.color.white);
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
			}
			
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
				((AudioViewHolder)viewHolder).mTrainingAudioIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((AudioViewHolder)viewHolder).mTrainingAudioTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((AudioViewHolder)viewHolder).mTrainingAudioReadView.setVisibility(View.VISIBLE);
				((AudioViewHolder)viewHolder).mTrainingAudioRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((AudioViewHolder)viewHolder).mTrainingAudioIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_audio_normal));
				((AudioViewHolder)viewHolder).mTrainingAudioIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((AudioViewHolder)viewHolder).mTrainingAudioTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((AudioViewHolder)viewHolder).mTrainingAudioReadView.setVisibility(View.GONE);
				((AudioViewHolder)viewHolder).mTrainingAudioRootLayout.setBackgroundResource(R.color.white);
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
			}
			
			((AudioViewHolder)viewHolder).mTrainingAudioSummaryTv.setText(mObj.getmDescription());
			((AudioViewHolder)viewHolder).mTrainingAudioViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
//			((AudioViewHolder)viewHolder).mTrainingAudioViewDurationTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
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
				((PdfViewHolder)viewHolder).mTrainingPdfIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_focused));
				((PdfViewHolder)viewHolder).mTrainingPdfIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((PdfViewHolder)viewHolder).mTrainingPdfTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((PdfViewHolder)viewHolder).mTrainingPdfReadView.setVisibility(View.VISIBLE);
				((PdfViewHolder)viewHolder).mTrainingPdfRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((PdfViewHolder)viewHolder).mTrainingPdfIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_normal));
				((PdfViewHolder)viewHolder).mTrainingPdfIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((PdfViewHolder)viewHolder).mTrainingPdfTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PdfViewHolder)viewHolder).mTrainingPdfReadView.setVisibility(View.GONE);
				((PdfViewHolder)viewHolder).mTrainingPdfRootLayout.setBackgroundResource(R.color.white);
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
			}
			
			((PdfViewHolder)viewHolder).mTrainingPdfViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((PdfViewHolder)viewHolder).mTrainingPdfViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
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
				((PptViewHolder)viewHolder).mTrainingPptIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((PptViewHolder)viewHolder).mTrainingPptTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((PptViewHolder)viewHolder).mTrainingPptReadView.setVisibility(View.VISIBLE);
				((PptViewHolder)viewHolder).mTrainingPptRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((PptViewHolder)viewHolder).mTrainingPptIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_ppt_normal));
				((PptViewHolder)viewHolder).mTrainingPptIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((PptViewHolder)viewHolder).mTrainingPptTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((PptViewHolder)viewHolder).mTrainingPptReadView.setVisibility(View.GONE);
				((PptViewHolder)viewHolder).mTrainingPptRootLayout.setBackgroundResource(R.color.white);
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
			}
			
			((PptViewHolder)viewHolder).mTrainingPptViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((PptViewHolder)viewHolder).mTrainingPptViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
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
				((DocViewHolder)viewHolder).mTrainingDocIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((DocViewHolder)viewHolder).mTrainingDocTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((DocViewHolder)viewHolder).mTrainingDocReadView.setVisibility(View.VISIBLE);
				((DocViewHolder)viewHolder).mTrainingDocRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((DocViewHolder)viewHolder).mTrainingDocIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_doc_normal));
				((DocViewHolder)viewHolder).mTrainingDocIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((DocViewHolder)viewHolder).mTrainingDocTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((DocViewHolder)viewHolder).mTrainingDocReadView.setVisibility(View.GONE);
				((DocViewHolder)viewHolder).mTrainingDocRootLayout.setBackgroundResource(R.color.white);
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
			}
			
			((DocViewHolder)viewHolder).mTrainingDocViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((DocViewHolder)viewHolder).mTrainingDocViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
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
				((XlsViewHolder)viewHolder).mTrainingXlsIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((XlsViewHolder)viewHolder).mTrainingXlsTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((XlsViewHolder)viewHolder).mTrainingXlsReadView.setVisibility(View.VISIBLE);
				((XlsViewHolder)viewHolder).mTrainingXlsRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((XlsViewHolder)viewHolder).mTrainingXlsIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_xls_normal));
				((XlsViewHolder)viewHolder).mTrainingXlsIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((XlsViewHolder)viewHolder).mTrainingXlsTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((XlsViewHolder)viewHolder).mTrainingXlsReadView.setVisibility(View.GONE);
				((XlsViewHolder)viewHolder).mTrainingXlsRootLayout.setBackgroundResource(R.color.white);
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
			}
			
			((XlsViewHolder)viewHolder).mTrainingXlsViewFileNameTv.setText(mObj.getmFileInfo().get(0).getmFileName());
			((XlsViewHolder)viewHolder).mTrainingXlsViewFileMetaTv.setText(mObj.getmFileInfo().get(0).getmPages());
			
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
			if(!mObj.isRead()){
				((QuizViewHolder)viewHolder).mTrainingQuizIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_feedback_focused));
				((QuizViewHolder)viewHolder).mTrainingQuizIndicatorIv.setBackgroundColor(mContext.getResources().getColor(R.color.unread_background));
				((QuizViewHolder)viewHolder).mTrainingQuizTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
				((QuizViewHolder)viewHolder).mTrainingQuizReadView.setVisibility(View.VISIBLE);
				((QuizViewHolder)viewHolder).mTrainingQuizRootLayout.setBackgroundResource(R.color.unread_background);
			}else{
				((QuizViewHolder)viewHolder).mTrainingQuizIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_feedback_normal));
				((QuizViewHolder)viewHolder).mTrainingQuizIndicatorIv.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				((QuizViewHolder)viewHolder).mTrainingQuizTitleTv.setTextColor(mContext.getResources().getColor(R.color.toolbar_background));
				((QuizViewHolder)viewHolder).mTrainingQuizReadView.setVisibility(View.GONE);
				((QuizViewHolder)viewHolder).mTrainingQuizRootLayout.setBackgroundResource(R.color.white);
			}
			((QuizViewHolder)viewHolder).mTrainingQuizDetailQuestionCountTv.setText(mObj.getmFileInfo().get(0).getmPages());
			((QuizViewHolder)viewHolder).mTrainingQuizDetailTimerTv.setText(mObj.getmFileInfo().get(0).getmDuration());
			((QuizViewHolder)viewHolder).mTrainingQuizDetailScoreTv.setText(mObj.getmFileInfo().get(0).getmQuestions());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
    
}
