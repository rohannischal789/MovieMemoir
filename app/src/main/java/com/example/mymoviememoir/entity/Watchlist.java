package com.example.mymoviememoir.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Watchlist {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "person_id")
    public int personID;
    @ColumnInfo(name = "movie_name")
    public String movieName;
    @ColumnInfo(name = "release_date")
    public Date releaseDate;
    @ColumnInfo(name = "watchlist_datetime")
    public Date watchDateTime;

    public Watchlist(int personID, String movieName, Date releaseDate, Date watchDateTime) {
        this.personID = personID;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.watchDateTime = watchDateTime;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public int getUid() {
        return uid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getWatchDateTime() {
        return watchDateTime;
    }

    public void setWatchDateTime(Date watchDateTime) {
        this.watchDateTime = watchDateTime;
    }
}

