package com.example.m_universe;

// ModelChat class represents a chat message
public class ModelChat {

    // message content
    private String message;

    // getter for message
    public String getMessage() {
        return message;
    }

    // setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // ID of the message receiver
    private String receiver;

    // getter for receiver
    public String getReceiver() {
        return receiver;
    }

    // setter for receiver
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    // ID of the message sender
    private String sender;

    // getter for sender
    public String getSender() {
        return sender;
    }

    // setter for sender
    public void setSender(String sender) {
        this.sender = sender;
    }

    // timestamp of the message
    private String timestamp;

    // getter for timestamp
    public String getTimestamp() {
        return timestamp;
    }

    // setter for timestamp
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // flag to check if the message has been read or not
    private boolean dilihat;

    // getter for dilihat
    public boolean isDilihat() {
        return dilihat;
    }

    // setter for dilihat
    public void setDilihat(boolean dilihat) {
        this.dilihat = dilihat;
    }

    // constructor with all fields
    public ModelChat(String message, String receiver, String sender, String type, String timestamp, boolean dilihat) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.type = type;
        this.timestamp = timestamp;
        this.dilihat = dilihat;
    }

    // type of the message (text, image, etc.)
    private String type;

    // getter for type
    public String getType() {
        return type;
    }

    // setter for type
    public void setType(String type) {
        this.type = type;
    }

    // default constructor
    public ModelChat() {
    }
}