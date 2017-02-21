package com.mindspree.days.model;

import com.mindspree.days.R;
import com.mindspree.days.lib.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vision51 on 2016. 12. 30..
 */

public class WeatherModel {

    public String mWeather = "";
    private double mTempMin = 0;
    private double mTempMax = 0;
    /*
    openweather api jsonformat example
    {"coord":{"lon":126.97,"lat":37.53},
     "weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"base":"stations",
     "main":{"temp":268.51,"pressure":1035,"humidity":57,"temp_min":297.838,"temp_max":271.15},
     "visibility":10000,
     "wind":{"speed":1,"deg":190},
     "clouds":{"all":1},
     "dt":1483062600,
     "sys":{"type":1,"id":8519,"message":0.0086,"country":"KR","sunrise":1483051589,"sunset":1483086183},"id":1837055,"name":"Yongsan","cod":200}
     */

    public static WeatherModel parseData(String jsonString)
    {

        WeatherModel model = new WeatherModel();
        try {
            JSONObject object = new JSONObject(jsonString);
            if(!object.isNull("weather")){
                JSONArray weatherObject = object.getJSONArray("weather");
                for(int i=0 ; i<weatherObject.length() ; i++){
                    JSONObject item = weatherObject.getJSONObject(i);
                    if(!item.isNull("main")){
                        model.mWeather = item.getString("main");
                    }
                }
            }
            if(!object.isNull("main")){
                JSONObject item = object.getJSONObject("main");
                if(!item.isNull("temp_min")){
                    model.mTempMin = item.getDouble("temp_min");
                }
                if(!item.isNull("temp_max")){
                    model.mTempMax = item.getDouble("temp_max");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public String getTemperatureMinimum(){
        return String.format("%.1f", mTempMin - 273.15);
    }

    public String getTemperatureMaximum(){
        return String.format("%.1f", mTempMax - 273.15);
    }

    public String getWeather(){
        if(mWeather.equals("Clear")){
            return AppUtils.getAppText(R.string.text_weather_clear);
        } else if(mWeather.equals("Clouds")){
            return AppUtils.getAppText(R.string.text_weather_clouds);
        } else if(mWeather.equals("Dust")){
            return AppUtils.getAppText(R.string.text_weather_dust);
        } else if(mWeather.equals("Haze")){
            return AppUtils.getAppText(R.string.text_weather_haze);
        } else if(mWeather.equals("Mist")){
            return AppUtils.getAppText(R.string.text_weather_mist);
        } else if(mWeather.equals("Fog")){
            return AppUtils.getAppText(R.string.text_weather_fog);
        } else if(mWeather.equals("Rain")){
            return AppUtils.getAppText(R.string.text_weather_rain);
        }
        return mWeather;
    }
}
