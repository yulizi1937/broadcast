/**
 * 
 */
package com.application.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.application.ui.adapter.SimpleItemRecyclerAdapter;
import com.application.ui.adapter.SimpleItemRecyclerAdapter.OnItemClickListener;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class SimpleRecyclerItemActivity extends SwipeBackBaseActivity {
	private static final String TAG = SimpleRecyclerItemActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private ObservableRecyclerView mRecyclerView;
	
	private SimpleItemRecyclerAdapter mAdapter;
	
	private ArrayList<String> mList;
	
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		getIntentData();
		initToolBar();
		initUi();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setRecycleAdapter();
		setUiListener();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_event_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(SimpleRecyclerItemActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll);
	}
	
	private void getIntentData(){
		mIntent = getIntent();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(R.string.CategoryActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private void setUiListener() {
		setOnClickListener();
		setRecyclerAdapterListener();
	}

	private void setOnClickListener() {
	}
	
	private void setRecycleAdapter(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(SimpleRecyclerItemActivity.this);
	    mRecyclerView.setLayoutManager(layoutManager);
	    mList = getList();
		if (mList != null) {
	    	mAdapter = new SimpleItemRecyclerAdapter(SimpleRecyclerItemActivity.this, mList);	
	    	mRecyclerView.setAdapter(mAdapter);
			mRecyclerView.addItemDecoration(
			        new HorizontalDividerItemDecoration.Builder(this)
			                .color(Utilities.getDividerColor())
			                .marginResId(R.dimen.fragment_language_divider_margin_left, R.dimen.fragment_language_divider_margin_left)
			                .build());
	    }
	}
	
	private void setRecyclerAdapterListener(){
		mAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				switch(view.getId()){
				case R.id.itemSimpleLayout:
					Intent mIntent = new Intent();
					mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mList.get(position));
					setResult(Activity.RESULT_OK, mIntent);
					finish();
					break;
				}
			}
		});
	}
	
	private ArrayList<String> getList(){
		try {
            mList= new ArrayList<String>();
            String [] mArray = mIntent.getStringArrayExtra(AppConstants.INTENTCONSTANTS.CATEGORYARRAY);
            for(String mString : mArray){
            	mList.add(mString);
            }
            return mList;
        } catch (Exception e) {
            FileLog.e("tmessages", e);
            return null;
        }
	}
}
