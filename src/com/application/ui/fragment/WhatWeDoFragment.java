/**
 * 
 */
package com.application.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.application.ui.view.PanningView;
import com.application.utils.FileLog;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class WhatWeDoFragment extends Fragment {
	private static final String TAG = ImageViewFragment.class.getSimpleName();

	private PanningView mBgImageView;
	private ImageView mFgImageView;
	private int mPosition;
	private int mWidth;
	private int mHeight;

	public static WhatWeDoFragment newInstance(int mPosition) {
		WhatWeDoFragment fragment = new WhatWeDoFragment();
		fragment.mPosition = mPosition;
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_whatwedo, container,
				false);
		mBgImageView = (PanningView) view.findViewById(R.id.fragmentWhatWeDoIv1);
		mFgImageView = (ImageView) view.findViewById(R.id.fragmentWhatWeDoIv2);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getWidthHeightOfScreen();
		setIntentDataToUi();
	}
	
	private void getWidthHeightOfScreen(){
		WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		mWidth = display.getWidth();
		mHeight = display.getHeight();
		
		FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(mWidth, mHeight, Gravity.CENTER);
		mBgImageView.setLayoutParams(mLayoutParams);
	}

	@SuppressWarnings("deprecation")
	private void setIntentDataToUi() {
		try {
			switch(mPosition){
			case 0:
				mBgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearebg1));
				mFgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearefg1));
				break;
			case 1:
				mBgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearebg2));
				mFgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearefg2));
				break;
			case 2:
				mBgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearebg3));
				mFgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearefg3));
				break;
			case 3:
				mBgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearebg4));
				mFgImageView.setImageDrawable(getResources().getDrawable(R.drawable.whatwearefg4));
				break;
			}
			mBgImageView.startPanning();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

}
