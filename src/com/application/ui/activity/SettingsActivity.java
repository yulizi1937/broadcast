/**
 * 
 */
package com.application.ui.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.application.ui.materialdialog.MaterialDialog;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.mobcast.R;

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
	private AppCompatTextView mAboutTv;
	private AppCompatTextView mAboutBuildVersionTv;
	private AppCompatTextView mFileStorageTv;
	private AppCompatTextView mFileStorageAppTv;
	private AppCompatTextView mFileStorageDeviceTv;
	


	private AppCompatCheckBox mNotificationBirthdayMuteCheckBox;
	private AppCompatCheckBox mNotificationDownloadAndNotifyCheckBox;

	private LinearLayout mGeneralLangugageLayout;
	private LinearLayout mNotificationBirthdayTimeLayout;
	private LinearLayout mFileStorageAppLayout;
	
	private RelativeLayout mNotificationBirthdayMuteLayout;
	private RelativeLayout mNotificationDownloadAndNotifyLayout;
	private RelativeLayout mAboutLayout;
	private RelativeLayout mAboutBuildVersionLayout;
	private RelativeLayout mFileStorageLayout;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initToolBar();
		initUi();
		setUiListener();
		setAnimation();
		setAppStorage();
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
		inflater.inflate(R.menu.menu_event_detail, menu);
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
		mAboutTv = (AppCompatTextView) findViewById(R.id.fragmentSettingsNotificationAboutTv);
		mAboutBuildVersionTv = (AppCompatTextView) findViewById(R.id.fragmentAboutBuildVersionTv);
		mFileStorageTv = (AppCompatTextView) findViewById(R.id.fragmentAboutFileStorageTv);
		mFileStorageAppTv = (AppCompatTextView)findViewById(R.id.fragmentAboutFileStoragePercenetageAppTv);
		mFileStorageDeviceTv = (AppCompatTextView)findViewById(R.id.fragmentAboutFileStoragePercenetageDeviceTv);


		mNotificationBirthdayMuteCheckBox = (AppCompatCheckBox) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteCheckTextView);
		mNotificationDownloadAndNotifyCheckBox = (AppCompatCheckBox) findViewById(R.id.fragmentSettingsNotificationDownloadAndNotifyCheckTextView);

		mGeneralLangugageLayout = (LinearLayout) findViewById(R.id.fragmentSettingsGeneralLanguageLayout);
		mNotificationBirthdayTimeLayout = (LinearLayout) findViewById(R.id.fragmentSettingsNotificationBirthdayTimeLayout);
		mFileStorageAppLayout = (LinearLayout)findViewById(R.id.fragmentAboutFileStoragePercentageLayout);
		
		mNotificationBirthdayMuteLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationBirthdayMuteLayout);
		mNotificationDownloadAndNotifyLayout = (RelativeLayout) findViewById(R.id.fragmentSettingsNotificationDownloadAndNotifyLayout);
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
	
	private void setAppStorage(){
		try{
			float mFloatAppStorage = AndroidUtilities.getPercentageOfFileSizeFromFreeMemory(AndroidUtilities.getFolderSize(new File(
					Environment.getExternalStorageDirectory(), "Download").getAbsolutePath()));
			LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, mFloatAppStorage);
			mFileStorageAppTv.setLayoutParams(params);
			
			if(mFloatAppStorage >= 90.0f){
				mFileStorageAppTv.setBackgroundColor(Color.RED);
			}
			
			LinearLayout.LayoutParams  paramsDevice = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100 - mFloatAppStorage);
			mFileStorageDeviceTv.setLayoutParams(paramsDevice);
			
			mFileStorageAppTv.setText(Utilities.formatFileSize(AndroidUtilities.getFolderSize(new File(
					Environment.getExternalStorageDirectory(), "Download").getAbsolutePath())));
			
			mFileStorageDeviceTv.setText(Utilities.formatFileSize(AndroidUtilities.getFolderSize(
					Environment.getExternalStorageDirectory().getAbsolutePath())));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setDataFromPreferences(){
		mGeneralLanguageSelectedTv.setText(ApplicationLoader.getPreferences().getAppLanguage());
		mNotificationBirthdayTimeSelectedTv.setText(ApplicationLoader.getPreferences().getBirthdayNotifyAt());
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}

	private void setOnClickListener() {
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
//            	startActivity(mIntent);
            	Utilities.stageQueue.postRunnable(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.exit(0);
					}
				});
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
}
