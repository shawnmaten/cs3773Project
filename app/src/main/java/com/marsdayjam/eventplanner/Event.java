package com.marsdayjam.eventplanner;

import com.marsdayjam.eventplanner.Calendar.CalendarEvent;
import com.marsdayjam.eventplanner.Employee.Employee;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private long id;
    private String name;
    private String host;
    private String location;
    private Date start;
    private Date end;
    private Employee manager;
    private ArrayList<Employee> members = new ArrayList<>();
    private ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return String.format("Event: %s %s %s %s %s %s %s",
                name, host, location, start.toString(), end.toString(), manager.getFirst(),
                manager.getLast());
    }

    public ArrayList<Employee> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Employee> members) {
        this.members = members;
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(ArrayList<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }
}
