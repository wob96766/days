package com.mindspree.days.lib;

public class AppConfig {

    // IS_TEST_APPLICATION == [true : test Server], [false : real Server]
    public static final boolean IS_TEST_APPLICATION		= true;
    // IS_BETA == [true : beta SERVICE], [false : open SERVICE]
    public static final boolean IS_BETA		            = false;

    public static final String REAL_SERVER_URL = "http://localhost/REST_API/";
    public static final String TEST_SERVER_URL = "http://127.0.0.1/REST_API/";

    public static final String SERVER_URL = IS_TEST_APPLICATION ? TEST_SERVER_URL : REAL_SERVER_URL;

    public static final String IMAGE_URL = SERVER_URL + "uploads/";
    public static final String APPLINK_URL = SERVER_URL + "app?";

    public static final String LOG_TAG = "MINDSPREE";

    public static final String OS_TYPE = "ANDROID";
    public static final int CAMERA_DONE_STEP = 2;

    public static final int LANGUAGE_MODE = 2; // 1: English, 2: Korean


    public static final class RestUrl {
        public static final String SAMPLE       			    = SERVER_URL+"v1/sample";

        public static final String OPENWEATHER                  = "http://api.openweathermap.org/data/2.5/weather";
        public static final String FOURSQUARE                  = "https://api.foursquare.com/v2/venues/search";

        public static final String CP_ANALYTICS                  = "http://www.codepleasure.net/api/sample/analytics";
    }

    public static final class WebUrl {
        public static final String TERMS                        = "http://mindspree.co/days/location_terms.html";
        public static final String POLICY                       = "http://mindspree.co/days/policy.html";
        public static final String LOCATIONTERMS                = "http://mindspree.co/days/location_terms.html";
    }

    public static final class Broadcast {
        public static final String REFRESH_DATA			    = "com.mindspree.days.action.REFRESH_DATA";
        public static final String REFRESH_CALENDAR			    = "com.mindspree.days.action.REFRESH_CALENDAR";
    }

    public static final class AppService {
        public static final String LOCATIONLOGGING			    = "com.mindspree.days.services.LocationLoggingService";
    }

    public static final class SchemeCode {
        public static final String TARGET					= "target";
        public static final String ID						= "id";
        public static final String CODE						= "code";
    }

    public static final class ActivityResult{
        public static final int ACCOUNT						= 10;
        public static final int WEBVIEW						= 11;
        public static final int IMAGEVIEW					= 12;
    }

    public static final class deviceModel {
        public static final String LG_F160K					= "LG-F160K";
        public static final String SHV_E210K				= "SHV-E210K";
        public static final String NEXUS_5					= "Nexus 5";
        public static final String NEXUS					= "Nexus";
    }


    public static final class cameraCode {
        public static final int GALLERY						= 9001;
        public static final int CAMERA						= 9002;
        public static final int CROP						= 9003;
    }

    public static final class HttpParam {
        public static final String page 				    = "page";
        public static final String prev 				    = "prev";
        public static final String next 				    = "next";

        public static final String error	 				= "error";
        public static final String code 					= "code";
        public static final String latitude 				= "lat";
        public static final String longiude 				= "lon";
        public static final String appid 					= "APPID";
        public static final String oauth_token 				= "oauth_token";
        public static final String location 				= "ll";
        public static final String version 				    = "v";

        public static final String id 				        = "id";
        public static final String name 				    = "name";
        public static final String content 				    = "content";
    }

    public static final class IntentCode {
        public static final int SAMPLE 				        = 1;
        public static final int ACTION_SIGNUP               = 101;
        public static final int ACTION_FINDPW 				= 102;
        public static final int ACTION_DATEPICK 			= 103;
        public static final int ACTION_MAPPICK1   			= 104;
        public static final int ACTION_MAPPICK2   			= 105;
    }

    public static final class IntentParam {
        public static final String TARGET 					= "TARGET";
        public static final String ID 					    = "ID";
        public static final String LATITUDE 				= "LATITUDE";
        public static final String MODE      		        = "MODE";
        public static final String LONGITUDE 		        = "LONGITUDE";
        public static final String TITLE 					= "TITLE";
        public static final String URL 					    = "URL";
        public static final String EMAIL 					= "EMAIL";
        public static final String PASSWORD 		        = "PASSWORD";
        public static final String IMAGE 					= "IMAGE";
        public static final String FILENAME 					= "FILENAME";
        public static final String TEXT 					= "TEXT";
        public static final String TIMELINE 				= "TIMELINE";
        public static final String DATELINE 				= "DATELINE";
        public static final String DATE 					= "DATE";

        public static final String TIMELINE_MODE 			= "TIMELINE_MODE"; //newrly added by mindspree
        public static final String START 					= "START";
        public static final String END 					    = "END";

    }

}
