/**
 * 
 */
package com.application.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
/**
 * A collection of Google Analytics trackers. Fetch the tracker you need using
 * {@code AnalyticsTrackers.getInstance().get(...)}
 * <p/>
 * This code was generated by Android Studio but can be safely modified by hand
 * at this point.
 * <p/>
 * TODO: Call {@link #initialize(Context)} from an entry point in your app
 * before using this!
 */
public final class AnalyticsTrackersV3 {

	public enum Target {
		APP,
		// Add more trackers here if you need, and update the code in
		// #get(Target) below
	}

	private static AnalyticsTrackersV3 mInstance;

	public static synchronized void initialize(Context context) {
		if (mInstance != null) {
			throw new IllegalStateException(
					"Extra call to initialize analytics trackers");
		}

		mInstance = new AnalyticsTrackersV3(context);
	}

	public static synchronized AnalyticsTrackersV3 getInstance() {
		if (mInstance == null) {
			throw new IllegalStateException(
					"Call initialize() before getInstance()");
		}

		return mInstance;
	}

	private final Map<Target, Tracker> mTrackers = new HashMap<Target, Tracker>();
	private final Context mContext;

	/**
	 * Don't instantiate directly - use {@link #getInstance()} instead.
	 */
	private AnalyticsTrackersV3(Context context) {
		mContext = ApplicationLoader.getApplication().getApplicationContext();
	}

	public synchronized Tracker get(Target target) {
		if (!mTrackers.containsKey(target)) {
			Tracker tracker;
			switch (target) {
			case APP:
				tracker = EasyTracker.getInstance(mContext);
				break;
			default:
				throw new IllegalArgumentException(
						"Unhandled analytics target " + target);
			}
			mTrackers.put(target, tracker);
		}

		return mTrackers.get(target);
	}
}