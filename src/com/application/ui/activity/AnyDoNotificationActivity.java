/**
 * 
 */
package com.application.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;

import com.application.ui.view.BottomSheet;
import com.application.ui.view.RoundedBackgroundSpan;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class AnyDoNotificationActivity extends AppCompatActivity {

	private static final String TAG = AnyDoNotificationActivity.class
			.getSimpleName();

	private BottomSheet mBottomSheet;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anydo_notification);
		setUpAnyDoNotification();
	}

	private void setUpAnyDoNotification() {
		showDialog(0);
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
}
