package com.example.luthiers.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luthiers.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    
    private List<String> mMovieTrailerIds;
    
    private TrailersInterface mTrailersInterface;
    
    public TrailersAdapter(List<String> movieTrailerIds, TrailersInterface trailersInterface) {
        mMovieTrailerIds = movieTrailerIds;
        
        mTrailersInterface = trailersInterface;
    }
    
    @NonNull
    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize the specific view for this MovieViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        
        return new TrailerViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.TrailerViewHolder holder, int position) {
        holder.bind(mMovieTrailerIds.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mMovieTrailerIds.size();
    }
    
    class TrailerViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView mMovieThumbnail;
        
        TrailerViewHolder(View itemView) {
            super(itemView);
            
            mMovieThumbnail = itemView.findViewById(R.id.iv_trailer_thumbnail);
            mMovieThumbnail.setOnClickListener(v -> mTrailersInterface.trailerOnClickListener(mMovieTrailerIds.get(getAdapterPosition())));
        }
        
        void bind(String movieTrailerId) {
            Picasso.get()
                    .load("http://img.youtube.com/vi/" + movieTrailerId + "/0.jpg")
                    .into(mMovieThumbnail);
        }
    }
    
    public interface TrailersInterface {
        void trailerOnClickListener(String movieTrailerKey);
    }
}