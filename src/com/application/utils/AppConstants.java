/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- ADD IPD ACTIVITY
 * DESCRIPTION 		:- SAVE IPD
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 1000B      VIKALP PATEL    07/02/2014        RELEASE         ADD VIDEO EXTENSION
 * 1000E      VIKALP PATEL    15/02/2014        RELEASE         ADDED PASS HASH IN JSON
 * --------------------------------------------------------------------------------------------------------------------
 */
package com.application.utils;

import android.os.Environment;

import com.squareup.okhttp.MediaType;

public class AppConstants {

	public interface API {
		//Content Type
		public static final MediaType API_CONTENTTYPE = MediaType.parse("application/json; charset=utf-8");
		// API V2
		public static final String API_HOST        = "http://54.165.41.77";
//		public static final String API_HOST        = "http://192.168.0.120";
		public static final String API_CLIENT      = "";
		public static final String API_VERSION     = "/v2";
		public static final String API_FOLDER      ="/api/";
		
		public static final String API_URL                  = API_HOST + API_CLIENT +  API_VERSION + API_FOLDER ; //http://192.168.0.114/demo/v2/api/sample.php
		
		public static final String API_LOGIN                = API_URL + "checkUser";
		public static final String API_VERIFY               = API_URL + "verifyUser";
		public static final String API_UPDATE_USER          = API_URL + "updateUser";
		public static final String API_FETCH_PUSH_DATA      = API_URL + "fetchPushData";
	}
	
	public interface API_KEY_PARAMETER{
		public static final String userName                = "mobileNumber";
		public static final String otp                     = "otp";
		public static final String deviceType              = "deviceType";
		public static final String deviceMfg               = "deviceMfg";
		public static final String deviceName              = "deviceName";
		public static final String regId                   = "regId";
		public static final String deviceSize              = "deviceSize";
		public static final String deviceOs                = "deviceOs";
		public static final String deviceId                = "deviceId";
		public static final String appVersion              = "appVersion";
		public static final String deviceIsTablet          = "deviceIsTablet";
		public static final String success                 = "success";
		public static final String apiKey                  = "apiKey";
		public static final String errorMessage            = "errorMessage";
		public static final String accessToken             = "accessToken";
		public static final String name                    = "name";
		public static final String user                    = "user";
		public static final String emailAddress            = "emailAddress";
		public static final String profileImage            = "profileImage";
		public static final String employeeId              = "employeeId";
		public static final String countryCode             = "countryCode";
		public static final String category                = "category";
		public static final String _id                     = "id";
		public static final String lastMobcastId           = "lastMobcastId";
		public static final String lastTrainingId          = "lastTrainingId";
		public static final String lastEventId             = "lastEventId";
		public static final String lastAwardId             = "lastAwardId";
		public static final String lastBirthdayId          = "lastBirthdayId";
		public static final String mobcast                 = "mobcast";
		public static final String training                = "training";
		public static final String chat                    = "chat";
		public static final String award                   = "award";
		public static final String event                   = "event";
		public static final String birthday                = "birthday";
		public static final String mobcastId               = "mobcastId";
		public static final String mobcastTitle            = "mobcastTitle";
		public static final String mobcastBy               = "mobcastBy";
		public static final String mobcastDescription      = "mobcastDescription";
		public static final String mobcastViewCount        = "mobcastViewCount";
		public static final String mobcastLikeCount        = "mobcastLikeCount";
		public static final String mobcastDate             = "mobcastDate";
		public static final String mobcastTime        	   = "mobcastTime";
		public static final String mobcastType        	   = "mobcastType";
		public static final String mobcastIsRead           = "mobcastIsRead";
		public static final String mobcastIsLiked          = "mobcastIsLiked";
		public static final String mobcastIsSharing        = "mobcastIsSharing";
		public static final String mobcastIsDownloadable   = "mobcastIsDownloadable";
		public static final String mobcastLink             = "mobcastLink";
		public static final String mobcastExpiryDate       = "mobcastExpiryDate";
		public static final String mobcastExpiryTime     	   = "mobcastExpiryTime";
		public static final String mobcastFileInfo        	   = "mobcastFileInfo";
		public static final String mobcastFileLink        	   = "mobcastFileLink";
		public static final String mobcastFileLang        	   = "mobcastFileLang";
		public static final String mobcastFileSize        	   = "mobcastFileSize";
		public static final String mobcastFileDuration         = "mobcastFileDuration";
		public static final String mobcastFilePages        	   = "mobcastFilePages";
		public static final String mobcastisDefault        	   = "mobcastIsDefault";
		public static final String mobcastLiveStream           = "mobcastLiveStream";
		public static final String mobcastLiveStreamYouTube    = "mobcastLiveStreamYouTube";
		public static final String mobcastReadDuration         = "mobcastReadDuration";
		public static final String mobcastFileName        	   = "mobcastFileName";
		public static final String mobcastThumbnail        	   = "mobcastThumbnail";
		
	}
	
	public interface API_HEADER{
		public static final String USER                 ="";
		public static final String PASSWORD             ="";
	}
	
	public interface PUSH{
		public static final String PROJECT_ID ="40394833851";
		public static final String API_KEY ="AIzaSyCK0tovRWYY_Iy42H-MfUntueLq-nZRNlg";
	}
	
	public interface YOUTUBE{
		public static final String API_KEY = "AIzaSyCK0tovRWYY_Iy42H-MfUntueLq-nZRNlg";
	}

	public interface FOLDER{
		public static final String ROOT_FOLDER = "/.con/.mobcast";
		public static final String BUILD_FOLDER = ROOT_FOLDER + "/.mobcastvanilla";
		public static final String LOG_FOLDER = Environment.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/logs/" ;
		public static final String IMAGE_FOLDER = Environment.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/image/" ;
		public static final String AUDIO_FOLDER = Environment.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/audio/" ;
		public static final String VIDEO_FOLDER = Environment.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/video/" ;
		public static final String DOCUMENT_FOLDER = Environment.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/document/" ;
		public static final String THUMBNAIL_FOLDER = Environment.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/thumbnail/" ;
	}
	
	public interface MOBCAST{
		public static final String HEADER   = "header";//0
		public static final String TEXT     = "text";//1
		public static final String NEWS     = "news";//2
		public static final String IMAGE    = "image";//3
		public static final String VIDEO    = "video";//4
		public static final String AUDIO    = "audio";//5
		public static final String DOC      = "doc";//6
		public static final String PDF      = "pdf";//7
		public static final String PPT      = "ppt";//8
		public static final String XLS      = "xls";//9
		public static final String FEEDBACK = "feedback";//10
	}
	
	public interface TYPE{
		public static final int IMAGE     = 0;
		public static final int VIDEO     = 1;
		public static final int AUDIO     = 2;
		public static final int DOC       = 3;
		public static final int PDF       = 4;
		public static final int PPT       = 5;
		public static final int XLS       = 6;
		public static final int OTHER     = 7;
		public static final int TEXT      = 8;
		public static final int STREAM    = 9;
		public static final int FEEDBACK  = 10;
		public static final int QUIZ      = 11;
	}
	
	public interface TRAINING{
		public static final String HEADER          = "header";//0
		public static final String TEXT            = "text";//1
		public static final String INTERACTIVE     = "interactive";//2
		public static final String IMAGE           = "image";//3
		public static final String VIDEO           = "video";//4
		public static final String AUDIO           = "audio";//5
		public static final String DOC             = "doc";//6
		public static final String PDF             = "pdf";//7
		public static final String PPT             = "ppt";//8
		public static final String XLS             = "xls";//9
		public static final String QUIZ            = "quiz";//10
	}
	
	public interface INTENT{
		public static final int INTENT_LANGUAGE     = 1001;
		public static final int INTENT_CATEGORY     = 1002;
	}
	
	public interface INTENTCONSTANTS{
		public static final String LANGUAGE         = "language";
		public static final String LANGUAGECODE     = "languageCode";
		public static final String ACTIVITYTITLE    = "activityTitle";
		public static final String CATEGORYARRAY    = "categoryArray";
		public static final String CATEGORY         = "category";
		public static final String HELP             = "help";
		public static final String USERNAME         = "userName";
		public static final String OTP              = "otp";
		public static final String MOBCAST          = "mobcast";
		public static final String CHAT             = "chat";
		public static final String TRAINING         = "training";
		public static final String AWARD            = "award";
		public static final String EVENT            = "event";
		public static final String OBJECT           = "object";
		public static final String ID               = "id";
	}
	
	public interface NOTIFICATION{
		public static final String TITLE      = "title";
		public static final String MESSAGE    = "message";
		public static final String SIZE       = "size";
		public static final String DURATION   = "duration";
		public static final String PAGES      = "pages";
		public static final String FILEPATH   = "filePath";
		public static final String THUMBPATH  = "ThumbnailPath";
	}
	
	public interface COLOR{
		public static final String MOBCAST_RED     = "#FD3F1F";
		public static final String MOBCAST_YELLOW  = "#FFCD00";
		public static final String MOBCAST_PURPLE  = "#8F46AD";
		public static final String MOBCAST_GREEN   = "#02B86B";
		public static final String MOBCAST_BLUE    = "#008CED";
	}
	
	public static final String deviceType = "android";

	public static final String GREEN = "#006400";
	public static final String BLUE = "#01AFD2";
	
	public static final int NOTIFICATION_ID = 434;

}
