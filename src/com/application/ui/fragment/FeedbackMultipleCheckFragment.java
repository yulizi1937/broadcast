/**
 * 
 */
package com.application.ui.fragment;

import java.io.File;
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

import com.application.beans.FeedbackPagerInfo;
import com.application.sqlite.DBConstant;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
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
	private AppCompatCheckBox mFeedbackOptionECheckBox;
	private AppCompatCheckBox mFeedbackOptionFCheckBox;
	private AppCompatCheckBox mFeedbackOptionGCheckBox;
	
	private int mPosition;
	private FeedbackPagerInfo mFeedbackPagerInfo;
	
	private String mContentTitle;
	private String mContentDesc;
	private String mContentOptionA;
	private String mContentOptionB;
	private String mContentOptionC;
	private String mContentOptionD;
	private String mContentOptionE;
	private String mContentOptionF;
	private String mContentOptionG;
	
	public static FeedbackMultipleCheckFragment newInstance(int mPosition, FeedbackPagerInfo mFeedbackPagerInfo) {
		FeedbackMultipleCheckFragment fragment = new FeedbackMultipleCheckFragment();
		fragment.mPosition = mPosition;
		fragment.mFeedbackPagerInfo = mFeedbackPagerInfo;
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
		mFeedbackOptionBCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox2);
		mFeedbackOptionCCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox3);
		mFeedbackOptionDCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox4);
		mFeedbackOptionECheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox5);
		mFeedbackOptionFCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox6);
		mFeedbackOptionGCheckBox = (AppCompatCheckBox)view.findViewById(R.id.fragmentFeedbackMultipleCheckBox7);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		getFeedbackFromDB();
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
		mFeedbackOptionACheckBox.setText(mContentOptionA);
		mFeedbackOptionBCheckBox.setText(mContentOptionB);
		
		if(!TextUtils.isEmpty(mContentOptionC)){
			mFeedbackOptionCCheckBox.setText(mContentOptionC);
			mFeedbackOptionCCheckBox.setVisibility(View.VISIBLE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionD)){
			mFeedbackOptionDCheckBox.setText(mContentOptionD);
			mFeedbackOptionDCheckBox.setVisibility(View.VISIBLE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionE)){
			mFeedbackOptionECheckBox.setVisibility(View.VISIBLE);
			mFeedbackOptionECheckBox.setText(mContentOptionE);
		}
		
		if(!TextUtils.isEmpty(mContentOptionF)){
			mFeedbackOptionFCheckBox.setVisibility(View.VISIBLE);
			mFeedbackOptionFCheckBox.setText(mContentOptionF);
		}
		
		if(!TextUtils.isEmpty(mContentOptionG)){
			mFeedbackOptionGCheckBox.setVisibility(View.VISIBLE);
			mFeedbackOptionGCheckBox.setText(mContentOptionG);
		}
	}
	
	private void setUiListener(){
		setCheckBoxListener();
	}
	
	private void setCheckBoxListener(){
		setCheckBoxListener(mFeedbackOptionACheckBox);
		setCheckBoxListener(mFeedbackOptionBCheckBox);
		setCheckBoxListener(mFeedbackOptionCCheckBox);
		setCheckBoxListener(mFeedbackOptionDCheckBox);
		setCheckBoxListener(mFeedbackOptionECheckBox);
		setCheckBoxListener(mFeedbackOptionFCheckBox);
		setCheckBoxListener(mFeedbackOptionGCheckBox);
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
		if(mFeedbackOptionACheckBox.isChecked()){
			mAnswer.add("A");
		}
		if(mFeedbackOptionBCheckBox.isChecked()){
			mAnswer.add("B");
		}
		if(mFeedbackOptionCCheckBox.isChecked()){
			mAnswer.add("C");
		}
		if(mFeedbackOptionDCheckBox.isChecked()){
			mAnswer.add("D");
		}
		if(mFeedbackOptionECheckBox.isChecked()){
			mAnswer.add("E");
		}
		if(mFeedbackOptionFCheckBox.isChecked()){
			mAnswer.add("F");
		}
		if(mFeedbackOptionGCheckBox.isChecked()){
			mAnswer.add("G");
		}
		
		String mOptionSelected= StringUtils.join(mAnswer.toArray(),",");
		ContentValues values = new ContentValues();
		values.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, mOptionSelected);
		getActivity().getContentResolver().update(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, values, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mFeedbackPagerInfo.getmFeedbackId(), mFeedbackPagerInfo.getmFeedbackQId()});
	}
}
