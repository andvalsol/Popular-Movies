package com.example.luthiers.popularmovies.repository.room.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.room.MovieDatabase;

import java.lang.ref.WeakReference;

public class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Long> {
    
    private WeakReference<Context> mContextWeakReference;
    
    public InsertMovieAsyncTask(Context context) {
        mContextWeakReference = new WeakReference<>(context);
    }
    
    @Override
    protected Long doInBackground(Movie... movies) {
        return MovieDatabase.getInstance(mContextWeakReference.get()).mMovieDao().insertMovieAsFavorite(movies[0]);
    }
}