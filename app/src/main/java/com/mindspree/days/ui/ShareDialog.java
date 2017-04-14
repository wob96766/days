package com.mindspree.days.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.ArrayList;


public class ShareDialog extends Dialog {
    private DatelineModel mDateline;
    private Activity mActivity;

    private DBWrapper mDBWrapper;
    private AppPreference mPreference;

    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    public ShareDialog(Activity activity, String selectedDate, DatelineModel dateline) {
        super(activity , android.R.style.Theme_Translucent_NoTitleBar);
        mActivity = activity;
        mDateline = dateline;
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
                        for(String imageUrl : mDateline.getPhotoList()) {
                            if(count < 3 ){
                                if(imageUrl.contains("http") || imageUrl.contains("https")){
                                    SharePhoto sharePhoto = new SharePhoto.Builder()
                                            .setImageUrl(Uri.parse(imageUrl))
                                            .build();
                                    builder.addMedium(sharePhoto);
                                } else {
                                    SharePhoto sharePhoto = new SharePhoto.Builder()
                                            .setBitmap(BitmapFactory.decodeFile(imageUrl))
                                            .build();
                                    builder.addMedium(sharePhoto);
                                }

                            } else {
                                break;
                            }
                            count++;
                        }
                        builder.setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#"+mDateline.getSummarize("sentence").replace(" ",""))
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
                            if(mDateline.getPhotoList().size() > 0 ) {
                                for(String imageUrl : mDateline.getPhotoList()) {
                                    if(imageUrl.contains("http") || imageUrl.contains("https")){
                                        KakaoLink kakaoLink = KakaoLink.getKakaoLink(mActivity);

                                        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                                        kakaoTalkLinkMessageBuilder
                                                .addText(mDateline.getSummarize("sentence"))
                                                .addImage(imageUrl, 320, 280);

                                        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mActivity);
                                    } else {
                                        Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
                                        ExifInterface ei = new ExifInterface(imageUrl);
                                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                                        switch(orientation) {

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

                                        StorageReference profileRef = mStorageReference.child(String.format("profiles/%s/%s", mPreference.getUserUid(), imageUrl));
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                                        byte[] dataCompress = baos.toByteArray();
                                        final int width = bitmap.getWidth();
                                        final int height = bitmap.getHeight();
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
                                                            .addText(mDateline.getSummarize("sentence"))
                                                            .addImage(stringUrl, width, height);

                                                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mActivity);

                                                } catch (KakaoParameterException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                    break;
                                }
                            } else {
                                try {
                                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(mActivity);
                                    final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mActivity);
                                    kakaoTalkLinkMessageBuilder.addText(mDateline.getSummarize("sentence"));
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
                    if(mDateline.getPhotoList().size() > 0 ) {
                        String imageUrl = mDateline.getPhotoList().get(0);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppText(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, mDateline.getSummarize("sentence"));
                        share.putExtra(Intent.EXTRA_TITLE, AppUtils.getAppText(R.string.app_name));
                        if(imageUrl.contains("http") || imageUrl.contains("https")) {
                            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl));
                        } else {
                            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + imageUrl));
                        }
                    } else {
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppText(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, mDateline.getSummarize("sentence"));
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
