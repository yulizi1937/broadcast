/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.sqlite.DBConstant;
import com.application.ui.adapter.MobcastRecyclerAdapter.ImageViewHolder;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
	
	private ProgressWheel mBirthdayProfileCircleIvProgressWheel;

	private AppCompatTextView mBirthdayProfileNameTv;
	private AppCompatTextView mBirthdayProfileDateTv;
	private AppCompatTextView mBirthdayProfileSunSignTv;
	private AppCompatTextView mBirthdayProfileAgeTv;
	private AppCompatTextView mBirthdayProfileDepTv;
	
	private ImageLoader mImageLoader;
	
	private Intent mIntent;
	private String mId;
	private String mCategory;
	private String mContentName;
	private String mContentCity;
	private String mContentMobile;
	private String mContentEmail;
	private String mContentDate;
	private String mContentSunSign;
	private String mContentAge;
	private String mContentDep;
	private String mContentDOB;
	private String mContentFileLink;
	private boolean isMale = false;
	
	private boolean isFromNotification = false;
	

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
		
		mBirthdayProfileCircleIvProgressWheel = (ProgressWheel)findViewById(R.id.fragmentBirthdayProfileImageLoadingProgress);

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
		try{
			if(mIntent!=null){
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).toString();
				isFromNotification = mIntent.getBooleanExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, false);
				if(!TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mCategory)){
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.BIRTHDAY)){
						mCursor = getContentResolver().query(DBConstant.Birthday_Columns.CONTENT_URI, null, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + "=?", new String[]{mId}, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + " DESC");
						getDataFromDBForBirthday(mCursor);
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(BirthdayProfileActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(BirthdayProfileActivity.this);
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
	
	private void getDataFromDBForBirthday(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentName = mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_NAME));
			mContentCity =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_CITY));
			mContentAge =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_AGE));
			mContentDep = mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DEPARTMENT));
			mContentDOB = mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DOB));
			mContentFileLink =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_LINK));
			mContentMobile =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVER_MOBILE));
			mContentEmail =  mCursor.getString(mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVER_EMAIL));
			setIntentDataToUi();
		}
	}
	
	private void setIntentDataToUi(){
		if(!TextUtils.isEmpty(mContentName)){
			mBirthdayProfileNameTv.setText(mContentName);
		}
		
		if(!TextUtils.isEmpty(mContentDep)){
			mBirthdayProfileDepTv.setText(mContentDep);
		}
		
		if(!TextUtils.isEmpty(mContentSunSign)){
			mBirthdayProfileSunSignTv.setText(mContentSunSign);
		}
		
		if(!TextUtils.isEmpty(mContentCity)){
			
		}
		
		if(TextUtils.isEmpty(mContentAge)){
			calculateAge();
			if(TextUtils.isEmpty(mContentAge)){
				mBirthdayProfileAgeTv.setText(mContentAge + " YRS");	
			}
		}
		
		if(!TextUtils.isEmpty(mContentDOB)){
			mBirthdayProfileDateTv.setText(mContentDOB);
		}
		
		if(!TextUtils.isEmpty(mContentFileLink)){
			mImageLoader = ApplicationLoader.getUILImageLoader();
			mImageLoader.displayImage(mContentFileLink, mBirthdayProfileCircleImageView, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					mBirthdayProfileCircleIvProgressWheel.setVisibility(View.VISIBLE);
					mBirthdayProfileCircleImageView.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					mBirthdayProfileCircleIvProgressWheel.setVisibility(View.GONE);
					mBirthdayProfileCircleImageView.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
					// TODO Auto-generated method stub
					mBirthdayProfileCircleIvProgressWheel.setVisibility(View.GONE);
					mBirthdayProfileCircleImageView.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					mBirthdayProfileCircleIvProgressWheel.setVisibility(View.GONE);
					mBirthdayProfileCircleImageView.setVisibility(View.VISIBLE);
				}
			});
		}
		
		updateReadInDb();
	}
	
	private void calculateAge(){
		try{
			mContentAge = String.valueOf(Integer.parseInt(Utilities.getCurrentYear()) - Integer.parseInt(mContentDOB.substring(0, 4)));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateReadInDb(){
		ContentValues values = new ContentValues();
		values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ, "true");
		getContentResolver().update(DBConstant.Birthday_Columns.CONTENT_URI, values, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + "=?", new String[]{mId});	
		
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
