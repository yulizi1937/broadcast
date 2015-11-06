/* HISTORY
 * CATEGORY			 :- VIEW | HELPER 
 * DEVELOPER		 :- VIKALP PATEL
 * AIM      		 :- PROVIDE FRAGMENT FOR TABS
 * NOTE: HANDLE WITH CARE 
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * TU001      VIKALP PATEL     29/07/2014                       CREATED
 * --------------------------------------------------------------------------------------------------------------------
 * 
 * *****************************************METHODS INFORMATION******************************************************** 
 * ********************************************************************************************************************
 * DEVELOPER		  METHOD								DESCRIPTION
 * ********************************************************************************************************************
 * VIKALP PATEL                          			
 * ********************************************************************************************************************
 *
 */

package com.application.ui.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.application.beans.SearchHeader;
import com.application.ui.fragment.MobcastRecyclerViewFragment;
import com.application.ui.fragment.TrainingRecyclerViewFragment;
import com.application.ui.fragment.ViewPagerTabRecyclerViewFragment;
import com.application.utils.CacheFragmentStatePagerAdapter;

/**
 * @author Vikalp Patel (vikalppatelce@yahoo.com)
 * @category Ui Helper
 * 
 */
public class SearchPagerAdapter extends CacheFragmentStatePagerAdapter {
	private static final String TAG = SearchPagerAdapter.class.getSimpleName();
	private ArrayList<SearchHeader> mArrayListSearchHeader;
	private int mScrollY;

	public SearchPagerAdapter(FragmentManager fm,
			ArrayList<SearchHeader> mArrayListSearchHeader) {
		super(fm);
		this.mArrayListSearchHeader = mArrayListSearchHeader;
	}

	// @Override
	// public Fragment getItem(int item) {
	// return SuperAwesomeCardFragment.newInstance(item);
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListSearchHeader.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mArrayListSearchHeader.get(position).getmTitle();
	}

	public void setScrollY(int scrollY) {
		mScrollY = scrollY;
	}

	@Override
	protected Fragment createItem(int position) {
		// Initialize fragments.
		// Please be sure to pass scroll position to each fragments using
		// setArguments.
		Fragment f;
		Bundle args = new Bundle();
		switch (position) {
		case 0:
			f = new MobcastRecyclerViewFragment();
			if (0 <= mScrollY) {
				args.putInt(MobcastRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
			}
			break;
		case 1:
			f = new TrainingRecyclerViewFragment();
			if (0 <= mScrollY) {
				args.putInt(TrainingRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
			}
			break;
		default:
			f = new ViewPagerTabRecyclerViewFragment();
			if (0 <= mScrollY) {
				args.putInt(
						ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION,
						1);
			}
			break;
		}
		f.setArguments(args);
		return f;
	}
}
