package com.gamerspot.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.format.DateFormat;
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

import java.io.FileInputStream;
import java.io.IOException;
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

    private NewsFeed feed;
    private Typeface font;
    private DateFormat df;
    private int descriptionLayoutWidth = 0;

    private TextView titleView;
    private TextView creatorView;
    private TextView dateView;
    private TextView descriptionView;
    private Button fullArticleButton;
    private LinearLayout descriptionLinearLayout;

    private static HashMap<String, BitmapDrawable> cachedImages;
    private Handler downloadFinishHandler;
    private List<URL> urlList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getActivity().getAssets(), "sans.semi-condensed.ttf");
        cachedImages = new HashMap();

        Log.i("NO OF IMAGES", cachedImages.size()+"");

        df = new DateFormat();
        feed = (NewsFeed) getArguments().get("FEED");
        urlList = new ArrayList<URL>();

        downloadFinishHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {

                    descriptionView.setText(Html.fromHtml(feed.getDescription(), new Html.ImageGetter() {

                        Drawable drawable;
                        int w;
                        int h;
                        double ratio;

                        @Override
                        public Drawable getDrawable(String source) {

                            if(cachedImages.containsKey(source)) {

                                drawable = cachedImages.get(source);
                                w = drawable.getIntrinsicWidth();
                                h = drawable.getIntrinsicHeight();
                                ratio = (double) w/h;

                                drawable.setBounds(0,0, descriptionLayoutWidth, (int) (descriptionLayoutWidth/ratio));

                            }

                            return drawable;
                        }
                    }, null));

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

        titleView.setText(feed.getTitle());
        creatorView.setText(feed.getCreator());
        dateView.setText(df.format(getActivity().getResources().getString(R.string.date_format), feed.getDate()));

        Html.fromHtml(feed.getDescription(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {

                try{

                    URL url = new URL(source);
                    urlList.add(url);

                }
                catch (MalformedURLException mue){
                    mue.printStackTrace();
                }

                return null;
            }
        }, null);


        final ViewTreeObserver viewTreeObserver = descriptionLinearLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                descriptionLayoutWidth = descriptionLinearLayout.getWidth();

                new DownloadThread(urlList, downloadFinishHandler, cachedImages).start();

                descriptionLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
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

            Log.i("THREAD", "thread starting");

            try {
                for (URL url : urlList) {
                    image = BitmapDrawable.createFromStream(url.openStream(), url.toString());
                    cache.put(url.toString(), (BitmapDrawable) image);
                }

                handler.sendEmptyMessage(1);
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }


        }
    }
}
