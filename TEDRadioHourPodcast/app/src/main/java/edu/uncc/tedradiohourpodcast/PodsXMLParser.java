package edu.uncc.tedradiohourpodcast;

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

public class PodsXMLParser extends DefaultHandler {
    ArrayList<Pod> podsArrayList;
    Pod podObject;
    StringBuilder xmlInnerText;

    public static ArrayList<Pod> parseNews(InputStream in) throws IOException, SAXException {
        PodsXMLParser parser = new PodsXMLParser();
        Xml.parse(in,Xml.Encoding.UTF_8,parser);

        return parser.getNewsArrayList();
    }

    public ArrayList<Pod> getNewsArrayList(){
        return podsArrayList;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        podsArrayList = new ArrayList<Pod>();
        xmlInnerText = new StringBuilder();
        podObject = new Pod();
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
            podObject = new Pod();
        }
        else if(localName.equals("image")){
                podObject.setImageURL(attributes.getValue("href"));
        }
        else if(localName.equals("enclosure")){
            podObject.setMediaLink(attributes.getValue("url"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equals("item")){
            podsArrayList.add(podObject);
        }
        else if(localName.equals("title")){
            podObject.setTitle(xmlInnerText.toString().trim());
        }
        else if(localName.equals("description")){
            podObject.setDescription(xmlInnerText.toString().trim());
        }
        else if(localName.equals("pubDate")){

            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            Date date;
            try {
                date=format.parse(xmlInnerText.toString().trim());
            } catch (ParseException e) {
                date=null;
            }

            podObject.setPublicationDate(date);
        }
        else if(localName.equals("duration")){
            podObject.setDuration(xmlInnerText.toString().trim());
        }

        xmlInnerText.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        xmlInnerText.append(ch,start,length);
    }
}
