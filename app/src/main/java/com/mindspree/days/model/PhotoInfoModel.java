package com.mindspree.days.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 30-10-2015.
 */
public class PhotoInfoModel {

    public String name = "";
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
    public String photo_size;
    public int sortseq;
    public String user_id;
    public String file_url;

    public PhotoInfoModel()
    {
    }


}
