/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.application.ui.adapter.CountryAdapter;
import com.application.ui.adapter.CountryAdapter.Country;
import com.application.ui.adapter.CountrySearchAdapter;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.SectionsListView;
import com.application.utils.AndroidUtilities;
import com.application.utils.LocaleController;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class CountrySelectActivity extends SwipeBackBaseActivity {
	private static final String TAG = CountrySelectActivity.class
			.getSimpleName();

	private FrameLayout fragmentView;

	private TextView mToolBarTitleTv;

	private Toolbar mToolBar;

	private AppCompatEditText mToolBarEv;

	private ImageView mToolBarSearch;
	private ImageView mToolBarSearchClear;

	private ImageView mToolBarDrawer;

	private SectionsListView listView;
	private TextView emptyTextView;
	private CountryAdapter listViewAdapter;
	private CountrySearchAdapter searchListViewAdapter;

	private boolean searchWas;
	private boolean searching = false;

	private CountrySelectActivityDelegate delegate;

	public static interface CountrySelectActivityDelegate {
		public abstract void didSelectCountry(String name);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_select);
		setSecurity();
		initUi();
		initToolBar();
		initListView();
		setUiListener();
		setMaterialRippleView();
	}

	private void initUi() {
		fragmentView = (FrameLayout) findViewById(R.id.activityCountrySelectFrameLayout);
	}

	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		setSupportActionBar(mToolBar);
		mToolBarTitleTv = (TextView) findViewById(R.id.toolbarTitleTv);
		mToolBarDrawer = (ImageView) findViewById(R.id.toolbarBackIv);
		mToolBarEv = (AppCompatEditText) findViewById(R.id.toolbarEv);
		mToolBarSearch = (ImageView) findViewById(R.id.toolbarSearch);
		mToolBarSearchClear = (ImageView) findViewById(R.id.toolbarSearchClear);

		mToolBarDrawer.setVisibility(View.VISIBLE);
		mToolBarTitleTv.setText(getResources().getString(
				R.string.CountrySelectActivityTitle));
	}

	private void setUiListener() {
		mToolBarSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				checkToolBarSearchAccordingly();
			}
		});

		mToolBarSearchClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkToolBarSearchAccordingly();
			}
		});
		setTextWatcher();
		
		
		mToolBarDrawer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				AndroidUtilities.exitWindowAnimation(CountrySelectActivity.this);
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setTextWatcher() {
		mToolBarEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				String text = mCharsequence.toString();
				searchListViewAdapter.search(text);
				if (text.length() != 0) {
					searchWas = true;
					if (listView != null) {
						listView.setAdapter(searchListViewAdapter);
						if (android.os.Build.VERSION.SDK_INT >= 11) {
							listView.setFastScrollAlwaysVisible(false);
						}
						listView.setFastScrollEnabled(false);
						listView.setVerticalScrollBarEnabled(true);
					}
					if (emptyTextView != null) {

					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence mCharsequence,
					int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable mEditable) {
				// TODO Auto-generated method stub

			}
		});
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void checkToolBarSearchAccordingly() {
		if (!searching) {// expand
			searching = true;
			mToolBarTitleTv.setVisibility(View.GONE);
			mToolBarEv.setVisibility(View.VISIBLE);
			mToolBarSearchClear.setVisibility(View.VISIBLE);
			mToolBarSearch.setVisibility(View.GONE);
			mToolBarEv.requestFocus();
		} else {// collapse
			searching = false;
			mToolBarSearchClear.setVisibility(View.GONE);
			mToolBarSearch.setVisibility(View.VISIBLE);
			mToolBarTitleTv.setVisibility(View.VISIBLE);
			mToolBarEv.setVisibility(View.GONE);
			searchListViewAdapter.search(null);
			searching = false;
			searchWas = false;
			ViewGroup group = (ViewGroup) listView.getParent();
			listView.setAdapter(listViewAdapter);
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				listView.setFastScrollAlwaysVisible(true);
			}
			listView.setFastScrollEnabled(true);
			listView.setVerticalScrollBarEnabled(false);

			emptyTextView.setText(getResources().getString(
					R.string.CountrySelectActivityTitle));
		}
	}

	@SuppressLint("NewApi")
	private void initListView() {
		searching = false;
		searchWas = false;

		listViewAdapter = new CountryAdapter(CountrySelectActivity.this);
		searchListViewAdapter = new CountrySearchAdapter(
				CountrySelectActivity.this, listViewAdapter.getCountries());

		LinearLayout emptyTextLayout = new LinearLayout(
				CountrySelectActivity.this);
		emptyTextLayout.setVisibility(View.INVISIBLE);
		emptyTextLayout.setOrientation(LinearLayout.VERTICAL);
		((FrameLayout) fragmentView).addView(emptyTextLayout);
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) emptyTextLayout
				.getLayoutParams();
		layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
		layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
		layoutParams.gravity = Gravity.TOP;
		emptyTextLayout.setLayoutParams(layoutParams);
		emptyTextLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		emptyTextView = new TextView(CountrySelectActivity.this);
		emptyTextView.setTextColor(0xff808080);
		emptyTextView.setTextSize(20);
		emptyTextView.setGravity(Gravity.CENTER);
		emptyTextView.setText(LocaleController.getString("NoResult",
				R.string.NoResult));
		emptyTextLayout.addView(emptyTextView);
		LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) emptyTextView
				.getLayoutParams();
		layoutParams1.width = LinearLayout.LayoutParams.MATCH_PARENT;
		layoutParams1.height = LinearLayout.LayoutParams.MATCH_PARENT;
		layoutParams1.weight = 0.5f;
		emptyTextView.setLayoutParams(layoutParams1);

		FrameLayout frameLayout = new FrameLayout(CountrySelectActivity.this);
		emptyTextLayout.addView(frameLayout);
		layoutParams1 = (LinearLayout.LayoutParams) frameLayout
				.getLayoutParams();
		layoutParams1.width = LinearLayout.LayoutParams.MATCH_PARENT;
		layoutParams1.height = LinearLayout.LayoutParams.MATCH_PARENT;
		layoutParams1.weight = 0.5f;
		frameLayout.setLayoutParams(layoutParams1);

		listView = new SectionsListView(CountrySelectActivity.this);
		listView.setEmptyView(emptyTextLayout);
		listView.setVerticalScrollBarEnabled(false);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setFastScrollEnabled(true);
		listView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		listView.setAdapter(listViewAdapter);
		if (Build.VERSION.SDK_INT >= 11) {
			listView.setFastScrollAlwaysVisible(true);
			listView.setVerticalScrollbarPosition(LocaleController.isRTL ? ListView.SCROLLBAR_POSITION_LEFT
					: ListView.SCROLLBAR_POSITION_RIGHT);
		}
		((FrameLayout) fragmentView).addView(listView);
		layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
		layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
		layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
		listView.setLayoutParams(layoutParams);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				Country country = null;
				if (searching && searchWas) {
					country = searchListViewAdapter.getItem(i);
				} else {
					int section = listViewAdapter.getSectionForPosition(i);
					int row = listViewAdapter
							.getPositionInSectionForPosition(i);
					if (row < 0 || section < 0) {
						return;
					}
					country = listViewAdapter.getItem(section, row);
				}
				if (i < 0) {
					return;
				}
				if (country != null) {
					// delegate.didSelectCountry(country.name);
					Intent mIntent = new Intent();
					mIntent.putExtra("country_code", "+" + country.code);
					mIntent.putExtra("country_code_", country.shortname);
					setResult(Activity.RESULT_OK, mIntent);
					finish();
				}
			}
		});

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int i) {
				if (i == SCROLL_STATE_TOUCH_SCROLL && searching && searchWas) {
					AndroidUtilities.hideKeyboard(CountrySelectActivity.this
							.getCurrentFocus());
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (absListView.isFastScrollEnabled()) {
					AndroidUtilities.clearDrawableAnimation(absListView);
				}
			}
		});
	}

	/*
	 * @Override public View createView(LayoutInflater inflater, final ViewGroup
	 * container) { if (fragmentView == null) {
	 * 
	 * 
	 * ActionBarMenu menu = actionBar.createMenu(); menu.addItem(0,
	 * R.drawable.ic_ab_search
	 * ).setIsSearchField(true).setActionBarMenuItemSearchListener(new
	 * ActionBarMenuItem.ActionBarMenuItemSearchListener() {
	 * 
	 * @Override public void onSearchExpand() { searching = true; }
	 * 
	 * @Override public void onSearchCollapse() {
	 * 
	 * }
	 * 
	 * @Override public void onTextChanged(EditText editText) {
	 * 
	 * } });
	 * 
	 * 
	 * } else { ViewGroup parent = (ViewGroup)fragmentView.getParent(); if
	 * (parent != null) { parent.removeView(fragmentView); } } return
	 * fragmentView; }
	 */

	@Override
	public void onResume() {
		super.onResume();
		if (listViewAdapter != null) {
			listViewAdapter.notifyDataSetChanged();
		}
	}

	public void setCountrySelectActivityDelegate(
			CountrySelectActivityDelegate delegate) {
		this.delegate = delegate;
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mToolBarDrawer);
			setMaterialRippleOnView(mToolBarSearch);
			setMaterialRippleOnView(mToolBarSearchClear);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	/**
	 * Google Analytics v3
	 */
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
