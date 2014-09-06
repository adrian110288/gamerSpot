package com.adrianlesniak.gamerspot.extra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.adrianlesniak.gamerspot.R;
import com.adrianlesniak.gamerspot.beans.DrawerNewsViewHolder;

/**
 * Created by Adrian Lesniak on 16-Jun-14.
 */

public class DrawerNewsAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] newsItems;
    private LayoutInflater mInflater;
    private DrawerNewsViewHolder holder;
    private View convertView;
    private Typeface typeface;
    private String itemText;
    private CommonUtilities utils;

    public DrawerNewsAdapter(Context context) {
        super(context, 0);

        this.context = context;
        utils = GamerSpotApplication.getUtils(context);
        typeface = utils.getTextFont();
        newsItems = context.getResources().getStringArray(R.array.drawer_news_items);
    }

    @Override
    public int getCount() {
        return newsItems.length;
    }

    @Override
    public String getItem(int position) {
        return newsItems[position];
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.convertView = convertView;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.drawer_news_item, null);

            holder = new DrawerNewsViewHolder();
            holder.newsItem = (android.widget.TextView) convertView.findViewById(R.id.drawer_news_listItem_textView);
            holder.indicator = convertView.findViewById(R.id.drawer_news_listItem_indicator);
            holder.itemIcon = (android.widget.ImageView) convertView.findViewById(R.id.item_icon);

            convertView.setTag(holder);

        } else {
            holder = (DrawerNewsViewHolder) convertView.getTag();
        }

        itemText = getItem(position);
        holder.newsItem.setText(itemText);


        if (position == 0) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setBackgroundColor(context.getResources().getColor(android.R.color.black));
            holder.itemIcon.setVisibility(View.GONE);
            ((LinearLayout) holder.indicator.getParent()).setBackgroundColor(getContext().getResources().getColor(R.color.NAV_DRAWER_COLOR_SELECTED));
        } else {
            holder.itemIcon.setVisibility(View.VISIBLE);

            if (position == 1) {
                holder.indicator.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                holder.itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_90px_noun_project_216));
            } else if (position == 2) {
                holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_XBOX));
                holder.itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_consoles_xbox_512));
            } else if (position == 3) {
                holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_PLAYSTATION));
                holder.itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_consoles_ps_512));
            } else if (position == 4) {
                holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_NINTENDO));
                holder.itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_nintendo_logo_2));
            } else if (position == 5) {
                holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_MOBILE));
                holder.itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_mobile_phone_8_512));
            } else if (position == 6) {
                holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.FAVOURITE_FEEDS));
                holder.itemIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_rating_important));
            }
        }

        return convertView;

    }

}
