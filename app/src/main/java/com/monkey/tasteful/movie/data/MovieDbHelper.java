package com.monkey.tasteful.movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by emanjaan on 5/21/16.
 */


public class MovieDbHelper extends SQLiteOpenHelper
{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";


    public MovieDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //put your sql create table statements here
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + Contract.movieEntry.TABLE_NAME + " ( " +
                        Contract.movieEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.movieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        Contract.movieEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        Contract.movieEntry.COLUMN_SUMMARY + " TEXT NOT NULL " +
                        " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.movieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
