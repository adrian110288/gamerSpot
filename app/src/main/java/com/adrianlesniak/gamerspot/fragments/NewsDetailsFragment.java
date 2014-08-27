package com.adrianlesniak.gamerspot.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.database.DAO;
import com.adrianlesniak.gamerspot.extra.CommonUtilities;
import com.adrianlesniak.gamerspot.interfaces.FullArticleClickListener;
import com.adrianlesniak.gamerspot.views.GamerSpotButton;
import com.enrique.stackblur.StackBlurManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsDetailsFragment extends Fragment implements FullArticleClickListener {

    private static final int IMAGE_DOWNLOAD_CONNECTION_EXCEPTION = -1;
    private static final int IMAGE_DOWNLOAD_QUEUE_COMPLETED = 1;
    private static HashMap<String, BitmapDrawable> cachedImages = new HashMap<String, BitmapDrawable>();
    private static List<URL> urlList;
    private NewsFeed feed;
    private int descriptionLayoutWidth = 0;
    private TextView titleView;
    private TextView creatorView;
    private TextView dateView;
    private GamerSpotButton fullArticleButton;
    private TextView descriptionView;
    private ImageView backgroundImageView;
    private LinearLayout descriptionLinearLayout;
    private ViewTreeObserver viewTreeObserver;
    private DAO dao;
    private boolean isArticleFavourite = false;
    private boolean isSearched;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        feed = (NewsFeed) getArguments().get("FEED");
        isSearched = getArguments().getBoolean("searched");
        urlList = new ArrayList<URL>();
        dao = CommonUtilities.getDatabaseAccessor();
        getImagesUrl(feed.getDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_details, container, false);

        Typeface font = CommonUtilities.getTextFont();

        titleView = (TextView) view.findViewById(R.id.details_title);
        titleView.setTypeface(font);
        creatorView = (TextView) view.findViewById(R.id.details_creator);
        creatorView.setTypeface(font);
        dateView = (TextView) view.findViewById(R.id.details_date);
        dateView.setTypeface(font);
        descriptionLinearLayout = (LinearLayout) view.findViewById(R.id.details_desc_layout);
        descriptionView = (TextView) view.findViewById(R.id.details_description);
        descriptionView.setTypeface(font);
        backgroundImageView = (ImageView) view.findViewById(R.id.backgroundImageView);
        fullArticleButton = (GamerSpotButton) view.findViewById(R.id.button_full_article);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewTreeObserver = descriptionLinearLayout.getViewTreeObserver();

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            @TargetApi(16)
            public void onGlobalLayout() {

                descriptionLayoutWidth = descriptionLinearLayout.getWidth();

                if (urlList.size() != 0 && CommonUtilities.isOnline()) {
                    getActivity().setProgressBarIndeterminateVisibility(true);
                    new DownloadThread(urlList, new DownloadFinishHandler(), cachedImages).start();
                } else if (urlList.size() == 0) {
                    displayText();
                } else if (!CommonUtilities.isOnline() && urlList.size() != 0) {
                    displayOnlyText();
                }

                try {
                    descriptionLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } catch (NoSuchMethodError nsme) {
                    descriptionLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    public void goToFullArticle(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(feed.getLink()));
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_favourite);

        if (dao.isFavourite(feed.getGuid())) {
            isArticleFavourite = true;
            menuItem.setIcon(R.drawable.ic_action_rating_important);
        } else menuItem.setIcon(R.drawable.ic_action_rating_not_important);
    }

    @Override
    @TargetApi(14)
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }

            case R.id.action_favourite: {

                if (isArticleFavourite) {
                    boolean isRemoved = dao.removeFromFavourites(feed.getGuid());
                    if (isRemoved) {
                        item.setIcon(R.drawable.ic_action_rating_not_important);
                        CommonUtilities.showToast(getResources().getString(R.string.article_removed_from_fav));
                    }
                } else {
                    boolean isAdded = dao.addToFavourites(feed.getGuid());
                    if (isAdded) {
                        item.setIcon(R.drawable.ic_action_rating_important);
                        CommonUtilities.showToast(getResources().getString(R.string.article_added_to_fav));
                    }
                }

                isArticleFavourite = !isArticleFavourite;

                return true;
            }

            case R.id.action_share: {

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this article " + feed.getLink());
                sendIntent.setType("text/plain");

                startActivity(sendIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getImagesUrl(String textIn) {

        Html.fromHtml(feed.getDescription(), new Html.ImageGetter() {

            URL url;
            int count = 0;

            @Override
            public Drawable getDrawable(String source) {

                try {
                    if (CommonUtilities.getCachedImage(source) == null) {

                        url = new URL(source);
                        urlList.add(url);
                        count++;
                    }
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                }

                return null;
            }
        }, null);

    }

    private void displayOnlyText() {

        titleView.setText(feed.getTitle());
        creatorView.setText(feed.getCreator());
        dateView.setText(CommonUtilities.getFormattedDate(feed.getDate()));
        descriptionView.setText(Html.fromHtml(feed.getDescription(), null, null));
    }

    private void displayText() {

        displayOnlyText();

        descriptionView.setText(Html.fromHtml(feed.getDescription(), new Html.ImageGetter() {

            Drawable drawable;
            int w;
            int h;
            double ratio;

            @Override
            public Drawable getDrawable(String source) {

                if (CommonUtilities.getCachedImage(source) != null) {

                    drawable = CommonUtilities.getCachedImage(source);
                    w = drawable.getIntrinsicWidth();
                    h = drawable.getIntrinsicHeight();
                    ratio = (double) w / h;

                    drawable.setBounds(0, 0, descriptionLayoutWidth, (int) (descriptionLayoutWidth / ratio));
                }

                return drawable;
            }
        }, null));

        /*Bitmap bitmap = CommonUtilities.getCachedBlurredImage(urlList.get(0).toString());

        if(bitmap != null) {
            backgroundImageView.setImageBitmap(bitmap);
        }*/
    }

    private Bitmap blurImage(Bitmap bitmap) {
        StackBlurManager stackBlurManager = new StackBlurManager(bitmap);
        return stackBlurManager.process(100);
    }

    private class DownloadThread extends Thread {

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
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(IMAGE_DOWNLOAD_CONNECTION_EXCEPTION);
            }
        }
    }

    //TODO Make blurredImages persistent

    private class DownloadFinishHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == IMAGE_DOWNLOAD_QUEUE_COMPLETED) {

                getActivity().setProgressBarIndeterminateVisibility(false);

                CommonUtilities.setCachedImages(cachedImages);

                titleView.setText(feed.getTitle());
                creatorView.setText(feed.getCreator());
                dateView.setText(CommonUtilities.getFormattedDate(feed.getDate()));

                URL url = urlList.get(0);
                BitmapDrawable d = cachedImages.get(url.toString());
                Bitmap blurredBitmap = blurImage(d.getBitmap());
                CommonUtilities.addCachedBlurredImage(url.toString(), blurredBitmap);

                displayText();

                descriptionView.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (msg.what == IMAGE_DOWNLOAD_CONNECTION_EXCEPTION) {
                displayOnlyText();
            }
        }
    }
}
