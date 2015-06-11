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
public class Event implements Parcelable {
	private String mEventId;
	private String mEventTitle;
	private String mEventViewCount;
	private String mEventBy;
	private String mEventDate;
	private String mEventTime;
	private String mEventSendDate;
	private String mEventSendTime;
	private String mEventSummary;
	private boolean isRead;
	private boolean isGoingToAttend;

	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Event(String mEventId, String mEventTitle, String mEventViewCount,
			String mEventBy, String mEventDate, String mEventTime,
			String mEventSendDate, String mEventSendTime, String mEventSummary,
			boolean isRead, boolean isGoingToAttend) {
		super();
		this.mEventId = mEventId;
		this.mEventTitle = mEventTitle;
		this.mEventViewCount = mEventViewCount;
		this.mEventBy = mEventBy;
		this.mEventDate = mEventDate;
		this.mEventTime = mEventTime;
		this.mEventSendDate = mEventSendDate;
		this.mEventSendTime = mEventSendTime;
		this.mEventSummary = mEventSummary;
		this.isRead = isRead;
		this.isGoingToAttend = isGoingToAttend;
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

	public String getmEventViewCount() {
		return mEventViewCount;
	}

	public void setmEventViewCount(String mEventViewCount) {
		this.mEventViewCount = mEventViewCount;
	}

	public String getmEventBy() {
		return mEventBy;
	}

	public void setmEventBy(String mEventBy) {
		this.mEventBy = mEventBy;
	}

	public String getmEventDate() {
		return mEventDate;
	}

	public void setmEventDate(String mEventDate) {
		this.mEventDate = mEventDate;
	}

	public String getmEventTime() {
		return mEventTime;
	}

	public void setmEventTime(String mEventTime) {
		this.mEventTime = mEventTime;
	}

	public String getmEventSendDate() {
		return mEventSendDate;
	}

	public void setmEventSendDate(String mEventSendDate) {
		this.mEventSendDate = mEventSendDate;
	}

	public String getmEventSendTime() {
		return mEventSendTime;
	}

	public void setmEventSendTime(String mEventSendTime) {
		this.mEventSendTime = mEventSendTime;
	}

	public String getmEventSummary() {
		return mEventSummary;
	}

	public void setmEventSummary(String mEventSummary) {
		this.mEventSummary = mEventSummary;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isGoingToAttend() {
		return isGoingToAttend;
	}

	public void setGoingToAttend(boolean isGoingToAttend) {
		this.isGoingToAttend = isGoingToAttend;
	}

	protected Event(Parcel in) {
		mEventId = in.readString();
		mEventTitle = in.readString();
		mEventViewCount = in.readString();
		mEventBy = in.readString();
		mEventDate = in.readString();
		mEventTime = in.readString();
		mEventSendDate = in.readString();
		mEventSendTime = in.readString();
		mEventSummary = in.readString();
		isRead = in.readByte() != 0x00;
		isGoingToAttend = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mEventId);
		dest.writeString(mEventTitle);
		dest.writeString(mEventViewCount);
		dest.writeString(mEventBy);
		dest.writeString(mEventDate);
		dest.writeString(mEventTime);
		dest.writeString(mEventSendDate);
		dest.writeString(mEventSendTime);
		dest.writeString(mEventSummary);
		dest.writeByte((byte) (isRead ? 0x01 : 0x00));
		dest.writeByte((byte) (isGoingToAttend ? 0x01 : 0x00));
	}

	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		@Override
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}
	};
}