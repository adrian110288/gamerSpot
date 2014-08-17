package com.gamerspot.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.support.v7.widget.ShareActionProvider;
import android.view.*;
import android.widget.*;

import com.enrique.stackblur.StackBlurManager;
import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.database.DAO;
import com.gamerspot.extra.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsDetailsFragment extends Fragment {

    private static final int IMAGE_DOWNLOAD_CONNECTION_EXCEPTION = -1;
    private static final int IMAGE_DOWNLOAD_QUEUE_COMPLETED = 1;

    private NewsFeed feed;
    private int descriptionLayoutWidth = 0;
    private TextView titleView;
    private TextView creatorView;
    private TextView dateView;
    private TextView descriptionView;
    private ImageView backgroundImageView;
    private LinearLayout descriptionLinearLayout;
    private static HashMap<String, BitmapDrawable> cachedImages = new HashMap();
    private static List<URL> urlList;
    private ViewTreeObserver viewTreeObserver;
    private static CommonUtilities utils;
    private DAO dao;
    private boolean isArticleFavourite = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        utils = GamerSpotApplication.getUtils(getActivity());
        feed = (NewsFeed) getArguments().get("FEED");
        urlList = new ArrayList<URL>();
        dao =utils.getDatabaseAccessor();
        getImagesUrl(feed.getDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_details, container, false);

        Typeface font = utils.getTextFont();

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

                if(urlList.size() !=0 && utils.isOnline()){
                    getActivity().setProgressBarIndeterminateVisibility(true);
                    new DownloadThread(urlList, new DownloadFinishHandler(), cachedImages).start();
                    Toast.makeText(getActivity(), "Downloading images ...", Toast.LENGTH_SHORT).show();
                }

                else if(urlList.size() == 0) {
                    displayText();
                }

                else if(!utils.isOnline() && urlList.size() != 0) {
                    displayOnlyText();
                }

                try{
                    descriptionLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                catch (NoSuchMethodError nsme){
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
        setShareOption(menu.findItem(R.id.action_share));

        MenuItem menuItem = menu.findItem(R.id.action_favourite);

        if(dao.isFavourite(feed.getGuid())) {
            isArticleFavourite = true;
            menuItem.setIcon(R.drawable.ic_action_3_rating_important);
        }
        else menuItem.setIcon(R.drawable.ic_action_3_rating_not_important);
    }

    @Override
    @TargetApi(14)
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            }

            case R.id.action_favourite: {

                if(isArticleFavourite == true) {
                    boolean isRemoved = dao.removeFromFavourites(feed.getGuid());
                    if(isRemoved) {
                        item.setIcon(R.drawable.ic_action_3_rating_not_important);
                        utils.showToast("Article removed as favourites");
                    }
                }
                else {
                    boolean isAdded = dao.addToFavourites(feed.getGuid());
                    if(isAdded) {
                        item.setIcon(R.drawable.ic_action_3_rating_important);
                        utils.showToast("Article added from favourites");
                    }
                }

                isArticleFavourite = !isArticleFavourite;

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(14)
    private void setShareOption(MenuItem shareitem){
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareitem);
        shareActionProvider.setShareIntent(getShareIntent());
    }

    @TargetApi(14)
    private Intent getShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this article " + feed.getLink());
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    @Override
    public void onResume() {
        super.onResume();


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

    private void displayText(){

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

    private class DownloadFinishHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == IMAGE_DOWNLOAD_QUEUE_COMPLETED) {

                getActivity().setProgressBarIndeterminateVisibility(false);

                utils.setCachedImages(cachedImages);

                titleView.setText(feed.getTitle());
                creatorView.setText(feed.getCreator());
                dateView.setText(utils.getFormattedDate(feed.getDate()));

                URL url = urlList.get(0);
                BitmapDrawable d = cachedImages.get(url.toString());

                Bitmap bitmap = d.getBitmap();

                backgroundImageView.setImageBitmap(blurImage(bitmap));

                displayText();

                descriptionView.setMovementMethod(LinkMovementMethod.getInstance());
            }

            else if (msg.what == IMAGE_DOWNLOAD_CONNECTION_EXCEPTION ) {
                displayOnlyText();
            }
        }
    }

    //TODO Make blurredImages persistent

    private Bitmap blurImage(Bitmap bitmap) {
        StackBlurManager stackBlurManager = new StackBlurManager(bitmap);
        return stackBlurManager.process(100);
    }
}
