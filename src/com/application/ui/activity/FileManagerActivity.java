/**
 * 
 */
package com.application.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.application.sqlite.DBConstant;
import com.application.ui.adapter.BaseFragmentAdapter;
import com.application.ui.materialdialog.MaterialDialog;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.ProgressWheel;
import com.application.ui.view.SharedDocumentCell;
import com.application.utils.AndroidUtilities;
import com.application.utils.AppConstants;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.LocaleController;
import com.application.utils.Utilities;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FileManagerActivity extends SwipeBackBaseActivity {
	private static final String TAG = FileManagerActivity.class.getSimpleName();

	private Toolbar mToolBar;
	
	private ActionMode mActionMode;
	private ActionMode.Callback mActionModeCallback;

	private ProgressWheel mToolBarMenuRefreshProgress;
	private ImageView mToolBarMenuRefresh;

	private FrameLayout mCroutonViewGroup;

	private ListView listView;
	private ListAdapter listAdapter;
	private TextView emptyView;

	private File currentDir;
	private ArrayList<ListItem> items = new ArrayList<ListItem>();
	private ArrayList<HistoryEntry> history = new ArrayList<HistoryEntry>();
	private long sizeLimit = 1024 * 1024 * 1536;
	private boolean receiverRegistered = false;
	private boolean scrolling;
	private final static int done = 3;

	private class ListItem {
		int icon;
		String title;
		String subtitle = "";
		String ext = "";
		String thumb;
		File file;
	}

	private class HistoryEntry {
		int scrollItem, scrollOffset;
		File dir;
		String title;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			Runnable r = new Runnable() {
				public void run() {
					try {
						if (currentDir == null) {
							listRoots();
						} else {
							listFiles(currentDir);
						}
					} catch (Exception e) {
						FileLog.e("tmessages", e);
					}
				}
			};
			if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
				listView.postDelayed(r, 1000);
			} else {
				r.run();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);
		initToolBar();
		initReceivers();
		initUi();

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_file_storage, menu);
		if (AndroidUtilities.isAboveGingerBread()) {
			MenuItem refreshItem = menu
					.findItem(R.id.action_refresh_actionable);
			if (refreshItem != null) {
				View mView = MenuItemCompat.getActionView(refreshItem);
				MaterialRippleLayout mToolBarMenuRefreshLayout = (MaterialRippleLayout) mView
						.findViewById(R.id.toolBarActionItemRefresh);
				mToolBarMenuRefreshProgress = (ProgressWheel) mView
						.findViewById(R.id.toolBarActionItemProgressWheel);
				mToolBarMenuRefresh = (ImageView) mView
						.findViewById(R.id.toolBarActionItemImageView);
				mToolBarMenuRefreshLayout
						.setOnClickListener(new View.OnClickListener() {
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
			AndroidUtilities.exitWindowAnimation(FileManagerActivity.this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (history.size() > 0) {
			HistoryEntry he = history.remove(history.size() - 1);
			if (he.dir != null) {
				listFiles(he.dir);
			} else {
				listRoots();
			}
			listView.setSelectionFromTop(he.scrollItem, he.scrollOffset);
		}
		
		if(mActionMode!=null){
			if(AndroidUtilities.isAboveHoneyComb()){
				mActionMode.finish();
			}
		}
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			if (receiverRegistered) {
				unregisterReceiver(receiver);
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e);
		}
	}

	private void toolBarRefresh() {
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

	private void initReceivers() {
		if (!receiverRegistered) {
			receiverRegistered = true;
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
			filter.addAction(Intent.ACTION_MEDIA_CHECKING);
			filter.addAction(Intent.ACTION_MEDIA_EJECT);
			filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			filter.addAction(Intent.ACTION_MEDIA_NOFS);
			filter.addAction(Intent.ACTION_MEDIA_REMOVED);
			filter.addAction(Intent.ACTION_MEDIA_SHARED);
			filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
			filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			filter.addDataScheme("file");
			registerReceiver(receiver, filter);
		}
	}

	private void initUi() {
		mCroutonViewGroup = (FrameLayout) findViewById(R.id.croutonViewGroup);

		listView = (ListView) findViewById(R.id.fragmentFileManagerListView);

		emptyView = (TextView) findViewById(R.id.fragmentFileManagerSearchEmptyView);
		initFileManager();
	}

	private void initFileManager() {
		listAdapter = new ListAdapter(FileManagerActivity.this);
		emptyView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		listView.setEmptyView(emptyView);
		listView.setAdapter(listAdapter);

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				scrolling = scrollState != SCROLL_STATE_IDLE;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressLint("NewApi") @Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int i, long l) {
				// TODO Auto-generated method stub
				if(AndroidUtilities.isAboveHoneyComb()){
					if(!listAdapter.getSelectedIdParentFolder(i)){
						createActionModeCallback();
						mActionMode = mToolBar.startActionMode(mActionModeCallback);
						listAdapter.toggleSelection(i);
						  mActionMode.setTitle(String.valueOf(listAdapter
				                    .getSelectedIdCount()));
					}
				}
				return true;
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("NewApi") @Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				if (i < 0 || i >= items.size()) {
					return;
				}
				
				if(mActionMode!=null){
					if(AndroidUtilities.isAboveHoneyComb()){
						if(!listAdapter.getSelectedIdParentFolder(i)){
							listAdapter.toggleSelection(i);
							if(listAdapter.getSelectedIdCount() > 0){
								mActionMode.setTitle(String.valueOf(listAdapter.getSelectedIdCount()));
								mActionMode.invalidate();
								return;	
							}else{
								mActionMode.finish();
							}
						}else{
							return;
						}
//			            mActionMode.setTitle(String.valueOf(listAdapter
//			                    .getSelectedCount()) + " selected");
					}
				}
				
				ListItem item = items.get(i);
				File file = item.file;
				if (file == null) {
					if (item.icon == R.drawable.ic_storage_gallery) {
					} else {
						HistoryEntry he = history.remove(history.size() - 1);
						if (he.dir != null) {
							listFiles(he.dir);
						} else {
							listRoots();
						}
						listView.setSelectionFromTop(he.scrollItem,
								he.scrollOffset);
					}
				} else if (file.isDirectory()) {
					HistoryEntry he = new HistoryEntry();
					he.scrollItem = listView.getFirstVisiblePosition();
					he.scrollOffset = listView.getChildAt(0).getTop();
					he.dir = currentDir;
					history.add(he);
					if (!listFiles(file)) {
						history.remove(he);
						return;
					}
					listView.setSelection(0);
				} else {
					if (!file.canRead()) {
						showErrorBox(LocaleController.getString("AccessError",
								R.string.AccessError));
						return;
					}
					if (sizeLimit != 0) {
						if (file.length() > sizeLimit) {
							showErrorBox(LocaleController.formatString(
									"FileUploadLimit",
									R.string.FileUploadLimit,
									Utilities.formatFileSize(sizeLimit)));
							return;
						}
					}
					if (file.length() == 0) {
						return;
					}
				}
			}
		});

		listRoots();
	}

	/**
	 * <b>Description: </b></br>Initialize ToolBar</br></br>
	 * 
	 * @author Vikalp Patel(VikalpPatelCE)
	 */
	private void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbarLayout);
		mToolBar.setTitle(getResources().getString(
				R.string.FileManagerActivityTitle));
		mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);
		setSupportActionBar(mToolBar);
	}

	private boolean listFiles(File dir) {
		if (!dir.canRead()) {
			if (dir.getAbsolutePath().startsWith(
					Environment.getExternalStorageDirectory().toString())
					|| dir.getAbsolutePath().startsWith("/sdcard")
					|| dir.getAbsolutePath().startsWith("/mnt/sdcard")) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)
						&& !Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED_READ_ONLY)) {
					currentDir = dir;
					items.clear();
					String state = Environment.getExternalStorageState();
					if (Environment.MEDIA_SHARED.equals(state)) {
						emptyView.setText(LocaleController.getString(
								"UsbActive", R.string.UsbActive));
					} else {
						emptyView.setText(LocaleController.getString(
								"NotMounted", R.string.NotMounted));
					}
					AndroidUtilities.clearDrawableAnimation(listView);
					scrolling = true;
					listAdapter.notifyDataSetChanged();
					return true;
				}
			}
			showErrorBox(LocaleController.getString("AccessError",
					R.string.AccessError));
			return false;
		}
		emptyView.setText(LocaleController.getString("NoFiles",
				R.string.NoFiles));
		File[] files = null;
		try {
			files = dir.listFiles();
		} catch (Exception e) {
			showErrorBox(e.getLocalizedMessage());
			return false;
		}
		if (files == null) {
			showErrorBox(LocaleController.getString("UnknownError",
					R.string.UnknownError));
			return false;
		}
		currentDir = dir;
		items.clear();
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				if (lhs.isDirectory() != rhs.isDirectory()) {
					return lhs.isDirectory() ? -1 : 1;
				}
				return lhs.getName().compareToIgnoreCase(rhs.getName());
				/*
				 * long lm = lhs.lastModified(); long rm = lhs.lastModified();
				 * if (lm == rm) { return 0; } else if (lm > rm) { return -1; }
				 * else { return 1; }
				 */
			}
		});
		for (File file : files) {
			if (file.getName().startsWith(".")) {
				continue;
			}
			ListItem item = new ListItem();
			item.title = file.getName();
			item.file = file;
			if (file.isDirectory()) {
				item.icon = R.drawable.ic_directory;
				item.subtitle = LocaleController.getString("Folder",
						R.string.Folder);
			} else {
				String fname = file.getName();
				String[] sp = fname.split("\\.");
				item.ext = sp.length > 1 ? sp[sp.length - 1] : "?";
				item.subtitle = Utilities.formatFileSize(file.length());
				fname = fname.toLowerCase();
				if (fname.endsWith(".jpg") || fname.endsWith(".png")
						|| fname.endsWith(".gif") || fname.endsWith(".jpeg")) {
					item.thumb = file.getAbsolutePath();
				}
			}
			items.add(item);
		}
		ListItem item = new ListItem();
		item.title = "..";
		if (history.size() > 0) {
			HistoryEntry entry = history.get(history.size() - 1);
			if (entry.dir == null) {
				item.subtitle = LocaleController.getString("Folder",
						R.string.Folder);
			} else {
				item.subtitle = entry.dir.toString();
			}
		} else {
			item.subtitle = LocaleController.getString("Folder",
					R.string.Folder);
		}
		item.icon = R.drawable.ic_directory;
		item.file = null;
		items.add(0, item);
		AndroidUtilities.clearDrawableAnimation(listView);
		scrolling = true;
		listAdapter.notifyDataSetChanged();
		return true;
	}

	private void showErrorBox(String error) {
		new MaterialDialog.Builder(FileManagerActivity.this)
				.title(LocaleController.getString("AppName", R.string.app_name))
				.content(error)
				.positiveText(LocaleController.getString("OK", R.string.OK))
				.show();
	}

	private void listRoots() {
		currentDir = null;
		items.clear();
		String extStorage = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		ListItem ext = new ListItem();
		/*if (Build.VERSION.SDK_INT < 9
				|| Environment.isExternalStorageRemovable()) {
			ext.title = LocaleController.getString("SdCard", R.string.SdCard);
		} else {
			ext.title = LocaleController.getString("InternalStorage",
					R.string.InternalStorage);
		}
		ext.icon = Build.VERSION.SDK_INT < 9
				|| Environment.isExternalStorageRemovable() ? R.drawable.ic_external_storage
				: R.drawable.ic_storage;
		ext.subtitle = getRootSubtitle(extStorage);
		ext.file = Environment.getExternalStorageDirectory();
		items.add(ext);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"/proc/mounts"));
			String line;
			HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
			ArrayList<String> result = new ArrayList<String>();
			String extDevice = null;
			while ((line = reader.readLine()) != null) {
				if ((!line.contains("/mnt") && !line.contains("/storage") && !line
						.contains("/sdcard"))
						|| line.contains("asec")
						|| line.contains("tmpfs") || line.contains("none")) {
					continue;
				}
				String[] info = line.split(" ");
				if (!aliases.containsKey(info[0])) {
					aliases.put(info[0], new ArrayList<String>());
				}
				aliases.get(info[0]).add(info[1]);
				if (info[1].equals(extStorage)) {
					extDevice = info[0];
				}
				result.add(info[1]);
			}
			reader.close();
			if (extDevice != null) {
				result.removeAll(aliases.get(extDevice));
				for (String path : result) {
					try {
						ListItem item = new ListItem();
						if (path.toLowerCase().contains("sd")) {
							ext.title = LocaleController.getString("SdCard",
									R.string.SdCard);
						} else {
							ext.title = LocaleController
									.getString("ExternalStorage",
											R.string.ExternalStorage);
						}
						item.icon = R.drawable.ic_external_storage;
						item.subtitle = getRootSubtitle(path);
						item.file = new File(path);
						items.add(item);
					} catch (Exception e) {
						FileLog.e("tmessages", e);
					}
				}
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e);
		}*/
		ListItem fs = new ListItem();
		/*fs.title = "/";
		fs.subtitle = LocaleController.getString("SystemRoot",
				R.string.SystemRoot);
		fs.icon = R.drawable.ic_directory;
		fs.file = new File("/");
		items.add(fs);*/

		try {
			File telegramPath = new File(
					Environment.getExternalStorageDirectory(), AppConstants.FOLDER.BUILD_FOLDER_FILE_MANAGER);
			if (telegramPath.exists()) {
				fs = new ListItem();
				fs.title = "Mobcast";
				fs.subtitle = telegramPath.toString();
				fs.icon = R.drawable.ic_directory;
				fs.file = telegramPath;
				items.add(fs);
			}
		} catch (Exception e) {
			FileLog.e("tmessages", e);
		}

		AndroidUtilities.clearDrawableAnimation(listView);
		scrolling = true;
		listAdapter.notifyDataSetChanged();
	}

	private String getRootSubtitle(String path) {
		StatFs stat = new StatFs(path);
		long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
		long free = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();
		if (total == 0) {
			return "";
		}
		return LocaleController.formatString("FreeOfTotal",
				R.string.FreeOfTotal, Utilities.formatFileSize(free),
				Utilities.formatFileSize(total));
	}

	@SuppressLint("NewApi") 
	private void createActionModeCallback(){
		if(AndroidUtilities.isAboveHoneyComb()){
			mActionModeCallback = new android.view.ActionMode.Callback() {
				@Override
				public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					Menu menuFromActionMode= mode.getMenu();
		            if(listAdapter.getSelectedIdCount() > 1){
		            	menuFromActionMode.findItem(R.id.action_info).setVisible(false);
		            }else{
		            	menuFromActionMode.findItem(R.id.action_info).setVisible(true);
		            }
					return true;
				}
				
				@Override
				public void onDestroyActionMode(android.view.ActionMode mode) {
					// TODO Auto-generated method stub
					mActionMode =null;
					listAdapter.clearSelectedId();
					listAdapter.notifyDataSetChanged();
				}
				
				@Override
				public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					 mode.setTitle(String.valueOf(listAdapter.getSelectedIdCount()));
		             MenuInflater inflater = mode.getMenuInflater();
		             inflater.inflate(R.menu.menu_actionmode_file_manager_single, menu);
		             return true;
				}
				
				@Override
				public boolean onActionItemClicked(android.view.ActionMode mode,
						MenuItem item) {
					// TODO Auto-generated method stub
					switch(item.getItemId()){
					case R.id.action_delete:
						showConfirmationMaterialDialog();
						break;
					case R.id.action_info:
						for(int i = 0 ; i < listAdapter.getCount() ;i++){
							if(listAdapter.getSelectedIds().get(i)){
								showInfo(currentDir + "/"+ items.get(i).title);
							}
						}
						break;
					}
					return true;
				}
			};
		}
	}
	
	
	
	private class ListAdapter extends BaseFragmentAdapter {
		private Context mContext;
		private ArrayList<Integer> mSelected;
		private SparseBooleanArray mSelectedItemsIds;

		public ListAdapter(Context context) {
			mContext = context;
			mSelected = new ArrayList<Integer>();
			mSelectedItemsIds = new SparseBooleanArray();
		}

		@Override
		public int getCount() {
			return items.size();
		}

		public boolean isSelected(int index){
			return !mSelected.contains(index);
		}
		
		public void toggleSelected(int index) {
			final boolean newState = !mSelected.contains(index);
			if (newState) 
				mSelected.add(index);
			 else 
				mSelected.remove((Integer) index);
			notifyDataSetChanged();
		}
		
		public void setSelected(int position){
			mSelected.add(position);
			notifyDataSetChanged();
		}
		
		public boolean getSelectedIdParentFolder(int position){
			if(items.get(position).title.equalsIgnoreCase(".."))
				return true;
			return false;
		}

		public void clearSelected() {
			mSelected.clear();
			notifyDataSetChanged();
		}

		public int getSelectedCount() {
			return mSelected.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}
		
		public void toggleSelection(int position) {
	        selectView(position, !mSelectedItemsIds.get(position));
	    }
	 
	    public void removeSelection() {
	        mSelectedItemsIds = new SparseBooleanArray();
	        notifyDataSetChanged();
	    }
	 
	    public void selectView(int position, boolean value) {
	        if (value)
	            mSelectedItemsIds.put(position, value);
	        else
	            mSelectedItemsIds.delete(position);
	 
	        notifyDataSetChanged();
	    }
	 
	    public int getSelectedIdCount() {
	        return mSelectedItemsIds.size();
	    }
	 
	    public SparseBooleanArray getSelectedIds() {
	        return mSelectedItemsIds;
	    }
	    
	    public void clearSelectedId() {
			mSelectedItemsIds.clear();
			notifyDataSetChanged();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public int getViewTypeCount() {
			return 2;
		}

		public int getItemViewType(int pos) {
			return items.get(pos).subtitle.length() > 0 ? 0 : 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new SharedDocumentCell(mContext);
			}
			SharedDocumentCell textDetailCell = (SharedDocumentCell) convertView;
			ListItem item = items.get(position);
			if (item.icon != 0) {
				((SharedDocumentCell) convertView)
						.setTextAndValueAndTypeAndThumb(item.title,
								item.subtitle, null, null, item.icon);
			} else {
				String type = item.ext.toUpperCase().substring(0,
						Math.min(item.ext.length(), 4));
				((SharedDocumentCell) convertView)
						.setTextAndValueAndTypeAndThumb(item.title,
								item.subtitle, type, item.thumb, 0);
			}
			
			 convertView
             .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                     : Color.TRANSPARENT);
			
			return convertView;
		}

	}
	
	private void deleteFiles(){
		try{
			for(int i = 0 ; i < listAdapter.getCount() ;i++){
				if(listAdapter.getSelectedIds().get(i)){
					new File(currentDir + "/"+ items.get(i).title).delete();
				}
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void showInfo(String mPath){
		String mTitle = null;
		String mDesc = null;
		String mId = null;
		Cursor mCursorMobcastFile = getContentResolver().query(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_FILE_PATH + "=?", new String[]{mPath}, null);
		
		if(mCursorMobcastFile!=null && mCursorMobcastFile.getCount() > 0){
			mCursorMobcastFile.moveToFirst();
			mId = mCursorMobcastFile.getString(mCursorMobcastFile.getColumnIndex(DBConstant.Mobcast_File_Columns.COLUMN_MOBCAST_ID));
			
			Cursor mCursorMobcast = getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_ID + "=?", new String[]{mId}, null);
			if(mCursorMobcast!=null && mCursorMobcast.getCount() > 0){
				mCursorMobcast.moveToFirst();
				mTitle = mCursorMobcast.getString(mCursorMobcast.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_TITLE));
				mDesc = mCursorMobcast.getString(mCursorMobcast.getColumnIndex(DBConstant.Mobcast_Columns.COLUMN_MOBCAST_DESC));
				
				if(!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mDesc)){
					showMaterialDialog(mTitle, mDesc);
				}
			}
			
			if(mCursorMobcast!=null){
				mCursorMobcast.close();
			}
		}
		
		if(mCursorMobcastFile!=null){
			mCursorMobcastFile.close();
		}
	}
	
	private void showMaterialDialog(String mTitle, String mDesc){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(FileManagerActivity.this)
        .title(mTitle)
        .content(mDesc)
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	dialog.dismiss();
            	listAdapter.clearSelectedId();
            	mActionMode.invalidate();
            }
        })
        .show();
	}
	
	private void showConfirmationMaterialDialog(){
		MaterialDialog mMaterialDialog = new MaterialDialog.Builder(FileManagerActivity.this)
        .title(getResources().getString(R.string.file_delete_message))
        .titleColor(Utilities.getAppColor())
        .positiveText(getResources().getString(R.string.sample_fragment_settings_dialog_language_positive))
        .positiveColor(Utilities.getAppColor())
        .negativeText(getResources().getString(R.string.sample_fragment_settings_dialog_language_negative))
        .negativeColor(Utilities.getAppColor())
        .callback(new MaterialDialog.ButtonCallback() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onPositive(MaterialDialog dialog) {
            	deleteFiles();
            	listAdapter.clearSelectedId();
            	mActionMode.invalidate();
//            	Intent mIntent = new Intent(Intent.ACTION_MEDIA_REMOVED);
//				sendBroadcast(mIntent);
            	dialog.dismiss();
            }
            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
            public void onNegative(MaterialDialog dialog) {
            	listAdapter.clearSelectedId();
            	mActionMode.invalidate();
            	dialog.dismiss();
            }
        })
        .show();
	}
}
