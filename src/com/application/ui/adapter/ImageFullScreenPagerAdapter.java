/**
 * 
 */
package com.application.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.ui.fragment.ImageViewFragment;
import com.application.ui.fragment.ImageViewFullScreenFragment;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class ImageFullScreenPagerAdapter extends FragmentStatePagerAdapter{
	private ArrayList<String> mArrayListString;
	/**
	 * @param fm
	 */
	public ImageFullScreenPagerAdapter(FragmentManager fm, ArrayList<String> mArrayListString) {
		super(fm);
		this.mArrayListString = mArrayListString;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
			return ImageViewFullScreenFragment.newInstance();	
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
