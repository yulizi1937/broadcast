/*
 * This is the source code of Telegram for Android v. 1.3.2.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013.
 */

package com.application.utils;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.application.crashreporting.ExceptionHandler;
import com.application.receiver.SyncAlarmReceiver;
import com.application.ui.calligraphy.CalligraphyConfig;
import com.application.ui.service.AnyDoNotificationService;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

@SuppressLint("NewApi")
public class ApplicationLoader extends Application {
	public static final String TAG = ApplicationLoader.class.getSimpleName();

	private GoogleCloudMessaging gcm;
	private AtomicInteger msgId = new AtomicInteger();
	private String regid;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static volatile Context applicationContext = null;
	public static volatile Handler applicationHandler = null;
	public static ApplicationLoader applicationLoader;
	private static volatile boolean applicationInited = false;
	public static volatile boolean isScreenOn = false;

	public static SharedPreferences sharedPreferences;
	public static AppPreferences preferences;
	
	public static ImageLoader mImageLoader;

	public static void postInitApplication() {
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		// TODO Auto-generated method stub
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		applicationHandler = new Handler(applicationContext.getMainLooper());
		applicationLoader = this;

		preferences = new AppPreferences(this);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		ExceptionHandler.register(applicationLoader);
		if (!ApplicationLoader.getPreferences().getAppLanguageCode()
				.equalsIgnoreCase("en")) {
			changeApplicationLanguage(ApplicationLoader.getPreferences()
					.getAppLanguageCode());
		} else {
			initCalligraphy();
		}
//		startAnyDoNotificationService();
		ApplicationLoader app = (ApplicationLoader) ApplicationLoader.applicationContext;
		if(!BuildVars.DEBUG_DESIGN){
			app.initPlayServices();
		}
		
		if(BuildVars.DEBUG_STETHO){
			initStetho(applicationLoader);
		}
		
		initImageLoader();
		
	}

	public static Context getApplication() {
		return applicationContext;
	}

	public static void changeApplicationLanguage(String language_code) {
		Resources res = getApplication().getResources();
		// Change locale settings in the app.
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(language_code.toLowerCase());
		res.updateConfiguration(conf, dm);
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

	public static void initCalligraphy() {
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/RobotoCondensedRegular.ttf")
				.setFontAttrId(R.attr.fontPath).build());
	}
	
	public static void setSyncServiceAlarm() {
		SyncAlarmReceiver syncAlarmReceiver = new SyncAlarmReceiver();
		syncAlarmReceiver.setAlarm(getApplication().getApplicationContext());
		ApplicationLoader.getPreferences().setSyncAlarmService(true);
	}
	
	public static void cancelSyncServiceAlarm() {
		SyncAlarmReceiver syncAlarmReceiver = new SyncAlarmReceiver();
		syncAlarmReceiver.cancelAlarm(getApplication().getApplicationContext());
		ApplicationLoader.getPreferences().setSyncAlarmService(false);
	}
	
	@SuppressWarnings("deprecation")
	public static void initImageLoader(){
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(ApplicationLoader.getApplication()));
		L.disableLogging();
	}
	
	public static ImageLoader getUILImageLoader(){
		if(mImageLoader==null){
			initImageLoader();
		}
		return mImageLoader;
	}

	public static void startAnyDoNotificationService() {
		Intent service = new Intent(getApplication(),
				AnyDoNotificationService.class);
		getApplication().startService(service);
	}
	
	/*
	 * CREATE DB
	 */
	
	/*public static void initDB(){
		Cursor mCursor = applicationContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null, null);
		mCursor.close();
		Utilities.devSendDBInMail(applicationContext);
	}*/
	
	/*
	 * STETHO DEBUG BRIDGE
	 */
	
	public static void initStetho(Context applicationContext){
		Stetho.initialize(
		        Stetho.newInitializerBuilder(applicationContext)
		            .enableDumpapp(Stetho.defaultDumperPluginsProvider(applicationContext))
		            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(applicationContext))
		            .build());
	}

	/*
	 * GCM API
	 */
	private void initPlayServices() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId();

			if (regid.length() == 0) {
				registerInBackground();
			}else{
				ApplicationLoader.getPreferences().setRegId(regid);
			}
		} else {
			FileLog.d("tmessages", "No valid Google Play Services APK found.");
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		return resultCode == ConnectionResult.SUCCESS;
	}

	private String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences(applicationContext);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			FileLog.d("tmessages", "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			FileLog.d("tmessages", "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(ApplicationLoader.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	public static int getAppVersion() {
		try {
			PackageInfo packageInfo = applicationContext.getPackageManager()
					.getPackageInfo(applicationContext.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground() {
		AsyncTask<String, String, Boolean> task = new AsyncTask<String, String, Boolean>() {
			@Override
			protected Boolean doInBackground(String... objects) {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(applicationContext);
				}
				int count = 0;
				while (count < 1000) {
					try {
						count++;
						regid = gcm.register(AppConstants.PUSH.PROJECT_ID);
						storeRegistrationId(applicationContext, regid);
						ApplicationLoader.getPreferences().setRegId(regid);
						return true;
					} catch (Exception e) {
						FileLog.e("tmessages", e);
					}
					try {
						if (count % 20 == 0) {
							Thread.sleep(60000 * 30);
						} else {
							Thread.sleep(5000);
						}
					} catch (InterruptedException e) {
						FileLog.e("tmessages", e);
					}
				}
				return false;
			}
		};

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null,
					null);
		} else {
			task.execute(null, null, null);
		}
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion();
		FileLog.e("tmessages", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
}
