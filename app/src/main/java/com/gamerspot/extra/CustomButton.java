package com.gamerspot.extra;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.gamerspot.R;
import com.gamerspot.enums.ButtonType;

/**
 * Created by Adrian on 12-Jul-14.
 */
public class CustomButton extends Button {

    private Context context;
    private TypedArray attr;
    private int buttonType;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initializeButton(attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initializeButton(AttributeSet attrs){

        setTypeface(App.getUtils(context).getTextFont());
        setTextColor(getResources().getColor(R.color.BUTTON_TEXT_COLOR));
        setBackground(getResources().getDrawable(R.drawable.fa_selector));

        attr = context.obtainStyledAttributes(attrs, R.styleable.custom);

        buttonType = attr.getInt(R.styleable.custom_type, 0);

        if(buttonType == (ButtonType.ARTICLE.getType())) {

            setText(R.string.button_full_article);
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.collections_view_as_list, 0, 0, 0);
            setCompoundDrawablePadding(-250);
            setPadding(200,0,0,0);
        }

        else if(buttonType == (ButtonType.SEARCH.getType())) {
            setText(R.string.search_dialog_button);
        }

        attr.recycle();
    }

}
