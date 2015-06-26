/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
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

import com.application.ui.adapter.ImageDetailPagerAdapter;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.ChipsLayout;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.FlowLayout;
import com.application.ui.view.MaterialRippleLayout;
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
public class ImageDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = ImageDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;
	
	private LinearLayout mLanguageLinearLayout;

	private FlowLayout mLanguageFlowLayout;


	private FrameLayout mCroutonViewGroup;
	
	private AppCompatTextView mImageTitleTv;
	private AppCompatTextView mImageByTv;
	private AppCompatTextView mImageViewTv;
	private AppCompatTextView mImageSummaryTextTv;
	private AppCompatTextView mLanguageHeaderTv;
	
	private ViewPager mImageViewPager;
	private CirclePageIndicator mImageCirclePageIndicator;
	
	
	private AppCompatTextView mImageNewsLinkTv;

	private LinearLayout mImageNewsLinkLayout;
	
	private ImageView mImageNextIv;
	private ImageView mImagePrevIv;
	private ImageView mShareIv;
	
	private ImageDetailPagerAdapter mAdapter;
	
	private ArrayList<String> mArrayListString;
	
	private boolean isTraining = false;//HDFC
	private boolean isShareOptionEnable = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail);
		initToolBar();
		initUi();
		initUiWithData();
		setUiListener();
		setImageViewPager();
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
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
		if(isShareOptionEnable){
			menu.findItem(R.id.action_share).setVisible(true);
		}else{
			menu.findItem(R.id.action_share).setVisible(false);
		}
		return super.onPrepareOptionsPanel(view, menu);
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
			AndroidUtilities.exitWindowAnimation(ImageDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(ImageDetailActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Image");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(ImageDetailActivity.this);
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
		
		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentImageDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentImageDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentImageDetailLanguageHeaderTv);
		
		mImageTitleTv = (AppCompatTextView)findViewById(R.id.fragmentImageDetailTitleTv);
		mImageByTv = (AppCompatTextView)findViewById(R.id.fragmentImageDetailByTv);
		mImageSummaryTextTv = (AppCompatTextView)findViewById(R.id.fragmentImageDetailSummaryTv);
		mImageViewTv = (AppCompatTextView)findViewById(R.id.fragmentImageDetailViewTv);
		
		mImageNextIv = (ImageView)findViewById(R.id.fragmentImageDetailNextIv);
		mImagePrevIv = (ImageView)findViewById(R.id.fragmentImageDetailPreviousIv);
		
		mImageViewPager = (ViewPager)findViewById(R.id.fragmentImageDetailViewPager);
		mImageCirclePageIndicator = (CirclePageIndicator)findViewById(R.id.fragmentImageDetailCirclePageIndicator);
		
		mImageNewsLinkTv = (AppCompatTextView)findViewById(R.id.fragmentImageDetailLinkTv);
		
		mImageNewsLinkLayout = (LinearLayout)findViewById(R.id.fragmentImageDetailViewSourceLayout);
	}

	private void initUiWithData(){
		mImageNewsLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
		initUiWithDataForWastingTime();//HDFC
	}
	
	private void initUiWithDataForWastingTime(){//HDFC
		if(getIntent().getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			isTraining = true;
			mImageTitleTv.setText(getResources().getString(R.string.sample_item_recycler_training_image_title));
			mImageSummaryTextTv.setText(getResources().getString(R.string.sample_item_recycler_training_image_desc));
		}
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
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
		setLanguageChipsLayout();
	}
	
	private void setOnClickListener(){
	}
	
	private void setImageViewPager(){
		mArrayListString = new ArrayList<String>();
		for(int i=0;i<3;i++){
			mArrayListString.add("1");
		}
		mAdapter = new ImageDetailPagerAdapter(getSupportFragmentManager(), mArrayListString, isTraining);
		mImageViewPager.setAdapter(mAdapter);
		mImageCirclePageIndicator.setViewPager(mImageViewPager);
	}
	
	private void setLanguageChipsLayout() {
		mLanguageLinearLayout.setVisibility(View.VISIBLE);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(2, 2, 2, 2);
		final String[] mLanguages = new String[] { "English", "Hindi",
				"Marathi", "Gujarati", "Bengali", "Telugu", "Kannad",
				"Punjabi", "Siddhi", "Bhojpuri" };
		for (int i = 0; i < 5; i++) {
			ChipsLayout mChip = new ChipsLayout(this);
			mChip.setDrawable(R.drawable.ic_chips_download);
			mChip.setText(mLanguages[i]);
			mChip.setLayoutParams(params);
			final int j = i;
			mChip.getChipLayout().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					 Utilities.showCrouton(ImageDetailActivity.this,
					 mCroutonViewGroup, mLanguages[j], Style.INFO);
				}
			});
			mLanguageFlowLayout.addView(mChip);
		}
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
