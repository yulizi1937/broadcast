/**
 * 
 */
package com.application.ui.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.application.beans.Language;
import com.application.ui.adapter.LanguageRecyclerAdapter;
import com.application.ui.adapter.LanguageRecyclerAdapter.OnItemClickListener;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.ObservableRecyclerView;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.Utilities;
import com.google.analytics.tracking.android.EasyTracker;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class LanguageActivity extends SwipeBackBaseActivity {
	private static final String TAG = LanguageActivity.class.getSimpleName();

	private Toolbar mToolBar;

	private FrameLayout mCroutonViewGroup;
	
	private ObservableRecyclerView mRecyclerView;
	
	private LanguageRecyclerAdapter mAdapter;
	
	private ArrayList<Language> mListLanguage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
		setSecurity();
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
		inflater.inflate(R.menu.menu_image_fullscreen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			AndroidUtilities.exitWindowAnimation(LanguageActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);
		
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll);
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(R.string.LanguageActivityTitle));
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
		LinearLayoutManager layoutManager = new LinearLayoutManager(LanguageActivity.this);
	    mRecyclerView.setLayoutManager(layoutManager);
	    mListLanguage = getListLanguage();
	    if(getListLanguage()!=null){
	    	mAdapter = new LanguageRecyclerAdapter(LanguageActivity.this, mListLanguage);	
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
				case R.id.itemLanugageLayout:
					Intent mIntent = new Intent();
					mIntent.putExtra(AppConstants.INTENTCONSTANTS.LANGUAGE, mListLanguage.get(position).getmLanguage());
					mIntent.putExtra(AppConstants.INTENTCONSTANTS.LANGUAGECODE, mListLanguage.get(position).getmLanguageCode());
					setResult(Activity.RESULT_OK, mIntent);
					finish();
					break;
				}
			}
		});
	}
	
	private ArrayList<Language> getListLanguage(){
		try {
            InputStream stream = ApplicationLoader.applicationContext.getResources().getAssets().open("language.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            ArrayList<Language> mListLanguage = new ArrayList<Language>();
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(";");
                Language c = new Language();
                c.setmLanguage(args[0]);
                c.setmLanguageCode(args[1]);
                mListLanguage.add(c);
            }
            reader.close();
            stream.close();
            return mListLanguage;
        } catch (Exception e) {
            FileLog.e("tmessages", e);
            return null;
        }
	}
	
	/**
	 * Google Analytics v3
	 */
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
