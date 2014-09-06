package com.adrianlesniak.gamerspot.extra;

import android.app.Application;
import android.content.Context;

import com.adrianlesniak.gamerspot.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Adrian Lesniak on 20-Jun-14.
 */
public class GamerSpotApplication extends Application {

    private static CommonUtilities utils = null;
    private static Tracker t = null;

    public GamerSpotApplication() {
    }

    public static CommonUtilities getUtils(Context c) {

        if (utils == null) {
            utils = new CommonUtilities(c);
        }

        return utils;
    }

    synchronized public static Tracker getTracker(Context c) {

        if (t == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(c);
            t = analytics.newTracker(R.xml.app_tracker);
        }

        return t;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
