package com.octabrain.search.data.json;

import android.graphics.Bitmap;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class Result {

    private int Width;
    private int Height;
    private String Title;
    private String MediaUrl;
    private Thumbnail Thumbnail;

    private Bitmap image;  // Loaded later.

    public String getTitle() {
        return Title;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getThumbnailUrl() {
        return Thumbnail.getMediaUrl();
    }

    public void setThumbnail(Bitmap thumbnailImage) {
        Thumbnail.setThumbnailImage(thumbnailImage);
    }

    public Bitmap getThumbnailImage() {
        return Thumbnail.getThumbnailImage();
    }

}
