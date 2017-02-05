package com.mindspree.days.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.mindspree.days.lib.AppUtils;

/**
 * Created by Admin on 19-10-2015.
 */
public class PhotosGridModel implements Parcelable , Comparable<PhotosGridModel>{

    private String path = "";
    private int itemId = 0;
    private boolean isDirectory = false;
    private String isFolder = "";
    private String counter = "";
    private Bitmap image = null;
    private String date = "";
    private int isBest = 0;

    private int clusterId = 0;
    private Float qualityRank = 0.0f;

    public PhotosGridModel(String path, boolean isDirectory, String isFolder, Bitmap image, String counter, int ClusterId, float QualityRank, int fileKey, String date, int isBest) {
        this.path = path;
        this.isDirectory = isDirectory;
        this.isFolder = isFolder;
        this.image = image;
        this.counter = counter;
        this.clusterId = ClusterId;
        this.qualityRank = QualityRank;
        this.itemId = fileKey;
        this.date = date;
        this.isBest = isBest;
    }

    public PhotosGridModel(Parcel in) {
        this.itemId = in.readInt();
        this.clusterId = in.readInt();
        this.path = in.readString();
        this.isFolder = in.readString();
        this.counter = in.readString();
    }



    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getClusterId() {
        return clusterId;
    }

    public int getIsBest() {
        return isBest;
    }

    public Float getQualityRank() {
        return qualityRank;
    }

    public int getItemId() {
        return itemId;
    }

    public String getDate() {
        return date;
    }
    public String getDateFormat() {
        return AppUtils.getDate(date);
    }

    public String getIsFolder() {
        return isFolder;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(clusterId);
        dest.writeInt(itemId);
        dest.writeString(path);
        dest.writeString(isFolder);
        dest.writeString(counter);

    }

    public static final Creator CREATOR = new Creator() {
        public PhotosGridModel createFromParcel(Parcel in) {
            return new PhotosGridModel(in);
        }

        public PhotosGridModel[] newArray(int size) {
            return new PhotosGridModel[size];
        }
    };

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(PhotosGridModel others) {

        return (this.clusterId-(others.getClusterId()))*(1);

    }
}
