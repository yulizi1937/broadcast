/**
 * 
 */
package com.application.ui.fragment;

import com.application.utils.Utilities;
import com.mobcast.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackSelectiveRadioFragment extends Fragment {
	private static final String TAG = FeedbackSelectiveRadioFragment.class.getSimpleName();
	
	private RadioGroup mFeedbackRadioGroup;
	
	private AppCompatTextView mFeedbackTitleTv;
	
	private AppCompatRadioButton mFeedbackOptionARadioButton;
	private AppCompatRadioButton mFeedbackOptionBRadioButton;
	private AppCompatRadioButton mFeedbackOptionCRadioButton;
	private AppCompatRadioButton mFeedbackOptionDRadioButton;
	
	public static FeedbackSelectiveRadioFragment newInstance() {
		FeedbackSelectiveRadioFragment fragment = new FeedbackSelectiveRadioFragment();
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_feedback_selective_radio,
				container, false);
		mFeedbackTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentFeedbackSelectiveRadioTitleTv);
		
		mFeedbackRadioGroup = (RadioGroup)view.findViewById(R.id.fragmentFeedbackSelectionRadioGroup);
		
		mFeedbackOptionARadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton1);
		mFeedbackOptionBRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton2);
		mFeedbackOptionCRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton3);
		mFeedbackOptionDRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton4);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
	}
	
	private void setUiListener(){
		setRadioGroupListener();
	}
	
	private void setRadioGroupListener(){
		mFeedbackRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group,
                    int checkedId) {
				// TODO Auto-generated method stub
				uiChangeOnRadioGroupListener(checkedId);
			}
		});
	}
	
	private void uiChangeOnRadioGroupListener(int checkedId){
		mFeedbackOptionARadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionBRadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionCRadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionDRadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionARadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionBRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionCRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionDRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		switch(checkedId){
		case R.id.fragmentFeedbackSelectionRadioButton1:
			mFeedbackOptionARadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionARadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentFeedbackSelectionRadioButton2:
			mFeedbackOptionBRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionBRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentFeedbackSelectionRadioButton3:
			mFeedbackOptionCRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionCRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentFeedbackSelectionRadioButton4:
			mFeedbackOptionDRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionDRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		}
	}
}
