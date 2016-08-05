package com.framgia.edu.weatherforecast.data.daos;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.edu.weatherforecast.data.local.DatabaseHelper;

/**
 * Created by binh on 8/3/16.
 */
public class DatabaseContentProvider {
    protected SQLiteDatabase mSqLiteDatabase;
    private DatabaseHelper mDatabaseHelper;

    public DatabaseContentProvider(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() throws SQLException {
        mDatabaseHelper.close();
    }

}
