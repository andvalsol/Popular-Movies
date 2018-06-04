package com.example.luthiers.popularmovies.repository.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.luthiers.popularmovies.entities.Movie;

import java.util.List;

/*
* For this Data Access Object we only need to get all the movies from the data base and insert a list of movies
* */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie_table")
    List<Movie> getAllMovies();
    
    @Insert
    void insertListMovies(List<Movie> movies);
    
}