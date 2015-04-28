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
        //public static final String COLUMN_NAME_CALENDARNAME = "calendar";
    }

    public static abstract class RolesTable implements BaseColumns {
        public static final String TABLE_NAME = "roles";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final int ROLE_HR = 0;
        public static final int ROLE_MG = 1;
        public static final int ROLE_GE = 2;
    }

    /* Table for event information and reference to the corresponding calendar and
       team databases.
     */
    public static abstract class EventTable implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_SDATE = "startDate";
        public static final String COLUMN_NAME_EDATE = "endDate";
        public static final String COLUMN_NAME_TEAMS= "teams";
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
    public static abstract class CalendarTable implements BaseColumns {
        public static String CalendarName = "default";
        public static final String TABLE_NAME = CalendarName;
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_EVENT = "event";

        public void setCalendarName(String name) {
            CalendarName = "CalendarOptions: " + name;
        }
    }
}
