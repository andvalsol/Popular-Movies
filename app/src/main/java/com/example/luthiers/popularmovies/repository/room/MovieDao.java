package com.example.luthiers.popularmovies.repository.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.example.luthiers.popularmovies.entities.Movie;

import java.util.List;

/*
* For this Data Access Object we only need to get all the movies from the data base and insert a list of movies
* */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie_table ORDER BY popularity DESC") //Get the most popular movies in descending order
    LiveData<List<Movie>> getMostPopularMovies();
    
    @Query("SELECT * FROM movie_table ORDER BY rating DESC") //Get the top rated movies in descending order
    LiveData<List<Movie>> getTopRatedMovies();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE) //We want to replace, since the rating can be updated
    Long[] insertListMovies(List<Movie> movies);//Return long so that we can check if the data was successfully saved
    
}