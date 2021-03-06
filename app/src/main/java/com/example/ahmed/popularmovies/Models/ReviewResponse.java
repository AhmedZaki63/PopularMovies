package com.example.ahmed.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReviewResponse {
    private ArrayList<ReviewResponse.Review> results;

    public ArrayList<ReviewResponse.Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewResponse.Review> results) {
        this.results = results;
    }

    public static class Review implements Parcelable {

        public static final Creator<Review> CREATOR = new Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };
        private String author;
        private String content;

        Review(Parcel in) {
            author = in.readString();
            content = in.readString();
        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(author);
            parcel.writeString(content);
        }
    }
}
