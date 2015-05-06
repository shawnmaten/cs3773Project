package com.marsdayjam.eventplanner;

import android.test.AndroidTestCase;

import com.marsdayjam.eventplanner.DB.DBContract;
import com.marsdayjam.eventplanner.DB.DBController;
import com.marsdayjam.eventplanner.Employee.Employee;

import java.util.List;

public class DBControllerTest extends AndroidTestCase {
    public static long testId;
    public static final String testEmail = "unit@test.com";
    public static final String testPassword = "unitTestPassword";
    public static final String testFirst = "Unit";
    public static final String testLast = "Last";
    public static final int testRole = DBContract.RolesTable.ROLE_GE;

    private static DBController dbController;

    public void testGetInstance() throws Exception {
        dbController = DBController.getInstance(getContext());
        if (dbController == null)
            throw new Exception("Method getInstance should not return null.");
    }

    public void testInsertEmployee() throws Exception {
        if ((testId = dbController.insertEmployee(
                testEmail,
                testPassword,
                testFirst,
                testLast,
                testRole)) < 1)
            throw new Exception("Method insertEmployee() failed.");
    }

    public void testDeleteEmployee() throws Exception {
        long id = dbController.insertEmployee(
                testEmail,
                testPassword,
                testFirst,
                testLast,
                testRole);
        dbController.deleteEmployee(id);
        if (dbController.getEmployee(id) != null)
            throw new Exception("Method deleteEmployee() failed.");
    }

    public void testGetAllEmployees() throws Exception {
        List<Employee> employeeList = dbController.getAllEmployees();
        if (employeeList == null)
            throw new Exception("Method getAllEmployees() returned null.");
        else if (employeeList.size() < 1)
            throw new Exception("Method getAllEmployees() returned List of size < 1");
    }

    public void testGetEmployeeCount() throws Exception {
        if (dbController.getEmployeeCount() < 1)
            throw new Exception("Method getEmployeeCount() returned < 1");
    }

    public void testGetEmployeeHelper() throws Exception {
        String testProjection = DBContract.EmployeeTable.COLUMN_NAME_EMAIL + "=?";
        String testSelectionArgs[] = {testEmail};

        if (dbController.getEmployeeHelper(testProjection, testSelectionArgs) == null)
            throw new Exception("Method getEmployeeHelper() returned null.");
    }

    // by email
    public void testGetEmployee() throws Exception {
        if (dbController.getEmployee(testEmail) == null)
            throw new Exception("Method getEmployee() returned null.");
    }

    // by id
    public void testGetEmployee1() throws Exception {
        if (dbController.getEmployee(testId) == null)
            throw new Exception("Method getEmployee() returned null.");
    }

    public void testUpdateEmployee() throws Exception {
        Employee employee = dbController.getEmployee(testId);
        employee.setEmail(testEmail+"updated");
        dbController.updateEmployee(employee);
        employee = dbController.getEmployee(testId);
        if (!employee.getEmail().equals(testEmail+"updated"))
            throw new Exception("Method updateEmployee() failed.");
    }
}