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
public class Recruitment implements Parcelable {
	private String mId;
	private String mJobTitle;
	private String mJobDesig;
	private String mJobLoc;
	private String mJobExp;
	private String mJobSkills;
	private String mJobDesc;
	private String mJobQualif;
	private String mJobCTC;
	private String mContactName;
	private String mContactMobile;
	private String mContactEmail;
	private String mContactDesig;
	private String mLikeCount;
	private String mReadCount;
	private String mShareCount;
	private String mReceivedDate;
	private String mReceivedTime;
	private String mDate;
	private String mMonth;
	private String mExpiryDate;
	private String mExpiryTime;
	private String mFileType;
	private boolean isRead;
	private boolean isLike;
	private boolean isSocialSharing;
	private boolean isContactSharing;

	public Recruitment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Recruitment(String mId, String mJobTitle, String mJobDesig,
			String mJobLoc, String mJobExp, String mJobSkills, String mJobDesc,
			String mJobQualif, String mJobCTC, String mContactName,
			String mContactMobile, String mContactEmail, String mContactDesig,
			String mLikeCount, String mReadCount, String mShareCount,
			String mReceivedDate, String mReceivedTime, String mDate,
			String mMonth, String mExpiryDate, String mExpiryTime,
			String mFileType, boolean isRead, boolean isLike,
			boolean isSocialSharing, boolean isContactSharing) {
		super();
		this.mId = mId;
		this.mJobTitle = mJobTitle;
		this.mJobDesig = mJobDesig;
		this.mJobLoc = mJobLoc;
		this.mJobExp = mJobExp;
		this.mJobSkills = mJobSkills;
		this.mJobDesc = mJobDesc;
		this.mJobQualif = mJobQualif;
		this.mJobCTC = mJobCTC;
		this.mContactName = mContactName;
		this.mContactMobile = mContactMobile;
		this.mContactEmail = mContactEmail;
		this.mContactDesig = mContactDesig;
		this.mLikeCount = mLikeCount;
		this.mReadCount = mReadCount;
		this.mShareCount = mShareCount;
		this.mReceivedDate = mReceivedDate;
		this.mReceivedTime = mReceivedTime;
		this.mDate = mDate;
		this.mMonth = mMonth;
		this.mExpiryDate = mExpiryDate;
		this.mExpiryTime = mExpiryTime;
		this.mFileType = mFileType;
		this.isRead = isRead;
		this.isLike = isLike;
		this.isSocialSharing = isSocialSharing;
		this.isContactSharing = isContactSharing;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmJobTitle() {
		return mJobTitle;
	}

	public void setmJobTitle(String mJobTitle) {
		this.mJobTitle = mJobTitle;
	}

	public String getmJobDesig() {
		return mJobDesig;
	}

	public void setmJobDesig(String mJobDesig) {
		this.mJobDesig = mJobDesig;
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

	public String getmJobSkills() {
		return mJobSkills;
	}

	public void setmJobSkills(String mJobSkills) {
		this.mJobSkills = mJobSkills;
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

	public String getmJobCTC() {
		return mJobCTC;
	}

	public void setmJobCTC(String mJobCTC) {
		this.mJobCTC = mJobCTC;
	}

	public String getmContactName() {
		return mContactName;
	}

	public void setmContactName(String mContactName) {
		this.mContactName = mContactName;
	}

	public String getmContactMobile() {
		return mContactMobile;
	}

	public void setmContactMobile(String mContactMobile) {
		this.mContactMobile = mContactMobile;
	}

	public String getmContactEmail() {
		return mContactEmail;
	}

	public void setmContactEmail(String mContactEmail) {
		this.mContactEmail = mContactEmail;
	}

	public String getmContactDesig() {
		return mContactDesig;
	}

	public void setmContactDesig(String mContactDesig) {
		this.mContactDesig = mContactDesig;
	}

	public String getmLikeCount() {
		return mLikeCount;
	}

	public void setmLikeCount(String mLikeCount) {
		this.mLikeCount = mLikeCount;
	}

	public String getmReadCount() {
		return mReadCount;
	}

	public void setmReadCount(String mReadCount) {
		this.mReadCount = mReadCount;
	}

	public String getmShareCount() {
		return mShareCount;
	}

	public void setmShareCount(String mShareCount) {
		this.mShareCount = mShareCount;
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

	public boolean isSocialSharing() {
		return isSocialSharing;
	}

	public void setSocialSharing(boolean isSocialSharing) {
		this.isSocialSharing = isSocialSharing;
	}

	public boolean isContactSharing() {
		return isContactSharing;
	}

	public void setContactSharing(boolean isContactSharing) {
		this.isContactSharing = isContactSharing;
	}

	protected Recruitment(Parcel in) {
		mId = in.readString();
		mJobTitle = in.readString();
		mJobDesig = in.readString();
		mJobLoc = in.readString();
		mJobExp = in.readString();
		mJobSkills = in.readString();
		mJobDesc = in.readString();
		mJobQualif = in.readString();
		mJobCTC = in.readString();
		mContactName = in.readString();
		mContactMobile = in.readString();
		mContactEmail = in.readString();
		mContactDesig = in.readString();
		mLikeCount = in.readString();
		mReadCount = in.readString();
		mShareCount = in.readString();
		mReceivedDate = in.readString();
		mReceivedTime = in.readString();
		mDate = in.readString();
		mMonth = in.readString();
		mExpiryDate = in.readString();
		mExpiryTime = in.readString();
		mFileType = in.readString();
		isRead = in.readByte() != 0x00;
		isLike = in.readByte() != 0x00;
		isSocialSharing = in.readByte() != 0x00;
		isContactSharing = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mJobTitle);
		dest.writeString(mJobDesig);
		dest.writeString(mJobLoc);
		dest.writeString(mJobExp);
		dest.writeString(mJobSkills);
		dest.writeString(mJobDesc);
		dest.writeString(mJobQualif);
		dest.writeString(mJobCTC);
		dest.writeString(mContactName);
		dest.writeString(mContactMobile);
		dest.writeString(mContactEmail);
		dest.writeString(mContactDesig);
		dest.writeString(mLikeCount);
		dest.writeString(mReadCount);
		dest.writeString(mShareCount);
		dest.writeString(mReceivedDate);
		dest.writeString(mReceivedTime);
		dest.writeString(mDate);
		dest.writeString(mMonth);
		dest.writeString(mExpiryDate);
		dest.writeString(mExpiryTime);
		dest.writeString(mFileType);
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isLike ? 0x01 : 0x00));
		dest.writeByte((byte) (isSocialSharing ? 0x01 : 0x00));
		dest.writeByte((byte) (isContactSharing ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Recruitment> CREATOR = new Parcelable.Creator<Recruitment>() {
		@Override
		public Recruitment createFromParcel(Parcel in) {
			return new Recruitment(in);
		}

		@Override
		public Recruitment[] newArray(int size) {
			return new Recruitment[size];
		}
	};
}