package com.framgia.edu.weatherforecast.util;

/**
 * Created by binh on 7/20/16.
 */
public class Temperature {
    private double mValue;

    public Temperature(double value){
        mValue = value;
    }

    @Override
    public String toString() {
        return String.valueOf(Math.round(mValue)) + (char) 0x00B0;
    }

    public static String stringForTemperature(double temperature) {
        return String.valueOf(Math.round(temperature)) + (char) 0x00B0;
    }
}
