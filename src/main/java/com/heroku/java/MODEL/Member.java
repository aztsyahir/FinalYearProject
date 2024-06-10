package com.heroku.java.MODEL;

public class Member {
    
    public int memberid;
    public String membername;
    public String memberStats;

    public Member() {
    }

    public Member(int memberid, String membername, String memberStats) {
        this.memberid = memberid;
        this.membername = membername;
        this.memberStats = memberStats;
    }


    public int getMemberid() {
        return this.memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public String getMembername() {
        return this.membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMemberStats() {
        return this.memberStats;
    }

    public void setMemberStats(String memberStats) {
        this.memberStats = memberStats;
    }

}
