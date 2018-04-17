package com.example.aleksandrromanov.popularmoviesapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MovieDetail extends AppCompatActivity {


    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mMovieRating;
    private RatingBar mMovieRatingBar;
    private TextView mMovieOverview;
    private ImageView mMoviePoster;
    private Button mMovieTrailerBtn;
    private Button mMovieFavouriteBtn;
    private Movie mMovie;
    //private CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

         mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
         mMoviePoster = (ImageView) findViewById(R.id.movie_poster_detail);
         mMovieOverview = (TextView) findViewById(R.id.tv_movie_overview);
         mMovieRatingBar = (RatingBar) findViewById(R.id.tv_movie_rating_bar);
         mMovieRating = (TextView) findViewById(R.id.tv_movie_rating);
         mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
         mMovieTrailerBtn = (Button) findViewById(R.id.tv_movie_trailer_button);
         mMovieFavouriteBtn = (Button) findViewById(R.id.tv_movie_favourite_button);

         mMovieOverview.setMovementMethod(new ScrollingMovementMethod());

        Bundle b = this.getIntent().getExtras();
        if (b != null){
            mMovie = b.getParcelable("com.example.aleksandrromanov.popularmoviesapp.Movie");
            if(mMovie != null){
                Picasso.with(this).load(mMovie.getPoster()).into(mMoviePoster);
                mMovieRatingBar.setRating(Float.parseFloat(mMovie.getRating()));
                mMovieRating.setText(mMovie.getRating() + "/10");
                mMovieOverview.setText(mMovie.getSynopsis());
                mMovieTitle.setText(mMovie.getTitle());
                mReleaseDate.setText(mMovie.getReleaseDate());
                if(mMovie.getTrailerId() != null){
                    mMovieTrailerBtn.setEnabled(true);
                    mMovieTrailerBtn.setAlpha(1.0f);
                    mMovieTrailerBtn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mMovie.getTrailerId())));
                        }
                    });
                }
                else{
                    mMovieTrailerBtn.setEnabled(false);
                    mMovieTrailerBtn.setAlpha(0.5f);
                }

                updateFavouriteButtonIcon();
                mMovieFavouriteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.toggleFavouriteMovie(mMovie.getId());
                        updateFavouriteButtonIcon();
                    }
                });

            }
            else{
                Log.d("Null","Null");
            }

        }

        else{
            Log.d("AAAAAA","AAAAAA");
        }

    }

    private void updateFavouriteButtonIcon(){
        if(MainActivity.isFavouriteMovie(mMovie.getId())){
            mMovieFavouriteBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_heart_on,0);
        }
        else{
            mMovieFavouriteBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_heart_off,0);
        }
    }
}
