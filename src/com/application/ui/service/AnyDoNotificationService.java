/**
 * 
 */
package com.application.ui.service;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;

import com.application.ui.activity.AnyDoNotificationActivity;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AnyDoNotificationService extends IntentService {

	/**
	 * @param name
	 */
	public AnyDoNotificationService() {
		super("AnyDoNotificationService");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent mIntent) {
		// TODO Auto-generated method stub
		
		final int mId = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.ID, -1);
		final String mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
		final int mType = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.TYPE, -1);
		AndroidUtilities.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ComponentName cn = new ComponentName(ApplicationLoader.getApplication(), AnyDoNotificationActivity.class);
				intent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
				intent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mCategory);
				intent.putExtra(AppConstants.INTENTCONSTANTS.TYPE, mType);
				intent.setComponent(cn);
				startActivity(intent);
			}
		}, 1000);
	}
}
