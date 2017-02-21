package com.mindspree.days.adapter;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.DatelineModel;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vision51 on 2016. 12. 15..
 */

public class SampleDecorator implements CalendarCellDecorator {

    ArrayList<DatelineModel> mDataList = new ArrayList<DatelineModel>();
    ArrayList<String> mEventList = new ArrayList<String>();
    HashMap<String, String> mEventMap = new HashMap<String, String>();

    public SampleDecorator(ArrayList<DatelineModel> source){
        mDataList = source;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat sdFormat= new SimpleDateFormat("yyyy-MM-dd");
        try {
            for(int i=0 ; i<source.size() ; i++) {
                Date date = dateFormat.parse(source.get(i).mUpdateDate);
                String dateString = sdFormat.format(date);
                mEventList.add(dateString);
                mEventMap.put(dateString, (mDataList.get(i).mMood == null ? "data" : mDataList.get(i).mMood));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void decorate(CalendarCellView cellView, Date date) {
        SimpleDateFormat sdFormat= new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = sdFormat.format(date);
        if(mEventList.contains(dateFormat)) {
            String dateString = Integer.toString(date.getDate());
            int iconId = DailyModel.getBackground(mEventMap.get(dateFormat));
            if(iconId>0) {
                /*SpannableString string = new SpannableString("111\n"+dateString);

                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                string.setSpan(span, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                cellView.getDayOfMonthTextView().setText(string);*/
                cellView.setBackgroundResource(iconId);
                SpannableString string = new SpannableString(dateString);
                string.setSpan(new RelativeSizeSpan(1.00f), 0, dateString.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                cellView.getDayOfMonthTextView().setText(string);
            } else {
                SpannableString string = new SpannableString(dateString);
                string.setSpan(new RelativeSizeSpan(1.00f), 0, dateString.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                cellView.getDayOfMonthTextView().setText(string);
            }

            //string.setSpan(new RelativeSizeSpan(0.50f), dateString.length(), string.length(),
             //       Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            /*String dateString = Integer.toString(date.getDate());
            SpannableString string = new SpannableString(dateString);
            string.setSpan(new RelativeSizeSpan(1.00f), 0, dateString.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            cellView.getDayOfMonthTextView().setText(string);
            int iconId = DailyModel.getIcon(mEventMap.get(dateFormat));
            if(iconId>0) {
                cellView.getDayOfMonthTextView().setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
                cellView.getDayOfMonthTextView().setCompoundDrawablePadding(0);

            }*/
        }else {
            String dateString = Integer.toString(date.getDate());
            SpannableString string = new SpannableString(dateString);
            string.setSpan(new RelativeSizeSpan(1.00f), 0, dateString.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            cellView.getDayOfMonthTextView().setText(string);
        }
    }
}
