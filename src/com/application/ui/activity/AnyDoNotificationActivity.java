/**
 * 
 */
package com.application.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.application.ui.view.BottomSheet;
import com.application.ui.view.BottomSheetAnyDo;
import com.application.ui.view.MaterialRippleLayout;
import com.application.ui.view.RoundedBackgroundSpan;
import com.application.utils.ApplicationLoader;
import com.application.utils.FileLog;
import com.application.utils.NotificationHandle;
import com.mobcast.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AnyDoNotificationActivity extends AppCompatActivity {

	private static final String TAG = AnyDoNotificationActivity.class
			.getSimpleName();

	private BottomSheet mBottomSheet;
	private BottomSheetAnyDo mBottomSheetAnyDo;
	private SystemBarTintManager mTintManager;
	
	private TextView mTextViewTv;
	private TextView mTextCloseTv;

	private int mId;
	private int mType;
	private String mCategory;
	private int mLayoutId;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anydo_notification);
		setUpAnyDoNotificationWithBottomSheet();
		setSystemBarTint(0);
		setUiListener();
	}

	private void setUpAnyDoNotificationWithBottomSheet() {
		showDialog(1);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	protected Dialog onCreateDialog(final int position) {
		if (position == 0) {
			mBottomSheet = new BottomSheet.Builder(this)
					.icon(getRoundedBitmap(R.drawable.ic_launcher))
					.title(getSpannableStringWithCounter(
							"Notifications            ", "6 ", " New"))
					.sheet(R.menu.menu_anydo).limit(R.integer.anydo_limit)
					.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
							onBackPressed();
						}
					}).listener(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).build();
			final Menu menu = mBottomSheet.getMenu();
			menu.getItem(0).setVisible(false);
			for (int i = 0; i < 10; i++) {
				if (i % 3 == 0) {
					menu.add(Menu.NONE, i, Menu.NONE, "PDF Notification");
					menu.findItem(i).setIcon(R.drawable.ic_item_pdf);
				} else if (i % 5 == 0) {
					menu.add(Menu.NONE, i, Menu.NONE, "Video Notification");
					menu.findItem(i).setIcon(R.drawable.ic_mobcast_video);
				} else if (i % 7 == 0) {
					menu.add(Menu.NONE, i, Menu.NONE, "Audio Notification");
					menu.findItem(i).setIcon(R.drawable.ic_mobcast_audio);
				} else {
					menu.add(Menu.NONE, i, Menu.NONE, "Text Notification");
					menu.findItem(i).setIcon(R.drawable.ic_mobcast_text);
				}
			}
			return mBottomSheet;
		}else{
			mBottomSheetAnyDo = new BottomSheetAnyDo.Builder(this, 1,
					"mobcast", R.layout.item_recycler_mobcast_text)
			.icon(getRoundedBitmap(R.drawable.ic_launcher))
			.title("Notifications")
			.sheet(R.menu.menu_anydo).limit(R.integer.anydo_limit)
			.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
			}).listener(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).build();
			
			return mBottomSheetAnyDo;
		}
	}
	
	private void setUiListener(){
		setOnClickListener();
		setMaterialRippleView();
	}
	
	private void setMaterialRippleView(){
		try{
			setMaterialRippleOnView(mTextCloseTv);
			setMaterialRippleOnView(mTextViewTv);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	private void setMaterialRippleOnView(View mView) {
		MaterialRippleLayout.on(mView).rippleColor(Color.parseColor("#FFFFFF"))
				.rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true)
				.rippleBackground(Color.parseColor("#00000000")).create();
	}
	
	private void setOnClickListener(){
		mTextViewTv = mBottomSheetAnyDo.getTextViewTv();
		mTextCloseTv = mBottomSheetAnyDo.getTextCloseTv();
		
		if (mTextViewTv != null) {
			mTextViewTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new NotificationHandle(ApplicationLoader.getApplication().getApplicationContext(), mId, mCategory, mType).getIntent();
					startActivity(mIntent);
					mBottomSheetAnyDo.dismiss();
				}
			});
		}

		if (mTextCloseTv != null) {
			mTextCloseTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mBottomSheetAnyDo!=null)
						mBottomSheetAnyDo.dismiss();
				}
			});
		}
	}

	private Drawable getRoundedBitmap(int imageId) {
		Bitmap src = BitmapFactory.decodeResource(getResources(), imageId);
		Bitmap dst;
		if (src.getWidth() >= src.getHeight()) {
			dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight()
					/ 2, 0, src.getHeight(), src.getHeight());
		} else {
			dst = Bitmap.createBitmap(src, 0,
					src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(),
					src.getWidth());
		}
		RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory
				.create(getResources(), dst);
		roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
		roundedBitmapDrawable.setAntiAlias(true);
		return roundedBitmapDrawable;
	}

	public SpannableStringBuilder getSpannableStringWithCounter(String mHeader,
			String mNotification, String mTail) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		stringBuilder.append(mHeader);
		stringBuilder.append(mNotification);
		stringBuilder.setSpan(new ForegroundColorSpan(
				R.color.toolbar_background), 0, mHeader.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		stringBuilder.setSpan(new RoundedBackgroundSpan(),
				mHeader.length() - 1,
				mHeader.length() + mNotification.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		stringBuilder.append(mTail);
		return stringBuilder;
	}

	@SuppressLint("NewApi")
	private void setSystemBarTint(int position) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(getResources().getColor(
						android.R.color.transparent));
			} else {
				mTintManager = new SystemBarTintManager(
						AnyDoNotificationActivity.this);
				// enable status bar tint
				mTintManager.setStatusBarTintEnabled(true);
				mTintManager.setStatusBarTintColor(getResources().getColor(
						android.R.color.transparent));
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}
}
