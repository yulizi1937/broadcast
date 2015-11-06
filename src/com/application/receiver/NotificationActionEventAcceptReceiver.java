/**
 * 
 */
package com.application.receiver;

import com.application.utils.NotificationActionHandler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * <b>Description:</b></br>NotificationController : Add Action Trigger</br>
 * @author Vikalp Patel(VikalpPatelCE)
 */
public class NotificationActionEventAcceptReceiver extends WakefulBroadcastReceiver{
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context mContext, Intent mIntent) {
		// TODO Auto-generated method stub
		NotificationActionHandler.getInstance().processNotificationAction(mContext, mIntent);
	}
}
