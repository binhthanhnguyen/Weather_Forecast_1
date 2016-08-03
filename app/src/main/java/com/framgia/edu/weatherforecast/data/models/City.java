package com.framgia.edu.weatherforecast.data.models;

import android.database.Cursor;

import com.framgia.edu.weatherforecast.data.local.DatabaseContract;

/**
 * Created by binh on 8/3/16.
 */
public class City {
    private int mId;
    private String mName;
    private double mLatitude;
    private double mLongitude;

    public City(Cursor cursor) {
        mId = cursor.getInt(0);
        mName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CityTable.NAME));
        mLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.CityTable.LATITUDE));
        mLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.CityTable.LONGITUDE));
    }

    public City(String name, double latitude, double longitude) {
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public City(int id, String name, double latitude, double longitude) {
        this(name, latitude, longitude);
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
