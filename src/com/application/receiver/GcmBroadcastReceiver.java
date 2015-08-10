/**
 * 
 */
package com.application.receiver;

import java.util.Iterator;
import java.util.Set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.application.ui.service.GCMIntentNotificationService;
import com.application.utils.BuildVars;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	private static final String TAG = GcmBroadcastReceiver.class
			.getSimpleName();
	private Intent mIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMIntentNotificationService.class.getName());
		// Start the service, keeping the device awake while it is launching.
		mIntent = intent.setComponent(comp);
		if (mIntent != null) {

			if (BuildVars.DEBUG_API) {
				dumpIntent(intent);
			}

			startWakefulService(context, mIntent);
//			setResultCode(Activity.RESULT_OK);
		}
	}

	public static void dumpIntent(Intent i) {
		Bundle bundle = i.getExtras();
		if (bundle != null) {
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();
			StringBuilder strBuilder = new StringBuilder();
			Log.i(TAG, "Dumping Intent - GCM");
			while (it.hasNext()) {
				String key = it.next();
				// Log.d(TAG, "[" + key + "=" + bundle.get(key) + "]");
				strBuilder.append("[" + key + "=" + bundle.get(key) + "]");
			}
			Log.d(TAG, strBuilder.toString());
		}
	}
}
