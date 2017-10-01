package com.example.ahmed.popularmovies;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.ahmed.popularmovies.Adapters.MovieAdapter;
import com.example.ahmed.popularmovies.DataBase.MovieDbHelper;
import com.example.ahmed.popularmovies.Models.MovieResponse;
import com.example.ahmed.popularmovies.Network.MovieApi;
import com.example.ahmed.popularmovies.Network.MovieClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private DataListener dataListener;
    private MovieAdapter movieAdapter;
    private ArrayList<MovieResponse.Movie> movieArrayList;
    private ProgressBar progressBar;
    private GridView gridView;
    private MovieApi movieApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        progressBar = view.findViewById(R.id.main_progress_bar);
        gridView = view.findViewById(R.id.movies_view);

        movieApi = MovieClient.createApi(MovieClient.buildRetrofit());

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortByChoice = prefs.getString(
                getString(R.string.prefs_choices_list_key), getString(R.string.pref_default_choice));
        if (sortByChoice.equals("favourite")) {
            MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
            movieArrayList = movieDbHelper.getAllFromDatabase();
            movieAdapter = new MovieAdapter(getContext(), movieArrayList);
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dataListener.setMovieData(movieAdapter.getItem(i));
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            getMovies(sortByChoice);
        }
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public void getMovies(final String sortByChoice) {

        movieApi.getMoviesData(sortByChoice, "en", BuildConfig.MOVIE_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            movieArrayList = movieResponse.getResults();
                            movieAdapter = new MovieAdapter(getContext(), movieArrayList);
                            gridView.setAdapter(movieAdapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    dataListener.setMovieData(movieAdapter.getItem(i));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getMovies(sortByChoice);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().finish();
                            }
                        }).setCancelable(false);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Sorry No Internet Connection");
                        alertDialog.show();
                    }
                });

        progressBar.setVisibility(View.GONE);
    }
}
