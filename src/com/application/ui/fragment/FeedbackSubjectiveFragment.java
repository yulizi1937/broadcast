/**
 * 
 */
package com.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackSubjectiveFragment extends Fragment {
	private static final String TAG = FeedbackSubjectiveFragment.class.getSimpleName();
	
	private AppCompatTextView mFeedbackTitleTv;
	
	private AppCompatEditText mFeedbackSubjectiveEd;
	
	public static FeedbackSubjectiveFragment newInstance() {
		FeedbackSubjectiveFragment fragment = new FeedbackSubjectiveFragment();
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_feedback_subjective_textview,
				container, false);
		mFeedbackTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentFeedbackRateRatingTitleTv);
		
		mFeedbackSubjectiveEd = (AppCompatEditText)view.findViewById(R.id.fragmentFeedbackSubjectiveEditText);
		
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
		addOnTextWatcher(mFeedbackSubjectiveEd);
	}

	private void addOnTextWatcher(AppCompatEditText mEditText){
		
	}
}
