/**
 * 
 */
package com.application.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.utils.AppConstants;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class DownloadProgressDialog extends AppCompatDialog {
	private ImageView mImageView;

	private AppCompatTextView mNameTextView;
	private AppCompatTextView mFactTextView;
	private AppCompatTextView mDownloadProgressTextView;

	private AppCompatButton mCancelBtn;

	private NumberProgressBar mNumberProgressBar;

	public DownloadProgressDialog(Context mContext) {
		super(mContext, R.style.MobcastProgressDialog);
		WindowManager.LayoutParams mLayoutParams = getWindow().getAttributes();
		mLayoutParams.gravity = 1;
		getWindow().setAttributes(mLayoutParams);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_progress_download, null);
		mImageView = (ImageView) mView
				.findViewById(R.id.dialogProgressDownloadIv);
		mNameTextView = (AppCompatTextView) mView
				.findViewById(R.id.dialogProgressDownloadNameTv);
		mFactTextView = (AppCompatTextView) mView
				.findViewById(R.id.dialogProgressDownloadFactsTv);
		mCancelBtn = (AppCompatButton) mView
				.findViewById(R.id.dialogProgressDownloadCancelBtn);
		mDownloadProgressTextView = (AppCompatTextView)mView.findViewById(R.id.dialogProgressDownloadProgressTv);
		mNumberProgressBar = (NumberProgressBar) mView
				.findViewById(R.id.dialogProgressDownloadProgressBar);
		LinearLayout.LayoutParams mRootLayoutParams = new LinearLayout.LayoutParams(
				-1, -2);
		addContentView(mView, mRootLayoutParams);
		WindowManager.LayoutParams mLayoutParams2 = getWindow().getAttributes();
		mLayoutParams2.dimAmount = 0.8F;
		getWindow().setAttributes(mLayoutParams2);
		getWindow().addFlags(2);
	}

	public void show() {
		setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface paramAnonymousDialogInterface) {
			}
		});
		mCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		super.show();
	}

	public void setFileName(String mFileName) {
		mNameTextView.setText(mFileName);
	}

	public void setFact(String mFact) {
		mFactTextView.setText(mFact);
	}

	public void setImageView(int mType) {
		int resId = R.drawable.media_doc_blue;
		switch (mType) {
		case AppConstants.TYPE.VIDEO:
			resId = R.drawable.ic_item_audio;
		}
		mImageView.setImageResource(resId);
	}

	public void setProgressChange(int mProgress) {
		if (mProgress == 100) {
			dismiss();
		} else {
			mNumberProgressBar.incrementProgressBy(mProgress);
		}
	}

	public NumberProgressBar getNumberProgressBar() {
		return mNumberProgressBar;
	}
	
	public AppCompatTextView getDownloadProgressTextView(){
		return mDownloadProgressTextView;
	}

	public AppCompatTextView getFactTextView() {
		return mFactTextView;
	}

	public AppCompatTextView getFileNameTextView() {
		return mNameTextView;
	}

	public AppCompatButton getCancelButton() {
		return mCancelBtn;
	}
	
	public ImageView getImageView(){
		return mImageView;
	}

}