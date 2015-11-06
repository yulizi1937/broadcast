/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.beans.Capture;
import com.application.ui.adapter.CaptureRecyclerAdapter;
import com.application.ui.adapter.CaptureRecyclerAdapter.OnItemClickListener;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.DownloadProgressDialog;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.NumberProgressBar;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.JSONRequestBuilder;
import com.application.utils.ProgressMutliPartEntity;
import com.application.utils.ProgressMutliPartEntity.ProgressListener;
import com.application.utils.Style;
import com.application.utils.ThemeUtils;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
@SuppressWarnings("deprecation")
public class CaptureActivity extends SwipeBackBaseActivity {
	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final int INTENT_PHOTO = 1001;
	private static final int INTENT_CAMERA = 1002;

	private Toolbar mToolBar;

	private FrameLayout mEmptyFrameLayout;

	private AppCompatTextView mEmptyTitleTextView;
	private AppCompatTextView mEmptyMessageTextView;

	private AppCompatTextView mNoteTv;

	private AppCompatButton mEmptyRefreshBtn;

	private AppCompatButton mBrowseBtn;
	private AppCompatButton mTakeAPicBtn;
	private AppCompatButton mSubmitBtn;

	private AppCompatEditText mNoteEv;
	
	private MaterialRippleLayout mToolBarMenuSubmitLayout;

	private ObservableRecyclerView mRecyclerView;

	private FrameLayout mCroutonViewGroup;

	private Context mContext;

	private ArrayList<Capture> mArrayListCapture = new ArrayList<>();

	private CaptureRecyclerAdapter mAdapter;

	private LinearLayoutManager mLinearLayoutManager;
	
	private Uri cameraFileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		setSecurity();
		initToolBar();
		initUi();
		applyTheme();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_capture, menu);
		/*if (AndroidUtilities.isAboveGingerBread()) {
			MenuItem refreshItem = menu
					.findItem(R.id.action_capture_submit);
			if (refreshItem != null) {
				View mView = MenuItemCompat.getActionView(refreshItem);
				mToolBarMenuSubmitLayout = (MaterialRippleLayout) mView
						.findViewById(R.id.toolBarActionItemSubmit);
				mToolBarMenuSubmitLayout
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View mView) {
								// TODO Auto-generated method stub
								apiSubmitCapture();
							}
						});
			}
		}*/
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(CaptureActivity.this);
			return true;
		case R.id.action_capture_submit:
			apiSubmitCapture();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
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

	private void initUi() {
		mContext = CaptureActivity.this;

		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		mRecyclerView = (ObservableRecyclerView) findViewById(R.id.scroll_wo);

		mEmptyFrameLayout = (FrameLayout) findViewById(R.id.fragmentEmptyLayout);

		mEmptyTitleTextView = (AppCompatTextView) findViewById(R.id.layoutEmptyTitleTv);
		mEmptyMessageTextView = (AppCompatTextView) findViewById(R.id.layoutEmptyMessageTv);
		mNoteTv = (AppCompatTextView) findViewById(R.id.fragmentCaptureNoteCharacterTv);

		mEmptyRefreshBtn = (AppCompatButton) findViewById(R.id.layoutEmptyRefreshBtn);
		mBrowseBtn = (AppCompatButton) findViewById(R.id.fragmentCaptureBrowseBtn);
		mTakeAPicBtn = (AppCompatButton) findViewById(R.id.fragmentCaptureTakeAPicBtn);
		mSubmitBtn = (AppCompatButton) findViewById(R.id.fragmentCaptureSubmitBtn);

		mNoteEv = (AppCompatEditText) findViewById(R.id.fragmentCaptureNoteEd);

		mLinearLayoutManager = new LinearLayoutManager(mContext);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		checkDataInAdapter();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.CaptureActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void checkDataInAdapter() {
		if (mArrayListCapture != null && mArrayListCapture.size() > 0) {
			setRecyclerAdapter();
		} else {
			setEmptyData();
		}
	}

	private void setEmptyData() {
		mEmptyTitleTextView.setText(getResources().getString(
				R.string.emptyCaptureTitle));
		mEmptyMessageTextView.setText(getResources().getString(
				R.string.emptyCaptureMessage));
		mEmptyRefreshBtn.setVisibility(View.GONE);
		mRecyclerView.setVisibility(View.GONE);
		mEmptyFrameLayout.setVisibility(View.VISIBLE);
	}

	private void setRecyclerAdapter() {
		mEmptyFrameLayout.setVisibility(View.GONE);
		mRecyclerView.setVisibility(View.VISIBLE);
		mAdapter = new CaptureRecyclerAdapter(mContext, mArrayListCapture);
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
				.addItemDecoration(new HorizontalDividerItemDecoration.Builder(
						this)
						.color(Utilities.getDividerColor())
						.marginResId(
								R.dimen.fragment_recyclerview_award_left_margin,
								R.dimen.fragment_recyclerview_award_right_margin)
						.build());
	}

	private void setUiListener() {
		addTextWatcher();
		setFocusListener();
		setClickListener();
		setRecyclerAdapterListener();
		setMaterialRippleView();
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(CaptureActivity.this).applyThemeButton(CaptureActivity.this, CaptureActivity.this, mToolBar, mBrowseBtn);
			ThemeUtils.getInstance(CaptureActivity.this).applyThemeButton(CaptureActivity.this, CaptureActivity.this, mToolBar, mTakeAPicBtn);
			ThemeUtils.getInstance(CaptureActivity.this).applyThemeButton(CaptureActivity.this, CaptureActivity.this, mToolBar, mSubmitBtn);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void setRecyclerAdapterListener() {
		if (mAdapter != null) {
			mAdapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					switch (view.getId()) {
					case R.id.itemRecyclerCaptureDeleteIv:
						mArrayListCapture.remove(position);
						notifyAdapterOnDataChanged();
						break;
					}
				}
			});
		}
	}

	private void setMaterialRippleView() {
		try {
			setMaterialRippleOnView(mBrowseBtn);
			setMaterialRippleOnView(mTakeAPicBtn);
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	private void addTextWatcher() {
		mNoteEv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence mCharsequence, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				if (mCharsequence.length() > 500) {
					mNoteTv.setTextColor(Color.RED);
				} else {
					mNoteTv.setTextColor(Utilities.getAppColor());
				}
				mNoteTv.setText(mCharsequence.length() + "/500");
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
	}

	private void setFocusListener() {
		mNoteEv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					mNoteEv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.shape_editbox_selected));
					ThemeUtils.getInstance(CaptureActivity.this).applyThemeEditText(CaptureActivity.this, CaptureActivity.this, mNoteEv);
				} else {
					mNoteEv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.shape_editbox_normal));
				}
			}
		});
	}

	private void setClickListener() {
		mBrowseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				getPhotoFromGallery();
			}
		});
		
		mTakeAPicBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				getPhotoFromCamera();
			}
		});
		
		mSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				apiSubmitCapture();
			}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void apiSubmitCapture(){
			if (Utilities.isInternetConnected()) {
				if(mArrayListCapture.size() > 0){
					if (AndroidUtilities.isAboveIceCreamSandWich()) {
						new AsyncCaptureTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
					} else {
						new AsyncCaptureTask().execute();
					}
				}else{
					Utilities.showCrouton(
							CaptureActivity.this,
							mCroutonViewGroup,
							getResources().getString(
									R.string.capture_validation),
							Style.ALERT);	
				}
			} else {
				Utilities.showCrouton(
						CaptureActivity.this,
						mCroutonViewGroup,
						getResources().getString(
								R.string.internet_unavailable),
						Style.ALERT);
			}
	}

	private void getPhotoFromGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, INTENT_PHOTO);
	}
	
	private void getPhotoFromCamera() {
		Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraFileUri =  Uri.fromFile(getOutputMediaFile());
		takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT,cameraFileUri);
		startActivityForResult(takePicIntent, INTENT_CAMERA);
	}
	
	/**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", cameraFileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        cameraFileUri = savedInstanceState.getParcelable("file_uri");
    }
	
	 private static File getOutputMediaFile() {
	        // External sdcard location
	        File mediaStorageDir = new File(
	                Environment
	                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	                AppConstants.FOLDER.THUMBNAIL_FOLDER);
	  
	        // Create the storage directory if it does not exist
	        if (!mediaStorageDir.exists()) {
	            if (!mediaStorageDir.mkdirs()) {
	                Log.d(TAG, "Oops! Failed create "
	                        + AppConstants.FOLDER.THUMBNAIL_FOLDER + " directory");
	                return null;
	            }
	        }
	  
	        // Create a media file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	                Locale.getDefault()).format(new java.util.Date());
	        File mediaFile;
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
	        return mediaFile;
	    }
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, mIntent);
		try{
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == INTENT_PHOTO) {
					Uri selectedImage = mIntent.getData();
					String mPicturePath = Utilities.getPath(selectedImage);
					if(!TextUtils.isEmpty(mPicturePath)){
						Capture Obj = new Capture();
						Obj.setmFilePath(mPicturePath);
						Obj.setmFileSize(Utilities
								.formatFileSize(new File(mPicturePath).length()));
						mArrayListCapture.add(Obj);	
					}
					
				}else if (requestCode == INTENT_CAMERA) {
					String mPicturePath = Utilities.getPath(cameraFileUri);
					if(!TextUtils.isEmpty(mPicturePath)){
						Capture Obj = new Capture();
						Obj.setmFilePath(mPicturePath);
						Obj.setmFileSize(Utilities
								.formatFileSize(new File(mPicturePath).length()));
						mArrayListCapture.add(Obj);
					}
				}
				notifyAdapterOnDataChanged();
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void notifyAdapterOnDataChanged() {
		if (mArrayListCapture != null && mArrayListCapture.size() > 0) {
			if (mAdapter != null) {
				if (mArrayListCapture.size() == 1) {
					mRecyclerView.setVisibility(View.VISIBLE);
					mEmptyFrameLayout.setVisibility(View.GONE);
				}
				mRecyclerView.getAdapter().notifyDataSetChanged();
			} else {
				setRecyclerAdapter();
			}
		} else {
			setEmptyData();
		}
	}

	/**
	 * AsyncTask : Capture
	 * @param mResponseFromApi
	 */
	private void parseDataFromApi(String mResponseFromApi) {
		try {
			if (!TextUtils.isEmpty(mResponseFromApi)) {
				mArrayListCapture.clear();
				mNoteEv.setText("");
				notifyAdapterOnDataChanged();
				Utilities.showCrouton(CaptureActivity.this, mCroutonViewGroup,
						Utilities.getSuccessMessageFromApi(mResponseFromApi),
						Style.CONFIRM);
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

	public class AsyncCaptureTask extends AsyncTask<Void, Integer, String> {
		private PowerManager.WakeLock mWakeLock;

		private NumberProgressBar mNumberProgressBar;

		private DownloadProgressDialog mDownloadProgressDialog;

		private AppCompatTextView mNameTextView;
		private AppCompatTextView mFactTextView;
		private AppCompatTextView mDownloadProgressTextView;
		
		private ImageView mImageView;

		private AppCompatButton mCancelBtn;
		
		private String mResponseFromApi;
		private boolean isSuccess = true;
		private String mErrorMessage = "";
		private long totalSize = 0;


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			acquirePowerLock();
			initUi();
			if(mDownloadProgressDialog!=null){
				mDownloadProgressDialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// updating progress bar value
			mNumberProgressBar.setProgress(progress[0]);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mResponseFromApi = apiUploadFileWithParameters();
				isSuccess = Utilities.isSuccessFromApi(mResponseFromApi);
				return mResponseFromApi;
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
				if(mDownloadProgressDialog!=null){
					mDownloadProgressDialog.dismiss();
				}
				releasePowerLock();
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		private String apiUploadFileWithParameters() {
			String responseString = null;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 700000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpPost httppost = new HttpPost(
					AppConstants.API.API_SUBMIT_CAPTURE);
			try {
				ProgressMutliPartEntity entity = new ProgressMutliPartEntity(
						new ProgressListener() {
							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});
				int j = 1;
				for (int i = 0; i < mArrayListCapture.size(); i++) {
					File mFilePath = new File(mArrayListCapture.get(i).getmFilePath());
					entity.addPart(AppConstants.API_KEY_PARAMETER.captureFile + j, new FileBody(mFilePath));
					j++;
				}
				entity.addPart(AppConstants.API_KEY_PARAMETER.accessToken,new StringBody(ApplicationLoader.getPreferences().getAccessToken()));
				entity.addPart(AppConstants.API_KEY_PARAMETER.userName, new StringBody(ApplicationLoader.getPreferences().getUserName()));
				entity.addPart(AppConstants.API_KEY_PARAMETER.captureNote, new StringBody(mNoteEv.getText().toString()));
				entity.addPart(AppConstants.API_KEY_PARAMETER.captureFileCount, new StringBody(String.valueOf(mArrayListCapture.size())));
				entity.addPart(AppConstants.API_KEY_PARAMETER.category, new StringBody(AppConstants.INTENTCONSTANTS.CAPTURE));
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = JSONRequestBuilder
							.getErrorMessageFromStatusCode(
									String.valueOf(statusCode),
									getResources().getString(
											R.string.api_unknown_error))
							.toString();
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}catch(Exception e){
				responseString = JSONRequestBuilder.getErrorMessageFromStatusCode("500", "Please try again!").toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			releasePowerLock();
			if(mDownloadProgressDialog!=null){
				mDownloadProgressDialog.dismiss();
			}
			if (isSuccess) {
				parseDataFromApi(mResponseFromApi);
			} else {
				mErrorMessage = Utilities
						.getErrorMessageFromApi(mResponseFromApi);
				Utilities.showCrouton(CaptureActivity.this, mCroutonViewGroup,
						mErrorMessage, Style.ALERT);
			}
		}
		
		/*
		 * Power Lock
		 */
		@SuppressLint("Wakelock")
		private void acquirePowerLock() {
			try {
				PowerManager pm = (PowerManager) mContext
						.getSystemService(Context.POWER_SERVICE);
				mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						getClass().getName());
				mWakeLock.acquire();
			} catch (Exception e) {
				FileLog.e(TAG, e.toString());
			}
		}

		private void releasePowerLock() {
			try {
				mWakeLock.release();
				mWakeLock = null;
			} catch (Exception e) {

			}
		}
		
		@SuppressWarnings("deprecation")
		private void initUi() {
			mDownloadProgressDialog = new DownloadProgressDialog(mContext);
			mFactTextView = mDownloadProgressDialog.getFactTextView();
			mNameTextView = mDownloadProgressDialog.getFileNameTextView();
			mCancelBtn = mDownloadProgressDialog.getCancelButton();
			mNumberProgressBar = mDownloadProgressDialog.getNumberProgressBar();
			mDownloadProgressTextView = mDownloadProgressDialog.getDownloadProgressTextView();
			mImageView = mDownloadProgressDialog.getImageView();
			
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_image));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_image));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_image));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_image));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_image));
			
			mNameTextView.setVisibility(View.GONE);
			mCancelBtn.setVisibility(View.GONE);
			mDownloadProgressTextView.setVisibility(View.GONE);
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
