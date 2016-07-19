package com.framgia.edu.weatherforecast.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by binh on 7/22/16.
 */
public class SettingsPreferences {
    private static final String PRE_SETTINGS_TEMPERATURE = "PRE_SETTINGS_TEMPERATURE ";
    private static final String PRE_SETTINGS_WIND_SPEED = "PRE_SETTINGS_WIND_SPEED";

    public static int getStoredSettingsTemperature(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PRE_SETTINGS_TEMPERATURE, 0);
    }

    public static void setStoredSettingsTemperature(Context context, int temperatureUnit) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(PRE_SETTINGS_TEMPERATURE, temperatureUnit).apply();
    }

    public static int getStoredSettingsWindSpeed(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PRE_SETTINGS_WIND_SPEED, 0);
    }

    public static void setStoredSettingsWindSpeed(Context context, int windSpeedUnit) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(PRE_SETTINGS_WIND_SPEED, windSpeedUnit).apply();
    }
}
