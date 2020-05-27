package com.example.mymoviememoir.entity;

public class Cinema {
    private int cinemaid;
    private String cinemaname;
    private String postcode;

    public Cinema(int cinemaId, String name, String postcode) {
        this.cinemaid = cinemaId;
        this.cinemaname = name;
        this.postcode = postcode;
    }

    public int getCinemaId() {
        return cinemaid;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaid = cinemaId;
    }

    public String getName() {
        return cinemaname;
    }

    public void setName(String name) {
        this.cinemaname = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
