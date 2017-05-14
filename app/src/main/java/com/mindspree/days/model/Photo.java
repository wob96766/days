package com.mindspree.days.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 30-10-2015.
 */
@IgnoreExtraProperties
public class Photo {

    public int file_index;
    public String file_location;
    public String file_name;
    public int is_best;
    public int cluster_id;
    public String quality_rank;
    public String blurring_index;
    public String psnr_index;
    public String face_area;
    public String face_distance;
    public String extra_feat;
    public String weight_coeff;
    public String final_score;
    public int delete_check;
    public String update_date;
    public int photo_size;
    public int sortseq;
    public String user_id;
    public String file_url;
    public int flag;

    public Photo()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getDatetimeIndex()
    {
        String datetimeIndex = "";
        if(update_date != null){
            datetimeIndex = update_date.replace("-","").replace(":","").replace(" ", "").replace(".","");
        }
        return datetimeIndex;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("file_index", file_index);
        result.put("file_url", file_url);
        result.put("file_location", file_location);
        result.put("file_name", file_name);
        result.put("is_best", is_best);
        result.put("cluster_id", cluster_id);
        result.put("quality_rank", quality_rank);
        result.put("blurring_index", blurring_index);
        result.put("psnr_index", psnr_index);
        result.put("face_area", face_area);
        result.put("face_distance", face_distance);
        result.put("extra_feat", extra_feat);
        result.put("weight_coeff", weight_coeff);
        result.put("final_score", final_score);
        result.put("delete_check", delete_check);
        result.put("update_date", update_date);
        result.put("photo_size", photo_size);
        result.put("sortseq", sortseq);
        result.put("user_id", user_id);
        result.put("flag", flag);
        return result;
    }
}
