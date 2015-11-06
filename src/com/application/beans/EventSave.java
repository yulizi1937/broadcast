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
public class EventSave implements Parcelable {
	private String mEventId;
	private String mEventTitle;
	private String mEventBy;
	private String mEventDesc;
	private String mEventGoingCount;
	private String mEventLikeCount;
	private String mEventReadCount;
	private String mEventInvitedCount;
	private String mEventReceivedDate;
	private String mEventReceivedTime;
	private boolean isRead;
	private boolean isLike;
	private boolean isSharing;
	private boolean isJoin;
	private String mFileLink;
	private String mFileSize;
	private String mEventStartDate;
	private String mEventEndDate;
	private String mEventStartTime;
	private String mEventEndTime;
	private String mEventVenue;
	private String mEventMap;
	private String mEventExpiryDate;
	private String mEventExpriyTime;

	public EventSave() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventSave(String mEventId, String mEventTitle, String mEventBy,
			String mEventDesc, String mEventGoingCount, String mEventLikeCount,
			String mEventReadCount, String mEventInvitedCount,
			String mEventReceivedDate, String mEventReceivedTime,
			boolean isRead, boolean isLike, boolean isSharing, boolean isJoin,
			String mFileLink, String mFileSize, String mEventStartDate,
			String mEventEndDate, String mEventStartTime, String mEventEndTime,
			String mEventVenue, String mEventMap, String mEventExpiryDate,
			String mEventExpriyTime) {
		super();
		this.mEventId = mEventId;
		this.mEventTitle = mEventTitle;
		this.mEventBy = mEventBy;
		this.mEventDesc = mEventDesc;
		this.mEventGoingCount = mEventGoingCount;
		this.mEventLikeCount = mEventLikeCount;
		this.mEventReadCount = mEventReadCount;
		this.mEventInvitedCount = mEventInvitedCount;
		this.mEventReceivedDate = mEventReceivedDate;
		this.mEventReceivedTime = mEventReceivedTime;
		this.isRead = isRead;
		this.isLike = isLike;
		this.isSharing = isSharing;
		this.isJoin = isJoin;
		this.mFileLink = mFileLink;
		this.mFileSize = mFileSize;
		this.mEventStartDate = mEventStartDate;
		this.mEventEndDate = mEventEndDate;
		this.mEventStartTime = mEventStartTime;
		this.mEventEndTime = mEventEndTime;
		this.mEventVenue = mEventVenue;
		this.mEventMap = mEventMap;
		this.mEventExpiryDate = mEventExpiryDate;
		this.mEventExpriyTime = mEventExpriyTime;
	}

	public String getmEventId() {
		return mEventId;
	}

	public void setmEventId(String mEventId) {
		this.mEventId = mEventId;
	}

	public String getmEventTitle() {
		return mEventTitle;
	}

	public void setmEventTitle(String mEventTitle) {
		this.mEventTitle = mEventTitle;
	}

	public String getmEventBy() {
		return mEventBy;
	}

	public void setmEventBy(String mEventBy) {
		this.mEventBy = mEventBy;
	}

	public String getmEventDesc() {
		return mEventDesc;
	}

	public void setmEventDesc(String mEventDesc) {
		this.mEventDesc = mEventDesc;
	}

	public String getmEventGoingCount() {
		return mEventGoingCount;
	}

	public void setmEventGoingCount(String mEventGoingCount) {
		this.mEventGoingCount = mEventGoingCount;
	}

	public String getmEventLikeCount() {
		return mEventLikeCount;
	}

	public void setmEventLikeCount(String mEventLikeCount) {
		this.mEventLikeCount = mEventLikeCount;
	}

	public String getmEventReadCount() {
		return mEventReadCount;
	}

	public void setmEventReadCount(String mEventReadCount) {
		this.mEventReadCount = mEventReadCount;
	}

	public String getmEventInvitedCount() {
		return mEventInvitedCount;
	}

	public void setmEventInvitedCount(String mEventInvitedCount) {
		this.mEventInvitedCount = mEventInvitedCount;
	}

	public String getmEventReceivedDate() {
		return mEventReceivedDate;
	}

	public void setmEventReceivedDate(String mEventReceivedDate) {
		this.mEventReceivedDate = mEventReceivedDate;
	}

	public String getmEventReceivedTime() {
		return mEventReceivedTime;
	}

	public void setmEventReceivedTime(String mEventReceivedTime) {
		this.mEventReceivedTime = mEventReceivedTime;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public boolean isSharing() {
		return isSharing;
	}

	public void setSharing(boolean isSharing) {
		this.isSharing = isSharing;
	}

	public boolean isJoin() {
		return isJoin;
	}

	public void setJoin(boolean isJoin) {
		this.isJoin = isJoin;
	}

	public String getmFileLink() {
		return mFileLink;
	}

	public void setmFileLink(String mFileLink) {
		this.mFileLink = mFileLink;
	}

	public String getmFileSize() {
		return mFileSize;
	}

	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	public String getmEventStartDate() {
		return mEventStartDate;
	}

	public void setmEventStartDate(String mEventStartDate) {
		this.mEventStartDate = mEventStartDate;
	}

	public String getmEventEndDate() {
		return mEventEndDate;
	}

	public void setmEventEndDate(String mEventEndDate) {
		this.mEventEndDate = mEventEndDate;
	}

	public String getmEventStartTime() {
		return mEventStartTime;
	}

	public void setmEventStartTime(String mEventStartTime) {
		this.mEventStartTime = mEventStartTime;
	}

	public String getmEventEndTime() {
		return mEventEndTime;
	}

	public void setmEventEndTime(String mEventEndTime) {
		this.mEventEndTime = mEventEndTime;
	}

	public String getmEventVenue() {
		return mEventVenue;
	}

	public void setmEventVenue(String mEventVenue) {
		this.mEventVenue = mEventVenue;
	}

	public String getmEventMap() {
		return mEventMap;
	}

	public void setmEventMap(String mEventMap) {
		this.mEventMap = mEventMap;
	}

	public String getmEventExpiryDate() {
		return mEventExpiryDate;
	}

	public void setmEventExpiryDate(String mEventExpiryDate) {
		this.mEventExpiryDate = mEventExpiryDate;
	}

	public String getmEventExpriyTime() {
		return mEventExpriyTime;
	}

	public void setmEventExpriyTime(String mEventExpriyTime) {
		this.mEventExpriyTime = mEventExpriyTime;
	}

	protected EventSave(Parcel in) {
		mEventId = in.readString();
		mEventTitle = in.readString();
		mEventBy = in.readString();
		mEventDesc = in.readString();
		mEventGoingCount = in.readString();
		mEventLikeCount = in.readString();
		mEventReadCount = in.readString();
		mEventInvitedCount = in.readString();
		mEventReceivedDate = in.readString();
		mEventReceivedTime = in.readString();
		isRead = in.readByte() != 0x00;
		isLike = in.readByte() != 0x00;
		isSharing = in.readByte() != 0x00;
		isJoin = in.readByte() != 0x00;
		mFileLink = in.readString();
		mFileSize = in.readString();
		mEventStartDate = in.readString();
		mEventEndDate = in.readString();
		mEventStartTime = in.readString();
		mEventEndTime = in.readString();
		mEventVenue = in.readString();
		mEventMap = in.readString();
		mEventExpiryDate = in.readString();
		mEventExpriyTime = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mEventId);
		dest.writeString(mEventTitle);
		dest.writeString(mEventBy);
		dest.writeString(mEventDesc);
		dest.writeString(mEventGoingCount);
		dest.writeString(mEventLikeCount);
		dest.writeString(mEventReadCount);
		dest.writeString(mEventInvitedCount);
		dest.writeString(mEventReceivedDate);
		dest.writeString(mEventReceivedTime);
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isLike ? 0x01 : 0x00));
		dest.writeValue(isSharing);
		dest.writeByte((byte) (isJoin ? 0x01 : 0x00));
		dest.writeString(mFileLink);
		dest.writeString(mFileSize);
		dest.writeString(mEventStartDate);
		dest.writeString(mEventEndDate);
		dest.writeString(mEventStartTime);
		dest.writeString(mEventEndTime);
		dest.writeString(mEventVenue);
		dest.writeString(mEventMap);
		dest.writeString(mEventExpiryDate);
		dest.writeString(mEventExpriyTime);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<EventSave> CREATOR = new Parcelable.Creator<EventSave>() {
		@Override
		public EventSave createFromParcel(Parcel in) {
			return new EventSave(in);
		}

		@Override
		public EventSave[] newArray(int size) {
			return new EventSave[size];
		}
	};
}
