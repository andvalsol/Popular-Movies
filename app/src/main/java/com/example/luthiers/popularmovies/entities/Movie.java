package com.example.luthiers.popularmovies.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * The POJO needs:
 * -> id -> this is for the room database
 * -> title --> Type String
 * -> image --> Type String
 * -> overview --> Type String
 * -> releaseDate --> Type String
 * -> popularity --> Type Float
 * -> rating --> Type Float
 * -> favourite --> Type boolean
 * */

/*
 * Since we're using Room, instead of using direct SQLite Database.
 * Set the Movie as @Entity with the name for the table = movie_table
 *
 * The table should have all the properties from below
 * */

//We're using Parcelable implementation since we pass the movie via intent
@Entity(tableName = "movie_table")
public class Movie implements Parcelable {
    //We need to set a unique primary key for every entry, since we are getting the id for every movie (provided by the sever), we can set it as the primary key
    @PrimaryKey
    private int id;
    
    private String title;
    private String image;
    private String overview;
    private String releaseDate;
    
    private Float rating;
    private Float popularity;
    
    private int queryAction;
    
    //Room require a constructor
    public Movie(int id, String title,
                 String image,
                 String overview,
                 String releaseDate,
                 Float rating,
                 Float popularity,
                 int queryAction) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.popularity = popularity;
        this.queryAction = queryAction;
    }
    
    @Ignore //Ignore this constructor, for Room logic
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readFloat();
        }
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readFloat();
        }
        queryAction = in.readInt();
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
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
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
    
    public Float getPopularity() {
        return popularity;
    }
    
    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }
    
    public int getQueryAction() {
        return queryAction;
    }
    
    public void setQueryAction(int queryAction) {
        this.queryAction = queryAction;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
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
        if (popularity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(popularity);
        }
        dest.writeInt(queryAction);
    }
}