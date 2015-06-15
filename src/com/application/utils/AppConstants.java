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
		public static final String API_HOST        = "http://192.168.0.119";
		public static final String API_CLIENT      = "";
		public static final String API_VERSION     = "/v2";
		public static final String API_FOLDER      ="/api/";
		
		public static final String API_URL         = API_HOST + API_CLIENT +  API_VERSION + API_FOLDER ; //http://192.168.0.114/demo/v2/api/sample.php
		
		public static final String API_LOGIN       = API_URL + "checkUser";
		public static final String API_VERIFY      = API_URL + "verifyUser";
		public static final String API_UPDATE_USER = API_URL + "updateUser";
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
	}
	
	public interface API_HEADER{
		public static final String USER                 ="";
		public static final String PASSWORD             ="";
	}
	
	public interface PUSH{
		public static final String PROJECT_ID ="40394833851";
		public static final String API_KEY ="AIzaSyBqYCtH2ut7FUmHI4nWITYj-IVEi58_m60";
	}
	
	public interface YOUTUBE{
		public static final String API_KEY = "AIzaSyCK0tovRWYY_Iy42H-MfUntueLq-nZRNlg";
	}

	public interface FOLDER{
		public static final String ROOT_FOLDER = "/.con/.mobcast";
		public static final String BUILD_FOLDER = ROOT_FOLDER + "/.mobcastvanilla";
		public static final String LOG_FOLDER = Environment
				.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/logs/" ;	
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
		public static final String AWARDS           = "awards";
		public static final String EVENTS           = "events";
	}
	
	public static final String deviceType = "android";
	
	public static final String EXTENSION = ".jpg";
	public static final String VIDEO_EXTENSION = ".mp4";
	public static final boolean DEBUG = false;
	public static final String WHATSAPP_DOMAIN = "@s.whatsapp.net";

	public static final String GREEN = "#006400";
	public static final String BLUE = "#01AFD2";
	
	public static final int NOTIFICATION_ID = 434;

}
