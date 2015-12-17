/**
 * 
 */
package com.application.receiver;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.application.ui.service.AppOpenRemindService;
import com.application.utils.ApplicationLoader;

/**
 * <b>Description:</b></br>AppOpenRemindAlarmReceiver : Alarm Manager wakes up with periodic frequency</br>
 * @author Vikalp Patel(VikalpPatelCE)
 */
public class AppOpenRemindAlarmReceiver extends WakefulBroadcastReceiver {
	@SuppressWarnings("unused")
	private static final String TAG = AppOpenRemindAlarmReceiver.class.getSimpleName();
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager mAlarmManager;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent mPendingIntent;
    @SuppressWarnings("unused")
	private Context mContext;
  
    @Override
    public void onReceive(Context context, Intent intent) {   
        // BEGIN_INCLUDE(alarm_onreceive)
        Intent service = new Intent(context, AppOpenRemindService.class);
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
        // END_INCLUDE(alarm_onreceive)
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
        mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AppOpenRemindAlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, ApplicationLoader.getPreferences().getAppOpenRemindHour());
        calendar.set(Calendar.MINUTE, ApplicationLoader.getPreferences().getAppOpenRemindMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
		if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}

		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, mPendingIntent);
		
		/*mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), (1000 * 60 * 1), mPendingIntent);*/
		
		/*
		 * Enable {@code SampleBootReceiver} to automatically restart the alarm
		 * when the device is rebooted.
		 */
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (mAlarmManager!= null) {
            mAlarmManager.cancel(mPendingIntent);
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)
}
