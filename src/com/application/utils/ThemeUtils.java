/**
 * 
 */
package com.application.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.ui.view.DontPressWithParentImageView;
import com.application.ui.view.FloatingActionButton;
import com.application.ui.view.SlidingTabLayout;
import com.application.ui.view.SmoothProgressBar;
import com.application.utils.AppConstants.TYPE;
import com.mobcast.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.svgparser.SVG;
import com.svgparser.SVGBuilder;


/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class ThemeUtils {
	private static final String TAG = ThemeUtils.class.getSimpleName();
	
	private Context mContext;
	private int mTheme = 0;
	
	private int mStatusBarColor;
	private int mToolBarColor;
	
	private int mLoginHeaderColor;
	private int mAdminColor;
	
	private int mByColor;
	
	private Drawable mBackgroundPressedDrawable;
	private Drawable mBackgroundFocusedEditText;
	private Drawable mBackgroundBy;
	

    private static volatile ThemeUtils Instance = null;
    public static ThemeUtils getInstance(Context activityContext) {
        ThemeUtils localInstance = Instance;
        if (localInstance == null) {
            synchronized (activityContext){
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new ThemeUtils();
                }
            }
        }
        return localInstance;
    }
    
    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
    public void applyTheme(Context mContext, Activity mActivity){
    	try{
    		this.mContext  = mContext;
    		mTheme = ApplicationLoader.getPreferences().getAppTheme();
    		SystemBarTintManager mTintManager = new SystemBarTintManager(mActivity);
    		mTintManager.setStatusBarTintEnabled(true);
    		switch(mTheme){
    		case 0:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_dblue);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_dblue);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_dblue);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_orange_fill);
    			break;
    		case 1:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_purple);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_purple);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_purple);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_purple);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_purple);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_purple);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_pink_fill);
    			break;
    		case 2:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_green);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_green);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_green);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_green);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_green);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_green);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_blue_fill);
    			break;
    		case 3:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_pink);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_pink);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_pink);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_pink);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_pink);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_pink);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_purple_fill);
    			break;
    		case 4:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_teal);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_teal);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_teal);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_teal);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_teal);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_teal);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_teal_fill);
    			break;
    		case 5:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_brown);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_brown);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_brown);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_brown);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_brown);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_brown);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_brown_fill);
    			break;
    		case 6:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_gray);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_gray);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_gray);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_purple);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_gray);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_gray);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_pink_fill);
    			break;
    		case 7:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_amber);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_amber);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_amber);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_purple);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_amber);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_amber);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_pink_fill);
    			break;
    		case 8:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_orange);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_orange);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_orange);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_green);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_orange);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_orange);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_blue_fill);
    			break;
    		case 9:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_lblue);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_lblue);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_lblue);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_lblue);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_lblue);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_orange_fill);
    			break;
    		case 10:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_red);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_red);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_red);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_green);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_red);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_red);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_blue_fill);
    			break;
    		case 11:
    			mToolBarColor = mContext.getResources().getColor(R.color.toolbar_background_dpurple);
    			mStatusBarColor = mContext.getResources().getColor(R.color.toolbar_background_statusbar_dpurple);
    			mLoginHeaderColor = mContext.getResources().getColor(R.color.textview_login_color_dpurple);
    			mAdminColor = mContext.getResources().getColor(R.color.editbox_color);
    			mByColor = mContext.getResources().getColor(R.color.item_by_color_purple);
    			mBackgroundPressedDrawable = mContext.getResources().getDrawable(R.drawable.shape_button_pressed_dpurple);
    			mBackgroundFocusedEditText = mContext.getResources().getDrawable(R.drawable.shape_editbox_selected_dpurple);
    			mBackgroundBy = mContext.getResources().getDrawable(R.drawable.shape_by_pink_fill);
    			break;
    		}
    		
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = mActivity.getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(mStatusBarColor);
			} else {
				mTintManager.setStatusBarTintColor(mStatusBarColor);
			}
    	}catch(Exception e){
    		FileLog.e(TAG, e.toString());
    	}
    }
    
    public void applyThemeSplash(Context mContext, Activity mActivity,
			FrameLayout mFrameLayout) {
		try {
			applyTheme(mContext, mActivity);
			mFrameLayout.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
    
	public void applyThemeLogin(Context mContext, Activity mActivity,
			AppCompatTextView mHeaderTextView,
			AppCompatTextView mAdminTextView,
			AppCompatTextView mCountryCodeTextView) {
		try {
			applyTheme(mContext, mActivity);
			mAdminTextView.setTextColor(mAdminColor);
			mHeaderTextView.setTextColor(mLoginHeaderColor);
			mCountryCodeTextView.setBackgroundColor(mByColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void applyThemeVerification(Context mContext, Activity mActivity, Toolbar mToolbar,
			AppCompatTextView mVerificationHeaderTextView) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
			mVerificationHeaderTextView.setTextColor(mLoginHeaderColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
    
	public void applyThemeCountrySelect(Context mContext, Activity mActivity,
			Toolbar mToolbar) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void applyThemeWithBy(Context mContext, Activity mActivity,
			Toolbar mToolbar, AppCompatTextView mAppCompatTextView) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
			mAppCompatTextView.setBackgroundDrawable(mBackgroundBy);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void applyThemeSetProfile(Context mContext, Activity mActivity, Toolbar mToolbar, FrameLayout mFrameLayout) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
			mFrameLayout.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void applyThemeDrawer(Context mContext, Activity mActivity,
			FrameLayout mFrameLayout, AppCompatTextView mTextView) {
		try {
			applyTheme(mContext, mActivity);
			mFrameLayout.setBackgroundColor(mToolBarColor);
			mTextView.setTextColor(mByColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void applyThemeMother(Context mContext, Activity mActivity, Toolbar mToolbar, SlidingTabLayout mSlidingTabLayout) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
			mSlidingTabLayout.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void applyThemeCapture(Context mContext, Activity mActivity, Toolbar mToolbar) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void applyThemeFeedback(Context mContext, Activity mActivity, Toolbar mToolbar, AppCompatTextView mAppCompatTextView, View mView) {
		try {
			applyTheme(mContext, mActivity);
			mAppCompatTextView.setBackgroundDrawable(mBackgroundBy);
			mView.setBackgroundColor(mByColor);
			mToolbar.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void applyThemeQuizScore(Context mContext, Activity mActivity, AppCompatTextView mAppCompatTextView, View mView) {
		try {
			applyTheme(mContext, mActivity);
			mAppCompatTextView.setBackgroundDrawable(mBackgroundBy);
			mView.setBackgroundColor(mByColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void applyThemeAwardProfile(Context mContext, Activity mActivity,
			Toolbar mToolbar, LinearLayout mLayout,
			AppCompatTextView mNameTextView, AppCompatTextView mDescTextView,
			AppCompatTextView mByTextView) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
			mByTextView.setBackgroundDrawable(mBackgroundBy);
			mNameTextView.setTextColor(mByColor);
			mDescTextView.setTextColor(mByColor);
			mLayout.setBackgroundColor(mToolBarColor);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	 
	public void applyThemeButton(Context mContext, Activity mActivity,
			AppCompatButton mAppCompatButton) {
		try {
			applyTheme(mContext, mActivity);
			mAppCompatButton.setBackgroundDrawable(mBackgroundPressedDrawable);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public void applyThemeButton(Context mContext, Activity mActivity, Toolbar mToolbar,
			AppCompatButton mAppCompatButton) {
		try {
			applyTheme(mContext, mActivity);
			mToolbar.setBackgroundColor(mToolBarColor);
			mAppCompatButton.setBackgroundDrawable(mBackgroundPressedDrawable);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void applyThemeEditText(Context mContext, Activity mActivity,
			LinearLayout mLinearLayout) {
		try {
			applyTheme(mContext, mActivity);
			mLinearLayout.setBackgroundDrawable(mBackgroundFocusedEditText);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void applyThemeEditText(Context mContext, Activity mActivity,
			AppCompatEditText mAppCompatEditText) {
		try {
			applyTheme(mContext, mActivity);
			mAppCompatEditText.setBackgroundDrawable(mBackgroundFocusedEditText);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static void applyThemeSlidingTab(Context mContext, TextView mTextView){
		try{
			switch(ApplicationLoader.getPreferences().getAppTheme()){
			case 0:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color));
				break;
			case 1:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_purple));
				break;
			case 2:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_green));
				break;
			case 6:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_purple));
				break;
			case 7:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_purple));
				break;
			case 8:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_green));
				break;
			case 10:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_green));
				break;
			case 11:
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color_purple));
				break;
			default :
				mTextView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_tab_text_color));
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static void applyThemeItemBirthdaySection(Context mContext,AppCompatTextView mAppCompatTextView, int whichTheme){
		try{
			switch(whichTheme){
			case 0:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_dblue));
				break;
			case 1:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_purple));
				break;
			case 2:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_green));
				break;
			case 3:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_pink));
				break;
			case 4:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_teal));
				break;
			case 5:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_brown));
				break;
			case 6:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_gray));
				break;
			case 7:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_amber));
				break;
			case 8:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_orange));
				break;
			case 9:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_lblue));
				break;
			case 10:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_red));
				break;
			case 11:
				mAppCompatTextView.setBackgroundColor(mContext.getResources().getColor(R.color.toolbar_background_dpurple));
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * Mobcast Theme
	 */

	@SuppressWarnings("deprecation")
	public static void applyThemeItemMobcast(Context mContext,
			int whichTheme, View mReadStripView, View mTimeLimeView,
			FrameLayout mLayout, AppCompatTextView mTitleTextView,
			AppCompatTextView mByTextView, ImageView mImageView, int mType,boolean isRead) {
		try{
			Resources mResources = mContext.getResources();
			switch(whichTheme){
			case 0:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_purple));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_green));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_pink));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_purple_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_teal));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_teal_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_brown));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_brown_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 6:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_gray));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 7:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_amber));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 8:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_orange));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_orange));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_orange));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 9:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_lblue));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 10:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_red));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_red));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 11:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_dpurple));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			}
			
			if(isRead){
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
				mImageView.setBackgroundColor(mResources.getColor(android.R.color.white));
			}else{
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
				mImageView.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}
			
			applyThemeToTimeLineIconsWithSVG(mImageView, mResources,isRead, mType, whichTheme);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressLint("NewApi") 
	public static void applyThemeToTimeLineIconsWithSVG(ImageView mImageView, Resources mResources, boolean isRead,int mType, int whichTheme){
		try{
			if(mImageView!=null){
				mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
			Drawable mDrawable = null;
			SVG mSVG;
			if(isRead){
				switch(whichTheme){
				case 0:
					break;
				case 1:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 2:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 3:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 4:
					break;
				case 5:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 6:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 7:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 8:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 10:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 11:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				}	
			}else{
				switch(whichTheme){
				case 0:
					break;
				case 1:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 2:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 3:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 4:
					break;
				case 5:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 6:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 7:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 8:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 10:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 11:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				}
			}
			if(mDrawable!=null){
				mImageView.setImageDrawable(mDrawable);	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressLint("NewApi") 
	public static Drawable applyThemeToTimeLineIconsWithSVG(Resources mResources, boolean isRead,int mType, int whichTheme){
		Drawable mDrawable = null;
		try{
			SVG mSVG;
			if(isRead){
				switch(whichTheme){
				case 0:
					break;
				case 1:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 2:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 3:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 4:
					break;
				case 5:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 6:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 7:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 8:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 10:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 11:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_read_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				}	
			}else{
				switch(whichTheme){
				case 0:
					break;
				case 1:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 2:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 3:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_pink).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 4:
					break;
				case 5:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_brown).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 6:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 7:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
					
				case 8:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 10:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_green).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				case 11:
					switch(mType){
					case TYPE.TEXT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.text_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.AUDIO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.audio_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.IMAGE:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.image_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.VIDEO:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.video_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PDF:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.pdf_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.XLS:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.xls_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.PPT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.ppt_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.DOC:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.doc_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.STREAM:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.live_stream_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.EVENT:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.event_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.FEEDBACK:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.feedback_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					case TYPE.QUIZ:
						mSVG = new SVGBuilder().readFromResource(mResources, R.raw.quiz_unread_purple).build();
						mDrawable = mSVG.getDrawable();
						break;
					}
					break;
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return mDrawable;
	}
	
	/**
	 * Training Theme
	 */
	
	@SuppressWarnings("deprecation")
	public static void applyThemeItemTraining(Context mContext,
			int whichTheme, View mReadStripView, View mTimeLimeView,
			FrameLayout mLayout, AppCompatTextView mTitleTextView,
			AppCompatTextView mByTextView, ImageView mImageView, int mType,boolean isRead) {
		try{
			Resources mResources = mContext.getResources();
			switch(whichTheme){
			case 0:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_purple));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_green));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_pink));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_purple_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_teal));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_teal_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_brown));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_brown_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 6:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_gray));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 7:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_amber));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 8:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_orange));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_orange));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_orange));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 9:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_lblue));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 10:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_red));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_red));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 11:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_dpurple));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			}
			
			if(isRead){
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
				mImageView.setBackgroundColor(mResources.getColor(android.R.color.white));
			}else{
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
				mImageView.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}
			
			applyThemeToTimeLineIconsWithSVG(mImageView, mResources,isRead, mType, whichTheme);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	/**
	 * Event Theme
	 */
	
	@SuppressWarnings("deprecation")
	public static void applyThemeItemEvent(Context mContext,
			int whichTheme, View mReadStripView, View mTimeLimeView,
			FrameLayout mLayout, AppCompatTextView mTitleTextView,
			AppCompatTextView mByTextView, ImageView mImageView,boolean isRead) {
		try{
			Resources mResources = mContext.getResources();
			switch(whichTheme){
			case 0:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_purple));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_green));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_pink));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_purple_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_teal));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_teal_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_brown));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_brown_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 6:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_gray));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 7:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_amber));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 8:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_orange));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_orange));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_orange));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 9:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_lblue));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 10:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_red));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				if(!isRead){
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_red));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			case 11:
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_dpurple));
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				if(!isRead){
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mReadStripView.setVisibility(View.VISIBLE);
				}
				break;
			}
			
			if(isRead){
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				mImageView.setBackgroundColor(mResources.getColor(android.R.color.white));
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
			}else{
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
				mImageView.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}
			applyThemeToTimeLineIconsWithSVG(mImageView, mResources,isRead, AppConstants.TYPE.EVENT, whichTheme);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void applyThemeItemAward(Context mContext, int whichTheme,
			View mReadStripView, FrameLayout mLayout,
			AppCompatTextView mTitleTextView,
			DontPressWithParentImageView mCongratulateIv,
			DontPressWithParentImageView mMessageIv, boolean isRead, boolean isCongratulate) {
		try{
			Resources mResources = mContext.getResources();
			if(!isRead){
				switch(whichTheme){
				case 0:
						mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 1:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 2:
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 3:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
					mReadStripView.setVisibility(View.VISIBLE);
				break;
				case 4:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 5:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 6:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mReadStripView.setVisibility(View.VISIBLE);
					break;	
				case 7:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 8:
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_orange));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_orange));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 9:
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_lblue));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 10:
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_red));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 11:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mReadStripView.setVisibility(View.VISIBLE);
					break;	
				}
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}else{
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
			}
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void applyThemeItemBirthday(Context mContext, int whichTheme,
			View mReadStripView, FrameLayout mLayout,
			AppCompatTextView mTitleTextView,
			DontPressWithParentImageView mCongratulateIv,
			DontPressWithParentImageView mMessageIv, boolean isRead, boolean isWish) {
		try{
			Resources mResources = mContext.getResources();
			if(!isRead){
				switch(whichTheme){
				case 0:
						mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 1:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 2:
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 3:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
					mReadStripView.setVisibility(View.VISIBLE);
				break;
				case 4:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 5:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 6:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 7:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 8:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_orange));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_orange));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 9:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_lblue));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 10:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_red));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 11:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				}
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}else{
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void applyThemeItemRecruitment(Context mContext, int whichTheme,
			View mReadStripView, View mTimeLineView, FrameLayout mIndicatorLayout,FrameLayout mLayout,
			AppCompatTextView mTitleTextView, AppCompatTextView mDateTextView, AppCompatTextView mMonthTextView,boolean isRead) {
		try{
			Resources mResources = mContext.getResources();
			if(!isRead){
				mDateTextView.setTextColor(Color.WHITE);
				mMonthTextView.setTextColor(Color.WHITE);
				switch(whichTheme){
				case 0:
						mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 1:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 2:
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
						mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 3:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_pink));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_pink_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 4:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_teal));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_teal_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 5:
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_brown));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_brown_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 6:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 7:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 8:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_orange));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_orange));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 9:
					mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 10:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_red));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 11:
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				}
			}else{
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				switch(whichTheme){
				case 0:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_border));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					break;
				case 1:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				case 2:
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_border));
					break;
				case 3:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_pink));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_pink));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_pink));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_pink_border));
					break;
				case 4:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_teal));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_teal));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_teal));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_teal_border));
					break;
				case 5:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_brown));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_brown));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_brown));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_brown_border));
					break;
				case 6:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				case 7:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				case 8:
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_border));
					break;
				case 9:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_border));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					break;
				case 10:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_border));
					break;
				case 11:
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				}
			}
			
			if(!isRead){
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}else{
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public static void applyThemeItemParichay(Context mContext, int whichTheme,
			View mReadStripView, View mTimeLineView, FrameLayout mIndicatorLayout,FrameLayout mLayout,
			AppCompatTextView mTitleTextView, AppCompatTextView mDateTextView, AppCompatTextView mMonthTextView, AppCompatTextView mForTextView,boolean isRead) {
		try{
			Resources mResources = mContext.getResources();
			if(!isRead){
				mDateTextView.setTextColor(Color.WHITE);
				mMonthTextView.setTextColor(Color.WHITE);
				switch(whichTheme){
				case 0:
						mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
						mTitleTextView.setTextColor(mResources.getColor(R.color.unread_highlight));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 1:
						mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_purple));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_purple));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 2:
						mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_green));
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 3:
						mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_purple_fill));
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_pink));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_pink));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_pink));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_pink_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 4:
						mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_teal_fill));	
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_teal));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_teal));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_teal));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_teal_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 5:
						mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_brown_fill));
						mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_brown));
						mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_brown));
						mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_brown));
						mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_brown_fill));
						mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 6:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_gray));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_gray));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 7:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 8:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_amber));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_amber));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 9:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_orange));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_lblue));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 10:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_red));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_red));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
					mReadStripView.setVisibility(View.VISIBLE);
					break;
				case 11:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_dpurple));
					mReadStripView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dpurple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
					mReadStripView.setVisibility(View.VISIBLE);
				break;
				}
			}else{
				mTitleTextView.setTextColor(Color.BLACK);
				mReadStripView.setVisibility(View.GONE);
				switch(whichTheme){
				case 0:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_border));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					break;
				case 1:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				case 2:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_border));
					break;
				case 3:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_purple_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_pink));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_pink));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_pink));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_pink_border));
					break;
				case 4:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_teal_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_teal));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_teal));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_teal));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_teal_border));
					break;
				case 5:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_brown_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_brown));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_brown));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_brown));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_brown_border));
					break;
				case 6:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				case 7:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				case 8:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTitleTextView.setTextColor(mResources.getColor(R.color.toolbar_background_statusbar_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_border));
					break;
				case 9:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_border));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_unread_strip_color_dlue));
					break;
				case 10:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_green));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_green));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_border));
					break;
				case 11:
					mForTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
					mDateTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mMonthTextView.setTextColor(mResources.getColor(R.color.item_by_color_purple));
					mTimeLineView.setBackgroundColor(mResources.getColor(R.color.item_by_color_purple));
					mIndicatorLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_border));
					break;
				}
			}
			
			if(!isRead){
				mLayout.setBackgroundColor(mResources.getColor(R.color.unread_background_offwhite));
			}else{
				mLayout.setBackgroundColor(mResources.getColor(android.R.color.white));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	/**
	 * Any.Do - Theme
	 */
	
	@SuppressWarnings("deprecation")
	public static void applyThemeAnyDOBy(AppCompatTextView mByTextView, ImageView mImageView, View mTimeLimeView,int mType,int whichTheme, Context mContext){
		try{
			
			Resources mResources = mContext.getResources();
			switch(whichTheme){
			case 0:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color));
				break;
			case 1:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_purple));
				break;
			case 2:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_green));
				break;
			case 3:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_purple_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_pink));
				break;
			case 4:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_teal_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_teal));
				break;
			case 5:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_brown_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_brown));
				break;
			case 6:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_gray));
				break;
			case 7:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_amber));
				break;
			case 8:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_orange));
				break;
			case 9:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_orange_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color));
				break;
			case 10:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_blue_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_orange));
				break;
			case 11:
				mByTextView.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_by_pink_fill));
				mTimeLimeView.setBackgroundColor(mResources.getColor(R.color.item_recycler_mobcast_timeline_color_gray));
				break;
			}
			
			applyThemeToTimeLineIconsWithSVG(mImageView, mResources, false, mType, whichTheme);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void applyThemeAnyDOCloseView(TextView mCloseTextView, TextView mViewTextView,int whichTheme, Context mContext){
		try{
			
			Resources mResources = mContext.getResources();
			switch(whichTheme){
			case 0:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background));
				break;
			case 1:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_purple));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_purple));
				break;
			case 2:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_green));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_green));
				break;
			case 3:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_pink));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_pink));
				break;
			case 4:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_teal));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_teal));
				break;
			case 5:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_brown));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_brown));
				break;
			case 6:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_gray));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_gray));
				break;
			case 7:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_amber));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_amber));
				break;
			case 8:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_orange));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_orange));
				break;
			case 9:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_lblue));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_lblue));
				break;
			case 10:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_red));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_red));
				break;
			case 11:
				mCloseTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_dpurple));
				mViewTextView.setBackgroundColor(mResources.getColor(R.color.toolbar_background_dpurple));
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static void applyThemeFAB(int whichTheme, FloatingActionButton mFAB){
		try{
			switch(whichTheme){
			case 0:
				mFAB.setColorNormalResId(R.color.item_by_color);
				break;
			case 1:
				mFAB.setColorNormalResId(R.color.item_by_color_purple);
				break;
			case 2:
				mFAB.setColorNormalResId(R.color.item_by_color_green);
				break;
			case 3:
				mFAB.setColorNormalResId(R.color.item_by_color_pink);
				break;
			case 4:
				mFAB.setColorNormalResId(R.color.item_by_color_teal);
				break;
			case 5:
				mFAB.setColorNormalResId(R.color.item_by_color_brown);
				break;
			case 6:
				mFAB.setColorNormalResId(R.color.item_by_color_purple);
				break;
			case 7:
				mFAB.setColorNormalResId(R.color.item_by_color_purple);
				break;
			case 8:
				mFAB.setColorNormalResId(R.color.item_by_color_green);
				break;
			case 9:
				mFAB.setColorNormalResId(R.color.item_by_color);
				break;
			case 10:
				mFAB.setColorNormalResId(R.color.item_by_color_green);
				break;
			case 11:
				mFAB.setColorNormalResId(R.color.item_by_color_purple);
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public static void applyThemeFrameLayout(int whichTheme, FrameLayout mFrameLayout){
		try{
			Resources mResources = ApplicationLoader.getApplication().getResources();
			switch(whichTheme){
			case 0:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_fill));
				break;
			case 1:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
				break;
			case 2:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
				break;
			case 3:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_pink_fill));
				break;
			case 4:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_teal_fill));
				break;
			case 5:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_brown_fill));
				break;
			case 6:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
				break;
			case 7:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
				break;
			case 8:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
				break;
			case 9:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_orange_fill));
				break;
			case 10:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_green_fill));
				break;
			case 11:
				mFrameLayout.setBackgroundDrawable(mResources.getDrawable(R.drawable.shape_round_purple_fill));
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void applyThemeSmoothProgressBar(int whichTheme, SmoothProgressBar mSmoothProgressBar){
		try{
			Resources mResources = ApplicationLoader.getApplication().getResources();
			switch(whichTheme){
			case 0:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background));
				break;
			case 1:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_purple));
				break;
			case 2:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_green));
				break;
			case 3:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_pink));
				break;
			case 4:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_teal));
				break;
			case 5:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_brown));
				break;
			case 6:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_gray));
				break;
			case 7:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_amber));
				break;
			case 8:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_orange));
				break;
			case 9:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_lblue));
				break;
			case 10:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_red));
				break;
			case 11:
				mSmoothProgressBar.setSmoothProgressDrawableColor(mResources.getColor(R.color.toolbar_background_dpurple));
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
}
