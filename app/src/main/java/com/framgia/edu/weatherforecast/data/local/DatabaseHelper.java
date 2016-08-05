package com.framgia.edu.weatherforecast.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by binh on 8/3/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT";
    private static final String COMMA = ", ";

    private static final String CREATE_CITY_TABLE = "CREATE TABLE "
            + DatabaseContract.CityTable.TABLE_NAME + " ("
            + DatabaseContract.CityTable.ID + INTEGER_TYPE + PRIMARY_KEY + COMMA
            + DatabaseContract.CityTable.NAME + TEXT_TYPE + COMMA
            + DatabaseContract.CityTable.LATITUDE + REAL_TYPE + COMMA
            + DatabaseContract.CityTable.LONGITUDE + REAL_TYPE + ")";

    private static final String DROP_CITY_TABLE = "DROP TABLE IF EXISTS "
            + DatabaseContract.CityTable.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_CITY_TABLE);
        onCreate(db);
    }
}
