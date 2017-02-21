package com.mindspree.days.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mindspree.days.R;

public class LockpinActivity extends BaseActivity {

    EditText mEditPassword;
    TextView mTextMessage;

    static public void startActivity(Context context){
        Intent i = new Intent(context, LockpinActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mScreenName = getClass().toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockpin);

        initData();
        initView();
    }

    @Override
    public void finish(){
        super.finish();
    }

    private void initData() {

    }

    private void initView() {
        AQuery aq = new AQuery(this);
        mTextMessage = aq.id(R.id.text_message).getTextView();
        mEditPassword = aq.id(R.id.edit_password).getEditText();
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 4) {
                    String p = mPreference.getPassword();
                    if (mPreference.getPassword().equals("")) {
                        mPreference.setPassword(s.toString());
                        mPreference.setLockOnOff(true);
                        mPreference.setLockScreen(false);
                        finish();
                    } else {
                        if(!mPreference.getPassword().equals(s.toString())){
                            mTextMessage.setText(getAppText(R.string.message_invalid_repassword));
                            mEditPassword.setText("");
                        }else {
                            mPreference.setLockScreen(false);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        aq.dismiss();
    }

    @Override
    public void onBackPressed() {

    }
}
