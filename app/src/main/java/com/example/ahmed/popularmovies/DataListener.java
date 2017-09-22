package com.example.ahmed.popularmovies;


interface DataListener {
    void setMovieData(String title, String id, String poster, String backdrop, String overview, String vote, String date);
}
