package edu.uncc.tedradiohourpodcast;

import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hello on 2/6/2017.
 */

public class GetPodsAsyncTask extends AsyncTask<String, Void, ArrayList<Pod>> {

    IData activity;

    public GetPodsAsyncTask(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Pod> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {

                return PodsXMLParser.parseNews(con.getInputStream());
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
    protected void onPostExecute(ArrayList<Pod> result) {
        activity.setupData(result);
        super.onPostExecute(result);
    }

    static public interface IData{
        public void setupData(ArrayList<Pod> result);
    }
}
