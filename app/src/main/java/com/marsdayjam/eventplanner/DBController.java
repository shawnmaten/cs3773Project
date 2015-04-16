package com.marsdayjam.eventplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marsdayjam.eventplanner.DBContract.EmployeeTable;

import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static DBController ourInstance;
    DBHelper dbHelper = null;
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

    /*THIS IS TO ADD EMPLOYEES TO THE DATABASE*/
    public long insertEmployee(String email, String password, String first, String last,int role) {
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, email);
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, password);
        values.put(EmployeeTable.COLUMN_NAME_FIRST, first);
        values.put(EmployeeTable.COLUMN_NAME_LAST, last);
        values.put(EmployeeTable.COLUMN_NAME_ROLE, role);
        return db.insert(EmployeeTable.TABLE_NAME, null, values);
    }

    /*THIS IS TO DELETE EMPLOYEES TO THE DATABASE*/
    public void deleteEmployee(int id){
        //First we have to tell it what Column we are going to find employee by
        String selection = EmployeeTable._ID + " LIKE ?";
        //Then we have to give it the value to match the employee by in the column
        String[] selectionArgs = { String.valueOf(id) };
        // Now put that plus the table name into the delete function to remove employee
        db.delete(EmployeeTable.TABLE_NAME, selection, selectionArgs);
    }

    //Get all employees
    public List<Employee> getAllEmployees(){
        List<Employee> employeeList = null;
        employeeList = new ArrayList<Employee>();      //Select All Query
      String selectQuery = "SELECT * FROM " + EmployeeTable.TABLE_NAME;
      Cursor cursor = db.rawQuery(selectQuery, null);

      //looping through all rows and adding to list
      if(cursor.moveToFirst()){
          do{
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
      return employeeList;
    }

    //Getting total employee count
    public int getEmployeeCount(){
        String countQuery = "SELECT  * FROM " + EmployeeTable.TABLE_NAME;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //updating a single employee
    public int updateEmployee(Employee employee){
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.COLUMN_NAME_EMAIL, employee.getEmail());
        values.put(EmployeeTable.COLUMN_NAME_FIRST, employee.getFirst());
        values.put(EmployeeTable.COLUMN_NAME_LAST, employee.getLast());
        values.put(EmployeeTable.COLUMN_NAME_PASSWORD, employee.getPassword());
        values.put(EmployeeTable.COLUMN_NAME_ROLE, employee.getRoleTitle());

        //updating row
        return db.update(EmployeeTable.TABLE_NAME,
                values,
                EmployeeTable._ID + " = ?",
                new String[]{String.valueOf(employee.getId())});
    }

    //Get a single employee
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


}
