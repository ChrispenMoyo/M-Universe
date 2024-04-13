package com.example.m_universe;

public class ModelUsers {
    private String uid;
    private String uname;
    private String grNum;
    private String email;
    private String image;

    // Default constructor required for Firebase
    public ModelUsers() {
    }

    // Constructor with parameters
    public ModelUsers(String uid, String uname, String grNum, String email, String image) {
        this.uid = uid;
        this.uname = uname;
        this.grNum = grNum;
        this.email = email;
        this.image = image;
    }

    // Getter methods
    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getGrNum() {
        return grNum;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    // Setter methods (if needed)
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setGrNum(String grNum) {
        this.grNum = grNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }
}