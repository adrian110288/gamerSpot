package com.gamerspot.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Window;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.extra.App;
import com.gamerspot.extra.CustomTypefaceSpan;
import com.gamerspot.fragments.NewsDetailsFragment;

/**
 * Created by Adrian Lesniak on 16-Jun-14.
 */
public class NewsDetailsActivity extends ActionBarActivity {

    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_news_details);


        NewsFeed feedIn = (NewsFeed) getIntent().getSerializableExtra("FEED");

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        App.getUtils(getApplicationContext()).setActionBar(actionbar, feedIn.getPlatform(), "GamerSpot");

        NewsDetailsFragment detailsFragment = new NewsDetailsFragment();
        Bundle b = new Bundle();
        b.putSerializable("FEED", feedIn);
        detailsFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction().add(R.id.details_content_frame, detailsFragment).commit();
    }

}
