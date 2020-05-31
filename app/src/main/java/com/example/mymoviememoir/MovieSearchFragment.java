package com.example.mymoviememoir;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.adapter.RecyclerViewAdapter;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText etMovieName;
    private Button btnSearch;
    private List<Movie> movies;
    private RecyclerViewAdapter adapter;
    private TextView emptyView;

    private TextView textView;
    private NetworkConnection networkConnection;
    public MovieSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.movie_search_fragment, container, false);
        ((MainActivity)getActivity()).setActionBarTitle("Movie Search");
        recyclerView = view.findViewById(R.id.recyclerView);
        networkConnection = new NetworkConnection();
        movies = new ArrayList<Movie>();
        emptyView = view.findViewById(R.id.empty_view);

        etMovieName = view.findViewById(R.id.etMovieName);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = etMovieName.getText().toString().trim();
                if (!movieName.isEmpty()) {
                    findMovieByName movieByName = new findMovieByName();
                    movieByName.execute(movieName);

                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a value to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private class findMovieByName extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            return networkConnection.findByMovieName(params[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> details) {
            if (details.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                adapter = new RecyclerViewAdapter(details);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapter);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
            }else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }
}