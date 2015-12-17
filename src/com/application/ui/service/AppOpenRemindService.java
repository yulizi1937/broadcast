/**
 * 
 */
package com.application.ui.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.application.receiver.AppOpenRemindAlarmReceiver;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.NotificationsController;
import com.application.utils.Utilities;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class AppOpenRemindService extends IntentService{
	public static final String TAG = AppOpenRemindService.class.getSimpleName();
	private int mId;
	private int mType;
	
    public AppOpenRemindService() {
        super("AppOpenRemindService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // Release the wake lock provided by the BroadcastReceiver.
    	if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getAccessToken())){
    		if(!isAppOpenBetweenTimeFrame()){
    			NotificationsController.getInstance().showOrUpdateNotification(false, false, -100000, AppConstants.INTENTCONSTANTS.APPREMIND, AppConstants.TYPE.APPREMIND);
    		}else{
    			Context mContext  = ApplicationLoader.getApplication().getApplicationContext();
    			if((Utilities.getUnreadCount(mContext) - Utilities.getUnreadOfBirthday(mContext)) > 0){
    				NotificationsController.getInstance().showOrUpdateNotification(false, false, -100000, AppConstants.INTENTCONSTANTS.NOTIFREMIND, AppConstants.TYPE.NOTIFREMIND);	
    			}
    		}
    	}else{
    		NotificationsController.getInstance().showOrUpdateNotification(false, false, -100000, AppConstants.INTENTCONSTANTS.LOGIN, AppConstants.TYPE.RELOGIN);
    	}
        AppOpenRemindAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }
    
    private boolean isAppOpenBetweenTimeFrame(){
    	if(System.currentTimeMillis() - ApplicationLoader.getPreferences().getLastAppOpenTimeStamp() > (1000 * 60 * 60) * 24 * 5){
    		return false;
    	}
    	return true;
    }
}
