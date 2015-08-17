/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.MobcastProgressDialog;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GAServiceManager;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class SettingsActivity extends SwipeBackBaseActivity {
	private static final String TAG = SettingsActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mGeneralHeaderTv;
	private AppCompatTextView mNotificationHeaderTv;
	private AppCompatTextView mAboutHeaderTv;
	private AppCompatTextView mGeneralLanguageTv;
	private AppCompatTextView mGeneralLanguageSelectedTv;
	private AppCompatTextView mNotificationBirthdayTimeTv;
	private AppCompatTextView mNotificationBirthdayTimeSelectedTv;
	private AppCompatTextView mNotificationBirthdayMuteTv;
	private AppCompatTextView mNotificationDownloadAndNotifyTv;
	private AppCompatTextView mNotificationAnyDoTv;
	private AppCompatTextView mAboutTv;
	private AppCompatTextView mAboutBuildVersionTv;
	private AppCompatTextView mFileStorageTv;
	private AppCompatTextView mFileStorageAppTv;
	private AppCompatTextView mFileStorageDeviceTv;
	
	private AppCompatCheckBox mNotificationBirthdayMuteCheckBox;
	private AppCompatCheckBox mNotificationDownloadAndNotifyCheckBox;
	private AppCompatCheckBox mNotificationAnyDoCheckBox;

	private LinearLayout mGeneralLangugageLayout;
	private LinearLayout mNotificationBirthdayTimeLayout;
	private LinearLayout mFileStorageAppLayout;
	
	private RelativeLayout mNotificationBirthdayMuteLayout;
	private RelativeLayout mNotificationDownloadAndNotifyLayout;
	private RelativeLayout mNotificationAnyDoLayout;
	private RelativeLayout mAboutLayout;
	private RelativeLayout mAboutBuildVersionLayout;
	private RelativeLayout mFileStorageLayout;
	
	private String mCategory = "settings";
	private String mSubCategory;
	private String mDescription;
	
	private boolean isAppLanguage = false;
	private boolean isBirthdayTime = false;
	private boolean isBirthdayMute = false;
	private boolean isCustomNotification = false;
	private boolean isDownloadAndNotify = false;
			
	private int isDeveloperMode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initToolBar();
		initUi();
		setUiListener();
		setAnimation();
		if(!BuildVars.DEBUG_DESIGN){
			setAppStorage();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(SettingsActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mGeneralHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsGeneralHeader);
		mNotificationHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationHeader);
		mAboutHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsAboutHeader);
		mGeneralLanguageTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsGeneralLanguageTv);
		mGeneralLanguageSelectedTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsGeneralLanguageSelectedTv);
		mNotificationBirthdayTimeTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationBirthdayTimeTv);
		mNotificationBirthdayTimeSelectedTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationBirthdayTimeSelectedTv);
		mNotificationBirthdayMuteTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteTv);
		mNotificationDownloadAndNotifyTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationBirthdayDownloadAndNotifyTv);
		mNotificationAnyDoTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationAnyDoTv);
		mAboutTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationAboutTv);
		mAboutBuildVersionTv = (AppCompatTextView) findViewById(R.id.fragmentAboutBuildVersionTv);
		mFileStorageTv = (AppCompatTextView) findViewById(R.id.fragmentAboutFileStorageTv);
		mFileStorageAppTv = (AppCompatTextView)findViewById(R.id.fragmentAboutFileStoragePercenetageAppTv);
		mFileStorageDeviceTv = (AppCompatTextView)findViewById(R.id.fragmentAboutFileStoragePercenetageDeviceTv);


		mNotificationBirthdayMuteCheckBox = (AppCompatCheckBox) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteCheckTextView);
		mNotificationDownloadAndNotifyCheckBox = (AppCompatCheckBox) findViewById(R.id.fragmentSettingsNotificationDownloadAndNotifyCheckTextView);
		mNotificationAnyDoCheckBox = (AppCompatCheckBox) findViewById(R.id.fragmentSettingsNotificationAnyDoCheckTextView);

		mGeneralLangugageLayout = (LinearLayout) findViewById(R.id.fragmentSettingsGeneralLanguageLayout);
		mNotificationBirthdayTimeLayout = (LinearLayout) findViewById(R.id.fragmentSettingsNotificationBirthdayTimeLayout);
		mFileStorageAppLayout = (LinearLayout)findViewById(R.id.fragmentAboutFileStoragePercentageLayout);
		
		mNotificationBirthdayMuteLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteLayout);
		mNotificationDownloadAndNotifyLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationDownloadAndNotifyLayout);
		mNotificationAnyDoLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationAnyDoNotificationLayout);
		mAboutLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationAboutLayout);
		mAboutBuildVersionLayout = (RelativeLayout) findViewById(R.id.fragmentAboutBuildVersionLayout);
		mFileStorageLayout = (RelativeLayout)findViewById(R.id.fragmentAboutFileStorageLayout);
		
		setDataFromPreferences();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.SettingsActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void setAppStorage(){
		try{
			if (AndroidUtilities.isAboveIceCreamSandWich()) {
				new AsyncStorageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
			} else {
				new AsyncStorageTask().execute();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setDataFromPreferences(){
		mGeneralLanguageSelectedTv.setText(ApplicationLoader.getPreferences().getAppLanguage());
		mNotificationBirthdayTimeSelectedTv.setText(ApplicationLoader.getPreferences().getBirthdayNotifyAt());
		
		if(ApplicationLoader.getPreferences().isAnyDoNotification()){
			mNotificationAnyDoCheckBox.setChecked(true);
		}else{
			mNotificationAnyDoCheckBox.setChecked(false);
		}
		
		if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
			mNotificationDownloadAndNotifyCheckBox.setChecked(true);
		}else{
			mNotificationDownloadAndNotifyCheckBox.setChecked(false);
		}
	}

	private void setUiListener() {
		setMaterialRippleView();
		setClickListener();
		setCheckBoxListener();
	}

	private void setClickListener() {
		mGeneralLangugageLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(SettingsActivity.this, LanguageActivity.class);
				startActivityForResult(mIntent, AppConstants.INTENT.INTENT_LANGUAGE);
				AndroidUtilities.enterWindowAnimation(SettingsActivity.this);
			}
		});

		mNotificationBirthdayTimeLayout
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showBirthdayMuteListDialog();
					}
				});

		mNotificationBirthdayMuteLayout
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mNotificationBirthdayMuteCheckBox.performClick();
					}
				});

		mNotificationDownloadAndNotifyLayout
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mNotificationDownloadAndNotifyCheckBox.performClick();
					}
				});
		
		mNotificationAnyDoLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mNotificationAnyDoCheckBox.performClick();
			}
		});
		
		mAboutLayout
		.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(SettingsActivity.this, AboutActivity.class);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(SettingsActivity.this);
			}
		});
		
		mFileStorageLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(SettingsActivity.this, FileManagerActivity.class);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(SettingsActivity.this);
			}
		});
		
		mAboutBuildVersionLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!ApplicationLoader.getPreferences().isDeveloperMode()){
					if(isDeveloperMode >=3){
						ApplicationLoader.getPreferences().setDeveloperMode(true);
						Utilities.showCrouton(SettingsActivity.this, mCroutonViewGroup, getResources().getString(R.string.developer_message), Style.INFO);
					}else{
						isDeveloperMode++;
					}
				}else{//Developer Mode
					showTrafficDialog();
				}
			}
		});
	}
	
	private void setCheckBoxListener(){
		mNotificationBirthdayMuteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		       @Override
		       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		    	   if(isChecked){
		    		   mDescription = "on";
		    	   }else{
		    		   mDescription = "off";
		    	   }
		    	   mSubCategory = "birthdayMute";
		    	   ApplicationLoader.getPreferences().setBirthdayNotificationMute(isChecked);
		    	   isBirthdayMute = true;
		    	   updateAppSettingsToApi();
		       }
		   }
		);  
		
		mNotificationAnyDoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		       @Override
		       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		    	   if(isChecked){
		    		   mDescription = "on";
		    	   }else{
		    		   mDescription = "off";
		    	   }
		    	   mSubCategory = "anyDo";
		    	   ApplicationLoader.getPreferences().setAnyDoNotification(isChecked);
		    	   isCustomNotification = true;
		    	   updateAppSettingsToApi();
		       }
		   }
		);
		
		mNotificationDownloadAndNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		       @Override
		       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		    	   if(isChecked){
		    		   mDescription = "on";
		    	   }else{
		    		   mDescription = "off";
		    	   }
		    	   mSubCategory = "downloadAndNotify";
		    	   ApplicationLoader.getPreferences().setDownloadAndNotify(isChecked);
		    	   isDownloadAndNotify = true;
		    	   updateAppSettingsToApi();
		       }
		   }
		);  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AppConstants.INTENT.INTENT_LANGUAGE && resultCode == Activity.RESULT_OK){
			String values[] = new String[2];
			values[0] = data.getStringExtra(AppConstants.INTENTCONSTANTS.LANGUAGE);
			values[1] = data.getStringExtra(AppConstants.INTENTCONSTANTS.LANGUAGECODE);
			showLanguageConfirmationDialog(0, values);
		}
	}
	
	private void showBirthdayMuteListDialog(){
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
        .title(getResources().getString(R.string.sample_fragment_settings_notification_birthday_time))
        .titleColor(Utilities.getAppColor())
        .customView(R.layout.dialog_settings_birthday_notify_at, true)
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.dialog_birthday_message_negative))
        .negativeColor(Utilities.getAppColor())
        .show();
		
		View mView = mMaterialDialog.getCustomView();
		RadioGroup mRadioGroup= (RadioGroup)mView.findViewById(R.id.dialogSettingsBirthdayNotifyAtRadioGroup);
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				mMaterialDialog.dismiss();
			}
		});
	}

	private void showLanguageConfirmationDialog(final int position, final String[] values){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
        .title(getResources().getString(R.string.sample_fragment_settings_dialog_language_title) + " " + values[0] + "?")
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.sample_fragment_settings_dialog_language_negative))
        .negativeColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
//            	Intent mIntent = new Intent(SettingsActivity.this, LoginActivity.class);
//            	mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            	mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            	ApplicationLoader.getPreferences().setAppLanguage(values[0]);
    			ApplicationLoader.getPreferences().setAppLanguageCode(values[1]);
            	setDataFromPreferences();
     		    mSubCategory = "language";
     		    mDescription = values[1];
     		    isAppLanguage = true;
     		    updateAppSettingsToApi();
     		    
//            	startActivity(mIntent);
//            	Utilities.stageQueue.postRunnable(new Runnable() {
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						System.exit(0);
//					}
//				});
//            	finish();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
	

	private void setMaterialRippleView() {
		try {
			setMaterialRippleWithGrayOnView(mGeneralLangugageLayout);
			setMaterialRippleWithGrayOnView(mNotificationBirthdayTimeLayout);
			setMaterialRippleWithGrayOnView(mNotificationBirthdayMuteLayout);
			setMaterialRippleWithGrayOnView(mNotificationDownloadAndNotifyLayout);
			setMaterialRippleWithGrayOnView(mAboutLayout);
			setMaterialRippleWithGrayOnView(mAboutBuildVersionLayout);
			setMaterialRippleWithGrayOnView(mFileStorageLayout);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	public class AsyncStorageTask extends AsyncTask<Void, Void, Void> {
		private float mFloatAppStorage = 0.0f;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mFloatAppStorage = AndroidUtilities.getPercentageOfFileSizeFromFreeMemory(AndroidUtilities.getFolderSize(new File(
						Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER).getAbsolutePath()));
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, mFloatAppStorage);
			mFileStorageAppTv.setLayoutParams(params);
			
			if(mFloatAppStorage >= 90.0f){
				mFileStorageAppTv.setBackgroundColor(Color.RED);
			}
			
			LinearLayout.LayoutParams  paramsDevice = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100 - mFloatAppStorage);
			mFileStorageDeviceTv.setLayoutParams(paramsDevice);
			
			mFileStorageAppTv.setText("Used : "+Utilities.formatFileSize(AndroidUtilities.getFolderSize(new File(
					Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER).getAbsolutePath())));
			
//			mFileStorageDeviceTv.setText(Utilities.formatFileSize(AndroidUtilities.getFolderSize(
//					Environment.getExternalStorageDirectory().getAbsolutePath())));
			
			mFileStorageDeviceTv.setText("Used : "+Utilities.formatFileSize(AndroidUtilities.getFolderSize(new File(
					Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER).getAbsolutePath())));
		}
	}
	
	
	/**
	 * Traffic : Stats
	 */
	
	private void showTrafficDialog(){
//		String mTrafficStatics = getTrafficStats();
		getTrafficStats();
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
        .title(getResources().getString(R.string.sample_fragment_settings_dialog_traffic_title))
//        .content(mTrafficStatics)
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
	
	private void getTrafficStats(){
		TrafficSnapshot latest=null;
		latest=new TrafficSnapshot(this);
		
		ArrayList<String> log=new ArrayList<String>();
		HashSet<Integer> intersection=new HashSet<Integer>(latest.apps.keySet());
		
		for (Integer uid : intersection) {
			TrafficRecord latest_rec=latest.apps.get(uid);
			emitLog(latest_rec.tag, latest_rec, log);
		}
		
		Collections.sort(log);
		
		for (String row : log) {
			Log.d("TrafficMonitor", row);
		}
	}
	
	private void emitLog(CharSequence name, TrafficRecord latest_rec, ArrayList<String> rows) {
		if (latest_rec.rx > -1 || latest_rec.tx > -1) {
			StringBuilder buf = new StringBuilder(name);

			buf.append("=");
			buf.append(String.valueOf(latest_rec.rx));
			buf.append(" received");

			buf.append(", ");
			buf.append(String.valueOf(latest_rec.tx));
			buf.append(" sent");
			
			rows.add(buf.toString());
		}
	}
	
	@SuppressLint("UseSparseArrays") class TrafficSnapshot {
		TrafficRecord device=null;
		HashMap<Integer, TrafficRecord> apps= new HashMap<Integer, TrafficRecord>();
		
		TrafficSnapshot(Context ctxt) {
			device=new TrafficRecord();
			
			HashMap<Integer, String> appNames=new HashMap<Integer, String>();
			
			for (ApplicationInfo app : ctxt.getPackageManager().getInstalledApplications(0)) {
				if(app.packageName.equalsIgnoreCase(getResources().getString(R.string.package_name))){
					appNames.put(app.uid, app.packageName);	
				}
			}
			
			for (Integer uid : appNames.keySet()) {
				apps.put(uid, new TrafficRecord(uid, appNames.get(uid)));
			}
		}
	}
	
	class TrafficRecord {
		long tx=0;
		long rx=0;
		String tag=null;
		
		TrafficRecord() {
			tx=TrafficStats.getTotalTxBytes();
			rx=TrafficStats.getTotalRxBytes();
		}
		
		TrafficRecord(int uid, String tag) {
			tx=TrafficStats.getUidTxBytes(uid);
			rx=TrafficStats.getUidRxBytes(uid);
			this.tag=tag;
		}
	}
	
	/**
	 * API : Submit App Settings To API
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void updateAppSettingsToApi(){
		if (Utilities.isInternetConnected()) {
				if(!TextUtils.isEmpty(mCategory) && !TextUtils.isEmpty(mSubCategory) && !TextUtils.isEmpty(mDescription)){
					if (AndroidUtilities.isAboveIceCreamSandWich()) {
						new AsyncSettingsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
					} else {
						new AsyncSettingsTask().execute();
					}
				}
		} else {
			Utilities.showCrouton(SettingsActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
		}
	}
	
	private String apiUpdateUserAppSettings() {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostAppSettingsData(mCategory, mSubCategory, mDescription);
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_APP_SETTINGS, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_APP_SETTINGS, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		try{
			if(!TextUtils.isEmpty(mResponseFromApi)){
				if(Utilities.isSuccessFromApi(mResponseFromApi)){
					Utilities.showCrouton(SettingsActivity.this, mCroutonViewGroup, Utilities.getSuccessMessageFromApi(mResponseFromApi), Style.CONFIRM);
					if(isAppLanguage){
						isAppLanguage = false;
						Utilities.stageQueue.postRunnable(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								System.exit(0);
							}
						});
					}else if(isBirthdayMute){
						if(ApplicationLoader.getPreferences().isBirthdayNotificationMute()){
							mNotificationBirthdayMuteCheckBox.setChecked(true);
						}else{
							mNotificationBirthdayMuteCheckBox.setChecked(false);
						}
						isBirthdayMute = false;
					}else if(isBirthdayTime){
						isBirthdayTime = false;
					}else if(isCustomNotification){
						if(ApplicationLoader.getPreferences().isAnyDoNotification()){
							mNotificationAnyDoCheckBox.setChecked(true);
						}else{
							mNotificationAnyDoCheckBox.setChecked(false);
						}
						isCustomNotification = false;
					}else if(isDownloadAndNotify){
						if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
							mNotificationDownloadAndNotifyCheckBox.setChecked(true);
						}else{
							mNotificationDownloadAndNotifyCheckBox.setChecked(false);
						}
						isDownloadAndNotify = false;
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	

	public class AsyncSettingsTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(SettingsActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingUpdate));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiUpdateUserAppSettings();
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(SettingsActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
	}
	
	/**
	 * Google Analytics
	 */
	 @Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    GAServiceManager.getInstance().dispatchLocalHits();
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	    GAServiceManager.getInstance().dispatchLocalHits();
	  }
	
}
