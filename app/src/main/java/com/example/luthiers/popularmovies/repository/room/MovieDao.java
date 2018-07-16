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
    //Get the most popular movies
    @Query("SELECT * FROM movie_table WHERE queryAction = 0")
    LiveData<List<Movie>> getMostPopularMovies();
    
    //Get the top rated movies
    @Query("SELECT * FROM movie_table WHERE queryAction = 1")
    LiveData<List<Movie>> getTopRatedMovies();
    
    //Insert the movies into the database, use onConflictStrategy as ignore, since a movie can be marked as favorite, and then lose this mark
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long[] insertListMovies(List<Movie> movies);//Return long so that we can check if the data was successfully saved
    
    //Insert the movie the user marked as favorite, use onConflictStrategy as replace
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertMovieAsFavorite(Movie movie);
    
    //We want to get the favorite movies, 0 = ROOM_MOST_POPULAR and 1 = ROOM_TOP_RATED and 2 = ROOM_FAVORITE
    @Query("SELECT * FROM movie_table WHERE queryAction = 2")
    LiveData<List<Movie>> getFavoriteMovies();
}