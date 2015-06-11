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
public class TrainingFileInfo implements Parcelable {
	private ArrayList<String> mFilePath;
	private String mDuration;
	private String mPages;
	private String mQuestions;
	private String mTimeTaken;
	private String mAttempts;
	private String mScore;
	private ArrayList<String> mFileLanguages;
	private String mSelectedLanguage;

	public TrainingFileInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrainingFileInfo(ArrayList<String> mFilePath, String mDuration,
			String mPages, String mQuestions, String mTimeTaken,
			String mAttempts, String mScore, ArrayList<String> mFileLanguages,
			String mSelectedLanguage) {
		super();
		this.mFilePath = mFilePath;
		this.mDuration = mDuration;
		this.mPages = mPages;
		this.mQuestions = mQuestions;
		this.mTimeTaken = mTimeTaken;
		this.mAttempts = mAttempts;
		this.mScore = mScore;
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

	public String getmTimeTaken() {
		return mTimeTaken;
	}

	public void setmTimeTaken(String mTimeTaken) {
		this.mTimeTaken = mTimeTaken;
	}

	public String getmAttempts() {
		return mAttempts;
	}

	public void setmAttempts(String mAttempts) {
		this.mAttempts = mAttempts;
	}

	public String getmScore() {
		return mScore;
	}

	public void setmScore(String mScore) {
		this.mScore = mScore;
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

	protected TrainingFileInfo(Parcel in) {
		if (in.readByte() == 0x01) {
			mFilePath = new ArrayList<String>();
			in.readList(mFilePath, String.class.getClassLoader());
		} else {
			mFilePath = null;
		}
		mDuration = in.readString();
		mPages = in.readString();
		mQuestions = in.readString();
		mTimeTaken = in.readString();
		mAttempts = in.readString();
		mScore = in.readString();
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
		dest.writeString(mTimeTaken);
		dest.writeString(mAttempts);
		dest.writeString(mScore);
		if (mFileLanguages == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(mFileLanguages);
		}
		dest.writeString(mSelectedLanguage);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<TrainingFileInfo> CREATOR = new Parcelable.Creator<TrainingFileInfo>() {
		@Override
		public TrainingFileInfo createFromParcel(Parcel in) {
			return new TrainingFileInfo(in);
		}

		@Override
		public TrainingFileInfo[] newArray(int size) {
			return new TrainingFileInfo[size];
		}
	};
}
