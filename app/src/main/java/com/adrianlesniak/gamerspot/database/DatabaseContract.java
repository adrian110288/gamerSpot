package com.adrianlesniak.gamerspot.database;

import android.provider.BaseColumns;

/**
 * Created by Adrian on 10-Jun-14.
 */
public final class DatabaseContract {

    public DatabaseContract() {
    }

    public static abstract class NewsFeedTable implements BaseColumns {

        public static final String TABLE_NAME = "NewsFeeds";

        public static final String COLUMN_NAME_ID = "guid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATE = "pubDate";
        public static final String COLUMN_NAME_CREATOR = "creator";
        public static final String COLUMN_NAME_PROVIDER = "provider";
        public static final String COLUMN_NAME_PLATFORM = "platform";
        public static final String COLUMN_NAME_VISITED = "visited";
    }

    public static abstract class SearchPhrasesTable implements BaseColumns {

        public static final String TABLE_NAME = "SearchPhrases";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PHRASE = "phrase";

    }

    public static abstract class FavouriteFeedsTable implements BaseColumns {

        public static final String TABLE_NAME = "FavouriteFeeds";

        public static final String COLUMN_NAME_ID = "faveId";
        public static final String COLUMN_FAVOURITE_FEED_ID = NewsFeedTable.COLUMN_NAME_ID;

    }
}
