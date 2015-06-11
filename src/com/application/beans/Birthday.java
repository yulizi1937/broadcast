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
public class Birthday implements Parcelable {
	private String mBirthdayUserName;
	private String mBirthdayUserDep;
	private String mBirthdayUserImage;
	private String mBirthdayDate;
	private boolean isWished;
	private boolean isMessaged;
	private boolean isRead;
	private String mBirthdayId;

	public Birthday() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Birthday(String mBirthdayUserName, String mBirthdayUserDep,
			String mBirthdayUserImage, String mBirthdayDate, boolean isWished,
			boolean isMessaged, boolean isRead, String mBirthdayId) {
		super();
		this.mBirthdayUserName = mBirthdayUserName;
		this.mBirthdayUserDep = mBirthdayUserDep;
		this.mBirthdayUserImage = mBirthdayUserImage;
		this.mBirthdayDate = mBirthdayDate;
		this.isWished = isWished;
		this.isMessaged = isMessaged;
		this.isRead = isRead;
		this.mBirthdayId = mBirthdayId;
	}

	public String getmBirthdayUserName() {
		return mBirthdayUserName;
	}

	public void setmBirthdayUserName(String mBirthdayUserName) {
		this.mBirthdayUserName = mBirthdayUserName;
	}

	public String getmBirthdayUserDep() {
		return mBirthdayUserDep;
	}

	public void setmBirthdayUserDep(String mBirthdayUserDep) {
		this.mBirthdayUserDep = mBirthdayUserDep;
	}

	public String getmBirthdayUserImage() {
		return mBirthdayUserImage;
	}

	public void setmBirthdayUserImage(String mBirthdayUserImage) {
		this.mBirthdayUserImage = mBirthdayUserImage;
	}

	public String getmBirthdayDate() {
		return mBirthdayDate;
	}

	public void setmBirthdayDate(String mBirthdayDate) {
		this.mBirthdayDate = mBirthdayDate;
	}

	public boolean isWished() {
		return isWished;
	}

	public void setWished(boolean isWished) {
		this.isWished = isWished;
	}

	public boolean isMessaged() {
		return isMessaged;
	}

	public void setMessaged(boolean isMessaged) {
		this.isMessaged = isMessaged;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getmBirthdayId() {
		return mBirthdayId;
	}

	public void setmBirthdayId(String mBirthdayId) {
		this.mBirthdayId = mBirthdayId;
	}

	protected Birthday(Parcel in) {
		mBirthdayUserName = in.readString();
		mBirthdayUserDep = in.readString();
		mBirthdayUserImage = in.readString();
		mBirthdayDate = in.readString();
		isWished = in.readByte() != 0x00;
		isMessaged = in.readByte() != 0x00;
		isRead = in.readByte() != 0x00;
		mBirthdayId = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mBirthdayUserName);
		dest.writeString(mBirthdayUserDep);
		dest.writeString(mBirthdayUserImage);
		dest.writeString(mBirthdayDate);
		dest.writeByte((byte) (isWished ? 0x01 : 0x00));
		dest.writeByte((byte) (isMessaged ? 0x01 : 0x00));
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeString(mBirthdayId);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Birthday> CREATOR = new Parcelable.Creator<Birthday>() {
		@Override
		public Birthday createFromParcel(Parcel in) {
			return new Birthday(in);
		}

		@Override
		public Birthday[] newArray(int size) {
			return new Birthday[size];
		}
	};
}