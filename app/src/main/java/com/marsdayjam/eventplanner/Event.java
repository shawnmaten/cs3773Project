package com.marsdayjam.eventplanner;

import java.util.Date;

public class Event {
    private long id;
    private String name;
    private String host;
    private String location;
    private Date start;
    private Date end;
    private Employee manager;

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
}
