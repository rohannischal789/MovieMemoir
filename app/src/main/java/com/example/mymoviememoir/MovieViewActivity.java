package com.example.mymoviememoir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoviememoir.database.WatchlistDatabase;
import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MovieViewActivity extends AppCompatActivity {
    private NetworkConnection networkConnection;
    private TextView tvGenre;
    private TextView tvCast;
    private TextView tvDirector;
    private TextView tvCountry;
    private TextView tvReleaseDate;
    private TextView tvMovieName2;
    private TextView tvSynopsis;
    private ImageView imageView;
    private RatingBar rBar;
    WatchlistDatabase db = null;
    WatchlistViewModel watchlistViewModel;
    private Button btnWatchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Bundle bundle = getIntent().getExtras();
        final Movie movie = bundle.getParcelable("selectedMovie");
        movie.setGenres(new ArrayList<String>());
        movie.setCast(new ArrayList<String>());
        movie.setDirector(new ArrayList<String>());
        tvMovieName2 = findViewById(R.id.tvMovieName2);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvGenre = findViewById(R.id.tvGenre);
        tvSynopsis = findViewById(R.id.tvSynopsis);
        tvCast = findViewById(R.id.tvCast);
        tvDirector = findViewById(R.id.tvDirector);
        tvCountry = findViewById(R.id.tvCountry);
        imageView = findViewById(R.id.ivPoster);
        rBar = findViewById(R.id.ratingBar1);

        networkConnection = new NetworkConnection();
        getMovieDetails movieDetails = new getMovieDetails();
        movieDetails.execute(movie);
        btnWatchlist = findViewById(R.id.btnWatchlist);
        SharedPreferences sharedPref = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        final int personID = sharedPref.getInt("personID",0);
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getApplication());

        watchlistViewModel.getAllWatchlist(personID).observe(this, new
                Observer<List<Watchlist>>() {
                    @Override
                    public void onChanged(@Nullable final List<Watchlist> watchlist) {
                        for (Watchlist w : watchlist) {
                            if (w.getMovieID() == movie.getMovieID())
                                btnWatchlist.setEnabled(false);
                        }
                    }
                });


        btnWatchlist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Watchlist watchlist = null;
                try {

                    if(btnWatchlist.isEnabled()) {
                        watchlist = new Watchlist(personID, tvMovieName2.getText().toString().replace("Movie name: ", ""),
                                new SimpleDateFormat("dd-MM-yyyy").parse(tvReleaseDate.getText().toString().replace("Release Date: ", "")), new Date(), movie.getMovieID());
                        watchlistViewModel.insert(watchlist);
                        Toast.makeText(getApplicationContext(), "Added movie to Watchlist" ,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Movie already exists in watchlist" ,Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnMemoir = findViewById(R.id.btnMemoir);
        btnMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieViewActivity.this,
                        AddMemoirActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedMovie", movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class getMovieDetails extends AsyncTask<Movie, Void, Movie> {

        @Override
        protected Movie doInBackground(Movie... params) {
            return networkConnection.getMovieDetails(params[0]);
        }

        @Override
        protected void onPostExecute(Movie details) {

            tvReleaseDate.setText("Release Date: " + new SimpleDateFormat("dd-MM-yyyy").format(details.getReleaseDate()));
            String genreName = "";
            for (int i = 0; i < details.getGenres().size(); i++) {
                genreName += i != details.getGenres().size() - 1 ? details.getGenres().get(i) + ", " : details.getGenres().get(i);
            }
            tvGenre.setText("Genre: " + genreName);

            String cast = "";
            for (int i = 0; i < details.getCast().size(); i++) {
                cast += i != details.getCast().size() - 1 ? details.getCast().get(i) + ", " : details.getCast().get(i);
            }

            String director = "";
            for (int i = 0; i < details.getDirector().size(); i++) {
                director += i != details.getDirector().size() - 1 ? details.getDirector().get(i) + ", " : details.getDirector().get(i);
            }
            tvDirector.setText("Director: " + director);
            tvCast.setText("Cast: " + cast);
            tvCountry.setText("Country: " + details.getCountry());

            tvMovieName2.setText("Movie name: " + details.getMovieName());
            tvSynopsis.setText("Synopsis: " + details.getSynopsis());

            rBar.setRating((float) details.getRating());
            String fullPath = "https://image.tmdb.org/t/p/w500/" + details.getMoviePoster();
            Picasso.get()
                    .load(fullPath)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200, 200)
                    .centerInside()
                    .into(imageView);

            //TextView resultTextView = findViewById(R.id.tvCheck);
            //resultTextView.setText(details);
            //Todo: save in shared preferences if all good
        }
    }
}
