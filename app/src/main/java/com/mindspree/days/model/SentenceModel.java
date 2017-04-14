package com.mindspree.days.model;

import android.widget.TextView;

import com.mindspree.days.R;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.engine.EngineDBInterface;
import com.mindspree.days.model.TimelineModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 19-10-2015.
 */
public class SentenceModel{

    public int mLocationCount = 0;
    public int mPhotoCount = 0;
    public String mMood = "";
    public String mSentence = "";
    public String mWeather = "";

    private String mPhotoGroup = "";


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


