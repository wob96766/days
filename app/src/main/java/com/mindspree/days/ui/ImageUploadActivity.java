package com.mindspree.days.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.Camera;
import com.mindspree.days.lib.GridViewAdapter;
import com.mindspree.days.lib.ImageItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sjkin on 2017-07-09.
 */

public class ImageUploadActivity extends BaseActivity {

    private Camera mCamera = new Camera(this, AppConfig.cameraCode.GALLERY, AppConfig.cameraCode.CAMERA, AppConfig.cameraCode.CROP);

    private TextView mTitle;
    private TextView mTextClose;
    private TextView mTextUpload;

    private ImageView mImageResult;
    private TextView  mTextResult;

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;

    private ImageItem mItemEditing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
        setContentView(R.layout.activity_fileupload);

        mTitle = (TextView)findViewById(R.id.toolbar_logo);
        mTextClose = (TextView)findViewById(R.id.toolbar_close);
        mTextUpload = (TextView)findViewById(R.id.toolbar_upload);
        mImageResult = (ImageView)findViewById(R.id.image_result);
        mTextResult = (TextView)findViewById(R.id.text_result);

        mTitle.setText("사진 업로드");
        mTitle.setTypeface(MainActivity.mBoldTypeface);
        mTextClose.setText("닫기");
        mTextClose.setTypeface(MainActivity.mBoldTypeface);
        mTextUpload.setText("업로드");
        mTextUpload.setTypeface(MainActivity.mBoldTypeface);

        mTextClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTextUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kei","업로드");
            }
        });

        mGridView = (GridView) findViewById(R.id.gridView);
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                mItemEditing = item;
                if (item.getEditItem()) {
                    showCameraDialog(AppConfig.cameraCode.GALLERY);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == AppConfig.cameraCode.GALLERY ||
                requestCode == AppConfig.cameraCode.CAMERA ||
                requestCode == AppConfig.cameraCode.CROP) {

            mCamera.cameraActivityResult(requestCode, resultCode, data);
            File imageFile = mCamera.getSelectedImageFile();
            if (imageFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                mItemEditing.setImage(bitmap);
                mItemEditing.setEditItem(false);
                mGridAdapter.notifyDataSetChanged();
                this.updateResultImage(bitmap);
                this.updateResultText(imageFile.getAbsolutePath());
            }
        }
    }

    private void showCameraDialog( int code){
        if(mCamera.checkCameraPermission()){
            mCamera.initCamera();
            mCamera.setCropOption(1, 1);

            switch (code) {
                case AppConfig.cameraCode.CAMERA:{
                    mCamera.showCamera();
                } break;

                case AppConfig.cameraCode.GALLERY:{
                    mCamera.showGallery();
                } break;

                default:
                    break;
            }
        }
    }

    private void updateResultImage(Bitmap bitmap) {
        mImageResult.setImageBitmap(bitmap);
    }

    private void updateResultText(String text) {
        mTextResult.setText(text);
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_photo);
            imageItems.add(new ImageItem(bitmap,null,true));
        }
        return imageItems;
    }
}
