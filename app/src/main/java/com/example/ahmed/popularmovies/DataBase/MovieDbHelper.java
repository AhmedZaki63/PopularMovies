package com.example.ahmed.popularmovies.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.ahmed.popularmovies.DataBase.MovieContract.MovieEntry;
import com.example.ahmed.popularmovies.Models.MovieResponse.Movie;

import java.util.ArrayList;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Movies.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Movie> getAllFromDatabase() {
        Cursor cursor = context.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);

        ArrayList<Movie> movies = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                String id = cursor.getString(cursor.getColumnIndex(MovieEntry._ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
                String poster = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
                String backdrop = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH));
                String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                String date = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
                String vote = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));

                Movie movie = new Movie(id, title, poster, backdrop, overview, date, vote);
                movies.add(movie);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return movies;
    }

    public boolean isSaved(Movie movie) {
        Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId()).build();
        return context.getContentResolver().query(uri, null, null, null, null)
                .getCount() > 0;
    }

    public void addToDatabase(Movie movie) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry._ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        contentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());

        context.getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
    }

    public void removeFromDatabase(Movie movie) {
        Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId()).build();
        context.getContentResolver().delete(uri, null, null);
    }

    public void removeAllFromDatabase() {
        context.getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
    }
}
