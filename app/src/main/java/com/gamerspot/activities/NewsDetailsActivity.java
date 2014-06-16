package com.gamerspot.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.*;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.fragments.NewsDetailsFragment;

/**
 * Created by Adrian Lesniak on 16-Jun-14.
 */
public class NewsDetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        NewsFeed feedIn = (NewsFeed) getIntent().getSerializableExtra("FEED");

        NewsDetailsFragment detailsFragment = new NewsDetailsFragment();
        Bundle b = new Bundle();
        b.putSerializable("FEED", feedIn);
        detailsFragment.setArguments(b);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.details_content_frame, detailsFragment);
        transaction.commit();

        //getSupportFragmentManager().beginTransaction().add(R.id.details_content_frame, detailsFragment).commit();
    }
}
