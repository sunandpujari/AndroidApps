package edu.uncc.itunestoppaidapps;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hello on 2/6/2017.
 */

public class GetAppsAsyncTask extends AsyncTask<String, Void, ArrayList<App>> {

    IData activity;

    public GetAppsAsyncTask(IData activity) {
        this.activity = activity;
    }


    @Override
    protected ArrayList<App> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            Log.d("demo",url.toString());

            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                Log.d("demo",sb.toString());
                return JSONApps(sb.toString());
            }
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(ProtocolException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<App> result) {
        activity.setupData(result);
        super.onPostExecute(result);
    }

    public ArrayList<App> JSONApps(String in) throws JSONException {

        ArrayList<App> appsList = new ArrayList<App>();

        JSONObject parentObject = new JSONObject(in);

        JSONArray parentArray = parentObject.getJSONObject("feed").getJSONArray("entry");

            for (int i = 0; i < parentArray.length(); i++) {

                JSONObject finalObject = parentArray.getJSONObject(i);

                App apps = new App();

                apps.setName(finalObject.getJSONObject("im:name").getString("label"));

                JSONArray imageArray = finalObject.getJSONArray("im:image");
                for (int j = 0; j < imageArray.length(); j++) {
                    if (imageArray.getJSONObject(j).getJSONObject("attributes").getString("height").equals("100")){
                        apps.setThumbnailURL(imageArray.getJSONObject(j).getString("label"));
                        break;
                    }
                }
                apps.setPrice(Float.parseFloat(finalObject.getJSONObject("im:price").getJSONObject("attributes").getString("amount")));
                apps.setCurrencyType(finalObject.getJSONObject("im:price").getJSONObject("attributes").getString("currency"));

                appsList.add(apps);
            }
            return appsList;
    }

    static public interface IData{
        public void setupData(ArrayList<App> result);
    }
}
