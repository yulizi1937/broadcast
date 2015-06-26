/**
 * 
 */
package com.application.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.ui.fragment.ImageViewFragment;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class ImageDetailPagerAdapter extends FragmentStatePagerAdapter{
	private ArrayList<String> mArrayListString;
	private boolean isTraining;//HDFC
	/**
	 * @param fm
	 */
	public ImageDetailPagerAdapter(FragmentManager fm, ArrayList<String> mArrayListString, boolean isTraining) {//HDFC
		super(fm);
		this.mArrayListString = mArrayListString;
		this.isTraining = isTraining;//HDFC
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
			return ImageViewFragment.newInstance(isTraining);//DFC	
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListString.size();
	}

}
