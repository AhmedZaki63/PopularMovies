package com.example.ahmed.popularmovies;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.main_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.movies_view)
    GridView gridView;
    private DataListener dataListener;
    private MovieAdapter movieAdapter;
    private ArrayList<MovieResponse.Movie> movieArrayList;
    private MovieApi movieApi;
    private boolean prefrenceChanged;

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
        ButterKnife.bind(this, view);

        movieApi = MovieClient.createApi(MovieClient.buildRetrofit());

        //setup Movies ArrayList, Adapter and GridView
        movieArrayList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getContext(), movieArrayList);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataListener.setMovieData(movieAdapter.getItem(i));
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("movies")) {
            movieArrayList = savedInstanceState.getParcelableArrayList("movies");
            movieAdapter.setData(movieArrayList);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(
                getString(R.string.prefs_choices_list_key), getString(R.string.pref_default_choice));
        if (sortType.equals("favourite")) {
            MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
            movieArrayList = movieDbHelper.getAllFromDatabase();
            movieAdapter.setData(movieArrayList);
        } else if (prefrenceChanged || movieArrayList.isEmpty()) {
            getMoviesData(sortType);
            prefrenceChanged = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!movieArrayList.isEmpty())
            outState.putParcelableArrayList("movies", movieArrayList);
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public void getMoviesData(final String sortType) {
        movieAdapter.clearData();
        progressBar.setVisibility(View.VISIBLE);
        movieApi.getMoviesData(sortType, "en", BuildConfig.MOVIE_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null) {
                                movieArrayList = movieResponse.getResults();
                                movieAdapter.setData(movieArrayList);
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getMoviesData(sortType);
                            }
                        }).setNegativeButton("Cancell", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setMessage("Sorry no internet connection");
                        alertDialog.show();
                    }
                });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefs_choices_list_key)))
            prefrenceChanged = true;
    }
}
