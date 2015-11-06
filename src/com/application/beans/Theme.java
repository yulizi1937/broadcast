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
public class Theme implements Parcelable {
	private String mColor;
	private boolean isSelected;

	public Theme() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Theme(String mColor, boolean isSelected) {
		super();
		this.mColor = mColor;
		this.isSelected = isSelected;
	}

	public String getmColor() {
		return mColor;
	}

	public void setmColor(String mColor) {
		this.mColor = mColor;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	protected Theme(Parcel in) {
		mColor = in.readString();
		isSelected = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mColor);
		dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Theme> CREATOR = new Parcelable.Creator<Theme>() {
		@Override
		public Theme createFromParcel(Parcel in) {
			return new Theme(in);
		}

		@Override
		public Theme[] newArray(int size) {
			return new Theme[size];
		}
	};
}
