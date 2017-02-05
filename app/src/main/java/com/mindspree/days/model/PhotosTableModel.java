package com.mindspree.days.model;

/**
 * Created by Admin on 30-10-2015.
 */
public class PhotosTableModel {

    private String file_location;
    private String file_name;
    private int cluster_id;
    private String quality_rank;
    private String blurring_index;
    private String psnr_index;
    private String face_area;
    private String face_distance;
    private String extra_feat;
    private String weight_coeff;
    private String final_score;
    private int delete_check;
    private String update_date;

    private int photo_size;


    public String getFile_location() {
        return file_location;
    }

    public void setFile_location(String file_location) {
        this.file_location = file_location;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(int cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getQuality_rank() {
        return quality_rank;
    }

    public void setQuality_rank(String quality_rank) {
        this.quality_rank = quality_rank;
    }

    public String getBlurring_index() {
        return blurring_index;
    }

    public void setBlurring_index(String blurring_index) {
        this.blurring_index = blurring_index;
    }

    public String getPsnr_index() {
        return psnr_index;
    }

    public void setPsnr_index(String psnr_index) {
        this.psnr_index = psnr_index;
    }

    public String getFace_area() {
        return face_area;
    }

    public void setFace_area(String face_area) {
        this.face_area = face_area;
    }

    public String getFace_distance() {
        return face_distance;
    }

    public void setFace_distance(String face_distance) {
        this.face_distance = face_distance;
    }

    public String getExtra_feat() {
        return extra_feat;
    }

    public void setExtra_feat(String extra_feat) {
        this.extra_feat = extra_feat;
    }

    public String getWeight_coeff() {
        return weight_coeff;
    }

    public void setWeight_coeff(String weight_coeff) {
        this.weight_coeff = weight_coeff;
    }

    public String getFinal_score() {
        return final_score;
    }

    public void setFinal_score(String final_score) {
        this.final_score = final_score;
    }

    public int getDelete_check() {
        return delete_check;
    }

    public void setDelete_check(int delete_check) {
        this.delete_check = delete_check;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public int getPhoto_size() {
        return photo_size;
    }

    public void setPhoto_size(int photo_size) {
        this.photo_size = photo_size;
    }
}
