package com.example.aleksandrromanov.popularmoviesapp;

/**
 * Created by aleksandrromanov on 02/03/2017.
 */

class Movie {

    private final String title;
    private final String poster;
    private final String synopsis;
    private final String rating;
    private final String releaseDate;

    public String getTitle(){
        return this.title;
    }

    private Movie(String title, String poster, String synopsis, String rating, String releaseDate){
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;

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

        Movie build(){
            return new Movie(title,moviePoster,plotSynopsis,rating,releaseDate);
        }


    }




}
