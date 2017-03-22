package edu.uncc.passwordgenerator;

/*
a. Assignment #.InClass 03
b. File Name.Group46_InClass03.zip
c. Full name of all students in your: Sunand Kumar Pujari, Vichitra Reddy Yellolu
*/

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private int asyncPasswordCount,asyncPasswordLength,threadPasswordCount,threadPasswordLength,currentProgress;
    private Handler handler;
    ProgressDialog progress;
    private ArrayList<String> threadPasswords,asyncPasswords;
    Bundle bundle;
    Message message;
    private final int Status_Step = 1010;
    private final int Status_Done = 1011;

    ExecutorService threadExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadExecutor= Executors.newFixedThreadPool(2);

        final SeekBar sbThreadPasswordCount=(SeekBar)findViewById(R.id.sbThreadPasswordCount);
        final TextView tvpc=(TextView)findViewById(R.id.tvThreadPasswordCountSelected);
        tvpc.setText(1+"");
        sbThreadPasswordCount.setProgress(1);
        sbThreadPasswordCount.setMax(10);
        sbThreadPasswordCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if(progress<1){
                    sbThreadPasswordCount.setProgress(1);
                    tvpc.setText(String.valueOf(1+""));
                }
                else
                    tvpc.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar sbThreadPasswordLength=(SeekBar)findViewById(R.id.sbThreadPasswordLength);
        final TextView tvcpl=(TextView)findViewById(R.id.tvThreadPawordLengthSelected);
        tvcpl.setText(7+"");
        sbThreadPasswordLength.setProgress(7);
        sbThreadPasswordLength.setMax(23);
        sbThreadPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress<7){
                    sbThreadPasswordLength.setProgress(7);
                    tvcpl.setText(7+"");
                }
                else
                    tvcpl.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar sbAsyncPasswordCount=(SeekBar)findViewById(R.id.sbAsyncPasswordCount);
        final TextView tvac=(TextView)findViewById(R.id.tvAsyncPasswordCountSelected);
        tvac.setText(String.valueOf(1+""));
        sbAsyncPasswordCount.setProgress(1);
        sbAsyncPasswordCount.setMax(10);
        sbAsyncPasswordCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<1)
                {
                    sbAsyncPasswordCount.setProgress(1);
                    tvac.setText(String.valueOf(1+""));

                }
                else
                    tvac.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar sbAsyncPasswordLength=(SeekBar)findViewById(R.id.sbAsyncPasswordLength);
        final TextView tval=(TextView)findViewById(R.id.tvAsyncPaswwordLengthSelected);

        tval.setText(String.valueOf(7+""));
        sbAsyncPasswordLength.setProgress(7);
        sbAsyncPasswordLength.setMax(23);
        sbAsyncPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<7){
                    sbAsyncPasswordLength.setProgress(7);
                    tval.setText(String.valueOf(7+""));
                }
                else
                    tval.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btnGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncPasswordCount = sbAsyncPasswordCount.getProgress();
                asyncPasswordLength = sbAsyncPasswordLength.getProgress();

                threadPasswordCount = sbThreadPasswordCount.getProgress();
                threadPasswordLength = sbThreadPasswordLength.getProgress();

                asyncPasswords = new ArrayList<String>();
                threadPasswords = new ArrayList<String>();

                progress = new ProgressDialog(MainActivity.this);
                progress.setMessage("Generating Passwords");
                progress.setMax(asyncPasswordCount+threadPasswordCount);
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.show();

                currentProgress=0;

                new AsyncPasswordGenerator().execute(asyncPasswordCount,asyncPasswordLength);
                threadExecutor.execute(new ThreadPasswordGenerator(threadPasswordCount,threadPasswordLength));

                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {

                        switch (msg.what){
                            case Status_Step:
                                progress.setProgress(++currentProgress);
                                break;
                            case Status_Done:
                                progress.dismiss();
                                Intent intent = new Intent(MainActivity.this,GeneratedPasswordsActivity.class);
                                intent.putStringArrayListExtra("threadPasswords",threadPasswords);
                                intent.putStringArrayListExtra("asyncPasswords",asyncPasswords);
                                startActivity(intent);
                                break;
                            default:
                                break;

                        }

                        return false;
                    }
                });


            }
        });
    }

    private class AsyncPasswordGenerator extends AsyncTask<Integer,Integer,Integer>{

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progress.setProgress(++currentProgress);
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            int passwordCount = params[0], passwordLength=params[1];

            for(int i=0;i<passwordCount;i++){

                asyncPasswords.add(Util.getPassword(passwordLength));
                publishProgress(i);

            }

            return null;
        }
    }

    private class ThreadPasswordGenerator implements Runnable{

        int passwordCount,passwordLength;
        public ThreadPasswordGenerator(int passwordCount, int passwordLength) {
            this.passwordCount=passwordCount;
            this.passwordLength=passwordLength;
        }

        @Override
        public void run() {

            for(int i=0; i<passwordCount;i++ )
            {
                bundle = new Bundle();
                message = new Message();

                threadPasswords.add(Util.getPassword(passwordLength));

                message.setData(bundle);
                message.what=Status_Step;
                handler.sendMessage(message);

            }

            while (true){
                if(currentProgress == threadPasswordCount+asyncPasswordCount){

                    message = new Message();
                    message.what=Status_Done;
                    handler.sendMessage(message);
                    break;
                }
            }

        }
    }
}
