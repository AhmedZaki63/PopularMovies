package com.example.ahmed.popularmovies.Network;

import com.example.ahmed.popularmovies.Models.MovieResponse;
import com.example.ahmed.popularmovies.Models.ReviewResponse;
import com.example.ahmed.popularmovies.Models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieApi {

    @GET("{sortType}")
    Call<MovieResponse> getMoviesData(@Path("sortType") String sortType
            , @Query("language") String language
            , @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailerResponse> getTrailersData(@Path("id") String id
            , @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewResponse> getReviewsData(@Path("id") String id
            , @Query("api_key") String apiKey);

}
