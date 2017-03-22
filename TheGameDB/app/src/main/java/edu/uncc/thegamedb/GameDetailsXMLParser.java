package edu.uncc.thegamedb;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by NANDU on 13-02-2017.
 */

public class GameDetailsXMLParser extends DefaultHandler {
    Game gameObject;
    StringBuilder xmlInnerText;
    boolean gameStarted=false,idStarted=false;
    String baseUrl="";

    public static Game parseGame(InputStream in) throws IOException, SAXException {
        GameDetailsXMLParser parser = new GameDetailsXMLParser();
        Xml.parse(in,Xml.Encoding.UTF_8,parser);

        return parser.getGame();
    }

    public Game getGame(){
        return gameObject;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
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

        if(localName.equals("Game") && !gameStarted){
            gameObject = new Game();
            gameStarted = true;
            gameObject.setBaseImageUrl(baseUrl);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equals("GameTitle")){
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
        else if(localName.equals("Publisher")){
            gameObject.setPublisher(xmlInnerText.toString());
        }
        else if(localName.equals("genre")){
            gameObject.setGenre(xmlInnerText.toString());
        }
        else if(localName.equals("baseImgUrl")){
            baseUrl = xmlInnerText.toString();
        }
        else if(localName.equals("Overview")){
            gameObject.setOverview(xmlInnerText.toString());
        }
        else if(localName.equals("Youtube")){
            gameObject.setYoutubeLink(xmlInnerText.toString());
        }
        else if(localName.equals("id")){
            if(!idStarted) {
                gameObject.setId(Integer.parseInt(xmlInnerText.toString().trim()));
                idStarted=true;
                gameObject.setSimilarGameIds("");
            }
            else {
                gameObject.setSimilarGameIds(gameObject.getSimilarGameIds().isEmpty() ? xmlInnerText.toString() : gameObject.getSimilarGameIds() + "," + xmlInnerText.toString());
            }
        }
        else if(localName.equals("original")){
            gameObject.setImagePath(xmlInnerText.toString());
        }
        else if(localName.equals("boxart")){
            gameObject.setBoxart(xmlInnerText.toString());
        }
        else if(localName.equals("clearlogo")){
            gameObject.setClearlogo(xmlInnerText.toString());
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

