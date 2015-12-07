/**
 * 
 */
package com.application.ui.activity;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
import java.util.ArrayList;
import java.util.Comparator;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.beans.Birthday;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.BirthdayRecyclerAdapter;
import com.application.ui.adapter.BirthdayRecyclerAdapter.OnItemClickListener;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.LoadToast;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.MobcastProgressDialog;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
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
public class BirthdayRecyclerActivity extends SwipeBackBaseActivity {
	private static final String TAG = BirthdayRecyclerActivity.class.getSimpleName();
	
	private Toolbar mToolBar;
	
	private TextView mToolBarTitleTv;
	
	private ImageView mToolBarDrawer;
	private ImageView mToolBarMenuRefresh;
	
	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatButton mEmptyRefreshBtn;

	private ProgressWheel mToolBarMenuRefreshProgress;
	
	private ObservableRecyclerView mRecyclerView;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private FrameLayout mCroutonViewGroup;
	
	private BirthdayRecyclerAdapter mAdapter;
	
	private ArrayList<Birthday> mArrayListBirthday;
	
	private Context mContext;

	private LinearLayoutManager mLinearLayoutManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_birthday);
		setSecurity();
		initToolBar();
		initUi();
		syncDataWithApi();
		setMaterialRippleView();
		applyTheme();
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkReadFromDBAndUpdateToObj();
		setUiListener();
		Utilities.showBadgeNotification(BirthdayRecyclerActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_birthday, menu);
	    if(AndroidUtilities.isAboveGingerBread()){
	    	MenuItem searchItem = menu.findItem(R.id.action_search);
		    SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView = null;
		    if (searchItem != null) {
		        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		    }
		    if (searchView != null) {
		        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		    }
		    
		    MenuItem refreshItem = menu.findItem(R.id.action_refresh_actionable);
		    if(refreshItem!=null){
		    	View mView = MenuItemCompat.getActionView(refreshItem);
		    	MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout)mView.findViewById(R.id.toolBarActionItemRefresh);
		    	mToolBarMenuRefreshProgress = (ProgressWheel)mView.findViewById(R.id.toolBarActionItemProgressWheel);
		    	mToolBarMenuRefresh = (ImageView)mView.findViewById(R.id.toolBarActionItemImageView);
		    	mToolBarMenuRefreshLayout.setOnClickListener(new View.OnClickListener() {
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
	        	AndroidUtilities.exitWindowAnimation(BirthdayRecyclerActivity.this);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void toolBarRefresh(){
		if(!mSwipeRefreshLayout.isRefreshing()){
			mSwipeRefreshLayout.setRefreshing(true);
		}
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		refreshFeedFromApi(true, true, 0);
	}
	
	@SuppressLint("NewApi")
	private void refreshFeedFromApi(boolean isRefreshFeed, boolean sortByAsc,
			int limit) {// sortByAsc:true-> new data //sortByAsc:false->Old Data
		if (Utilities.isInternetConnected()) {
			if (AndroidUtilities.isAboveIceCreamSandWich()) {
				new AsyncRefreshTask(isRefreshFeed, sortByAsc, limit)
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
								null, null, null);
			} else {
				new AsyncRefreshTask(isRefreshFeed, sortByAsc, limit).execute();
			}
		}
	}

	private void initUi(){
		mContext = BirthdayRecyclerActivity.this;
		
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll_wo);
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		
		mEmptyFrameLayout = (FrameLayout) findViewById(R.id.fragmentEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) findViewById(R.id.layoutEmptyMessageTv);

		mEmptyRefreshBtn = (AppCompatButton) findViewById(R.id.layoutEmptyRefreshBtn);

		mLinearLayoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);

		ApplicationLoader.getPreferences().setViewIdBirthday("-1");

		checkDataInAdapter();
		
		setSwipeRefreshProgressColorScheme();
	}
	private void setSwipeRefreshProgressColorScheme(){
		mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
	}
	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		mToolBar.setTitle(getResources().getString(R.string.BirthdayRecyclerActivityTitle));
		setSupportActionBar(mToolBar);
	}
	
	private void setRecyclerAdapter(){
		mAdapter = new BirthdayRecyclerAdapter(BirthdayRecyclerActivity.this, mArrayListBirthday);
        if(AndroidUtilities.isAboveIceCreamSandWich()){
        	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
            mRecyclerView.setAdapter(mScaleInAdapter);
        }else{
        	mRecyclerView.setAdapter(mAdapter);
        }
		mRecyclerView.addItemDecoration(
		        new HorizontalDividerItemDecoration.Builder(this)
		                .color(Utilities.getDividerColor())
		                .marginResId(R.dimen.fragment_recyclerview_award_left_margin, R.dimen.fragment_recyclerview_award_right_margin)
		                .build());
	}
	
	private void setUiListener(){
		setRecyclerAdapterListener();
		setSwipeRefreshListener();
//		setMaterialRippleView();
		setClickListener();
	}
	
	private void syncDataWithApi(){
		refreshFeedFromApi(true, true, 0);
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(BirthdayRecyclerActivity.this).applyThemeCapture(BirthdayRecyclerActivity.this, BirthdayRecyclerActivity.this, mToolBar);
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
				refreshFeedFromApi(true, true, AppConstants.BULK);
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
				refreshFeedFromApi(true, true, 0);
			}
		});
	}
	
	private void setEmptyData() {
		mEmptyTitleTextView.setText(getResources().getString(
				R.string.emptyBirthdayTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyBirthdayMessage));
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
	}
	
	private void setRecyclerAdapterListener(){
		if(mAdapter!=null){
			mAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					switch (view.getId()) {
					case R.id.itemRecyclerBirthdayMessageIv:
						showBirthdayMessageDialog(position);
						break;
					case R.id.itemRecyclerBirthdayWishIv:
						if (mArrayListBirthday.get(position).isWished()) {
							Utilities.showCrouton(BirthdayRecyclerActivity.this,mCroutonViewGroup,getResources().getString(R.string.wished_message_validation)+ " "+ mArrayListBirthday.get(position).getmBirthdayUserName(),Style.ALERT);
						} else {
							showBirthdayWishDialog(position);
						}
						break;
					default:
						Intent mIntent = new Intent(BirthdayRecyclerActivity.this, BirthdayProfileActivity.class);
						mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, AppConstants.INTENTCONSTANTS.BIRTHDAY);
						mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, mArrayListBirthday.get(position).getmBirthdayId());
						saveViewPosition(position);
						startActivity(mIntent);
						AndroidUtilities.enterWindowAnimation(BirthdayRecyclerActivity.this);
						break;
					}
				}
			});
		}
	}
	
	/**
	 * <b>Description: </b></br>Expiry : Check Expired Mobcast and delete</br></br>
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void checkExpiredBirthdayAndDeleteFromDB(){
		try{
			long mCurrentTimeMillis = System.currentTimeMillis() - 604800000;
			int  i = getContentResolver().delete(DBConstant.Birthday_Columns.CONTENT_URI, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVED_DATE_FORMATTED + "<?", new String[]{String.valueOf(mCurrentTimeMillis)});
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void checkDataInAdapter() {
		checkExpiredBirthdayAndDeleteFromDB();
		Cursor mCursor = getContentResolver().query(DBConstant.Birthday_Columns.CONTENT_URI,null,null,null,DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID+ " DESC");
		if (mCursor != null && mCursor.getCount() > 0) {
			addBirthdayObjectListFromDBToBeans(mCursor, false, false);
			if (mArrayListBirthday.size() > 0) {
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
	
	public class BirthdaySort implements Comparator<Birthday> {
		@Override
		public int compare(Birthday Obj1, Birthday Obj2) {
			try {
				if (Integer.parseInt(Obj1.getmId()) < Integer.parseInt(Obj2.getmId())) {
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

	private void addBirthdayObjectListFromDBToBeans(Cursor mCursor,
			boolean isFromBroadCastReceiver, boolean isToAddOldData) {
		int mIntBirthdayId = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID);
		int mIntBirthdayName = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_NAME);
		int mIntBirthdayDep = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DEPARTMENT);
		int mIntBirthdayImageLink = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_LINK);
		int mIntBirthdayDate = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE_FORMATTED);
		int mIntBirthdayReceivedDate = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVED_DATE_FORMATTED);
		int mIntBirthdayDOB = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DOB);
		int mIntBirthdayMobile = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVER_MOBILE);
		int mIntBirthdayIsRead = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ);
		int mIntBirthdayIsWished = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_WISHED);
		int mIntBirthdayIsMessage = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MESSAGE);
		int mIntBirthdayIsMale = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MALE);
		int mIntBirthdayCity = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_CITY);
		int mIntBirthdaySunSign = mCursor.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_SUN_SIGN);

		if (!isToAddOldData) {
			mArrayListBirthday = new ArrayList<Birthday>();
		} else {
			if (mArrayListBirthday == null) {
				mArrayListBirthday = new ArrayList<Birthday>();
			}
		}
		mCursor.moveToFirst();
		int i = 0;
		do {
			Birthday Obj = new Birthday();
			String mBirthdayId = mCursor.getString(mIntBirthdayId);
			Obj.setmId(mBirthdayId);
			Obj.setmBirthdayId(mBirthdayId);
			Obj.setmBirthdayUserName(mCursor.getString(mIntBirthdayName));
			Obj.setmBirthdayUserDep(mCursor.getString(mIntBirthdayDep));
			Obj.setmBirthdayUserImage(mCursor.getString(mIntBirthdayImageLink));
			Obj.setmBirthdayDate(mCursor.getString(mIntBirthdayDate));
			Obj.setmReceivedDate(mCursor.getString(mIntBirthdayReceivedDate));
			Obj.setmDOB(mCursor.getString(mIntBirthdayDOB));
			Obj.setmMobile(mCursor.getString(mIntBirthdayMobile));
			Obj.setRead(Boolean.parseBoolean(mCursor.getString(mIntBirthdayIsRead)));
			Obj.setWished(Boolean.parseBoolean(mCursor.getString(mIntBirthdayIsWished)));
			Obj.setMessaged(Boolean.parseBoolean(mCursor.getString(mIntBirthdayIsMessage)));
			Obj.setMale(Boolean.parseBoolean(mCursor.getString(mIntBirthdayIsMale)));
			Obj.setmBirthdayUserCity(mCursor.getString(mIntBirthdayCity));
			Obj.setmBirthdayUserSunSign(mCursor.getString(mIntBirthdaySunSign));
			Obj.setmFileType(AppConstants.BIRTHDAY.DETAIL);

			if(i==0){
				Birthday secObj = new Birthday();
				secObj.setmFileType(AppConstants.BIRTHDAY.SECTION);
				secObj.setmBirthdayId(mBirthdayId);
				secObj.setmEmployeeId(Utilities.getCurrentDay(Integer.parseInt(Obj.getmDOB().substring(5, 7)),Integer.parseInt(Obj.getmDOB().substring(8, 10))));
				mArrayListBirthday.add(secObj);
				i++;
			}else if(mArrayListBirthday.get(i-1).getmFileType().equalsIgnoreCase(AppConstants.BIRTHDAY.DETAIL)){
				if(!mArrayListBirthday.get(i-1).getmDOB().equalsIgnoreCase(Obj.getmDOB())){
					Birthday secObj = new Birthday();
					secObj.setmFileType(AppConstants.BIRTHDAY.SECTION);
					secObj.setmBirthdayId(mBirthdayId);
					secObj.setmEmployeeId(Utilities.getCurrentDay(Integer.parseInt(Obj.getmDOB().substring(5, 7)),Integer.parseInt(Obj.getmDOB().substring(8, 10))));
					mArrayListBirthday.add(secObj);
					i++;
				}
			}

			if (!isFromBroadCastReceiver) {
				mArrayListBirthday.add(Obj);
			} else {
				mArrayListBirthday.add(0, Obj);
			}
			i++;
		} while (mCursor.moveToNext());

//		Collections.sort(mArrayListBirthday, new BirthdaySort());
	}

	private void saveViewPosition(int position) {
		ApplicationLoader.getPreferences().setViewIdBirthday(
				String.valueOf(position));
	}

	private void checkReadFromDBAndUpdateToObj() {
		int position = Integer.parseInt(ApplicationLoader.getPreferences()
				.getViewIdBirthday());
		if (position >= 0 && position < mArrayListBirthday.size()) {
			if (mArrayListBirthday != null && mArrayListBirthday.size() > 0) {
				Cursor mCursor = getContentResolver()
						.query(DBConstant.Birthday_Columns.CONTENT_URI,new String[] {DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID,DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ,DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_LIKE},
								DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + "=?",
								new String[] { mArrayListBirthday.get(position)
										.getmId() }, null);

				if (mCursor != null && mCursor.getCount() > 0) {
					mCursor.moveToFirst();
					boolean isToNotify = false;
					if (Boolean
							.parseBoolean(mCursor.getString(mCursor
									.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ)))) {
						mArrayListBirthday.get(position).setRead(true);
						isToNotify = true;
					}
					
					if (Boolean
							.parseBoolean(mCursor.getString(mCursor
									.getColumnIndex(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_WISHED)))) {
						mArrayListBirthday.get(position).setWished(true);
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
			if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.BIRTHDAY)) {
				Cursor mCursor = null;
				boolean isToAddOldData = false;
				if (mId == -786) {
					mCursor = getContentResolver().query(
							DBConstant.Birthday_Columns.CONTENT_URI,null,DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + "<?",new String[] { mArrayListBirthday.get(mArrayListBirthday.size() - 1).getmId() },
							DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + " DESC");
					isToAddOldData = true;
				} else {
					mCursor = getContentResolver().query(
							DBConstant.Birthday_Columns.CONTENT_URI, null, null,
							null,
							DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + " DESC");
				}
				if (mCursor != null && mCursor.getCount() > 0) {
					mSwipeRefreshLayout.setEnabled(true);
					mSwipeRefreshLayout.setRefreshing(true);
					mCursor.moveToFirst();
					addBirthdayObjectListFromDBToBeans(mCursor, false,
							isToAddOldData);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (mAdapter != null) {
								mAdapter.addBirthdayObjList(mArrayListBirthday);
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
	
	private void showBirthdayWishDialog(final int mPosition){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(BirthdayRecyclerActivity.this)
        .title(getResources().getString(R.string.dialog_birthday_wish_title) + " ?")
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.sample_fragment_settings_dialog_language_negative))
        .negativeColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	if (!mArrayListBirthday.get(mPosition).isWished()) {
					UserReport.updateUserReportApi(mArrayListBirthday.get(mPosition).getmId(),AppConstants.INTENTCONSTANTS.BIRTHDAY,AppConstants.REPORT.WISH, "");
					Utilities.showCrouton(BirthdayRecyclerActivity.this,mCroutonViewGroup,getResources().getString(R.string.congratulate_message)+ " "+ mArrayListBirthday.get(mPosition).getmBirthdayUserName(), Style.CONFIRM);
					ContentValues mValues = new ContentValues();
					mValues.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_WISHED,"true");
					getContentResolver().update(DBConstant.Birthday_Columns.CONTENT_URI, mValues, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + "=?", new String[]{mArrayListBirthday.get(mPosition).getmBirthdayId()});
					mArrayListBirthday.get(mPosition).setWished(true);
					mRecyclerView.getAdapter().notifyItemChanged(mPosition);
				}
            	dialog.dismiss();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
	
	
	private void showBirthdayMessageDialog(final int mPosition) {
		final MaterialDialog mMaterialDialog = new MaterialDialog.Builder(BirthdayRecyclerActivity.this)
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
					sendMessage(mMessageEd.getText().toString(), mArrayListBirthday.get(mPosition).getmId(), String.valueOf(mPosition));
					mMaterialDialog.dismiss();
				}
			}
		});
	}
	
	
	
	/**
	 * AsyncTask To Refresh
	 */

	public String apiRefreshFeedBirthday(boolean sortByAsc, int limit) {
		try {
			JSONObject jsonObj = null;
			jsonObj = JSONRequestBuilder.getPostFetchFeedBirthday(mArrayListBirthday != null ? mArrayListBirthday.get(0).getmBirthdayId() : String.valueOf("0"));
			if (BuildVars.USE_OKHTTP) {
				return RetroFitClient.postJSON(new OkHttpClient(),AppConstants.API.API_FETCH_FEED_BIRTHDAY,jsonObj.toString(), TAG);
			} else {
				return RestClient.postJSON(
						AppConstants.API.API_FETCH_FEED_BIRTHDAY, jsonObj, TAG);
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
				JSONArray mJSONMobMainArrObj = mJSONObj.getJSONArray(AppConstants.API_KEY_PARAMETER.birthday);

				for (int i = 0; i < mJSONMobMainArrObj.length(); i++) {
					JSONObject mJSONMobObj = mJSONMobMainArrObj.getJSONObject(i);

					String mBirthdayId = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayId);
					String mDate = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayCreateDate).substring(0, 11);
					String mTime = "00:00:00";
					String DOB = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayDateOfBirth);
					String mFileLink = mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserProfileImage);
					ContentValues values = new ContentValues();
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID,mBirthdayId);
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DOB,DOB);
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVED_DATE_FORMATTED,Utilities.getMilliSecond(mDate, mTime));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_NAME,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserName));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVER_MOBILE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserMobileNumber));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_RECEIVER_EMAIL,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserEmailId));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_CITY,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserCity));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DEPARTMENT,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserDep));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_SUN_SIGN,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserSunSign));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MALE,mJSONMobObj.getString(AppConstants.API_KEY_PARAMETER.birthdayUserIsMale));
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_FILE_LINK,mFileLink);
					values.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_DATE_FORMATTED,Utilities.getCurrentYear()+DOB.replace("-", "").substring(4, 6));

					getContentResolver().insert(DBConstant.Birthday_Columns.CONTENT_URI, values);

				}

				if (isToAddOldData) {
					passDataToFragment(-786, AppConstants.INTENTCONSTANTS.BIRTHDAY);
				} else {
					passDataToFragment(0, AppConstants.INTENTCONSTANTS.BIRTHDAY);
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
		private int limit;

		public AsyncRefreshTask(boolean isRefreshFeed, boolean sortByAsc,
				int limit) {
			this.isRefreshFeed = isRefreshFeed;
			this.sortByAsc = sortByAsc;
			this.limit = limit;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mArrayListBirthday == null) {
				mProgressDialog = new MobcastProgressDialog(
						BirthdayRecyclerActivity.this);
				mProgressDialog.setMessage(ApplicationLoader.getApplication()
						.getResources().getString(R.string.loadingRefresh));
				mProgressDialog.show();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = apiRefreshFeedBirthday(sortByAsc, limit);
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
				if (isSuccess) {
					parseDataFromApi(mResponseFromApi, !sortByAsc);
				}else{
					if(sortByAsc){
						AndroidUtilities.showSnackBar(BirthdayRecyclerActivity.this, Utilities.getErrorMessageFromApi(mResponseFromApi));
					}
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
	private void sendMessage(String mMessage, String mId, String mPosition) {
		if (!TextUtils.isEmpty(mMessage)) {
			if (Utilities.isInternetConnected()) {
				if (AndroidUtilities.isAboveIceCreamSandWich()) {
					new AsyncSendMessageTask(mMessage, mId, mPosition).executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
				} else {
					new AsyncSendMessageTask(mMessage, mId, mPosition).execute();
				}
			} else {
				Utilities.showCrouton(BirthdayRecyclerActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
			}
		}
	}

	private String apiSendMessage(String mMessage, String mCategory, String mId) {
		try {
			JSONObject jsonObj = JSONRequestBuilder.getPostMessage(mMessage,
					mId, mCategory);
			if (BuildVars.USE_OKHTTP) {
				return RetroFitClient.postJSON(new OkHttpClient(),AppConstants.API.API_SUBMIT_AWARD_MESSAGE,jsonObj.toString(), TAG);
			} else {
				return RestClient.postJSON(AppConstants.API.API_SUBMIT_AWARD_MESSAGE,jsonObj, TAG);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi, String mId, String mPosition) {
		try {
			if (!TextUtils.isEmpty(mResponseFromApi)) {
//				Utilities.showCrouton(BirthdayRecyclerActivity.this,mCroutonViewGroup,Utilities.getSuccessMessageFromApi(mResponseFromApi),Style.CONFIRM);
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_MESSAGE,"true");
				getContentResolver().update(DBConstant.Birthday_Columns.CONTENT_URI, mValues, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_ID + "=?", new String[]{mId});
				mArrayListBirthday.get(Integer.parseInt(mPosition)).setMessaged(true);
				mRecyclerView.getAdapter().notifyItemChanged(Integer.parseInt(mPosition));
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
		private String mPosition;
		private MobcastProgressDialog mProgressDialog;
		private LoadToast mLoadToast;

		public AsyncSendMessageTask(String mMessage, String mId, String mPosition) {
			this.mMessage = mMessage;
			this.mId = mId;
			this.mPosition = mPosition;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*mProgressDialog = new MobcastProgressDialog(BirthdayRecyclerActivity.this);
			mProgressDialog.setMessage(ApplicationLoader.getApplication()
					.getResources().getString(R.string.loadingSubmit));
			mProgressDialog.show();*/
			
			mLoadToast = new LoadToast(BirthdayRecyclerActivity.this);
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
				mResponseFromApi = apiSendMessage(mMessage,AppConstants.INTENTCONSTANTS.BIRTHDAY, mId);
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

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}

			if (isSuccess) {
				if(mLoadToast!=null){
					mLoadToast.success();
				}
				parseDataFromApi(mResponseFromApi, mId,mPosition);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
//				Utilities.showCrouton(BirthdayRecyclerActivity.this,mCroutonViewGroup, mErrorMessage, Style.ALERT);
				if(mLoadToast!=null){
					mLoadToast.error();
				}
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

