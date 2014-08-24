package com.adrianlesniak.gamerspot.activities;

import android.content.Intent;
import android.os.Bundle;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.fragments.SearchResultsFragment;
import com.adrianlesniak.gamerspot.interfaces.OnHeadlineSelectedListener;

import java.util.ArrayList;

/**
 * Created by Adrian on 24-Aug-14.
 */
public class SearchResultActivity extends BaseActivity implements OnHeadlineSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ArrayList<NewsFeed> searchResults = (ArrayList<NewsFeed>) getIntent().getSerializableExtra("resultList");
        setActionBar(searchResults.size() + " search results");
        setActionBarSubtitle("Searched for \"" + getIntent().getStringExtra("searchPhrase") + "\"");

        if (findViewById(R.id.content_frame) != null) {

            if (savedInstanceState != null) {
                return;
            }

            SearchResultsFragment headlinesFragment = new SearchResultsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("resultList", searchResults);
            headlinesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, headlinesFragment, "MAIN").commit();
        }
    }

    @Override
    public void onArticleSelected(NewsFeed feedClicked, boolean isSearched) {

        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("FEED", feedClicked);
        intent.putExtra("isSearched", isSearched);
        startActivity(intent);
    }
}
