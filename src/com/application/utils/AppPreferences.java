package com.application.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

	SharedPreferences sharedPreferences;
	Editor editor;

	public AppPreferences(Context context) {
		// TODO Auto-generated constructor stub
		sharedPreferences = context.getSharedPreferences("Cache",
				Context.MODE_PRIVATE);
	}
	
	public SharedPreferences getAppPreferences(){
		return sharedPreferences;
	}

	/*
	 * PREFERENCES : API DETAILS
	 */

	public void setApiKey(String str) {
		editor = sharedPreferences.edit();
		editor.putString("API_KEY", str);
		editor.commit();
	}

	public String getApiKey() {
		String flag = sharedPreferences.getString("API_KEY", null);
		return flag;
	}
	
	public void setAccessToken(String str) {
		editor = sharedPreferences.edit();
		editor.putString("accessToken", str);
		editor.commit();
	}

	public String getAccessToken() {
		String flag = sharedPreferences.getString("accessToken", null);
		return flag;
	}
	
	
	/*
	 * PREFERENCES : LOGIN VIA SUPPORT : OTP
	 */

	public void setLoginViaSupport(String str) {
		editor = sharedPreferences.edit();
		editor.putString("loginViaSupport", str);
		editor.commit();
	}

	public String getLoginViaSupport() {
		String flag = sharedPreferences.getString("loginViaSupport", "Mobcast Support");
		return flag;
	}
	
	public void setToByPassOTP(boolean flag) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isToByPassOTP", flag);
		editor.commit();
	}

	public boolean isToByPassOTP() {
		return sharedPreferences.getBoolean("isToByPassOTP", true);
	}
	
	public void setAttemptedToLoginDidntReceiveOTP(boolean flag) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isAttemptedToLoginDidntReceiveOTP", flag);
		editor.commit();
	}

	public boolean isAttemptedToLoginDidntReceiveOTP() {
		return sharedPreferences.getBoolean("isAttemptedToLoginDidntReceiveOTP", false);
	}
	
	/*
	 * PREFERENCES : APPLICATION DETAILS
	 */

	public void setFirstTime(boolean flag) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isFirstTime", flag);
		editor.commit();
	}

	public boolean getFirstTime() {
		return sharedPreferences.getBoolean("isFirstTime", false);
	}
	
	public void setLoggedIn(boolean flag) {
		editor = sharedPreferences.edit();
		editor.putBoolean("setLoggedIn", flag);
		editor.commit();
	}

	public boolean getLoggedIn() {
		return sharedPreferences.getBoolean("setLoggedIn", false);
	}
	
	/*
	 * PREFERENCES : USER DETAILS
	 */
	
	public void setRegId(String str) {
		editor = sharedPreferences.edit();
		editor.putString("_regId_", str);
		editor.commit();
	}

	public String getRegId() {
		String flag = sharedPreferences.getString("_regId_", null);
		return flag;
	}

	public void setUserId(String str) {
		editor = sharedPreferences.edit();
		editor.putString("_userId_", str);
		editor.commit();
	}

	public String getUserId() {
		String flag = sharedPreferences.getString("_userId_", null);
		return flag;
	}
	
	public void setUserName(String str) {
		editor = sharedPreferences.edit();
		editor.putString("_userName_", str);
		editor.commit();
	}

	public String getUserName() {
		String flag = sharedPreferences.getString("_userName_", null);
		return flag;
	}
	
	public void setName(String str) {
		editor = sharedPreferences.edit();
		editor.putString("_name", str);
		editor.commit();
	}

	public String getName() {
		String flag = sharedPreferences.getString("_name", null);
		return flag;
	}

	public void setDisplayName(String str) {
		editor = sharedPreferences.edit();
		editor.putString("displayname", str);
		editor.commit();
	}

	public String getDisplayName() {
		String flag = sharedPreferences.getString("displayname", null);
		return flag;
	}

	public void setUserPassword(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userPassword", str);
		editor.commit();
	}

	public String getUserPassword() {
		String flag = sharedPreferences.getString("userPassword", null);
		return flag;
	}
	
	public void setUserEmailAddress(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userEmailAddress", str);
		editor.commit();
	}

	public String getUserEmailAddress() {
		String flag = sharedPreferences.getString("userEmailAddress", null);
		return flag;
	}
	
	public void setUserEmployeeId(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userEmployeeId", str);
		editor.commit();
	}

	public String getUserEmployeeId() {
		String flag = sharedPreferences.getString("userEmployeeId", null);
		return flag;
	}
	
	public void setUserDisplayName(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userDisplayName", str);
		editor.commit();
	}

	public String getUserDisplayName() {
		String flag = sharedPreferences.getString("userDisplayName", null);
		return flag;
	}
	
	public void setUserProfileImagePath(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userProfileImagePath", str);
		editor.commit();
	}

	public String getUserProfileImagePath() {
		String flag = sharedPreferences.getString("userProfileImagePath", null);
		return flag;
	}
	
	public void setUserProfileImageLink(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userProfileImageLink", str);
		editor.commit();
	}

	public String getUserProfileImageLink() {
		String flag = sharedPreferences.getString("userProfileImageLink", null);
		return flag;
	}
	
	public void setUserFavouriteQuestion(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userFavouriteQuestion", str);
		editor.commit();
	}

	public String getUserFavouriteQuestion() {
		String flag = sharedPreferences.getString("userFavouriteQuestion", null);
		return flag;
	}
	
	public void setUserFavouriteAnswer(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userFavouriteAnswer", str);
		editor.commit();
	}

	public String getUserFavouriteAnswer() {
		String flag = sharedPreferences.getString("userFavouriteAnswer", null);
		return flag;
	}
	
	public void setUserBirthdate(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userBirthdate", str);
		editor.commit();
	}

	public String getUserBirthdate() {
		String flag = sharedPreferences.getString("userBirthdate", null);
		return flag;
	}
	
	/*
	 * PREFERENCES : DEVICE DETAILS
	 */

	public void setScreenWidth(String str) {
		editor = sharedPreferences.edit();
		editor.putString("screenWidth", str);
		editor.commit();
	}

	public String getScreenWidth() {
		String flag = sharedPreferences.getString("screenWidth", null);
		return flag;
	}

	public void setDeviceId(String id) {
		editor = sharedPreferences.edit();
		editor.putString("deviceId", id);
		editor.commit();
	}

	public String getDeviceId() {
		String deviceId = sharedPreferences.getString("deviceId",
				"Device Id Not Found");
		return deviceId;
	}

	public void setDeviceSize(String id) {
		editor = sharedPreferences.edit();
		editor.putString("deviceId", id);
		editor.commit();
	}

	public String getDeviceSize() {
		String deviceId = sharedPreferences.getString("deviceSize",
				"Device Size Not Found");
		return deviceId;
	}
	
	/*
	 * PREFERENCES : APPLICATION SETTINGS
	 */
	public void setAppLanguageCode(String mAppLanguageCode) {
		editor = sharedPreferences.edit();
		editor.putString("appLanguageCode", mAppLanguageCode);
		editor.commit();
	}

	public String getAppLanguageCode() {
		String mAppLanguageCode = sharedPreferences.getString("appLanguageCode",
				"en");
		return mAppLanguageCode;
	}
	
	public void setAppLanguage(String mAppLanguage) {
		editor = sharedPreferences.edit();
		editor.putString("appLanguage", mAppLanguage);
		editor.commit();
	}

	public String getAppLanguage() {
		String mAppLanguage = sharedPreferences.getString("appLanguage",
				"English");
		return mAppLanguage;
	}
	
	public void setBirthdayNotifyAt(String mBirthdayNotifyAt) {
		editor = sharedPreferences.edit();
		editor.putString("birthdayNotifyAt", mBirthdayNotifyAt);
		editor.commit();
	}

	public String getBirthdayNotifyAt() {
		String mBirthdayNotifyAt= sharedPreferences.getString("birthdayNotifyAt",
				"9:00 am");
		return mBirthdayNotifyAt;
	}
	
	public void setDownloadAndNotify(boolean mDownloadAndNotify) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mDownloadAndNotify", mDownloadAndNotify);
		editor.commit();
	}

	public boolean getDownloadAndNotify() {
		boolean mDownloadAndNotify= sharedPreferences.getBoolean("mDownloadAndNotify",
				true);
		return mDownloadAndNotify;
	}
	
	public void setStopSyncAtSleepingHours(boolean mIsStopSyncAtSleepingHours) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mIsStopSyncAtSleepingHours", mIsStopSyncAtSleepingHours);
		editor.commit();
	}

	public boolean isStopSyncAtSleepingHours() {
		boolean mIsStopSyncAtSleepingHours= sharedPreferences.getBoolean("mIsStopSyncAtSleepingHours",true);
		return mIsStopSyncAtSleepingHours;
	}
	
	public void setBirthdayNotificationMute(boolean isBirthdayNotificationMute) {
		editor = sharedPreferences.edit();
		editor.putBoolean("setBirthdayNotificationMute", isBirthdayNotificationMute);
		editor.commit();
	}

	public boolean isBirthdayNotificationMute() {
		boolean isBirthdayNotificationMute= sharedPreferences.getBoolean("setBirthdayNotificationMute",false);
		return isBirthdayNotificationMute;
	}
	
	public void setAnyDoNotification(boolean isAnyDoNotification) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isAnyDoNotification", isAnyDoNotification);
		editor.commit();
	}

	public boolean isAnyDoNotification() {
		boolean isAnyDoNotification= sharedPreferences.getBoolean("isAnyDoNotification",
				true);
		return isAnyDoNotification;
	}
	
	public void setChatSound(boolean mChatSound) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mChatSound", mChatSound);
		editor.commit();
	}

	public boolean getChatSound() {
		boolean mChatSound= sharedPreferences.getBoolean("mChatSound",
				false);
		return mChatSound;
	}
	
	public void setDeveloperMode(boolean mDeveloperMode) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mDeveloperMode", mDeveloperMode);
		editor.commit();
	}

	public boolean isDeveloperMode() {
		boolean mDeveloperMode= sharedPreferences.getBoolean("mDeveloperMode",false);
		return mDeveloperMode;
	}
	
	public void setSyncFrequency(int mSyncFrequency) {
		editor = sharedPreferences.edit();
		editor.putInt("mSyncFrequency", mSyncFrequency);
		editor.commit();
	}

	public int getSyncFrequency() {
		int mSyncFrequency= sharedPreferences.getInt("mSyncFrequency",15);
		return mSyncFrequency;
	}
	
	public void setAppTheme(int AppTheme) {
		editor = sharedPreferences.edit();
		editor.putInt("AppTheme", AppTheme);
		editor.commit();
	}

	public int getAppTheme() {
		int AppTheme = sharedPreferences.getInt("AppTheme", 1);
		return AppTheme;
	}
	
	public void setTAppTheme(int TAppTheme) {
		editor = sharedPreferences.edit();
		editor.putInt("TAppTheme", TAppTheme);
		editor.commit();
	}

	public int getTAppTheme() {
		int TAppTheme = sharedPreferences.getInt("TAppTheme", 1);
		return TAppTheme;
	}
	
	public void setAppCustomTextFont(boolean isAppCustomTextFont) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isAppCustomTextFont", isAppCustomTextFont);
		editor.commit();
	}

	public boolean isAppCustomTextFont() {
		boolean isAppCustomTextFont= sharedPreferences.getBoolean("isAppCustomTextFont",true);
		return isAppCustomTextFont;
	}
	
	public void setAppShowCaseViewMother(boolean isAppShowCaseViewMother) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isAppShowCaseViewMother", isAppShowCaseViewMother);
		editor.commit();
	}

	public boolean isAppShowCaseViewMother() {
		boolean isAppShowCaseViewMother= sharedPreferences.getBoolean("isAppShowCaseViewMother",false);
		return isAppShowCaseViewMother;
	}
	
	public void setAppUpdateAvail(boolean mAppUpdateAvail) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mAppUpdateAvail", mAppUpdateAvail);
		editor.commit();
	}

	public boolean isAppUpdateAvail() {
		boolean mAppUpdateAvail= sharedPreferences.getBoolean("mAppUpdateAvail",false);
		return mAppUpdateAvail;
	}
	
	public void setCropWork(boolean misCropWork) {
		editor = sharedPreferences.edit();
		editor.putBoolean("misCropWork", misCropWork);
		editor.commit();
	}

	public boolean isCropWork() {
		boolean misCropWork = sharedPreferences.getBoolean("misCropWork", true);
		return misCropWork;
	}
	
	public void setAppSendPackets(String AppSendPackets) {
		editor = sharedPreferences.edit();
		editor.putString("AppSendPackets", AppSendPackets);
		editor.commit();
	}

	public String getAppSendPackets() {
		String AppSendPackets = sharedPreferences.getString("AppSendPackets", null);
		return AppSendPackets;
	}
	
	public void setAppReceivePackets(String AppReceivePackets) {
		editor = sharedPreferences.edit();
		editor.putString("AppReceivePackets", AppReceivePackets);
		editor.commit();
	}

	public String getAppReceivePackets() {
		String AppReceivePackets = sharedPreferences.getString("AppReceivePackets", null);
		return AppReceivePackets;
	}
	
	/**
	 * Remove Video Encryption 
	 */
	public void setEncryptedVideoDeleted(boolean mIsEncryptedVideoDeleted) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mIsEncryptedVideoDeleted", mIsEncryptedVideoDeleted);
		editor.commit();
	}

	public boolean isEncryptedVideoDeleted() {
		boolean mIsEncryptedVideoDeleted= sharedPreferences.getBoolean("mIsEncryptedVideoDeleted",false);
		return mIsEncryptedVideoDeleted;
	}
	
	/**
	 * RecyclerView : Save Position 
	 */

	public void setViewIdMobcast(String mLastIdMobcast) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdMobcast", mLastIdMobcast);
		editor.commit();
	}

	public String getViewIdMobcast() {
		String mViewIdMobcast= sharedPreferences.getString("setViewIdMobcast","-1");
		return mViewIdMobcast;
	}
	
	public void setViewIdTraining(String mLastIdTraining) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdTraining", mLastIdTraining);
		editor.commit();
	}

	public String getViewIdTraining() {
		String mViewIdTraining= sharedPreferences.getString("setViewIdTraining","-1");
		return mViewIdTraining;
	}
	
	public void setViewIdEvent(String mLastIdEvent) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdEvent", mLastIdEvent);
		editor.commit();
	}

	public String getViewIdEvent() {
		String mViewIdEvent= sharedPreferences.getString("setViewIdEvent","-1");
		return mViewIdEvent;
	}
	
	public void setViewIdAward(String mLastIdAward) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdAward", mLastIdAward);
		editor.commit();
	}

	public String getViewIdAward() {
		String mViewIdAward= sharedPreferences.getString("setViewIdAward","-1");
		return mViewIdAward;
	}
	
	
	public void setViewIdRecruitment(String mLastIdRecruitment) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdRecruitment", mLastIdRecruitment);
		editor.commit();
	}

	public String getViewIdRecruitment() {
		String mViewIdRecruitment= sharedPreferences.getString("setViewIdRecruitment","-1");
		return mViewIdRecruitment;
	}
	
	public void setViewIdParichay(String mLastIdParichay) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdParichay", mLastIdParichay);
		editor.commit();
	}

	public String getViewIdParichay() {
		String mViewIdParichay= sharedPreferences.getString("setViewIdParichay","-1");
		return mViewIdParichay;
	}
	
	public void setLikeIdParichay(String mLastIdParichay) {
		editor = sharedPreferences.edit();
		editor.putString("setLikeIdParichay", mLastIdParichay);
		editor.commit();
	}

	public String getLikeIdParichay() {
		String mLikeIdParichay= sharedPreferences.getString("setLikeIdParichay","-1");
		return mLikeIdParichay;
	}
	
	public void setViewIdBirthday(String mLastIdBirthday) {
		editor = sharedPreferences.edit();
		editor.putString("setViewIdBirthday", mLastIdBirthday);
		editor.commit();
	}

	public String getViewIdBirthday() {
		String mViewIdBirthday= sharedPreferences.getString("setViewIdBirthday","-1");
		return mViewIdBirthday;
	}
	
	
	public void setVideoViewPosition(int mLastPosition) {
		editor = sharedPreferences.edit();
		editor.putInt("setPositionVideoView", mLastPosition);
		editor.commit();
	}

	public int getVideoViewPosition() {
		int mVideoViewPosition= sharedPreferences.getInt("setPositionVideoView",-1);
		return mVideoViewPosition;
	}
	
	public void setAudioPlayPosition(int mLastPosition) {
		editor = sharedPreferences.edit();
		editor.putInt("setPositionAudioPlay", mLastPosition);
		editor.commit();
	}

	public int getAudioPlayPosition() {
		int mAudioPlayPosition= sharedPreferences.getInt("setPositionAudioPlay",-1);
		return mAudioPlayPosition;
	}

	/**
	 * Last id : Mobcast, Training, Event, Award & Birthday
	 */

	public void setLastIdMobcast(String mLastIdMobcast) {
		editor = sharedPreferences.edit();
		editor.putString("mLastIdMobcast", mLastIdMobcast);
		editor.commit();
	}

	public String getLastIdMobcast() {
		String mLastIdMobcast= sharedPreferences.getString("mLastIdMobcast","0");
		return mLastIdMobcast;
	}
	
	public void setLastIdTraining(String mLastIdTraining) {
		editor = sharedPreferences.edit();
		editor.putString("mLastIdTraining", mLastIdTraining);
		editor.commit();
	}

	public String getLastIdTraining() {
		String mLastIdTraining= sharedPreferences.getString("mLastIdTraining","0");
		return mLastIdTraining;
	}
	
	public void setLastIdEvent(String mLastIdEvent) {
		editor = sharedPreferences.edit();
		editor.putString("mLastIdEvent", mLastIdEvent);
		editor.commit();
	}

	public String getLastIdEvent() {
		String mLastIdEvent= sharedPreferences.getString("mLastIdEvent","0");
		return mLastIdEvent;
	}
	
	public void setLastIdAward(String mLastIdAward) {
		editor = sharedPreferences.edit();
		editor.putString("mLastIdAward", mLastIdAward);
		editor.commit();
	}

	public String getLastIdAward() {
		String mLastIdAward= sharedPreferences.getString("mLastIdAward","0");
		return mLastIdAward;
	}
	
	public void setLastIdBirthday(String mLastIdBirthday) {
		editor = sharedPreferences.edit();
		editor.putString("mLastIdBirthday", mLastIdBirthday);
		editor.commit();
	}

	public String getLastIdBirthday() {
		String mLastIdBirthday= sharedPreferences.getString("mLastIdBirthday","0");
		return mLastIdBirthday;
	}
	
	/**
	 * Broadcast receiver
	 */
	
	public void setRefreshMobcastWithNewDataAvail(boolean mRefreshMobcastWithNewDataAvail) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mRefreshMobcastWithNewDataAvail", mRefreshMobcastWithNewDataAvail);
		editor.commit();
	}

	public boolean isRefreshMobcastWithNewDataAvail() {
		boolean mRefreshMobcastWithNewDataAvail= sharedPreferences.getBoolean("mRefreshMobcastWithNewDataAvail",false);
		return mRefreshMobcastWithNewDataAvail;
	}
	
	public void setRefreshTrainingWithNewDataAvail(boolean mRefreshTrainingWithNewDataAvail) {
		editor = sharedPreferences.edit();
		editor.putBoolean("mRefreshTrainingWithNewDataAvail", mRefreshTrainingWithNewDataAvail);
		editor.commit();
	}

	public boolean isRefreshTrainingWithNewDataAvail() {
		boolean mRefreshTrainingWithNewDataAvail= sharedPreferences.getBoolean("mRefreshTrainingWithNewDataAvail",false);
		return mRefreshTrainingWithNewDataAvail;
	}
	
	/**
	 * Drawer
	 */

	public void setUnreadRecruitment(int mUnreadRecruitment) {
		editor = sharedPreferences.edit();
		editor.putInt("mUnreadRecruitment", mUnreadRecruitment);
		editor.commit();
	}

	public int getUnreadRecruitment() {
		int mUnreadRecruitment= sharedPreferences.getInt("mUnreadRecruitment",0);
		return mUnreadRecruitment;
	}
	
	public void setUnreadParichay(int mUnreadParichay) {
		editor = sharedPreferences.edit();
		editor.putInt("mUnreadParichay", mUnreadParichay);
		editor.commit();
	}

	public int getUnreadParichay() {
		int mUnreadParichay= sharedPreferences.getInt("mUnreadParichay",0);
		return mUnreadParichay;
	}
	
	/**
	 * Sync 
	 */
	public void setSyncAlarmService(boolean value) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isSyncAlarmService", value);
		editor.commit();
	}

	public boolean isSyncAlarmService() {
		return sharedPreferences.getBoolean("isSyncAlarmService", false);
	}
	
	public void setLastSyncTimeStampMessage(String mLastSyncTimeStampMessage) {
		editor = sharedPreferences.edit();
		editor.putString("mLastSyncTimeStampMessage", mLastSyncTimeStampMessage);
		editor.commit();
	}

	public String getLastSyncTimeStampMessage() {
		String mLastSyncTimeStampMessage= sharedPreferences.getString("mLastSyncTimeStampMessage","Sync will be done automatically!");
		return mLastSyncTimeStampMessage;
	}
	
	/**
	 * App : Remind
	 */
	
	public void setLastAppOpenService(boolean value) {
		editor = sharedPreferences.edit();
		editor.putBoolean("isLastAppOpenService", value);
		editor.commit();
	}

	public boolean isLastAppOpenService() {
		return sharedPreferences.getBoolean("isLastAppOpenService", false);
	}
	
	public void setLastAppOpenTimeStamp(Long mLastAppOpenTimeStamp) {
		editor = sharedPreferences.edit();
		editor.putLong("mLastAppOpenTimeStamp", mLastAppOpenTimeStamp);
		editor.commit();
	}

	public Long getLastAppOpenTimeStamp() {
		Long mLastAppOpenTimeStamp= sharedPreferences.getLong("mLastAppOpenTimeStamp",-1);
		return mLastAppOpenTimeStamp;
	}
	
	public void setAppOpenRemindHour(int mAppOpenRemindHour) {
		editor = sharedPreferences.edit();
		editor.putInt("mAppOpenRemindHour", mAppOpenRemindHour);
		editor.commit();
	}

	public int getAppOpenRemindHour() {
		int mAppOpenRemindHour= sharedPreferences.getInt("mAppOpenRemindHour",10);
		return mAppOpenRemindHour;
	}
	
	public void setAppOpenRemindMinute(int mAppOpenRemindMinute) {
		editor = sharedPreferences.edit();
		editor.putInt("mAppOpenRemindMinute", mAppOpenRemindMinute);
		editor.commit();
	}

	public int getAppOpenRemindMinute() {
		int mAppOpenRemindMinute= sharedPreferences.getInt("mAppOpenRemindMinute",5);
		return mAppOpenRemindMinute;
	}
	
	/**
	 * Chat
	 */
	public void setChatOppositePerson(String mChatOppositePerson) {
		editor = sharedPreferences.edit();
		editor.putString("mChatOppositePerson", mChatOppositePerson);
		editor.commit();
	}

	public String getChatOppositePerson() {
		String mChatOppositePerson= sharedPreferences.getString("mChatOppositePerson","User1");
		return mChatOppositePerson;
	}
	
	public void setChatFrom(String mChatFrom) {
		editor = sharedPreferences.edit();
		editor.putString("mChatFrom", mChatFrom);
		editor.commit();
	}

	public String getChatFrom() {
		String mChatFrom= sharedPreferences.getString("mChatFrom","User1");
		return mChatFrom;
	}
	
	public void setChatTo(String mChatTo) {
		editor = sharedPreferences.edit();
		editor.putString("mChatTo", mChatTo);
		editor.commit();
	}

	public String getChatTo() {
		String mChatTo= sharedPreferences.getString("mChatTo","User1");
		return mChatTo;
	}
	
	/**
	 * Parichay
	 */
	
	public void setParichayJSON(String str) {// Parichay JSON
		editor = sharedPreferences.edit();
		editor.putString("parichayJSON", str);
		editor.commit();
	}

	public String getParichayJSON() { // Parichay JSON
		return sharedPreferences.getString("parichayJSON", null);
	}
	
	/**
	 * clear Preferences
	 */
	
	public void clearPreferences() {
		SharedPreferences preferences = ApplicationLoader.getPreferences().getAppPreferences();
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
	
}
