package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.CalendarTable;
import com.marsdayjam.eventplanner.DBContract.CalendarList;

import java.util.ArrayList;
import java.util.List;

/*
public class DBControllerD {
    private static DBController ourInstance;
    private DBHelperD Helper;
    private SQLiteDatabase db;

    private DBControllerD(Context context) {
        Helper = new DBHelperD(context);
        db = Helper.getWritableDatabase();
    }

    // Handle Event Table operations



    // Handle Team Table operations



    // Handle CalendarList operations
    public long insertCalendar(String calendarName) {
        long id;
        ContentValues values = new ContentValues();
        values.put(CalendarList.COLUMN_NAME_CALENDARNAME, calendarName);
        id = db.insert(CalendarList.TABLE_NAME, null, values);
        return id;
    }

    public void deleteCalendar(String calendar) {
        String[] whereArgs = {calendar};
        db.delete(CalendarList.TABLE_NAME, CalendarList.COLUMN_NAME_CALENDARNAME+ "=?", whereArgs);
    }


    // Handle CalendarEvent operations

    public long insertCalendarEvent(String date, int start, int end, String event){
        long id;
        ContentValues values = new ContentValues();
        values.put(CalendarTable.COLUMN_NAME_DATE, date);
        values.put(CalendarTable.COLUMN_NAME_START, start);
        values.put(CalendarTable.COLUMN_NAME_END, end);
        values.put(CalendarTable.COLUMN_NAME_EVENT, event);
        id = db.insert(CalendarTable.TABLE_NAME, null, values);
        return id;
    }

    public String findAllEvents(String date, int ) {
        String[] columns = {CalendarTable.COLUMN_NAME_EVENT};
        Cursor cursor = db.query(CalendarTable.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            String event = cursor.getColumnIndex(CalendarTable.COLUMN_NAME_EVENT)
            buffer.append(event +"\n\n");
        }
        return buffer.toString();
    }

    public void deleteEvents(String date) {
        String[] whereArgs = {date};
        db.delete(CalendarTable.TABLE_NAME, CalendarTable.COLUMN_NAME_DATE+ "=?", whereArgs);
    }

}
*/