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


        String hash_string="";

        if(mSentence == null || mSentence.equals("")) {
            if (mWeather.equals("")) {
                hash_string = hash_string + String.format(" %s \n\n", "원하시는 문구를 직접 작성하실 수도 있습니다 ");

                hash_string = hash_string + String.format(AppUtils.getAppText(R.string.format_summarize_date_withweather), mWeather, mLocationCount, mPhotoCount);

                return String.format(AppUtils.getAppText(R.string.format_summarize_date), mLocationCount, mPhotoCount);
            } else {


                hash_string = hash_string + String.format(" %s \n\n", "원하시는 문구를 직접 작성하실 수도 있습니다 ");

                hash_string = hash_string + String.format(AppUtils.getAppText(R.string.format_summarize_date_withweather), mWeather, mLocationCount, mPhotoCount);


            }
            return hash_string;

        } else {

            ArrayList sentenceHash;
            sentenceHash = new ArrayList<String>(Arrays.asList(mSentence.split("#")));
            String sentenceOnly = sentenceHash.get(0).toString();
            String hashOnly ="";

            if(sentenceHash.size()>=2)
            {
                for(int i=1;i<sentenceHash.size();i++)
                    hashOnly = hashOnly + "#" + sentenceHash.get(i).toString();
            }

            return String.format("%s \n\n %s", sentenceOnly, hashOnly);

        }
    }
}


