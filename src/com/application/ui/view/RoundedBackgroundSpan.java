/**
 * 
 */
package com.application.ui.view;

import com.application.utils.ApplicationLoader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class RoundedBackgroundSpan extends ReplacementSpan {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.style.ReplacementSpan#draw(android.graphics.Canvas,
	 * java.lang.CharSequence, int, int, float, int, int, int,
	 * android.graphics.Paint)
	 */
	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {
		// TODO Auto-generated method stub
		RectF rect = new RectF(x, top,
				x + MeasureText(paint, text, start, end), bottom);
		
		switch(ApplicationLoader.getPreferences().getAppTheme()){
		case 0:
			paint.setColor(Color.parseColor("#FE9915"));//text color
			break;
		case 1:
			paint.setColor(Color.parseColor("#FF4081"));//text color
			break;
		case 2:
			paint.setColor(Color.parseColor("#2196F3"));//text color
			break;
		case 3:
			paint.setColor(Color.parseColor("#3F51B5"));//text color
			break;
		case 4:
			paint.setColor(Color.parseColor("#FF9800"));//text color
			break;
		case 5:
			paint.setColor(Color.parseColor("#FF5722"));//text color
			break;
		case 6:
			paint.setColor(Color.parseColor("#FF4081"));//text color
			break;
		case 7:
			paint.setColor(Color.parseColor("#FF4081"));//text color
			break;
		case 8:
			paint.setColor(Color.parseColor("#2196F3"));//text color
			break;
		case 9:
			paint.setColor(Color.parseColor("#FE9915"));//text color
			break;
		case 10:
			paint.setColor(Color.parseColor("#3F51B5"));//text color
			break;
		case 11:
			paint.setColor(Color.parseColor("#FF4081"));//text color
			break;
		default:
			paint.setColor(Color.parseColor("#FF0000"));//background color	
			break;
		}
		canvas.drawRoundRect(rect, 3, 3, paint);
//		canvas.drawCircle(rect.right, rect.left, 7, paint);
		
		paint.setColor(Color.parseColor("#FFFFFF"));// text color
		canvas.drawText(text, start, end, x, y, paint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.style.ReplacementSpan#getSize(android.graphics.Paint,
	 * java.lang.CharSequence, int, int, android.graphics.Paint.FontMetricsInt)
	 */
	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end,
			FontMetricsInt fm) {
		// TODO Auto-generated method stub
		return Math.round(MeasureText(paint, text, start, end));
	}

	private float MeasureText(Paint paint, CharSequence text, int start,
			int end) {
		return paint.measureText(text, start, end);
	}

}
