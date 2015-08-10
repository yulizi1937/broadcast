/**
 * 
 */
package com.application.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.beans.QuizPagerInfo;
import com.application.ui.fragment.QuizMultipleCheckFragment;
import com.application.ui.fragment.QuizSelectiveRadioFragment;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class QuizViewPagerAdapter extends FragmentStatePagerAdapter{
	private ArrayList<QuizPagerInfo> mArrayListQuizPagerInfo;
	/**
	 * @param fm
	 */
	public QuizViewPagerAdapter(FragmentManager fm, ArrayList<QuizPagerInfo> mArrayListQuizPagerInfo) {
		super(fm);
		this.mArrayListQuizPagerInfo = mArrayListQuizPagerInfo;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		if(mArrayListQuizPagerInfo.get(position).getmQuizType().equalsIgnoreCase("selective")){
			return QuizSelectiveRadioFragment.newInstance(position, mArrayListQuizPagerInfo.get(position));	
		}else{
			return QuizMultipleCheckFragment.newInstance(position, mArrayListQuizPagerInfo.get(position));
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListQuizPagerInfo.size();
	}

}
