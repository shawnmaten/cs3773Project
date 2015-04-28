package com.marsdayjam.eventplanner;

import android.test.AndroidTestCase;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.util.List;

public final class DBControllerTest extends AndroidTestCase {
    public static final String testEmail = "unit@test.com";
    public static final String testPassword = "unitTestPassword";
    public static final String testFirst = "Unit";
    public static final String testLast = "Test";
    public static final int testRole = 0;

    private long testId;
    private DBController dbController;

    @Override
    protected void setUp() throws Exception {super.setUp();

        dbController = DBController.getMockDBController(
                new RenamingDelegatingContext(getContext(), this.toString()));
        assertNotNull(dbController);
        testId = dbController.insertEmployee(
                testEmail,
                testPassword,
                testFirst,
                testLast,
                testRole);
        assertTrue(testId > 0);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertEmployee() throws Exception {
        assertTrue(dbController.insertEmployee(
                testEmail,
                testPassword,
                testFirst,
                testLast,
                testRole) > 0);
    }

    public void testDeleteEmployee() throws Exception {
        assertNotNull(dbController.getEmployee(testId));
        dbController.deleteEmployee(testId);
        assertNull(dbController.getEmployee(testId));
    }

    public void testGetAllEmployees() throws Exception {
        List<Employee> employeeList = dbController.getAllEmployees();
        assertNotNull(employeeList);
    }

    public void testGetEmployeeCount() throws Exception {
        assertEquals(dbController.getEmployeeCount(), 2); // 2 because of HR default
    }

    public void testGetEmployeeHelper() throws Exception {
        String testProjection = DBContract.EmployeeTable.COLUMN_NAME_EMAIL + " = ?";
        String testSelectionArgs[] = {testEmail};

        assertNotNull(dbController.getEmployeeHelper(testProjection, testSelectionArgs));
    }

    // by email
    public void testGetEmployee() throws Exception {
        assertNotNull(dbController.getEmployee(testEmail));
    }

    // by id
    public void testGetEmployee1() throws Exception {
        assertNotNull(dbController.getEmployee(testId));
    }

    public void testUpdateEmployee() throws Exception {
        Employee employee = dbController.getEmployee(testId);
        employee.setEmail(testEmail+"updated");
        dbController.updateEmployee(employee);
        employee = dbController.getEmployee(testId);
        assertEquals(employee.getEmail(), testEmail+"updated");
    }
}