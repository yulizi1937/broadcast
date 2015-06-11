package com.application.sqlite;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBConstant {

	public static final String DB_NAME                          = "ApplicationDB";
	public static final String TABLE_ANNOUNCEMENT 			    = "announcement";
	public static final String TABLE_EVENT 			            = "event";
	public static final String TABLE_AWARD 			            = "award";
	public static final String TABLE_NEWS 			            = "news";
	public static final String TABLE_TRAINING		            = "training";
	public static final String TABLE_FEEDBACK		            = "feedback";
	
	public static final String INDEX_ANNOUNCEMENT_ID	            = "_id_unique_announcement";
	public static final String INDEX_EVENT_ID		                = "_id_unique_event";
	public static final String INDEX_AWARD_ID		                = "_id_unique_award";
	public static final String INDEX_NEWS_ID		                = "_id_unique_news";
	public static final String INDEX_TRAINING_ID		            = "_id_unique_training";
	public static final String INDEX_FEEDBACK_ID		            = "_id_unique_feedback";
	
	public static final String INDEX_ID_ORDER                      =" ASC";
	
	
	public static class Announcement_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/announcement");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/announcement";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_ANNOUNCE_ID 					    = "_announce_id";
		public static final String COLUMN_ANNOUNCE_TITLE					= "_announce_title";
		public static final String COLUMN_ANNOUNCE_DESC						= "_announce_desc";
		public static final String COLUMN_ANNOUNCE_FROM						= "_announce_from";
		public static final String COLUMN_ANNOUNCE_BY						= "_announce_by";
		public static final String COLUMN_ANNOUNCE_FILELINK				    = "_announce_file_link";
		public static final String COLUMN_ANNOUNCE_FILEPATH				    = "_announce_file_path";
		public static final String COLUMN_ANNOUNCE_EXPIRY					= "_announce_expiry";
		public static final String COLUMN_ANNOUNCE_TYPE			   			= "_announce_type";
		public static final String COLUMN_ANNOUNCE_NAME						= "_announce_name";
		public static final String COLUMN_ANNOUNCE_SUMMARY					= "_announce_summary";
		public static final String COLUMN_ANNOUNCE_IS_READ                 	= "_announce_is_read";
		public static final String COLUMN_ANNOUNCE_IS_SHARE            		= "_announce_is_share";
		public static final String COLUMN_ANNOUNCE_IS_LIKE            		= "_announce_is_like";
		public static final String COLUMN_ANNOUNCE_DATE      				= "_announce_date";
		public static final String COLUMN_ANNOUNCE_DATE_FORMATTED			= "_announce_date_formatted";
		public static final String COLUMN_ANNOUNCE_TIME 				    = "_announce_time";
		public static final String COLUMN_ANNOUNCE_SEEN_NO 				    = "_announce_seen_no";
		public static final String COLUMN_ANNOUNCE_READ_NO 				    = "_announce_read_no";
		public static final String COLUMN_ANNOUNCE_SHARE_NO 				= "_announce_share_no";
		public static final String COLUMN_ANNOUNCE_FILE_APPEND 				= "_announce_file_append";
		public static final String COLUMN_ANNOUNCE_IS_SHARING 				= "_announce_sharing";	
		
	}
	
	
	public static class Event_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/event");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/event";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_EVENT_ID 					        = "_event_id";
		public static final String COLUMN_EVENT_NAME 					    = "_event_name";
		public static final String COLUMN_EVENT_TITLE 					    = "_event_title";
		public static final String COLUMN_EVENT_DESC 					    = "_event_desc";
		public static final String COLUMN_EVENT_VENUE 					    = "_event_venue";
		public static final String COLUMN_EVENT_START_TIME				    = "_event_start_time";
		public static final String COLUMN_EVENT_END_TIME				    = "_event_end_time";
		public static final String COLUMN_EVENT_START_DATE				    = "_event_start_date";
		public static final String COLUMN_EVENT_END_DATE				    = "_event_end_date";
		public static final String COLUMN_EVENT_DURATION				    = "_event_duration";
		public static final String COLUMN_EVENT_LANDMARK				    = "_event_land_mark";
		public static final String COLUMN_EVENT_EXPIRY				        = "_event_expiry";
		public static final String COLUMN_EVENT_FILE_LINK				    = "_event_file_link";
		public static final String COLUMN_EVENT_FILE_PATH				    = "_event_file_path";
		public static final String COLUMN_EVENT_FILE_APPEND				    = "_event_file_append";
		public static final String COLUMN_EVENT_SUMMARY				        = "_event_summary";
		public static final String COLUMN_EVENT_JOIN				        = "_event_join";
		public static final String COLUMN_EVENT_IS_CALENDAR				    = "_event_is_calendar";
		public static final String COLUMN_EVENT_MAP				            = "_event_map";
		public static final String COLUMN_EVENT_INVITED_NO				    = "_event_invited_no";
		public static final String COLUMN_EVENT_GOING_NO				    = "_event_going_no";
		public static final String COLUMN_EVENT_DECLINE_NO				    = "_event_decline_no";
		public static final String COLUMN_EVENT_MAYBE_NO				    = "_event_maybe_no";
		public static final String COLUMN_EVENT_IS_READ				        = "_event_is_read";
		public static final String COLUMN_EVENT_START_DATE_FORMATTED	    = "_event_start_date_formatted";
		public static final String COLUMN_EVENT_END_DATE_FORMATTED	        = "_event_end_date_formatted";
		public static final String COLUMN_EVENT_RECEIVED_DATE        	    = "_event_received_date";
		public static final String COLUMN_EVENT_IS_SHARING		        	= "_event_sharing";
	}
	
	public static class Award_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/award");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/award";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_AWARD_ID 					        = "_award_id";
		public static final String COLUMN_AWARD_RECEIVED_DATE		        = "_award_received_date";
		public static final String COLUMN_AWARD_DATE 					    = "_award_date";
		public static final String COLUMN_AWARD_TITLE 					    = "_award_title";
		public static final String COLUMN_AWARD_DESC 					    = "_award_desc";
		public static final String COLUMN_AWARD_FILE_LINK			        = "_award_file_link";
		public static final String COLUMN_AWARD_FILE_PATH			        = "_award_file_path";
		public static final String COLUMN_AWARD_FILE_APPEND			        = "_award_file_append";
		public static final String COLUMN_AWARD_IS_SHARE 					= "_award_is_share";
		public static final String COLUMN_AWARD_IS_CONGRATULATE		        = "_award_is_congratulate";
		public static final String COLUMN_AWARD_BY 					        = "_award_by";
		public static final String COLUMN_AWARD_READ_NO				        = "_award_read_no";
		public static final String COLUMN_AWARD_CONGRATULATE_NO		        = "_award_congratulate_no";
		public static final String COLUMN_AWARD_DATE_FORMATTED		        = "_award_date_formatted";
		public static final String COLUMN_AWARD_RECEIVED_DATE_FORMATTED     = "_award_received_date_formatted";
		public static final String COLUMN_AWARD_NAME 				        = "_award_name";
		public static final String COLUMN_AWARD_RECEIVER_EMAIL 				= "_award_receiver_email";
		public static final String COLUMN_AWARD_RECEIVER_MOBILE 			= "_award_receiver_mobile";
		public static final String COLUMN_AWARD_IS_LIKE     				= "_award_is_like";
		public static final String COLUMN_AWARD_LIKE_NO 			    	= "_award_like_no";
		public static final String COLUMN_AWARD_RECEIVER_NAME 				= "_award_receiver_name";
		public static final String COLUMN_AWARD_IS_READ 		     		= "_award_is_read";
		public static final String COLUMN_AWARD_EXPIRY         				= "_award_expiry";
		public static final String COLUMN_AWARD_IS_SHARING     				= "_award_sharing";
	}
	
	public static class News_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/news");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/news";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_NEWS_ID 					        = "_news_id";
		public static final String COLUMN_NEWS_DATE 				        = "_news_date";
		public static final String COLUMN_NEWS_TITLE 				        = "_news_title";
		public static final String COLUMN_NEWS_NAME 				        = "_news_name";
		public static final String COLUMN_NEWS_DESC 				        = "_news_desc";
		public static final String COLUMN_NEWS_SUMMARY 				        = "_news_summary";
		public static final String COLUMN_NEWS_SOURCE 				        = "_news_source";
		public static final String COLUMN_NEWS_TYPE 				        = "_news_type";
		public static final String COLUMN_NEWS_FILE_LINK			        = "_news_file_link";
		public static final String COLUMN_NEWS_FILE_PATH			        = "_news_file_path";
		public static final String COLUMN_NEWS_FILE_APPEND			        = "_news_file_append";
		public static final String COLUMN_NEWS_IS_READ 				        = "_news_is_read";
		public static final String COLUMN_NEWS_IS_LIKE 				        = "_news_is_like";
		public static final String COLUMN_NEWS_IS_SHARE 			        = "_news_is_share";
		public static final String COLUMN_NEWS_LIKE_NO 				        = "_news_like_no";
		public static final String COLUMN_NEWS_READ_NO 				        = "_news_read_no";
		public static final String COLUMN_NEWS_BY 					        = "_news_by";
		public static final String COLUMN_NEWS_FROM 				        = "_news_from";
		public static final String COLUMN_NEWS_IS_SHARING 			        = "_news_sharing";
		public static final String COLUMN_NEWS_EXPIRY 				        = "_news_expiry";
		public static final String COLUMN_NEWS_DATE_FORMATTED		        = "_news_date_formatted";
		
	}
	
	public static class Training_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/training");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/training";
		
		public static final String COLUMN_ID 								    = "_id";
		public static final String COLUMN_TRAINING_ID 					        = "_training_id";
		public static final String COLUMN_TRAINING_DATE 				        = "_training_date";
		public static final String COLUMN_TRAINING_TITLE 				        = "_training_title";
		public static final String COLUMN_TRAINING_NAME 				        = "_training_name";
		public static final String COLUMN_TRAINING_DESC 				        = "_training_desc";
		public static final String COLUMN_TRAINING_SUMMARY 				        = "_training_summary";
		public static final String COLUMN_TRAINING_SOURCE 				        = "_training_source";
		public static final String COLUMN_TRAINING_TYPE 				        = "_training_type";
		public static final String COLUMN_TRAINING_FILE_LINK			        = "_training_file_link";
		public static final String COLUMN_TRAINING_FILE_PATH			        = "_training_file_path";
		public static final String COLUMN_TRAINING_FILE_APPEND			        = "_training_file_append";
		public static final String COLUMN_TRAINING_IS_READ 				        = "_training_is_read";
		public static final String COLUMN_TRAINING_IS_LIKE 				        = "_training_is_like";
		public static final String COLUMN_TRAINING_IS_SHARE 			        = "_training_is_share";
		public static final String COLUMN_TRAINING_LIKE_NO 				        = "_training_like_no";
		public static final String COLUMN_TRAINING_READ_NO 				        = "_training_read_no";
		public static final String COLUMN_TRAINING_BY 					        = "_training_by";
		public static final String COLUMN_TRAINING_FROM 				        = "_training_from";
		public static final String COLUMN_TRAINING_IS_SHARING 			        = "_training_sharing";
		public static final String COLUMN_TRAINING_EXPIRY 				        = "_training_expiry";
		public static final String COLUMN_TRAINING_DATE_FORMATTED		        = "_training_date_formatted";
		
	}
	
	public static class Feedback_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/feedback");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/feedback";
		
		public static final String COLUMN_ID 								    = "_id";
		public static final String COLUMN_FEEDBACK_ID 					        = "_feedback_id";
		public static final String COLUMN_FEEDBACK_DATE 					    = "_feedback_date";
		public static final String COLUMN_FEEDBACK_TITLE 					    = "_feedback_title";
		public static final String COLUMN_FEEDBACK_NAME 					    = "_feedback_name";
		public static final String COLUMN_FEEDBACK_DESC 					    = "_feedback_desc";
		public static final String COLUMN_FEEDBACK_SUMMARY 					    = "_feedback_summary";
		public static final String COLUMN_FEEDBACK_IS_READ 					    = "_feedback_is_read";
		public static final String COLUMN_FEEDBACK_IS_LIKE 					    = "_feedback_is_like";
		public static final String COLUMN_FEEDBACK_IS_SHARE 					= "_feedback_is_share";
		public static final String COLUMN_FEEDBACK_READ_NO 					    = "_feedback_read_no";
		public static final String COLUMN_FEEDBACK_LIKE_NO 					    = "_feedback_like_no";
		public static final String COLUMN_FEEDBACK_ATTEMPT_NO 					= "_feedback_attempted_no";
		public static final String COLUMN_FEEDBACK_BY 					        = "_feedback_by";
		public static final String COLUMN_FEEDBACK_FROM 					    = "_feedback_from";
		public static final String COLUMN_FEEDBACK_EXPIRY 					    = "_feedback_expiry";
		public static final String COLUMN_FEEDBACK_DATE_FORMATTED 				= "_feedback_date_formatted";
		public static final String COLUMN_FEEDBACK_QUESTION 					= "_feedback_question";
		public static final String COLUMN_FEEDBACK_QUESTION_TYPE 				= "_feedback_question_type";
		public static final String COLUMN_FEEDBACK_OPTION1 					    = "_feedback_option_1";
		public static final String COLUMN_FEEDBACK_OPTION2 					    = "_feedback_option_2";
		public static final String COLUMN_FEEDBACK_OPTION3 					    = "_feedback_option_3";
		public static final String COLUMN_FEEDBACK_OPTION4 					    = "_feedback_option_4";
		public static final String COLUMN_FEEDBACK_CORRECT_OPTION 				= "_feedback_correct_option";
		public static final String COLUMN_FEEDBACK_TIME_LIMIT 					= "_feedback_time_limit";
		public static final String COLUMN_FEEDBACK_SCORE 					    = "_feedback_score";
		public static final String COLUMN_FEEDBACK_IS_ATTEMPTED 				= "_feedback_is_attempted";
		public static final String COLUMN_FEEDBACK_RANKING 					    = "_feedback_ranking";
	}
	
}