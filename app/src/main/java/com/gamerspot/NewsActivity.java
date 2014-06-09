package com.gamerspot;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.gamerspot.com.gamerspot.beans.NewsFeed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.*;
import javax.xml.parsers.*;

public class NewsActivity extends ActionBarActivity {

    private HashMap<Integer, String[]> urlsMap;
    private static FeedFetcherTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        downloadTask = new FeedFetcherTask(this.getApplicationContext());
        downloadTask.execute();

    }

    private class FeedFetcherTask extends AsyncTask<Void, Void, ArrayList<NewsFeed>> {

        private Context context;
        private String[] pcFeedUrls;

        private ArrayList<NewsFeed> newFeedsList;

        private FeedFetcherTask(Context c) {

            context = c;
            pcFeedUrls = c.getResources().getStringArray(R.array.pc_feeds);

            newFeedsList = new ArrayList<NewsFeed>();
        }

        @Override
        protected ArrayList<NewsFeed> doInBackground(Void... params) {

            getNewsForPc();

            return newFeedsList;
        }

        /*
        @Override
        protected void onPostExecute(ArrayList<NewsFeed> o) {
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }*/

        private void getNewsForPc(){

            int platform = NewsFeed.PLATFORM_PC;

            long time = System.currentTimeMillis();
            for(String url: pcFeedUrls) {

                parseRssFeed(url, platform);
            }

            long finish = System.currentTimeMillis()-time;

            Log.i("time elapsed", finish/1000.0 + "");
        }

        private void parseRssFeed(String urlIn, int platformIn){

            try {
                URL url = new URL(urlIn);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                InputSource is = new InputSource(br);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);

                NodeList nodeList = document.getElementsByTagName("item");

                String provider = document.getElementsByTagName("title").item(0).getTextContent();
                int platform = platformIn;

                for(int i=0;i<nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    NewsFeed feed = null;
                    String title;
                    String link;
                    String description;
                    String guid;
                    String pubDate;
                    String creator;

                    if(node.getNodeType() == Node.ELEMENT_NODE) {

                        Element element = (Element) node;
                        feed = new NewsFeed();

                        title = element.getElementsByTagName("title").item(0).getTextContent();
                        feed.setTitle(title);

                        link = element.getElementsByTagName("link").item(0).getTextContent();
                        feed.setLink(link);

                        description = element.getElementsByTagName("description").item(0).getTextContent();
                        feed.setDescription(description);

                        guid = element.getElementsByTagName("guid").item(0).getTextContent();
                        feed.setGuid(guid);

                        pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();
                        feed.setDate(pubDate);

                        creator = element.getElementsByTagName("dc:creator").item(0).getTextContent();
                        feed.setAuthor(creator);

                        feed.setProvider(provider);
                        feed.setPlatform(platform);

                        Log.i("feed", feed.toString());

                        newFeedsList.add(feed);

                    }
                }

                //TODO insert to SQLite
                //TODO return list of NewsFeed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
