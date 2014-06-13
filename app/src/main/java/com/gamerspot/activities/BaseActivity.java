package com.gamerspot.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gamerspot.R;

/**
 * Created by Adrian on 12-Jun-14.
 */
public class BaseActivity extends ActionBarActivity {

    private String[] drawerItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ArrayAdapter<String> drawerListAdapter;

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence actionBarTitle;
    private String drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBarTitle = getTitle();
        drawerTitle = getResources().getString(R.string.drawer_title);

        drawerItems = this.getResources().getStringArray(R.array.nav_drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        drawerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems);
        drawerListView.setAdapter(drawerListAdapter);

        drawerListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(drawerTitle);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(actionBarTitle);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
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

}