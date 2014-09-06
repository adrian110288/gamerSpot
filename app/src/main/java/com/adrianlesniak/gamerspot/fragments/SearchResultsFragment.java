package com.adrianlesniak.gamerspot.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.database.DAO;
import com.adrianlesniak.gamerspot.extra.CommonUtilities;
import com.adrianlesniak.gamerspot.extra.CustomTypefaceSpan;
import com.adrianlesniak.gamerspot.extra.GamerSpotApplication;
import com.adrianlesniak.gamerspot.extra.NewsFeedsAdapter;
import com.adrianlesniak.gamerspot.interfaces.OnHeadlineSelectedListener;

import java.util.ArrayList;


/**
 * Created by Adrian on 24-Aug-14.
 */
public class SearchResultsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<NewsFeed> feedList;
    private NewsFeedsAdapter feedsAdapter;
    private OnHeadlineSelectedListener mCallback;
    private ListView listView;
    private NewsFeed feedSelectedForContextmenu;
    private DAO dao;
    private Activity parentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;

        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedList = (ArrayList<NewsFeed>) getArguments().get("resultList");
        feedsAdapter = new NewsFeedsAdapter(parentActivity, feedList);
        dao = GamerSpotApplication.getUtils(parentActivity).getDatabaseAccessor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_headlines, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setListView(view);
    }

    private void setListView(View view) {
        listView = (ListView) view.findViewById(R.id.search_headlines_list_view);
        listView.setAdapter(feedsAdapter);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        SpannableString spannableString;

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        feedSelectedForContextmenu = feedList.get(info.position);

        if (v.getId() == R.id.search_headlines_list_view) {
            parentActivity.getMenuInflater().inflate(R.menu.context_menu_list_item, menu);

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
                spannableString.setSpan(new CustomTypefaceSpan(parentActivity, "fonts/sans.semi-condensed.ttf"), 0, infotext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

                if (deleted) {
                    boolean removed = feedList.remove(feedSelectedForContextmenu);

                    Log.i("REMOVED", removed + "");

                    feedsAdapter.notifyDataSetChanged();
                    CommonUtilities.showToast("News removed");

                    if (feedList.size() == 0) {
                        parentActivity.finish();
                    }
                }
            }
        }


        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onArticleSelected(feedList.get(position), true);
        listView.setItemChecked(position, true);
        dao.setFeedVisited(feedList.get(position).getGuid());
    }

}
