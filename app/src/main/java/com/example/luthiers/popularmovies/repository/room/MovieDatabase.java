package com.example.luthiers.popularmovies.repository.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.luthiers.popularmovies.entities.Movie;


/*
* Use a singleton pattern for the MovieDatabase
* */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies_database";
    private static MovieDatabase sInstance;
    
    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            //Use lock object for multithreading safety
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .allowMainThreadQueries() //TODO remove this line!
                        .build();
            }
        }
        
        return sInstance;
    }
    
    public abstract MovieDao mMovieDao();
}