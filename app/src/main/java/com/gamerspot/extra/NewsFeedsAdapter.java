package com.gamerspot.extra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gamerspot.R;
import com.gamerspot.beans.FeedViewHolder;
import com.gamerspot.beans.NewsFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 10-Jun-14.
 */
public class NewsFeedsAdapter extends ArrayAdapter<NewsFeed> {

    private Context context;
    private Typeface platformFont;
    private Typeface titleFont;
    private Typeface dateFont;
    private ArrayList<NewsFeed> feedsList;
    private FeedViewHolder holder;
    private NewsFeed feed;
    private int platform;
    private DateFormat df;
    private LayoutInflater mInflater;

    public NewsFeedsAdapter(Context contextIn, List<NewsFeed> feedsListIn) {
        super(contextIn, 0, feedsListIn);

        context = contextIn;
        platformFont = Typeface.createFromAsset(context.getAssets(), "Gamegirl.ttf");
        titleFont = Typeface.createFromAsset(context.getAssets(), "sans.semi-condensed.ttf");
        dateFont = Typeface.createFromAsset(context.getAssets(), "sans.semi-condensed.ttf");
        df = new DateFormat();

        feedsList = (ArrayList<NewsFeed>) feedsListIn;
    }

    @Override
    public int getCount() {
        return feedsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        feed = getItem(position);
        platform = feed.getPlatform();

        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.feed_list_item, null);

            holder = new FeedViewHolder();

            holder.title_textView = (TextView) convertView.findViewById(R.id.feed_title);
            holder.platform_textView = (TextView) convertView.findViewById(R.id.feed_platform);
            holder.date_textView = (TextView) convertView.findViewById(R.id.feed_date);
            holder.helperView = convertView.findViewById(R.id.helperView);

            holder.title_textView.setTypeface(titleFont);
            holder.platform_textView.setTypeface(platformFont);
            holder.date_textView.setTypeface(dateFont);

            convertView.setTag(holder);

        } else
            holder = (FeedViewHolder) convertView.getTag();

        holder.title_textView.setText(feed.getTitle());

        switch(platform){
            case 1: {
                holder.platform_textView.setText("PC");
                holder.helperView.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_PC));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.pc_newsfeed_selector));
                break;
            }

            case 2: {
                holder.platform_textView.setText("XBOX");
                holder.helperView.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_XBOX));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.xbox_newsfeed_selector));
                break;
            }

            case 3: {
                holder.platform_textView.setText("PLAYSTATION");
                holder.helperView.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_PLAYSTATION));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.playstation_newsfeed_selector));
                break;
            }

            case 4: {
                holder.platform_textView.setText("NINTENDO");
                holder.helperView.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_NINTENDO));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.nintendo_newsfeed_selector));
                break;
            }

            case 5: {
                holder.platform_textView.setText("MOBILE");
                holder.helperView.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_MOBILE));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.mobile_newsfeed_selector));
                break;
            }

            default: {
                holder.platform_textView.setText("");
                holder.helperView.setBackgroundColor(0);
                break;
            }
        }

        holder.date_textView.setText(df.format(context.getResources().getString(R.string.date_format), feed.getDate()));

        return convertView;
    }
}
