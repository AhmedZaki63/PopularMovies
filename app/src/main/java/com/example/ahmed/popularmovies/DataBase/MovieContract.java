package com.example.ahmed.popularmovies.DataBase;


import android.provider.BaseColumns;

class MovieContract {
    static final class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "Movies";

        static final String COLUMN_TITLE = "title";
        static final String COLUMN_POSTER_PATH = "poster_path";
        static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_RELEASE_DATE = "release_date";
        static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }
}
