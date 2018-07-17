package com.example.luthiers.popularmovies.repository.room.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.luthiers.popularmovies.entities.Movie;
import com.example.luthiers.popularmovies.repository.room.MovieDatabase;

import java.lang.ref.WeakReference;

public class RemoveMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
    
    private WeakReference<Context> mContextWeakReference;
    
    public RemoveMovieAsyncTask(Context context) {
        mContextWeakReference = new WeakReference<>(context);
    }
    
    @Override
    protected Void doInBackground(Movie... movies) {
        int removed = MovieDatabase.getInstance(mContextWeakReference.get()).mMovieDao().removeMovieFromFavorites(movies[0]);
    
        Log.d("Removed", "Removed " + removed);
        
        return null;
    }
}