package com.example.m_universe;

public class Post {
    public String title;
    public String content;
    public String username;

    public Post() {
        // Default constructor required for Firebase
    }

    public Post(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;

    }
}
