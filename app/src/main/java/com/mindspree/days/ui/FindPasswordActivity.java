package com.mindspree.days.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppUtils;

public class FindPasswordActivity extends BaseActivity {

    EditText mEditEmail;
    private FirebaseAuth mAuth;

    static public void startActivity(Context context){
        Intent i = new Intent(context, FindPasswordActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mScreenName = getClass().toString();
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_findpassword);

        initData();
        initView();
    }

    private void initData() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initView() {
        AQuery aq = new AQuery(this);

        Toolbar toolbar = (Toolbar) aq.id(R.id.toolbar).getView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        mEditEmail = aq.id(R.id.edit_email).getEditText();
        aq.id(R.id.btn_find).clicked(mOnClickListener);

        aq.dismiss();
    }

    private void finishActivity(){
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }
    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private boolean validateData(){
        String email = mEditEmail.getText().toString();

        if (email.equals("")) {
            showToast(getAppText(R.string.message_input_email));
            return false;
        }  else if(!AppUtils.isEmail(email)){
            showToast(getAppText(R.string.message_invalid_email));
            return false;
        }
        return true;
    }

    private void requestResetPassword() {
        hideSoftKeyboard(mEditEmail);
        showLoadingDialog();
        String email = mEditEmail.getText().toString();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissLoadingDialog();
                if (!task.isSuccessful()) {
                    showToast(task.getException().toString());
                } else {
                    showToast(getAppText(R.string.message_reset_password));
                    finish();
                }

            }
        });
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btn_find:
                    if(validateData()) {
                        requestResetPassword();
                    }
                    break;
            }
        }
    };


}
