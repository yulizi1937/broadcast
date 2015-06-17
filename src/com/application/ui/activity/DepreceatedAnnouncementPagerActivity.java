/**
 * 
 */
package com.application.ui.activity;

import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.beans.AnnouncementPagerHeader;
import com.application.ui.fragment.ViewPagerAdapter;
import com.application.ui.view.SlidingTabLayout;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class DepreceatedAnnouncementPagerActivity extends AppCompatActivity {
	private static final String TAG = DepreceatedAnnouncementPagerActivity.class.getSimpleName();

	private Toolbar mToolBar;
	private TextView mToolBarTitleTv;
	private ImageView mToolBarBackIv;
	private ImageView mToolBarOverFlowIv;
	
	private ViewPager mViewPager;
//	private PagerSlidingTabStrip mPagerSlidingTabStrp;
	private SlidingTabLayout mSlidingTabLayout;
	private ViewPager.SimpleOnPageChangeListener mViewPagerListener;
	
	private HashMap<String, AnnouncementPagerHeader> mHashMapAnnouncementPagerHeader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announcement_pager);
		initUi();
        initToolBar();
        
        getAnnouncementPagerHeader();
        setPagerSlidingTabStrip();
	}
	
	private void initUi(){
		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mPagerSlidingTabStrp = (PagerSlidingTabStrip) findViewById(R.id.pager_sliding_tab_strip);
		mSlidingTabLayout =  (SlidingTabLayout)findViewById(R.id.sliding_tab_layout);
	}
	
	private void initToolBar(){
		mToolBar = (Toolbar)findViewById(R.id.toolbar);
		if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    	mToolBarTitleTv = (TextView)findViewById(R.id.toolbarTitleTv);
    	mToolBarBackIv = (ImageView)findViewById(R.id.toolbarBackIv);
    	mToolBarOverFlowIv = (ImageView)findViewById(R.id.toolbarOverflowIv);
    	setToolBarUiListener();
    }
	
	private void setToolBarUiListener(){
		mToolBarOverFlowIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				showOverFlowMenu();
				String marr2[] = new String[] { "Home","Categories","Top Paid",
						"Top Free", "Top Grossing", "Top New Paid",
						"Top New Free", "Trending" };
				ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(
						getSupportFragmentManager(),mHashMapAnnouncementPagerHeader);
				mViewPager.setAdapter(viewpageradapter);
				mSlidingTabLayout.notifyDataSetChanged(mViewPager);
			}
		});
	}
	
	private void setPagerSlidingTabStrip() {
		String[]marr = new String[]{ "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
			"Top New Free", "Trending" };
		ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(
				getSupportFragmentManager(),mHashMapAnnouncementPagerHeader);
		mViewPager.setAdapter(viewpageradapter);
//		mPagerSlidingTabStrp.setShouldExpand(true);
//		mPagerSlidingTabStrp.setViewPager(mViewPager);
		mSlidingTabLayout.setBackgroundColor(Color.parseColor(getResources().getString(R.string.toolbar_background)));
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mViewPager);
	}
	
	private void getAnnouncementPagerHeader(){
		mHashMapAnnouncementPagerHeader = new HashMap<String, AnnouncementPagerHeader>();
	}
	
	
	private void showOverFlowMenu(){
		PopupMenu mPopMenu = new PopupMenu(DepreceatedAnnouncementPagerActivity.this, mToolBarOverFlowIv,Gravity.TOP);
		mPopMenu.inflate(R.menu.menu_mother);
		mPopMenu.show();
	}
}
