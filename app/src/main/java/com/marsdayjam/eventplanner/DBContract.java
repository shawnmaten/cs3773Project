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
    }

    public static abstract class RolesTable implements BaseColumns {
        public static final String TABLE_NAME = "roles";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_EDIT_EMPLOYEES = "edit_employees";
    }
}
