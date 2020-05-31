package com.example.mymoviememoir.networkconnection;

import android.util.Log;

import com.example.mymoviememoir.entity.Cinema;
import com.example.mymoviememoir.entity.CinemaMovie;
import com.example.mymoviememoir.entity.Credential;
import com.example.mymoviememoir.entity.Memoir;
import com.example.mymoviememoir.entity.MemoirDetail;
import com.example.mymoviememoir.entity.MonthMovie;
import com.example.mymoviememoir.entity.Person;
import com.example.mymoviememoir.model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {

    private OkHttpClient client = null;
    private String results;
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL = "http://192.168.157.1:43592/MyMovieMemoir/webresources/";

    private static final String MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key=d6e8c114ba9fd0c52689305746305a3b&language=en-US&include_adult=false&query=";
    private static String MOVIE_CREDIT_URL = "https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=d6e8c114ba9fd0c52689305746305a3b";
    private static String MOVIE_DETAILS_URL = "https://api.themoviedb.org/3/movie/{movie_id}?api_key=d6e8c114ba9fd0c52689305746305a3b&language=en-US";
    private static String GENRE_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=d6e8c114ba9fd0c52689305746305a3b&language=en-US";

    public String verifyUser(String username, String password) {
        final String methodPath = "restmovie.credential/findByUsername/" + username;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        String passwordHash = "";
        JSONArray array = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            array = new JSONArray(results);


            if (results.equals("[]")) {
                results = "Incorrect username";
            } else {
                JSONObject object = null;
                object = array.getJSONObject(0);

                passwordHash = object.getString("passwordhash");

                if (passwordHash.equalsIgnoreCase(password)) {
                    JSONObject personObject = null;
                    personObject = object.getJSONObject("personid");
                    results = personObject.getString("personid");
                } else {
                    results = "Incorrect password";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean doesUsernameExist(String username) {
        final String methodPath = "restmovie.credential/findByUsername/" + username;
        Request.Builder builder = new Request.Builder();
        boolean result = false;
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!results.equals("[]")) {
            result = true;
        }
        return result;
    }

    public int getMaxPersonID() {
        final String methodPath = "restmovie.person/getMaxPersonID/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        int num = 0;
        JSONArray array = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            num = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public int getMaxCredID() {
        final String methodPath = "restmovie.credential/getMaxCredentialID/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        int num = 0;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            num = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public int getMaxMemoirID() {
        final String methodPath = "restmovie.memoir/getMaxMemoirID/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        int num = 0;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            num = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public String addPerson(String firstName, String surname, String gender, String birthday, String address, String state, String postcode, String username, String password) {
        int maxPersonID = getMaxPersonID();

        Date dob = null;
        try {
            dob = new SimpleDateFormat("dd/MM/yyyy").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Person person = new Person(maxPersonID, firstName, surname, gender, dob, address, state, postcode);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        //RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API_BASE_URL).setConverter(new GsonConverter.create(gson)).build();
        //Gson gson = new Gson();
        String personJson = gson.toJson(person);
        String strResponse = "";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json ", personJson);
        final String methodPath = "restmovie.person/";
        RequestBody body = RequestBody.create(personJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("error ", e.toString());
        }
        Log.i("strResponse ", strResponse);

        addCredential(username, password, maxPersonID);

        return String.valueOf(maxPersonID);
    }

    public String addCredential(String username, String password, int personid) {
        int maxCredID = getMaxCredID();
        Credential cred = new Credential(maxCredID, username, password);
        cred.setPersonID(personid);
        Gson gson = new Gson();
        String credJson = gson.toJson(cred);
        String strResponse = "";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json ", credJson);
        final String methodPath = "restmovie.credential/";
        RequestBody body = RequestBody.create(credJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public List<Movie> findByMovieName(String movieName) {
        Request.Builder builder = new Request.Builder();
        builder.url(MOVIE_SEARCH_URL + movieName);
        Request request = builder.build();
        JSONArray array = new JSONArray();
        ArrayList<Movie> moviesArr = new ArrayList<Movie>();
        JSONObject jsonResponse = new JSONObject();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONObject(results);

            if (results.equals("[]")) {
                //results = "Incorrect username";
            } else {
                JSONArray movies = null;
                try {
                    movies = jsonResponse.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject currMovie = null;
                    try {
                        currMovie = movies.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String name = currMovie.getString("title");
                    String releaseDate = currMovie.getString("release_date");
                    String overview = currMovie.getString("overview");
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String posterPath = currMovie.getString("poster_path");
                    int movieID = currMovie.getInt("id");
                    moviesArr.add(new Movie(movieID, name, date, posterPath, overview));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesArr;
    }

    public Movie getMovieDetails(Movie movie) {
        Request.Builder builder = new Request.Builder();
        builder.url(MOVIE_DETAILS_URL.replace("{movie_id}", String.valueOf(movie.getMovieID())));
        Request request = builder.build();
        JSONArray array = new JSONArray();
        //Movie movie = new Movie();
        JSONObject jsonResponse = new JSONObject();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONObject(results);
            JSONArray genres = null;
            genres = jsonResponse.getJSONArray("genres");
            for (int i = 0; i < genres.length(); i++) {
                JSONObject currGenre = null;
                try {
                    currGenre = genres.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movie.getGenres().add(currGenre.getString("name"));
            }

            JSONObject currCountry = null;
            JSONArray countries = jsonResponse.getJSONArray("production_countries");
            if (countries.length() > 0) {
                currCountry = countries.getJSONObject(0);
                movie.setCountry(currCountry.getString("name"));
            }

            String releaseDate = jsonResponse.getString("release_date");
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movie.setReleaseDate(date);
            String overview = jsonResponse.getString("overview");
            String posterPath = jsonResponse.getString("poster_path");
            movie.setSynopsis(overview);
            movie.setMoviePoster(posterPath);
            double rating = jsonResponse.getDouble("vote_average");
            movie.setRating(rating / 2);

            movie = getMovieCast(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }

    public Movie getMovieCast(Movie movie) {
        Request.Builder builder = new Request.Builder();
        builder.url(MOVIE_CREDIT_URL.replace("{movie_id}", String.valueOf(movie.getMovieID())));
        Request request = builder.build();
        JSONArray array = new JSONArray();
        JSONObject jsonResponse = new JSONObject();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONObject(results);
            JSONArray cast = null;
            cast = jsonResponse.getJSONArray("cast");
            int limit = cast.length() > 5 ? 5 : cast.length();
            for (int i = 0; i < limit; i++) {
                JSONObject currCast = null;
                try {
                    currCast = cast.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movie.getCast().add(currCast.getString("name"));
            }

            JSONArray crew = jsonResponse.getJSONArray("crew");
            for (int i = 0; i < crew.length(); i++) {
                JSONObject currCrew = null;
                try {
                    currCrew = crew.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (currCrew.getString("job").equalsIgnoreCase("director")) {
                    movie.getDirector().add(currCrew.getString("name"));
                }
            }

            String releaseDate = jsonResponse.getString("release_date");
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movie.setReleaseDate(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }

    public List<Movie> findTop5UserRatedMovies(int personID) {
        final String methodPath = "restmovie.memoir/findTop5RecentYearHighestRatedMovies/" + personID;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray array = new JSONArray();
        ArrayList<Movie> moviesArr = new ArrayList<Movie>();
        JSONArray jsonResponse;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONArray(results);

            if (results.equals("[]")) {
                results = "No movies so far";
            } else {

                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject currMovie = null;
                    try {
                        currMovie = jsonResponse.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String name = currMovie.getString("movieName");
                    Double rating = currMovie.getDouble("starRating");
                    String releaseDate = currMovie.getString("movieReleaseDate");
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    moviesArr.add(new Movie(name, date, rating));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesArr;
    }

    public List<Cinema> getAllCinemas() {
        final String methodPath = "restmovie.cinema";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        ArrayList<Cinema> cinemas = new ArrayList<>();
        JSONArray array = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            array = new JSONArray(results);

            if (results.equals("[]")) {
                results = "No data";
            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = null;
                    object = array.getJSONObject(i);
                    int cinemaId = object.getInt("cinemaid");
                    String cinemaName = object.getString("cinemaname");
                    String postcode = object.getString("postcode");
                    Cinema cinema = new Cinema(cinemaId, cinemaName, postcode);
                    cinemas.add(cinema);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cinemas;
    }

    public String addCinema(String cinemaName, String postcode) {
        int maxCinemaID = getMaxCinemaID();
        Cinema cinema = new Cinema(maxCinemaID, cinemaName, postcode);
        Gson gson = new Gson();
        String cinemaJSON = gson.toJson(cinema);
        String strResponse = "";
        //this is for testing, you can check how the json looks like in Logcat
        Log.i("json ", cinemaJSON);
        final String methodPath = "restmovie.cinema/";
        RequestBody body = RequestBody.create(cinemaJSON, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public int getMaxCinemaID() {
        final String methodPath = "restmovie.cinema/getMaxCinemaID/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        int num = 0;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            num = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public Boolean doesCinemaExist(String cinemaName, String postcode) {
        final String methodPath = "restmovie.cinema/doesCinemaExist/" + cinemaName + "/" + postcode;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        Boolean isExistingCinema = true;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            isExistingCinema = Boolean.valueOf(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExistingCinema;
    }

    public boolean addMemoir(String moviename, String moviereleasedate, String watchdate, String watchtime, String comment, String starrating, String cinemaid, String personid) {

        Boolean isAdded = false;
        try {
            int maxMemoirID = getMaxMemoirID();

            Date releaseDate = null;
            releaseDate = new SimpleDateFormat("dd/MM/yyyy").parse(moviereleasedate);

            Date watchDateTime = null;
            watchDateTime = new SimpleDateFormat("dd/MM/yyyyHH:mm").parse(watchdate + watchtime);

            Memoir Memoir = new Memoir(maxMemoirID, moviename, releaseDate, watchDateTime, comment, Double.valueOf(starrating));
            Memoir.setCinemaId(Integer.valueOf(cinemaid));
            int credentialID = getCredentialID(Integer.valueOf(personid));
            Memoir.setCredentialId(credentialID);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            //RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API_BASE_URL).setConverter(new GsonConverter.create(gson)).build();
            //Gson gson = new Gson();
            String memoirJson = gson.toJson(Memoir);
            String strResponse = "";
            //this is for testing, you can check how the json looks like in Logcat
            Log.i("json ", memoirJson);
            final String methodPath = "restmovie.memoir/";
            RequestBody body = RequestBody.create(memoirJson, JSON);
            Request request = new Request.Builder()
                    .url(BASE_URL + methodPath)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                strResponse = response.body().string();
                isAdded = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("error ", e.toString());
            }
            Log.i("strResponse ", strResponse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isAdded;
    }

    public int getCredentialID(int personID) {
        final String methodPath = "restmovie.credential/getCredentialID/" + personID;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        int num = 0;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            num = Integer.parseInt(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }


    public MemoirDetail findByMovieNameAndYear(String movieName, String year) {
        Request.Builder builder = new Request.Builder();
        builder.url(MOVIE_SEARCH_URL + movieName + "&primary_release_year=" + Integer.valueOf(year));
        Request request = builder.build();
        JSONArray array = new JSONArray();
        MemoirDetail mem = new MemoirDetail();
        HashMap<Integer, String> genreMap = getAllGenres();
        JSONObject jsonResponse = new JSONObject();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONObject(results);

            if (results.equals("[]")) {
                //results = "Incorrect username";
            } else {
                JSONArray movies = null;
                try {
                    movies = jsonResponse.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject currMovie = movies.getJSONObject(0);
                String poster_path = currMovie.getString("poster_path");
                Double publicRating = currMovie.getDouble("vote_average");
                int movieID = currMovie.getInt("id");
                ArrayList<String> genreList = new ArrayList<>();
                JSONArray genres = currMovie.getJSONArray("genre_ids");
                for (int i = 0; i < genres.length(); i++) {
                    String value = genreMap.get(genres.getInt(i));
                    genreList.add(value);
                }
                mem = new MemoirDetail(movieID, publicRating, poster_path, genreList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mem;
    }

    public HashMap<Integer, String> getAllGenres() {
        Request.Builder builder = new Request.Builder();
        builder.url(GENRE_URL);
        HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        Request request = builder.build();
        JSONObject jsonResponse = new JSONObject();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONObject(results);

            if (results.equals("[]")) {
                //results = "Incorrect username";
            } else {
                JSONArray genres = null;
                genres = jsonResponse.getJSONArray("genres");
                for (int i = 0; i < genres.length(); i++) {
                    JSONObject currGenre = null;
                    currGenre = genres.getJSONObject(i);
                    int id = currGenre.getInt("id");
                    String name = currGenre.getString("name");
                    hmap.put(id, name);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hmap;
    }

    public List<MemoirDetail> getMemoirs(int personID) {
        final String methodPath = "restmovie.memoir/findByPersonID/" + personID;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        ArrayList<MemoirDetail> memArr = new ArrayList<MemoirDetail>();
        JSONArray jsonResponse;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONArray(results);

            if (results.equals("[]")) {
                //results = "Incorrect username";
            } else {
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject currMem = null;
                    currMem = jsonResponse.getJSONObject(i);
                    int memID = currMem.getInt("memoirid");

                    // movie name, the release date, an image (from Google or any other API), and the date that the user watched it, cinema suburb/postcode, user comment/memory, and the user’s rating score as stars.
                    String movieName = currMem.getString("moviename");
                    String releaseDate = currMem.getString("moviereleasedate");
                    String watchDate = currMem.getString("watchdatetime");

                    String comment = currMem.getString("comment");
                    JSONObject cinema = currMem.getJSONObject("cinemaid");
                    String postcode = cinema.getString("postcode");

                    Date relDate = null;
                    try {
                        relDate = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date watDate = null;
                    try {
                        watDate = new SimpleDateFormat("yyyy-MM-dd").parse(watchDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Double starrating = currMem.getDouble("starrating");

                    MemoirDetail mem = findByMovieNameAndYear(movieName, String.valueOf(relDate.getYear() + 1900));
                    memArr.add(new MemoirDetail(memID, movieName, relDate, watDate, comment, starrating, mem.getMovieId(), mem.getPublicRating(), mem.getPosterPath(), mem.getGenres(), postcode));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return memArr;
    }

    public List<CinemaMovie> getMoviesWatchedPerPostcode(String personID, String startDate, String endDate) {
        ArrayList<CinemaMovie> data = new ArrayList<>();
        try {
            Date sDate = null;
            sDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);

            Date eDate = null;
            eDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);

            final String methodPath = "restmovie.memoir/findWatchedMovieCountByPostcode/" + personID + "/" + startDate + "/" + endDate;
            Request.Builder builder = new Request.Builder();
            builder.url(BASE_URL + methodPath);
            Request request = builder.build();
            JSONArray jsonResponse;
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONArray(results);

            if (results.equals("[]")) {
                //results = "Incorrect username";
            } else {
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject currMem = null;
                    currMem = jsonResponse.getJSONObject(i);
                    int watchedMovieCount = currMem.getInt("watchedMovieCount");
                    String cinemaPostCode = currMem.getString("cinemaPostCode");
                    data.add(new CinemaMovie(cinemaPostCode,watchedMovieCount));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MonthMovie> getWatchedMoviesPerMonth(String personID, String year) {
        ArrayList<MonthMovie> data = new ArrayList<>();
        try {
            final String methodPath = "restmovie.memoir/findWatchedMoviesPerMonth/" + personID + "/" + year;
            Request.Builder builder = new Request.Builder();
            builder.url(BASE_URL + methodPath);
            Request request = builder.build();
            JSONArray jsonResponse;
            Response response = client.newCall(request).execute();
            results = response.body().string();
            jsonResponse = new JSONArray(results);

            if (results.equals("[]")) {
                //results = "Incorrect username";
            } else {
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject currMem = null;
                    currMem = jsonResponse.getJSONObject(i);
                    int watchedMovieCount = currMem.getInt("watchedMovieCount");
                    String watchMonth = currMem.getString("watchMonth");
                    data.add(new MonthMovie(watchMonth, watchedMovieCount));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Person getPersonByID(int personID) {
        final String methodPath = "restmovie.person/" + personID;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        Person person = new Person();
        JSONObject object;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            object = new JSONObject(results);
            if (results.equals("[]")) {
                results = "Incorrect username";
            } else {
                person.setFirstname(object.getString("firstname"));
                person.setAddress(object.getString("address"));
                person.setPersonid(personID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }
}

