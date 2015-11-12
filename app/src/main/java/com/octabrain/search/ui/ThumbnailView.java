package com.octabrain.search.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.octabrain.search.R;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class ThumbnailView extends FrameLayout {

    private ImageView image;

    public ThumbnailView(Context context) {
        super(context);
        init(context);
    }

    public ThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_thumbnail, this);
        image = (ImageView) findViewById(R.id.thumbnail_image);
    }

    public void showImage(Bitmap bitmap, boolean animate) {
        image.setImageBitmap(bitmap);
        if (animate) {
            Animation appear = AnimationUtils.loadAnimation(getContext(), R.anim.appear);
            image.setAnimation(appear);
        }
    }

}
