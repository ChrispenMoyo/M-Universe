package com.example.m_universe;

import com.google.firebase.database.IgnoreExtraProperties;

// The @IgnoreExtraProperties annotation tells Firebase to ignore any properties in the class that are not defined as fields.
@IgnoreExtraProperties
public class ModelChatList {

    // Getter method for the id field.
    public String getId() {
        return id;
    }

    // Setter method for the id field.
    public void setId(String id) {
        this.id = id;
    }

    // Default constructor.
    public ModelChatList() {
    }

    // Constructor that takes an id as a parameter and sets the id field to that value.
    public ModelChatList(String id) {
        this.id = id;
    }

    // The id field represents the unique identifier for a chat list item.
    String id;
}