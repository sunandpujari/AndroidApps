package edu.uncc.wordcounter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/*
 Assignment #. Home Work 3
 File Name. HW3_Group09.zip
 Full name of the student. Sunand Kumar Pujari, Sai Yesaswy Mylavarapu
*/

public class MainActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener{

    ProgressDialog progress;


    private int counter=0,currentStep=0,totalWords;
    HashMap<Integer,String> searchWords;
    HashMap<Integer,String> elements;
    HashMap<String,Integer> wordFrequencies;
    String currentTag;
    LinearLayout.LayoutParams params;
    LinearLayout linearLayout;
    boolean matchCases;
    final static int Request_Code=1010;
    final static int Result_Ok=1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout)findViewById(R.id.scrollViewLinear);

        linearLayout.removeAllViews();

        elements = new HashMap<Integer,String>();
        currentTag = new String();

        ++counter;
        currentTag="exitText"+counter;
        EditText editText = new EditText(MainActivity.this);
        editText.setId(counter);
        editText.setTag(currentTag);
        params = new LinearLayout.LayoutParams(800 , 120);
        editText.setLayoutParams(params);

        elements.put(counter,currentTag);
        linearLayout.addView(editText);

        ++counter;
        currentTag="btnAdd"+counter;
        FloatingActionButton btnAdd = new FloatingActionButton(MainActivity.this);
        btnAdd.setOnClickListener(this);
        btnAdd.setId(counter);
        btnAdd.setTag(currentTag);
        btnAdd.setImageResource(R.drawable.ic_action_plus);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(120, 120);
        param.setMargins(810, Integer.parseInt(String.valueOf(-90)), 0, 0);
        btnAdd.setSize(FloatingActionButton.SIZE_MINI);
        btnAdd.setLayoutParams(param);

        elements.put(counter,currentTag);
        linearLayout.addView(btnAdd);

        searchWords = new HashMap<Integer, String>();

        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextCurrent = (EditText) findViewById(counter-1);

                if(editTextCurrent.getText().toString().isEmpty() || editTextCurrent.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Input can't be Empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(searchWords.containsValue(editTextCurrent.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Duplicate entries Exits!",Toast.LENGTH_SHORT).show();
                    return;
                }
                editTextCurrent.setKeyListener(null);
                searchWords.put(editTextCurrent.getId(),editTextCurrent.getText().toString());

                totalWords = searchWords.size();

                CheckBox checkBox = (CheckBox)findViewById(R.id.cbMatchCases);

                if(checkBox.isChecked())
                    matchCases=true;
                else
                    matchCases=false;

                progress = new ProgressDialog(MainActivity.this);
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();

                wordFrequencies = new HashMap<String, Integer>();

                Iterator it = searchWords.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    new AsyncWordCounter().execute(pair.getValue().toString());
                    it.remove();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {


        if((v.getTag().toString()).equals("btnAdd"+v.getId())) {

            if(elements.size()<40){

                EditText editTextCurrent = (EditText) findViewById(v.getId()-1);

                if(editTextCurrent.getText().toString().isEmpty() || editTextCurrent.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Input can't be Empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(searchWords.containsValue(editTextCurrent.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Duplicate entries Exits!",Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextCurrent.setKeyListener(null);
                searchWords.put(editTextCurrent.getId(),editTextCurrent.getText().toString());

                linearLayout.removeView(v);
                currentTag="btnRemove"+counter;
                FloatingActionButton btnRemove = new FloatingActionButton(MainActivity.this);
                btnRemove.setOnClickListener(this);
                btnRemove.setId(counter);
                btnRemove.setTag(currentTag);
                btnRemove.setImageResource(R.drawable.ic_action_minus);
                params = new LinearLayout.LayoutParams(120, 120);
                params.setMargins(810, Integer.parseInt(String.valueOf(-90)), 0, 0);
                btnRemove.setSize(FloatingActionButton.SIZE_MINI);
                btnRemove.setLayoutParams(params);

                elements.put(counter,currentTag);
                linearLayout.addView(btnRemove);

                ++counter;
                currentTag="exitText"+counter;
                EditText editText = new EditText(MainActivity.this);
                editText.setId(counter);
                editText.requestFocus();
                editText.setTag(currentTag);
                params = new LinearLayout.LayoutParams(800 , 120);
                editText.setLayoutParams(params);

                elements.put(counter,currentTag);
                linearLayout.addView(editText);

                ++counter;
                currentTag="btnAdd"+counter;
                FloatingActionButton btnAdd = new FloatingActionButton(MainActivity.this);
                btnAdd.setOnClickListener(this);
                btnAdd.setId(counter);
                btnAdd.setTag(currentTag);
                btnAdd.setImageResource(R.drawable.ic_action_plus);
                params = new LinearLayout.LayoutParams(120, 120);
                params.setMargins(810, Integer.parseInt(String.valueOf(-90)), 0, 0);
                btnAdd.setSize(FloatingActionButton.SIZE_MINI);
                btnAdd.setLayoutParams(params);

                elements.put(counter,currentTag);
                linearLayout.addView(btnAdd);
            }else {
                Toast.makeText(getApplicationContext(),"Exceeds max word limit",Toast.LENGTH_SHORT).show();
            }
        }
        else if((v.getTag().toString()).equals("btnRemove"+v.getId())) {
            linearLayout.removeView(v);
            linearLayout.removeView(findViewById(v.getId() - 1));
            elements.remove(v.getId());
            elements.remove(v.getId() - 1);

            searchWords.remove(v.getId() - 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Code) {
            // Make sure the request was successful
            if (resultCode == Result_Ok) {
                searchWords = new HashMap<Integer, String>();
                wordFrequencies = new HashMap<String, Integer>();

                CheckBox matchCase= (CheckBox) findViewById(R.id.cbMatchCases);
                matchCase.setChecked(false);

                linearLayout.removeAllViews();

                counter=0;
                totalWords=0;
                currentStep=0;

                elements = new HashMap<Integer,String>();
                currentTag = new String();

                ++counter;
                currentTag="exitText"+counter;
                EditText editText = new EditText(MainActivity.this);
                editText.setId(counter);
                editText.setTag(currentTag);
                params = new LinearLayout.LayoutParams(800 , 120);
                editText.setLayoutParams(params);

                elements.put(counter,currentTag);
                linearLayout.addView(editText);

                ++counter;
                currentTag="btnAdd"+counter;
                FloatingActionButton btnAdd = new FloatingActionButton(MainActivity.this);
                btnAdd.setOnClickListener(this);
                btnAdd.setId(counter);
                btnAdd.setTag(currentTag);
                btnAdd.setImageResource(R.drawable.ic_action_plus);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(120, 120);
                param.setMargins(810, Integer.parseInt(String.valueOf(-90)), 0, 0);
                btnAdd.setSize(FloatingActionButton.SIZE_MINI);
                btnAdd.setLayoutParams(param);

                elements.put(counter,currentTag);
                linearLayout.addView(btnAdd);
            }
        }
    }

    private class AsyncWordCounter extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(totalWords==currentStep){

                progress.dismiss();

                Intent intent = new Intent(MainActivity.this,ResultsActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("wordFrequencies",wordFrequencies);
                intent.putExtras(extras);
                startActivityForResult(intent,Request_Code);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String line;
            String keyword=null;
            int counter=0;

            try {
                keyword = params[0];

                Log.d("demo",keyword);

                InputStream fis = getAssets().open("textfile.txt");
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);

                while ((line = br.readLine()) != null) {
                    line = line.replaceAll("\\W", " ");
                    String[] words = line.split(" ");

                    for (String word:words) {
                        if(matchCases){
                            if(word.equals(keyword))
                                counter++;
                        }
                        else {
                            if(word.equalsIgnoreCase(keyword))
                                counter++;
                        }
                    }
                }
                wordFrequencies.put(keyword,counter);
                publishProgress(1);
            }catch(IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ++currentStep;

        }
    }
}
