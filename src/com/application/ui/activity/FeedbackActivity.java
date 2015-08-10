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

import com.application.beans.FeedbackPagerInfo;
import com.application.beans.MobcastFeedbackSubmit;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.FeedbackViewPagerAdapter;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.CirclePageIndicator;
import com.application.ui.view.MobcastProgressDialog;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.RestClient;
import com.application.utils.RetroFitClient;
import com.application.utils.Style;
import com.application.utils.UserReport;
import com.application.utils.Utilities;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FeedbackActivity extends SwipeBackBaseActivity {
	private static final String TAG = FeedbackActivity.class.getSimpleName();
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;

	private AppCompatTextView mFeedbackQuestionHeaderTv;
	private AppCompatTextView mFeedbackQuestionPagerCounterTv;

	private AppCompatButton mFeedbackNavigationNextBtn;
	private AppCompatButton mFeedbackNavigationPrevBtn;

	private CirclePageIndicator mFeedbackCirclePageIndicator;

	private ViewPager mFeedbackViewPager;

	private FeedbackViewPagerAdapter mAdapter;

	private ViewPager.OnPageChangeListener mPagerListener;

	private ArrayList<FeedbackPagerInfo> mArrayListFeedbackPagerInfo;

	private boolean isCirclePagerIndicatorEnable = false;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initToolBar();
		initUi();
		setUiListener();
		getIntentData();
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
		inflater.inflate(R.menu.menu_quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(FeedbackActivity.this);
			return true;
		case R.id.action_report:
			Intent mIntent  = new Intent(FeedbackActivity.this, ReportActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, "Android:Feedback");
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(FeedbackActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		mFeedbackQuestionHeaderTv = (AppCompatTextView) findViewById(R.id.fragmentFeedbackQuestionNumberTextTv);
		mFeedbackQuestionPagerCounterTv = (AppCompatTextView) findViewById(R.id.fragmentFeedbackQuestionPageCountTv);

		mFeedbackNavigationNextBtn = (AppCompatButton) findViewById(R.id.fragmentFeedbackNextBtn);
		mFeedbackNavigationPrevBtn = (AppCompatButton) findViewById(R.id.fragmentFeedbackPreviousBtn);

		mFeedbackViewPager = (ViewPager) findViewById(R.id.fragmentFeedbackQuestionViewPager);

		mFeedbackCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.fragmentFeedbackQuestionCirclePageIndicator);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.FeedbackActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setMaterialRippleView();
		setOnClickListener();
	}
	
	private void getIntentData(){
		mIntent = getIntent();
		try{
			if(mIntent!=null){
				Cursor mCursor = null;
				mId = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.ID);
				mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY).toString();
				if(!TextUtils.isEmpty(mId) && !TextUtils.isEmpty(mCategory)){
					if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
						mCursor = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId}, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + " DESC");
						getDataFromDBForMobcast(mCursor);
					}
					if(mCursor!=null){
						mCursor.close();
					}
				}else{
					finish();
					AndroidUtilities.exitWindowAnimation(FeedbackActivity.this);
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			finish();
			AndroidUtilities.exitWindowAnimation(FeedbackActivity.this);
		}
	}
	
	
	private void getDataFromDBForMobcast(Cursor mCursor){
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			mContentTitle = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE));
			mContentDesc = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC));
			mContentIsLike = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)));
			mContentIsSharing =  Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_LIKE)));
			mContentLikeCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_LIKE_NO));
			mContentViewCount = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_VIEWCOUNT));
			mContentBy = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_BY));
			mContentDate = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DATE));
			mContentTime = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TIME));
			mContentIsRead = Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ)));
			
			Cursor mCursorFeedback = getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID+ "=?", new String[]{mId}, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + " ASC");
			if(mCursorFeedback!=null && mCursorFeedback.getCount() > 0){
				mCursorFeedback.moveToFirst();
				mArrayListFeedbackPagerInfo = new ArrayList<>();
				do {
					FeedbackPagerInfo mObj = new FeedbackPagerInfo();
					mObj.setmFeedbackId(mId);
					mObj.setmFeedbackQId(mCursorFeedback.getString(mCursorFeedback.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID)));
					mObj.setmFeedbackType(mCursorFeedback.getString(mCursorFeedback.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_TYPE)));
					mArrayListFeedbackPagerInfo.add(mObj);
				} while (mCursorFeedback.moveToNext());
				
			}
			if(mCursorFeedback!=null)
				mCursorFeedback.close();
			clearAnswerFromDB();
			setIntentDataToUi();
		}
	}
	
	private void clearAnswerFromDB(){
		ContentValues values = new ContentValues();
		values.put(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER, "");
		for(int i = 0 ; i< mArrayListFeedbackPagerInfo.size() ;i++){
			getContentResolver().update(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, values, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" + " AND "+ DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?",new String[]{mArrayListFeedbackPagerInfo.get(i).getmFeedbackId(), mArrayListFeedbackPagerInfo.get(i).getmFeedbackQId()});
		}
	}
	
	private void setIntentDataToUi(){
		try{
			mToolBar.setTitle(mContentTitle);
			mToolBar.setSubtitle(mContentDesc);
			
			setFeedbackViewPager();
			updateReadInDb();
			if(!mContentIsRead){
				UserReport.updateUserReportApi(mId, mCategory, AppConstants.REPORT.READ, "");
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void updateReadInDb(){
		ContentValues values = new ContentValues();
		if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
			values.put(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ, "true");
			getContentResolver().update(DBConstant.Mobcast_Columns.CONTENT_URI, values, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId});	
		}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
			values.put(DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ, "true");
			getContentResolver().update(DBConstant.Training_Columns.CONTENT_URI, values, DBConstant.Training_Columns.COLUMN_TRAINING_ID + "=?", new String[]{mId});
		}
	}

	private void setFeedbackViewPager() {
		if(mArrayListFeedbackPagerInfo!=null && mArrayListFeedbackPagerInfo.size() > 0){
			if (mArrayListFeedbackPagerInfo.size() < 7) {
				isCirclePagerIndicatorEnable = true;
			}
			mAdapter = new FeedbackViewPagerAdapter(getSupportFragmentManager(),
					mArrayListFeedbackPagerInfo);
			setViewPagerListener();
			mFeedbackViewPager.setAdapter(mAdapter);
			if (isCirclePagerIndicatorEnable) {
				mFeedbackCirclePageIndicator.setViewPager(mFeedbackViewPager);
				mFeedbackCirclePageIndicator
						.setOnPageChangeListener(mPagerListener);
			} else {
				mFeedbackQuestionPagerCounterTv.setVisibility(View.VISIBLE);
				mFeedbackCirclePageIndicator.setVisibility(View.GONE);
				mFeedbackViewPager.setOnPageChangeListener(mPagerListener);
			}
			
			if (mArrayListFeedbackPagerInfo.size() == 1) {
				mFeedbackNavigationPrevBtn.setVisibility(View.INVISIBLE);
				mFeedbackNavigationNextBtn.setText(getResources().getString(
						R.string.button_submit));
			}
		}
	}

	private void setViewPagerListener() {
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
	}

	private void uiOnChangeOfPagerListener(int mNumberQuestion) {
		mFeedbackQuestionHeaderTv.setText(getResources().getString(
				R.string.sample_question_header_box)
				+ " " + mNumberQuestion);
		if (!isCirclePagerIndicatorEnable) {
			mFeedbackQuestionPagerCounterTv.setText(mNumberQuestion + " / "
					+ mArrayListFeedbackPagerInfo.size());
		}

		if (mNumberQuestion == mArrayListFeedbackPagerInfo.size()) {
			mFeedbackNavigationNextBtn.setText(getResources().getString(
					R.string.button_submit));
		} else {
			mFeedbackNavigationNextBtn.setText(getResources().getString(
					R.string.button_next));
		}

		if (mNumberQuestion == 1) {
			mFeedbackNavigationPrevBtn.setEnabled(false);
		} else {
			mFeedbackNavigationPrevBtn.setEnabled(true);
		}

		if (mArrayListFeedbackPagerInfo.size() == 1) {
			mFeedbackNavigationPrevBtn.setVisibility(View.INVISIBLE);
			mFeedbackNavigationNextBtn.setText(getResources().getString(
					R.string.button_submit));
		}
	}

	private void setOnClickListener() {
		mFeedbackNavigationNextBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if (mFeedbackNavigationNextBtn
								.getText()
								.toString()
								.equalsIgnoreCase(
										getResources().getString(
												R.string.button_next))) {
							changePagerTo(mFeedbackViewPager.getCurrentItem() + 1);
						} else if (mFeedbackNavigationNextBtn
								.getText()
								.toString()
								.equalsIgnoreCase(
										getResources().getString(
												R.string.button_submit))) {
							ArrayList<MobcastFeedbackSubmit> mList = validateIfEveryQuestionAnswer();
							if (mList != null && mList.size() > 0) {
								if(Utilities.isInternetConnected()){
									new AsyncFeedbackSubmitTask(mList).execute();
								}else{
									Utilities.showCrouton(FeedbackActivity.this,mCroutonViewGroup,getResources().getString(
													R.string.internet_unavailable),
											Style.ALERT);
								}
							}else{
								int mPosition = getPositionOfUnAnsweredFeedback();
								if (mPosition != -1) {
									changePagerTo(mPosition);
									Utilities.showCrouton(FeedbackActivity.this,mCroutonViewGroup,
											getResources().getString(
													R.string.feedback_incomplete),Style.ALERT);
								}
							}
						}
					}
				});

		mFeedbackNavigationPrevBtn
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						changePagerTo(mFeedbackViewPager.getCurrentItem() - 1);
					}
				});
	}

	private void changePagerTo(int position) {
		mFeedbackViewPager.setCurrentItem(position, true);
	}
	

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mFeedbackNavigationNextBtn);
			setMaterialRippleOnView(mFeedbackNavigationPrevBtn);
			setMaterialRippleOnView(mFeedbackQuestionHeaderTv);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
	
	private void showFeedbackThankYouDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(FeedbackActivity.this)
        .title(getResources().getString(R.string.fragment_feedback_thank_you_header))
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            	finish();
            	AndroidUtilities.exitWindowAnimation(FeedbackActivity.this);
            }
        })
        .show();
	}
	
	private ArrayList<MobcastFeedbackSubmit> validateIfEveryQuestionAnswer(){
		boolean isAnswered = true;
		ArrayList<MobcastFeedbackSubmit> mArrayListMobcastFeedbackSubmit = new ArrayList<>();
		for(int i = 0 ;i < mArrayListFeedbackPagerInfo.size() ;i++){
			Cursor mCursor = getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" +" AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mArrayListFeedbackPagerInfo.get(i).getmFeedbackId(), mArrayListFeedbackPagerInfo.get(i).getmFeedbackQId()}, DBConstant.Mobcast_Feedback_Columns.COLUMN_ID + " ASC");
			if(mCursor!=null &&mCursor.getCount() > 0){
				mCursor.moveToFirst();
				String mAnswer = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER));
				if(!TextUtils.isEmpty(mAnswer)){
					isAnswered = isAnswered && true;
					MobcastFeedbackSubmit Obj = new MobcastFeedbackSubmit();
					Obj.setMobcastFeedbackAnswer(mAnswer);
					Obj.setMobcastFeedbackId(mArrayListFeedbackPagerInfo.get(i).getmFeedbackId());
					Obj.setMobcastFeedbackQueId(mArrayListFeedbackPagerInfo.get(i).getmFeedbackQId());
					Obj.setMobcastFeedbackQueType(mArrayListFeedbackPagerInfo.get(i).getmFeedbackType());
					mArrayListMobcastFeedbackSubmit.add(Obj);
				}else{
					isAnswered = isAnswered && false;
					return null;
				}
			}
			
			if(mCursor!=null)
				mCursor.close();
		}
		if(isAnswered){
			return mArrayListMobcastFeedbackSubmit;
		}else{
			return null;
		}
	}
	
	
	private int getPositionOfUnAnsweredFeedback(){
		boolean isAnswered = true;
		int mPosition;
		for(int i = 0 ;i < mArrayListFeedbackPagerInfo.size() ;i++){
			Cursor mCursor = getContentResolver().query(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ID + "=?" +" AND " + DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_QID + "=?", new String[]{mArrayListFeedbackPagerInfo.get(i).getmFeedbackId(), mArrayListFeedbackPagerInfo.get(i).getmFeedbackQId()}, DBConstant.Mobcast_Feedback_Columns.COLUMN_ID + " ASC");
			if(mCursor!=null &&mCursor.getCount() > 0){
				mCursor.moveToFirst();
				String mAnswer = mCursor.getString(mCursor.getColumnIndex(DBConstant.Mobcast_Feedback_Columns.COLUMN_MOBCAST_FEEDBACK_ANSWER));
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
	
	private String apiSubmitUserFeedback(ArrayList<MobcastFeedbackSubmit> mList) {
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostFeedbackSubmitData(mList);
			if(BuildVars.USE_OKHTTP){
				return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SUBMIT_FEEDBACK, jsonObj.toString(), TAG);	
			}else{
				return RestClient.postJSON(AppConstants.API.API_SUBMIT_FEEDBACK, jsonObj, TAG);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		if(Utilities.isSuccessFromApi(mResponseFromApi)){
			showFeedbackThankYouDialog();
		}
	}
	
	public class AsyncFeedbackSubmitTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;
		private ArrayList<MobcastFeedbackSubmit> mList;

		public AsyncFeedbackSubmitTask(ArrayList<MobcastFeedbackSubmit> mList){
			this.mList = mList;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new MobcastProgressDialog(FeedbackActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication().getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiSubmitUserFeedback(mList);
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
				Utilities.showCrouton(FeedbackActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
	}
}
