package com.gamerspot.extra;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;

import com.gamerspot.R;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Adrian Lesniak on 19-Jun-14.
 */
public class CommonUtilities {

    private Context context;
    private AssetManager assetManager;
    private Typeface textFont;
    private Typeface themeFont;
    private DateFormat df;
    private String dateFormatString;

    private HashMap<String, Drawable> cachedImages;

    public CommonUtilities(Context c){

        context = c;
        assetManager = context.getAssets();
        textFont = Typeface.createFromAsset(assetManager, "sans.semi-condensed.ttf");
        themeFont = Typeface.createFromAsset(assetManager, "Gamegirl.ttf");
        df = new DateFormat();
        dateFormatString = context.getResources().getString(R.string.date_format);

        cachedImages = new HashMap<String, Drawable>();
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

    public HashMap<String, Drawable> getCachedImages(){
        return cachedImages;
    }

    public void addCachedImage(String url, Drawable image) {
        cachedImages.put(url, image);
    }
}
