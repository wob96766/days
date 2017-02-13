package com.mindspree.days.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;

public class SignupActivity extends BaseActivity {
    EditText mEditEmail;
    EditText mEditPassword;
    EditText mEditRepassword;

    CheckBox mCheckTerms;
    CheckBox mCheckPrivacy;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public static void startActivityForResult(Activity context) {
        Intent i = new Intent(context, SignupActivity.class);
        context.startActivityForResult(i, AppConfig.IntentCode.ACTION_SIGNUP);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_signup);

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
        mEditPassword = aq.id(R.id.edit_password).getEditText();
        mEditRepassword = aq.id(R.id.edit_repassword).getEditText();

        mCheckTerms = aq.id(R.id.check_term).getCheckBox();
        mCheckPrivacy = aq.id(R.id.check_privacy).getCheckBox();

        aq.id(R.id.btn_signup).clicked(mOnClickListener);
        aq.id(R.id.text_terms).clicked(mOnClickListener);
        aq.id(R.id.text_privacy).clicked(mOnClickListener);

        aq.dismiss();
    }

    private boolean validateData() {
        String email = mEditEmail.getText().toString();

        if (email.equals("")) {
            showToast(getAppText(R.string.message_input_email));
            return false;
        }  else if(!AppUtils.isEmail(email)){
            showToast(getAppText(R.string.message_invalid_email));
            return false;
        }  else if (mEditPassword.getText().toString().equals("")) {
            showToast(getAppText(R.string.message_input_password));
            return false;
        } else if (mEditRepassword.getText().toString().equals("")) {
            showToast(getAppText(R.string.message_invalid_repassword));
            return false;
        } else if(!mCheckTerms.isChecked()){
            showToast(getAppText(R.string.message_invalid_terms));
            return false;
        } else if(!mCheckPrivacy.isChecked()){
            showToast(getAppText(R.string.message_invalid_privacy));
            return false;
        }

        return true;
    }


    private void finishActivity(){
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }
    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public  void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuth != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }


    private void requestFirebaseSignup() {
        hideSoftKeyboard(mEditEmail);
        showLoadingDialog();
        String email = mEditEmail.getText().toString();
        String password= mEditPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        dismissLoadingDialog();
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e) {
                                showToast(getAppText(R.string.message_error_weak_password));
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                showToast(getAppText(R.string.message_error_invalid_email));
                            }  catch(FirebaseAuthInvalidUserException e) {
                                showToast(getAppText(R.string.message_error_user_exist));
                            } catch(FirebaseAuthUserCollisionException e) {
                                showToast(getAppText(R.string.message_error_user_exist));
                            } catch(Exception e) {
                                showToast(task.getException().toString());
                            }
                        } else {

                        }
                    }
                });
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_signup:
                    if(validateData()) {
                        requestFirebaseSignup();
                    }
                    break;
                case R.id.text_terms: {
                    WebviewActivity.startActivity(getContext(), AppConfig.WebUrl.TERMS);
                }break;
                case R.id.text_privacy: {
                    WebviewActivity.startActivity(getContext(), AppConfig.WebUrl.POLICY);
                }break;

            }
        }
    };

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent intent = new Intent();
                intent.putExtra(AppConfig.IntentParam.EMAIL, user.getEmail());
                intent.putExtra(AppConfig.IntentParam.PASSWORD, mEditPassword.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                // User is signed out

            }
        }
    };

}
