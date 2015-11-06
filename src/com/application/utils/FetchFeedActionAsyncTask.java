/**
 * 
 */
package com.application.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;

import com.application.sqlite.DBConstant;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FetchFeedActionAsyncTask extends AsyncTask<String, String, String> {
	private PowerManager.WakeLock mWakeLock;

	private String TAG;
	private JSONObject mJSONObject;
	private Context mContext;
	private String mCategory;
	private boolean isSuccess = false;
	
	OnPostExecuteFeedActionTaskListener onPostExecuteListener;

	public void setOnPostExecuteFeedActionTaskListener(
			OnPostExecuteFeedActionTaskListener onPostExecuteListener) {
		this.onPostExecuteListener = onPostExecuteListener;
	}

	public interface OnPostExecuteFeedActionTaskListener {
		public abstract void onPostExecute(String mReponseFromApi, boolean isSuccess);
	}

	public FetchFeedActionAsyncTask(Context mContext, String mCategory,JSONObject mJSONObject, String TAG) {
		this.TAG = TAG;
		this.mContext = mContext;
		this.mCategory = mCategory;
		this.mJSONObject = mJSONObject;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (mContext != null) {
			acquirePowerLock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		String mResponseFromApi;
		try{
			mResponseFromApi = RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_ACTION, mJSONObject.toString(), TAG);
			updateActionsInDB(mResponseFromApi);
			return mResponseFromApi;
		}catch(Exception e){
			mResponseFromApi = JSONRequestBuilder.getErrorMessageFromApi("mErrorMessage").toString();
			return  mResponseFromApi;
		}
	}

	@Override
	protected void onPostExecute(String mResponseFromApi) {
		// TODO Auto-generated method stub
		super.onPostExecute(mResponseFromApi);
		try {
			releasePowerLock();
			if (onPostExecuteListener != null) {
				if (Utilities.isSuccessFromApi(mResponseFromApi)) {
					onPostExecuteListener.onPostExecute(mResponseFromApi, isSuccess);
				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCancelled(String result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}
	
	/**
	 * Update : Actions
	 */
	private void updateActionsInDB(String mResponseFromApi){
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			updateActionsInMobcast(mResponseFromApi);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			updateActionsInTraining(mResponseFromApi);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
			updateActionsInAward(mResponseFromApi);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
			updateActionsInEvent(mResponseFromApi);
		}
	}
	
	/**
	 * Update Mobcast  : Actions -> Single Update : Use Transaction in replacement
	 */
	private void updateActionsInMobcast(String mResponseFromApi) {
		try {
			ContentResolver mContentResolver = mContext.getContentResolver();
			JSONObject mJSONMobcastObj = new JSONObject(mResponseFromApi);
			JSONArray mJSONMobcastArray = mJSONMobcastObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcast);
			for(int i = 0 ; i < mJSONMobcastArray.length();i++){
				JSONObject mJSONMobObj = mJSONMobcastArray.getJSONObject(i);
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLikeCount));
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastViewCount));
				mContentResolver.update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastId)});
			}
			isSuccess = true;
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateActionsInTraining(String mResponseFromApi) {
		try {
			ContentResolver mContentResolver = mContext.getContentResolver();
			JSONObject mJSONTrainingObj = new JSONObject(mResponseFromApi);
			JSONArray mJSONTrainingArray = mJSONTrainingObj.getJSONArray(AppConstants.API_KEY_PARAMETER.training);
			for(int i = 0 ; i < mJSONTrainingArray.length();i++){
				JSONObject mJSONMobObj = mJSONTrainingArray.getJSONObject(i);
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLikeCount));
				mValues.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingViewCount));
				mContentResolver.update(DBConstant.Training_Columns.CONTENT_URI, mValues, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingId)});
			}
			isSuccess = true;
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateActionsInEvent(String mResponseFromApi) {
		try {
			ContentResolver mContentResolver = mContext.getContentResolver();
			JSONObject mJSONEventObj = new JSONObject(mResponseFromApi);
			JSONArray mJSONEventArray = mJSONEventObj.getJSONArray(AppConstants.API_KEY_PARAMETER.event);
			for(int i = 0 ; i < mJSONEventArray.length();i++){
				JSONObject mJSONMobObj = mJSONEventArray.getJSONObject(i);
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventLikeCount));
				mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventViewCount));
				mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventInvitedCount));
				mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventGoingCount));
				mContentResolver.update(DBConstant.Event_Columns.CONTENT_URI, mValues, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventId)});
			}
			isSuccess = true;
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateActionsInAward(String mResponseFromApi) {
		try {
			ContentResolver mContentResolver = mContext.getContentResolver();
			JSONObject mJSONAwardObj = new JSONObject(mResponseFromApi);
			JSONArray mJSONAwardArray = mJSONAwardObj.getJSONArray(AppConstants.API_KEY_PARAMETER.award);
			for(int i = 0 ; i < mJSONAwardArray.length();i++){
				JSONObject mJSONMobObj = mJSONAwardArray.getJSONObject(i);
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardLikeCount));
				mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardViewCount));
				mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO, mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCongratulatedCount));
				mContentResolver.update(DBConstant.Award_Columns.CONTENT_URI, mValues, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardId)});
			}
			isSuccess = true;
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * Power Lock
	 */
	@SuppressLint("Wakelock")
	private void acquirePowerLock() {
		try {
			PowerManager pm = (PowerManager) mContext
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void releasePowerLock() {
		try {
			mWakeLock.release();
			mWakeLock = null;
		} catch (Exception e) {

		}
	}
}
