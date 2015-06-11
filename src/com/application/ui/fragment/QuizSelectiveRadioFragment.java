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
public class QuizSelectiveRadioFragment extends Fragment {
	private static final String TAG = QuizSelectiveRadioFragment.class.getSimpleName();
	
	private RadioGroup mQuizRadioGroup;
	
	private AppCompatTextView mQuizTitleTv;
	
	private AppCompatRadioButton mQuizOptionARadioButton;
	private AppCompatRadioButton mQuizOptionBRadioButton;
	private AppCompatRadioButton mQuizOptionCRadioButton;
	private AppCompatRadioButton mQuizOptionDRadioButton;
	
	public static QuizSelectiveRadioFragment newInstance() {
		QuizSelectiveRadioFragment fragment = new QuizSelectiveRadioFragment();
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_quiz_selective_radio,
				container, false);
		mQuizTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentQuizSelectiveRadioTitleTv);
		
		mQuizRadioGroup = (RadioGroup)view.findViewById(R.id.fragmentQuizSelectionRadioGroup);
		
		mQuizOptionARadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton1);
		mQuizOptionBRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton2);
		mQuizOptionCRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton3);
		mQuizOptionDRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton4);
		
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
		mQuizRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group,
                    int checkedId) {
				// TODO Auto-generated method stub
				uiChangeOnRadioGroupListener(checkedId);
			}
		});
	}
	
	private void uiChangeOnRadioGroupListener(int checkedId){
		mQuizOptionARadioButton.setTextColor(Utilities.getAppColor());
		mQuizOptionBRadioButton.setTextColor(Utilities.getAppColor());
		mQuizOptionCRadioButton.setTextColor(Utilities.getAppColor());
		mQuizOptionDRadioButton.setTextColor(Utilities.getAppColor());
		mQuizOptionARadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionBRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionCRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionDRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		switch(checkedId){
		case R.id.fragmentQuizSelectionRadioButton1:
			mQuizOptionARadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionARadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentQuizSelectionRadioButton2:
			mQuizOptionBRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionBRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentQuizSelectionRadioButton3:
			mQuizOptionCRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionCRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentQuizSelectionRadioButton4:
			mQuizOptionDRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionDRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		}
	}
}
