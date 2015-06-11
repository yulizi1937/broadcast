/**
 * 
 */
package com.application.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class MobcastProgressDialog extends AppCompatDialog{
	private ImageView mImageView;
	private Animation mAnimationRotate;

	public MobcastProgressDialog(Context mContext) {
		super(mContext, R.style.MobcastProgressDialog);
		WindowManager.LayoutParams mLayoutParams = getWindow()
				.getAttributes();
		mLayoutParams.gravity = 1;
		getWindow().setAttributes(mLayoutParams);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress_word, null);
		mImageView = (ImageView)mView.findViewById(R.id.dialogProgressWordIv);
		LinearLayout.LayoutParams mRootLayoutParams = new LinearLayout.LayoutParams(
				-1, -2);
		addContentView(mView, mRootLayoutParams);
		WindowManager.LayoutParams mLayoutParams2 = getWindow()
				.getAttributes();
		mLayoutParams2.dimAmount = 0.8F;
		getWindow().setAttributes(mLayoutParams2);
		getWindow().addFlags(2);
		mAnimationRotate = AnimationUtils.loadAnimation(mContext, R.anim.rotate_infinite);
	}

	public void show() {
		mImageView.setBackgroundResource(R.drawable.splash_logo);
		setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface paramAnonymousDialogInterface) {
				mImageView.startAnimation(mAnimationRotate);
			}
		});
		super.show();
	}
}