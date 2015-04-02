package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.marsdayjam.eventplanner.DBContract.LoginTable;

public class DBController {
    private static DBController ourInstance;

    private SQLiteDatabase db;

    public static DBController getInstance(Context context) {
        if (ourInstance != null)
            return ourInstance;
        else {
            ourInstance = new DBController(context);
            return ourInstance;
        }
    }

    private DBController(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertLogin(String email, String password) {
        ContentValues values = new ContentValues();
        values.put(LoginTable.COLUMN_NAME_EMAIL, email);
        values.put(LoginTable.COLUMN_NAME_PASSWORD, password);
        return db.insert(LoginTable.TABLE_NAME, null, values);
    }
}
