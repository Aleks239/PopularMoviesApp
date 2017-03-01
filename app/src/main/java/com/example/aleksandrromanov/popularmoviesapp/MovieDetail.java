package com.example.aleksandrromanov.popularmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetail extends AppCompatActivity {

    private TextView mMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieDetail = (TextView) findViewById(R.id.tv_movie_detail);

        Intent movieDetailIntent = getIntent();
        if(movieDetailIntent != null){
            if (movieDetailIntent.hasExtra(Intent.EXTRA_TEXT)){
                String movie = movieDetailIntent.getStringExtra(Intent.EXTRA_TEXT);
                mMovieDetail.setText(movie);
            }
        }
    }
}
