package com.application.utils;

import java.io.File;

import org.json.JSONObject;

public class JSONRequestBuilder {
	private static final String TAG = JSONRequestBuilder.class.getSimpleName();

	public static JSONObject getPostLoginData(String mUserName) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, mUserName);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceMfg, Utilities.getDeviceMfg());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceName, Utilities.getDeviceName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.regId, ApplicationLoader.getPreferences().getRegId());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceOs, Utilities.getSDKVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.appVersion, Utilities.getApplicationVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceId, Utilities.getDeviceId());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceIsTablet, false);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceSize, Utilities.checkDisplaySize());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	
	public static JSONObject getPostVerifyData(String mUserName, String mOtp) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, mUserName);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceMfg, Utilities.getDeviceMfg());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceName, Utilities.getDeviceName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.regId, ApplicationLoader.getPreferences().getRegId());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceOs, Utilities.getSDKVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.appVersion, Utilities.getApplicationVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceId, Utilities.getDeviceId());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceIsTablet, false);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceSize, Utilities.checkDisplaySize());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.otp, mOtp);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getErrorMessageFromStatusCode(String mResponseCode, String mResponseMessage){
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.errorMessage,
					mResponseCode + " " + mResponseMessage);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.success, false);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getErrorMessageFromApi(String mErrorMessage){
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.errorMessage,mErrorMessage);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.success, false);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostUserProfile(String mName, String mEmailAddress, String mEmployeeId, String mProfilePath){
		JSONObject stringBuffer = new JSONObject();
		try {
			JSONObject mJSONObjUser = new JSONObject();
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.name, mName);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.emailAddress, mEmailAddress);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.employeeId, mEmployeeId);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.profileImage, Utilities.getEncodedFileToByteArray(new File(mProfilePath).getAbsolutePath()));
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.user, mJSONObjUser);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
}
