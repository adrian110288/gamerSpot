package com.gamerspot.extra;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Adrian Lesniak on 20-Jun-14.
 */
public class App extends Application {

    private static CommonUtilities utils = null;

    public App(){}

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("LOG", "Application created");
    }

    public static CommonUtilities getUtils(Context c){

        if(utils == null){

            Log.i("LOG", "Creating Utils class");
            utils = new CommonUtilities(c);
        }

        return utils;
    }
}
