package com.heroku.java.MODEL;

public class Team {
    public int teamid;
    public String teamname;
    public int teamstats;

    public Team() {
    }

    public Team(int teamid, String teamname, int teamstats) {
        this.teamid = teamid;
        this.teamname = teamname;
        this.teamstats = teamstats;
    }

    public Team(int teamid) {
        this.teamid = teamid;

    }

    public int getTeamid() {
        return this.teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public String getTeamname() {
        return this.teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public int getTeamstats() {
        return this.teamstats;
    }

    public void setTeamstats(int teamstats) {
        this.teamstats = teamstats;
    }

}
