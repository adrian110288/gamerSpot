package com.gamerspot;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamerspot.com.gamerspot.beans.NewsFeed;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Adrian on 05-Jun-14.
 */
public class FeedFetcher {

    private Context context;
    private String[] pcUrls;
    private String[] xBoxUrls;
    private String[] playStationUrls;

    private ArrayList<NewsFeed> newFeeds;

    public FeedFetcher(Context c) {

        context = c;

        pcUrls = context.getResources().getStringArray(R.array.pc_feeds);
        xBoxUrls = context.getResources().getStringArray(R.array.xbox_feeds);
        //TODO get playstation urls
    }

    public ArrayList<NewsFeed> fetchAll(){

        ArrayList<NewsFeed> allNewFeeds = new ArrayList<NewsFeed>();

        allNewFeeds.addAll(fetchForPC());

        //Collections.sort(allNewFeeds);

        return allNewFeeds;
    }

    public ArrayList<NewsFeed> fetchForPC(){

        final int platform = NewsFeed.PLATFORM_PC;
        newFeeds = new ArrayList<NewsFeed>();

        for(String url: pcUrls) {

            newFeeds.addAll(downloadFeeds(url, platform));
        }

        return newFeeds;
    }

    public ArrayList<NewsFeed> fetchForXBox(){}

    public ArrayList<NewsFeed> fetchForPlayStation(){}

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
