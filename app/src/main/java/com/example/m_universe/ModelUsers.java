package com.example.m_universe;

public class ModelUsers {
    private String uid;
    private String name;
    private String image;

    // Default constructor required for Firebase
    public ModelUsers() {
    }

    // Constructor with parameters
    public ModelUsers(String uid, String name, String image) {
        this.uid = uid;
        this.name = name;
        this.image = image;
    }

    // Getter methods
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    // Setter methods (if needed)
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
