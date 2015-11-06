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
public class MobcastFeedbackSubmit implements Parcelable {
	private String mobcastFeedbackId;
	private String mobcastFeedbackQueId;
	private String mobcastFeedbackQueType;
	private String mobcastFeedbackAnswer;

	public MobcastFeedbackSubmit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MobcastFeedbackSubmit(String mobcastFeedbackId,
			String mobcastFeedbackQueId, String mobcastFeedbackQueType,
			String mobcastFeedbackAnswer) {
		super();
		this.mobcastFeedbackId = mobcastFeedbackId;
		this.mobcastFeedbackQueId = mobcastFeedbackQueId;
		this.mobcastFeedbackQueType = mobcastFeedbackQueType;
		this.mobcastFeedbackAnswer = mobcastFeedbackAnswer;
	}

	public String getMobcastFeedbackId() {
		return mobcastFeedbackId;
	}

	public void setMobcastFeedbackId(String mobcastFeedbackId) {
		this.mobcastFeedbackId = mobcastFeedbackId;
	}

	public String getMobcastFeedbackQueId() {
		return mobcastFeedbackQueId;
	}

	public void setMobcastFeedbackQueId(String mobcastFeedbackQueId) {
		this.mobcastFeedbackQueId = mobcastFeedbackQueId;
	}

	public String getMobcastFeedbackQueType() {
		return mobcastFeedbackQueType;
	}

	public void setMobcastFeedbackQueType(String mobcastFeedbackQueType) {
		this.mobcastFeedbackQueType = mobcastFeedbackQueType;
	}

	public String getMobcastFeedbackAnswer() {
		return mobcastFeedbackAnswer;
	}

	public void setMobcastFeedbackAnswer(String mobcastFeedbackAnswer) {
		this.mobcastFeedbackAnswer = mobcastFeedbackAnswer;
	}

	protected MobcastFeedbackSubmit(Parcel in) {
		mobcastFeedbackId = in.readString();
		mobcastFeedbackQueId = in.readString();
		mobcastFeedbackQueType = in.readString();
		mobcastFeedbackAnswer = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mobcastFeedbackId);
		dest.writeString(mobcastFeedbackQueId);
		dest.writeString(mobcastFeedbackQueType);
		dest.writeString(mobcastFeedbackAnswer);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<MobcastFeedbackSubmit> CREATOR = new Parcelable.Creator<MobcastFeedbackSubmit>() {
		@Override
		public MobcastFeedbackSubmit createFromParcel(Parcel in) {
			return new MobcastFeedbackSubmit(in);
		}

		@Override
		public MobcastFeedbackSubmit[] newArray(int size) {
			return new MobcastFeedbackSubmit[size];
		}
	};
}
