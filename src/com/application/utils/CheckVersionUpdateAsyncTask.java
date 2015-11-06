/**
 * 
 */
package com.application.utils;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class CheckVersionUpdateAsyncTask extends AsyncTask<String, String, String> {
	private String mResponseFromApi;
	private static final String TAG = CheckVersionUpdateAsyncTask.class.getSimpleName();
	OnPostExecuteListener onPostExecuteListener;

	public void setOnPostExecuteListener(
			OnPostExecuteListener onPostExecuteListener) {
		this.onPostExecuteListener = onPostExecuteListener;
	}

	public interface OnPostExecuteListener {
		public abstract void onPostExecute(String mResponseFromApi);
	}
	
	public CheckVersionUpdateAsyncTask(){
		
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
			mResponseFromApi = apiUserCheckVersion();
			return mResponseFromApi;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	private String apiUserCheckVersion() {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostAppVersionCheckData();
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_APP_VERSION, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_APP_VERSION, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	@Override
	protected void onPostExecute(String mResponseFromApi) {
		// TODO Auto-generated method stub
		super.onPostExecute(mResponseFromApi);
		if (onPostExecuteListener != null) {
			onPostExecuteListener.onPostExecute(mResponseFromApi);
		}
	}
}
