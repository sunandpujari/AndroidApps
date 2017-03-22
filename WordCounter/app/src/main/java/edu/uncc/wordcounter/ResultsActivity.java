package edu.uncc.wordcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        HashMap<String,String> wordFrequencies;

        TextView textView;

        if(this.getIntent().getExtras() != null) {
            wordFrequencies = (HashMap<String, String>)this.getIntent().getExtras().getSerializable("wordFrequencies");

            LinearLayout sbLinearWords = (LinearLayout)findViewById(R.id.sbLinearWords);

            Iterator it = wordFrequencies.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                textView= new TextView(ResultsActivity.this);
                textView.setText(pair.getKey() + " : " + pair.getValue());
                textView.setPadding(5,20,20,20);
                textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                textView.setTextSize(20);
                sbLinearWords.addView(textView);

                it.remove(); // avoids a ConcurrentModificationException
            }

        }



        findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ResultsActivity.this,MainActivity.class);
                setResult(MainActivity.Result_Ok, intent);
                finish();
            }
        });


    }
}
