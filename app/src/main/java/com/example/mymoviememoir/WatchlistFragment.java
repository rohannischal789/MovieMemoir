package com.example.mymoviememoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.adapter.WatchlistRecyclerViewAdapter;
import com.example.mymoviememoir.database.WatchlistDatabase;
import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;

import java.util.List;

public class WatchlistFragment extends Fragment {

    WatchlistDatabase db = null;
    WatchlistViewModel watchlistViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Watchlist> movies;
    private WatchlistRecyclerViewAdapter adapter;
    public WatchlistFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        final View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        final TextView tvShowMessage = view.findViewById(R.id.tv_showmessage);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final int personID = sharedPref.getInt("personID",0);

        recyclerView = view.findViewById(R.id.recyclerViewWatchlist);
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getActivity().getApplication());
        watchlistViewModel.getAllWatchlist(personID).observe(this, new
                Observer<List<Watchlist>>() {
                    @Override
                    public void onChanged(@Nullable final List<Watchlist> watchlist) {
                        adapter = new WatchlistRecyclerViewAdapter(watchlist);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(adapter);
                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        /*String allCustomers = "";
                        for (Watchlist temp : watchlist) {
                            String customerstr = (temp.getMovieName() + " " +
                                    temp.getReleaseDate() + " " + temp.getWatchDateTime());
                            allCustomers = allCustomers +
                                    System.getProperty("line.separator") + customerstr;
                        }
                        tvShowMessage.setText("All data: " + allCustomers);*/
                    }
                });
        return view;
    }
}