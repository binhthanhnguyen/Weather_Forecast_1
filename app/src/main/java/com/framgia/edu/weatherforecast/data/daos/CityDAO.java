package com.framgia.edu.weatherforecast.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.framgia.edu.weatherforecast.data.local.DatabaseContract;
import com.framgia.edu.weatherforecast.data.models.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 8/3/16.
 */
public class CityDAO extends DatabaseContentProvider {
    private static CityDAO sInstance = null;

    private CityDAO(Context context) {
        super(context);
    }

    public static CityDAO getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CityDAO(context);
        }
        return sInstance;
    }

    public boolean insert(City city) throws SQLException{
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CityTable.NAME, city.getName());
        values.put(DatabaseContract.CityTable.LATITUDE, city.getLatitude());
        values.put(DatabaseContract.CityTable.LONGITUDE, city.getLongitude());
        try {
            mSqLiteDatabase.insert(DatabaseContract.CityTable.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        close();
        return true;
    }

    public City findCityById(int cityId) throws SQLException {
        open();
        Cursor cursor = mSqLiteDatabase.query(DatabaseContract.CityTable.TABLE_NAME, null,
                DatabaseContract.CityTable.ID + " = ?", new String[]{String.valueOf(cityId)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        City city = new City(cursor);
        cursor.close();
        close();
        return city;
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        open();
        Cursor cursor = mSqLiteDatabase.query(DatabaseContract.CityTable.TABLE_NAME, null,
                null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                cities.add(new City(cursor));
            }
        }
        cursor.close();
        close();
        return cities;
    }

    public boolean delete(City city) throws SQLException{
        try {
            mSqLiteDatabase.delete(DatabaseContract.CityTable.TABLE_NAME,
                    DatabaseContract.CityTable.ID + " = ?", new String[]{String.valueOf(city.getId())});
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        close();
        return true;
    }
}
