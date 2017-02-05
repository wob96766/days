package com.mindspree.days.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.model.DatelineModel;


public class EditTextDialog extends Dialog {
    private DatelineModel mDateline;
    private Activity mActivity;

    private DBWrapper mDBWrapper;
    private AppPreference mPreference;

    private EditText mEditContent;
    private String mSentence;


    public EditTextDialog(Activity activity, DatelineModel dateline, String sentence) {
        super(activity , android.R.style.Theme_Translucent_NoTitleBar);
        mActivity = activity;
        mDateline = dateline;
        mSentence = sentence;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.dialog_edittext);

        initView();
    }

    private void close(){
        this.dismiss();
    }

    private void initView(){
        AQuery aq = new AQuery(getWindow().getDecorView());

        aq.id(R.id.btn_confirm).clicked(mOnClickListener);
        aq.id(R.id.btn_cancel).clicked(mOnClickListener);
        mEditContent = aq.id(R.id.edit_content).text(mSentence).getEditText();
        aq.dismiss();
    }



    View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_confirm:
                    mDBWrapper.setSentence(mDateline.getDate(), mEditContent.getText().toString());
                    close();
                    break;
                case R.id.btn_cancel:
                    close();
                    break;
            }
        }

    };


}
