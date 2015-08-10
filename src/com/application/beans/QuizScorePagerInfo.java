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
	private String mQuestionId;

	public QuizScorePagerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuizScorePagerInfo(String mQuestionNo, String mQuestionTitle,
			String mQuestionId) {
		super();
		this.mQuestionNo = mQuestionNo;
		this.mQuestionTitle = mQuestionTitle;
		this.mQuestionId = mQuestionId;
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

	public String getmQuestionId() {
		return mQuestionId;
	}

	public void setmQuestionId(String mQuestionId) {
		this.mQuestionId = mQuestionId;
	}

	protected QuizScorePagerInfo(Parcel in) {
		mQuestionNo = in.readString();
		mQuestionTitle = in.readString();
		mQuestionId = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mQuestionNo);
		dest.writeString(mQuestionTitle);
		dest.writeString(mQuestionId);
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