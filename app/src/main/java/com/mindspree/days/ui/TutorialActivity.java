package com.mindspree.days.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.view.AnimatingProgressBar;

import java.util.HashMap;


public class TutorialActivity extends BaseActivity {
    private TextView mTextPrev;
    private TextView mTextNext;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int MAX_TAB_NUM = 4;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TutorialActivity.class);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        initData();
        initView();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private void initData() {
    }

    private void initView() {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        AQuery aq = new AQuery(this);
        mTextPrev = aq.id(R.id.text_prev).clicked(mOnClickListener).getTextView();
        mTextNext = aq.id(R.id.text_next).clicked(mOnClickListener).getTextView();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) aq.id(R.id.container).getView();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setActionButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(MAX_TAB_NUM);
        aq.dismiss();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
//                case R.id.text_prev:
//                    if(mViewPager.getCurrentItem() > 0) {
//                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
//                    }
//                    setActionButton();
//                    break;
                case R.id.text_next:
//                    if(mViewPager.getCurrentItem() < MAX_TAB_NUM-1) {
//                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() +1);
//                    } else{
                        finish();
//                    }
                    setActionButton();
                    break;
            }
        }
    };

    private void setActionButton() {
        int position = mViewPager.getCurrentItem();
        if(position > 0) {
            //mTextPrev.setVisibility(View.VISIBLE);
        }else
            mTextPrev.setVisibility(View.INVISIBLE);
        if(position <= MAX_TAB_NUM-1)
            mTextNext.setVisibility(View.VISIBLE);
        else
            mTextNext.setVisibility(View.INVISIBLE);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TutorialFragment.newInstance(getContext(), position, getAppText(R.string.message_tutotial_text_01));
                case 1:
                    return TutorialFragment.newInstance(getContext(), position, getAppText(R.string.message_tutotial_text_02));
                case 2:
                    return TutorialFragment.newInstance(getContext(), position, getAppText(R.string.message_tutotial_text_03));
                case 3:
                    return TutorialFragment.newInstance(getContext(), position, getAppText(R.string.message_tutotial_text_04));
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return MAX_TAB_NUM;
        }
    }



}
