package com.adrianlesniak.gamerspot.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.extra.CommonUtilities;
import com.adrianlesniak.gamerspot.extra.DrawerNewsAdapter;
import com.adrianlesniak.gamerspot.fragments.NewsHeadlinesFragment;

/**
 * Created by Adrian on 21-Aug-14.
 */
public abstract class NavigationDrawerActivity extends BaseActivity implements ListView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private LinearLayout drawerContainer;
    private TextView catHeader;
    private ListView drawerNewsListView;
    private DrawerNewsAdapter drawerListAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private NewsHeadlinesFragment fragment;
    private long selectedDrawerItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setNavigationDrawer();
    }

    protected void setNavigationDrawer() {
        findViewReferences();

        drawerListAdapter = new DrawerNewsAdapter(this);
        drawerNewsListView.setAdapter(drawerListAdapter);
        drawerNewsListView.setOnItemClickListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_navigation_drawer, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setActionBar(getResources().getString(R.string.drawer_title));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setActionBar();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        catHeader.setTypeface(CommonUtilities.getThemeFont());
    }

    private void findViewReferences() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerContainer = (LinearLayout) findViewById(R.id.drawerlayout);
        drawerNewsListView = (ListView) findViewById(R.id.left_drawer_newslist);
        catHeader = (TextView) findViewById(R.id.cat_header);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        sendEvent("Navigation drawer", "Select", id + "");

        if (selectedDrawerItem != id) {
            setDrawerItemSelected(id);

            fragment = (NewsHeadlinesFragment) getSupportFragmentManager().findFragmentByTag("MAIN");
            fragment.refresh(id);
            drawerLayout.closeDrawer(drawerContainer);
        }

    }

    public long getDrawerItemSelected() {
        return selectedDrawerItem;
    }

    public void setDrawerItemSelected(long id) {
        selectedDrawerItem = id;
        CommonUtilities.getDatabaseAccessor().resetLimits();

        for (int i = 0; i < drawerNewsListView.getCount(); i++) {

            View drawerItem = drawerNewsListView.getChildAt(i);
            drawerItem.setBackgroundResource(R.drawable.navigation_drawer_selector);
            View indicator = drawerItem.findViewById(R.id.drawer_news_listItem_indicator);
            indicator.setVisibility(View.INVISIBLE);

            if (i == id) {

                drawerItem.setBackgroundResource(R.color.NAV_DRAWER_COLOR_SELECTED);
                indicator.setVisibility(View.VISIBLE);
                break;
            }

        }
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
}
