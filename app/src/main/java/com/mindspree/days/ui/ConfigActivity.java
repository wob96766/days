package com.mindspree.days.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.mindspree.days.R;
import com.mindspree.days.data.DBWrapper;

public class ConfigActivity extends BaseActivity {

    String[] mDistanceList;
    String[] mDurationList;
    private DBWrapper mDBWrapper;
    private EditText mEditDistance;
    private EditText mEditDuration;
    private Button mButtonSave;
    private AlertDialog mDistanceDialog = null;
    private AlertDialog mDurationDialog = null;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, ConfigActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        setContentView(R.layout.activity_config);

        initData();
        initView();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }

    private void initData(){
        mDistanceList = getResources().getStringArray(R.array.array_diatance);
        mDurationList = getResources().getStringArray(R.array.array_duration);
        mDBWrapper = new DBWrapper(mPreference.getUserUid());
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
        mEditDistance = aq.id(R.id.edit_distance).text(String.format("%d", mPreference.getDistance())).clicked(mOnClickListener).getEditText();
        mEditDuration = aq.id(R.id.edit_duration).text(String.format("%d", mPreference.getDuration())).clicked(mOnClickListener).getEditText();
        mButtonSave = aq.id(R.id.btn_save).clicked(mOnClickListener).getButton();

        aq.dismiss();
    }

    public void showDistanceDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getAppText(R.string.hint_trace_distance));
        builder.setSingleChoiceItems(mDistanceList, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                    case 1:
                        mEditDistance.setText(mDistanceList[item]);
                        break;
                }
                mDistanceDialog.dismiss();
            }
        });
        mDistanceDialog = builder.create();
        mDistanceDialog.show();
    }

    public void showDurationDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getAppText(R.string.hint_trace_duration));
        builder.setSingleChoiceItems(mDurationList, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                    case 1:
                    case 2:
                        mEditDuration.setText(mDurationList[item]);
                        break;
                }
                mDurationDialog.dismiss();
            }
        });
        mDurationDialog = builder.create();
        mDurationDialog.show();
    }

    private boolean validateData() {
        String distanceString = mEditDistance.getText().toString();
        String durationString = mEditDuration.getText().toString();
        try{
            Integer.parseInt(distanceString);
        } catch(Exception e){
            showToast(getAppText(R.string.message_invalid_distance));
            return false;
        }
        try{
            Integer.parseInt(durationString);
        } catch(Exception e){
            showToast(getAppText(R.string.message_invalid_duration));
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

    private void requestConfigUpdate() {
        hideSoftKeyboard(mEditDistance);
        hideSoftKeyboard(mEditDuration);
        try{
            String distanceString = mEditDistance.getText().toString();
            mPreference.setDistance(Integer.parseInt(distanceString));
        } catch(Exception e){
            e.printStackTrace();
        }
        try{
            String durationString = mEditDuration.getText().toString();
            mPreference.setDuration(Integer.parseInt(durationString));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_save:
                    if (validateData()) {
                        requestConfigUpdate();
                        finish();
                    }
                    break;
                case R.id.edit_distance:
                    showDistanceDialog();
                    break;
                case R.id.edit_duration:
                    showDurationDialog();
                    break;
            }
        }
    };

}
