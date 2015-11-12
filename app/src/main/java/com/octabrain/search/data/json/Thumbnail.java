package com.octabrain.search.data.json;

import android.graphics.Bitmap;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class Thumbnail {

    private String MediaUrl;
    private Bitmap thumbnailImage;  // Loaded later.

    public String getMediaUrl() {
        return MediaUrl;
    }

    public Bitmap getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(Bitmap thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

}
