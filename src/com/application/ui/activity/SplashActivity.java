/**
 * 
 */
package com.application.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.ShimmerFrameLayout;
import com.application.utils.AndroidUtilities;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.ThemeUtils;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class SplashActivity extends SwipeBackBaseActivity {
	private static final String TAG = SplashActivity.class.getSimpleName();

	private AppCompatTextView mAppNameTv;

	private ImageView mAppLogoIv;

	private ImageView mAppPdfIv;

	private SystemBarTintManager mTintManager;

	private Animation mAnimRotate;

	private ValueAnimator animatorPdf;

	private FrameLayout mLayout;

	private ShimmerFrameLayout mShimmerFrameLayout;

	private int mWidth;
	private int mHeight;

	private int mDelay = 3000;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setFullScreenA();
		super.onCreate(savedInstanceState);
		setSecurity();
		setContentView(R.layout.activity_splash);
		initUi();
		applyTheme();
		setAnimation();
		redirectToScreen();
	}

	@Override
	public void onResume() {
		super.onResume();
        if(mShimmerFrameLayout!=null){
            mShimmerFrameLayout.startShimmerAnimation();
        }
	}

	@Override
	public void onPause() {
        if(mShimmerFrameLayout!=null){
            mShimmerFrameLayout.stopShimmerAnimation();
//            removeResourcesFromMemory();
        }
		super.onPause();
	}

	private void initUi() {
		mAppNameTv = (AppCompatTextView) findViewById(R.id.activitySplashAppNameTv);

		mAppLogoIv = (ImageView) findViewById(R.id.activitySplashLogoIv);
		// mAppPdfIv = (ImageView) findViewById(R.id.activitySplashPdfIv);

		mShimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.activitySplashShimmerLayout);

		mLayout = (FrameLayout) findViewById(R.id.activitySplashLayout);

		addLayoutListener();

		addShimmerAnimation();
	}
	
	private void applyTheme(){
		try{
			ThemeUtils.getInstance(SplashActivity.this).applyThemeSplash(SplashActivity.this, SplashActivity.this, mLayout);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	private void addShimmerAnimation() {
		// mShimmerFrameLayout.setDuration(2000);
		// mShimmerFrameLayout.setAngle(ShimmerFrameLayout.MaskAngle.CW_90);
	}

	private void addLayoutListener() {
		ViewTreeObserver vto = mLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mLayout.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				mWidth = mLayout.getMeasuredWidth();
				mHeight = mLayout.getMeasuredHeight();
			}
		});
	}

	private void redirectToScreen() {
		if (ApplicationLoader.getPreferences().getAccessToken() != null
				&& ApplicationLoader.getPreferences().getUserName() != null) {
			mDelay = 1000;
		}

		AndroidUtilities.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (ApplicationLoader.getPreferences().getAccessToken() != null
						&& ApplicationLoader.getPreferences().getUserName() != null) {
					Intent mIntent = new Intent(SplashActivity.this,
							MotherActivity.class);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(SplashActivity.this);
					finish();
				} else {
					if(!ApplicationLoader.getPreferences().isAttemptedToLoginDidntReceiveOTP()){
						Intent mIntent = new Intent(SplashActivity.this,
								LoginActivity.class);
						startActivity(mIntent);
						AndroidUtilities.enterWindowAnimation(SplashActivity.this);
						finish();
					}else{
						Intent mIntent = new Intent(SplashActivity.this,
								VerificationActivity.class);
						startActivity(mIntent);
						AndroidUtilities.enterWindowAnimation(SplashActivity.this);
						finish();
					}
				}
			}
		}, mDelay);
	}
	
	 private void removeResourcesFromMemory(){
	        try{
	            mShimmerFrameLayout = null;
	            Drawable mDrawable = mAppLogoIv.getDrawable();
	            if (mDrawable instanceof BitmapDrawable) {
	                BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
	                Bitmap bitmap = bitmapDrawable.getBitmap();
	                bitmap.recycle();
	            }
	            mAppLogoIv = null;
	        }catch(Exception e){
	            FileLog.e(TAG, e.toString());
	        }
	    }

	private void setAnimation() {
		mAnimRotate = AnimationUtils
				.loadAnimation(this, R.anim.rotate_infinite);
		mAppLogoIv.startAnimation(mAnimRotate);

		/*
		 * mAppPdfIv.setVisibility(View.VISIBLE); setAnimator();
		 * animatorPdf.start();
		 */
	}

	private void setAnimator() {
		animatorPdf = ValueAnimator.ofFloat(0, 1); // values from 0 to 1
		animatorPdf.setDuration(2000); // 5 seconds duration from 0 to 1
		animatorPdf
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float value = ((Float) (animation.getAnimatedValue()))
								.floatValue();
						// Set translation of your view here. Position can be
						// calculated
						// out of value. This code should move the view in a
						// half circle.
						ViewHelper.setTranslationX(mAppPdfIv,
								(float) (200.0 * Math.sin(value * Math.PI)));
						ViewHelper.setTranslationY(mAppPdfIv,
								(float) (200.0 * Math.cos(value * Math.PI)));
					}

				});
	}

	private void setFullScreenA() {
		if (!AndroidUtilities.isAboveLollyPop()) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
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

	/**
	 * Google Analytics
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
