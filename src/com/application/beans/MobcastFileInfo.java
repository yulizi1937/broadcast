/**
 * 
 */
package com.application.beans;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class MobcastFileInfo implements Parcelable {
	private ArrayList<String> mFilePath;
	private String mDuration;
	private String mPages;
	private String mQuestions;
	private ArrayList<String> mFileLanguages;
	private String mSelectedLanguage;

	public MobcastFileInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MobcastFileInfo(ArrayList<String> mFilePath, String mDuration,
			String mPages, String mQuestions, ArrayList<String> mFileLanguages,
			String mSelectedLanguage) {
		super();
		this.mFilePath = mFilePath;
		this.mDuration = mDuration;
		this.mPages = mPages;
		this.mQuestions = mQuestions;
		this.mFileLanguages = mFileLanguages;
		this.mSelectedLanguage = mSelectedLanguage;
	}

	public ArrayList<String> getmFilePath() {
		return mFilePath;
	}

	public void setmFilePath(ArrayList<String> mFilePath) {
		this.mFilePath = mFilePath;
	}

	public String getmDuration() {
		return mDuration;
	}

	public void setmDuration(String mDuration) {
		this.mDuration = mDuration;
	}

	public String getmPages() {
		return mPages;
	}

	public void setmPages(String mPages) {
		this.mPages = mPages;
	}

	public String getmQuestions() {
		return mQuestions;
	}

	public void setmQuestions(String mQuestions) {
		this.mQuestions = mQuestions;
	}

	public ArrayList<String> getmFileLanguages() {
		return mFileLanguages;
	}

	public void setmFileLanguages(ArrayList<String> mFileLanguages) {
		this.mFileLanguages = mFileLanguages;
	}

	public String getmSelectedLanguage() {
		return mSelectedLanguage;
	}

	public void setmSelectedLanguage(String mSelectedLanguage) {
		this.mSelectedLanguage = mSelectedLanguage;
	}

	protected MobcastFileInfo(Parcel in) {
		if (in.readByte() == 0x01) {
			mFilePath = new ArrayList<String>();
			in.readList(mFilePath, String.class.getClassLoader());
		} else {
			mFilePath = null;
		}
		mDuration = in.readString();
		mPages = in.readString();
		mQuestions = in.readString();
		if (in.readByte() == 0x01) {
			mFileLanguages = new ArrayList<String>();
			in.readList(mFileLanguages, String.class.getClassLoader());
		} else {
			mFileLanguages = null;
		}
		mSelectedLanguage = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (mFilePath == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(mFilePath);
		}
		dest.writeString(mDuration);
		dest.writeString(mPages);
		dest.writeString(mQuestions);
		if (mFileLanguages == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(mFileLanguages);
		}
		dest.writeString(mSelectedLanguage);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<MobcastFileInfo> CREATOR = new Parcelable.Creator<MobcastFileInfo>() {
		@Override
		public MobcastFileInfo createFromParcel(Parcel in) {
			return new MobcastFileInfo(in);
		}

		@Override
		public MobcastFileInfo[] newArray(int size) {
			return new MobcastFileInfo[size];
		}
	};
}
