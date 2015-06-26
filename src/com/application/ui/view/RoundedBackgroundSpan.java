/**
 * 
 */
package com.application.ui.view;

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
		paint.setColor(Color.parseColor("#FF0000"));//background color
		canvas.drawRoundRect(rect, 3, 3, paint);
//		canvas.drawCircle(rect.right, rect.left, 7, paint);
		paint.setColor(Color.parseColor("#FFFFFF"));//text color
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
