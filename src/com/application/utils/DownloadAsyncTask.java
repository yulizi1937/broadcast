/**
 * 
 */
package com.application.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.ImageView;

import com.application.ui.view.DownloadProgressDialog;
import com.application.ui.view.NumberProgressBar;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class DownloadAsyncTask extends AsyncTask<String, String, String> {
	private PowerManager.WakeLock mWakeLock;

	private NumberProgressBar mNumberProgressBar;

	private DownloadProgressDialog mDownloadProgressDialog;

	private AppCompatTextView mNameTextView;
	private AppCompatTextView mFactTextView;
	private AppCompatTextView mDownloadProgressTextView;

	private AppCompatButton mCancelBtn;

	private ImageView mImageView;

	private long mFileSize = 0;
	private long mFileDownloadedSize = 0;

	private Context mContext;

	private int mType;
	private String mFilePath;
	private String mFileURL;
	
	private boolean isThumbnail;
	private boolean isToEncrypt;
	
	private String TAG;
	
	OnPostExecuteListener onPostExecuteListener;

	public void setOnPostExecuteListener(
			OnPostExecuteListener onPostExecuteListener) {
		this.onPostExecuteListener = onPostExecuteListener;
	}

	public interface OnPostExecuteListener {
		public abstract void onPostExecute(boolean isDownloaded);
	}

	public DownloadAsyncTask(Context mContext, boolean isThumbnail,
			boolean isToEncrypt, String mFileURL, String mFilePath, int mType,
			long mFileSize, String TAG) {
		this.mContext = mContext;
		this.mFileURL = mFileURL;
		this.mFilePath = mFilePath;
		this.mType = mType;
		this.mFileSize = mFileSize;
		this.isToEncrypt = isToEncrypt;
		this.isThumbnail = isThumbnail;
		this.TAG = TAG;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (mContext != null) {
			acquirePowerLock();
			initUi();
			if(mDownloadProgressDialog!=null){
				mDownloadProgressDialog.show();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			return String.valueOf(downloadFileWithProgress(new OkHttpClient(), mFileURL, mFilePath, isThumbnail, isToEncrypt));
		}catch(Exception e){
			return "false";
		}
	}

	@Override
	protected void onPostExecute(String isDownloaded) {
		// TODO Auto-generated method stub
		super.onPostExecute(isDownloaded);
		try{
			releasePowerLock();
			dismissDialog();
			if (onPostExecuteListener != null) {
				onPostExecuteListener.onPostExecute(Boolean.parseBoolean(isDownloaded));
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCancelled(String result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}

	/*
	 * 
	 */

	@SuppressWarnings("deprecation")
	private void initUi() {
		mDownloadProgressDialog = new DownloadProgressDialog(mContext);
		mFactTextView = mDownloadProgressDialog.getFactTextView();
		mNameTextView = mDownloadProgressDialog.getFileNameTextView();
		mCancelBtn = mDownloadProgressDialog.getCancelButton();
		mNumberProgressBar = mDownloadProgressDialog.getNumberProgressBar();
		mDownloadProgressTextView = mDownloadProgressDialog.getDownloadProgressTextView();
		mImageView = mDownloadProgressDialog.getImageView();
		
		mNameTextView.setText(Utilities.getFileName(mFileURL));
		switch(mType){
		case AppConstants.TYPE.VIDEO:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_video));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_video));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_video));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_video));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_video));
			break;
		case AppConstants.TYPE.IMAGE:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_image));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_image));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_image));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_image));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_image));
			break;
		case AppConstants.TYPE.AUDIO:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_audio));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_audio));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_audio));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_audio));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_audio));
			break;
		case AppConstants.TYPE.PDF:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_pdf));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_pdf));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_pdf));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_pdf));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_pdf));
			break;
		case AppConstants.TYPE.DOC:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_doc));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_doc));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_doc));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_doc));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_doc));
			break;
		case AppConstants.TYPE.XLS:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_xls));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_xls));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_xls));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_xls));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_xls));
			break;
		case AppConstants.TYPE.PPT:
			mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_ppt));
			mDownloadProgressTextView.setTextColor(mContext.getResources().getColor(R.color.item_ppt));
			mNumberProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.item_ppt));
			mNumberProgressBar.setProgressTextColor(mContext.getResources().getColor(R.color.item_ppt));
			mNameTextView.setTextColor(mContext.getResources().getColor(R.color.item_ppt));
			break;
		}
	}
	
	/*
	 * Download File with Okhttp
	 */
	
	public boolean downloadFileWithProgress(OkHttpClient okHttpClient, String url, String filePath, boolean isThumbnail, boolean isEncrypt) {
		try {
			long startMilliSeconds = System.currentTimeMillis();
			Request request = new Request.Builder().url(url).build();
			Response mResponseObj = okHttpClient.newCall(request).execute();
			if (mResponseObj.isSuccessful()) {
				InputStream is = mResponseObj.body().byteStream();
				if (is != null) {
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(new File(Utilities.getFilePath(mType, isThumbnail, Utilities.getFileName(mFilePath))));
						byte data[] = new byte[1024];
						int bufferLength = 0;
						while ((bufferLength = is.read(data)) > 0) {
							fos.write(data, 0, bufferLength);
							//SA Increment Number Progress Bar
							mFileDownloadedSize += bufferLength;
							/*((Activity) mContext).runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									float perFloat = ((float)mFileDownloadedSize/mFileSize) * 100;
//									mNumberProgressBar.incrementProgressBy((int)perFloat);
									mNumberProgressBar.setProgress((int)perFloat);
									mDownloadProgressTextView.setText(Utilities.formatFileSize(mFileDownloadedSize) +" / " + Utilities.formatFileSize(mFileSize));
								}
							});*/
                         AndroidUtilities.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									float perFloat = ((float)mFileDownloadedSize/mFileSize) * 100;
//									mNumberProgressBar.incrementProgressBy((int)perFloat);
									mNumberProgressBar.setProgress((int)perFloat);
									mDownloadProgressTextView.setText(Utilities.formatFileSize(mFileDownloadedSize) +" / " + Utilities.formatFileSize(mFileSize));									
								}
							});
							//EA Increment Number Progress Bar
						}
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					} finally {
						try {
							fos.flush();
							fos.close();
							is.close();
							if(isEncrypt){
								Utilities.fbConcealEncryptFile(TAG, new File(filePath));
							}
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						}
					}

					if (BuildVars.DEBUG_API) {
						Log.i(TAG, url);
						long timeTaken = (System.currentTimeMillis()-startMilliSeconds);
						Log.i(TAG, timeTaken + " millisec");
					}
					return true;
				}
			}else{
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
			return false;
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
			return false;
		}
		return false;
	}

	private void dismissDialog(){
		if(mDownloadProgressDialog!=null){
			mDownloadProgressDialog.dismiss();
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
}
