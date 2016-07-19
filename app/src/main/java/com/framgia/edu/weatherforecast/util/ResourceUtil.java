package com.framgia.edu.weatherforecast.util;

import android.content.Context;

/**
 * Created by binh on 7/20/16.
 */
public class ResourceUtil {

    public static int getIdentifier(Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName.replace("-", "_"), "drawable", context.getPackageName());
    }
}
