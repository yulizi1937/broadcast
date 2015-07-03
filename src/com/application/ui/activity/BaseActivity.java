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

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.application.beans.Mobcast;
import com.application.beans.Training;
import com.application.ui.adapter.SimpleHeaderRecyclerAdapter;
import com.application.ui.adapter.SimpleRecyclerAdapter;
import com.application.ui.calligraphy.CalligraphyContextWrapper;
import com.application.ui.view.BottomSheet;
import com.application.ui.view.MaterialRippleLayout;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
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
		
		for (int i = 0; i <= 15; i++) {
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
			} else if (i == 5 || i == 13) {
				Obj.setmFileType(AppConstants.TRAINING.DOC);
			} else if (i == 6 || i == 14) {
				Obj.setmFileType(AppConstants.TRAINING.XLS);
			} else if (i == 7 || i == 15) {
				Obj.setmFileType(AppConstants.TRAINING.QUIZ);
			} else {
				Obj.setmFileType(AppConstants.TRAINING.TEXT);
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
}
