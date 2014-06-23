package com.gamerspot.activities;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gamerspot.R;
import com.gamerspot.extra.App;
import com.gamerspot.extra.CustomTypefaceSpan;
import com.gamerspot.extra.DrawerNewsAdapter;

import static com.gamerspot.extra.App.*;

/**
 * Created by Adrian on 12-Jun-14.
 */
public class BaseActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerNewsListView;
    private DrawerNewsAdapter drawerListAdapter;
    private ActionBar bar;
    private TextView newsHeader;

    private ActionBarDrawerToggle drawerToggle;
    private String drawerTitle;

    private Typeface navDrawerHeaderTypeface;

    private static String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_news);

        /*
         * Customization of ActionBar
         */
        appName  = getApplicationContext().getResources().getString(R.string.app_name);
        bar = getSupportActionBar();
        App.getUtils(getApplicationContext()).setActionBar(bar, 0, appName);
        drawerTitle = getResources().getString(R.string.drawer_title);

        instantiateViews();

        drawerListAdapter = new DrawerNewsAdapter(this);
        drawerNewsListView.setAdapter(drawerListAdapter);

        drawerNewsListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                App.getUtils(getApplicationContext()).setActionBar(bar, 0,drawerTitle);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                App.getUtils(getApplicationContext()).setActionBar(bar, 0, appName);
                //setActionBar(actionBarTitle);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    /*
    private void setActionBar(String title) {

        ActionBar actionBar = getSupportActionBar();
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new CustomTypefaceSpan(this, "Gamegirl.ttf"),0, appName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannableString);

    }
*/
    private void instantiateViews(){

        newsHeader = (TextView) findViewById(R.id.left_drawer_news_heading);
        navDrawerHeaderTypeface = Typeface.createFromAsset(getAssets(), "weblysleekuis_bold.ttf");
        newsHeader.setTypeface(navDrawerHeaderTypeface);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerNewsListView = (ListView) findViewById(R.id.left_drawer_newslist);

    }

}
