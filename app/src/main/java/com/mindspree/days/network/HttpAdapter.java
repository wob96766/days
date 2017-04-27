package com.mindspree.days.network;

import android.content.Context;
import com.loopj.android.http.RequestParams;
import com.mindspree.days.lib.AppConfig;


public class HttpAdapter extends BaseHttpAdapter{
	
	private static HttpAdapter uniqueInstance;
	
	public HttpAdapter(Context context) {
		super(context);
	}

	public void RequestWeather(final Context context, double latitude, double longitude, String ApiKey, AsyncHttpResponse handler) {
		RequestParams params = new RequestParams();
		params.put(AppConfig.HttpParam.latitude, latitude);
		params.put(AppConfig.HttpParam.longiude, longitude);
		params.put(AppConfig.HttpParam.appid, ApiKey);

		HttpRequestModel model = new HttpRequestModel(RequestCode.GET_WEATHER, false, AppConfig.RestUrl.OPENWEATHER, params, handler);
		executeGet(context, model);
	}

	public void RequestPOI(final Context context, double latitude, double longitude, String ApiKey, Object tag, AsyncHttpResponse handler) {
		RequestParams params = new RequestParams();
		params.put(AppConfig.HttpParam.location, String.format("%.7f,%7f",latitude, longitude));
		params.put(AppConfig.HttpParam.oauth_token, ApiKey);
		params.put(AppConfig.HttpParam.version, "20170105");

		HttpRequestModel model = new HttpRequestModel(RequestCode.GET_POI, false, AppConfig.RestUrl.FOURSQUARE, params, tag, handler);
		executeGet(context, model);
	}


	public void RequestAnalytics(final Context context, String id, String name, String content, AsyncHttpResponse handler) {
		RequestParams params = new RequestParams();
		params.put(AppConfig.HttpParam.id, id);
		params.put(AppConfig.HttpParam.name, name);
		params.put(AppConfig.HttpParam.content, content);

		HttpRequestModel model = new HttpRequestModel(RequestCode.POST_ANALYTICS, false, AppConfig.RestUrl.CP_ANALYTICS, params, handler);
		executePost(context, model);
	}
}
