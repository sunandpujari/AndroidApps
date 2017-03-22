package edu.uncc.cnnnews;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Hello on 2/6/2017.
 */

public class GetNewsAsyncTask extends AsyncTask<String, Void, ArrayList<News>> {

    IData activity;

    public GetNewsAsyncTask(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {

                return NewsXMLParser.parseNews(con.getInputStream());
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
    protected void onPostExecute(ArrayList<News> result) {
        activity.setupData(result);
        super.onPostExecute(result);
    }

    static public interface IData{
        public void setupData(ArrayList<News> result);
    }
}
