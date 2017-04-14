package com.mindspree.days.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.adapter.TimelineAdapter;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.interfaces.OnItemClickListener;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.model.DatelineModel;
import com.mindspree.days.model.SentenceModel;
import com.mindspree.days.model.TimelineModel;

import java.util.ArrayList;

/**
 * Created by Admin on 19-10-2015.
 */
public class TimelineActivity extends BaseActivity {
    protected TimelineAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int mPastVisibleItemCount = 0;
    private DBWrapper mPhotoWrapper;
    private String mSelectedDate = "";

    private ArrayList<TimelineModel> mDataList;
    private TextView mTextSentence;
    private TextView mTextLocationcount;
    private TextView mTextPhotocount;
    private DatelineModel mDateline;

    public SentenceModel sentenceModel;


    public static void startActivity(Context context, String dateString, DatelineModel dateline)
    {
        Intent intent = new Intent(context, TimelineActivity.class);
        intent.putExtra(AppConfig.IntentParam.DATE, dateString);
        intent.putExtra(AppConfig.IntentParam.DATELINE, dateline);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_timeline);
        initData();
        initView();
        requestSentence();
        requestTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            ShareDialog dialog = new ShareDialog(this, mSelectedDate, mDateline);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    public void initData() {

        mPhotoWrapper = new DBWrapper(mPreference.getUserUid());
        mDateline = getIntent().getParcelableExtra(AppConfig.IntentParam.DATELINE);
        mSelectedDate = getIntent().getStringExtra(AppConfig.IntentParam.DATE);
    }

    public void initView() {
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

        aq.id(R.id.text_titlebar).text(mSelectedDate);

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
        mTextSentence = aq.id(R.id.text_sentence).getTextView();
        mTextPhotocount= aq.id(R.id.textPhotocount).getTextView();
        mTextLocationcount= aq.id(R.id.textLocationcount).getTextView();

        aq.id(R.id.text_edit).clicked(mOnClickListener);
        aq.dismiss();
    }

    public void requestTimeline() {
        mDataList = mPhotoWrapper.getTiemlineList(mSelectedDate);
        mAdapter.setDataSource(mDataList);

    }

    public void requestSentence() {
        SentenceModel sentence = mPhotoWrapper.getSentence(mSelectedDate);
        sentenceModel = sentence;
        mTextSentence.setText(sentence.getSummarize());
//        mTextPhotocount.setText(sentence.mPhotoCount);
//        mTextLocationcount.setText(sentence.mLocationCount);
    }

    public void finishView() {

    }


    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.text_edit:
                    EditTextDialog dialog = new EditTextDialog(getActivity(), mDateline, mTextSentence.getText().toString());
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestSentence();
                        }
                    });
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
                case R.id.image_location:
                    TimelineModel model = mAdapter.getItem(position);
                    SearchMapActivity.startActivity(getContext(), model.mLocationId, model.mLatitude, model.mLogitude);
                    break;
            }
        }
    };
}
