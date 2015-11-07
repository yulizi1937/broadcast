/**
 * 
 */
package com.application.utils;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class BuildVars {
	/**
	 * BUILD PURPOSE : All must be false
	 */
	public static final boolean DEBUG = false;
	public static final boolean DEBUG_VERSION = false;
	public static final boolean DEBUG_SCREENSHOT = false;

	
	/**
	 * DEBUG : DEVELOPER : OKHTTP, STETHO(CHROME PLUGIN) : All must be false for Production Build
	 */
	public static final boolean DEBUG_OKHTTP = false;
	public static final boolean DEBUG_STETHO = false;
	public static final boolean DEBUG_API = false;
	
	
	/**
	 * DEBUG : OTP, CRASH, DESIGN, SMS
	 */
	public static final boolean DEBUG_SMS = false;
	public static final boolean DEBUG_DESIGN = false;
	public static final boolean DEBUG_OTP = false;
	public static final boolean DEBUG_CRASH_EMAIL = false;
	
	/**
	 * DESIGN : GRID 
	 */
	public static final boolean IS_GRID = true;
	
	/**
	 * Sync 
	 */
	public static final boolean IS_AUTOSYNC = true;
	
	/**
	 * Network Preferences
	 */
	
	public static final boolean USE_OKHTTP = true;
	
	/**
	 * CLIENT PURPOSE
	 */
	public static final boolean IS_MOBILENUMBER_PRIMARY = true;
	
	
	/**
	 * CRASH REPORTING : DUMPING STACKTRACE IN A MAIL
	 */
	public static String SEND_LOGS_EMAIL = "androidbugtrace@gmail.com";
	public static String EMAIL_USERNAME = "androidbugtrace@gmail.com";
	public static String EMAIL_PASSWORD = "";
	public static String EMAIL_TO = "vikalppatel043@gmail.com";
	public static String EMAIL_SUBJECT = "Mobcast v2 |  Database";
	
	public static String EMAIL_BODY = "Debugging Generated Database File";
	
	public static String ERROR_EMAIL_TO_1 = "androidbugtrace@gmail.com";
	public static String ERROR_EMAIL_TO_2 = "vikalp@mobcast.in";
	public static String ERROR_EMAIL_SUBJECT = "Mobcast v2 | Crash Report |";
}
