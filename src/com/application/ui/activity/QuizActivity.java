/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.QuizPagerInfo;
import com.application.beans.QuizScorePagerInfo;
import com.application.beans.TrainingQuizSubmit;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.QuizViewPagerAdapter;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ProgressTimerWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.NotificationsController;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class QuizActivity extends BaseActivity {
	private static final String TAG = QuizActivity.class.getSimpleName();
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private ImageView mQuizCoverIv;

	private ProgressTimerWheel mQuizProgressTimerWheel;

	private AppCompatTextView mQuizProgressTimerNumberTv;
	private AppCompatTextView mQuizProgressTimerTextTv;

	private AppCompatTextView mQuizQuestionHeaderTv;
	private AppCompatTextView mQuizQuestionPagerCounterTv;
	
	private View mQuizLineView;

	private AppCompatTextView mQuizDescriptionTv;
	
	private AppCompatButton mQuizNavigationNextBtn;
	private AppCompatButton mQuizNavigationPrevBtn;

	private CirclePageIndicator mQuestionCirclePageIndicator;

	private ViewPager mQuestionViewPager;

	private QuizViewPagerAdapter mAdapter;

	private ViewPager.OnPageChangeListener mPagerListener;

	private int mTimerProgress = 0;
	private boolean isTimerRunning;
	private int mTimerProgressMax = 360;

	private Handler mHandler;

	private ArrayList<QuizPagerInfo> mArrayListQuizPagerInfo;
	private ArrayList<QuizScorePagerInfo> mArrayListQuizScorePagerInfo;

	private boolean isCirclePagerIndicatorEnable = false;
	
	private boolean isContentLiked = false;
	
	private Intent mIntent;
	private String mId;
	private String mCategory;
	private String mContentTitle;
	private String mContentDesc;
	private String mContentLikeCount;
	private String mContentViewCount;
	private String mContentBy;
	private String mContentDate;
	private String mContentTime;
	private boolean mContentIsSharing;
	private boolean mContentIsLike;
	private boolean mContentIsRead;
	
	private int mQuizScore = 0;
	private int mQuizTimeTaken = 0;
	private int mQuizTotalPoints = 0;
	
	private boolean isFromNotification = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		setSecurity();
		initToolBar();
		initUi();
		setUiListener();
		getIntentData();
		applyTheme();
		NotificationsController.getInstance().dismissNotification();
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
			if (isContentLiked) {
				menu.findItem(R.id.action_like).setIcon(R.drawable.ic_liked);
			} else {
				menu.findItem(R.id.action_like).setIcon(R.drawable.ic_like);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return super.onPrepareOptionsPanel(view, menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(QuizActivity.this);
			if(isFromNotification){
				Intent mIntent = new Intent(QuizActivity.this, MotherActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, isFromTraining(mCategory));
				startActivity(mIntent);
			}
			return true;
		case R.id.action_like:
			actionLikeQuiz();
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(QuizActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Quiz");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(QuizActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(isFromNotification){
			Intent mIntent = new Intent(QuizActivity.this, MotherActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, isFromTraining(mCategory));
			startActivity(mIntent);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mQuizCoverIv = (ImageView) findViewById(R.id.fragmentQuizTimerCoverIv);

		mQuizProgressTimerNumberTv = (AppCompatTextView) findViewById(R.id.fragmentQuizTimerSecondTv);
		mQuizProgressTimerTextTv = (AppCompatTextView) findViewById(R.id.fragmentQuizTimerSecondTextTv);
		mQuizQuestionHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentQuizQuestionNumberTextTv);
		mQuizQuestionPagerCounterTv = (AppCompatTextView) findViewById(R.id.fragmentQuizQuestionPageCountTv);
		
		mQuizDescriptionTv = (AppCompatTextView)findViewById(R.id.fragmentQuizDescriptionTv);

		mQuizProgressTimerWheel = (ProgressTimerWheel) findViewById(R.id.fragmentQuizTimerProgressWheel);

		mQuizNavigationNextBtn = (AppCompatButton) findViewById(R.id.fragmentQuizNextBtn);
		mQuizNavigationPrevBtn = (AppCompatButton) findViewById(R.id.fragmentQuizPreviousBtn);
		
		mQuizLineView = (View)findViewById(R.id.fragmentQuizQuestionLineView);

		mQuestionViewPager = (ViewPager) findViewById(R.id.fragmentQuizQuestionViewPager);

		mQuestionCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentQuizQuestionCirclePageIndicator);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(R.string.QuizActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}
	
	private void applyTheme() {
		try {
			ThemeUtils.getInstance(QuizActivity.this).applyThemeFeedback(
					QuizActivity.this, QuizActivity.this, mToolBar,
					mQuizQuestionHeaderTv, mQuizLineView);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
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
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
						mCursor = getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId}, DBConstant.Training_Columns.COLUMN_TRAINING_ID + " DESC");
						getDataFromDBForTraining(mCursor);
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(QuizActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(QuizActivity.this);
		}
	}
	
	private void getDataFromDBForTraining(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_VIEWCOUNT));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ)));
			
			Cursor mCursorQuiz = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID+ "=?", new String[]{mId}, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + " ASC");
			if(mCursorQuiz!=null && mCursorQuiz.getCount() > 0){
				mCursorQuiz.moveToFirst();
				mArrayListQuizPagerInfo = new ArrayList<>();
				mTimerProgressMax = Integer.parseInt(mCursorQuiz.getString(mCursorQuiz.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_DURATION)));
				do {
					QuizPagerInfo mObj = new QuizPagerInfo();
					mObj.setmQuizId(mId);
					mObj.setmQuizQId(mCursorQuiz.getString(mCursorQuiz.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID)));
					mObj.setmQuizType(mCursorQuiz.getString(mCursorQuiz.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE)));
					mArrayListQuizPagerInfo.add(mObj);
				} while (mCursorQuiz.moveToNext());
				
			}
			if(mCursorQuiz!=null)
				mCursorQuiz.close();
			clearAnswerFromDB();
			setIntentDataToUi();
		}
	}
	
	private void clearAnswerFromDB(){
		ContentValues values = new ContentValues();
		values.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER, "");
		for(int i = 0 ; i< mArrayListQuizPagerInfo.size() ;i++){
			getContentResolver().update(DBConstant.Training_Quiz_Columns.CONTENT_URI, values, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" + " AND "+ DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?",new String[]{mArrayListQuizPagerInfo.get(i).getmQuizId(), mArrayListQuizPagerInfo.get(i).getmQuizQId()});
		}
	}
	
	private void setIntentDataToUi(){
		try{
//			mToolBar.setTitle(mContentTitle);
//			mToolBar.setSubtitle(mContentDesc);
			
			if(!TextUtils.isEmpty(mContentDesc)){
				mQuizDescriptionTv.setText(mContentDesc);
			}
			
			setQuizViewPager();
			setQuizTimerStart();
//			updateReadInDb();
			if(!mContentIsRead){
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
			}
			if(mContentIsLike){
				isContentLiked = true;
			}
			supportInvalidateOptionsMenu();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void actionLikeQuiz(){
		try{
			if(!mContentIsLike){
				mContentLikeCount  = String.valueOf(Integer.parseInt(mContentLikeCount)+1);
				if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
					ContentValues values = new ContentValues();
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, "true");
					values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mContentLikeCount);
					getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
				}
				isContentLiked = true;
				mContentIsLike = true;
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.LIKE, "");
			}else{
				if(isContentLiked){
					mContentLikeCount = String.valueOf(Integer.parseInt(mContentLikeCount)-1);
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
						ContentValues values = new ContentValues();
						values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_LIKE, "false");
						values.put(DBConstant.Training_Columns.COLUMN_TRAINING_LIKE_NO, mContentLikeCount);
						getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
					}
					isContentLiked = false;
					mContentIsLike =false;
					UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.UNLIKE, "");
				}
			}
			supportInvalidateOptionsMenu();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void updateReadInDb(){
		ContentValues values = new ContentValues();
		 if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, "true");
			getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
		}
	}
	
	private void updateAttemptCountInDb(){
		try{
			ContentValues values = new ContentValues();
			 if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				Cursor mCursor = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?", new String[]{mId}, null);
				if(mCursor!=null && mCursor.getCount() > 0){
					int mAttemptedCount = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT_COUNT)));
					values.put(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ATTEMPT_COUNT, String.valueOf(mAttemptedCount+=1));
					getContentResolver().update(DBConstant.Training_Quiz_Columns.CONTENT_URI, values, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?", new String[]{mId});
				}
				
				if(mCursor!=null){
					mCursor.close();
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setQuizTimerStart() {
		mHandler = new Handler();
		final Runnable mQuizTimerTextRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mQuizProgressTimerNumberTv.setText(String
						.valueOf(mTimerProgressMax - mTimerProgress));
			}
		};

		final Runnable mQuizTimerRunnable = new Runnable() {
			public void run() {
				isTimerRunning = true;
				while (mTimerProgress <= mTimerProgressMax) {
					mQuizProgressTimerWheel.setProgress(getTimerProgress());
					mHandler.post(mQuizTimerTextRunnable);
					mTimerProgress++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(mTimerProgress == mTimerProgressMax){
						AndroidUtilities.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								showQuizTimesUpDialog();	
							}
						});
					}
				}
				isTimerRunning = false;
			}
		};

		Thread mQuizTimerThread = new Thread(mQuizTimerRunnable);
		mQuizTimerThread.start();
	}

	private int getTimerProgress() {
		return Math.round((mTimerProgress / (float) mTimerProgressMax) * 100
				* (360 / (float) 100));
	}

	private void setQuizViewPager() {
		try{
			if(mArrayListQuizPagerInfo!=null && mArrayListQuizPagerInfo.size()>0){
				if (mArrayListQuizPagerInfo.size() < 7) {
					isCirclePagerIndicatorEnable = true;
				}
				mAdapter = new QuizViewPagerAdapter(getSupportFragmentManager(),
						mArrayListQuizPagerInfo);
				setViewPagerListener();
				mQuestionViewPager.setAdapter(mAdapter);
				if (isCirclePagerIndicatorEnable) {
					mQuestionCirclePageIndicator.setViewPager(mQuestionViewPager);
					mQuestionCirclePageIndicator
							.setOnPageChangeListener(mPagerListener);
				} else {
					mQuizQuestionPagerCounterTv.setVisibility(View.VISIBLE);
					mQuestionCirclePageIndicator.setVisibility(View.GONE);
					mQuestionViewPager.setOnPageChangeListener(mPagerListener);
					mQuizQuestionPagerCounterTv.setText(1 + " / "
							+ mArrayListQuizPagerInfo.size());
				}
				
				if (mArrayListQuizPagerInfo.size() == 1) {
					mQuizNavigationPrevBtn.setVisibility(View.INVISIBLE);
					mQuizNavigationNextBtn.setText(getResources().getString(R.string.button_submit));
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setViewPagerListener() {
		try{
			mPagerListener = new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					int mNumberQuestion = position + 1;
					uiOnChangeOfPagerListener(mNumberQuestion);
				}

				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int state) {
					// TODO Auto-generated method stub

				}
			};
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void uiOnChangeOfPagerListener(int mNumberQuestion) {
		try{
			mQuizQuestionHeaderTv.setText(getResources().getString(
					R.string.sample_question_header_box)
					+ " " + mNumberQuestion);
			if (!isCirclePagerIndicatorEnable) {
				mQuizQuestionPagerCounterTv.setText(mNumberQuestion + " / "
						+ mArrayListQuizPagerInfo.size());
			}

			if (mNumberQuestion == mArrayListQuizPagerInfo.size()) {
				mQuizNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			} else {
				mQuizNavigationNextBtn.setText(getResources().getString(
						R.string.button_next));
			}
			
			if (mNumberQuestion == 1) {
				mQuizNavigationPrevBtn.setEnabled(false);
			} else {
				mQuizNavigationPrevBtn.setEnabled(true);
			}

			if (mArrayListQuizPagerInfo.size() == 1) {
				mQuizNavigationPrevBtn.setVisibility(View.INVISIBLE);
				mQuizNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setOnClickListener() {
		mQuizNavigationNextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (mQuizNavigationNextBtn
						.getText()
						.toString()
						.equalsIgnoreCase(
								getResources().getString(R.string.button_next))) {
					changePagerTo(mQuestionViewPager.getCurrentItem() + 1);
				} else if (mQuizNavigationNextBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_submit))) {
					ArrayList<TrainingQuizSubmit> mList = validateIfEveryQuestionAnswer();
					if (mList != null && mList.size() > 0) {
						if(Utilities.isInternetConnected()){
							new AsyncQuizSubmitTask(mList).execute();
						}else{
							Utilities.showCrouton(QuizActivity.this,mCroutonViewGroup,getResources().getString(
											R.string.internet_unavailable),
									Style.ALERT);
						}
					}else{
						int mPosition = getPositionOfUnAnsweredQuiz();
						if (mPosition != -1) {
							changePagerTo(mPosition);
							Utilities.showCrouton(QuizActivity.this,mCroutonViewGroup,
									getResources().getString(
											R.string.quiz_incomplete),Style.ALERT);
						}
					}
				}
			}
		});

		mQuizNavigationPrevBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changePagerTo(mQuestionViewPager.getCurrentItem() - 1);
			}
		});
	}
	
	private void showQuizTimesUpDialog(){
		try{
			MaterialDialog mMaterialDialog = new MaterialDialog.Builder(QuizActivity.this)
	        .title(getResources().getString(R.string.fragment_quiztimes_up_header))
	        .titleColor(Utilities.getAppColor())
	        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
	        .positiveColor(Utilities.getAppColor())
	        .cancelable(false)
	        .callback(new MaterialDialog.ButtonCallback() {
	            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	            public void onPositive(MaterialDialog dialog) {
	            	dialog.dismiss();
	            	finish();
	            	AndroidUtilities.exitWindowAnimation(QuizActivity.this);
	            }
	        })
	        .show();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void changePagerTo(int position) {
		mQuestionViewPager.setCurrentItem(position, true);
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mQuizNavigationNextBtn);
			setMaterialRippleOnView(mQuizNavigationPrevBtn);
			setMaterialRippleWithGrayOnView(mQuizCoverIv);
			setMaterialRippleOnView(mQuizQuestionHeaderTv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private ArrayList<TrainingQuizSubmit> validateIfEveryQuestionAnswer(){
		boolean isAnswered = true;
		ArrayList<TrainingQuizSubmit> mArrayListTrainingQuizSubmit = new ArrayList<>();
		for(int i = 0 ;i < mArrayListQuizPagerInfo.size() ;i++){
			Cursor mCursor = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" +" AND " + DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mArrayListQuizPagerInfo.get(i).getmQuizId(), mArrayListQuizPagerInfo.get(i).getmQuizQId()}, DBConstant.Training_Quiz_Columns.COLUMN_ID + " ASC");
			if(mCursor!=null &&mCursor.getCount() > 0){
				mCursor.moveToFirst();
				String mAnswer = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER));
				if(!TextUtils.isEmpty(mAnswer)){
					isAnswered = isAnswered && true;
					TrainingQuizSubmit Obj = new TrainingQuizSubmit();
					Obj.setTrainingQuizAnswer(mAnswer);
					Obj.setTrainingQuizId(mArrayListQuizPagerInfo.get(i).getmQuizId());
					Obj.setTrainingQuizQueId(mArrayListQuizPagerInfo.get(i).getmQuizQId());
					Obj.setTrainingQuizQueType(mArrayListQuizPagerInfo.get(i).getmQuizType());
					mArrayListTrainingQuizSubmit.add(Obj);
				}else{
					isAnswered = isAnswered && false;
					return null;
				}
			}
			
			if(mCursor!=null)
				mCursor.close();
		}
		if(isAnswered){
			return mArrayListTrainingQuizSubmit;
		}else{
			return null;
		}
	}
	
	
	private int getPositionOfUnAnsweredQuiz(){
		boolean isAnswered = true;
		int mPosition;
		for(int i = 0 ;i < mArrayListQuizPagerInfo.size() ;i++){
			Cursor mCursor = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" +" AND " + DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mArrayListQuizPagerInfo.get(i).getmQuizId(), mArrayListQuizPagerInfo.get(i).getmQuizQId()}, DBConstant.Training_Quiz_Columns.COLUMN_ID + " ASC");
			if(mCursor!=null &&mCursor.getCount() > 0){
				mCursor.moveToFirst();
				String mAnswer = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER));
				if(!TextUtils.isEmpty(mAnswer)){
					isAnswered = isAnswered && true;
				}else{
					isAnswered = isAnswered && false;
					mPosition = i;
					return mPosition;
				}
			}
			
			if(mCursor!=null)
				mCursor.close();
		}
		return -1;
	}
	
	private void businessLogicOfQuizScore(){
		try{
			mArrayListQuizScorePagerInfo = new ArrayList<>();
			for(int i = 0 ;i < mArrayListQuizPagerInfo.size() ;i++){
				QuizScorePagerInfo mQuizScorePagerInfo = new QuizScorePagerInfo();
				Cursor mCursor = getContentResolver().query(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ID + "=?" +" AND " + DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID + "=?", new String[]{mArrayListQuizPagerInfo.get(i).getmQuizId(), mArrayListQuizPagerInfo.get(i).getmQuizQId()}, DBConstant.Training_Quiz_Columns.COLUMN_ID + " ASC");
				if(mCursor!=null &&mCursor.getCount() > 0){
					mCursor.moveToFirst();
					String mAnswer = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_ANSWER));
					int mPoints  = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QUESTION_POINTS)));
					String mQueType = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_TYPE));
					String mQueId = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_QID));
					String mCorrectAnswer = mCursor.getString(mCursor.getColumnIndex(DBConstant.Training_Quiz_Columns.COLUMN_TRAINING_QUIZ_CORRECT_OPTION));
					mQuizTotalPoints +=mPoints;
					if(!TextUtils.isEmpty(mAnswer) && !TextUtils.isEmpty(mCorrectAnswer)){
						if(mQueType.equalsIgnoreCase("selective")){
							if(mAnswer.equalsIgnoreCase(mCorrectAnswer)){
								mQuizScore+=mPoints;
							}else{
								mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
								mQuizScorePagerInfo.setmQuestionId(mQueId);
							}
						}else{
							/**
							 *Business Logic, if all option are correct, then only correct else count as wroung 
							 */
							if(mAnswer.length() == mCorrectAnswer.length()){
								if(mAnswer.length() == 1){
									if(mAnswer.equalsIgnoreCase(mCorrectAnswer)){
										mQuizScore+=mPoints;		
									}else{
										mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
										mQuizScorePagerInfo.setmQuestionId(mQueId);		
									}
								}else {
									if(mAnswer.length() > 1 && mCorrectAnswer.length() > 1){
										String mCorrectAnswerArr1[] = mCorrectAnswer.split(",");
										String mAnswerArr[] = mAnswer.split(",");
										float mTempPoints = 0;
										for (int j = 0; j < mCorrectAnswerArr1.length; j++) {
											for (int l = 0; l < mAnswerArr.length; l++) {
												if(mAnswerArr[l].equalsIgnoreCase(mCorrectAnswerArr1[j])){
													mTempPoints+= (float)mPoints/mCorrectAnswerArr1.length;
												}
											}
										}
										
										if(mPoints == Math.ceil(mTempPoints)){
											mQuizScore+=mPoints;			
										}else{
											mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
											mQuizScorePagerInfo.setmQuestionId(mQueId);	
										}
									}else{
										mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
										mQuizScorePagerInfo.setmQuestionId(mQueId);		
									}
								}
							}else{
								mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
								mQuizScorePagerInfo.setmQuestionId(mQueId);
							}
							
							/**
							 * Business Logic according to number of correct option and relevant score
							 */
							/*if (mAnswer.length() > 1) {
								if (mCorrectAnswer.length() < 1) {
									mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
									mQuizScorePagerInfo.setmQuestionId(mQueId);
									String [] mAnswerArr = mAnswer.split(",");
									for (int k = 0; k < mAnswerArr.length; k++) {
										if(mCorrectAnswer.equalsIgnoreCase(mAnswerArr[k])){
											mQuizScore+=(mPoints/mAnswerArr.length);
										}
									}
								}else{
									String mCorrectAnswerArr1[] = mCorrectAnswer.split(",");
									String mAnswerArr[] = mAnswer.split(",");
									int mTempPoints = 0;
									for (int j = 0; j < mCorrectAnswerArr1.length; j++) {
										for (int l = 0; l < mAnswerArr.length; l++) {
											if(mAnswerArr[l].equalsIgnoreCase(mCorrectAnswerArr1[j])){
												mQuizScore += mPoints/mCorrectAnswerArr1.length;
												mTempPoints+= mPoints/mCorrectAnswerArr1.length;
											}
										}
									}
									
									if(mAnswerArr.length > mCorrectAnswerArr1.length){//negative marking
										int mNumberExtraOption = mAnswerArr.length - mCorrectAnswerArr1.length;
										mQuizScore = mQuizScore - (mPoints/mNumberExtraOption);
										mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
										mQuizScorePagerInfo.setmQuestionId(mQueId);
									}else if(mAnswerArr.length < mCorrectAnswerArr1.length){
										int mNumberExtraOption = mCorrectAnswerArr1.length - mAnswerArr.length;
										mQuizScore = mQuizScore - (mPoints/mNumberExtraOption);
										mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
										mQuizScorePagerInfo.setmQuestionId(mQueId);
									}else if(mTempPoints!= mPoints){
										mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
										mQuizScorePagerInfo.setmQuestionId(mQueId);
									}
								}
							}else{
								if(mCorrectAnswer.length()<=1){
									if(mAnswer.equalsIgnoreCase(mCorrectAnswer)){
										mQuizScore+=mPoints;
									}else{
										mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
										mQuizScorePagerInfo.setmQuestionId(mQueId);
									}
								}else{
									String [] mCorrectAnswerArr = mCorrectAnswer.split(",");
									for (int k = 0; k < mCorrectAnswerArr.length; k++) {
										if(mAnswer.equalsIgnoreCase(mCorrectAnswerArr[k])){
											mQuizScore+=(mPoints/mCorrectAnswerArr.length);
										}
									}
									mQuizScorePagerInfo.setmQuestionNo(String.valueOf(i+1));
									mQuizScorePagerInfo.setmQuestionId(mQueId);
								}
							}*/						
						}
					}
				}
				
				if(mCursor!=null)
					mCursor.close();
				
				if(!TextUtils.isEmpty(mQuizScorePagerInfo.getmQuestionNo())){
					mArrayListQuizScorePagerInfo.add(mQuizScorePagerInfo);	
				}
				
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private String apiSubmitUserQuiz(ArrayList<TrainingQuizSubmit> mList) {
		try {
			businessLogicOfQuizScore();
			mQuizTimeTaken = mTimerProgress;
			JSONObject jsonObj = JSONRequestBuilder.getPostQuizSubmitData(mList, String.valueOf(mQuizScore), String.valueOf(mQuizTimeTaken));
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SUBMIT_QUIZ, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_SUBMIT_QUIZ, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		if(Utilities.isSuccessFromApi(mResponseFromApi)){
			updateReadInDb();
			updateAttemptCountInDb();
			Intent mIntent = new Intent(QuizActivity.this, QuizScoreActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.TIMETAKEN, String.valueOf(mQuizTimeTaken));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.POINTS, String.valueOf(mQuizScore));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.TOTALPOINTS, String.valueOf(mQuizTotalPoints));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.TOTAL, String.valueOf(mArrayListQuizPagerInfo.size()));
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.QUIZINCORRECT, mArrayListQuizScorePagerInfo);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.ISFROMNOTIFICATION, isFromNotification);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(QuizActivity.this);
			finish();
		}
	}
	
	public class AsyncQuizSubmitTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;
		private ArrayList<TrainingQuizSubmit> mList;

		public AsyncQuizSubmitTask(ArrayList<TrainingQuizSubmit> mList){
			this.mList = mList;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(QuizActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiSubmitUserQuiz(mList);
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
				if(mProgressDialog!=null){
					mProgressDialog.dismiss();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(QuizActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
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
