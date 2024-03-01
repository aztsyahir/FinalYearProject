package com.heroku.java.MODEL;

public class PlayerStats {
    public int yearsPlaying;
    public int tournamentsJoined;
    public int tournamentsWon;

    public PlayerStats() {
    }

    public PlayerStats(int yearsPlaying, int tournamentsJoined, int tournamentsWon) {
        this.yearsPlaying = yearsPlaying;
        this.tournamentsJoined = tournamentsJoined;
        this.tournamentsWon = tournamentsWon;
    }

    public int getYearsPlaying() {
        return this.yearsPlaying;
    }

    public void setYearsPlaying(int yearsPlaying) {
        this.yearsPlaying = yearsPlaying;
    }

    public int getTournamentsJoined() {
        return this.tournamentsJoined;
    }

    public void setTournamentsJoined(int tournamentsJoined) {
        this.tournamentsJoined = tournamentsJoined;
    }

    public int getTournamentsWon() {
        return this.tournamentsWon;
    }

    public void setTournamentsWon(int tournamentsWon) {
        this.tournamentsWon = tournamentsWon;
    }

}
