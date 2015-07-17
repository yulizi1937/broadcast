/**
 * 
 */
package com.application.utils;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class BuildVars {
	/*
	 * DEBUGGING PURPOSE
	 */
	public static final boolean DEBUG = true;
	public static final boolean DEBUG_VERSION = true;
	public static final boolean DEBUG_SCREENSHOT = true;
	public static final boolean DEBUG_SMS = false;
	public static final boolean DEBUG_OKHTTP = true;
	public static final boolean DEBUG_STETHO = true;
	public static final boolean DEBUG_API = true;
	public static final boolean DEBUG_DESIGN = false;
	public static final boolean DEBUG_OTP = true;
	
	/*
	 * Network Preferences
	 */
	
	public static final boolean USE_OKHTTP = true;
	
	/*
	 * CLIENT PURPOSE
	 */
	public static final boolean IS_MOBILENUMBER_PRIMARY = true;
	
	
	/*
	 * 
	 */
	
	/*
	 * CRASH REPORTING
	 */
	public static String SEND_LOGS_EMAIL = "androidbugtrace@gmail.com";
	public static String EMAIL_USERNAME = "androidbugtrace@gmail.com";
	public static String EMAIL_PASSWORD = "android2";
	public static String EMAIL_TO = "vikalppatel043@gmail.com";
	public static String EMAIL_SUBJECT = "Mobcast v2 |  Database";
	
	public static String EMAIL_BODY = "Debugging Generated Database File";
	
	public static String ERROR_EMAIL_TO_1 = "androidbugtrace@gmail.com";
	public static String ERROR_EMAIL_TO_2 = "vikalp@mobcast.in";
	public static String ERROR_EMAIL_SUBJECT = "Mobcast v2 | Crash Report |";
}
