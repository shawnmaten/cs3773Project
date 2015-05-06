package com.marsdayjam.eventplanner.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marsdayjam.eventplanner.DB.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DB.DBContract.RolesTable;
import com.marsdayjam.eventplanner.DB.DBContract.CalendarTable;
import com.marsdayjam.eventplanner.DB.DBContract.EventTable;
import com.marsdayjam.eventplanner.DB.DBContract.EventMembersTable;
import com.marsdayjam.eventplanner.DB.DBContract.TeamTable;
import com.marsdayjam.eventplanner.DB.DBContract.TeamMembersTable;

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

    private static final String CREATE_EVENT =
            "CREATE TABLE " + EventTable.TABLE_NAME + LP +
                    EventTable._ID + " INTEGER PRIMARY KEY" + CM +
                    EventTable.COLUMN_NAME_EVENT_NAME + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_HOST + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_LOCATION + TEXT_TYPE + CM +
                    EventTable.COLUMN_NAME_START + INT_TYPE + CM +
                    EventTable.COLUMN_NAME_END + INT_TYPE + CM +
                    EventTable.COLUMN_NAME_MANAGER_ID + TEXT_TYPE + CM +
                    " FOREIGN KEY " + LP + EventTable.COLUMN_NAME_MANAGER_ID + RP + " REFERENCES "
                        + EmployeeTable.TABLE_NAME + LP + EmployeeTable._ID + RP + RP;

    private static final String CREATE_EVENT_MEMBERS =
            "CREATE TABLE " + EventMembersTable.TABLE_NAME + LP +
                    EventMembersTable._ID + " INTEGER PRIMARY KEY" + CM +
                    EventMembersTable.COLUMN_NAME_EVENT_ID + INT_TYPE + CM +
                    EventMembersTable.COLUMN_NAME_EMPLOYEE_ID + INT_TYPE + CM +
                    " FOREIGN KEY " + LP + EventMembersTable.COLUMN_NAME_EVENT_ID + RP +
                    " REFERENCES " + EventTable.TABLE_NAME + LP + EventTable._ID + RP +
                    " FOREIGN KEY " + LP + EventMembersTable.COLUMN_NAME_EMPLOYEE_ID + RP +
                    " REFERENCES " + EmployeeTable.TABLE_NAME + LP + EmployeeTable._ID + RP +
                    RP;

    private static final String CREATE_TEAM =
            "CREATE TABLE " + TeamTable.TABLE_NAME + LP +
                    TeamTable._ID + " INTEGER PRIMARY KEY" + CM +
                    TeamTable.COLUMN_NAME_EVENT_ID + INT_TYPE + CM +
                    TeamTable.COLUMN_NAME_SUPERVISOR_ID + INT_TYPE + CM +
                    TeamTable.COLUMN_NAME_TEAM_NAME + TEXT_TYPE + CM +
                    TeamTable.COLUMN_NAME_DUTIES + TEXT_TYPE + CM +
                    " FOREIGN KEY " + LP + TeamTable.COLUMN_NAME_EVENT_ID + RP + " REFERENCES "
                        + EventTable.TABLE_NAME + LP + EventTable._ID + RP +
                    " FOREIGN KEY " + LP + TeamTable.COLUMN_NAME_SUPERVISOR_ID + RP + " REFERENCES "
                        + EmployeeTable.TABLE_NAME + LP + EmployeeTable._ID + RP + RP;

    private static final String CREATE_TEAM_MEMBERS =
            "CREATE TABLE " + TeamMembersTable.TABLE_NAME + LP +
                    TeamMembersTable._ID + " INTEGER PRIMARY KEY" + CM +
                    TeamMembersTable.COLUMN_NAME_TEAM_ID + INT_TYPE + CM +
                    TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID + INT_TYPE + CM +
                    " FOREIGN KEY " + LP + TeamMembersTable.COLUMN_NAME_TEAM_ID + RP + 
                        " REFERENCES " + TeamTable.TABLE_NAME + LP + TeamTable._ID + RP +
                    " FOREIGN KEY " + LP + TeamMembersTable.COLUMN_NAME_EMPLOYEE_ID + RP + 
                        " REFERENCES " + EmployeeTable.TABLE_NAME + LP + EmployeeTable._ID + RP + 
                    RP;

    private static final String CREATE_CALENDAR =
            "CREATE TABLE " + CalendarTable.TABLE_NAME + LP +
                    CalendarTable._ID + " INTEGER PRIMARY KEY" + CM +
                    CalendarTable.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_START + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_END + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_EMPLOYEE_ID + INT_TYPE + CM +
                    CalendarTable.COLUMN_NAME_EVENT_ID + INT_TYPE + CM +
                    " FOREIGN KEY " + LP + CalendarTable.COLUMN_NAME_EMPLOYEE_ID + RP +
                    " REFERENCES " + EmployeeTable.TABLE_NAME + LP + EmployeeTable._ID + RP +
                    " FOREIGN KEY " + LP + CalendarTable.COLUMN_NAME_EVENT_ID + RP +
                    " REFERENCES " + EventTable.TABLE_NAME + LP + EventTable._ID + RP +
                    RP;

    private static final String SQL_DELETE_ENTRIES =
            " DROP TABLE IF EXISTS " + EmployeeTable.TABLE_NAME +
                    " DROP TABLE IF EXISTS " + RolesTable.TABLE_NAME +
                    " DROP TABLE IF EXISTS " + EventTable.TABLE_NAME +
                    " DROP TABLE IF EXISTS " + TeamTable.TABLE_NAME +
                    " DROP TABLE IF EXISTS " + TeamMembersTable.TABLE_NAME +
                    " DROP TABLE IF EXISTS " + CalendarTable.TABLE_NAME;

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
        db.execSQL(CREATE_EVENT);
        db.execSQL(CREATE_EVENT_MEMBERS);
        db.execSQL(CREATE_TEAM);
        db.execSQL(CREATE_TEAM_MEMBERS);
        db.execSQL(CREATE_CALENDAR);

        values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, "celia.hodes@eventplanners.com");
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, "celiaHodes1234");
        values.put(EmployeeTable.COLUMN_NAME_FIRST, "Celia");
        values.put(EmployeeTable.COLUMN_NAME_LAST, "Hodes");
        values.put(EmployeeTable.COLUMN_NAME_ROLE, 1);
        db.insert(DBContract.EmployeeTable.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, "nancy.botwin@eventplanners.com");
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, "nancyBotwin1234");
        values.put(EmployeeTable.COLUMN_NAME_FIRST, "Nancy");
        values.put(EmployeeTable.COLUMN_NAME_LAST, "Botwin");
        values.put(EmployeeTable.COLUMN_NAME_ROLE, 2);
        db.insert(DBContract.EmployeeTable.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, "andy.botwin@eventplanners.com");
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, "andyBotwin1234");
        values.put(EmployeeTable.COLUMN_NAME_FIRST, "Andy");
        values.put(EmployeeTable.COLUMN_NAME_LAST, "Botwin");
        values.put(EmployeeTable.COLUMN_NAME_ROLE, 3);
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
