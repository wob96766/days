package com.mindspree.days.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.DatelineAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.interfaces.OnItemClickListener;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.DatelineModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JournalFragment extends BaseFragment{

    protected ViewGroup mRootView;
    protected DatelineAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int mPastVisibleItemCount = 0;
    private SwipeRefreshLayout mRefreshLayout;
    private DBWrapper mPhotoWrapper;

    private ArrayList<DatelineModel> mDataList;
    private BroadcastReceiver mRefreshCast;
    private boolean mIsLoaded = false;
    private int mNextpage = 0;


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
            mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_journal, container, false);
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
        requestDatelinelist();
    }

    public void initData() {
        mPhotoWrapper = new DBWrapper(mPreference.getUserUid());
        RegistDateRefreshCast();
    }

    public void initView() {
        AQuery aq = new AQuery(mRootView);

        aq.id(R.id.text_content).clicked(mOnClickListener);
        mRecyclerView = (RecyclerView)aq.id(R.id.view_recycler).getView();
        mAdapter = new DatelineAdapter(getContext());
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

                if (!mIsLoaded) {
                    if (mNextpage == 0 && (mVisibleItemCount + mPastVisibleItemCount) >= mTotalItemCount) {
                        requestDatelinelist();
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRefreshLayout = (SwipeRefreshLayout) aq.id(R.id.view_refresh).getView();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNextpage = 0;
                requestDatelinelist();
                mRefreshLayout.setRefreshing(false);
            }
        });
        aq.dismiss();
    }

    protected void RegistDateRefreshCast()
    {
        mRefreshCast = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                mNextpage = 0;
                requestDatelinelist();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.Broadcast.REFRESH_DATA);
        getActivity().registerReceiver(mRefreshCast, filter);
    }

    public void requestDatelinelist() {
        mDataList = mPhotoWrapper.getDatelineList(mNextpage++);
        mAdapter.setDataSource(mDataList);
        mIsLoaded = true;

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

    OnItemClickListener mOnItemClickListener = new OnItemClickListener(){

        @Override
        public void onItemClick(View view, int position) {
            switch (view.getId()){
                case R.id.image_thumnail1: {
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 0) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail2:{
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 1) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail3: {
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 2) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                    break;
                case R.id.image_thumnail4:{
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 3) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail5: {
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 4) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail6: {
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 5) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail7:{
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 6) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail8: {
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 7) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.image_thumnail9: {
                    DatelineModel model = mAdapter.getItem(position);
                    if (model.getPhotoList().size() > 8) {
                        ImageviewActivity.startActivity(getContext(), model.getPhotoString());
                    }
                }
                break;
                case R.id.text_month:
                case R.id.text_day:{
                    DatelineModel model = mAdapter.getItem(position);
                    TimelineActivity.startActivity(getContext(), model.getDate(), model);
                }
                    break;
            }
        }
    };
}
