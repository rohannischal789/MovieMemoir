package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mymoviememoir.model.Movie;

import java.text.SimpleDateFormat;

public class MovieViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Bundle bundle = getIntent().getExtras();
        Movie movie = bundle.getParcelable("selectedMovie");
        final TextView tvMovieName2 = findViewById(R.id.tvMovieName2);
        final TextView tvReleaseDate = findViewById(R.id.tvReleaseDate);
        final TextView tvGenre = findViewById(R.id.tvGenre);
        final TextView tvSynopsis = findViewById(R.id.tvSynopsis);
        final TextView tvCast = findViewById(R.id.tvCast);
        final TextView tvDirector = findViewById(R.id.tvDirector);
        final TextView tvCountry = findViewById(R.id.tvCountry);
        tvMovieName2.setText(movie.getMovieName());
        tvReleaseDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(movie.getReleaseDate()));
        tvSynopsis.setText(movie.getSynopsis());
        /*tvMovieName2.setText(movie.getMovieName());
        tvMovieName2.setText(movie.getMovieName());
        tvMovieName2.setText(movie.getMovieName());
        tvMovieName2.setText(movie.getMovieName());
        tvMovieName2.setText(movie.getMovieName());*/

    }
}
