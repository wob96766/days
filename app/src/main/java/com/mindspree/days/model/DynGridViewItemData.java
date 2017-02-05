package com.mindspree.days.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Class to get and set the data related the Grid view
 */

public class DynGridViewItemData implements Parcelable , Comparable<DynGridViewItemData> {

    private int m_clusterId;
    private String m_imagePath;
    private String m_szLabel;
    private String m_count;
    private String m_isFolder;
    private int m_nWidth, m_nHeight, m_nPadding;
    private int m_nBackgroundRes,
            m_nFavoriteOnRes,
            m_nFavoriteOffRes,
            m_nImgRes, m_nItemId;
    private boolean m_bFavorite, m_bShowFavorite;
    private boolean m_bAllowDrag = false;

    public DynGridViewItemData(String label,
                               String count,
                               String isFoldet,
                               int w, int h, int padding,
                               int backres,
                               int favorite_on_res,
                               int favorite_off_res,
                               boolean b_favorite_state,
                               boolean b_allow_state,
                               boolean b_show_fav,
                               int itemimg,
                               int id,
                               String imagePath,
                               int clusterId) {
        m_nWidth = w;
        m_nHeight = h;
        m_nPadding = padding;
        m_szLabel = label;
        m_count = count;

        m_nBackgroundRes = backres;
        m_nFavoriteOnRes = favorite_on_res;
        m_nFavoriteOffRes = favorite_off_res;
        m_nImgRes = itemimg;
        m_nItemId = id;
        m_bFavorite = b_favorite_state;
        m_bAllowDrag = b_show_fav;
        m_bShowFavorite = b_allow_state;

        m_imagePath = imagePath;
        m_clusterId = clusterId;
        m_isFolder = isFoldet;
    }

    public DynGridViewItemData(Parcel in) {
        String[] data = new String[1];
        in.readStringArray(data);
        this.m_imagePath = data[0];

        String[] dataFolder = new String[1];
        in.readStringArray(dataFolder);
        this.m_isFolder = dataFolder[0];

        String[] dataCounter = new String[1];
        in.readStringArray(dataCounter);
        this.m_count = dataCounter[0];
    }

    public int getWidth() {
        return m_nWidth;
    }

    public int getHeight() {
        return m_nHeight;
    }

    public int getPadding() {
        return m_nPadding;
    }

    public String getLabel() {
        return m_szLabel;
    }

    public void setLabel(String p) {
        m_szLabel = p;
    }

    public String getM_count() {
        return m_count;
    }

    public String getM_isFolder() {
        return m_isFolder;
    }

    public void setM_isFolder(String m_isFolder) {
        this.m_isFolder = m_isFolder;
    }


    public void setM_count(String m_count) {
        this.m_count = m_count;
    }

    public int getBackgroundRes() {
        return m_nBackgroundRes;
    }

    public void setBackgroundRes(int p) {
        m_nBackgroundRes = p;
    }

    public int getFavoriteOnRes() {
        return m_nFavoriteOnRes;
    }

    public void setFavoriteOnRes(int p) {
        m_nFavoriteOnRes = p;
    }

    public int getFavoriteOffRes() {
        return m_nFavoriteOffRes;
    }

    public void setFavoriteOffRes(int p) {
        m_nFavoriteOffRes = p;
    }

    public int getImageRes() {
        return m_nImgRes;
    }

    public void setImageRes(int p) {
        m_nImgRes = p;
    }


    public int getItemId() {
        return m_nItemId;
    }

    public void setItemId(int p) {
        m_nItemId = p;
    }

    public boolean getFavoriteState() {
        return m_bFavorite;
    }

    public void setFavoriteState(boolean p) {
        m_bFavorite = p;
    }

    public boolean getFavoriteStateShow() {
        return m_bShowFavorite;
    }

    public void setFavoriteStateShow(boolean p) {
        m_bShowFavorite = p;
    }

    public boolean getAllowDrag() {
        return m_bAllowDrag;
    }

    public void setAllowDrag(boolean p) {
        m_bAllowDrag = p;
    }

    public String getM_imagePath() {
        return m_imagePath;
    }

    public int getM_clusterId() {
        return m_clusterId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeStringArray(new String[]{this.m_imagePath});
        dest.writeStringArray(new String[]{this.m_isFolder});
        dest.writeStringArray(new String[]{this.m_count});

    }

    public static final Creator CREATOR = new Creator() {
        public DynGridViewItemData createFromParcel(Parcel in) {
            return new DynGridViewItemData(in);
        }

        public DynGridViewItemData[] newArray(int size) {
            return new DynGridViewItemData[size];
        }
    };


    @Override
    public int compareTo(DynGridViewItemData others) {

//        return (this.-(others.getClusterId()))*(1);

        return 0;
    }
}
