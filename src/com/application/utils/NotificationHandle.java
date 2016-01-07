/**
 * 
 */
package com.application.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.application.sqlite.DBConstant;
import com.application.ui.activity.AudioDetailActivity;
import com.application.ui.activity.AwardProfileActivity;
import com.application.ui.activity.BirthdayRecyclerActivity;
import com.application.ui.activity.ChatDetailActivity;
import com.application.ui.activity.DocDetailActivity;
import com.application.ui.activity.EventDetailActivity;
import com.application.ui.activity.FeedbackActivity;
import com.application.ui.activity.ImageDetailActivity;
import com.application.ui.activity.InteractiveDetailActivity;
import com.application.ui.activity.LoginActivity;
import com.application.ui.activity.MotherActivity;
import com.application.ui.activity.ParichayActivity;
import com.application.ui.activity.PdfDetailActivity;
import com.application.ui.activity.PptDetailActivity;
import com.application.ui.activity.QuizActivity;
import com.application.ui.activity.TextDetailActivity;
import com.application.ui.activity.VideoDetailActivity;
import com.application.ui.activity.XlsDetailActivity;
import com.application.ui.activity.YouTubeLiveStreamActivity;
import com.mobcast.R;


/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class NotificationHandle {
	private static final String TAG = NotificationHandle.class.getSimpleName();
	
	private int mId;
	private int mType;
	private String mCategory;
	private Context mContext;
	
	
	public NotificationHandle(Context mContext,int mId, String mCategory, int mType){
		this.mId = mId;
		this.mCategory = mCategory;
		this.mType = mType;
		this.mContext = mContext;
	}
	
	public Intent getIntent() {
		Intent mIntent = null;
		mIntent = getIntentOnType(mIntent);
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			getIntentForMobcast(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			getIntentForTraining(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)){
			getIntentForAward(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.EVENT)){
			getIntentForEvent(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.RELOGIN)){
			getIntentForReLogin(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.CHAT)){
			getIntentForChat(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.UPDATEAPP)){
			getIntentForUpdateApp(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.BIRTHDAY)){
			getIntentForBirthday(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.REFERRAL)){
			getIntentForParichayReferral(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.APPREMIND)){
			getIntentForAppRemind(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.NOTIFREMIND)){
			getIntentForAppRemind(mIntent);
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.LOGIN)){
			getIntentForLogin(mIntent);
		}
		return mIntent;
	}
	
	public Intent getIntentOnType(Intent mIntent){
		switch(mType){
		case AppConstants.TYPE.TEXT:
			mIntent = new Intent(mContext, TextDetailActivity.class);
			break;
		case AppConstants.TYPE.AUDIO:
			mIntent = new Intent(mContext, AudioDetailActivity.class);
			break;
		case AppConstants.TYPE.IMAGE:
			mIntent = new Intent(mContext, ImageDetailActivity.class);
			break;
		case AppConstants.TYPE.VIDEO:
			mIntent = new Intent(mContext, VideoDetailActivity.class);
			break;
		case AppConstants.TYPE.PDF:
			mIntent = new Intent(mContext, PdfDetailActivity.class);
			break;
		case AppConstants.TYPE.XLS:
			mIntent = new Intent(mContext, XlsDetailActivity.class);
			break;
		case AppConstants.TYPE.DOC:
			mIntent = new Intent(mContext, DocDetailActivity.class);
			break;
		case AppConstants.TYPE.PPT:
			mIntent = new Intent(mContext, PptDetailActivity.class);
			break;
		case AppConstants.TYPE.STREAM:
			mIntent = new Intent(mContext, YouTubeLiveStreamActivity.class);
			break;
		case AppConstants.TYPE.FEEDBACK:
			mIntent = new Intent(mContext, FeedbackActivity.class);
			break;
		case AppConstants.TYPE.QUIZ:
			mIntent = new Intent(mContext, QuizActivity.class);
			break;
		case AppConstants.TYPE.EVENT:
			mIntent = new Intent(mContext, EventDetailActivity.class);
			break;
		case AppConstants.TYPE.AWARD:
			mIntent = new Intent(mContext, AwardProfileActivity.class);
			break;
		case AppConstants.TYPE.INTERACTIVE:
			mIntent = new Intent(mContext, InteractiveDetailActivity.class);
			break;
		case AppConstants.TYPE.RELOGIN:
			mIntent = new Intent(mContext, LoginActivity.class);
			break;
		case AppConstants.TYPE.CHAT:
			mIntent = new Intent(mContext, ChatDetailActivity.class);
			break;
		case AppConstants.TYPE.UPDATEAPP:
			mIntent = new Intent(mContext, MotherActivity.class);
			break;
		case AppConstants.TYPE.APPREMIND:
			mIntent = new Intent(mContext, MotherActivity.class);
			break;
		case AppConstants.TYPE.NOTIFREMIND:
			mIntent = new Intent(mContext, MotherActivity.class);
			break;
		case AppConstants.TYPE.BIRTHDAY:
			mIntent = new Intent(mContext, BirthdayRecyclerActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.BIRTHDAY);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
			break;
		case AppConstants.TYPE.REFERRAL:
			mIntent = new Intent(mContext, ParichayActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.REFERRAL);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, mId);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
			break;
		default:
			mIntent = new Intent(mContext, MotherActivity.class);
			break;
		}
		
		return mIntent;
	}
	
	public Intent getIntentForMobcast(Intent mIntent){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID+ "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_Columns.COLUMN_ID + " DESC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.MOBCAST);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC)));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		}
		mCursor.close();
		
		Cursor mFileCursor = mContext.getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID +"=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + " DESC");
		if(mFileCursor!=null && mFileCursor.getCount() > 0){
			mFileCursor.moveToFirst();
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION)));
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES)));
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH)));
		}
		mFileCursor.close();
		
		return mIntent;
	}
	
	public Intent getIntentForTraining(Intent mIntent){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID+ "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_Columns.COLUMN_ID + " DESC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.TRAINING);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC)));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		}
		mCursor.close();
		
		Cursor mFileCursor = mContext.getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID +"=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + " DESC");
		if(mFileCursor!=null && mFileCursor.getCount() > 0){
			mFileCursor.moveToFirst();
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION)));
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES)));
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, mFileCursor.getString(mFileCursor.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH)));
		}
		mFileCursor.close();
		
		return mIntent;
	}
	
	
	public Intent getIntentForEvent(Intent mIntent){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID+ "=?", new String[]{String.valueOf(mId)}, DBConstant.Event_Columns.COLUMN_ID + " DESC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.EVENT);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_DESC)));
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_VENUE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_BY)));
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH)));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		}
		mCursor.close();
		return mIntent;
	}
	
	public Intent getIntentForAward(Intent mIntent){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, null, DBConstant.Award_Columns.COLUMN_AWARD_ID+ "=?", new String[]{String.valueOf(mId)}, DBConstant.Award_Columns.COLUMN_ID + " DESC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.AWARD);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME)));
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION)));
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DATE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_TIME)));
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK)));
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH)));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		}
		mCursor.close();
		return mIntent;
	}	
	
	public Intent getIntentForReLogin(Intent mIntent){
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.RELOGIN);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mContext.getResources().getString(R.string.relogin_title));
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mContext.getResources().getString(R.string.relogin_message));
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		return mIntent;
	}
	
	public Intent getIntentForLogin(Intent mIntent){
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.LOGIN);
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
		mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mContext.getResources().getString(R.string.login_title));
		mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mContext.getResources().getString(R.string.login_message));
		mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
	return mIntent;
	}
	
	public Intent getIntentForAppRemind(Intent mIntent){
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.APPREMIND);
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
		mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mContext.getResources().getString(R.string.appremind_title));
		mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mContext.getResources().getString(R.string.appremind_message));
		mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		return mIntent;
	}
	
	public Intent getIntentForNotifRemind(Intent mIntent){
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.NOTIFREMIND);
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
		mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mContext.getResources().getString(R.string.notifremind_title));
		mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mContext.getResources().getString(R.string.notifremind_message));
		mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		return mIntent;
	}
	
	public Intent getIntentForChat(Intent mIntent){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Chat_Columns.CONTENT_URI, null, null, null, DBConstant.Chat_Columns.COLUMN_ID + " DESC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.CHAT);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, ApplicationLoader.getPreferences().getChatOppositePerson());
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mCursor.getString(mCursor.getColumnIndex(DBConstant.Chat_Columns.COLUMN_MESSAGE)));
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");	
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		}
		mCursor.close();
	    return mIntent;
   }
	
	public Intent getIntentForUpdateApp(Intent mIntent){
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.UPDATEAPP);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mContext.getResources().getString(R.string.update_title_message));
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mContext.getResources().getString(R.string.update_delete_message));
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");	
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, false);
	    return mIntent;
   }
	
	public Intent getIntentForBirthday(Intent mIntent){
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.BIRTHDAY);
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
		mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, mContext.getResources().getString(R.string.birthday_title_message));
		mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, mContext.getResources().getString(R.string.birthday_desc_message));
		mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
		mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");	
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		return mIntent;
	}
	
	public Intent getIntentForParichayReferral(Intent mIntent){
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.REFERRAL);
		mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, String.valueOf(mId));
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Parichay_Referral_Columns.CONTENT_URI, null, DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursor!=null && mCursor.getCount() >0){
			mCursor.moveToFirst();
			mIntent.putExtra(AppConstants.NOTIFICATION.TITLE, "Referred: "+mCursor.getString(mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_NAME)) + " for " + mCursor.getString(mCursor.getColumnIndex(DBConstant.Parichay_Referral_Columns.COLUMN_PARICHAY_REFERRED_FOR)));
			String message = Utilities.getStatusMessageForParichayReferral(mCursor);
			mIntent.putExtra(AppConstants.NOTIFICATION.MESSAGE, message);
			mIntent.putExtra(AppConstants.NOTIFICATION.DURATION, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.PAGES, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.SIZE, " ");
			mIntent.putExtra(AppConstants.NOTIFICATION.THUMBPATH, " ");	
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, true);
		}
		if(mCursor!=null){
			mCursor.close();
		}
		return mIntent;
	}
}
