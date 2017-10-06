package com.example.ahmed.popularmovies.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ahmed.popularmovies.Models.MovieResponse.Movie;
import com.example.ahmed.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends BaseAdapter {

    @BindView(R.id.list_item_image)
    ImageView moviePoster;
    private ArrayList<Movie> movieArrayList;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Movie getItem(int i) {
        return movieArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.movie_item, null);
        }

        ButterKnife.bind(this, view);
        final String BASE_URL = "https://image.tmdb.org/t/p/w185";

        Picasso.with(context)
                .load(BASE_URL + getItem(i).getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(moviePoster);

        return view;
    }

    public void setData(ArrayList<Movie> movieList) {
        movieArrayList = movieList;
        notifyDataSetChanged();
    }

    public void clearData() {
        movieArrayList.clear();
        notifyDataSetChanged();
    }
}
