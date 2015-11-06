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
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;

import com.application.beans.ParichayReferral;
import com.application.ui.view.FlexibleDividerDecoration;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.mobcast.R;

public class ParichayReferralRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.VisibilityProvider{
	private static final String TAG = ParichayReferralRecyclerAdapter.class
			.getSimpleName();

	private static final int VIEW_TYPE_HEADER = 0;
	private static final int VIEW_TYPE_BANNER = 1;
	private static final int VIEW_TYPE_IRF = 2;
	private static final int VIEW_TYPE_FMCG = 3;
	private static final int VIEW_TYPE_FOOTER = 4;
	

	private LayoutInflater mInflater;
	private Context mContext;
	private int whichTheme = 0;
	private View mHeaderView;
	private boolean isGrid = false;
	private ArrayList<ParichayReferral> mArrayListParichayReferral;
	public OnItemClickListener mItemClickListener;
	public OnItemLongClickListener mItemLongClickListener;

	public ParichayReferralRecyclerAdapter(Context mContext,
			ArrayList<ParichayReferral> mArrayListParichayReferral,View headerView, boolean isGrid) {
		this.mContext = mContext;
		this.mArrayListParichayReferral = mArrayListParichayReferral;
		mHeaderView = headerView;
		this.isGrid = isGrid;
		mInflater = LayoutInflater.from(mContext);
		whichTheme = ApplicationLoader.getPreferences().getAppTheme();
	}

	public void addParichayObjList(ArrayList<ParichayReferral> mListParichay) {
		mArrayListParichayReferral = mListParichay;
		notifyDataSetChanged();
	}

	public void updateEventObj(int position, ArrayList<ParichayReferral> mListParichay) {
		mArrayListParichayReferral = mListParichay;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if(!isGrid){
    		switch(position){
        	case 0:
        		return VIEW_TYPE_HEADER;
        	case 1:
        		return VIEW_TYPE_BANNER;
        	default:
        		return getItemTypeFromObject(position-2);
        	}
    	}else{
    		switch(position){
        	case 0:
        		return VIEW_TYPE_HEADER;
        	case 1:
        		return VIEW_TYPE_HEADER;
        	case 2:
        		return VIEW_TYPE_BANNER;
        	default:
        		return getItemTypeFromObject(position-3);
        	}
    	}
	}

	public int getItemTypeFromObject(int position) {
		if (mArrayListParichayReferral.get(position).getmType().equalsIgnoreCase(AppConstants.PARICHAY.IRF)){
			return VIEW_TYPE_IRF;
		}else if (mArrayListParichayReferral.get(position).getmType().equalsIgnoreCase(AppConstants.PARICHAY.FMCG)){
			return VIEW_TYPE_FMCG;
		}else if (mArrayListParichayReferral.get(position).getmType().equalsIgnoreCase(AppConstants.PARICHAY.FOOTER)){
			return VIEW_TYPE_FOOTER;
		}else{
			return VIEW_TYPE_HEADER;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case VIEW_TYPE_FOOTER:
			return new FooterViewHolder(mInflater.inflate(
					R.layout.layout_footerview, parent, false));
		case VIEW_TYPE_BANNER:
			return new ParichayBannerViewHolder(mInflater.inflate(
					R.layout.item_recycler_parichay_referred_cover, parent, false));
		case VIEW_TYPE_IRF:
			return new ParichayIRFViewHolder(mInflater.inflate(
					R.layout.item_recycler_parichay_irf, parent, false));
		case VIEW_TYPE_FMCG:
			return new ParichayFMCGViewHolder(mInflater.inflate(
					R.layout.item_recycler_parichay_fmcg, parent, false));
		default:
			return new HeaderViewHolder(mHeaderView);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(viewHolder instanceof ParichayIRFViewHolder){
			processParichayIRFViewHolder(viewHolder, position);
		}if(viewHolder instanceof ParichayFMCGViewHolder){
			processParichayFMCGViewHolder(viewHolder, position);
		}else if(viewHolder instanceof ParichayBannerViewHolder){
			processParichayBannerViewHolder(viewHolder, position);
		} 
	}

	@Override
	public int getItemCount() {
		if (mHeaderView == null) {
            return mArrayListParichayReferral.size();
        } else {
        	if(!isGrid){
        		return mArrayListParichayReferral.size() + 2;	
        	}else{
        		return mArrayListParichayReferral.size() + 3;
        	}
            
        }
	}

	public class ParichayIRFViewHolder extends RecyclerView.ViewHolder {
		AppCompatTextView mSequenceTv;
		AppCompatTextView mNameTv;
		AppCompatTextView mReferredForTv;
		AppCompatTextView mTelephonicTv;
		AppCompatTextView mOnlineWrittenTv;
		AppCompatTextView mPR1Tv;
		AppCompatTextView mPR2Tv;
		AppCompatTextView mHRTv;
		AppCompatTextView mInstall1Tv;
		AppCompatTextView mInstall2Tv;
		AppCompatTextView mInstall3Tv;
		AppCompatTextView mReasonTv;
		
		ImageView mExpandIv;
		ImageView mTelephonicIv;
		ImageView mOnlineWrittenIv;
		ImageView mPR1Iv;
		ImageView mPR2Iv;
		ImageView mHRIv;
		ImageView mInstall1Iv;
		ImageView mInstall2Iv;
		ImageView mInstall3Iv;
		
		FrameLayout mRootLayout;
		
		LinearLayout mIRFInterviewLayout;
		LinearLayout mIRFInstallmentLayout;
		LinearLayout mIRFProcessLayout;
		
		View mTelephonicView;
		View mOnlineWrittenView;
		View mPR1View;
		View mPR2View;
		View mHRView;
		View mJoinedView;
		
		public ParichayIRFViewHolder(View view) {
			super(view);
			mSequenceTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayIRFSequenceTv);
			mNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayIRFTitleTv);
			mReferredForTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayIRFReferredForTv);
			mTelephonicTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFTelephoneTv);
			mOnlineWrittenTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFWrittenTv);
			mPR1Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFPR1Tv);
			mPR2Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFPR2Tv);
			mHRTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFHRTv);
			mInstall1Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFInstallment1Tv);
			mInstall2Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFInstallment2Tv);
			mInstall3Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFInstallment3Tv);
			mReasonTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayIRFReasonTv);
			
			mExpandIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFCollapseIv);
			mTelephonicIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFTelephoneIv);
			mOnlineWrittenIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFWrittenIv);
			mPR1Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFPR1Iv);
			mPR2Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFPR2Iv);
			mHRIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFHRIv);
			mInstall1Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFInstallment1Iv);
			mInstall2Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFInstallment2Iv);
			mInstall3Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayIRFInstallment3Iv);
			
			mRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerParichayIRFRootLayout);
			
			mIRFInterviewLayout = (LinearLayout)view.findViewById(R.id.itemRecyclerParichayIRFProcessInterviewLayout);
			mIRFInstallmentLayout = (LinearLayout)view.findViewById(R.id.itemRecyclerParichayIRFInstallmentLayout);
			mIRFProcessLayout = (LinearLayout)view.findViewById(R.id.itemRecyclerParichayIRFProcessDetailLayout);
			
			mTelephonicView = (View)view.findViewById(R.id.itemRecyclerParichayIRFTelephoneLv);
			mOnlineWrittenView = (View)view.findViewById(R.id.itemRecyclerParichayIRFWrittenLv);
			mPR1View = (View)view.findViewById(R.id.itemRecyclerParichayIRFPR1Lv);
			mPR2View = (View)view.findViewById(R.id.itemRecyclerParichayIRFPR2Lv);
			mHRView = (View)view.findViewById(R.id.itemRecyclerParichayIRFHRLv);
			mJoinedView = (View)view.findViewById(R.id.itemRecyclerParichayIRFJoined);
		}
	}
	
	
	public class ParichayFMCGViewHolder extends RecyclerView.ViewHolder {
		AppCompatTextView mSequenceTv;
		AppCompatTextView mNameTv;
		AppCompatTextView mReferredForTv;
		AppCompatTextView mTelephonicTv;
		AppCompatTextView mPR1Tv;
		AppCompatTextView mPR2Tv;
		AppCompatTextView mHRTv;
		AppCompatTextView mInstall1Tv;
		AppCompatTextView mInstall2Tv;
		AppCompatTextView mInstall3Tv;
		AppCompatTextView mReasonTv;
		
		ImageView mExpandIv;
		ImageView mTelephonicIv;
		ImageView mPR1Iv;
		ImageView mPR2Iv;
		ImageView mHRIv;
		ImageView mInstall1Iv;
		ImageView mInstall2Iv;
		ImageView mInstall3Iv;
		
		FrameLayout mRootLayout;
		
		LinearLayout mFMCGInterviewLayout;
		LinearLayout mFMCGInstallmentLayout;
		
		View mTelephonicView;
		View mPR1View;
		View mPR2View;
		View mHRView;
		View mJoinedView;
		
		public ParichayFMCGViewHolder(View view) {
			super(view);
			mSequenceTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayFMCGSequenceTv);
			mNameTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayFMCGTitleTv);
			mReferredForTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayFMCGReferredForTv);
			mTelephonicTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGTelephoneTv);
			mPR1Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGPR1Tv);
			mPR2Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGPR2Tv);
			mHRTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGHRTv);
			mInstall1Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGInstallment1Tv);
			mInstall2Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGInstallment2Tv);
			mInstall3Tv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGInstallment3Tv);
			mReasonTv = (AppCompatTextView)view.findViewById(R.id.itemRecyclerParichayFMCGReasonTv);
			
			mExpandIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGCollapseIv);
			mTelephonicIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGTelephoneIv);
			mPR1Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGPR1Iv);
			mPR2Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGPR2Iv);
			mHRIv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGHRIv);
			mInstall1Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGInstallment1Iv);
			mInstall2Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGInstallment2Iv);
			mInstall3Iv = (ImageView)view.findViewById(R.id.itemRecyclerParichayFMCGInstallment3Iv);
			
			mRootLayout = (FrameLayout)view.findViewById(R.id.itemRecyclerParichayFMCGRootLayout);
			
			mFMCGInterviewLayout = (LinearLayout)view.findViewById(R.id.itemRecyclerParichayFMCGProcessInterviewLayout);
			mFMCGInstallmentLayout = (LinearLayout)view.findViewById(R.id.itemRecyclerParichayFMCGInstallmentLayout);
			
			mTelephonicView = (View)view.findViewById(R.id.itemRecyclerParichayFMCGTelephoneLv);
			mPR1View = (View)view.findViewById(R.id.itemRecyclerParichayFMCGPR1Lv);
			mPR2View = (View)view.findViewById(R.id.itemRecyclerParichayFMCGPR2Lv);
			mHRView = (View)view.findViewById(R.id.itemRecyclerParichayFMCGHRLv);
			mJoinedView = (View)view.findViewById(R.id.itemRecyclerParichayFMCGJoined);
		}
	}
	
	public class ParichayBannerViewHolder extends RecyclerView.ViewHolder
			implements View.OnClickListener {
		AppCompatTextView mCSequenceTv;
		AppCompatTextView mReferredTv;
		
		FrameLayout mRootLayout;
		
		public ParichayBannerViewHolder(View view) {
			super(view);
			mCSequenceTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayCandidatesSequenceTv);
			mReferredTv = (AppCompatTextView) view.findViewById(R.id.itemRecyclerParichayCandidatesReferredTv);
			
			mRootLayout = (FrameLayout) view.findViewById(R.id.itemRecyclerParichayCoverRootLayout);
			
			mRootLayout.setOnClickListener(this);
		}

		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(v, getLayoutPosition());
			}
		}
	}
	
	 static class HeaderViewHolder extends RecyclerView.ViewHolder {
	        public HeaderViewHolder(View view) {
	            super(view);
	        }
	    }
	
	public class FooterViewHolder extends RecyclerView.ViewHolder {
		ProgressWheel mFooterProgressWheel;

		public FooterViewHolder(View view) {
			super(view);
			mFooterProgressWheel = (ProgressWheel)view.findViewById(R.id.itemFooterProgressWheel);
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

	public void setOnItemClickListener(
			final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	public interface OnItemLongClickListener {
		public void onItemLongClick(View view, int position);
	}

	public void setOnItemLongClickListener(
			final OnItemLongClickListener mItemLongClickListener) {
		this.mItemLongClickListener = mItemLongClickListener;
	}
	
	private void processParichayBannerViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			((ParichayBannerViewHolder)viewHolder).mCSequenceTv.setText(String.valueOf(mArrayListParichayReferral.size()));
			if(mArrayListParichayReferral.size() == 0){
				((ParichayBannerViewHolder)viewHolder).mReferredTv.setText("NO CANDIDATES BEEN REFERRED");	
			}else{
				((ParichayBannerViewHolder)viewHolder).mReferredTv.setText("CANDIDATES REFERRED");
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void processParichayIRFViewHolder(final RecyclerView.ViewHolder viewHolder, final int position){
		try{
			final ParichayReferral mObj = mArrayListParichayReferral.get(isGrid ? position-3 : position-2);
			((ParichayIRFViewHolder)viewHolder).mNameTv.setText(mObj.getmName());
			((ParichayIRFViewHolder)viewHolder).mReferredForTv.setText(mObj.getmReferredFor());
			((ParichayIRFViewHolder)viewHolder).mSequenceTv.setText(String.valueOf(position-1));
			changeUiAccToProcessed(mObj, mObj.getIsTelephone(), ((ParichayIRFViewHolder)viewHolder).mTelephonicTv, ((ParichayIRFViewHolder)viewHolder).mReasonTv,((ParichayIRFViewHolder)viewHolder).mTelephonicIv, ((ParichayIRFViewHolder)viewHolder).mTelephonicView);
			changeUiAccToProcessed(mObj, mObj.getIsOnlineWritten(), ((ParichayIRFViewHolder)viewHolder).mOnlineWrittenTv, ((ParichayIRFViewHolder)viewHolder).mReasonTv,((ParichayIRFViewHolder)viewHolder).mOnlineWrittenIv, ((ParichayIRFViewHolder)viewHolder).mOnlineWrittenView);
			changeUiAccToProcessed(mObj, mObj.getIsPR1(), ((ParichayIRFViewHolder)viewHolder).mPR1Tv, ((ParichayIRFViewHolder)viewHolder).mReasonTv,((ParichayIRFViewHolder)viewHolder).mPR1Iv, ((ParichayIRFViewHolder)viewHolder).mPR1View);
			changeUiAccToProcessed(mObj, mObj.getIsPR2(), ((ParichayIRFViewHolder)viewHolder).mPR2Tv, ((ParichayIRFViewHolder)viewHolder).mReasonTv,((ParichayIRFViewHolder)viewHolder).mPR2Iv, ((ParichayIRFViewHolder)viewHolder).mPR2View);
			changeUiAccToProcessed(mObj, mObj.getIsHR(), ((ParichayIRFViewHolder)viewHolder).mHRTv, ((ParichayIRFViewHolder)viewHolder).mReasonTv,((ParichayIRFViewHolder)viewHolder).mHRIv, ((ParichayIRFViewHolder)viewHolder).mHRView);
			
			changeUiAccToProcessed(mObj, mObj.getIsInstallment1(), ((ParichayIRFViewHolder)viewHolder).mInstall1Tv, ((ParichayIRFViewHolder)viewHolder).mInstall1Iv);
			changeUiAccToProcessed(mObj, mObj.getIsInstallment2(), ((ParichayIRFViewHolder)viewHolder).mInstall2Tv, ((ParichayIRFViewHolder)viewHolder).mInstall2Iv);
			changeUiAccToProcessed(mObj, mObj.getIsInstallment3(), ((ParichayIRFViewHolder)viewHolder).mInstall3Tv, ((ParichayIRFViewHolder)viewHolder).mInstall3Iv);
			
			if(!mObj.isExpand()){
				((ParichayIRFViewHolder)viewHolder).mIRFInterviewLayout.setVisibility(View.GONE);
				((ParichayIRFViewHolder)viewHolder).mIRFInstallmentLayout.setVisibility(View.GONE);
				((ParichayIRFViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_expand));
			}else{
				((ParichayIRFViewHolder)viewHolder).mIRFInterviewLayout.setVisibility(View.VISIBLE);
				((ParichayIRFViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_collapse));
				if(mObj.getIsInstallment1()!=0 || mObj.getIsInstallment2()!=0 || mObj.getIsInstallment3()!=0){
					((ParichayIRFViewHolder)viewHolder).mIRFInstallmentLayout.setVisibility(View.VISIBLE);	
				}else{
					((ParichayIRFViewHolder)viewHolder).mIRFInstallmentLayout.setVisibility(View.GONE);	
				}
			}
			
			((ParichayIRFViewHolder)viewHolder).mRootLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if(mObj.isExpand()){
						((ParichayIRFViewHolder)viewHolder).mIRFInterviewLayout.setVisibility(View.GONE);
						((ParichayIRFViewHolder)viewHolder).mIRFInstallmentLayout.setVisibility(View.GONE);
						((ParichayIRFViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_expand));
					}else{
						((ParichayIRFViewHolder)viewHolder).mIRFInterviewLayout.setVisibility(View.VISIBLE);
						((ParichayIRFViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_collapse));
						if(mObj.getIsInstallment1()!=0 || mObj.getIsInstallment2()!=0 || mObj.getIsInstallment3()!=0){
							((ParichayIRFViewHolder)viewHolder).mIRFInstallmentLayout.setVisibility(View.VISIBLE);	
						}else{
							((ParichayIRFViewHolder)viewHolder).mIRFInstallmentLayout.setVisibility(View.GONE);	
						}
					}
					mObj.setExpand(!mObj.isExpand());
				}
			});
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void processParichayFMCGViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
		try{
			final ParichayReferral mObj = mArrayListParichayReferral.get(isGrid ? position-3 : position-2);
			((ParichayFMCGViewHolder)viewHolder).mNameTv.setText(mObj.getmName());
			((ParichayFMCGViewHolder)viewHolder).mReferredForTv.setText(mObj.getmReferredFor());
			((ParichayFMCGViewHolder)viewHolder).mSequenceTv.setText(String.valueOf(position-1));
			changeUiAccToProcessed(mObj, mObj.getIsTelephone(), ((ParichayFMCGViewHolder)viewHolder).mTelephonicTv,((ParichayFMCGViewHolder)viewHolder).mReasonTv, ((ParichayFMCGViewHolder)viewHolder).mTelephonicIv, ((ParichayFMCGViewHolder)viewHolder).mTelephonicView);
			changeUiAccToProcessed(mObj, mObj.getIsPR1(), ((ParichayFMCGViewHolder)viewHolder).mPR1Tv,((ParichayFMCGViewHolder)viewHolder).mReasonTv, ((ParichayFMCGViewHolder)viewHolder).mPR1Iv, ((ParichayFMCGViewHolder)viewHolder).mPR1View);
			changeUiAccToProcessed(mObj, mObj.getIsPR2(), ((ParichayFMCGViewHolder)viewHolder).mPR2Tv, ((ParichayFMCGViewHolder)viewHolder).mReasonTv,((ParichayFMCGViewHolder)viewHolder).mPR2Iv, ((ParichayFMCGViewHolder)viewHolder).mPR2View);
			changeUiAccToProcessed(mObj, mObj.getIsHR(), ((ParichayFMCGViewHolder)viewHolder).mHRTv,((ParichayFMCGViewHolder)viewHolder).mReasonTv, ((ParichayFMCGViewHolder)viewHolder).mHRIv, ((ParichayFMCGViewHolder)viewHolder).mHRView);
			
			changeUiAccToProcessed(mObj, mObj.getIsInstallment1(), ((ParichayFMCGViewHolder)viewHolder).mInstall1Tv, ((ParichayFMCGViewHolder)viewHolder).mInstall1Iv);
			changeUiAccToProcessed(mObj, mObj.getIsInstallment2(), ((ParichayFMCGViewHolder)viewHolder).mInstall2Tv, ((ParichayFMCGViewHolder)viewHolder).mInstall2Iv);
			changeUiAccToProcessed(mObj, mObj.getIsInstallment3(), ((ParichayFMCGViewHolder)viewHolder).mInstall3Tv, ((ParichayFMCGViewHolder)viewHolder).mInstall3Iv);
			
			if(!mObj.isExpand()){
				((ParichayFMCGViewHolder)viewHolder).mFMCGInterviewLayout.setVisibility(View.GONE);
				((ParichayFMCGViewHolder)viewHolder).mFMCGInstallmentLayout.setVisibility(View.GONE);
				((ParichayFMCGViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_expand));
			}else{
				((ParichayFMCGViewHolder)viewHolder).mFMCGInterviewLayout.setVisibility(View.VISIBLE);
				((ParichayFMCGViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_collapse));
				if(mObj.getIsInstallment1()!=0 || mObj.getIsInstallment2()!=0 || mObj.getIsInstallment3()!=0){
					((ParichayFMCGViewHolder)viewHolder).mFMCGInstallmentLayout.setVisibility(View.VISIBLE);	
				}else{
					((ParichayFMCGViewHolder)viewHolder).mFMCGInstallmentLayout.setVisibility(View.GONE);	
				}
			}
			
			((ParichayFMCGViewHolder)viewHolder).mRootLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if(mObj.isExpand()){
						((ParichayFMCGViewHolder)viewHolder).mFMCGInterviewLayout.setVisibility(View.GONE);
						((ParichayFMCGViewHolder)viewHolder).mFMCGInstallmentLayout.setVisibility(View.GONE);
						((ParichayFMCGViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_expand));
					}else{
						((ParichayFMCGViewHolder)viewHolder).mFMCGInterviewLayout.setVisibility(View.VISIBLE);
						((ParichayFMCGViewHolder)viewHolder).mExpandIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_collapse));
						if(mObj.getIsInstallment1()!=0 || mObj.getIsInstallment2()!=0 || mObj.getIsInstallment3()!=0){
							((ParichayFMCGViewHolder)viewHolder).mFMCGInstallmentLayout.setVisibility(View.VISIBLE);	
						}else{
							((ParichayFMCGViewHolder)viewHolder).mFMCGInstallmentLayout.setVisibility(View.GONE);	
						}
					}
					mObj.setExpand(!mObj.isExpand());
				}
			});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void changeUiAccToProcessed(ParichayReferral mObj ,int isProccessed, AppCompatTextView mTextView, AppCompatTextView mReasonTextView,ImageView mImageView, View mView){
		switch(isProccessed){
		case 0:
			mView.setBackgroundColor(mContext.getResources().getColor(R.color.drawer_item_title));
			mTextView.setTextColor(mContext.getResources().getColor(R.color.drawer_item_title));
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_uncheck));
			mReasonTextView.setVisibility(View.GONE);
			break;
		case 1:
			mView.setBackgroundColor(mContext.getResources().getColor(R.color.unread_highlight));
			mTextView.setTextColor(mContext.getResources().getColor(R.color.unread_highlight));
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_check));
			mReasonTextView.setVisibility(View.GONE);
			break;
		case 2:
			mView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
			mTextView.setTextColor(mContext.getResources().getColor(R.color.red));
			mReasonTextView.setVisibility(View.VISIBLE);
			mReasonTextView.setText("Reason: "+mObj.getmReason());
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_cancel));
			break;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	private void changeUiAccToProcessed(ParichayReferral mObj ,int isProccessed, AppCompatTextView mTextView, ImageView mImageView){
		switch(isProccessed){
		case 0:
			mTextView.setTextColor(mContext.getResources().getColor(R.color.drawer_item_title));
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_uncheck));
			break;
		case 1:
			mTextView.setTextColor(mContext.getResources().getColor(R.color.unread_highlight));
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_check));
			break;
		case 2:
			mTextView.setTextColor(mContext.getResources().getColor(R.color.red));
			mTextView.setText(mObj.getmReason());
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_parichay_cancel));
			break;
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
