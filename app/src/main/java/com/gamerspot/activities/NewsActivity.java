package com.gamerspot.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.*;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;
import com.gamerspot.database.DAO;
import com.gamerspot.database.GamerSpotDBHelper;
import com.gamerspot.extra.CustomTypefaceSpan;
import com.gamerspot.extra.NewsFeedsAdapter;
import com.gamerspot.fragments.NewsHeadlinesFragment;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

public class NewsActivity extends BaseActivity {

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
}
