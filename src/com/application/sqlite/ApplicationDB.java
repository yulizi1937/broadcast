package com.application.sqlite;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.application.utils.BuildVars;

public class ApplicationDB extends ContentProvider{
	
	public static final String AUTHORITY = "com.application.sqlite.mobcast.ApplicationDB";
	
	private static final UriMatcher sUriMatcher;
	private static final String TAG = ApplicationDB.class.getSimpleName();
	
	private static final int ANNOUNCEMENT = 1;
	private static final int EVENT        = 2;
	private static final int AWARD        = 3;
	private static final int NEWS         = 4;
	private static final int TRAINING     = 5;
	private static final int FEEDBACK     = 6;
	
	private static HashMap<String, String> announcementProjectionMap;
	private static HashMap<String, String> eventProjectionMap;
	private static HashMap<String, String> awardProjectionMap;
	private static HashMap<String, String> newsProjectionMap;
	private static HashMap<String, String> trainingProjectionMap;
	private static HashMap<String, String> feedbackProjectionMap;
	
	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DBConstant.DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//announcement
			StringBuilder strBuilderAnnouncement = new StringBuilder();
			strBuilderAnnouncement.append("CREATE TABLE ");
			strBuilderAnnouncement.append(DBConstant.TABLE_ANNOUNCEMENT);
			strBuilderAnnouncement.append('(');
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			/*INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),*/
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_ID +" TEXT UNIQUE," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TITLE +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DESC +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FROM +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILELINK +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILEPATH +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILE_APPEND +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_EXPIRY +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TYPE +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_NAME +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SUMMARY +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ +" NUMBER DEFAULT 0," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_SHARE +" NUMBER DEFAULT 0," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_LIKE +" NUMBER DEFAULT 0," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SHARE_NO +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_READ_NO +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SEEN_NO +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED +" TEXT ," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_SHARING +" NUMBER DEFAULT 0," );
			strBuilderAnnouncement.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TIME +" TEXT" );
			strBuilderAnnouncement.append(')');
			db.execSQL(strBuilderAnnouncement.toString());
			if (BuildVars.DEBUG) {
				Log.i(TAG, strBuilderAnnouncement.toString());
			}
		
		//event
		StringBuilder strBuilderEvent = new StringBuilder();
		strBuilderEvent.append("CREATE TABLE ");
		strBuilderEvent.append(DBConstant.TABLE_EVENT);
		strBuilderEvent.append('(');
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		/*INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),*/
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_ID +" TEXT UNIQUE," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_NAME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_TITLE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_DESC +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_VENUE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_DURATION +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_LANDMARK +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_APPEND +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_SUMMARY +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_JOIN +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_CALENDAR +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_MAP +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_DECLINE_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_MAYBE_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ +" NUMBER DEFAULT 1," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE_FORMATTED +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE_FORMATTED +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE +" TEXT" );
		strBuilderEvent.append(')');
		db.execSQL(strBuilderEvent.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderEvent.toString());
		}
		
		//award
		StringBuilder strBuilderAward = new StringBuilder();
		strBuilderAward.append("CREATE TABLE ");
		strBuilderAward.append(DBConstant.TABLE_AWARD);
		strBuilderAward.append('(');
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		/*INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),*/
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_ID +" TEXT UNIQUE," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DATE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_TITLE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DESC +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_APPEND +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_BY +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_NAME +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ +" NUMBER DEFAULT 1," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_NAME +" TEXT" );
		strBuilderAward.append(')');
		db.execSQL(strBuilderAward.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderAward.toString());
		}
		
		//news
		StringBuilder strBuilderNews = new StringBuilder();
		strBuilderNews.append("CREATE TABLE ");
		strBuilderNews.append(DBConstant.TABLE_NEWS);
		strBuilderNews.append('(');
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		/*INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),*/
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_ID +" TEXT UNIQUE," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_DATE +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_TITLE +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_NAME +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_DESC +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_SUMMARY +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_SOURCE +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_TYPE +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_FILE_LINK +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_FILE_PATH +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_FILE_APPEND +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_IS_READ +" NUMBER DEFAULT 1," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_IS_SHARE +" NUMBER DEFAULT 0," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_LIKE_NO +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_READ_NO +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_BY +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_FROM +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_EXPIRY +" TEXT ," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderNews.append(DBConstant.News_Columns.COLUMN_NEWS_DATE_FORMATTED +" TEXT" );
		strBuilderNews.append(')');
		db.execSQL(strBuilderNews.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderNews.toString());
		}
		
		//training
		StringBuilder strBuilderTraining = new StringBuilder();
		strBuilderTraining.append("CREATE TABLE ");
		strBuilderTraining.append(DBConstant.TABLE_TRAINING);
		strBuilderTraining.append('(');
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		/*INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),*/
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_ID +" TEXT UNIQUE," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_DATE +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_NAME +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_DESC +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_SUMMARY +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_SOURCE +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_LINK +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_PATH +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_APPEND +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ +" NUMBER DEFAULT 1," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARE +" NUMBER DEFAULT 0," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_BY +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_FROM +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY +" TEXT ," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED +" TEXT" );
		strBuilderTraining.append(')');
		db.execSQL(strBuilderTraining.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderTraining.toString());
		}
		
		//feedback
		StringBuilder strBuilderFeedback = new StringBuilder();
		strBuilderFeedback.append("CREATE TABLE ");
		strBuilderFeedback.append(DBConstant.TABLE_FEEDBACK);
		strBuilderFeedback.append('(');
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		/*INTEGER(20) PRIMARY KEY NOT NULL DEFAULT (STRFTIME('%s',CURRENT_TIMESTAMP)),*/
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ID +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DATE +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_TITLE +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_NAME +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DESC +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_SUMMARY +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_READ +" NUMBER DEFAULT 1," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_SHARE +" NUMBER DEFAULT 0," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_LIKE_NO +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_READ_NO +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ATTEMPT_NO +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_BY +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_FROM +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_EXPIRY +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DATE_FORMATTED +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_QUESTION +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_QUESTION_TYPE +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION1 +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION2 +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION3 +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION4 +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_CORRECT_OPTION +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_TIME_LIMIT +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_SCORE +" TEXT ," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_ATTEMPTED +" NUMBER DEFAULT 0," );
		strBuilderFeedback.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_RANKING +" TEXT" );
		strBuilderFeedback.append(')');
		db.execSQL(strBuilderFeedback.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderFeedback.toString());
		}
		
		/*
		 * CREATE INDEX : FOR FAST RETREIVING 
		 */
		
		StringBuilder strBuilderAnnouncementIndex = new StringBuilder();
		strBuilderAnnouncementIndex.append("CREATE UNIQUE INDEX ");
		strBuilderAnnouncementIndex.append(DBConstant.INDEX_ANNOUNCEMENT_ID);
		strBuilderAnnouncementIndex.append(" ON ");
		strBuilderAnnouncementIndex.append(DBConstant.TABLE_ANNOUNCEMENT +" ");
		strBuilderAnnouncementIndex.append('(');
		strBuilderAnnouncementIndex.append(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_ID  );
		strBuilderAnnouncementIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderAnnouncementIndex.append(')');
		db.execSQL(strBuilderAnnouncementIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderAnnouncementIndex.toString());
		}
		
		StringBuilder strBuilderEventIndex = new StringBuilder();
		strBuilderEventIndex.append("CREATE UNIQUE INDEX ");
		strBuilderEventIndex.append(DBConstant.INDEX_EVENT_ID);
		strBuilderEventIndex.append(" ON ");
		strBuilderEventIndex.append(DBConstant.TABLE_EVENT +" ");
		strBuilderEventIndex.append('(');
		strBuilderEventIndex.append(DBConstant.Event_Columns.COLUMN_EVENT_ID  );
		strBuilderEventIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderEventIndex.append(')');
		db.execSQL(strBuilderEventIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderEventIndex.toString());
		}
		
		StringBuilder strBuilderNewsIndex = new StringBuilder();
		strBuilderNewsIndex.append("CREATE UNIQUE INDEX ");
		strBuilderNewsIndex.append(DBConstant.INDEX_NEWS_ID);
		strBuilderNewsIndex.append(" ON ");
		strBuilderNewsIndex.append(DBConstant.TABLE_NEWS +" ");
		strBuilderNewsIndex.append('(');
		strBuilderNewsIndex.append(DBConstant.News_Columns.COLUMN_NEWS_ID  );
		strBuilderNewsIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderNewsIndex.append(')');
		db.execSQL(strBuilderNewsIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderNewsIndex.toString());
		}
		
		StringBuilder strBuilderAwardIndex = new StringBuilder();
		strBuilderAwardIndex.append("CREATE UNIQUE INDEX ");
		strBuilderAwardIndex.append(DBConstant.INDEX_AWARD_ID);
		strBuilderAwardIndex.append(" ON ");
		strBuilderAwardIndex.append(DBConstant.TABLE_AWARD +" ");
		strBuilderAwardIndex.append('(');
		strBuilderAwardIndex.append(DBConstant.Award_Columns.COLUMN_AWARD_ID  );
		strBuilderAwardIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderAwardIndex.append(')');
		db.execSQL(strBuilderAwardIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderAwardIndex.toString());
		}
		
		StringBuilder strBuilderTrainingIndex = new StringBuilder();
		strBuilderTrainingIndex.append("CREATE UNIQUE INDEX ");
		strBuilderTrainingIndex.append(DBConstant.INDEX_TRAINING_ID);
		strBuilderTrainingIndex.append(" ON ");
		strBuilderTrainingIndex.append(DBConstant.TABLE_TRAINING +" ");
		strBuilderTrainingIndex.append('(');
		strBuilderTrainingIndex.append(DBConstant.Training_Columns.COLUMN_TRAINING_ID  );
		strBuilderTrainingIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderTrainingIndex.append(')');
		db.execSQL(strBuilderTrainingIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderTrainingIndex.toString());
		}
		
		StringBuilder strBuilderFeedbackIndex = new StringBuilder();
		strBuilderFeedbackIndex.append("CREATE UNIQUE INDEX ");
		strBuilderFeedbackIndex.append(DBConstant.INDEX_FEEDBACK_ID);
		strBuilderFeedbackIndex.append(" ON ");
		strBuilderFeedbackIndex.append(DBConstant.TABLE_FEEDBACK +" ");
		strBuilderFeedbackIndex.append('(');
		strBuilderFeedbackIndex.append(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ID  );
		strBuilderFeedbackIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderFeedbackIndex.append(')');
		db.execSQL(strBuilderFeedbackIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderFeedbackIndex.toString());
		}
	}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_ANNOUNCEMENT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_EVENT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_AWARD);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_NEWS);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TRAINING);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_FEEDBACK);
			
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_ANNOUNCEMENT_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_EVENT_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_AWARD_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_NEWS_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_TRAINING_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_FEEDBACK_ID);
			
			onCreate(db);
		}
	}

	/* VERSION      DATABASE_VERSION      MODIFIED            BY
	 * ----------------------------------------------------------------
	 * V 0.0.1             1              31/03/15        VIKALP PATEL
	 * -----------------------------------------------------------------
	 */
	private static final int DATABASE_VERSION = 1;
		
	OpenHelper openHelper;


	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case ANNOUNCEMENT:
			count = db.delete(DBConstant.TABLE_ANNOUNCEMENT, where, whereArgs);
			break;
		case EVENT:
			count = db.delete(DBConstant.TABLE_EVENT, where, whereArgs);
			break;
		case AWARD:
			count = db.delete(DBConstant.TABLE_AWARD, where, whereArgs);
			break;
		case NEWS:
			count = db.delete(DBConstant.TABLE_NEWS, where, whereArgs);
			break;
		case TRAINING:
			count = db.delete(DBConstant.TABLE_TRAINING, where, whereArgs);
			break;
		case FEEDBACK:
			count = db.delete(DBConstant.TABLE_FEEDBACK, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}


	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) {
		case ANNOUNCEMENT:
			return DBConstant.Announcement_Columns.CONTENT_TYPE;
		case EVENT:
			return DBConstant.Event_Columns.CONTENT_TYPE;
		case AWARD:
			return DBConstant.Award_Columns.CONTENT_TYPE;
		case NEWS:
			return DBConstant.News_Columns.CONTENT_TYPE;
		case TRAINING:
			return DBConstant.Training_Columns.CONTENT_TYPE;
		case FEEDBACK:
			return DBConstant.Feedback_Columns.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}


	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != ANNOUNCEMENT && sUriMatcher.match(uri)!= EVENT
				&& sUriMatcher.match(uri)!=AWARD && sUriMatcher.match(uri)!= NEWS
				&& sUriMatcher.match(uri)!= TRAINING && sUriMatcher.match(uri)!= FEEDBACK)
		{ 
			throw new IllegalArgumentException("Unknown URI " + uri); 
		}
		
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} 
		else {
			values = new ContentValues();
		}
		
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long rowId = 0;
		
		switch (sUriMatcher.match(uri)) 
		{
			case ANNOUNCEMENT:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_ANNOUNCEMENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Announcement_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case EVENT:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_EVENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Event_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case AWARD:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_AWARD, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Award_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case NEWS:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_NEWS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.News_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case TRAINING:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_TRAINING, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Training_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case FEEDBACK:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_FEEDBACK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Feedback_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
				
		}
		throw new SQLException("Failed to insert row into " + uri);
	}


	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		openHelper 		= new OpenHelper(getContext());
		return true;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = openHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)) {
		case ANNOUNCEMENT:
			qb.setTables(DBConstant.TABLE_ANNOUNCEMENT);
			qb.setProjectionMap(announcementProjectionMap);
			break;
		case EVENT:
			qb.setTables(DBConstant.TABLE_EVENT);
			qb.setProjectionMap(eventProjectionMap);
			break;
		case AWARD:
			qb.setTables(DBConstant.TABLE_AWARD);
			qb.setProjectionMap(awardProjectionMap);
			break;
		case NEWS:
			qb.setTables(DBConstant.TABLE_NEWS);
			qb.setProjectionMap(newsProjectionMap);
			break;
		case TRAINING:
			qb.setTables(DBConstant.TABLE_TRAINING);
			qb.setProjectionMap(trainingProjectionMap);
			break;
		case FEEDBACK:
			qb.setTables(DBConstant.TABLE_FEEDBACK);
			qb.setProjectionMap(feedbackProjectionMap);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
//		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}


	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int count = -1;
		switch (sUriMatcher.match(uri)) {
		case ANNOUNCEMENT:
			count = db.update(DBConstant.TABLE_ANNOUNCEMENT, values, where, whereArgs);
			break;
		case EVENT:
			count = db.update(DBConstant.TABLE_EVENT, values, where, whereArgs);
			break;
		case AWARD:
			count = db.update(DBConstant.TABLE_AWARD, values, where, whereArgs);
			break;
		case NEWS:
			count = db.update(DBConstant.TABLE_NEWS, values, where, whereArgs);
			break;
		case TRAINING:
			count = db.update(DBConstant.TABLE_TRAINING, values, where, whereArgs);
			break;
		case FEEDBACK:
			count = db.update(DBConstant.TABLE_FEEDBACK, values, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_ANNOUNCEMENT, ANNOUNCEMENT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_EVENT, EVENT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_AWARD, AWARD);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_NEWS, NEWS);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TRAINING, TRAINING);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_FEEDBACK, FEEDBACK);

		
		announcementProjectionMap = new HashMap<String, String>();
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ID, DBConstant.Announcement_Columns.COLUMN_ID);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_ID, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_ID);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TITLE, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TITLE);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DESC, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DESC);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FROM, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FROM);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILELINK, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILELINK);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_EXPIRY, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_EXPIRY);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TYPE, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TYPE);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_NAME, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_NAME);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SUMMARY, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SUMMARY);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_SHARE, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_SHARE);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_LIKE, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_LIKE);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SHARE_NO, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SHARE_NO);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_READ_NO, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_READ_NO);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SEEN_NO, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_SEEN_NO);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TIME, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_TIME);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILEPATH, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILEPATH);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILE_APPEND, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_FILE_APPEND);
		announcementProjectionMap.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_SHARING, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_SHARING);
		
		eventProjectionMap = new HashMap<String, String>();
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_ID, DBConstant.Event_Columns.COLUMN_ID);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_ID, DBConstant.Event_Columns.COLUMN_EVENT_ID);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_NAME, DBConstant.Event_Columns.COLUMN_EVENT_NAME);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_TITLE, DBConstant.Event_Columns.COLUMN_EVENT_TITLE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_DESC, DBConstant.Event_Columns.COLUMN_EVENT_DESC);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_VENUE, DBConstant.Event_Columns.COLUMN_EVENT_VENUE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME, DBConstant.Event_Columns.COLUMN_EVENT_START_TIME);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME, DBConstant.Event_Columns.COLUMN_EVENT_END_TIME);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE, DBConstant.Event_Columns.COLUMN_EVENT_START_DATE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE, DBConstant.Event_Columns.COLUMN_EVENT_END_DATE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_DURATION, DBConstant.Event_Columns.COLUMN_EVENT_DURATION);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_LANDMARK, DBConstant.Event_Columns.COLUMN_EVENT_LANDMARK);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY, DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK, DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH, DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_APPEND, DBConstant.Event_Columns.COLUMN_EVENT_FILE_APPEND);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_SUMMARY, DBConstant.Event_Columns.COLUMN_EVENT_SUMMARY);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_JOIN, DBConstant.Event_Columns.COLUMN_EVENT_JOIN);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_CALENDAR, DBConstant.Event_Columns.COLUMN_EVENT_IS_CALENDAR);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_MAP, DBConstant.Event_Columns.COLUMN_EVENT_MAP);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_DECLINE_NO, DBConstant.Event_Columns.COLUMN_EVENT_DECLINE_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_MAYBE_NO, DBConstant.Event_Columns.COLUMN_EVENT_MAYBE_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, DBConstant.Event_Columns.COLUMN_EVENT_IS_READ);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE_FORMATTED, DBConstant.Event_Columns.COLUMN_EVENT_START_DATE_FORMATTED);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE_FORMATTED, DBConstant.Event_Columns.COLUMN_EVENT_END_DATE_FORMATTED);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE, DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING, DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING);
		
		awardProjectionMap = new HashMap<String, String>();
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_ID, DBConstant.Award_Columns.COLUMN_ID);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_ID, DBConstant.Award_Columns.COLUMN_AWARD_ID);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE, DBConstant.Award_Columns.COLUMN_AWARD_DATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_TITLE, DBConstant.Award_Columns.COLUMN_AWARD_TITLE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DESC, DBConstant.Award_Columns.COLUMN_AWARD_DESC);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK, DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH, DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_APPEND, DBConstant.Award_Columns.COLUMN_AWARD_FILE_APPEND);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARE, DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE, DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_BY, DBConstant.Award_Columns.COLUMN_AWARD_BY);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO, DBConstant.Award_Columns.COLUMN_AWARD_READ_NO);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO, DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED, DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_NAME, DBConstant.Award_Columns.COLUMN_AWARD_NAME);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE, DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO, DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_NAME, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_NAME);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, DBConstant.Award_Columns.COLUMN_AWARD_IS_READ);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY, DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING, DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING);
		
		newsProjectionMap = new HashMap<String, String>();
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_ID, DBConstant.News_Columns.COLUMN_ID);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_ID, DBConstant.News_Columns.COLUMN_NEWS_ID);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_DATE, DBConstant.News_Columns.COLUMN_NEWS_DATE);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_TITLE, DBConstant.News_Columns.COLUMN_NEWS_TITLE);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_NAME, DBConstant.News_Columns.COLUMN_NEWS_NAME);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_DESC, DBConstant.News_Columns.COLUMN_NEWS_DESC);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_SUMMARY, DBConstant.News_Columns.COLUMN_NEWS_SUMMARY);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_SOURCE, DBConstant.News_Columns.COLUMN_NEWS_SOURCE);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_TYPE, DBConstant.News_Columns.COLUMN_NEWS_TYPE);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_FILE_LINK, DBConstant.News_Columns.COLUMN_NEWS_FILE_LINK);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_FILE_PATH, DBConstant.News_Columns.COLUMN_NEWS_FILE_PATH);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_FILE_APPEND, DBConstant.News_Columns.COLUMN_NEWS_FILE_APPEND);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_IS_READ, DBConstant.News_Columns.COLUMN_NEWS_IS_READ);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_IS_LIKE, DBConstant.News_Columns.COLUMN_NEWS_IS_LIKE);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_IS_SHARE, DBConstant.News_Columns.COLUMN_NEWS_IS_SHARE);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_LIKE_NO, DBConstant.News_Columns.COLUMN_NEWS_LIKE_NO);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_READ_NO, DBConstant.News_Columns.COLUMN_NEWS_READ_NO);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_BY, DBConstant.News_Columns.COLUMN_NEWS_BY);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_FROM, DBConstant.News_Columns.COLUMN_NEWS_FROM);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_EXPIRY, DBConstant.News_Columns.COLUMN_NEWS_EXPIRY);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_IS_SHARING, DBConstant.News_Columns.COLUMN_NEWS_IS_SHARING);
		newsProjectionMap.put(DBConstant.News_Columns.COLUMN_NEWS_DATE_FORMATTED, DBConstant.News_Columns.COLUMN_NEWS_DATE_FORMATTED);
		
		trainingProjectionMap = new HashMap<String, String>();
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_ID, DBConstant.Training_Columns.COLUMN_ID);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_ID, DBConstant.Training_Columns.COLUMN_TRAINING_ID);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE, DBConstant.Training_Columns.COLUMN_TRAINING_DATE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE, DBConstant.Training_Columns.COLUMN_TRAINING_TITLE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_NAME, DBConstant.Training_Columns.COLUMN_TRAINING_NAME);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_DESC, DBConstant.Training_Columns.COLUMN_TRAINING_DESC);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_SUMMARY, DBConstant.Training_Columns.COLUMN_TRAINING_SUMMARY);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_SOURCE, DBConstant.Training_Columns.COLUMN_TRAINING_SOURCE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE, DBConstant.Training_Columns.COLUMN_TRAINING_TYPE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_LINK, DBConstant.Training_Columns.COLUMN_TRAINING_FILE_LINK);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_PATH, DBConstant.Training_Columns.COLUMN_TRAINING_FILE_PATH);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_APPEND, DBConstant.Training_Columns.COLUMN_TRAINING_FILE_APPEND);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARE, DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO, DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_BY, DBConstant.Training_Columns.COLUMN_TRAINING_BY);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_FROM, DBConstant.Training_Columns.COLUMN_TRAINING_FROM);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY, DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING, DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED, DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED);
		
		feedbackProjectionMap = new HashMap<String, String>();
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_ID, DBConstant.Feedback_Columns.COLUMN_ID);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ID, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ID);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DATE, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DATE);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_TITLE, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_TITLE);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_NAME, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_NAME);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DESC, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DESC);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_SUMMARY, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_SUMMARY);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_READ, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_READ);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_LIKE, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_LIKE);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_SHARE, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_SHARE);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_READ_NO, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_READ_NO);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_LIKE_NO, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_LIKE_NO);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ATTEMPT_NO, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_ATTEMPT_NO);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_BY, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_BY);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_FROM, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_FROM);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_EXPIRY, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_EXPIRY);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DATE_FORMATTED, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_DATE_FORMATTED);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_QUESTION, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_QUESTION);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_QUESTION_TYPE, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_QUESTION_TYPE);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION1, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION1);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION2, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION2);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION3, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION3);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION4, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_OPTION4);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_CORRECT_OPTION, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_CORRECT_OPTION);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_TIME_LIMIT, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_TIME_LIMIT);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_SCORE, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_SCORE);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_ATTEMPTED, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_IS_ATTEMPTED);
		feedbackProjectionMap.put(DBConstant.Feedback_Columns.COLUMN_FEEDBACK_RANKING, DBConstant.Feedback_Columns.COLUMN_FEEDBACK_RANKING);
		
		}
}
