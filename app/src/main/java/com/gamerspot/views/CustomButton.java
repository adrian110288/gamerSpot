package com.gamerspot.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamerspot.R;
import com.gamerspot.enums.ButtonType;
import com.gamerspot.extra.CommonUtilities;

/**
 * Created by Adrian on 12-Jul-14.
 */
public class CustomButton extends RelativeLayout {

    private TypedArray attr;
    private int buttonType;
    private TextView text;
    private ImageView image;

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeButton(attrs);
    }

    private void initializeButton(AttributeSet attrs) {

        inflate(getContext(), R.layout.full_article_button_layout, this);
        obtainReferences();

        attr = getContext().obtainStyledAttributes(attrs, R.styleable.custom);
        buttonType = attr.getInt(R.styleable.custom_type, 0);
        attr.recycle();

        if (buttonType == (ButtonType.ARTICLE.getType())) {
            text.setText(R.string.button_full_article);
            image.setImageDrawable(getResources().getDrawable(R.drawable.collections_view_as_list));
        } else if (buttonType == (ButtonType.SEARCH.getType())) {
            text.setText(R.string.search_dialog_button);
            image.setImageDrawable(getResources().getDrawable(R.drawable.search_bck));
        }
    }

    private void obtainReferences() {
        text = (TextView) findViewById(R.id.button_text);
        text.setTypeface(CommonUtilities.getTextFont());
        image = (ImageView) findViewById(R.id.button_image);
    }

    private void updateSelector(int platformId) {

    }

}
