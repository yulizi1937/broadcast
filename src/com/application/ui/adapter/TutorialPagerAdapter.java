/**
 * 
 */
package com.application.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.ui.fragment.TutorialFragmentA;
import com.application.ui.fragment.TutorialFragmentB;
import com.application.ui.fragment.TutorialFragmentC;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class TutorialPagerAdapter extends FragmentStatePagerAdapter {
	private static final int mPageCount = 5;

	/**
	 * @param fm
	 */
	public TutorialPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		if (position % 2 == 0) {
			return TutorialFragmentA.newInstance(position);	
		} else if (position % 3 == 0) {
			return TutorialFragmentC.newInstance(position);
		}else{
			return TutorialFragmentB.newInstance(position);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPageCount;
	}

}
