package com.example.ahmed.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements DataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainFragment mainFragment = new MainFragment();
        mainFragment.setDataListener(this);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, mainFragment).commit();
    }

    @Override
    public void setMovieData(String title, String id, String poster, String backdrop, String overview, String vote, String date) {
        Bundle b = new Bundle();
        b.putString("Id", id);
        b.putString("Title", title);
        b.putString("Cover", backdrop);
        b.putString("Poster", poster);
        b.putString("Overview", overview);
        b.putString("Vote", vote);
        b.putString("Date", date);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
