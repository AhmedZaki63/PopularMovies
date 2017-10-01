package com.example.ahmed.popularmovies.Models;

import java.util.ArrayList;

public class TrailerResponse {
    private ArrayList<TrailerResponse.Trailer> results;

    public ArrayList<TrailerResponse.Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerResponse.Trailer> results) {
        this.results = results;
    }

    public static class Trailer {

        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }
}
