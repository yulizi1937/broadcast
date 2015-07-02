/**
 * 
 */
package com.application.beans;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class Mobcast implements Parcelable {
	private String mId;
	private String mTitle;
	private String mDescription;
	private String mViewCount;
	private String mLikeCount;
	private String mBy;
	private String mDate;
	private String mTime;
	private boolean isLike;
	private boolean isRead;
	private boolean isDownloadable;
	private boolean isSharing;
	private String mExpiryDate;
	private String mExpiryTime;
	private String mFileType;
	private String mLink;
	private ArrayList<MobcastFileInfo> mFileInfo;

	public Mobcast(String mId, String mTitle, String mDescription,
			String mViewCount, String mLikeCount, String mBy, String mDate,
			String mTime, boolean isLike, boolean isRead,
			boolean isDownloadable, boolean isSharing, String mExpiryDate,
			String mExpiryTime, String mFileType, String mLink,
			ArrayList<MobcastFileInfo> mFileInfo) {
		super();
		this.mId = mId;
		this.mTitle = mTitle;
		this.mDescription = mDescription;
		this.mViewCount = mViewCount;
		this.mLikeCount = mLikeCount;
		this.mBy = mBy;
		this.mDate = mDate;
		this.mTime = mTime;
		this.isLike = isLike;
		this.isRead = isRead;
		this.isDownloadable = isDownloadable;
		this.isSharing = isSharing;
		this.mExpiryDate = mExpiryDate;
		this.mExpiryTime = mExpiryTime;
		this.mFileType = mFileType;
		this.mLink = mLink;
		this.mFileInfo = mFileInfo;
	}

	public Mobcast() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getmDescription() {
		return mDescription;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
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

	public String getmBy() {
		return mBy;
	}

	public void setmBy(String mBy) {
		this.mBy = mBy;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	public String getmTime() {
		return mTime;
	}

	public void setmTime(String mTime) {
		this.mTime = mTime;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isDownloadable() {
		return isDownloadable;
	}

	public void setDownloadable(boolean isDownloadable) {
		this.isDownloadable = isDownloadable;
	}

	public boolean isSharing() {
		return isSharing;
	}

	public void setSharing(boolean isSharing) {
		this.isSharing = isSharing;
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

	public String getmLink() {
		return mLink;
	}

	public void setmLink(String mLink) {
		this.mLink = mLink;
	}

	public ArrayList<MobcastFileInfo> getmFileInfo() {
		return mFileInfo;
	}

	public void setmFileInfo(ArrayList<MobcastFileInfo> mFileInfo) {
		this.mFileInfo = mFileInfo;
	}

	protected Mobcast(Parcel in) {
		mId = in.readString();
		mTitle = in.readString();
		mDescription = in.readString();
		mViewCount = in.readString();
		mLikeCount = in.readString();
		mBy = in.readString();
		mDate = in.readString();
		mTime = in.readString();
		isLike = in.readByte() != 0x00;
		isRead = in.readByte() != 0x00;
		isDownloadable = in.readByte() != 0x00;
		isSharing = in.readByte() != 0x00;
		mExpiryDate = in.readString();
		mExpiryTime = in.readString();
		mFileType = in.readString();
		mLink = in.readString();
		if (in.readByte() == 0x01) {
			mFileInfo = new ArrayList<MobcastFileInfo>();
			in.readList(mFileInfo, MobcastFileInfo.class.getClassLoader());
		} else {
			mFileInfo = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mTitle);
		dest.writeString(mDescription);
		dest.writeString(mViewCount);
		dest.writeString(mLikeCount);
		dest.writeString(mBy);
		dest.writeString(mDate);
		dest.writeString(mTime);
		dest.writeByte((byte) (isLike ? 0x01 : 0x00));
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isDownloadable ? 0x01 : 0x00));
		dest.writeByte((byte) (isSharing ? 0x01 : 0x00));
		dest.writeString(mExpiryDate);
		dest.writeString(mExpiryTime);
		dest.writeString(mFileType);
		dest.writeString(mLink);
		if (mFileInfo == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(mFileInfo);
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Mobcast> CREATOR = new Parcelable.Creator<Mobcast>() {
		@Override
		public Mobcast createFromParcel(Parcel in) {
			return new Mobcast(in);
		}

		@Override
		public Mobcast[] newArray(int size) {
			return new Mobcast[size];
		}
	};
}