package com.example.mymoviememoir.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.MovieViewActivity;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter<WatchlistRecyclerViewAdapter.ViewHolder> {
    private Context context;
    WatchlistViewModel watchlistViewModel;
    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the
        public TextView tvMovieName;
        public TextView tvReleaseDate;
        public TextView tvWatchDateTime;
        public ImageButton btnView;
        public ImageButton btnDelete;

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

    public WatchlistRecyclerViewAdapter(List<Watchlist> watchlist, WatchlistViewModel watchlistViewModel1) {
        this.watchlists = watchlist;
        watchlistViewModel = watchlistViewModel1;
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

        viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieViewActivity.class);
                Bundle bundle = new Bundle();
                Movie movie = new Movie();
                movie.setMovieID(watchlist.getMovieID());
                movie.setMovieName(watchlist.getMovieName());
                movie.setReleaseDate(watchlist.getReleaseDate());
                bundle.putParcelable("selectedMovie", movie);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("You are about to delete " + watchlist.getMovieName() +" from your watchlist. Do you really want to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        watchlistViewModel.delete(watchlist);
                        Toast.makeText(context, "Watchlist was successfully deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Delete cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });


    }
}