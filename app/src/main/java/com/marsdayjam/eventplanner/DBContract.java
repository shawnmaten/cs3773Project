package com.marsdayjam.eventplanner;

import android.provider.BaseColumns;

public final class DBContract {

    public DBContract() {}

    public static abstract class EmployeeTable implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ROLE = "role";
    }

    public static abstract class RolesTable implements BaseColumns {
        public static final String TABLE_NAME = "roles";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final int ROLE_HR = 0;
        public static final int ROLE_MG = 1;
        public static final int ROLE_GE = 2;
    }
}
