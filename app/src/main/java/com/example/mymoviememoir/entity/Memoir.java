package com.example.mymoviememoir.entity;

import java.util.Date;

public class Memoir {
    private Integer memoirid;
    private String moviename;
    private Date moviereleasedate;
    private Date watchdatetime;
    private String comment;
    private Double starrating;
    private Cinema cinemaid;
    private Credential credentialid;

    public Memoir(Integer memoirid, String moviename, Date moviereleasedate, Date watchdatetime, String comment, Double starrating) {
        this.memoirid = memoirid;
        this.moviename = moviename;
        this.moviereleasedate = moviereleasedate;
        this.watchdatetime = watchdatetime;
        this.comment = comment;
        this.starrating = starrating;
    }

    public Integer getMemoirid() {
        return memoirid;
    }

    public void setMemoirid(Integer memoirid) {
        this.memoirid = memoirid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public Date getMoviereleasedate() {
        return moviereleasedate;
    }

    public void setMoviereleasedate(Date moviereleasedate) {
        this.moviereleasedate = moviereleasedate;
    }

    public Date getWatchdatetime() {
        return watchdatetime;
    }

    public void setWatchdatetime(Date watchdatetime) {
        this.watchdatetime = watchdatetime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getStarrating() {
        return starrating;
    }

    public void setStarrating(Double starrating) {
        this.starrating = starrating;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
    }

    public Credential getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Credential credentialid) {
        this.credentialid = credentialid;
    }

    public void setCinemaId(int cinemaid) {
        this.cinemaid = new Cinema();
        this.cinemaid.setCinemaId(cinemaid);
    }

    public void setCredentialId(int credentialId) {
        this.credentialid = new Credential();
        this.credentialid.setCredentialid(credentialId);
    }
}
