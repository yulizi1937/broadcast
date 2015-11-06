/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Award;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.AwardRecyclerAdapter;
import com.application.ui.adapter.AwardRecyclerAdapter.OnItemClickListenerA;
import com.application.ui.adapter.AwardRecyclerAdapter.OnItemLongClickListenerA;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.LoadToast;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.VerticalDividerItemDecoration;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FetchFeedActionAsyncTask;
import com.application.utils.FetchFeedActionAsyncTask.OnPostExecuteFeedActionTaskListener;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
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
public class AwardRecyclerActivity extends SwipeBackBaseActivity {
	@SuppressWarnings("unused")
	private static final String TAG = AwardRecyclerActivity.class
			.getSimpleName();

	private Toolbar mToolBar;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatButton mEmptyRefreshBtn;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ObservableRecyclerView mRecyclerView;

	private FrameLayout mCroutonViewGroup;

	private AwardRecyclerAdapter mAdapter;

	private ArrayList<Award> mArrayListAward;

	private Context mContext;

	private GridLayoutManager mGridLayoutManager;
	
	private boolean isGrid = false;
	private int mGridColumn = 1;

	private boolean mLoadMore = false;
	int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_award);
		setSecurity();
		initToolBar();
		initUi();
		setMaterialRippleView();
		syncDataWithApi();
		applyTheme();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		Utilities.showBadgeNotification(AwardRecyclerActivity.this);
	}
	
	private void syncDataWithApi(){
		try{
			if (mArrayListAward != null && mArrayListAward.size() > 0) {
				refreshFeedFromApi(true, true, 0,true);	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_award, menu);
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
			AndroidUtilities.exitWindowAnimation(AwardRecyclerActivity.this);
			return true;
		case R.id.action_search:
			Intent mIntent = new Intent(AwardRecyclerActivity.this,
					SearchActivity.class);
			mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY,
					AppConstants.INTENTCONSTANTS.AWARD);
			startActivity(mIntent);
			AndroidUtilities.enterWindowAnimation(AwardRecyclerActivity.this);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void toolBarRefresh() {
		if(!mSwipeRefreshLayout.isRefreshing()){
			mSwipeRefreshLayout.setRefreshing(true);
		}
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		if (!mLoadMore) {
			mLoadMore = true;
			refreshFeedFromApi(true, true, 0,false);
		}
	}

	private void setUiListener() {
		setRecyclerAdapterListener();
		setRecyclerScrollListener();
		setSwipeRefreshListener();
//		setMaterialRippleView();
		setClickListener();
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(AwardRecyclerActivity.this).applyThemeCapture(AwardRecyclerActivity.this, AwardRecyclerActivity.this, mToolBar);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mEmptyRefreshBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void setClickListener() {
		mEmptyRefreshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				refreshFeedFromApi(true, true, AppConstants.BULK,false);
			}
		});
	}

	@SuppressLint("NewApi")
	private void setSwipeRefreshListener() {
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mToolBarMenuRefresh.setVisibility(View.GONE);
				mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
				refreshFeedFromApi(true, true, 0,false);
			}
		});
	}

	@SuppressLint("NewApi")
	private void refreshFeedFromApi(boolean isRefreshFeed, boolean sortByAsc,
			int limit, boolean isAutoRefresh) {// sortByAsc:true-> new data //sortByAsc:false->Old Data //isAutoRefresh: true-> onResume ? false-> onPullToRefresh;
		if (Utilities.isInternetConnected()) {
			if (AndroidUtilities.isAboveIceCreamSandWich()) {
				new AsyncRefreshTask(isRefreshFeed, sortByAsc, limit, isAutoRefresh)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
								null, null, null);
			} else {
				new AsyncRefreshTask(isRefreshFeed, sortByAsc, limit, isAutoRefresh).execute();
			}
		}
	}

	private void initUi() {
		mContext = AwardRecyclerActivity.this;

		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		mRecyclerView = (ObservableRecyclerView) findViewById(R.id.scroll_wo);

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

		mEmptyFrameLayout = (FrameLayout) findViewById(R.id.fragmentEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton) findViewById(R.id.layoutEmptyRefreshBtn);

		isToApplyGridOrNot();
		
		mGridLayoutManager = new GridLayoutManager(mContext, mGridColumn);
		mRecyclerView.setLayoutManager(mGridLayoutManager);

		ApplicationLoader.getPreferences().setViewIdAward("-1");

		checkDataInAdapter();

		setSwipeRefreshLayoutCustomisation();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.AwardRecyclerActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setRecyclerAdapter() {
		mAdapter = new AwardRecyclerAdapter(mContext, mArrayListAward);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(false);
		if (AndroidUtilities.isAboveIceCreamSandWich()) {
			AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(
					mAdapter);
			ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(
					mAlphaAdapter);
			mRecyclerView.setAdapter(mScaleInAdapter);
		} else {
			mRecyclerView.setAdapter(mAdapter);
		}
		mRecyclerView
				.addItemDecoration(/*new HorizontalDividerItemDecoration.Builder(
						this)
						.color(Utilities.getDividerColor())
						.marginResId(
								R.dimen.fragment_recyclerview_award_left_margin,
								R.dimen.fragment_recyclerview_award_right_margin)
						.build()*/
						new HorizontalDividerItemDecoration.Builder(this).color(Utilities.getDividerColor())
								.sizeResId(R.dimen.fragment_recyclerview_divider)
								.visibilityProvider(mAdapter).build());
		
		
		
		if(isGrid){
			mRecyclerView
			.addItemDecoration(/*new VerticalDividerItemDecoration.Builder(
					this)
					.color(Utilities.getDividerColor())
					.marginResId(
							R.dimen.fragment_recyclerview_award_left_margin,
							R.dimen.fragment_recyclerview_award_right_margin)
					.build()*/
					new VerticalDividerItemDecoration.Builder(this).color(Utilities.getDividerColor())
							.sizeResId(R.dimen.fragment_recyclerview_divider)
							.visibilityProvider(mAdapter).build());	
		}
	}

	private void setEmptyData() {
		mEmptyTitleTextView.setText(getResources().getString(
				R.string.emptyAwardTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyAwardMessage));
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
	}

	private void setSwipeRefreshLayoutCustomisation() {
		mSwipeRefreshLayout.setColorSchemeColors(
				Color.parseColor(AppConstants.COLOR.MOBCAST_RED),
				Color.parseColor(AppConstants.COLOR.MOBCAST_YELLOW),
				Color.parseColor(AppConstants.COLOR.MOBCAST_PURPLE),
				Color.parseColor(AppConstants.COLOR.MOBCAST_GREEN),
				Color.parseColor(AppConstants.COLOR.MOBCAST_BLUE));
	}

	private void setRecyclerAdapterListener() {
		if (mAdapter != null) {
			mAdapter.setOnItemClickListener(new OnItemClickListenerA() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					switch (view.getId()) {
					case R.id.itemRecyclerAwardRootLayout:
						Intent mIntentEvent = new Intent(
								AwardRecyclerActivity.this,
								AwardProfileActivity.class);
						mIntentEvent.putExtra(
								AppConstants.INTENTCONSTANTS.CATEGORY,
								AppConstants.INTENTCONSTANTS.AWARD);
						mIntentEvent.putExtra(AppConstants.INTENTCONSTANTS.ID,
								mArrayListAward.get(position).getmId());
						saveViewPosition(position);
						startActivity(mIntentEvent);
						AndroidUtilities
								.enterWindowAnimation(AwardRecyclerActivity.this);
						break;
					case R.id.itemRecyclerCongratulateIv:
						if (mArrayListAward.get(position).isCongratulated()) {
							Utilities
									.showCrouton(
											AwardRecyclerActivity.this,
											mCroutonViewGroup,
											getResources()
													.getString(
															R.string.congratulate_message_validation)
													+ " "
													+ mArrayListAward.get(
															position)
															.getmName(),
											Style.ALERT);
						} else {
							showAwardCongratulateDialog(position);
						}
						break;
					case R.id.itemRecyclerAwardMessageIv:
						showAwardMessageDialog(position);
						break;
					}

				}
			});
		}

		if (mAdapter != null) {
			mAdapter.setOnItemLongClickListener(new OnItemLongClickListenerA() {
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					// position=-1;
					showContextMenu(position, view);
				}
			});
		}
	}

	private void showAwardCongratulateDialog(final int mPosition) {
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(
				AwardRecyclerActivity.this)
				.title(getResources().getString(
						R.string.dialog_birthday_award_title)
						+ " ?")
				.titleColor(Utilities.getAppColor())
				.positiveText(
						getResources()
								.getString(
										R.string.sample_fragment_settings_dialog_language_positive))
				.positiveColor(Utilities.getAppColor())
				.negativeText(
						getResources()
								.getString(
										R.string.sample_fragment_settings_dialog_language_negative))
				.negativeColor(Utilities.getAppColor())
				.callback(new MaterialDialog.ButtonCallback() {
					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onPositive(MaterialDialog dialog) {
						dialog.dismiss();
						if (!mArrayListAward.get(mPosition).isCongratulated()) {
							UserReport.updateUserReportApi(
									mArrayListAward.get(mPosition).getmId(),
									AppConstants.INTENTCONSTANTS.AWARD,
									AppConstants.REPORT.CONG, "");
							Utilities.showCrouton(
									AwardRecyclerActivity.this,
									mCroutonViewGroup,
									getResources().getString(
											R.string.congratulate_message)
											+ " "
											+ mArrayListAward.get(mPosition)
													.getmName(), Style.CONFIRM);
							ContentValues mValues = new ContentValues();
							mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE,"true");
							String mCongratsCount = String.valueOf(Integer.parseInt(mArrayListAward.get(mPosition).getmCongratulatedCount())+1);
							mArrayListAward.get(mPosition).setmCongratulatedCount(mCongratsCount);
							mValues.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO, mCongratsCount);
							getContentResolver().update(DBConstant.Award_Columns.CONTENT_URI,mValues,DBConstant.Award_Columns.COLUMN_AWARD_ID+ "=?",new String[] { mArrayListAward.get(mPosition).getmId() });
							mArrayListAward.get(mPosition).setCongratulated(true);
							mRecyclerView.getAdapter().notifyItemChanged(mPosition);
						}
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						dialog.dismiss();
					}
				}).show();
	}

	private void showAwardMessageDialog(final int mPosition) {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(AwardRecyclerActivity.this)
				.title(getResources().getString(R.string.dialog_birthday_message_title))
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_birthday_message, true)
				.cancelable(true).show();

		View mView = mMaterialDialog.getCustomView();
		final AppCompatEditText mMessageEd = (AppCompatEditText) mView
				.findViewById(R.id.dialogBirthdayMessageEd);
		AppCompatButton mSendBtn = (AppCompatButton) mView
				.findViewById(R.id.dialogBirthdayMessageSendBtn);
		final AppCompatTextView mMessageEdCounterTv = (AppCompatTextView) mView
				.findViewById(R.id.dialogBirthdayMessageTv);

		mMessageEd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				if (mCharsequence.length() > 140) {
					mMessageEdCounterTv.setTextColor(Color.RED);
				} else {
					mMessageEdCounterTv.setTextColor(Utilities.getAppColor());
				}
				mMessageEdCounterTv.setText(mCharsequence.length() + "/140");

			}

			@Override
			public void beforeTextChanged(CharSequence mCharsequence,
					int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable mEditable) {
				// TODO Auto-generated method stub
			}
		});

		try {
			setMaterialRippleWithGrayOnView(mSendBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
		mSendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(mMessageEd.getText().toString())){
					sendMessage(mMessageEd.getText().toString(), mArrayListAward.get(mPosition).getmId());
					mMaterialDialog.dismiss();
				}
			}
		});
	}

	private void setRecyclerScrollListener() {
		if (mAdapter != null) {
			mRecyclerView.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView,
						int newState) {
					// TODO Auto-generated method stub
					super.onScrollStateChanged(recyclerView, newState);
					int topRowVerticalPosition = (recyclerView == null || recyclerView
							.getChildCount() == 0) ? 0 : recyclerView
							.getChildAt(0).getTop();
					mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
				}

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					// TODO Auto-generated method stub
					super.onScrolled(recyclerView, dx, dy);
					mVisibleItemCount = mGridLayoutManager.getChildCount();
					mTotalItemCount = mGridLayoutManager.getItemCount();
					mFirstVisibleItem = mGridLayoutManager
							.findFirstVisibleItemPosition();

					if (!mLoadMore) {
						if (mVisibleItemCount + mFirstVisibleItem >= mTotalItemCount) {
							mLoadMore = true;
							refreshFeedFromApi(true, false, AppConstants.BULK,false);
						}
					}
				}
			});
		}
	}

	private void checkDataInAdapter() {
		checkExpiredAwardAndDeleteFromDB();
		Cursor mCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI,null,null,null,DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED+ " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addAwardObjectListFromDBToBeans(mCursor, false, false);
			if (mArrayListAward.size() > 0) {
				setRecyclerAdapter();
			}else {
				setEmptyData();
			}
		} else {
			setEmptyData();
		}

		if (mCursor != null) {
			mCursor.close();
		}
	}

	public class AwardSort implements Comparator<Award> {
		@Override
		public int compare(Award Obj1, Award Obj2) {
			try {
				if (Integer.parseInt(Obj1.getmId()) < Integer.parseInt(Obj2
						.getmId())) {
					return 1;
				} else {
					return -1;
				}
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
				return -1;
			}
		}
	}
	
	private void checkExpiredAwardAndDeleteFromDB(){
		long mCurrentTimeMillis = System.currentTimeMillis();
		getContentResolver().delete(DBConstant.Award_Columns.CONTENT_URI, DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED + "<?", new String[]{String.valueOf(mCurrentTimeMillis)});
	}

	private void addAwardObjectListFromDBToBeans(Cursor mCursor,
			boolean isFromBroadCastReceiver, boolean isToAddOldData) {
		int mIntAwardId = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_ID);
		int mIntAwardName = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_NAME);
		int mIntAwardRecognition = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION);
		int mIntAwardThumbLink = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK);
		int mIntAwardThumbPath = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH);
		int mIntAwardReceivedDate = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_DATE);
		int mIntAwardReceivedTime = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_TIME);
		int mIntAwardDate = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE);
		int mIntAwardViewCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO);
		int mIntAwardLikeCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO);
		int mIntAwardCongratulatedCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO);
		int mIntAwardIsRead = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ);
		int mIntAwardIsLike = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE);
		int mIntAwardIsCongratulated = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE);

		if (!isToAddOldData) {
			mArrayListAward = new ArrayList<Award>();
		} else {
			if (mArrayListAward == null) {
				mArrayListAward = new ArrayList<Award>();
			}
		}
		mCursor.moveToFirst();
		do {
			Award Obj = new Award();
			String mAwardId = mCursor.getString(mIntAwardId);
			String mAwardReceivedDate = mCursor.getString(mIntAwardReceivedDate);
			String mAwardReceivedTime = mCursor.getString(mIntAwardReceivedTime);
			Obj.setmId(mAwardId);
			Obj.setmName(mCursor.getString(mIntAwardName));
			Obj.setmRecognition(mCursor.getString(mIntAwardRecognition));
			Obj.setmThumbLink(mCursor.getString(mIntAwardThumbLink));
			Obj.setmThumbPath(mCursor.getString(mIntAwardThumbPath));
			Obj.setmReceivedDate(mAwardReceivedDate);
			Obj.setmReceivedTime(mAwardReceivedTime);
			Obj.setmAwardDate(mCursor.getString(mIntAwardDate));
			Obj.setmLikeCount(mCursor.getString(mIntAwardLikeCount));
			Obj.setmViewCount(mCursor.getString(mIntAwardViewCount));
			Obj.setmCongratulatedCount(mCursor.getString(mIntAwardCongratulatedCount)==null?"0":mCursor.getString(mIntAwardCongratulatedCount));
			Obj.setRead(Boolean.parseBoolean(mCursor.getString(mIntAwardIsRead)));
			Obj.setLike(Boolean.parseBoolean(mCursor.getString(mIntAwardIsLike)));
			Obj.setCongratulated(Boolean.parseBoolean(mCursor.getString(mIntAwardIsCongratulated)));
			Obj.setmFileType(AppConstants.INTENTCONSTANTS.AWARD);

			if (!isFromBroadCastReceiver) {
				mArrayListAward.add(Obj);
			} else {
				mArrayListAward.add(0, Obj);
			}
		} while (mCursor.moveToNext());

		Collections.sort(mArrayListAward, new AwardSort());
	}

	private void saveViewPosition(int position) {
		ApplicationLoader.getPreferences().setViewIdAward(
				String.valueOf(position));
	}

	private void checkReadFromDBAndUpdateToObj() {
		int position = Integer.parseInt(ApplicationLoader.getPreferences()
				.getViewIdAward());
		if (position >= 0 && position < mArrayListAward.size()) {
			if (mArrayListAward != null && mArrayListAward.size() > 0) {
				Cursor mCursor = getContentResolver()
						.query(DBConstant.Award_Columns.CONTENT_URI,new String[] {DBConstant.Award_Columns.COLUMN_AWARD_ID,DBConstant.Award_Columns.COLUMN_AWARD_IS_READ,DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE,DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO },
								DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?",
								new String[] { mArrayListAward.get(position)
										.getmId() }, null);

				if (mCursor != null && mCursor.getCount() > 0) {
					mCursor.moveToFirst();
					boolean isToNotify = false;
					if (Boolean
							.parseBoolean(mCursor.getString(mCursor
									.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ)))) {
						mArrayListAward.get(position).setRead(true);
						isToNotify = true;
					}
					if (Boolean
							.parseBoolean(mCursor.getString(mCursor
									.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE)))) {
						mArrayListAward.get(position).setLike(true);
						mArrayListAward
								.get(position)
								.setmLikeCount(
										mCursor.getString(mCursor
												.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO)));
						isToNotify = true;
					}
					if (isToNotify) {
						mRecyclerView.getAdapter().notifyDataSetChanged();
					}
				}

				if (mCursor != null)
					mCursor.close();
			}
		}
	}

	public void passDataToFragment(int mId, String mCategory) {
		try {
			if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.AWARD)) {
				Cursor mCursor = null;
				boolean isToAddOldData = false;
				if (mId == -786) {
					mCursor = mContext.getContentResolver().query(
							DBConstant.Award_Columns.CONTENT_URI,
							null,
							DBConstant.Award_Columns.COLUMN_AWARD_ID + "<?",
							new String[] { mArrayListAward.get(
									mArrayListAward.size() - 1).getmId() },
							DBConstant.Award_Columns.COLUMN_AWARD_ID + " DESC");
					isToAddOldData = true;
				} else {
					mCursor = mContext.getContentResolver().query(
							DBConstant.Award_Columns.CONTENT_URI, null, null,
							null,
							DBConstant.Award_Columns.COLUMN_AWARD_ID + " DESC");
				}
				if (mCursor != null && mCursor.getCount() > 0) {
					mSwipeRefreshLayout.setEnabled(true);
					mSwipeRefreshLayout.setRefreshing(true);
					mCursor.moveToFirst();
					addAwardObjectListFromDBToBeans(mCursor, false,
							isToAddOldData);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mAdapter != null) {
								mAdapter.addAwardObjList(mArrayListAward);
								mRecyclerView.getAdapter()
										.notifyDataSetChanged();
							} else {
								setRecyclerAdapter();
								setUiListener();
							}
							mSwipeRefreshLayout.setRefreshing(false);

							if (mRecyclerView.getVisibility() == View.GONE) {
								mEmptyFrameLayout.setVisibility(View.GONE);
								mRecyclerView.setVisibility(View.VISIBLE);
							}
						}
					}, 1000);

				}

				if (mCursor != null)
					mCursor.close();
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	/*
	 * ContextMenu
	 */
	private void showContextMenu(int mPosition, View mView) {
		try {
			// mPosition = mPosition - 1;
			if (mPosition != -1) {
				int mType = AppConstants.TYPE.AWARD;
				String mTitle = mArrayListAward.get(mPosition).getmName();
				boolean isRead = mArrayListAward.get(mPosition).isRead();
				ContextMenuFragment newFragment = new ContextMenuFragment(
						mPosition, mType, mTitle, isRead, mView);
				newFragment.show(getSupportFragmentManager(), "dialog");
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	public class ContextMenuFragment extends DialogFragment {
		int mPosition;
		int mType;
		String mTitle;
		boolean isRead;
		View mView;

		public ContextMenuFragment(int mPosition, int mType, String mTitle,
				boolean isRead, View mView) {
			this.mPosition = mPosition;
			this.mTitle = mTitle;
			this.mType = mType;
			this.isRead = isRead;
			this.mView = mView;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return getContextMenu(mPosition, mType, mTitle, isRead, mView);
		}
	}

	private BottomSheet getContextMenu(final int mPosition, int mType,
			final String mTitle, boolean isRead, final View mView) {
		BottomSheet mBottomSheet;
		mBottomSheet = new BottomSheet.Builder(AwardRecyclerActivity.this)
				.icon(Utilities.getRoundedBitmapForContextMenu(mType))
				.title(mTitle).sheet(R.menu.context_menu_award).build();
		final Menu menu = mBottomSheet.getMenu();

		SpannableString mSpannabledRead = new SpannableString(getResources()
				.getString(R.string.context_menu_read));
		SpannableString mSpannabledUnRead = new SpannableString(getResources()
				.getString(R.string.context_menu_unread));
		SpannableString mSpannabledDelete = new SpannableString(getResources()
				.getString(R.string.context_menu_delete));
		SpannableString mSpannabledView = new SpannableString(getResources()
				.getString(R.string.context_menu_view));

		mSpannabledRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
				mSpannabledRead.length(), 0);
		mSpannabledUnRead.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
				mSpannabledUnRead.length(), 0);
		mSpannabledDelete.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
				mSpannabledDelete.length(), 0);
		mSpannabledView.setSpan(new ForegroundColorSpan(Color.GRAY), 0,
				mSpannabledView.length(), 0);

		menu.getItem(0).setTitle(mSpannabledRead);
		menu.getItem(1).setTitle(mSpannabledUnRead);
		menu.getItem(2).setTitle(mSpannabledDelete);
		menu.getItem(3).setTitle(mSpannabledView);

		/**
		 * Read
		 */
		menu.getItem(0).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						contextMenuMarkAsRead(mPosition);
						return true;
					}
				});

		/**
		 * UnRead
		 */
		menu.getItem(1).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						contextMenuMarkAsUnRead(mPosition);
						return true;
					}
				});

		/**
		 * Delete
		 */
		menu.getItem(2).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showDeleteConfirmationDialog(mPosition, mTitle);
						// contextMenuDelete(mPosition);
						return true;
					}
				});

		/**
		 * View
		 */
		menu.getItem(3).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						contextMenuView(mPosition, mView);
						return true;
					}
				});

		return mBottomSheet;
	}

	private void contextMenuMarkAsRead(int mPosition) {
		try {
			ContentValues values = new ContentValues();
			String mAwardId = mArrayListAward.get(mPosition).getmId();
			values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, "true");
			getContentResolver().update(DBConstant.Award_Columns.CONTENT_URI,
					values, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?",
					new String[] { mAwardId });
			mArrayListAward.get(mPosition).setRead(true);
			mRecyclerView.getAdapter().notifyItemChanged(mPosition);
			UserReport.updateUserReportApi(mAwardId,
					AppConstants.INTENTCONSTANTS.AWARD,
					AppConstants.REPORT.READ, "");
			Utilities.showBadgeNotification(AwardRecyclerActivity.this);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void contextMenuMarkAsUnRead(int mPosition) {
		try {
			ContentValues values = new ContentValues();
			values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ, "false");
			getContentResolver().update(DBConstant.Award_Columns.CONTENT_URI,
					values, DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?",
					new String[] { mArrayListAward.get(mPosition).getmId() });
			mArrayListAward.get(mPosition).setRead(false);
			mRecyclerView.getAdapter().notifyItemChanged(mPosition);
			Utilities.showBadgeNotification(AwardRecyclerActivity.this);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void contextMenuDelete(int mPosition) {
		try {
			getContentResolver().delete(DBConstant.Award_Columns.CONTENT_URI,
					DBConstant.Award_Columns.COLUMN_AWARD_ID + "=?",
					new String[] { mArrayListAward.get(mPosition).getmId() });
			mArrayListAward.remove(mPosition);
			if (mArrayListAward.size() == 0) {
				checkDataInAdapter();
			} else {
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
			Utilities.showBadgeNotification(AwardRecyclerActivity.this);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void contextMenuView(int mPosition, View mView) {
		try {
			mView.performClick();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void showDeleteConfirmationDialog(final int mPosition, String mTitle) {
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(
				AwardRecyclerActivity.this)
				.title(getResources()
						.getString(R.string.content_delete_message)
						+ " "
						+ mTitle + "?")
				.iconRes(R.drawable.context_menu_delete)
				.titleColor(Utilities.getAppColor())
				.positiveText(getResources().getString(R.string.button_delete))
				.positiveColor(Utilities.getAppColor())
				.negativeText(
						getResources()
								.getString(
										R.string.sample_fragment_settings_dialog_language_negative))
				.negativeColor(Utilities.getAppColor())
				.callback(new MaterialDialog.ButtonCallback() {
					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onPositive(MaterialDialog dialog) {
						dialog.dismiss();
						contextMenuDelete(mPosition);
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						dialog.dismiss();
					}
				}).show();
	}

	/*
	 * AsyncTask To Refresh
	 */

	public String apiRefreshFeedAward(boolean sortByAsc, int limit) {
		try {
			JSONObject jsonObj = null;
			if (sortByAsc) {
				jsonObj = JSONRequestBuilder.getPostFetchFeedAward(sortByAsc,
						limit, mArrayListAward != null ? mArrayListAward.get(0)
								.getmId() : String.valueOf("0"));
			} else {
//				jsonObj = JSONRequestBuilder.getPostFetchFeedAward(sortByAsc,
//						limit, mArrayListAward.get(mArrayListAward.size() - 2)
//								.getmId());
				
				jsonObj= JSONRequestBuilder.getPostFetchFeedAward(sortByAsc,limit, isGrid?mArrayListAward.get(mArrayListAward.size()-3).getmId():mArrayListAward.get(mArrayListAward.size()-2).getmId());
			}
			if (BuildVars.USE_OKHTTP) {
				return RetroFitClient.postJSON(new OkHttpClient(),
						AppConstants.API.API_FETCH_FEED_AWARD,
						jsonObj.toString(), TAG);
			} else {
				return RestClient.postJSON(
						AppConstants.API.API_FETCH_FEED_AWARD, jsonObj, TAG);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	public String apiRefreshFeedAction() {
		try {
			JSONObject jsonObj = JSONRequestBuilder
					.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.AWARD);
			if (BuildVars.USE_OKHTTP) {
				return RetroFitClient.postJSON(new OkHttpClient(),
						AppConstants.API.API_FETCH_FEED_ACTION,
						jsonObj.toString(), TAG);
			} else {
				return RestClient.postJSON(
						AppConstants.API.API_FETCH_FEED_ACTION, jsonObj, TAG);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	public void parseDataFromApi(String mResponseFromApi, boolean isToAddOldData) {
		try {
			if (Utilities.isSuccessFromApi(mResponseFromApi)) {
				JSONObject mJSONObj = new JSONObject(mResponseFromApi);
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.award);

				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);

					String mAwardId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardId);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardSentDate);
					String mTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardSentTime);
					String mExpiryDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardExpiryDate);
					String mExpiryTime = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardExpiryTime);
					String mFileLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCoverLink);
					String mFileName = Utilities.getFileName(mFileLink);
					String mThumbnailLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCoverThumbnail);
					String mThumbnailPath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, true,Utilities.getFileName(mThumbnailLink));
					String mFilePath = Utilities.getFilePath(AppConstants.TYPE.IMAGE, false,Utilities.getFileName(mFileLink));
					ContentValues values = new ContentValues();
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_ID,mAwardId);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECOGNITION,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardTitle));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_NAME,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverName));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_MOBILE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverMobile));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVER_EMAIL,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReceiverEmail));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDate));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE,mDate);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_TIME,mTime);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DATE_FORMATTED,Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED, Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DESCRIPTION,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDescription));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_CITY,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCity));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_DEPARTMENT,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardDepartment));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardReadCount));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardLikeCount));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardCongratulatedCount));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_READ,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsRead));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_LIKE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsLike));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_CONGRATULATE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsCongratulated));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_IS_SHARING,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardIsSharing));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_LINK,mFileLink);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_PATH,mFilePath);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_FILE_SIZE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.awardFileSize));
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_LINK,mThumbnailLink);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_THUMBNAIL_PATH,mThumbnailPath);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE,mExpiryDate);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_TIME,mExpiryTime);
					values.put(DBConstant.Award_Columns.COLUMN_AWARD_EXPIRY_DATE_FORMATTED,Utilities.getMilliSecond(mExpiryDate, mExpiryTime));

					getContentResolver().insert(DBConstant.Award_Columns.CONTENT_URI, values);

				}

				if (isToAddOldData) {
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.AWARD);
				} else {
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.AWARD);
				}
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	public class AsyncRefreshTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;
		private boolean isRefreshFeed = true;
		private boolean sortByAsc = false;
		private boolean isAutoRefresh = false;
		private int limit;

		public AsyncRefreshTask(boolean isRefreshFeed, boolean sortByAsc,
				int limit, boolean isAutoRefresh) {
			this.isRefreshFeed = isRefreshFeed;
			this.sortByAsc = sortByAsc;
			this.limit = limit;
			this.isAutoRefresh = isAutoRefresh;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mArrayListAward == null) {
				mProgressDialog = new MobcastProgressDialog(
						AwardRecyclerActivity.this);
				mProgressDialog.setMessage(ApplicationLoader.getApplication()
						.getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}

			if (!sortByAsc) {
				for(int i = 0 ; i < mGridColumn ; i++){
					Award Obj = new Award();
					Obj.setmFileType(AppConstants.MOBCAST.FOOTER);
					mArrayListAward.add(Obj);
				}
				mRecyclerView.getAdapter().notifyDataSetChanged();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = isRefreshFeed ? apiRefreshFeedAward(
						sortByAsc, limit) : apiRefreshFeedAction();
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if (!sortByAsc) {
					mLoadMore = false;
					for(int i = 0; i < mGridColumn;i++){
						mArrayListAward.remove(mArrayListAward.size()-1);
					}
					mRecyclerView.getAdapter().notifyDataSetChanged();
				}

				if (isSuccess) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
				}else{
					if(sortByAsc){
						if(!isAutoRefresh){
							AndroidUtilities.showSnackBar(AwardRecyclerActivity.this, Utilities.getErrorMessageFromApi(mResponseFromApi));
						}else{//show new awards available
							
						}
					}
				}
				
				if(sortByAsc){
					refreshFeedActionFromApi(isAutoRefresh);
				}

				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				if (mSwipeRefreshLayout.isRefreshing()) {
					mToolBarMenuRefresh.setVisibility(View.VISIBLE);
					mToolBarMenuRefreshProgress.setVisibility(View.GONE);
					mSwipeRefreshLayout.setRefreshing(false);
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}

	/**
	 * AsyncTask : Sent Chat to Server
	 */

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void sendMessage(String mMessage, String mId) {
		if (!TextUtils.isEmpty(mMessage)) {
			if (Utilities.isInternetConnected()) {
				if (AndroidUtilities.isAboveIceCreamSandWich()) {
					new AsyncSendMessageTask(mMessage, mId).executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				} else {
					new AsyncSendMessageTask(mMessage, mId).execute();
				}
			} else {
				Utilities
						.showCrouton(
								AwardRecyclerActivity.this,
								mCroutonViewGroup,
								getResources().getString(
										R.string.internet_unavailable),
								Style.ALERT);
			}
		}
	}

	private String apiSendMessage(String mMessage, String mCategory, String mId) {
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostMessage(mMessage,
					mId, mCategory);
			if (BuildVars.USE_OKHTTP) {
				return RetroFitClient.postJSON(new OkHttpClient(),
						AppConstants.API.API_SUBMIT_AWARD_MESSAGE,
						jsonObj.toString(), TAG);
			} else {
				return RestClient.postJSON(AppConstants.API.API_SUBMIT_AWARD_MESSAGE,
						jsonObj, TAG);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		try {
			if (!TextUtils.isEmpty(mResponseFromApi)) {
//				Utilities.showCrouton(AwardRecyclerActivity.this,mCroutonViewGroup,Utilities.getSuccessMessageFromApi(mResponseFromApi),Style.CONFIRM);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	public class AsyncSendMessageTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private String mMessage;
		private String mId;
		private MobcastProgressDialog mProgressDialog;
		private LoadToast mLoadToast;

		public AsyncSendMessageTask(String mMessage, String mId) {
			this.mMessage = mMessage;
			this.mId = mId;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*mProgressDialog = new MobcastProgressDialog(
					AwardRecyclerActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication()
					.getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();*/
			mLoadToast = new LoadToast(AwardRecyclerActivity.this);
			mLoadToast.setText(ApplicationLoader.getApplication()
					.getResources().getString(R.string.loadingMessage));
			mLoadToast.setProgressColor(ApplicationLoader.getApplication().getResources().getColor(R.color.toolbar_background));
			mLoadToast.setTranslationY(170);
			mLoadToast.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = apiSendMessage(mMessage,
						AppConstants.INTENTCONSTANTS.AWARD, mId);
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
				
				if(mLoadToast!=null){
					mLoadToast.error();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}

				if (isSuccess) {
					if(mLoadToast!=null){
						mLoadToast.success();
					}
					parseDataFromApi(mResponseFromApi);
				} else {
					mErrorMessage = Utilities.getErrorMessageFromApi(mResponseFromApi);
//					Utilities.showCrouton(AwardRecyclerActivity.this,mCroutonViewGroup, mErrorMessage, Style.ALERT);
					if(mLoadToast!=null){
						mLoadToast.error();
					}
				}
			}catch(Exception e){
				FileLog.e(TAG, e.toString());
			}
		}
	}
	
	/**
	 * Async : Refresh Feed Like + Read count
	 */
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void refreshFeedActionFromApi(boolean isAutoRefresh){
		try{
			if(Utilities.isInternetConnected()){
				if(!isAutoRefresh){
					if(!mSwipeRefreshLayout.isRefreshing()){
						mSwipeRefreshLayout.setRefreshing(true);
					}
				}
				FetchFeedActionAsyncTask mFetchFeedActionAsyncTask = new FetchFeedActionAsyncTask(AwardRecyclerActivity.this, AppConstants.INTENTCONSTANTS.AWARD, JSONRequestBuilder.getPostFetchFeedAction(AppConstants.INTENTCONSTANTS.AWARD), TAG);
				if(AndroidUtilities.isAboveIceCreamSandWich()){
					mFetchFeedActionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				}else{
					mFetchFeedActionAsyncTask.execute();
				}
				mFetchFeedActionAsyncTask.setOnPostExecuteFeedActionTaskListener(new OnPostExecuteFeedActionTaskListener() {
					@Override
					public void onPostExecute(String mResponseFromApi, boolean isSuccess) {
						// TODO Auto-generated method stub
						if(isSuccess){
							updateArrayListAwardObjectWithLatestFeedActionCount();
						}
						mSwipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void updateArrayListAwardObjectWithLatestFeedActionCount(){
		try{
			Cursor mCursor = getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, new String[]{DBConstant.Award_Columns.COLUMN_ID, DBConstant.Award_Columns.COLUMN_AWARD_ID, DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO, DBConstant.Award_Columns.COLUMN_AWARD_READ_NO},  null, null, DBConstant.Award_Columns.COLUMN_AWARD_RECEIVED_DATE_FORMATTED + " DESC");
			if(mCursor!=null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				int mIntAwardId = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_ID);
				int mIntAwardLikeCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_LIKE_NO);
				int mIntAwardViewCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_READ_NO);
				int mIntAwardCongratulateCount = mCursor.getColumnIndex(DBConstant.Award_Columns.COLUMN_AWARD_CONGRATULATE_NO);
				int i = 0;
				do{
					Award Obj = mArrayListAward.get(i);
					if(!Obj.getmFileType().toString().equalsIgnoreCase(AppConstants.MOBCAST.FOOTER)){
						if(Obj.getmId().equalsIgnoreCase(mCursor.getString(mIntAwardId))){
							Obj.setmLikeCount(mCursor.getString(mIntAwardLikeCount));
							Obj.setmViewCount(mCursor.getString(mIntAwardViewCount));
							Obj.setmCongratulatedCount(mCursor.getString(mIntAwardCongratulateCount));
						}
					}
					i++;
				}while(mCursor.moveToNext());
			}
			
			if(mCursor!=null){
				mCursor.close();
			}
			mRecyclerView.getAdapter().notifyDataSetChanged();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void isToApplyGridOrNot(){
		try{
			if(BuildVars.IS_GRID){
				if(AndroidUtilities.getScreenSizeInInches() >= 7.0 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					isGrid = true;
					mGridColumn = 2;
				}	
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
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
