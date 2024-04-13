package com.example.m_universe;

public class User {
    private String grNum;
    private String email;
    private String uid;
    private String uimage;

    private String uname;

    // Default constructor (required for Firebase)
    public User() {
        // Default constructor required for Firebase
    }

    // Parameterized constructor
    public User(String grNum, String email, String uid, String uname, String uimage) {
        this.grNum = grNum;
        this.email = email;
        this.uid = uid;
        this.uname = uname;
        this.uimage = uimage;
    }

    public String getGrNum() {
        return grNum;
    }

    public void setGrNum(String grNum) {
        this.grNum = grNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return uimage;
    }

    public void setImage(String uimage) {
        this.uimage = uimage;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}