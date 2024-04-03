package com.heroku.java.MODEL;

import java.sql.Date;
import org.springframework.web.multipart.MultipartFile;

public class Event {
    public int eventid;
    public String eventname;
    public String eventtype;
    public int eventcapacity;
    public String eventvenue;
    public String eventstate;
    public Date eventdate;
    public Date eventlastdate;
    public int eventstats;
    public byte[] eventimgbyte;
    public MultipartFile eventimgs;
    String eventimage;

    public Event() {
    }

    public Event(int eventid, String eventname, String eventtype, int eventcapacity, String eventvenue,
            String eventstate,
            Date eventdate, Date eventlastdate, int eventstats, byte[] eventimgbyte, MultipartFile eventimgs, String eventimage) {
        this.eventid = eventid;
        this.eventname = eventname;
        this.eventtype = eventtype;
        this.eventcapacity = eventcapacity;
        this.eventvenue = eventvenue;
        this.eventstate = eventstate;
        this.eventdate = eventdate;
        this.eventlastdate = eventlastdate;
        this.eventstats = eventstats;
        this.eventimgbyte = eventimgbyte;
        this.eventimgs = eventimgs;
        this.eventimage = eventimage;
    }


    public int getEventid() {
        return this.eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return this.eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventtype() {
        return this.eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public int getEventcapacity() {
        return this.eventcapacity;
    }

    public void setEventcapacity(int eventcapacity) {
        this.eventcapacity = eventcapacity;
    }

    public String getEventvenue() {
        return this.eventvenue;
    }

    public void setEventvenue(String eventvenue) {
        this.eventvenue = eventvenue;
    }

    public String getEventstate() {
        return this.eventstate;
    }

    public void setEventstate(String eventstate) {
        this.eventstate = eventstate;
    }

    public Date getEventdate() {
        return this.eventdate;
    }

    public void setEventdate(Date eventdate) {
        this.eventdate = eventdate;
    }

    public Date getEventlastdate() {
        return this.eventlastdate;
    }

    public void setEventlastdate(Date eventlastdate) {
        this.eventlastdate = eventlastdate;
    }

    public int getEventstats() {
        return this.eventstats;
    }

    public void setEventstats(int eventstats) {
        this.eventstats = eventstats;
    }

    public byte[] getEventimgbyte() {
        return this.eventimgbyte;
    }

    public void setEventimgbyte(byte[] eventimgbyte) {
        this.eventimgbyte = eventimgbyte;
    }

    public MultipartFile getEventimgs() {
        return this.eventimgs;
    }

    public void setEventimgs(MultipartFile eventimgs) {
        this.eventimgs = eventimgs;
    }

    public String getEventimage() {
        return this.eventimage;
    }

    public void setEventimage(String eventimage) {
        this.eventimage = eventimage;
    }
    

}
