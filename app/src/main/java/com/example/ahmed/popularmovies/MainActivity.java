package com.example.ahmed.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.popularmovies.Models.MovieResponse;

public class MainActivity extends AppCompatActivity implements DataListener {

    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, mainFragment, "main_fragment").commit();
        } else {
            mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("main_fragment");
        }
        mainFragment.setDataListener(this);
    }

    @Override
    public void setMovieData(MovieResponse.Movie movie) {
        Bundle b = new Bundle();
        b.putParcelable("movie", movie);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
