package com.gamerspot.fragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.extra.App;
import com.gamerspot.extra.CommonUtilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsDetailsFragment extends Fragment {

    private static final int IMAGE_DOWNLOAD_CONNECTION_EXCEPTION = -1;
    private static final int IMAGE_DOWNLOAD_QUEUE_COMPLETED = 1;

    private NewsFeed feed;
    private Typeface font;
    private int descriptionLayoutWidth = 0;
    private TextView titleView;
    private TextView creatorView;
    private TextView dateView;
    private TextView descriptionView;
    private Button fullArticleButton;
    private LinearLayout descriptionLinearLayout;
    private static HashMap<String, BitmapDrawable> cachedImages = new HashMap();;
    private Handler downloadFinishHandler;
    private static List<URL> urlList;
    private Intent intent;
    private static FragmentManager fragmentManager;
    private ViewTreeObserver viewTreeObserver;
    private static CommonUtilities utils;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        utils = App.getUtils(getActivity());
        font = utils.getTextFont();

        feed = (NewsFeed) getArguments().get("FEED");
        urlList = new ArrayList<URL>();
        intent = new Intent(Intent.ACTION_VIEW);

        fragmentManager = getFragmentManager();

        getImagesUrl(feed.getDescription());

        downloadFinishHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == IMAGE_DOWNLOAD_QUEUE_COMPLETED) {

                    getActivity().setProgressBarIndeterminateVisibility(false);

                    utils.setCachedImages(cachedImages);

                    titleView.setText(feed.getTitle());
                    creatorView.setText(feed.getCreator());
                    dateView.setText(utils.getFormattedDate(feed.getDate()));

                    displayTextWithImages();

                    descriptionView.setMovementMethod(LinkMovementMethod.getInstance());
                }

                else if (msg.what == IMAGE_DOWNLOAD_CONNECTION_EXCEPTION ) {
                    displayOnlyText();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_details, container, false);

        titleView = (TextView) view.findViewById(R.id.details_title);
        titleView.setTypeface(font);
        creatorView = (TextView) view.findViewById(R.id.details_creator);
        creatorView.setTypeface(font);
        dateView = (TextView) view.findViewById(R.id.details_date);
        dateView.setTypeface(font);
        descriptionLinearLayout = (LinearLayout) view.findViewById(R.id.details_desc_layout);
        descriptionView = (TextView) view.findViewById(R.id.details_description);
        descriptionView.setTypeface(font);
        fullArticleButton = (Button) view.findViewById(R.id.button_full_article);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setData(Uri.parse(feed.getLink()));
                startActivity(intent);
            }
        });

        viewTreeObserver = descriptionLinearLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                descriptionLayoutWidth = descriptionLinearLayout.getWidth();

                if(urlList.size() !=0 && utils.isOnline()){
                    getActivity().setProgressBarIndeterminateVisibility(true);
                    Toast.makeText(getActivity(), "Downloading images ...", Toast.LENGTH_SHORT).show();
                    new DownloadThread(urlList, downloadFinishHandler, cachedImages).start();
                }

                else if(urlList.size() == 0) {

                    displayTextWithImages();
                }

                else if(!utils.isOnline() && urlList.size() != 0) {
                    displayOnlyText();
                }

                descriptionLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getImagesUrl(String textIn){

        Html.fromHtml(feed.getDescription(), new Html.ImageGetter() {

            URL url;
            int count = 0;

            @Override
            public Drawable getDrawable(String source) {

                try{
                    if(utils.getCachedImage(source) == null){

                        url = new URL(source);
                        urlList.add(url);
                        count++;
                    }
                }
                catch (MalformedURLException mue){
                    mue.printStackTrace();
                }

                return null;
            }
        }, null);

    }

    private void displayOnlyText(){

        titleView.setText(feed.getTitle());
        creatorView.setText(feed.getCreator());
        dateView.setText(utils.getFormattedDate(feed.getDate()));
        descriptionView.setText(Html.fromHtml(feed.getDescription(), null, null));

    }

    private void displayTextWithImages(){

        titleView.setText(feed.getTitle());
        creatorView.setText(feed.getCreator());
        dateView.setText(utils.getFormattedDate(feed.getDate()));

        descriptionView.setText(Html.fromHtml(feed.getDescription(), new Html.ImageGetter() {

            Drawable drawable;
            int w;
            int h;
            double ratio;

            @Override
            public Drawable getDrawable(String source) {

                if(utils.getCachedImage(source) != null) {

                    drawable = utils.getCachedImage(source);
                    w = drawable.getIntrinsicWidth();
                    h = drawable.getIntrinsicHeight();
                    ratio = (double) w/h;

                    drawable.setBounds(0,0, descriptionLayoutWidth, (int) (descriptionLayoutWidth/ratio));
                }

                return drawable;
            }
        }, null));
    }

    private class DownloadThread extends Thread{

        private Handler handler;
        private List<URL> urlList;
        private HashMap<String, BitmapDrawable> cache;
        private Drawable image;

        public DownloadThread(List<URL> urlList, Handler handlerIn, HashMap<String, BitmapDrawable> cacheIn) {

            handler = handlerIn;
            this.urlList = urlList;
            cache = cacheIn;
        }

        @Override
        public void run() {
            super.run();

            try {
                for (URL url : urlList) {
                    image = BitmapDrawable.createFromStream(url.openStream(), url.toString());
                    cache.put(url.toString(), (BitmapDrawable) image);
                }

                handler.sendEmptyMessage(IMAGE_DOWNLOAD_QUEUE_COMPLETED);
            }

            catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(IMAGE_DOWNLOAD_CONNECTION_EXCEPTION);
            }
        }
    }
}
