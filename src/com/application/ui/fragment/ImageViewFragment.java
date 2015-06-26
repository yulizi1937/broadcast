/**
 * 
 */
package com.application.ui.fragment;

import android.content.Intent;
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
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ImageViewFragment extends Fragment {
	private static final String TAG = ImageViewFragment.class.getSimpleName();
	
	private ImageView mImageView;
	private boolean isTraining = false;//HDFC
	
	public static ImageViewFragment newInstance(boolean isTraining) {//HDFC
		ImageViewFragment fragment = new ImageViewFragment();
		fragment.isTraining = isTraining;
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
		initUiWithDataForWastingTime();//HDFC
	}
	
	private void setUiListener(){
		setOnClickListener();
	}
	
	@SuppressWarnings("deprecation")
	private void initUiWithDataForWastingTime(){//HDFC
		if(isTraining){
			mImageView.setImageDrawable(getResources().getDrawable(R.drawable.adityapuri));
		}
	}
	
	private void setOnClickListener(){
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getActivity(), ImageFullScreenActivity.class);
				mIntent.putExtra(AppConstants.INTENTCONSTANTS.TRAINING, isTraining);//HDFC
				startActivity(mIntent);
				AndroidUtilities.enterWindowAnimation(getActivity());
			}
		});
	}
}
