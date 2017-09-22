package com.example.ahmed.popularmovies;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class MainFragment extends Fragment {

    private DataListener dataListener;
    private MoviesTask moviesTask;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieArrayList;
    private ProgressBar progressBar;
    private String sortByChoice;

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

        movieArrayList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getContext(), movieArrayList);

        progressBar = view.findViewById(R.id.main_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        GridView gridView = view.findViewById(R.id.movies_view);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie selectedMovie = movieAdapter.getItem(i);

                String movieId = selectedMovie.getId();
                String movieTitle = selectedMovie.getTitle();
                String movieBackdrop = selectedMovie.getBackdrop();
                String moviePoster = selectedMovie.getPoster();
                String movieOverview = selectedMovie.getOverview();
                String movieVote = selectedMovie.getVote();
                String movieDate = selectedMovie.getDate();
                dataListener.setMovieData(movieTitle, movieId, moviePoster, movieBackdrop, movieOverview, movieVote, movieDate);
            }
        });

        return view;
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortByChoice = prefs.getString(
                getString(R.string.prefs_choices_list_key), getString(R.string.pref_default_choice));
        moviesTask = new MoviesTask();
        moviesTask.execute(sortByChoice);
    }

    private class MoviesTask extends AsyncTask<String, Void, Movie[]> {

        final String LOG_TAG = MoviesTask.class.getSimpleName();

        private Movie[] getDataFromJson(String jsonString) throws JSONException {
            JSONObject moviesJsonObject = new JSONObject(jsonString);
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray("results");

            String title, id, poster, backdrop, overview, vote, date;
            Movie[] moviesArray = new Movie[moviesJsonArray.length()];

            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject obj = moviesJsonArray.getJSONObject(i);
                title = obj.getString("title");
                id = obj.getString("id");
                poster = obj.getString("poster_path");
                backdrop = obj.getString("backdrop_path");
                overview = obj.getString("overview");
                vote = obj.getString("vote_average");
                date = obj.getString("release_date");
                moviesArray[i] = new Movie(title, id, poster, backdrop, overview, vote, date);
            }

            return moviesArray;
        }

        private String getResponseFromHttpUrl(URL url) {
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line = reader.readLine();
                builder.append(line);
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            String movieJsonStr;
            URL url = null;
            final String BASE_URL = "http://api.themoviedb.org/3/movie";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(strings[0])
                    .appendQueryParameter("language", "en")
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_API_KEY)
                    .build();

            try {
                Log.v(LOG_TAG, builtUri.toString());
                url = new URL(builtUri.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            movieJsonStr = getResponseFromHttpUrl(url);

            try {
                if (movieJsonStr != null)
                    return getDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] resultMovies) {
            progressBar.setVisibility(View.GONE);

            if (resultMovies != null) {
                movieArrayList = new ArrayList<>(Arrays.asList(resultMovies));
                movieAdapter.clear();
                movieAdapter.addAll(movieArrayList);
                movieAdapter.notifyDataSetChanged();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moviesTask = new MoviesTask();
                        moviesTask.execute(sortByChoice);
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
        }
    }
}
