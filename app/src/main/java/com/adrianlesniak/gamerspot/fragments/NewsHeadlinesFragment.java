package com.adrianlesniak.gamerspot.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.activities.NavigationDrawerActivity;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.database.DAO;
import com.adrianlesniak.gamerspot.extra.CommonUtilities;
import com.adrianlesniak.gamerspot.extra.CustomTypefaceSpan;
import com.adrianlesniak.gamerspot.extra.GamerSpotApplication;
import com.adrianlesniak.gamerspot.extra.NewsFeedsAdapter;
import com.adrianlesniak.gamerspot.interfaces.OnHeadlineSelectedListener;
import com.adrianlesniak.gamerspot.threading.FeedFetcherTask;

import java.util.ArrayList;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsHeadlinesFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static FeedFetcherTask downloadTask;
    private static boolean buttonDismissed = false;
    private static int buttonVisible = Button.GONE;
    private static ArrayList<NewsFeed> feedList = new ArrayList<NewsFeed>();
    private Context context;
    private ListView listView;
    private NewsFeedsAdapter feedsAdapter;
    private OnHeadlineSelectedListener mCallback;
    private DAO dao;
    private SearchDialogFragment searchDialogFragment;
    private AboutDialogFragment aboutDialogFragment;
    private FeedFetchHandler feedFetchHandler;
    private int mLastFirstVisibleItem = 0;
    private boolean animDownFinished = false;
    private boolean animUpFinished = false;
    private NewsFeed feedSelectedForContextmenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
        dao = GamerSpotApplication.getUtils(context).getDatabaseAccessor();
        feedFetchHandler = new FeedFetchHandler();

        //TODO Loader required - Genymotion log (Skipped xxx frames. The application may be doing too much work on its main thread.)

        feedList = dao.getFeeds(null);
        feedsAdapter = new NewsFeedsAdapter(context, feedList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_headlines, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mCallback.onArticleSelected(feedList.get(position), false);
        listView.setItemChecked(position, true);
        dao.setFeedVisited(feedList.get(position).getGuid());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            if (listView.getLastVisiblePosition() >= listView.getCount() - 1) {
                loadMoreData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (listView.getId() == view.getId() && !buttonDismissed) {

            int currentFirstVisibleItem = listView.getFirstVisiblePosition();

            if (buttonVisible == Button.VISIBLE) {

            /* Scrolling down the list */
                if (currentFirstVisibleItem > mLastFirstVisibleItem) {

                    if (!animDownFinished) {

                        animDownFinished = true;
                        animUpFinished = false;
                    }

                /* Scrolling up the list */
                } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {

                    if (!animUpFinished) {

                        animUpFinished = true;
                        animDownFinished = false;
                    }
                }
            }
            mLastFirstVisibleItem = currentFirstVisibleItem;
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListView(view);

        if (CommonUtilities.isOnline()) {

            getActivity().setProgressBarIndeterminateVisibility(true);
            downloadTask = new FeedFetcherTask(context, feedFetchHandler);
            //downloadTask.execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListView(View view) {
        listView = (ListView) view.findViewById(R.id.headlines_list_view);
        listView.setAdapter(feedsAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        registerForContextMenu(listView);
    }

    private void loadMoreData() {

        long platformId = ((NavigationDrawerActivity) getActivity()).getDrawerItemSelected();
        ArrayList<NewsFeed> dataToAttach;

        if (platformId == 0) {
            dataToAttach = dao.loadMoreDataForScroll(null);
        } else {
            dataToAttach = dao.loadMoreDataForScroll(platformId);
        }

        feedList.addAll(dataToAttach);
        feedsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SpannableString spannableString;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new CustomTypefaceSpan(getActivity(), "fonts/sans.semi-condensed.ttf"), 0, menuItem.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            menuItem.setTitle(spannableString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {

            searchDialogFragment = new SearchDialogFragment();
            searchDialogFragment.setTargetFragment(this, 1);
            searchDialogFragment.show(getFragmentManager(), "search");

            return true;
        }

        if (id == R.id.action_refresh) {

            //TODO repeated code. Create method for starting async task
            if (CommonUtilities.isOnline()) {

                getActivity().setProgressBarIndeterminateVisibility(true);
                downloadTask = new FeedFetcherTask(context, feedFetchHandler);
                downloadTask.execute();
            } else
                Toast.makeText(getActivity(), context.getResources().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();

            return true;
        }

        if (id == R.id.action_about) {
            aboutDialogFragment = new AboutDialogFragment();
            aboutDialogFragment.setTargetFragment(this, 1);
            aboutDialogFragment.show(getFragmentManager(), "about");
        }

        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        SpannableString spannableString;

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        feedSelectedForContextmenu = feedList.get(info.position);

        if (v.getId() == R.id.headlines_list_view) {
            getActivity().getMenuInflater().inflate(R.menu.context_menu_list_item, menu);

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                String infotext = "";

                if (menuItem.getItemId() == R.id.context_toggle_favourite) {
                    if (dao.isFavourite(feedSelectedForContextmenu.getGuid())) {
                        infotext = "Remove from favourites";
                    } else {
                        infotext = "Add to favourites";
                    }
                } else if (menuItem.getItemId() == R.id.context_toggle_read) {
                    if (dao.isVisited(feedSelectedForContextmenu.getGuid())) {
                        infotext = "Mark as unread";
                    } else {
                        infotext = "Mark as read";
                    }
                } else {
                    infotext = "Delete";
                }

                spannableString = new SpannableString(infotext);
                spannableString.setSpan(new CustomTypefaceSpan(getActivity(), "fonts/sans.semi-condensed.ttf"), 0, infotext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                menuItem.setTitle(spannableString);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String feedId = feedSelectedForContextmenu.getGuid();

        switch (item.getItemId()) {

            case R.id.context_toggle_favourite: {
                if (dao.isFavourite(feedId)) {
                    dao.removeFromFavourites(feedId);
                    CommonUtilities.showToast(getResources().getString(R.string.article_removed_from_fav));

                    //TODO remove from list if Nav drawer item == 6

                } else {
                    dao.addToFavourites(feedId);
                    CommonUtilities.showToast(getResources().getString(R.string.article_added_to_fav));
                }

                break;
            }

            case R.id.context_toggle_read: {
                if (dao.isVisited(feedId)) {
                    dao.setFeedNotVisited(feedId);
                    CommonUtilities.showToast("Marked as unread");
                } else {
                    dao.setFeedVisited(feedId);
                    CommonUtilities.showToast("Marked as read");
                }
                break;
            }

            case R.id.context_delete_feed: {
                boolean deleted = dao.removeFeed(feedId);

                if(deleted){
                    feedList.remove(feedSelectedForContextmenu);
                    feedsAdapter.notifyDataSetChanged();
                    CommonUtilities.showToast("News removed");
                }
            }
        }


        return super.onContextItemSelected(item);
    }

    public void refresh(long id) {

        if (id == 6) {
            feedList = dao.getAllFavourites();

            if (feedList.size() == 0) {
                CommonUtilities.showToast("No favourites");
            }
        } else {

            if (id > 0 && id < 6) {
                feedList = dao.getFeeds(id);
            } else if (id == 0) {
                feedList = dao.getFeeds(null);
            }

        }

        feedsAdapter = new NewsFeedsAdapter(context, feedList);
        listView.setAdapter(feedsAdapter);
        listView.smoothScrollToPosition(0);
    }

    private class FeedFetchHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int newFeeds = msg.what;

            if (newFeeds > 0 && ((NavigationDrawerActivity) getActivity()).getDrawerItemSelected() == 0) {
                refresh(0);
            }
            CommonUtilities.showToast(getResources().getQuantityString(R.plurals.new_feeds_plurals, newFeeds, newFeeds));
            getActivity().setProgressBarIndeterminateVisibility(false);
        }
    }
}
