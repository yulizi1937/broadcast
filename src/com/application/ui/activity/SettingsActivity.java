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
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SwitchCompat;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.application.beans.Theme;
import com.application.ui.adapter.SimpleThemeRecyclerAdapter;
import com.application.ui.adapter.SimpleThemeRecyclerAdapter.OnItemClickListener;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
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
	private AppCompatTextView mNotificationRemindTv;
	private AppCompatTextView mSyncFrequencyTv;
	private AppCompatTextView mSyncSleepingTv;
	private AppCompatTextView mStyleTextFontTv;
	private AppCompatTextView mAboutTv;
	private AppCompatTextView mAboutBuildVersionTv;
	private AppCompatTextView mFileStorageTv;
	private AppCompatTextView mFileStorageAppTv;
	private AppCompatTextView mFileStorageDeviceTv;
	
	private SwitchCompat mNotificationBirthdayMuteCheckBox;
	private SwitchCompat mNotificationDownloadAndNotifyCheckBox;
	private SwitchCompat mNotificationAnyDoCheckBox;
	private SwitchCompat mSyncSleepingCheckBox;

	private LinearLayout mGeneralLangugageLayout;
	private LinearLayout mNotificationBirthdayTimeLayout;
	private LinearLayout mNotificationRemindLayout;
	private LinearLayout mFileStorageAppLayout;
	private LinearLayout mSyncFrequencyLayout;
	private LinearLayout mStyleTextFontLayout;
	private LinearLayout mStyleThemeAppLayout;
	private LinearLayout mSyncSleepingLayout;
	
	private View mStyleThemeAppView;
	
	private RelativeLayout mNotificationBirthdayMuteLayout;
	private RelativeLayout mNotificationDownloadAndNotifyLayout;
	private RelativeLayout mNotificationAnyDoLayout;
	private RelativeLayout mAboutLayout;
	private RelativeLayout mAboutBuildVersionLayout;
	private RelativeLayout mFileStorageLayout;
	
	private SimpleThemeRecyclerAdapter mThemeAdapter;
	
	private String mCategory = "settings";
	private String mSubCategory;
	private String mDescription;
	
	private boolean isAppLanguage = false;
	private boolean isSyncFrequency = false;
	private boolean isBirthdayTime = false;
	private boolean isBirthdayMute = false;
	private boolean isStyleTextFont = false;
	private boolean isStyleAppTheme = false;
	private boolean isCustomNotification = false;
	private boolean isDownloadAndNotify = false;
	private boolean isNotificationRemind = false;
	private boolean isStopSyncAtSleepingHours = true;
	
	private boolean workAroundTimePicker = false;
	
			
	private int isDeveloperMode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setSecurity();
		initToolBar();
		initUi();
		setUiListener();
		setAnimation();
		applyTheme();
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
		mNotificationRemindTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationUnreadRemindAtTv);
		mSyncFrequencyTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsSyncFrequencySelectedTv);
		mStyleTextFontTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsStyleTextFontSelectedTv);
		mAboutTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationAboutTv);
		mAboutBuildVersionTv = (AppCompatTextView) findViewById(R.id.fragmentAboutBuildVersionTv);
		mFileStorageTv = (AppCompatTextView) findViewById(R.id.fragmentAboutFileStorageTv);
		mFileStorageAppTv = (AppCompatTextView)findViewById(R.id.fragmentAboutFileStoragePercenetageAppTv);
		mFileStorageDeviceTv = (AppCompatTextView)findViewById(R.id.fragmentAboutFileStoragePercenetageDeviceTv);
		mSyncSleepingTv = (AppCompatTextView)findViewById(R.id.fragmentSettingsSyncSleepingTv);
		


		mNotificationBirthdayMuteCheckBox = (SwitchCompat) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteCheckTextView);
		mNotificationDownloadAndNotifyCheckBox = (SwitchCompat) findViewById(R.id.fragmentSettingsNotificationDownloadAndNotifyCheckTextView);
		mNotificationAnyDoCheckBox = (SwitchCompat) findViewById(R.id.fragmentSettingsNotificationAnyDoCheckTextView);
		mSyncSleepingCheckBox = (SwitchCompat) findViewById(R.id.fragmentSettingsSyncSleepingCheckTextView);

		mGeneralLangugageLayout = (LinearLayout) findViewById(R.id.fragmentSettingsGeneralLanguageLayout);
		mNotificationBirthdayTimeLayout = (LinearLayout) findViewById(R.id.fragmentSettingsNotificationBirthdayTimeLayout);
		mNotificationRemindLayout = (LinearLayout) findViewById(R.id.fragmentSettingsNotificationUnreadRemindLayout);
		mFileStorageAppLayout = (LinearLayout)findViewById(R.id.fragmentAboutFileStoragePercentageLayout);
		mSyncFrequencyLayout = (LinearLayout)findViewById(R.id.fragmentSettingsSyncFrequencyLayout);
		mStyleTextFontLayout = (LinearLayout)findViewById(R.id.fragmentSettingsStyleTextFontLayout);
		mStyleThemeAppLayout = (LinearLayout)findViewById(R.id.fragmentSettingsStyleAppThemeLayout);
		mSyncSleepingLayout = (LinearLayout)findViewById(R.id.fragmentSettingsSyncSleepingLayout);
		
		mNotificationBirthdayMuteLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteLayout);
		mNotificationDownloadAndNotifyLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationDownloadAndNotifyLayout);
		mNotificationAnyDoLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationAnyDoNotificationLayout);
		mAboutLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationAboutLayout);
		mAboutBuildVersionLayout = (RelativeLayout) findViewById(R.id.fragmentAboutBuildVersionLayout);
		mFileStorageLayout = (RelativeLayout)findViewById(R.id.fragmentAboutFileStorageLayout);
		
		mStyleThemeAppView = (View)findViewById(R.id.fragmentSettingsStyleAppThemeSelectedTv);
		
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
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(SettingsActivity.this).applyThemeCountrySelect(SettingsActivity.this, SettingsActivity.this, mToolBar);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
		try{
			mGeneralLanguageSelectedTv.setText(ApplicationLoader.getPreferences().getAppLanguage());
			mNotificationBirthdayTimeSelectedTv.setText(ApplicationLoader.getPreferences().getBirthdayNotifyAt());
			
			if(ApplicationLoader.getPreferences().isAnyDoNotification()){
				mNotificationAnyDoCheckBox.setChecked(true);
			}else{
				mNotificationAnyDoCheckBox.setChecked(false);
			}
			
			if(ApplicationLoader.getPreferences().isBirthdayNotificationMute()){
				mNotificationBirthdayMuteCheckBox.setChecked(true);
			}else{
				mNotificationBirthdayMuteCheckBox.setChecked(false);
			}
			
			if(ApplicationLoader.getPreferences().getDownloadAndNotify()){
				mNotificationDownloadAndNotifyCheckBox.setChecked(true);
			}else{
				mNotificationDownloadAndNotifyCheckBox.setChecked(false);
			}
			
			if(ApplicationLoader.getPreferences().getSyncFrequency()==60*24){
				mSyncFrequencyTv.setText("24 hours");
			}else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*8){
				mSyncFrequencyTv.setText("8 hours");
			}else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*2){
				mSyncFrequencyTv.setText("2 hours");
			}else if(ApplicationLoader.getPreferences().getSyncFrequency()!=60*4){
				mSyncFrequencyTv.setText(ApplicationLoader.getPreferences().getSyncFrequency() + " mins");
			}else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*4){
				mSyncFrequencyTv.setText("4 hours");
			}
			
			if(ApplicationLoader.getPreferences().isStopSyncAtSleepingHours()){
				mSyncSleepingCheckBox.setChecked(true);
			}else{
				mSyncSleepingCheckBox.setChecked(false);
			}
			
			switch (ApplicationLoader.getPreferences().getAppTheme()) {
			case 0:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_dblue));
				break;
			case 1:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_purple));
				break;
			case 2:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_green));
				break;
			case 3:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_pink));
				break;
			case 4:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_teal));
				break;
			case 5:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_brown));
				break;
			case 6:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_gray));
				break;
			case 7:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_amber));
				break;
			case 8:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_orange));
				break;
			case 9:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_lblue));
				break;
			case 10:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_red));
				break;
			case 11:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_dpurple));
				break;
			default:
				mStyleThemeAppView.setBackgroundColor(getResources().getColor(R.color.toolbar_background_dblue));
				break;
			}
			
			mAboutBuildVersionTv.setText(getResources().getString(R.string.sample_fragment_settings_about_build_version_text) + "\n v"+Utilities.getApplicationVersion() + " ( "+ Utilities.getApplicationVersionCode() + getResources().getString(R.string.app_build)+" ) ");
			
			if(ApplicationLoader.getPreferences().getAppOpenRemindHour()!=-1 && ApplicationLoader.getPreferences().getAppOpenRemindMinute()!=-1){
				mNotificationRemindTv.setText(String.format("%02d", ApplicationLoader.getPreferences().getAppOpenRemindHour()) + ":" + String.format("%02d", ApplicationLoader.getPreferences().getAppOpenRemindMinute()));	
			}else{
				mNotificationRemindTv.setText("Never remind me");
			}
		
		}catch(Exception e){
			FileLog.e(TAG, e.toString());	
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
		
		mNotificationRemindLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNotificationRemindPicker();
			}
		});
		
		mSyncFrequencyLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showSyncFrequencyDialog();
			}
		});
		
		mStyleTextFontLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showStyleTextFontDialog();
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
				if(BuildVars.DEBUG){
					Intent mIntent = new Intent(SettingsActivity.this, FileManagerActivity.class);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(SettingsActivity.this);
				}
			}
		});
		
		mStyleThemeAppLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				showStyleAppThemeDialog();
				showStyleAppThemeListDialog();
			}
		});
		
		mAboutBuildVersionLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(BuildVars.DEBUG){
					if(!ApplicationLoader.getPreferences().isDeveloperMode()){
						if(isDeveloperMode >=3){
							ApplicationLoader.getPreferences().setDeveloperMode(true);
							AndroidUtilities.showSnackBar(SettingsActivity.this, getResources().getString(R.string.developer_message));
						}else{
							isDeveloperMode++;
						}
					}else{//Developer Mode
						showTrafficDialog();
					}
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
		
		mSyncSleepingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		       @Override
		       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		    	   if(isChecked){
		    		   mDescription = "on";
		    	   }else{
		    		   mDescription = "off";
		    	   }
		    	   mSubCategory = "stopSyncAtSleepingHours";
		    	   ApplicationLoader.getPreferences().setStopSyncAtSleepingHours(isChecked);
		    	   isStopSyncAtSleepingHours = isChecked;
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
	
	private void showSyncFrequencyDialog() {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
				.title(getResources().getString(R.string.sample_fragment_settings_dialog_sync_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_settings_sync, true)
				.cancelable(true).show();

		View mView = mMaterialDialog.getCustomView();
		final RadioGroup mSyncRadioGroup = (RadioGroup) mView.findViewById(R.id.dialogSyncRadioGroup);
		final AppCompatRadioButton mSyncRadioButton1 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton1);
		final AppCompatRadioButton mSyncRadioButton2 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton2);
		final AppCompatRadioButton mSyncRadioButton3 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton3);
		final AppCompatRadioButton mSyncRadioButton4 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton4);
		final AppCompatRadioButton mSyncRadioButton5 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton5);
		final AppCompatRadioButton mSyncRadioButton6 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton6);
		final AppCompatRadioButton mSyncRadioButton7 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton7);
		final AppCompatButton mSyncSubmitBtn = (AppCompatButton) mView.findViewById(R.id.dialogSyncButton);
		try {
			setMaterialRippleWithGrayOnView(mSyncSubmitBtn);
			if(ApplicationLoader.getPreferences().getSyncFrequency()==15){
				mSyncRadioButton1.setChecked(true);
			}else if(ApplicationLoader.getPreferences().getSyncFrequency()==30){
				mSyncRadioButton2.setChecked(true);
			}else if(ApplicationLoader.getPreferences().getSyncFrequency()==60){
				mSyncRadioButton3.setChecked(true);
			} else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*2){
				mSyncRadioButton4.setChecked(true);
			} else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*4){
				mSyncRadioButton5.setChecked(true);
			} else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*8){
				mSyncRadioButton6.setChecked(true);
			} else if(ApplicationLoader.getPreferences().getSyncFrequency()==60*24){
				mSyncRadioButton7.setChecked(true);
			}  
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		mSyncSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int mSyncFreq = -1;
				switch(mSyncRadioGroup.getCheckedRadioButtonId()){
				case R.id.dialogSyncRadioButton1:
					mSyncFreq = 15;
					break;
				case R.id.dialogSyncRadioButton2:
					mSyncFreq = 30;
					break;
				case R.id.dialogSyncRadioButton3:
					mSyncFreq = 60;
					break;
				case R.id.dialogSyncRadioButton4:
					mSyncFreq = 60*2;
					break;
				case R.id.dialogSyncRadioButton5:
					mSyncFreq = 60*4;
					break;
				case R.id.dialogSyncRadioButton6:
					mSyncFreq = 60*8;
					break;
				case R.id.dialogSyncRadioButton7:
					mSyncFreq = 60*24;
					break;
				}
				applySyncFrequency(mSyncFreq);
				mMaterialDialog.dismiss();
				setDataFromPreferences();
			}
		});
	}
	
	private void applySyncFrequency(int mSyncFreq){
		try{
			if(mSyncFreq!=-1){
				ApplicationLoader.getPreferences().setSyncFrequency(mSyncFreq);	
				ApplicationLoader.cancelSyncServiceAlarm();
				ApplicationLoader.setSyncServiceAlarm();
				mSubCategory = "syncFrequency";
     		    mDescription = String.valueOf(ApplicationLoader.getPreferences().getSyncFrequency());
     		    isSyncFrequency = true;
     		    updateAppSettingsToApi();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void showNotificationRemindPicker(){
		try{
			TimePickerDialog mTimePicker = new TimePickerDialog(SettingsActivity.this, new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {
					// TODO Auto-generated method stub
						showConfirmationNotificationReminder(hourOfDay, minute);	
				}
			}, ApplicationLoader.getPreferences().getAppOpenRemindHour(), ApplicationLoader.getPreferences().getAppOpenRemindMinute(), true);
			mTimePicker.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
				}
			});
			mTimePicker.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
				}
			});
			mTimePicker.setTitle(getResources().getString(R.string.sample_fragment_settings_notification_remind_title));
			mTimePicker.show();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void showConfirmationNotificationReminder(final int hourOfDay, final int minute){
		try{
			AlertDialog.Builder mAlertDialog;
			mAlertDialog = new AlertDialog.Builder(SettingsActivity.this)
					.setTitle(getResources().getString(R.string.sample_fragment_settings_notification_remind_dialog_title)+ " "+ String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + "?")
					.setPositiveButton(getResources().getString(R.string.OK), new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							applyNotificationReminder(hourOfDay, minute);
						}
					})
					.setNeutralButton(getResources().getString(R.string.never_remindme), new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							applyNotificationReminder(-1, -1);
						}
					})
					.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
			mAlertDialog.show();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void applyNotificationReminder(int hourOfDay, int minute){
		try{
			ApplicationLoader.getPreferences().setAppOpenRemindHour(hourOfDay);
			ApplicationLoader.getPreferences().setAppOpenRemindMinute(minute);
			ApplicationLoader.cancelAppOpenRemindServiceAlarm();
			if(hourOfDay!=-1 && minute != -1){
				ApplicationLoader.setAppOpenRemindServiceAlarm();
			}
			mSubCategory = "notifReminder";
 		    mDescription = String.valueOf(ApplicationLoader.getPreferences().getAppOpenRemindHour() + ":" + ApplicationLoader.getPreferences().getAppOpenRemindMinute());
 		    isNotificationRemind = true;
 		    updateAppSettingsToApi();
 		   if(ApplicationLoader.getPreferences().getAppOpenRemindHour()!=-1 && ApplicationLoader.getPreferences().getAppOpenRemindMinute()!=-1){
 				mNotificationRemindTv.setText(String.format("%02d", ApplicationLoader.getPreferences().getAppOpenRemindHour()) + ":" + String.format("%02d", ApplicationLoader.getPreferences().getAppOpenRemindMinute()));	
 			}else{
 				mNotificationRemindTv.setText("Never remind me");
 			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
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
	
	
	private void showStyleTextFontDialog() {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
				.title(getResources().getString(R.string.sample_fragment_settings_dialog_sync_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_settings_sync, true)
				.cancelable(true).show();

		View mView = mMaterialDialog.getCustomView();
		final RadioGroup mSyncRadioGroup = (RadioGroup) mView.findViewById(R.id.dialogSyncRadioGroup);
		final AppCompatRadioButton mSyncRadioButton1 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton1);
		final AppCompatRadioButton mSyncRadioButton2 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton2);
		final AppCompatRadioButton mSyncRadioButton3 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton3);
		final AppCompatRadioButton mSyncRadioButton4 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton4);
		final AppCompatButton mSyncSubmitBtn = (AppCompatButton) mView.findViewById(R.id.dialogSyncButton);
		try {
			mSyncRadioButton1.setText(getResources().getString(R.string.sample_fragment_settings_style_textfont_selected));
			mSyncRadioButton2.setText(getResources().getString(R.string.sample_fragment_settings_style_textfont_default));
			setMaterialRippleWithGrayOnView(mSyncSubmitBtn);
			if(ApplicationLoader.getPreferences().isAppCustomTextFont()){
				mSyncRadioButton1.setChecked(true);
			}else{
				mSyncRadioButton2.setChecked(false);
			}  
			
			mSyncRadioButton3.setVisibility(View.GONE);
			mSyncRadioButton4.setVisibility(View.GONE);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		mSyncSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				boolean isCustomFont = false;
				switch(mSyncRadioGroup.getCheckedRadioButtonId()){
				case R.id.dialogSyncRadioButton1:
					isCustomFont = true;
					break;
				case R.id.dialogSyncRadioButton2:
					isCustomFont = false;
					break;
				}
				applyStyleTextFont(isCustomFont);
				mMaterialDialog.dismiss();
				setDataFromPreferences();
			}
		});
	}
	
	private void applyStyleTextFont(boolean isCustomFont){
		ApplicationLoader.getPreferences().setAppCustomTextFont(isCustomFont);
		mSubCategory = "style";
		mDescription = isCustomFont ? "custom" : "default";
		isStyleTextFont = true;
		updateAppSettingsToApi();
	}
	
	private void showStyleAppThemeListDialog() {
		/*final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
				.title(getResources().getString(R.string.sample_fragment_settings_dialog_theme_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_theme_list, true)
				.cancelable(true).show();*/
		
		final AppCompatDialog  mMateriaLDialog= new AppCompatDialog(SettingsActivity.this);
		mMateriaLDialog.setContentView(R.layout.dialog_theme_list);
		mMateriaLDialog.setTitle(getResources().getString(R.string.sample_fragment_settings_dialog_theme_title));
		mMateriaLDialog.getWindow().setLayout(Utilities.dpToPx(320, getResources()), Utilities.dpToPx(450, getResources()));
		mMateriaLDialog.show();

//		View mView = mMaterialDialog.getCustomView();
		ObservableRecyclerView mRecyclerView = (ObservableRecyclerView)mMateriaLDialog.findViewById(R.id.dialogThemeListRecyclerView);
		final TextView mSubmitBtn = (TextView) mMateriaLDialog.findViewById(R.id.dialogThemeListSubmitTv);
		GridLayoutManager mGridLayoutManager = new GridLayoutManager(SettingsActivity.this, 2);
		mRecyclerView.setLayoutManager(mGridLayoutManager);
		try {
			final ArrayList<Theme> mListTheme = Utilities.getThemeList();
			mThemeAdapter = new SimpleThemeRecyclerAdapter(SettingsActivity.this, mListTheme);
			
			mRecyclerView.setAdapter(mThemeAdapter);
			
			mThemeAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					ApplicationLoader.getPreferences().setTAppTheme(position);
					for(int i = 0 ; i < mListTheme.size() ; i++){
						mListTheme.get(i).setSelected(false);
					}
					mListTheme.get(position).setSelected(true);
					mThemeAdapter.notifyDataSetChanged();
				}
			});
			
			
			mSubmitBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					int whichTheme = ApplicationLoader.getPreferences().getTAppTheme();
					applyStyleThemeApp(whichTheme);
					mMateriaLDialog.dismiss();
					setDataFromPreferences();
				}
			});
			
			setMaterialRippleWithGrayOnView(mSubmitBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	
	
	private void showStyleAppThemeDialog() {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
				.title(getResources().getString(R.string.sample_fragment_settings_dialog_theme_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_settings_sync, true)
				.cancelable(true).show();

		View mView = mMaterialDialog.getCustomView();
		final RadioGroup mSyncRadioGroup = (RadioGroup) mView.findViewById(R.id.dialogSyncRadioGroup);
		final AppCompatRadioButton mSyncRadioButton1 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton1);
		final AppCompatRadioButton mSyncRadioButton2 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton2);
		final AppCompatRadioButton mSyncRadioButton3 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton3);
		final AppCompatRadioButton mSyncRadioButton4 = (AppCompatRadioButton) mView.findViewById(R.id.dialogSyncRadioButton4);
		final AppCompatButton mSyncSubmitBtn = (AppCompatButton) mView.findViewById(R.id.dialogSyncButton);
		try {
			mSyncRadioButton1.setText(getResources().getString(R.string.sample_fragment_settings_style_apptheme_dblue));
			mSyncRadioButton2.setText(getResources().getString(R.string.sample_fragment_settings_style_apptheme_purple));
			mSyncRadioButton3.setText(getResources().getString(R.string.sample_fragment_settings_style_apptheme_green));
			setMaterialRippleWithGrayOnView(mSyncSubmitBtn);
			if (ApplicationLoader.getPreferences().getAppTheme() == 0) {
				mSyncRadioButton1.setChecked(true);
			} else if (ApplicationLoader.getPreferences().getAppTheme() == 1) {
				mSyncRadioButton2.setChecked(true);
			} else {
				mSyncRadioButton3.setChecked(true);
			}
			mSyncRadioButton4.setVisibility(View.GONE);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		mSyncSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int whichTheme = 0;
				switch(mSyncRadioGroup.getCheckedRadioButtonId()){
				case R.id.dialogSyncRadioButton1:
					whichTheme = 0;
					break;
				case R.id.dialogSyncRadioButton2:
					whichTheme = 1;
					break;
				case R.id.dialogSyncRadioButton3:
					whichTheme = 2;
					break;
				}
				applyStyleThemeApp(whichTheme);
				mMaterialDialog.dismiss();
				setDataFromPreferences();
			}
		});
	}
	
	private void applyStyleThemeApp(int whichTheme){
		ApplicationLoader.getPreferences().setAppTheme(whichTheme);
		mSubCategory = "style";
		switch(whichTheme){
		case 0 :
			mDescription = "dblue";
			break;
		case 1 :
			mDescription = "purple";
			break;
		case 2 :
			mDescription = "green";
			break;
		case 3 :
			mDescription = "pink";
			break;
		case 4 :
			mDescription = "teal";
			break;
		case 5 :
			mDescription = "brown";
			break;
		case 6 :
			mDescription = "gray";
			break;
		case 7 :
			mDescription = "amber";
			break;
		case 8 :
			mDescription = "orange";
			break;
		case 9 :
			mDescription = "lblue";
			break;
		case 10 :
			mDescription = "red";
			break;
		case 11 :
			mDescription = "dpurple";
			break;
		}
		isStyleAppTheme= true;
		updateAppSettingsToApi();
	}
	

	private void setMaterialRippleView() {
		try {
			setMaterialRippleWithGrayOnView(mGeneralLangugageLayout);
			setMaterialRippleWithGrayOnView(mNotificationBirthdayTimeLayout);
			setMaterialRippleWithGrayOnView(mNotificationBirthdayMuteLayout);
			setMaterialRippleWithGrayOnView(mNotificationDownloadAndNotifyLayout);
			setMaterialRippleWithGrayOnView(mSyncFrequencyLayout);
			setMaterialRippleWithGrayOnView(mStyleTextFontLayout);
			setMaterialRippleWithGrayOnView(mStyleThemeAppLayout);
			setMaterialRippleWithGrayOnView(mAboutLayout);
			setMaterialRippleWithGrayOnView(mAboutBuildVersionLayout);
			setMaterialRippleWithGrayOnView(mFileStorageLayout);
			setMaterialRippleWithGrayOnView(mSyncSleepingLayout);
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
						Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER_FILE_MANAGER).getAbsolutePath()));
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
					Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER_FILE_MANAGER).getAbsolutePath())));
			
//			mFileStorageDeviceTv.setText(Utilities.formatFileSize(AndroidUtilities.getFolderSize(
//					Environment.getExternalStorageDirectory().getAbsolutePath())));
			
			mFileStorageDeviceTv.setText("Used : "+Utilities.formatFileSize(AndroidUtilities.getFolderSize(new File(
					Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER_FILE_MANAGER).getAbsolutePath())));
		}
	}
	
	
	/**
	 * Traffic : Stats
	 */
	
	private void showTrafficDialog(){
		String mTrafficStatics = AndroidUtilities.getAppNetworkTraffic();
//		getTrafficStats();
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(SettingsActivity.this)
        .title(getResources().getString(R.string.sample_fragment_settings_dialog_traffic_title))
        .content(mTrafficStatics)
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
//				if(app.packageName.equalsIgnoreCase(getResources().getString(R.string.package_name))){
					appNames.put(app.uid, app.packageName);	
//				}
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
					}else if(isNotificationRemind){
						isNotificationRemind = false;
					}else if(isSyncFrequency){
						isSyncFrequency = false;
					}else if(isStyleTextFont){
						isStyleTextFont = false;
						Utilities.stageQueue.postRunnable(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								System.exit(0);
							}
						});
					}else if(isStyleAppTheme){
						isStyleAppTheme = false;
						Utilities.stageQueue.postRunnable(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								System.exit(0);
							}
						});
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
	 * Google Analytics v3
	 */
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
