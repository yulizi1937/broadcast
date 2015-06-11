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
import android.widget.ImageView;

import com.application.beans.Training;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.utils.AppConstants;
import com.mobcast.R;

public class TrainingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
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

    private LayoutInflater mInflater;
    private ArrayList<Training> mArrayListTraining;
    private View mHeaderView;
    public OnItemClickListener mItemClickListener;

    public TrainingRecyclerAdapter(Context context, ArrayList<Training> mArrayListTraining, View headerView) {
        mInflater = LayoutInflater.from(context);
        this.mArrayListTraining = mArrayListTraining;
        mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mArrayListTraining.size();
        } else {
            return mArrayListTraining.size() + 1;
        }
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
    	}{
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
    	default:
    		return new HeaderViewHolder(mHeaderView);
    	}
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof TextViewHolder) {
			// ((TextViewHolder)
			// viewHolder).textView.setText(mArrayListTraining.get(position -
			// 1).getmTitle());
		} else if (viewHolder instanceof ImageViewHolder) {
		} else if (viewHolder instanceof VideoViewHolder) {
		} else if (viewHolder instanceof AudioViewHolder) {
		} else if (viewHolder instanceof PdfViewHolder) {
		} else if (viewHolder instanceof DocViewHolder) {
		} else if (viewHolder instanceof PptViewHolder) {
		} else if (viewHolder instanceof XlsViewHolder) {
		} else if (viewHolder instanceof QuizViewHolder) {
		} else if (viewHolder instanceof InteractiveViewHolder) {
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
    	FrameLayout mTrainingTextRootLayout;
    	
    	View mTrainingTextReadView;
        ImageView mTrainingTextIndicatorIv;
        
        AppCompatTextView mTrainingTextTitleTv;
        AppCompatTextView mTrainingTextByTv;
        AppCompatTextView mTrainingTextViewCountTv;
        AppCompatTextView mTrainingTextSummaryTv;
        
        public TextViewHolder(View view) {
            super(view);
            mTrainingTextRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingTextRootLayout);
            
            mTrainingTextReadView = (View)view.findViewById(R.id.itemRecyclerTrainingTextReadView);
            
            mTrainingTextIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingTextIndicatorImageView);
            
            mTrainingTextTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextTitleTv);
            mTrainingTextByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextByTv);
            mTrainingTextViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextViewCountTv);
            mTrainingTextSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingTextSummaryTv);
            
            mTrainingTextRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    
    
	public class ImageViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
    	FrameLayout mTrainingImageRootLayout;
    	
    	View mTrainingImageReadView;
        ImageView mTrainingImageIndicatorIv;
        ImageView mTrainingImageViewPagerNextIv;
        ImageView mTrainingImageViewPagerPrevIv;
        ImageView mTrainingImageMainImageViewIv;
        
        AppCompatTextView mTrainingImageTitleTv;
        AppCompatTextView mTrainingImageByTv;
        AppCompatTextView mTrainingImageViewCountTv;
        
        
        public ImageViewHolder(View view) {
            super(view);
            mTrainingImageRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerTrainingImageRootLayout);
            
            mTrainingImageReadView = (View)view.findViewById(R.id.itemRecyclerTrainingImageReadView);
            
            mTrainingImageIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingImageIndicatorImageView);
            mTrainingImageViewPagerNextIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingImageViewPagerNextIv);
            mTrainingImageViewPagerPrevIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingImageViewPagerPreviousIv);
            
            mTrainingImageMainImageViewIv = (ImageView)view.findViewById(R.id.itemRecyclerTrainingImageMainImageIv);
            
            mTrainingImageTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageTitleTv);
            mTrainingImageByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageByTv);
            mTrainingImageViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingImageViewCountTv);
            
            mTrainingImageRootLayout.setOnClickListener(this);
            
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
	public class VideoViewHolder extends RecyclerView.ViewHolder implements
			View.OnClickListener {
    	FrameLayout mTrainingVideoRootLayout;
    	
    	View mTrainingVideoReadView;
        ImageView mTrainingVideoIndicatorIv;
        ImageView mTrainingVideoThumbnailIv;
        ImageView mTrainingVideoPlayIv;
        
        AppCompatTextView mTrainingVideoTitleTv;
        AppCompatTextView mTrainingVideoByTv;
        AppCompatTextView mTrainingVideoViewCountTv;
        AppCompatTextView mTrainingVideoViewDurationTv;
        
        
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
            mTrainingVideoViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingVideoDurationTv);
            
            mTrainingVideoRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mTrainingAudioRootLayout;
    	
    	View mTrainingAudioReadView;
        ImageView mTrainingAudioIndicatorIv;
        ImageView mTrainingAudioFileTypeIv;
        
        AppCompatTextView mTrainingAudioTitleTv;
        AppCompatTextView mTrainingAudioByTv;
        AppCompatTextView mTrainingAudioViewCountTv;
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
            mTrainingAudioViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioDetailFileInfoDetailTv);
            mTrainingAudioViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioDetailFileInfoNameTv);
            mTrainingAudioSummaryTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingAudioSummaryTv);
            
            mTrainingAudioRootLayout.setOnClickListener(this);
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public  class PdfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mTrainingPdfRootLayout;
    	
    	View mTrainingPdfReadView;
        ImageView mTrainingPdfIndicatorIv;
        ImageView mTrainingPdfFileTypeIv;
        
        AppCompatTextView mTrainingPdfTitleTv;
        AppCompatTextView mTrainingPdfByTv;
        AppCompatTextView mTrainingPdfViewCountTv;
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
            mTrainingPdfViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfDetailFileInfoDetailTv);
            mTrainingPdfViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPdfDetailFileInfoNameTv);
            
            mTrainingPdfRootLayout.setOnClickListener(this);
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mTrainingDocRootLayout;
    	
    	View mTrainingDocReadView;
        ImageView mTrainingDocIndicatorIv;
        ImageView mTrainingDocFileTypeIv;
        
        AppCompatTextView mTrainingDocTitleTv;
        AppCompatTextView mTrainingDocByTv;
        AppCompatTextView mTrainingDocViewCountTv;
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
            mTrainingDocViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocDetailFileInfoDetailTv);
            mTrainingDocViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingDocDetailFileInfoNameTv);
            
            mTrainingDocRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
   public class PptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mTrainingPptRootLayout;
    	
    	View mTrainingPptReadView;
        ImageView mTrainingPptIndicatorIv;
        ImageView mTrainingPptFileTypeIv;
        
        AppCompatTextView mTrainingPptTitleTv;
        AppCompatTextView mTrainingPptByTv;
        AppCompatTextView mTrainingPptViewCountTv;
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
            mTrainingPptViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptDetailFileInfoDetailTv);
            mTrainingPptViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingPptDetailFileInfoNameTv);
            
            mTrainingPptRootLayout.setOnClickListener(this);
        }
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public class XlsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    	FrameLayout mTrainingXlsRootLayout;
    	
    	View mTrainingXlsReadView;
        ImageView mTrainingXlsIndicatorIv;
        ImageView mTrainingXlsFileTypeIv;
        
        AppCompatTextView mTrainingXlsTitleTv;
        AppCompatTextView mTrainingXlsByTv;
        AppCompatTextView mTrainingXlsViewCountTv;
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
            mTrainingXlsViewFileMetaTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsDetailFileInfoDetailTv);
            mTrainingXlsViewFileNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerTrainingXlsDetailFileInfoNameTv);
            
            mTrainingXlsRootLayout.setOnClickListener(this);
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    
    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
        }
        
        public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
    }
    
    public class InteractiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
}
