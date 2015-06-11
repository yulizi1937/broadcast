/**
 * 
 */
package com.application.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.beans.QuizScorePagerInfo;
import com.application.ui.fragment.QuizScoreFragment;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class QuizScoreViewPagerAdapter extends FragmentStatePagerAdapter{
	private ArrayList<QuizScorePagerInfo> mArrayListQuizScorePagerInfo;
	/**
	 * @param fm
	 */
	public QuizScoreViewPagerAdapter(FragmentManager fm, ArrayList<QuizScorePagerInfo> mArrayListQuizScorePagerInfo) {
		super(fm);
		this.mArrayListQuizScorePagerInfo = mArrayListQuizScorePagerInfo;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
			return new QuizScoreFragment().newInstance(mArrayListQuizScorePagerInfo.get(position));
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListQuizScorePagerInfo.size();
	}

}
