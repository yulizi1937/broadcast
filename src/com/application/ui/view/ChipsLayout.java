/**
 * 
 */
package com.application.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ChipsLayout extends LinearLayout {

	private AppCompatTextView mAppCompatTextView;
	private ImageView mImageView;
	private LinearLayout mRootLayout;
	
	public OnChipListener mChipListener;

	public interface OnChipListener {
		public void onChipDownload(View view);
	}
	
	public ChipsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(context);
		setClickable(true);
		setFocusable(true);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public ChipsLayout(Context context) {
		super(context);
		initializeViews(context);
		setClickable(true);
		setFocusable(true);
		// TODO Auto-generated constructor stub
	}

	public void setDrawable(int mDrawable) {
		mImageView.setImageResource(mDrawable);
	}

	public void setText(String mText) {
		mAppCompatTextView.setText(mText);
	}

	/**
	 * Inflates the views in the layout.
	 * 
	 * @param context
	 *            the current context for the view.
	 */
	private void initializeViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_chips_layout, this);
		mImageView = (ImageView) findViewById(R.id.itemChipsLayoutIv);
		mAppCompatTextView = (AppCompatTextView) findViewById(R.id.itemChipsLayoutTv);
		mRootLayout = (LinearLayout)findViewById(R.id.itemChipsRootLayout);
		mRootLayout.setClickable(true);
	}
	
	public LinearLayout getChipLayout(){
		return this.mRootLayout;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
}
