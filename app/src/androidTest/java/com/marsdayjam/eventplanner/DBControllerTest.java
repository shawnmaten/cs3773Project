package com.marsdayjam.eventplanner;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

public final class DBControllerTest extends AndroidTestCase {
    public static final String testEmail = "unit@test.com";
    public static final String testPassword = "unitTestPassword";
    public static final String testFirst = "Unit";
    public static final String testLast = "Test";
    public static final int testRole = 0;

    public static final String testName = "Unit Test Event";
    public static final String testHost = "Host";
    public static final String testLocation = "Location";
    public static final String testStart = "May 6, 2015 12:00:00 PM";
    public static final String testEnd = "May 6, 2015 2:30:00 PM";

    public static final String testTeamName = "Team";
    public static final String testTeamDuties = "Duties";
    
    private Employee testEmployee;
    private Event testEvent;
    private Team testTeam;
    private DBController dbController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        dbController = DBController.getMockDBController(
                new RenamingDelegatingContext(getContext(), this.toString()));
        assertNotNull(dbController);

        testEmployee = new Employee();
        testEmployee.setEmail(testEmail);
        testEmployee.setPassword(testPassword);
        testEmployee.setFirst(testFirst);
        testEmployee.setLast(testLast);
        testEmployee.setRoleCode(testRole);
        testEmployee.setId(dbController.insertEmployee(
                testEmail,
                testPassword,
                testFirst,
                testLast,
                testRole));
        assertTrue(testEmployee.getId() > 0);

        testEvent = new Event();
        testEvent.setName(testName);
        testEvent.setHost(testHost);
        testEvent.setLocation(testLocation);
        testEvent.setStart(DateFormat.getDateTimeInstance().parse(testStart));
        testEvent.setEnd(DateFormat.getDateTimeInstance().parse(testEnd));
        testEvent.setManager(testEmployee);
        testEvent.setId(dbController.insertEvent(testEvent));
        assertTrue(testEvent.getId() > 0);

        testTeam = new Team();
        testTeam.setEvent(testEvent);
        testTeam.setSupervisor(testEmployee);
        testTeam.setName(testTeamName);
        testTeam.setDuties(testTeamDuties);
        testTeam.setId(dbController.insertTeam(testTeam));
        assertTrue(testTeam.getId() > 0);
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
        assertNotNull(dbController.getEmployee(testEmployee.getId()));
        dbController.deleteEmployee(testEmployee.getId());
        assertNull(dbController.getEmployee(testEmployee.getId()));
    }

    public void testGetAllEmployees() throws Exception {
        List<Employee> employeeList = dbController.getAllEmployees();
        assertNotNull(employeeList);
    }

    public void testGetEmployeeCount() throws Exception {
        assertEquals(dbController.getEmployeeCount(), 4); // 2 because of HR default
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
        assertNotNull(dbController.getEmployee(testEmployee.getId()));
    }

    public void testUpdateEmployee() throws Exception {
        Employee employee = dbController.getEmployee(testEmployee.getId());
        employee.setEmail(testEmail+"updated");
        dbController.updateEmployee(employee);
        employee = dbController.getEmployee(testEmployee.getId());
        assertEquals(employee.getEmail(), testEmail + "updated");
    }

    public void testInsertEvent() throws Exception {
        assertTrue(dbController.insertEvent(testEvent) > 0);
    }

    public void testDeleteEvent() throws Exception {
        dbController.deleteEvent(testEvent.getId());
        assertNull(dbController.getEvent(testEvent.getId()));
    }

    public void testGetEvent() throws Exception {
        assertNotNull(dbController.getEvent(testEvent.getId()));
    }

    public void testInsertTeam() throws Exception {
        assertTrue(dbController.insertTeam(testTeam) > 0);
    }

    public void testDeleteTeam() throws Exception {
        assertNotNull(dbController.getTeam(testTeam.getId()));
        dbController.deleteTeam(testTeam.getId());
        assertNull(dbController.getTeam(testTeam.getId()));
    }

    public void testGetTeam() throws Exception {
        assertNotNull(dbController.getTeam(testTeam.getId()));
    }
}