package com.gamerspot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.gamerspot.beans.NewsFeed;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adrian on 10-Jun-14.
 */
public class DAO {

    private Context context;
    private GamerSpotDBHelper dbHelper;
    private SQLiteDatabase database;
    private ContentValues values;
    private ArrayList<NewsFeed> queriedList;
    private ArrayList<NewsFeed> newlyInsertedFeeds;

    private static final String[] listViewProjection = {
            DatabaseContract.NewsFeedTable.COLUMN_NAME_ID,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_TITLE,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_LINK,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_DESCRIPTION,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_CREATOR,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_PROVIDER,
            DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM
    };

    int queryLimit = 20;

    String sortOrder = DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE + " DESC";
    String sortOrderWithLimit = DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE + " DESC LIMIT 20 OFFSET 0 ";

    public DAO(Context contextIn) {
        this.context = contextIn;

        if(dbHelper == null) {
            dbHelper = new GamerSpotDBHelper(context);
        }
    }

    public void close() {
        dbHelper.close();
    }

    public int insertAllFeeds(ArrayList<NewsFeed> list) {

        database = dbHelper.getWritableDatabase();

        int count =0;
        values = new ContentValues();

        try{
            database.beginTransaction();

            for(NewsFeed feed : list) {

                try{
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_ID, feed.getGuid());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_TITLE, feed.getTitle());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_LINK, feed.getLink());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_DESCRIPTION, feed.getDescription());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE, feed.getDate().getTime());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_CREATOR, feed.getCreator());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_PROVIDER, feed.getProvider());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM, feed.getPlatform());

                    long inserted = database.insertOrThrow(DatabaseContract.NewsFeedTable.TABLE_NAME, null, values);
                    if(inserted != -1) {

                        count++;
                    }
                }

                catch(Exception e) {
                    continue;
                }
            }

            database.setTransactionSuccessful();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            database.endTransaction();
        }

        Log.i("ROWS_INSERTED", count + "");

        return count;
    }

    public ArrayList<NewsFeed> getAllFeeds(){

        database = dbHelper.getReadableDatabase();
        queriedList = new ArrayList<NewsFeed>();
        NewsFeed feed;

        Cursor c = database.query(DatabaseContract.NewsFeedTable.TABLE_NAME, listViewProjection, null, null, null, null, sortOrderWithLimit);
        queriedList = this.traverseCursor(c);

        return queriedList;
    }

    public ArrayList<NewsFeed> getPlatformFeeds(long platform) {

        database = dbHelper.getReadableDatabase();
        queriedList = new ArrayList<NewsFeed>();

        String [] selection = {String.valueOf(platform)};

        Cursor c = database.query(DatabaseContract.NewsFeedTable.TABLE_NAME, listViewProjection, DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM + "=?", selection, null, null, sortOrderWithLimit);
        queriedList = this.traverseCursor(c);

        return queriedList;

    }

    private ArrayList<NewsFeed> traverseCursor(Cursor c) {

        ArrayList<NewsFeed> tempList = new ArrayList<NewsFeed>();
        Cursor cursor = c;
        NewsFeed feed;

        c.moveToNext();

        while(!c.isAfterLast()) {

            feed = new NewsFeed();

            feed.setGuid(c.getString(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_ID)));
            feed.setTitle(c.getString(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_TITLE)));
            feed.setLink(c.getString(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_LINK)));
            feed.setDescription(c.getString(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_DESCRIPTION)));
            feed.setDate(c.getLong(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE)));
            feed.setCreator(c.getString(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_CREATOR)));
            feed.setProvider(c.getString(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_PROVIDER)));
            feed.setPlatform(c.getInt(c.getColumnIndex(DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM)));

            tempList.add(feed);
            c.moveToNext();
        }

        return tempList;
    }

    public boolean setFeedVisited(String guid){
        //TODO setFeedVisited method to implement
        return false;
    }

    public boolean insertPhrase(String phraseIn) {

        database = dbHelper.getWritableDatabase();
        values = new ContentValues();

        values.put(DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE, phraseIn);

        long inserted = database.insertOrThrow(DatabaseContract.SearchPhrasesTable.TABLE_NAME, null, values);

        return (inserted != -1) ? true : false;

    }

    public ArrayList<String> getPhrases(String phraseIn) {

        database = dbHelper.getReadableDatabase();

        ArrayList<String> result = new ArrayList<String>();

        String selectStatement = "SELECT " + DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE + " FROM " + DatabaseContract.SearchPhrasesTable.TABLE_NAME + " WHERE " + DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE + " LIKE '"+ phraseIn+"%"+"'";
        Log.i("PHRASES STATEMENT", selectStatement);
        Cursor c = database.rawQuery(selectStatement, null);

        c.moveToNext();

        while(!c.isAfterLast()) {

            result.add(c.getString(c.getColumnIndex(DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE)));
            c.moveToNext();
        }

        Log.i("PHRASES LIST", result.size()+"");

        return  result;
    }

    public boolean isFavourite(String feedId) {
        database = dbHelper.getReadableDatabase();

        String searchStatement = "SELECT " + DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID + " FROM " + DatabaseContract.FavouriteFeedsTable.TABLE_NAME  + " WHERE " + DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID + " = " + "'" +feedId + "'";
        Cursor results = database.rawQuery(searchStatement, null);

        return (results.getCount() != 0);
    }

    public boolean addToFavourites(String feedId) {

        database = dbHelper.getWritableDatabase();

        long inserted = -1;

        if(!isFavourite(feedId)) {
            values = new ContentValues();
            values.put(DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID, feedId);

            inserted = database.insertOrThrow(DatabaseContract.FavouriteFeedsTable.TABLE_NAME, null, values);
        }
        return (inserted > 0);
    }

    public boolean removeFromFavourites(String feedId) {
        database = dbHelper.getWritableDatabase();
        int rowsAffected = database.delete(DatabaseContract.FavouriteFeedsTable.TABLE_NAME, DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID + "="+ "'" +feedId + "'", null );
        return (rowsAffected > 0);
    }
}
