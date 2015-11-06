/**
 * 
 */
package com.application.ui.service;

import android.app.IntentService;
import android.content.Intent;

import com.application.utils.AndroidUtilities;
import com.application.utils.NotificationsController;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class NotificationRepeat extends IntentService {

    public NotificationRepeat() {
        super("NotificationRepeat");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                NotificationsController.getInstance().repeatNotificationMaybe();
            }
        });
    }
}
