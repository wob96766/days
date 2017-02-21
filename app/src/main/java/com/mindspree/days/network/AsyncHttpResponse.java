package com.mindspree.days.network;


import cz.msebera.android.httpclient.Header;

public interface AsyncHttpResponse
{
	public void onHttpStart(int reqCode);
	public void onHttpFinish(int reqCode);
	public void onHttpSuccess(int reqCode, int code, Header[] headers, byte[] response, Object tag);
	public void onHttpFailure(int reqCode, int statusCode, Header[] headers, byte[] responseBody, Throwable error);

}
