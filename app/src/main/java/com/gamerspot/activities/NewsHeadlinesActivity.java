package com.gamerspot.activities;

import android.content.Intent;
import android.os.Bundle;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.fragments.NewsHeadlinesFragment;
import com.gamerspot.interfaces.OnHeadlineSelectedListener;

public class NewsHeadlinesActivity extends NavigationDrawerActivity implements OnHeadlineSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar("GamerSpot");

        if (findViewById(R.id.content_frame) != null) {

            if (savedInstanceState != null) {
                return;
            }

            NewsHeadlinesFragment headlinesFragment = new NewsHeadlinesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, headlinesFragment, "MAIN").commit();
        }
    }

    @Override
    public void onArticleSelected(NewsFeed feedClicked) {

        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("FEED", feedClicked);
        startActivity(intent);
    }
}
