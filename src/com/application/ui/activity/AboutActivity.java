/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.ui.view.BottomSheet;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.UserReport;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AboutActivity extends SwipeBackBaseActivity {
	private static final String TAG = AboutActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private ImageView mAboutLogoIv;

	private AppCompatTextView mAboutVersionTv;
	private AppCompatTextView mAboutVersionDetailTv;
	private AppCompatTextView mAboutFeedbackTv;
	private AppCompatTextView mAboutFeedbackDetailTv;
	private AppCompatTextView mAboutReportTv;
	private AppCompatTextView mAboutReportDetailTv;
	private AppCompatTextView mAboutPrivacyPolicyTv;

	private LinearLayout mAboutVersionLayout;
	private LinearLayout mAboutFeedbackLayout;
	private LinearLayout mAboutReportLayout;

	private WebView mAboutWebView;

	private boolean isShareOptionEnable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setSecurity();
		initToolBar();
		initUi();
		setUiListener();
		setAnimation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		try{
			if (isShareOptionEnable) {
				menu.findItem(R.id.action_share).setVisible(true);
			} else {
				menu.findItem(R.id.action_share).setVisible(false);
			}
		}catch(Exception e){
			
		}
		return super.onPrepareOptionsPanel(view, menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(AboutActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mAboutLogoIv = (ImageView) findViewById(R.id.fragmentAboutLogoIv);

		mAboutVersionTv = (AppCompatTextView) findViewById(R.id.fragmentAboutVersionTv);
		mAboutVersionDetailTv = (AppCompatTextView) findViewById(R.id.fragmentAboutVersionDetailTv);
		mAboutFeedbackTv = (AppCompatTextView) findViewById(R.id.fragmentAboutSendFeedbackTv);
		mAboutFeedbackDetailTv = (AppCompatTextView) findViewById(R.id.fragmentAboutSendFeedbackDetailTv);
		mAboutReportTv = (AppCompatTextView) findViewById(R.id.fragmentAboutReportTv);
		mAboutReportDetailTv = (AppCompatTextView) findViewById(R.id.fragmentAboutReportDetailTv);
		mAboutPrivacyPolicyTv = (AppCompatTextView) findViewById(R.id.fragmentAboutPrivacyPolicyTv);

		mAboutVersionLayout = (LinearLayout) findViewById(R.id.fragmentAboutVersionLayout);
		mAboutReportLayout = (LinearLayout) findViewById(R.id.fragmentAboutReportLayout);
		mAboutFeedbackLayout = (LinearLayout) findViewById(R.id.fragmentAboutSendFeedbackLayout);

		mAboutWebView = (WebView) findViewById(R.id.fragmentAboutDetailWebView);

		mAboutPrivacyPolicyTv.setText(Html.fromHtml(getResources().getString(
				R.string.sample_about_privacy_policy)));

		setWebViewData();
	}

	private void setWebViewData() {
		String text = getResources().getString(R.string.sample_about_detail);
		String htmlText = "<html><body style=\"text-align:justify\"><font size=\"1\" color=\"#254E7A\"> %s </font></body></Html>";
		mAboutWebView.loadData(String.format(htmlText, text), "text/html",
				"utf-8");
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(R.string.AboutActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.Shake).duration(2000).playOn(mAboutLogoIv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setOnClickListener();
	}

	private void setOnClickListener() {
		mAboutFeedbackLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(AboutActivity.this,
						FeedbackAppActivity.class);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(AboutActivity.this);
			}
		});

		mAboutReportLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(AboutActivity.this,
						ReportActivity.class);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(AboutActivity.this);
			}
		});
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		UserReport.updateUserReportApi("-1", "about", AppConstants.REPORT.SHARE, "");
		return getShareAction();
	}

	protected BottomSheet getShareAction() {
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				"Hello ").limit(R.integer.bs_initial_grid_row).build();
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
