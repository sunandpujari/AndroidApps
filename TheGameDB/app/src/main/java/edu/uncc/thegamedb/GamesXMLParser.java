package edu.uncc.thegamedb;

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

public class GamesXMLParser extends DefaultHandler {
    ArrayList<Game> gamesArrayList;
    Game gameObject;
    StringBuilder xmlInnerText;

    public static ArrayList<Game> parseGames(InputStream in) throws IOException, SAXException {
        GamesXMLParser parser = new GamesXMLParser();
        Xml.parse(in,Xml.Encoding.UTF_8,parser);

        return parser.getGamesArrayList();
    }

    public ArrayList<Game> getGamesArrayList(){
        return gamesArrayList;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        gamesArrayList = new ArrayList<Game>();
        xmlInnerText = new StringBuilder();
        gameObject = new Game();

    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if(localName.equals("Game")){
            gameObject = new Game();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equals("Game")){
            gamesArrayList.add(gameObject);
        }
        else if(localName.equals("id")){
           gameObject.setId(Integer.parseInt(xmlInnerText.toString().trim()));
        }
        else if(localName.equals("GameTitle")){
            gameObject.setGameTitle(xmlInnerText.toString());
        }
        else if(localName.equals("ReleaseDate")){
            if(!IsNullorEmply(xmlInnerText.toString()) && xmlInnerText.toString().length()>3) {
                gameObject.setReleaseDate(xmlInnerText.toString());
            }
            else
                gameObject.setReleaseDate("");
        }
        else if(localName.equals("Platform")){
            gameObject.setPlatform(xmlInnerText.toString());
        }

        xmlInnerText.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        xmlInnerText.append(ch,start,length);
    }

    private boolean IsNullorEmply(String str){
        return (str == null || str.isEmpty());
    }
}
