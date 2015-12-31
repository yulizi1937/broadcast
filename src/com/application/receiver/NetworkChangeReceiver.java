/**
 * 
 */
package com.application.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.application.ui.service.SyncService;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
	private static boolean isCalledOnce = true;
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo activeWifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeHighNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_HIPRI);

		boolean isMobileConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();

		boolean isHighSpeedConnected = activeHighNetInfo != null && activeHighNetInfo.isConnectedOrConnecting();

		boolean isWifiConnected = activeWifiInfo != null && activeWifiInfo.isConnectedOrConnecting();
		if (isWifiConnected || isMobileConnected || isHighSpeedConnected) {
			if (!TextUtils.isEmpty(ApplicationLoader.getPreferences().getAccessToken())) {
				if (!ApplicationLoader.getPreferences().isSyncAlarmService()) {
					if(BuildVars.IS_AUTOSYNC){
						ApplicationLoader.setSyncServiceAlarm();
					}
				}
				if(isCalledOnce){
					context.startService(new Intent(context,SyncService.class));
					isCalledOnce = false;
				}
			}
		}else{
			isCalledOnce = true;
		}
	}
}
