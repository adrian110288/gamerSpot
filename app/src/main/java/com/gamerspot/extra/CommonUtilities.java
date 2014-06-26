package com.gamerspot.extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;

import com.gamerspot.R;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class CommonUtilities {

    private static Context context;
    private AssetManager assetManager;
    private static ConnectivityManager connectivityManager;
    private static NetworkInfo networkInfo;
    private Typeface textFont;
    private Typeface themeFont;
    private DateFormat df;
    private String dateFormatString;
    private static String appName;
    private static SpannableString spannableString;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private String drawerItemSelectedKey;

    private HashMap<String, BitmapDrawable> cachedImages;

    public CommonUtilities(Context c){

        context = c;
        assetManager = context.getAssets();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        textFont = Typeface.createFromAsset(assetManager, "sans.semi-condensed.ttf");
        themeFont = Typeface.createFromAsset(assetManager, "Gamegirl.ttf");
        appName = context.getResources().getString(R.string.app_name);
        spannableString = new SpannableString(appName);
        df = new DateFormat();
        dateFormatString = context.getResources().getString(R.string.date_format);

        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_pref_name), Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
        drawerItemSelectedKey = context.getResources().getString(R.string.drawer_item_selected);

        cachedImages = new HashMap<String, BitmapDrawable>();
    }

    public Typeface getTextFont(){
        return textFont;
    }

    public Typeface getThemeFont(){
        return themeFont;
    }

    public String getFormattedDate(Date dateIn){

        return df.format(dateFormatString, dateIn).toString();
    }

    public HashMap<String, BitmapDrawable> getCachedImages(){
        return cachedImages;
    }

    public BitmapDrawable getCachedImage(String idIn){

        return cachedImages.get(idIn);
    }

    public void addCachedImage(String url, BitmapDrawable image) {
        cachedImages.put(url, image);
    }

    public void setCachedImages(HashMap<String, BitmapDrawable> imagesIn) {

        cachedImages = imagesIn;
    }

    public void setActionBar(ActionBar actionBar, int platformIn, String name) {

        if(name != null) {
            spannableString = new SpannableString(name);
        }
        else {
            spannableString = new SpannableString(appName);
        }

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        spannableString.setSpan(new CustomTypefaceSpan(context, "Gamegirl.ttf"),0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannableString);

        if(platformIn != 0) {

            switch (platformIn){

                case 1: {
                    actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.PLATFORM_PC)));
                    break;
                }
                case 2: {
                    actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.PLATFORM_XBOX)));
                    break;
                }
                case 3: {
                    actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.PLATFORM_PLAYSTATION)));
                    break;
                }
                case 4: {
                    actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.PLATFORM_NINTENDO)));
                    break;
                }
                case 5: {
                    actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.PLATFORM_MOBILE)));
                    break;
                }
            }

        }
    }

    public static boolean isOnline() {

        boolean isOnline = false;

        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo.isConnected()) {
            isOnline = true;
        } else {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (networkInfo.isConnected()) {
                isOnline = true;
            }
        }

        return isOnline;
    }

    public long getDrawerItemSelected(){

        return sharedPreferences.getLong(drawerItemSelectedKey, 0);

    }

    public void setDrawerItemSelected(long id) {

        prefEditor.putLong(drawerItemSelectedKey, id);
        prefEditor.commit();
    }

}


