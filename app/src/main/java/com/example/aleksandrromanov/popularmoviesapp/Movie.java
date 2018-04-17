package com.example.aleksandrromanov.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.System.out;

/**
 * Created by aleksandrromanov on 02/03/2017.
 */

/**
 * The ideas of parcelable implementation was taken from
 * https://github.com/erangaeb/dev-notes/blob/master/android-parcelable/User.java
 */

class Movie implements Parcelable {

    private  String title;
    private  String poster;
    private  String synopsis;
    private  String rating;
    private  String releaseDate;
    private  String trailerId;
    private  int id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.poster);
        parcel.writeString(this.rating);
        parcel.writeString(this.synopsis);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.trailerId);
        parcel.writeInt(this.id);
    }



    private Movie(Parcel in) {
        this.title = in.readString();
        this.poster = in.readString();
        this.rating = in.readString();
        this.synopsis = in.readString();
        this.releaseDate = in.readString();
        this.trailerId = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public String getTitle(){
        return this.title;
    }

    public String getPoster(){
        return this.poster;

    }

    public String getSynopsis(){
        return this.synopsis;
    }

    public String getRating(){
        return this.rating;
    }

    public String getReleaseDate(){
        return this.releaseDate;
    }

    public String getTrailerId() { return this.trailerId; }

    public int getId() { return this.id; }

    private Movie(String title, String poster, String synopsis, String rating, String releaseDate, String trailerId, int id){
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.trailerId = trailerId;
        this.id = id;
    }

    static MovieBuilder newBuilder(){
        return new MovieBuilder();
    }

    static class MovieBuilder{

        String title;
        String moviePoster;
        String plotSynopsis;
        String rating;
        String releaseDate;
        String trailerId;
        int id;

        MovieBuilder withTitle(String title){
            this.title = title;
            return this;
        }
        MovieBuilder addPoster(String poster){
            this.moviePoster = poster;
            return this;
        }
        MovieBuilder addSynopsis(String synopsis){
            this.plotSynopsis = synopsis;
            return this;
        }
        MovieBuilder addRating(String rating){
            this.rating = rating;
            return this;
        }
        MovieBuilder withDate(String date){
            this.releaseDate = date;
            return this;
        }

        MovieBuilder withTrailer(String id){
            this.trailerId = id;
            return this;
        }

        MovieBuilder withId(int id){
            this.id = id;
            return this;
        }

        Movie build(){
            return new Movie(this.title,this.moviePoster,this.plotSynopsis,this.rating,this.releaseDate,this.trailerId,this.id);
        }


    }




}
