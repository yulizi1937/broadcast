/**
 * 
 */
package com.application.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class DontPressWithParentImageView extends ImageView {

	public DontPressWithParentImageView(Context context) {
		super(context);
	}

	public DontPressWithParentImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DontPressWithParentImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (pressed && getParent() instanceof View
				&& ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}

}
