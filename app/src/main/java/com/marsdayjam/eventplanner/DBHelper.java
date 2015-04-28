package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.RolesTable;
import com.marsdayjam.eventplanner.DBContract.CalendarList;
import com.marsdayjam.eventplanner.DBContract.CalendarTable;
import com.marsdayjam.eventplanner.DBContract.EventTable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String CM = ",";
    private static final String LP = " ( ";
    private static final String RP = " ) ";

    private static final String CREATE_EMPLOYEE =
            "CREATE TABLE " + EmployeeTable.TABLE_NAME + LP +
                    EmployeeTable._ID + " INTEGER PRIMARY KEY " + CM +
                    EmployeeTable.COLUMN_NAME_EMAIL + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_PASSWORD + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_FIRST + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_LAST + TEXT_TYPE + CM +
                    EmployeeTable.COLUMN_NAME_ROLE + INT_TYPE + CM +
                    " FOREIGN KEY " + LP + EmployeeTable.COLUMN_NAME_ROLE + RP + " REFERENCES "
                        + RolesTable.TABLE_NAME + LP + RolesTable._ID + RP + RP;

    private static final String CREATE_ROLES =
            " CREATE TABLE " + RolesTable.TABLE_NAME + LP +
                    RolesTable._ID + " INTEGER PRIMARY KEY " + CM +
                    RolesTable.COLUMN_NAME_TITLE + TEXT_TYPE + CM +
                    RolesTable.COLUMN_EDIT_EMPLOYEES + INT_TYPE + RP;

    private static final String SQL_DELETE_ENTRIES =
            " DROP TABLE IF EXISTS " + EmployeeTable.TABLE_NAME +
            " DROP TABLE IF EXISTS " + RolesTable.TABLE_NAME;

    private static final String CREATE_CALENDAR_LIST =
            "CREATE TABLE " + CalendarList.TABLE_NAME + LP +
                    CalendarList.COLUMN_NAME_CALENDARNAME + TEXT_TYPE + RP;

    private static final String CREATE_CALENDAR =
            "CREATE TABLE " + CalendarTable.TABLE_NAME + LP +
                    CalendarTable._ID + " INTEGER PRIMARY KEY" + CM +
                    CalendarTable.COLUMN_NAME_DATE + TEXT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_STARTH + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_STARTM + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_ENDH + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_ENDM + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_EVENT + TEXT_TYPE + RP;

    private static final String CREATE_EVENT =
            "CREATE TABLE " + EventTable.TABLE_NAME + LP +
                    EventTable._ID + " INTEGER PRIMARY KEY" + CM +
                    EventTable.COLUMN_NAME_EVENTNAME + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_HOST + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_LOCATION + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_SDATE + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_ETIME + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_EDATE + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_TEAMNAME + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_CALENDARNAME + TEXT_TYPE + RP;

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
        db.execSQL(CREATE_CALENDAR_LIST);
        db.execSQL(CREATE_CALENDAR);
        db.execSQL(CREATE_EVENT);

        values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, "hr1@eventplanners.com");
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, "hr1Password");
        values.put(EmployeeTable.COLUMN_NAME_FIRST, "Nancy");
        values.put(EmployeeTable.COLUMN_NAME_LAST, "Botwin");
        values.put(EmployeeTable.COLUMN_NAME_ROLE, 1);
        db.insert(DBContract.EmployeeTable.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(RolesTable.COLUMN_NAME_TITLE, "Human Resources");
        values.put(RolesTable.COLUMN_EDIT_EMPLOYEES, 1);
        db.insert(DBContract.RolesTable.TABLE_NAME, null, values);
        values.put(RolesTable.COLUMN_NAME_TITLE, "Manager");
        values.put(RolesTable.COLUMN_EDIT_EMPLOYEES, 0);
        db.insert(DBContract.RolesTable.TABLE_NAME, null, values);
        values.put(RolesTable.COLUMN_NAME_TITLE, "General Employee");
        values.put(RolesTable.COLUMN_EDIT_EMPLOYEES, 0);
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
