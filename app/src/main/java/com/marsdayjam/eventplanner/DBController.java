package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.RolesTable;
import com.marsdayjam.eventplanner.DBContract.EventTable;
import com.marsdayjam.eventplanner.DBContract.EventMembersTable;
import com.marsdayjam.eventplanner.DBContract.TeamTable;
import com.marsdayjam.eventplanner.DBContract.TeamMembersTable;
import com.marsdayjam.eventplanner.DBContract.CalendarTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, employee.getEmail());
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, employee.getPassword());
        values.put(EmployeeTable.COLUMN_NAME_FIRST, employee.getFirst());
        values.put(EmployeeTable.COLUMN_NAME_LAST, employee.getLast());
        values.put(EmployeeTable.COLUMN_NAME_ROLE, employee.getRoleCode());
        employee.setId(db.insert(EmployeeTable.TABLE_NAME, null, values));
        for (CalendarEvent calendarEvent : employee.getCalendarEvents())
            insertCalendarEvent(calendarEvent);
        return employee.getId();
    }

    /*THIS IS TO DELETE EMPLOYEES TO THE DATABASE*/
    public void deleteEmployee(Employee employee){
        String selection = EmployeeTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(employee.getId()) };
        for (CalendarEvent calendarEvent : employee.getCalendarEvents())
            deleteCalendarEvent(calendarEvent);
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
                employee.setCalendarEvents(getCalendarEvents(employee));
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
            employee.setCalendarEvents(getCalendarEvents(employee));
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
        event.setId(db.insert(EventTable.TABLE_NAME, null, values));
        for (CalendarEvent calendarEvent : event.getCalendarEvents())
            insertCalendarEvent(calendarEvent);
        return event.getId();
    }

    // Delete an Event based on its id.
    public void deleteEvent(Event event){
        String selection = EventTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(event.getId()) };
        for (CalendarEvent calendarEvent : event.getCalendarEvents())
            deleteCalendarEvent(calendarEvent);
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

    // Gets all Events for a manager.
    public ArrayList<Event> getEvents(Employee manager) {
        String selection = EventTable.COLUMN_NAME_MANAGER_ID + "=?";
        String selectionArgs[] = {
                Long.toString(manager.getId())
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
        ArrayList<Event> events = new ArrayList<>();
        if(cursor.moveToFirst()){
            Event event = new Event();
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
            event.setCalendarEvents(getCalendarEvents(event));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    // Inserts a Employee into a team.
    public long insertEventMember(Event event, Employee employee) {
        long id;
        ContentValues values = new ContentValues();
        values.put(EventMembersTable.COLUMN_NAME_EVENT_ID, event.getId());
        values.put(EventMembersTable.COLUMN_NAME_EMPLOYEE_ID, employee.getId());
        id = db.insert(EventMembersTable.TABLE_NAME, null, values);
        getEventMembers(event);
        return id;
    }

    // Deletes an an Employee from a team.
    public void deleteEventMember(Event event, Employee employee){
        String selection = EventMembersTable.COLUMN_NAME_EVENT_ID + "=?" + " AND " +
                EventMembersTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String[] selectionArgs = {
                String.valueOf(event.getId()),
                String.valueOf(employee.getId())
        };
        db.delete(EventMembersTable.TABLE_NAME, selection, selectionArgs);
        event.setMembers(getEventMembers(event));
    }

    // Gets a list of Employees that our on a Team by the Team's id.
    public ArrayList<Employee> getEventMembers(Event event) {
        String selection = EventMembersTable.COLUMN_NAME_EVENT_ID + "=?";
        String selectionArgs[] = {
                Long.toString(event.getId())
        };
        String[] projection = {
                EventMembersTable._ID,
                EventMembersTable.COLUMN_NAME_EVENT_ID,
                EventMembersTable.COLUMN_NAME_EMPLOYEE_ID
        };
        String sortOrder = RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                EventMembersTable.TABLE_NAME,
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
                    EventMembersTable.COLUMN_NAME_EMPLOYEE_ID));
            members.add(getEmployee(employeeId));
        }
        cursor.close();
        event.setMembers(members);
        return members;
    }

    // Inserts an Event.
    public long insertTeam(Team team) {
        ContentValues values = new ContentValues();
        values.put(TeamTable.COLUMN_NAME_EVENT_ID, team.getEvent().getId());
        values.put(TeamTable.COLUMN_NAME_SUPERVISOR_ID, team.getSupervisor().getId());
        values.put(TeamTable.COLUMN_NAME_TEAM_NAME, team.getName());
        values.put(TeamTable.COLUMN_NAME_DUTIES, team.getDuties());
        team.setId(db.insert(TeamTable.TABLE_NAME, null, values));
        for (Employee member : team.getMembers())
            insertTeamMember(team, member);
        return team.getId();
    }

    // Delete an Event based on its id.
    public void deleteTeam(Team team){
        String selection = TeamTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(team.getId()) };
        for (Employee member : team.getMembers())
            deleteTeamMember(team, member);
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
            getTeamMembers(team);
        }
        cursor.close();
        return team;
    }

    // Inserts a Employee into a team.
    public long insertTeamMember(Team team, Employee employee) {
        long id;
        ContentValues values = new ContentValues();
        values.put(TeamMembersTable.COLUMN_NAME_TEAM_ID, team.getId());
        values.put(TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID, employee.getId());
        id =  db.insert(TeamMembersTable.TABLE_NAME, null, values);
        getTeamMembers(team);
        return id;
    }

    // Deletes an an Employee from a team.
    public void deleteTeamMember(Team team, Employee employee){
        String selection = TeamMembersTable.COLUMN_NAME_TEAM_ID + "=?" + " AND " +
                TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String[] selectionArgs = {
                String.valueOf(team.getId()),
                String.valueOf(employee.getId())
        };
        db.delete(TeamMembersTable.TABLE_NAME, selection, selectionArgs);
        team.setMembers(getTeamMembers(team));
    }

    // Gets a list of Employees that our on a Team by the Team's id.
    public ArrayList<Employee> getTeamMembers(Team team) {
        String selection = TeamMembersTable.COLUMN_NAME_TEAM_ID + "=?";
        String selectionArgs[] = {
                Long.toString(team.getId())
        };
        String[] projection = {
                TeamMembersTable._ID,
                TeamMembersTable.COLUMN_NAME_TEAM_ID,
                TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID
        };
        String sortOrder = RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                TeamMembersTable.TABLE_NAME,
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
                    TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID));
            members.add(getEmployee(employeeId));
        }
        cursor.close();
        team.setMembers(members);
        return members;
    }

    // Insert an individual CalendarEvent.
    public long insertCalendarEvent(CalendarEvent calendarEvent) {
        ContentValues values = new ContentValues();
        values.put(CalendarTable.COLUMN_NAME_DESCRIPTION, calendarEvent.getDescription());
        values.put(CalendarTable.COLUMN_NAME_START, calendarEvent.getStart().getTime());
        values.put(CalendarTable.COLUMN_NAME_END, calendarEvent.getEnd().getTime());
        if (calendarEvent.getEmployee() != null)
            values.put(CalendarTable.COLUMN_NAME_EMPLOYEE_ID, calendarEvent.getEmployee().getId());
        else
            values.put(CalendarTable.COLUMN_NAME_EVENT_ID, calendarEvent.getEvent().getId());
        calendarEvent.setId(db.insert(CalendarTable.TABLE_NAME, null, values));
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
        String selection = CalendarTable._ID + "=?";
        String[] selectionArgs = { String.valueOf(calendarEvent.getId()) };
        db.delete(CalendarTable.TABLE_NAME, selection, selectionArgs);
        if (calendarEvent.getEmployee() != null)
            calendarEvent.getEmployee().setCalendarEvents(getCalendarEvents(
                    calendarEvent.getEmployee()));
        else
            calendarEvent.getEvent().setCalendarEvents(getCalendarEvents(
                    calendarEvent.getEvent()));
    }

    // Get all CalendarEvents for an Employee.
    public ArrayList<CalendarEvent> getCalendarEvents(Employee employee) {
        String selection = CalendarTable.COLUMN_NAME_EMPLOYEE_ID + "=?";
        String selectionArgs[] = {
                Long.toString(employee.getId())
        };
        ArrayList<CalendarEvent> calendarEvents = getCalendarEventsHelper(selection, selectionArgs);
        employee.setCalendarEvents(calendarEvents);
        return calendarEvents;
    }

    // Get all CalendarEvents for an Event.
    public ArrayList<CalendarEvent> getCalendarEvents(Event event) {
        String selection = CalendarTable.COLUMN_NAME_EVENT_ID + "=?";
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
                CalendarTable._ID,
                CalendarTable.COLUMN_NAME_DESCRIPTION,
                CalendarTable.COLUMN_NAME_START,
                CalendarTable.COLUMN_NAME_END

        };
        String sortOrder = RolesTable._ID + " DESC";
        Cursor cursor = db.query(
                CalendarTable.TABLE_NAME,
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
                    CalendarTable.COLUMN_NAME_DESCRIPTION)));
            calendarEvent.setStart(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    CalendarTable.COLUMN_NAME_START))));
            calendarEvent.setEnd(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(
                    CalendarTable.COLUMN_NAME_END))));
            calendarEvents.add(calendarEvent);
        }
        cursor.close();
        return calendarEvents;
    }
}
