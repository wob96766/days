package com.mindspree.days.ui;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.mindspree.days.R;
import com.mindspree.days.lib.RotateTransform;

import java.lang.reflect.Field;

import uk.co.senab.photoview.PhotoViewAttacher;

public final class TutorialFragment extends BaseFragment {

    public Context mContext;
    private View mRootView;

    private int mIndex;
    private String mTextContent;
    private ImageView mImageView;


    public static TutorialFragment newInstance(Context context, int index, String text) {
        TutorialFragment fragment = new TutorialFragment();
        fragment.mContext = context;
        fragment.mIndex = index;
        fragment.mTextContent = text;
        return fragment;
    }

    @Override
    public void onDetach() {
        try {
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
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.card_tutorial, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initData() {
    }

    private void initView() {
        AQuery aq = new AQuery(mRootView);
        aq.id(R.id.text_title).text(mTextContent);

        if(mIndex == 0)
            aq.id(R.id.view_first).visible().text(mTextContent);
        else
            aq.id(R.id.view_first).invisible().text(mTextContent);
        if(mIndex == 1)
            aq.id(R.id.view_second).visible().text(mTextContent);
        else
            aq.id(R.id.view_second).invisible().text(mTextContent);
        if(mIndex == 2)
            aq.id(R.id.view_third).visible().text(mTextContent);
        else
            aq.id(R.id.view_third).invisible().text(mTextContent);
        if(mIndex == 3)
            aq.id(R.id.view_fourth).visible().text(mTextContent);
        else
            aq.id(R.id.view_fourth).invisible().text(mTextContent);
        aq.dismiss();
    }




}