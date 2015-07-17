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
public class FeedbackPagerInfo implements Parcelable {
	private String mFeedbackId;
	private String mFeedbackType;
	private String mFeedbackQId;

	public FeedbackPagerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedbackPagerInfo(String mFeedbackId, String mFeedbackType,
			String mFeedbackQId) {
		super();
		this.mFeedbackId = mFeedbackId;
		this.mFeedbackType = mFeedbackType;
		this.mFeedbackQId = mFeedbackQId;
	}

	public String getmFeedbackId() {
		return mFeedbackId;
	}

	public void setmFeedbackId(String mFeedbackId) {
		this.mFeedbackId = mFeedbackId;
	}

	public String getmFeedbackType() {
		return mFeedbackType;
	}

	public void setmFeedbackType(String mFeedbackType) {
		this.mFeedbackType = mFeedbackType;
	}

	public String getmFeedbackQId() {
		return mFeedbackQId;
	}

	public void setmFeedbackQId(String mFeedbackQId) {
		this.mFeedbackQId = mFeedbackQId;
	}

	protected FeedbackPagerInfo(Parcel in) {
		mFeedbackId = in.readString();
		mFeedbackQId = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mFeedbackId);
		dest.writeString(mFeedbackQId);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<FeedbackPagerInfo> CREATOR = new Parcelable.Creator<FeedbackPagerInfo>() {
		@Override
		public FeedbackPagerInfo createFromParcel(Parcel in) {
			return new FeedbackPagerInfo(in);
		}

		@Override
		public FeedbackPagerInfo[] newArray(int size) {
			return new FeedbackPagerInfo[size];
		}
	};
}
