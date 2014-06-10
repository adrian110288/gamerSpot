package com.gamerspot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gamerspot.beans.NewsFeed;
import com.gamerspot.database.DAO;
import com.gamerspot.database.GamerSpotDBHelper;
import com.gamerspot.extra.CustomTypefaceSpan;
import com.gamerspot.extra.NewsFeedsAdapter;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class NewsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static FeedFetcherTask downloadTask;
    private static String appName;
    private ListView newsFeedsListView;
    private NewsFeedsAdapter feedsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        /*
         * Customization of ActionBar
         */
        appName  = this.getResources().getString(R.string.app_name);
        ActionBar actionBar = getSupportActionBar();
        SpannableString spannableString = new SpannableString(appName);
        spannableString.setSpan(new CustomTypefaceSpan(this, "Gamegirl.ttf"),0, appName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannableString);

        newsFeedsListView = (ListView) findViewById(R.id.NewsFeedslistView);
        newsFeedsListView.setOnItemClickListener(this);

        downloadTask = new FeedFetcherTask(this.getApplicationContext());
        downloadTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class FeedFetcherTask extends AsyncTask<String, Void, Void> {

        private Context context;
        private String[] pcFeedUrls;

        private ArrayList<NewsFeed> newFeedsList;
        private DAO dao;


        private FeedFetcherTask(Context c) {

            context = c;
            pcFeedUrls = c.getResources().getStringArray(R.array.pc_feeds);
            newFeedsList = new ArrayList<NewsFeed>();
            dao = new DAO(context);
        }

        @Override
        protected Void doInBackground(String... params) {

            long time = System.currentTimeMillis();

            getNewsForPc();
            storeNewsInDatabase();

            long finish = System.currentTimeMillis()-time;
            Log.i("time elapsed", finish/1000.0 + "");

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);

            ArrayList<NewsFeed> list = dao.getAllFeeds();

            feedsAdapter = new NewsFeedsAdapter(context, list);
            newsFeedsListView.setAdapter(feedsAdapter);
        }

        /*
        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }*/

        private void getNewsForPc(){

            int platform = NewsFeed.PLATFORM_PC;

            for(String url: pcFeedUrls) {
                parseRssFeed(url, platform);
            }
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
                        feed.setCreator(creator);

                        feed.setProvider(provider);
                        feed.setPlatform(platform);

                        newFeedsList.add(feed);

                    }
                }

                //TODO insert to SQLite
                //TODO return list of NewsFeed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void storeNewsInDatabase(){

            int rows = dao.insertAllFeeds(newFeedsList);
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
