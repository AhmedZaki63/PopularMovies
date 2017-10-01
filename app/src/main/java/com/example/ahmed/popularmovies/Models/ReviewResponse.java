package com.example.ahmed.popularmovies.Models;

import java.util.ArrayList;

public class ReviewResponse {
    private ArrayList<ReviewResponse.Review> results;

    public ArrayList<ReviewResponse.Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewResponse.Review> results) {
        this.results = results;
    }

    public static class Review {

        private String author;
        private String content;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
