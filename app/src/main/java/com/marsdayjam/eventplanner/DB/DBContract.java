package com.marsdayjam.eventplanner.DB;

import android.provider.BaseColumns;

public final class DBContract {

    public DBContract() {}

    public static abstract class EmployeeTable implements BaseColumns {
        public static final String TABLE_NAME = "employee";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_FIRST = "first";
        public static final String COLUMN_NAME_LAST = "last";
        public static final String COLUMN_NAME_ROLE = "role";
    }

    public static abstract class RolesTable implements BaseColumns {
        public static final String TABLE_NAME = "roles";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_EDIT_EMPLOYEES = "edit_employees";
        public static final int HR = 1;
        public static final int MG = 2;
        public static final int GE = 3;
    }

    // Everything after this is from Juan

    // Table for planned events.
    public static abstract class EventTable implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENT_NAME = "name";
        public static final String COLUMN_NAME_HOST = "host";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_MANAGER_ID= "manger_id";
    }

    // Table for associating employees to events.
    public static abstract class EventMembersTable implements BaseColumns {
        public static final String TABLE_NAME = "event_members";
        public static final String COLUMN_NAME_EVENT_ID = "event_id";
        public static final String COLUMN_NAME_EMPLOYEE_ID = "employee_id";
    }

    // Table for teams.
    public static abstract class TeamTable implements BaseColumns {
        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_NAME_EVENT_ID = "event_id";
        public static final String COLUMN_NAME_SUPERVISOR_ID = "supervisor_id";
        public static final String COLUMN_NAME_TEAM_NAME = "name";
        public static final String COLUMN_NAME_DUTIES = "duties";
    }

    // Table for associating employees to teams.
    public static abstract class TeamMembersTable implements BaseColumns {
        public static final String TABLE_NAME = "team_members";
        public static final String COLUMN_NAME_TEAM_ID = "team_id";
        public static final String COLUMN_NAME_EMPLOYEE_ID = "employee_id";
    }

    // Table for all calendar events. Holds both employee & event calendar data.
    public static abstract class CalendarTable implements BaseColumns {
        public static final String TABLE_NAME = "calendar_events";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_EMPLOYEE_ID = "employee_id";
        public static final String COLUMN_NAME_EVENT_ID = "event_id";
    }
}
