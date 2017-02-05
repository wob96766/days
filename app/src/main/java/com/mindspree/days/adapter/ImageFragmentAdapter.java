package com.mindspree.days.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mindspree.days.ui.ImageFragment;

import java.util.ArrayList;

public class ImageFragmentAdapter extends FragmentPagerAdapter {
    protected ArrayList<String> mDataSource;
    private Context mContext;

    public ImageFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public ArrayList<String> getDataSource() {
        return mDataSource;
    }

    public void setDataSource(ArrayList<String> dataSource) {
        mDataSource = dataSource;
    }

    public void appendDataList(ArrayList<String> dataSource) {
        mDataSource.addAll(dataSource);
    }

    @Override
    public Fragment getItem(int position) {
        if (mDataSource != null) {
            return ImageFragment.newInstance(mContext, mDataSource.get(position % getCount()));
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

}