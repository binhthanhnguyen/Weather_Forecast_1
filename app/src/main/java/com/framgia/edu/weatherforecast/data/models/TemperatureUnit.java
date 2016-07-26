package com.framgia.edu.weatherforecast.data.models;

/**
 * Created by binh on 7/23/16.
 */
public abstract class TemperatureUnit {
    public static final int CELSIUS = 0;
    public static final int FAHRENHEIT = 1;

    public static double toCelsius(double fahrenheit) {
        return (fahrenheit - 32) / 1.8;
    }

    public static double toFahrenheit(double celsius) {
        return celsius * 1.8 + 32;
    }
}
