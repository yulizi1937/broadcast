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
public class MIS implements Parcelable {
	private String mTitle;
	private String mDesc;
	private String mLink;

	public MIS() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MIS(String mTitle, String mDesc, String mLink) {
		super();
		this.mTitle = mTitle;
		this.mDesc = mDesc;
		this.mLink = mLink;
	}

	protected MIS(Parcel in) {
		mTitle = in.readString();
		mDesc = in.readString();
		mLink = in.readString();
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmDesc() {
		return mDesc;
	}

	public void setmDesc(String mDesc) {
		this.mDesc = mDesc;
	}

	public String getmLink() {
		return mLink;
	}

	public void setmLink(String mLink) {
		this.mLink = mLink;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mDesc);
		dest.writeString(mLink);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<MIS> CREATOR = new Parcelable.Creator<MIS>() {
		@Override
		public MIS createFromParcel(Parcel in) {
			return new MIS(in);
		}

		@Override
		public MIS[] newArray(int size) {
			return new MIS[size];
		}
	};
}
