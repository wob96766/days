package com.mindspree.days.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.AutoCompleteAdapter;
import com.mindspree.days.adapter.DatelineAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.interfaces.OnItemClickListener;
import com.mindspree.days.model.DatelineModel;
import com.mindspree.days.model.SearchModel;
import com.mindspree.days.model.TimelineModel;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

/**
 * Created by vision51 on 2016. 12. 27..
 */

public class SearchActivity extends BaseActivity{
    private EditText mEditSearchText;
    protected DatelineAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private DBWrapper mDBWrapper;

    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int mPastVisibleItemCount = 0;

    private ArrayList<DatelineModel> mDataList;

    private boolean mIsLoaded = false;
    private int mNextpage = 0;
    private String mSearchText = "";

    public static void startActivity(Context context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_search);
        initData();
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    private void initData() {
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
    }

    private void initView() {
        AQuery aq = new AQuery(this);

        Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        aq.id(R.id.image_deletetext).clicked(mOnClickListener);
        mEditSearchText = aq.id(R.id.edit_searchtext).getEditText();
        mEditSearchText.setFocusableInTouchMode(true);
        mEditSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mNextpage = 0;
                    mSearchText = mEditSearchText.getText().toString();
                    requestDatelinelist();
                    return true;
                }
                return false;
            }
        });

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
        aq.dismiss();
    }

    public void requestDatelinelist() {

        if(mSearchText.length() > 0) {
            mDataList = mDBWrapper.getDatelineList(mSearchText, mNextpage++);
            mAdapter.setDataSource(mDataList);
            mIsLoaded = true;
        } else {
            showToast(getAppText(R.string.message_searchtext_enter));
        }

    }


    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.image_deletetext:
                    mEditSearchText.setText("");
                    break;
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
                case R.id.text_month:
                case R.id.text_day:{
                    DatelineModel model = mAdapter.getItem(position);
                    TimelineModel model_timeline = new TimelineModel();
                    TimelineActivity.startActivity(getContext(), model.getDate(), model,model_timeline,"");
                }
                break;
            }
        }
    };
}
