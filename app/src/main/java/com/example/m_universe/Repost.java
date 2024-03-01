package com.example.m_universe;

public class Repost {
    private String postId;
    private String userId;
    private String reposterName;

    public Repost() {
    }

    public Repost(String postId, String userId, String reposterName) {
        this.postId = postId;
        this.userId = userId;
        this.reposterName = reposterName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReposterName() {
        return reposterName;
    }

    public void setReposterName(String reposterName) {
        this.reposterName = reposterName;
    }
}