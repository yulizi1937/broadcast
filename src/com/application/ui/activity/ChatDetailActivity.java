/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.MessageObject;
import com.application.sqlite.DBConstant;
import com.application.ui.adapter.ChatListViewAdapter;
import com.application.ui.view.ChatListView;
import com.application.ui.view.MobcastProgressDialog;
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
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ChatDetailActivity extends SwipeBackBaseActivity {
	private static final String TAG = ChatDetailActivity.class.getSimpleName();
	
	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private ChatListView mChatListView;
	private ChatListViewAdapter mAdapter;
	
	private ImageView mChatSendIv;
	private AppCompatEditText mChatMessageEd;
	
	private ArrayList<MessageObject> mArrayListMessageObject = new ArrayList<>();
	
	private Intent mIntent;
	private String mId;
	private String mContentUserName;
	private String mContentDp;
	private String mContentDesc;
	private String mFrom;
	private String mTo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_detail);
		setSecurity();
		initToolBar();
		initUi();
		getIntentData();
		addMessageObjectFromDBToList();
		setChatListView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		registerReceiver(mBroadCastReceiver, new IntentFilter(NotificationsController.BROADCAST_CHAT_ACTION));
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mBroadCastReceiver);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(ChatDetailActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
				R.string.TextDetailActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}
	
	private void initUi(){
		mChatListView  = (ChatListView)findViewById(R.id.fragmentChatListView);
		mChatMessageEd = (AppCompatEditText)findViewById(R.id.fragmentChatMessageEv);
		mChatSendIv = (ImageView)findViewById(R.id.fragmentChatSendIv);
	}
	
	private void getIntentData(){
		try{
			mFrom = ApplicationLoader.getPreferences().getChatFrom();
			mTo = ApplicationLoader.getPreferences().getChatTo();
			mToolBar.setTitle(ApplicationLoader.getPreferences().getChatOppositePerson());
			/*mIntent = getIntent();
			mToolBar.setTitle(mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.DISPLAYNAME));
			mFrom = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.FROM);
			mTo = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.TO);*/
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent mIntent) {
				String mCategory = mIntent.getStringExtra(AppConstants.INTENTCONSTANTS.CATEGORY);
			if (mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.CHAT)){
				addMessageObjectFromDBOnPush();
				}
			try {
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}
		}
	};
	
	private void addMessageObjectFromDBOnPush(){
		Cursor mCursor = getContentResolver().query(DBConstant.Chat_Columns.CONTENT_URI, null, null, null, DBConstant.Chat_Columns.COLUMN_ID + " DESC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			MessageObject Obj = new MessageObject();
			Obj.setThisUserSentRight(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Chat_Columns.COLUMN_ISLEFT))));
			Obj.setMessageText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Chat_Columns.COLUMN_MESSAGE)));
			mArrayListMessageObject.add(Obj);
			
			if(mAdapter==null){
				setChatListView();
			}
			mChatListView.post(new Runnable() {
				@Override
				public void run() {
					try {
						mChatListView.setSelectionFromTop(mArrayListMessageObject.size() - 1,-100000- mChatListView.getPaddingTop());
						mChatMessageEd.setText("");
						mChatMessageEd.setEnabled(true);
					} catch (Exception e) {
						Log.i(TAG, e.toString());
					}
				}
			});
		}
		
		mCursor.close();
	}
	
	private void setChatListView(){
		if(mAdapter == null && mArrayListMessageObject!=null && mArrayListMessageObject.size() > 0){
			mAdapter = new ChatListViewAdapter(ChatDetailActivity.this, mArrayListMessageObject);
			mChatListView.setAdapter(mAdapter);
		}
	}
	
	private void addMessageObjectFromDBToList(){
		Cursor mCursor = getContentResolver().query(DBConstant.Chat_Columns.CONTENT_URI, null, null, null, DBConstant.Chat_Columns.COLUMN_ID + " ASC");
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			do {
				MessageObject Obj = new MessageObject();
				Obj.setMessageText(mCursor.getString(mCursor.getColumnIndex(DBConstant.Chat_Columns.COLUMN_MESSAGE)));
				Obj.setThisUserSentRight(Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(DBConstant.Chat_Columns.COLUMN_ISLEFT))));
				Obj.setMessageType(0);
				mArrayListMessageObject.add(Obj);
			} while (mCursor.moveToNext());
		}
		
		if(mCursor!=null){
			mCursor.close();
		}
		
	}
	
	private void setUiListener(){
		setClickListener();
	}
	
	private void setClickListener(){
		mChatSendIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void sendMessage(){
		if (!TextUtils.isEmpty(mChatMessageEd.getText().toString())) {
			if (Utilities.isInternetConnected()) {
					if (AndroidUtilities.isAboveIceCreamSandWich()) {
						new AsyncSendMessageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
					} else {
						new AsyncSendMessageTask().execute();
					}
			} else {
				Utilities.showCrouton(
						ChatDetailActivity.this,mCroutonViewGroup,getResources().getString(R.string.internet_unavailable),Style.ALERT);
			}
		}
	}
	
	/*
	 * AsyncTask : Sent Chat to Server
	 */
	
	private String apiSendMessage() {
		try {
				JSONObject jsonObj = JSONRequestBuilder.getPostChatMessage(mChatMessageEd.getText().toString(), mFrom, mTo);
				if(BuildVars.USE_OKHTTP){
					return RetroFitClient.postJSON(new OkHttpClient(), AppConstants.API.API_SEND_CHAT, jsonObj.toString(), TAG);	
				}else{
					return RestClient.postJSON(AppConstants.API.API_SEND_CHAT, jsonObj, TAG);	
				}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	private void parseDataFromApi(String mResponseFromApi) {
		try{
			if(!TextUtils.isEmpty(mResponseFromApi)){
				ContentValues mValues = new ContentValues();
				mValues.put(DBConstant.Chat_Columns.COLUMN_ISLEFT, "true");
				mValues.put(DBConstant.Chat_Columns.COLUMN_MESSAGE, mChatMessageEd.getText().toString());
				getContentResolver().insert(DBConstant.Chat_Columns.CONTENT_URI, mValues);
				if(mAdapter==null){
					setChatListView();
				}
				mChatListView.post(new Runnable() {
					@Override
					public void run() {
						try {
							mChatListView.setSelectionFromTop(mArrayListMessageObject.size() - 1,-100000- mChatListView.getPaddingTop());
							mChatMessageEd.setText("");
							mChatMessageEd.setEnabled(true);
						} catch (Exception e) {
							Log.i(TAG, e.toString());
						}
					}
				});
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public class AsyncSendMessageTask extends AsyncTask<Void, Void, Void> {
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private MobcastProgressDialog mProgressDialog;

		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mChatMessageEd.setEnabled(false);
			MessageObject Obj = new MessageObject();
			Obj.setThisUserSentRight(true);
			Obj.setMessageText(mChatMessageEd.getText().toString());
			mArrayListMessageObject.add(Obj);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				mResponseFromApi = apiSendMessage();
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
			
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi);
			} else {
				mArrayListMessageObject.remove(mArrayListMessageObject.size()-1);
				mChatMessageEd.setEnabled(true);
				mErrorMessage = Utilities.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(ChatDetailActivity.this, mCroutonViewGroup,
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
