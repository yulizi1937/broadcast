/**
 * 
 */
package com.application.widget;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class WidgetItem implements Parcelable {
	private String mType;
	private String mCategory;
	private String mTitle;
	private String mDesc;
	private String mId;

	public WidgetItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WidgetItem(String mType, String mCategory, String mTitle,
			String mDesc, String mId) {
		super();
		this.mType = mType;
		this.mCategory = mCategory;
		this.mTitle = mTitle;
		this.mDesc = mDesc;
		this.mId = mId;
	}

	protected WidgetItem(Parcel in) {
		mType = in.readString();
		mCategory = in.readString();
		mTitle = in.readString();
		mDesc = in.readString();
		mId = in.readString();
	}

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public String getmCategory() {
		return mCategory;
	}

	public void setmCategory(String mCategory) {
		this.mCategory = mCategory;
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

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mType);
		dest.writeString(mCategory);
		dest.writeString(mTitle);
		dest.writeString(mDesc);
		dest.writeString(mId);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<WidgetItem> CREATOR = new Parcelable.Creator<WidgetItem>() {
		@Override
		public WidgetItem createFromParcel(Parcel in) {
			return new WidgetItem(in);
		}

		@Override
		public WidgetItem[] newArray(int size) {
			return new WidgetItem[size];
		}
	};
}
