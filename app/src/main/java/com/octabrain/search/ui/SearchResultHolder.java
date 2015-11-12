package com.octabrain.search.ui;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.octabrain.search.R;
import com.octabrain.search.data.json.Result;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class SearchResultHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView resolution;
    private ThumbnailView thumbanail;

    public SearchResultHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.result_view_title);
        title.setTypeface(FontsHelper.getRobotoRegular(itemView.getContext()));
        thumbanail = (ThumbnailView) itemView.findViewById(R.id.result_view_thumbnail);
        resolution = (TextView) itemView.findViewById(R.id.result_view_image_resolution);
        resolution.setTypeface(FontsHelper.getRobotoRegular(itemView.getContext()));
    }

    public void bindData(Result result) {
        title.setText(result.getTitle());
        resolution.setText(result.getWidth() + "x" + result.getHeight());
    }

    public void bindThumbnail(Bitmap bitmap, boolean animate) {
        thumbanail.showImage(bitmap, animate);
    }

    public void unbindThumbnail() {
        thumbanail.showImage(null, false);
    }

}
