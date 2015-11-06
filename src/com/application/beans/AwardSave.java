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
public class AwardSave implements Parcelable {
	private String mAwardId;
	private String mAwardTitle;
	private String mAwardName;
	private String mAwardMobile;
	private String mAwardEmail;
	private String mAwardDate;
	private String mAwardReceivedDate;
	private String mAwardReceivedTime;
	private String mAwardDesc;
	private String mAwardCity;
	private String mAwardDep;
	private String mAwardViewCount;
	private String mAwardLikeCount;
	private String mAwardCongratulateCount;
	private boolean mIsRead;
	private boolean mIsLike;
	private boolean mIsCongratulate;
	private boolean mIsSharing;
	private String mFileLink;
	private String mFileSize;
	private String mThumbnailLink;
	private String mExpiryDate;
	private String mExpiryTime;

	public AwardSave() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AwardSave(String mAwardId, String mAwardTitle, String mAwardName,
			String mAwardMobile, String mAwardEmail, String mAwardDate,
			String mAwardReceivedDate, String mAwardReceivedTime,
			String mAwardDesc, String mAwardCity, String mAwardDep,
			String mAwardViewCount, String mAwardLikeCount,
			String mAwardCongratulateCount, boolean mIsRead, boolean mIsLike,
			boolean mIsCongratulate, boolean mIsSharing, String mFileLink,
			String mFileSize, String mThumbnailLink, String mExpiryDate,
			String mExpiryTime) {
		super();
		this.mAwardId = mAwardId;
		this.mAwardTitle = mAwardTitle;
		this.mAwardName = mAwardName;
		this.mAwardMobile = mAwardMobile;
		this.mAwardEmail = mAwardEmail;
		this.mAwardDate = mAwardDate;
		this.mAwardReceivedDate = mAwardReceivedDate;
		this.mAwardReceivedTime = mAwardReceivedTime;
		this.mAwardDesc = mAwardDesc;
		this.mAwardCity = mAwardCity;
		this.mAwardDep = mAwardDep;
		this.mAwardViewCount = mAwardViewCount;
		this.mAwardLikeCount = mAwardLikeCount;
		this.mAwardCongratulateCount = mAwardCongratulateCount;
		this.mIsRead = mIsRead;
		this.mIsLike = mIsLike;
		this.mIsCongratulate = mIsCongratulate;
		this.mIsSharing = mIsSharing;
		this.mFileLink = mFileLink;
		this.mFileSize = mFileSize;
		this.mThumbnailLink = mThumbnailLink;
		this.mExpiryDate = mExpiryDate;
		this.mExpiryTime = mExpiryTime;
	}

	public String getmAwardId() {
		return mAwardId;
	}

	public void setmAwardId(String mAwardId) {
		this.mAwardId = mAwardId;
	}

	public String getmAwardTitle() {
		return mAwardTitle;
	}

	public void setmAwardTitle(String mAwardTitle) {
		this.mAwardTitle = mAwardTitle;
	}

	public String getmAwardName() {
		return mAwardName;
	}

	public void setmAwardName(String mAwardName) {
		this.mAwardName = mAwardName;
	}

	public String getmAwardMobile() {
		return mAwardMobile;
	}

	public void setmAwardMobile(String mAwardMobile) {
		this.mAwardMobile = mAwardMobile;
	}

	public String getmAwardEmail() {
		return mAwardEmail;
	}

	public void setmAwardEmail(String mAwardEmail) {
		this.mAwardEmail = mAwardEmail;
	}

	public String getmAwardDate() {
		return mAwardDate;
	}

	public void setmAwardDate(String mAwardDate) {
		this.mAwardDate = mAwardDate;
	}

	public String getmAwardReceivedDate() {
		return mAwardReceivedDate;
	}

	public void setmAwardReceivedDate(String mAwardReceivedDate) {
		this.mAwardReceivedDate = mAwardReceivedDate;
	}

	public String getmAwardReceivedTime() {
		return mAwardReceivedTime;
	}

	public void setmAwardReceivedTime(String mAwardReceivedTime) {
		this.mAwardReceivedTime = mAwardReceivedTime;
	}

	public String getmAwardDesc() {
		return mAwardDesc;
	}

	public void setmAwardDesc(String mAwardDesc) {
		this.mAwardDesc = mAwardDesc;
	}

	public String getmAwardCity() {
		return mAwardCity;
	}

	public void setmAwardCity(String mAwardCity) {
		this.mAwardCity = mAwardCity;
	}

	public String getmAwardDep() {
		return mAwardDep;
	}

	public void setmAwardDep(String mAwardDep) {
		this.mAwardDep = mAwardDep;
	}

	public String getmAwardViewCount() {
		return mAwardViewCount;
	}

	public void setmAwardViewCount(String mAwardViewCount) {
		this.mAwardViewCount = mAwardViewCount;
	}

	public String getmAwardLikeCount() {
		return mAwardLikeCount;
	}

	public void setmAwardLikeCount(String mAwardLikeCount) {
		this.mAwardLikeCount = mAwardLikeCount;
	}

	public String getmAwardCongratulateCount() {
		return mAwardCongratulateCount;
	}

	public void setmAwardCongratulateCount(String mAwardCongratulateCount) {
		this.mAwardCongratulateCount = mAwardCongratulateCount;
	}

	public boolean ismIsRead() {
		return mIsRead;
	}

	public void setmIsRead(boolean mIsRead) {
		this.mIsRead = mIsRead;
	}

	public boolean ismIsLike() {
		return mIsLike;
	}

	public void setmIsLike(boolean mIsLike) {
		this.mIsLike = mIsLike;
	}

	public boolean ismIsCongratulate() {
		return mIsCongratulate;
	}

	public void setmIsCongratulate(boolean mIsCongratulate) {
		this.mIsCongratulate = mIsCongratulate;
	}

	public boolean ismIsSharing() {
		return mIsSharing;
	}

	public void setmIsSharing(boolean mIsSharing) {
		this.mIsSharing = mIsSharing;
	}

	public String getmFileLink() {
		return mFileLink;
	}

	public void setmFileLink(String mFileLink) {
		this.mFileLink = mFileLink;
	}

	public String getmFileSize() {
		return mFileSize;
	}

	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	public String getmThumbnailLink() {
		return mThumbnailLink;
	}

	public void setmThumbnailLink(String mThumbnailLink) {
		this.mThumbnailLink = mThumbnailLink;
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

	protected AwardSave(Parcel in) {
		mAwardId = in.readString();
		mAwardTitle = in.readString();
		mAwardName = in.readString();
		mAwardMobile = in.readString();
		mAwardEmail = in.readString();
		mAwardDate = in.readString();
		mAwardReceivedDate = in.readString();
		mAwardReceivedTime = in.readString();
		mAwardDesc = in.readString();
		mAwardCity = in.readString();
		mAwardDep = in.readString();
		mAwardViewCount = in.readString();
		mAwardLikeCount = in.readString();
		mAwardCongratulateCount = in.readString();
		mIsRead = in.readByte() != 0x00;
		mIsLike = in.readByte() != 0x00;
		mIsCongratulate = in.readByte() != 0x00;
		mIsSharing = in.readByte() != 0x00;
		mFileLink = in.readString();
		mFileSize = in.readString();
		mThumbnailLink = in.readString();
		mExpiryDate = in.readString();
		mExpiryTime = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAwardId);
		dest.writeString(mAwardTitle);
		dest.writeString(mAwardName);
		dest.writeString(mAwardMobile);
		dest.writeString(mAwardEmail);
		dest.writeString(mAwardDate);
		dest.writeString(mAwardReceivedDate);
		dest.writeString(mAwardReceivedTime);
		dest.writeString(mAwardDesc);
		dest.writeString(mAwardCity);
		dest.writeString(mAwardDep);
		dest.writeString(mAwardViewCount);
		dest.writeString(mAwardLikeCount);
		dest.writeString(mAwardCongratulateCount);
		dest.writeByte((byte) (mIsRead ? 0x01 : 0x00));
		dest.writeByte((byte) (mIsLike ? 0x01 : 0x00));
		dest.writeByte((byte) (mIsCongratulate ? 0x01 : 0x00));
		dest.writeByte((byte) (mIsSharing ? 0x01 : 0x00));
		dest.writeString(mFileLink);
		dest.writeString(mFileSize);
		dest.writeString(mThumbnailLink);
		dest.writeString(mExpiryDate);
		dest.writeString(mExpiryTime);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<AwardSave> CREATOR = new Parcelable.Creator<AwardSave>() {
		@Override
		public AwardSave createFromParcel(Parcel in) {
			return new AwardSave(in);
		}

		@Override
		public AwardSave[] newArray(int size) {
			return new AwardSave[size];
		}
	};
}
