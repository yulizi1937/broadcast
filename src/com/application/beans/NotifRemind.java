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
public class NotifRemind implements Parcelable {
	private String mTitle;
	private String mCategory;
	private int mType;
	private String mId;

	public NotifRemind() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotifRemind(String mTitle, String mCategory, int mType, String mId) {
		super();
		this.mTitle = mTitle;
		this.mCategory = mCategory;
		this.mType = mType;
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmCategory() {
		return mCategory;
	}

	public void setmCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public int getmType() {
		return mType;
	}

	public void setmType(int mType) {
		this.mType = mType;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	protected NotifRemind(Parcel in) {
		mTitle = in.readString();
		mCategory = in.readString();
		mType = in.readInt();
		mId = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mCategory);
		dest.writeInt(mType);
		dest.writeString(mId);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<NotifRemind> CREATOR = new Parcelable.Creator<NotifRemind>() {
		@Override
		public NotifRemind createFromParcel(Parcel in) {
			return new NotifRemind(in);
		}

		@Override
		public NotifRemind[] newArray(int size) {
			return new NotifRemind[size];
		}
	};
}