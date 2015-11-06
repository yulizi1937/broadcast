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
public class SearchHeader implements Parcelable {
	private String mTitle;
	private boolean mIsUnread;

	public SearchHeader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchHeader(String mTitle, boolean mIsUnread) {
		super();
		this.mTitle = mTitle;
		this.mIsUnread = mIsUnread;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public boolean ismIsUnread() {
		return mIsUnread;
	}

	public void setmIsUnread(boolean mIsUnread) {
		this.mIsUnread = mIsUnread;
	}

	protected SearchHeader(Parcel in) {
		mTitle = in.readString();
		mIsUnread = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeByte((byte) (mIsUnread ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<SearchHeader> CREATOR = new Parcelable.Creator<SearchHeader>() {
		@Override
		public SearchHeader createFromParcel(Parcel in) {
			return new SearchHeader(in);
		}

		@Override
		public SearchHeader[] newArray(int size) {
			return new SearchHeader[size];
		}
	};
}
