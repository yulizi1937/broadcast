/**
 * 
 */
package com.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

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
	
	public static FeedbackRatingFragment newInstance() {
		FeedbackRatingFragment fragment = new FeedbackRatingFragment();
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
		if(mRating > 0){
			mRatingBar.setBackgroundColor(Utilities.getAppHighlightedColor());
		}else{
			mRatingBar.setBackgroundColor(getResources().getColor(android.R.color.white));
		}
	}
}
