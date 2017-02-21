package com.mindspree.days.network;

import com.loopj.android.http.RequestParams;

public class HttpRequestModel {
	public String mUrl = "";
	public RequestParams mParams;
	public int mRequestCode = 0;
	public boolean mIsProgressVisible = false;
	public Object mTag;
	public AsyncHttpResponse mHandler;
	
	public HttpRequestModel(int requestCode, String url, RequestParams params, AsyncHttpResponse handler)
	{
		this.mRequestCode = requestCode;
		this.mUrl = url;
		this.mParams = params;
		this.mHandler = handler;
	}
	
	public HttpRequestModel(int requestCode, boolean isProgressVisible, String url, RequestParams params, AsyncHttpResponse handler)
	{
		this.mRequestCode = requestCode;
		this.mIsProgressVisible = isProgressVisible;
		this.mUrl = url;
		this.mParams = params;
		this.mHandler = handler;
	}

	public HttpRequestModel(int requestCode, boolean isProgressVisible, String url, RequestParams params, Object tag, AsyncHttpResponse handler)
	{
		this.mRequestCode = requestCode;
		this.mIsProgressVisible = isProgressVisible;
		this.mUrl = url;
		this.mParams = params;
		this.mTag = tag;
		this.mHandler = handler;
	}
	
}
