package com.gamerspot;

import android.util.Log;

import com.gamerspot.beans.NewsFeed;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Adrian on 09-Jun-14.
 */
public class ParserHandler extends DefaultHandler {

    private ArrayList<NewsFeed> list = null;
    private NewsFeed feed = null;
    StringBuilder builder;

    @Override
    public void startDocument() throws SAXException {
        list = new ArrayList<NewsFeed>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        builder=new StringBuilder();

        if(localName.equalsIgnoreCase("item")) {
            feed = new NewsFeed();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(localName.equalsIgnoreCase("title")) {
            Log.i("title", builder.toString());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String tempString=new String(ch, start, length);
        builder.append(tempString);
    }

    public ArrayList<NewsFeed> getList(){
        return list;
    }
}
