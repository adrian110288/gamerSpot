package com.gamerspot.activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Window;

import com.gamerspot.R;
import com.gamerspot.extra.CustomTypefaceSpan;

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
        spannableString.setSpan(new CustomTypefaceSpan(this, "Gamegirl.ttf"), 0, actionBarTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannableString);

        if (platformIn != 0) {

            ColorDrawable colorDrawable = null;
            Drawable iconDrawable = null;

            switch (platformIn) {

                case 1: {
                    colorDrawable = new ColorDrawable(this.getResources().getColor(R.color.PLATFORM_PC));
                    iconDrawable = getResources().getDrawable(R.drawable.ic_action_mouse_512);
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

}
