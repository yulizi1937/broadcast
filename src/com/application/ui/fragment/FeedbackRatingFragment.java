/**
 * 
 */
package com.application.ui.fragment;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.application.beans.FeedbackPagerInfo;
import com.application.sqlite.DBConstant;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackRatingFragment extends Fragment {
	private static final String TAG = FeedbackRatingFragment.class.getSimpleName();
	
	private AppCompatTextView mFeedbackTitleTv;
	
	private AppCompatRatingBar mFeedbackRatingBar;
	
	private int mPosition;
	private FeedbackPagerInfo mFeedbackPagerInfo;
	
	private String mContentTitle;
	
	public static FeedbackRatingFragment newInstance(int mPosition, FeedbackPagerInfo mFeedbackPagerInfo) {
		FeedbackRatingFragment fragment = new FeedbackRatingFragment();
		fragment.mPosition = mPosition;
		fragment.mFeedbackPagerInfo = mFeedbackPagerInfo;
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_feedback_rate_ratingbar,
				container, false);
		mFeedbackTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentFeedbackRateRatingTitleTv);
		
		mFeedbackRatingBar = (AppCompatRatingBar)view.findViewById(R.id.fragmentFeedbackRateRatingBar);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		getFeedbackFromDB();
	}
	
	private void getFeedbackFromDB(){
		Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mFeedbackPagerInfo.getmFeedbackId(), mFeedbackPagerInfo.getmFeedbackQId()}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION));
		}
		
		if(mCursor!=null)
			mCursor.close();
		
		setUiWithData();
	}
	
	private void setUiWithData(){
		mFeedbackTitleTv.setText(mContentTitle);
	}
	
	private void setUiListener(){
		setCheckBoxListener();
	}
	
	private void setCheckBoxListener(){
		setRatingBarListener(mFeedbackRatingBar);
	}
	
	private void setRatingBarListener(final AppCompatRatingBar mRatingBar){
		mRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				// TODO Auto-generated method stub
				uiChangeOnRatingChangedListener(mRatingBar, rating);
			}
		});
	}
	
	
	private void uiChangeOnRatingChangedListener(AppCompatRatingBar mRatingBar, float mRating){
		/*if(mRating > 0){
			mRatingBar.setBackgroundColor(Utilities.getAppHighlightedColor());
		}else{
			mRatingBar.setBackgroundColor(getResources().getColor(android.R.color.white));
		}*/
		saveValueInDB();
	}
	
	private void saveValueInDB(){
		String mOptionSelected= String.valueOf((int)mFeedbackRatingBar.getRating());
		ContentValues values = new ContentValues();
		if(mOptionSelected.equalsIgnoreCase("0")){
			values.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, "");		
		}else{
			values.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, mOptionSelected);
		}
		getActivity().getContentResolver().update(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, values, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mFeedbackPagerInfo.getmFeedbackId(), mFeedbackPagerInfo.getmFeedbackQId()});
	}
}
