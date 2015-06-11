/**
 * 
 */
package com.application.ui.fragment;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.ui.activity.TutorialActivity;
import com.application.utils.AndroidUtilities;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.mobcast.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class TutorialFragmentA extends Fragment {
	private static final String TAG = TutorialFragmentA.class.getSimpleName();

	private ImageView mImageViewSlider1;
	private ImageView mImageViewSlider2;
	private ImageView mImageViewSlider3;
	private ImageView mGuideImageView;
	private ImageView mDrawerImageView;
	private ImageView mImageViewHeader;

	private FrameLayout mFrameLayout;

	private LinearLayout mRootLayout;

	private AppCompatTextView mHeaderTv;
	private AppCompatTextView mFunctionalityTv;

	private Animation mAnimSlideInLeft;
	private Animation mAnimSlideInRight;
	private Animation mAnimSlideOutRight;
	private Animation mAnimSlideOutLeft;
	private Animation mAnimDrawerOutLeft;

	private int whichPage;
	private int mWidth;
	private int mHeight;

	private int mHeaderWidth;
	private int mHeaderHeight;

	private int mSliderHeight;
	private int mSliderWidth;

	private int mDrawerHeight;
	private int mDrawerWidth;
	
	private boolean isSlider2 = false;
	private boolean isSlider3 = false;
	private boolean isDrawer = false;
	private boolean isDrawerStart = false;
	

	public static TutorialFragmentA newInstance(int i) {
		TutorialFragmentA fragment = new TutorialFragmentA();
		fragment.whichPage = i;
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tutoriala, container,
				false);

		mFrameLayout = (FrameLayout) view
				.findViewById(R.id.fragmentTutorialFrameLayout);

		mRootLayout = (LinearLayout) view
				.findViewById(R.id.fragmentTutorialLayout);

		mHeaderTv = (AppCompatTextView) view
				.findViewById(R.id.fragmentTutorialHeaderTitleTv);
		mFunctionalityTv = (AppCompatTextView) view
				.findViewById(R.id.fragmentTutorialFunctionalityTitleTv);

		mImageViewSlider1 = (ImageView) view
				.findViewById(R.id.fragmentTutorialSliderIv1);
		mImageViewSlider2 = (ImageView) view
				.findViewById(R.id.fragmentTutorialSliderIv2);
		mImageViewSlider3 = (ImageView) view
				.findViewById(R.id.fragmentTutorialSliderIv3);
		mImageViewHeader = (ImageView) view
				.findViewById(R.id.fragmentTutorialHeaderIv);
		mGuideImageView = (ImageView) view
				.findViewById(R.id.fragmentTutorialPointerIv);
		mDrawerImageView = (ImageView) view
				.findViewById(R.id.fragmentTutorialDrawerIv);

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
		setBitmapFromAsset(mImageViewHeader, "tutorial/a11_1.png");
		mImageViewHeader.setVisibility(View.VISIBLE);

		setBitmapFromAsset(mImageViewSlider1, "tutorial/a12_1.png");
		mImageViewSlider1.setVisibility(View.VISIBLE);

		setBitmapFromAsset(mImageViewSlider2, "tutorial/a12_2.png");
		setBitmapFromAsset(mImageViewSlider3, "tutorial/a12_3.png");
		setBitmapFromAsset(mDrawerImageView, "tutorial/a1_4.png");
	}

	private void setBitmapFromAsset(ImageView mImageView, String mPath) {
		Bitmap mOriginalBitmap = getBitmapFromAsset(mPath);
		if (mOriginalBitmap != null) {
			mImageView.setImageBitmap(mOriginalBitmap);
		}
	}

	private void setAnimation() {
		mAnimSlideOutLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_out_left);
		mAnimDrawerOutLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_out_left);
		mAnimSlideOutRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_out_right);
		mAnimSlideInLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_in_left);
		mAnimSlideInRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_in_right);

		mAnimSlideInLeft.setAnimationListener(new AnimationListener() {
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
				if(!isSlider2){
					mImageViewSlider1.setVisibility(View.GONE);
					mImageViewSlider2.setVisibility(View.VISIBLE);
					setBitmapFromAsset(mImageViewHeader, "tutorial/a11_2.png");
					isSlider2 = true;
				}
				
				if(!isSlider3){
					mImageViewSlider2.setVisibility(View.GONE);
					mImageViewSlider3.setVisibility(View.VISIBLE);
					setBitmapFromAsset(mImageViewHeader, "tutorial/a11_3.png");
					isSlider3 = true;
				}

			}
		});

		mAnimSlideInRight.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mDrawerImageView.setVisibility(View.VISIBLE);
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
						mDrawerImageView.startAnimation(mAnimDrawerOutLeft);
					}
				}, 500);
			}
		});

		mAnimSlideOutLeft.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if(!isSlider2){
					mImageViewSlider2.setVisibility(View.VISIBLE);
					mImageViewSlider2.startAnimation(mAnimSlideInLeft);
					return;
				}
				if(isDrawerStart){
					return;
				}
				if(!isSlider3){
					mImageViewSlider3.setVisibility(View.VISIBLE);
					mImageViewSlider3.startAnimation(mAnimSlideInLeft);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if(!isSlider2){
					mImageViewSlider1.setVisibility(View.GONE);
					setBitmapFromAsset(mImageViewHeader, "tutorial/a11_2.png");
					isSlider2 = true;
					mImageViewSlider2.startAnimation(mAnimSlideOutLeft);
					return;
				}
				
				if(!isSlider3){
					mImageViewSlider2.setVisibility(View.GONE);
					mImageViewSlider3.setVisibility(View.VISIBLE);
					setBitmapFromAsset(mImageViewHeader, "tutorial/a11_3.png");
					isSlider3 = true;
					mDrawerImageView.startAnimation(mAnimSlideInRight);
					AndroidUtilities.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							isDrawerStart = true;
						}
					}, 500);
				}
			}
		});
		
		mAnimDrawerOutLeft.setAnimationListener(new AnimationListener() {
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
					mDrawerImageView.setVisibility(View.GONE);
					mImageViewSlider1.setVisibility(View.VISIBLE);
					mImageViewSlider3.setVisibility(View.GONE);
					isDrawer = true;
					isDrawerStart = false;
					isSlider2 = false;
					isSlider3 = false;
					setBitmapFromAsset(mImageViewHeader, "tutorial/a11_1.png");
					mImageViewSlider1.startAnimation(mAnimSlideOutLeft);
			}
		});
		
		mAnimSlideOutRight.setAnimationListener(new AnimationListener() {
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

			}
		});

		AndroidUtilities.runOnUIThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mImageViewSlider1.startAnimation(mAnimSlideOutLeft);
			}
		}, 1000);
		
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
