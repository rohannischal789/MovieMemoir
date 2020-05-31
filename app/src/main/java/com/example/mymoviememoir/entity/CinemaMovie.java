package com.example.mymoviememoir.entity;

public class CinemaMovie {

    private String postcode;
    private int moviesWatched;

    public CinemaMovie(String postcode, int moviesWatched) {
        this.postcode = postcode;
        this.moviesWatched = moviesWatched;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public int getMoviesWatched() {
        return moviesWatched;
    }

    public void setMoviesWatched(int moviesWatched) {
        this.moviesWatched = moviesWatched;
    }
}
