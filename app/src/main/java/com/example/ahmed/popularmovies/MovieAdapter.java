package com.example.ahmed.popularmovies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MovieAdapter extends BaseAdapter {

    private ArrayList<Movie> movieArrayList;
    private Context context;

    MovieAdapter(Context context, ArrayList<Movie> movieArrayList) {
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

        final String BASE_URL = "https://image.tmdb.org/t/p/w185";

        ImageView moviePoster = view.findViewById(R.id.list_item_image);
        Picasso.with(context)
                .load(BASE_URL + getItem(i).getPoster())
                .into(moviePoster);

        return view;
    }

    void addAll(ArrayList<Movie> movieList) {
        movieArrayList.addAll(movieList);
    }

    void clear() {
        movieArrayList.clear();
    }
}
