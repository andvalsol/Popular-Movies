package com.example.luthiers.popularmovies.repository.network;

import com.example.luthiers.popularmovies.entities.Review;

import java.util.List;

public interface GetReviewsFromMovieInterface {
    void getReviewsFromMovie(List<Review> movieReviews);
}