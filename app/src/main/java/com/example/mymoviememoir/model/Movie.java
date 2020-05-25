package com.example.mymoviememoir.model;

public class Movie {

    private int movieID;
    private String movieName;
    private int releaseYear;
    private String moviePoster;

    public Movie(int movieID, String movieName, int releaseYear, String moviePoster) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.releaseYear = releaseYear;
        this.moviePoster = moviePoster;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }
}
