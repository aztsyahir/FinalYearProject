package com.heroku.java.MODEL;

import java.sql.Date;
import org.springframework.web.multipart.MultipartFile;

public class EventDetail extends Event {
    public int edid;
    public String edtype;
    public int edcapacity;
    public String edvenue;
    public String edstate;
    public Date eddate;
    public Date edlastdate;
    public String edstatus;
    public int edstats;
    public byte[] edimgbyte;
    public MultipartFile edimgs;
    String edimage;
    public String registrationstatus;
    public int registrationid;
    private boolean isDirectRegistration;

    public EventDetail() {
    }

    public EventDetail(int eventid, String eventname, String edtype, int edcapacity, String edvenue, String edstate,
            Date eddate,
            Date edlastdate, String edstatus, int edstats, byte[] edimgbyte, MultipartFile edimgs, String edimage) {
        super(eventid, eventname);
        this.edtype = edtype;
        this.edcapacity = edcapacity;
        this.edvenue = edvenue;
        this.edstate = edstate;
        this.eddate = eddate;
        this.edlastdate = edlastdate;
        this.edstatus = edstatus;
        this.edstats = edstats;
        this.edimgbyte = edimgbyte;
        this.edimgs = edimgs;
        this.edimage = edimage;
    }

    public EventDetail(int eventid, String eventname, int edid, String edtype) {
        super(eventid, eventname);
        this.edid = edid;
        this.edtype = edtype;
    }

    public EventDetail(int eventid, String eventname, int edid, String edtype, int edcapacity, String edvenue,
            String edstate, Date eddate,
            Date edlastdate, String edstatus, int edstats, byte[] edimgbyte, MultipartFile edimgs, String edimage) {
        super(eventid, eventname);
        this.edid = edid;
        this.edtype = edtype;
        this.edcapacity = edcapacity;
        this.edvenue = edvenue;
        this.edstate = edstate;
        this.eddate = eddate;
        this.edlastdate = edlastdate;
        this.edstatus = edstatus;
        this.edstats = edstats;
        this.edimgbyte = edimgbyte;
        this.edimgs = edimgs;
        this.edimage = edimage;
    }

    public EventDetail(int eventid, String eventname, String edtype, int edcapacity, String edvenue, String edstate,
            Date eddate, Date edlastdate, String edstatus, int edstats, String edimage,
            String registrationstatus, int registrationid, boolean isDirectRegistration) {
        this.eventid = eventid;
        this.eventname = eventname;
        this.edtype = edtype;
        this.edcapacity = edcapacity;
        this.edvenue = edvenue;
        this.edstate = edstate;
        this.eddate = eddate;
        this.edlastdate = edlastdate;
        this.edstatus = edstatus;
        this.edstats = edstats;
        this.edimage = edimage;
        this.registrationstatus = registrationstatus;
        this.registrationid = registrationid;
        this.isDirectRegistration = isDirectRegistration;
    }

    // public EventDetail(int eventid, String eventname, int edid, int edStats, int edcapacity) {
    //     this.eventid = eventid;
    //     this.eventname = eventname;
    //     this.edid = edid;
    //     this.edstats = edStats;
    //     this.edcapacity = edcapacity;
    // }

    public EventDetail(int edid,String edtype,int edcapacity,String edvenue,String edstate,String edstatus,int edstats,int eventid,String eventname){
        super(eventid,eventname);
        this.edid = edid;
        this.edtype = edtype;
        this.edcapacity = edcapacity;
        this.edvenue = edvenue;
        this.edstate = edstate;
        this.edstatus = edstatus;
        this.edstats = edstats;
    }

    public int getEdid() {
        return this.edid;
    }

    public void setEdid(int edid) {
        this.edid = edid;
    }

    public String getEdtype() {
        return this.edtype;
    }

    public void setEdtype(String edtype) {
        this.edtype = edtype;
    }

    public int getEdcapacity() {
        return this.edcapacity;
    }

    public void setEdcapacity(int edcapacity) {
        this.edcapacity = edcapacity;
    }

    public String getEdvenue() {
        return this.edvenue;
    }

    public void setEdvenue(String edvenue) {
        this.edvenue = edvenue;
    }

    public String getEdstate() {
        return this.edstate;
    }

    public void setEdstate(String edstate) {
        this.edstate = edstate;
    }

    public Date getEddate() {
        return this.eddate;
    }

    public void setEddate(Date eddate) {
        this.eddate = eddate;
    }

    public Date getEdlastdate() {
        return this.edlastdate;
    }

    public void setEdlastdate(Date edlastdate) {
        this.edlastdate = edlastdate;
    }

    public String getEdstatus() {
        return this.edstatus;
    }

    public void setEdstatus(String edstatus) {
        this.edstatus = edstatus;
    }

    public int getEdstats() {
        return this.edstats;
    }

    public void setEdstats(int edstats) {
        this.edstats = edstats;
    }

    public byte[] getEdimgbyte() {
        return this.edimgbyte;
    }

    public void setEdimgbyte(byte[] edimgbyte) {
        this.edimgbyte = edimgbyte;
    }

    public MultipartFile getEdimgs() {
        return this.edimgs;
    }

    public void setEdimgs(MultipartFile edimgs) {
        this.edimgs = edimgs;
    }

    public String getEdimage() {
        return this.edimage;
    }

    public void setEdimage(String edimage) {
        this.edimage = edimage;
    }

    public String getRegistrationStatus() {
        return registrationstatus;
    }

    public void setRegistrationStatus(String registrationstatus) {
        this.registrationstatus = registrationstatus;
    }

    public int getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(int registrationid) {
        this.registrationid = registrationid;
    }

    public boolean isDirectRegistration() {
        return isDirectRegistration;
    }

    public void setDirectRegistration(boolean isDirectRegistration) {
        this.isDirectRegistration = isDirectRegistration;
    }

}
