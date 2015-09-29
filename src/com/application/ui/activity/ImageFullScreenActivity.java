/**
 * 
 */
package com.application.ui.activity;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.adapter.ImageFullScreenPagerAdapter;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.CirclePageIndicator;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageFullScreenActivity extends SwipeBackBaseActivity {
	private static final String TAG = ImageFullScreenActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private ViewPager mImageViewPager;
	private CirclePageIndicator mImageCirclePageIndicator;

	private ImageView mImageNextIv;
	private ImageView mImagePrevIv;

	private ImageFullScreenPagerAdapter mAdapter;

	private List<String> mArrayListString;

	private SystemBarTintManager mTintManager;

	private boolean isShareOptionEnable = false;
	
	private Intent mIntent;
	private int mPosition;
	private String mContentFilePath[];
//	private ArrayList<String> mContentDecryptedFilePath = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_fullscreen);
		setSecurity();
		setSystemBarTint();
		initToolBar();
		initUi();
		setUiListener();
		getIntentData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		decryptFileOnResume();
//		if(mContentDecryptedFilePath.size()> 0){
			setImageViewPager();
//		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		deleteDecryptedFile();
	}
	
	@Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		if(isShareOptionEnable){
			menu.findItem(R.id.action_share).setVisible(true);
			menu.findItem(R.id.action_refresh_actionable).setVisible(true);
		}else{
			menu.findItem(R.id.action_share).setVisible(false);
			menu.findItem(R.id.action_refresh_actionable).setVisible(false);
			menu.findItem(R.id.action_like).setVisible(false);
		}
		return super.onPrepareOptionsPanel(view, menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_text_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_refresh_actionable:
			return true;
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(ImageFullScreenActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(ImageFullScreenActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Image");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ImageFullScreenActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mImageNextIv = (ImageView) findViewById(R.id.fragmentImageFullScreenNextIv);
		mImagePrevIv = (ImageView) findViewById(R.id.fragmentImageFullScreenPreviousIv);

		mImageViewPager = (ViewPager) findViewById(R.id.fragmentImageFullScreenViewPager);
		mImageCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentImageFullScreenCirclePageIndicator);
	}

	private void getIntentData(){
		mIntent = getIntent();
		mPosition = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.POSITION, 0);
		mContentFilePath = mIntent.getStringArrayExtra(AppConstants.INTENTCONSTANTS.OBJECT);
//		if(mContentFilePath!=null){
//			setImageViewPager();
//		}
	}
	
	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.ImageDetailActivityTitle));
		mToolBar.setBackgroundColor(getResources().getColor(
				R.color.black_shade_light));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
	}

	private void setOnClickListener() {
	}

	private void setImageViewPager() {
//		mArrayListString = Arrays.asList(mContentFilePath);
//		mAdapter = new ImageFullScreenPagerAdapter(getSupportFragmentManager(),mContentDecryptedFilePath, mPosition);
		mAdapter = new ImageFullScreenPagerAdapter(getSupportFragmentManager(),Arrays.asList(mContentFilePath), mPosition);
		mImageViewPager.setAdapter(mAdapter);
		mImageCirclePageIndicator.setViewPager(mImageViewPager);
	}

	private void setMaterialRippleView() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	@SuppressLint("NewApi")
	private void setSystemBarTint() {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(Color.BLACK);
			} else {
				mTintManager = new SystemBarTintManager(
						ImageFullScreenActivity.this);
				// enable status bar tint
				mTintManager.setStatusBarTintEnabled(true);
				mTintManager.setStatusBarTintColor(getResources().getColor(
						android.R.color.black));
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	
	/*private void decryptFileOnResume(){
		try{
			if(mContentFilePath!=null && mContentFilePath.length > 0){
				mContentDecryptedFilePath.clear();
				for(int i = 0; i < mContentFilePath.length;i++){
					if(Utilities.isContainsDecrypted(mContentFilePath[i])){
						mContentFilePath[i] = mContentFilePath[i].replace(AppConstants.decrypted, "");
					}
					if(checkIfFileExists(mContentFilePath[i])){
						mContentDecryptedFilePath.add(Utilities.fbConcealDecryptFile(TAG, new File(mContentFilePath[i])));
					}
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void deleteDecryptedFile(){
		try{
			if(mContentDecryptedFilePath!=null && mContentDecryptedFilePath.size() > 0){
				for(int i = 0 ;i < mContentDecryptedFilePath.size();i++){
					if(Utilities.isContainsDecrypted(mContentDecryptedFilePath.get(i))){
						new File(mContentDecryptedFilePath.get(i)).delete();
					}
				}	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}*/
	
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
