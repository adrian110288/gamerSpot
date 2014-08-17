package com.gamerspot.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamerspot.R;
import com.gamerspot.extra.GamerSpotApplication;


/**
 * Created by Adrian on 01-Aug-14.
 */
public class FeedsNotification extends LinearLayout implements Animation.AnimationListener {

    private Context context;
    private boolean isShown = false;
    private static final int IMAGE_LAYOUT_ID= R.layout.notifier_circle_image_layout;
    private static final int INFO_LAYOUT_ID= R.layout.notifier_circle_news_layout;
    private static float ORIGINAL_POSITION = -1000;
    private static float ANIMATE_TO_POSITION = 120;

    private TextView numberTextView;
    private TextView textTextView;
    private int count = 0;

    private static Animation foldFlipAnimator;
    private static Animation revealFlipAnimator;

    public FeedsNotification(Context context) {
        super(context);
        init(context);
    }

    public FeedsNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FeedsNotification(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        this.context = context;

        foldFlipAnimator = AnimationUtils.loadAnimation(context, R.anim.notifier_fold_anim);
        revealFlipAnimator = AnimationUtils.loadAnimation(context, R.anim.notified_reveal_anim);
        foldFlipAnimator.setAnimationListener(this);
        revealFlipAnimator.setAnimationListener(this);

        inflate(getContext(), IMAGE_LAYOUT_ID, this);
        //setY(ORIGINAL_POSITION);
        setPivotX(0.5f);
        setPivotY(0.5f);

    }

    private void setUpViews() {
        numberTextView = (TextView) findViewById(R.id.feeds_notif_number);
        textTextView = (TextView) findViewById(R.id.feeds_notif_static_text);
        numberTextView.setText(Integer.toString(count));
        setUpTypeface();
    }

    private void setUpTypeface() {
        Typeface textTypeface = GamerSpotApplication.getUtils(context).getTextFont();
        numberTextView.setTypeface(textTypeface);
        textTextView.setTypeface(textTypeface);
    }

    public void show(int count){
        setShown(true);
        setCountText(count);
        startAnimation();
    }

    private void setCountText(int count){
        this.count = count;
    }

    private void startAnimation(){

        clearAnimation();
        setAnimation(foldFlipAnimator);
        startAnimation(foldFlipAnimator);
    }

    @TargetApi(14)
    private void slideDown(){

    }
    @TargetApi(14)
    private void flip(){

    }

    @TargetApi(14)
    private void slideUp(){

    }

    public boolean isShown(){
        return isShown;
    }

    public float getOriginalPosition(){
        return ORIGINAL_POSITION;
    }

    public void setOriginalPosition(int position){
        ORIGINAL_POSITION = position;
    }

    public float getAnimateToPosition(){
        return ANIMATE_TO_POSITION;
    }

    public void setAnimateToPosition(int position) {
        ANIMATE_TO_POSITION = position;
    }

    public void setShown(boolean shown){
        isShown = shown;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if(animation == foldFlipAnimator){
            inflate(context, INFO_LAYOUT_ID, null);
            //setUpViews();
            clearAnimation();
            setAnimation(revealFlipAnimator);
            startAnimation(revealFlipAnimator);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
