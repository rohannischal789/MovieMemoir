package com.example.mymoviememoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.adapter.HomeRecycleViewAdapter;
import com.example.mymoviememoir.entity.Person;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Movie> movies;
    private HomeRecycleViewAdapter adapter;
    private NetworkConnection networkConnection;
    private TextView tvPersonName;
    private TextView tvDate;
    private TextView emptyView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTop5);
        networkConnection = new NetworkConnection();
        movies = new ArrayList<Movie>();
        tvPersonName = view.findViewById(R.id.tvPersonName);
        tvDate = view.findViewById(R.id.tvDate);
        emptyView = view.findViewById(R.id.empty_view);

        tvDate.setText("Today's Date: " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int personID = sharedPref.getInt("personID", 0);

        GetPerson getPerson = new GetPerson();
        getPerson.execute(personID);

        return view;
    }

    private class GetPerson extends AsyncTask<Integer, Void, Person> {

        @Override
        protected Person doInBackground(Integer... params) {
            return networkConnection.getPersonByID(params[0]);
        }

        @Override
        protected void onPostExecute(Person details) {
            tvPersonName.setText("Hello " + details.getFirstname());
            FindTop5UserRatedMovies movies = new FindTop5UserRatedMovies();
            movies.execute(details.getPersonid());
        }
    }

    private class FindTop5UserRatedMovies extends AsyncTask<Integer, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Integer... params) {
            return networkConnection.findTop5UserRatedMovies(params[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> details) {

            if (details.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                adapter = new HomeRecycleViewAdapter(details);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapter);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }
}