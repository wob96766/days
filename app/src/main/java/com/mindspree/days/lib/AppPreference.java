package com.mindspree.days.lib;

import android.content.Context;
import android.content.SharedPreferences;

import com.mindspree.days.AppApplication;

import java.util.ArrayList;

public class AppPreference {

	private static AppPreference uniqueInstance;

	private final String TAG_APP					= "com.mindspree.days";
	private final String TAG_USER_UID	  			= "USER_UID";
	private final String TAG_USER_NAME	  			= "USER_NAME";
	private final String TAG_USER_EMAIL	  			= "USER_EMAIL";
	private final String TAG_USER_IMAGE	  			= "USER_IMAGE";
	private final String TAG_HAS_LAUNCH_HISTORY 	= "HAS_LAUNCH_HISTORY";
	private final String TAG_MEASURE_LATITUDE 		= "MEASURE_LATITUDE";
	private final String TAG_MEASURE_LONGITUDE  	= "MEASURE_LONGITUDE";
	private final String TAG_LATITUDE 				= "LATITUDE";
	private final String TAG_LONGITUDE  			= "LONGITUDE";
    private final String TAG_DISTANCE 				= "DISTANCE";
    private final String TAG_DURATION     			= "DURATION";
	private final String TAG_DATE    				= "DATE";
	private final String TAG_WEATHER_DATE    		= "WEATHER_DATE";
	private final String TAG_PIN    				= "PIN";
    private final String TAG_LOCKSCREEN    	        = "LOCKSCREEN";

	private final String TAG_MEASURE_TIME 			= "MEASURE_TIME";
	private final String TAG_LOGGING_OFF	 		= "LOGGING_OFF";
	private final String TAG_LOCK_OFF	 			= "LOCK_OFF";
	private final String TAG_PUSH_OFF	 			= "PUSH_OFF";

	private final String TAG_POI_TITLE1 		= "POI_TITLE1";
	private final String TAG_POI_LATITUDE1 		= "POI_LATITUDE1";
	private final String TAG_POI_LONGITUDE1  	= "POI_LONGITUDE1";

	private final String TAG_POI_TITLE2 		= "POI_TITLE2";
	private final String TAG_POI_LATITUDE2 		= "POI_LATITUDE2";
	private final String TAG_POI_LONGITUDE2  	= "POI_LONGITUDE2";

	public ArrayList<String> mHistorys = new ArrayList<String>();

	private final Context mApplicationContext = AppApplication.getAppInstance();

	public static synchronized AppPreference getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new AppPreference();
		}
		return uniqueInstance;
	}

	public AppPreference() {
	}

	private void putBoolean(String keyname, boolean value) {
		SharedPreferences config = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);

		SharedPreferences.Editor configEditor = config.edit();
		configEditor.putBoolean(keyname, value);

		configEditor.commit();
	}

	private void putInt(String keyname, int value) {
		SharedPreferences config = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);

		SharedPreferences.Editor configEditor = config.edit();
		configEditor.putInt(keyname, value);

		configEditor.commit();
	}

	private void putLong(String keyname, long value) {
		SharedPreferences config = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);

		SharedPreferences.Editor configEditor = config.edit();
		configEditor.putLong(keyname, value);

		configEditor.commit();
	}

	private void putString(String keyname, String value) {
		SharedPreferences config = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);

		SharedPreferences.Editor configEditor = config.edit();
		configEditor.putString(keyname, value);

		configEditor.commit();
	}

	private boolean getBoolean(String keyname, boolean value) {
		SharedPreferences prefs = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);
		return prefs.getBoolean(keyname, value);
	}

	private int getInt(String keyname, int value) {
		SharedPreferences prefs = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);
		return prefs.getInt(keyname, value);
	}

	private long getLong(String keyname, int value) {
		SharedPreferences prefs = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);
		return prefs.getLong(keyname, value);
	}

	private String getString(String keyname, String value) {
		SharedPreferences prefs = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);
		return prefs.getString(keyname, value);
	}

	public void clearData() {

		SharedPreferences config = mApplicationContext.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE);

		SharedPreferences.Editor configEditor = config.edit();
//		configEditor.remove(TAG_HAS_LAUNCH_HISTORY);
		configEditor.remove(TAG_MEASURE_LATITUDE);
		configEditor.remove(TAG_MEASURE_LONGITUDE);
        configEditor.remove(TAG_MEASURE_TIME);
		configEditor.remove(TAG_USER_UID);
        configEditor.remove(TAG_USER_NAME);
        configEditor.remove(TAG_USER_EMAIL);
        configEditor.remove(TAG_USER_IMAGE);
        configEditor.remove(TAG_LOGGING_OFF);
		configEditor.remove(TAG_LOCK_OFF);
		configEditor.remove(TAG_PUSH_OFF);
        configEditor.remove(TAG_DISTANCE);
        configEditor.remove(TAG_DURATION);
		configEditor.remove(TAG_DATE);
		configEditor.remove(TAG_WEATHER_DATE);
		configEditor.remove(TAG_PIN);
        configEditor.remove(TAG_LOCKSCREEN);

		configEditor.remove(TAG_POI_TITLE1);
		configEditor.remove(TAG_POI_LATITUDE1);
		configEditor.remove(TAG_POI_LONGITUDE1);
		configEditor.remove(TAG_POI_TITLE2);
		configEditor.remove(TAG_POI_LATITUDE2);
		configEditor.remove(TAG_POI_LONGITUDE2);

		configEditor.commit();
	}
	public void setUserUid(String value) {
		putString(TAG_USER_UID, value);
	}

	public String getUserUid() {
		return getString(TAG_USER_UID, "");
	}

	public boolean IsLogin() {
		return !getUserUid().equals("");
	}

    public void setUserEmail(String value) {
        putString(TAG_USER_EMAIL, value);
    }

    public String getUserEmail() {
        return getString(TAG_USER_EMAIL, "");
    }


    public void setDistance(int value) {
        putInt(TAG_DISTANCE, value);
    }

    public int getDistance() {
        return getInt(TAG_DISTANCE, 300);
    }

    public void setDuration(int value) {
        putInt(TAG_DURATION, value);
    }

    //public int getDuration() {
    //    return getInt(TAG_DURATION, 10);
    //}
	public int getDuration() {
		return getInt(TAG_DURATION, 4);
	}

    public void setLaunchHistory(String value) {
		putString(TAG_HAS_LAUNCH_HISTORY, value);
	}

	public String getLaunchHistory() {
		return getString(TAG_HAS_LAUNCH_HISTORY, "");
	}

	public double getLatitude(){
		return Double.valueOf(getString(TAG_LATITUDE, "0"));
	}

	public void setLatitude(double setValue){
		putString(TAG_LATITUDE, Double.toString(setValue));
	}

	public double getLongitude(){
		return Double.valueOf(getString(TAG_LONGITUDE, "0"));
	}

	public void setLongitude(double setValue){
		putString(TAG_LONGITUDE, String.valueOf(setValue));
	}

	public double getMeasureLatitude(){
		return Double.valueOf(getString(TAG_MEASURE_LATITUDE, "0"));
	}

	public void setMeasureLatitude(double setValue){
		putString(TAG_MEASURE_LATITUDE, Double.toString(setValue));
	}

	public double getMeasureLongitude(){
		return Double.valueOf(getString(TAG_MEASURE_LONGITUDE, "0"));
	}

	public void setMeasureLongitude(double setValue){
		putString(TAG_MEASURE_LONGITUDE, String.valueOf(setValue));
	}
////

	public void setPoiTitle1(String value) {
		putString(TAG_POI_TITLE1, value);
	}

	public String getPoiTitle1() {
		return getString(TAG_POI_TITLE1, "");
	}

	public double getPoiLatitude1(){
		return Double.valueOf(getString(TAG_POI_LATITUDE1, "0"));
	}

	public void setPoiLatitude1(double setValue){
		putString(TAG_POI_LATITUDE1, Double.toString(setValue));
	}

	public double getPoiLongitude1(){
		return Double.valueOf(getString(TAG_POI_LONGITUDE1, "0"));
	}

	public void setPoiLongitude1(double setValue){
		putString(TAG_POI_LONGITUDE1, String.valueOf(setValue));
	}

	public void setPoiTitle2(String value) {
		putString(TAG_POI_TITLE2, value);
	}

	public String getPoiTitle2() {
		return getString(TAG_POI_TITLE2, "");
	}

	public double getPoiLatitude2(){
		return Double.valueOf(getString(TAG_POI_LATITUDE2, "0"));
	}

	public void setPoiLatitude2(double setValue){
		putString(TAG_POI_LATITUDE2, Double.toString(setValue));
	}

	public double getPoiLongitude2(){
		return Double.valueOf(getString(TAG_POI_LONGITUDE2, "0"));
	}

	public void setPoiLongitude2(double setValue){
		putString(TAG_POI_LONGITUDE2, String.valueOf(setValue));
	}

	public void setDate(String setValue){
		putString(TAG_DATE, setValue);
	}

	public String getDate(){
		return getString(TAG_DATE, "");
	}

	public void setWeatherDate(String setValue){
		putString(TAG_WEATHER_DATE, setValue);
	}

	public String getWeatherDate(){
		return getString(TAG_WEATHER_DATE, "");
	}

	public void setLoggingOnOff(boolean setValue){
		putBoolean(TAG_LOGGING_OFF, setValue);
	}

	public boolean getLoggingOnOff(){
		return getBoolean(TAG_LOGGING_OFF, true);
	}

	public void setLockOnOff(boolean setValue){
		putBoolean(TAG_LOCK_OFF, setValue);
	}

	public boolean getLockOnOff(){
		return getBoolean(TAG_LOCK_OFF, false);
	}

	public void setPushOnOff(boolean setValue){
		putBoolean(TAG_PUSH_OFF, setValue);
	}

	public boolean getPushOnOff(){
		return getBoolean(TAG_PUSH_OFF, true);
	}

    public void setLockScreen(boolean setValue){
        putBoolean(TAG_LOCKSCREEN, setValue);
    }

    public boolean getLockScreen(){
        return getBoolean(TAG_LOCKSCREEN, false);
    }

	public void setPassword(String setValue){
		putString(TAG_PIN, setValue);
	}

	public String getPassword(){
		return getString(TAG_PIN, "");
	}

	public String getMemberToken() {
		return "test_token";
	}
}
