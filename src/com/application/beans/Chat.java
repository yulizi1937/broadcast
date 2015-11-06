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
public class Chat implements Parcelable {
	private String mName;
	private String mMessage;
	private String isRead;
	private String mUnreadCount;
	private String mLastMessageTime;
	private String mUserDpLink;
	private String mUserDpPath;

	public Chat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Chat(String mName, String mMessage, String isRead,
			String mUnreadCount, String mLastMessageTime, String mUserDpLink,
			String mUserDpPath) {
		super();
		this.mName = mName;
		this.mMessage = mMessage;
		this.isRead = isRead;
		this.mUnreadCount = mUnreadCount;
		this.mLastMessageTime = mLastMessageTime;
		this.mUserDpLink = mUserDpLink;
		this.mUserDpPath = mUserDpPath;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmMessage() {
		return mMessage;
	}

	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getmUnreadCount() {
		return mUnreadCount;
	}

	public void setmUnreadCount(String mUnreadCount) {
		this.mUnreadCount = mUnreadCount;
	}

	public String getmLastMessageTime() {
		return mLastMessageTime;
	}

	public void setmLastMessageTime(String mLastMessageTime) {
		this.mLastMessageTime = mLastMessageTime;
	}

	public String getmUserDpLink() {
		return mUserDpLink;
	}

	public void setmUserDpLink(String mUserDpLink) {
		this.mUserDpLink = mUserDpLink;
	}

	public String getmUserDpPath() {
		return mUserDpPath;
	}

	public void setmUserDpPath(String mUserDpPath) {
		this.mUserDpPath = mUserDpPath;
	}

	public static Parcelable.Creator<Chat> getCreator() {
		return CREATOR;
	}

	protected Chat(Parcel in) {
		mName = in.readString();
		mMessage = in.readString();
		isRead = in.readString();
		mUnreadCount = in.readString();
		mLastMessageTime = in.readString();
		mUserDpLink = in.readString();
		mUserDpPath = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mMessage);
		dest.writeString(isRead);
		dest.writeString(mUnreadCount);
		dest.writeString(mLastMessageTime);
		dest.writeString(mUserDpLink);
		dest.writeString(mUserDpPath);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Chat> CREATOR = new Parcelable.Creator<Chat>() {
		@Override
		public Chat createFromParcel(Parcel in) {
			return new Chat(in);
		}

		@Override
		public Chat[] newArray(int size) {
			return new Chat[size];
		}
	};
}
