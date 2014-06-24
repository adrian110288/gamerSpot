package com.gamerspot.extra;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        utils = App.getUtils(context);
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

            convertView.setTag(holder);

        }
        else {
            holder = (DrawerNewsViewHolder) convertView.getTag();
        }

        itemText = getItem(position);
        holder.newsItem.setText(itemText);

        return convertView;

    }

}
