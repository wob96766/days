package com.mindspree.days.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.SampleDecorator;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.DatelineModel;
import com.mindspree.days.model.TimelineModel;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SummarizedFragment extends BaseFragment{

    protected ViewGroup mRootView;
    private CalendarPickerView mCalendarView;
    private DBWrapper mDBWrapper;
    private ArrayList<DatelineModel> mDatelineList;
    private SwipeRefreshLayout mRefreshLayout;
    private BroadcastReceiver mRefreshCast;


    @Override
    public void onDetach() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
            if(mRefreshCast != null)
                getActivity().unregisterReceiver(mRefreshCast);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onDetach();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null)
        {
            mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_summarized, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
        {
            parent.removeView(mRootView);
        }
        return mRootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();


        requestEmotions();
    }

    private void requestEmotions() {
        mDatelineList = mDBWrapper.getDatelineList();
        loadView();
    }

    protected void RegistDateRefreshCast()
    {
        mRefreshCast = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                requestEmotions();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.Broadcast.REFRESH_CALENDAR);
        getActivity().registerReceiver(mRefreshCast, filter);
    }

    public void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
        RegistDateRefreshCast();
    }

    public void loadView() {
        AQuery aq = new AQuery(mRootView);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        mCalendarView = (CalendarPickerView) aq.id(R.id.view_calendar).getView();
        SampleDecorator decorator = new SampleDecorator(mDatelineList);
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
                // cancel all highlight
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                String dateString = dateFormat.format(date);
                TimelineModel model_timeline = new TimelineModel();
                TimelineActivity.startActivity(getContext(), dateString, mDBWrapper.getDateline(dateString),model_timeline,"");
                mCalendarView.selectDate(date);
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });
        mCalendarView.setOnInvalidDateSelectedListener(new CalendarPickerView.OnInvalidDateSelectedListener() {
            @Override
            public void onInvalidDateSelected(Date date) {
                showToast(getAppText(R.string.message_select_invaliddate));
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) aq.id(R.id.view_refresh).getView();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestEmotions();
                mRefreshLayout.setRefreshing(false);
            }
        });


        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 1);
        Date fromday = new Date();
        if(mDatelineList.size() > 0){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                fromday = dateFormat.parse(mDatelineList.get(0).mUpdateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mCalendarView.init(fromday, today.getTime())
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
               ;// .displayOnly(); //readonly mode if you need

        aq.dismiss();
    }
    public void finishView() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
            }
        }
    };
}
