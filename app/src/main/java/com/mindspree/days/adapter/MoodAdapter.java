package com.mindspree.days.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindspree.days.R;
import com.mindspree.days.model.DailyModel;


/**
 * Created by vision51 on 2017. 1. 19..
 */

public class MoodAdapter extends ArrayAdapter<DailyModel> {
    public MoodAdapter(Context context, int resource, int textViewResourceId, DailyModel[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Use super class to create the View
        View v = super.getView(position, convertView, parent);
        TextView tv = (TextView)v.findViewById(R.id.text_title);
        //Put the image on the TextView
        tv.setCompoundDrawablesWithIntrinsicBounds(this.getItem(position).mResourceId, 0, 0, 0);


        return v;
    }
}
