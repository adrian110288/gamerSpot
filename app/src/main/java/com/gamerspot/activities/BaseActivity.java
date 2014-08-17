package com.gamerspot.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gamerspot.R;
import com.gamerspot.extra.GamerSpotApplication;
import com.gamerspot.extra.CommonUtilities;
import com.gamerspot.extra.DrawerNewsAdapter;
import com.gamerspot.fragments.NewsHeadlinesFragment;

/**
 * Created by Adrian on 12-Jun-14.
 */
public class BaseActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerNewsListView;
    private DrawerNewsAdapter drawerListAdapter;
    private ActionBar bar;

    private ActionBarDrawerToggle drawerToggle;
    private String drawerTitle;

    private NewsHeadlinesFragment fragment;
    private Bundle fragmentBundle;
    private CommonUtilities utils;

    private static String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_news);

        utils = GamerSpotApplication.getUtils(getApplicationContext());
        /*
         * Customization of ActionBar
         */
        appName  = getApplicationContext().getResources().getString(R.string.app_name);
        bar = getSupportActionBar();
        utils.setActionBar(bar, 0, appName);
        drawerTitle = getResources().getString(R.string.drawer_title);

        utils.setDrawerItemSelected(0);

        instantiateViews();

        drawerListAdapter = new DrawerNewsAdapter(this);
        drawerNewsListView.setAdapter(drawerListAdapter);

        drawerNewsListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(utils.getDrawerItemSelected() != id){
                    fragment = (NewsHeadlinesFragment) getSupportFragmentManager().findFragmentByTag("MAIN");
                    fragment.refresh(id);
                }

                drawerLayout.closeDrawer(drawerNewsListView);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                utils.setActionBar(bar, 0,drawerTitle);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                utils.setActionBar(bar, 0, appName);
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

    private void instantiateViews(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerNewsListView = (ListView) findViewById(R.id.left_drawer_newslist);

    }

}
