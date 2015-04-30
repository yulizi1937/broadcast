/*
 * This is the source code of Telegram for Android v. 1.3.2.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013.
 */

package com.application.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

@SuppressLint("NewApi")
public class ApplicationLoader extends Application {
	public static final String TAG = ApplicationLoader.class.getSimpleName();
	
	private String regid;
	public static volatile Context applicationContext = null;
	public static volatile Handler applicationHandler = null;
	public static ApplicationLoader applicationLoader;
	private static volatile boolean applicationInited = false;
	public static volatile boolean isScreenOn = false;

	public static SharedPreferences sharedPreferences;
	public static AppPreferences preferences;
	
	

	public static void postInitApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		applicationHandler = new Handler(applicationContext.getMainLooper());
		applicationLoader = this;
		
		preferences = new AppPreferences(this);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public static Context getApplication() {
		return applicationContext;
	}
	
	public static AppPreferences getPreferences() {
		return preferences;
	}

	public static SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}
	
	public static synchronized ApplicationLoader getInstance() {
		return applicationLoader;
	}
}
