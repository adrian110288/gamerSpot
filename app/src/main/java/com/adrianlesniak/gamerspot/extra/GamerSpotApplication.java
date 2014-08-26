package com.adrianlesniak.gamerspot.extra;

import android.app.Application;
import android.content.Context;

/**
 * Created by Adrian Lesniak on 20-Jun-14.
 */
public class GamerSpotApplication extends Application {

    private static CommonUtilities utils = null;

    public GamerSpotApplication() {
    }

    public static CommonUtilities getUtils(Context c) {

        if (utils == null) {
            utils = new CommonUtilities(c);
        }

        return utils;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
