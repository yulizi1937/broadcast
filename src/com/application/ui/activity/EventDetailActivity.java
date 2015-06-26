/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.view.BottomSheet;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class EventDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = EventDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mEventCoverLeftNumberTv;
	private AppCompatTextView mEventCoverLeftTextTv;
	private AppCompatTextView mEventTitleTv;
	private AppCompatTextView mEventByTv;
	private AppCompatTextView mEventViewTv;
	private AppCompatTextView mEventTimeDateNumberTv;
	private AppCompatTextView mEventTimeDateTextTv;
	private AppCompatTextView mEventTimeTillDayTv;
	private AppCompatTextView mEventTimeTillTv;
	private AppCompatTextView mEventTimeHoursNumberTv;
	private AppCompatTextView mEventTImeHoursTextTv;
	private AppCompatTextView mEventLocationTextTv;
	private AppCompatTextView mEventLocationViewMapTv;
	private AppCompatTextView mEventAttendInvitedNumberTv;
	private AppCompatTextView mEventAttendInvitedTextTv;
	private AppCompatTextView mEventAttendGoingNumberTv;
	private AppCompatTextView mEventAttendGoingTextTv;
	private AppCompatTextView mEventSummaryTitleTv;
	private AppCompatTextView mEventSummaryTextTv;

	private AppCompatButton mEventAttendJoinBtn;
	private AppCompatButton mEventAttendDeclineBtn;

	private ImageView mEventShareIv;
	private ImageView mEventAddToCalIv;

	private ImageView mEventCoverIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		initToolBar();
		initUi();
		setUiListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_event_detail, menu);
		if (AndroidUtilities.isAboveGingerBread()) {
			MenuItem searchItem = menu.findItem(R.id.action_search);
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = null;
			if (searchItem != null) {
				searchView = (SearchView) MenuItemCompat
						.getActionView(searchItem);
			}
			if (searchView != null) {
				searchView.setSearchableInfo(searchManager
						.getSearchableInfo(getComponentName()));
			}

			MenuItem refreshItem = menu
					.findItem(R.id.action_refresh_actionable);
			if (refreshItem != null) {
				View mView = MenuItemCompat.getActionView(refreshItem);
				MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout) mView
						.findViewById(R.id.toolBarActionItemRefresh);
				mToolBarMenuRefreshProgress = (ProgressWheel) mView
						.findViewById(R.id.toolBarActionItemProgressWheel);
				mToolBarMenuRefresh = (ImageView) mView
						.findViewById(R.id.toolBarActionItemImageView);
				mToolBarMenuRefreshLayout
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View mView) {
								// TODO Auto-generated method stub
								toolBarRefresh();
							}
						});
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_refresh_actionable:
			toolBarRefresh();
			return true;
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(EventDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mToolBarMenuRefresh.setVisibility(View.VISIBLE);
				mToolBarMenuRefreshProgress.setVisibility(View.GONE);
			}
		}, 5000);
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mEventCoverLeftNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailLeftNumberTv);
		mEventCoverLeftTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailLeftTextTv);

		mEventTitleTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailTitleTv);

		mEventByTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailByTv);
		mEventViewTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailViewTv);

		mEventTimeDateNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDateTv);
		mEventTimeDateTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDateTextTv);
		mEventTimeTillDayTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDayTv);
		mEventTimeTillTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventDayTillTv);
		mEventTimeHoursNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventHoursTv);
		mEventTImeHoursTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventHoursTextTv);

		mEventLocationTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventLocationTv);
		mEventLocationViewMapTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailEventLocationViewMapTv);

		mEventAttendInvitedNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetaiTotalAttendTv);
		mEventAttendInvitedTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetaiTotalAttendTextTv);
		mEventAttendGoingNumberTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetaiTotalGoingTv);
		mEventAttendGoingTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetaiTotalGoingTextTv);

		mEventSummaryTitleTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailAboutEventTv);
		mEventSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentEventDetailSummaryTv);

		mEventAttendJoinBtn = (AppCompatButton) findViewById(R.id.fragmentEventDetailJoinBtn);
		mEventAttendDeclineBtn = (AppCompatButton) findViewById(R.id.fragmentEventDetailDeclineBtn);

		mEventShareIv = (ImageView) findViewById(R.id.fragmentEventDetailEventShareIv);
		mEventAddToCalIv = (ImageView) findViewById(R.id.fragmentEventDetailEventAddToCalendarIv);
		mEventCoverIv = (ImageView) findViewById(R.id.fragmentEventDetailEventCoverIv);

		setHtmlData();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.EventDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}

	private void setHtmlData() {
		mEventLocationViewMapTv.setText(Html.fromHtml(getResources().getString(
				R.string.sample_event_view_map)));
	}

	private void setOnClickListener() {
		mEventShareIv.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showShareDialog();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void showShareDialog() {
		showDialog(0);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				"Hello ").limit(R.integer.bs_initial_grid_row).build();
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mEventAddToCalIv);
			setMaterialRippleOnView(mEventShareIv);
			setMaterialRippleOnView(mEventAttendJoinBtn);
			setMaterialRippleOnView(mEventCoverIv);
			setMaterialRippleWithGrayOnView(mEventAttendDeclineBtn);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
}
