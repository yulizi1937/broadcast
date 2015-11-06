/**
 * 
 */
package com.application.widget;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.application.sqlite.DBConstant;
import com.application.ui.activity.TextDetailActivity;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.NotificationHandle;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
/**
 * If you are familiar with Adapter of ListView,this is the same as adapter with
 * few changes
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class WidgetListProvider implements RemoteViewsFactory {
	private static final String TAG = WidgetListProvider.class.getSimpleName();
	private ArrayList<WidgetItem> mArrayListWidgetItem = new ArrayList<WidgetItem>();
	private Context mContext = null;
	private int appWidgetId;

	public WidgetListProvider(Context mContext, Intent intent) {
		this.mContext = mContext;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
		populateWidgetListItem();
	}

	private void populateWidgetListItem() {
		try{
			getUnreadMobcast();
			getUnreadTraining();
			getUnreadAward();
			getUnreadEvent();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	

	@Override
	public int getCount() {
		return mArrayListWidgetItem.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * Similar to getView of Adapter where instead of View we return RemoteViews
	 */
	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_row);
		try{
			WidgetItem listItem = mArrayListWidgetItem.get(position);
			remoteView.setTextViewText(R.id.widgetRowTitleTv, listItem.getmTitle());
			remoteView.setTextViewText(R.id.widgetRowDescTv, listItem.getmDesc());
			remoteView.setImageViewResource(R.id.widgetRowImageView, getDrawableFromType(listItem.getmType()));
			
//			final Intent fillInIntent = new NotificationHandle(mContext, Integer.parseInt(listItem.getmId()), listItem.getmCategory(), Utilities.getMediaType(listItem.getmType())).getIntent();
			final Intent fillInIntent = new Intent();
			fillInIntent.setClass(mContext, TextDetailActivity.class);
			fillInIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, listItem.getmCategory());
			fillInIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, listItem.getmId());
			remoteView.setOnClickFillInIntent(R.id.widgetItemLayout, fillInIntent);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		
		return remoteView;
	}

	@Override
	public RemoteViews getLoadingView() {
		return new RemoteViews(mContext.getPackageName(), R.layout.widget_loading);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
		populateWidgetListItem();
	}

	@Override
	public void onDestroy() {
	}
	
	/**
	 * Unread
	 */
	
	private void getUnreadMobcast(){
		try{
			Cursor mCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ + "=?", new String[]{"false"}, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED + " DESC");

			if (mCursor != null && mCursor.getCount() > 0) {
				int mId = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
				int mTitle = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE);
				int mDesc = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC);
				int mType = mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE);
				mCursor.moveToFirst();
				do {
					WidgetItem mObj = new WidgetItem();
					mObj.setmTitle(mCursor.getString(mTitle));
					mObj.setmDesc(mCursor.getString(mDesc));
					mObj.setmId(mCursor.getString(mId));
					mObj.setmType(mCursor.getString(mType));
					mObj.setmCategory(AppConstants.INTENTCONSTANTS.MOBCAST);
					mArrayListWidgetItem.add(mObj);
				} while (mCursor.moveToNext());
			}
			if(mCursor!=null){
				mCursor.close();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getUnreadTraining(){
		try{
			Cursor mCursor = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ + "=?", new String[]{"false"}, DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED + " DESC");

			if (mCursor != null && mCursor.getCount() > 0) {
				int mId = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_ID);
				int mTitle = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE);
				int mDesc = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC);
				int mType = mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE);
				mCursor.moveToFirst();
				do {
					WidgetItem mObj = new WidgetItem();
					mObj.setmTitle(mCursor.getString(mTitle));
					mObj.setmDesc(mCursor.getString(mDesc));
					mObj.setmId(mCursor.getString(mId));
					mObj.setmType(mCursor.getString(mType));
					mObj.setmCategory(AppConstants.INTENTCONSTANTS.TRAINING);
					mArrayListWidgetItem.add(mObj);
				} while (mCursor.moveToNext());
			}
			if(mCursor!=null){
				mCursor.close();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getUnreadAward(){
		try{
			Cursor mCursor = mContext.getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI,null,null,null,DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED+ " DESC");

			if (mCursor != null && mCursor.getCount() > 0) {
				int mId = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_ID);
				int mTitle = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME);
				int mDesc = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION);
				mCursor.moveToFirst();
				do {
					WidgetItem mObj = new WidgetItem();
					mObj.setmTitle(mCursor.getString(mTitle));
					mObj.setmDesc(mCursor.getString(mDesc));
					mObj.setmId(mCursor.getString(mId));
					mObj.setmType(AppConstants.INTENTCONSTANTS.AWARD);
					mObj.setmCategory(AppConstants.INTENTCONSTANTS.AWARD);
					mArrayListWidgetItem.add(mObj);
				} while (mCursor.moveToNext());
			}
			if(mCursor!=null){
				mCursor.close();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getUnreadEvent(){
		try{
			Cursor mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI,null,null,null,DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED+ " DESC");

			if (mCursor != null && mCursor.getCount() > 0) {
				int mId = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_ID);
				int mTitle = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE);
				int mDesc = mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_DESC);
				mCursor.moveToFirst();
				do {
					WidgetItem mObj = new WidgetItem();
					mObj.setmTitle(mCursor.getString(mTitle));
					mObj.setmDesc(mCursor.getString(mDesc));
					mObj.setmId(mCursor.getString(mId));
					mObj.setmType(AppConstants.INTENTCONSTANTS.EVENT);
					mObj.setmCategory(AppConstants.INTENTCONSTANTS.EVENT);
					mArrayListWidgetItem.add(mObj);
				} while (mCursor.moveToNext());
			}
			if(mCursor!=null){
				mCursor.close();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	
	/**
	 * get drawable
	 */
	private int getDrawableFromType(String mType) {
		int mIntType = Utilities.getMediaType(mType);
		switch (mIntType) {
		case AppConstants.TYPE.TEXT:
			return R.drawable.ic_mobcast_text_focused;
		case AppConstants.TYPE.IMAGE:
			return R.drawable.ic_mobcast_image_focus;
		case AppConstants.TYPE.VIDEO:
			return R.drawable.ic_mobcast_video_focused;
		case AppConstants.TYPE.AUDIO:
			return R.drawable.ic_mobcast_audio_focused;
		case AppConstants.TYPE.PDF:
			return R.drawable.ic_mobcast_pdf_focused;
		case AppConstants.TYPE.DOC:
			return R.drawable.ic_mobcast_doc_focus;
		case AppConstants.TYPE.PPT:
			return R.drawable.ic_mobcast_ppt_focused;
		case AppConstants.TYPE.XLS:
			return R.drawable.ic_mobcast_xls_focused;
		case AppConstants.TYPE.FEEDBACK:
			return R.drawable.ic_mobcast_feedback_focused;
		case AppConstants.TYPE.QUIZ:
			return R.drawable.ic_mobcast_quiz_focused;
		case AppConstants.TYPE.AWARD:
			return  R.drawable.ic_award_cong_focus;
		case AppConstants.TYPE.EVENT:
			return R.drawable.ic_event_focused;
					
		default:
			return R.drawable.ic_launcher;
		}
	}

}
