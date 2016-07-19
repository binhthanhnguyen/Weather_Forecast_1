package com.framgia.edu.weatherforecast.util;

import com.framgia.edu.weatherforecast.data.models.DataPoint;
import com.framgia.edu.weatherforecast.data.models.ForecastResponse;
import com.framgia.edu.weatherforecast.data.models.TemperatureUnit;

import java.util.List;

/**
 * Created by binh on 7/24/16.
 */
public class UnitConverter {
    public static ForecastResponse temperatureToCelsius(ForecastResponse forecastResponse) {
        DataPoint currentDataPoint= forecastResponse.getCurrently();
        List<DataPoint> hourlyData = forecastResponse.getHourly().getData();
        List<DataPoint> dailyData = forecastResponse.getDaily().getData();
        DataPoint dataPoint;
        int dataSize;
        double temperature = currentDataPoint.getTemperature();
        double temperatureMax;

        currentDataPoint.setTemperature(TemperatureUnit.toCelsius(temperature));
        dataSize = hourlyData.size();
        for (int i = 0; i < dataSize; i++) {
            dataPoint = hourlyData.get(i);
            temperature = dataPoint.getTemperature();
            dataPoint.setTemperature(TemperatureUnit.toCelsius(temperature));

        }
        dataSize = dailyData.size();
        for (int i = 0; i < dataSize; i++) {
            dataPoint = dailyData.get(i);
            temperature = dataPoint.getTemperature();
            temperatureMax = dataPoint.getTemperatureMax();
            dataPoint.setTemperature(TemperatureUnit.toCelsius(temperature));
            dataPoint.setTemperatureMax(TemperatureUnit.toCelsius(temperatureMax));
        }
        return forecastResponse;
    }

    public static ForecastResponse temperatureToFahrenheit(ForecastResponse forecastResponse) {
        DataPoint currentDataPoint = forecastResponse.getCurrently();
        List<DataPoint> hourlyData = forecastResponse.getHourly().getData();
        List<DataPoint> dailyData = forecastResponse.getDaily().getData();
        DataPoint dataPoint;
        double temperature = currentDataPoint.getTemperature();
        double temperatureMax;
        int dataSize;

        currentDataPoint.setTemperature(TemperatureUnit.toFahrenheit(temperature));
        dataSize = hourlyData.size();
        for (int i = 0; i < dataSize; i++) {
            dataPoint = hourlyData.get(i);
            temperature = dataPoint.getTemperature();
            dataPoint.setTemperature(TemperatureUnit.toFahrenheit(temperature));
        }

        dataSize = dailyData.size();
        for (int i = 0; i < dataSize; i++) {
            dataPoint = dailyData.get(i);
            temperature = dataPoint.getTemperature();
            temperatureMax = dataPoint.getTemperatureMax();
            dataPoint.setTemperature(TemperatureUnit.toFahrenheit(temperature));
            dataPoint.setTemperatureMax(TemperatureUnit.toFahrenheit(temperatureMax));
        }
        return forecastResponse;
    }
}
