/**
 * 
 */
package com.application.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.application.ui.materialdialog.MaterialDialog;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ProgressMaterialDialogWord {
	private Context mContext;
	
	private String mContent;
	
	private MaterialDialog mMaterialDialog;
	
	private View mView;
	
	private AppCompatTextView mAppCompatTextView;
	
	public ProgressMaterialDialogWord(Context mContext, String mContent) {
		this.mContext = mContext;
		this.mContent = mContent;
		mMaterialDialog = new MaterialDialog.Builder(mContext).title("")
				.titleColor(Utilities.getAppColor())
				.customView(R.layout.dialog_progress_word, true)
				.cancelable(false).show();
		mView = mMaterialDialog.getCustomView();
		mAppCompatTextView = (AppCompatTextView) mView
				.findViewById(R.id.dialogProgressWordTv);
	}

	public void showDialog() {
	}

	public void changeContent(String mContent) {
		mAppCompatTextView.setText(mContent);
	}

	public void dismissDialog() {
		if (mMaterialDialog != null) {
			mMaterialDialog.dismiss();
		}
	}
}
