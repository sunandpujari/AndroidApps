package edu.uncc.cnnnews;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a) Assignment #. InClass 05
        b) Full name of the student. Sunand Kumar Pujari
                * */

        public class MainActivity extends AppCompatActivity implements GetNewsAsyncTask.IData, GetImageAsyncTask.IImage {

            ArrayList<News> newsList;
            int counter =0,size;
            ProgressDialog progress;
            ProgressBar pbImageLoad;
            GetImageAsyncTask imageAsyncTask;
            Button btnFinish,btnGetNews;
            ImageButton btnNext,btnFirst,btnPrevious,btnLast;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_cnn_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        pbImageLoad=(ProgressBar)findViewById(R.id.pbImageLoad);
        pbImageLoad.setVisibility(View.GONE);

        btnGetNews = (Button)findViewById(R.id.btnGetNews);
        btnFinish = (Button)findViewById(R.id.btnFinish);
        btnGetNews.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnFirst = (ImageButton)findViewById(R.id.btnFirst);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        btnLast = (ImageButton)findViewById(R.id.btnLast);


        btnGetNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsList = new ArrayList<News>();

                progress = new ProgressDialog(MainActivity.this);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setCancelable(false);
                progress.show();

                counter=0;
                new GetNewsAsyncTask(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_tech.rss");
            }
        });

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newsList!=null){
                    if(!imageAsyncTask.isCancelled())
                        imageAsyncTask.cancel(true);

                    counter=0;
                    FillNewsDetails(newsList.get(counter));
                }

            }
        });
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newsList!=null) {
                    if(!imageAsyncTask.isCancelled())
                        imageAsyncTask.cancel(true);

                    counter = size - 1;
                    FillNewsDetails(newsList.get(counter));
                }

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newsList!=null){
                    if(!imageAsyncTask.isCancelled())
                        imageAsyncTask.cancel(true);

                    if(counter>0) {
                        counter -= 1;
                        FillNewsDetails(newsList.get(counter));
                    }
                }

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newsList!=null){
                    if(!imageAsyncTask.isCancelled())
                        imageAsyncTask.cancel(true);

                    if(counter<size-1) {
                        counter += 1;
                        FillNewsDetails(newsList.get(counter));
                    }
                }
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void FillNewsDetails(News news){

        if(news != null){
            imageAsyncTask = new GetImageAsyncTask(MainActivity.this);
            imageAsyncTask.execute(news.getUrlToImage());

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.svLinearNews);
            //spinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));

            linearLayout.removeAllViews();

            TextView textView = new TextView(MainActivity.this);
            textView.setText(news.getTitle());
            textView.setTextSize(15);
            textView.setTypeface(null, Typeface.BOLD);
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

            newsList = result;
            size = result.size();
            FillNewsDetails(newsList.get(counter));

            btnNext.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            btnFirst.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            btnPrevious.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            btnLast.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            btnFinish.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
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
            pbImageLoad.setVisibility(View.GONE);
        }
    }
}
