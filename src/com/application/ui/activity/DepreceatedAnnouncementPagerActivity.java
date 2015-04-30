/**
 * 
 */
package com.application.ui.activity;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.beans.AnnouncementPagerHeader;
import com.application.sqlite.DBConstant;
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
        
        ContentValues values = new ContentValues();
        values.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE, "2015-02-19");
        values.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED, "19 Feb'15");
        values.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ, "1");
        getContentResolver().insert(DBConstant.Announcement_Columns.CONTENT_URI, values);
        
        ContentValues values1 = new ContentValues();
        values1.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE, "2015-02-19");
        values1.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED, "19 Feb'15");
        values1.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ, "1");
        getContentResolver().insert(DBConstant.Announcement_Columns.CONTENT_URI, values1);
        
        ContentValues values2 = new ContentValues();
        values2.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE, "2015-02-18");
        values2.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED, "18 Feb'15");
        values2.put(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ, "0");
        getContentResolver().insert(DBConstant.Announcement_Columns.CONTENT_URI, values2);
        
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
		
		String projection[] = new String[] {
				DBConstant.Announcement_Columns.COLUMN_ID,
				DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_ID,
				DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED,
				DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE,
				DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ};
		
		Cursor mCursor = getContentResolver().query(
				DBConstant.Announcement_Columns.CONTENT_URI, projection, null,null,
				DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE + " DESC");
		
		if(mCursor!=null && mCursor.getCount() > 0){
			mCursor.moveToFirst();
			int mColumnDate = mCursor.getColumnIndex(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE);
			int mColumnDateFormatted = mCursor.getColumnIndex(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE_FORMATTED);
			int mColumnRead = mCursor.getColumnIndex(DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_IS_READ);
			do {
				AnnouncementPagerHeader obj = new AnnouncementPagerHeader();
				obj.setmTitle(mCursor.getString(mColumnDateFormatted));
				
				String mDate = mCursor.getString(mColumnDate);
				int mReadCounter = 0;
				Cursor mReadCursor = getContentResolver().query(DBConstant.Announcement_Columns.CONTENT_URI, projection, DBConstant.Announcement_Columns.COLUMN_ANNOUNCE_DATE + " =?", new String[]{mDate}, null);
				if(mReadCursor!=null && mReadCursor.getCount() > 0){
					mReadCursor.moveToFirst();
					do {
						mReadCounter+=Integer.parseInt(mReadCursor.getString(mColumnRead));
					} while (mReadCursor.moveToNext());
					obj.setmUnreadCount(String.valueOf(mReadCounter));
				}else{
					obj.setmUnreadCount("0");	
				}
				mReadCursor.close();
				mHashMapAnnouncementPagerHeader.put(mDate, obj);
			} while (mCursor.moveToNext());
		}
		
		mCursor.close();
	}
	
	
	private void showOverFlowMenu(){
		PopupMenu mPopMenu = new PopupMenu(DepreceatedAnnouncementPagerActivity.this, mToolBarOverFlowIv,Gravity.TOP);
		mPopMenu.inflate(R.menu.mother);
		mPopMenu.show();
	}
}
