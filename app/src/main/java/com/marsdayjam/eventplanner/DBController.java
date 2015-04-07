package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;

public class DBController {
    private static DBController ourInstance;

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
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertLogin(String email, String password, int role) {
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, email);
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, password);
        values.put(EmployeeTable.COLUMN_NAME_ROLE, role);
        return db.insert(EmployeeTable.TABLE_NAME, null, values);
    }

    public Employee getEmployee(String email) {
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
        String selection = EmployeeTable.COLUMN_NAME_EMAIL + "=?";
        String selectionArgs[] = {
                email
        };

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
            employee.setPassword(password);
            employee.setFirst(first);
            employee.setLast(last);
            employee.setRoleCode(roleCode);
        }
        else
            employee = null;

        cursor.close();
        return employee;
    }

}
