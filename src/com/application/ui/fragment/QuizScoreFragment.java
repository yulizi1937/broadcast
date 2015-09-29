/**
 * 
 */
package com.application.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.beans.QuizScorePagerInfo;
import com.application.sqlite.DBConstant;
import com.application.ui.activity.QuizActivity;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
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
	
	private View mQuizScoreLineView;
	
	private QuizScorePagerInfo mQuizScorePagerInfo;
	
	private String mQuestionTitle;
	private StringBuilder mQuestionCorrectAnswer = new StringBuilder();
	private String mQuestionNumber;
	
	
	public QuizScoreFragment newInstance(QuizScorePagerInfo mQuizScorePagerInfo) {
		QuizScoreFragment fragment = new QuizScoreFragment();
		fragment.mQuizScorePagerInfo = mQuizScorePagerInfo;
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
		
		mQuizScoreLineView = (View)view.findViewById(R.id.fragmentQuizScoreLineView);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		applyTheme();
		getDataFromDB();
	}
	
	private void applyTheme() {
		try {
			ThemeUtils.getInstance(getActivity()).applyThemeQuizScore(
					getActivity(), getActivity(), 
					mQuizScoreQuestionNumberTv, mQuizScoreLineView);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getDataFromDB(){
		mQuestionNumber =  getActivity().getResources().getString(R.string.sample_question_header_box) + " "+mQuizScorePagerInfo.getmQuestionNo();
		Cursor mCursor = getActivity().getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mQuizScorePagerInfo.getmQuestionId()}, null);
		
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mQuestionTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION));
			String mCorrectOption = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION));
			if(mCorrectOption.length() > 1){
				String mArrCorrectOption[] = mCorrectOption.split(",");
				for(int i = 0; i < mArrCorrectOption.length ;i++){
					if(mArrCorrectOption[i].equalsIgnoreCase("A")){
						mQuestionCorrectAnswer.append("A. " +mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1)) +"\n\n");
					}else if(mArrCorrectOption[i].equalsIgnoreCase("B")){
						mQuestionCorrectAnswer.append("B. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2))+"\n\n") ;
					}else if(mArrCorrectOption[i].equalsIgnoreCase("C")){
						mQuestionCorrectAnswer.append("C. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3))+"\n\n");
					} else if(mArrCorrectOption[i].equalsIgnoreCase("D")){
						mQuestionCorrectAnswer.append("D. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4))+"\n\n") ;
					} else if(mArrCorrectOption[i].equalsIgnoreCase("E")){
						mQuestionCorrectAnswer.append("E. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5))+"\n\n");
					} else if(mArrCorrectOption[i].equalsIgnoreCase("F")){
						mQuestionCorrectAnswer.append("F. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6))+"\n\n");
					} else if(mArrCorrectOption[i].equalsIgnoreCase("G")){
						mQuestionCorrectAnswer.append("G. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7))+"\n\n");
					}	
				}
				
			}else{
				if(mCorrectOption.equalsIgnoreCase("A")){
					mQuestionCorrectAnswer.append("A. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1)));
				}else if(mCorrectOption.equalsIgnoreCase("B")){
					mQuestionCorrectAnswer.append("B. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2)));
				}else if(mCorrectOption.equalsIgnoreCase("C")){
					mQuestionCorrectAnswer.append("C." +mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3)));
				} else if(mCorrectOption.equalsIgnoreCase("D")){
					mQuestionCorrectAnswer.append("D. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4)));
				} else if(mCorrectOption.equalsIgnoreCase("E")){
					mQuestionCorrectAnswer.append("E. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5)));
				} else if(mCorrectOption.equalsIgnoreCase("F")){
					mQuestionCorrectAnswer.append("F. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6)));
				} else if(mCorrectOption.equalsIgnoreCase("G")){
					mQuestionCorrectAnswer.append("G. "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7)));
				}   
			}
		}
		
		if(mCursor!=null){
			mCursor.close();
		}
		
		setIntentDataToUi();
	}
	
	private void setIntentDataToUi(){
		if(!TextUtils.isEmpty(mQuestionNumber)){
			mQuizScoreQuestionNumberTv.setText(mQuestionNumber);
		}
		
		if(!TextUtils.isEmpty(mQuestionCorrectAnswer.toString())){
			mQuizScoreQuestionCorrectAnswerTv.setText(mQuestionCorrectAnswer.toString());
		}
		
		if(!TextUtils.isEmpty(mQuestionTitle)){
			mQuizScoreQuestionTitleTv.setText(mQuestionTitle);
		}
	}
	
}
