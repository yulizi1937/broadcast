

package com.application.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.StateSet;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.application.ui.view.SnackBar;
import com.application.widget.WidgetProvider;
import com.mobcast.R;

@SuppressLint("NewApi")
public class AndroidUtilities {
	private static final String TAG = AndroidUtilities.class.getSimpleName();
	private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<String, Typeface>();
	private static int prevOrientation = -10;
	private static boolean waitingForSms = false;
	private static final Object smsLock = new Object();

	public static int statusBarHeight = 0;
	public static float density = 1;
	public static Point displaySize = new Point();
	public static Integer photoSize = null;
	private static Boolean isTablet = null;

	static {
		density = ApplicationLoader.applicationContext.getResources()
				.getDisplayMetrics().density;
		checkDisplaySize();
	}

	@SuppressWarnings("ResourceType")
	public static void lockOrientation(Activity activity) {
		if (activity == null || prevOrientation != -10) {
			return;
		}
		try {
			prevOrientation = activity.getRequestedOrientation();
			WindowManager manager = (WindowManager) activity
					.getSystemService(Activity.WINDOW_SERVICE);
			if (manager != null && manager.getDefaultDisplay() != null) {
				int rotation = manager.getDefaultDisplay().getRotation();
				int orientation = activity.getResources().getConfiguration().orientation;
				int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
				int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;
				if (Build.VERSION.SDK_INT < 9) {
					SCREEN_ORIENTATION_REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
					SCREEN_ORIENTATION_REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				}

				if (rotation == Surface.ROTATION_270) {
					if (orientation == Configuration.ORIENTATION_PORTRAIT) {
						activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					} else {
						activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					}
				} else if (rotation == Surface.ROTATION_90) {
					if (orientation == Configuration.ORIENTATION_PORTRAIT) {
						activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					} else {
						activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					}
				} else if (rotation == Surface.ROTATION_0) {
					if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
						activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					} else {
						activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					}
				} else {
					if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
						activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					} else {
						activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					}
				}
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
		}
	}

	public static int getSlidingTabTextSize() {
		// TODO Auto-generated method stub
		int width = ApplicationLoader.getApplication().getResources().getDisplayMetrics().widthPixels;
		if (width < 320) {
			return 8;//10//9
		} else if (width > 320 && width <= 480) {
			return 9;//11//10
		} else if (width > 480 && width < 540) {
			return 10;//12//11
		} else {
			return 11;//13//12
		}
	}

	public static int getSlidingTabPadding() {
		int width = ApplicationLoader.getApplication().getResources()
				.getDisplayMetrics().widthPixels;
		if (width < 320) {
			return 20;//18//19
		} else if (width > 320 && width <= 480) {
			return 19;//17//18
		} else if (width > 480 && width < 540) {
			return 17;//15//16
		} else {
			return 16;//16
		}
	}
	
	@SuppressLint("DefaultLocale") public static double getScreenSizeInInches(){
		try{
			DisplayMetrics metrics = ApplicationLoader.getApplication().getResources().getDisplayMetrics();
			int widthPixels = metrics.widthPixels;
			int heightPixels = metrics.heightPixels;
			
			float widthDpi = metrics.xdpi;
			float heightDpi = metrics.ydpi;
			
			float widthInches = widthPixels / widthDpi;
			float heightInches = heightPixels / heightDpi;
			
			double diagonalInches = Math.sqrt(
				    (widthInches * widthInches) 
				    + (heightInches * heightInches));
			return Double.parseDouble(String.format("%.2f", diagonalInches));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return 5.0;
	}

	@SuppressWarnings("ResourceType")
	public static void unlockOrientation(Activity activity) {
		if (activity == null) {
			return;
		}
		try {
			if (prevOrientation != -10) {
				activity.setRequestedOrientation(prevOrientation);
				prevOrientation = -10;
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
		}
	}

	public static Typeface getTypeface(String assetPath) {
		synchronized (typefaceCache) {
			if (!typefaceCache.containsKey(assetPath)) {
				try {
					Typeface t = Typeface.createFromAsset(
							ApplicationLoader.applicationContext.getAssets(),
							assetPath);
					typefaceCache.put(assetPath, t);
				} catch (Exception e) {
					FileLog.e("Typefaces", "Could not get typeface '"
							+ assetPath + "' because " + e.getMessage());
					return null;
				}
			}
			return typefaceCache.get(assetPath);
		}
	}

	public static void showKeyboard(View view) {
		if (view == null) {
			return;
		}
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

		((InputMethodManager) view.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
	}

	public static boolean isKeyboardShowed(View view) {
		if (view == null) {
			return false;
		}
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		return inputManager.isActive(view);
	}

	public static void hideKeyboard(View view) {
		if (view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (!imm.isActive()) {
			return;
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static File getCacheDir() {
		if (Environment.getExternalStorageState() == null
				|| Environment.getExternalStorageState().startsWith(
						Environment.MEDIA_MOUNTED)) {
			try {
				File file = ApplicationLoader.applicationContext
						.getExternalCacheDir();
				if (file != null) {
					return file;
				}
			} catch (Exception e) {
				FileLog.e("tmessages", e.toString());
			}
		}
		try {
			File file = ApplicationLoader.applicationContext.getCacheDir();
			if (file != null) {
				return file;
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
		}
		return new File("");
	}

	public static int dp(float value) {
		return (int) Math.ceil(density * value);
	}

	public static float dpf2(float value) {
		return density * value;
	}

	public static float px(float value) {
		return density * value;
	}

	public static int dpFromPx(final float px) {
		return (int) Math.ceil(px
				/ ApplicationLoader.getApplication().getResources()
						.getDisplayMetrics().density);
	}

	public static int pxFromDp(final int dp) {
		return (int) Math.ceil(dp
				* ApplicationLoader.getApplication().getResources()
						.getDisplayMetrics().density);
	}

	@SuppressWarnings("deprecation")
	public static String checkDisplaySize() {
		try {
			WindowManager manager = (WindowManager) ApplicationLoader.applicationContext
					.getSystemService(Context.WINDOW_SERVICE);
			if (manager != null) {
				Display display = manager.getDefaultDisplay();
				if (display != null) {
					if (android.os.Build.VERSION.SDK_INT < 13) {
						displaySize
								.set(display.getWidth(), display.getHeight());
					} else {
						display.getSize(displaySize);
					}
					FileLog.e("tmessages", "display size = " + displaySize.x
							+ " " + displaySize.y);
					return "display size = " + displaySize.x
							+ " " + displaySize.y;
				}
			}
			return "display size = manager==null ";
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
			return "display size = exception "+e;
		}
	}

	public static void runOnUIThread(Runnable runnable) {
		runOnUIThread(runnable, 0);
	}

	public static void runOnUIThread(Runnable runnable, long delay) {
		if (delay == 0) {
			ApplicationLoader.applicationHandler.post(runnable);
		} else {
			ApplicationLoader.applicationHandler.postDelayed(runnable, delay);
		}
	}

	public static void cancelRunOnUIThread(Runnable runnable) {
		ApplicationLoader.applicationHandler.removeCallbacks(runnable);
	}

	public static boolean isTablet() {
		if (isTablet == null) {
			isTablet = true;
		}
		return isTablet;
	}

	public static boolean isSmallTablet() {
		float minSide = Math.min(displaySize.x, displaySize.y) / density;
		return minSide <= 700;
	}

	public static int getMinTabletSide() {
		if (!isSmallTablet()) {
			int smallSide = Math.min(displaySize.x, displaySize.y);
			int leftSide = smallSide * 35 / 100;
			if (leftSide < dp(320)) {
				leftSide = dp(320);
			}
			return smallSide - leftSide;
		} else {
			int smallSide = Math.min(displaySize.x, displaySize.y);
			int maxSide = Math.max(displaySize.x, displaySize.y);
			int leftSide = maxSide * 35 / 100;
			if (leftSide < dp(320)) {
				leftSide = dp(320);
			}
			return Math.min(smallSide, maxSide - leftSide);
		}
	}
	
	public static void clearCursorDrawable(EditText editText) {
		if (editText == null || Build.VERSION.SDK_INT < 12) {
			return;
		}
		try {
			Field mCursorDrawableRes = TextView.class
					.getDeclaredField("mCursorDrawableRes");
			mCursorDrawableRes.setAccessible(true);
			mCursorDrawableRes.setInt(editText, 0);
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
		}
	}

	public static int getViewInset(View view) {
		if (view == null || Build.VERSION.SDK_INT < 21) {
			return 0;
		}
		try {
			Field mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
			mAttachInfoField.setAccessible(true);
			Object mAttachInfo = mAttachInfoField.get(view);
			if (mAttachInfo != null) {
				Field mStableInsetsField = mAttachInfo.getClass()
						.getDeclaredField("mStableInsets");
				mStableInsetsField.setAccessible(true);
				Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
				return insets.bottom;
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
		}
		return 0;
	}

	public static int getCurrentActionBarHeight() {
		if (isTablet()) {
			return dp(64);
		} else if (ApplicationLoader.applicationContext.getResources()
				.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return dp(48);
		} else {
			return dp(56);
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static Point getRealScreenSize() {
		Point size = new Point();
		try {
			WindowManager windowManager = (WindowManager) ApplicationLoader.applicationContext
					.getSystemService(Context.WINDOW_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				windowManager.getDefaultDisplay().getRealSize(size);
			} else {
				try {
					Method mGetRawW = Display.class.getMethod("getRawWidth");
					Method mGetRawH = Display.class.getMethod("getRawHeight");
					size.set((Integer) mGetRawW.invoke(windowManager
							.getDefaultDisplay()), (Integer) mGetRawH
							.invoke(windowManager.getDefaultDisplay()));
				} catch (Exception e) {
					size.set(windowManager.getDefaultDisplay().getWidth(),
							windowManager.getDefaultDisplay().getHeight());
					FileLog.e("tmessages", e.toString());
				}
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e.toString());
		}
		return size;
	}

	public static void setListViewEdgeEffectColor(AbsListView listView,
			int color) {
		if (Build.VERSION.SDK_INT >= 21) {
			try {
				Field field = AbsListView.class
						.getDeclaredField("mEdgeGlowTop");
				field.setAccessible(true);
				EdgeEffect mEdgeGlowTop = (EdgeEffect) field.get(listView);
				if (mEdgeGlowTop != null) {
					mEdgeGlowTop.setColor(color);
				}

				field = AbsListView.class.getDeclaredField("mEdgeGlowBottom");
				field.setAccessible(true);
				EdgeEffect mEdgeGlowBottom = (EdgeEffect) field.get(listView);
				if (mEdgeGlowBottom != null) {
					mEdgeGlowBottom.setColor(color);
				}
			} catch (Exception e) {
				FileLog.e("tmessages", e.toString());
			}
		}
	}

	public static void clearDrawableAnimation(View view) {
		if (Build.VERSION.SDK_INT < 21 || view == null) {
			return;
		}
		Drawable drawable = null;
		if (view instanceof ListView) {
			drawable = ((ListView) view).getSelector();
			if (drawable != null) {
				drawable.setState(StateSet.NOTHING);
			}
		} else {
			drawable = view.getBackground();
			if (drawable != null) {
				drawable.setState(StateSet.NOTHING);
				drawable.jumpToCurrentState();
			}
		}
	}

	public static boolean isGingerBread() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1) {
			return true;
		}
		return false;
	}

	public static boolean isHoneyComb() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB) {
			return true;
		}
		return false;
	}

	public static boolean isIceCreamSandWich() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return true;
		}
		return false;
	}

	public static boolean isJellyBean() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
			return true;
		}
		return false;
	}

	public static boolean isKitKat() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			return true;
		}
		return false;
	}

	public static boolean isLollyPop() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
			return true;
		}
		return false;
	}

	public static boolean isAboveGingerBread() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
			return true;
		}
		return false;
	}

	public static boolean isAboveHoneyComb() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return true;
		}
		return false;
	}

	public static boolean isAboveIceCreamSandWich() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return true;
		}
		return false;
	}

	public static boolean isAboveJellyBean() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return true;
		}
		return false;
	}

	public static boolean isAboveKitKat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return true;
		}
		return false;
	}

	public static boolean isAboveLollyPop() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return true;
		}
		return false;
	}
	
	public static boolean isAboveMarshMallow() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return true;
		}
		return false;
	}
	
	public static boolean isPermissionStorageAllowed(){
		try{
			return ActivityCompat.checkSelfPermission(ApplicationLoader.getApplication().getApplicationContext(), AppConstants.PERMISSION.STORAGE) == PackageManager.PERMISSION_GRANTED;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return false;
	}
	
	public static boolean isBelowLollyPop() {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
			return true;
		}
		return false;
	}
	
	public static boolean isAppLanguageIsEnglish() {
		if (ApplicationLoader.getPreferences().getAppLanguageCode().equalsIgnoreCase("en")) {
			return true;
		}
		return false;
	}
	
	public static boolean isAppStyleCustomFont() {
		return ApplicationLoader.getPreferences().isAppCustomTextFont();
	}
	
	public static long getFolderSize(String mFolderPath){
		if(new File(mFolderPath).exists()){
			if(new File(mFolderPath).isDirectory()){
				return FileUtils.sizeOfDirectory(new File(mFolderPath));
			}
		}
		return 0;
	}
	
	public static float getPercentageOfFileSizeFromFreeMemory(long mFileSize) {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
		long free = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();
		if (total == 0) {
			return 0;
		}

		if (free != 0) {
			return ((float)mFileSize / free) * 10000;
		}

		return 0;
	}
	
	public static void enterWindowAnimation(Activity mActivity){
		mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	public static void exitWindowAnimation(Activity mActivity){
		mActivity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_right);
	}
	
	public static void showSnackBar(Activity mActivity, String mMessage){
		try{
			new SnackBar.Builder(mActivity)
		    .withMessage(mMessage)
		    .withStyle(SnackBar.Style.INFO)
		    .withTextColorId(R.color.sb__snack_text_color)
		    .withDuration(SnackBar.SHORT_SNACK)
		    .show();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static void updateAppWidget(){
		try{
			Context mContext = ApplicationLoader.getApplication();
			RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
			ComponentName thisWidget = new ComponentName(mContext,WidgetProvider.class);
			AppWidgetManager.getInstance(mContext).updateAppWidget(thisWidget,remoteViews);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static String getAppNetworkTraffic() {
		String mUsage = "Traffic Usage: \n";
		try {
			final PackageManager pm = ApplicationLoader.getApplication().getPackageManager();
			List<ApplicationInfo> packages = pm.getInstalledApplications(0);
			for (ApplicationInfo packageInfo : packages) {
				// get the UID for the selected app
				if (packageInfo.packageName.equalsIgnoreCase(ApplicationLoader.getApplication().getResources().getString(R.string.package_name))) {
					int UID = packageInfo.uid;
					double received = (double) TrafficStats.getUidRxBytes(UID);
					double send = (double) TrafficStats.getUidTxBytes(UID);
					
					if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getAppReceivePackets())){
						received = Double.parseDouble(ApplicationLoader.getPreferences().getAppReceivePackets()) + received;
					}
					
					if(!TextUtils.isEmpty(ApplicationLoader.getPreferences().getAppSendPackets())){
						send = Double.parseDouble(ApplicationLoader.getPreferences().getAppSendPackets()) + send;
					}
					
					if(received+send >0){
						mUsage += "Received: "+Utilities.formatFileSize(received) + "\n";
						mUsage += "Send: "+Utilities.formatFileSize(send)+ "\n";
						mUsage += "Total: "+Utilities.formatFileSize(received+send)+ "\n";
					}
				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
			mUsage = e.toString();
		}
		return mUsage;
	}
	
	/**
	 * Animation
	 */
	
	public static void expand(final View v) {
		try {
			v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			final int targtetHeight = v.getMeasuredHeight();
			v.getLayoutParams().height = 0;
			v.setVisibility(View.VISIBLE);
			Animation a = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime,Transformation t) {
					v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT: (int) (targtetHeight * interpolatedTime);
					v.requestLayout();
				}
				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};
			a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
			v.startAnimation(a);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	public static void collapse(final View v) {
		try {
			final int initialHeight = v.getMeasuredHeight();
			Animation a = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime,Transformation t) {
					if (interpolatedTime == 1) {
						v.setVisibility(View.GONE);
					} else {
						v.getLayoutParams().height = initialHeight- (int) (initialHeight * interpolatedTime);
						v.requestLayout();
					}
				}
				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};
			a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
			v.startAnimation(a);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	
}
