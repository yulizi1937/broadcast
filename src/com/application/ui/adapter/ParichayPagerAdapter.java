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
import com.application.ui.fragment.ParichayFragment;
import com.application.ui.fragment.ParichayReferralFragment;
import com.application.ui.view.RoundedBackgroundSpan;
import com.application.utils.CacheFragmentStatePagerAdapter;

/**
 * @author Vikalp Patel (vikalppatelce@yahoo.com)
 * @category Ui Helper
 * 
 */
public class ParichayPagerAdapter extends CacheFragmentStatePagerAdapter {
	private static final String TAG = ParichayPagerAdapter.class.getSimpleName();
	private ArrayList<MotherHeader> mArrayListMotherHeader;
	private int mScrollY;
	private Fragment mParichayFragment;
	public ParichayPagerAdapter(FragmentManager fm,
			ArrayList<MotherHeader> mArrayListMotherHeader) {
		super(fm);
		this.mArrayListMotherHeader = mArrayListMotherHeader;
	}

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
			f = new ParichayFragment();
			mParichayFragment = f;
			if (0 <= mScrollY) {
				args.putInt(ParichayFragment.ARG_INITIAL_POSITION, 1);
			}
			break;
		default:
			f = new ParichayReferralFragment();
			if (0 <= mScrollY) {
				args.putInt(ParichayReferralFragment.ARG_INITIAL_POSITION, 1);
			}
			break;
		}
		f.setArguments(args);
		return f;
	}
	
	public ParichayFragment getParichayFragment(){
		if(mParichayFragment!=null){
			if(mParichayFragment instanceof ParichayFragment){
				return (ParichayFragment) mParichayFragment;
			}
		}
		return null;
	}
}
