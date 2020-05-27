package com.example.mymoviememoir.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Movie implements Parcelable {

    private int movieID;
    private String movieName;
    private Date releaseDate;
    private String synopsis;
    private String moviePoster;
    private String country;
    private double rating;
    private ArrayList<String> genres;
    private ArrayList<String> cast;
    private ArrayList<String> director;

    public Movie()
    {}

    public Movie(int movieID, String movieName, Date releaseDate, String synopsis, String moviePoster, ArrayList<String> genres, ArrayList<String> cast, ArrayList<String> director) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.synopsis = synopsis;
        this.moviePoster = moviePoster;
        this.genres = genres;
        this.cast = cast;
        this.director = director;
    }

    public Movie(int movieID, String movieName, Date releaseDate, String moviePoster, String synopsis) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.synopsis = synopsis;
        genres = new ArrayList<>();
        cast = new ArrayList<>();
        director = new ArrayList<>();
    }

    public Movie(String movieName, Date releaseDate, double rating) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public Movie(Parcel in) {
        long[] longData = new long[2];
        in.readLongArray(longData);
        this.movieID = (int) longData[0];
        this.releaseDate = new Date(longData[0]);
        //this.releaseDate = new Date(in.readLong());
        String[] data = new String[3];
        in.readStringArray(data);
        this.movieName = data[0];
        this.moviePoster = data[1];
        this.synopsis = data[2];
    }

    public void writeToParcel(Parcel parcel, int flags) {
        //parcel.writeLong(releaseDate.getTime());
        parcel.writeLongArray(new long[]{ Long.valueOf(movieID), releaseDate.getTime()});
        //parcel.writeInt(movieID);
        parcel.writeStringArray(new String[] {movieName,moviePoster,synopsis});
    }

    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public int getReleaseYear() {
        return releaseDate.getYear() + 1900;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public void setCast(ArrayList<String> cast) {
        this.cast = cast;
    }

    public ArrayList<String> getDirector() {
        return director;
    }

    public void setDirector(ArrayList<String> director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
