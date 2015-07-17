/**
 * 
 */
package com.application.ui.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.http.entity.mime.MinimalField;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Birthday;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.CircleImageView;
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
public class BirthdayProfileActivity extends SwipeBackBaseActivity {
	private static final String TAG = BirthdayProfileActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;

	private FrameLayout mBirthdayProfileFrameLayout;

	private CircleImageView mBirthdayProfileCircleImageView;

	private ImageView mBirthdayProfileActionCallIv;
	private ImageView mBirthdayProfileActionChatIv;
	private ImageView mBirthdayProfileActionMsgIv;
	private ImageView mBirthdayProfileActionEmailIv;

	private AppCompatTextView mBirthdayProfileNameTv;
	private AppCompatTextView mBirthdayProfileDateTv;
	private AppCompatTextView mBirthdayProfileSunSignTv;
	private AppCompatTextView mBirthdayProfileAgeTv;
	private AppCompatTextView mBirthdayProfileDepTv;
	
	private Intent mIntent;
	private ArrayList<Birthday> mArrayListBirthday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday_profile);
		initToolBar();
		initUi();
		getIntentData();
		setUiListener();
		setAnimation();
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
		inflater.inflate(R.menu.menu_profile, menu);
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
			AndroidUtilities.exitWindowAnimation(BirthdayProfileActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(BirthdayProfileActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Profile");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(BirthdayProfileActivity.this);
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

		mBirthdayProfileFrameLayout = (FrameLayout) findViewById(R.id.fragmentBirthdayProfileRootLayout);

		mBirthdayProfileCircleImageView = (CircleImageView) findViewById(R.id.fragmentBirthdayProfileImageIv);

		mBirthdayProfileNameTv = (AppCompatTextView) findViewById(R.id.fragmentBirthdayProfileNameTv);
		mBirthdayProfileDateTv = (AppCompatTextView) findViewById(R.id.fragmentBirthdayProfileDateTv);
		mBirthdayProfileSunSignTv = (AppCompatTextView) findViewById(R.id.fragmentBirthdayProfileSunSignTv);
		mBirthdayProfileAgeTv = (AppCompatTextView) findViewById(R.id.fragmentBirthdayProfileAgeTv);
		mBirthdayProfileDepTv = (AppCompatTextView) findViewById(R.id.fragmentBirthdayProfileDepartmentTv);

		mBirthdayProfileActionCallIv = (ImageView) findViewById(R.id.fragmentBirthdayProfileCallIv);
		mBirthdayProfileActionChatIv = (ImageView) findViewById(R.id.fragmentBirthdayProfileChatIv);
		mBirthdayProfileActionMsgIv = (ImageView) findViewById(R.id.fragmentBirthdayProfileMessageIv);
		mBirthdayProfileActionEmailIv = (ImageView) findViewById(R.id.fragmentBirthdayProfileEmailIv);
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		mArrayListBirthday = mIntent.getParcelableArrayListExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
		int position = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.ID, 0);
		if (position < 2) {
			position = 0;
		} else if (position < 4) {
			position = 1;
		} else {
			position = 2;
		}
		Birthday Obj = mArrayListBirthday.get(position);
		mBirthdayProfileNameTv.setText(Obj.getmBirthdayUserName());
		mBirthdayProfileDepTv.setText(Obj.getmBirthdayUserDep());
		if(Obj.getmBirthdayUserName().equalsIgnoreCase("Ashwin Roy")){
			mBirthdayProfileAgeTv.setText("28");
			mBirthdayProfileDateTv.setText("28 August");
		}else if(Obj.getmBirthdayUserName().equalsIgnoreCase("Vikalp Patel")){
			mBirthdayProfileAgeTv.setText("24");
			mBirthdayProfileDateTv.setText("08 October");
		}else {
			mBirthdayProfileAgeTv.setText("26");
			mBirthdayProfileDateTv.setText("10 December");
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
				R.string.BirthdayProfileActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setAnimation() {
		try {
			YoYo.with(Techniques.ZoomIn).duration(1000)
					.playOn(mBirthdayProfileCircleImageView);
			YoYo.with(Techniques.BounceInUp).delay(1000).duration(500)
					.playOn(mBirthdayProfileActionChatIv);
			YoYo.with(Techniques.BounceInUp).delay(1500).duration(500)
					.playOn(mBirthdayProfileActionCallIv);
			YoYo.with(Techniques.BounceInUp).delay(2000).duration(500)
					.playOn(mBirthdayProfileActionMsgIv);
			YoYo.with(Techniques.BounceInUp).delay(2500).duration(500)
					.playOn(mBirthdayProfileActionEmailIv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}

	private void setOnClickListener() {
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
			setMaterialRippleOnView(mBirthdayProfileFrameLayout);
			setMaterialRippleOnView(mBirthdayProfileCircleImageView);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
