/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.ui.view.BottomSheet;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class InteractiveDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = InteractiveDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;
	
	private AppCompatTextView mInteractiveTitleTv;
	private AppCompatTextView mInteractiveByTv;
	private AppCompatTextView mInteractiveViewTv;
	private AppCompatTextView mInteractiveSummaryTextTv;
	
	private AppCompatTextView mInteractiveLinkTv;
	
	private LinearLayout mInteractiveLinkLayout;
	
	private boolean isShareOptionEnable = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interactive_detail);
		initToolBar();
		initUi();
		initUiWithData();
		setUiListener();
		setAnimation();
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		if(isShareOptionEnable){
			menu.findItem(R.id.action_share).setVisible(true);
		}else{
			menu.findItem(R.id.action_share).setVisible(false);
		}
		return super.onPrepareOptionsPanel(view, menu);
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_text_detail, menu);
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
			AndroidUtilities.exitWindowAnimation(InteractiveDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(InteractiveDetailActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Interactive");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(InteractiveDetailActivity.this);
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
		
		mInteractiveTitleTv = (AppCompatTextView)findViewById(R.id.fragmentInteractiveDetailTitleTv);
		
		mInteractiveByTv = (AppCompatTextView)findViewById(R.id.fragmentInteractiveDetailByTv);
		mInteractiveSummaryTextTv = (AppCompatTextView)findViewById(R.id.fragmentInteractiveDetailSummaryTv);
		mInteractiveViewTv = (AppCompatTextView)findViewById(R.id.fragmentInteractiveDetailViewTv);
		
		mInteractiveLinkTv = (AppCompatTextView)findViewById(R.id.fragmentInteractiveDetailLinkTv);
		
		mInteractiveLinkLayout = (LinearLayout)findViewById(R.id.fragmentInteractiveDetailViewSourceLayout);
		
	}
	
	private void initUiWithData(){
		mInteractiveLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.InteractiveDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation(){
		try{
			YoYo.with(Techniques.ZoomIn).duration(500).playOn(mInteractiveTitleTv);
			YoYo.with(Techniques.FadeInLeft).duration(1000).playOn(mInteractiveLinkLayout);
		}catch(Exception e){
			Log.i(TAG, e.toString());
		}
	}
	
	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
	}
	
	private void setOnClickListener(){
		mInteractiveLinkLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntentWebView = new Intent(InteractiveDetailActivity.this, WebViewActivity.class);
				startActivity(mIntentWebView);
			}
		});
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return getShareAction();
	}
	
	protected BottomSheet getShareAction(){
    	return getShareActions(new BottomSheet.Builder(this).grid().title("Share To "), "Hello ").limit(R.integer.bs_initial_grid_row).build();
    }

	private void setMaterialRippleView() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
