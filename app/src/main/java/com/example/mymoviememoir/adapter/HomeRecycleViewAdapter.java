package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Movie;

import java.text.SimpleDateFormat;
import java.util.List;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseDate;
        public RatingBar ratingBar;


        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName3);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            ratingBar = itemView.findViewById(R.id.ratingBar2);
            context = itemView.getContext();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private List<Movie> movies;

    // Pass in the contact array into the constructor
    public HomeRecycleViewAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    //This method creates a new view holder that is constructed with a new View, inflated from a layout
    @Override
    public HomeRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View moviesView = inflater.inflate(R.layout.rv_home_layout, parent, false);
        // construct the viewholder with the new view
        HomeRecycleViewAdapter.ViewHolder viewHolder = new HomeRecycleViewAdapter.ViewHolder(moviesView);
        return viewHolder;
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull HomeRecycleViewAdapter.ViewHolder viewHolder,
                                 int position) {
        final Movie movie = movies.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName = viewHolder.tvMovieName;
        tvMovieName.setText(movie.getMovieName());
        TextView tvReleaseYear = viewHolder.tvReleaseDate;
        tvReleaseYear.setText(new SimpleDateFormat("dd-MM-yyyy").format(movie.getReleaseDate()));
        RatingBar ratingBar = viewHolder.ratingBar;
        ratingBar.setRating((float) movie.getRating());
    }
}