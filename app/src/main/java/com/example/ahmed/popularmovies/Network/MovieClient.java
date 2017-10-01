package com.example.ahmed.popularmovies.Network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    public static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MovieApi createApi(Retrofit retrofit) {
        return retrofit.create(MovieApi.class);
    }
}
