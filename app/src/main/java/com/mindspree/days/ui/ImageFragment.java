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

public final class ImageFragment extends BaseFragment {

    public Context mContext;

    private String mImageUrl;
    private ImageView mImageView;
    private View mRootView;
    PhotoViewAttacher mAttacher;


    public static ImageFragment newInstance(Context context, String url) {
        ImageFragment fragment = new ImageFragment();
        fragment.mContext = context;
        fragment.mImageUrl = url;
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
            mRootView = (ViewGroup) inflater.inflate(R.layout.card_image, container, false);
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
        //mAttacher
        mImageView = aq.id(R.id.image_title).getImageView();
        if (mImageUrl != null) {

            try {
                ExifInterface ei = new ExifInterface(mImageUrl);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        Glide.with(mContext).load(mImageUrl).asBitmap().transform(new RotateTransform(mContext, 90)).into(mImageView);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        Glide.with(mContext).load(mImageUrl).transform(new RotateTransform(mContext, 180)).into(mImageView);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        Glide.with(mContext).load(mImageUrl).asBitmap().transform(new RotateTransform(mContext, 270)).into(mImageView);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:

                    default:
                        break;
                }
                Glide.with(mContext).load(mImageUrl).asBitmap().into(mImageView);
                mAttacher = new PhotoViewAttacher(mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
           // mAttacher.update();
        }
        aq.dismiss();
    }




}