package com.example.luthiers.popularmovies.pojos;

/*
 * The POJO needs:
 * -> title --> Type String
 * -> image --> Type String
 * -> overview --> Type String
 * -> releaseDate --> Type String
 * */

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    
    private String title, image, overview, releaseDate;
    private Float rating;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public Float getRating() {
        return rating;
    }
    
    public void setRating(Float rating) {
        this.rating = rating;
    }
    
    public Movie(String title, String image, String overview, String releaseDate, Float rating) {
        this.title = title;
        this.image = image;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }
    
    
    protected Movie(Parcel in) {
        title = in.readString();
        image = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readFloat();
        }
    }
    
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(rating);
        }
    }
}