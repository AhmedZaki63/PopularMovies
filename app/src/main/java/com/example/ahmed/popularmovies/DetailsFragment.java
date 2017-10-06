package com.example.ahmed.popularmovies;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsFragment extends Fragment {

    protected ReviewAdapter reviewAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.details_image)
    ImageView movieImageView;
    @BindView(R.id.favourite_fab)
    FloatingActionButton favouriteButton;
    @BindView(R.id.details_title)
    TextView titleText;
    @BindView(R.id.details_date)
    TextView dateText;
    @BindView(R.id.details_vote)
    TextView voteText;
    @BindView(R.id.details_overview)
    TextView overviewText;
    @BindView(R.id.trailers_view)
    RecyclerView trailerRecyclerView;
    @BindView(R.id.reviews_view)
    RecyclerView reviewRecyclerView;
    @BindView(R.id.no_trailers)
    TextView noTrailers;
    @BindView(R.id.no_reviews)
    TextView noReviews;
    private Movie movie;
    private ArrayList<TrailerResponse.Trailer> trailersList;
    private ArrayList<ReviewResponse.Review> reviewList;
    private TrailerAdapter trailerAdapter;
    private MovieApi movieApi;
    private MovieDbHelper movieDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        movie = getArguments().getParcelable("movie");
        movieApi = MovieClient.createApi(MovieClient.buildRetrofit());
        movieDbHelper = new MovieDbHelper(getContext());

        setDataToView();

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

        //setup Trailer ArrayList, Adapter and RecyclerView
        trailersList = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(getContext(), trailersList);
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        trailerRecyclerView.setNestedScrollingEnabled(false);

        //setup Review ArrayList, Adapter and RecyclerView
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList, getContext());
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("trailers") && savedInstanceState.containsKey("reviews")) {
                trailersList = savedInstanceState.getParcelableArrayList("trailers");
                trailerAdapter.addAll(trailersList);
                if (trailersList.isEmpty())
                    noTrailers.setVisibility(View.VISIBLE);

                reviewList = savedInstanceState.getParcelableArrayList("reviews");
                reviewAdapter.setData(reviewList);
                if (reviewList.isEmpty())
                    noReviews.setVisibility(View.VISIBLE);
            }
        } else {
            getTrailersData(movie.getId());
            getReviewsData(movie.getId());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("trailers", trailersList);
        outState.putParcelableArrayList("reviews", reviewList);
    }

    public void setDataToView() {
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + movie.getBackdrop_path())
                    .placeholder(R.drawable.placeholder)
                    .into(movieImageView);

        else
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w320" + movie.getPoster_path())
                    .placeholder(R.drawable.placeholder)
                    .into(movieImageView);

        titleText.setText(movie.getTitle());
        dateText.setText(movie.getRelease_date());

        String vote = movie.getVote_average();
        if (vote == null || vote.equals("0"))
            voteText.setText(getText(R.string.no_vote_text));
        else
            voteText.setText("Rate : " + vote + "/10");

        overviewText.setText(movie.getOverview());
    }

    public void getTrailersData(String id) {

        movieApi.getTrailersData(id, BuildConfig.MOVIE_API_KEY)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null) {
                                trailersList = trailerResponse.getResults();
                                trailerAdapter.addAll(trailersList);
                                if (trailersList.isEmpty())
                                    noTrailers.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                        noTrailers.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void getReviewsData(String id) {

        movieApi.getReviewsData(id, BuildConfig.MOVIE_API_KEY)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            ReviewResponse reviewResponse = response.body();
                            if (reviewResponse != null) {
                                reviewList = reviewResponse.getResults();
                                reviewAdapter.setData(reviewList);
                                if (reviewList.isEmpty())
                                    noReviews.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                        noReviews.setVisibility(View.VISIBLE);
                    }
                });
    }
}
