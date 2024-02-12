package com.example.m_universe;

public class User {
    private String grNum;
    private String email;
    private String userId;

    private String uname;

    // Default constructor (required for Firebase)
    public User() {
        // Default constructor required for Firebase
    }

    // Parameterized constructor
    public User(String grNum, String email, String userId, String uname) {
        this.grNum = grNum;
        this.email = email;
        this.userId = userId;
        this.uname = uname;
    }

    public String getGrNum() {
        return grNum;
    }

    public void setGrNum(String grNum) { this.grNum = grNum;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email;}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) { this.userId = userId;}

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) { this.uname = uname;}
}
