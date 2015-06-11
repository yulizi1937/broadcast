/**
 * 
 */
package com.application.ui.fragment;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.application.ui.view.TouchImageView;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageViewFullScreenFragment extends Fragment {
	private static final String TAG = ImageViewFullScreenFragment.class
			.getSimpleName();

	private TouchImageView mTouchImageView;

	public static ImageViewFullScreenFragment newInstance() {
		ImageViewFullScreenFragment fragment = new ImageViewFullScreenFragment();
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_imageview_fullscreen,
				container, false);
		mTouchImageView = (TouchImageView) view
				.findViewById(R.id.fragmentImageViewFullScreen);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
	}

	private void setUiListener() {
	}

}
