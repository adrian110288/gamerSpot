package com.gamerspot.animations;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.view.View;
import android.widget.Button;

/**
 * Created by Adrian on 10-Jul-14.
 */
public class NewFeedsButtonAnimations {

    @TargetApi(14)
    public static void slideButtonUp (final Button buttonIn, int rootViewHeight) {

        ObjectAnimator.ofFloat(buttonIn, View.TRANSLATION_Y, rootViewHeight - 300).setDuration(200).start();
    }

    @TargetApi(14)
    public static void slideButtonDown (final Button buttonIn, int rootViewHeight) {

        ObjectAnimator.ofFloat(buttonIn, View.TRANSLATION_Y, rootViewHeight).setDuration(200).start();
    }
}
