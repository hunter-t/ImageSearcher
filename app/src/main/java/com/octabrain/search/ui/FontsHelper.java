package com.octabrain.search.ui;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class FontsHelper {

    private static final String ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    private static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";

    private static Typeface robotoLight;
    private static Typeface robotoRegular;

    public static Typeface getRobotoLight(Context context) {
        if (robotoLight == null) {
            robotoLight = Typeface.createFromAsset(context.getAssets(), ROBOTO_LIGHT);
        }
        return robotoLight;
    }

    public static Typeface getRobotoRegular(Context context) {
        if (robotoRegular == null) {
            robotoRegular = Typeface.createFromAsset(context.getAssets(), ROBOTO_REGULAR);
        }
        return robotoRegular;
    }

}
