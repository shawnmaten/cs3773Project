package com.marsdayjam.eventplanner.Employee;

import com.marsdayjam.eventplanner.Calendar.CalendarEvent;

import java.util.ArrayList;

public class Employee {
    private long id;
    private String email;
    private String password;
    private String first;
    private String last;
    private long roleCode;
    private String roleTitle;
    private ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public long getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(long roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    @Override
    public String toString() {
        return String.format("Employee: %d %s %s %s %s %d %s",
                id, email, password, first, last, roleCode, roleTitle);
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(ArrayList<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }
}
