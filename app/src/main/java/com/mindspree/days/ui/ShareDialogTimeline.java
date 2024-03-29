package com.mindspree.days.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.lib.CircleTransform;
import com.mindspree.days.lib.RotateTransform;
import com.mindspree.days.model.DatelineModel;
import com.mindspree.days.model.TimelineModel;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.mindspree.days.lib.AppUtils.collage_gen;
import static com.mindspree.days.lib.AppUtils.collage_gen_Timeline;
import static java.lang.Math.round;


public class ShareDialogTimeline extends Dialog {
    private TimelineModel mTimeline;
    private Activity mActivity;

    private DBWrapper mDBWrapper;
    private AppPreference mPreference;

    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    public ShareDialogTimeline(Activity activity, String selectedDate, TimelineModel dateline) {
        super(activity , android.R.style.Theme_Translucent_NoTitleBar);
        mActivity = activity;
        mTimeline = dateline;
        mPreference = AppPreference.getInstance();
        mDBWrapper = new DBWrapper(mPreference.getUserUid());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(mActivity);
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.3f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_share);

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReferenceFromUrl(AppUtils.getAppText(R.string.firebase_bucket_name));

        initView();
    }

    private void close(){
        this.dismiss();

    }

    private void initView(){
        AQuery aq = new AQuery(getWindow().getDecorView());

        aq.id(R.id.btn_facebook).clicked(mOnClickListener);
        aq.id(R.id.btn_kakao).clicked(mOnClickListener);
        aq.id(R.id.btn_etc).clicked(mOnClickListener);

        aq.id(R.id.btn_cancel).clicked(mOnClickListener);

        aq.dismiss();
    }



    View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_facebook:{
                    if(AppUtils.isPackageInstalled(mActivity, "com.facebook.katana")){
                        ShareMediaContent.Builder builder = new ShareMediaContent.Builder();
                        int count = 0;
                        for(String imageUrl : mTimeline.getPhotoList()) {
                            if(count < 3 ){
                                if(imageUrl.contains("http") || imageUrl.contains("https")){
                                    SharePhoto sharePhoto = new SharePhoto.Builder()
                                            .setImageUrl(Uri.parse(imageUrl))
                                            .build();
                                    builder.addMedium(sharePhoto);
                                } else {
                                    try {
                                        Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
                                        ExifInterface ei = new ExifInterface(imageUrl);
                                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                                        switch (orientation) {

                                            case ExifInterface.ORIENTATION_ROTATE_90:
                                                bitmap = AppUtils.rotateBitmap(bitmap, 90);
                                                break;

                                            case ExifInterface.ORIENTATION_ROTATE_180:
                                                bitmap = AppUtils.rotateBitmap(bitmap, 180);
                                                break;

                                            case ExifInterface.ORIENTATION_ROTATE_270:
                                                bitmap = AppUtils.rotateBitmap(bitmap, 270);
                                                break;

                                            case ExifInterface.ORIENTATION_NORMAL:

                                            default:
                                                break;
                                        }
                                        SharePhoto sharePhoto = new SharePhoto.Builder()
                                                .setBitmap(bitmap)
                                                .build();
                                        builder.addMedium(sharePhoto);
                                    } catch(Exception e){
                                        e.printStackTrace();
                                        continue;
                                    }
                                }

                            } else {
                                break;
                            }
                            count++;
                        }
//                        builder.setShareHashtag(new ShareHashtag.Builder()
//                                .setHashtag("#"+mDateline.getSummarize("sentence").replace(" ",""))
//                                .build());

//                        builder.setShareHashtag(new ShareHashtag.Builder()
//                                .setHashtag(mDateline.getSummarize("ShortSentence").replace(" ",""))
//                                .build());




                        builder.setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#fgh#fgh")
                                .build());

                        ShareContent shareContent =  builder.build();
                        com.facebook.share.widget.ShareDialog shareDialog = new com.facebook.share.widget.ShareDialog(mActivity);
                        shareDialog.show(shareContent, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
                        close();
                    } else {
                        AppUtils.openPlayStore(mActivity, "com.facebook.katana");
                    }

                }
                break;
                case R.id.btn_kakao:{
                    if(AppUtils.isPackageInstalled(mActivity, "com.kakao.talk")){
                        try {
                            if(mTimeline.getPhotoList().size() > 0 ) {

                                // Create Collage //
                                int photo_cnt=0;
                                Bitmap bitmap_target =null;
                                bitmap_target=AppUtils.collage_gen_Timeline(mTimeline);

//                                //////////////////////////////////////////
//
                                String imageUrl = mTimeline.getPhotoList().get(0).toString();
                                StorageReference profileRef = mStorageReference.child(String.format("profiles/%s/%s", mPreference.getUserUid(), imageUrl));
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                bitmap_target.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                                        byte[] dataCompress = baos.toByteArray();
                                        final int width = bitmap_target.getWidth();
                                        final int height = bitmap_target.getHeight();
                                        UploadTask uploadTask = profileRef.putBytes(dataCompress);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception exception) {
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                String stringUrl = taskSnapshot.getDownloadUrl().toString();
                                                try {
                                                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(mActivity);

                                                    final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                                                    kakaoTalkLinkMessageBuilder
                                                            .addText(mTimeline.getSummarize(getContext()))
                                                            .addImage(stringUrl, width, height);

                                                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mActivity);

                                                } catch (KakaoParameterException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });




                            } else {
                                try {
                                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(mActivity);
                                    final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mActivity);
                                    kakaoTalkLinkMessageBuilder.addText(mTimeline.getSummarize(getContext()));
                                } catch (KakaoParameterException e) {
                                    e.printStackTrace();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        close();
                    } else {
                        AppUtils.openPlayStore(mActivity, "com.kakao.talk");
                    }

                }
                break;
                case R.id.btn_etc:{
                    Intent share = new Intent(Intent.ACTION_SEND);
                    int temp = mTimeline.getPhotoList().size();
                    if(mTimeline.getPhotoList().size() > 0 ) {


                        // Create Collage //
                        int photo_cnt=0;
                        Bitmap bitmap_target =null;
                        bitmap_target=AppUtils.collage_gen_Timeline(mTimeline);

                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/days_resample_images"); //

                        if(myDir.exists() && myDir.isDirectory()) {
                            // do nothing
                        }else{
                            myDir.mkdirs();
                        }

                        String fname = "Days_Moment_upload" + ".jpg";
                        File file = new File(myDir, fname);
                        String days_moment_resample_image=file.toString();

                        if (file.exists())
                            file.delete();

                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap_target.compress(Bitmap.CompressFormat.JPEG, 50, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String imageUrl = mTimeline.getPhotoList().get(0);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppText(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, mTimeline.getSummarize(getContext()));
                        share.putExtra(Intent.EXTRA_TITLE, AppUtils.getAppText(R.string.app_name));
                        if(imageUrl.contains("http") || imageUrl.contains("https")) {
                            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl));
                        } else {
                            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + days_moment_resample_image));
//                            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + imageUrl));
//                            share.putExtra(Intent.EXTRA_STREAM, days_moment_resample_image);
                        }


                    } else {
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppText(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, mTimeline.getSummarize(getContext()));
                        share.putExtra(Intent.EXTRA_TITLE, AppUtils.getAppText(R.string.app_name));
                    }


                    mActivity.startActivity(Intent.createChooser(share, AppUtils.getAppText(R.string.text_share)));



                    close();
                }
                break;
                case R.id.btn_cancel:

                    close();


                    break;
            }
        }

    };




}
