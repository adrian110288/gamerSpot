package com.gamerspot.extra;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Adrian on 10-Jul-14.
 */
public class Animations {

    public static void slideOut(View view){

        int viewHeight = view.getHeight();

        view.invalidate();

        ObjectAnimator.ofFloat(view, "translationY", -50).setDuration(200).start();
    }

    public static void slideIn(View view){

        view.invalidate();
        ObjectAnimator.ofFloat(view, "translationY", 0).setDuration(200).start();
    }
}
