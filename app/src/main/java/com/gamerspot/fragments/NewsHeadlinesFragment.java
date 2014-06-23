package com.gamerspot.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.database.DAO;
import com.gamerspot.extra.App;
import com.gamerspot.extra.NewsFeedsAdapter;

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
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsHeadlinesFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private Context context;
    private static FeedFetcherTask downloadTask;
    private ListView listView;
    private Button newFeedsButton;
    private static boolean buttonDismissed = false;
    private static int buttonVisible = Button.GONE;
    private NewsFeedsAdapter feedsAdapter;
    private OnHeadlineSelectedListener mCallback;
    private static ArrayList<NewsFeed> feedList = new ArrayList<NewsFeed>();;
    private DAO dao;
    private ConnectivityManager connManager;
    private NetworkInfo networkInfo;
    private static int launchCount = 0;
    private static int newRowsInserted = 0;
    private String pluralString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();
        launchCount++;
        pluralString = getResources().getQuantityString(R.plurals.new_feeds_plurals, newRowsInserted, newRowsInserted);

        downloadTask = new FeedFetcherTask(context);
        dao = new DAO(context);

        //TODO Loader required - Genymotion log (Skipped xxx frames. The application may be doing too much work on its main thread.)
        if(launchCount == 1) {
            feedList = dao.getAllFeeds();
        }

        dao.close();
        feedsAdapter = new NewsFeedsAdapter(context, feedList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_headlines, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mCallback.onArticleSelected(feedList.get(position));
        listView.setItemChecked(position, true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        int threshold = 1;

        if (scrollState == SCROLL_STATE_IDLE) {
            if (listView.getLastVisiblePosition() >= listView.getCount() - threshold) {

                //TODO Implement loading more data

                Log.i("LOG", "Load more data");

                listView.setOnScrollListener(null);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(NewsFeed feed);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newFeedsButton = (Button) view.findViewById(R.id.new_feeds_button);
        newFeedsButton.setText(pluralString);
        newFeedsButton.setVisibility(buttonVisible);

        if(newRowsInserted == 0){
            newFeedsButton.setVisibility(Button.GONE);
        }

        newFeedsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                buttonVisible = Button.GONE;

                feedList.clear();
                feedList = dao.getAllFeeds();
                feedsAdapter.addAll(feedList);
                feedsAdapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(0);
                newFeedsButton.setVisibility(buttonVisible);

                buttonDismissed = true;
            }
        });

        listView = (ListView) view.findViewById(R.id.headlines_list_view);
        listView.setAdapter(feedsAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        listView.setFastScrollEnabled(true);

        if (launchCount == 1) {
            if (App.getUtils(getActivity()).isOnline()) {
                downloadTask.execute();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class FeedFetcherTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private String[] pcFeedUrls;
        private String[] xboxFeedUrls;
        private String[] playstationFeedUrls;
        private String[] nintendoFeedUrls;
        private String[] mobileFeedUrls;

        private ArrayList<NewsFeed> newFeeds;

        public FeedFetcherTask(Context c) {

            context = c;
            pcFeedUrls = c.getResources().getStringArray(R.array.pc_feeds);
            xboxFeedUrls = c.getResources().getStringArray(R.array.xbox_feeds);
            playstationFeedUrls = c.getResources().getStringArray(R.array.playstation_feeds);
            nintendoFeedUrls = c.getResources().getStringArray(R.array.nintendo_feeds);
            mobileFeedUrls = c.getResources().getStringArray(R.array.mobile_feeds);

            newFeeds = new ArrayList<NewsFeed>();
        }

        @Override
        protected Integer doInBackground(String... params) {

            long time = System.currentTimeMillis();

            getActivity().setProgressBarIndeterminateVisibility(true);

            getNewsForPc();
            getNewsForXbox();
            getNewsForPlaystation();
            getNewsForNintendo();
            getNewsForMobile();

            newRowsInserted = storeNewsInDatabase();

            long finish = System.currentTimeMillis() - time;
            Log.i("time elapsed", finish / 1000.0 + "");

            return null;
        }

        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);

            if (getActivity() != null) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (newRowsInserted == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.no_new_tweets), Toast.LENGTH_SHORT).show();
                } else {
                    newFeedsButton.setVisibility(Button.VISIBLE);
                    buttonVisible = Button.VISIBLE;
                    newFeedsButton.setText(getResources().getQuantityString(R.plurals.new_feeds_plurals, newRowsInserted, newRowsInserted));

                    //TODO Create animation
                }
            }

            else{
                buttonVisible = Button.VISIBLE;
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        private void retainListViewPosition() {

            int index = listView.getFirstVisiblePosition() + newFeeds.size();
            View v = listView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            listView.setSelectionFromTop(index, top);
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
                NewsFeed feed = null;
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

                        newFeeds.add(feed);
                    }
                }
            } catch (ConnectException ce) {
                Log.i("EXCEPTION", "ConnectException");
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.connection_exception_error), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("EXCEPTION", "Exception");
            }
        }

        private int storeNewsInDatabase() {

            return dao.insertAllFeeds(newFeeds);
        }
    }
}
