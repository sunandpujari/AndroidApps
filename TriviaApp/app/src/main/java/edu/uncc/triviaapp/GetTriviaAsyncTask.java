package edu.uncc.triviaapp;

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

public class GetTriviaAsyncTask extends AsyncTask<String, Void, ArrayList<Trivia>> {

    IData activity;

    public GetTriviaAsyncTask(IData activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Trivia> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statusCode = con.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                return JSONTrivia(sb.toString());
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
    protected void onPostExecute(ArrayList<Trivia> result) {
        activity.setupData(result);
        super.onPostExecute(result);

    }

    public ArrayList<Trivia> JSONTrivia(String in) throws JSONException {

        ArrayList<Trivia> triviaList = new ArrayList<Trivia>();

        JSONObject parentObject,finalObject,choiceObject=null;
        JSONArray parentArray=null,choiceArray=null;
        ArrayList<String> choices;

        parentObject = new JSONObject(in);

        if(parentObject.has("questions"))
        parentArray = parentObject.getJSONArray("questions");


        for (int i = 0; i < parentArray.length(); i++) {

            finalObject = parentArray.getJSONObject(i);

            Trivia trivia = new Trivia();

            if(finalObject.has("id"))
            trivia.setId(finalObject.getInt("id"));

            if(finalObject.has("text"))
            trivia.setQuestion(finalObject.getString("text"));

            if(finalObject.has("image"))
            trivia.setImagePath(finalObject.getString("image"));

            if(finalObject.has("choices"))
            choiceObject = finalObject.getJSONObject("choices");

            if(choiceObject.has("choice"))
            choiceArray = choiceObject.getJSONArray("choice");

            choices = new ArrayList<>();
            for (int j=0; j<choiceArray.length();j++){
                choices.add(choiceArray.getString(j));
            }
            trivia.setChoice(choices);

            if(choiceObject.has("answer"))
            trivia.setAnswer(choiceObject.getInt("answer"));

            triviaList.add(trivia);
        }
        return triviaList;
    }

    static public interface IData{
        public void setupData(ArrayList<Trivia> result);
    }
}
