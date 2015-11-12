package com.octabrain.search.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.octabrain.search.R;
import com.octabrain.search.data.ResultsDataSource;
import com.octabrain.search.data.json.Result;
import com.octabrain.search.events.Event;
import com.octabrain.search.events.EventDispatcher;
import com.octabrain.search.events.EventListener;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class ImageActivity extends AppCompatActivity {

    private static final int MAX_WIDTH = 1080;
    private static final int MAX_HEIGHT = 720;

    private ImageView image;
    private boolean isLoading;
    private View progressIndicator;
    private EventListener<Result> onImageLoaded;
    private EventListener<Result> onThumbnailLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String query = getIntent().getStringExtra("query");
        final int resultIdx = getIntent().getIntExtra("resultIdx", 0);
        final Result result = ResultsDataSource.get(query).getResults().get(resultIdx);

        String title = result.getTitle();
        if (title == null)
            title = getResources().getString(R.string.image_activity_title);
        getSupportActionBar().setTitle(title);

        image = (ImageView) findViewById(R.id.img);
        progressIndicator = findViewById(R.id.progress_indicator);

        if (savedInstanceState != null)
            isLoading = savedInstanceState.getBoolean("is_loading");
        if (result.getImage() != null) {
            showFullBitmap(result.getImage());
        } else {
            // Load full-sized bitmap.
            String imageUrl = result.getMediaUrl();
            ImageRequest imageRequest = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            isLoading = false;
                            result.setImage(bitmap);
                            showFullBitmap(result.getImage());
                        }
                    }, MAX_WIDTH, MAX_HEIGHT, ImageView.ScaleType.CENTER_CROP, null, null);
            if (!isLoading) {
                isLoading = true; // Prevent double queuing.
                Volley.newRequestQueue(getApplicationContext()).add(imageRequest);
            }
            // Show thumbnail while full bitmap is loading.
            if (result.getThumbnail() != null) {
                showThumbnail(result.getThumbnail());
            } else {
                // Wait when thumbnail is loaded.
                EventDispatcher.addListener(Event.THUMBNAIL_LOADED,
                        onThumbnailLoaded = new EventListener<Result>() {
                            @Override
                            public void handle(Event<Result> event) {
                                if (event.getMessage() == result) {
                                    result.setThumbnail(event.getMessage().getThumbnail());
                                    showThumbnail(result.getThumbnail());
                                }
                            }
                        }
                );
            }
        }
    }

    private void showThumbnail(Bitmap thumbnail) {
        image.setImageBitmap(thumbnail);
        image.setAlpha(.3f);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.setAnimation(rotate);
    }

    private void showFullBitmap(Bitmap bitmap) {
        progressIndicator.clearAnimation();
        progressIndicator.setVisibility(View.GONE);
        image.setAlpha(1f);
        image.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_loading", isLoading);
    }

    @Override
    protected void onDestroy() {
        EventDispatcher.removeListener(Event.THUMBNAIL_LOADED, onThumbnailLoaded);
        super.onDestroy();
    }
}
