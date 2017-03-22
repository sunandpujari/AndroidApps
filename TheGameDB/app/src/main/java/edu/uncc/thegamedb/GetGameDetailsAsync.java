package edu.uncc.thegamedb;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Hello on 2/16/2017.
 */

public class GetGameDetailsAsync extends AsyncTask<String, Void, Game> {

    Idata activity;

    public GetGameDetailsAsync(Idata activity) {
        this.activity = activity;
    }

    @Override
    protected Game doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return GameDetailsXMLParser.parseGame(con.getInputStream());
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(ProtocolException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Game result) {
        activity.setupData(result);
        super.onPostExecute(result);
    }

    static public interface Idata{
        public void setupData(Game result);
    }


}
