package com.framgia.edu.weatherforecast.data.local;

/**
 * Created by binh on 8/3/16.
 */
public class DatabaseContract {
    public static final String DB_NAME = "weather_forecast.db";

    public abstract class CityTable {
        public static final String TABLE_NAME = "cities";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }
}
