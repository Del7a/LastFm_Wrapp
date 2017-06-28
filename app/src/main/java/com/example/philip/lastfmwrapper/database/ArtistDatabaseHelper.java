package com.example.philip.lastfmwrapper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by philip on 30.04.17.
 */

public class ArtistDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "artisttable.db";
    private static final int DATABASE_VERSION = 3;

    public ArtistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        ArtistTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        ArtistTable.onUpgrade(database, oldVersion, newVersion);
    }
}
