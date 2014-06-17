package com.gamerspot.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gamerspot.R;
import com.gamerspot.beans.NewsFeed;

/**
 * Created by Adrian on 13-Jun-14.
 */
public class NewsDetailsFragment extends Fragment {

    private NewsFeed feed;
    private Typeface font;
    private DateFormat df;

    private TextView titleView;
    private TextView creatorView;
    private TextView dateView;
    private TextView descriptionView;
    private Button fullArticleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getActivity().getAssets(), "sans.semi-condensed.ttf");
        df = new DateFormat();
        feed = (NewsFeed) getArguments().get("FEED");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_details, null);

        titleView = (TextView) view.findViewById(R.id.details_title);
        titleView.setTypeface(font);
        creatorView = (TextView) view.findViewById(R.id.details_creator);
        creatorView.setTypeface(font);
        dateView = (TextView) view.findViewById(R.id.details_date);
        dateView.setTypeface(font);
        descriptionView = (TextView) view.findViewById(R.id.details_description);
        descriptionView.setTypeface(font);
        fullArticleButton = (Button) view.findViewById(R.id.button_full_article);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleView.setText(feed.getTitle());
        creatorView.setText(feed.getCreator());
        dateView.setText(df.format(getActivity().getResources().getString(R.string.date_format), feed.getDate()));
        descriptionView.setText(feed.getDescription());
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
