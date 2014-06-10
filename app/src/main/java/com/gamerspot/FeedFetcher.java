package com.gamerspot;

import android.content.Context;
import android.os.Handler;

import com.gamerspot.beans.NewsFeed;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Adrian on 05-Jun-14.
 */
public class FeedFetcher {

    private Context context;

    //URLs to RSS feeds
    private String[] pcUrls;
    private String[] xBoxUrls;
    private String[] playStationUrls;

    //final list containing all of the new feeds
    private ArrayList<NewsFeed> newFeeds;

    private static Thread backgroundDownload;
    private static Handler downloadThreadHandler;

    private static final int THREAD_FINISH = 1;

    public FeedFetcher(Context c) {

        context = c;
        newFeeds = new ArrayList<NewsFeed>();

        pcUrls = context.getResources().getStringArray(R.array.pc_feeds);
        //xBoxUrls = context.getResources().getStringArray(R.array.xbox_feeds);
        //TODO get playstation urls

    }

    public ArrayList<NewsFeed> fetchAll(){


        return null;
    }

    public void fetchForPC(){

        final int platform = NewsFeed.PLATFORM_PC;

        for(String url: pcUrls) {

            newFeeds.addAll(downloadFeeds(url, platform));
        }
    }

    /*
    public ArrayList<NewsFeed> fetchForXBox(){}

    public ArrayList<NewsFeed> fetchForPlayStation(){}

    */
    private ArrayList<NewsFeed> downloadFeeds(final String urlIn, final int platform) {

        String xmlFeed = "";
        ArrayList<NewsFeed> newDownloadedFeeds = new ArrayList<NewsFeed>();

        try {
            URL url = new URL(urlIn);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            xmlFeed = readStream(con.getInputStream());

            //TODO parse xml
            //TODO create list of NewsFeed
            //TODO insert to SQLite
            //TODO return list of NewsFeed

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDownloadedFeeds;
    }

    private String readStream(InputStream in) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));

            String output = "";
            String line = "";

            while ((line = reader.readLine()) != null) {
                output +=line;
            }

            return output;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }
}
