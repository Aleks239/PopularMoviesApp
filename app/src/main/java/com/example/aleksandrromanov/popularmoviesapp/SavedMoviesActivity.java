package com.example.aleksandrromanov.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SavedMoviesActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener,LoaderManager.LoaderCallbacks<List<Movie>> {
    private RecyclerView mRecycleView;
    private List<Movie> mDataSource;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private static final String LOG_TAG = SavedMoviesActivity.class.getName();
    private static int MOVIE_POSTER_LOADER_ID = 1;


    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent movieDetailIntent = new Intent(context,destinationClass);
        movieDetailIntent.putExtra("com.example.aleksandrromanov.popularmoviesapp.Movie", movie);
        startActivity(movieDetailIntent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movies);

        mErrorTextView = (TextView) findViewById(R.id.tv_error_occurred);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecycleView = (RecyclerView)findViewById(R.id.rv_posters_view);

        mRecycleView.setHasFixedSize(true);

        mDataSource = new ArrayList<>();
        //mFavouriteMovies = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;

        mRecycleView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this,mDataSource,this);
        mRecycleView.setAdapter(mMovieAdapter);
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(MOVIE_POSTER_LOADER_ID,bundleForLoader,SavedMoviesActivity.this);

        List<MovieEntity> allEntities = MovieEntity.findWithQuery(MovieEntity.class, "Select * from MOVIE_ENTITY");
        for (MovieEntity e: allEntities) {
            Log.d("ALL" ,"Movie: " + e.identity + " is " + e.title);
        }

    }






    private static class MovieListLoader extends AsyncTaskLoader<List<Movie>> {

        private class ContextWrapper{
            Context context;

            ContextWrapper(Context c){
                this.context = c;
            }

            private Context getContext(){
                return context;
            }
        }

        private Bundle args;
        List<Movie> movies = null;
        private ContextWrapper wrapper;



        MovieListLoader(Context context, Bundle args){
            super(context);
            this.args = args;
            this.wrapper = new ContextWrapper(context);

        }

        @Nullable
        @Override
        public List<Movie> loadInBackground() {
            List<MovieEntity> allEntities = MovieEntity.findWithQuery(MovieEntity.class, "Select * from MOVIE_ENTITY");
            List<Movie> movies = new ArrayList<Movie>();
            Movie.MovieBuilder builder = Movie.newBuilder();
            for (MovieEntity e:allEntities) {
                Movie movie = builder.withTitle(e.title)
                        .addPoster(e.poster)
                        .addSynopsis(e.synopsis).addRating(e.rating)
                        .withDate(e.release).withTrailer(e.trailer).withId(e.identity).build();
                movies.add(movie);
            }
            return movies;
        }

        @Override
        protected void onStartLoading() {
            SavedMoviesActivity ma = (SavedMoviesActivity) this.wrapper.getContext();
            if(movies!=null){
                deliverResult(movies);
            }else{
                if(ma.mDataSource.size() < 1){
                    ma.showLoadingIndicator();
                }
                forceLoad();
            }

        }

        @Override
        public void deliverResult(@Nullable List<Movie> data) {
            movies = data;
            super.deliverResult(data);
        }
    }



    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {

        return new MovieListLoader(SavedMoviesActivity.this, args);


    }


    private void showMoviePosters(){
        mRecycleView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);

    }


    private void showLoadingIndicator(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(data == null || data.size() == 0){
            showErrorMessage();
        }
        else{
            if(mDataSource!=null)
                Log.d(LOG_TAG,"Amount of items in the dataSource is: " + mDataSource.size());

            else
                Log.d(LOG_TAG,"First time is null");

            mDataSource.addAll(data);
            mMovieAdapter.setMoviePosterPaths(mDataSource);
            showMoviePosters();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
