package com.example.luthiers.popularmovies.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


/*
 * The POJO needs:
 * -> title --> Type String
 * -> image --> Type String
 * -> overview --> Type String
 * -> releaseDate --> Type String
 * */

/*
 * Since we're using Room, instead of using direct SQLite Database.
 * Set the Movie as @Entity with the name for the table = movie_table
 * */

//We're using Parcelable implementation since we pass the movie via intent
@Entity(tableName = "movie_table")
public class Movie implements Parcelable {
    
    //We need to set a unique primary key for every entry, since we are getting the id for every movie we can set it as the primary key
    @PrimaryKey
    private int id;
    
    private String title;
    private String image;
    private String overview;
    private String releaseDate;
    
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
    
    
    //Tell Room to ignore this constructor
    @Ignore
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