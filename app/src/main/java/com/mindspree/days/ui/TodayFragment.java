package com.mindspree.days.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.MoodAdapter;
import com.mindspree.days.adapter.TimelineAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.interfaces.OnItemClickListener;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.SentenceModel;
import com.mindspree.days.model.WeatherModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.RequestCode;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class TodayFragment extends BaseFragment{


    protected ViewGroup mRootView;
    protected TimelineAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int mPastVisibleItemCount = 0;
    private SwipeRefreshLayout mRefreshLayout;
    private DBWrapper mDBWrapper;

    private ArrayList<TimelineModel> mDataList;
    private TextView mTextSentence;
    private TextView mTextMood;

    AlertDialog mMoodDialog = null;
    private BroadcastReceiver mRefreshCast;
    private SentenceModel mSentence;


    @Override
    public void onDetach() {
        try {
            if(mRefreshCast != null)
                getActivity().unregisterReceiver(mRefreshCast);
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
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
            mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_today, container, false);
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
        initView();
        requestSentence();
        requestTimeline();
        RequestWeather();
    }

    private void RequestWeather() {
        mHttpClient.RequestWeather(getContext(), mPreference.getLatitude(), mPreference.getLongitude(), getAppText(R.string.openweather_key), mAsyncHttpResponse);
    }

    protected void RegistDateRefreshCast()
    {
        mRefreshCast = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                requestSentence();
                requestTimeline();
                RequestWeather();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.Broadcast.REFRESH_DATA);
        getActivity().registerReceiver(mRefreshCast, filter);
    }

    public void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
        RegistDateRefreshCast();
    }

    public void initView() {
        AQuery aq = new AQuery(mRootView);

        aq.id(R.id.text_content).clicked(mOnClickListener);
        mRecyclerView = (RecyclerView)aq.id(R.id.view_recycler).getView();
        mAdapter = new TimelineAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
                mVisibleItemCount = layoutManager.getChildCount();
                mTotalItemCount = layoutManager.getItemCount();
                mPastVisibleItemCount = layoutManager.findFirstVisibleItemPosition();

                /*if (!mIsLoaded && mModel.IsExistMore()) {
                        mIsLoaded = true;
                        requestContentList(mModel.mNextCursor);
                }*/
            }
        });
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRefreshLayout = (SwipeRefreshLayout) aq.id(R.id.view_refresh).getView();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestSentence();
                requestTimeline();
                mHttpClient.RequestWeather(getContext(), mPreference.getLatitude(), mPreference.getLongitude(), getAppText(R.string.openweather_key), mAsyncHttpResponse);
                mRefreshLayout.setRefreshing(false);
            }
        });
        mTextSentence = aq.id(R.id.text_sentence).getTextView();
        mTextMood = aq.id(R.id.text_mood).clicked(mOnClickListener).getTextView();
        aq.dismiss();
    }

    public void requestTimeline() {
        mDataList = mDBWrapper.getTiemlineList();
        mAdapter.setDataSource(mDataList);

    }

    public void requestSentence() {
        String weather = "";
        if(mSentence != null) {
            weather = mSentence.mWeather;
        }
        mSentence = mDBWrapper.getSentence();
        mSentence.mWeather = weather;
        mTextSentence.setText(mSentence.getSummarize());
        mTextMood.setText(mSentence.mMood);
        int iconId = DailyModel.getIcon(mSentence.mMood);
        if(iconId>0) {
            mTextMood.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
        }
    }

    public void finishView() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    public void showMoodDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getAppText(R.string.message_ask_emotion));
        builder.setAdapter(new MoodAdapter(getContext(), R.layout.card_mood, R.id.text_title, DailyModel.mMoodList), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        mDBWrapper.setMood(DailyModel.mMoodList[item].toString());
                        mTextMood.setText(DailyModel.mMoodList[item].toString());
                        int iconId = DailyModel.getIcon(DailyModel.mMoodList[item].toString());
                        if(iconId>0) {
                            mTextMood.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
                        }
                        getActivity().sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_CALENDAR));
                        break;
                }
                mMoodDialog.dismiss();
            }
        });
        /*builder.setSingleChoiceItems(mMoodList, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        mDBWrapper.setMood(mMoodList[item].toString());
                        mTextMood.setText(mMoodList[item].toString());
                        getActivity().sendBroadcast(new Intent(AppConfig.Broadcast.REFRESH_CALENDAR));
                        break;
                }
                mMoodDialog.dismiss();
            }
        });*/
        mMoodDialog = builder.create();
        mMoodDialog.show();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.text_mood:
                    showMoodDialog();
                    break;
            }
        }
    };

    OnItemClickListener mOnItemClickListener = new OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            switch (view.getId()){
                case R.id.image_thumnail1: {
                    TimelineModel model = mAdapter.getItem(position);
                    if (model.getImageList().size() > 0) {
                        ImagePickActivity.startActivity(getContext(), model);
                    }
                }
                break;
                case R.id.image_thumnail2: {
                    TimelineModel model = mAdapter.getItem(position);
                    if (model.getImageList().size() > 1) {
                        ImagePickActivity.startActivity(getContext(), model);
                    }
                }
                break;
                case R.id.image_thumnail3: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getImageList().size() > 2) {
                        ImagePickActivity.startActivity(getContext(), model);
                    }
                }
                break;
                case R.id.image_location: {
                    TimelineModel model = mAdapter.getItem(position);
                    SearchMapActivity.startActivity(getContext(), model.mLocationId, model.mLatitude, model.mLogitude);
                }
                    break;
            }
        }
    };

    // 서버 응답 처리
    AsyncHttpResponse mAsyncHttpResponse = new AsyncHttpResponse()
    {
        @Override
        public void onHttpStart(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_WEATHER:
                    break;
            }
        }

        @Override
        public void onHttpFinish(int reqCode) {
            switch(reqCode)
            {
                case RequestCode.GET_WEATHER:
                    break;
            }
        }

        @Override
        public void onHttpSuccess(int reqCode, int code, Header[] headers, byte[] response, Object tag) {
            String data = new String(response);
            try {
                switch(reqCode) {
                    case RequestCode.GET_WEATHER:
                        try {
                            WeatherModel model = WeatherModel.parseData(data);
                            mSentence.mWeather = model.getWeather();
                            mTextSentence.setText(mSentence.getSummarize());
                        } catch (Exception e) {
                            //showToast(getString(R.string.message_network_error));
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onHttpFailure(int reqCode, int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            try {
                switch(reqCode)
                {
                    case RequestCode.GET_WEATHER:
                        showToast(getString(R.string.message_network_error));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
}
