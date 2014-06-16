package com.gamerspot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gamerspot.beans.NewsFeed;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsDetailsFragment extends Fragment {

    private NewsFeed feed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feed = (NewsFeed) getArguments().get("FEED");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
