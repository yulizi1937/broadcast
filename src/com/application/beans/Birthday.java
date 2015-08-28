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
	private String mId;
	private String mDOB;
	private String mMonth;
	private String mYear;
	private String mReceivedDate;
	private String mMobile;
	private String mEmployeeId;
	private String mCountryCode;
	private String mBirthdayUserName;
	private String mBirthdayUserDep;
	private String mBirthdayUserImage;
	private String mBirthdayUserCity;
	private String mBirthdayUserSunSign;
	private boolean isMale;
	private String mBirthdayDate;
	private boolean isWished;
	private boolean isMessaged;
	private boolean isRead;
	private String mBirthdayId;
	private String mFileType;

	public Birthday() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Birthday(String mId, String mDOB, String mMonth, String mYear,
			String mReceivedDate, String mMobile, String mEmployeeId,
			String mCountryCode, String mBirthdayUserName,
			String mBirthdayUserDep, String mBirthdayUserImage,
			String mBirthdayUserCity, String mBirthdayUserSunSign,
			boolean isMale, String mBirthdayDate, boolean isWished,
			boolean isMessaged, boolean isRead, String mBirthdayId,
			String mFileType) {
		super();
		this.mId = mId;
		this.mDOB = mDOB;
		this.mMonth = mMonth;
		this.mYear = mYear;
		this.mReceivedDate = mReceivedDate;
		this.mMobile = mMobile;
		this.mEmployeeId = mEmployeeId;
		this.mCountryCode = mCountryCode;
		this.mBirthdayUserName = mBirthdayUserName;
		this.mBirthdayUserDep = mBirthdayUserDep;
		this.mBirthdayUserImage = mBirthdayUserImage;
		this.mBirthdayUserCity = mBirthdayUserCity;
		this.mBirthdayUserSunSign = mBirthdayUserSunSign;
		this.isMale = isMale;
		this.mBirthdayDate = mBirthdayDate;
		this.isWished = isWished;
		this.isMessaged = isMessaged;
		this.isRead = isRead;
		this.mBirthdayId = mBirthdayId;
		this.mFileType = mFileType;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmDOB() {
		return mDOB;
	}

	public void setmDOB(String mDOB) {
		this.mDOB = mDOB;
	}

	public String getmMonth() {
		return mMonth;
	}

	public void setmMonth(String mMonth) {
		this.mMonth = mMonth;
	}

	public String getmYear() {
		return mYear;
	}

	public void setmYear(String mYear) {
		this.mYear = mYear;
	}

	public String getmReceivedDate() {
		return mReceivedDate;
	}

	public void setmReceivedDate(String mReceivedDate) {
		this.mReceivedDate = mReceivedDate;
	}

	public String getmMobile() {
		return mMobile;
	}

	public void setmMobile(String mMobile) {
		this.mMobile = mMobile;
	}

	public String getmEmployeeId() {
		return mEmployeeId;
	}

	public void setmEmployeeId(String mEmployeeId) {
		this.mEmployeeId = mEmployeeId;
	}

	public String getmCountryCode() {
		return mCountryCode;
	}

	public void setmCountryCode(String mCountryCode) {
		this.mCountryCode = mCountryCode;
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

	public String getmBirthdayUserCity() {
		return mBirthdayUserCity;
	}

	public void setmBirthdayUserCity(String mBirthdayUserCity) {
		this.mBirthdayUserCity = mBirthdayUserCity;
	}

	public String getmBirthdayUserSunSign() {
		return mBirthdayUserSunSign;
	}

	public void setmBirthdayUserSunSign(String mBirthdayUserSunSign) {
		this.mBirthdayUserSunSign = mBirthdayUserSunSign;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
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

	public String getmFileType() {
		return mFileType;
	}

	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	protected Birthday(Parcel in) {
		mId = in.readString();
		mDOB = in.readString();
		mMonth = in.readString();
		mYear = in.readString();
		mReceivedDate = in.readString();
		mMobile = in.readString();
		mEmployeeId = in.readString();
		mCountryCode = in.readString();
		mBirthdayUserName = in.readString();
		mBirthdayUserDep = in.readString();
		mBirthdayUserImage = in.readString();
		mBirthdayUserCity = in.readString();
		mBirthdayUserSunSign = in.readString();
		isMale = in.readByte() != 0x00;
		mBirthdayDate = in.readString();
		isWished = in.readByte() != 0x00;
		isMessaged = in.readByte() != 0x00;
		isRead = in.readByte() != 0x00;
		mBirthdayId = in.readString();
		mFileType = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mDOB);
		dest.writeString(mMonth);
		dest.writeString(mYear);
		dest.writeString(mReceivedDate);
		dest.writeString(mMobile);
		dest.writeString(mEmployeeId);
		dest.writeString(mCountryCode);
		dest.writeString(mBirthdayUserName);
		dest.writeString(mBirthdayUserDep);
		dest.writeString(mBirthdayUserImage);
		dest.writeString(mBirthdayUserCity);
		dest.writeString(mBirthdayUserSunSign);
		dest.writeByte((byte) (isMale ? 0x01 : 0x00));
		dest.writeString(mBirthdayDate);
		dest.writeByte((byte) (isWished ? 0x01 : 0x00));
		dest.writeByte((byte) (isMessaged ? 0x01 : 0x00));
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeString(mBirthdayId);
		dest.writeString(mFileType);
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