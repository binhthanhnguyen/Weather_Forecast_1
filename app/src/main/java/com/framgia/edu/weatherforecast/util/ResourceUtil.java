package com.framgia.edu.weatherforecast.util;

import com.framgia.edu.weatherforecast.R;


/**
 * Created by binh on 7/20/16.
 */
public class ResourceUtil {

    public static int getIdentifier(String resourceName) {
        switch (resourceName) {
            case "clear-day" :
                return R.drawable.clear_day;
            case "clear-night" :
                return R.drawable.clear_night;
            case "rain" :
                return R.drawable.rain;
            case "snow" :
                return R.drawable.snow;
            case "sleet" :
                return R.drawable.sleet;
            case "wind" :
                return R.drawable.wind;
            case "cloudy" :
                return R.drawable.cloudy;
            case "fog" :
                return R.drawable.fog;
            case "partly-cloudy-day" :
                return R.drawable.partly_cloudy_day;
            case "partly-cloudy-night" :
                return R.drawable.partly_cloudy_night;
            case "sunny" :
                return R.drawable.sunny;
            default:
                return 0;
        }
    }

    public static int getBackgroundIdentifier(String icon) {
        switch (icon) {
            case "clear-day" :
                return R.drawable.bg_clear_day;
            case "clear-night" :
                return R.drawable.bg_clear_night;
            case "rain" :
                return R.drawable.bg_rain;
            case "snow" :
                return R.drawable.bg_snow_day;
            case "wind" :
                return R.drawable.bg_wind;
            case "fog" :
                return R.drawable.bg_fog;
            case "cloudy" :
                return R.drawable.bg_cloudy_day;
            case "partly-cloudy-day" :
                return R.drawable.bg_cloudy_day;
            case "partly-cloudy-night" :
                return R.drawable.bg_cloudy_night;
            default:
                return 0;
        }
    }
}
