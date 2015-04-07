package com.marsdayjam.eventplanner;

public class Employee {
    private int id;
    private String email;
    private String password;
    private int roleCode;
    private String roleTitle;

    public Employee(int id, String email, String password, int roleCode, String roleTitle) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roleCode = roleCode;
        this.roleTitle = roleTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public String getRoleTitle() {
        return roleTitle;
    }
}
