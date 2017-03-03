package com.example.aleksandrromanov.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aleksandrromanov on 03/02/2017.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviePosterViewHolder> {


    public interface MovieItemClickListener{
        void onClick(Movie movie);
    }

    private final MovieItemClickListener mMovieClickHandler;

    private List<Movie> mImagesDataSource;
    private final static String LOG_TAG = MovieAdapter.class.getName();
    private Context mContext;



    public MovieAdapter(Context c, List<Movie> data, MovieItemClickListener clickHandler){
        mMovieClickHandler = clickHandler;
        mContext = c;
        mImagesDataSource = data;

    }

    public void setMoviePosterPaths(List<Movie> source){
        mImagesDataSource = source;
        notifyDataSetChanged();
    }


    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.movie_poster_item;
        View imageView = LayoutInflater.from(context).inflate(layout,parent,false);
        return new MoviePosterViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        Picasso.with(mContext).load(mImagesDataSource.get(position).getPoster()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(mImagesDataSource == null) return  0;
        else return mImagesDataSource.size();
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie  movie = mImagesDataSource.get(adapterPosition);
            mMovieClickHandler.onClick(movie);
        }

        private ImageView imageView;

        private MoviePosterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster_item);
            itemView.setOnClickListener(this);
        }


    }


}
