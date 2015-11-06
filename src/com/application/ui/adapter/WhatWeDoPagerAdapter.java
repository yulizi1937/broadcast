/**
 * 
 */
package com.application.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.ui.fragment.WhatWeDoFragment;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class WhatWeDoPagerAdapter extends FragmentStatePagerAdapter{
	private static final String TAG = WhatWeDoPagerAdapter.class.getSimpleName();
	private static int PAGE_COUNT = 4;
	/**
	 * @param fm
	 */
	public WhatWeDoPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
			return WhatWeDoFragment.newInstance(position);	
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}
