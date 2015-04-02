package com.marsdayjam.eventplanner;

import android.provider.BaseColumns;

public final class DBContract {

    public DBContract() {}

    public static abstract class LoginTable implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }
}
