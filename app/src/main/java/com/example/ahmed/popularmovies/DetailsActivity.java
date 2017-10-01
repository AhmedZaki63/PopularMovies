package com.example.ahmed.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, detailsFragment, "details_fragment").commit();
        } else {
            detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("details_fragment");
        }
    }
}
