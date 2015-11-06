package com.application.ui.showcaseview;

import android.graphics.Point;

/**
 * Created by deanwild on 04/08/15.
 */
public interface Target {
    Target NONE = new Target() {
        @Override
        public Point getPoint() {
            return new Point(1000000, 1000000);
        }

        @Override
        public int getRadius() {
            return 200;
        }
    };

    Point getPoint();

    int getRadius();
}
