package com.marsdayjam.eventplanner;

import android.app.usage.UsageEvents;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import com.marsdayjam.eventplanner.DBContract.CalendarTable;
import com.marsdayjam.eventplanner.DBContract.CalendarList;
import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.EventTable;


import java.util.ArrayList;
import java.util.List;


public class DBControllerD {
    private static DBController ourInstance;
    private DBHelperD Helper;
    private SQLiteDatabase db;

    private DBControllerD(Context context) {
        Helper = new DBHelperD(context);
        db = Helper.getWritableDatabase();
    }

    // Handle Event Table operations
    // Event and Calendar names must be unique(no doubles)
    public long insertEvent(String eventName, String host, String location, String sDate,
                            String eTime, String eDate, String teamName, String calendarName) {
        long id;
        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_EVENTNAME, eventName);
        values.put(EventTable.COLUMN_NAME_HOST, host);
        values.put(EventTable.COLUMN_NAME_LOCATION, location);
        values.put(EventTable.COLUMN_NAME_SDATE, sDate);
        values.put(EventTable.COLUMN_NAME_ETIME, eTime);
        values.put(EventTable.COLUMN_NAME_EDATE, eDate);
        values.put(EventTable.COLUMN_NAME_TEAMNAME, teamName);
        values.put(EventTable.COLUMN_NAME_CALENDARNAME, calendarName);
        id = db.insert(CalendarList.TABLE_NAME, null, values);
        long cid = insertCalendar(calendarName);
        return id; // negative if something went wrong, is row index otherwise
    }

    // returns an arraylist of the eventNames intended for easy listing of Active Events
    public ArrayList<String> findAllEvents() {
        String[] columns = {EventTable.COLUMN_NAME_EVENTNAME};
        Cursor cursor = db.query(EventTable.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<String>();
        while(cursor.moveToNext()){
            int index  = cursor.getColumnIndex(EventTable.COLUMN_NAME_EVENTNAME);
            String eventName = cursor.getString(index);
            list.add(eventName);
        }
        return list;
    }

    /*
    Returns ArrayList containing all relevant Event Data for viewing.
    Must remember index of each piece of information when using the ArrayList.
     */
    public ArrayList<String> eventDetails(String eventName){
        ArrayList<String> list = new ArrayList<String>();
        String[] columns = {EventTable.COLUMN_NAME_EVENTNAME, EventTable.COLUMN_NAME_HOST,
                            EventTable.COLUMN_NAME_LOCATION, EventTable.COLUMN_NAME_SDATE,
                            EventTable.COLUMN_NAME_EDATE, EventTable.COLUMN_NAME_ETIME};
        Cursor cursor = db.query(EventTable.TABLE_NAME, columns, EventTable.COLUMN_NAME_EVENTNAME+
                " = '"+eventName+"'", null, null, null, null);
        cursor.moveToNext();

            int index = cursor.getColumnIndex(EventTable.COLUMN_NAME_EVENTNAME);
            String event = cursor.getString(index);
            list.add(event);
            index = cursor.getColumnIndex(EventTable.COLUMN_NAME_HOST);
            String host = cursor.getString(index);
            list.add(host);
            index = cursor.getColumnIndex(EventTable.COLUMN_NAME_LOCATION);
            String location = cursor.getString(index);
            list.add(location);
            index = cursor.getColumnIndex(EventTable.COLUMN_NAME_SDATE);
            String startD = cursor.getString(index);
            list.add(startD);
            index = cursor.getColumnIndex(EventTable.COLUMN_NAME_EDATE);
            String endD = cursor.getString(index);
            list.add(endD);
            index = cursor.getColumnIndex(EventTable.COLUMN_NAME_ETIME);
            String endT = cursor.getString(index);
            list.add(endT);

        return list;
    }

    public void deleteEvent(String eventName) {
        String[] columns = {EventTable.COLUMN_NAME_EVENTNAME, EventTable.COLUMN_NAME_CALENDARNAME};
        Cursor cursor = db.query(EventTable.TABLE_NAME, columns, EventTable.COLUMN_NAME_EVENTNAME+
                                 " = '"+eventName+"'", null, null, null, null);
        cursor.moveToNext();
        int index = cursor.getColumnIndex(EventTable.COLUMN_NAME_CALENDARNAME);
        String calendarName = cursor.getString(index);
        deleteCalendar(calendarName);

        String[] whereArgs = {eventName};
        db.delete(EventTable.TABLE_NAME, EventTable.COLUMN_NAME_EVENTNAME+ "=?", whereArgs);
    }


    // Handle Team Table operations





    // Handle CalendarList operations
    public long insertCalendar(String calendarName) {
        long id;
        //arbitrary method
        //createCalendarTable(calendarName);
        ContentValues values = new ContentValues();
        values.put(CalendarList.COLUMN_NAME_CALENDARNAME, calendarName);
        id = db.insert(CalendarList.TABLE_NAME, null, values);
        return id; // negative if something went wrong, is row index otherwise
    }

    public void deleteCalendar(String calendarName) {
        //arbitrary method
        //deleteCalendarTable(calendarName);
        String[] whereArgs = {calendarName};
        db.delete(CalendarList.TABLE_NAME, CalendarList.COLUMN_NAME_CALENDARNAME+ "=?", whereArgs);
    }

    // returns an arraylist of the calendarNames intended for easy calendar(Table) selection
    public ArrayList<String> findAllCalendars() {
        String[] columns = {CalendarList.COLUMN_NAME_CALENDARNAME};
        Cursor cursor = db.query(CalendarList.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<String>();
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(CalendarList.COLUMN_NAME_CALENDARNAME);
            String calendarName = cursor.getString(index);
            list.add(calendarName);
        }
        return list;
    }



    // Handle CalendarEvent operations
    public long insertCalendarEvent(String date, int startH, int startM, int endH, int endM,
                                    String event){
        long id;
        ContentValues values = new ContentValues();
        values.put(CalendarTable.COLUMN_NAME_DATE, date);
        values.put(CalendarTable.COLUMN_NAME_STARTH, startH);
        values.put(CalendarTable.COLUMN_NAME_STARTM, startM);
        values.put(CalendarTable.COLUMN_NAME_ENDH, endH);
        values.put(CalendarTable.COLUMN_NAME_ENDM, endM);
        values.put(CalendarTable.COLUMN_NAME_EVENT, event);
        id = db.insert(CalendarTable.TABLE_NAME, null, values);
        return id; // negative if something went wrong, is row index otherwise
    }

    // returns a String of all events (including start and end time for each) for the date passed in
    // date passed in must be in form given by Caldroid Calendar
    public String findAllCalendarEvents(String date) {
        String[] columns = {CalendarTable.COLUMN_NAME_EVENT, CalendarTable.COLUMN_NAME_STARTH,
                            CalendarTable.COLUMN_NAME_STARTM, CalendarTable.COLUMN_NAME_ENDH,
                            CalendarTable.COLUMN_NAME_ENDM};
        Cursor cursor = db.query(CalendarTable.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(CalendarTable.COLUMN_NAME_EVENT);
            String event = cursor.getString(index);
            index = cursor.getColumnIndex(CalendarTable.COLUMN_NAME_STARTH);
            int startH = cursor.getInt(index);
            index = cursor.getColumnIndex(CalendarTable.COLUMN_NAME_STARTM);
            int startM = cursor.getInt(index);
            index = cursor.getColumnIndex(CalendarTable.COLUMN_NAME_ENDH);
            int endH = cursor.getInt(index);
            index = cursor.getColumnIndex(CalendarTable.COLUMN_NAME_ENDM);
            int endM = cursor.getInt(index);
            buffer.append(event +"\n" +"\t"+startH+":"+startM+" - "+endH+":"+endM+"\n\n");
        }
        return buffer.toString();
    }

    // Deletes all events for the Date sent in as argument(Must be in same form Caldroid Calander returns)
    public void deleteCalendarsEvents(String date) {
        String[] whereArgs = {date};
        db.delete(CalendarTable.TABLE_NAME, CalendarTable.COLUMN_NAME_DATE+ "=?", whereArgs);
    }

}
