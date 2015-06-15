/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Award;
import com.application.ui.adapter.AwardRecyclerAdapter;
import com.application.ui.adapter.AwardRecyclerAdapter.OnItemClickListener;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AwardRecyclerActivity extends SwipeBackBaseActivity {
	@SuppressWarnings("unused")
	private static final String TAG = AwardRecyclerActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private ObservableRecyclerView mRecyclerView;

	private FrameLayout mCroutonViewGroup;

	private AwardRecyclerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_award);
		initToolBar();
		initUi();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setRecycleAdapter();
		setUiListener();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_award, menu);
		if (AndroidUtilities.isAboveGingerBread()) {
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
			AndroidUtilities.exitWindowAnimation(AwardRecyclerActivity.this);
			return true;
		case R.id.action_search:
			Intent mIntent = new Intent(AwardRecyclerActivity.this,
					SearchActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.AWARDS);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(AwardRecyclerActivity.this);
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
		mRecyclerView = (ObservableRecyclerView) findViewById(R.id.scroll_wo);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.AwardRecyclerActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setRecycleAdapter() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(
				AwardRecyclerActivity.this);
		mRecyclerView.setLayoutManager(layoutManager);
		ArrayList<Award> mList = new ArrayList<Award>();
		for (int i = 0; i < 30; i++) {
			Award obj = new Award();
			obj.setmAwardName("AWARD FOR BEST SALES OF THE WEEK "
					+ String.valueOf(i));
			mList.add(obj);
		}
		mAdapter = new AwardRecyclerAdapter(AwardRecyclerActivity.this, mList);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView
				.addItemDecoration(new HorizontalDividerItemDecoration.Builder(
						this)
						.color(Utilities.getDividerColor())
						.marginResId(
								R.dimen.fragment_recyclerview_award_left_margin,
								R.dimen.fragment_recyclerview_award_right_margin)
						.build());
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
	}

	private void setRecyclerAdapterListener() {
		mAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				switch (view.getId()) {
				case R.id.itemRecyclerCongratulateIv:
					Utilities.showCrouton(AwardRecyclerActivity.this,
							mCroutonViewGroup, "itemRecyclerCongratulateIv",
							Style.INFO);
					break;
				default:
					Utilities.showCrouton(AwardRecyclerActivity.this,
							mCroutonViewGroup, "itemRecylerAwardRootLayout",
							Style.INFO);
					break;
				}
			}
		});
	}
}
