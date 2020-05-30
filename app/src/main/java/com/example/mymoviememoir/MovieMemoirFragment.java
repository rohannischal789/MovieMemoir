package com.example.mymoviememoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.adapter.MemoirRecyclerViewAdapter;
import com.example.mymoviememoir.entity.MemoirDetail;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MovieMemoirFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MemoirDetail> memoirs;
    private MemoirRecyclerViewAdapter adapter;
    private NetworkConnection networkConnection;
    private Spinner sortSpinner;
    private Spinner genreSpinner;

    public MovieMemoirFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.movie_memoir_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMemoir);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        genreSpinner = view.findViewById(R.id.genreSpinner);
        networkConnection = new NetworkConnection();
        memoirs = new ArrayList<MemoirDetail>();
        GetAllGenres getAllGenres = new GetAllGenres();
        getAllGenres.execute();
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                String selectedSort = parent.getItemAtPosition(position).toString();
                switch (selectedSort.toLowerCase()) {
                    case "watch date":
                        Collections.sort(memoirs, new Comparator<MemoirDetail>() {
                            @Override
                            public int compare(MemoirDetail m1, MemoirDetail m2) {
                                return m2.getWatchdatetime().compareTo(m1.getWatchdatetime());
                            }
                        });
                        break;
                    case "user rating":
                        Collections.sort(memoirs, new Comparator<MemoirDetail>() {
                            @Override
                            public int compare(MemoirDetail m1, MemoirDetail m2) {
                                return Double.compare(m2.getStarrating(), m1.getStarrating());
                            }
                        });
                        break;
                    case "public rating":
                        Collections.sort(memoirs, new Comparator<MemoirDetail>() {
                            @Override
                            public int compare(MemoirDetail m1, MemoirDetail m2) {
                                return Double.compare(m2.getPublicRating(), m1.getPublicRating());
                            }
                        });
                        break;
                }
                updateData(memoirs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            final List<MemoirDetail> filteredData = new ArrayList<>();
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                String selectedGenre = parent.getItemAtPosition(position).toString();
                if (!selectedGenre.equalsIgnoreCase("All")) {
                    for (MemoirDetail mem : memoirs) {
                        for (String genre : mem.getGenres()) {
                            if (genre.equalsIgnoreCase(selectedGenre)) {
                                filteredData.add(mem);
                            }
                        }
                    }
                }
                else
                {
                    filteredData.addAll(memoirs);
                }
                updateData(filteredData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SharedPreferences sharedPref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int personID = sharedPref.getInt("personID", 0);
        GetMemoirs getMemoirs = new GetMemoirs();
        getMemoirs.execute(personID);
        return view;
    }

    public void updateData(List<MemoirDetail> data) {
        adapter = new MemoirRecyclerViewAdapter(data);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private class GetMemoirs extends AsyncTask<Integer, Void, List<MemoirDetail>> {

        @Override
        protected List<MemoirDetail> doInBackground(Integer... params) {
            return networkConnection.getMemoirs(params[0]);
        }

        @Override
        protected void onPostExecute(List<MemoirDetail> details) {
            memoirs = details;
            Collections.sort(memoirs, new Comparator<MemoirDetail>() {
                @Override
                public int compare(MemoirDetail m1, MemoirDetail m2) {
                    return m2.getWatchdatetime().compareTo(m1.getWatchdatetime());
                }
            });
            updateData(memoirs);
        }
    }

    private class GetAllGenres extends AsyncTask<Void, Void, HashMap<Integer, String>> {

        @Override
        protected HashMap<Integer, String> doInBackground(Void... voids) {
            return networkConnection.getAllGenres();
        }

        @Override
        protected void onPostExecute(HashMap<Integer, String> details) {

            final List<String> list = new ArrayList<String>();
            list.add("All");
            for (Integer key : details.keySet()) {
                list.add(details.get(key));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genreSpinner.setAdapter(adapter);

        }
    }
}
