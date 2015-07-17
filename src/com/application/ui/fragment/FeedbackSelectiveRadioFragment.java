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
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.application.beans.FeedbackPagerInfo;
import com.application.sqlite.DBConstant;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackSelectiveRadioFragment extends Fragment {
	private static final String TAG = FeedbackSelectiveRadioFragment.class.getSimpleName();
	
	private RadioGroup mFeedbackRadioGroup;
	
	private AppCompatTextView mFeedbackTitleTv;
	private int mPosition;
	private FeedbackPagerInfo mFeedbackPagerInfo;
	
	private AppCompatRadioButton mFeedbackOptionARadioButton;
	private AppCompatRadioButton mFeedbackOptionBRadioButton;
	private AppCompatRadioButton mFeedbackOptionCRadioButton;
	private AppCompatRadioButton mFeedbackOptionDRadioButton;
	private AppCompatRadioButton mFeedbackOptionERadioButton;
	private AppCompatRadioButton mFeedbackOptionFRadioButton;
	private AppCompatRadioButton mFeedbackOptionGRadioButton;
	
	
	private String mContentTitle;
	private String mContentDesc;
	private String mContentOptionA;
	private String mContentOptionB;
	private String mContentOptionC;
	private String mContentOptionD;
	private String mContentOptionE;
	private String mContentOptionF;
	private String mContentOptionG;
	
	public static FeedbackSelectiveRadioFragment newInstance(int mPosition, FeedbackPagerInfo mFeedbackPagerInfo) {
		FeedbackSelectiveRadioFragment fragment = new FeedbackSelectiveRadioFragment();
		fragment.mPosition = mPosition;
		fragment.mFeedbackPagerInfo = mFeedbackPagerInfo;
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
		mFeedbackOptionERadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton5);
		mFeedbackOptionFRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton6);
		mFeedbackOptionGRadioButton = (AppCompatRadioButton)view.findViewById(R.id.fragmentFeedbackSelectionRadioButton7);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		getFeedbackFromDB();
	}
	
	private void setUiListener(){
		setRadioGroupListener();
	}
	
	private void getFeedbackFromDB(){
		Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mFeedbackPagerInfo.getmFeedbackId(), mFeedbackPagerInfo.getmFeedbackQId()}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION));
			mContentOptionA = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1));
			mContentOptionB = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2));
			mContentOptionC = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3));
			mContentOptionD = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4));
			mContentOptionE = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5));
			mContentOptionF = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6));
			mContentOptionG = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7));	
		}
		
		if(mCursor!=null)
			mCursor.close();
		
		setUiWithData();
	}
	
	private void setUiWithData(){
		mFeedbackTitleTv.setText(mContentTitle);
		mFeedbackOptionARadioButton.setText(mContentOptionA);
		mFeedbackOptionBRadioButton.setText(mContentOptionB);
		
		if(!TextUtils.isEmpty(mContentOptionC)){
			mFeedbackOptionCRadioButton.setVisibility(View.VISIBLE);
			mFeedbackOptionCRadioButton.setText(mContentOptionC);
		}
		
		if(!TextUtils.isEmpty(mContentOptionD)){
			mFeedbackOptionDRadioButton.setVisibility(View.VISIBLE);
			mFeedbackOptionDRadioButton.setText(mContentOptionD);
		}
		
		if(!TextUtils.isEmpty(mContentOptionE)){
			mFeedbackOptionERadioButton.setVisibility(View.VISIBLE);
			mFeedbackOptionERadioButton.setText(mContentOptionE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionF)){
			mFeedbackOptionFRadioButton.setVisibility(View.VISIBLE);
			mFeedbackOptionFRadioButton.setText(mContentOptionF);
		}
		
		if(!TextUtils.isEmpty(mContentOptionG)){
			mFeedbackOptionGRadioButton.setVisibility(View.VISIBLE);
			mFeedbackOptionGRadioButton.setText(mContentOptionG);
		}
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
		mFeedbackOptionERadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionFRadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionGRadioButton.setTextColor(Utilities.getAppColor());
		mFeedbackOptionARadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionBRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionCRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionDRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionERadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionFRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
		mFeedbackOptionGRadioButton.setBackgroundColor(getResources().getColor(android.R.color.white));
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
		case R.id.fragmentFeedbackSelectionRadioButton5:
			mFeedbackOptionERadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionERadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentFeedbackSelectionRadioButton6:
			mFeedbackOptionFRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionFRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		case R.id.fragmentFeedbackSelectionRadioButton7:
			mFeedbackOptionGRadioButton.setTextColor(getResources().getColor(android.R.color.white));
			mFeedbackOptionGRadioButton.setBackgroundColor(Utilities.getAppHighlightedColor());
			break;
		}
		saveValueInDB();
	}
	
	private void saveValueInDB(){
		String mOptionSelected = null;
		switch(mFeedbackRadioGroup.getCheckedRadioButtonId()){
		case R.id.fragmentFeedbackSelectionRadioButton1:
			mOptionSelected = "A";
			break;
		case R.id.fragmentFeedbackSelectionRadioButton2:
			mOptionSelected = "B";
			break;
		case R.id.fragmentFeedbackSelectionRadioButton3:
			mOptionSelected = "C";
			break;
		case R.id.fragmentFeedbackSelectionRadioButton4:
			mOptionSelected = "D";
			break;
		case R.id.fragmentFeedbackSelectionRadioButton5:
			mOptionSelected = "E";
			break;
		case R.id.fragmentFeedbackSelectionRadioButton6:
			mOptionSelected = "F";
			break;
		case R.id.fragmentFeedbackSelectionRadioButton7:
			mOptionSelected = "G";
			break;
		}
		ContentValues values = new ContentValues();
		values.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, mOptionSelected);
		getActivity().getContentResolver().update(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, values, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mFeedbackPagerInfo.getmFeedbackId(), mFeedbackPagerInfo.getmFeedbackQId()});
	}
}
