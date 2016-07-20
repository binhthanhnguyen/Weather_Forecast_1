package com.framgia.edu.weatherforecast.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by binh on 7/14/16.
 */
public class ForecastResponse {
    @SerializedName("latitude")
    private double mLatitude;
    @SerializedName("longitude")
    private double mLongitude;
    @SerializedName("timezone")
    private String mTimezone;
    @SerializedName("offset")
    private int mOffset;
    @SerializedName("currently")
    private DataPoint mCurrently;
    @SerializedName("hourly")
    private DataBlock mHourly;
    @SerializedName("daily")
    private DataBlock mDaily;
    @SerializedName("minutely")
    private DataBlock mMinutely;

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        this.mTimezone = timezone;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public DataPoint getCurrently() {
        return mCurrently;
    }

    public void setCurrently(DataPoint currently) {
        this.mCurrently = currently;
    }

    public DataBlock getHourly() {
        return mHourly;
    }

    public void setHourly(DataBlock hourly) {
        this.mHourly = hourly;
    }

    public DataBlock getDaily() {
        return mDaily;
    }

    public void setDaily(DataBlock daily) {
        this.mDaily = daily;
    }

    public DataBlock getMinutely() {
        return mMinutely;
    }

    public void setMinutely(DataBlock minutely) {
        this.mMinutely = minutely;
    }
}
