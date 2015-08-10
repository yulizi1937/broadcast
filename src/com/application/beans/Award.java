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
public class Award implements Parcelable {
	private String mId;
	private String mName;
	private String mRecognition;
	private String mThumbLink;
	private String mThumbPath;
	private String mReceivedDate;
	private String mReceivedTime;
	private String mAwardDate;
	private String mViewCount;
	private String mLikeCount;
	private String mCongratulatedCount;
	private String mFileType;
	private boolean isRead;
	private boolean isCongratulated;
	private boolean isLike;

	public Award() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Award(String mId, String mName, String mRecognition,
			String mThumbLink, String mThumbPath, String mReceivedDate,
			String mReceivedTime, String mAwardDate, String mViewCount,
			String mLikeCount, String mCongratulatedCount, String mFileType,
			boolean isRead, boolean isCongratulated, boolean isLike) {
		super();
		this.mId = mId;
		this.mName = mName;
		this.mRecognition = mRecognition;
		this.mThumbLink = mThumbLink;
		this.mThumbPath = mThumbPath;
		this.mReceivedDate = mReceivedDate;
		this.mReceivedTime = mReceivedTime;
		this.mAwardDate = mAwardDate;
		this.mViewCount = mViewCount;
		this.mLikeCount = mLikeCount;
		this.mCongratulatedCount = mCongratulatedCount;
		this.mFileType = mFileType;
		this.isRead = isRead;
		this.isCongratulated = isCongratulated;
		this.isLike = isLike;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmRecognition() {
		return mRecognition;
	}

	public void setmRecognition(String mRecognition) {
		this.mRecognition = mRecognition;
	}

	public String getmThumbLink() {
		return mThumbLink;
	}

	public void setmThumbLink(String mThumbLink) {
		this.mThumbLink = mThumbLink;
	}

	public String getmThumbPath() {
		return mThumbPath;
	}

	public void setmThumbPath(String mThumbPath) {
		this.mThumbPath = mThumbPath;
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

	public String getmAwardDate() {
		return mAwardDate;
	}

	public void setmAwardDate(String mAwardDate) {
		this.mAwardDate = mAwardDate;
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

	public String getmCongratulatedCount() {
		return mCongratulatedCount;
	}

	public void setmCongratulatedCount(String mCongratulatedCount) {
		this.mCongratulatedCount = mCongratulatedCount;
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

	public boolean isCongratulated() {
		return isCongratulated;
	}

	public void setCongratulated(boolean isCongratulated) {
		this.isCongratulated = isCongratulated;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	protected Award(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mRecognition = in.readString();
		mThumbLink = in.readString();
		mThumbPath = in.readString();
		mReceivedDate = in.readString();
		mReceivedTime = in.readString();
		mAwardDate = in.readString();
		mViewCount = in.readString();
		mLikeCount = in.readString();
		mCongratulatedCount = in.readString();
		mFileType = in.readString();
		isRead = in.readByte() != 0x00;
		isCongratulated = in.readByte() != 0x00;
		isLike = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mName);
		dest.writeString(mRecognition);
		dest.writeString(mThumbLink);
		dest.writeString(mThumbPath);
		dest.writeString(mReceivedDate);
		dest.writeString(mReceivedTime);
		dest.writeString(mAwardDate);
		dest.writeString(mViewCount);
		dest.writeString(mLikeCount);
		dest.writeString(mCongratulatedCount);
		dest.writeString(mFileType);
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isCongratulated ? 0x01 : 0x00));
		dest.writeByte((byte) (isLike ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Award> CREATOR = new Parcelable.Creator<Award>() {
		@Override
		public Award createFromParcel(Parcel in) {
			return new Award(in);
		}

		@Override
		public Award[] newArray(int size) {
			return new Award[size];
		}
	};
}