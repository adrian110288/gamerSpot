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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleSelected(NewsFeed feedClicked) {

            Intent intent = new Intent(this, NewsDetailsActivity.class);
            intent.putExtra("FEED", feedClicked);
            startActivity(intent);

    }
}
