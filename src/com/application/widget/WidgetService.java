/**
 * 
 */
package com.application.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class WidgetService extends RemoteViewsService {
	/*
	 * So pretty simple just defining the Adapter of the listview here Adapter
	 * is ListProvider
	 */

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		return (new WidgetListProvider(this.getApplicationContext(), intent));
	}

}
