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
	private String mWinnerName;
	private String mAwardName;
	private String mWinnerImage;
	private String isRead;
	private String mAwardId;
	private boolean isCongratulated;

	public Award() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Award(String mWinnerName, String mAwardName, String mWinnerImage,
			String isRead, String mAwardId, boolean isCongratulated) {
		super();
		this.mWinnerName = mWinnerName;
		this.mAwardName = mAwardName;
		this.mWinnerImage = mWinnerImage;
		this.isRead = isRead;
		this.mAwardId = mAwardId;
		this.isCongratulated = isCongratulated;
	}

	public String getmWinnerName() {
		return mWinnerName;
	}

	public void setmWinnerName(String mWinnerName) {
		this.mWinnerName = mWinnerName;
	}

	public String getmAwardName() {
		return mAwardName;
	}

	public void setmAwardName(String mAwardName) {
		this.mAwardName = mAwardName;
	}

	public String getmWinnerImage() {
		return mWinnerImage;
	}

	public void setmWinnerImage(String mWinnerImage) {
		this.mWinnerImage = mWinnerImage;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getmAwardId() {
		return mAwardId;
	}

	public void setmAwardId(String mAwardId) {
		this.mAwardId = mAwardId;
	}

	public boolean isCongratulated() {
		return isCongratulated;
	}

	public void setCongratulated(boolean isCongratulated) {
		this.isCongratulated = isCongratulated;
	}

	protected Award(Parcel in) {
		mWinnerName = in.readString();
		mAwardName = in.readString();
		mWinnerImage = in.readString();
		isRead = in.readString();
		mAwardId = in.readString();
		isCongratulated = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mWinnerName);
		dest.writeString(mAwardName);
		dest.writeString(mWinnerImage);
		dest.writeString(isRead);
		dest.writeString(mAwardId);
		dest.writeByte((byte) (isCongratulated ? 0x01 : 0x00));
	}

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