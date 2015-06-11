/*
 * This is the source code of Telegram for Android v. 2.0.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.application.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.utils.AndroidUtilities;
import com.application.utils.LocaleController;
import com.mobcast.R;

public class SharedDocumentCell extends FrameLayout {

	private ImageView placeholderImabeView;
	private TextView nameTextView;
	private TextView extTextView;
	private TextView dateTextView;
	private ImageView statusImageView;

	private boolean needDivider;

	private static Paint paint;

	private int TAG;

	private int icons[] = { R.drawable.media_doc_blue,
			R.drawable.media_doc_green, R.drawable.media_doc_red,
			R.drawable.media_doc_yellow };

	public SharedDocumentCell(Context context) {
		super(context);

		if (paint == null) {
			paint = new Paint();
			paint.setColor(0xffd9d9d9);
			paint.setStrokeWidth(1);
		}

		placeholderImabeView = new ImageView(context);
		addView(placeholderImabeView);
		LayoutParams layoutParams = (LayoutParams) placeholderImabeView
				.getLayoutParams();
		layoutParams.width = AndroidUtilities.dp(40);
		layoutParams.height = AndroidUtilities.dp(40);
		layoutParams.leftMargin = AndroidUtilities.dp(12);
		layoutParams.rightMargin = 0;
		layoutParams.topMargin = AndroidUtilities.dp(8);
		layoutParams.gravity = Gravity.LEFT;
		placeholderImabeView.setLayoutParams(layoutParams);

		extTextView = new TextView(context);
		extTextView.setTextColor(0xffffffff);
		extTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		extTextView.setTypeface(AndroidUtilities
				.getTypeface("fonts/rmedium.ttf"));
		extTextView.setLines(1);
		extTextView.setMaxLines(1);
		extTextView.setSingleLine(true);
		extTextView.setGravity(Gravity.CENTER);
		extTextView.setEllipsize(TextUtils.TruncateAt.END);
		addView(extTextView);
		layoutParams = (LayoutParams) extTextView.getLayoutParams();
		layoutParams.width = AndroidUtilities.dp(32);
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.topMargin = AndroidUtilities.dp(22);
		layoutParams.leftMargin = LocaleController.isRTL ? 0 : AndroidUtilities
				.dp(16);
		layoutParams.rightMargin = LocaleController.isRTL ? AndroidUtilities
				.dp(16) : 0;
		layoutParams.gravity = LocaleController.isRTL ? Gravity.RIGHT
				: Gravity.LEFT;
		extTextView.setLayoutParams(layoutParams);

		nameTextView = new TextView(context);
		nameTextView.setTextColor(0xff222222);
		nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		nameTextView.setTypeface(AndroidUtilities
				.getTypeface("fonts/rmedium.ttf"));
		nameTextView.setLines(1);
		nameTextView.setMaxLines(1);
		nameTextView.setSingleLine(true);
		nameTextView.setEllipsize(TextUtils.TruncateAt.END);
		nameTextView.setGravity((LocaleController.isRTL ? Gravity.RIGHT
				: Gravity.LEFT) | Gravity.CENTER_VERTICAL);
		addView(nameTextView);
		layoutParams = (LayoutParams) nameTextView.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.topMargin = AndroidUtilities.dp(5);
		layoutParams.leftMargin = LocaleController.isRTL ? AndroidUtilities
				.dp(8) : AndroidUtilities.dp(72);
		layoutParams.rightMargin = LocaleController.isRTL ? AndroidUtilities
				.dp(72) : AndroidUtilities.dp(8);
		layoutParams.gravity = LocaleController.isRTL ? Gravity.RIGHT
				: Gravity.LEFT;
		nameTextView.setLayoutParams(layoutParams);

		statusImageView = new ImageView(context);
		statusImageView.setVisibility(INVISIBLE);
		addView(statusImageView);
		layoutParams = (LayoutParams) statusImageView.getLayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.topMargin = AndroidUtilities.dp(35);
		layoutParams.leftMargin = LocaleController.isRTL ? AndroidUtilities
				.dp(8) : AndroidUtilities.dp(72);
		layoutParams.rightMargin = LocaleController.isRTL ? AndroidUtilities
				.dp(72) : AndroidUtilities.dp(8);
		layoutParams.gravity = LocaleController.isRTL ? Gravity.RIGHT
				: Gravity.LEFT;
		statusImageView.setLayoutParams(layoutParams);

		dateTextView = new TextView(context);
		dateTextView.setTextColor(0xff999999);
		dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		dateTextView.setLines(1);
		dateTextView.setMaxLines(1);
		dateTextView.setSingleLine(true);
		dateTextView.setEllipsize(TextUtils.TruncateAt.END);
		dateTextView.setGravity((LocaleController.isRTL ? Gravity.RIGHT
				: Gravity.LEFT) | Gravity.CENTER_VERTICAL);
		addView(dateTextView);
		layoutParams = (LayoutParams) dateTextView.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.topMargin = AndroidUtilities.dp(30);
		layoutParams.leftMargin = AndroidUtilities.dp(72);
		layoutParams.rightMargin = AndroidUtilities.dp(8);
		layoutParams.gravity = Gravity.LEFT;
		dateTextView.setLayoutParams(layoutParams);
	}

	private int getThumbForNameOrMime(String name, String mime) {
		if (name != null && name.length() != 0) {
			int color = -1;
			if (name.contains(".doc") || name.contains(".txt")
					|| name.contains(".psd")) {
				color = 0;
			} else if (name.contains(".xls") || name.contains(".csv")) {
				color = 1;
			} else if (name.contains(".pdf") || name.contains(".ppt")
					|| name.contains(".key")) {
				color = 2;
			} else if (name.contains(".zip") || name.contains(".rar")
					|| name.contains(".ai") || name.contains(".mp3")
					|| name.contains(".mov") || name.contains(".avi")) {
				color = 3;
			}
			if (color == -1) {
				int idx;
				String ext = (idx = name.lastIndexOf(".")) == -1 ? "" : name
						.substring(idx + 1);
				if (ext.length() != 0) {
					color = ext.charAt(0) % icons.length;
				} else {
					color = name.charAt(0) % icons.length;
				}
			}
			return icons[color];
		}
		return icons[0];
	}

	public void setTextAndValueAndTypeAndThumb(String text, String value,
			String type, String thumb, int resId) {
		nameTextView.setText(text);
		dateTextView.setText(value);
		if (type != null) {
			extTextView.setVisibility(VISIBLE);
			extTextView.setText(type);
		} else {
			extTextView.setVisibility(INVISIBLE);
		}
		if (resId == 0) {
			placeholderImabeView.setImageResource(getThumbForNameOrMime(text,
					type));
			placeholderImabeView.setVisibility(VISIBLE);
		} else {
			placeholderImabeView.setImageResource(resId);
			placeholderImabeView.setVisibility(VISIBLE);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(
				widthMeasureSpec,
				MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56)
						+ (needDivider ? 1 : 0), MeasureSpec.EXACTLY));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (needDivider) {
			canvas.drawLine(AndroidUtilities.dp(72), getHeight() - 1,
					getWidth() - getPaddingRight(), getHeight() - 1, paint);
		}
	}
}
