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
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import com.application.beans.MotherHeader;
import com.application.ui.fragment.ChatRecyclerViewFragment;
import com.application.ui.fragment.MISRecyclerViewFragment;
import com.application.ui.fragment.MobcastRecyclerViewFragment;
import com.application.ui.fragment.TrainingRecyclerViewFragment;
import com.application.ui.view.RoundedBackgroundSpan;
import com.application.utils.CacheFragmentStatePagerAdapter;

/**
 * @author Vikalp Patel (vikalppatelce@yahoo.com)
 * @category Ui Helper
 * 
 */
public class MotherPagerAdapter extends CacheFragmentStatePagerAdapter {
	private static final String TAG = MotherPagerAdapter.class.getSimpleName();
	private ArrayList<MotherHeader> mArrayListMotherHeader;
	private int mScrollY;

	public MotherPagerAdapter(FragmentManager fm,
			ArrayList<MotherHeader> mArrayListMotherHeader) {
		super(fm);
		this.mArrayListMotherHeader = mArrayListMotherHeader;
	}

	// @Override
	// public Fragment getItem(int item) {
	// return SuperAwesomeCardFragment.newInstance(item);
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListMotherHeader.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return getSpannableStringWithCounter(position);
	}

	public SpannableStringBuilder getSpannableStringWithCounter(int position) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		String mHeaderText = mArrayListMotherHeader.get(position).getmTitle()
				+ "  ";
		if (mArrayListMotherHeader.get(position).getmUnreadCount()
				.equalsIgnoreCase("0")) {
			return stringBuilder.append(mHeaderText);
		} else {
			String mCounterText = mArrayListMotherHeader.get(position)
					.getmUnreadCount() + " ";
			stringBuilder.append(mHeaderText);
			stringBuilder.append(mCounterText);
			stringBuilder.setSpan(new RoundedBackgroundSpan(),
					mHeaderText.length() - 1, mHeaderText.length()
							+ mCounterText.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return stringBuilder;
		}
	}

	public void setScrollY(int scrollY) {
		mScrollY = scrollY;
	}
	
	public void notifyDataSetChanged(ArrayList<MotherHeader> mArrayListMotherHeader){
		this.mArrayListMotherHeader = mArrayListMotherHeader;
		notifyDataSetChanged();
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
		/*case 1:
			f = new MISRecyclerViewFragment();
			if (0 <= mScrollY) {
				args.putInt(ChatRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
			}
			break;*/
		default:
			f = new ChatRecyclerViewFragment();
			if (0 <= mScrollY) {
				args.putInt(ChatRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
			}
			break;
		}
		f.setArguments(args);
		return f;
	}
}
