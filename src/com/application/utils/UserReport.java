/**
 * 
 */
package com.application.utils;

import org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class UserReport {
	private static final String TAG = UserReport.class.getSimpleName();
	public static void updateUserReportApi(final String mId, final String mCategory,
			final String mActivity, final String mDuration) {
		Utilities.reportQueue.postRunnable(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FileLog.e(TAG, mCategory + " : " + mId + " -> " +mActivity);
				JSONObject mJSONobj = JSONRequestBuilder.getPostUpdateReport(mId, mCategory, mActivity, mDuration);
				RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_UPDATE_REPORT, mJSONobj.toString(), TAG);
			}
		});
	}
	
	public static void updateUserRemoteReportApi(final String mId, final String mCategory,final String mUniqueId) {
		Utilities.reportQueue.postRunnable(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FileLog.e(TAG, mCategory + " : " + mId + " -> " +mUniqueId);
				JSONObject mJSONobj = JSONRequestBuilder.getPostUpdateRemoteReport(mId, mCategory, mUniqueId);
				RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_UPDATE_REMOTE_REPORT, mJSONobj.toString(), TAG);
			}
		});
	}
	
	public static void updateUserIssueApi(final String mIssue,final String mUserName) {
		Utilities.reportQueue.postRunnable(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FileLog.e(TAG,  mIssue + " -> " +mUserName);
				JSONObject mJSONobj = JSONRequestBuilder.getPostIssueData(mUserName, mIssue);
				RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SUBMIT_ISSUE, mJSONobj.toString(), TAG);
			}
		});
	}
	
	public static void updateUserAppDataApi(final String mId, final String mUniqueId, final String mCategory, final String mDescription) {
		Utilities.reportQueue.postRunnable(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FileLog.e(TAG,  mUniqueId + " -> " +mUniqueId);
				JSONObject mJSONobj = JSONRequestBuilder.getPostAppData(mCategory, mDescription, mUniqueId, mId);
				RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SUBMIT_APP_DATA, mJSONobj.toString(), TAG);
			}
		});
	}
	
	public static void updateUserRemoteSettingsApi(final String mCategory, final String mSubCategory,final String mDescription) {
		Utilities.reportQueue.postRunnable(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject mJSONobj = JSONRequestBuilder.getPostAppSettingsData(mCategory, mSubCategory, mDescription);
				RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_APP_SETTINGS, mJSONobj.toString(), TAG);
			}
		});
	}
}
