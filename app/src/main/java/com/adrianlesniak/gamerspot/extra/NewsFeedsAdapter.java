package com.adrianlesniak.gamerspot.extra;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.FeedViewHolder;
import com.adrianlesniak.gamerspot.beans.NewsFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 10-Jun-14.
 */
public class NewsFeedsAdapter extends ArrayAdapter<NewsFeed> {

    private ArrayList<NewsFeed> feedsList;
    private FeedViewHolder holder;
    private NewsFeed feed;
    private int platform;
    private LayoutInflater mInflater;

    public NewsFeedsAdapter(Context contextIn, List<NewsFeed> feedsListIn) {
        super(contextIn, 0, feedsListIn);
        mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.feed_list_item, null);

            holder = new FeedViewHolder();

            holder.title_textView = (TextView) convertView.findViewById(R.id.feed_title);
            holder.platform_imageView = (ImageView) convertView.findViewById(R.id.platform_image_view);
            holder.date_textView = (TextView) convertView.findViewById(R.id.feed_date);
            holder.creator_textView = (TextView) convertView.findViewById(R.id.feed_creator);
            holder.image_holder = (android.widget.LinearLayout) convertView.findViewById(R.id.image_holder);
            holder.infoView = (android.widget.RelativeLayout) convertView.findViewById(R.id.info_view);

            holder.title_textView.setTypeface(CommonUtilities.getTextFont());
            holder.date_textView.setTypeface(CommonUtilities.getTextFont());
            holder.creator_textView.setTypeface(CommonUtilities.getBoldTextFont());

            convertView.setTag(holder);

        } else {
            holder = (FeedViewHolder) convertView.getTag();
        }

        holder.title_textView.setText(feed.getTitle());
        holder.creator_textView.setText(feed.getProvider());

        switch (platform) {
            case 1: {
                // holder.platform_textView.setText("PC");
                holder.platform_imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_90px_noun_project_216));
                holder.image_holder.setBackgroundColor(getColor(R.color.PLATFORM_PC));
                holder.infoView.setBackgroundResource(R.drawable.pc_newsfeed_selector);
                break;
            }

            case 2: {
                //holder.platform_textView.setText("XBOX");
                holder.platform_imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_consoles_xbox_512));
                holder.image_holder.setBackgroundColor(getColor(R.color.PLATFORM_XBOX));
                holder.infoView.setBackgroundResource(R.drawable.xbox_newsfeed_selector);
                break;
            }

            case 3: {
                // holder.platform_textView.setText("PLAYSTATION");
                holder.platform_imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_consoles_ps_512));
                holder.image_holder.setBackgroundColor(getColor(R.color.PLATFORM_PLAYSTATION));
                holder.infoView.setBackgroundResource(R.drawable.playstation_newsfeed_selector);
                break;
            }

            case 4: {
                //holder.platform_textView.setText("NINTENDO");
                holder.platform_imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_nintendo_logo_2));
                holder.image_holder.setBackgroundColor(getColor(R.color.PLATFORM_NINTENDO));
                holder.infoView.setBackgroundResource(R.drawable.nintendo_newsfeed_selector);
                break;
            }

            case 5: {
                //holder.platform_textView.setText("MOBILE");
                holder.platform_imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_mobile_phone_8_512));
                holder.image_holder.setBackgroundColor(getColor(R.color.PLATFORM_MOBILE));
                holder.infoView.setBackgroundResource(R.drawable.mobile_newsfeed_selector);
                break;
            }

            default: {
                break;
            }
        }

        holder.date_textView.setText(CommonUtilities.getFormattedDate(feed.getDate()));

        return convertView;
    }

    private int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }
}
