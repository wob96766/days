package com.mindspree.days.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageItem {
    private Bitmap image;
    private String title;
    private boolean editItem;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
        this.editItem = false;
    }

    public ImageItem(Bitmap image, String title, boolean editItem) {
        super();
        this.image = image;
        this.title = title;
        this.editItem = editItem;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getEditItem() { return this.editItem; }

    public void setEditItem(boolean editItem) { this.editItem = editItem; }
}
