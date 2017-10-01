package com.example.ahmed.popularmovies.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MovieResponse {

    private ArrayList<Movie> results;

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    public static class Movie implements Parcelable {

        public static final Creator<Movie> CREATOR = new Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel in) {
                return new Movie(in);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };
        private String id;
        private String vote_average;
        private String title;
        private String poster_path;
        private String backdrop_path;
        private String overview;
        private String release_date;

        public Movie(String id, String title, String poster_path, String backdrop_path, String overview, String release_date, String vote_average) {
            this.id = id;
            this.vote_average = vote_average;
            this.title = title;
            this.poster_path = poster_path;
            this.backdrop_path = backdrop_path;
            this.overview = overview;
            this.release_date = release_date;
        }

        Movie(Parcel in) {
            id = in.readString();
            vote_average = in.readString();
            title = in.readString();
            poster_path = in.readString();
            backdrop_path = in.readString();
            overview = in.readString();
            release_date = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVote_average() {
            return vote_average;
        }

        public void setVote_average(String vote_average) {
            this.vote_average = vote_average;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(vote_average);
            parcel.writeString(title);
            parcel.writeString(poster_path);
            parcel.writeString(backdrop_path);
            parcel.writeString(overview);
            parcel.writeString(release_date);
        }
    }
}
