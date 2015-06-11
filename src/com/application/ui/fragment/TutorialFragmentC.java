/**
 * 
 */
package com.application.ui.fragment;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.utils.AndroidUtilities;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class TutorialFragmentC extends Fragment {
	private static final String TAG = TutorialFragmentC.class.getSimpleName();

	private ImageView mImageViewMobcast;
	private ImageView mImageViewShare;
	private ImageView mGuideImageView;

	private FrameLayout mFrameLayout;

	private LinearLayout mRootLayout;

	private AppCompatTextView mHeaderTv;
	private AppCompatTextView mFunctionalityTv;

	private Animation mAnimSlideInUp;
	private Animation mAnimSlideOutDown;

	private int whichPage;
	private int mWidth;
	private int mHeight;

	public static TutorialFragmentC newInstance(int i) {
		TutorialFragmentC fragment = new TutorialFragmentC();
		fragment.whichPage = i;
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tutorialc, container,
				false);

		mFrameLayout = (FrameLayout) view
				.findViewById(R.id.fragmentTutorialCFrameLayout);

		mRootLayout = (LinearLayout) view
				.findViewById(R.id.fragmentTutorialCLayout);

		mHeaderTv = (AppCompatTextView) view
				.findViewById(R.id.fragmentTutorialCHeaderTitleTv);
		mFunctionalityTv = (AppCompatTextView) view
				.findViewById(R.id.fragmentTutorialCFunctionalityTitleTv);

		mImageViewMobcast = (ImageView) view
				.findViewById(R.id.fragmentTutorialCScrollIv1);
		mImageViewShare = (ImageView) view
				.findViewById(R.id.fragmentTutorialCShareIv);

		mGuideImageView = (ImageView) view
				.findViewById(R.id.fragmentTutorialCPointerIv);

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		addLayoutListener();
		changeFrameLayoutBackgroundColor();
		setAnimation();
	}

	private void initImageLoader() {
		setBitmapFromAsset(mImageViewMobcast, "tutorial/b12_2.png");
		setBitmapFromAsset(mImageViewShare, "tutorial/c12_1.png");
	}

	private void setBitmapFromAsset(ImageView mImageView, String mPath) {
		Bitmap mOriginalBitmap = getBitmapFromAsset(mPath);
		if (mOriginalBitmap != null) {
			mImageView.setImageBitmap(mOriginalBitmap);
		}
	}

	private void setAnimation() {
		mAnimSlideInUp = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_in_up);
		mAnimSlideOutDown = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_out_down);

		setAnimationListener();

		AndroidUtilities.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mImageViewShare.startAnimation(mAnimSlideInUp);
			}
		}, 1000);
	}

	private void setAnimationListener() {

		mAnimSlideInUp.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mImageViewShare.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				AndroidUtilities.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mImageViewShare.startAnimation(mAnimSlideOutDown);
					}
				}, 1000);
			}
		});
		
		mAnimSlideOutDown.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mImageViewShare.setVisibility(View.GONE);
				AndroidUtilities.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mImageViewShare.startAnimation(mAnimSlideInUp);
					}
				}, 1000);
			}
		});
	}

	private void changeFrameLayoutBackgroundColor() {
		switch (whichPage) {
		case 0:
			mRootLayout.setBackgroundColor(Color.parseColor("#FF4521"));
			break;
		case 1:
			mRootLayout.setBackgroundColor(Color.parseColor("#FECC00"));
			break;
		case 2:
			mRootLayout.setBackgroundColor(Color.parseColor("#8E44AD"));
			break;
		case 3:
			mRootLayout.setBackgroundColor(Color.parseColor("#06AE66"));
			break;
		case 4:
			mRootLayout.setBackgroundColor(Color.parseColor("#048EEE"));
			break;
		}
	}

	private void addLayoutListener() {
		ViewTreeObserver vto = mFrameLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mFrameLayout.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				mWidth = mFrameLayout.getMeasuredWidth();
				mHeight = mFrameLayout.getMeasuredHeight();
				initImageLoader();
			}
		});
	}

	private Bitmap getBitmapFromAsset(String filePath) {
		Bitmap bitmap = null;
		try {
			AssetManager assetManager = ApplicationLoader.getApplication()
					.getResources().getAssets();
			InputStream istr;
			istr = assetManager.open(filePath);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			// handle exception
			FileLog.e(TAG, e.toString());
		}
		return bitmap;
	}

}
