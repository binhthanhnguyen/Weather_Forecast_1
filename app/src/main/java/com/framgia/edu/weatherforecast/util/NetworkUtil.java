package com.framgia.edu.weatherforecast.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by binh on 8/1/16.
 */
public class NetworkUtil {
    public static int TYPE_MOBILE = 2;
    public static int TYPE_WIFI = 1;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
            else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
