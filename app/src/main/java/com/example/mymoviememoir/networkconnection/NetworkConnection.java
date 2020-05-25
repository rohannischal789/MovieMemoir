package com.example.mymoviememoir.networkconnection;

import android.util.Log;

import com.example.mymoviememoir.SignInActivity;
import com.example.mymoviememoir.entity.Credential;
import com.example.mymoviememoir.entity.Person;
import com.example.mymoviememoir.model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public String findByUsername(String username, String password) {
        final String methodPath = "restmovie.credential/findByUsername/" + username;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        String passwordHash = "";
        String credentialID = "";
        JSONArray array = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            array = new JSONArray(results);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (results.equals("[]")) {
            results = "Incorrect username";
        } else {
            JSONObject object = null;
            try {
                object = array.getJSONObject(0);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                passwordHash = object.getString("passwordhash");
                credentialID = object.getString("credentialid");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (passwordHash.equalsIgnoreCase(password)) {
                results = credentialID;
            } else {
                results = "Incorrect password";
            }
        }
        return results;
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
            Log.i("excep ", e.toString());
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
            if(countries.length() > 0) {
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
                if(currCrew.getString("job").equalsIgnoreCase("director"))
                {
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

    /*

                    String releaseDate = currMovie.getString("release_date");
                    String overview = currMovie.getString("overview");
                    Date date= null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String posterPath = currMovie.getString("poster_path");
                    int movieID = currMovie.getInt("id");
                    moviesArr.add(new Movie(movieID,name,date,posterPath,overview));
     */
}
