package com.example.luthiers.popularmovies.repository.network;

import android.os.AsyncTask;

import com.example.luthiers.popularmovies.utils.MovieUtils;

import java.io.IOException;

public class GetMovieTrailer extends AsyncTask<Integer, Void, String> {
    
    private GetMovieTrailerInterface delegate;
    
    public GetMovieTrailer(GetMovieTrailerInterface delegate) {
        this.delegate = delegate;
    }
    
    
    @Override
    protected String doInBackground(Integer... movieIds) {
        try {
            //Here we get the JSON from the trailer
            String jsonTrailer = MovieNetworkDataSource.getMovieTrailerKey(movieIds[0]);
    
            return MovieUtils.getMovieTrailerKeyFromJsonResource(jsonTrailer);
        } catch (IOException e) {
            return "";
        }
    }
    
    @Override
    protected void onPostExecute(String movieTrailerKey) {
        super.onPostExecute(movieTrailerKey);
        
        delegate.getMovieTrailerKey(movieTrailerKey);
    }
}