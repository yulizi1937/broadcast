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

}
