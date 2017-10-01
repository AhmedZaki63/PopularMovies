package com.example.ahmed.popularmovies;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.popularmovies.Adapters.ReviewAdapter;
import com.example.ahmed.popularmovies.Adapters.TrailerAdapter;
import com.example.ahmed.popularmovies.DataBase.MovieDbHelper;
import com.example.ahmed.popularmovies.Models.MovieResponse.Movie;
import com.example.ahmed.popularmovies.Models.ReviewResponse;
import com.example.ahmed.popularmovies.Models.TrailerResponse;
import com.example.ahmed.popularmovies.Network.MovieApi;
import com.example.ahmed.popularmovies.Network.MovieClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsFragment extends Fragment {

    ArrayList<ReviewResponse.Review> reviewList;
    LinearLayout noReviews, noTrailers;
    private String id, title, poster, cover, overview, vote, date;
    private FloatingActionButton favouriteButton;
    private ArrayList<TrailerResponse.Trailer> trailersList;
    private RecyclerView videoRecyclerView;
    private RecyclerView reviewRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();


        videoRecyclerView = view.findViewById(R.id.videos_view);
        reviewRecyclerView = view.findViewById(R.id.reviews_view);
        noReviews = view.findViewById(R.id.no_reviews);
        noTrailers = view.findViewById(R.id.no_trailers);

        final MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        Bundle bundle = getArguments();
        final Movie movie = bundle.getParcelable("movie");
        if (movie != null) {
            id = movie.getId();
            title = movie.getTitle();
            cover = movie.getBackdrop_path();
            poster = movie.getPoster_path();
            overview = movie.getOverview();
            vote = movie.getVote_average();
            date = movie.getRelease_date();
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle(title);
        }

        setDataToView(view);

        if (movieDbHelper.isSaved(movie))
            favouriteButton.setImageResource(R.drawable.ic_favorite_24dp);

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieDbHelper.isSaved(movie)) {
                    movieDbHelper.removeFromDatabase(movie);
                    favouriteButton.setImageResource(R.drawable.ic_favorite_border_24dp);
                } else {
                    movieDbHelper.addToDatabase(movie);
                    favouriteButton.setImageResource(R.drawable.ic_favorite_24dp);
                }
            }
        });

        MovieApi movieApi = MovieClient.createApi(MovieClient.buildRetrofit());

        movieApi.getVideosData(id, BuildConfig.MOVIE_API_KEY)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {

                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null) {
                                trailersList = trailerResponse.getResults();

                                videoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
                                videoRecyclerView.setNestedScrollingEnabled(false);

                                RecyclerView.Adapter trailerAdapter = new TrailerAdapter(getContext(), trailersList);
                                videoRecyclerView.setAdapter(trailerAdapter);
                            }

                            if (trailersList.isEmpty())
                                noTrailers.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Trailers Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        movieApi.getReviewsData(id, BuildConfig.MOVIE_API_KEY)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {

                            ReviewResponse reviewResponse = response.body();
                            if (reviewResponse != null) {
                                reviewList = reviewResponse.getResults();
                                reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));

                                RecyclerView.Adapter reviewAdapter = new ReviewAdapter(reviewList, getContext());
                                reviewRecyclerView.setAdapter(reviewAdapter);
                            }
                            if (reviewList.isEmpty())
                                noReviews.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Reviews Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    public void setDataToView(View view) {

        ImageView movieImageView = view.findViewById(R.id.details_image);

        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + cover)
                    .placeholder(R.drawable.placeholder)
                    .into(movieImageView);

        else
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w320" + poster)
                    .placeholder(R.drawable.placeholder)
                    .into(movieImageView);

        TextView titleText = view.findViewById(R.id.details_title);
        titleText.setText(title);

        TextView dateText = view.findViewById(R.id.details_date);
        dateText.setText(date);

        TextView voteText = view.findViewById(R.id.details_vote);
        if (vote == null || vote.equals("0"))
            voteText.setText(getText(R.string.no_vote_text));
        else
            voteText.setText("Rate : " + vote + "/10");

        TextView overviewText = view.findViewById(R.id.details_overview);
        overviewText.setText(overview);

        favouriteButton = view.findViewById(R.id.favourite_fab);
    }

}
