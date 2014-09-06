package com.adrianlesniak.gamerspot.activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Window;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.extra.CustomTypefaceSpan;
import com.adrianlesniak.gamerspot.extra.GamerSpotApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Adrian on 12-Jun-14.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setActionBar();
    }

    protected void setActionBar() {
        setActionBar(getResources().getString(R.string.app_name));
    }

    protected void setActionBar(String name) {
        setActionBar(0, name);
    }

    protected void setActionBar(int platformIn, String name) {

        SpannableString spannableString;
        String actionBarTitle = "";

        if (actionBar == null) {
            actionBar = getSupportActionBar();
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        spannableString = new SpannableString(name);
        actionBarTitle = name;

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        spannableString.setSpan(new CustomTypefaceSpan(this, "fonts/Gamegirl.ttf"), 0, actionBarTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannableString);

        if (platformIn != 0) {

            ColorDrawable colorDrawable = null;
            Drawable iconDrawable = null;

            switch (platformIn) {

                case 1: {
                    colorDrawable = new ColorDrawable(this.getResources().getColor(R.color.PLATFORM_PC));
                    iconDrawable = getResources().getDrawable(R.drawable.ic_action_90px_noun_project_216);
                    break;
                }
                case 2: {
                    colorDrawable = new ColorDrawable(this.getResources().getColor(R.color.PLATFORM_XBOX));
                    iconDrawable = getResources().getDrawable(R.drawable.ic_action_consoles_xbox_512);
                    break;
                }
                case 3: {
                    colorDrawable = new ColorDrawable(this.getResources().getColor(R.color.PLATFORM_PLAYSTATION));
                    iconDrawable = getResources().getDrawable(R.drawable.ic_action_consoles_ps_512);
                    break;
                }
                case 4: {
                    colorDrawable = new ColorDrawable(this.getResources().getColor(R.color.PLATFORM_NINTENDO));
                    iconDrawable = getResources().getDrawable(R.drawable.ic_action_nintendo_logo_2);
                    break;
                }
                case 5: {
                    colorDrawable = new ColorDrawable(this.getResources().getColor(R.color.PLATFORM_MOBILE));
                    iconDrawable = getResources().getDrawable(R.drawable.ic_action_mobile_phone_8_512);
                    break;
                }
            }

            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setIcon(iconDrawable);
        }
    }

    protected void setActionBarSubtitle(String subtitle) {
        SpannableString spannableString = new SpannableString(subtitle);
        spannableString.setSpan(new CustomTypefaceSpan(this, "fonts/sans.semi-condensed.ttf"), 0, subtitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setSubtitle(spannableString);
    }

    public void sendEvent(String category, String action, String label) {

        Tracker tracker = GamerSpotApplication.getTracker(getApplicationContext());

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());

        Log.i("LOG", "Sending Event");
    }

    public void sendScreen(String screenName) {

        Tracker tracker = GamerSpotApplication.getTracker(getApplicationContext());
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.AppViewBuilder().build());
    }

}
