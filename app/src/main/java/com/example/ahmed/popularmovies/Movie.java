package com.example.ahmed.popularmovies;


class Movie {

    private String title;
    private String id;
    private String poster;
    private String backdrop;
    private String overview;
    private String vote;
    private String date;

    Movie(String title, String id, String poster, String backdrop, String overview, String vote, String date) {
        this.title = title;
        this.id = id;
        this.poster = poster;
        this.backdrop = backdrop;
        this.overview = overview;
        this.vote = vote;
        this.date = date;
    }

    String getTitle() {
        return title;
    }

    String getId() {
        return id;
    }

    String getPoster() {
        return poster;
    }

    String getBackdrop() {
        return backdrop;
    }

    String getOverview() {
        return overview;
    }

    String getVote() {
        return vote;
    }

    String getDate() {
        return date;
    }
}
