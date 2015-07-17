/**
 * 
 */
package com.application.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.beans.FeedbackPagerInfo;
import com.application.sqlite.DBConstant;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackSubjectiveFragment extends Fragment {
	private static final String TAG = FeedbackSubjectiveFragment.class.getSimpleName();
	
	private AppCompatTextView mFeedbackTitleTv;
	
	private AppCompatEditText mFeedbackSubjectiveEd;
	
	private int mPosition;
	private FeedbackPagerInfo mFeedbackPagerInfo;
	
	private String mContentTitle;
	
	public static FeedbackSubjectiveFragment newInstance(int mPosition , FeedbackPagerInfo mFeedbackPagerInfo) {
		FeedbackSubjectiveFragment fragment = new FeedbackSubjectiveFragment();
		fragment.mPosition = mPosition;
		fragment.mFeedbackPagerInfo = mFeedbackPagerInfo;
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_feedback_subjective_textview,
				container, false);
		mFeedbackTitleTv = (AppCompatTextView)view.findViewById(R.id.fragmentFeedbackSubjectiveTextTitleTv);
		
		mFeedbackSubjectiveEd = (AppCompatEditText)view.findViewById(R.id.fragmentFeedbackSubjectiveEditText);
		
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
		}
		
		if(mCursor!=null)
			mCursor.close();
		
		setUiWithData();
	}
	private void setUiWithData(){
		mFeedbackTitleTv.setText(mContentTitle);
	}
	
	private void setUiListener(){
		addOnTextWatcher(mFeedbackSubjectiveEd);
	}

	private void addOnTextWatcher(AppCompatEditText mEditText){
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				saveValueInDB();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void saveValueInDB(){
		String mOptionSelected = mFeedbackSubjectiveEd.getText().toString();
		ContentValues values = new ContentValues();
		values.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, mOptionSelected);
		getActivity().getContentResolver().update(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, values, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mFeedbackPagerInfo.getmFeedbackId(), mFeedbackPagerInfo.getmFeedbackQId()});
	}
}
