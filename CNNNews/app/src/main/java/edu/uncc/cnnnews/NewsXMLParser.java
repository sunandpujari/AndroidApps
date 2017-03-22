package edu.uncc.cnnnews;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NANDU on 13-02-2017.
 */

public class NewsXMLParser extends DefaultHandler {
    ArrayList<News> newsArrayList;
    News newsObject;
    StringBuilder xmlInnerText;

    public static ArrayList<News> parseNews(InputStream in) throws IOException, SAXException {
        NewsXMLParser parser = new NewsXMLParser();
        Xml.parse(in,Xml.Encoding.UTF_8,parser);

        return parser.getNewsArrayList();
    }

    public ArrayList<News> getNewsArrayList(){
        return newsArrayList;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        newsArrayList = new ArrayList<News>();
        xmlInnerText = new StringBuilder();
        newsObject = new News();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
       // Log.d("demo", "localname: "+localName+"  qname: "+qName);
        if(localName.equals("item")){
            newsObject = new News();
        }
        else if(localName.equals("content")){
            if(attributes.getValue("height").equals(attributes.getValue("width")) && attributes.getValue("medium").equals("image")){
                newsObject.setUrlToImage(attributes.getValue("url"));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equals("item")){
            newsArrayList.add(newsObject);
        }
        else if(localName.equals("title")){
            newsObject.setTitle(xmlInnerText.toString());
        }
        else if(localName.equals("description")){
            newsObject.setDescription(xmlInnerText.toString());
        }
        else if(localName.equals("pubDate")){

            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            Date date;
            String resultDate;

            try {
                date=format.parse(xmlInnerText.toString());

                format = new SimpleDateFormat("yyyy-MM-dd");
                resultDate=format.format(date);

            } catch (ParseException e) {
                resultDate=xmlInnerText.toString();
            }

            newsObject.setPublishedAt(resultDate);
        }

        xmlInnerText.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        xmlInnerText.append(ch,start,length);
    }
}
