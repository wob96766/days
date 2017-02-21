package com.mindspree.days.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

/**
 * Created by vision51 on 2017. 1. 2..
 */

public  class RotateTransform extends BitmapTransformation {
    private int mOrientation = 0;

    public RotateTransform(Context context, int orientation) {
        super(context);
        mOrientation = orientation;
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int exifOrientationDegrees = getExifOrientationDegrees(mOrientation);
        return TransformationUtils.rotateImageExif(toTransform, pool, exifOrientationDegrees);
    }

    private int getExifOrientationDegrees(int orientation) {
        int exifInt;
        switch (orientation) {
            case 90:
                exifInt = ExifInterface.ORIENTATION_ROTATE_90;
                break;
            default:
                exifInt = ExifInterface.ORIENTATION_NORMAL;
                break;
        }
        return exifInt;
    }

    @Override public String getId() {
        return getClass().getName();
    }
}