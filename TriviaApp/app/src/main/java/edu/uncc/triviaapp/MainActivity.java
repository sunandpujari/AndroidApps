package edu.uncc.triviaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTriviaAsyncTask.IData {


    ArrayList<Trivia> triviaList;
    ProgressBar pbSpinner;
    ImageView imageView;
    Button btnStart,btnExit;
    TextView tvLoading;

    public static final String TRIVIA = "trivia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        triviaList=new ArrayList<Trivia>();

        pbSpinner = (ProgressBar)findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imgTrivia);
        btnStart= (Button) findViewById(R.id.btnStart);
        btnExit = (Button)findViewById(R.id.btnExit);
        tvLoading = (TextView) findViewById(R.id.tvLoading);

        btnExit.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        imageView.setVisibility(View.INVISIBLE);
        tvLoading.setVisibility(View.VISIBLE);

        pbSpinner.setVisibility(ProgressBar.VISIBLE);


        new GetTriviaAsyncTask(MainActivity.this).execute("http://dev.theappsdr.com/apis/trivia_json/index.php");


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(triviaList.size()==0)
                    return;

                Intent intent = new Intent(MainActivity.this,TriviaActivity.class);
                intent.putParcelableArrayListExtra(TRIVIA,triviaList);
                startActivity(intent);
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setupData(ArrayList<Trivia> result) {

        triviaList=result;
        imageView.setVisibility(View.VISIBLE);
        pbSpinner.setVisibility(ProgressBar.GONE);
        btnStart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvLoading.setVisibility(View.INVISIBLE);
    }
}
