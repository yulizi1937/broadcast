/**
 * 
 */
package com.application.ui.service;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.application.receiver.GcmBroadcastReceiver;
import com.application.sqlite.DBConstant;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.NotificationsController;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class GCMIntentNotificationService extends IntentService {
	private static final String TAG = GCMIntentNotificationService.class
			.getSimpleName();
	private Intent mIntent;

	private boolean isTraining = false;
	private boolean isMobcast = false;
	private boolean isChat = false;
	private boolean isAward = false;
	private boolean isEvent = false;
	private boolean isBirthday = false;
	
	private boolean isRemoteContentDelete = false;
	private boolean isRemoteWipe = false;
	private boolean isRemoteLogout = false;
	private boolean isUpdateApp = false;
	private boolean isRemoteSuspend = false;
	private boolean isGCMDelieveryCheck = false;
	private boolean isPullAppData = false;
	private boolean isSyncFrequency = false;
	private boolean isParichayReferral = false;

	private String _id;
	private String mBroadcastType;

	/**
	 * @param name
	 */
	public GCMIntentNotificationService() {
		super("GCMIntentNotificationService");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent mIntent) {
		// TODO Auto-generated method stub
		if (checkIntentData(mIntent)) {
			apiCallFromGCM();
		}
		releasePowerLock(mIntent);
	}

	private void releasePowerLock(Intent mIntent) {
		GcmBroadcastReceiver.completeWakefulIntent(mIntent);
	}

	private boolean checkIntentData(Intent mIntent) {
		try{
			_id = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER._id);
			mBroadcastType = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.category);
			if (mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)) {
				isMobcast = true;
				return true;
			} else if (mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)) {
				isTraining = true;
				return true;
			} else if (mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.CHAT)) {
				isChat = true;
				getChatMessage(mIntent);
				return false;
			} else if (mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)) {
				isAward = true;
				return true;
			} else if (mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)) {
				isEvent = true;
				return true;
			} else if (mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.BIRTHDAY)) {
				isBirthday = true;
				getBirthdayMessage(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REMOTECONTENTDELETE)){
				isRemoteContentDelete = true;
				remoteContentDelete(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REMOTEWIPE)){
				isRemoteWipe = true;
				remoteWipe(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REMOTELOGOUT)){
				isRemoteLogout = true;
				remoteLogout(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.UPDATEAPP)){
				isUpdateApp = true;
				updateApp(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.GCMSUCCESS)){
				isGCMDelieveryCheck = true;
				gcmDelieveryCheck(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REMOTESUSPEND)){
				isRemoteSuspend = true;
				remoteSuspend(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.APPPULLDATA)){
				isPullAppData = true;
				pullAppData(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.SYNCFREQUENCY)){
				isSyncFrequency = true;
				changeSyncFrequency(mIntent);
				return false;
			}else if(mBroadcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REFERRAL)){
				isParichayReferral = true;
				fetchParichayReferral(mIntent);
				return false;
			}
			return false;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return false;
		}
	}
	
	private void apiCallFromGCM(){
		try{
			if(Utilities.isInternetConnected()){
				String mResponeFromApi = apiFetchPushData();
				if(!TextUtils.isEmpty(mResponeFromApi)){
					parseDataFromApi(mResponeFromApi);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private String apiFetchPushData() {
		try {
			if (!TextUtils.isEmpty(_id) && !TextUtils.isEmpty(mBroadcastType)) {
				JSONObject jsonObj = JSONRequestBuilder.getPostFetchPushData(
						mBroadcastType, _id);
				if (BuildVars.USE_OKHTTP) {
					return RetroFitClient.postJSON(new OkHttpClient(),
							AppConstants.API.API_FETCH_PUSH_DATA,
							jsonObj.toString(), TAG);
				} else {
					return RestClient.postJSON(
							AppConstants.API.API_FETCH_PUSH_DATA, jsonObj, TAG);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				if (isMobcast) {
					parseDataForMobcast(mResponseFromApi);
				} else if (isTraining) {
					parseDataForTraining(mResponseFromApi);
				} else if (isChat) {

				} else if (isAward) {
					parseDataForAward(mResponseFromApi);
				} else if (isEvent) {
					parseDataForEvent(mResponseFromApi);
				} else if (isBirthday) {

				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void parseDataForMobcast(String mResponseFromApi) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONObject mJSONMobObj = mJSONObj.getJSONObject(AppConstants.API_KEY_PARAMETER.mobcast);
				String mMobcastId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastId);
				String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastType);
				String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastDate);
				String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastTime);
				String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastExpiryDate);
				String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastExpiryTime);
				ContentValues values = new ContentValues();
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, mMobcastId);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastTitle));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastBy));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastDescription));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastViewCount));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLikeCount));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE, mDate);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME, mTime);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE, mType);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsRead));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsLiked));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsSharing));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastIsDownloadable));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLink));
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE, mExpiryDate);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME, mExpiryTime);
				values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
				
				Cursor mIsExistCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID},  DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId}, null);
				if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
					mIsExistCursor.close();
					return;
				}
				if(mIsExistCursor!=null)
					mIsExistCursor.close();
				
				
				Uri isInsertUri = getContentResolver().insert(DBConstant.Mobcast_Columns.CONTENT_URI, values);
				boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
				
				if(isInsertedInDB){
					ApplicationLoader.getPreferences().setLastIdMobcast(mMobcastId);
				}
				
				boolean isThumbnailDownloaded = false;
				boolean isFileDownloaded = false;
				
				int mIntType = Utilities.getMediaType(mType);
				
				if(mIntType!= AppConstants.TYPE.FEEDBACK && mIntType!= AppConstants.TYPE.TEXT){
					JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFileInfo);
					
					for (int i = 0; i < mJSONArrMobFileObj.length(); i++) {
						JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(i);
						ContentValues valuesFileInfo = new ContentValues();
						String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastThumbnail);
						String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLink);
						String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileName);
						String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastisDefault);
						String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID, mMobcastId);
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK, mFileLink);
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileId));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLang));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileSize));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileDuration));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFilePages));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, mIsDefault);
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLiveStream));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLiveStreamYouTube));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastReadDuration));
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME, mFileName);
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK, mThumbnailLink);
						valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH, mThumbnailName);
						
						
						Uri isInsertFileInfoUri = getContentResolver().insert(DBConstant.Mobcast_File_Columns.CONTENT_URI, valuesFileInfo);
						Utilities.checkWhetherInsertedOrNot(TAG,isInsertFileInfoUri);
						
						if (Boolean.parseBoolean(mIsDefault)) { //download file which is by default only
							if(!TextUtils.isEmpty(mThumbnailLink)){
								isThumbnailDownloaded = Utilities.downloadFile(mIntType, true,false, mThumbnailLink, Utilities.getFileName(mThumbnailName));//thumbnail, isEncrypt
								}

								if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
									if(!TextUtils.isEmpty(mFileLink)){
									boolean isToEncrypt = Utilities.isToEncryptFile(mIntType);
										isFileDownloaded = Utilities.downloadFile(mIntType, false, isToEncrypt, mFileLink, mFileName);
									}
								}
						}

					}
				}else if(mIntType == AppConstants.TYPE.FEEDBACK){
					JSONArray mJSONArrMobFeedObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFeedbackInfo);
					
					for (int k = 0; k < mJSONArrMobFeedObj.length(); k++) {
							JSONObject mJSONFeedbackObj = mJSONArrMobFeedObj.getJSONObject(k);
							ContentValues valuesFeedbackInfo = new ContentValues();
							String mFeedbackId = mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackId);
							String mFeedbackQid = mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueId);
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID, mFeedbackId);
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID, mFeedbackQid);
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_TYPE, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueType));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackQueTitle));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ATTEMPT, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption1));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption2));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption3));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption4));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption5));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption6));
							valuesFeedbackInfo.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7, mJSONFeedbackObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFeedbackOption7));
							
							Uri isInsertFeedbackInfoUri = getContentResolver().insert(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, valuesFeedbackInfo);
							Utilities.checkWhetherInsertedOrNot(TAG,isInsertFeedbackInfoUri);
					}
				}
				
				NotificationsController.getInstance()
				.showOrUpdateNotification(isFileDownloaded,isThumbnailDownloaded,Integer.parseInt(mMobcastId),AppConstants.INTENTCONSTANTS.MOBCAST,mIntType);
				
				UserReport.updateUserReportApi(mMobcastId, AppConstants.INTENTCONSTANTS.MOBCAST, AppConstants.REPORT.PUSH, "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void parseDataForTraining(String mResponseFromApi) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONObject mJSONMobObj = mJSONObj.getJSONObject(AppConstants.API_KEY_PARAMETER.training);
				String mTrainingId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingId);
				String mType = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingType);
				String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingDate);
				String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingTime);
				String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingExpiryDate);
				String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingExpiryTime);
				ContentValues values = new ContentValues();
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_ID, mTrainingId);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingTitle));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_BY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingBy));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DESC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingDescription));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingViewCount));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLikeCount));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE, mDate);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME, mTime);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE, mType);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsRead));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsLiked));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsSharing));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingIsDownloadable));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LINK, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLink));
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE, mExpiryDate);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME, mExpiryTime);
				values.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
				
				Cursor mIsExistCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID},  DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, null);
				if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
					mIsExistCursor.close();
					return;
				}
				if(mIsExistCursor!=null)
					mIsExistCursor.close();
				
				
				Uri isInsertUri = getContentResolver().insert(DBConstant.Training_Columns.CONTENT_URI, values);
				boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
				
				if(isInsertedInDB){
					ApplicationLoader.getPreferences().setLastIdTraining(mTrainingId);
				}
				
				boolean isThumbnailDownloaded = false;
				boolean isFileDownloaded = false;
				
				int mIntType = Utilities.getMediaType(mType);
				
				if(mIntType!= AppConstants.TYPE.QUIZ && mIntType!= AppConstants.TYPE.TEXT){
					JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingFileInfo);
					
					for (int i = 0; i < mJSONArrMobFileObj.length(); i++) {
						JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(i);
						ContentValues valuesFileInfo = new ContentValues();
						String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingThumbnail);
						String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileLink);
						String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileName);
						String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingisDefault);
						String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_ID, mTrainingId);
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK, mFileLink);
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileId));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileLang));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileSize));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFileDuration));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFilePages));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, mIsDefault);
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingLiveStream));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingLiveStreamYouTube));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingReadDuration));
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME, mFileName);
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK, mThumbnailLink);
						valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH, mThumbnailName);
						
						
						Uri isInsertFileInfoUri = getContentResolver().insert(DBConstant.Training_File_Columns.CONTENT_URI, valuesFileInfo);
						Utilities.checkWhetherInsertedOrNot(TAG,isInsertFileInfoUri);
						
						if (Boolean.parseBoolean(mIsDefault)) { //download file which is by default only
							if(!TextUtils.isEmpty(mThumbnailLink)){
								isThumbnailDownloaded = Utilities.downloadFile(mIntType, true,false, mThumbnailLink, Utilities.getFileName(mThumbnailName));//thumbnail, isEncrypt
								}

								if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
									if(!TextUtils.isEmpty(mFileLink)){
									boolean isToEncrypt = Utilities.isToEncryptFile(mIntType);
										isFileDownloaded = Utilities.downloadFile(mIntType, false, isToEncrypt, mFileLink, mFileName);
									}
								}
						}

					}
				}else if(mIntType == AppConstants.TYPE.QUIZ){
					JSONArray mJSONArrMobFeedObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingQuizInfo);
					
					for (int k = 0; k < mJSONArrMobFeedObj.length(); k++) {
							JSONObject mJSONQuizObj = mJSONArrMobFeedObj.getJSONObject(k);
							ContentValues valuesQuizInfo = new ContentValues();
							String mQuizId = mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizId);
							String mQuizQid = mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizQueId);
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID, mQuizId);
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID, mQuizQid);
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizQueType));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizQueTitle));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizCorrectAnswer));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizScore));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizDuration));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption1));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption2));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption3));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption4));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption5));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption6));
							valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption7));
							
							Uri isInsertQuizInfoUri = getContentResolver().insert(DBConstant.Training_Quiz_Columns.CONTENT_URI, valuesQuizInfo);
							Utilities.checkWhetherInsertedOrNot(TAG,isInsertQuizInfoUri);
					}
				}
				
				NotificationsController.getInstance()
				.showOrUpdateNotification(isFileDownloaded,isThumbnailDownloaded,Integer.parseInt(mTrainingId),AppConstants.INTENTCONSTANTS.TRAINING,mIntType);
				
				UserReport.updateUserReportApi(mTrainingId, AppConstants.INTENTCONSTANTS.TRAINING, AppConstants.REPORT.PUSH, "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	private void parseDataForEvent(String mResponseFromApi) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONObject mJSONMobObj = mJSONObj.getJSONObject(AppConstants.API_KEY_PARAMETER.event);
				String mEventId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventId);
				String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventSentDate);
				String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventSentTime);
				String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventExpiryDate);
				String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventExpiryTime);
				String mFileLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventCoverLink);
				String mFileName = Utilities.getFileName(mFileLink);
				ContentValues values = new ContentValues();
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_ID, mEventId);
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_TITLE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventTitle));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_BY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventBy));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_DESC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventDescription));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventGoingCount));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventLikeCount));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventReadCount));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventInvitedCount));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE, mDate);
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME, mTime);
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsRead));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsLiked));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsSharing));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventIsGoing));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK, mFileLink);
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH, Utilities.getFilePath(AppConstants.TYPE.IMAGE, false, mFileName));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFileSize));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventStartDate));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventEndDate));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventFromTime));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventToTime));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_VENUE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventVenue));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_MAP, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventMap));
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE, mExpiryDate);
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_TIME, mExpiryTime);
				values.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
				
				Cursor mIsExistCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, new String[]{DBConstant.Event_Columns.COLUMN_ID},  DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mEventId}, null);
				if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
					mIsExistCursor.close();
					return;
				}
				if(mIsExistCursor!=null)
					mIsExistCursor.close();
				
				
				Uri isInsertUri = getContentResolver().insert(DBConstant.Event_Columns.CONTENT_URI, values);
				boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
				
				if(isInsertedInDB){
					ApplicationLoader.getPreferences().setLastIdEvent(mEventId);
				}

				int mIntType = AppConstants.TYPE.EVENT;
				
				if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
					if(!TextUtils.isEmpty(mFileLink)){
					boolean isToEncrypt = Utilities.isToEncryptFile(mIntType);
						Utilities.downloadFile(AppConstants.TYPE.IMAGE, false, isToEncrypt, mFileLink, mFileName);
					}
				}
				
				
				NotificationsController.getInstance().showOrUpdateNotification(false,false,Integer.parseInt(mEventId),AppConstants.INTENTCONSTANTS.EVENT,mIntType);
				
				UserReport.updateUserReportApi(mEventId, AppConstants.INTENTCONSTANTS.EVENT, AppConstants.REPORT.PUSH, "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void parseDataForAward(String mResponseFromApi) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONObject mJSONMobObj = mJSONObj.getJSONObject(AppConstants.API_KEY_PARAMETER.award);
				String mAwardId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardId);
				String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardSentDate);
				String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardSentTime);
				String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardExpiryDate);
				String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardExpiryTime);
				String mFileLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCoverLink);
				String mFileName = Utilities.getFileName(mFileLink);
				String mThumbnailLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCoverThumbnail);
				String mThumbnailPath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, true, Utilities.getFileName(mThumbnailLink));
				String mFilePath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, false, Utilities.getFileName(mFileLink));
				ContentValues values = new ContentValues();
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_ID, mAwardId);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardTitle));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_NAME, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverName));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverMobile));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverEmail));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDate));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE, mDate);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_TIME, mTime);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDescription));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_CITY, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCity));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDepartment));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReadCount));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardLikeCount));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCongratulatedCount));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsRead));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsLike));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsCongratulated));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsSharing));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK, mFileLink);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH, mFilePath);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardFileSize));
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK, mThumbnailLink);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH, mThumbnailPath);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE, mExpiryDate);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_TIME, mExpiryTime);
				values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED, Utilities.getMilliSecond(mExpiryDate, mExpiryTime));
				
				Cursor mIsExistCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, new String[]{DBConstant.Award_Columns.COLUMN_ID},  DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mAwardId}, null);
				if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
					mIsExistCursor.close();
					return;
				}
				if(mIsExistCursor!=null)
					mIsExistCursor.close();
				
				
				Uri isInsertUri = getContentResolver().insert(DBConstant.Award_Columns.CONTENT_URI, values);
				boolean isInsertedInDB = Utilities.checkWhetherInsertedOrNot(TAG,isInsertUri);
				
				if(isInsertedInDB){
					ApplicationLoader.getPreferences().setLastIdAward(mAwardId);
				}
				
				boolean isThumbnailDownloaded = false;
				boolean isFileDownloaded = false;
				
				int mIntType = AppConstants.TYPE.IMAGE;
				
					if(!TextUtils.isEmpty(mThumbnailLink)){
						isThumbnailDownloaded = Utilities.downloadFile(mIntType, true,false, mThumbnailLink, Utilities.getFileName(mThumbnailPath));//thumbnail, isEncrypt
						}

						if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
							if(!TextUtils.isEmpty(mFileLink)){
							boolean isToEncrypt = Utilities.isToEncryptFile(mIntType);
								isFileDownloaded = Utilities.downloadFile(mIntType, false, isToEncrypt, mFileLink, mFileName);
							}
						}
				
				NotificationsController.getInstance()
				.showOrUpdateNotification(isFileDownloaded,isThumbnailDownloaded,Integer.parseInt(mAwardId),AppConstants.INTENTCONSTANTS.AWARD, AppConstants.TYPE.AWARD);
				
				UserReport.updateUserReportApi(mAwardId, AppConstants.INTENTCONSTANTS.AWARD, AppConstants.REPORT.PUSH, "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * Chat : Push
	 */
	
	private void getChatMessage(Intent mIntent){
		ContentValues mValues = new ContentValues();
		mValues.put(DBConstant.Chat_Columns.COLUMN_MESSAGE, mIntent.getStringExtra("messageText"));
		mValues.put(DBConstant.Chat_Columns.COLUMN_ISLEFT, "false");
		getContentResolver().insert(DBConstant.Chat_Columns.CONTENT_URI, mValues);
		
		NotificationsController.getInstance().showOrUpdateNotification(false, false, 0, AppConstants.INTENTCONSTANTS.CHAT, AppConstants.TYPE.CHAT);
	}
	
	/**
	 * Birthday : Push
	 */
	
	private void getBirthdayMessage(Intent mIntent){
		if(!ApplicationLoader.getPreferences().isBirthdayNotificationMute()){
			NotificationsController.getInstance().showOrUpdateNotification(false, false, 0, AppConstants.INTENTCONSTANTS.BIRTHDAY, AppConstants.TYPE.BIRTHDAY);
		}
	}
	
	/**
	 * Remote Content Delete 
	 */
	private void remoteContentDelete(Intent mIntent){
		String mCategory = mIntent.getStringExtra("broadcastType");
		String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			getContentResolver().delete(DBConstant.Mobcast_Columns.CONTENT_URI,DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " IN ("+ _id + ")", null);	
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			getContentResolver().delete(DBConstant.Training_Columns.CONTENT_URI,DBConstant.Training_Columns.COLUMN_TRAINING_ID + " IN ("+ _id + ")", null);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
			getContentResolver().delete(DBConstant.Award_Columns.CONTENT_URI,DBConstant.Award_Columns.COLUMN_AWARD_ID + " IN ("+ _id + ")", null);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
			getContentResolver().delete(DBConstant.Event_Columns.CONTENT_URI,DBConstant.Event_Columns.COLUMN_EVENT_ID + " IN ("+ _id + ")", null);
		}
		UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
	}
	
	/**
	 * Remote Suspend
	 */
	
	private void remoteSuspend(Intent mIntent){
		try{
			String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
			UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
			apiCallForRemoteSuspend(true);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void apiCallForRemoteSuspend(boolean isSuspendUser){
		if(Utilities.isInternetConnected()){
			String mResponseFromApi = apiUserAppLogOut();
			if(!TextUtils.isEmpty(mResponseFromApi)){
				if(Utilities.isSuccessFromApi(mResponseFromApi)){
					ApplicationLoader.getPreferences().clearPreferences();
					Utilities.deleteTables();
					Utilities.deleteAppFolder(new File(AppConstants.FOLDER.BUILD_FOLDER));
					ApplicationLoader.cancelSyncServiceAlarm();
				}
			}
		}
	}
	
	/**
	 * Remote Wipe 
	 */
	private void remoteWipe(Intent mIntent){
		try{
			String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
			UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
			apiCallForRemoteWipe(true);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void apiCallForRemoteWipe(boolean isRemoteWipe){
		if(Utilities.isInternetConnected()){
			if(isRemoteWipe){
				Utilities.deleteTables();
				Utilities.deleteAppFolder(new File(AppConstants.FOLDER.BUILD_FOLDER));
				ApplicationLoader.cancelSyncServiceAlarm();
			}else{
				String mResponseFromApi = apiUserAppLogOut();
				if(!TextUtils.isEmpty(mResponseFromApi)){
					if(Utilities.isSuccessFromApi(mResponseFromApi)){
						ApplicationLoader.getPreferences().clearPreferences();
						ApplicationLoader.cancelSyncServiceAlarm();
					}
				}	
			}
		}
	}
	
	private String apiUserAppLogOut() {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostAppLogOutData();
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_LOGOUT_USER, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_LOGOUT_USER, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	/**
	 * Remote LogOut
	 */
	private void remoteLogout(Intent mIntent){
		try{
			String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
			UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
			apiCallForRemoteWipe(false);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * Update App
	 */
	
	private void updateApp(Intent mIntent){
		NotificationsController.getInstance().showOrUpdateNotification(false, false, -1, AppConstants.INTENTCONSTANTS.UPDATEAPP, AppConstants.TYPE.UPDATEAPP);
		String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
		UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
	}
	
	/**
	 * GCM Delievery Check
	 */
	private void gcmDelieveryCheck(Intent mIntent){
		String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
		UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
	}
	
	/*
	 * Change Settings
	 */
	
	/**
	 * Sync Frequency
	 */
	
	private void changeSyncFrequency(Intent mIntent){
		try{
			String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
			int mSyncFreq = Integer.parseInt(mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.syncFrequencyMins));
			if(mSyncFreq!=-1){
				ApplicationLoader.getPreferences().setSyncFrequency(mSyncFreq);	
				ApplicationLoader.cancelSyncServiceAlarm();
				ApplicationLoader.setSyncServiceAlarm();
			}
			UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
			UserReport.updateUserRemoteSettingsApi("settings", "syncFrequency", String.valueOf(mSyncFreq));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * Parichay Referral
	 */
	private void fetchParichayReferral(Intent mIntent){
		try{
			String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
			UserReport.updateUserRemoteReportApi(_id, mBroadcastType, mUniqueId);
			ArrayList<String> mList = new ArrayList<>();
			mList.add(_id);
			apiCallForParichayReferral(mList, mBroadcastType);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void apiCallForParichayReferral(ArrayList<String> mId, String mCategory) {
		try{
			if (Utilities.isInternetConnected()) {
				String mResponseFromApi = apiRefreshFeedActionParichayReferral(mId, mCategory);
				if (!TextUtils.isEmpty(mResponseFromApi)) {
					if (Utilities.isSuccessFromApi(mResponseFromApi)) {
						parseDataForParichayReferral(mResponseFromApi);
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void parseDataForParichayReferral(String mResponseFromApi){
		try{
			JSONObject mJSONObj = new JSONObject(mResponseFromApi);
			JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.parichayReferral);
			
			for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
				JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
				ContentValues mValues = new ContentValues();
				String mReferredId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.referredId);
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_JOB_ID, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobId));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID, mReferredId);
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_TYPE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.jobUnit));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_TELEPHONIC, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isTelephonic));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_ONLINE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isOnlineWritten));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR1, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isPR1));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_PR2, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isPR2));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_HR, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isHR));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_IS_DUPLICATE, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.isDuplicate));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REASON, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.reason));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL1, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment1));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL2, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment2));
				mValues.put(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_INSTALL3, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.installment3));
				
				getContentResolver().update(DBConstant.Parichay_Referral_Columns.CONTENT_URI, mValues, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + "=?", new String[]{mReferredId});
				
				NotificationsController.getInstance().showOrUpdateNotification(false,false,Integer.parseInt(mReferredId),AppConstants.INTENTCONSTANTS.REFERRAL, AppConstants.TYPE.REFERRAL);
			}
		}catch(Exception e){
			
		}
	}
	
	private String apiRefreshFeedActionParichayReferral(ArrayList<String> mId, String mCategory){
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFetchFeedActionParichayReferred(mId,AppConstants.INTENTCONSTANTS.PARICHAYREF);
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_ACTION_PAR, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_FETCH_FEED_ACTION_PAR, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	/**
	 * Pull App Data
	 */
	private void pullAppData(Intent mIntent){
		try{
			String mUniqueId = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.uniqueId);
			String mMobcastType = mIntent.getStringExtra(AppConstants.API_KEY_PARAMETER.mobcastType);
			StringBuilder mDescription = new StringBuilder("isPullAppData");
			Cursor mCursor = null;
			if(mMobcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{_id}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mDescription.append("Title : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
					mDescription.append(",Desc : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC)));
					mDescription.append(",Link : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK)));
					mDescription.append(", By : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)));
					mDescription.append(", Date : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)));
					mDescription.append(", Time : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME)));
					Cursor mCursorFile = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID+"=?", new String[]{_id}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
					if(mCursorFile!=null && mCursorFile.getCount() > 0){
						mCursorFile.moveToFirst();
						do {
							if(Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
								mDescription.append(", Path : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH)));
								mDescription.append(", FLink : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK)));
								mDescription.append(", TLink : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK)));
								mDescription.append(", TPath : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH)));
							}
						} while (mCursorFile.moveToNext());
						
					}
					if(mCursorFile!=null)
						mCursorFile.close();
				}
			}else if(mMobcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{_id}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mDescription.append("Title : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
					mDescription.append(",Desc : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC)));
					mDescription.append(",Link : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LINK)));
					mDescription.append(", By : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)));
					mDescription.append(", Date : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)));
					mDescription.append(", Time : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME)));
					Cursor mCursorFile = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID+"=?", new String[]{_id}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
					if(mCursorFile!=null && mCursorFile.getCount() > 0){
						mCursorFile.moveToFirst();
						do {
							if(Boolean.parseBoolean(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
								mDescription.append(", Path : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH)));
								mDescription.append(", FLink : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK)));
								mDescription.append(", TLink : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK)));
								mDescription.append(", TPath : ").append(mCursorFile.getString(mCursorFile.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH)));
							}
						} while (mCursorFile.moveToNext());
						
					}
					if(mCursorFile!=null)
						mCursorFile.close();
				}
			} else if(mMobcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
				mCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{_id}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mDescription.append("Title : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE)));
					mDescription.append(",Desc : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_DESC)));
					mDescription.append(",Link : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK)));
					mDescription.append(", By : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_BY)));
					mDescription.append(", Date : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE)));
					mDescription.append(", Time : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME)));
				}
			}else if(mMobcastType.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
				mCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, null, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{_id}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mDescription.append("Title : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME)));
					mDescription.append(",Desc : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION)));
					mDescription.append(",Link : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK)));
					mDescription.append(", City : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CITY)));
					mDescription.append(", RDate : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE)));
					mDescription.append(", Date : ").append(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DATE)));
				}
			}
			
			if(mCursor!=null)
				mCursor.close();
			
			UserReport.updateUserAppDataApi(_id, mUniqueId, mMobcastType, mDescription.toString());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
