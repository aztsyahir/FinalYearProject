package com.heroku.java.MODEL;

public class Member extends Player {

    public int memberid;
    public Team team;

    public Member() {
    }

    public Member(int memberid, int playerid, String playername, String playeremail, String playerpassword,
            String playergender,
            int playerage, int playerstats, Team team) {
        super(playerid, playername, playeremail, playerpassword, playergender, playerage, playerstats);
        this.memberid = memberid;
        this.team = team;
    }

    public Member(int playerid, int teamid) {
        super(playerid);
        this.team = new Team(teamid);
    }

    public int getMemberid() {
        return this.memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getTeamid() {
        return team.getTeamid();
    }

    public String getTeamname() {
        return team.getTeamname();
    }
}
