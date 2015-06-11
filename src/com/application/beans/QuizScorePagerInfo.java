/**
 * 
 */
package com.application.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class QuizScorePagerInfo implements Parcelable {
	private String mQuestionNo;
	private String mQuestionTitle;
	private String mCorrectAnswer;

	public QuizScorePagerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuizScorePagerInfo(String mQuestionNo, String mQuestionTitle,
			String mCorrectAnswer) {
		super();
		this.mQuestionNo = mQuestionNo;
		this.mQuestionTitle = mQuestionTitle;
		this.mCorrectAnswer = mCorrectAnswer;
	}

	public String getmQuestionNo() {
		return mQuestionNo;
	}

	public void setmQuestionNo(String mQuestionNo) {
		this.mQuestionNo = mQuestionNo;
	}

	public String getmQuestionTitle() {
		return mQuestionTitle;
	}

	public void setmQuestionTitle(String mQuestionTitle) {
		this.mQuestionTitle = mQuestionTitle;
	}

	public String getmCorrectAnswer() {
		return mCorrectAnswer;
	}

	public void setmCorrectAnswer(String mCorrectAnswer) {
		this.mCorrectAnswer = mCorrectAnswer;
	}

	protected QuizScorePagerInfo(Parcel in) {
		mQuestionNo = in.readString();
		mQuestionTitle = in.readString();
		mCorrectAnswer = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mQuestionNo);
		dest.writeString(mQuestionTitle);
		dest.writeString(mCorrectAnswer);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<QuizScorePagerInfo> CREATOR = new Parcelable.Creator<QuizScorePagerInfo>() {
		@Override
		public QuizScorePagerInfo createFromParcel(Parcel in) {
			return new QuizScorePagerInfo(in);
		}

		@Override
		public QuizScorePagerInfo[] newArray(int size) {
			return new QuizScorePagerInfo[size];
		}
	};
}
