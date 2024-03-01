package com.example.m_universe;

public class Notification {
    private String userId;
    private String actionType;
    private String postId;
    private long timestamp;
    private String message;

    public Notification() {
        // Default constructor required for Firebase
    }

    public Notification(String userId, String actionType, String postId, long timestamp, String message) {
        this.userId = userId;
        this.actionType = actionType;
        this.postId = postId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
