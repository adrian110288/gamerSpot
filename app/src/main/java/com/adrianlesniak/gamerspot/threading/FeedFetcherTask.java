package com.adrianlesniak.gamerspot.threading;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.database.DAO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FeedFetcherTask extends AsyncTask<String, Void, Integer> {

    private static final String junkText = "<p style=\"padding:5px;background:#ffffcc;";
    private Context context;
    private String[] pcFeedUrls;
    private String[] xboxFeedUrls;
    private String[] playstationFeedUrls;
    private String[] nintendoFeedUrls;
    private String[] mobileFeedUrls;
    private ArrayList<NewsFeed> newFeeds = new ArrayList<NewsFeed>();
    private DAO dao;
    private Handler feedFetchHandler;

    public FeedFetcherTask(Context c, Handler handler) {

        context = c;
        feedFetchHandler = handler;
        getUrlsFromResources();
        dao = new DAO(context);
    }

    @Override
    protected Integer doInBackground(String... params) {

        long time = System.currentTimeMillis();

        getNewsForPc();
        getNewsForXbox();
        getNewsForPlaystation();
        getNewsForNintendo();
        getNewsForMobile();

        int newRowsInserted = storeNewsInDatabase(newFeeds);

        long finish = System.currentTimeMillis() - time;
        Log.i("time elapsed", finish / 1000.0 + "");

        return newRowsInserted;
    }

    @Override
    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);

        feedFetchHandler.sendEmptyMessage(count);
    }

    private void getUrlsFromResources() {
        pcFeedUrls = context.getResources().getStringArray(R.array.pc_feeds);
        xboxFeedUrls = context.getResources().getStringArray(R.array.xbox_feeds);
        playstationFeedUrls = context.getResources().getStringArray(R.array.playstation_feeds);
        nintendoFeedUrls = context.getResources().getStringArray(R.array.nintendo_feeds);
        mobileFeedUrls = context.getResources().getStringArray(R.array.mobile_feeds);
    }

    private void getNewsForPc() {

        int platform = NewsFeed.PLATFORM_PC;

        for (String url : pcFeedUrls) {
            parseRssFeed(url, platform);
        }
    }

    private void getNewsForXbox() {

        int platform = NewsFeed.PLATFORM_XBOX;

        for (String url : xboxFeedUrls) {
            parseRssFeed(url, platform);
        }
    }

    private void getNewsForPlaystation() {

        int platform = NewsFeed.PLATFORM_PLAYSTATION;

        for (String url : playstationFeedUrls) {

            parseRssFeed(url, platform);
        }
    }

    private void getNewsForNintendo() {

        int platform = NewsFeed.PLATFORM_NINTENDO;

        for (String url : nintendoFeedUrls) {
            parseRssFeed(url, platform);
        }
    }

    private void getNewsForMobile() {

        int platform = NewsFeed.PLATFORM_MOBILE;

        for (String url : mobileFeedUrls) {
            parseRssFeed(url, platform);
        }
    }

    private void parseRssFeed(String urlIn, int platformIn) {

        //TODO Too many GC, also need to close streams

        try {
            HttpClient apacheClient = new DefaultHttpClient();
            HttpResponse response = apacheClient.execute(new HttpGet(urlIn));

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            InputSource is = new InputSource(br);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);

            NodeList nodeList = document.getElementsByTagName("item");

            String provider = document.getElementsByTagName("title").item(0).getTextContent();
            int platform = platformIn;

            Node node;
            NewsFeed feed;
            String title;
            String link;
            String description;
            NodeList guidNodeList;
            String guid;
            String pubDate;
            NodeList creatorNodeList;
            String creator;

            Element element;

            for (int i = 0; i < nodeList.getLength(); i++) {

                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    element = (Element) node;
                    feed = new NewsFeed();

                    title = element.getElementsByTagName("title").item(0).getTextContent();
                    feed.setTitle(title);

                    link = element.getElementsByTagName("link").item(0).getTextContent();
                    feed.setLink(link);

                    description = element.getElementsByTagName("description").item(0).getTextContent();

                    if (description.contains(junkText)) {
                        String substring = description.substring(description.indexOf(junkText), description.length());
                        description = description.replace(substring, "");
                    }

                    feed.setDescription(description);

                    guidNodeList = element.getElementsByTagName("guid");

                    if (guidNodeList == null | guidNodeList.getLength() < 1) {
                        feed.setGuid(link);
                    } else {
                        guid = guidNodeList.item(0).getTextContent();
                        feed.setGuid(guid);
                    }

                    pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();
                    feed.setDate(pubDate);

                    creatorNodeList = element.getElementsByTagName("dc:creator");

                    if (creatorNodeList == null | creatorNodeList.getLength() < 1) {
                        feed.setCreator(provider);
                    } else {
                        creator = creatorNodeList.item(0).getTextContent();
                        feed.setCreator(creator);
                    }

                    feed.setProvider(provider);
                    feed.setPlatform(platform);
                    feed.setVisited(false);

                    newFeeds.add(feed);
                }
            }
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int storeNewsInDatabase(ArrayList<NewsFeed> list) {

        int count = dao.insertAllFeeds(list);
        dao.close();
        return count;
    }
}