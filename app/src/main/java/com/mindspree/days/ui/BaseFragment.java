/*
 * Copyright (C) 2015 Likealike, GreenlightPartners
 * Class Name : BaseActivity 
 * Description 
 * : 비즈니스에서 사용되는 Activity들의 상위클래스
 * : Strategy Pattern 적용
 */
package com.mindspree.days.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tagmanager.DataLayer;
import com.loopj.android.http.AsyncHttpClient;
import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppConfig;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.network.HttpAdapter;
import com.mindspree.days.view.LoadingDialog;

import java.util.ArrayList;
import java.util.Arrays;


public class BaseFragment extends Fragment 
{
	/* mContext
	 * type : Context
	 * description : Fragment에서 사용하는 Context를 관리합니다.
	 */
	private Context mContext;
	/* mContext
	 * type : Context
	 * description : Fragment에서 사용하는 Root View 관리합니다.
	 */
	protected ViewGroup mRootView;
	/* mIntent
	 * type : Intent
	 * description : Fragment에서 사용하는 Intent를 관리합니다.
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
	/* getCotnext
	 * params : 
	 * result : void
	 * description : Fragment를 사용하는 Context 객체를 가지고 옵니다.
	 */
	protected LoadingDialog mLoadingDialog;
	Tracker mBizTracker = null;
    protected String mScreenName = "";

	public Context getContext()
	{
		if(mContext == null)
			mContext = getActivity();
		return 	mContext;
	}

	public Fragment getFragment()
	{
		return 	this;
	}
	/* getBaseIntent
	 * params : 
	 * result : void
	 * description : Fragment가 실생되고 있는 Intent를 가지고 옵니다.
	 */
	protected Intent getIntent()
	{
		if(mIntent == null)
			mIntent = getActivity().getIntent();
		return 	mIntent;
	}

	protected DisplayMetrics getDisplayInfo()
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    return displaymetrics;
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

	// 키보드 표시
	protected void showSoftkeyboard(View view){
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService( Context.INPUT_METHOD_SERVICE );

		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		
	}
	// 키보드 숨김
	protected void hideSoftKeyboard(View view){

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService( Context.INPUT_METHOD_SERVICE );

		imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
	}
	protected void initTracker()
	{
		if( !AppConfig.IS_BETA ){
			mBizTracker = AppApplication.getAppInstance().getTracker(AppApplication.TrackerName.APP_TRACKER);
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
	public void onStop() {
		super.onStop();
		if(mBizTracker != null && !AppConfig.IS_BETA && !mScreenName.equals("")){
			GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
		}
	}

    protected void showLoadingDialog() {
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }else {
            mLoadingDialog = new LoadingDialog(getContext(), R.style.TransparentDialog);
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
