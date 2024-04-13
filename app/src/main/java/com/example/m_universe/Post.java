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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
