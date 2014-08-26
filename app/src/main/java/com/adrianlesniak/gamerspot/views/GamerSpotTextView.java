package com.adrianlesniak.gamerspot.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adrianlesniak.gamerspot.extra.CommonUtilities;

/**
 * Created by Adrian on 23-Aug-14.
 */
public class GamerSpotTextView extends TextView {

    public GamerSpotTextView(Context context) {
        super(context);
        init();
    }

    public GamerSpotTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GamerSpotTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setTypeface(CommonUtilities.getTextFont());
    }
}
