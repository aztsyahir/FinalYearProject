package com.heroku.java.MODEL;

public class Admin {

    public int adminid;
    public String adminname;
    public String adminemail;
    public String adminpassword;

    public Admin() {
    }

    public Admin(int adminid, String adminname, String adminemail, String adminpassword) {
        this.adminid = adminid;
        this.adminname = adminname;
        this.adminemail = adminemail;
        this.adminpassword = adminpassword;
    }

    public int getAdminid() {
        return this.adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public String getAdminname() {
        return this.adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminemail() {
        return this.adminemail;
    }

    public void setAdminemail(String adminemail) {
        this.adminemail = adminemail;
    }

    public String getAdminpassword() {
        return this.adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }

}
