/**
 * 
 */
package com.application.ui.fragment;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.ui.view.TouchImageView;
import com.application.utils.FileLog;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageViewFullScreenFragment extends Fragment {
	private static final String TAG = ImageViewFullScreenFragment.class
			.getSimpleName();

	private TouchImageView mTouchImageView;
	private int mPosition;
	private List<String> mContentFilePath;

	public static ImageViewFullScreenFragment newInstance(int mPosition, List<String> mContentFilePath) {//HDFC
		ImageViewFullScreenFragment fragment = new ImageViewFullScreenFragment();
		fragment.mContentFilePath = mContentFilePath;
		fragment.mPosition = mPosition;
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
		setUiWithIntentData();
	}

	private void setUiListener() {
	}
	
	private void setUiWithIntentData(){
		try{
			mTouchImageView.setImageURI(Uri.parse(mContentFilePath.get(mPosition)));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
}
