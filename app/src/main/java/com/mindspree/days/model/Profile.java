package com.mindspree.days.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vision51 on 2017. 1. 5..
 */
@IgnoreExtraProperties
public class Profile {

    public String name;
    public String imageurl;
    public String gender;
    public String birthday;
    public String address1;
    public double latitude1;
    public double longitude1;
    public String address2;
    public double latitude2;
    public double longitude2;

    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Profile(String name, String imageurl, String gender, String birthday, String address1, String address2) {
        this.name = name;
        this.imageurl = imageurl;
        this.gender = gender;
        this.birthday = birthday;
        this.address1 = address1;
        this.address2 = address2;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("imageurl", imageurl);
        result.put("gender", gender);
        result.put("birthday", birthday);
        result.put("address1", address1);
        result.put("latitude1", latitude1);
        result.put("longitude1", longitude1);
        result.put("address2", address2);
        result.put("latitude2", latitude2);
        result.put("longitude2", longitude2);

        return result;
    }
}
