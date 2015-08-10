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
public class QuizPagerInfo implements Parcelable {
	private String mQuizId;
	private String mQuizType;
	private String mQuizQId;

	public QuizPagerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuizPagerInfo(String mQuizId, String mQuizType, String mQuizQId) {
		super();
		this.mQuizId = mQuizId;
		this.mQuizType = mQuizType;
		this.mQuizQId = mQuizQId;
	}

	public String getmQuizId() {
		return mQuizId;
	}

	public void setmQuizId(String mQuizId) {
		this.mQuizId = mQuizId;
	}

	public String getmQuizType() {
		return mQuizType;
	}

	public void setmQuizType(String mQuizType) {
		this.mQuizType = mQuizType;
	}

	public String getmQuizQId() {
		return mQuizQId;
	}

	public void setmQuizQId(String mQuizQId) {
		this.mQuizQId = mQuizQId;
	}

	protected QuizPagerInfo(Parcel in) {
		mQuizId = in.readString();
		mQuizType = in.readString();
		mQuizQId = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mQuizId);
		dest.writeString(mQuizType);
		dest.writeString(mQuizQId);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<QuizPagerInfo> CREATOR = new Parcelable.Creator<QuizPagerInfo>() {
		@Override
		public QuizPagerInfo createFromParcel(Parcel in) {
			return new QuizPagerInfo(in);
		}

		@Override
		public QuizPagerInfo[] newArray(int size) {
			return new QuizPagerInfo[size];
		}
	};
}