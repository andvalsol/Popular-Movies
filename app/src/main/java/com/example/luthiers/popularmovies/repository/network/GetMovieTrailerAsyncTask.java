package com.example.luthiers.popularmovies.repository.network;

import android.os.AsyncTask;

import com.example.luthiers.popularmovies.utils.MovieUtils;

import java.io.IOException;
import java.util.List;

public class GetMovieTrailerAsyncTask extends AsyncTask<Integer, Void, List<String>> {
    
    private GetMovieTrailerInterface delegate;
    
    public GetMovieTrailerAsyncTask(GetMovieTrailerInterface delegate) {
        this.delegate = delegate;
    }
    
    @Override
    protected List<String> doInBackground(Integer... movieIds) {
        try {
            //Here we get the JSON from the trailer
            String jsonTrailer = MovieNetworkDataSource.getMovieTrailerKey(movieIds[0]);
    
            return MovieUtils.getMovieTrailerKeyFromJsonResource(jsonTrailer);
        } catch (IOException e) {
            return null;
        }
    }
    
    @Override
    protected void onPostExecute(List<String> movieTrailerKeys) {
        super.onPostExecute(movieTrailerKeys);
        
        delegate.getMovieTrailerKey(movieTrailerKeys);
    }
}