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
public class AnnouncementPagerHeader implements Parcelable {
	private String mTitle;
	private String mUnreadCount;

	public AnnouncementPagerHeader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnnouncementPagerHeader(String mTitle, String mUnreadCount) {
		super();
		this.mTitle = mTitle;
		this.mUnreadCount = mUnreadCount;
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

	protected AnnouncementPagerHeader(Parcel in) {
		mTitle = in.readString();
		mUnreadCount = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mUnreadCount);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<AnnouncementPagerHeader> CREATOR = new Parcelable.Creator<AnnouncementPagerHeader>() {
		@Override
		public AnnouncementPagerHeader createFromParcel(Parcel in) {
			return new AnnouncementPagerHeader(in);
		}

		@Override
		public AnnouncementPagerHeader[] newArray(int size) {
			return new AnnouncementPagerHeader[size];
		}
	};
}
