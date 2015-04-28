package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marsdayjam.eventplanner.DBContract.CalendarList;
import com.marsdayjam.eventplanner.DBContract.CalendarTable;
import com.marsdayjam.eventplanner.DBContract.EventTable;
import com.marsdayjam.eventplanner.DBContract.EmployeeTable;


/*
public class DBHelperD extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String CM = ",";
    private static final String LP = " ( ";
    private static final String RP = " ) ";

    private static final String CREATE_CALENDAR_LIST =
            "CREATE TABLE " + DBContract.CalendarList.TABLE_NAME + LP +
                    DBContract.CalendarList.COLUMN_NAME_CALENDARNAME + TEXT_TYPE + RP;

    private static final String CREATE_CALENDAR =
            "CREATE TABLE " + DBContract.CalendarTable.TABLE_NAME + LP +
                    CalendarTable._ID + " INTEGER PRIMARY KEY" + CM +
                    CalendarTable.COLUMN_NAME_DATE + TEXT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_START + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_END + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_EVENT + TEXT_TYPE + RP;

    private static final String CREATE_EVENT =
            "CREATE TABLE " + DBContract.CalendarTable.TABLE_NAME + LP +
                    EventTable._ID + " INTEGER PRIMARY KEY" + CM +
                    EventTable.COLUMN_NAME_SDATE + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_EDATE + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_TEAMS+ TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_CALENDARNAME + TEXT_TYPE + RP;

    public static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EventPlannerDynamic.db";

    public DBHelperD(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    public void onCreate(SQLiteDatabase db) {
        ContentValues values;

        db.execSQL(CREATE_CALENDAR_LIST);

        values = new ContentValues();
        values.put(CalendarList.COLUMN_NAME_CALENDARNAME, EmployeeTable.COLUMN_NAME_CALENDARNAME);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void updateVersion(){
        DATABASE_VERSION++;
    }
}
*/