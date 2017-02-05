package com.mindspree.days.model;

import com.mindspree.days.R;
import com.mindspree.days.lib.AppUtils;

/**
 * Created by Admin on 19-10-2015.
 */
public class SentenceModel{

    public int mLocationCount = 0;
    public int mPhotoCount = 0;
    public String mMood = "";
    public String mSentence = "";
    public String mWeather = "";

    public SentenceModel(){
    }

    public String getSummarize(){
        if(mSentence == null || mSentence.equals("")) {
            if (mWeather.equals("")) {
                return String.format(AppUtils.getAppText(R.string.format_summarize_date), mLocationCount, mPhotoCount);
            } else {
                return String.format(AppUtils.getAppText(R.string.format_summarize_date_withweather), mWeather, mLocationCount, mPhotoCount);
            }
        } else {
            return mSentence;
        }
    }
}


