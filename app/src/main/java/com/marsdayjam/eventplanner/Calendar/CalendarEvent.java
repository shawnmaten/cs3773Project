package com.marsdayjam.eventplanner.Calendar;

import com.marsdayjam.eventplanner.Employee.Employee;
import com.marsdayjam.eventplanner.Event;

import java.util.Date;

public class CalendarEvent {
    private long id;
    private String description;
    private Date start;
    private Date end;
    private Employee employee;
    private Event event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
