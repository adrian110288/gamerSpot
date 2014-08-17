package com.gamerspot.extra;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Adrian Lesniak on 20-Jun-14.
 */
public class GamerSpotApplication extends Application {

    private static CommonUtilities utils = null;

    public GamerSpotApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static CommonUtilities getUtils(Context c){

        if(utils == null){
            utils = new CommonUtilities(c);
        }

        return utils;
    }
}
