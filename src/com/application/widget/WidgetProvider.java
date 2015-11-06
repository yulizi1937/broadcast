/**
 * 
 */
package com.application.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.application.ui.activity.MotherActivity;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class WidgetProvider extends AppWidgetProvider {
	public static final String ACTION = "WidgetProvider.Action";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	/*
	 * this method is called every 30 mins as specified on widgetinfo.xml this
	 * method is also called on every phone reboot
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		/*
		 * int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen
		 */
		for (int i = 0; i < N; ++i) {
			RemoteViews remoteViews = updateWidgetListView(context,
					appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

		// which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		// RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, WidgetService.class);
		// passing app widget id to that RemoteViews Service
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// setting a unique Uri to the intent
		// don't know its purpose to me right now
		// svcIntent.setData(Uri.fromParts("content",
		// String.valueOf(appWidgetId), null));
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		// setting adapter to listview of the widget
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
				svcIntent);
		remoteViews.setTextViewText(R.id.widgetUnreadTv,Utilities.getUnreadCount(context) + " "+ context.getResources().getString(R.string.unread));
		// setting an empty view in case of no data
		remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
		 // Each list item will call setOnClickExtra() to let the list know
        // which item
        // is selected by a user.
        final PendingIntent updateEventIntent = getLaunchPendingIntentTemplate(context);
        remoteViews.setPendingIntentTemplate(R.id.listViewWidget, updateEventIntent);
		return remoteViews;
	}
	
	 /**
     * Build a {@link PendingIntent} to launch the Calendar app. This should be used
     * in combination with {@link RemoteViews#setPendingIntentTemplate(int, PendingIntent)}.
     */
    private PendingIntent getLaunchPendingIntentTemplate(Context context) {
        Intent launchIntent = new Intent();
        launchIntent.setAction(Intent.ACTION_VIEW);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            launchIntent.setClass(context, MotherActivity.class);
            return PendingIntent.getActivity(context, 0 /* no requestCode */, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}