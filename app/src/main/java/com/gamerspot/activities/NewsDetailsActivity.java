package com.gamerspot.activities;

import android.os.Bundle;
import android.view.View;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.fragments.NewsDetailsFragment;
import com.gamerspot.interfaces.FullArticleClickListener;

/**
 * Created by Adrian Lesniak on 16-Jun-14.
 */
public class NewsDetailsActivity extends BaseActivity {

    private FullArticleClickListener fullArticleClickListener;
    private NewsDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        NewsFeed feedIn = (NewsFeed) getIntent().getSerializableExtra("FEED");
        setActionBar(feedIn.getPlatform(), "");

        detailsFragment = new NewsDetailsFragment();
        fullArticleClickListener = detailsFragment;
        Bundle b = new Bundle();
        b.putSerializable("FEED", feedIn);
        detailsFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction().add(R.id.details_content_frame, detailsFragment).commit();
    }

    public void goToFullArticle(View view) {
        detailsFragment.goToFullArticle(view);
    }
}
