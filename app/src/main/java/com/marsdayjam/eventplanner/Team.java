package com.marsdayjam.eventplanner;

import com.marsdayjam.eventplanner.Employee.Employee;

import java.util.ArrayList;

public class Team {
    private long id;
    private Event event;
    private Employee supervisor;
    private String name;
    private String duties;
    private ArrayList<Employee> members = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public ArrayList<Employee> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Employee> members) {
        this.members = members;
    }
}
