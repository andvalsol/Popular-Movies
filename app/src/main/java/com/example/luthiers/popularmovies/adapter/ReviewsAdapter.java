package com.example.luthiers.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luthiers.popularmovies.R;
import com.example.luthiers.popularmovies.entities.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    
    private List<Review> mReviews;
    private ReviewsInterface mReviewsInterface;
    
    public ReviewsAdapter(List<Review> reviews, ReviewsInterface reviewsInterface) {
        mReviews = reviews;
        mReviewsInterface = reviewsInterface;
    }
    
    @NonNull
    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize the specific view for this MovieViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
    
        return new ReviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewViewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mReviews.size();
    }
    
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        
        private TextView mtvReview;
        private TextView mtvAuthor;
    
        ReviewViewHolder(View itemView) {
            super(itemView);
    
            mtvAuthor = itemView.findViewById(R.id.tv_review_author);
            mtvReview = itemView.findViewById(R.id.tv_review_content);
            
            itemView.setOnClickListener(v -> mReviewsInterface.recyclerViewOnClick());
        }
        
        void bind(Review review) {
            mtvReview.setText(review.getReview());
            mtvAuthor.setText(review.getAuthor());
        }
    }
    
    public interface ReviewsInterface {
        void recyclerViewOnClick();
    }
}