package com.gamerspot.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Adrian on 10-Jun-14.
 */
public class GamerSpotDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GamerSpot.db";

    private static final String COMMA_SEP = ", ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    public static final String SQL_CREATE_NEWSFEEDS_TABLE = "CREATE TABLE " + DatabaseContract.NewsFeedTable.TABLE_NAME + " (" +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_ID + " TEXT PRIMARY KEY UNIQUE," +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE + INTEGER_TYPE + COMMA_SEP +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_CREATOR + TEXT_TYPE + COMMA_SEP +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_PROVIDER + TEXT_TYPE + COMMA_SEP +
        DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM + INTEGER_TYPE + " )";

    public static final String SQL_DELETE_NEWSFEEDS_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.NewsFeedTable.TABLE_NAME;

    public GamerSpotDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NEWSFEEDS_TABLE);
        Log.i("DB" , "NewsFeed table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_NEWSFEEDS_TABLE);
        onCreate(db);
    }
}
