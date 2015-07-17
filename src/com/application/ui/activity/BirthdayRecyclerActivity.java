/**
 * 
 */
package com.application.ui.activity;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.beans.Birthday;
import com.application.ui.adapter.BirthdayRecyclerAdapter;
import com.application.ui.adapter.BirthdayRecyclerAdapter.OnItemClickListener;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.HorizontalDividerItemDecoration;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ObservableRecyclerView;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.SimpleSectionedRecyclerViewAdapter;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.Style;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class BirthdayRecyclerActivity extends SwipeBackBaseActivity {
	private static final String TAG = BirthdayRecyclerActivity.class.getSimpleName();
	
	private Toolbar mToolBar;
	
	private TextView mToolBarTitleTv;
	
	private ImageView mToolBarDrawer;
	private ImageView mToolBarMenuRefresh;
	
	private ProgressWheel mToolBarMenuRefreshProgress;
	
	
	private ObservableRecyclerView mRecyclerView;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private FrameLayout mCroutonViewGroup;
	
	private BirthdayRecyclerAdapter mAdapter;
	
	private ArrayList<Birthday> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler_birthday);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_birthday, menu);
	    if(AndroidUtilities.isAboveGingerBread()){
	    	MenuItem searchItem = menu.findItem(R.id.action_search);
		    SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView = null;
		    if (searchItem != null) {
		        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		    }
		    if (searchView != null) {
		        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		    }
		    
		    MenuItem refreshItem = menu.findItem(R.id.action_refresh_actionable);
		    if(refreshItem!=null){
		    	View mView = MenuItemCompat.getActionView(refreshItem);
		    	MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout)mView.findViewById(R.id.toolBarActionItemRefresh);
		    	mToolBarMenuRefreshProgress = (ProgressWheel)mView.findViewById(R.id.toolBarActionItemProgressWheel);
		    	mToolBarMenuRefresh = (ImageView)mView.findViewById(R.id.toolBarActionItemImageView);
		    	mToolBarMenuRefreshLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View mView) {
						// TODO Auto-generated method stub
						toolBarRefresh();
					}
				});
		    }
	    }
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		  switch (item.getItemId()) {
	        case R.id.action_refresh_actionable:
	            toolBarRefresh();
	            return true;
	        case android.R.id.home:
	        	finish();
	        	AndroidUtilities.exitWindowAnimation(BirthdayRecyclerActivity.this);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void toolBarRefresh(){
		mToolBarMenuRefresh.setVisibility(View.GONE);
		mToolBarMenuRefreshProgress.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mToolBarMenuRefresh.setVisibility(View.VISIBLE);
				mToolBarMenuRefreshProgress.setVisibility(View.GONE);				
			}
		}, 5000);
	}

	private void initUi(){
		mCroutonViewGroup = (FrameLayout)findViewById(R.id.croutonViewGroup);
		mRecyclerView = (ObservableRecyclerView)findViewById(R.id.scroll_wo);
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		setSwipeRefreshProgressColorScheme();
	}
	private void setSwipeRefreshProgressColorScheme(){
		mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
	}
	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		mToolBar.setTitle(getResources().getString(R.string.BirthdayRecyclerActivityTitle));
		setSupportActionBar(mToolBar);
	}
	
	private void setRecycleAdapter(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(BirthdayRecyclerActivity.this);
	    mRecyclerView.setLayoutManager(layoutManager);
	    mList = new ArrayList<Birthday>();
		for (int i = 0; i < 3; i++) {
	    	Birthday obj = new Birthday();
	    	if(i==0){
	    		obj.setmBirthdayUserDep("Sales");
		    	obj.setmBirthdayUserName("Ashwin Roy");
	    	}else if(i == 1){
	    		obj.setmBirthdayUserDep("Technology");
		    	obj.setmBirthdayUserName("Vikalp Patel");
	    	}else {
	    		obj.setmBirthdayUserDep("HR");
		    	obj.setmBirthdayUserName("Vandana Pandey");
	    	}
	    	mList.add(obj);
	    }
		mAdapter = new BirthdayRecyclerAdapter(BirthdayRecyclerActivity.this, mList);
		
		//This is the code to provide a sectioned list
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"August 22 2015, WEDNESDAY"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(1,"October 26 2015, FRIDAY"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(2,"December 28 2015, MONDAY"));

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                  SimpleSectionedRecyclerViewAdapter(this,R.layout.section_layout_birthday_heading,R.id.sectionBirthdayHeader,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        
        if(AndroidUtilities.isAboveIceCreamSandWich()){
        	AlphaInAnimationAdapter mAlphaAdapter = new AlphaInAnimationAdapter(mSectionedAdapter);
            ScaleInAnimationAdapter mScaleInAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
            mRecyclerView.setAdapter(mScaleInAdapter);
        }else{
        	mRecyclerView.setAdapter(mSectionedAdapter);
        }
		mRecyclerView.addItemDecoration(
		        new HorizontalDividerItemDecoration.Builder(this)
		                .color(Utilities.getDividerColor())
		                .marginResId(R.dimen.fragment_recyclerview_award_left_margin, R.dimen.fragment_recyclerview_award_right_margin)
		                .build());
	}
	
	private void setUiListener(){
		setRecyclerAdapterListener();
	}
	
	private void setRecyclerAdapterListener(){
		mAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				switch (view.getId()) {
				case R.id.itemRecyclerBirthdayMessageIv:
					showBirthdayMessageDialog();
					break;
				case R.id.itemRecyclerBirthdayWishIv:
					showBirthdayWishDialog();
					break;
				default:
					Intent mIntent = new Intent(BirthdayRecyclerActivity.this, BirthdayProfileActivity.class);
					mIntent.putExtra(AppConstants.INTENTCONSTANTS.CATEGORY, mList);
					mIntent.putExtra(AppConstants.INTENTCONSTANTS.ID, position);
					startActivity(mIntent);
					AndroidUtilities.enterWindowAnimation(BirthdayRecyclerActivity.this);
					break;
				}
			}
		});
	}
	
	private void showBirthdayWishDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(BirthdayRecyclerActivity.this)
        .title(getResources().getString(R.string.dialog_birthday_wish_title) + " ?")
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.sample_fragment_settings_dialog_language_negative))
        .negativeColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
            	dialog.dismiss();
            }
        })
        .show();
	}
	
	
	private void showBirthdayMessageDialog(){
	MaterialDialog mMaterialDialog = new MaterialDialog.Builder(BirthdayRecyclerActivity.this)
        .title(getResources().getString(R.string.dialog_birthday_message_title))
        .titleColor(Utilities.getAppColor())
        .customView(R.layout.dialog_birthday_message, true)
        .positiveText(getResources().getString(R.string.dialog_birthday_message_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.dialog_birthday_message_negative))
        .negativeColor(Utilities.getAppColor())
        .show();
	
	View mView = mMaterialDialog.getCustomView();
	AppCompatEditText mMessageEd= (AppCompatEditText)mView.findViewById(R.id.dialogBirthdayMessageEd);
	final AppCompatTextView mMessageEdCounterTv= (AppCompatTextView)mView.findViewById(R.id.dialogBirthdayMessageTv);
	
	mMessageEd.addTextChangedListener(new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence mCharsequence, int start,
				int before, int count) {
			// TODO Auto-generated method stub
			if(mCharsequence.length() > 140){
				mMessageEdCounterTv.setTextColor(Color.RED);
			}else{
				mMessageEdCounterTv.setTextColor(Utilities.getAppColor());
			}
			mMessageEdCounterTv.setText(mCharsequence.length()+"/140");
			
		}

		@Override
		public void beforeTextChanged(CharSequence mCharsequence,
				int start, int count, int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable mEditable) {
			// TODO Auto-generated method stub
		}
	});
	}
}

