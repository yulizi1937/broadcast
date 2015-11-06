/**
 * 
 */
package com.application.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.application.receiver.NotificationActionAwardReceiver;
import com.application.receiver.NotificationActionEventAcceptReceiver;
import com.application.receiver.NotificationActionEventDeclineReceiver;
import com.application.ui.service.AnyDoNotificationService;
import com.application.ui.service.GCMIntentNotificationService;
import com.application.ui.service.NotificationDelay;
import com.application.ui.service.NotificationRepeat;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class NotificationsController {
	private static final String TAG = NotificationsController.class.getSimpleName();
	public static final String BROADCAST_ACTION = "com.application.utils.NotificationsController";
	public static final String BROADCAST_CHAT_ACTION = "com.application.utils.NotificationsController.ChatAction";
	
	private NotificationManagerCompat notificationManager = null;
	
	private boolean inChatSoundEnabled = true;
	
	private int total_unread_count = 1;
	private int personal_count = 0;
	
	private Context mContext;
	
	private MediaPlayer mediaPlayerIn;
    private MediaPlayer mediaPlayerOut;
    private AudioManager audioManager;

    private static volatile NotificationsController Instance = null;
    public static NotificationsController getInstance() {
        NotificationsController localInstance = Instance;
        if (localInstance == null) {
            synchronized (GCMIntentNotificationService.class){
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new NotificationsController();
                }
            }
        }
        return localInstance;
    }

    public NotificationsController() {
        notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
        inChatSoundEnabled = ApplicationLoader.getPreferences().getChatSound();

        try {
            audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(Context.AUDIO_SERVICE);
            //mediaPlayer = new MediaPlayer();
        } catch (Exception e) {
            FileLog.e(TAG, e);
        }
    }
    
    public void scheduleNotificationRepeat() {
        try {
            AlarmManager alarm = (AlarmManager) ApplicationLoader.applicationContext.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pintent = PendingIntent.getService(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class), 0);
            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", Activity.MODE_PRIVATE);
            int minutes = preferences.getInt("repeat_messages", 60);
            if (minutes > 0 && personal_count > 0) {
                alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + minutes * 60 * 1000, pintent);
            } else {
                alarm.cancel(pintent);
            }
        } catch (Exception e) {
            FileLog.e(TAG, e);
        }
    }

    public void scheduleNotificationDelay(boolean onlineReason) {
        try {
            FileLog.e(TAG, "delay notification start");
            AlarmManager alarm = (AlarmManager) ApplicationLoader.applicationContext.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pintent = PendingIntent.getService(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, NotificationDelay.class), 0);
            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", Activity.MODE_PRIVATE);
            if (onlineReason) {
                alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 3 * 1000, pintent);
            } else {
                alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 500, pintent);
            }
        } catch (Exception e) {
            FileLog.e(TAG, e);
        }
    }

    public void notificationDelayReached() {
        FileLog.e(TAG, "delay reached");
		showOrUpdateNotification(false,true, 0, "notifications", 0);
    }

    public void repeatNotificationMaybe() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 11 && hour <= 22) {
            notificationManager.cancel(1);
			showOrUpdateNotification(false,true, 0, "notifications", 0);
        } else {
            scheduleNotificationRepeat();
        }
    }
    
    public void showOrUpdateNotification(boolean isFileDownloaded,boolean isThumbnailDownloaded, int mId, String mCategory, int mType){
    	try{
        	mContext = ApplicationLoader.getApplication().getApplicationContext();
        	Intent intent =  new NotificationHandle(ApplicationLoader.applicationContext, mId, mCategory, mType).getIntent();
            intent.setFlags(32768);
            
            PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            
            String mTitle = intent.getStringExtra(AppConstants.NOTIFICATION.TITLE);
            
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ApplicationLoader.applicationContext)
            .setContentTitle(mTitle)
            .setSmallIcon(R.drawable.ic_mobcast_notification_white) //Notification small white icon
            .setAutoCancel(true)
            .setNumber(total_unread_count)
            .setContentIntent(contentIntent)
            .setGroup("messages")
            .setGroupSummary(true)
            .setColor(0xff2ca5e0);
            
            int priority = 0; //Notification SDK 21+ has priority
            if (priority == 0) {
                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            } else if (priority == 1) {
                mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            } else if (priority == 2) {
                mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            }

            mBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
            
            String message = intent.getStringExtra(AppConstants.NOTIFICATION.MESSAGE);
            
            mBuilder.setContentText(message);
            Bitmap mLargeBitmap =  BitmapFactory.decodeResource(ApplicationLoader.getApplication().getResources(),R.drawable.ic_launcher);
            
            mBuilder.setLargeIcon(mLargeBitmap);//Notification Large Icon
            mBuilder.setTicker(mTitle);//Notification Ticker at status bar
            
            String choosenSoundPath = "MobcastSound";
            String defaultPath = Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
            
            try{
            	if (choosenSoundPath != null && !choosenSoundPath.equals("NoSound")) {
                    if (choosenSoundPath.equals(defaultPath)) {
                        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, AudioManager.STREAM_NOTIFICATION);
                    } else {
                    	Uri mUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification_sound);
                    	mBuilder.setSound(mUri);
//                        mBuilder.setSound(mUri, AudioManager.STREAM_NOTIFICATION);
//                    	mBuilder.setSubText(total_unread_count + " " +mContext.getResources().getString(R.string.new_) + " "+ mCategory);
                    }
                }
            }catch(Exception e){
            	FileLog.e(TAG, e.toString());
            }
            
            int ledColor = 0xff00ff00;
            int needVibrate = 0;
            
            if (ledColor != 0) {
                mBuilder.setLights(ledColor, 1000, 1000);
            }
            if (needVibrate == 2) {
                mBuilder.setVibrate(new long[]{0, 0});
            } else if (needVibrate == 1) {
                mBuilder.setVibrate(new long[]{0, 100, 0, 100});
            } else if (needVibrate == 0 || needVibrate == 4) {
                mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
            } else if (needVibrate == 3) {
                mBuilder.setVibrate(new long[]{0, 1000});
            }
            
            switch(mType){
            case AppConstants.TYPE.TEXT:
            	mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            	mBuilder.setSubText("1 "+mContext.getResources().getString(R.string.new_) + " "+ mCategory);
            	break;
            case AppConstants.TYPE.IMAGE:
    			try {
    				BitmapFactory.Options options = new BitmapFactory.Options();
    				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    				String mImagePath = intent.getStringExtra(AppConstants.NOTIFICATION.THUMBPATH);
    				Bitmap mImageBitmap = BitmapFactory.decodeFile(mImagePath,options);
    				mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(mImageBitmap));
    			} catch (Exception e) {
    				FileLog.e(TAG, e.toString());
    			}
            	break;
            case AppConstants.TYPE.AWARD:
    			try {
    				Intent awardCongratulateIntent = new Intent(mContext, NotificationActionAwardReceiver.class);
    				awardCongratulateIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, intent.getStringExtra(AppConstants.INTENTCONSTANTS.ID));
    				awardCongratulateIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.AWARD);
    				awardCongratulateIntent.putExtra(AppConstants.INTENTCONSTANTS.ACTION, AppConstants.REPORT.CONG);
    				PendingIntent awardCongratulatePendingIntent = PendingIntent.getBroadcast(mContext,0, awardCongratulateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    				mBuilder.addAction(R.drawable.ic_notification_action_award, mContext.getResources().getString(R.string.congratulated_action), awardCongratulatePendingIntent);
    				
    				String mImagePath = intent.getStringExtra(AppConstants.NOTIFICATION.THUMBPATH);
    				Bitmap mImageBitmap = Utilities.getRoundedBitmap(mImagePath);
    				if(mImageBitmap!=null){
    					mBuilder.setLargeIcon(mImageBitmap);
    				}
    			} catch (Exception e) {
    				FileLog.e(TAG, e.toString());
    			}
            	break;
            case AppConstants.TYPE.VIDEO:
            	try {
    				BitmapFactory.Options options = new BitmapFactory.Options();
    				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    				String mImagePath = intent.getStringExtra(AppConstants.NOTIFICATION.THUMBPATH);
    				Bitmap mImageBitmap = BitmapFactory.decodeFile(mImagePath,options);
    				mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(mImageBitmap));
    			} catch (Exception e) {
    				FileLog.e(TAG, e.toString());
    			}
            	break;
            case AppConstants.TYPE.EVENT:
    			try {
    				Intent eventActionGoingIntent = new Intent(mContext, NotificationActionEventAcceptReceiver.class);
    				eventActionGoingIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, intent.getStringExtra(AppConstants.INTENTCONSTANTS.ID));
    				eventActionGoingIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.EVENT);
    				eventActionGoingIntent.putExtra(AppConstants.INTENTCONSTANTS.ACTION, AppConstants.REPORT.JOIN);
    				PendingIntent pendingEventActionGoingIntent = PendingIntent.getBroadcast(mContext,0, eventActionGoingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    				mBuilder.addAction(R.drawable.ic_notification_action_check, mContext.getResources().getString(R.string.accept), pendingEventActionGoingIntent);
    				
    				Intent eventActionDeclineIntent = new Intent(mContext, NotificationActionEventDeclineReceiver.class);
    				eventActionDeclineIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, intent.getStringExtra(AppConstants.INTENTCONSTANTS.ID));
    				eventActionDeclineIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.EVENT);
    				eventActionDeclineIntent.putExtra(AppConstants.INTENTCONSTANTS.ACTION, AppConstants.REPORT.DECL);
    				PendingIntent pendingEventActionDeclineIntent = PendingIntent.getBroadcast(mContext,0, eventActionDeclineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    				mBuilder.addAction(R.drawable.ic_notification_action_close, mContext.getResources().getString(R.string.notgoing), pendingEventActionDeclineIntent);
    				
    			} catch (Exception e) {
    				FileLog.e(TAG, e.toString());
    			}
            	break;
            case AppConstants.TYPE.REFERRAL:
            	mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            	mBuilder.setSubText("Update on Parichay");
            	break;
            case AppConstants.TYPE.PDF:
            	break;
            }
            
            notificationManager.notify(AppConstants.NOTIFICATION_ID, mBuilder.build());
            sendAlertToPebble(message);
            //To Do
//            showWearNotifications(isBoolean);
//            scheduleNotificationRepeat();
            sendBroadcastIfAppIsRunning(mCategory, mId);
            if(ApplicationLoader.getPreferences().isAnyDoNotification()){
            	if((mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.RELOGIN) 
            			|| mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.UPDATEAPP)
            			|| mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REFERRAL)
            			|| mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.BIRTHDAY))){
            		
            	}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.CHAT)){
            		sendBroadcastIfAppIsRunningForChat();        		
            	}else{
            		showAnyDoNotification(mId, mCategory, mType);	
            	}
            }
         Utilities.showBadgeNotification(mContext);  
    	}catch(Exception e){
    		FileLog.e(TAG, e.toString());
    	}
    }
    
    private void showAnyDoNotification(int mId, String mCategory, int mType){
    	Intent mIntent= new Intent(ApplicationLoader.getApplication().getApplicationContext(), AnyDoNotificationService.class);
    	mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mCategory);
    	mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
    	mIntent.putExtra(AppConstants.INTENTCONSTANTS.TYPE, mType);
    	ApplicationLoader.getApplication().getApplicationContext().startService(mIntent);
    }
    
    
    private void sendBroadcastIfAppIsRunning(String mBroadcast, int mId) {
		try {
			ActivityManager am = (ActivityManager) ApplicationLoader
					.getApplication()
					.getSystemService(
							ApplicationLoader.getApplication().ACTIVITY_SERVICE);
			// get the info from the currently running task
			List<ActivityManager.RunningTaskInfo> taskInfo = am
					.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			// if app is running
			if (componentInfo.getPackageName().equalsIgnoreCase(ApplicationLoader.getApplication().getResources()
									.getString(R.string.package_name))) {
				Intent mIntent = new Intent(BROADCAST_ACTION);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mBroadcast);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
				if(mBroadcast.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
					ApplicationLoader.getPreferences().setRefreshMobcastWithNewDataAvail(true);
				}else if(mBroadcast.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
					ApplicationLoader.getPreferences().setRefreshTrainingWithNewDataAvail(true);
				}
				ApplicationLoader.getApplication().getApplicationContext().sendBroadcast(mIntent);
			}else{
				ApplicationLoader.getPreferences().setRefreshMobcastWithNewDataAvail(false);
				ApplicationLoader.getPreferences().setRefreshTrainingWithNewDataAvail(false);
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
    
    private void sendBroadcastIfAppIsRunningForChat() {
		try {
			ActivityManager am = (ActivityManager) ApplicationLoader
					.getApplication()
					.getSystemService(
							ApplicationLoader.getApplication().ACTIVITY_SERVICE);
			// get the info from the currently running task
			List<ActivityManager.RunningTaskInfo> taskInfo = am
					.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			// if app is running
			if (componentInfo.getPackageName().equalsIgnoreCase(ApplicationLoader.getApplication().getResources()
									.getString(R.string.package_name))) {
				Intent mIntent = new Intent(BROADCAST_CHAT_ACTION);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.CHAT);
				ApplicationLoader.getApplication().getApplicationContext().sendBroadcast(mIntent);
			} 
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
    
    
    private void sendAlertToPebble(String message) {
        try {
            final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

            final HashMap<String, String> data = new HashMap<>();
            data.put("title", LocaleController.getString("AppName", R.string.app_name));
            data.put("body", message);
            final JSONObject jsonData = new JSONObject(data);
            final String notificationData = new JSONArray().put(jsonData).toString();

            i.putExtra("messageType", "PEBBLE_ALERT");
            i.putExtra("sender", LocaleController.formatString("AppName", R.string.app_name));
            i.putExtra("notificationData", notificationData);

            ApplicationLoader.applicationContext.sendBroadcast(i);
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
    }
    
    public void dismissNotification() {
        try {
            notificationManager.cancel(AppConstants.NOTIFICATION_ID);
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
    }
    
    
}
