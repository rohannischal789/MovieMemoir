package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("selectedMovie");
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
        networkConnection = new NetworkConnection();
        getMovieDetails movieDetails = new getMovieDetails();
        movieDetails.execute(movie);

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

            String fullPath = "https://image.tmdb.org/t/p/w500/" + details.getMoviePoster();
            Picasso.get()
                    .load(fullPath)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(200, 200)
                    .centerInside()
                    .into(imageView);
            ///Toast.makeText(getActivity(), "Movies searched" ,Toast.LENGTH_SHORT).show();

            //TextView resultTextView = findViewById(R.id.tvCheck);
            //resultTextView.setText(details);
            //Todo: save in shared preferences if all good
        }
    }
}
