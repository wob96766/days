package com.mindspree.days.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;

    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mButtonLogin;
    private long backKeyPressedTime = 0;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mScreenName = getClass().toString();
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
        setContentView(R.layout.activity_login);

        initData();
        initView();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_up);
    }

    private void initData(){
        mAuth = FirebaseAuth.getInstance();
    }

    private void initView() {
        AQuery aq = new AQuery(this);

        mEditEmail = (EditText)aq.id(R.id.edit_email).getView();
        mEditPassword = (EditText)aq.id(R.id.edit_password).getView();
        mButtonLogin = aq.id(R.id.btn_login).clicked(mOnClickListener).getButton();
        aq.id(R.id.text_signup).clicked(mOnClickListener);
        aq.id(R.id.text_findpassword).clicked(mOnClickListener);

        aq.dismiss();
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

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showToast(getAppText(R.string.message_app_finish));
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
        }
        super.onBackPressed();
    }

    private boolean validateData() {
        String email = mEditEmail.getText().toString();

        if (email.equals("")) {
            showToast(getAppText(R.string.message_input_email));
            return false;
        }  else if(!AppUtils.isEmail(email)){
            showToast(getAppText(R.string.message_invalid_email));
            return false;
        } else if (mEditPassword.getText().toString().equals("")) {
            showToast(getAppText(R.string.message_input_password));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AppConfig.IntentCode.ACTION_SIGNUP) {
            if(mPreference.IsLogin()){
                String email = data.getStringExtra(AppConfig.IntentParam.EMAIL);
                String password = data.getStringExtra(AppConfig.IntentParam.PASSWORD);
                requestFirebaseLogin(email, password);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestFirebaseLogin(String email, String password) {
        showLoadingDialog();
        hideSoftKeyboard(mEditEmail);
        hideSoftKeyboard(mEditPassword);

        mAuth.signInWithEmailAndPassword(email, password)
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
                                showToast(getAppText(R.string.message_error_user_exists));
                            } catch(FirebaseAuthUserCollisionException e) {
                                showToast(getAppText(R.string.message_error_user_exists));
                            } catch(Exception e) {
                                if(task.getException().toString().contains("The password is invalid or the user does not have a password")) {
                                    showToast(getAppText(R.string.message_fail_login));
                                } else {
                                    showToast(task.getException().toString());
                                }
                            }
                            mEditPassword.setText("");
                        } else {

                        }
                    }
                });
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login:
                    if (validateData()) {
                        String email = mEditEmail.getText().toString();
                        String password= mEditPassword.getText().toString();
                        requestFirebaseLogin(email, password);
                    }
                    break;
                case R.id.text_signup:
                    SignupActivity.startActivityForResult(getActivity());
                    break;
                case R.id.text_findpassword:
                    FindPasswordActivity.startActivity(getActivity());
                    break;
            }
        }
    };

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                mPreference.setUserUid(user.getUid());
                mPreference.setUserEmail(user.getEmail());

                MainActivity.startActivity(getContext());
                finish();
            } else {
                // User is signed out

            }
        }
    };
}
