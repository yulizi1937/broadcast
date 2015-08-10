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
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.application.beans.QuizPagerInfo;
import com.application.sqlite.DBConstant;
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
	private AppCompatCheckBox mQuizOptionECheckBox;
	private AppCompatCheckBox mQuizOptionFCheckBox;
	private AppCompatCheckBox mQuizOptionGCheckBox;
	
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
	
	public static QuizMultipleCheckFragment newInstance(int mPosition, QuizPagerInfo mQuizPagerInfo) {
		QuizMultipleCheckFragment fragment = new QuizMultipleCheckFragment();
		fragment.mPosition = mPosition;
		fragment.mQuizPagerInfo = mQuizPagerInfo;
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
		mQuizOptionBCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox2);
		mQuizOptionCCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox3);
		mQuizOptionDCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox4);
		mQuizOptionECheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox5);
		mQuizOptionFCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox6);
		mQuizOptionGCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentQuizMultipleCheckBox7);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		getQuizFromDB();
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
		mQuizOptionACheckBox.setText(mContentOptionA);
		mQuizOptionBCheckBox.setText(mContentOptionB);
		
		if(!TextUtils.isEmpty(mContentOptionC)){
			mQuizOptionCCheckBox.setText(mContentOptionC);
			mQuizOptionCCheckBox.setVisibility(View.VISIBLE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionD)){
			mQuizOptionDCheckBox.setText(mContentOptionD);
			mQuizOptionDCheckBox.setVisibility(View.VISIBLE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionE)){
			mQuizOptionECheckBox.setVisibility(View.VISIBLE);
			mQuizOptionECheckBox.setText(mContentOptionE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionF)){
			mQuizOptionFCheckBox.setVisibility(View.VISIBLE);
			mQuizOptionFCheckBox.setText(mContentOptionF);
		}
		
		if(!TextUtils.isEmpty(mContentOptionG)){
			mQuizOptionGCheckBox.setVisibility(View.VISIBLE);
			mQuizOptionGCheckBox.setText(mContentOptionG);
		}
	}
	
	private void setUiListener(){
		setCheckBoxListener();
	}
	
	private void setCheckBoxListener(){
		setCheckBoxListener(mQuizOptionACheckBox);
		setCheckBoxListener(mQuizOptionBCheckBox);
		setCheckBoxListener(mQuizOptionCCheckBox);
		setCheckBoxListener(mQuizOptionDCheckBox);
		setCheckBoxListener(mQuizOptionECheckBox);
		setCheckBoxListener(mQuizOptionFCheckBox);
		setCheckBoxListener(mQuizOptionGCheckBox);
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
		saveValueInDB();
	}
	
	private void saveValueInDB(){
		ArrayList<String> mAnswer = new ArrayList<>();
		if(mQuizOptionACheckBox.isChecked()){
			mAnswer.add("A");
		}
		if(mQuizOptionBCheckBox.isChecked()){
			mAnswer.add("B");
		}
		if(mQuizOptionCCheckBox.isChecked()){
			mAnswer.add("C");
		}
		if(mQuizOptionDCheckBox.isChecked()){
			mAnswer.add("D");
		}
		if(mQuizOptionECheckBox.isChecked()){
			mAnswer.add("E");
		}
		if(mQuizOptionFCheckBox.isChecked()){
			mAnswer.add("F");
		}
		if(mQuizOptionGCheckBox.isChecked()){
			mAnswer.add("G");
		}
		
		String mOptionSelected= StringUtils.join(mAnswer.toArray(),",");
		ContentValues values = new ContentValues();
		values.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER, mOptionSelected);
		getActivity().getContentResolver().update(DBConstant.Training_Quiz_Columns.CONTENT_URI, values, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" + " AND " + DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mQuizPagerInfo.getmQuizId(), mQuizPagerInfo.getmQuizQId()});
	}
}
