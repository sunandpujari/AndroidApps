package edu.uncc.triviaapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StatsActivity extends AppCompatActivity {

    ArrayList<Trivia> triviaList;
    int size,correctAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        if(getIntent().getExtras()!=null) {
            triviaList = (ArrayList<Trivia>) getIntent().getExtras().<Trivia>getParcelableArrayList(MainActivity.TRIVIA);

            size =triviaList.size();
            correctAnswers=0;

            for(int i=0;i<triviaList.size();i++){
                if(triviaList.get(i).getAnswer() == triviaList.get(i).getUserAnswer()) {
                    triviaList.get(i).setResult(true);
                    ++correctAnswers;
                }
            }

            Collections.sort(triviaList,new StatsActivity.CustomComparator());

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.svLinearResults);
            TextView textView;

            for (Trivia trivia: triviaList) {

                textView = new TextView(StatsActivity.this);
                textView.setText(trivia.getQuestion());
                linearLayout.addView(textView);

                textView = new TextView(StatsActivity.this);
                if(trivia.getUserAnswer()!=0)
                    textView.setText(trivia.getChoice().get(trivia.getUserAnswer()-1));
                else
                    textView.setText("Not Attempted");
                if(!trivia.result)
                    textView.setBackgroundColor(ContextCompat.getColor(StatsActivity.this, R.color.colorRed));
                else
                    textView.setBackgroundColor(ContextCompat.getColor(StatsActivity.this, R.color.colorGreen));

                linearLayout.addView(textView);

                textView = new TextView(StatsActivity.this);
                textView.setText(trivia.getChoice().get(trivia.getAnswer()-1));
                linearLayout.addView(textView);


                textView = new TextView(StatsActivity.this);
                textView.setText("");
                linearLayout.addView(textView);

            }

            Collections.sort(triviaList,new StatsActivity.CustomComparator());
            Collections.reverse(triviaList);

            ProgressBar progressBar = (ProgressBar)findViewById(R.id.pbPerformance);
            progressBar.setProgress(correctAnswers);
            progressBar.setMax(size);
            //progressBar.setBackgroundColor(ContextCompat.getColor(StatsActivity.this, R.color.colorPrimary));

            TextView tvPercentage = (TextView)findViewById(R.id.tvPercentage);
            int percentage =(int)((correctAnswers * 100.0f) / size);
            tvPercentage.setText(percentage  +"%");

            findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StatsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
                    startActivity(intent);
                }
            });


        }
    }

    private class CustomComparator implements Comparator<Trivia> {
        @Override
        public int compare(Trivia o1, Trivia o2) {
            return (o1.isResult()==o2.isResult())?0:(o1.isResult())?1:-1 ;
        }
    }
}
