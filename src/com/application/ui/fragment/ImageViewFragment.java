/**
 * 
 */
package com.application.ui.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.ui.activity.ImageFullScreenActivity;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageViewFragment extends Fragment {
	private static final String TAG = ImageViewFragment.class.getSimpleName();
	
	private ImageView mImageView;
	private ArrayList<String> mContentFilePath;
	private int mPosition;
	
	public static ImageViewFragment newInstance(int mPosition, ArrayList<String> mContentFilePath) {
		ImageViewFragment fragment = new ImageViewFragment();
		fragment.mContentFilePath = mContentFilePath;
		fragment.mPosition = mPosition;
        return fragment;
    }
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_imageview,
				container, false);
		mImageView = (ImageView)view.findViewById(R.id.fragmentImageView);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUiListener();
		setIntentDataToUi();
	}
	
	private void setUiListener(){
		setOnClickListener();
	}
	
	private void setIntentDataToUi() {
		try {
			mImageView.setImageURI(Uri.parse(mContentFilePath.get(mPosition)));
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}
	
	
	private void setOnClickListener(){
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getActivity(), ImageFullScreenActivity.class);
				String mArray[] = new String[mContentFilePath.size()];
				
				for(int i =0 ;i < mContentFilePath.size() ;i++){
					mArray[i] = mContentFilePath.get(i);
				}
				
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.OBJECT, mArray);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.POSITION, mPosition);
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(getActivity());
			}
		});
	}
}
