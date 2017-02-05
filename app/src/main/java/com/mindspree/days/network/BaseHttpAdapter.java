package com.mindspree.days.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.mindspree.days.AppApplication;
import com.mindspree.days.R;
import com.mindspree.days.lib.AppPreference;
import com.mindspree.days.view.LoadingDialog;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import cz.msebera.android.httpclient.Header;

public class BaseHttpAdapter {
	protected int mMethod;
	protected int mRequestCode;
	protected String mReqUrl;
	protected String mJsonString;
	protected int mHttpCode;
	protected RequestParams mHttpParams;
	
	protected Context mContext;
	
	private int  mRetryNumber = 3;
	private int  mTimeout = 1500;
	
	public static AsyncHttpClient mInstance;
	private static HttpAdapter uniqueInstance;

	public static synchronized HttpAdapter getInstance(Context context) {
		if(uniqueInstance == null) {
			uniqueInstance = new HttpAdapter(context);
		}
		
		return uniqueInstance;
	}
	
	public static synchronized AsyncHttpClient getClientInstance() {
		if(mInstance == null) {
			mInstance = new AsyncHttpClient();
			PersistentCookieStore myCookieStore = new PersistentCookieStore(AppApplication.getAppInstance());
			mInstance.setCookieStore(myCookieStore);
		}
		return mInstance;
	}

	public static void exchangeCookie() {
		mInstance = getClientInstance();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(AppApplication.getAppInstance());
		mInstance.setCookieStore(myCookieStore);
	}
	
	
	public BaseHttpAdapter(Context context) {
		mContext = context;
		initHttpClient();
	}
	
	private void initHttpClient(){
		mInstance = getClientInstance();
		
		
		mInstance.setTimeout(10000);
		mInstance.setMaxRetriesAndTimeout(mRetryNumber, mTimeout);
		PersistentCookieStore myCookieStore = new PersistentCookieStore(AppApplication.getAppInstance());
		mInstance.setCookieStore(myCookieStore);
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		mInstance.setThreadPool(threadPool);
		
		String userAgent = getMakeUserAgent();
		mInstance.setUserAgent(userAgent);	
	}
	
	private String getMakeUserAgent(){
		StringBuilder userAgent = new StringBuilder("Saessac");

		
		String densityStr = new String();
		float density = mContext.getResources().getDisplayMetrics().density;
		if( density == 0.75 ){
			densityStr = "ldpi";
		}
		else if( density == 1.0 ){
			densityStr = "mdpi";
		}
		else if( density == 1.5 ){
			densityStr = "hdpi";
		}
		else if( density == 2.0 ){
			densityStr = "xdpi";
		}
		else if( density == 3.0 ){
			densityStr = "xxdpi";
		}
		else if( density == 4.0 ){
			densityStr = "xxxdpi";
		}
		// Agent 설정
		userAgent.append( " ("+"ANDROID; " );
		userAgent.append("sdk/"+android.os.Build.VERSION.SDK_INT+"; ");
		userAgent.append("Scale/"+densityStr);
		userAgent.append(")");
		
		return userAgent.toString();
	}

	public void removeOAuthToken() {
		mInstance.removeHeader("Authorization");
	}

	public void addOAuthToken(  ) {
		removeOAuthToken();

		mInstance.addHeader("mb_token", AppPreference.getInstance().getMemberToken());
	}

	public AsyncHttpClient getClient(){
		return mInstance;
	}

	protected void executeGet(final Context context, final HttpRequestModel model)
	{
		String mb_token = AppPreference.getInstance().getMemberToken();
		if(!mb_token.equals("")) {
			mInstance.addHeader("mb_token", mb_token);
		}
		mInstance.get(model.mUrl, model.mParams, new AsyncHttpResponseHandler(){

			private LoadingDialog dialog;
			
			@Override
			public void onStart() {
				if(model.mIsProgressVisible)
				{
					if(dialog != null)
						dialog.dismiss();
					dialog = new LoadingDialog(context, R.style.TransparentDialog);
					dialog.show();
				}
				if(model.mHandler != null)
					model.mHandler.onHttpStart(model.mRequestCode);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				error.printStackTrace();
				//AppUtils.showNetWorkErrorDialog(context, booleanType.NO_FINISH);
				if(model.mHandler != null)
					model.mHandler.onHttpFailure(model.mRequestCode, statusCode, headers, responseBody, error);
			}
			
			@Override
			public void onFinish() 
			{
				try
				{
					if(model.mIsProgressVisible)
						dialog.dismiss();
					if(model.mHandler != null)
						model.mHandler.onHttpFinish(model.mRequestCode);
				}catch(Exception ex)
				{
					
				}
			}
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] response) 
			{
				if(model.mHandler != null)
					model.mHandler.onHttpSuccess(model.mRequestCode, code, headers, response, model.mTag);
			}
		});
	}
	
	protected void executePost(final Context context, final HttpRequestModel model)
	{
		String mb_token = AppPreference.getInstance().getMemberToken();
		if(!mb_token.equals("")) {
			mInstance.addHeader("mb_token", mb_token);
		}
		mInstance.post(model.mUrl, model.mParams, new AsyncHttpResponseHandler(){
			
			private LoadingDialog dialog;
			
			@Override
			public void onStart() 
			{
                if(model.mIsProgressVisible){
                    if(dialog != null)
                        dialog.dismiss();
                    dialog = new LoadingDialog(context, R.style.TransparentDialog);
                    dialog.show();
                }
				if(model.mHandler != null)
					model.mHandler.onHttpStart(model.mRequestCode);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody,Throwable error) {
				error.printStackTrace();
				if(model.mHandler != null)
					model.mHandler.onHttpFailure(model.mRequestCode, statusCode, headers, responseBody, error);
			}
			
			@Override
			public void onFinish() 
			{
				try
				{
					if(model.mIsProgressVisible)
                        dialog.dismiss();
					if(model.mHandler != null)
						model.mHandler.onHttpFinish(model.mRequestCode);
				}catch(Exception ex)
				{
					
				}
			}
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] response) 
			{
				if(model.mHandler != null)
					model.mHandler.onHttpSuccess(model.mRequestCode, code, headers, response, model.mTag);
			}
			
		});
	}
	
	protected void executePut(final Context context, final HttpRequestModel model)
	{
		String mb_token = AppPreference.getInstance().getMemberToken();
		if(!mb_token.equals("")) {
			mInstance.addHeader("mb_token", mb_token);
		}
		mInstance.put(model.mUrl, model.mParams, new AsyncHttpResponseHandler(){
	
			private LoadingDialog dialog;
					
			@Override
			public void onStart() {
				if(model.mIsProgressVisible){
					if(dialog != null)
						dialog.dismiss();
					dialog = new LoadingDialog(context, R.style.TransparentDialog);
					dialog.show();
				}
				if(model.mHandler != null)
					model.mHandler.onHttpStart(model.mRequestCode);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody,Throwable error) {
				error.printStackTrace();
				//AppUtils.showNetWorkErrorDialog(context, booleanType.NO_FINISH);
				if(model.mHandler != null)
					model.mHandler.onHttpFailure(model.mRequestCode, statusCode, headers, responseBody, error);
			}
			
			@Override
			public void onFinish() 
			{
				try
				{
					if(model.mIsProgressVisible)
						dialog.dismiss();
					if(model.mHandler != null)
						model.mHandler.onHttpFinish(model.mRequestCode);
				}catch(Exception ex)
				{
					
				}
			}
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] response) 
			{
				if(model.mHandler != null)
					model.mHandler.onHttpSuccess(model.mRequestCode, code, headers, response, model.mTag);
			}
			
		});
	}
	
	protected void executeDelete(final Context context, final HttpRequestModel model)
	{
		String mb_token = AppPreference.getInstance().getMemberToken();
		if(!mb_token.equals("")) {
			mInstance.addHeader("mb_token", mb_token);
		}
		String sUrl = model.mUrl;
		if(model.mParams != null)
			sUrl = model.mUrl + "?" + model.mParams.toString();
		mInstance.delete(sUrl, new AsyncHttpResponseHandler(){
			
			private LoadingDialog dialog;
			
			@Override
			public void onStart() {
				if(model.mIsProgressVisible){
					if(dialog != null)
						dialog.dismiss();
					dialog = new LoadingDialog(context, R.style.TransparentDialog);
					dialog.show();
				}
				if(model.mHandler != null)
					model.mHandler.onHttpStart(model.mRequestCode);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody,Throwable error) {
				error.printStackTrace();
				//AppUtils.showNetWorkErrorDialog(context, booleanType.NO_FINISH);
				if(model.mHandler != null)
					model.mHandler.onHttpFailure(model.mRequestCode, statusCode, headers, responseBody, error);
			}
			
			@Override
			public void onFinish() {
				try
				{
					if(model.mIsProgressVisible)
						dialog.dismiss();
					if(model.mHandler != null)
						model.mHandler.onHttpFinish(model.mRequestCode);
				}catch(Exception ex)
				{
					
				}
			}
			
			@Override
			public void onSuccess(int code, Header[] headers, byte[] response) 
			{
				if(model.mHandler != null)
					model.mHandler.onHttpSuccess(model.mRequestCode, code, headers, response, model.mTag);
			}
		});
	}
	
}
