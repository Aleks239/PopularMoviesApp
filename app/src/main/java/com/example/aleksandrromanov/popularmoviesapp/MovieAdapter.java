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

    private List<String> mImagesDataSource;
    private final static String LOG_TAG = MovieAdapter.class.getName();
    private Context mContext;

    public MovieAdapter(Context c, List<String> data){
        mContext = c;
        mImagesDataSource = data;

    }

    public void setMoviePosterPaths(List<String> source){
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
        Picasso.with(mContext).load(mImagesDataSource.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(mImagesDataSource == null) return  0;
        else return mImagesDataSource.size();
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster_item);
        }
    }


}
