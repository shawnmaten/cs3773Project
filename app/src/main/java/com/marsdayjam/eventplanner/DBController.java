package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.RolesTable;
import com.marsdayjam.eventplanner.DBContract.EventTable;
import com.marsdayjam.eventplanner.DBContract.TeamTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBController {
    private static DBController ourInstance;
    private DBHelper dbHelper;
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
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static DBController getMockDBController(Context context) {
        return new DBController(context);
    }

    /*THIS IS TO ADD EMPLOYEES TO THE DATABASE*/
    public long insertEmployee(String email, String password, String first, String last, int role) {
        long id;
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, email);
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, password);
        values.put(EmployeeTable.COLUMN_NAME_FIRST, first);
        values.put(EmployeeTable.COLUMN_NAME_LAST, last);
        values.put(EmployeeTable.COLUMN_NAME_ROLE, role);
        id = db.insert(EmployeeTable.TABLE_NAME, null, values);
        return id;
    }

    /*THIS IS TO DELETE EMPLOYEES TO THE DATABASE*/
    public void deleteEmployee(long id){
        //First we have to tell it what Column we are going to find employee by
        String selection = EmployeeTable._ID + " LIKE ?";
        //Then we have to give it the value to match the employee by in the column
        String[] selectionArgs = { String.valueOf(id) };
        // Now put that plus the table name into the delete function to remove employee
        db.delete(EmployeeTable.TABLE_NAME, selection, selectionArgs);
    }

    //Get all employees
    public List<Employee> getAllEmployees(){
        List<Employee> employeeList;
        employeeList = new ArrayList<>();      //Select All Query
        String selectQuery = "SELECT * FROM " + EmployeeTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(cursor.getString(0)));
                employee.setEmail(cursor.getString(1));
                employee.setFirst(cursor.getString(2));
                employee.setLast(cursor.getString(3));
                //Adding employee to list
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
        //return employee list
        cursor.close();
        return employeeList;
    }

    //Getting total employee count
    public long getEmployeeCount(){
        String countQuery = "SELECT  * FROM " + EmployeeTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //updating a single employee
    public long updateEmployee(Employee employee){
        long id;
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, employee.getEmail());
        values.put(EmployeeTable.COLUMN_NAME_FIRST, employee.getFirst());
        values.put(EmployeeTable.COLUMN_NAME_LAST, employee.getLast());
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, employee.getPassword());
        values.put(EmployeeTable.COLUMN_NAME_ROLE, employee.getRoleTitle());

        //updating row
        id =  db.update(EmployeeTable.TABLE_NAME,
                values,
                EmployeeTable._ID + " = ?",
                new String[]{String.valueOf(employee.getId())});
        
        return id;
    }

    // helper for getEmployee() so it can take id or email
    public Employee getEmployeeHelper(String selection, String selectionArgs[]) {
        Cursor cursor;
        Employee employee;

        String[] projection = {
                EmployeeTable._ID,
                EmployeeTable.COLUMN_NAME_EMAIL,
                EmployeeTable.COLUMN_NAME_PASSWORD,
                EmployeeTable.COLUMN_NAME_FIRST,
                EmployeeTable.COLUMN_NAME_LAST,
                EmployeeTable.COLUMN_NAME_ROLE
        };
        String sortOrder = EmployeeTable._ID + " DESC";

        cursor = db.query(
                EmployeeTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(EmployeeTable._ID)
            );
            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(EmployeeTable.COLUMN_NAME_EMAIL)
            );
            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(EmployeeTable.COLUMN_NAME_PASSWORD)
            );
            String first = cursor.getString(
                    cursor.getColumnIndexOrThrow(EmployeeTable.COLUMN_NAME_FIRST)
            );
            String last = cursor.getString(
                    cursor.getColumnIndexOrThrow(EmployeeTable.COLUMN_NAME_LAST)
            );
            int roleCode = cursor.getInt(
                    cursor.getColumnIndexOrThrow(EmployeeTable.COLUMN_NAME_ROLE)
            );
            cursor.close();

            employee =  new Employee();
            employee.setId(id);
            employee.setEmail(email);
            employee.setPassword(password);
            employee.setFirst(first);
            employee.setLast(last);
            employee.setRoleCode(roleCode);
            employee.setRoleTitle(getRoleDescription(roleCode));
        }
        else
            employee = null;

        cursor.close();
        return employee;
    }

    // Get a single employee by their email, used for login
    public Employee getEmployee(String email) {
        String selection = EmployeeTable.COLUMN_NAME_EMAIL + "=?";
        String selectionArgs[] = {
                email
        };
        return getEmployeeHelper(selection, selectionArgs);
    }

    // Get a single employee by their id
    public Employee getEmployee(long id) {
        String selection = EmployeeTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(id)
        };
        return getEmployeeHelper(selection, selectionArgs);
    }

    // Get the word description of an employee role
    public String getRoleDescription(int roleCode) {
        String selection = RolesTable._ID + "=?";
        String selectionArgs[] = {
                Integer.toString(roleCode)
        };
        String[] projection = {
                RolesTable._ID,
                RolesTable.COLUMN_NAME_TITLE
        };
        String sortOrder = RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                RolesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        String title = "";

        if (cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndexOrThrow(RolesTable.COLUMN_NAME_TITLE));
        }

        cursor.close();
        return title;
    }

    // Everything after this is from Juan

    // Inserts an Event.
    public long insertEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventTable.COLUMN_NAME_EVENT_NAME, event.getName());
        values.put(EventTable.COLUMN_NAME_HOST, event.getHost());
        values.put(EventTable.COLUMN_NAME_LOCATION, event.getLocation());
        values.put(EventTable.COLUMN_NAME_START, event.getStart().getTime());
        values.put(EventTable.COLUMN_NAME_END, event.getEnd().getTime());
        values.put(EventTable.COLUMN_NAME_MANAGER_ID, event.getManager().getId());
        return db.insert(EventTable.TABLE_NAME, null, values);
    }

    // Delete an Event based on its id.
    public void deleteEvent(long id){
        //First we have to tell it what Column we are going to find employee by
        String selection = EventTable._ID + " LIKE ?";
        //Then we have to give it the value to match the employee by in the column
        String[] selectionArgs = { String.valueOf(id) };
        // Now put that plus the table name into the delete function to remove employee
        db.delete(EventTable.TABLE_NAME, selection, selectionArgs);
    }

    // Gets an Event based on its id.
    public Event getEvent(long id) {
        String selection = EventTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(id)
        };
        String[] projection = {
                EventTable._ID,
                EventTable.COLUMN_NAME_EVENT_NAME,
                EventTable.COLUMN_NAME_HOST,
                EventTable.COLUMN_NAME_LOCATION,
                EventTable.COLUMN_NAME_START,
                EventTable.COLUMN_NAME_END,
                EventTable.COLUMN_NAME_MANAGER_ID
        };
        String sortOrder = RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                EventTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        Event event = null;
        if(cursor.moveToFirst()){
            event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(EventTable._ID)));
            event.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                    EventTable.COLUMN_NAME_EVENT_NAME)));
            event.setHost(cursor.getString(cursor.getColumnIndexOrThrow(
                    EventTable.COLUMN_NAME_HOST)));
            event.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(
                    EventTable.COLUMN_NAME_LOCATION)));
            event.setStart(new Date(cursor.getInt(cursor.getColumnIndexOrThrow(
                    EventTable.COLUMN_NAME_START))));
            event.setEnd(new Date(cursor.getInt(cursor.getColumnIndexOrThrow(
                    EventTable.COLUMN_NAME_END))));
            event.setManager(getEmployee(cursor.getInt(cursor.getColumnIndexOrThrow(
                    EventTable.COLUMN_NAME_MANAGER_ID))));
        }
        cursor.close();
        return event;
    }

    // Inserts an Event.
    public long insertTeam(Team team) {
        ContentValues values = new ContentValues();
        values.put(TeamTable.COLUMN_NAME_EVENT_ID, team.getEvent().getId());
        values.put(TeamTable.COLUMN_NAME_SUPERVISOR_ID, team.getSupervisor().getId());
        values.put(TeamTable.COLUMN_NAME_TEAM_NAME, team.getName());
        values.put(TeamTable.COLUMN_NAME_DUTIES, team.getDuties());
        return db.insert(TeamTable.TABLE_NAME, null, values);
    }

    // Delete an Event based on its id.
    public void deleteTeam(long id){
        //First we have to tell it what Column we are going to find employee by
        String selection = TeamTable._ID + " LIKE ?";
        //Then we have to give it the value to match the employee by in the column
        String[] selectionArgs = { String.valueOf(id) };
        // Now put that plus the table name into the delete function to remove employee
        db.delete(TeamTable.TABLE_NAME, selection, selectionArgs);
    }

    // Gets an Event based on its id.
    public Team getTeam(long id) {
        String selection = TeamTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(id)
        };
        String[] projection = {
                TeamTable._ID,
                TeamTable.COLUMN_NAME_EVENT_ID,
                TeamTable.COLUMN_NAME_SUPERVISOR_ID,
                TeamTable.COLUMN_NAME_TEAM_NAME,
                TeamTable.COLUMN_NAME_DUTIES,
        };
        String sortOrder = RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                TeamTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        Team team = null;
        if(cursor.moveToFirst()){
            team = new Team();
            team.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TeamTable._ID)));
            team.setEvent(getEvent(cursor.getInt(cursor.getColumnIndexOrThrow(
                    TeamTable.COLUMN_NAME_EVENT_ID))));
            team.setSupervisor(getEmployee(cursor.getInt(cursor.getColumnIndexOrThrow(
                    TeamTable.COLUMN_NAME_SUPERVISOR_ID))));
            team.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                    TeamTable.COLUMN_NAME_TEAM_NAME)));
            team.setDuties(cursor.getString(cursor.getColumnIndexOrThrow(
                    TeamTable.COLUMN_NAME_DUTIES)));
        }
        cursor.close();
        return team;
    }

    /**
     * Returns ArrayList containing all relevant Event Data for viewing. Must remember index of each
     * piece of information when using the ArrayList.
     */
    /*
    public ArrayList<String> eventDetails(String eventName){
        ArrayList<String> list = new ArrayList<String>();
        String[] columns = {DBContract.EventTable.COLUMN_NAME_EVENT_NAME, DBContract.EventTable.COLUMN_NAME_HOST,
                DBContract.EventTable.COLUMN_NAME_LOCATION, DBContract.EventTable.COLUMN_NAME_SDATE,
                DBContract.EventTable.COLUMN_NAME_EDATE, DBContract.EventTable.COLUMN_NAME_ETIME};
        Cursor cursor = db.query(DBContract.EventTable.TABLE_NAME, columns, DBContract.EventTable.COLUMN_NAME_EVENT_NAME +
                " = '"+eventName+"'", null, null, null, null);
        cursor.moveToNext();

        int index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_EVENT_NAME);
        String event = cursor.getString(index);
        list.add(event);
        index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_HOST);
        String host = cursor.getString(index);
        list.add(host);
        index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_LOCATION);
        String location = cursor.getString(index);
        list.add(location);
        index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_SDATE);
        String startD = cursor.getString(index);
        list.add(startD);
        index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_EDATE);
        String endD = cursor.getString(index);
        list.add(endD);
        index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_ETIME);
        String endT = cursor.getString(index);
        list.add(endT);

        return list;
    }
    */

    /*
    public void deleteEvent(String eventName) {
        String[] columns = {DBContract.EventTable.COLUMN_NAME_EVENT_NAME, DBContract.EventTable.COLUMN_NAME_CALENDARNAME};
        Cursor cursor = db.query(DBContract.EventTable.TABLE_NAME, columns, DBContract.EventTable.COLUMN_NAME_EVENT_NAME +
                " = '"+eventName+"'", null, null, null, null);
        cursor.moveToNext();
        int index = cursor.getColumnIndex(DBContract.EventTable.COLUMN_NAME_CALENDARNAME);
        String calendarName = cursor.getString(index);
        deleteCalendar(calendarName);

        String[] whereArgs = {eventName};
        db.delete(DBContract.EventTable.TABLE_NAME, DBContract.EventTable.COLUMN_NAME_EVENT_NAME + "=?", whereArgs);
    }
    */

    /**
     * Handle CalendarList operations.
     */
    /*
    public long insertCalendar(String calendarName) {
        long id;
        //arbitrary method
        //createCalendarTable(calendarName);
        ContentValues values = new ContentValues();
        values.put(DBContract.CalendarList.COLUMN_NAME_CALENDARNAME, calendarName);
        id = db.insert(DBContract.CalendarList.TABLE_NAME, null, values);
        return id; // negative if something went wrong, is row index otherwise
    }
    */

    /*
    public void deleteCalendar(String calendarName) {
        //arbitrary method
        //deleteCalendarTable(calendarName);
        String[] whereArgs = {calendarName};
        db.delete(DBContract.CalendarList.TABLE_NAME, DBContract.CalendarList.COLUMN_NAME_CALENDARNAME+ "=?", whereArgs);
    }
    */

    /**
     * Returns an arraylist of the calendarNames intended for easy calendar(Table) selection.
     */
    /*
    public ArrayList<String> findAllCalendars() {
        String[] columns = {DBContract.CalendarList.COLUMN_NAME_CALENDARNAME};
        Cursor cursor = db.query(DBContract.CalendarList.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<String>();
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DBContract.CalendarList.COLUMN_NAME_CALENDARNAME);
            String calendarName = cursor.getString(index);
            list.add(calendarName);
        }
        return list;
    }
    */

    /**
     * Handle CalendarEvent operations
     */
    /*
    public long insertCalendarEvent(String date, int startH, int startM, int endH, int endM,
                                    String event){
        long id;
        ContentValues values = new ContentValues();
        values.put(DBContract.CalendarTable.COLUMN_NAME_DATE, date);
        values.put(DBContract.CalendarTable.COLUMN_NAME_STARTH, startH);
        values.put(DBContract.CalendarTable.COLUMN_NAME_STARTM, startM);
        values.put(DBContract.CalendarTable.COLUMN_NAME_ENDH, endH);
        values.put(DBContract.CalendarTable.COLUMN_NAME_ENDM, endM);
        values.put(DBContract.CalendarTable.COLUMN_NAME_EVENT, event);
        id = db.insert(DBContract.CalendarTable.TABLE_NAME, null, values);
        return id; // negative if something went wrong, is row index otherwise
    }
    */

    /**
     * Returns a String of all events (including start and end time for each) for the date passed
     * in. Date passed in must be in form given by Caldroid Calendar.
     */
    /*
    public String findAllCalendarEvents(String date) {
        String[] columns = {DBContract.CalendarTable.COLUMN_NAME_EVENT, DBContract.CalendarTable.COLUMN_NAME_STARTH,
                DBContract.CalendarTable.COLUMN_NAME_STARTM, DBContract.CalendarTable.COLUMN_NAME_ENDH,
                DBContract.CalendarTable.COLUMN_NAME_ENDM};
        Cursor cursor = db.query(DBContract.CalendarTable.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DBContract.CalendarTable.COLUMN_NAME_EVENT);
            String event = cursor.getString(index);
            index = cursor.getColumnIndex(DBContract.CalendarTable.COLUMN_NAME_STARTH);
            int startH = cursor.getInt(index);
            index = cursor.getColumnIndex(DBContract.CalendarTable.COLUMN_NAME_STARTM);
            int startM = cursor.getInt(index);
            index = cursor.getColumnIndex(DBContract.CalendarTable.COLUMN_NAME_ENDH);
            int endH = cursor.getInt(index);
            index = cursor.getColumnIndex(DBContract.CalendarTable.COLUMN_NAME_ENDM);
            int endM = cursor.getInt(index);
            buffer.append(event +"\n" +"\t"+startH+":"+startM+" - "+endH+":"+endM+"\n\n");
        }
        return buffer.toString();
    }
    */

    /**
     * Deletes all events for the Date sent in as argument. (Must be in same form Caldroid Calander
     * returns).
     */
    /*
    public void deleteCalendarsEvents(String date) {
        String[] whereArgs = {date};
        db.delete(DBContract.CalendarTable.TABLE_NAME, DBContract.CalendarTable.COLUMN_NAME_DATE + "=?", whereArgs);
    }
    */
}
