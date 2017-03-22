package edu.uncc.passwordgenerator;

import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedPasswordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);

        ArrayList<String> threadPasswords,asyncPasswords;

        ScrollView threadScrollView = (ScrollView)findViewById(R.id.sbThreadPasswords);
        ScrollView asyncScrollView = (ScrollView)findViewById(R.id.sbAsyncPasswords);


        TextView textView;

        if(this.getIntent().getExtras() != null) {
            threadPasswords = this.getIntent().getExtras().getStringArrayList("threadPasswords");
            asyncPasswords = this.getIntent().getExtras().getStringArrayList("asyncPasswords");

            LinearLayout asyncLayout = (LinearLayout)findViewById(R.id.sbLinearAsyncPasswords);
            LinearLayout threadLayout = (LinearLayout)findViewById(R.id.sbLinearThreadPasswords);


            for(String password: threadPasswords){
                textView= new TextView(GeneratedPasswordsActivity.this);
                textView.setText(password);
                textView.setPadding(20,50,20,50);
                textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                threadLayout.addView(textView);
            }

            for(String password: asyncPasswords){
                textView= new TextView(GeneratedPasswordsActivity.this);
                textView.setText(password);
                textView.setPadding(20,50,20,50);
                textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                asyncLayout.addView(textView);
            }


            Button finish = (Button)findViewById(R.id.btnFinish);
            finish.setText("finish");
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

    }
}
