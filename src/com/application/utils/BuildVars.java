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
	public static final boolean DEBUG = true;
	public static final boolean DEBUG_VERSION = true;
	public static final boolean DEBUG_SCREENSHOT = true;

	
	/**
	 * DEBUG : DEVELOPER : OKHTTP, STETHO(CHROME PLUGIN) : All must be false for Production Build
	 */
	public static final boolean DEBUG_OKHTTP = true;
	public static final boolean DEBUG_STETHO = true;
	public static final boolean DEBUG_API = true;
	
	
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
