package com.example.luthiers.popularmovies.repository.network;

import android.os.AsyncTask;

import com.example.luthiers.popularmovies.entities.Review;
import com.example.luthiers.popularmovies.utils.MovieUtils;

import java.io.IOException;
import java.util.List;

public class GetReviewsFromMovieAsyncTask extends AsyncTask<Integer, Void, List<Review>> {
    
    private GetReviewsFromMovieInterface delegate;
    
    public GetReviewsFromMovieAsyncTask(GetReviewsFromMovieInterface delegate) {
        this.delegate = delegate;
    }
    
    @Override
    protected List<Review> doInBackground(Integer... integers) {
        try {
            String jsonMovieReviews = MovieNetworkDataSource.getReviewsFromMovie(integers[0]);
        
            return MovieUtils.getReviewsFromJsonMovie(jsonMovieReviews);
        } catch (IOException e) {
            return null;
        }
    }
    
    @Override
    protected void onPostExecute(List<Review> reviews) {
        super.onPostExecute(reviews);
        
        delegate.getReviewsFromMovie(reviews);
    }
}