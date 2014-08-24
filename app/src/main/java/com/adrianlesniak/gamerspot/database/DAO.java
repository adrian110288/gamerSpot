package com.adrianlesniak.gamerspot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adrianlesniak.gamerspot.beans.NewsFeed;

import java.util.ArrayList;

/**
 * Created by Adrian on 10-Jun-14.
 */
public class DAO {

    private final static int QUERY_LIMIT = 20;
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
    private GamerSpotDBHelper dbHelper;
    private SQLiteDatabase database;
    private ContentValues values;
    private ArrayList<NewsFeed> queriedList;
    private int QUERY_OFFSET = 0;

    public DAO(Context context) {

        if (dbHelper == null) {
            dbHelper = new GamerSpotDBHelper(context);
        }
    }

    public void close() {
        dbHelper.close();
    }

    public void resetLimits() {
        this.QUERY_OFFSET = 0;
    }

    private void incrementLimits() {
        this.QUERY_OFFSET += 20;
    }

    public int insertAllFeeds(ArrayList<NewsFeed> list) {

        database = dbHelper.getWritableDatabase();

        int count = 0;
        values = new ContentValues();

        try {
            database.beginTransaction();

            for (NewsFeed feed : list) {

                try {
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_ID, feed.getGuid());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_TITLE, feed.getTitle());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_LINK, feed.getLink());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_DESCRIPTION, feed.getDescription());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE, feed.getDate().getTime());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_CREATOR, feed.getCreator());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_PROVIDER, feed.getProvider());
                    values.put(DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM, feed.getPlatform());

                    long inserted = database.insertOrThrow(DatabaseContract.NewsFeedTable.TABLE_NAME, null, values);
                    if (inserted != -1) {

                        count++;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

        return count;
    }

    public ArrayList<NewsFeed> getFeeds(Long platform) {

        int QUERY_OFFSET = 0;
        String sortOrderWithLimit = DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE + " DESC LIMIT " + this.QUERY_LIMIT + " OFFSET " + QUERY_OFFSET;

        return queryData(platform, sortOrderWithLimit);
    }

    public ArrayList<NewsFeed> loadMoreDataForScroll(Long platform) {
        incrementLimits();
        String sortOrderWithLimit = DatabaseContract.NewsFeedTable.COLUMN_NAME_DATE + " DESC LIMIT " + this.QUERY_LIMIT + " OFFSET " + QUERY_OFFSET;

        return queryData(platform, sortOrderWithLimit);
    }

    private ArrayList<NewsFeed> queryData(Long platform, String sortStatement){

        database = dbHelper.getReadableDatabase();
        queriedList = new ArrayList<NewsFeed>();

        String[] selection = {String.valueOf(platform)};
        Cursor c;

        if (platform == null) {
            c = database.query(DatabaseContract.NewsFeedTable.TABLE_NAME, listViewProjection, null, null, null, null, sortStatement);
        } else {
            c = database.query(DatabaseContract.NewsFeedTable.TABLE_NAME, listViewProjection, DatabaseContract.NewsFeedTable.COLUMN_NAME_PLATFORM + "=?", selection, null, null, sortStatement);
        }

        queriedList = this.traverseCursor(c);

        return queriedList;
    }

    private ArrayList<NewsFeed> traverseCursor(Cursor c) {

        ArrayList<NewsFeed> tempList = new ArrayList<NewsFeed>();
        Cursor cursor = c;
        NewsFeed feed;

        if(cursor != null) {
            c.moveToNext();

            while (!c.isAfterLast()) {

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
        }

        return tempList;
    }

    public boolean setFeedVisited(String guid) {
        //TODO setFeedVisited method to implement
        return false;
    }

    public boolean insertPhrase(String phraseIn) {

        database = dbHelper.getWritableDatabase();
        long inserted = -1;

        if(!phraseExists(phraseIn) && phraseIn.length() > 1) {
            values = new ContentValues();
            values.put(DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE, phraseIn);
            inserted = database.insertOrThrow(DatabaseContract.SearchPhrasesTable.TABLE_NAME, null, values);
        }

        return (inserted != -1) ? true : false;
    }

    public ArrayList<String> getPhrases(String phraseIn) {

        database = dbHelper.getReadableDatabase();

        ArrayList<String> result = new ArrayList<String>();

        String selectStatement = "SELECT " + DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE + " FROM " + DatabaseContract.SearchPhrasesTable.TABLE_NAME + " WHERE " + DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE + " LIKE '" + phraseIn + "%" + "'";
        Cursor c = database.rawQuery(selectStatement, null);

        c.moveToNext();

        while (!c.isAfterLast()) {

            result.add(c.getString(c.getColumnIndex(DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE)));
            c.moveToNext();
        }
        return result;
    }

    private boolean phraseExists(String phraseIn) {

        String selectStatement = "SELECT " + DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE + " FROM " + DatabaseContract.SearchPhrasesTable.TABLE_NAME + " WHERE " + DatabaseContract.SearchPhrasesTable.COLUMN_NAME_PHRASE + " = " + "'" + phraseIn + "'";;
        Cursor c = database.rawQuery(selectStatement, null);

        return (c.getCount() == 0) ? false  :true ;
    }

    public boolean isFavourite(String feedId) {
        database = dbHelper.getReadableDatabase();

        String searchStatement = "SELECT " + DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID + " FROM " + DatabaseContract.FavouriteFeedsTable.TABLE_NAME + " WHERE " + DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID + " = " + "'" + feedId + "'";
        Cursor results = database.rawQuery(searchStatement, null);

        return (results.getCount() != 0);
    }

    public boolean addToFavourites(String feedId) {

        database = dbHelper.getWritableDatabase();

        long inserted = -1;

        if (!isFavourite(feedId)) {
            values = new ContentValues();
            values.put(DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID, feedId);

            inserted = database.insertOrThrow(DatabaseContract.FavouriteFeedsTable.TABLE_NAME, null, values);
        }
        return (inserted > 0);
    }

    public boolean removeFromFavourites(String feedId) {
        database = dbHelper.getWritableDatabase();
        int rowsAffected = database.delete(DatabaseContract.FavouriteFeedsTable.TABLE_NAME, DatabaseContract.FavouriteFeedsTable.COLUMN_FAVOURITE_FEED_ID + "=" + "'" + feedId + "'", null);
        return (rowsAffected > 0);
    }

    public ArrayList<NewsFeed> searchArticles(String stringPhrase) {

        database = dbHelper.getReadableDatabase();
        ArrayList<NewsFeed> list = null;

        String statement = "SELECT * FROM " + DatabaseContract.NewsFeedTable.TABLE_NAME + " WHERE " + DatabaseContract.NewsFeedTable.COLUMN_NAME_TITLE + " LIKE '" + "%" + stringPhrase + "%" + "'";
        Cursor c = null;

        if(stringPhrase.length() > 3) {
            c = database.rawQuery(statement, null);
            list = traverseCursor(c);
        }

        return list;
    }
}
