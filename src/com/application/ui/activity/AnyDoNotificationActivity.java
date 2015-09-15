/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.sqlite.DBConstant;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.BottomSheetAnyDo;
import com.application.ui.view.CircleImageView;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.RoundedBackgroundSpan;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FetchActionAsyncTask;
import com.application.utils.FetchActionAsyncTask.OnPostExecuteTaskListener;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.NotificationHandle;
import com.application.utils.NotificationsController;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AnyDoNotificationActivity extends AppCompatActivity {

	private static final String TAG = AnyDoNotificationActivity.class
			.getSimpleName();

	private BottomSheet mBottomSheet;
	private BottomSheetAnyDo mBottomSheetAnyDo;
	private SystemBarTintManager mTintManager;
	
	private TextView mTextViewTv;
	private TextView mTextCloseTv;

	private int mId;
	private int mType;
	private String mCategory;
	private int mLayoutId;
	private Intent mIntent;
	
	private View mContentLayout;
	private FrameLayout mActivityLayout;
	
	private Handler mHandler = new Handler();
	private int delayInStartMillis = 10000;
	private int delayInBtnMillis = 30000;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anydo_notification);
		setSecurity();
		mActivityLayout = (FrameLayout)findViewById(R.id.activityAnyDoNotificationActivityLayout);
		getIntentData();
	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		try {
			if (AndroidUtilities.isAppLanguageIsEnglish()) {
				super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
			} else {
				super.attachBaseContext(newBase);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void setUpAnyDoNotificationWithBottomSheet() {
		showDialog(1);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		mId = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.ID, -1);
		mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
		mType = mIntent.getIntExtra(AppConstants.INTENTCONSTANTS.TYPE, -1);
		mLayoutId = getLayoutFromType(mType);
		setUpAnyDoNotificationWithBottomSheet();
		processAnyDoNotification();
		setSystemBarTint(0);
		setUiListener();
		updateReportToApi();
		refreshUserActivityWithApi();
	}
	
	private int getLayoutFromType(int mType) {
		switch (mType) {
		case AppConstants.TYPE.TEXT:
			return R.layout.item_recycler_mobcast_text;
		case AppConstants.TYPE.AUDIO:
			return R.layout.item_recycler_mobcast_audio;
		case AppConstants.TYPE.VIDEO:
			return R.layout.item_recycler_mobcast_video;
		case AppConstants.TYPE.PDF:
			return R.layout.item_recycler_mobcast_pdf;
		case AppConstants.TYPE.XLS:
			return R.layout.item_recycler_mobcast_xls;
		case AppConstants.TYPE.PPT:
			return R.layout.item_recycler_mobcast_ppt;
		case AppConstants.TYPE.DOC:
			return R.layout.item_recycler_mobcast_doc;
		case AppConstants.TYPE.FEEDBACK:
			return R.layout.item_recycler_mobcast_feedback;
		case AppConstants.TYPE.QUIZ:
			return R.layout.item_recycler_training_quiz;
		case AppConstants.TYPE.IMAGE:
			return R.layout.item_recycler_mobcast_image;
		case AppConstants.TYPE.STREAM:
			return R.layout.item_recycler_mobcast_video;
		case AppConstants.TYPE.EVENT:
			return R.layout.item_recycler_event;
		case AppConstants.TYPE.AWARD:
			return R.layout.item_recycler_award;
		default:
			return R.layout.item_recycler_mobcast_text;
		}
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	protected Dialog onCreateDialog(final int position) {
		if (position == 0) {
			mBottomSheet = new BottomSheet.Builder(this)
					.icon(getRoundedBitmap(R.drawable.ic_launcher))
					.title(getSpannableStringWithCounter(
							"Notifications            ", "6 ", " New"))
					.sheet(R.menu.menu_anydo).limit(R.integer.anydo_limit)
					.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
//							onBackPressed();
							finish();
						}
					}).listener(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).build();
			final Menu menu = mBottomSheet.getMenu();
			menu.getItem(0).setVisible(false);
			for (int i = 0; i < 10; i++) {
				if (i % 3 == 0) {
					menu.add(Menu.NONE, i, Menu.NONE, "PDF Notification");
					menu.findItem(i).setIcon(R.drawable.ic_item_pdf);
				} else if (i % 5 == 0) {
					menu.add(Menu.NONE, i, Menu.NONE, "Video Notification");
					menu.findItem(i).setIcon(R.drawable.ic_mobcast_video);
				} else if (i % 7 == 0) {
					menu.add(Menu.NONE, i, Menu.NONE, "Audio Notification");
					menu.findItem(i).setIcon(R.drawable.ic_mobcast_audio);
				} else {
					menu.add(Menu.NONE, i, Menu.NONE, "Text Notification");
					menu.findItem(i).setIcon(R.drawable.ic_mobcast_text);
				}
			}
			return mBottomSheet;
		}else{
			mBottomSheetAnyDo = new BottomSheetAnyDo.Builder(this, mId,
					mCategory, mLayoutId)
			.icon(getRoundedBitmap(R.drawable.ic_launcher))
			.title(getResources().getString(R.string.app_name))
			.sheet(R.menu.menu_anydo).limit(R.integer.anydo_limit)
			.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
//					onBackPressed();
					finish();
				}
			}).listener(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).build();
			
			mBottomSheetAnyDo.setCancelable(true);
			mBottomSheetAnyDo.setCanceledOnSwipeDown(true);
			mBottomSheetAnyDo.setCanceledOnTouchOutside(true);
			mBottomSheetAnyDo.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					FileLog.e(TAG, "Cancelled");
					removeRepeatableRunnable();
					finish();
				}
			});
			return mBottomSheetAnyDo;
		}
	}
	
	private void setUiListener(){
		setOnClickListener();
		setMaterialRippleView();
	}
	
	private void updateReportToApi(){
		UserReport.updateUserReportApi(String.valueOf(mId), mCategory, AppConstants.REPORT.ANYDO, "");
	}
	
	private void refreshUserActivityWithApi(){
		try{
			if(mType == AppConstants.TYPE.TEXT || mType == AppConstants.TYPE.IMAGE
					|| mType == AppConstants.TYPE.AUDIO || mType == AppConstants.TYPE.VIDEO
					|| mType == AppConstants.TYPE.PDF   || mType == AppConstants.TYPE.PPT
					|| mType == AppConstants.TYPE.XLS   || mType == AppConstants.TYPE.DOC){
					
					mHandler.postDelayed(repeatableRunnable, delayInStartMillis);	
				}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private Runnable repeatableRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				syncUserActivityDataWithApi();
				mHandler.postDelayed(repeatableRunnable, delayInBtnMillis);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	};
	
	private void syncUserActivityDataWithApi(){
		FetchActionAsyncTask mFetchActionAsyncTask = new FetchActionAsyncTask(AnyDoNotificationActivity.this, String.valueOf(mId), mCategory, TAG);
		mFetchActionAsyncTask.execute();
		mFetchActionAsyncTask.setOnPostExecuteListener(new OnPostExecuteTaskListener() {
			@Override
			public void onPostExecute(String mViewCount, String mLikeCount) {
				// TODO Auto-generated method stub
				updateUserActivityToDBAndUi(mViewCount, mLikeCount);
			}
		});
	}
	
	private void updateUserActivityToDBAndUi(String mViewCount, String mLikeCount){
		try{
			if(mContentLayout==null){
				mContentLayout = mBottomSheetAnyDo.getContentView();
			}
			updateUserActivityToDB(mLikeCount, mViewCount);
			switch(mType){
			case AppConstants.TYPE.TEXT:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.IMAGE:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.AUDIO:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.VIDEO:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.PDF:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.DOC:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.PPT:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptViewCountTv)).setText(mViewCount);
				break;
			case AppConstants.TYPE.XLS:
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsLikeCountTv)).setText(mLikeCount);
				((AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsViewCountTv)).setText(mViewCount);
				break;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateUserActivityToDB(String mLikeCount , String mViewCount){
		try{
			ContentValues mValues = new ContentValues();
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO, mLikeCount);
				mValues.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT, mViewCount);
				getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, mValues, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)});
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				mValues.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mLikeCount);
				mValues.put(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT, mViewCount);
				getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, mValues, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void removeRepeatableRunnable(){
		try{
			mHandler.removeCallbacks(repeatableRunnable);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	private void setMaterialRippleView(){
		try{
			setMaterialRippleOnView(mTextCloseTv);
			setMaterialRippleOnView(mTextViewTv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setMaterialRippleOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#FFFFFF"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}
	
	private void setOnClickListener(){
		mTextViewTv = mBottomSheetAnyDo.getTextViewTv();
		mTextCloseTv = mBottomSheetAnyDo.getTextCloseTv();
		
		if (mTextViewTv != null) {
			mTextViewTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					removeRepeatableRunnable();
					Intent mIntent = new NotificationHandle(ApplicationLoader.getApplication().getApplicationContext(), mId, mCategory, mType).getIntent();
					startActivity(mIntent);
					NotificationsController.getInstance().dismissNotification();
					mBottomSheetAnyDo.dismiss();
				}
			});
		}
		
		if(mContentLayout!=null){
			mContentLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					removeRepeatableRunnable();
					Intent mIntent = new NotificationHandle(ApplicationLoader.getApplication().getApplicationContext(), mId, mCategory, mType).getIntent();
					startActivity(mIntent);
					NotificationsController.getInstance().dismissNotification();
					mBottomSheetAnyDo.dismiss();
				}
			});
		}

		if (mTextCloseTv != null) {
			mTextCloseTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					removeRepeatableRunnable();
					if(mBottomSheetAnyDo!=null)
						mBottomSheetAnyDo.dismiss();
					finish();
				}
			});
		}
		
		mActivityLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				removeRepeatableRunnable();
				finish();
			}
		});
	}

	private Drawable getRoundedBitmap(int imageId) {
		Bitmap src = BitmapFactory.decodeResource(getResources(), imageId);
		Bitmap dst;
		if (src.getWidth() >= src.getHeight()) {
			dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight()
					/ 2, 0, src.getHeight(), src.getHeight());
		} else {
			dst = Bitmap.createBitmap(src, 0,
					src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(),
					src.getWidth());
		}
		RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory
				.create(getResources(), dst);
		roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
		roundedBitmapDrawable.setAntiAlias(true);
		return roundedBitmapDrawable;
	}

	public SpannableStringBuilder getSpannableStringWithCounter(String mHeader,
			String mNotification, String mTail) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		stringBuilder.append(mHeader);
		stringBuilder.append(mNotification);
		stringBuilder.setSpan(new ForegroundColorSpan(
				R.color.toolbar_background), 0, mHeader.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		stringBuilder.setSpan(new RoundedBackgroundSpan(),
				mHeader.length() - 1,
				mHeader.length() + mNotification.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		stringBuilder.append(mTail);
		return stringBuilder;
	}

	@SuppressLint("NewApi")
	private void setSystemBarTint(int position) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(getResources().getColor(
						android.R.color.transparent));
			} else {
				mTintManager = new SystemBarTintManager(
						AnyDoNotificationActivity.this);
				// enable status bar tint
				mTintManager.setStatusBarTintEnabled(true);
				mTintManager.setStatusBarTintColor(getResources().getColor(
						android.R.color.transparent));
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void processAnyDoNotification(){
		mContentLayout = mBottomSheetAnyDo.getContentView();
		switch(mType){
		case AppConstants.TYPE.TEXT:
			processNotificationText();
			break;
		case AppConstants.TYPE.STREAM:
			processNotificationStream();
			break;
		case AppConstants.TYPE.VIDEO:
			processNotificationVideo();
			break;
		case AppConstants.TYPE.AUDIO:
			processNotificationAudio();
			break;
		case AppConstants.TYPE.IMAGE:
			processNotificationImage();
			break;
		case AppConstants.TYPE.PDF:
			processNotificationPdf();
			break;
		case AppConstants.TYPE.XLS:
			processNotificationXls();
			break;
		case AppConstants.TYPE.PPT:
			processNotificationPpt();
			break;
		case AppConstants.TYPE.DOC:
			processNotificationDoc();
			break;
		case AppConstants.TYPE.FEEDBACK:
			processNotificationFeedback();
			break;
		case AppConstants.TYPE.QUIZ:
			processNotificationQuiz();
			break;
		case AppConstants.TYPE.EVENT:
			processNotificationEvent();
			break;
		case AppConstants.TYPE.AWARD:
			processNotificationAward();
			break;
		}
	}
	
	private void processNotificationText(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextByTv);
		AppCompatTextView mSummaryTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextSummaryTv);
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastTextViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastTextLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastTextReadView).setVisibility(View.GONE);
		
		Cursor mCursor = null;
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mSummaryTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
				mSummaryTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
			}
		}
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	@SuppressWarnings("deprecation")
	private void processNotificationVideo(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoByTv);
		final ImageView mCoverImageView = (ImageView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoThumbnailImageIv);
		final ImageView mPlayImageView = (ImageView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoPlayImageIv);
		final ProgressWheel mProgressWheel = (ProgressWheel)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoLoadingProgress);
		AppCompatTextView mDurationTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoDurationTv);
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoLikeCountTv);
		AppCompatTextView mViewCountTv  = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoReadView).setVisibility(View.GONE);
		
		ImageLoader mImageLoader = ApplicationLoader.getUILImageLoader();
		
		String mContentFileThumbPath = null;
		String mContentFileThumbLink = null;
		Cursor mCursor =null;
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			mDurationTv.setVisibility(View.GONE);
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mContentFileThumbPath = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH));
						mContentFileThumbLink = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
			}
			
			mDurationTv.setVisibility(View.GONE);
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
						mContentFileThumbPath = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH));
						mContentFileThumbLink = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}
		
		try {
			final String mThumbnailPath = mContentFileThumbPath;
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				mCoverImageView.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mContentFileThumbLink, mCoverImageView, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.VISIBLE);
				        mCoverImageView.setVisibility(View.GONE);
				        mPlayImageView.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mCoverImageView.setVisibility(View.VISIBLE);
						mPlayImageView.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mCoverImageView.setVisibility(View.VISIBLE);
						mPlayImageView.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mCoverImageView.setVisibility(View.VISIBLE);
						mPlayImageView.setVisibility(View.VISIBLE);
					}
				});
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	@SuppressWarnings("deprecation")
	private void processNotificationImage(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageByTv);
		final ImageView mCoverImageView = (ImageView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageMainImageIv);
		final ProgressWheel mProgressWheel = (ProgressWheel)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageLoadingProgress);
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastImageViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastImageLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastImageReadView).setVisibility(View.GONE);
		
		ImageLoader mImageLoader = ApplicationLoader.getUILImageLoader();
		
		String mContentFileThumbPath = null;
		String mContentFileThumbLink = null;
		Cursor mCursor = null;
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
//				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						String mThumbPath = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_PATH));
						String mThumbLink = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_THUMBNAIL_LINK));
						
						if(!TextUtils.isEmpty(mThumbLink)){
							mContentFileThumbLink = mThumbLink;	
						}
						
						if(!TextUtils.isEmpty(mThumbPath)){
							mContentFileThumbPath = mThumbPath;	
						}
					}
//				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
						mContentFileThumbPath = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_PATH));
						mContentFileThumbLink = mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_THUMBNAIL_LINK));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}	
		}
		
		try {
			mCoverImageView.setImageURI(Uri.parse(mContentFileThumbPath));
			
			final String mThumbnailPath = mContentFileThumbPath;
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				mCoverImageView.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mContentFileThumbLink, mCoverImageView, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.VISIBLE);
						mCoverImageView.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mCoverImageView.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mCoverImageView.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mCoverImageView.setVisibility(View.VISIBLE);
					}
				});
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationAudio(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioByTv);
		AppCompatTextView mDescTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioSummaryTv);
		AppCompatTextView mNameTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioDetailFileInfoNameTv);
		
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastAudioReadView).setVisibility(View.GONE);
		
		Cursor mCursor = null;
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mDescTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC)));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
				mDescTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC)));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}
		
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationPdf(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfByTv);
		AppCompatTextView mNameTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfDetailFileInfoNameTv);
		
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastPdfReadView).setVisibility(View.GONE);
		
		Cursor mCursor = null;
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}
		
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	
	private void processNotificationPpt(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptByTv);
		AppCompatTextView mNameTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptDetailFileInfoNameTv);
		
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastPptViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastPptLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastPptReadView).setVisibility(View.GONE);
		
		Cursor mCursor = null;
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			 mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			 mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
					mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
					mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
					mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
				}
				
				Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
				
				if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
					mCursorFileInfo.moveToFirst();
					do{
						if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
							mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME)));
//							mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION)));
						}
					}while(mCursorFileInfo.moveToNext());
				}
				
				if(mCursorFileInfo!=null){
					mCursorFileInfo.close();
				}
		}
		
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationDoc(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocByTv);
		AppCompatTextView mNameTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocDetailFileInfoNameTv);
		
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastDocViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastDocLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastDocReadView).setVisibility(View.GONE);
		
		Cursor mCursor = null;
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.CATEGORY)){
			 mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			 mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
					mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
					mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO)));
					mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT)));
				}
				
				Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
				
				if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
					mCursorFileInfo.moveToFirst();
					do{
						if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
							mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME)));
//							mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION)));
						}
					}while(mCursorFileInfo.moveToNext());
				}
				
				if(mCursorFileInfo!=null){
					mCursorFileInfo.close();
				}
		}
			
		
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationXls(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsByTv);
		AppCompatTextView mNameTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsDetailFileInfoNameTv);
		
		AppCompatTextView mLikeCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsLikeCountTv);
		AppCompatTextView mViewCountTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsViewCountTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastXlsReadView).setVisibility(View.GONE);
		
		Cursor mCursor = null;
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			 mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
				mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
				mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
			}
			
			Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Mobcast_File_Columns.COLUMN_ID + " ASC");
			
			if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
				mCursorFileInfo.moveToFirst();
				do{
					if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_IS_DEFAULT)))){
						mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_NAME)));
//						mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_DURATION)));
					}
				}while(mCursorFileInfo.moveToNext());
			}
			
			if(mCursorFileInfo!=null){
				mCursorFileInfo.close();
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			 mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
					mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
					mLikeCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO)));
					mViewCountTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT)));
				}
				
				Cursor mCursorFileInfo = getContentResolver().query(DBConstant.Training_File_Columns.CONTENT_URI, null, DBConstant.Training_File_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, DBConstant.Training_File_Columns.COLUMN_ID + " ASC");
				
				if(mCursorFileInfo!=null && mCursorFileInfo.getCount() > 0){
					mCursorFileInfo.moveToFirst();
					do{
						if(Boolean.parseBoolean(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_IS_DEFAULT)))){
							mNameTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_NAME)));
//							mDetailTv.setText(mCursorFileInfo.getString(mCursorFileInfo.getColumnIndex(DBConstant.Training_File_Columns.COLUMN_TRAINING_FILE_DURATION)));
						}
					}while(mCursorFileInfo.moveToNext());
				}
				
				if(mCursorFileInfo!=null){
					mCursorFileInfo.close();
				}
		}
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationStream(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoByTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoLikeCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoViewCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoLinkTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoReadView).setVisibility(View.GONE);
		AppCompatTextView mDurationTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastVideoDurationTv);
		
		Cursor mCursor = null;
		
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			 mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
				mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
			}
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			 mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					mCursor.moveToFirst();
					mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
					mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
				}
		}
		mDurationTv.setText("LIVE");
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	
	private void processNotificationFeedback(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackByTv);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackLikeCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackViewCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackReadView).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackLinkTv).setVisibility(View.GONE);
		AppCompatTextView mQuestionTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerMobcastFeedbackDetailQuestionTv);
		
		Cursor mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE)));
			mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME))));
		}
		
		Cursor mCursorFeedback = getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursorFeedback!=null && mCursorFeedback.getCount() > 0){
			mQuestionTv.setText(mCursorFeedback.getCount() + " " +getResources().getString(R.string.item_recycler_mobcast_feedback_question));	
		}else{
			mQuestionTv.setVisibility(View.GONE);
		}
		
		if(mCursorFeedback!=null)
			mCursorFeedback.close();
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationQuiz(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizByTv);
		mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizLikeCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizViewCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizReadView).setVisibility(View.GONE);
		AppCompatTextView mQuestionTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizDetailQuestionTv);
		AppCompatTextView mTimeTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizDetailTimeTv);
		AppCompatTextView mTotalPointsTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerTrainingQuizDetailPointsTv);
		
		Cursor mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE)));
			mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME))));
		}
		
		int mTotalPoints = 0;
		String []mTime;
		Cursor mCursorQuiz = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursorQuiz!=null && mCursorQuiz.getCount() > 0){
			mQuestionTv.setText(mCursorQuiz.getCount() + " " +getResources().getString(R.string.item_recycler_mobcast_feedback_question));	
			mCursorQuiz.moveToFirst();
			mTime = Utilities.convertTimeFromSecsTo(Long.parseLong(mCursorQuiz.getString(mCursorQuiz.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION)))).split(" ");
			mTimeTv.setText(mTime[0]+" "+mTime[1]);
			do{
				mTotalPoints+=Integer.parseInt(mCursorQuiz.getString(mCursorQuiz.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS)));
			}while(mCursorQuiz.moveToNext());
			
			mTotalPointsTv.setText(String.valueOf(mTotalPoints));
		}else{
			mQuestionTv.setVisibility(View.GONE);
		}
		
		if(mCursorQuiz!=null)
			mCursorQuiz.close();
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	private void processNotificationEvent(){
		AppCompatTextView mTitleTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerEventTitleTv);
		AppCompatTextView mByTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerEventByTv);
		mContentLayout.findViewById(R.id.itemRecyclerEventLikeCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerEventViewCountTv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerEventReadView).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerEventDetailIsGoingLayout).setVisibility(View.GONE);
		AppCompatTextView mDaysLeftTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerEventDetailDaysLeftTv);
		AppCompatTextView mAttendTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerEventDetailAttendanceTv);
		
		
		Cursor mCursor = getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mTitleTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_TITLE)));
			mByTv.setText(Utilities.formatBy(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_BY)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_RECEIVED_TIME))));
			mDaysLeftTv.setText(Utilities.formatDaysLeft(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_DATE)), mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_START_TIME))));
			mAttendTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Event_Columns.COLUMN_EVENT_GOING_NO)) + " "+ ApplicationLoader.getApplication().getResources().getString(R.string.going));
		}
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	
	private void processNotificationAward(){
		AppCompatTextView mReceiverNameTv = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerAwardWinnerName);
		AppCompatTextView mAwardName = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerAwardName);
		final CircleImageView mImageView = (CircleImageView)mContentLayout.findViewById(R.id.itemRecyclerAwardIv);
		final ProgressWheel mProgressWheel = (ProgressWheel)mContentLayout.findViewById(R.id.itemRecyclerAwardImageLoadingProgress);
		mContentLayout.findViewById(R.id.itemRecyclerCongratulateIv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerAwardMessageIv).setVisibility(View.GONE);
		mContentLayout.findViewById(R.id.itemRecyclerReadView).setVisibility(View.GONE);
		AppCompatTextView mCongratulatedCount = (AppCompatTextView)mContentLayout.findViewById(R.id.itemRecyclerAwardCongratulatedTv);
		String mContentFileThumbPath = null;
		String mContentFileThumbLink = null;
		
		ImageLoader mImageLoader = ApplicationLoader.getUILImageLoader();
		
		Cursor mCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, null, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?", new String[]{String.valueOf(mId)}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mReceiverNameTv.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME)));
			mAwardName.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION)));
			mCongratulatedCount.setText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO)) + " " + getResources().getString(R.string.congratulated_text));
			mContentFileThumbPath = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH));
			mContentFileThumbLink = mCursor.getString(mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK));
		}
		
		try {
			mImageView.setImageURI(Uri.parse(mContentFileThumbPath));
			
			final String mThumbnailPath = mContentFileThumbPath;
			if(Utilities.checkIfFileExists(mThumbnailPath)){
				mImageView.setImageURI(Uri.parse(mThumbnailPath));
			}else{
				mImageLoader.displayImage(mContentFileThumbLink, mImageView, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.VISIBLE);
						mImageView.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mImageView.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap mBitmap) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mImageView.setVisibility(View.VISIBLE);
						Utilities.writeBitmapToSDCard(mBitmap, mThumbnailPath);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						mProgressWheel.setVisibility(View.GONE);
						mImageView.setVisibility(View.VISIBLE);
					}
				});
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		
		if(mCursor!=null)
			mCursor.close();
	}
	
	/**
	 * Security : Couldn't capture ScreenShot
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void setSecurity() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (!BuildVars.DEBUG_SCREENSHOT) {
				getWindow().setFlags(LayoutParams.FLAG_SECURE,
						LayoutParams.FLAG_SECURE);
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
