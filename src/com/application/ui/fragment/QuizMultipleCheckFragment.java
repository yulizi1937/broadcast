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
public class QuizMultipleCheckFragment extends Fragment {
	private static final String TAG = QuizMultipleCheckFragment.class.getSimpleName();
	
	private AppCompatTextView mQuizTitleTv;
	
	private AppCompatCheckBox mQuizOptionACheckBox;
	private AppCompatCheckBox mQuizOptionBCheckBox;
	private AppCompatCheckBox mQuizOptionCCheckBox;
	private AppCompatCheckBox mQuizOptionDCheckBox;
	
	public static QuizMultipleCheckFragment newInstance() {
		QuizMultipleCheckFragment fragment = new QuizMultipleCheckFragment();
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_quiz_multiple_check,
				container, false);
		mQuizTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentQuizMultipleCheckTitleTv);
		
		mQuizOptionACheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox1);
		mQuizOptionCCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox2);
		mQuizOptionBCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox3);
		mQuizOptionDCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox4);
		
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
		setCheckBoxListener(mQuizOptionACheckBox);
		setCheckBoxListener(mQuizOptionBCheckBox);
		setCheckBoxListener(mQuizOptionCCheckBox);
		setCheckBoxListener(mQuizOptionDCheckBox);
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
