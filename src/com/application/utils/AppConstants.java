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

public class AppConstants {

	public interface API {
		// API V1
		public static final String URL_REG = "http://www.mobcast.in/salesinterns/register.php";
		public static final String URL_LOGIN = "http://www.mobcast.in/salesinterns/login.php";
		public static final String URL_ADDMEETING = "http://www.mobcast.in/salesinterns/sendMeetingReview.php";
	}
	public interface PUSH{
		public static final String PROJECT_ID ="83009443246";
		public static final String API_KEY ="AIzaSyBqYCtH2ut7FUmHI4nWITYj-IVEi58_m60";
	}

	public interface FOLDER{
		public static final String ROOT_FOLDER = "/.con/.mobcast";
		public static final String BUILD_FOLDER = ROOT_FOLDER + "/.mobcastvanilla";
		public static final String LOG_FOLDER = Environment
				.getExternalStorageDirectory().getPath() + BUILD_FOLDER + "/logs/" ;	
	}
	
	
	public static final String EXTENSION = ".jpg";
	public static final String VIDEO_EXTENSION = ".mp4";
	public static final boolean DEBUG = false;
	public static final String WHATSAPP_DOMAIN = "@s.whatsapp.net";

	public static final String GREEN = "#006400";
	public static final String BLUE = "#01AFD2";
	
	public static final int NOTIFICATION_ID = 434;

}
