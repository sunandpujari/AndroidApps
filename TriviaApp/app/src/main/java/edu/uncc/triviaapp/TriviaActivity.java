package edu.uncc.triviaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TriviaActivity extends AppCompatActivity implements GetImageAsyncTask.IImage {

    ArrayList<Trivia> triviaList;
    int size,counter;
    TextView tvQuestionNumer,tvLoading;
    Button btnPrevious,btnNext;
    TextView tvTimeLeft;
    CountDownTimer countDownTimer;
    long milliSecondsLeft,timerLength;
    ProgressBar pbImageLoad;
    ImageView imgQuestion;
    GetImageAsyncTask imageAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        btnPrevious = (Button)findViewById(R.id.btnPrevious);
        btnNext = (Button)findViewById(R.id.btnNext);

        tvTimeLeft = (TextView)findViewById(R.id.tvTimeLeft);
        imgQuestion = (ImageView)findViewById(R.id.imgQuestion);
        tvLoading = (TextView)findViewById(R.id.tvLoading);

        btnNext.setBackgroundColor(ContextCompat.getColor(TriviaActivity.this, R.color.colorPrimary));
        pbImageLoad=(ProgressBar)findViewById(R.id.pbImageLoad);
        pbImageLoad.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);

        if(getIntent().getExtras()!=null){
            triviaList = (ArrayList<Trivia>)getIntent().getExtras().<Trivia>getParcelableArrayList(MainActivity.TRIVIA);
            size = triviaList.size();

            Collections.sort(triviaList,new TriviaActivity.CustomComparator());

            tvQuestionNumer = (TextView)findViewById(R.id.tvQuestionNumbe);

            FillQuestion(triviaList.get(0));
        }

        timerLength=getResources().getInteger(R.integer.countdown_time);

        countDownTimer =
            new CountDownTimer(timerLength, 1000) {
                public void onTick(long millisUntilFinished) {
                    milliSecondsLeft=millisUntilFinished;
                    tvTimeLeft.setText("Time Left: " + millisUntilFinished / 1000 +" seconds");
                }
                public void onFinish() {

                    Intent intent = new Intent(TriviaActivity.this,StatsActivity.class);
                    intent.putParcelableArrayListExtra(MainActivity.TRIVIA,triviaList);
                    startActivity(intent);
                }
            };

        countDownTimer.start();

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!imageAsyncTask.isCancelled())
                    imageAsyncTask.cancel(true);

                if(counter>0){
                    counter-=1;
                    FillQuestion(triviaList.get(counter));
                }

                if(counter==0) {
                    btnPrevious.setBackgroundResource(android.R.drawable.btn_default);
                }
                else
                    btnPrevious.setBackgroundColor(ContextCompat.getColor(TriviaActivity.this, R.color.colorPrimary));

                btnNext.setText(R.string.next);

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!imageAsyncTask.isCancelled())
                    imageAsyncTask.cancel(true);

                if(counter<size-1){
                    counter+=1;
                    FillQuestion(triviaList.get(counter));
                }

                if(btnNext.getText().equals(getString(R.string.finish).toUpperCase())) {

                    countDownTimer.cancel();

                    Intent intent = new Intent(TriviaActivity.this,StatsActivity.class);
                    intent.putParcelableArrayListExtra(MainActivity.TRIVIA,triviaList);
                    startActivity(intent);
                }

                if(counter==size-1)
                    btnNext.setText(R.string.finish);
                else
                    btnNext.setText(R.string.next);

                btnPrevious.setBackgroundColor(ContextCompat.getColor(TriviaActivity.this, R.color.colorPrimary));
            }
        });
    }

    private void FillQuestion(Trivia trivia){

        tvQuestionNumer.setText("Q"+(trivia.getId()+1));

        imgQuestion.setImageDrawable(null);

        LinearLayout linerLayout = (LinearLayout)findViewById(R.id.svLinerQuestion);
        linerLayout.removeAllViews();

        if(!IsEmptyOrNull(trivia.getImagePath())){
            ImageView image = new ImageView(TriviaActivity.this);
            image.setId(trivia.getId());
            linerLayout.addView(image);
            imgQuestion.getLayoutParams().height = 300;
            pbImageLoad.setVisibility(View.VISIBLE);
            tvLoading.setVisibility(View.VISIBLE);

            imageAsyncTask = new GetImageAsyncTask(TriviaActivity.this);
            imageAsyncTask.execute(trivia.getImagePath());

        }else {
            imgQuestion.getLayoutParams().height = 1;
            pbImageLoad.setVisibility(View.GONE);
            tvLoading.setVisibility(View.INVISIBLE);
        }

        TextView question = new TextView(TriviaActivity.this);
        question.setText(trivia.getQuestion());

        linerLayout.addView(question);

        RadioGroup radioGroup = new RadioGroup(TriviaActivity.this);
        radioGroup.setId(counter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int idx = group.indexOfChild(group.findViewById(checkedId));

                if(idx>=0)
                triviaList.get(counter).setUserAnswer(idx+1);

            }
        });

        RadioButton radioButton;

        for (String choice:trivia.getChoice()){
            radioButton = new RadioButton(TriviaActivity.this);
            radioButton.setText(choice);
            //radioButton.setButtonDrawable(R.drawable.apptheme_btn_radio_holo_light);
            radioGroup.addView(radioButton);
        }

        if(trivia.getUserAnswer() !=0)
            radioGroup.check(radioGroup.getChildAt(trivia.getUserAnswer()-1).getId());

        linerLayout.addView(radioGroup);
    }

    @Override
    public void setupImage(Bitmap result) {
        imgQuestion.setImageBitmap(result);
        pbImageLoad.setVisibility(View.GONE);
        tvLoading.setVisibility(View.INVISIBLE);
    }

    private class CustomComparator implements Comparator<Trivia> {
        @Override
        public int compare(Trivia o1, Trivia o2) {
            return (o1.getId()==o2.getId())?0:(o1.getId()>o2.getId())?1:-1 ;
        }
    }

    private boolean IsEmptyOrNull(String str){
        return (str == null || str.isEmpty());
    }
}
