package com.mindspree.days.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppUtils;
import com.mindspree.days.lib.Camera;
import com.mindspree.days.lib.CircleTransform;
import com.mindspree.days.model.DateTime;
import com.mindspree.days.model.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends BaseActivity {

    String[] mGenderList ;

    private Camera mCamera = new Camera(this, AppConfig.cameraCode.GALLERY, AppConfig.cameraCode.CAMERA, AppConfig.cameraCode.CROP);

    private FirebaseAuth mAuth;

    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    private DatabaseReference mDatabase;
    private DatabaseReference mProfileReference;

    private EditText mEditEmail;
    private EditText mEditName;
    private Button mButtonSave;
    private ImageView mImageProfile;
    private File mImageFile;
    private EditText mEditGender;
    private EditText mEditBirthday;
    private EditText mEditAddress1;
    private EditText mEditAddress2;
    private Profile mProfile =  new Profile();

    AlertDialog mGenderDialog = null;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_profile);

        initData();
        initView();
        RequestUserProfile();
    }


    private void RequestUserProfile() {
        showLoadingDialog();
        mProfileReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        dismissLoadingDialog();
                        if(profile != null){
                            mProfile = profile;
                            if(mProfile != null) {
                                mEditName.setText(mProfile.name);
                                mEditGender.setText(mProfile.gender);
                                mEditBirthday.setText(mProfile.birthday);
                                mEditAddress1.setText(mProfile.address1);
                                mEditAddress2.setText(mProfile.address2);
                                if (mProfile.imageurl != null && !mProfile.imageurl.equals("")) {
                                    Glide.with(getContext()).load(mProfile.imageurl).bitmapTransform(new CircleTransform(getContext())).into(mImageProfile);
                                } else {
                                    Glide.with(getContext()).load(R.mipmap.ic_person).bitmapTransform(new CircleTransform(getContext())).into(mImageProfile);
                                }
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showToast(getAppText(R.string.message_network_error));
                    }
                });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == AppConfig.cameraCode.GALLERY ||
                requestCode == AppConfig.cameraCode.CAMERA ||
                requestCode == AppConfig.cameraCode.CROP){
            mCamera.cameraActivityResult(requestCode, resultCode, data);

            if( mCamera.getCurrentCameraStep() == AppConfig.CAMERA_DONE_STEP ){
                mImageFile = mCamera.getSelectedImageFile();
                Bitmap bitmap = BitmapFactory.decodeFile(mImageFile.getAbsolutePath());

                StorageReference profileRef = mStorageReference.child(String.format("profiles/%s/%s", mPreference.getUserUid(), mImageFile.getName()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                byte[] dataCompress = baos.toByteArray();

//                //  Save Profile file begin
//
//                String root = Environment.getExternalStorageDirectory().toString();
//                File myDir = new File(root + "/days_resample_images_profile"); //
//
//                if(myDir.exists() && myDir.isDirectory()) {
//                    // do nothing
//                }else{
//                    myDir.mkdirs();
//                }
//
//                String fname = "Days_profile" + ".days";
//                File file = new File(myDir, fname);
//                String days_profile_image=file.toString();
//
//                if (file.exists())
//                    file.delete();
//
//                try {
//                    FileOutputStream out = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
//                    out.flush();
//                    out.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                //  Save Profile file end

                UploadTask uploadTask = profileRef.putBytes(dataCompress);
                showLoadingDialog();
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        dismissLoadingDialog();
                        showToast(getAppText(R.string.message_network_error));
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dismissLoadingDialog();
                        mProfile.imageurl = taskSnapshot.getDownloadUrl().toString();
                        Glide.with(ProfileActivity.this).load(mImageFile).bitmapTransform(new CircleTransform(getContext())).into(mImageProfile);
                    }
                });
            }
        } else if(requestCode == AppConfig.IntentCode.ACTION_MAPPICK1) {

            // Only for debugging
            mProfile.address1 = "My house";
            mProfile.latitude1 = 37.350086;
            mProfile.longitude1 = -121.991964;
            mEditAddress1.setText("My house");


            if(resultCode==1) {
                if(data != null) {
                    String title = data.getStringExtra(AppConfig.IntentParam.TITLE);
                    double latitude = data.getDoubleExtra(AppConfig.IntentParam.LATITUDE, 0);
                    double longitude = data.getDoubleExtra(AppConfig.IntentParam.LONGITUDE, 0);
                    if(latitude != 0 && longitude != 0){
                        mProfile.address1 = title;
                        mProfile.latitude1 = latitude;
                        mProfile.longitude1 = longitude;
                        mEditAddress1.setText(title);
                    }
                }
            }


        } else if(requestCode == AppConfig.IntentCode.ACTION_MAPPICK2) {
            if(resultCode==1) {
                if(data != null) {
                    String title = data.getStringExtra(AppConfig.IntentParam.TITLE);
                    double latitude = data.getDoubleExtra(AppConfig.IntentParam.LATITUDE, 0);
                    double longitude = data.getDoubleExtra(AppConfig.IntentParam.LONGITUDE, 0);
                    if(latitude != 0 && longitude != 0){
                        mProfile.address2 = title;
                        mProfile.latitude2 = latitude;
                        mProfile.longitude2 = longitude;
                        mEditAddress2.setText(title);
                    }
                }
            }
        }
    }

    private void initData(){
        mGenderList = getResources().getStringArray(R.array.array_gender);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProfileReference = mDatabase.child("users").child(mPreference.getUserUid()).child("profile");
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReferenceFromUrl(getAppText(R.string.firebase_bucket_name));
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

        mEditEmail = aq.id(R.id.edit_email).text(mPreference.getUserEmail()).getEditText();
        mEditName = aq.id(R.id.edit_name).getEditText();
        mEditGender = aq.id(R.id.edit_gender).clicked(mOnClickListener).getEditText();
        mEditBirthday = aq.id(R.id.edit_birthday).clicked(mOnClickListener).getEditText();
        mEditAddress1 = aq.id(R.id.edit_address1).clicked(mOnClickListener).getEditText();
        mEditAddress2 = aq.id(R.id.edit_address2).clicked(mOnClickListener).getEditText();
        mImageProfile = aq.id(R.id.image_photo).clicked(mOnClickListener).getImageView();
        mButtonSave = aq.id(R.id.btn_save).clicked(mOnClickListener).getButton();

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
        }
        return true;
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void requestProfileUpdate() {
        hideSoftKeyboard(mEditEmail);
        hideSoftKeyboard(mEditName);
        showLoadingDialog();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            mDatabase.child("users").child(mPreference.getUserUid()).child("profile").setValue(mProfile);
            showToast(getAppText(R.string.message_complete_save));
            dismissLoadingDialog();
            finish();
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

    private void showDateDialog(final View textView, final DateTime datetime){

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTime newDateTime = new DateTime();
                DateTime todayTime = new DateTime(new Date());

                newDateTime.year = view.getYear();
                newDateTime.month = view.getMonth()+1;
                newDateTime.day = view.getDayOfMonth();

                newDateTime.hour = datetime.hour;
                newDateTime.minute = datetime.minute;
                newDateTime.second = datetime.second;

                if(textView.getTag() != null && textView.getTag() instanceof String){
                    if(textView instanceof EditText)
                        ((EditText)textView).setText( newDateTime.getAppDateFormat());
                    textView.setTag( newDateTime.getServerDateFormat());
                } else {
                    if(textView instanceof EditText)
                        ((EditText)textView).setText( newDateTime.getAppDateFormat());
                    textView.setTag( newDateTime.getServerDateFormat());
                }
            }

        }, datetime.year, datetime.month-1, datetime.day );

        dialog.show();
    }

    public void showGenderDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getAppText(R.string.hint_profile_gender));
        builder.setSingleChoiceItems(mGenderList, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                    case 1:
                        mEditGender.setText(mGenderList[item]);
                        break;
                }
                mGenderDialog.dismiss();
            }
        });
        mGenderDialog = builder.create();
        mGenderDialog.show();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_photo:
                    showCameraDialog( AppConfig.cameraCode.GALLERY );
                    break;
                case R.id.edit_birthday:
                    if(view.getTag() == null ) {
                        showDateDialog(view, new DateTime("1999-01-01"));
                    } else {
                        showDateDialog(view, new DateTime(view.getTag().toString()));
                    }
                    break;
                case R.id.edit_gender:
                    showGenderDialog();
                    break;
                case R.id.edit_address1:
                    SearchMapActivity.startActivityForResult(getActivity(), 1, mPreference.getLatitude(), mPreference.getLongitude());
                    break;
                case R.id.edit_address2:
                    SearchMapActivity.startActivityForResult(getActivity(), 2, mPreference.getLatitude(), mPreference.getLongitude());
                    break;
                case R.id.btn_save:
                    if (validateData()) {
                        mProfile.name = mEditName.getText().toString();
                        mProfile.gender = mEditGender.getText().toString();
                        mProfile.birthday = mEditBirthday.getTag()== null ? "" : mEditBirthday.getTag().toString();
                        mProfile.address1 = mEditAddress1.getText().toString();
                        mProfile.address2 = mEditAddress2.getText().toString();
                        if(mProfile.address1 != null && !mProfile.address1.equals("")){
                            mPreference.setPoiTitle1(mProfile.address1);
                            mPreference.setPoiLatitude1(mProfile.latitude1);
                            mPreference.setPoiLongitude1(mProfile.longitude1);
                        }
                        if(mProfile.address2 != null && !mProfile.address2.equals("")){
                            mPreference.setPoiTitle2(mProfile.address2);
                            mPreference.setPoiLatitude2(mProfile.latitude2);
                            mPreference.setPoiLongitude2(mProfile.longitude2);
                        }
                        requestProfileUpdate();
                    }
                    break;
            }
        }
    };

}
