package com.gamerspot.extra;

import android.app.Application;
import android.util.Log;

/**
 * Created by Adrian Lesniak on 20-Jun-14.
 */
public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("LOG", "Application created");
    }
}
