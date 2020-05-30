package com.example.mymoviememoir.entity;

import java.util.ArrayList;
import java.util.Date;

public class MemoirDetail extends Memoir {
    private Integer movieId;
    private double publicRating;
    private String posterPath;
    private ArrayList<String> genres;

    public MemoirDetail(){}

    public MemoirDetail(Integer movieId, double publicRating, String posterPath, ArrayList<String> genres) {
        this.movieId = movieId;
        this.publicRating = publicRating;
        this.posterPath = posterPath;
        this.genres = genres;
    }

    public MemoirDetail(Integer memoirid, String moviename, Date moviereleasedate, Date watchdatetime, String comment, Double starrating, Integer movieId, double publicRating, String posterPath, ArrayList<String> genres, String postcode) {
        super(memoirid, moviename, moviereleasedate, watchdatetime, comment, starrating);
        super.setCinemaPostcode(postcode);
        this.movieId = movieId;
        this.publicRating = publicRating;
        this.posterPath = posterPath;
        this.genres = genres;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public double getPublicRating() {
        return publicRating;
    }

    public void setPublicRating(double publicRating) {
        this.publicRating = publicRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }
}
