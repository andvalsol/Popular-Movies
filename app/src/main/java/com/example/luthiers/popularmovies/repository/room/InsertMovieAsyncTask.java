package com.example.luthiers.popularmovies.repository.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.luthiers.popularmovies.entities.Movie;

import java.lang.ref.WeakReference;

public class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Long> {
    
    private WeakReference<Context> mContextWeakReference;
    private InsertMovieInterface mDelegate;
    
    public InsertMovieAsyncTask(Context context, InsertMovieInterface insertMovieInterface) {
        mContextWeakReference = new WeakReference<>(context);
        mDelegate = insertMovieInterface;
    }
    
    @Override
    protected Long doInBackground(Movie... movies) {
        return MovieDatabase.getInstance(mContextWeakReference.get()).mMovieDao().insertMovieAsFavorite(movies[0]);
    }
    
    @Override
    protected void onPostExecute(Long aLong) {
        if (aLong > 0) {
            //The movie was successfully inserted
            mDelegate.movieInserted(true);
        } else {
            mDelegate.movieInserted(false);
        }
    }
    
    public interface InsertMovieInterface {
        void movieInserted(boolean isMarkedAsFavorite);
    }
}
