package com.heroku.java.MODEL;

import java.util.Objects;

public class Player {

    public int playerid;
    public String playername;
    public String playeremail;
    public String playerpassword;
    public String playergender;
    public int playerage;
    public String playerstats;

    public Player() {
    }

    public Player(int playerid, String playername, String playeremail, String playerpassword, String playergender,
            int playerage, String playerstats) {
        this.playerid = playerid;
        this.playername = playername;
        this.playeremail = playeremail;
        this.playerpassword = playerpassword;
        this.playergender = playergender;
        this.playerage = playerage;
        this.playerstats = playerstats;
    }

    public Player(int playerid, String playername, String playeremail, String playerpassword) {
        this.playerid = playerid;
        this.playername = playername;
        this.playeremail = playeremail;
        this.playerpassword = playerpassword;
    }

    public int getPlayerid() {
        return this.playerid;
    }

    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    public String getPlayername() {
        return this.playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getPlayeremail() {
        return this.playeremail;
    }

    public void setPlayeremail(String playeremail) {
        this.playeremail = playeremail;
    }

    public String getPlayerpassword() {
        return this.playerpassword;
    }

    public void setPlayerpassword(String playerpassword) {
        this.playerpassword = playerpassword;
    }

    public String getPlayergender() {
        return this.playergender;
    }

    public void setPlayergender(String playergender) {
        this.playergender = playergender;
    }

    public int getPlayerage() {
        return this.playerage;
    }

    public void setPlayerage(int playerage) {
        this.playerage = playerage;
    }

    public String getPlayerstats() {
        return this.playerstats;
    }

    public void setPlayerstats(String playerstats) {
        this.playerstats = playerstats;
    }

}
