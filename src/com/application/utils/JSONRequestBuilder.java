package com.application.utils;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;

import com.application.beans.MobcastFeedbackSubmit;
import com.application.sqlite.DBConstant;

public class JSONRequestBuilder {
	private static final String TAG = JSONRequestBuilder.class.getSimpleName();

	public static JSONObject getPostLoginData(String mUserName, String mCountryCode) {
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
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.countryCode, mCountryCode);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFeedbackSubmitData(ArrayList<MobcastFeedbackSubmit> mList) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.API_KEY_PARAMETER.mobcast);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.subcategory, AppConstants.API_KEY_PARAMETER.feedback);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.mobcastFeedbackId, mList.get(0).getMobcastFeedbackId());
			JSONArray  mJSONFeedbackInfoArray = new JSONArray();
			for(int i = 0 ; i< mList.size();i++){
				JSONObject mJSONFeedbackInfoObj = new JSONObject();
				mJSONFeedbackInfoObj.put(AppConstants.API_KEY_PARAMETER.mobcastFeedbackId, mList.get(i).getMobcastFeedbackId());
				mJSONFeedbackInfoObj.put(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueId, mList.get(i).getMobcastFeedbackQueId());
				mJSONFeedbackInfoObj.put(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueType, mList.get(i).getMobcastFeedbackQueType());
				mJSONFeedbackInfoObj.put(AppConstants.API_KEY_PARAMETER.mobcastFeedbackAnswer, mList.get(i).getMobcastFeedbackAnswer());
				mJSONFeedbackInfoArray.put(mJSONFeedbackInfoObj);
			}
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.mobcastFeedbackInfo, mJSONFeedbackInfoArray);
			
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	
	public static JSONObject getPostFetchPushData(String mCategory, String mId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
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
	
	public static JSONObject getPostFetchFeedMobcast() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.MOBCAST);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastMobcastId, ApplicationLoader.getPreferences().getLastIdMobcast());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedTraining() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.TRAINING);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastTrainingId, ApplicationLoader.getPreferences().getLastIdTraining());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedEvent() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.EVENT);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastEventId, ApplicationLoader.getPreferences().getLastIdEvent());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedAward() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.AWARD);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastAwardId, ApplicationLoader.getPreferences().getLastIdAward());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedBirthday() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.AWARD);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastBirthdayId, ApplicationLoader.getPreferences().getLastIdBirthday());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedActionForId(String mCategory, String mId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedAction(String mCategory) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.MOBCAST);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			Cursor mCursor = null;
			int mIntColumnMobcastId = -1;
			if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)) {
				mCursor = ApplicationLoader.getApplication().getApplicationContext().getContentResolver().query(DBConstant.
						Mobcast_Columns.CONTENT_URI	, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID}, null, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " ASC");
				mIntColumnMobcastId = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
			}
			
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				
				ArrayList<String> mArrayListId = new ArrayList<>();
				do {
					mArrayListId.add(mCursor.getString(mIntColumnMobcastId));
				} while (mCursor.moveToNext());
				
				String mMessageIdString= StringUtils.join(mArrayListId.toArray(),",");
				stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mMessageIdString);
			}
			
			if(mCursor!=null)
				mCursor.close();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostUpdateReport(String mId , String mCategory, String mActivity, String mDuration){
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.action, mActivity);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.duration, mDuration);
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
			try{
				mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.profileImage, Utilities.getEncodedFileToByteArray(new File(mProfilePath).getAbsolutePath()));
			}catch(Exception e){
			}
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.user, mJSONObjUser);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
}
