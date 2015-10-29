/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.SmoothProgressBar;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class WebViewActivity extends SwipeBackBaseActivity {
	private static final String TAG = WebViewActivity.class.getSimpleName();

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;
	
	private Toolbar mToolBar;
    
	private WebView mWebView;
	
	private Intent mIntent;
    
	private SmoothProgressBar mSmoothProgressBar;
	
	private boolean isShareOptionEnable = true;
	
	private String mUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		setSecurity();
		initToolBar();
		initUi();
		getIntentData();
		initWebViewClient();
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsPanel(view, menu);
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_web, menu);
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
			AndroidUtilities.exitWindowAnimation(WebViewActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(WebViewActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Web");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(WebViewActivity.this);
			return true;
		case R.id.action_view_web:
			Intent mBrowserIntent = new Intent(Intent.ACTION_VIEW);
			mBrowserIntent.setData(Uri.parse(mUrl));
			startActivity(mBrowserIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void toolBarRefresh() {
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(mUrl)){
			mWebView.loadUrl(mUrl);
		}
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
		mWebView = (WebView)findViewById(R.id.fragmentWebViewWebView);
		mSmoothProgressBar = (SmoothProgressBar)findViewById(R.id.fragmentWebViewProgressBar);
	}
	
	@SuppressLint("NewApi") private void initWebViewClient(){
		mWebView.setWebChromeClient(new MyWebViewChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());
		try{
			if(AndroidUtilities.isAboveHoneyComb()){
				mWebView.getSettings().setBuiltInZoomControls(true);
				mWebView.getSettings().setDisplayZoomControls(false);
				mWebView.getSettings().setJavaScriptEnabled(true);
				mWebView.getSettings().setSupportZoom(true);
				mWebView.getSettings().setDomStorageEnabled(true);
				mWebView.getSettings().setSaveFormData(true);
				mWebView.getSettings().setSavePassword(true);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
    	mWebView.loadUrl(mUrl);
    	setToolBarOption();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.WebViewActivity));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(WebViewActivity.this).applyThemeCountrySelect(WebViewActivity.this, WebViewActivity.this, mToolBar);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		if(mIntent!=null){
			if(mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE).equalsIgnoreCase("help")){
				mToolBar.setTitle(getResources().getString(R.string.textview_help));	
			}else if(mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ACTIVITYTITLE).equalsIgnoreCase(AppConstants.INTENTCONSTANTS.OPEN)){
				mUrl = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.LINK);
			}
		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		}

		@Override
		public void onPageFinished(WebView view, String url) {
		}

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class MyWebViewChromeClient extends WebChromeClient{
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			if(newProgress == 100){
				mSmoothProgressBar.setVisibility(View.GONE);
			}else{
				mSmoothProgressBar.setVisibility(View.VISIBLE);
			}
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
