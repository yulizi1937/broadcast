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

	public void setUserId(String str) {
		editor = sharedPreferences.edit();
		editor.putString("_userId_", str);
		editor.commit();
	}

	public String getUserId() {
		String flag = sharedPreferences.getString("_userId_", null);
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
}
