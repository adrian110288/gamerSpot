package com.adrianlesniak.gamerspot.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.extra.CustomTypefaceSpan;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedList = (ArrayList<NewsFeed>) getArguments().get("resultList");
        feedsAdapter = new NewsFeedsAdapter(getActivity(), feedList);
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

        if (v.getId() == R.id.search_headlines_list_view) {
            getActivity().getMenuInflater().inflate(R.menu.context_menu_list_item, menu);

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                spannableString = new SpannableString(menuItem.getTitle());
                spannableString.setSpan(new CustomTypefaceSpan(getActivity(), "fonts/sans.semi-condensed.ttf"), 0, menuItem.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                menuItem.setTitle(spannableString);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onArticleSelected(feedList.get(position), true);
        listView.setItemChecked(position, true);
    }

}
