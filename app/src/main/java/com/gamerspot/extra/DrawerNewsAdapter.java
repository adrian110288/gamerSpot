package com.gamerspot.extra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gamerspot.R;
import com.gamerspot.beans.DrawerNewsViewHolder;

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

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.drawer_news_item, null);

            holder = new DrawerNewsViewHolder();
            holder.newsItem = (android.widget.TextView) convertView.findViewById(R.id.drawer_news_listItem_textView);
            holder.newsItem.setTypeface(typeface);

            holder.indicator = (View) convertView.findViewById(R.id.drawer_news_listItem_indicator);

            convertView.setTag(holder);

        }
        else {
            holder = (DrawerNewsViewHolder) convertView.getTag();
        }

        itemText = getItem(position);
        holder.newsItem.setText(itemText);


        if(position == 0) holder.indicator.setVisibility(View.INVISIBLE);
        else if(position == 1) holder.indicator.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        else if(position == 2) holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_XBOX));
        else if(position == 3) holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_PLAYSTATION));
        else if(position == 4) holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_NINTENDO));
        else if(position == 5) holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_MOBILE));

        return convertView;

    }

}
