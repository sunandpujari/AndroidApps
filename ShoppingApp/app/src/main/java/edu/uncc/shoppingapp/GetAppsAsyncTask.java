package edu.uncc.shoppingapp;

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

public class GetAppsAsyncTask extends AsyncTask<String, Void, ArrayList<Item>> {

    IData activity;

    public GetAppsAsyncTask(IData activity) {
        this.activity = activity;
    }


    @Override
    protected ArrayList<Item> doInBackground(String... params) {

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
    protected void onPostExecute(ArrayList<Item> result) {
        activity.setupData(result);
        super.onPostExecute(result);
    }

    public ArrayList<Item> JSONApps(String in) throws JSONException {

        ArrayList<Item> appsList = new ArrayList<Item>();

        JSONArray parentArray = new JSONArray(in);

            for (int i = 0; i < parentArray.length(); i++) {

                JSONObject finalObject = parentArray.getJSONObject(i);

                Item apps = new Item();

                apps.setName(finalObject.getString("name"));

                JSONObject imageObject = finalObject.getJSONObject("image_urls");
                JSONArray imageArray = imageObject.getJSONArray("300x400");
                for (int j = 0; j < imageArray.length(); j++) {
                        apps.setImageUrl(imageArray.getJSONObject(j).getString("url"));
                        break;
                }
                JSONObject skusObject = finalObject.getJSONArray("skus").getJSONObject(0);
                apps.setMsrp_price(Float.parseFloat(skusObject.getString("msrp_price")));
                apps.setSale_price(Float.parseFloat(skusObject.getString("sale_price")));
                appsList.add(apps);
            }
            return appsList;
    }

    static public interface IData{
        public void setupData(ArrayList<Item> result);
    }
}
