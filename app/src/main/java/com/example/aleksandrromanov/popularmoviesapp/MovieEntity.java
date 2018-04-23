package com.example.aleksandrromanov.popularmoviesapp;

import com.orm.SugarRecord;

public class MovieEntity extends SugarRecord<MovieEntity> {

      int identity;
      String title;
      String poster;
      String synopsis;
      String rating;
      String release;
      String trailer;



      public MovieEntity(){}

      public MovieEntity(int identity, String title, String poster, String synopsis, String rating, String release, String trailer){
          this.identity = identity;
          this.title = title;
          this.poster = poster;
          this.synopsis = synopsis;
          this.rating = rating;
          this.release = release;
          this.trailer = trailer;




      }

}
