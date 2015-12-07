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
public class ParichayReferral implements Parcelable {
	private String mJobId;
	private String mJobUnit;
	private String mReferredId;
	private String mDate;
	private String mName;
	private String mReferredFor;
	private String mType;
	private String mReason;
	private int isTelephone;
	private int isOnlineWritten;
	private int isPR1;
	private int isPR2;
	private int isHR;
	private int isInstallment1;
	private int isInstallment2;
	private int isInstallment3;
	private int install;
	private int isDuplicate;
	private boolean isExpand;

	public ParichayReferral() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParichayReferral(String mJobId, String mJobUnit, String mReferredId,
			String mDate, String mName, String mReferredFor, String mType,
			String mReason, int isTelephone, int isOnlineWritten, int isPR1,
			int isPR2, int isHR, int isInstallment1, int isInstallment2,
			int isInstallment3, int install, int isDuplicate, boolean isExpand) {
		super();
		this.mJobId = mJobId;
		this.mJobUnit = mJobUnit;
		this.mReferredId = mReferredId;
		this.mDate = mDate;
		this.mName = mName;
		this.mReferredFor = mReferredFor;
		this.mType = mType;
		this.mReason = mReason;
		this.isTelephone = isTelephone;
		this.isOnlineWritten = isOnlineWritten;
		this.isPR1 = isPR1;
		this.isPR2 = isPR2;
		this.isHR = isHR;
		this.isInstallment1 = isInstallment1;
		this.isInstallment2 = isInstallment2;
		this.isInstallment3 = isInstallment3;
		this.install = install;
		this.isDuplicate = isDuplicate;
		this.isExpand = isExpand;
	}

	public String getmJobId() {
		return mJobId;
	}

	public void setmJobId(String mJobId) {
		this.mJobId = mJobId;
	}

	public String getmJobUnit() {
		return mJobUnit;
	}

	public void setmJobUnit(String mJobUnit) {
		this.mJobUnit = mJobUnit;
	}

	public String getmReferredId() {
		return mReferredId;
	}

	public void setmReferredId(String mReferredId) {
		this.mReferredId = mReferredId;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmReferredFor() {
		return mReferredFor;
	}

	public void setmReferredFor(String mReferredFor) {
		this.mReferredFor = mReferredFor;
	}

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public String getmReason() {
		return mReason;
	}

	public void setmReason(String mReason) {
		this.mReason = mReason;
	}

	public int getIsTelephone() {
		return isTelephone;
	}

	public void setIsTelephone(int isTelephone) {
		this.isTelephone = isTelephone;
	}

	public int getIsOnlineWritten() {
		return isOnlineWritten;
	}

	public void setIsOnlineWritten(int isOnlineWritten) {
		this.isOnlineWritten = isOnlineWritten;
	}

	public int getIsPR1() {
		return isPR1;
	}

	public void setIsPR1(int isPR1) {
		this.isPR1 = isPR1;
	}

	public int getIsPR2() {
		return isPR2;
	}

	public void setIsPR2(int isPR2) {
		this.isPR2 = isPR2;
	}

	public int getIsHR() {
		return isHR;
	}

	public void setIsHR(int isHR) {
		this.isHR = isHR;
	}

	public int getIsInstallment1() {
		return isInstallment1;
	}

	public void setIsInstallment1(int isInstallment1) {
		this.isInstallment1 = isInstallment1;
	}

	public int getIsInstallment2() {
		return isInstallment2;
	}

	public void setIsInstallment2(int isInstallment2) {
		this.isInstallment2 = isInstallment2;
	}

	public int getIsInstallment3() {
		return isInstallment3;
	}

	public void setIsInstallment3(int isInstallment3) {
		this.isInstallment3 = isInstallment3;
	}

	public int getInstall() {
		return install;
	}

	public void setInstall(int install) {
		this.install = install;
	}

	public int getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(int isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}

	protected ParichayReferral(Parcel in) {
		mJobId = in.readString();
		mJobUnit = in.readString();
		mReferredId = in.readString();
		mDate = in.readString();
		mName = in.readString();
		mReferredFor = in.readString();
		mType = in.readString();
		mReason = in.readString();
		isTelephone = in.readInt();
		isOnlineWritten = in.readInt();
		isPR1 = in.readInt();
		isPR2 = in.readInt();
		isHR = in.readInt();
		isInstallment1 = in.readInt();
		isInstallment2 = in.readInt();
		isInstallment3 = in.readInt();
		install = in.readInt();
		isDuplicate = in.readInt();
		isExpand = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mJobId);
		dest.writeString(mJobUnit);
		dest.writeString(mReferredId);
		dest.writeString(mDate);
		dest.writeString(mName);
		dest.writeString(mReferredFor);
		dest.writeString(mType);
		dest.writeString(mReason);
		dest.writeInt(isTelephone);
		dest.writeInt(isOnlineWritten);
		dest.writeInt(isPR1);
		dest.writeInt(isPR2);
		dest.writeInt(isHR);
		dest.writeInt(isInstallment1);
		dest.writeInt(isInstallment2);
		dest.writeInt(isInstallment3);
		dest.writeInt(install);
		dest.writeInt(isDuplicate);
		dest.writeByte((byte) (isExpand ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ParichayReferral> CREATOR = new Parcelable.Creator<ParichayReferral>() {
		@Override
		public ParichayReferral createFromParcel(Parcel in) {
			return new ParichayReferral(in);
		}

		@Override
		public ParichayReferral[] newArray(int size) {
			return new ParichayReferral[size];
		}
	};
}