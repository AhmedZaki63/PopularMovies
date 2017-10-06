package com.example.ahmed.popularmovies.DataBase;


import android.net.Uri;
import android.provider.BaseColumns;

class MovieContract {

    static final String AUTHORITY = "com.example.ahmed.popularmovies";
    static final String PATH_MOVIES = "Movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final class MovieEntry implements BaseColumns {
        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        static final String TABLE_NAME = "Movies";

        static final String COLUMN_TITLE = "title";
        static final String COLUMN_POSTER_PATH = "poster_path";
        static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_RELEASE_DATE = "release_date";
        static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }
}
