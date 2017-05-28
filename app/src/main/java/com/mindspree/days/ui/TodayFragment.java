package com.mindspree.days.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.MoodAdapter;
import com.mindspree.days.adapter.TimelineAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.interfaces.OnItemClickListener;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.DailyModel;
import com.mindspree.days.model.TimelineModel;
import com.mindspree.days.model.SentenceModel;
import com.mindspree.days.model.WeatherModel;
import com.mindspree.days.network.AsyncHttpResponse;
import com.mindspree.days.network.RequestCode;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


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
    private TextView mTextLocationcount;
    private TextView mTextPhotocount;
    private ImageView mImageWeather;
    private TextView mTextMood;
    private TextView mTextDate;
    private TextView mTextDay;
    private TextView mTextYearMonth;
    private TextView mTextDaysbot;

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
        mTextPhotocount= aq.id(R.id.textPhotocount).getTextView();
        mTextLocationcount= aq.id(R.id.textLocationcount).getTextView();
        mImageWeather =aq.id(R.id.imageWeather).getImageView();
        mTextMood = aq.id(R.id.text_mood).clicked(mOnClickListener).getTextView();
        mTextYearMonth = aq.id(R.id.text_yearmonth).getTextView();
        mTextDay = aq.id(R.id.text_day).getTextView();
        mTextDate = aq.id(R.id.text_date).getTextView();
        mTextDaysbot = aq.id(R.id.textDaysbot).getTextView();
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
        mTextPhotocount.setText(String.format("%d",mSentence.mPhotoCount));
        mTextLocationcount.setText(String.format("%d",mSentence.mLocationCount));


        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM");
        Calendar cal = Calendar.getInstance();
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        cal.add(Calendar.DATE, 0);
        String DateToday1 = dateFormat1.format(cal.getTime()); //your formatted date here

        DateFormat dateFormat2 = new SimpleDateFormat("dd");
        String DateToday2 = dateFormat2.format(cal.getTime()); //your formatted date here


        mTextYearMonth.setText(String.format("%s", DateToday1));

//        SpannableString ss1=  new SpannableString(String.format("%s %s", DateToday2,dayOfTheWeek));
//        ss1.setSpan(new RelativeSizeSpan(2f), 0,5, 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color

        // Get current hour






        int textSize1 = getResources().getDimensionPixelSize(R.dimen.text_13)*2;
        int textSize2 = getResources().getDimensionPixelSize(R.dimen.text_15);

        String text1 = DateToday2;
        String text2 = dayOfTheWeek;

        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(textSize1), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(textSize2), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        CharSequence finalText = TextUtils.concat(span1, " ", span2);
        mTextDate.setText(finalText);


        if(weather.equals(AppUtils.getAppText(R.string.text_weather_clear)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_sun_2x);
        }else if(weather.equals(AppUtils.getAppText(R.string.text_weather_clouds)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_cloud_2x);
        }else if(weather.equals(AppUtils.getAppText(R.string.text_weather_dust)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_dust_2x);
        }else if(weather.equals(AppUtils.getAppText(R.string.text_weather_haze)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_mist_2x);
        }else if(weather.equals(AppUtils.getAppText(R.string.text_weather_mist)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_mist_2x);
        }else if(weather.equals(AppUtils.getAppText(R.string.text_weather_fog)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_mist_2x);
        }else if(weather.equals(AppUtils.getAppText(R.string.text_weather_rain)))
        {
            mImageWeather.setImageResource(R.mipmap.ic_rain_2x);
        }else
        {
            mImageWeather.setImageResource(R.mipmap.ic_sun_2x);
        }



        //////////////////////////////////////////////
        //      Days bot notification windows       //
        //////////////////////////////////////////////

        String mMood = mTextMood.getText().toString();
        String mMood_kr ="";
        if(mMood!=null || mMood!="") {

            if(hourofday>=17 && hourofday <=21){

                if(mMood.equals("Happy")){
                    mMood_kr="Days: 오늘은 기분 좋은 하루군요!";  // For sentence
                }else if(mMood.equals("Angry")){
                    mMood_kr="Days: 오늘은 기분이 별로인가 봐요";
                }else if(mMood.equals("Sad")){
                    mMood_kr="Days: 오늘은 좀 슬픈신가 봐요";
                }else if(mMood.equals("Busy")){
                    mMood_kr="Days: 오늘 하루도 바쁘셨군요!";
                }
                mTextDaysbot.setText(mMood_kr);

            }

//            mTextDaysbot.setText("Days:~$ 오늘은 어떤 하루였나요?"+ "\n"+ "mood를 설정해 주세요");


        }else {
            if(hourofday >=3)
            {
                mTextDaysbot.setText("Days: 오늘은 어떤 하루였나요?"+ "\n"+ "mood를 설정해 주세요");
            }



        }




                int iconId = DailyModel.getIcon(mSentence.mMood);
                if(iconId>0) {
                    mTextMood.setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
                }


        // Change font
        mTextSentence.setTypeface(MainActivity.mTypeface);
        mTextDaysbot.setTypeface(MainActivity.mBoldTypeface);
        mTextYearMonth.setTypeface(MainActivity.mBoldTypeface);
        mTextDate.setTypeface(MainActivity.mBoldTypeface);
        mTextMood.setTypeface(MainActivity.mBoldTypeface);

        }


    public String requestWeather() {
        String weather = "";
        if(mSentence != null) {
            weather = mSentence.mWeather;
        }
        mSentence = mDBWrapper.getSentence();
        mSentence.mWeather = weather;

        return weather;
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
                    if (model.getPhotoList().size() > 0) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(0));
                    }
                }
                break;
                case R.id.image_thumnail2: {
                    TimelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 1) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(1));
                    }
                }
                break;
                case R.id.image_thumnail3: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 2) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(2));
                    }
                }
                break;
                case R.id.image_thumnail4: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 3) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(3));
                    }
                }
                break;
                case R.id.image_thumnail5: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 4) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(4));
                    }
                }
                break;
                case R.id.image_thumnail6: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 5) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(5));
                    }
                }
                break;
                case R.id.image_thumnail7: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 6) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(6));
                    }
                }
                break;
                case R.id.image_thumnail8: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 7) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(7));
                    }
                }
                break;
                case R.id.image_thumnail9: {
                    TimelineModel model = mAdapter.getItem(position);
                    if(model.getPhotoList().size() > 8) {
                        ImagePickActivity.startActivity(getContext(), model, model.getPhotoList().get(8));
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
                            mTextPhotocount.setText(mSentence.mPhotoCount);
                            mTextLocationcount.setText(mSentence.mLocationCount);

                            if(model.mWeather.equals("Clear"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_sun_2x);
                            }else if(model.mWeather.equals("Clouds"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_cloud_2x);
                            }else if(model.mWeather.equals("Dust"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_dust_2x);
                            }else if(model.mWeather.equals("Haze"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_mist_2x);
                            }else if(model.mWeather.equals("Mist"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_mist_2x);
                            }else if(model.mWeather.equals("Fog"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_mist_2x);
                            }else if(model.mWeather.equals("Rain"))
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_rain_2x);
                            }else
                            {
                                mImageWeather.setImageResource(R.mipmap.ic_sun_2x);
                            }




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
