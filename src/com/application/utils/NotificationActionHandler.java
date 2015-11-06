/**
 * 
 */
package com.application.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.application.sqlite.DBConstant;
import com.application.ui.activity.EventDetailActivity;
import com.mobcast.R;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.widget.Toast;


/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class NotificationActionHandler {
	private static final String TAG = NotificationActionHandler.class.getSimpleName();
	private static volatile NotificationActionHandler Instance = null;
	
	private Context mContext;
	private Intent mIntent;
	private String mCategory;
	private String mId;
	private String mAction;

	public static NotificationActionHandler getInstance() {
		NotificationActionHandler localInstance = Instance;
		if (localInstance == null) {
			localInstance = Instance;
			if (localInstance == null) {
				Instance = localInstance = new NotificationActionHandler();
			}
		}
		return localInstance;
	}
	
	public void processNotificationAction(Context mContext, Intent mIntent){
		this.mContext = mContext;
		this.mIntent = mIntent;
		mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
		mId= mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
		mAction = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ACTION);
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
			if(mAction.equalsIgnoreCase(AppConstants.REPORT.CONG)){
				processActionCongratulate();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
			processActionEvent(mAction);
		}
	}
	
	
	private void processActionCongratulate(){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, null, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{mId}, null);
		
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.CONG, "");
			UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
			
			ContentValues mValues = new ContentValues();
			mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE, "true");
			mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, "true");
			mContext.getContentResolver().update(DBConstant.Award_Columns.CONTENT_URI, mValues, DBConstant.Award_Columns.COLUMN_AWARD_ID +"=?", new String[]{mId});
		}
		
		if(mCursor!=null){
			mCursor.close();
		}
		
		NotificationsController.getInstance().dismissNotification();
	}
	
	private void processActionEvent(String mIsGoing){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId}, null);
		
		if (mCursor != null && mCursor.getCount() > 0) {
			mCursor.moveToFirst();
			if(mIsGoing.equalsIgnoreCase(AppConstants.REPORT.JOIN)){
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.JOIN, "");
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.CALE, "");
				addEventToCalendarDirectly(mContext);
				mIsGoing = "1";
			}else if(mIsGoing.equalsIgnoreCase(AppConstants.REPORT.DECL)){
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.DECL, "");
				mIsGoing = "0";
			}
			UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
			
			ContentValues mValues = new ContentValues();
			mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, mIsGoing);
			mValues.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, "true");
			mContext.getContentResolver().update(DBConstant.Event_Columns.CONTENT_URI, mValues, DBConstant.Event_Columns.COLUMN_EVENT_ID +"=?", new String[]{mId});
		}
		
		if(mCursor!=null){
			mCursor.close();
		}
		
		NotificationsController.getInstance().dismissNotification();
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
	private void addEventToCalendarDirectly(Context mContext){
		 try{
			 String mContentTitle = null;
			 String mContentDesc = null;
			 String mContentEventFromTime = null;
			 String mContentEventDate = null;
			 String mContentEventToTime = null;
		     String mContentEventVenue = null;
			 ContentValues values = new ContentValues();
			 Date mFromTime = null;
				Date mToTime = null;
				try {
					Cursor mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{mId}, DBConstant.Event_Columns.COLUMN_EVENT_ID + " DESC");
					if(mCursor!=null && mCursor.getCount() > 0){
						mCursor.moveToFirst();
						mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE));
						mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_DESC));
						mContentEventDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE));
						mContentEventFromTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME));
						mContentEventToTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME));
						mContentEventVenue = mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_VENUE));
					}
					
					if(mCursor!=null){
						mCursor.close();
					}
					mFromTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mContentEventDate + " " +mContentEventFromTime);
					mToTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mContentEventDate + " "+mContentEventToTime);
					values.put(CalendarContract.Events.DTSTART, mFromTime.getTime());
					values.put(CalendarContract.Events.DTEND,mToTime.getTime());
					values.put(CalendarContract.Events.EVENT_LOCATION,mContentEventVenue);
					values.put(CalendarContract.Events.ALL_DAY, false);
					values.put(CalendarContract.Events.STATUS, 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     values.put(CalendarContract.Events.TITLE, mContentTitle);
		     values.put(CalendarContract.Events.DESCRIPTION, mContentDesc);
		     TimeZone timeZone = TimeZone.getDefault();
		     values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		        // default calendar
		     values.put(CalendarContract.Events.CALENDAR_ID, 1);
//		     values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;");
		        //for one hour
//		     values.put(CalendarContract.Events.DURATION, "+P1H");
		     
		     values.put(CalendarContract.Events.HAS_ALARM, 1);
		        // insert event to calendar
		     Uri mUri = mContext.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
		     if(Utilities.checkWhetherInsertedOrNot(TAG, mUri)){
		    	 Toast.makeText(mContext, mContext.getResources().getString(R.string.fragment_event_added), Toast.LENGTH_SHORT).show();
		     }else{
		    	 Toast.makeText(mContext, mContext.getResources().getString(R.string.fragment_event_add_error), Toast.LENGTH_SHORT).show();
		     }
		 }catch(Exception e){
			 FileLog.e(TAG, e.toString());
		 }
	}
}
