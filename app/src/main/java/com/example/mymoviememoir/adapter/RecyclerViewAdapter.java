package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter
        <RecyclerViewAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseYear;
        public ImageView imageView;
        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName1);
            tvReleaseYear = itemView.findViewById(R.id.tvReleaseYear);
            imageView = itemView.findViewById(R.id.ivMoviePoster);
        }
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }
    private List<Movie> movies;
    // Pass in the contact array into the constructor
    public RecyclerViewAdapter(List<Movie> movies) {
        this.movies = movies;
    }
    //This method creates a new view holder that is constructed with a new View, inflated from a layout
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View moviesView = inflater.inflate(R.layout.rv_layout, parent, false);
        // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(moviesView);
        return viewHolder;
    }
    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder,
                                 int position) {
        final Movie movie = movies.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName = viewHolder.tvMovieName;
        tvMovieName.setText(movie.getMovieName());
        TextView tvReleaseYear = viewHolder.tvReleaseYear;
        tvReleaseYear.setText((Integer.toString(movie.getReleaseYear())));
        ImageView imageView = viewHolder.imageView;
        String fullPath = "https://image.tmdb.org/t/p/w500/" + movie.getMoviePoster();
        Picasso.get()
                .load(fullPath)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(imageView);
    }
}
