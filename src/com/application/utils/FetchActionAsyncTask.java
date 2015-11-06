/**
 * 
 */
package com.application.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FetchActionAsyncTask extends AsyncTask<String, String, String> {
	private PowerManager.WakeLock mWakeLock;

	private String TAG;
	private String mId;
	private String mCategory;
	private Context mContext;
	
	OnPostExecuteTaskListener onPostExecuteListener;

	public void setOnPostExecuteListener(
			OnPostExecuteTaskListener onPostExecuteListener) {
		this.onPostExecuteListener = onPostExecuteListener;
	}

	public interface OnPostExecuteTaskListener {
		public abstract void onPostExecute(String mViewCount, String mLikeCount);
	}

	public FetchActionAsyncTask(Context mContext, String mId, String mCategory, String TAG) {
		this.TAG = TAG;
		this.mCategory = mCategory;
		this.mId = mId;
		this.mContext = mContext;
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
		try{
			JSONObject mJSONObj = JSONRequestBuilder.getPostFetchFeedActionForId(mCategory, mId);
			return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_FETCH_FEED_ACTION, mJSONObj.toString(), TAG);
		}catch(Exception e){
			return JSONRequestBuilder.getErrorMessageFromApi("mErrorMessage").toString();
		}
	}

	@Override
	protected void onPostExecute(String mResponseFromApi) {
		// TODO Auto-generated method stub
		super.onPostExecute(mResponseFromApi);
		releasePowerLock();
		if (onPostExecuteListener != null) {
			String mViewCount = null;
			String mLikeCount = null;
			if(Utilities.isSuccessFromApi(mResponseFromApi)){
				try {
					JSONObject mJSONObj = new JSONObject(mResponseFromApi);
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
						JSONObject mJSONMobObj= mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.mobcast).getJSONObject(0);
						mViewCount = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastViewCount);
						mLikeCount = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.mobcastLikeCount);
					}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
						JSONObject mJSONMobObj= mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.training).getJSONObject(0);
						mViewCount = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingViewCount);
						mLikeCount = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.trainingLikeCount);
					}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
						JSONObject mJSONMobObj= mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.event).getJSONObject(0);
						mViewCount = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventViewCount) + "," + mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventInvitedCount);
						mLikeCount = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventLikeCount) + ","+mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.eventGoingCount);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					FileLog.e(TAG, e.toString());
				}catch (Exception e){
					FileLog.e(TAG, e.toString());
				}
			}
			onPostExecuteListener.onPostExecute(mViewCount, mLikeCount);
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
	
	/*
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
