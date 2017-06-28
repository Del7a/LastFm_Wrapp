package com.example.philip.lastfmwrapper.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by philip on 30.04.17.
 */

public class ArtistTable {
    public static final String TABLE_ARTIST = "artist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LASTFM_ID = "mbid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PLAYS = "plays";
    public static final String COLUMN_LISTENERS = "listeners";
    public static final String COLUMN_IMG_URL = "image_url";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ARTIST
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LASTFM_ID + " text not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PLAYS + " text, "
            + COLUMN_LISTENERS + " text, "
            + COLUMN_IMG_URL + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ArtistTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST);
        onCreate(database);
    }
}
