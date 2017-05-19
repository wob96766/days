package com.mindspree.days.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.SampleDecorator;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.DatelineModel;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 19-10-2015.
 */
public class DatePickActivity extends BaseActivity {

    private CalendarPickerView mCalendarView;
    private DBWrapper mDBWrapper;
    private ArrayList<DatelineModel> mDataList;
    private ArrayList<DatelineModel> mDatelineList;

    public static void startActivityForResult(Fragment context, int intentCode) {
        Intent intent = new Intent(context.getActivity(), DatePickActivity.class);
        context.startActivityForResult(intent, intentCode);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
        setContentView(R.layout.activity_datepick);
        initData();
        initView();

        requestDaily();
        requestEmotions();


    }

    private void initView() {
        AQuery aq = new AQuery(this);
        Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(-1);
                finish();
            }
        });
        aq.dismiss();
    }


    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            List<Date> selectedDate = mCalendarView.getSelectedDates();
            if(selectedDate.size() <= 1) {
                showToast(getAppText(R.string.message_select_period));
            } else {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Intent intent = new Intent();
                intent.putExtra(AppConfig.IntentParam.START, dateFormatter.format(selectedDate.get(0)));
                intent.putExtra(AppConfig.IntentParam.END, dateFormatter.format(selectedDate.get(selectedDate.size()-1)));
                setResult(1, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestEmotions() {
        mDatelineList = mDBWrapper.getDatelineList();
        loadView();
    }

    private void requestDaily() {
        mDataList = mDBWrapper.getDatelineList();
    }

    public void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
    }

    public void loadView() {
        AQuery aq = new AQuery(this);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        SampleDecorator decorator = new SampleDecorator(mDatelineList);


        mCalendarView = (CalendarPickerView) aq.id(R.id.view_calendar).getView();
        mCalendarView.setDecorators(Arrays.<CalendarCellDecorator>asList(decorator));
        mCalendarView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                return false;
            }
        });
        mCalendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 1);
        Date fromday = new Date();
        if(mDataList.size() > 0){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                fromday = dateFormat.parse(mDataList.get(0).mUpdateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mCalendarView.setOnInvalidDateSelectedListener(new CalendarPickerView.OnInvalidDateSelectedListener() {
            @Override
            public void onInvalidDateSelected(Date date) {
                showToast(getAppText(R.string.message_select_invaliddate));
            }
        });
        mCalendarView.init(fromday, today.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
        ;

        aq.dismiss();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
            }
        }
    };

}
