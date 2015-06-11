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
	
	public void setUserProfileImage(String str) {
		editor = sharedPreferences.edit();
		editor.putString("userProfileImage", str);
		editor.commit();
	}

	public String getUserProfileImage() {
		String flag = sharedPreferences.getString("userProfileImage", null);
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
}
