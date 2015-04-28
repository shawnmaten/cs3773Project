package com.marsdayjam.eventplanner;

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
        //leave unimplemented until decide on calendarTable schema
        //will be name of corresponding CalendarTable (must be unique)
        //public static final String COLUMN_NAME_CALENDARNAME = "calendar";
    }

    public static abstract class RolesTable implements BaseColumns {
        public static final String TABLE_NAME = "roles";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_EDIT_EMPLOYEES = "edit_employees";
    }

    /* Table for event information and reference to the corresponding calendar and
       team databases.
     */
    public static abstract class EventTable implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENTNAME= "eventName";
        public static final String COLUMN_NAME_SDATE = "startDate";
        public static final String COLUMN_NAME_HOST = "host";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ETIME = "endTime";
        public static final String COLUMN_NAME_EDATE = "endDate";
        public static final String COLUMN_NAME_TEAMNAME= "teamName";
        //will be name of corresponding CalendarTable (must be unique)
        public static final String COLUMN_NAME_CALENDARNAME = "calendar";
    }

    /*List of all Calendars including both personal and event for easier search
      of existing calendars for view or delete.
     */
    public static abstract class CalendarList implements BaseColumns {
        public static final String TABLE_NAME = "calendarList";
        public static final String COLUMN_NAME_CALENDARNAME = "calendarName";
    }

    /* Table for storing events in CalendarOptions.
       Personal CalendarOptions database created on employee creation and deleted on employee deletion.
       Event CalendarOptions database created on creation of an event and deleted on event canceled or end
       date reached.
     */
    //Definitely needs editing
    public static abstract class CalendarTable implements BaseColumns {
        public static final String TABLE_NAME = "calendarName";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_STARTH = "startHour";
        public static final String COLUMN_NAME_STARTM = "startMinute";
        public static final String COLUMN_NAME_ENDH = "endHour";
        public static final String COLUMN_NAME_ENDM = "endMinute";
        public static final String COLUMN_NAME_EVENT = "event";
    }

    /* Table for storing Teams by unique TeamName and listing members for the team
    */
    public static abstract class TeamTable implements BaseColumns {
        public static final String TABLE_NAME = "TeamName";
        public static final String COLUMN_NAME_SUPERVISOR = "supervisor";
        public static final String COLUMN_NAME_DUTIES = "duties";
        public static final String COLUMN_NAME_MEMBERS = "members";
    }

    /* Table for event information and reference to the corresponding calendar and
       team databases.
     */
    public static abstract class EventTable implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENTNAME= "eventName";
        public static final String COLUMN_NAME_SDATE = "startDate";
        public static final String COLUMN_NAME_HOST = "host";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ETIME = "endTime";
        public static final String COLUMN_NAME_EDATE = "endDate";
        public static final String COLUMN_NAME_TEAMNAME= "teamName";
        //will be name of corresponding CalendarTable (must be unique)
        public static final String COLUMN_NAME_CALENDARNAME = "calendar";
    }

    /*List of all Calendars including both personal and event for easier search
      of existing calendars for view or delete.
     */
    public static abstract class CalendarList implements BaseColumns {
        public static final String TABLE_NAME = "calendarList";
        public static final String COLUMN_NAME_CALENDARNAME = "calendarName";
    }

    /* Table for storing events in CalendarOptions.
       Personal CalendarOptions database created on employee creation and deleted on employee deletion.
       Event CalendarOptions database created on creation of an event and deleted on event canceled or end
       date reached.
     */
    //Definitely needs editing
    public static abstract class CalendarTable implements BaseColumns {
        public static final String TABLE_NAME = "calendarName";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_STARTH = "startHour";
        public static final String COLUMN_NAME_STARTM = "startMinute";
        public static final String COLUMN_NAME_ENDH = "endHour";
        public static final String COLUMN_NAME_ENDM = "endMinute";
        public static final String COLUMN_NAME_EVENT = "event";
    }

    /* Table for storing Teams by unique TeamName and listing members for the team
    */
    public static abstract class TeamTable implements BaseColumns {
        public static final String TABLE_NAME = "TeamName";
        public static final String COLUMN_NAME_SUPERVISOR = "supervisor";
        public static final String COLUMN_NAME_DUTIES = "duties";
        public static final String COLUMN_NAME_MEMBERS = "members";
    }
}
