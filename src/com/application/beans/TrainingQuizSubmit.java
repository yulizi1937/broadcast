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
public class TrainingQuizSubmit implements Parcelable {
	private String trainingQuizId;
	private String trainingQuizQueId;
	private String trainingQuizQueType;
	private String trainingQuizAnswer;

	public TrainingQuizSubmit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrainingQuizSubmit(String trainingQuizId, String trainingQuizQueId,
			String trainingQuizQueType, String trainingQuizAnswer) {
		super();
		this.trainingQuizId = trainingQuizId;
		this.trainingQuizQueId = trainingQuizQueId;
		this.trainingQuizQueType = trainingQuizQueType;
		this.trainingQuizAnswer = trainingQuizAnswer;
	}

	public String getTrainingQuizId() {
		return trainingQuizId;
	}

	public void setTrainingQuizId(String trainingQuizId) {
		this.trainingQuizId = trainingQuizId;
	}

	public String getTrainingQuizQueId() {
		return trainingQuizQueId;
	}

	public void setTrainingQuizQueId(String trainingQuizQueId) {
		this.trainingQuizQueId = trainingQuizQueId;
	}

	public String getTrainingQuizQueType() {
		return trainingQuizQueType;
	}

	public void setTrainingQuizQueType(String trainingQuizQueType) {
		this.trainingQuizQueType = trainingQuizQueType;
	}

	public String getTrainingQuizAnswer() {
		return trainingQuizAnswer;
	}

	public void setTrainingQuizAnswer(String trainingQuizAnswer) {
		this.trainingQuizAnswer = trainingQuizAnswer;
	}

	protected TrainingQuizSubmit(Parcel in) {
		trainingQuizId = in.readString();
		trainingQuizQueId = in.readString();
		trainingQuizQueType = in.readString();
		trainingQuizAnswer = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(trainingQuizId);
		dest.writeString(trainingQuizQueId);
		dest.writeString(trainingQuizQueType);
		dest.writeString(trainingQuizAnswer);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<TrainingQuizSubmit> CREATOR = new Parcelable.Creator<TrainingQuizSubmit>() {
		@Override
		public TrainingQuizSubmit createFromParcel(Parcel in) {
			return new TrainingQuizSubmit(in);
		}

		@Override
		public TrainingQuizSubmit[] newArray(int size) {
			return new TrainingQuizSubmit[size];
		}
	};
}
