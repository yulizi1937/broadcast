/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
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
import android.widget.Toast;

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
public class PdfDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = PdfDetailActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private LinearLayout mLanguageLinearLayout;

	private FlowLayout mLanguageFlowLayout;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mPdfTitleTv;
	private AppCompatTextView mPdfByTv;
	private AppCompatTextView mPdfViewTv;
	private AppCompatTextView mPdfSummaryTextTv;
	private AppCompatTextView mPdfFileNameTv;
	private AppCompatTextView mPdfFileInfoTv;
	private AppCompatTextView mLanguageHeaderTv;

	private ImageView mPdfFileIv;

	private AppCompatTextView mPdfNewsLinkTv;

	private LinearLayout mPdfNewsLinkLayout;

	private RelativeLayout mPdfFileLayout;

	private boolean isShareOptionEnable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_detail);
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
			AndroidUtilities.exitWindowAnimation(PdfDetailActivity.this);
			return true;
		case R.id.action_share:
			showDialog(0);
			return true;
		case R.id.action_report:
			Intent mIntent = new Intent(PdfDetailActivity.this,
					ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					"Android:Pdf");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(PdfDetailActivity.this);
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

		mLanguageLinearLayout = (LinearLayout) findViewById(R.id.fragmentPdfDetailLanguageLayout);
		mLanguageFlowLayout = (FlowLayout) findViewById(R.id.fragmentPdfDetailLanguageFlowLayout);
		mLanguageHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailLanguageHeaderTv);

		mPdfTitleTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailTitleTv);

		mPdfByTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailByTv);
		mPdfSummaryTextTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailSummaryTv);
		mPdfViewTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailViewTv);
		mPdfFileInfoTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailFileDetailIv);
		mPdfFileNameTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailFileNameIv);

		mPdfFileIv = (ImageView) findViewById(R.id.fragmentPdfDetailImageIv);

		mPdfFileLayout = (RelativeLayout) findViewById(R.id.fragmentPdfDetailRelativeLayout);

		mPdfNewsLinkTv = (AppCompatTextView) findViewById(R.id.fragmentPdfDetailLinkTv);

		mPdfNewsLinkLayout = (LinearLayout) findViewById(R.id.fragmentPdfDetailViewSourceLayout);
	}

	private void initUiWithData() {
		mPdfNewsLinkTv.setText(Html.fromHtml(getResources().getString(
				R.string.sample_news_detail_link)));
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.PdfDetailActivityTitle));
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
		mPdfFileLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
//				copyPdfFromAssets();// HDFC
			}
		});
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private void copyPdfFromAssets() {// HDFC
		try {
			AssetManager assetManager = getAssets();

			InputStream in = null;
			OutputStream out = null;
			File file = new File(getFilesDir(), "hdfc_bank.pdf");
			try {
				in = assetManager.open("hdfc_bank.pdf");
				out = openFileOutput(file.getName(),
						Context.MODE_WORLD_READABLE);

				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.parse("file://" + getFilesDir()
							+ "/hdfc_bank.pdf"), "application/pdf");

			startActivity(intent);
		} catch (Exception e) {
		}
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
		for (int i = 0; i < 3; i++) {
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
							Utilities.showCrouton(PdfDetailActivity.this,
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
			YoYo.with(Techniques.ZoomIn).duration(500).playOn(mPdfFileLayout);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
