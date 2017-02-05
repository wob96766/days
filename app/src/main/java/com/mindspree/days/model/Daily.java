package com.mindspree.days.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vision51 on 2016. 12. 30..
 */
@IgnoreExtraProperties
public class Daily {
    public int daily_index;
    public String mood;
    public String user_id;
    public String create_date;
    public String weather;

    public Daily(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("daily_index", daily_index);
        result.put("mood", mood);
        result.put("user_id", user_id);
        result.put("create_date", create_date);
        result.put("weather", weather);

        return result;
    }

    public String getDatetimeIndex()
    {
        String datetimeIndex = "";
        if(create_date != null){
            datetimeIndex = create_date.replace("-","").replace(":","").replace(" ", "").replace(".","");
        }
        return datetimeIndex;
    }
}
