/**
 * 
 */
package com.application.utils;

import java.io.IOException;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class OkHttpLoggingInterceptors implements Interceptor {
	private String TAG = OkHttpLoggingInterceptors.class.getSimpleName();

	public OkHttpLoggingInterceptors(String TAG) {
		this.TAG = TAG;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		long t1 = System.nanoTime();
		Log.e(TAG,
				String.format("%s on %s%n%s with %s", request.url(),
						chain.connection(), request.headers(), request.body().toString()));
		
		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		Log.e(TAG, String.format("%s in %.1fms%n%s %s", response.request().url(),
				(t2 - t1) / 1e6d, response.headers(), response.body().toString(), response.body().string()));

		return response;
	}
}
