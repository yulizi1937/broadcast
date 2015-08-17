package com.application.sqlite;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DBConstant {

	public static final String DB_NAME                          = "ApplicationDB";
	public static final String TABLE_MOBCAST 	    		    = "mobcast";
	public static final String TABLE_CHAT      		            = "chat";
	public static final String TABLE_TRAINING		            = "training";
	public static final String TABLE_EVENT 			            = "event";
	public static final String TABLE_AWARD 			            = "award";
	public static final String TABLE_BIRTHDAY		            = "birthday";
	public static final String TABLE_MOBCAST_FILE 	            = "mobcastFileInfo";
	public static final String TABLE_TRAINING_FILE	            = "trainingFileInfo";
	public static final String TABLE_MOBCAST_FEEDBACK           = "mobcastFeedback";
	public static final String TABLE_TRAINING_QUIZ              = "trainingQuiz";
	
	public static final String INDEX_MOBCAST_ID	                    = "_id_unique_mobcast";
	public static final String INDEX_CHAT_ID		                = "_id_unique_chat";
	public static final String INDEX_TRAINING_ID		            = "_id_unique_training";
	public static final String INDEX_EVENT_ID		                = "_id_unique_event";
	public static final String INDEX_AWARD_ID		                = "_id_unique_award";
	public static final String INDEX_BIRTHDAY_ID	                = "_id_unique_birthday";
	public static final String INDEX_MOBCAST_FILE		            = "_id_unique_mobcast_file";
	public static final String INDEX_TRAINING_FILE		            = "_id_unique_training_file";
	public static final String INDEX_MOBCAST_FEEDBACK	            = "_id_unique_mobcast_feedback";
	public static final String INDEX_TRAINING_QUIZ		            = "_id_unique_training_quiz";
	
	public static final String INDEX_ID_ORDER                       = " ASC";
	
	
	public static class Mobcast_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/mobcast");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/mobcast";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_MOBCAST_ID 					    = "_mobcast_id";
		public static final String COLUMN_MOBCAST_TITLE					    = "_mobcast_title";
		public static final String COLUMN_MOBCAST_DESC						= "_mobcast_desc";
		public static final String COLUMN_MOBCAST_BY						= "_mobcast_by";
		public static final String COLUMN_MOBCAST_VIEWCOUNT				    = "_mobcast_view_count";
		public static final String COLUMN_MOBCAST_DATE  				    = "_mobcast_date";
		public static final String COLUMN_MOBCAST_TIME 					    = "_mobcast_time";
		public static final String COLUMN_MOBCAST_TYPE			   			= "_mobcast_type";
		public static final String COLUMN_MOBCAST_DATE_FORMATTED			= "_mobcast_date_formatted";
		public static final String COLUMN_MOBCAST_TIME_FORMATTED		    = "_mobcast_time_formatted";
		public static final String COLUMN_MOBCAST_IS_READ                 	= "_mobcast_is_read";
		public static final String COLUMN_MOBCAST_IS_SHARE            		= "_mobcast_is_share";
		public static final String COLUMN_MOBCAST_IS_LIKE            		= "_mobcast_is_like";
		public static final String COLUMN_MOBCAST_IS_SHARING 				= "_mobcast_is_sharing";
		public static final String COLUMN_MOBCAST_IS_DOWNLOADABLE 		    = "_mobcast_is_downloadable";
		public static final String COLUMN_MOBCAST_LIKE_NO 				    = "_mobcast_seen_no";
		public static final String COLUMN_MOBCAST_READ_NO 				    = "_mobcast_read_no";
		public static final String COLUMN_MOBCAST_SHARE_NO 	     			= "_mobcast_share_no";
		public static final String COLUMN_MOBCAST_LINK       				= "_mobcast_link";
		public static final String COLUMN_MOBCAST_EXPIRY_DATE 				= "_mobcast_expiry_date";
		public static final String COLUMN_MOBCAST_EXPIRY_TIME 				= "_mobcast_expiry_time";
		public static final String COLUMN_MOBCAST_FILE_ID 				    = "_mobcast_file_id";
	}
	
	public static class Training_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/training");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/training";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_TRAINING_ID 					    = "_training_id";
		public static final String COLUMN_TRAINING_TITLE					= "_training_title";
		public static final String COLUMN_TRAINING_DESC						= "_training_desc";
		public static final String COLUMN_TRAINING_BY						= "_training_by";
		public static final String COLUMN_TRAINING_VIEWCOUNT				= "_training_view_count";
		public static final String COLUMN_TRAINING_DATE  				    = "_training_date";
		public static final String COLUMN_TRAINING_TIME 					= "_training_time";
		public static final String COLUMN_TRAINING_TYPE			   			= "_training_type";
		public static final String COLUMN_TRAINING_DATE_FORMATTED			= "_training_date_formatted";
		public static final String COLUMN_TRAINING_TIME_FORMATTED		    = "_training_time_formatted";
		public static final String COLUMN_TRAINING_IS_READ                 	= "_training_is_read";
		public static final String COLUMN_TRAINING_IS_SHARE            		= "_training_is_share";
		public static final String COLUMN_TRAINING_IS_LIKE            		= "_training_is_like";
		public static final String COLUMN_TRAINING_IS_SHARING 				= "_training_is_sharing";
		public static final String COLUMN_TRAINING_IS_DOWNLOADABLE   		= "_training_is_downloadable";
		public static final String COLUMN_TRAINING_LIKE_NO 				    = "_training_seen_no";
		public static final String COLUMN_TRAINING_READ_NO 				    = "_training_read_no";
		public static final String COLUMN_TRAINING_SHARE_NO 	     		= "_training_share_no";
		public static final String COLUMN_TRAINING_LINK       				= "_training_link";
		public static final String COLUMN_TRAINING_EXPIRY_DATE 				= "_training_expiry_date";
		public static final String COLUMN_TRAINING_EXPIRY_TIME 				= "_training_expiry_time";
		public static final String COLUMN_TRAINING_FILE_ID 				    = "_training_file_id";
	}
	
	public static class Chat_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/chat");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/chat";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_USER_ID 					    = "_user_id";
		public static final String COLUMN_GROUP_ID						= "_group_id";
		public static final String COLUMN_GROUP_ID_MYSQL				= "_group_id_mysql";
		public static final String COLUMN_CITY_ID						= "_city_id";
		public static final String COLUMN_USER_JABBER_ID			    = "_user_jabber_id";
		public static final String COLUMN_USER_ID_MYSQL					= "_user_id_mysql";
		public static final String COLUMN_USER_SENT_MESSAGE			    = "_is_user_sent";
		public static final String COLUMN_MESSAGE						= "_message";
		public static final String COLUMN_MESSAGE_ID					= "_message_id";
		public static final String COLUMN_PATH                 			= "_path";
		public static final String COLUMN_FILE_LINK            			= "_file_link";
		public static final String COLUMN_TYPE      					= "_type";
		public static final String COLUMN_ISSENT 				        = "_is_sent";
		public static final String COLUMN_ISREAD 				    	= "_is_read";
		public static final String COLUMN_ISDELIEVERED 					= "_is_delieverd";
		public static final String COLUMN_ISNOTIFIED 					= "_is_notified";
		public static final String COLUMN_TIMESTAMP 					= "_timestamp";
		public static final String COLUMN_MESSAGE_TIME 					= "_message_time";
		public static final String COLUMN_MESSAGE_DATE 					= "_message_date";
		public static final String COLUMN_TAGGED	 					= "_tagged";
		public static final String COLUMN_ISLEFT	 					= "_is_left";
	}
	
	
	public static class Event_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/event");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/event";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_EVENT_ID 					        = "_event_id";
		public static final String COLUMN_EVENT_TITLE 					    = "_event_title";
		public static final String COLUMN_EVENT_BY 					        = "_event_by";
		public static final String COLUMN_EVENT_DESC 					    = "_event_desc";
		public static final String COLUMN_EVENT_VENUE 					    = "_event_venue";
		public static final String COLUMN_EVENT_START_TIME				    = "_event_start_time";
		public static final String COLUMN_EVENT_END_TIME				    = "_event_end_time";
		public static final String COLUMN_EVENT_START_DATE				    = "_event_start_date";
		public static final String COLUMN_EVENT_END_DATE				    = "_event_end_date";
		public static final String COLUMN_EVENT_DURATION				    = "_event_duration";
		public static final String COLUMN_EVENT_LANDMARK				    = "_event_land_mark";
		public static final String COLUMN_EVENT_FILE_LINK				    = "_event_file_link";
		public static final String COLUMN_EVENT_FILE_PATH				    = "_event_file_path";
		public static final String COLUMN_EVENT_FILE_APPEND				    = "_event_file_append";
		public static final String COLUMN_EVENT_FILE_SIZE 				    = "_event_file_size";
		public static final String COLUMN_EVENT_IS_JOIN				        = "_event_is_join";
		public static final String COLUMN_EVENT_IS_CALENDAR				    = "_event_is_calendar";
		public static final String COLUMN_EVENT_IS_SHARING		        	= "_event_sharing";
		public static final String COLUMN_EVENT_IS_READ				        = "_event_is_read";
		public static final String COLUMN_EVENT_IS_LIKE				        = "_event_is_like";
		public static final String COLUMN_EVENT_MAP				            = "_event_map";
		public static final String COLUMN_EVENT_INVITED_NO				    = "_event_invited_no";
		public static final String COLUMN_EVENT_GOING_NO				    = "_event_going_no";
		public static final String COLUMN_EVENT_DECLINE_NO				    = "_event_decline_no";
		public static final String COLUMN_EVENT_MAYBE_NO				    = "_event_maybe_no";
		public static final String COLUMN_EVENT_LIKE_NO				        = "_event_like_no";
		public static final String COLUMN_EVENT_READ_NO				        = "_event_read_no";
		public static final String COLUMN_EVENT_START_DATE_FORMATTED	    = "_event_start_date_formatted";
		public static final String COLUMN_EVENT_END_DATE_FORMATTED	        = "_event_end_date_formatted";
		public static final String COLUMN_EVENT_RECEIVED_DATE        	    = "_event_received_date";
		public static final String COLUMN_EVENT_RECEIVED_TIME        	    = "_event_received_time";
		public static final String COLUMN_EVENT_RECEIVED_DATE_FORMATTED     = "_event_received_date_formatted";
		public static final String COLUMN_EVENT_EXPIRY_DATE				    = "_event_expiry_date";
		public static final String COLUMN_EVENT_EXPIRY_TIME				    = "_event_expiry_time";
		public static final String COLUMN_EVENT_EXPIRY_DATE_FORMATTED		= "_event_expiry_date_formatted";
	}
	
	public static class Award_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/award");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/award";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_AWARD_ID 					        = "_award_id";
		public static final String COLUMN_AWARD_NAME 				        = "_award_name";
		public static final String COLUMN_AWARD_RECOGNITION 		        = "_award_recognition";
		public static final String COLUMN_AWARD_CONGRATULATE_NO		        = "_award_congratulate_no";
		public static final String COLUMN_AWARD_CITY        		        = "_award_city";
		public static final String COLUMN_AWARD_DEPARTMENT        		    = "_award_department";
		public static final String COLUMN_AWARD_FILE_LINK			        = "_award_file_link";
		public static final String COLUMN_AWARD_FILE_PATH			        = "_award_file_path";
		public static final String COLUMN_AWARD_FILE_SIZE			        = "_award_file_size";
		public static final String COLUMN_AWARD_THUMBNAIL_PATH			    = "_award_thumbnail_path";
		public static final String COLUMN_AWARD_THUMBNAIL_LINK			    = "_award_thumbnail_link";
		public static final String COLUMN_AWARD_DESCRIPTION 			    = "_award_description";
		public static final String COLUMN_AWARD_RECEIVER_EMAIL 			    = "_award_receiver_email";
		public static final String COLUMN_AWARD_READ_NO      			    = "_award_read_no";
		public static final String COLUMN_AWARD_LIKE_NO      			    = "_award_like_no";
		public static final String COLUMN_AWARD_RECEIVED_DATE      			= "_award_received_date";
		public static final String COLUMN_AWARD_EXPIRY_DATE_FORMATTED    	= "_award_expiry_date_formatted";
		public static final String COLUMN_AWARD_FILE_APPEND			        = "_award_file_append";
		public static final String COLUMN_AWARD_IS_SHARE 					= "_award_is_share";
		public static final String COLUMN_AWARD_IS_CONGRATULATE		        = "_award_is_congratulate";
		public static final String COLUMN_AWARD_IS_READ 		     		= "_award_is_read";
		public static final String COLUMN_AWARD_IS_SHARING     				= "_award_sharing";
		public static final String COLUMN_AWARD_IS_LIKE     				= "_award_is_like";
		public static final String COLUMN_AWARD_IS_MESSAGE     				= "_award_is_message";
		public static final String COLUMN_AWARD_DATE 					    = "_award_date";
		public static final String COLUMN_AWARD_TIME  					    = "_award_time";
		public static final String COLUMN_AWARD_DATE_FORMATTED		        = "_award_date_formatted";
		public static final String COLUMN_AWARD_RECEIVED_DATE_FORMATTED     = "_award_received_date_formatted";
		public static final String COLUMN_AWARD_RECEIVER_TIME_FORMATTED		= "_award_receiver_time_formatted";
		public static final String COLUMN_AWARD_RECEIVER_MOBILE 			= "_award_receiver_mobile";
		public static final String COLUMN_AWARD_EXPIRY_DATE         	    = "_award_expiry_date";
		public static final String COLUMN_AWARD_EXPIRY_TIME     		    = "_award_expiry_time";
		
	}
	
	public static class Birthday_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/birthday");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/birthday";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_BIRTHDAY_ID 					    = "_birthday_id";
		public static final String COLUMN_BIRTHDAY_NAME 				    = "_birthday_name";
		public static final String COLUMN_BIRTHDAY_DEPARTMENT        		= "_birthday_department";
		public static final String COLUMN_BIRTHDAY_IS_SHARE 				= "_birthday_is_share";
		public static final String COLUMN_BIRTHDAY_IS_WISHED		        = "_birthday_is_congratulate";
		public static final String COLUMN_BIRTHDAY_IS_READ 		     		= "_birthday_is_read";
		public static final String COLUMN_BIRTHDAY_IS_SHARING     			= "_birthday_sharing";
		public static final String COLUMN_BIRTHDAY_IS_LIKE     				= "_birthday_is_like";
		public static final String COLUMN_BIRTHDAY_IS_MESSAGE     			= "_birthday_is_message";
		public static final String COLUMN_BIRTHDAY_AGE 		                = "_birthday_recognition";
		public static final String COLUMN_BIRTHDAY_SUN_SIGN		            = "_birthday_congratulate_no";
		public static final String COLUMN_BIRTHDAY_CITY        		        = "_birthday_city";
		public static final String COLUMN_BIRTHDAY_FILE_LINK			    = "_birthday_file_link";
		public static final String COLUMN_BIRTHDAY_FILE_PATH			    = "_birthday_file_path";
		public static final String COLUMN_BIRTHDAY_FILE_APPEND			    = "_birthday_file_append";
		public static final String COLUMN_BIRTHDAY_DOB  					= "_birthday_dob";
		public static final String COLUMN_BIRTHDAY_DATE 					= "_birthday_date";
		public static final String COLUMN_BIRTHDAY_DAY  					= "_birthday_day";
		public static final String COLUMN_BIRTHDAY_DATE_FORMATTED		    = "_birthday_date_formatted";
		public static final String COLUMN_BIRTHDAY_RECEIVED_DATE_FORMATTED  = "_birthday_received_date_formatted";
		public static final String COLUMN_BIRTHDAY_RECEIVER_TIME_FORMATTED	= "_birthday_receiver_time_formatted";
		public static final String COLUMN_BIRTHDAY_RECEIVER_MOBILE 			= "_birthday_receiver_mobile";
		public static final String COLUMN_BIRTHDAY_EXPIRY_DATE         	    = "_birthday_expiry_date";
		public static final String COLUMN_BIRTHDAY_EXPIRY_TIME     		    = "_birthday_expiry_time";
		
	}
	
	public static class Mobcast_File_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/mobcastFileInfo");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/mobcastFileInfo";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_MOBCAST_ID 						= "_mobcast_id";
		public static final String COLUMN_MOBCAST_FILE_ID 				    = "_mobcast_file_id";
		public static final String COLUMN_MOBCAST_FILE_LINK 			 	= "_mobcast_file_link";
		public static final String COLUMN_MOBCAST_FILE_LANG 				= "_mobcast_file_lang";
		public static final String COLUMN_MOBCAST_FILE_SIZE 				= "_mobcast_file_size";
		public static final String COLUMN_MOBCAST_FILE_PATH 				= "_mobcast_file_path";
		public static final String COLUMN_MOBCAST_FILE_DURATION 			= "_mobcast_file_duration";
		public static final String COLUMN_MOBCAST_FILE_PAGES 				= "_mobcast_file_pages";
		public static final String COLUMN_MOBCAST_FILE_READ_DURATION 		= "_mobcast_file_read_duration";
		public static final String COLUMN_MOBCAST_FILE_NAME         		= "_mobcast_file_name";
		public static final String COLUMN_MOBCAST_FILE_THUMBNAIL_LINK 		= "_mobcast_file_thumbnail_link";
		public static final String COLUMN_MOBCAST_FILE_THUMBNAIL_PATH 		= "_mobcast_file_thumbnail_path";
		public static final String COLUMN_MOBCAST_FILE_IS_DEFAULT 			= "_mobcast_file_is_default";
		public static final String COLUMN_MOBCAST_LIVE_STREAM 				= "_mobcast_live_stream";
		public static final String COLUMN_MOBCAST_LIVE_STREAM_YOUTUBE 		= "_mobcast_live_stream_youtube";
		public static final String COLUMN_MOBCAST_FILE_APPEND 				= "_mobcast_file_append";
		
	}
	
	public static class Training_File_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/trainingFileInfo");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/trainingFileInfo";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_TRAINING_ID 						= "_training_id";
		public static final String COLUMN_TRAINING_FILE_ID 				    = "_training_file_id";
		public static final String COLUMN_TRAINING_FILE_LINK 			 	= "_training_file_link";
		public static final String COLUMN_TRAINING_FILE_LANG 				= "_training_file_lang";
		public static final String COLUMN_TRAINING_FILE_SIZE 				= "_training_file_size";
		public static final String COLUMN_TRAINING_FILE_PATH 				= "_training_file_path";
		public static final String COLUMN_TRAINING_FILE_DURATION 			= "_training_file_duration";
		public static final String COLUMN_TRAINING_FILE_PAGES 				= "_training_file_pages";
		public static final String COLUMN_TRAINING_FILE_NAME         		= "_training_file_name";
		public static final String COLUMN_TRAINING_FILE_THUMBNAIL_LINK 		= "_training_file_thumbnail_link";
		public static final String COLUMN_TRAINING_FILE_THUMBNAIL_PATH 		= "_training_file_thumbnail_path";
		public static final String COLUMN_TRAINING_FILE_READ_DURATION 		= "_training_file_read_duration";
		public static final String COLUMN_TRAINING_FILE_IS_DEFAULT 			= "_training_file_is_default";
		public static final String COLUMN_TRAINING_LIVE_STREAM 				= "_training_live_stream";
		public static final String COLUMN_TRAINING_LIVE_STREAM_YOUTUBE 		= "_training_live_stream_youtube";
		public static final String COLUMN_TRAINING_FILE_APPEND 				= "_training_file_append";
		
	}
	
	public static class Mobcast_Feedback_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/mobcastFeedback");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/mobcastFeedback";
		
		public static final String COLUMN_ID 								= "_id";
		public static final String COLUMN_MOBCAST_FEEDBACK_ID 				= "_mobcast_feedback_id";
		public static final String COLUMN_MOBCAST_FEEDBACK_QID 			 	= "_mobcast_feedback_queid";
		public static final String COLUMN_MOBCAST_FEEDBACK_QUESTION		    = "_mobcast_feedback_question";
		public static final String COLUMN_MOBCAST_FEEDBACK_TYPE  		    = "_mobcast_feedback_type";
		public static final String COLUMN_MOBCAST_FEEDBACK_ANSWER		    = "_mobcast_feedback_answer";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_1 	    = "_mobcast_feedback_option_1";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_2 	    = "_mobcast_feedback_option_2";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_3 	    = "_mobcast_feedback_option_3";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_4 	    = "_mobcast_feedback_option_4";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_5 	    = "_mobcast_feedback_option_5";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_6 	    = "_mobcast_feedback_option_6";
		public static final String COLUMN_MOBCAST_FEEDBACK_OPTION_7 	    = "_mobcast_feedback_option_7";
		
	}
	
	
	public static class Training_Quiz_Columns implements BaseColumns
	{
		public static final Uri CONTENT_URI = Uri.parse("content://"+ ApplicationDB.AUTHORITY + "/trainingQuiz");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/trainingQuiz";
		
		public static final String COLUMN_ID 							= "_id";
		public static final String COLUMN_TRAINING_QUIZ_ID 				= "_training_quiz_id";
		public static final String COLUMN_TRAINING_QUIZ_QID 			= "_training_quiz_queid";
		public static final String COLUMN_TRAINING_QUIZ_QUESTION 		= "_training_quiz_question";
		public static final String COLUMN_TRAINING_QUIZ_TYPE    		= "_training_quiz_type";
		public static final String COLUMN_TRAINING_QUIZ_ANSWER  		= "_training_quiz_answer";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_1 	    = "_training_quiz_option_1";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_2 	    = "_training_quiz_option_2";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_3 	    = "_training_quiz_option_3";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_4 	    = "_training_quiz_option_4";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_5 	    = "_training_quiz_option_5";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_6 	    = "_training_quiz_option_6";
		public static final String COLUMN_TRAINING_QUIZ_OPTION_7 	    = "_training_quiz_option_7";
		public static final String COLUMN_TRAINING_QUIZ_CORRECT_OPTION  = "_training_correct_option";
		public static final String COLUMN_TRAINING_QUIZ_DURATION 	    = "_training_quiz_duration";
		public static final String COLUMN_TRAINING_QUIZ_QUESTION_POINTS = "_training_quiz_question_points";
		
	}
	
	
}