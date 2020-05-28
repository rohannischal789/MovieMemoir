package com.example.mymoviememoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mymoviememoir.dao.WatchlistDAO;
import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.utilities.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Watchlist.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class WatchlistDatabase extends RoomDatabase {
    public abstract WatchlistDAO watchlistDAO();
    private static WatchlistDatabase INSTANCE;
    //we create an ExecutorService with a fixed thread pool so we can later run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static synchronized WatchlistDatabase getInstance(final Context
                                                                    context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchlistDatabase.class, "WatchlistDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
