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
	
	private static final int MOBCAST           = 1;
	private static final int CHAT              = 2;
	private static final int TRAINING          = 3;
	private static final int EVENTS            = 4;
	private static final int AWARDS            = 5;
	private static final int BIRTHDAY          = 6;
	private static final int MOBCAST_FILE      = 7;
	private static final int TRAINING_FILE     = 8;
	private static final int MOBCAST_FEEDBACK  = 9;
	private static final int TRAINING_QUIZ     = 10;
	
	private static HashMap<String, String> mobcastProjectionMap;
	private static HashMap<String, String> chatProjectionMap;
	private static HashMap<String, String> trainingProjectionMap;
	private static HashMap<String, String> eventProjectionMap;
	private static HashMap<String, String> awardProjectionMap;
	private static HashMap<String, String> birthdayProjectionMap;
	private static HashMap<String, String> mobcastFileProjectionMap;
	private static HashMap<String, String> trainingFileProjectionMap;
	private static HashMap<String, String> mobcastFeedbackProjectionMap;
	private static HashMap<String, String> trainingQuizProjectionMap;
	
	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DBConstant.DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//mobcast
			StringBuilder strBuilderMobcast = new StringBuilder();
			strBuilderMobcast.append("CREATE TABLE ");
			strBuilderMobcast.append(DBConstant.TABLE_MOBCAST);
			strBuilderMobcast.append('(');
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID +" INTEGER UNIQUE," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED +" INTEGER ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED +" INTEGER ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ +" NUMBER DEFAULT 0," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING +" NUMBER DEFAULT 0," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARE +" NUMBER DEFAULT 0," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE +" NUMBER DEFAULT 0," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE +" NUMBER DEFAULT 1," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_READ_NO +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_SHARE_NO +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_FILE_ID +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE +" TEXT ," );
			strBuilderMobcast.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME +" TEXT" );
			strBuilderMobcast.append(')');
			db.execSQL(strBuilderMobcast.toString());
			if (BuildVars.DEBUG) {
				Log.i(TAG, strBuilderMobcast.toString());
			}
			
			
			//training
			StringBuilder strBuilderTraining = new StringBuilder();
			strBuilderTraining.append("CREATE TABLE ");
			strBuilderTraining.append(DBConstant.TABLE_TRAINING);
			strBuilderTraining.append('(');
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_ID +" INTEGER UNIQUE," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_BY +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_DESC +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_DATE +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_TIME +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED +" INTEGER ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED +" INTEGER ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ +" NUMBER DEFAULT 0," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING +" NUMBER DEFAULT 0," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARE +" NUMBER DEFAULT 0," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE +" NUMBER DEFAULT 0," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE +" NUMBER DEFAULT 1," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_SHARE_NO +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_LINK +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_ID +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE +" TEXT ," );
			strBuilderTraining.append(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME +" TEXT" );
			strBuilderTraining.append(')');
			db.execSQL(strBuilderTraining.toString());
			if (BuildVars.DEBUG) {
				Log.i(TAG, strBuilderTraining.toString());
			}

			StringBuilder strBuilderChat = new StringBuilder();
			strBuilderChat.append("CREATE TABLE ");
			strBuilderChat.append(DBConstant.TABLE_CHAT);
			strBuilderChat.append('(');
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_USER_ID +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_GROUP_ID +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_CITY_ID +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_USER_ID_MYSQL +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_USER_JABBER_ID +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_GROUP_ID_MYSQL +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_MESSAGE +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_MESSAGE_ID +" TEXT UNIQUE," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_PATH +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_FILE_LINK +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_TYPE +" NUMBER DEFAULT 0," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_ISSENT +" NUMBER DEFAULT 0," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_USER_SENT_MESSAGE +" NUMBER DEFAULT 0," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_ISDELIEVERED +" NUMBER DEFAULT 0," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_ISREAD +" NUMBER DEFAULT 0," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_TIMESTAMP +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_MESSAGE_TIME +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_MESSAGE_DATE +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_TAGGED +" TEXT ," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_ISLEFT +" NUMBER DEFAULT 0," );
			strBuilderChat.append(DBConstant.Chat_Columns.COLUMN_ISNOTIFIED +" NUMBER DEFAULT 0" );
			strBuilderChat.append(')');
			db.execSQL(strBuilderChat.toString());
			if (BuildVars.DEBUG) {
				Log.i(TAG, strBuilderChat.toString());
			}		
			
		
		//event
		StringBuilder strBuilderEvent = new StringBuilder();
		strBuilderEvent.append("CREATE TABLE ");
		strBuilderEvent.append(DBConstant.TABLE_EVENT);
		strBuilderEvent.append('(');
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_ID +" INTEGER UNIQUE," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_TITLE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_BY +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_DURATION +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_LANDMARK +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_VENUE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_DESC +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_DECLINE_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_MAYBE_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN +" NUMBER DEFAULT 2," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_CALENDAR +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_MAP +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_APPEND +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_TIME +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED +" INTEGER ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE_FORMATTED +" TEXT ," );
		strBuilderEvent.append(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE_FORMATTED +" TEXT" );
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
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_ID +" INTEGER UNIQUE," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_NAME +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_CITY +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_FILE_APPEND +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_IS_MESSAGE +" NUMBER DEFAULT 0," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DATE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_TIME +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED +" INTEGER ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_TIME +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE +" TEXT ," );
		strBuilderAward.append(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE +" NUMBER DEFAULT 0" );
		strBuilderAward.append(')');
		db.execSQL(strBuilderAward.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderAward.toString());
		}
		
		
		//birthday
		StringBuilder strBuilderBirthday = new StringBuilder();
		strBuilderBirthday.append("CREATE TABLE ");
		strBuilderBirthday.append(DBConstant.TABLE_BIRTHDAY);
		strBuilderBirthday.append('(');
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID +" INTEGER UNIQUE," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_NAME +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DEPARTMENT +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_LIKE +" NUMBER DEFAULT 0," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MESSAGE +" NUMBER DEFAULT 0," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ +" NUMBER DEFAULT 0," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_SHARE +" NUMBER DEFAULT 0," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_SHARING +" NUMBER DEFAULT 0," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_WISHED +" NUMBER DEFAULT 0," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_AGE +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_SUN_SIGN +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_CITY +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE_FORMATTED +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DAY +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DOB +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_LINK +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_PATH +" TEXT ," );
		strBuilderBirthday.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_APPEND +" TEXT" );
		strBuilderBirthday.append(')');
		db.execSQL(strBuilderBirthday.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderBirthday.toString());
		}
		
		//mobcastfile
		StringBuilder strBuilderMobcastFile = new StringBuilder();
		strBuilderMobcastFile.append("CREATE TABLE ");
		strBuilderMobcastFile.append(DBConstant.TABLE_MOBCAST_FILE);
		strBuilderMobcastFile.append('(');
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID +" INTEGER ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID +" TEXT UNIQUE," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT +" NUMBER DEFAULT 0," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE +" TEXT ," );
		strBuilderMobcastFile.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_APPEND +" TEXT" );
		strBuilderMobcastFile.append(')');
		db.execSQL(strBuilderMobcastFile.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderMobcastFile.toString());
		}
		
		
		//trainingfile
		StringBuilder strBuilderTrainingFile = new StringBuilder();
		strBuilderTrainingFile.append("CREATE TABLE ");
		strBuilderTrainingFile.append(DBConstant.TABLE_TRAINING_FILE);
		strBuilderTrainingFile.append('(');
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_ID +" INTEGER ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID +" TEXT UNIQUE," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT +" NUMBER DEFAULT 0," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE +" TEXT ," );
		strBuilderTrainingFile.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_APPEND +" TEXT" );
		strBuilderTrainingFile.append(')');
		db.execSQL(strBuilderTrainingFile.toString());
		if (BuildVars.DEBUG) {
				Log.i(TAG, strBuilderTrainingFile.toString());
		}
		
		//mobcastfeedback
		StringBuilder strBuilderMobcastFeedback = new StringBuilder();
		strBuilderMobcastFeedback.append("CREATE TABLE ");
		strBuilderMobcastFeedback.append(DBConstant.TABLE_MOBCAST_FEEDBACK);
		strBuilderMobcastFeedback.append('(');
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID +" TEXT UNIQUE," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID +" INTEGER ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_TYPE +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1 +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2 +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3 +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4 +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5 +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6 +" TEXT ," );
		strBuilderMobcastFeedback.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7 +" TEXT" );
		strBuilderMobcastFeedback.append(')');
		db.execSQL(strBuilderMobcastFeedback.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderMobcastFeedback.toString());
		}
		
		//trainingquiz
		StringBuilder strBuilderTrainingQuiz = new StringBuilder();
		strBuilderTrainingQuiz.append("CREATE TABLE ");
		strBuilderTrainingQuiz.append(DBConstant.TABLE_TRAINING_QUIZ);
		strBuilderTrainingQuiz.append('(');
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID +" TEXT UNIQUE," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID +" INTEGER ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7 +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION +" TEXT ," );
		strBuilderTrainingQuiz.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS +" TEXT" );
		strBuilderTrainingQuiz.append(')');
		db.execSQL(strBuilderTrainingQuiz.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderTrainingQuiz.toString());
		}
		
		/*
		 * CREATE INDEX : FOR FAST RETREIVING 
		 */
		
		StringBuilder strBuilderMobcastIndex = new StringBuilder();
		strBuilderMobcastIndex.append("CREATE UNIQUE INDEX ");
		strBuilderMobcastIndex.append(DBConstant.INDEX_MOBCAST_ID);
		strBuilderMobcastIndex.append(" ON ");
		strBuilderMobcastIndex.append(DBConstant.TABLE_MOBCAST +" ");
		strBuilderMobcastIndex.append('(');
		strBuilderMobcastIndex.append(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID  );
		strBuilderMobcastIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderMobcastIndex.append(')');
		db.execSQL(strBuilderMobcastIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderMobcastIndex.toString());
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
		
		StringBuilder strBuilderBirthdayIndex = new StringBuilder();
		strBuilderBirthdayIndex.append("CREATE UNIQUE INDEX ");
		strBuilderBirthdayIndex.append(DBConstant.INDEX_BIRTHDAY_ID);
		strBuilderBirthdayIndex.append(" ON ");
		strBuilderBirthdayIndex.append(DBConstant.TABLE_BIRTHDAY +" ");
		strBuilderBirthdayIndex.append('(');
		strBuilderBirthdayIndex.append(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID  );
		strBuilderBirthdayIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderBirthdayIndex.append(')');
		db.execSQL(strBuilderBirthdayIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderBirthdayIndex.toString());
		}
		
		StringBuilder strBuilderMobcastFileIndex = new StringBuilder();
		strBuilderMobcastFileIndex.append("CREATE UNIQUE INDEX ");
		strBuilderMobcastFileIndex.append(DBConstant.INDEX_MOBCAST_FILE);
		strBuilderMobcastFileIndex.append(" ON ");
		strBuilderMobcastFileIndex.append(DBConstant.TABLE_MOBCAST_FILE +" ");
		strBuilderMobcastFileIndex.append('(');
		strBuilderMobcastFileIndex.append(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID  );
		strBuilderMobcastFileIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderMobcastFileIndex.append(')');
		db.execSQL(strBuilderMobcastFileIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderMobcastFileIndex.toString());
		}
		
		StringBuilder strBuilderTrainingFileIndex = new StringBuilder();
		strBuilderTrainingFileIndex.append("CREATE UNIQUE INDEX ");
		strBuilderTrainingFileIndex.append(DBConstant.INDEX_TRAINING_FILE);
		strBuilderTrainingFileIndex.append(" ON ");
		strBuilderTrainingFileIndex.append(DBConstant.TABLE_TRAINING_FILE +" ");
		strBuilderTrainingFileIndex.append('(');
		strBuilderTrainingFileIndex.append(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID  );
		strBuilderTrainingFileIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderTrainingFileIndex.append(')');
		db.execSQL(strBuilderTrainingFileIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderTrainingFileIndex.toString());
		}
		
		StringBuilder strBuilderMobcastFeedbackIndex = new StringBuilder();
		strBuilderMobcastFeedbackIndex.append("CREATE UNIQUE INDEX ");
		strBuilderMobcastFeedbackIndex.append(DBConstant.INDEX_MOBCAST_FEEDBACK);
		strBuilderMobcastFeedbackIndex.append(" ON ");
		strBuilderMobcastFeedbackIndex.append(DBConstant.TABLE_MOBCAST_FEEDBACK +" ");
		strBuilderMobcastFeedbackIndex.append('(');
		strBuilderMobcastFeedbackIndex.append(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID  );
		strBuilderMobcastFeedbackIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderMobcastFeedbackIndex.append(')');
		db.execSQL(strBuilderMobcastFeedbackIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderMobcastFeedbackIndex.toString());
		}
		
		StringBuilder strBuilderTrainingQuizIndex = new StringBuilder();
		strBuilderTrainingQuizIndex.append("CREATE UNIQUE INDEX ");
		strBuilderTrainingQuizIndex.append(DBConstant.INDEX_TRAINING_QUIZ);
		strBuilderTrainingQuizIndex.append(" ON ");
		strBuilderTrainingQuizIndex.append(DBConstant.TABLE_TRAINING_QUIZ +" ");
		strBuilderTrainingQuizIndex.append('(');
		strBuilderTrainingQuizIndex.append(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID  );
		strBuilderTrainingQuizIndex.append(DBConstant.INDEX_ID_ORDER);
		strBuilderTrainingQuizIndex.append(')');
		db.execSQL(strBuilderTrainingQuizIndex.toString());
		if (BuildVars.DEBUG) {
			Log.i(TAG, strBuilderTrainingQuizIndex.toString());
		}
	}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_MOBCAST);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_CHAT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TRAINING);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_EVENT);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_AWARD);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_BIRTHDAY);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_MOBCAST_FILE);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TRAINING_FILE);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_MOBCAST_FEEDBACK);
			db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_TRAINING_QUIZ);
			
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_MOBCAST_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_TRAINING_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_EVENT_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_AWARD_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_BIRTHDAY_ID);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_MOBCAST_FILE);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_TRAINING_FILE);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_MOBCAST_FEEDBACK);
			db.execSQL("DROP INDEX IF EXISTS " + DBConstant.INDEX_TRAINING_QUIZ);
			
			onCreate(db);
		}
	}

	/* VERSION      DATABASE_VERSION      MODIFIED            BY
	 * ----------------------------------------------------------------
	 * V 0.0.1             1              16/05/15        VIKALP PATEL
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
		case MOBCAST:
			count = db.delete(DBConstant.TABLE_MOBCAST, where, whereArgs);
			break;
		case CHAT:
			count = db.delete(DBConstant.TABLE_CHAT, where, whereArgs);
			break;
		case TRAINING:
			count = db.delete(DBConstant.TABLE_TRAINING, where, whereArgs);
			break;
		case EVENTS:
			count = db.delete(DBConstant.TABLE_EVENT, where, whereArgs);
			break;
		case AWARDS:
			count = db.delete(DBConstant.TABLE_AWARD, where, whereArgs);
			break;
		case BIRTHDAY:
			count = db.delete(DBConstant.TABLE_BIRTHDAY, where, whereArgs);
			break;
		case MOBCAST_FILE:
			count = db.delete(DBConstant.TABLE_MOBCAST_FILE, where, whereArgs);
			break;
		case TRAINING_FILE:
			count = db.delete(DBConstant.TABLE_TRAINING_FILE, where, whereArgs);
			break;
		case MOBCAST_FEEDBACK:
			count = db.delete(DBConstant.TABLE_MOBCAST_FEEDBACK, where, whereArgs);
			break;
		case TRAINING_QUIZ:
			count = db.delete(DBConstant.TABLE_TRAINING_QUIZ, where, whereArgs);
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
		case MOBCAST:
			return DBConstant.Mobcast_Columns.CONTENT_TYPE;
		case TRAINING:
			return DBConstant.Training_Columns.CONTENT_TYPE;
		case CHAT:
			return DBConstant.Chat_Columns.CONTENT_TYPE;
		case EVENTS:
			return DBConstant.Event_Columns.CONTENT_TYPE;
		case AWARDS:
			return DBConstant.Award_Columns.CONTENT_TYPE;
		case BIRTHDAY:
			return DBConstant.Birthday_Columns.CONTENT_TYPE;
		case MOBCAST_FILE:
			return DBConstant.Mobcast_File_Columns.CONTENT_TYPE;
		case TRAINING_FILE:
			return DBConstant.Training_File_Columns.CONTENT_TYPE;
		case MOBCAST_FEEDBACK:
			return DBConstant.Mobcast_Feedback_Columns.CONTENT_TYPE;
		case TRAINING_QUIZ:
			return DBConstant.Training_Quiz_Columns.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}


	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != MOBCAST && sUriMatcher.match(uri)!= EVENTS
				&& sUriMatcher.match(uri)!= CHAT && sUriMatcher.match(uri)!= TRAINING
				&& sUriMatcher.match(uri)!=AWARDS && sUriMatcher.match(uri)!= BIRTHDAY
				&& sUriMatcher.match(uri)!= TRAINING_FILE && sUriMatcher.match(uri)!= MOBCAST_FILE
				&& sUriMatcher.match(uri)!= MOBCAST_FEEDBACK && sUriMatcher.match(uri)!= TRAINING_QUIZ)
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
			case MOBCAST:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_MOBCAST, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Mobcast_Columns.CONTENT_URI, rowId);
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
			case CHAT:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_CHAT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Chat_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case EVENTS:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_EVENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Event_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case AWARDS:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_AWARD, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Award_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case BIRTHDAY:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_BIRTHDAY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Birthday_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case MOBCAST_FILE:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_MOBCAST_FILE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Mobcast_File_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case TRAINING_FILE:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_TRAINING_FILE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Training_File_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case MOBCAST_FEEDBACK:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_MOBCAST_FEEDBACK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;
				}
				break;
			case TRAINING_QUIZ:
				 rowId = db.insertWithOnConflict(DBConstant.TABLE_TRAINING_QUIZ, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (rowId > 0) 
				{
					Uri noteUri = ContentUris.withAppendedId(DBConstant.Training_Quiz_Columns.CONTENT_URI, rowId);
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
		case MOBCAST:
			qb.setTables(DBConstant.TABLE_MOBCAST);
			qb.setProjectionMap(mobcastProjectionMap);
			break;
		case TRAINING:
			qb.setTables(DBConstant.TABLE_TRAINING);
			qb.setProjectionMap(trainingProjectionMap);
			break;
		case CHAT:
			qb.setTables(DBConstant.TABLE_CHAT);
			qb.setProjectionMap(chatProjectionMap);
			break;
		case EVENTS:
			qb.setTables(DBConstant.TABLE_EVENT);
			qb.setProjectionMap(eventProjectionMap);
			break;
		case AWARDS:
			qb.setTables(DBConstant.TABLE_AWARD);
			qb.setProjectionMap(awardProjectionMap);
			break;
		case BIRTHDAY:
			qb.setTables(DBConstant.TABLE_BIRTHDAY);
			qb.setProjectionMap(birthdayProjectionMap);
			break;
		case MOBCAST_FILE:
			qb.setTables(DBConstant.TABLE_MOBCAST_FILE);
			qb.setProjectionMap(mobcastFileProjectionMap);
			break;
		case TRAINING_FILE:
			qb.setTables(DBConstant.TABLE_TRAINING_FILE);
			qb.setProjectionMap(trainingFileProjectionMap);
			break;
		case MOBCAST_FEEDBACK:
			qb.setTables(DBConstant.TABLE_MOBCAST_FEEDBACK);
			qb.setProjectionMap(mobcastFeedbackProjectionMap);
			break;
		case TRAINING_QUIZ:
			qb.setTables(DBConstant.TABLE_TRAINING_QUIZ);
			qb.setProjectionMap(trainingQuizProjectionMap);
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
		case MOBCAST:
			count = db.update(DBConstant.TABLE_MOBCAST, values, where, whereArgs);
			break;
		case TRAINING:
			count = db.update(DBConstant.TABLE_TRAINING, values, where, whereArgs);
			break;
		case CHAT:
			count = db.update(DBConstant.TABLE_CHAT, values, where, whereArgs);
			break;
		case EVENTS:
			count = db.update(DBConstant.TABLE_EVENT, values, where, whereArgs);
			break;
		case AWARDS:
			count = db.update(DBConstant.TABLE_AWARD, values, where, whereArgs);
			break;
		case BIRTHDAY:
			count = db.update(DBConstant.TABLE_BIRTHDAY, values, where, whereArgs);
			break;
		case MOBCAST_FILE:
			count = db.update(DBConstant.TABLE_MOBCAST_FILE, values, where, whereArgs);
			break;
		case TRAINING_FILE:
			count = db.update(DBConstant.TABLE_TRAINING_FILE, values, where, whereArgs);
			break;
		case MOBCAST_FEEDBACK:
			count = db.update(DBConstant.TABLE_MOBCAST_FEEDBACK, values, where, whereArgs);
			break;
		case TRAINING_QUIZ:
			count = db.update(DBConstant.TABLE_TRAINING_QUIZ, values, where, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_MOBCAST, MOBCAST);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TRAINING, TRAINING);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_CHAT, CHAT);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_EVENT, EVENTS);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_AWARD, AWARDS);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_BIRTHDAY, BIRTHDAY);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_MOBCAST_FILE, MOBCAST_FILE);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TRAINING_FILE, TRAINING_FILE);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_MOBCAST_FEEDBACK, MOBCAST_FEEDBACK);
		sUriMatcher.addURI(AUTHORITY, DBConstant.TABLE_TRAINING_QUIZ, TRAINING_QUIZ);

		
		mobcastProjectionMap = new HashMap<String, String>();
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_ID, DBConstant.Mobcast_Columns.COLUMN_ID);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE_FORMATTED);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME_FORMATTED);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TYPE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_SHARING);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_DOWNLOADABLE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_READ_NO, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_READ_NO);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_SHARE_NO, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_SHARE_NO);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LINK);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_DATE);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_EXPIRY_TIME);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_FILE_ID, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_FILE_ID);
		mobcastProjectionMap.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT);
		
		trainingProjectionMap = new HashMap<String, String>();
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_ID, DBConstant.Training_Columns.COLUMN_ID);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_ID, DBConstant.Training_Columns.COLUMN_TRAINING_ID);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE, DBConstant.Training_Columns.COLUMN_TRAINING_TITLE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_BY, DBConstant.Training_Columns.COLUMN_TRAINING_BY);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_DESC, DBConstant.Training_Columns.COLUMN_TRAINING_DESC);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE, DBConstant.Training_Columns.COLUMN_TRAINING_DATE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED, DBConstant.Training_Columns.COLUMN_TRAINING_DATE_FORMATTED);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME, DBConstant.Training_Columns.COLUMN_TRAINING_TIME);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED, DBConstant.Training_Columns.COLUMN_TRAINING_TIME_FORMATTED);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_TYPE, DBConstant.Training_Columns.COLUMN_TRAINING_TYPE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARE, DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING, DBConstant.Training_Columns.COLUMN_TRAINING_IS_SHARING);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE, DBConstant.Training_Columns.COLUMN_TRAINING_IS_DOWNLOADABLE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO, DBConstant.Training_Columns.COLUMN_TRAINING_READ_NO);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_SHARE_NO, DBConstant.Training_Columns.COLUMN_TRAINING_SHARE_NO);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_LINK, DBConstant.Training_Columns.COLUMN_TRAINING_LINK);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE, DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_DATE);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME, DBConstant.Training_Columns.COLUMN_TRAINING_EXPIRY_TIME);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_FILE_ID, DBConstant.Training_Columns.COLUMN_TRAINING_FILE_ID);
		trainingProjectionMap.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT);
		
		chatProjectionMap = new HashMap<String, String>();
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_ID, DBConstant.Chat_Columns.COLUMN_ID);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_USER_ID, DBConstant.Chat_Columns.COLUMN_USER_ID);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_GROUP_ID, DBConstant.Chat_Columns.COLUMN_GROUP_ID);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_CITY_ID, DBConstant.Chat_Columns.COLUMN_CITY_ID);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_USER_ID_MYSQL, DBConstant.Chat_Columns.COLUMN_USER_ID_MYSQL);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_USER_JABBER_ID, DBConstant.Chat_Columns.COLUMN_USER_JABBER_ID);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_GROUP_ID_MYSQL, DBConstant.Chat_Columns.COLUMN_GROUP_ID_MYSQL);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_MESSAGE, DBConstant.Chat_Columns.COLUMN_MESSAGE);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_MESSAGE_ID, DBConstant.Chat_Columns.COLUMN_MESSAGE_ID);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_USER_SENT_MESSAGE, DBConstant.Chat_Columns.COLUMN_USER_SENT_MESSAGE);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_PATH, DBConstant.Chat_Columns.COLUMN_PATH);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_FILE_LINK, DBConstant.Chat_Columns.COLUMN_FILE_LINK);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_TYPE, DBConstant.Chat_Columns.COLUMN_TYPE);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_ISSENT, DBConstant.Chat_Columns.COLUMN_ISSENT);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_ISDELIEVERED, DBConstant.Chat_Columns.COLUMN_ISDELIEVERED);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_ISREAD, DBConstant.Chat_Columns.COLUMN_ISREAD);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_ISNOTIFIED, DBConstant.Chat_Columns.COLUMN_ISNOTIFIED);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_TIMESTAMP, DBConstant.Chat_Columns.COLUMN_TIMESTAMP);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_MESSAGE_TIME, DBConstant.Chat_Columns.COLUMN_MESSAGE_TIME);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_MESSAGE_DATE, DBConstant.Chat_Columns.COLUMN_MESSAGE_DATE);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_TAGGED, DBConstant.Chat_Columns.COLUMN_TAGGED);
		chatProjectionMap.put(DBConstant.Chat_Columns.COLUMN_ISLEFT, DBConstant.Chat_Columns.COLUMN_ISLEFT);
		
		
		eventProjectionMap = new HashMap<String, String>();
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_ID, DBConstant.Event_Columns.COLUMN_ID);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_ID, DBConstant.Event_Columns.COLUMN_EVENT_ID);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_TITLE, DBConstant.Event_Columns.COLUMN_EVENT_TITLE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_DESC, DBConstant.Event_Columns.COLUMN_EVENT_DESC);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_BY, DBConstant.Event_Columns.COLUMN_EVENT_BY);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_VENUE, DBConstant.Event_Columns.COLUMN_EVENT_VENUE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME, DBConstant.Event_Columns.COLUMN_EVENT_START_TIME);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_END_TIME, DBConstant.Event_Columns.COLUMN_EVENT_END_TIME);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE, DBConstant.Event_Columns.COLUMN_EVENT_START_DATE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE, DBConstant.Event_Columns.COLUMN_EVENT_END_DATE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_DURATION, DBConstant.Event_Columns.COLUMN_EVENT_DURATION);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_LANDMARK, DBConstant.Event_Columns.COLUMN_EVENT_LANDMARK);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK, DBConstant.Event_Columns.COLUMN_EVENT_FILE_LINK);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH, DBConstant.Event_Columns.COLUMN_EVENT_FILE_PATH);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_APPEND, DBConstant.Event_Columns.COLUMN_EVENT_FILE_APPEND);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE, DBConstant.Event_Columns.COLUMN_EVENT_FILE_SIZE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_CALENDAR, DBConstant.Event_Columns.COLUMN_EVENT_IS_CALENDAR);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN, DBConstant.Event_Columns.COLUMN_EVENT_IS_JOIN);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_READ, DBConstant.Event_Columns.COLUMN_EVENT_IS_READ);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE, DBConstant.Event_Columns.COLUMN_EVENT_IS_LIKE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING, DBConstant.Event_Columns.COLUMN_EVENT_IS_SHARING);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_MAP, DBConstant.Event_Columns.COLUMN_EVENT_MAP);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO, DBConstant.Event_Columns.COLUMN_EVENT_INVITED_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_READ_NO, DBConstant.Event_Columns.COLUMN_EVENT_READ_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO, DBConstant.Event_Columns.COLUMN_EVENT_LIKE_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO, DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_DECLINE_NO, DBConstant.Event_Columns.COLUMN_EVENT_DECLINE_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_MAYBE_NO, DBConstant.Event_Columns.COLUMN_EVENT_MAYBE_NO);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE_FORMATTED, DBConstant.Event_Columns.COLUMN_EVENT_START_DATE_FORMATTED);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_END_DATE_FORMATTED, DBConstant.Event_Columns.COLUMN_EVENT_END_DATE_FORMATTED);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE, DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME, DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED, DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE_FORMATTED);
		eventProjectionMap.put(DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED, DBConstant.Event_Columns.COLUMN_EVENT_EXPIRY_DATE_FORMATTED);
		
		
		awardProjectionMap = new HashMap<String, String>();
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_ID, DBConstant.Award_Columns.COLUMN_ID);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_ID, DBConstant.Award_Columns.COLUMN_AWARD_ID);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_NAME, DBConstant.Award_Columns.COLUMN_AWARD_NAME);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION, DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO, DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_CITY, DBConstant.Award_Columns.COLUMN_AWARD_CITY);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT, DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK, DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH, DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE, DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK, DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH, DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION, DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO, DBConstant.Award_Columns.COLUMN_AWARD_READ_NO);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO, DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED, DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_APPEND, DBConstant.Award_Columns.COLUMN_AWARD_FILE_APPEND);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARE, DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE, DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE, DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, DBConstant.Award_Columns.COLUMN_AWARD_IS_READ);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING, DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE, DBConstant.Award_Columns.COLUMN_AWARD_DATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_TIME, DBConstant.Award_Columns.COLUMN_AWARD_TIME);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED, DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE, DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE);
		awardProjectionMap.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_TIME, DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_TIME);
		
		
		birthdayProjectionMap = new HashMap<String, String>();
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_ID, DBConstant.Birthday_Columns.COLUMN_ID);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_NAME, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_NAME);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DEPARTMENT, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DEPARTMENT);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_LIKE, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_LIKE);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MESSAGE, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MESSAGE);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_SHARE, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_SHARE);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_SHARING, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_SHARING);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_WISHED, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_WISHED);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_AGE, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_AGE);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_SUN_SIGN, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_SUN_SIGN);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_CITY, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_CITY);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DOB, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DOB);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE_FORMATTED, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE_FORMATTED);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DAY, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DAY);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_APPEND, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_APPEND);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_LINK, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_LINK);
		birthdayProjectionMap.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_PATH, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_PATH);

		
		mobcastFileProjectionMap = new HashMap<String, String>();
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_ID, DBConstant.Mobcast_File_Columns.COLUMN_ID);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_ID);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LINK);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_LANG);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_SIZE);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PAGES);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_READ_DURATION);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_APPEND, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_APPEND);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM);
		mobcastFileProjectionMap.put(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE);
		
		
		trainingFileProjectionMap = new HashMap<String, String>();
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_ID, DBConstant.Training_File_Columns.COLUMN_ID);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_ID, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_ID);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LINK);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_LANG);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PATH);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_SIZE);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_PAGES);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_READ_DURATION);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_APPEND, DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_APPEND);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM, DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM);
		trainingFileProjectionMap.put(DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE, DBConstant.Training_File_Columns.COLUMN_TRAINING_LIVE_STREAM_YOUTUBE);

		mobcastFeedbackProjectionMap = new HashMap<String, String>();
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_ID, DBConstant.Mobcast_Feedback_Columns.COLUMN_ID);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QUESTION);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_TYPE, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_TYPE);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_1);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_2);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_3);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_4);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_5);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_6);
		mobcastFeedbackProjectionMap.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_OPTION_7);
		
		trainingQuizProjectionMap = new HashMap<String, String>();
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_ID, DBConstant.Training_Quiz_Columns.COLUMN_ID);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_1);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_2);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_3);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_4);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_5);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_6);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_OPTION_7);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION);
		trainingQuizProjectionMap.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS);
		}
}
