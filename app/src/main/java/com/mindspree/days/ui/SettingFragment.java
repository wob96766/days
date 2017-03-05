package com.mindspree.days.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.CircleTransform;
import com.mindspree.days.model.Profile;

import java.lang.reflect.Field;

public class SettingFragment extends BaseFragment{

    private ImageView mImageUser;
    private TextView mTextName;

    private SwitchCompat mSwitchPassword;
    private SwitchCompat mSwitchPush;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabase;
    private DatabaseReference mProfileReference;
    private Dialog mDialog;
    private String[] mMoodList;

    @Override
    public void onDetach() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onDetach();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null)
        {
            mRootView = (ViewGroup)inflater.inflate(R.layout.fragment_setting, container, false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
        {
            parent.removeView(mRootView);
        }
        return mRootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1) {
            switch (requestCode) {
                case AppConfig.IntentCode.ACTION_DATEPICK: {
                    if(data != null) {
                        String start = data.getStringExtra(AppConfig.IntentParam.START);
                        String end = data.getStringExtra(AppConfig.IntentParam.END);
                        DataBackupActivity.startActivity(getContext(), start, end);
                    }
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        AQuery aq = new AQuery(mRootView);

        mTextName = aq.id(R.id.text_name).getTextView();
        aq.id(R.id.text_email).text(mPreference.getUserEmail());
        mImageUser = aq.id(R.id.image_photo).getImageView();

        aq.dismiss();
    }
    public void initData() {
        mMoodList = getResources().getStringArray(R.array.array_data);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProfileReference = mDatabase.child("users").child(mPreference.getUserUid()).child("profile");
        //mProfileReference.addChildEventListener(mChildEventListener);
        mProfileReference.addValueEventListener(mValueEventListener);
    }
    public void initView() {
        AQuery aq = new AQuery(mRootView);

        mTextName = aq.id(R.id.text_name).getTextView();
        mImageUser = aq.id(R.id.image_photo).getImageView();

        mImageUser = aq.id(R.id.image_photo).clicked(mOnClickListener).getImageView();
        mSwitchPassword = (SwitchCompat) aq.id(R.id.switch_password).getView();
        mSwitchPassword.setChecked(mPreference.getLockOnOff());
        mSwitchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    LockpinActivity.startActivity(getContext());
                } else {
                    mPreference.setLockOnOff(b);
                    mPreference.setPassword("");
                }

            }
        });
        mSwitchPush = (SwitchCompat) aq.id(R.id.switch_push).getView();
        mSwitchPush.setChecked(mPreference.getPushOnOff());
        mSwitchPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPreference.setPushOnOff(b);
            }
        });
        aq.id(R.id.view_data).clicked(mOnClickListener);
        aq.id(R.id.view_mode).clicked(mOnClickListener);
        aq.id(R.id.view_connect).clicked(mOnClickListener);
        aq.id(R.id.view_trace).clicked(mOnClickListener);
        aq.id(R.id.view_tutorial).clicked(mOnClickListener);
        aq.id(R.id.view_terms).clicked(mOnClickListener);
        aq.id(R.id.view_policy).clicked(mOnClickListener);
        aq.id(R.id.view_logout).clicked(mOnClickListener);
        mProfileReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        refreshData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        aq.dismiss();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }
    public void showDataDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getAppText(R.string.message_data_backup_restore));
        builder.setSingleChoiceItems(mMoodList, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        DatePickActivity.startActivityForResult(SettingFragment.this, AppConfig.IntentCode.ACTION_DATEPICK);
                        break;
                    case 1:
                        DataRestoreActivity.startActivity(getContext());
                        break;
                }
                mDialog.dismiss();
            }
        });
        mDialog = builder.create();
        mDialog.show();

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.image_photo:
                    ProfileActivity.startActivity(getContext());
                    break;
                case R.id.view_trace:
                    ConfigActivity.startActivity(getContext());
                    break;
                case R.id.view_terms:
                    WebviewActivity.startActivity(getContext(), AppConfig.WebUrl.TERMS);
                    break;
                case R.id.view_policy:
                    WebviewActivity.startActivity(getContext(), AppConfig.WebUrl.POLICY);
                    break;
                case R.id.view_tutorial:
                    TutorialActivity.startActivity(getContext());
                    break;
                case R.id.view_data:
                    showDataDialog();
                    break;
                case R.id.view_logout:
                    mPreference.clearData();
                    FirebaseAuth.getInstance().signOut();
                    break;
            }
        }
    };

    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            refreshData(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            //showToast(getAppText(R.string.message_network_error));
        }
    };

    private void refreshData(DataSnapshot dataSnapshot) {
        Profile profile = dataSnapshot.getValue(Profile.class);
        if(profile != null) {
            try {
                mTextName.setText(profile.name);
                if (profile.imageurl != null && !profile.imageurl.equals("")) {
                    Glide.with(getContext()).load(profile.imageurl).bitmapTransform(new CircleTransform(getContext())).into(mImageUser);
                } else {
                    Glide.with(getContext()).load(R.mipmap.ic_person).bitmapTransform(new CircleTransform(getContext())).into(mImageUser);
                }
                // junyong - Clear Location data when there is no location information on the Server.
                if(profile.address1 != null && !profile.address1.equals("")){
                    mPreference.setPoiTitle1(profile.address1);
                    mPreference.setPoiLatitude1(profile.latitude1);
                    mPreference.setPoiLongitude1(profile.longitude1);
                } else {
                    mPreference.setPoiTitle1("");
                    mPreference.setPoiLatitude1(0);
                    mPreference.setPoiLongitude1(0);
                }
                if(profile.address2 != null && !profile.address2.equals("")){
                    mPreference.setPoiTitle2(profile.address2);
                    mPreference.setPoiLatitude2(profile.latitude2);
                    mPreference.setPoiLongitude2(profile.longitude2);
                } else{mPreference.setPoiTitle2("");
                    mPreference.setPoiLatitude2(0);
                    mPreference.setPoiLongitude2(0);

                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
