package com.gamerspot.com.gamerspot.beans;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * Created by Adrian on 05-Jun-14.
 */
public class NewsFeed implements Serializable, Comparable {

    /*
    1: pc
    2: xbox
    3: ps
     */

    public static final int PLATFORM_PC = 1;
    public static final int PLATFORM_XBOX = 2;
    public static final int PLATFORM_PS = 3;

    private String guid;
    private String title;
    private String description;
    private String link;
    private String date;
    private String author;
    private String provider;
    private int platform;

    public NewsFeed(){}

    public NewsFeed(String guid, String provider, String title, String description, String link, String date, String author, int platform) {
        this.provider = provider;
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.author = author;
        this.platform = platform;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
        return getTitle()+" - " + getProvider()+" "+ getPlatform();
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
