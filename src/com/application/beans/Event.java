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
public class Event implements Parcelable {
	private String mId;
	private String mTitle;
	private String mViewCount;
	private String mLikeCount;
	private String mGoingCount;
	private String mBy;
	private String mStartDate;
	private String mStartTime;
	private String mReceivedDate;
	private String mReceivedTime;
	private String mDaysLeft;
	private String mFileType;
	private boolean isRead;
	private boolean isGoingToAttend;
	private boolean isLike;

	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Event(String mId, String mTitle, String mViewCount,
			String mLikeCount, String mGoingCount, String mBy,
			String mStartDate, String mStartTime, String mReceivedDate,
			String mReceivedTime, String mDaysLeft, String mFileType,
			boolean isRead, boolean isGoingToAttend, boolean isLike) {
		super();
		this.mId = mId;
		this.mTitle = mTitle;
		this.mViewCount = mViewCount;
		this.mLikeCount = mLikeCount;
		this.mGoingCount = mGoingCount;
		this.mBy = mBy;
		this.mStartDate = mStartDate;
		this.mStartTime = mStartTime;
		this.mReceivedDate = mReceivedDate;
		this.mReceivedTime = mReceivedTime;
		this.mDaysLeft = mDaysLeft;
		this.mFileType = mFileType;
		this.isRead = isRead;
		this.isGoingToAttend = isGoingToAttend;
		this.isLike = isLike;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmViewCount() {
		return mViewCount;
	}

	public void setmViewCount(String mViewCount) {
		this.mViewCount = mViewCount;
	}

	public String getmLikeCount() {
		return mLikeCount;
	}

	public void setmLikeCount(String mLikeCount) {
		this.mLikeCount = mLikeCount;
	}

	public String getmGoingCount() {
		return mGoingCount;
	}

	public void setmGoingCount(String mGoingCount) {
		this.mGoingCount = mGoingCount;
	}

	public String getmBy() {
		return mBy;
	}

	public void setmBy(String mBy) {
		this.mBy = mBy;
	}

	public String getmStartDate() {
		return mStartDate;
	}

	public void setmStartDate(String mStartDate) {
		this.mStartDate = mStartDate;
	}

	public String getmStartTime() {
		return mStartTime;
	}

	public void setmStartTime(String mStartTime) {
		this.mStartTime = mStartTime;
	}

	public String getmReceivedDate() {
		return mReceivedDate;
	}

	public void setmReceivedDate(String mReceivedDate) {
		this.mReceivedDate = mReceivedDate;
	}

	public String getmReceivedTime() {
		return mReceivedTime;
	}

	public void setmReceivedTime(String mReceivedTime) {
		this.mReceivedTime = mReceivedTime;
	}

	public String getmDaysLeft() {
		return mDaysLeft;
	}

	public void setmDaysLeft(String mDaysLeft) {
		this.mDaysLeft = mDaysLeft;
	}

	public String getmFileType() {
		return mFileType;
	}

	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isGoingToAttend() {
		return isGoingToAttend;
	}

	public void setGoingToAttend(boolean isGoingToAttend) {
		this.isGoingToAttend = isGoingToAttend;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	protected Event(Parcel in) {
		mId = in.readString();
		mTitle = in.readString();
		mViewCount = in.readString();
		mLikeCount = in.readString();
		mGoingCount = in.readString();
		mBy = in.readString();
		mStartDate = in.readString();
		mStartTime = in.readString();
		mReceivedDate = in.readString();
		mReceivedTime = in.readString();
		mDaysLeft = in.readString();
		mFileType = in.readString();
		isRead = in.readByte() != 0x00;
		isGoingToAttend = in.readByte() != 0x00;
		isLike = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mTitle);
		dest.writeString(mViewCount);
		dest.writeString(mLikeCount);
		dest.writeString(mGoingCount);
		dest.writeString(mBy);
		dest.writeString(mStartDate);
		dest.writeString(mStartTime);
		dest.writeString(mReceivedDate);
		dest.writeString(mReceivedTime);
		dest.writeString(mDaysLeft);
		dest.writeString(mFileType);
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isGoingToAttend ? 0x01 : 0x00));
		dest.writeByte((byte) (isLike ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		@Override
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}
	};
}