package com.framgia.edu.weatherforecast.data.models;

/**
 * Created by binh on 7/25/16.
 */
public class SpeedUnit {
    public static final int MILES_PER_HOUR = 0;
    public static final int KILOMETRES_PER_HOUR = 1;
    public static final int METRES_PER_SECOND = 2;

    public static double fromMphToKph(double mph) {
        return mph * 1.609344;
    }

    public static double fromMphToMetresPerSecond(double mph) {
        return mph * 0.44704;
    }


    public static double fromKphToMph(double kilometresPerHour) {
        return kilometresPerHour / 1.609344;
    }

    public static double fromKphToMetresPerSecond(double kilometresPerHour) {
        return kilometresPerHour / 3.6;
    }

    public static double fromMetresPerSecondToMph(double metresPerSecond) {
        return metresPerSecond / 0.44704;
    }

    public static double fromMetresPerSecondToKph(double metresPerSecond) {
        return metresPerSecond * 3.6;
    }
}