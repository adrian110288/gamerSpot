package com.adrianlesniak.gamerspot.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
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
        return inflater.inflate(R.layout.fragment_news_headlines, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setListView(view);
    }

    private void setListView(View view) {
        listView = (ListView) view.findViewById(R.id.headlines_list_view);
        listView.setAdapter(feedsAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onArticleSelected(feedList.get(position), true);
        listView.setItemChecked(position, true);
    }
}
