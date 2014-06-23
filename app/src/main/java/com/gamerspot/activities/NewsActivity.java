package com.gamerspot.activities;

import android.content.Intent;
import android.os.*;
import android.view.*;
import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.fragments.NewsDetailsFragment;
import com.gamerspot.fragments.NewsHeadlinesFragment;

public class NewsActivity extends BaseActivity implements NewsHeadlinesFragment.OnHeadlineSelectedListener{

    private NewsDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(findViewById(R.id.content_frame ) != null) {

            if (savedInstanceState != null) {
                return;
            }

            NewsHeadlinesFragment headlinesFragment = new NewsHeadlinesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, headlinesFragment).commit();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onArticleSelected(NewsFeed feedClicked) {

            Intent intent = new Intent(this, NewsDetailsActivity.class);
            intent.putExtra("FEED", feedClicked);
            startActivity(intent);
    }
}
