/**
 * 
 */
package com.application.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.application.beans.FeedbackPagerInfo;
import com.application.ui.fragment.FeedbackMultipleCheckFragment;
import com.application.ui.fragment.FeedbackRatingFragment;
import com.application.ui.fragment.FeedbackSelectiveRadioFragment;
import com.application.ui.fragment.FeedbackSubjectiveFragment;
import com.application.ui.fragment.QuizMultipleCheckFragment;
import com.application.ui.fragment.QuizSelectiveRadioFragment;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class FeedbackViewPagerAdapter extends FragmentStatePagerAdapter{
	private ArrayList<FeedbackPagerInfo> mArrayListFeedbackPagerInfo;
	/**
	 * @param fm
	 */
	public FeedbackViewPagerAdapter(FragmentManager fm, ArrayList<FeedbackPagerInfo> mArrayListFeedbackPagerInfo) {
		super(fm);
		this.mArrayListFeedbackPagerInfo = mArrayListFeedbackPagerInfo;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		if(mArrayListFeedbackPagerInfo.get(position).getmFeedbackType().equalsIgnoreCase("selective")){
			return FeedbackSelectiveRadioFragment.newInstance(position);	
		}else if(mArrayListFeedbackPagerInfo.get(position).getmFeedbackType().equalsIgnoreCase("multiple")){
			return FeedbackMultipleCheckFragment.newInstance();
		}else if(mArrayListFeedbackPagerInfo.get(position).getmFeedbackType().equalsIgnoreCase("rating")){
			return FeedbackRatingFragment.newInstance();
		}else{
			return FeedbackSubjectiveFragment.newInstance();
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListFeedbackPagerInfo.size();
	}

}
