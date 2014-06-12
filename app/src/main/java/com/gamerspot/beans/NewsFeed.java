package com.gamerspot.beans;

import android.util.Log;

import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Adrian on 05-Jun-14.
 */
public class NewsFeed implements Serializable, Comparable {

    /*
    1: pc
    2: xbox
    3: playstation
    4: nintendo
    5: mobile
     */

    public static final int PLATFORM_PC = 1;
    public static final int PLATFORM_XBOX = 2;
    public static final int PLATFORM_PLAYSTATION = 3;
    public static final int PLATFORM_NINTENDO = 4;
    public static final int PLATFORM_MOBILE = 5;

    private String guid;
    private String title;
    private String description;
    private String link;
    private Date date;
    private String creator;
    private String provider;
    private int platform;

    public NewsFeed(){}

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String dateIn) {
        try {
            date = new Date(dateIn);
        }
        catch(IllegalArgumentException iae){

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                date = inputFormat.parse(dateIn);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDate(long milisecondsIn) {

        date = new Date(milisecondsIn);
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String author) {
        this.creator = author;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getPlatform(){
        return platform;
    }

    public void setPlatform(int platformIn) {
        this.platform = platformIn;
    }

    @Override
    public String toString() {
        return "ID "+getGuid()+" TITLE "+getTitle()+" DESCRIPTION " + getDescription()+" LINK "+ getLink() +" CREATOR "+getCreator() +" PROVIDER "+getProvider() + " PLATFORM "+getPlatform();
    }

    @Override
    public int compareTo(Object another) {

        Date otherDate = ((NewsFeed) another).getDate();

        return this.getDate().compareTo(otherDate);
    }
}
