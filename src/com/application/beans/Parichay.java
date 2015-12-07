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
public class Parichay implements Parcelable {
	private String mId;
	private String mJobPosition;
	private String mJobUnit;
	private String mJobLoc;
	private String mJobExp;
	private String mJobAgeLimit;
	private String mJobDesc;
	private String mJobQualif;
	private String mJobDesiredProfile;
	private String mLikeCount;
	private String mJobHQ;
	private String mJobDivision;
	private String mJobRegion;
	private String mReadCount;
	private String mReceivedDate;
	private String mReceivedTime;
	private String mDate;
	private String mMonth;
	private String mExpiryDate;
	private String mExpiryTime;
	private String mFileType;
	private String mInstall;
	private boolean isRead;
	private boolean isLike;

	public Parichay() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Parichay(String mId, String mJobPosition, String mJobUnit,
			String mJobLoc, String mJobExp, String mJobAgeLimit,
			String mJobDesc, String mJobQualif, String mJobDesiredProfile,
			String mLikeCount, String mJobHQ, String mJobDivision,
			String mJobRegion, String mReadCount, String mReceivedDate,
			String mReceivedTime, String mDate, String mMonth,
			String mExpiryDate, String mExpiryTime, String mFileType,
			String mInstall, boolean isRead, boolean isLike) {
		super();
		this.mId = mId;
		this.mJobPosition = mJobPosition;
		this.mJobUnit = mJobUnit;
		this.mJobLoc = mJobLoc;
		this.mJobExp = mJobExp;
		this.mJobAgeLimit = mJobAgeLimit;
		this.mJobDesc = mJobDesc;
		this.mJobQualif = mJobQualif;
		this.mJobDesiredProfile = mJobDesiredProfile;
		this.mLikeCount = mLikeCount;
		this.mJobHQ = mJobHQ;
		this.mJobDivision = mJobDivision;
		this.mJobRegion = mJobRegion;
		this.mReadCount = mReadCount;
		this.mReceivedDate = mReceivedDate;
		this.mReceivedTime = mReceivedTime;
		this.mDate = mDate;
		this.mMonth = mMonth;
		this.mExpiryDate = mExpiryDate;
		this.mExpiryTime = mExpiryTime;
		this.mFileType = mFileType;
		this.mInstall = mInstall;
		this.isRead = isRead;
		this.isLike = isLike;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmJobPosition() {
		return mJobPosition;
	}

	public void setmJobPosition(String mJobPosition) {
		this.mJobPosition = mJobPosition;
	}

	public String getmJobUnit() {
		return mJobUnit;
	}

	public void setmJobUnit(String mJobUnit) {
		this.mJobUnit = mJobUnit;
	}

	public String getmJobLoc() {
		return mJobLoc;
	}

	public void setmJobLoc(String mJobLoc) {
		this.mJobLoc = mJobLoc;
	}

	public String getmJobExp() {
		return mJobExp;
	}

	public void setmJobExp(String mJobExp) {
		this.mJobExp = mJobExp;
	}

	public String getmJobAgeLimit() {
		return mJobAgeLimit;
	}

	public void setmJobAgeLimit(String mJobAgeLimit) {
		this.mJobAgeLimit = mJobAgeLimit;
	}

	public String getmJobDesc() {
		return mJobDesc;
	}

	public void setmJobDesc(String mJobDesc) {
		this.mJobDesc = mJobDesc;
	}

	public String getmJobQualif() {
		return mJobQualif;
	}

	public void setmJobQualif(String mJobQualif) {
		this.mJobQualif = mJobQualif;
	}

	public String getmJobDesiredProfile() {
		return mJobDesiredProfile;
	}

	public void setmJobDesiredProfile(String mJobDesiredProfile) {
		this.mJobDesiredProfile = mJobDesiredProfile;
	}

	public String getmLikeCount() {
		return mLikeCount;
	}

	public void setmLikeCount(String mLikeCount) {
		this.mLikeCount = mLikeCount;
	}

	public String getmJobHQ() {
		return mJobHQ;
	}

	public void setmJobHQ(String mJobHQ) {
		this.mJobHQ = mJobHQ;
	}

	public String getmJobDivision() {
		return mJobDivision;
	}

	public void setmJobDivision(String mJobDivision) {
		this.mJobDivision = mJobDivision;
	}

	public String getmJobRegion() {
		return mJobRegion;
	}

	public void setmJobRegion(String mJobRegion) {
		this.mJobRegion = mJobRegion;
	}

	public String getmReadCount() {
		return mReadCount;
	}

	public void setmReadCount(String mReadCount) {
		this.mReadCount = mReadCount;
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

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	public String getmMonth() {
		return mMonth;
	}

	public void setmMonth(String mMonth) {
		this.mMonth = mMonth;
	}

	public String getmExpiryDate() {
		return mExpiryDate;
	}

	public void setmExpiryDate(String mExpiryDate) {
		this.mExpiryDate = mExpiryDate;
	}

	public String getmExpiryTime() {
		return mExpiryTime;
	}

	public void setmExpiryTime(String mExpiryTime) {
		this.mExpiryTime = mExpiryTime;
	}

	public String getmFileType() {
		return mFileType;
	}

	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	public String getmInstall() {
		return mInstall;
	}

	public void setmInstall(String mInstall) {
		this.mInstall = mInstall;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	protected Parichay(Parcel in) {
		mId = in.readString();
		mJobPosition = in.readString();
		mJobUnit = in.readString();
		mJobLoc = in.readString();
		mJobExp = in.readString();
		mJobAgeLimit = in.readString();
		mJobDesc = in.readString();
		mJobQualif = in.readString();
		mJobDesiredProfile = in.readString();
		mLikeCount = in.readString();
		mJobHQ = in.readString();
		mJobDivision = in.readString();
		mJobRegion = in.readString();
		mReadCount = in.readString();
		mReceivedDate = in.readString();
		mReceivedTime = in.readString();
		mDate = in.readString();
		mMonth = in.readString();
		mExpiryDate = in.readString();
		mExpiryTime = in.readString();
		mFileType = in.readString();
		mInstall = in.readString();
		isRead = in.readByte() != 0x00;
		isLike = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mJobPosition);
		dest.writeString(mJobUnit);
		dest.writeString(mJobLoc);
		dest.writeString(mJobExp);
		dest.writeString(mJobAgeLimit);
		dest.writeString(mJobDesc);
		dest.writeString(mJobQualif);
		dest.writeString(mJobDesiredProfile);
		dest.writeString(mLikeCount);
		dest.writeString(mJobHQ);
		dest.writeString(mJobDivision);
		dest.writeString(mJobRegion);
		dest.writeString(mReadCount);
		dest.writeString(mReceivedDate);
		dest.writeString(mReceivedTime);
		dest.writeString(mDate);
		dest.writeString(mMonth);
		dest.writeString(mExpiryDate);
		dest.writeString(mExpiryTime);
		dest.writeString(mFileType);
		dest.writeString(mInstall);
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isLike ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Parichay> CREATOR = new Parcelable.Creator<Parichay>() {
		@Override
		public Parichay createFromParcel(Parcel in) {
			return new Parichay(in);
		}

		@Override
		public Parichay[] newArray(int size) {
			return new Parichay[size];
		}
	};
}