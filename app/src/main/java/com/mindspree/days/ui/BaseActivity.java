package com.mindspree.days.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.network.HttpAdapter;
import com.mindspree.days.view.LoadingDialog;
import com.mindspree.days.view.LoadingImageDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseActivity extends AppCompatActivity {
    /* mIntent
	 * type : Intent
	 * description : Activity에서 사용하는 Intent를 관리합니다.
	 */
    protected Intent mIntent;
    /* mHttpClient
	 * type : HttpClient
	 * description : 동기적으로 호출되는 HTTP Connection Client를 관리합니다.
	 */
    protected HttpAdapter mHttpClient;
    /* mAsyncClient
     * type : AsyncHttpClient
     * description : 비동기적으로 호출되는 HTTP Connection Client를 관리합니다.
     */
    protected AsyncHttpClient mAsyncClient;
    /* mPreference
     * type : AppPreference
     * description : 앱에서 사용되는 Preference를 관리합니다.
     */
    protected AppPreference mPreference = AppPreference.getInstance();
    protected String mScreenName = "";

    protected LoadingDialog mLoadingDialog;
    Tracker mBizTracker = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHttpClient = HttpAdapter.getInstance(getContext());
        mAsyncClient = mHttpClient.getClient();
        initTracker();
        if(mBizTracker != null && !AppConfig.IS_BETA && !mScreenName.equals("")){
            mBizTracker.setScreenName(mScreenName);
            mBizTracker.send(new HitBuilders.AppViewBuilder().build());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mBizTracker != null && !AppConfig.IS_BETA && !mScreenName.equals("")){
            mBizTracker.setScreenName(mScreenName);
            mBizTracker.send(new HitBuilders.ScreenViewBuilder().build());
            GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mBizTracker != null && !AppConfig.IS_BETA && !mScreenName.equals("")){
            GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
        }
    }

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    // 키보드 표시
    protected void showSoftkeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }

    // 키보드 숨김
    protected void hideSoftKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected String getAppText(int resourceId)
    {
        try
        {
            return getResources().getString(resourceId);
        }
        catch(Exception ex)
        {
            return "";
        }
    }

    protected String getAppText(int resourceId, String defaultText)
    {
        try
        {
            defaultText = getResources().getString(resourceId);
        }
        catch(Exception ex)
        {
            return "";
        }
        return defaultText;
    }

    protected ArrayList<String> getStringArray(int resourceId)
    {
        try
        {
            return  new ArrayList<String>(Arrays.asList(getResources().getStringArray(resourceId)));
        }
        catch(Exception ex)
        {
            return new ArrayList<String>();
        }
    }

    protected String getStringArrayItem(int resourceId, int position)
    {
        try
        {
            return Arrays.asList(getResources().getStringArray(resourceId)).get(position);
        }
        catch(Exception ex)
        {
            return "";
        }
    }
    public void SendScreenTraker(String screenName)
    {
        if(mBizTracker != null){
            // Set screen name.
            mBizTracker.setScreenName(screenName);
            // Send a screen view.
            mBizTracker.send(new HitBuilders.AppViewBuilder().build());
        }
    }

    protected void initTracker()
    {
        if( !AppConfig.IS_BETA ){
            //return ;
            mBizTracker = AppApplication.getAppInstance().getTracker(AppApplication.TrackerName.APP_TRACKER);
        }
    }

    protected void SendEventTraker(String categoryId, String actionId, String labelId, int value)
    {
        if(mBizTracker != null){
            mBizTracker.setScreenName(this.getClass().getSimpleName());
            mBizTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(categoryId)
                    .setAction(actionId)
                    .setLabel(labelId)
                    .build());
        }
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }else {
            mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog()
    {
        if(mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    // 알림 토스트
    protected void showToast(String content)
    {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

}
