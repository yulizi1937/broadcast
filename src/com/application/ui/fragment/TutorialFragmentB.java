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
public class TutorialFragmentB extends Fragment {
	private static final String TAG = TutorialFragmentB.class.getSimpleName();

	private ImageView mImageViewScroll1;
	private ImageView mImageViewScroll2;
	private ImageView mImageViewDetail;
	private ImageView mGuideImageView;

	private FrameLayout mFrameLayout;

	private LinearLayout mRootLayout;

	private AppCompatTextView mHeaderTv;
	private AppCompatTextView mFunctionalityTv;

	private Animation mAnimSlideInLeft;
	private Animation mAnimSlideOutRight;
	private Animation mAnimSlideInUp;
	private Animation mAnimSlideOutUp;
	

	private int whichPage;
	private int mWidth;
	private int mHeight;
	
	private boolean isScroll1 = false;
	private boolean isScroll2 = false;
	private boolean isDetail = false;
	

	public static TutorialFragmentB newInstance(int i) {
		TutorialFragmentB fragment = new TutorialFragmentB();
		fragment.whichPage = i;
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tutorialb, container,
				false);

		mFrameLayout = (FrameLayout) view
				.findViewById(R.id.fragmentTutorialBFrameLayout);

		mRootLayout = (LinearLayout) view
				.findViewById(R.id.fragmentTutorialBLayout);

		mHeaderTv = (AppCompatTextView) view
				.findViewById(R.id.fragmentTutorialBHeaderTitleTv);
		mFunctionalityTv = (AppCompatTextView) view
				.findViewById(R.id.fragmentTutorialBFunctionalityTitleTv);

		mImageViewScroll1 = (ImageView)view.findViewById(R.id.fragmentTutorialBScrollIv1);
		mImageViewScroll2 = (ImageView)view.findViewById(R.id.fragmentTutorialBScrollIv2);
		
		mImageViewDetail = (ImageView)view.findViewById(R.id.fragmentTutorialBDetailIv);
		
		mGuideImageView = (ImageView) view
				.findViewById(R.id.fragmentTutorialBPointerIv);
		

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
		setBitmapFromAsset(mImageViewScroll1, "tutorial/b11_1.png");
		mImageViewScroll1.setVisibility(View.VISIBLE);

		setBitmapFromAsset(mImageViewScroll2, "tutorial/a12_3.png");
		setBitmapFromAsset(mImageViewDetail, "tutorial/b12_2.png");
	}

	private void setBitmapFromAsset(ImageView mImageView, String mPath) {
		Bitmap mOriginalBitmap = getBitmapFromAsset(mPath);
		if (mOriginalBitmap != null) {
			mImageView.setImageBitmap(mOriginalBitmap);
		}
	}

	private void setAnimation() {
		mAnimSlideOutRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_out_right);
		mAnimSlideInLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_in_left);
		mAnimSlideInUp = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_in_up);
		mAnimSlideOutUp = AnimationUtils.loadAnimation(getActivity(),
				R.anim.tutorial_slide_out_up);

		setAnimationListener();
		
		AndroidUtilities.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mImageViewScroll1.startAnimation(mAnimSlideOutUp);
			}
		}, 1000);
	}
	
	private void setAnimationListener(){
		mAnimSlideOutUp.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mImageViewScroll2.setVisibility(View.VISIBLE);
				mImageViewScroll2.startAnimation(mAnimSlideInUp);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mImageViewScroll1.setVisibility(View.GONE);
			}
		});
		
		mAnimSlideInUp.setAnimationListener(new AnimationListener() {
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
				mImageViewScroll1.setVisibility(View.VISIBLE);
				mImageViewDetail.setVisibility(View.VISIBLE);
				mImageViewDetail.startAnimation(mAnimSlideInLeft);
			}
		});
		
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
				mImageViewDetail.startAnimation(mAnimSlideOutRight);
			}
		});
		
		mAnimSlideOutRight.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				mImageViewScroll1.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mImageViewDetail.setVisibility(View.GONE);
				mImageViewScroll1.startAnimation(mAnimSlideOutUp);
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
