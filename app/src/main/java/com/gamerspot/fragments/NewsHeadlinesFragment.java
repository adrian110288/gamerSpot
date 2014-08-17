package com.gamerspot.fragments;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import com.gamerspot.R;
import com.gamerspot.animations.NewFeedsButtonAnimations;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.database.DAO;
import com.gamerspot.extra.*;
import com.gamerspot.interfaces.OnHeadlineSelectedListener;
import com.gamerspot.threading.FeedFetcherTask;

import java.util.ArrayList;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsHeadlinesFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    private Context context;
    private static FeedFetcherTask downloadTask;
    private ListView listView;
    private static boolean buttonDismissed = false;
    private static int buttonVisible = Button.GONE;
    private NewsFeedsAdapter feedsAdapter;
    private OnHeadlineSelectedListener mCallback;
    private static ArrayList<NewsFeed> feedList = new ArrayList<NewsFeed>();
    private DAO dao;
    private SearchDialogFragment searchDialogFragment;
    private CommonUtilities utils;
    private FeedFetchHandler feedFetchHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
        utils = GamerSpotApplication.getUtils(context);
        dao = new DAO(context);
        feedFetchHandler = new FeedFetchHandler();

        //TODO Loader required - Genymotion log (Skipped xxx frames. The application may be doing too much work on its main thread.)

        feedList = dao.getAllFeeds();
        dao.close();
        feedsAdapter = new NewsFeedsAdapter(context, feedList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

        mCallback.onArticleSelected(feedList.get(position));
        listView.setItemChecked(position, true);
    }

    private int mLastFirstVisibleItem = 0;
    private boolean animDownFinished = false;
    private boolean animUpFinished = false;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(listView.getId() == view.getId() && buttonDismissed != true) {

            int currentFirstVisibleItem = listView.getFirstVisiblePosition();

            if(buttonVisible  == Button.VISIBLE) {

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

        if (utils.isOnline()) {

            getActivity().setProgressBarIndeterminateVisibility(true);
            downloadTask = new FeedFetcherTask(context, feedFetchHandler);
            downloadTask.execute();
        }

        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListView(View view){
        listView = (ListView) view.findViewById(R.id.headlines_list_view);
        listView.setAdapter(feedsAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_search) {

            searchDialogFragment = new SearchDialogFragment();
            searchDialogFragment.setTargetFragment(this, 1);
            searchDialogFragment.show(getFragmentManager(), "search");

            return true;
        }

        if(id == R.id.action_refresh) {

            if(GamerSpotApplication.getUtils(context).isOnline()){

                getActivity().setProgressBarIndeterminateVisibility(true);
                downloadTask = new FeedFetcherTask(context, feedFetchHandler);
                downloadTask.execute();
            }

            else Toast.makeText(getActivity(), context.getResources().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();

            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getActivity(), data.getStringExtra("phrase"), Toast.LENGTH_SHORT).show();
    }

    public void refresh(long id) {

        utils.setDrawerItemSelected(id);

        //feedList.clear();

        if(id != 0) {
            feedList = dao.getPlatformFeeds(id);
        }

        else {
            feedList = dao.getAllFeeds();
        }

        feedsAdapter = new NewsFeedsAdapter(context, feedList);
        listView.setAdapter(feedsAdapter);
        dao.close();
    }

    private class FeedFetchHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            utils.showToast("NEW FEEDS: "+msg.what);
            getActivity().setProgressBarIndeterminateVisibility(false);
        }
    }
}
