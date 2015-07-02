/**
 * 
 */
package com.application.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.util.Log;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class RetroFitClient {

	public static String postJSON(OkHttpClient okHttpClient, String url,
			String json, String TAG) {
		try {
			long startMilliSeconds = System.currentTimeMillis();
			if(BuildVars.DEBUG_STETHO){
				okHttpClient.networkInterceptors().add(new StethoInterceptor());
			}
			okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
			okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
			RequestBody body = RequestBody.create(
					AppConstants.API.API_CONTENTTYPE, json);
			Request request = new Request.Builder().url(url).post(body).build();
			Response mResponseObj = okHttpClient.newCall(request).execute();
			if (mResponseObj.isSuccessful()) {
				InputStream is = mResponseObj.body().byteStream();
				if (is != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();

					String line = null;
					try {
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (BuildVars.DEBUG_API) {
						Log.i(TAG, url);
						Log.i(TAG, json);
						Log.i(TAG, sb.toString());
						long timeTaken = (System.currentTimeMillis()-startMilliSeconds);
						Log.i(TAG, timeTaken + " millisec");
					}
					return sb.toString();
				}
			}else{
				if (BuildVars.DEBUG_API) {
					Log.i(TAG, url);
					Log.i(TAG, json);
				}
				return JSONRequestBuilder.getErrorMessageFromStatusCode(String.valueOf(mResponseObj.code()), mResponseObj.message()).toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
			return JSONRequestBuilder.getErrorMessageFromApi(ApplicationLoader.getApplication().getResources().getString(R.string.api_connection_timeout)).toString();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
			return JSONRequestBuilder.getErrorMessageFromApi(ApplicationLoader.getApplication().getResources().getString(R.string.api_unknown_error)).toString();
		}
		return null;
	}
	
	public static boolean downloadFileWith(OkHttpClient okHttpClient, String url,
			String filePath, boolean isEncrypt, String TAG) {
		try {
			long startMilliSeconds = System.currentTimeMillis();
			Request request = new Request.Builder().url(url).build();
			Response mResponseObj = okHttpClient.newCall(request).execute();
			if (mResponseObj.isSuccessful()) {
				InputStream is = mResponseObj.body().byteStream();
				if (is != null) {
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(filePath);
						byte data[] = new byte[1024];
						int bufferLength = 0;
						while ((bufferLength = is.read(data)) > 0) {
							fos.write(data, 0, bufferLength);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							fos.flush();
							fos.close();
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (BuildVars.DEBUG_API) {
						Log.i(TAG, url);
						long timeTaken = (System.currentTimeMillis()-startMilliSeconds);
						Log.i(TAG, timeTaken + " millisec");
					}
					return true;
				}
			}else{
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
			return false;
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
			return false;
		}
		return false;
	}
}
