/**
 * 
 */
package com.application.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackMultipleCheckFragment extends Fragment {
	private static final String TAG = FeedbackMultipleCheckFragment.class.getSimpleName();
	
	private AppCompatTextView mFeedbackTitleTv;
	
	private AppCompatCheckBox mFeedbackOptionACheckBox;
	private AppCompatCheckBox mFeedbackOptionBCheckBox;
	private AppCompatCheckBox mFeedbackOptionCCheckBox;
	private AppCompatCheckBox mFeedbackOptionDCheckBox;
	
	public static FeedbackMultipleCheckFragment newInstance() {
		FeedbackMultipleCheckFragment fragment = new FeedbackMultipleCheckFragment();
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_feedback_multiple_check,
				container, false);
		mFeedbackTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentFeedbackMultipleCheckTitleTv);
		
		mFeedbackOptionACheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox1);
		mFeedbackOptionCCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox2);
		mFeedbackOptionBCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox3);
		mFeedbackOptionDCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox4);
		
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
		setCheckBoxListener(mFeedbackOptionACheckBox);
		setCheckBoxListener(mFeedbackOptionBCheckBox);
		setCheckBoxListener(mFeedbackOptionCCheckBox);
		setCheckBoxListener(mFeedbackOptionDCheckBox);
	}
	
	private void setCheckBoxListener(final AppCompatCheckBox mCheckBox){
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
					uiChangeOnCheckChangedListener(mCheckBox, isChecked);					
			}
		});
	}
	
	private void uiChangeOnCheckChangedListener(AppCompatCheckBox mCheckBox, boolean isChecked){
		if(isChecked){
			mCheckBox.setTextColor(getResources().getColor(android.R.color.white));
			mCheckBox.setBackgroundColor(Utilities.getAppHighlightedColor());
		}else{
			mCheckBox.setTextColor(Utilities.getAppColor());
			mCheckBox.setBackgroundColor(getResources().getColor(android.R.color.white));
		}
	}
}
