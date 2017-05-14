package com.mindspree.days.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vision51 on 2016. 12. 30..
 */
@IgnoreExtraProperties
public class Location {
    public int location_index;
    public String user_id;
    public String name;
    public double latitude;
    public double longitude;
    public double measure_latitude;
    public double measure_longitude;
    public int lock;
    public String measure_date;
    public String create_date;
    public String update_date;
    public String category;

    public Location(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("location_index", location_index);
        result.put("name", name);
        result.put("user_id", user_id);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("measure_latitude", latitude);
        result.put("measure_longitude", longitude);
        result.put("lock", lock);
        result.put("measure_date", create_date);
        result.put("create_date", create_date);
        result.put("update_date", update_date);
        result.put("category", category);

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
