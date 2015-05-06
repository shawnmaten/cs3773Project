package com.marsdayjam.eventplanner.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marsdayjam.eventplanner.Calendar.CalendarEvent;
import com.marsdayjam.eventplanner.Employee.Employee;
import com.marsdayjam.eventplanner.Event;
import com.marsdayjam.eventplanner.Team;

import java.util.ArrayList;
import java.util.Date;

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
    public long insertEmployee(Employee employee) {
        ContentValues values = new ContentValues();
        values.put(DBContract.EmployeeTable.COLUMN_NAME_EMAIL, employee.getEmail());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_PASSWORD, employee.getPassword());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_FIRST, employee.getFirst());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_LAST, employee.getLast());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_ROLE, employee.getRoleCode());
        employee.setId(db.insert(DBContract.EmployeeTable.TABLE_NAME, null, values));
        for (CalendarEvent calendarEvent : employee.getCalendarEvents())
            insertCalendarEvent(calendarEvent);
        return employee.getId();
    }

    /*THIS IS TO DELETE EMPLOYEES TO THE DATABASE*/
    public void deleteEmployee(Employee employee){
        String selection = DBContract.EmployeeTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(employee.getId()) };
        for (CalendarEvent calendarEvent : employee.getCalendarEvents())
            deleteCalendarEvent(calendarEvent);
        db.delete(DBContract.EmployeeTable.TABLE_NAME, selection, selectionArgs);
    }

    //Get all employees
    public ArrayList<Employee> getAllEmployees(){
        ArrayList<Employee> employeeList = new ArrayList<>();      //Select All Query
        String selectQuery = "SELECT * FROM " + DBContract.EmployeeTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.EmployeeTable._ID)));
                employee.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(
                        DBContract.EmployeeTable.COLUMN_NAME_EMAIL)));
                employee.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(
                        DBContract.EmployeeTable.COLUMN_NAME_PASSWORD)));
                employee.setFirst(cursor.getString(cursor.getColumnIndexOrThrow(
                        DBContract.EmployeeTable.COLUMN_NAME_FIRST)));
                employee.setLast(cursor.getString(cursor.getColumnIndexOrThrow(
                        DBContract.EmployeeTable.COLUMN_NAME_LAST)));
                employee.setRoleCode(cursor.getInt(cursor.getColumnIndexOrThrow(
                        DBContract.EmployeeTable.COLUMN_NAME_ROLE)));
                employee.setRoleTitle(getRoleDescription(employee.getRoleCode()));
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
        String countQuery = "SELECT  * FROM " + DBContract.EmployeeTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //updating a single employee
    public long updateEmployee(Employee employee){
        long id;
        ContentValues values = new ContentValues();
        values.put(DBContract.EmployeeTable.COLUMN_NAME_EMAIL, employee.getEmail());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_FIRST, employee.getFirst());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_LAST, employee.getLast());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_PASSWORD, employee.getPassword());
        values.put(DBContract.EmployeeTable.COLUMN_NAME_ROLE, employee.getRoleCode());

        //updating row
        id =  db.update(DBContract.EmployeeTable.TABLE_NAME,
                values,
                DBContract.EmployeeTable._ID + " = ?",
                new String[]{String.valueOf(employee.getId())});
        
        return id;
    }

    // helper for getEmployee() so it can take id or email
    public Employee getEmployeeHelper(String selection, String selectionArgs[]) {
        Cursor cursor;
        Employee employee;

        String[] projection = {
                DBContract.EmployeeTable._ID,
                DBContract.EmployeeTable.COLUMN_NAME_EMAIL,
                DBContract.EmployeeTable.COLUMN_NAME_PASSWORD,
                DBContract.EmployeeTable.COLUMN_NAME_FIRST,
                DBContract.EmployeeTable.COLUMN_NAME_LAST,
                DBContract.EmployeeTable.COLUMN_NAME_ROLE
        };
        String sortOrder = DBContract.EmployeeTable._ID + " DESC";

        cursor = db.query(
                DBContract.EmployeeTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DBContract.EmployeeTable._ID)
            );
            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.EmployeeTable.COLUMN_NAME_EMAIL)
            );
            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.EmployeeTable.COLUMN_NAME_PASSWORD)
            );
            String first = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.EmployeeTable.COLUMN_NAME_FIRST)
            );
            String last = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.EmployeeTable.COLUMN_NAME_LAST)
            );
            int roleCode = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DBContract.EmployeeTable.COLUMN_NAME_ROLE)
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
            employee.setCalendarEvents(getCalendarEvents(employee));
        }
        else
            employee = null;

        cursor.close();
        return employee;
    }

    // Get a single employee by their email, used for login
    public Employee getEmployee(String email) {
        String selection = DBContract.EmployeeTable.COLUMN_NAME_EMAIL + "=?";
        String selectionArgs[] = {
                email
        };
        return getEmployeeHelper(selection, selectionArgs);
    }

    // Get a single employee by their id
    public Employee getEmployee(long id) {
        String selection = DBContract.EmployeeTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(id)
        };
        return getEmployeeHelper(selection, selectionArgs);
    }

    // Get the word description of an employee role
    public String getRoleDescription(long roleCode) {
        String selection = DBContract.RolesTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(roleCode)
        };
        String[] projection = {
                DBContract.RolesTable._ID,
                DBContract.RolesTable.COLUMN_NAME_TITLE
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.RolesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        String title = "";

        if (cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.RolesTable.COLUMN_NAME_TITLE));
        }

        cursor.close();
        return title;
    }

    // Get list of roles.
    public ArrayList<String> getRoles() {
        ArrayList<String> roles = new ArrayList<>();      //Select All Query
        String selectQuery = "SELECT * FROM " + DBContract.RolesTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                roles.add(cursor.getString(cursor.getColumnIndex(DBContract.RolesTable.COLUMN_NAME_TITLE)));
            } while (cursor.moveToNext());
        }
        //return employee list
        cursor.close();
        return roles;
    }

    // Everything after this is from Juan

    // Inserts an Event.
    public long insertEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(DBContract.EventTable.COLUMN_NAME_EVENT_NAME, event.getName());
        values.put(DBContract.EventTable.COLUMN_NAME_HOST, event.getHost());
        values.put(DBContract.EventTable.COLUMN_NAME_LOCATION, event.getLocation());
        values.put(DBContract.EventTable.COLUMN_NAME_START, event.getStart().getTime());
        values.put(DBContract.EventTable.COLUMN_NAME_END, event.getEnd().getTime());
        values.put(DBContract.EventTable.COLUMN_NAME_MANAGER_ID, event.getManager().getId());
        event.setId(db.insert(DBContract.EventTable.TABLE_NAME, null, values));
        for (CalendarEvent calendarEvent : event.getCalendarEvents())
            insertCalendarEvent(calendarEvent);
        return event.getId();
    }

    // Delete an Event based on its id.
    public void deleteEvent(Event event){
        String selection = DBContract.EventTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(event.getId()) };
        for (CalendarEvent calendarEvent : event.getCalendarEvents())
            deleteCalendarEvent(calendarEvent);
        db.delete(DBContract.EventTable.TABLE_NAME, selection, selectionArgs);
    }

    // Gets an Event based on its id.
    public Event getEvent(long id) {
        String selection = DBContract.EventTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(id)
        };
        String[] projection = {
                DBContract.EventTable._ID,
                DBContract.EventTable.COLUMN_NAME_EVENT_NAME,
                DBContract.EventTable.COLUMN_NAME_HOST,
                DBContract.EventTable.COLUMN_NAME_LOCATION,
                DBContract.EventTable.COLUMN_NAME_START,
                DBContract.EventTable.COLUMN_NAME_END,
                DBContract.EventTable.COLUMN_NAME_MANAGER_ID
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.EventTable.TABLE_NAME,
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
            event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.EventTable._ID)));
            event.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_EVENT_NAME)));
            event.setHost(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_HOST)));
            event.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_LOCATION)));
            event.setStart(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_START))));
            event.setEnd(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_END))));
            event.setManager(getEmployee(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_MANAGER_ID))));
        }
        cursor.close();
        return event;
    }

    // Gets all Events for a manager.
    public ArrayList<Event> getEventsForManager(Employee manager) {
        String selection = DBContract.EventTable.COLUMN_NAME_MANAGER_ID + "=?";
        String selectionArgs[] = {
                Long.toString(manager.getId())
        };
        String[] projection = {
                DBContract.EventTable._ID,
                DBContract.EventTable.COLUMN_NAME_EVENT_NAME,
                DBContract.EventTable.COLUMN_NAME_HOST,
                DBContract.EventTable.COLUMN_NAME_LOCATION,
                DBContract.EventTable.COLUMN_NAME_START,
                DBContract.EventTable.COLUMN_NAME_END,
                DBContract.EventTable.COLUMN_NAME_MANAGER_ID
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.EventTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Event> events = new ArrayList<>();
        while (cursor.moveToNext()){
            Event event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.EventTable._ID)));
            event.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_EVENT_NAME)));
            event.setHost(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_HOST)));
            event.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_LOCATION)));
            event.setStart(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_START))));
            event.setEnd(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_END))));
            event.setManager(getEmployee(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventTable.COLUMN_NAME_MANAGER_ID))));
            event.setCalendarEvents(getCalendarEvents(event));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    // Gets all Events for an employee.
    public ArrayList<Event> getEventsForEmployee(Employee employee) {
        String selection = DBContract.EventMembersTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String selectionArgs[] = {
                Long.toString(employee.getId())
        };
        String[] projection = {
                DBContract.EventMembersTable._ID,
                DBContract.EventMembersTable.COLUMN_NAME_EVENT_ID,
                DBContract.EventMembersTable.COLUMN_NAME_EMPLOYEE_ID
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.EventMembersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Event> events = new ArrayList<>();
        if(cursor.moveToFirst()){
            Event event = getEvent(cursor.getLong(
                    cursor.getColumnIndex(DBContract.EventMembersTable.COLUMN_NAME_EVENT_ID)));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    // Inserts a Employee into a team.
    public long insertEventMember(Event event, Employee employee) {
        long id;
        ContentValues values = new ContentValues();
        values.put(DBContract.EventMembersTable.COLUMN_NAME_EVENT_ID, event.getId());
        values.put(DBContract.EventMembersTable.COLUMN_NAME_EMPLOYEE_ID, employee.getId());
        id = db.insert(DBContract.EventMembersTable.TABLE_NAME, null, values);
        getEventMembers(event);
        return id;
    }

    // Deletes an an Employee from a team.
    public void deleteEventMember(Event event, Employee employee){
        String selection = DBContract.EventMembersTable.COLUMN_NAME_EVENT_ID + "=?" + " AND " +
                DBContract.EventMembersTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String[] selectionArgs = {
                String.valueOf(event.getId()),
                String.valueOf(employee.getId())
        };
        db.delete(DBContract.EventMembersTable.TABLE_NAME, selection, selectionArgs);
        event.setMembers(getEventMembers(event));
    }

    // Gets a list of Employees that our on a Team by the Team's id.
    public ArrayList<Employee> getEventMembers(Event event) {
        String selection = DBContract.EventMembersTable.COLUMN_NAME_EVENT_ID + "=?";
        String selectionArgs[] = {
                Long.toString(event.getId())
        };
        String[] projection = {
                DBContract.EventMembersTable._ID,
                DBContract.EventMembersTable.COLUMN_NAME_EVENT_ID,
                DBContract.EventMembersTable.COLUMN_NAME_EMPLOYEE_ID
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.EventMembersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Employee> members = new ArrayList<>();
        while (cursor.moveToNext()) {
            long employeeId = cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.EventMembersTable.COLUMN_NAME_EMPLOYEE_ID));
            members.add(getEmployee(employeeId));
        }
        cursor.close();
        event.setMembers(members);
        return members;
    }

    // Inserts an Event.
    public long insertTeam(Team team) {
        ContentValues values = new ContentValues();
        values.put(DBContract.TeamTable.COLUMN_NAME_EVENT_ID, team.getEvent().getId());
        values.put(DBContract.TeamTable.COLUMN_NAME_SUPERVISOR_ID, team.getSupervisor().getId());
        values.put(DBContract.TeamTable.COLUMN_NAME_TEAM_NAME, team.getName());
        values.put(DBContract.TeamTable.COLUMN_NAME_DUTIES, team.getDuties());
        team.setId(db.insert(DBContract.TeamTable.TABLE_NAME, null, values));
        for (Employee member : team.getMembers())
            insertTeamMember(team, member);
        return team.getId();
    }

    // Delete an Event based on its id.
    public void deleteTeam(Team team){
        String selection = DBContract.TeamTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(team.getId()) };
        for (Employee member : team.getMembers())
            deleteTeamMember(team, member);
        db.delete(DBContract.TeamTable.TABLE_NAME, selection, selectionArgs);
    }

    // Gets an Event based on its id.
    public Team getTeam(long id) {
        String selection = DBContract.TeamTable._ID + "=?";
        String selectionArgs[] = {
                Long.toString(id)
        };
        String[] projection = {
                DBContract.TeamTable._ID,
                DBContract.TeamTable.COLUMN_NAME_EVENT_ID,
                DBContract.TeamTable.COLUMN_NAME_SUPERVISOR_ID,
                DBContract.TeamTable.COLUMN_NAME_TEAM_NAME,
                DBContract.TeamTable.COLUMN_NAME_DUTIES,
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.TeamTable.TABLE_NAME,
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
            team.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.TeamTable._ID)));
            team.setEvent(getEvent(cursor.getInt(cursor.getColumnIndexOrThrow(
                    DBContract.TeamTable.COLUMN_NAME_EVENT_ID))));
            team.setSupervisor(getEmployee(cursor.getInt(cursor.getColumnIndexOrThrow(
                    DBContract.TeamTable.COLUMN_NAME_SUPERVISOR_ID))));
            team.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.TeamTable.COLUMN_NAME_TEAM_NAME)));
            team.setDuties(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.TeamTable.COLUMN_NAME_DUTIES)));
            getTeamMembers(team);
        }
        cursor.close();
        return team;
    }

    // Inserts a Employee into a team.
    public long insertTeamMember(Team team, Employee employee) {
        long id;
        ContentValues values = new ContentValues();
        values.put(DBContract.TeamMembersTable.COLUMN_NAME_TEAM_ID, team.getId());
        values.put(DBContract.TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID, employee.getId());
        id =  db.insert(DBContract.TeamMembersTable.TABLE_NAME, null, values);
        getTeamMembers(team);
        return id;
    }

    // Deletes an an Employee from a team.
    public void deleteTeamMember(Team team, Employee employee){
        String selection = DBContract.TeamMembersTable.COLUMN_NAME_TEAM_ID + "=?" + " AND " +
                DBContract.TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String[] selectionArgs = {
                String.valueOf(team.getId()),
                String.valueOf(employee.getId())
        };
        db.delete(DBContract.TeamMembersTable.TABLE_NAME, selection, selectionArgs);
        team.setMembers(getTeamMembers(team));
    }

    // Gets a list of Employees that our on a Team by the Team's id.
    public ArrayList<Employee> getTeamMembers(Team team) {
        String selection = DBContract.TeamMembersTable.COLUMN_NAME_TEAM_ID + "=?";
        String selectionArgs[] = {
                Long.toString(team.getId())
        };
        String[] projection = {
                DBContract.TeamMembersTable._ID,
                DBContract.TeamMembersTable.COLUMN_NAME_TEAM_ID,
                DBContract.TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID
        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.TeamMembersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Employee> members = new ArrayList<>();
        while (cursor.moveToNext()) {
            long employeeId = cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID));
            members.add(getEmployee(employeeId));
        }
        cursor.close();
        team.setMembers(members);
        return members;
    }

    // Insert an individual CalendarEvent.
    public long insertCalendarEvent(CalendarEvent calendarEvent) {
        ContentValues values = new ContentValues();
        values.put(DBContract.CalendarTable.COLUMN_NAME_DESCRIPTION, calendarEvent.getDescription());
        values.put(DBContract.CalendarTable.COLUMN_NAME_START, calendarEvent.getStart().getTime());
        values.put(DBContract.CalendarTable.COLUMN_NAME_END, calendarEvent.getEnd().getTime());
        if (calendarEvent.getEmployee() != null)
            values.put(DBContract.CalendarTable.COLUMN_NAME_EMPLOYEE_ID, calendarEvent.getEmployee().getId());
        else
            values.put(DBContract.CalendarTable.COLUMN_NAME_EVENT_ID, calendarEvent.getEvent().getId());
        calendarEvent.setId(db.insert(DBContract.CalendarTable.TABLE_NAME, null, values));
        if (calendarEvent.getEmployee() != null)
            calendarEvent.getEmployee().setCalendarEvents(getCalendarEvents(
                    calendarEvent.getEmployee()));
        else
            calendarEvent.getEvent().setCalendarEvents(getCalendarEvents(
                    calendarEvent.getEvent()));
        return calendarEvent.getId();
    }

    // Delete an individual CalendarEvent.
    public void deleteCalendarEvent(CalendarEvent calendarEvent){
        String selection = DBContract.CalendarTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(calendarEvent.getId()) };
        db.delete(DBContract.CalendarTable.TABLE_NAME, selection, selectionArgs);
        if (calendarEvent.getEmployee() != null)
            calendarEvent.getEmployee().setCalendarEvents(getCalendarEvents(
                    calendarEvent.getEmployee()));
        else
            calendarEvent.getEvent().setCalendarEvents(getCalendarEvents(
                    calendarEvent.getEvent()));
    }

    // Get all CalendarEvents for an Employee.
    public ArrayList<CalendarEvent> getCalendarEvents(Employee employee) {
        String selection = DBContract.CalendarTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String selectionArgs[] = {
                Long.toString(employee.getId())
        };
        ArrayList<CalendarEvent> calendarEvents = getCalendarEventsHelper(selection, selectionArgs);
        employee.setCalendarEvents(calendarEvents);
        return calendarEvents;
    }

    // Get all CalendarEvents for an Event.
    public ArrayList<CalendarEvent> getCalendarEvents(Event event) {
        String selection = DBContract.CalendarTable.COLUMN_NAME_EVENT_ID + "=?";
        String selectionArgs[] = {
                Long.toString(event.getId())
        };
        ArrayList<CalendarEvent> calendarEvents = getCalendarEventsHelper(selection, selectionArgs);
        event.setCalendarEvents(calendarEvents);
        return calendarEvents;
    }

    // Helper for getting CalendarEvents.
    public ArrayList<CalendarEvent> getCalendarEventsHelper(String selection, String selectionArgs[]) {
        String[] projection = {
                DBContract.CalendarTable._ID,
                DBContract.CalendarTable.COLUMN_NAME_DESCRIPTION,
                DBContract.CalendarTable.COLUMN_NAME_START,
                DBContract.CalendarTable.COLUMN_NAME_END

        };
        String sortOrder = DBContract.RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                DBContract.CalendarTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
        while (cursor.moveToNext()) {
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(
                    DBContract.CalendarTable.COLUMN_NAME_DESCRIPTION)));
            calendarEvent.setStart(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.CalendarTable.COLUMN_NAME_START))));
            calendarEvent.setEnd(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    DBContract.CalendarTable.COLUMN_NAME_END))));
            calendarEvents.add(calendarEvent);
        }
        cursor.close();
        return calendarEvents;
    }
}
