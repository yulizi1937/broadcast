/**
 * 
 */
package com.application.ui.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.application.receiver.SyncAlarmReceiver;
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
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class SyncService extends IntentService{
	public static final String TAG = SyncService.class.getSimpleName();
	public static final String BROADCAST_ACTION = "com.application.ui.service.SyncService";
	
	private String mLastMobcastId  = "-1";
	private String mLastTrainingId = "-1";
	private String mLastEventId    = "-1";
	private String mLastAwardId    = "-1";
	private Context mContext;
	private boolean isNotifiedOnce = false;
	private boolean isLastIdFromDBWithoutException = false;
	
	private boolean isFromManualSync = false;
	
	private boolean isNewMobcastAdded = false;
	private boolean isNewTrainingAdded = false;
	
    public SyncService() {
        super("SyncService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // Release the wake lock provided by the BroadcastReceiver.
    	if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getAccessToken())){
    		isFromManualSync = intent.getBooleanExtra(AppConstants.INTENTCONSTANTS.ISFROMAPP, false);
    		if(!isSleepingHoursGoingOn() || isFromManualSync){
    			getLastIdFromDB();
            	apiCallFromSyncService();
    		}
    	}
        SyncAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }
    
    private boolean isSleepingHoursGoingOn(){
    	if(ApplicationLoader.getPreferences().isStopSyncAtSleepingHours()){
    		Calendar mCalendar = Calendar.getInstance(); 
    		int mHourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
    		if(mHourOfDay >=23 || mHourOfDay <=8 ){
    			return true;
    		}
    	}
    	return false;
    }
    
    private void getLastIdFromDB(){
    	try{
    		mContext  = ApplicationLoader.getApplication().getApplicationContext();
        	
        	Cursor mMobcastCursor  = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID},  null, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED + " DESC");
        	if(mMobcastCursor!=null && mMobcastCursor.getCount() > 0){
        		mMobcastCursor.moveToFirst();
        		mLastMobcastId =  mMobcastCursor.getString(mMobcastCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID));
        	}
        	if(mMobcastCursor!=null){
        		mMobcastCursor.close();
        	}
        	
        	Cursor mTrainingCursor  = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID, DBConstant.Training_Columns.COLUMN_TRAINING_ID},  null, null, DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED + " DESC");
        	if(mTrainingCursor!=null && mTrainingCursor.getCount() > 0){
        		mTrainingCursor.moveToFirst();
        		mLastTrainingId =  mTrainingCursor.getString(mTrainingCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_ID));
        	}
        	if(mTrainingCursor!=null){
        		mTrainingCursor.close();
        	}
        	
        	Cursor mEventCursor  = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, new String[]{DBConstant.Event_Columns.COLUMN_ID, DBConstant.Event_Columns.COLUMN_EVENT_ID},  null, null, DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED  + " DESC");
        	if(mEventCursor!=null && mEventCursor.getCount() > 0){
        		mEventCursor.moveToFirst();
        		mLastEventId =  mEventCursor.getString(mEventCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_ID));
        	}
        	if(mEventCursor!=null){
        		mEventCursor.close();
        	}
        	
        	Cursor mAwardCursor  = mContext.getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, new String[]{DBConstant.Award_Columns.COLUMN_ID, DBConstant.Award_Columns.COLUMN_AWARD_ID},  null, null, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED  + " DESC");
        	if(mAwardCursor!=null && mAwardCursor.getCount() > 0){
        		mAwardCursor.moveToFirst();
        		mLastAwardId =  mAwardCursor.getString(mAwardCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_ID));
        	}
        	if(mAwardCursor!=null){
        		mAwardCursor.close();
        	}
        	
        	isLastIdFromDBWithoutException = true;
    	}catch(Exception e){
    		FileLog.e(TAG, e.toString());
    	}
    }
    
    private void apiCallFromSyncService(){
		try{
			if(Utilities.isInternetConnected()){
				String mResponeFromApi = apiFetchSyncData();
				if(!TextUtils.isEmpty(mResponeFromApi)){
					parseDataFromApi(mResponeFromApi);
				}
			}else{
				ApplicationLoader.getPreferences().setLastSyncTimeStampMessage(Utilities.getSyncTime(true));
				sendBroadcastIfAppIsRunning();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private String apiFetchSyncData() {
		try {
			if (isLastIdFromDBWithoutException) {
				JSONObject jsonObj = JSONRequestBuilder.getPostSyncMobcastData(AppConstants.INTENTCONSTANTS.SYNC, mLastMobcastId, mLastTrainingId, mLastEventId, mLastAwardId);
				if (BuildVars.USE_OKHTTP) {
					return RetroFitClient.postJSON(new OkHttpClient(),AppConstants.API.API_SYNC,jsonObj.toString(), TAG);
				} else {
					return RestClient.postJSON(AppConstants.API.API_SYNC, jsonObj, TAG);
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
				parseDataForMobcast(mResponseFromApi);
				parseDataForTraining(mResponseFromApi);
				parseDataForAward(mResponseFromApi);
				parseDataForEvent(mResponseFromApi);
				ApplicationLoader.getPreferences().setLastSyncTimeStampMessage(Utilities.getSyncTime(false));
				sendBroadcastIfAppIsRunning();
			}else{
				if(Utilities.getErrorMessageFromApi(mResponseFromApi).equalsIgnoreCase("Invalid token")){
					ApplicationLoader.getPreferences().clearPreferences();
					Utilities.deleteTables();
					Utilities.deleteAppFolder(new File(AppConstants.FOLDER.BUILD_FOLDER));
					NotificationsController.getInstance().showOrUpdateNotification(false, false, 0,AppConstants.INTENTCONSTANTS.RELOGIN,AppConstants.TYPE.RELOGIN);
				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForMobcast(String mResponseFromApi){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcast);
				ArrayList<String> mArrayListMobcastId = new ArrayList<>();
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					String mMobcastId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastId);
					
					mArrayListMobcastId.add(mMobcastId);
					
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
					
					Cursor mIsExistCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, new String[]{DBConstant.Mobcast_Columns.COLUMN_ID},  DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mMobcastId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						continue;
					}
					
					if(!isNewMobcastAdded){
						isNewMobcastAdded = true;	
					}
					
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					mContext.getContentResolver().insert(DBConstant.Mobcast_Columns.CONTENT_URI, values);
					
					final int mIntType = Utilities.getMediaType(mType);
					
					if (mIntType != AppConstants.TYPE.FEEDBACK && (mIntType != AppConstants.TYPE.TEXT || mIntType != AppConstants.TYPE.INTERACTIVE)) {
						JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcastFileInfo);
						
						for (int j = 0; j < mJSONArrMobFileObj.length(); j++) {
							JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(j);
							ContentValues valuesFileInfo = new ContentValues();
							final String mThumbnailLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastThumbnail);
							String mFileLink = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLink);
							final String mFileName = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileName);
							String mIsDefault = mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastisDefault);
							final String mThumbnailName = Utilities.getFilePath(mIntType, true, Utilities.getFileName(mThumbnailLink));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID, mMobcastId);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK, mFileLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileId));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH, Utilities.getFilePath(mIntType, false, mFileName));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileLang));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileSize));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFileDuration));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES, Utilities.getFileMetaData(mIntType, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastFilePages)));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, mIsDefault);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLiveStream));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLiveStreamYouTube));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.mobcastReadDuration));
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME, mFileName);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK, mThumbnailLink);
							valuesFileInfo.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH, mThumbnailName);
							
							mContext.getContentResolver().insert(DBConstant.Mobcast_File_Columns.CONTENT_URI, valuesFileInfo);
							
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
								
								Uri isInsertFeedbackInfoUri = mContext.getContentResolver().insert(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, valuesFeedbackInfo);
								if(BuildVars.DEBUG){
									Utilities.checkWhetherInsertedOrNot(TAG,isInsertFeedbackInfoUri);
								}
						}
					}
					
					if(!isNotifiedOnce){
						if(Utilities.isContentDateSameAsToday(mDate)){
							NotificationsController.getInstance().showOrUpdateNotification(false, false,Integer.parseInt(mMobcastId),AppConstants.INTENTCONSTANTS.MOBCAST,mIntType);
							isNotifiedOnce = true;
						}	
					}
				}
				if(mArrayListMobcastId.size()>0){
					UserReport.updateUserReportApi(StringUtils.join(mArrayListMobcastId, ","), AppConstants.INTENTCONSTANTS.MOBCAST, AppConstants.REPORT.SYNC, "");	
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	private void parseDataForTraining(String mResponseFromApi) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.training);
				ArrayList<String> mArrayListTrainingId = new ArrayList<>();
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					String mTrainingId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingId);
					
					mArrayListTrainingId.add(mTrainingId);
					
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
					
					Cursor mIsExistCursor = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, new String[]{DBConstant.Training_Columns.COLUMN_ID},  DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mTrainingId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						continue;
					}
					
					if(!isNewTrainingAdded){
						isNewTrainingAdded = true;
					}
					
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					
					mContext.getContentResolver().insert(DBConstant.Training_Columns.CONTENT_URI, values);
					
					int mIntType = Utilities.getMediaType(mType);
					
					if(mIntType!= AppConstants.TYPE.QUIZ && (mIntType != AppConstants.TYPE.TEXT || mIntType != AppConstants.TYPE.INTERACTIVE)){
						JSONArray mJSONArrMobFileObj = mJSONMobObj.getJSONArray(AppConstants.API_KEY_PARAMETER.trainingFileInfo);
						
						for (int j = 0; j < mJSONArrMobFileObj.length(); j++) {
							JSONObject mJSONFileInfoObj = mJSONArrMobFileObj.getJSONObject(j);
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
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES, Utilities.getFileMetaData(mIntType, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingFilePages)));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, mIsDefault);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingLiveStream));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingLiveStreamYouTube));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION, mJSONFileInfoObj.getString(AppConstants.API_KEY_PARAMETER.trainingReadDuration));
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME, mFileName);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK, mThumbnailLink);
							valuesFileInfo.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH, mThumbnailName);
							
							
							mContext.getContentResolver().insert(DBConstant.Training_File_Columns.CONTENT_URI, valuesFileInfo);
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
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.isSingleAttempt));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizCorrectAnswer));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizScore));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizDuration));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption1));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption2));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption3));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption4));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption5));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption6));
								valuesQuizInfo.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7, mJSONQuizObj.getString(AppConstants.API_KEY_PARAMETER.trainingQuizOption7));
								
								Uri isInsertQuizInfoUri = mContext.getContentResolver().insert(DBConstant.Training_Quiz_Columns.CONTENT_URI, valuesQuizInfo);
								Utilities.checkWhetherInsertedOrNot(TAG,isInsertQuizInfoUri);
						}
					}
					
					if(!isNotifiedOnce){
						if(Utilities.isContentDateSameAsToday(mDate)){
							NotificationsController.getInstance().showOrUpdateNotification(false, false,Integer.parseInt(mTrainingId),AppConstants.INTENTCONSTANTS.TRAINING,mIntType);
							isNotifiedOnce = true;
						}	
					}
				}
				if(mArrayListTrainingId.size() > 0){
					UserReport.updateUserReportApi(StringUtils.join(mArrayListTrainingId, ","), AppConstants.INTENTCONSTANTS.TRAINING, AppConstants.REPORT.SYNC, "");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForAward(String mResponseFromApi){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.award);
				
				ArrayList<String> mArrayListAwardId = new ArrayList<>();
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					String mAwardId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardId);
					
					mArrayListAwardId.add(mAwardId);
					
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
					
					
					Cursor mIsExistCursor = mContext.getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, new String[]{DBConstant.Award_Columns.COLUMN_ID},  DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mAwardId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						continue;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					mContext.getContentResolver().insert(DBConstant.Award_Columns.CONTENT_URI, values);
					
					
					if(!isNotifiedOnce){
						if(Utilities.isContentDateSameAsToday(mDate)){
							NotificationsController.getInstance().showOrUpdateNotification(false, false,Integer.parseInt(mAwardId),AppConstants.INTENTCONSTANTS.AWARD,AppConstants.TYPE.AWARD);
							isNotifiedOnce = true;
						}	
					}
				}
				if(mArrayListAwardId.size() > 0){
					UserReport.updateUserReportApi(StringUtils.join(mArrayListAwardId, ","), AppConstants.INTENTCONSTANTS.AWARD, AppConstants.REPORT.SYNC, "");
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void parseDataForEvent(String mResponseFromApi){
		try{
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.event);
				
				ArrayList<String> mArrayListEventId = new ArrayList<>();
				
				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);
					
					String mEventId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventId);
					
					mArrayListEventId.add(mEventId);
					
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
					
					
					Cursor mIsExistCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, new String[]{DBConstant.Event_Columns.COLUMN_ID},  DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mEventId}, null);
					if(mIsExistCursor!=null && mIsExistCursor.getCount() > 0){
						mIsExistCursor.close();
						continue;
					}
					if(mIsExistCursor!=null)
						mIsExistCursor.close();
					
					mContext.getContentResolver().insert(DBConstant.Event_Columns.CONTENT_URI, values);
					
					if(!isNotifiedOnce){
						if(Utilities.isContentDateSameAsToday(mDate)){
							NotificationsController.getInstance().showOrUpdateNotification(false, false,Integer.parseInt(mEventId),AppConstants.INTENTCONSTANTS.EVENT,AppConstants.TYPE.EVENT);
							isNotifiedOnce = true;
						}	
					}
				}
				if(mArrayListEventId.size() > 0){
					UserReport.updateUserReportApi(StringUtils.join(mArrayListEventId, ","), AppConstants.INTENTCONSTANTS.EVENT, AppConstants.REPORT.SYNC, "");
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	 private void sendBroadcastIfAppIsRunning() {
			try {
				ActivityManager am = (ActivityManager) ApplicationLoader
						.getApplication()
						.getSystemService(
								ApplicationLoader.getApplication().ACTIVITY_SERVICE);
				// get the info from the currently running task
				List<ActivityManager.RunningTaskInfo> taskInfo = am
						.getRunningTasks(1);
				ComponentName componentInfo = taskInfo.get(0).topActivity;
				// if app is running
				if (componentInfo.getPackageName().equalsIgnoreCase(ApplicationLoader.getApplication().getResources()
										.getString(R.string.package_name))) {
					Intent mIntent = new Intent(BROADCAST_ACTION);
					if(isNewMobcastAdded){
						ApplicationLoader.getPreferences().setRefreshMobcastWithNewDataAvail(true);
					}
					
					if(isNewTrainingAdded){
						ApplicationLoader.getPreferences().setRefreshTrainingWithNewDataAvail(true);
					}
					ApplicationLoader.getApplication().getApplicationContext().sendBroadcast(mIntent);
				} 
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}
		}
}
