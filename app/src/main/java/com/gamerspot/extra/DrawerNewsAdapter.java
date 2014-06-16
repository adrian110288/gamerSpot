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

    public DrawerNewsAdapter(Context context) {
        super(context, 0);

        this.context = context;
        typeface = Typeface.createFromAsset(context.getAssets(), "weblysleekuisl.ttf");
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
            holder.stub = (ViewStub) convertView.findViewById(R.id.drawer_stub);
            holder.newsItem = (android.widget.TextView) convertView.findViewById(R.id.drawer_news_listItem_textView);
            holder.newsItem.setTypeface(typeface);

            convertView.setTag(holder);

        }
        else {
            holder = (DrawerNewsViewHolder) convertView.getTag();
        }

        String itemText = getItem(position);
        holder.newsItem.setText(itemText);

        if(position>0) {

            holder.newsIndicator = holder.stub.inflate();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.newsIndicator.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, R.id.drawer_news_indicator);
            holder.newsIndicator.setLayoutParams(params);

            if(itemText.toLowerCase().indexOf("pc") != -1){
                holder.newsIndicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_PC));
            }

            if(itemText.toLowerCase().indexOf("xbox") != -1){
                holder.newsIndicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_XBOX));
            }

            if(itemText.toLowerCase().indexOf("playstation") != -1){
                holder.newsIndicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_PLAYSTATION));
            }

            if(itemText.toLowerCase().indexOf("nintendo") != -1){
                holder.newsIndicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_NINTENDO));
            }

            if(itemText.toLowerCase().indexOf("mobile") != -1){
                holder.newsIndicator.setBackgroundColor(context.getResources().getColor(R.color.PLATFORM_MOBILE));
            }
        }

        return convertView;

    }

}
