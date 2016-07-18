package com.framgia.binhnguyen.weatherforecast.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by binh on 7/6/16.
 */
public class DataPoint {
    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("sunriseTime")
    private String mSunriseTime;
    @SerializedName("sunsetTime")
    private String mSunsetTime;
    @SerializedName("nearestStormDistance")
    private String mNearestStormDistance;
    @SerializedName("NearestStormBearing")
    private String mNearestStormBearing;
    @SerializedName("precipIntensity")
    private String mPrecipIntensity;
    @SerializedName("temperature")
    private double mTemperature;
    @SerializedName("temperatureMin")
    private double mTemperatureMin;
    @SerializedName("temperatureMinTime")
    private double mTemperatureMinTime;
    @SerializedName("temperatureMax")
    private double mTemperatureMax;
    @SerializedName("temperatureMaxTime")
    private double mTemperatureMaxTime;
    @SerializedName("dewPoint")
    private double mDewPoint;
    @SerializedName("windSpeed")
    private double mWindSpeed;
    @SerializedName("windBearing")
    private double mWindBearing;
    @SerializedName("cloudCover")
    private double mCloudCover;
    @SerializedName("humidity")
    private double mHumidity;
    @SerializedName("pressure")
    private double mPressure;
    @SerializedName("Visibility")
    private double mVisibility;
    @SerializedName("ozone")
    private double mOzone;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public String getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.mSunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return mSunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.mSunsetTime = sunsetTime;
    }

    public String getNearestStormDistance() {
        return mNearestStormDistance;
    }

    public void setNearestStormDistance(String nearestStormDistance) {
        this.mNearestStormDistance = nearestStormDistance;
    }

    public String getNearestStormBearing() {
        return mNearestStormBearing;
    }

    public void setNearestStormBearing(String nearestStormBearing) {
        this.mNearestStormBearing = nearestStormBearing;
    }

    public String getPrecipIntensity() {
        return mPrecipIntensity;
    }

    public void setPrecipIntensity(String precipIntensity) {
        this.mPrecipIntensity = precipIntensity;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        this.mTemperature = temperature;
    }

    public double getTemperatureMin() {
        return mTemperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.mTemperatureMin = temperatureMin;
    }

    public double getTemperatureMinTime() {
        return mTemperatureMinTime;
    }

    public void setTemperatureMinTime(double temperatureMinTime) {
        this.mTemperatureMinTime = temperatureMinTime;
    }

    public double getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.mTemperatureMax = temperatureMax;
    }

    public double getTemperatureMaxTime() {
        return mTemperatureMaxTime;
    }

    public void setTemperatureMaxTime(double temperatureMaxTime) {
        this.mTemperatureMaxTime = temperatureMaxTime;
    }

    public double getDewPoint() {
        return mDewPoint;
    }

    public void setDewPoint(double dewPoint) {
        this.mDewPoint = dewPoint;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.mWindSpeed = windSpeed;
    }

    public double getWindBearing() {
        return mWindBearing;
    }

    public void setWindBearing(double windBearing) {
        this.mWindBearing = windBearing;
    }

    public double getCloudCover() {
        return mCloudCover;
    }

    public void setCloudCover(double cloudCover) {
        this.mCloudCover = cloudCover;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        this.mHumidity = humidity;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double pressure) {
        this.mPressure = pressure;
    }

    public double getVisibility() {
        return mVisibility;
    }

    public void setVisibility(double visibility) {
        this.mVisibility = visibility;
    }

    public double getOzone() {
        return mOzone;
    }

    public void setOzone(double ozone) {
        this.mOzone = ozone;
    }
}
