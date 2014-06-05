package com.gamerspot;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.gamerspot.com.gamerspot.beans.NewsFeed;

public class NewsActivity extends ActionBarActivity {

    //private static RequestQueue queue;
    //private ArrayList<String> pcFeedUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);



        String[] pcUrls = this.getResources().getStringArray(R.array.pc_feeds);

        for(String url : pcUrls) {
            fetchNews(url, NewsFeed.PLATFORM_PC);
        }
    }

    private void fetchNews(final String url, int platformIn) {

        final int platform = platformIn;

        StringRequest xmlDoc = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        Log.i(url, s.length() +" for "+ platform);
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "Error fetching news");
                    }
                }
        );

        queue.add(xmlDoc);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
