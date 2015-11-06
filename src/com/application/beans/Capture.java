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
public class Capture implements Parcelable {
	private String mId;
	private String mFilePath;
	private String mFileThumbnailPath;
	private String mFileSize;

	public Capture() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Capture(String mId, String mFilePath, String mFileThumbnailPath,
			String mFileSize) {
		super();
		this.mId = mId;
		this.mFilePath = mFilePath;
		this.mFileThumbnailPath = mFileThumbnailPath;
		this.mFileSize = mFileSize;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmFilePath() {
		return mFilePath;
	}

	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

	public String getmFileThumbnailPath() {
		return mFileThumbnailPath;
	}

	public void setmFileThumbnailPath(String mFileThumbnailPath) {
		this.mFileThumbnailPath = mFileThumbnailPath;
	}

	public String getmFileSize() {
		return mFileSize;
	}

	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	protected Capture(Parcel in) {
		mId = in.readString();
		mFilePath = in.readString();
		mFileThumbnailPath = in.readString();
		mFileSize = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mFilePath);
		dest.writeString(mFileThumbnailPath);
		dest.writeString(mFileSize);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Capture> CREATOR = new Parcelable.Creator<Capture>() {
		@Override
		public Capture createFromParcel(Parcel in) {
			return new Capture(in);
		}

		@Override
		public Capture[] newArray(int size) {
			return new Capture[size];
		}
	};
}
