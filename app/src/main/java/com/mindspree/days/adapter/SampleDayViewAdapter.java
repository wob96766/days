package com.mindspree.days.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mindspree.days.R;
import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.DayViewAdapter;

/**
 * Created by vision51 on 2016. 12. 15..
 */

public class SampleDayViewAdapter implements DayViewAdapter {

    @Override
    public void makeCellView(CalendarCellView parent) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_calendar, null);
        parent.addView(layout);
        parent.setDayOfMonthTextView((TextView) layout.findViewById(R.id.view_day));

    }
}