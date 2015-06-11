/**
 * 
 */
package com.application.ui.fragment;

import java.util.ArrayList;

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

import com.application.beans.QuizScorePagerInfo;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class QuizScoreFragment extends Fragment {
	private static final String TAG = QuizScoreFragment.class.getSimpleName();
	
	private AppCompatTextView mQuizScoreQuestionTitleTv;
	private AppCompatTextView mQuizScoreQuestionNumberTv;
	private AppCompatTextView mQuizScoreQuestionCorrectAnswerTv;
	private QuizScorePagerInfo mQuizScorePagerInfo;
	
	
	public QuizScoreFragment newInstance(QuizScorePagerInfo mQuizScorePagerInfo) {
		QuizScoreFragment fragment = new QuizScoreFragment();
		this.mQuizScorePagerInfo = mQuizScorePagerInfo;
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_quiz_score_view,
				container, false);
		
		mQuizScoreQuestionTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentQuizScoreQuestionTitleTv);
		mQuizScoreQuestionNumberTv = (AppCompatTextView)view.findViewById(R.id.fragmentQuizScoreQuestionNumberTextTv);
		mQuizScoreQuestionCorrectAnswerTv = (AppCompatTextView)view.findViewById(R.id.fragmentQuizScoreCorrectAnswerTv);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		setUiListener();
	}
	
	private void initData(){
//		mQuizScoreQuestionCorrectAnswerTv.setText(mQuizScorePagerInfo.getmCorrectAnswer());
//		mQuizScoreQuestionNumberTv.setText(mQuizScorePagerInfo.getmQuestionNo());
//		mQuizScoreQuestionTitleTv.setText(mQuizScorePagerInfo.getmQuestionTitle());
	}
	
	private void setUiListener(){
	}
	
}
