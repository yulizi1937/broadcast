package com.application.utils;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.text.TextUtils;

import com.application.beans.MobcastFeedbackSubmit;
import com.application.beans.TrainingQuizSubmit;
import com.application.sqlite.DBConstant;
import com.mobcast.R;

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
	
	
	public static JSONObject getPostAppLogOutData() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.LOGOUT);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.device, AppConstants.deviceType);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostAppVersionCheckData() {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.appVersion, Utilities.getApplicationVersion());
//			stringBuffer.put(AppConstants.API_KEY_PARAMETER.appVersion, "2.1");
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.device, AppConstants.deviceType);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.regId, ApplicationLoader.getPreferences().getRegId());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostResentOTPData(String mUserName, String mCategory) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, mUserName);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostAppFeedbackData(String mCategory, String mDescription) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.description, mDescription);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.company, ApplicationLoader.getApplication().getResources().getString(R.string.app_name));
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostAppReportData(String mCategory, String mDescription) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.description, mDescription);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.company, ApplicationLoader.getApplication().getResources().getString(R.string.app_name));
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostAppSettingsData(String mCategory, String mSubCategory, String mDescription) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.subCategory, mSubCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.description, mDescription);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.company, ApplicationLoader.getApplication().getResources().getString(R.string.app_name));
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
	
	public static JSONObject getPostQuizSubmitData(ArrayList<TrainingQuizSubmit> mList, String mTrainingQuizScore, String mTrainingQuizTimeTaken) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.API_KEY_PARAMETER.training);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.subcategory, AppConstants.API_KEY_PARAMETER.quiz);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.trainingQuizId, mList.get(0).getTrainingQuizId());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.trainingQuizScore, mTrainingQuizScore);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.trainingQuizTimeTaken, mTrainingQuizTimeTaken);
			JSONArray  mJSONQuizInfoArray = new JSONArray();
			for(int i = 0 ; i< mList.size();i++){
				JSONObject mJSONQuizInfoObj = new JSONObject();
				mJSONQuizInfoObj.put(AppConstants.API_KEY_PARAMETER.trainingQuizId, mList.get(i).getTrainingQuizId());
				mJSONQuizInfoObj.put(AppConstants.API_KEY_PARAMETER.trainingQuizQueId, mList.get(i).getTrainingQuizQueId());
				mJSONQuizInfoObj.put(AppConstants.API_KEY_PARAMETER.trainingQuizQueType, mList.get(i).getTrainingQuizQueType());
				mJSONQuizInfoObj.put(AppConstants.API_KEY_PARAMETER.trainingQuizAnswer, mList.get(i).getTrainingQuizAnswer());
				mJSONQuizInfoArray.put(mJSONQuizInfoObj);
			}
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.trainingQuizInfo, mJSONQuizInfoArray);
			
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
	
	public static JSONObject getPostFetchFeedMobcast(boolean sortByAsc, int limit, String mLastMobcastId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.MOBCAST);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastMobcastId, mLastMobcastId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedTraining(boolean sortByAsc, int limit, String mLastTrainingId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.TRAINING);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastTrainingId, mLastTrainingId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedEvent(boolean sortByAsc, int limit, String mLastEventId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.EVENT);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastEventId, mLastEventId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedAward(boolean sortByAsc, int limit, String mLastAwardId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.AWARD);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastAwardId, mLastAwardId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedBirthday(String mLastBirthdayId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.BIRTHDAY);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastBirthdayId, mLastBirthdayId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedRecruitment(boolean sortByAsc, int limit, String mLastRecruitmentId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.RECRUITMENT);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastRecruitmentId, mLastRecruitmentId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedParichay(boolean sortByAsc, int limit, String mLastParichayId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.PARICHAY);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastParichayId, mLastParichayId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostFetchFeedParichayReferral(boolean sortByAsc, int limit, String mLastParichayReferralId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.PARICHAY);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastParichayReferralId, mLastParichayReferralId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, limit);
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
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			Cursor mCursor = null;
			int mIntColumnMobcastId = -1;
			if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)) {
				mCursor = ApplicationLoader.getApplication().getApplicationContext().getContentResolver().query(DBConstant.
						Mobcast_Columns.CONTENT_URI	, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID}, null, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " ASC");
				mIntColumnMobcastId = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				mCursor = ApplicationLoader.getApplication().getApplicationContext().getContentResolver().query(DBConstant.
						Training_Columns.CONTENT_URI	, new String[]{DBConstant.Event_Columns.COLUMN_ID, DBConstant.Training_Columns.COLUMN_TRAINING_ID}, null, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + " ASC");
				mIntColumnMobcastId = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_ID);
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
				mCursor = ApplicationLoader.getApplication().getApplicationContext().getContentResolver().query(DBConstant.
						Event_Columns.CONTENT_URI	, new String[]{DBConstant.Event_Columns.COLUMN_ID, DBConstant.Event_Columns.COLUMN_EVENT_ID}, null, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + " ASC");
				mIntColumnMobcastId = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_ID);
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
				mCursor = ApplicationLoader.getApplication().getApplicationContext().getContentResolver().query(DBConstant.
						Award_Columns.CONTENT_URI	, new String[]{DBConstant.Award_Columns.COLUMN_ID, DBConstant.Award_Columns.COLUMN_AWARD_ID}, null, null, DBConstant.Award_Columns.COLUMN_AWARD_ID + " ASC");
				mIntColumnMobcastId = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_ID);
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.PARICHAY)){
				mCursor = ApplicationLoader.getApplication().getApplicationContext().getContentResolver().query(DBConstant.
						Parichay_Referral_Columns.CONTENT_URI	, new String[]{DBConstant.Parichay_Referral_Columns.COLUMN_ID, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID}, null, null, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + " ASC");
				mIntColumnMobcastId = mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID);
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
	
	public static JSONObject getPostSyncMobcastData(String mCategory, String mLastMobcastId, String mLastTrainingId, String mLastEventId, String mLastAwardId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.SYNC);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastMobcastId, mLastMobcastId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastTrainingId, mLastTrainingId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastEventId, mLastEventId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.lastAwardId, mLastAwardId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostSearchData(String mCategory, String mLastId, String mSearchTerm,String mSearchBy, String mFilter, int mLimit, boolean sortByAsc) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mLastId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.sortByAsc, sortByAsc);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.limit, mLimit);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.searchTerm, mSearchTerm);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.by, mSearchBy);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.filter, mFilter);
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
	
	public static JSONObject getPostUpdateRemoteReport(String mId , String mCategory, String mUniqueId){
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.uniqueId, mUniqueId);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostRecruitmentContactShareDate(String mId, String mReferenceName, String mReferencePhoneNumber, String mReferenceEmailAddress) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.RECRUITMENT);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.referenceName, !TextUtils.isEmpty(mReferenceName)?mReferenceName:" ");
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.referenceEmailAddress, !TextUtils.isEmpty(mReferenceEmailAddress)?mReferenceEmailAddress:" ");
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.referencePhoneNumber, !TextUtils.isEmpty(mReferencePhoneNumber)?mReferencePhoneNumber:" ");
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	
	public static JSONObject getPostIssueData(String mUserName, String mIssue) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, mUserName);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceMfg, Utilities.getDeviceMfg());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceName, Utilities.getDeviceName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceOs, Utilities.getSDKVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.appVersion, Utilities.getApplicationVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceId, Utilities.getDeviceId());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.ISSUE);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.issue, mIssue);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostAppData(String mCategory,
			String mDescription, String mUniqueId, String mId) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.uniqueId, mUniqueId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.appVersion, Utilities.getApplicationVersion());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.deviceType, AppConstants.deviceType);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.description, mDescription);
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
	
	public static JSONObject getPostUserProfile(String mName, String mEmailAddress, String mEmployeeId, String mProfilePath, String mFavouriteQuestion , String mFavouriteAnswer, String mDOB, boolean isRemovedProfile){
		JSONObject stringBuffer = new JSONObject();
		try {
			JSONObject mJSONObjUser = new JSONObject();
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.name, mName);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.emailAddress, mEmailAddress);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.employeeId, mEmployeeId);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.favouriteQuestion, mFavouriteQuestion);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.favouriteAnswer, mFavouriteAnswer);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.birthdate, mDOB);
			mJSONObjUser.put(AppConstants.API_KEY_PARAMETER.removedProfile, isRemovedProfile);
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
	
	public static JSONObject getPostChatMessage(String mMessage, String mFrom, String mTo) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, AppConstants.INTENTCONSTANTS.CHAT);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.message, mMessage);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.from, mFrom);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.to, mTo);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
	
	public static JSONObject getPostMessage(String mMessage, String mId, String mCategory) {
		JSONObject stringBuffer = new JSONObject();
		try {
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.userName, ApplicationLoader.getPreferences().getUserName());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.accessToken, ApplicationLoader.getPreferences().getAccessToken());
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.category, mCategory);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER.message, mMessage);
			stringBuffer.put(AppConstants.API_KEY_PARAMETER._id, mId);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		return stringBuffer;
	}
}
