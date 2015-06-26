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

import com.application.beans.Mobcast;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MaterialRippleLayout.RippleBuilder;
import com.application.utils.AppConstants;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mobcast.R;

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

    private LayoutInflater mInflater;
    private ArrayList<Mobcast> mArrayListMobcast;
    private View mHeaderView;
    public OnItemClickListener mItemClickListener;
    private Context mContext;

    public MobcastRecyclerAdapter(Context context, ArrayList<Mobcast> mArrayListMobcast, View headerView) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mArrayListMobcast = mArrayListMobcast;
        mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mArrayListMobcast.size();
        } else {
            return mArrayListMobcast.size() + 1;
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
    	default:
    		return new HeaderViewHolder(mHeaderView);
    	}
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof TextViewHolder) {
//			setAnimation(((TextViewHolder)
//					viewHolder).mMobcastTextMaterialRootLayout);
		} else if (viewHolder instanceof ImageViewHolder) {
			((ImageViewHolder)viewHolder).mMobcastImageRootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.background_unread));
			((ImageViewHolder)viewHolder).mMobcastImageTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_highlight));
			((ImageViewHolder)viewHolder).mMobcastImageIndicatorIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mobcast_image_focus));
		} else if (viewHolder instanceof VideoViewHolder) {
		} else if (viewHolder instanceof AudioViewHolder) {
		} else if (viewHolder instanceof PdfViewHolder) {
		} else if (viewHolder instanceof DocViewHolder) {
		} else if (viewHolder instanceof PptViewHolder) {
		} else if (viewHolder instanceof XlsViewHolder) {
		} else if (viewHolder instanceof FeedbackViewHolder) {
		} else if (viewHolder instanceof NewsViewHolder) {
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
        AppCompatTextView mMobcastTextSummaryTv;
        
        public TextViewHolder(View view) {
            super(view);
            mMobcastTextRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastTextRootLayout);
            
            mMobcastTextReadView = (View)view.findViewById(R.id.itemRecyclerMobcastTextReadView);
            
            mMobcastTextIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastTextIndicatorImageView);
            
            mMobcastTextTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextTitleTv);
            mMobcastTextByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextByTv);
            mMobcastTextViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastTextViewCountTv);
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
        ImageView mMobcastImageViewPagerNextIv;
        ImageView mMobcastImageViewPagerPrevIv;
        ImageView mMobcastImageMainImageViewIv;
        
        AppCompatTextView mMobcastImageTitleTv;
        AppCompatTextView mMobcastImageByTv;
        AppCompatTextView mMobcastImageViewCountTv;
        
        
        public ImageViewHolder(View view) {
            super(view);
            mMobcastImageRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastImageRootLayout);
            
            mMobcastImageReadView = (View)view.findViewById(R.id.itemRecyclerMobcastImageReadView);
            
            mMobcastImageIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastImageIndicatorImageView);
            mMobcastImageViewPagerNextIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastImageViewPagerNextIv);
            mMobcastImageViewPagerPrevIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastImageViewPagerPreviousIv);
            
            mMobcastImageMainImageViewIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastImageMainImageIv);
            
            mMobcastImageTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageTitleTv);
            mMobcastImageByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageByTv);
            mMobcastImageViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastImageViewCountTv);
            
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
        AppCompatTextView mMobcastVideoViewDurationTv;
        
        
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
            mMobcastVideoViewDurationTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastVideoDurationTv);
            
            mMobcastVideoReadView.setVisibility(View.INVISIBLE);
            
            mMobcastVideoRootLayout.setOnClickListener(this);
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
        
        AppCompatTextView mMobcastFeedbackTitleTv;
        AppCompatTextView mMobcastFeedbackByTv;
        AppCompatTextView mMobcastFeedbackViewCountTv;
        AppCompatTextView mMobcastFeedbackDetailQuestionCountTv;
        
        
        public FeedbackViewHolder(View view) {
            super(view);
            mMobcastFeedbackRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerMobcastFeedbackRootLayout);
            
            mMobcastFeedbackReadView = (View)view.findViewById(R.id.itemRecyclerMobcastFeedbackReadView);
            
            mMobcastFeedbackIndicatorIv = (ImageView)view.findViewById(R.id.itemRecyclerMobcastFeedbackIndicatorImageView);
            
            mMobcastFeedbackTitleTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackTitleTv);
            mMobcastFeedbackByTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackByTv);
            mMobcastFeedbackViewCountTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerMobcastFeedbackViewCountTv);
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
}
