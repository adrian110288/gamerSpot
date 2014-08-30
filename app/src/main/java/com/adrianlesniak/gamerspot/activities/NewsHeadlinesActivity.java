package com.adrianlesniak.gamerspot.activities;

import android.content.Intent;
import android.os.Bundle;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.NewsFeed;
import com.adrianlesniak.gamerspot.fragments.NewsHeadlinesFragment;
import com.adrianlesniak.gamerspot.interfaces.OnHeadlineSelectedListener;

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
    public void onArticleSelected(NewsFeed feedClicked, boolean isSearched) {

        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra("FEED", feedClicked);

        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_to_top, R.anim.activity_stay_visible);
    }
}
