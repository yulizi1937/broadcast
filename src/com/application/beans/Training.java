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
public class Training implements Parcelable {
	private String mTitle;
	private String mViewCount;
	private String mBy;
	private String mDate;
	private String mTime;
	private String mSummary;
	private String mFileType;
	private TrainingFileInfo mFileInfo;

	public Training() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Training(String mTitle, String mViewCount, String mBy, String mDate,
			String mTime, String mSummary, String mFileType,
			TrainingFileInfo mFileInfo) {
		super();
		this.mTitle = mTitle;
		this.mViewCount = mViewCount;
		this.mBy = mBy;
		this.mDate = mDate;
		this.mTime = mTime;
		this.mSummary = mSummary;
		this.mFileType = mFileType;
		this.mFileInfo = mFileInfo;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmViewCount() {
		return mViewCount;
	}

	public void setmViewCount(String mViewCount) {
		this.mViewCount = mViewCount;
	}

	public String getmBy() {
		return mBy;
	}

	public void setmBy(String mBy) {
		this.mBy = mBy;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	public String getmTime() {
		return mTime;
	}

	public void setmTime(String mTime) {
		this.mTime = mTime;
	}

	public String getmSummary() {
		return mSummary;
	}

	public void setmSummary(String mSummary) {
		this.mSummary = mSummary;
	}

	public String getmFileType() {
		return mFileType;
	}

	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	public TrainingFileInfo getmFileInfo() {
		return mFileInfo;
	}

	public void setmFileInfo(TrainingFileInfo mFileInfo) {
		this.mFileInfo = mFileInfo;
	}

	protected Training(Parcel in) {
		mTitle = in.readString();
		mViewCount = in.readString();
		mBy = in.readString();
		mDate = in.readString();
		mTime = in.readString();
		mSummary = in.readString();
		mFileType = in.readString();
		mFileInfo = (TrainingFileInfo) in.readValue(TrainingFileInfo.class
				.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mViewCount);
		dest.writeString(mBy);
		dest.writeString(mDate);
		dest.writeString(mTime);
		dest.writeString(mSummary);
		dest.writeString(mFileType);
		dest.writeValue(mFileInfo);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Training> CREATOR = new Parcelable.Creator<Training>() {
		@Override
		public Training createFromParcel(Parcel in) {
			return new Training(in);
		}

		@Override
		public Training[] newArray(int size) {
			return new Training[size];
		}
	};
}
