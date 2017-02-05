package com.mindspree.days.model;

import com.mindspree.days.R;

/**
 * Created by vision51 on 2016. 12. 30..
 */

public class DailyModel {

    static public final DailyModel[] mMoodList = {new DailyModel(R.mipmap.ic_happy, "Happy"),new DailyModel(R.mipmap.ic_angry, "Angry"),new DailyModel(R.mipmap.ic_sad, "Sad"), new DailyModel(R.mipmap.ic_busy,"Busy")};
    static public final DailyModel[] mMoodBackgroundList = {new DailyModel(R.mipmap.bg_happy, "Happy"),new DailyModel(R.mipmap.bg_angry, "Angry"),new DailyModel(R.mipmap.bg_sad, "Sad"), new DailyModel(R.mipmap.bg_busy,"Busy"), new DailyModel(R.mipmap.bg_data,"data")};

    public int mResourceId = 0;
    public String mMood = "";
    public String mUpdateDate = "";

    public DailyModel(){
    }

    public DailyModel(String mood, String updateDate){
        mMood = (mood == null ? "" : mood);
        mUpdateDate = updateDate;
    }

    public DailyModel(int resourceId, String mood){
        mResourceId = resourceId;
        mMood = mood;
    }

    static public int getIcon(String mood){
        for(int i=0 ; i<mMoodList.length ; i++){
            if(mMoodList[i].toString().equals(mood))
                return mMoodList[i].mResourceId;
        }
        return -1;
    }
    static public int getBackground(String mood){
        for(int i=0 ; i<mMoodBackgroundList.length ; i++){
            if(mMoodBackgroundList[i].toString().equals(mood))
                return mMoodBackgroundList[i].mResourceId;
        }
        return -1;
    }
    @Override
    public String toString(){
        return mMood;
    }
}
