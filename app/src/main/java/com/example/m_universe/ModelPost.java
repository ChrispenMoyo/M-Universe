package com.example.m_universe;

public class ModelPost {
    public ModelPost() {
    }

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getUdp() {
        return udp;
    }

    public void setUdp(String udp) {
        this.udp = udp;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPlike() {
        return plike;
    }

    public void setPlike(String plike) {
        this.plike = plike;
    }
    // ...
    String  plike;

    String pid;

    public String getPcomments() {
        return pcomments;
    }

    public void setPcomments(String pcomments) {
        this.pcomments = pcomments;
    }

    // New fields for reposts and share
    String p_reposts;
    String pshare;

    public String getP_reposts() {
        return p_reposts;
    }

    public void setP_reposts(String p_reposts) {
        this.p_reposts = p_reposts;
    }

    public String getPshare() {
        return pshare;
    }

    public void setPshare(String pshare) {
        this.pshare = pshare;
    }

    public ModelPost(String description, String pid, String ptime, String pcomments, String udp, String uemail, String uid, String uimage, String uname, String plike, String p_reposts, String pshare) {
        this.description = description;
        this.pid = pid;
        this.ptime = ptime;
        this.pcomments = pcomments;
//        this.title = title;
        this.udp = udp;
        this.uemail = uemail;
        this.uid = uid;
        this.uimage = uimage;
        this.uname = uname;
        this.plike = plike;
        this.p_reposts = p_reposts;
        this.pshare = pshare;
    }

    String ptime, pcomments;

//    String title;

    String udp;
    String uemail;
    String uid;
    String uimage;

    String uname;
}
