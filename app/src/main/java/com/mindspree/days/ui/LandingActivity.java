package com.mindspree.days.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.model.DatelineModel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class LandingActivity extends BaseActivity {

    private String date;
    private String action;
    private DBWrapper mDBWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        date = i.getStringExtra("date");
        action = i.getStringExtra("action");
        /*if(action == null){
            if(i.getData() != null && i.getData().getQueryParameterNames().contains("target_url")) {
                Uri uri = Uri.parse(getIntent().getData().getQueryParameter("target_url"));
                date = uri.getQueryParameter("date");
                action = uri.getQueryParameter("action");
            } else {
                date = i.getData().getQueryParameter("date");
                action = i.getData().getQueryParameter("action");
            }
        }

        if(action.equals("dateline")){
            if(AppPreference.getInstance().IsLogin()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                String dateString = dateFormat.format(date);
                TimelineActivity.startActivity(getContext(), dateString, mDBWrapper.getDateline(dateString));
            } else {

                SplashActivity.startActivity(getContext());
            }
        } else if(action.equals("app")){

        }*/
        if(AppPreference.getInstance().IsLogin()) {
            MainActivity.startActivity(getContext());
        } else {
            SplashActivity.startActivity(getContext());
        }
        finish();
    }
}
