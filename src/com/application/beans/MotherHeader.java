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
public class MotherHeader implements Parcelable {
	private String mTitle;
	private String mUnreadCount;
	private boolean mIsUnread;

	public MotherHeader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MotherHeader(String mTitle, String mUnreadCount, boolean mIsUnread) {
		super();
		this.mTitle = mTitle;
		this.mUnreadCount = mUnreadCount;
		this.mIsUnread = mIsUnread;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmUnreadCount() {
		return mUnreadCount;
	}

	public void setmUnreadCount(String mUnreadCount) {
		this.mUnreadCount = mUnreadCount;
	}

	public boolean ismIsUnread() {
		return mIsUnread;
	}

	public void setmIsUnread(boolean mIsUnread) {
		this.mIsUnread = mIsUnread;
	}

	protected MotherHeader(Parcel in) {
		mTitle = in.readString();
		mUnreadCount = in.readString();
		mIsUnread = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mUnreadCount);
		dest.writeByte((byte) (mIsUnread ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<MotherHeader> CREATOR = new Parcelable.Creator<MotherHeader>() {
		@Override
		public MotherHeader createFromParcel(Parcel in) {
			return new MotherHeader(in);
		}

		@Override
		public MotherHeader[] newArray(int size) {
			return new MotherHeader[size];
		}
	};
}
