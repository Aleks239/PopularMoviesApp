package com.example.aleksandrromanov.popularmoviesapp;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {


    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mMovieRating;
    private TextView mMovieOverview;
    private ImageView mMoviePoster;
    private Movie mMovie;
    //private CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

         mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
         mMoviePoster = (ImageView) findViewById(R.id.movie_poster_detail);
         mMovieOverview = (TextView) findViewById(R.id.tv_movie_overview);
         mMovieRating = (TextView) findViewById(R.id.tv_movie_rating);
         mReleaseDate = (TextView) findViewById(R.id.tv_release_date);

         mMovieOverview.setMovementMethod(new ScrollingMovementMethod());

        Bundle b = this.getIntent().getExtras();
        if (b != null){
            mMovie = b.getParcelable("com.example.aleksandrromanov.popularmoviesapp.Movie");
            if(mMovie != null){
                Picasso.with(this).load(mMovie.getPoster()).into(mMoviePoster);
                mMovieRating.setText(mMovie.getRating() + "/10");
                mMovieOverview.setText(mMovie.getSynopsis());
                mMovieTitle.setText(mMovie.getTitle());
                mReleaseDate.setText(mMovie.getReleaseDate());
            }
            else{
                Log.d("Null","Null");
            }

        }

        else{
            Log.d("AAAAAA","AAAAAA");
        }

    }
}
