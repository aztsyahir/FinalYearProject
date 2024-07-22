package com.heroku.java.MODEL;

public class Event {
    public int eventid;
    public String eventname;
    public EventDetail eventDetail;
    private int eventDay;

    public Event() {
    }

    public Event(int eventid, String eventname, EventDetail eventDetail, int eventDay) {
        this.eventid = eventid;
        this.eventname = eventname;
        this.eventDetail = eventDetail;
        this.eventDay = eventDay;
    }

    public Event(int eventid, String eventname) {
        this.eventid = eventid;
        this.eventname = eventname;
    }

    public Event(EventDetail eventDetail) {
        this.eventDetail = eventDetail;
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

    public EventDetail getEventDetail() {
        return this.eventDetail;
    }

    public void setEventDetail(EventDetail eventDetail) {
        this.eventDetail = eventDetail;
    }

    public int getEventDay() {
        return this.eventDay;
    }

    public void setEventDay(int eventDay) {
        this.eventDay = eventDay;
    }

}
