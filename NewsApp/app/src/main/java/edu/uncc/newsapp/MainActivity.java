package edu.uncc.newsapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
/*
a. Assignment #. In class assignment 04
b. File Name. Group09_InClass04.zip
c. Full name of all students in your group. Sunand Kumar Pujari, Sai Yesaswy Mylavarapur
*/

public class MainActivity extends AppCompatActivity implements GetNewsAsyncTask.IData,GetImageAsyncTask.IImage {

    Spinner spinner;
    ProgressDialog progress;
    final static String Key="fea2809595e84e9f9bc0bba12400406b";
    final static String Base_Url="https://newsapi.org/v1/articles";
    HashMap<String,String> News_Sources = new HashMap<String, String>(){
        {
            put("BBC","bbc-news");
            put("CNN","cnn");
            put("Buzzfeed","buzzfeed");
            put("ESPN","espn");
            put("Sky News","sky-news");
        }
    };

    ArrayList<News> news;
    int counter =0,size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spNewsSource);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.news_source, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        findViewById(R.id.btnGetNews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String source = spinner.getSelectedItem().toString();
                String url = Base_Url+"?apiKey="+Key+"&source="+News_Sources.get(source);

                progress = new ProgressDialog(MainActivity.this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setCancelable(false);
                progress.show();

                counter=0;

                new GetNewsAsyncTask(MainActivity.this).execute(url);

            }
        });


        findViewById(R.id.btnFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news!=null){
                    counter=0;
                    FillNewsDetails(news.get(counter));
                }

            }
        });
        findViewById(R.id.btnLast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news!=null) {
                    counter = size - 1;
                    FillNewsDetails(news.get(counter));
                }

            }
        });
        findViewById(R.id.btnPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news!=null){
                if(counter>0) {
                    counter -= 1;
                    FillNewsDetails(news.get(counter));
                }
                }

            }
        });
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news!=null){
                if(counter<size-1) {
                    counter += 1;
                    FillNewsDetails(news.get(counter));
                }
                }
            }
        });
        findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void FillNewsDetails(News news){

        Log.d("demo","fill news");

        if(news != null){
            new GetImageAsyncTask(MainActivity.this).execute(news.getUrlToImage());

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.svLinearNews);
            //spinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));

            linearLayout.removeAllViews();

            TextView textView = new TextView(MainActivity.this);
            textView.setText(news.getTitle());
            textView.setTextSize(15);
            textView.setTypeface(null, Typeface.BOLD);
            linearLayout.addView(textView);

            textView = new TextView(MainActivity.this);
            textView.setText("Author: "+news.getAuthor());
            linearLayout.addView(textView);

            textView = new TextView(MainActivity.this);
            textView.setText("Published on: "+news.getPublishedAt());
            linearLayout.addView(textView);

            textView = new TextView(MainActivity.this);
            textView.setText("");
            linearLayout.addView(textView);

            textView = new TextView(MainActivity.this);
            textView.setText("Description:");
            linearLayout.addView(textView);

            textView = new TextView(MainActivity.this);
            textView.setText(news.getDescription());
            linearLayout.addView(textView);

        }


    }

    @Override
    public void setupData(ArrayList<News> result) {

        if(result!=null) {

            news = result;
            size = result.size();
            FillNewsDetails(news.get(counter));
        }
        else {
            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
        }

        progress.dismiss();
    }

    @Override
    public void setupImage(Bitmap result) {

        if(result!=null){
            ImageView newsImage = (ImageView)findViewById(R.id.imNews);
            newsImage.setImageBitmap(result);
        }

    }
}
