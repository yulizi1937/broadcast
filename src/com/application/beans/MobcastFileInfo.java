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
public class MobcastFileInfo implements Parcelable {
	private String mDuration;
	private String mFileId;
	private String mPages;
	private String mQuestions;
	private String mFileLanguages;
	private String mSelectedLanguage;
	private String mFilePath;
	private String mFileLink;
	private String mThumbnailLink;
	private String mThumbnailPath;
	private String mFileName;
	private String mFileSize;
	private String mFileIsDefault;

	public MobcastFileInfo(String mDuration, String mFileId, String mPages,
			String mQuestions, String mFileLanguages, String mSelectedLanguage,
			String mFilePath, String mFileLink, String mThumbnailLink,
			String mThumbnailPath, String mFileName, String mFileSize,
			String mFileIsDefault) {
		super();
		this.mDuration = mDuration;
		this.mFileId = mFileId;
		this.mPages = mPages;
		this.mQuestions = mQuestions;
		this.mFileLanguages = mFileLanguages;
		this.mSelectedLanguage = mSelectedLanguage;
		this.mFilePath = mFilePath;
		this.mFileLink = mFileLink;
		this.mThumbnailLink = mThumbnailLink;
		this.mThumbnailPath = mThumbnailPath;
		this.mFileName = mFileName;
		this.mFileSize = mFileSize;
		this.mFileIsDefault = mFileIsDefault;
	}

	public MobcastFileInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getmDuration() {
		return mDuration;
	}

	public void setmDuration(String mDuration) {
		this.mDuration = mDuration;
	}

	public String getmFileId() {
		return mFileId;
	}

	public void setmFileId(String mFileId) {
		this.mFileId = mFileId;
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

	public String getmFileLanguages() {
		return mFileLanguages;
	}

	public void setmFileLanguages(String mFileLanguages) {
		this.mFileLanguages = mFileLanguages;
	}

	public String getmSelectedLanguage() {
		return mSelectedLanguage;
	}

	public void setmSelectedLanguage(String mSelectedLanguage) {
		this.mSelectedLanguage = mSelectedLanguage;
	}

	public String getmFilePath() {
		return mFilePath;
	}

	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

	public String getmFileLink() {
		return mFileLink;
	}

	public void setmFileLink(String mFileLink) {
		this.mFileLink = mFileLink;
	}

	public String getmThumbnailLink() {
		return mThumbnailLink;
	}

	public void setmThumbnailLink(String mThumbnailLink) {
		this.mThumbnailLink = mThumbnailLink;
	}

	public String getmThumbnailPath() {
		return mThumbnailPath;
	}

	public void setmThumbnailPath(String mThumbnailPath) {
		this.mThumbnailPath = mThumbnailPath;
	}

	public String getmFileName() {
		return mFileName;
	}

	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	public String getmFileSize() {
		return mFileSize;
	}

	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	public String getmFileIsDefault() {
		return mFileIsDefault;
	}

	public void setmFileIsDefault(String mFileIsDefault) {
		this.mFileIsDefault = mFileIsDefault;
	}

	protected MobcastFileInfo(Parcel in) {
		mDuration = in.readString();
		mFileId = in.readString();
		mPages = in.readString();
		mQuestions = in.readString();
		mFileLanguages = in.readString();
		mSelectedLanguage = in.readString();
		mFilePath = in.readString();
		mFileLink = in.readString();
		mThumbnailLink = in.readString();
		mThumbnailPath = in.readString();
		mFileName = in.readString();
		mFileSize = in.readString();
		mFileIsDefault = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mDuration);
		dest.writeString(mFileId);
		dest.writeString(mPages);
		dest.writeString(mQuestions);
		dest.writeString(mFileLanguages);
		dest.writeString(mSelectedLanguage);
		dest.writeString(mFilePath);
		dest.writeString(mFileLink);
		dest.writeString(mThumbnailLink);
		dest.writeString(mThumbnailPath);
		dest.writeString(mFileName);
		dest.writeString(mFileSize);
		dest.writeString(mFileIsDefault);
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
