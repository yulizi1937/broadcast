/**
 * 
 */
package com.application.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.application.beans.QuizPagerInfo;
import com.application.sqlite.DBConstant;
import com.application.utils.Utilities;
import com.mobcast.R;

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
	private AppCompatRadioButton mQuizOptionERadioButton;
	private AppCompatRadioButton mQuizOptionFRadioButton;
	private AppCompatRadioButton mQuizOptionGRadioButton;
	
	private int mPosition;
	private QuizPagerInfo mQuizPagerInfo;
	
	private String mContentTitle;
	private String mContentDesc;
	private String mContentOptionA;
	private String mContentOptionB;
	private String mContentOptionC;
	private String mContentOptionD;
	private String mContentOptionE;
	private String mContentOptionF;
	private String mContentOptionG;
	
	public static QuizSelectiveRadioFragment newInstance(int mPosition, QuizPagerInfo mQuizPagerInfo) {
		QuizSelectiveRadioFragment fragment = new QuizSelectiveRadioFragment();
		fragment.mPosition = mPosition;
		fragment.mQuizPagerInfo = mQuizPagerInfo;
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
		mQuizOptionERadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton5);
		mQuizOptionFRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton6);
		mQuizOptionGRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentQuizSelectionRadioButton7);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		getQuizFromDB();
	}
	
	private void setUiListener(){
		setRadioGroupListener();
	}
	
	private void getQuizFromDB(){
		Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" + " AND " + DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mQuizPagerInfo.getmQuizId(), mQuizPagerInfo.getmQuizQId()}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION));
			mContentOptionA = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1));
			mContentOptionB = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2));
			mContentOptionC = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3));
			mContentOptionD = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4));
			mContentOptionE = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5));
			mContentOptionF = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6));
			mContentOptionG = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7));	
		}
		
		if(mCursor!=null)
			mCursor.close();
		
		setUiWithData();
	}
	
	private void setUiWithData(){
		mQuizTitleTv.setText(mContentTitle);
		mQuizOptionARadioButton.setText(mContentOptionA);
		mQuizOptionBRadioButton.setText(mContentOptionB);
		
		if(!TextUtils.isEmpty(mContentOptionC)){
			mQuizOptionCRadioButton.setVisibility(View.VISIBLE);
			mQuizOptionCRadioButton.setText(mContentOptionC);
		}
		
		if(!TextUtils.isEmpty(mContentOptionD)){
			mQuizOptionDRadioButton.setVisibility(View.VISIBLE);
			mQuizOptionDRadioButton.setText(mContentOptionD);
		}
		
		if(!TextUtils.isEmpty(mContentOptionE)){
			mQuizOptionERadioButton.setVisibility(View.VISIBLE);
			mQuizOptionERadioButton.setText(mContentOptionE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionF)){
			mQuizOptionFRadioButton.setVisibility(View.VISIBLE);
			mQuizOptionFRadioButton.setText(mContentOptionF);
		}
		
		if(!TextUtils.isEmpty(mContentOptionG)){
			mQuizOptionGRadioButton.setVisibility(View.VISIBLE);
			mQuizOptionGRadioButton.setText(mContentOptionG);
		}
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
		mQuizOptionERadioButton.setTextColor(Utilities.getAppColor());
		mQuizOptionFRadioButton.setTextColor(Utilities.getAppColor());
		mQuizOptionGRadioButton.setTextColor(Utilities.getAppColor());
		
		mQuizOptionARadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionBRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionCRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionDRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionERadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionFRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mQuizOptionGRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		
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
		case R.id.fragmentQuizSelectionRadioButton5:
			mQuizOptionERadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionERadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentQuizSelectionRadioButton6:
			mQuizOptionFRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionFRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentQuizSelectionRadioButton7:
			mQuizOptionGRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mQuizOptionGRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		}
		saveValueInDB();
	}
	
	private void saveValueInDB(){
		String mOptionSelected = null;
		switch(mQuizRadioGroup.getCheckedRadioButtonId()){
		case R.id.fragmentQuizSelectionRadioButton1:
			mOptionSelected = "A";
			break;
		case R.id.fragmentQuizSelectionRadioButton2:
			mOptionSelected = "B";
			break;
		case R.id.fragmentQuizSelectionRadioButton3:
			mOptionSelected = "C";
			break;
		case R.id.fragmentQuizSelectionRadioButton4:
			mOptionSelected = "D";
			break;
		case R.id.fragmentQuizSelectionRadioButton5:
			mOptionSelected = "E";
			break;
		case R.id.fragmentQuizSelectionRadioButton6:
			mOptionSelected = "F";
			break;
		case R.id.fragmentQuizSelectionRadioButton7:
			mOptionSelected = "G";
			break;
		}
		ContentValues values = new ContentValues();
		values.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER, mOptionSelected);
		getActivity().getContentResolver().update(DBConstant.Training_Quiz_Columns.CONTENT_URI, values, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" + " AND " + DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mQuizPagerInfo.getmQuizId(), mQuizPagerInfo.getmQuizQId()});
	}
}
