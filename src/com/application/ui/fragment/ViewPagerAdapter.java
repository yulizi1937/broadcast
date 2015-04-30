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

package com.application.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import com.application.beans.AnnouncementPagerHeader;
import com.application.ui.view.RoundedBackgroundSpan;

/**
 * @author Vikalp Patel (vikalppatelce@yahoo.com)
 * @category Ui Helper
 * 
 */
public class ViewPagerAdapter extends FragmentPagerAdapter/*implements
		IconTabProvider */{
	private static final String TAG = ViewPagerAdapter.class.getSimpleName();
	private HashMap<String, AnnouncementPagerHeader> mHashMapAnnouncementPagerHeader;
	private ArrayList<AnnouncementPagerHeader> mArrayListAnnouncementPagerHeader;

	public ViewPagerAdapter(FragmentManager fm, HashMap<String, AnnouncementPagerHeader> mHashMapAnnouncementPagerHeader) {
		super(fm);
		this.mHashMapAnnouncementPagerHeader = mHashMapAnnouncementPagerHeader;
		mArrayListAnnouncementPagerHeader = new ArrayList<AnnouncementPagerHeader>();
		mArrayListAnnouncementPagerHeader.addAll(mHashMapAnnouncementPagerHeader.values());
	}

	@Override
	public Fragment getItem(int item) {
		return SuperAwesomeCardFragment.newInstance(item);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHashMapAnnouncementPagerHeader.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return getSpannableStringWithCounter(position);
	}
	
	public SpannableStringBuilder getSpannableStringWithCounter(int position){
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		String mHeaderText = mArrayListAnnouncementPagerHeader.get(position).getmTitle()+"  ";
		if(mArrayListAnnouncementPagerHeader.get(position).getmUnreadCount().equalsIgnoreCase("0")){
			return stringBuilder.append(mHeaderText);
		}else{
			String mCounterText = mArrayListAnnouncementPagerHeader.get(position).getmUnreadCount()+" ";
			stringBuilder.append(mHeaderText);
			stringBuilder.append(mCounterText);
			stringBuilder.setSpan(new RoundedBackgroundSpan(), mHeaderText.length()-1, mHeaderText.length()+mCounterText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return stringBuilder;	
		}
	}
	
	/*@Override
	public int getPageIconResId(int position) {
		// TODO Auto-generated method stub
		return ICONS[position];
	}*/
}
