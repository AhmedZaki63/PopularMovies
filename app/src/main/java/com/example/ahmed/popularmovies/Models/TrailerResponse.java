package com.example.ahmed.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TrailerResponse {
    private ArrayList<TrailerResponse.Trailer> results;

    public ArrayList<TrailerResponse.Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerResponse.Trailer> results) {
        this.results = results;
    }

    public static class Trailer implements Parcelable {

        public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
            @Override
            public Trailer createFromParcel(Parcel in) {
                return new Trailer(in);
            }

            @Override
            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };
        private String key;

        Trailer(Parcel in) {
            key = in.readString();
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(key);
        }
    }
}
