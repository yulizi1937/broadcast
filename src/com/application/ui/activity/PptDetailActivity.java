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
import android.widget.RelativeLayout;

import com.application.ui.view.BottomSheet;
import com.application.ui.view.ChipsLayout;
import com.application.ui.view.FlowLayout;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class PptDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = PptDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private LinearLayout mLanguageLinearLayout;

	private FlowLayout mLanguageFlowLayout;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mPptTitleTv;
	private AppCompatTextView mPptByTv;
	private AppCompatTextView mPptViewTv;
	private AppCompatTextView mPptSummaryTextTv;
	private AppCompatTextView mPptFileNameTv;
	private AppCompatTextView mPptFileInfoTv;
	private AppCompatTextView mLanguageHeaderTv;

	private AppCompatTextView mPptNewsLinkTv;

	private LinearLayout mPptNewsLinkLayout;

	private ImageView mPptFileIv;

	private RelativeLayout mPptFileLayout;

	private boolean isShareOptionEnable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ppt_detail);
		initToolBar();
		initUi();
		initUiWithData();
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
		if (isShareOptionEnable) {
			menu.findItem(R.id.action_share).setVisible(true);
		} else {
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
			AndroidUtilities.exitWindowAnimation(PptDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(PptDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Ppt");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(PptDetailActivity.this);
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

		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentPptDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentPptDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailLanguageHeaderTv);

		mPptTitleTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailTitleTv);

		mPptByTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailByTv);
		mPptSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailSummaryTv);
		mPptViewTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailViewTv);
		mPptFileInfoTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailFileDetailIv);
		mPptFileNameTv = (AppCompatTextView) findViewById(R.id.fragmentPptDetailFileNameIv);

		mPptFileIv = (ImageView) findViewById(R.id.fragmentPptDetailImageIv);

		mPptFileLayout = (RelativeLayout) findViewById(R.id.fragmentPptDetailRelativeLayout);
		
		mPptNewsLinkTv = (AppCompatTextView)findViewById(R.id.fragmentPptDetailLinkTv);
		
		mPptNewsLinkLayout = (LinearLayout)findViewById(R.id.fragmentPptDetailViewSourceLayout);
	}

	private void initUiWithData(){
		mPptNewsLinkTv.setText(Html.fromHtml(getResources().getString(R.string.sample_news_detail_link)));
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.PptDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
		setToolBarOption();
		setLanguageChipsLayout();
	}

	private void setOnClickListener() {
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
		for (int i = 0; i < 2; i++) {
			ChipsLayout mChip = new ChipsLayout(this);
			mChip.setDrawable(R.drawable.ic_chips_download);
			mChip.setText(mLanguages[i]);
			mChip.setLayoutParams(params);
			final int j = i;
			mChip.getChipLayout().setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							Utilities.showCrouton(PptDetailActivity.this,
									mCroutonViewGroup, mLanguages[j],
									Style.INFO);
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

	protected BottomSheet getShareAction() {
		return getShareActions(
				new BottomSheet.Builder(this).grid().title("Share To "),
				"Hello ").limit(R.integer.bs_initial_grid_row).build();
	}

	private void setMaterialRippleView() {
		try {
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.ZoomIn).duration(500).playOn(mPptFileLayout);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
