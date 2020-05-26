package com.example.mymoviememoir.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entity.Watchlist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<WatchlistRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private int checkedPosition = 0;
    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseDate;
        public TextView tvWatchDateTime;
        public Button btnView;
        public Button btnDelete;

        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName5);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate2);
            tvWatchDateTime = itemView.findViewById(R.id.tvWatchDate);
            btnView = itemView.findViewById(R.id.btnView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            context = itemView.getContext();
        }
    }

    @Override
    public int getItemCount() {
        return watchlists.size();
    }

    private List<Watchlist> watchlists;

    public WatchlistRecyclerViewAdapter(List<Watchlist> watchlist) {
        this.watchlists = watchlist;
    }

    //This method creates a new view holder that is constructed with a new View, inflated from a layout
    @Override
    public WatchlistRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View watchlistView = inflater.inflate(R.layout.rv_watchlist_layout, parent, false);
        // construct the viewholder with the new view
        WatchlistRecyclerViewAdapter.ViewHolder viewHolder = new WatchlistRecyclerViewAdapter.ViewHolder(watchlistView);
        return viewHolder;
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull final WatchlistRecyclerViewAdapter.ViewHolder viewHolder,
                                 int position) {
        final Watchlist watchlist = watchlists.get(position);
        // viewholder binding with its data at the specified position
        final TextView tvMovieName = viewHolder.tvMovieName;
        tvMovieName.setText(watchlist.getMovieName());
        TextView tvReleaseDate = viewHolder.tvReleaseDate;
        tvReleaseDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(watchlist.getReleaseDate()));
        TextView tvWatchDate = viewHolder.tvWatchDateTime;
        tvWatchDate.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(watchlist.getWatchDateTime()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public Watchlist getSelected() {
        if (checkedPosition != -1) {
            return watchlists.get(checkedPosition);
        }
        return null;
    }
}