package com.example.mymoviememoir.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.repository.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {
    private WatchlistRepository wRepository;
    private MutableLiveData<List<Watchlist>> allWatchlist;
    public WatchlistViewModel () {
        allWatchlist=new MutableLiveData<>();
    }
    public void setWatchlist(List<Watchlist> watchlists) {
        allWatchlist.setValue(watchlists);
    }
    public LiveData<List<Watchlist>> getAllWatchlist(int personID) {
        return wRepository.getAllWatchlist(personID);
    }
    public void initalizeVars(Application application){
        wRepository = new WatchlistRepository(application);
    }
    public void insert(Watchlist watchlist) {
        wRepository.insert(watchlist);
    }
    public void insertAll(Watchlist... watchlist) {
        wRepository.insertAll(watchlist);
    }
    public void deleteAll() {
        wRepository.deleteAll();
    }
    public Watchlist findByID(int watchlistId){
        return wRepository.findByID(watchlistId);
    }
}