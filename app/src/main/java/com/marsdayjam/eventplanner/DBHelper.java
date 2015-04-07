package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.RolesTable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String CM = ",";
    private static final String LP = " ( ";
    private static final String RP = " ) ";

    private static final String CREATE_EMPLOYEE =
            "CREATE TABLE " + EmployeeTable.TABLE_NAME + LP +
                    EmployeeTable._ID + " INTEGER PRIMARY KEY" + CM +
                    EmployeeTable.COLUMN_NAME_EMAIL + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_PASSWORD + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_FIRST + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_LAST + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_ROLE + INT_TYPE + RP;

    private static final String CREATE_ROLES =
            "CREATE TABLE " + RolesTable.TABLE_NAME + LP +
                    RolesTable._ID + " INTEGER PRIMARY KEY" + CM +
                    RolesTable.COLUMN_NAME_CODE + INT_TYPE + CM +
                    RolesTable.COLUMN_NAME_TITLE + TEXT_TYPE + RP;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EmployeeTable.TABLE_NAME +
            "DROP TABLE IF EXISTS " + RolesTable.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EventPlanner.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        ContentValues values;

        db.execSQL(CREATE_EMPLOYEE);
        db.execSQL(CREATE_ROLES);

        values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, "hr1@eventplanners.com");
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, "hr1Password");
        values.put(EmployeeTable.COLUMN_NAME_FIRST, "Bob");
        values.put(EmployeeTable.COLUMN_NAME_LAST, "HR-Person");
        values.put(EmployeeTable.COLUMN_NAME_ROLE, 0);
        db.insert(DBContract.EmployeeTable.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(RolesTable.COLUMN_NAME_CODE, RolesTable.ROLE_HR);
        values.put(RolesTable.COLUMN_NAME_TITLE, "Human Resources");
        db.insert(DBContract.RolesTable.TABLE_NAME, null, values);
        values.put(RolesTable.COLUMN_NAME_CODE, RolesTable.ROLE_MG);
        values.put(RolesTable.COLUMN_NAME_TITLE, "Manager");
        db.insert(DBContract.RolesTable.TABLE_NAME, null, values);
        values.put(RolesTable.COLUMN_NAME_CODE, RolesTable.ROLE_GE);
        values.put(RolesTable.COLUMN_NAME_TITLE, "General Employee");
        db.insert(DBContract.RolesTable.TABLE_NAME, null, values);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
