package com.marsdayjam.eventplanner;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.marsdayjam.eventplanner.Calendar.CalendarEvent;
import com.marsdayjam.eventplanner.DB.DBContract;
import com.marsdayjam.eventplanner.DB.DBController;
import com.marsdayjam.eventplanner.Employee.Employee;

import java.text.DateFormat;
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

    public static final String testCalendarEventDescription = "Description";
    public static final String testCalendarEventStart = "May 6, 2015 12:00:00 PM";
    public static final String testCalendarEventEnd = "May 6, 2015 2:30:00 PM";
    
    private Employee testEmployee;
    private Event testEvent;
    private Team testTeam;
    private CalendarEvent testCalendarEvent;
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
        dbController.insertEmployee(testEmployee);
        assertTrue(testEmployee.getId() > 0);

        testEvent = new Event();
        testEvent.setName(testName);
        testEvent.setHost(testHost);
        testEvent.setLocation(testLocation);
        testEvent.setStart(DateFormat.getDateTimeInstance().parse(testStart));
        testEvent.setEnd(DateFormat.getDateTimeInstance().parse(testEnd));
        testEvent.setManager(testEmployee);
        dbController.insertEvent(testEvent);
        assertTrue(testEvent.getId() > 0);

        testTeam = new Team();
        testTeam.setEvent(testEvent);
        testTeam.setSupervisor(testEmployee);
        testTeam.setName(testTeamName);
        testTeam.setDuties(testTeamDuties);
        dbController.insertTeam(testTeam);
        assertTrue(testTeam.getId() > 0);

        testCalendarEvent = new CalendarEvent();
        testCalendarEvent.setDescription(testCalendarEventDescription);
        testCalendarEvent.setStart(DateFormat.getDateTimeInstance()
                .parse(testCalendarEventStart));
        testCalendarEvent.setEnd(DateFormat.getDateTimeInstance()
                .parse(testCalendarEventEnd));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertEmployee() throws Exception {
        assertTrue(dbController.insertEmployee(testEmployee) > 0);
    }

    public void testDeleteEmployee() throws Exception {
        assertNotNull(dbController.getEmployee(testEmployee.getId()));
        dbController.deleteEmployee(testEmployee);
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

    public void testGetEmployeeEmail() throws Exception {
        assertNotNull(dbController.getEmployee(testEmail));
    }

    public void testGetEmployeeId() throws Exception {
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
        dbController.deleteEvent(testEvent);
        assertNull(dbController.getEvent(testEvent.getId()));
    }

    public void testGetEvent() throws Exception {
        assertNotNull(dbController.getEvent(testEvent.getId()));
    }

    public void testInsertEventMember() throws Exception {
        assertTrue(dbController.insertEventMember(testEvent, testEmployee) > 0);
    }

    public void testDeleteEventMember() throws Exception {
        dbController.insertEventMember(testEvent, testEmployee);
        assertFalse(dbController.getEventMembers(testEvent).isEmpty());
        dbController.deleteEventMember(testEvent, testEmployee);
        assertTrue(dbController.getEventMembers(testEvent).isEmpty());
    }

    public void testGetEventMembers() throws Exception {
        dbController.insertEventMember(testEvent, testEmployee);
        assertFalse(dbController.getEventMembers(testEvent).isEmpty());
    }

    public void testInsertTeam() throws Exception {
        assertTrue(dbController.insertTeam(testTeam) > 0);
    }

    public void testDeleteTeam() throws Exception {
        assertNotNull(dbController.getTeam(testTeam.getId()));
        dbController.deleteTeam(testTeam);
        assertNull(dbController.getTeam(testTeam.getId()));
    }

    public void testGetTeam() throws Exception {
        assertNotNull(dbController.getTeam(testTeam.getId()));
    }

    public void testInsertTeamMember() throws Exception {
        assertTrue(dbController.insertTeamMember(testTeam, testEmployee) > 0);
    }

    public void testDeleteTeamMember() throws Exception {
        dbController.insertTeamMember(testTeam, testEmployee);
        assertFalse(dbController.getTeamMembers(testTeam).isEmpty());
        dbController.deleteTeamMember(testTeam, testEmployee);
        assertTrue(dbController.getTeamMembers(testTeam).isEmpty());
    }

    public void testGetTeamMembers() throws Exception {
        dbController.insertTeamMember(testTeam, testEmployee);
        assertFalse(dbController.getTeamMembers(testTeam).isEmpty());
    }

    public void testInsertCalendarEvent() throws Exception {
        testCalendarEvent.setEmployee(testEmployee);
        assertTrue(dbController.insertCalendarEvent(testCalendarEvent) > 0);
    }

    public void testDeleteCalendarEvent() throws Exception {
        testCalendarEvent.setEmployee(testEmployee);
        dbController.insertCalendarEvent(testCalendarEvent);
        assertFalse(dbController.getCalendarEvents(testEmployee).isEmpty());
        dbController.deleteCalendarEvent(testCalendarEvent);
        assertTrue(dbController.getCalendarEvents(testEmployee).isEmpty());
    }

    public void testGetCalendarEventForEmployee() throws Exception {
        testCalendarEvent.setEmployee(testEmployee);
        dbController.insertCalendarEvent(testCalendarEvent);
        assertFalse(dbController.getCalendarEvents(testEmployee).isEmpty());
    }

    public void testGetCalendarEventForEvent() throws Exception {
        testCalendarEvent.setEvent(testEvent);
        dbController.insertCalendarEvent(testCalendarEvent);
        assertFalse(dbController.getCalendarEvents(testEvent).isEmpty());
    }
}