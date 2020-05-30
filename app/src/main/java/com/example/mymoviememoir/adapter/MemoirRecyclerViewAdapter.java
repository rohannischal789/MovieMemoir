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

import com.example.mymoviememoir.MovieViewActivity;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entity.Memoir;
import com.example.mymoviememoir.entity.MemoirDetail;
import com.example.mymoviememoir.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemoirRecyclerViewAdapter extends RecyclerView.Adapter<MemoirRecyclerViewAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseDate;
        public TextView tvCinemaPostcode;
        public TextView tvWatchDate;
        public TextView tvComment;
        public ImageView imgPoster;
        public RatingBar ratingBar;


        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName6);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate6);
            ratingBar = itemView.findViewById(R.id.ratingBar6);
            tvCinemaPostcode = itemView.findViewById(R.id.tvCinemaPostcode);
            tvWatchDate = itemView.findViewById(R.id.tvWatchDate6);
            tvComment = itemView.findViewById(R.id.tvComment);
            imgPoster = itemView.findViewById(R.id.imgMoviePoster);
            context = itemView.getContext();
        }
    }

    @Override
    public int getItemCount() {
        return memoirs.size();
    }

    private List<MemoirDetail> memoirs;

    // Pass in the contact array into the constructor
    public MemoirRecyclerViewAdapter(List<MemoirDetail> memoirs) {
        this.memoirs = memoirs;
    }

    //This method creates a new view holder that is constructed with a new View, inflated from a layout
    @Override
    public MemoirRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View moviesView = inflater.inflate(R.layout.rv_memoir_layout, parent, false);
        // construct the viewholder with the new view
        MemoirRecyclerViewAdapter.ViewHolder viewHolder = new MemoirRecyclerViewAdapter.ViewHolder(moviesView);
        return viewHolder;
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull MemoirRecyclerViewAdapter.ViewHolder viewHolder,
                                 int position) {
        final MemoirDetail mem = memoirs.get(position);
        // viewholder binding with its data at the specified position
        final TextView tvMovieName = viewHolder.tvMovieName;
        tvMovieName.setText(mem.getMoviename());
        TextView tvReleaseYear = viewHolder.tvReleaseDate;
        tvReleaseYear.setText(new SimpleDateFormat("dd-MM-yyyy").format(mem.getMoviereleasedate()));
        TextView tvWatchDate = viewHolder.tvWatchDate;
        tvWatchDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(mem.getWatchdatetime()));
        TextView tvCinemaPostcode = viewHolder.tvCinemaPostcode;
        tvCinemaPostcode.setText(mem.getCinemaid().getPostcode());
        TextView tvComment = viewHolder.tvComment;
        tvComment.setText(mem.getComment());
        RatingBar ratingBar = viewHolder.ratingBar;
        ratingBar.setRating(mem.getStarrating().floatValue());

        ImageView imageView = viewHolder.imgPoster;
        String fullPath = "https://image.tmdb.org/t/p/w500/" + mem.getPosterPath();
        Picasso.get()
                .load(fullPath)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieViewActivity.class);
                Movie movie = new Movie();
                movie.setMovieID(mem.getMovieId());
                movie.setReleaseDate(new Date());
                movie.setMovieName(tvMovieName.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedMovie", movie);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
}
