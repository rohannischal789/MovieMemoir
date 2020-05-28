package com.example.mymoviememoir.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymoviememoir.entity.Watchlist;

import java.util.List;

@Dao
public interface WatchlistDAO {
    //@Query("SELECT * FROM watchlist")
    //LiveData<List<Watchlist>> getAll();
    @Query("SELECT * FROM watchlist where person_id= :personId")
    LiveData<List<Watchlist>> getAllByPersonID(int personId);
    @Query("SELECT * FROM watchlist WHERE uid = :watchlistId LIMIT 1")
    Watchlist findByID(int watchlistId);
    @Insert
    void insertAll(Watchlist... watchlist);
    @Insert
    long insert(Watchlist watchlist);
    @Delete
    void delete(Watchlist watchlist);
    @Query("DELETE FROM watchlist")
    void deleteAll();
}
