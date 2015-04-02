package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public Cursor readLogin(String email) {
        String[] projection = {
                LoginTable._ID,
                LoginTable.COLUMN_NAME_EMAIL,
                LoginTable.COLUMN_NAME_PASSWORD
        };
        String sortOrder = LoginTable._ID + " DESC";
        String selection = LoginTable.COLUMN_NAME_EMAIL + "=?";
        String selectionArgs[] = {
                email
        };

        return db.query(
                LoginTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public boolean checkLogin(String email, String password) {
        Cursor cursor = readLogin(email);
        cursor.moveToFirst();
        String dbPassword = cursor.getString(
                cursor.getColumnIndexOrThrow(LoginTable.COLUMN_NAME_PASSWORD)
        );
        return dbPassword.equals(password);
    }
}
