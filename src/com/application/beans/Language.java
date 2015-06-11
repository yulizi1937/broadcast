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
public class Language implements Parcelable {
	private String mLanguage;
	private String mLanguageCode;

	public Language() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Language(String mLanguage, String mLanguageCode) {
		super();
		this.mLanguage = mLanguage;
		this.mLanguageCode = mLanguageCode;
	}

	public String getmLanguage() {
		return mLanguage;
	}

	public void setmLanguage(String mLanguage) {
		this.mLanguage = mLanguage;
	}

	public String getmLanguageCode() {
		return mLanguageCode;
	}

	public void setmLanguageCode(String mLanguageCode) {
		this.mLanguageCode = mLanguageCode;
	}

	protected Language(Parcel in) {
		mLanguage = in.readString();
		mLanguageCode = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mLanguage);
		dest.writeString(mLanguageCode);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>() {
		@Override
		public Language createFromParcel(Parcel in) {
			return new Language(in);
		}

		@Override
		public Language[] newArray(int size) {
			return new Language[size];
		}
	};
}
