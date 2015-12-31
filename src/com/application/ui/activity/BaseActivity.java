/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.application.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.application.beans.Chat;
import com.application.beans.MIS;
import com.application.beans.Mobcast;
import com.application.beans.Training;
import com.application.ui.adapter.SimpleHeaderRecyclerAdapter;
import com.application.ui.adapter.SimpleRecyclerAdapter;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.MaterialRippleLayout;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.BuildVars;
import com.application.utils.FileLog;
import com.mobcast.R;

public abstract class BaseActivity extends AppCompatActivity {
	private static final String TAG = BaseActivity.class.getSimpleName();
	private static final int NUM_OF_ITEMS = 20;
	private static final int NUM_OF_ITEMS_FEW = 3;
	
	@Override
    protected void attachBaseContext(Context newBase) {
        try{
        	if(AndroidUtilities.isAppLanguageIsEnglish()){
        			super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));	
        	}else{
        		super.attachBaseContext(newBase);
        	}
        }catch(Exception e){
        	FileLog.e(TAG, e.toString());
        }
    }

	protected int getActionBarSize() {
		TypedValue typedValue = new TypedValue();
		int[] textSizeAttr = new int[] { R.attr.actionBarSize };
		int indexOfAttrTextSize = 0;
		TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
		int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
		a.recycle();
		return actionBarSize;
	}

	protected int getScreenHeight() {
		return findViewById(android.R.id.content).getHeight();
	}

	public static ArrayList<String> getDummyData() {
		return getDummyData(NUM_OF_ITEMS);
	}

	public static ArrayList<String> getDummyData(int num) {
		ArrayList<String> items = new ArrayList<String>();
		for (int i = 1; i <= num; i++) {
			items.add("Item " + i);
		}
		return items;
	}

	public static ArrayList<Mobcast> getDummyMobcastData() {
		return getDummyMobcastData(NUM_OF_ITEMS);
	}

	public static ArrayList<Mobcast> getDummyMobcastData(int num) {
		ArrayList<Mobcast> items = new ArrayList<Mobcast>();
		/*for (int i = 1; i <= num; i++) {
			Mobcast Obj = new Mobcast();
			if (i % 3 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.VIDEO);
			} else if (i % 5 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.IMAGE);
			} else if (i % 7 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.AUDIO);
			} else if (i % 11 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.PDF);
			} else if (i % 13 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.DOC);
			} else if (i % 17 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.XLS);
			} else if (i % 19 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.FEEDBACK);
			} else if (i % 23 == 0) {
				Obj.setmFileType(AppConstants.MOBCAST.NEWS);
			} else {
				Obj.setmFileType(AppConstants.MOBCAST.TEXT);
			}
			items.add(Obj);
		}*/
		
		/*for (int i = 0; i <= 15; i++) {
			Mobcast Obj = new Mobcast();
			if (i == 0 || i == 8) {
				Obj.setmFileType(AppConstants.MOBCAST.TEXT);
			} else if (i == 1 || i == 9) {
				Obj.setmFileType(AppConstants.MOBCAST.VIDEO);
			} else if (i == 2 || i == 10) {
				Obj.setmFileType(AppConstants.MOBCAST.IMAGE);
			} else if (i == 3 || i == 11) {
				Obj.setmFileType(AppConstants.MOBCAST.AUDIO);
			} else if (i == 4 || i == 12) {
				Obj.setmFileType(AppConstants.MOBCAST.PDF);
			} else if (i == 5 || i == 13) {
				Obj.setmFileType(AppConstants.MOBCAST.DOC);
			} else if (i == 6 || i == 14) {
				Obj.setmFileType(AppConstants.MOBCAST.XLS);
			} else if (i == 7 || i == 15) {
				Obj.setmFileType(AppConstants.MOBCAST.FEEDBACK);
			} else {
				Obj.setmFileType(AppConstants.MOBCAST.TEXT);
			}
			items.add(Obj);
		}*/
		
		for (int i = 0; i <= 8; i++) {
			Mobcast Obj = new Mobcast();
			if (i == 0) {
				Obj.setmBy("By : Consumer 360");
				Obj.setmTitle("Welcome address - Piyush Mathur, President, Nielsen India Region.");
				Obj.setRead(true);
				Obj.setmExpiryDate("a");
				Obj.setmFileType(AppConstants.MOBCAST.IMAGE);
			} else if (i == 1) {
				Obj.setmBy("By : Thought Leadership");
				Obj.setmTitle("Hinterlands Become FMCG's Trump Card : Nielsen Featured Insights ");
				Obj.setRead(false);
				Obj.setmExpiryTime("8 Pages");
				Obj.setmTime("a");
				Obj.setmExpiryDate("Trump Card.pdf");
				Obj.setmFileType(AppConstants.MOBCAST.PDF);
			} else if (i == 2) {
				Obj.setmBy("By : Press Releases");
				Obj.setmTitle("CCI Q1 2015");
				Obj.setRead(false);
				Obj.setmExpiryDate("CCI Q1 2015 - Press Release.docx");
				Obj.setmExpiryTime("3 Pages");
				Obj.setmTime("a");
				Obj.setmFileType(AppConstants.MOBCAST.DOC);
			} else if (i == 3) {
				Obj.setmBy("By : Event Video");
				Obj.setmTitle("MASTER PROMO VIDEO");
				Obj.setRead(false);
				Obj.setmFileType(AppConstants.MOBCAST.VIDEO);
			} else if (i == 4) {
				Obj.setmBy("By : Consumer 360");
				Obj.setmTitle("Keynote Address - Harish Manwani, Non-Executive Chairman, Hindustan Unilever Ltd.");
				Obj.setRead(true);
				Obj.setmExpiryDate("b");
				Obj.setmFileType(AppConstants.MOBCAST.IMAGE);
			} else if (i == 5) {
				Obj.setmBy("By : Consumer 360");
				Obj.setmTitle("Dharavi Rocks.");
				Obj.setRead(false);
				Obj.setmExpiryDate("c");
				Obj.setmFileType(AppConstants.MOBCAST.IMAGE);
			} else if (i == 6) {
				Obj.setmBy("By : Thought Leadership");
				Obj.setmTitle("Super Consumers - Racing to the Top : Nielsen Featured Insights ");
				Obj.setRead(true);
				Obj.setmExpiryTime("12 Pages");
				Obj.setmTime("b");
				Obj.setmExpiryDate("Racing to the Top.pdf");
				Obj.setmFileType(AppConstants.MOBCAST.PDF);
			} else if (i == 7) {
				Obj.setmBy("By : Press Releases");
				Obj.setmTitle("FMCG is the most preferred sector for campus placements, for the fourth year, followed by E-Commerce. HUL is the top recruiter, followed by P&G and Google.");
				Obj.setRead(false);
				Obj.setmExpiryDate("Bschool24thFeb.docx");
				Obj.setmExpiryTime("5 Pages");
				Obj.setmTime("b");
				Obj.setmFileType(AppConstants.MOBCAST.DOC);
			} else {
				Obj.setmBy("By : Press Releases");
				Obj.setmTitle("PCM Workshop & Consumer 360");
				Obj.setmFileType(AppConstants.MOBCAST.FEEDBACK);
				Obj.setRead(false);
			}
			Obj.setmLikeCount("12K+");
			Obj.setmViewCount("10K+");
			items.add(Obj);
		}
		return items;
	}

	public static ArrayList<Training> getDummyTrainingData() {
		return getDummyTrainingData(NUM_OF_ITEMS);
	}
	
	public static ArrayList<Training> getDummyTrainingData(int num) {
		ArrayList<Training> items = new ArrayList<Training>();
/*		for (int i = 1; i <= num; i++) {
			Training Obj = new Training();
			if (i % 3 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.VIDEO);
			} else if (i % 5 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.IMAGE);
			} else if (i % 7 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.AUDIO);
			} else if (i % 11 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.PDF);
			} else if (i % 13 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.DOC);
			} else if (i % 17 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.XLS);
			} else if (i % 19 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.QUIZ);
			} else if (i % 23 == 0) {
				Obj.setmFileType(AppConstants.TRAINING.INTERACTIVE);
			} else {
				Obj.setmFileType(AppConstants.TRAINING.TEXT);
			}
			items.add(Obj);
		}*/
		
		for (int i = 0; i <= 15; i++) {
			Training Obj = new Training();
			if (i == 0 || i == 8) {
				Obj.setmFileType(AppConstants.TRAINING.TEXT);
			} else if (i == 1 || i == 9) {
				Obj.setmFileType(AppConstants.TRAINING.VIDEO);
			} else if (i == 2 || i == 10) {
				Obj.setmFileType(AppConstants.TRAINING.IMAGE);
			} else if (i == 3 || i == 11) {
				Obj.setmFileType(AppConstants.TRAINING.AUDIO);
			} else if (i == 4 || i == 12) {
				Obj.setmFileType(AppConstants.TRAINING.PDF);
			} else if (i == 7 || i == 15) {
				Obj.setmFileType(AppConstants.TRAINING.QUIZ);
			} else {
				Obj.setmFileType(AppConstants.TRAINING.TEXT);
			}
			items.add(Obj);
		}
		return items;
	}
	
	public static ArrayList<Chat> getDummyChatData() {
		return getDummyChatData(1);
	}
	
	public static ArrayList<Chat> getDummyChatData(int num) {
		ArrayList<Chat> items = new ArrayList<Chat>();
		for (int i = 0; i < num; i++) {
			Chat Obj = new Chat();
			if(ApplicationLoader.getPreferences().getUserName().equalsIgnoreCase(AppConstants.mChatUser1)){
				Obj.setmName(AppConstants.mChatName2);
				Obj.setIsRead(AppConstants.mChatUser1);//from
				Obj.setmLastMessageTime(AppConstants.mChatUser2);//to
				Obj.setmUserDpLink(AppConstants.mChatUserDp1);
				ApplicationLoader.getPreferences().setChatOppositePerson(AppConstants.mChatName2);
				ApplicationLoader.getPreferences().setChatFrom(AppConstants.mChatUser1);
				ApplicationLoader.getPreferences().setChatTo(AppConstants.mChatUser2);
				items.add(Obj);
			}else if(ApplicationLoader.getPreferences().getUserName().equalsIgnoreCase(AppConstants.mChatUser2)){
				Obj.setmName(AppConstants.mChatName1);
				Obj.setIsRead(AppConstants.mChatUser2);//from
				Obj.setmLastMessageTime(AppConstants.mChatUser1);//to
				Obj.setmUserDpLink(AppConstants.mChatUserDp2);
				ApplicationLoader.getPreferences().setChatOppositePerson(AppConstants.mChatName1);
				ApplicationLoader.getPreferences().setChatFrom(AppConstants.mChatUser2);
				ApplicationLoader.getPreferences().setChatTo(AppConstants.mChatUser1);
				items.add(Obj);
			}
		}
		return items;
	}
	
	public static ArrayList<MIS> getDummyMISData() {
		return getDummyMISData(9);
	}
	
	public static ArrayList<MIS> getDummyMISData(int num) {
		ArrayList<MIS> items = new ArrayList<MIS>();
		for (int i = 0; i < num; i++) {
			MIS Obj = new MIS();
			switch(i){
			case 0:
				Obj.setmTitle("My Office – Attendance & PJP");
				Obj.setmLink("in.jts.myapp");
				break;
			case 1:
				Obj.setmTitle("Cavinkomm Reports");
				Obj.setmLink("http://124.7.223.32/CKN");
				break;
			case 2:
				Obj.setmTitle("Take Insight Reports – Secondary Sales MIS");
				Obj.setmLink("http://www.ckinsight.com");
				break;
			case 3:
				Obj.setmTitle("RSSM wise Productivity, ECO and Unbilled Outlets");
				Obj.setmLink("http://www.cavinkomm.net/RSConnectMISReport/");
				break;
			case 4:
				Obj.setmTitle("Top Outlet Performance(Outlet Trend Report - Week wise)");
				Obj.setmLink("http://www.cavinkomm.net/RSConnectMISReport/");
				break;
			case 5:
				Obj.setmTitle("SFA Reports – IVY Mobility");
				Obj.setmLink("http://idistcavinkarein.ivymobileapps.com/web");
				break;
			case 6:
				Obj.setmTitle("Utility Reports");
				Obj.setmLink("http://cavinkomm.net/ckplreport/utilityreports/upgraded/home.asp");
				break;
			case 7:
				Obj.setmTitle("Market Working Report");
				Obj.setmLink("http://cavinkomm.net/mktg/upgraded/reports/loginfo.asp");
				break;
			case 8:
				Obj.setmTitle("CK HRIMS – Employee Portal");
				Obj.setmLink("http://my.cavinkare.com");
				break;
			}
			items.add(Obj);
		}
		return items;
	}

	protected void setDummyData(ListView listView) {
		setDummyData(listView, NUM_OF_ITEMS);
	}

	protected void setDummyDataFew(ListView listView) {
		setDummyData(listView, NUM_OF_ITEMS_FEW);
	}

	protected void setDummyData(ListView listView, int num) {
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getDummyData(num)));
	}

	protected void setDummyDataWithHeader(ListView listView, int headerHeight) {
		setDummyDataWithHeader(listView, headerHeight, NUM_OF_ITEMS);
	}

	protected void setDummyDataWithHeader(ListView listView, int headerHeight,
			int num) {
		View headerView = new View(this);
		headerView.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
		headerView.setMinimumHeight(headerHeight);
		// This is required to disable header's list selector effect
		headerView.setClickable(true);
		setDummyDataWithHeader(listView, headerView, num);
	}

	protected void setDummyDataWithHeader(ListView listView, View headerView,
			int num) {
		listView.addHeaderView(headerView);
		setDummyData(listView, num);
	}

	protected void setDummyData(GridView gridView) {
		gridView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getDummyData()));
	}

	protected void setDummyData(RecyclerView recyclerView) {
		setDummyData(recyclerView, NUM_OF_ITEMS);
	}

	protected void setDummyDataFew(RecyclerView recyclerView) {
		setDummyData(recyclerView, NUM_OF_ITEMS_FEW);
	}

	protected void setDummyData(RecyclerView recyclerView, int num) {
		recyclerView.setAdapter(new SimpleRecyclerAdapter(this,
				getDummyData(num)));
	}

	protected void setDummyDataWithHeader(RecyclerView recyclerView,
			int headerHeight) {
		View headerView = new View(this);
		headerView.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
		headerView.setMinimumHeight(headerHeight);
		// This is required to disable header's list selector effect
		headerView.setClickable(true);
		setDummyDataWithHeader(recyclerView, headerView);
	}

	protected void setDummyDataWithHeader(RecyclerView recyclerView,
			View headerView) {
		recyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(this,
				getDummyData(), headerView));
	}

	/**
	 * Security : Couldn't capture ScreenShot
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	protected void setSecurity() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (!BuildVars.DEBUG_SCREENSHOT) {
				getWindow().setFlags(LayoutParams.FLAG_SECURE,
						LayoutParams.FLAG_SECURE);
			}
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	protected void setFullScreen(){
		try{
			if(AndroidUtilities.isAboveKitKat()){
				View decorView = getWindow().getDecorView();
				// Hide both the navigation bar and the status bar.
				// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
				// a general rule, you should design your app to hide the status bar whenever you
				// hide the navigation bar.
				int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
				decorView.setSystemUiVisibility(uiOptions);
			}else{
		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}

	protected BottomSheet.Builder getShareActions(BottomSheet.Builder builder,
			String text) {
		PackageManager pm = this.getPackageManager();

		final Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		final List<ResolveInfo> list = pm.queryIntentActivities(shareIntent, 0);

		for (int i = 0; i < list.size(); i++) {
			builder.sheet(i, list.get(i).loadIcon(pm), list.get(i)
					.loadLabel(pm));
		}

		builder.listener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ActivityInfo activity = list.get(which).activityInfo;
				ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				Intent newIntent = (Intent) shareIntent.clone();
				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				newIntent.setComponent(name);
				startActivity(newIntent);
			}
		});
		return builder;
	}

	protected void setMaterialRippleWithGrayOnView(View mView) {
		MaterialRippleLayout
				.on(mView)
				.rippleColor(Color.parseColor("#aaaaaa"))
				.rippleAlpha(0.2f)
				.rippleHover(true)
				.rippleOverlay(true)
				.rippleBackground(
						Color.parseColor("#00000000"))
				.create();
	}

	protected void setMaterialRippleOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#ffffff"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}
	
	protected void setToolBarOption(){
		supportInvalidateOptionsMenu();
	}
	
	protected boolean isFileCorrupted(String mFilePath, String mFileSize){
		try{
			if(new File(mFilePath).length() == Long.parseLong(mFileSize)){
				return false;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return false;
		}
		return true;
	}
	
	protected int isFromTraining(String mCategory){
		try{
			if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.MOBCAST)){
				return 0;
			}else if(mCategory.equalsIgnoreCase(AppConstants.INTENTCONSTANTS.TRAINING)){
				return 1;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return 0;
	}
}
