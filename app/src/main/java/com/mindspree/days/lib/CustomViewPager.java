package com.mindspree.days.lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Admin on 29-10-2015.
 */
public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof TouchImageView) {
//            return ((TouchImageView) v).canScrollHorizontallyFroyo(-dx);
            return super.canScroll(v, checkV, dx, x, y);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }

}