/*
 * Copyright 2011, 2015 Kai Liao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.application.ui.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.ui.view.BottomSheet.Builder;
import com.mobcast.R;

/**
 * One way to present a set of actions to a user is with bottom sheets, a sheet
 * of paper that slides up from the bottom edge of the screen. Bottom sheets
 * offer flexibility in the display of clear and simple actions that do not need
 * explanation.
 * <p/>
 * https://www.google.com/design/spec/components/bottom-sheets.html
 * <p/>
 * Project: BottomSheet Created by Kai Liao on 2014/9/21.
 */
@SuppressWarnings("unused")
public class BottomSheetAnyDo extends Dialog implements DialogInterface {

	private String moreText;
	private Drawable close;
	private Drawable more;
	private boolean collapseListIcons;
	private int mStatusBarHeight;

	private final SparseIntArray hidden = new SparseIntArray();

	// translucent support
	private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
	private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
	private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
	private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
	private boolean mInPortrait;
	private String sNavBarOverride;
	private boolean mNavBarAvailable;
	private float mSmallestWidthDp;

	private ImageView icon;

	private Builder builder;

	private int limit = -1;
	private boolean cancelOnTouchOutside = true;
	private boolean cancelOnSwipeDown = true;
	private OnDismissListener dismissListener;
	private View contentLayout;
	private FrameLayout rootLayout;
	private LayoutInflater inflater;
	private int mId;
	private int mLayoutId;
	private String mCategory;
	private TextView mTextViewTv;
	private TextView mTextCloseTv;

	BottomSheetAnyDo(Context context) {
		super(context, R.style.BottomSheet_Dialog);
	}

	@SuppressWarnings("WeakerAccess")
	BottomSheetAnyDo(Context context, int theme) {
		super(context, theme);

		TypedArray a = getContext().obtainStyledAttributes(null,
				R.styleable.BottomSheet, R.attr.bottomSheetStyle, 0);
		try {
			more = a.getDrawable(R.styleable.BottomSheet_bs_moreDrawable);
			close = a.getDrawable(R.styleable.BottomSheet_bs_closeDrawable);
			moreText = a.getString(R.styleable.BottomSheet_bs_moreText);
			collapseListIcons = a.getBoolean(
					R.styleable.BottomSheet_bs_collapseListIcons, true);
		} finally {
			a.recycle();
		}

		// https://github.com/jgilfelt/SystemBarTint/blob/master/library/src/com/readystatesoftware/systembartint/SystemBarTintManager.java
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			mInPortrait = (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
			try {
				Class c = Class.forName("android.os.SystemProperties");
				@SuppressWarnings("unchecked")
				Method m = c.getDeclaredMethod("get", String.class);
				m.setAccessible(true);
				sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
			} catch (Throwable e) {
				sNavBarOverride = null;
			}

			// check theme attrs
			int[] as = { android.R.attr.windowTranslucentNavigation};
			a = context.obtainStyledAttributes(as);
			try {
				mNavBarAvailable = a.getBoolean(0, false);
			} finally {
				a.recycle();
			}

			// check window flags
			WindowManager.LayoutParams winParams = ((Activity) context)
					.getWindow().getAttributes();

			int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
			if ((winParams.flags & bits) != 0) {
				mNavBarAvailable = true;
			}

			mSmallestWidthDp = getSmallestWidthDp(wm);
			if (mNavBarAvailable)
				setTranslucentStatus(true);
			mStatusBarHeight = getInternalDimensionSize(context.getResources(),
					STATUS_BAR_HEIGHT_RES_NAME);
		}
	}

	@SuppressLint("NewApi")
	private float getSmallestWidthDp(WindowManager wm) {
		DisplayMetrics metrics = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			wm.getDefaultDisplay().getRealMetrics(metrics);
		} else {
			// this is not correct, but we don't really care pre-kitkat
			wm.getDefaultDisplay().getMetrics(metrics);
		}
		float widthDp = metrics.widthPixels / metrics.density;
		float heightDp = metrics.heightPixels / metrics.density;
		return Math.min(widthDp, heightDp);
	}

	@TargetApi(14)
	private int getNavigationBarHeight(Context context) {
		Resources res = context.getResources();
		int result = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (hasNavBar(context)) {
				String key;
				if (mInPortrait) {
					key = NAV_BAR_HEIGHT_RES_NAME;
				} else {
					if (!isNavigationAtBottom())
						return 0;
					key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
				}
				return getInternalDimensionSize(res, key);
			}
		}
		return result;
	}

	@TargetApi(14)
	private boolean hasNavBar(Context context) {
		Resources res = context.getResources();
		int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool",
				"android");
		if (resourceId != 0) {
			boolean hasNav = res.getBoolean(resourceId);
			// check override flag (see static block)
			if ("1".equals(sNavBarOverride)) {
				hasNav = false;
			} else if ("0".equals(sNavBarOverride)) {
				hasNav = true;
			}
			return hasNav;
		} else { // fallback
			return !ViewConfiguration.get(context).hasPermanentMenuKey();
		}
	}

	private int getInternalDimensionSize(Resources res, String key) {
		int result = 0;
		int resourceId = res.getIdentifier(key, "dimen", "android");
		if (resourceId > 0) {
			result = res.getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * Should a navigation bar appear at the bottom of the screen in the current
	 * device configuration? A navigation bar may appear on the right side of
	 * the screen in certain configurations.
	 * 
	 * @return True if navigation should appear at the bottom of the screen,
	 *         False otherwise.
	 */
	private boolean isNavigationAtBottom() {
		return (mSmallestWidthDp >= 600 || mInPortrait);
	}

	/**
	 * Hacky way to get gridview's column number
	 */
	private int getNumColumns() {
		try {
			Field numColumns = GridView.class
					.getDeclaredField("mRequestedNumColumns");
			numColumns.setAccessible(true);
			return 1;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		super.setCanceledOnTouchOutside(cancel);
		cancelOnTouchOutside = cancel;
	}

	/**
	 * Sets whether this dialog is canceled when swipe it down
	 * 
	 * @param cancel
	 *            whether this dialog is canceled when swipe it down
	 */
	public void setCanceledOnSwipeDown(boolean cancel) {
		cancelOnSwipeDown = cancel;
	}

	private void init(final Context context) {
		setCanceledOnTouchOutside(cancelOnTouchOutside);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ClosableSlidingLayout mDialogView = (ClosableSlidingLayout) View
				.inflate(context, R.layout.bottom_sheet_dialog_any_do, null);
		setContentView(mDialogView);
		mLayoutId = builder.mLayoutId;
		mId = builder.mId;
		mCategory = builder.mCategory;
		contentLayout = (View) View.inflate(context, mLayoutId, null);/*
																	 * inflater.
																	 * inflate
																	 * (mLayoutId
																	 * ,
																	 * mDialogView
																	 * , false);
																	 */
		rootLayout = (FrameLayout) mDialogView
				.findViewById(R.id.bottom_sheet_frame);
		mTextViewTv = (TextView)mDialogView.findViewById(R.id.bottom_sheet_view);
		mTextCloseTv = (TextView)mDialogView.findViewById(R.id.bottom_sheet_close);
		rootLayout.addView(contentLayout);
		if (!cancelOnSwipeDown)
			mDialogView.swipeable = cancelOnSwipeDown;
		mDialogView.setSlideListener(new ClosableSlidingLayout.SlideListener() {
			@Override
			public void onClosed() {
				BottomSheetAnyDo.this.dismiss();
			}

			@Override
			public void onOpened() {
			}
		});

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
			}
		});
		int[] location = new int[2];
		mDialogView.getLocationOnScreen(location);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			mDialogView.setPadding(0, location[0] == 0 ? mStatusBarHeight : 0,
					0, 0);
			mDialogView.getChildAt(0).setPadding(
					0,
					0,
					0,
					mNavBarAvailable ? getNavigationBarHeight(getContext())
							+ mDialogView.getPaddingBottom() : 0);
		}

		final TextView title = (TextView) mDialogView
				.findViewById(R.id.bottom_sheet_title);
		if (builder.title != null) {
			title.setVisibility(View.VISIBLE);
			title.setText(builder.title);
		}

		icon = (ImageView) mDialogView
				.findViewById(R.id.bottom_sheet_title_image);

		mDialogView.mTarget = rootLayout;

		limit = Integer.MAX_VALUE;

		mDialogView.setCollapsible(false);
	}
	
	public TextView getTextViewTv(){
		return mTextViewTv;
	}
	
	public TextView getTextCloseTv(){
		return mTextCloseTv;
	}
	
	public View getContentView(){
		return contentLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(getContext());

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;

		TypedArray a = getContext().obtainStyledAttributes(
				new int[] { android.R.attr.layout_width });
		try {
			params.width = a.getLayoutDimension(0,
					ViewGroup.LayoutParams.MATCH_PARENT);
		} finally {
			a.recycle();
		}
		super.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (dismissListener != null)
					dismissListener.onDismiss(dialog);
			}
		});
		getWindow().setAttributes(params);
	}

	@SuppressWarnings("SameParameterValue")
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}

		win.setAttributes(winParams);
		// instance
		win.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		this.dismissListener = listener;
	}

	/**
	 * Constructor using a context for this builder and the
	 * {@link com.cocosw.bottomsheet.BottomSheet} it creates.
	 */
	public static class Builder {

		private final Context context;
		private int theme;
		private final ActionMenu menu;
		private CharSequence title;
		private boolean grid;
		private OnClickListener listener;
		private OnDismissListener dismissListener;
		private Drawable icon;
		private int limit = -1;
		private MenuItem.OnMenuItemClickListener menulistener;
		private int mId;
		private int mLayoutId;
		private String mCategory;

		/**
		 * Constructor using a context for this builder and the
		 * {@link com.cocosw.bottomsheet.BottomSheet} it creates.
		 * 
		 * @param context
		 *            A Context for built BottomSheet.
		 */
		public Builder(@NonNull Activity context, int mId, String mCategory,
				int mLayoutId) {
			this(context, R.style.BottomSheet_Dialog);
			TypedArray ta = context.getTheme().obtainStyledAttributes(
					new int[] { R.attr.bottomSheetStyle });
			this.mId = mId;
			this.mCategory = mCategory;
			this.mLayoutId = mLayoutId;
			try {
				theme = ta.getResourceId(0, R.style.BottomSheet_Dialog);
			} finally {
				ta.recycle();
			}
		}

		/**
		 * Constructor using a context for this builder and the
		 * {@link com.cocosw.bottomsheet.BottomSheet} it creates with given
		 * style
		 * 
		 * @param context
		 *            A Context for built BottomSheet.
		 * @param theme
		 *            The theme id will be apply to BottomSheet
		 */
		public Builder(Context context, @StyleRes int theme) {
			this.context = context;
			this.theme = theme;
			this.menu = new ActionMenu(context);
		}

		/**
		 * Set menu resources as list item to display in BottomSheet
		 * 
		 * @param xmlRes
		 *            menu resource id
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder sheet(@MenuRes int xmlRes) {
			new MenuInflater(context).inflate(xmlRes, menu);
			return this;
		}

		/**
		 * Add one item into BottomSheet
		 * 
		 * @param id
		 *            ID of item
		 * @param iconRes
		 *            icon resource
		 * @param textRes
		 *            text resource
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder sheet(int id, @DrawableRes int iconRes,
				@StringRes int textRes) {
			ActionMenuItem item = new ActionMenuItem(context, 0, id, 0, 0,
					context.getText(textRes));
			item.setIcon(iconRes);
			menu.add(item);
			return this;
		}

		/**
		 * Add one item into BottomSheet
		 * 
		 * @param id
		 *            ID of item
		 * @param icon
		 *            icon
		 * @param text
		 *            text
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder sheet(int id, @NonNull Drawable icon,
				@NonNull CharSequence text) {
			ActionMenuItem item = new ActionMenuItem(context, 0, id, 0, 0, text);
			item.setIcon(icon);
			menu.add(item);
			return this;
		}

		/**
		 * Add one item without icon into BottomSheet
		 * 
		 * @param id
		 *            ID of item
		 * @param textRes
		 *            text resource
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder sheet(int id, @StringRes int textRes) {
			menu.add(0, id, 0, textRes);
			return this;
		}

		/**
		 * Add one item without icon into BottomSheet
		 * 
		 * @param id
		 *            ID of item
		 * @param text
		 *            text
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder sheet(int id, @NonNull CharSequence text) {
			menu.add(0, id, 0, text);
			return this;
		}

		/**
		 * Set title for BottomSheet
		 * 
		 * @param titleRes
		 *            title for BottomSheet
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder title(@StringRes int titleRes) {
			title = context.getText(titleRes);
			return this;
		}

		/**
		 * Remove an item from BottomSheet
		 * 
		 * @param id
		 *            ID of item
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		@Deprecated
		public Builder remove(int id) {
			menu.removeItem(id);
			return this;
		}

		/**
		 * Set title for BottomSheet
		 * 
		 * @param icon
		 *            icon for BottomSheet
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder icon(Drawable icon) {
			this.icon = icon;
			return this;
		}

		/**
		 * Set title for BottomSheet
		 * 
		 * @param iconRes
		 *            icon resource id for BottomSheet
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder icon(@DrawableRes int iconRes) {
			this.icon = context.getResources().getDrawable(iconRes);
			return this;
		}

		/**
		 * Set OnclickListener for BottomSheet
		 * 
		 * @param listener
		 *            OnclickListener for BottomSheet
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder listener(@NonNull OnClickListener listener) {
			this.listener = listener;
			return this;
		}

		/**
		 * Set OnMenuItemClickListener for BottomSheet
		 * 
		 * @param listener
		 *            OnMenuItemClickListener for BottomSheet
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder listener(
				@NonNull MenuItem.OnMenuItemClickListener listener) {
			this.menulistener = listener;
			return this;
		}

		/**
		 * Show BottomSheet in dark color theme looking
		 * 
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder darkTheme() {
			theme = R.style.BottomSheet_Dialog_Dark;
			return this;
		}

		/**
		 * Show BottomSheet
		 * 
		 * @return Instance of bottomsheet
		 */
		public BottomSheetAnyDo show(int mId, String mCategory, int mLayoutId) {
			BottomSheetAnyDo dialog = build();
			dialog.show();
			return dialog;
		}

		/**
		 * Show items in grid instead of list
		 * 
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder grid() {
			this.grid = true;
			return this;
		}

		/**
		 * Set initial number of actions which will be shown in current sheet.
		 * If more actions need to be shown, a "more" action will be displayed
		 * in the last position.
		 * 
		 * @param limitRes
		 *            resource id for initial number of actions
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder limit(@IntegerRes int limitRes) {
			limit = context.getResources().getInteger(limitRes);
			return this;
		}

		/**
		 * Create a BottomSheet but not show it
		 * 
		 * @return Instance of bottomsheet
		 */
		@SuppressLint("Override")
		public BottomSheetAnyDo build() {
			BottomSheetAnyDo dialog = new BottomSheetAnyDo(context, theme);
			dialog.builder = this;
			return dialog;
		}

		/**
		 * Set title for BottomSheet
		 * 
		 * @param title
		 *            title for BottomSheet
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder title(CharSequence title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the OnDismissListener for BottomSheet
		 * 
		 * @param listener
		 *            OnDismissListener for Bottom
		 * @return This Builder object to allow for chaining of calls to set
		 *         methods
		 */
		public Builder setOnDismissListener(@NonNull OnDismissListener listener) {
			this.dismissListener = listener;
			return this;
		}
	}

}
