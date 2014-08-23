package com.gamerspot.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.gamerspot.R;
import com.gamerspot.database.DAO;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class CommonUtilities {

    private static Context context;
    private static Typeface textFont;
    private static Typeface boldTextFont;
    private static Typeface themeFont;
    private static DateFormat df;
    private static String dateFormatString;
    private static DAO dao;
    private static HashMap<String, BitmapDrawable> cachedImages;
    private static HashMap<String, Bitmap> cachedBlurredImages;

    public CommonUtilities(Context c) {

        context = c;
        textFont = Typeface.createFromAsset(context.getAssets(), "fonts/sans.semi-condensed.ttf");
        boldTextFont = Typeface.createFromAsset(context.getAssets(), "fonts/dejaVu_sans_semi_condensed_bold.ttf");
        themeFont = Typeface.createFromAsset(context.getAssets(), "fonts/Gamegirl.ttf");
        df = new DateFormat();
        dateFormatString = context.getResources().getString(R.string.date_format);
        dao = new DAO(context);
        cachedImages = new HashMap<String, BitmapDrawable>();
        cachedBlurredImages = new HashMap<String, Bitmap>();
    }

    public static boolean isOnline() {

        boolean isOnline = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

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

    public static Typeface getTextFont() {
        return textFont;
    }

    public static Typeface getBoldTextFont() {
        return boldTextFont;
    }

    public static Typeface getThemeFont() {
        return themeFont;
    }

    public static String getFormattedDate(Date dateIn) {

        return df.format(dateFormatString, dateIn).toString();
    }

    public static HashMap<String, BitmapDrawable> getCachedImages() {
        return cachedImages;
    }

    public static void setCachedImages(HashMap<String, BitmapDrawable> imagesIn) {
        cachedImages = imagesIn;
    }

    public static void addCachedBlurredImage(String imageId, Bitmap imageBitmapIn) {
        cachedBlurredImages.put(imageId, imageBitmapIn);
    }

    public static BitmapDrawable getCachedImage(String idIn) {
        return cachedImages.get(idIn);
    }

    public static Bitmap getCachedBlurredImage(String idIn) {
        return cachedBlurredImages.get(idIn);
    }

    public static DAO getDatabaseAccessor() {
        return dao;
    }

    public static void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}


