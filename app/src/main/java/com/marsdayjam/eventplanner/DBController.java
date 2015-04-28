package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;
import com.marsdayjam.eventplanner.DBContract.CalendarList;

import java.util.ArrayList;
import java.util.List;

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
    public long insertEmployee(String email, String password, String first, String last, int role) {
        long id;
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, email);
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, password);
        values.put(EmployeeTable.COLUMN_NAME_FIRST, first);
        values.put(EmployeeTable.COLUMN_NAME_LAST, last);
        values.put(EmployeeTable.COLUMN_NAME_ROLE, role);
        //values.put(EmployeeTable.COLUMN_NAME_CALENDARNAME, calendarName);
        id = db.insert(EmployeeTable.TABLE_NAME, null, values);
        //DBControllerD.insertCalendar(calendarName);
        return id;
    }

    /*THIS IS TO DELETE EMPLOYEES TO THE DATABASE*/
    public void deleteEmployee(long id){
        //First we have to tell it what Column we are going to find employee by
        String selection = EmployeeTable._ID + " LIKE ?";

        /*
        String[] columns = {EmployeeTable._ID, EmployeeTable.COLUMN_NAME_CALENDARNAME};
        Cursor cursor = db.query(EmployeeTable.TABLE_NAME, columns, EmployeeTable_ID+
                " = '"+id+"'", null, null, null, null);
        cursor.moveToNext();
        int index = cursor.getColumnIndex(EmployeeTable.COLUMN_NAME_CALENDARNAME);
        String calendarName = cursor.getString(index);
        DBControllerD.deleteCalendar(calendarName);
        */

        //Then we have to give it the value to match the employee by in the column
        String[] selectionArgs = { String.valueOf(id) };
        // Now put that plus the table name into the delete function to remove employee
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
}
