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

        actionbar = getSupportActionBar();

        NewsFeed feedIn = (NewsFeed) getIntent().getSerializableExtra("FEED");

        setActionbar(feedIn.getPlatform());

        NewsDetailsFragment detailsFragment = new NewsDetailsFragment();
        Bundle b = new Bundle();
        b.putSerializable("FEED", feedIn);
        detailsFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction().add(R.id.details_content_frame, detailsFragment).commit();
    }

    //TODO Use CommonUtilities
    private void setActionbar(int platformIn) {

        String appName = getResources().getString(R.string.app_name);
        SpannableString spannableString = new SpannableString(appName);
        spannableString.setSpan(new CustomTypefaceSpan(this, "Gamegirl.ttf"),0, appName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionbar.setTitle(spannableString);

        switch (platformIn){

            case 1: {
                actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PLATFORM_PC)));
                break;
            }
            case 2: {
                actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PLATFORM_XBOX)));
                break;
            }
            case 3: {
                actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PLATFORM_PLAYSTATION)));
                break;
            }
            case 4: {
                actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PLATFORM_NINTENDO)));
                break;
            }
            case 5: {
                actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PLATFORM_MOBILE)));
                break;
            }
        }

    }
}
