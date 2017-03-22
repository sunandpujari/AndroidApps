package edu.uncc.tedradiohourpodcast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetPodsAsyncTask.IData,PodsAdapter.ICallBack,View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    ProgressDialog progress;
    RecyclerView recyclerView;
    PodsAdapter podsAdapter;
    List<Pod> podsList;
    ImageButton btnPlayPause;
    SeekBar seekBarProgress;
    Pod podObject;
    String flag="1";

    MediaPlayer mediaPlayer;
    int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();

    public static final String POD_ITEM = "pod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_ted_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        podsList = new ArrayList<>();

        btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        seekBarProgress = (SeekBar)findViewById(R.id.seekBarProgress);

        btnPlayPause.setVisibility(View.GONE);
        seekBarProgress.setVisibility(View.GONE);

        recyclerView = (RecyclerView)findViewById(R.id.rec_list);

        progress = new ProgressDialog(MainActivity.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();

        new GetPodsAsyncTask(MainActivity.this).execute("https://www.npr.org/rss/podcast.php?id=510298");
    }

    @Override
    public void setupData(ArrayList<Pod> result) {

        if(result != null) {
            podsList = result;

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            podsAdapter = new PodsAdapter(podsList, "1", MainActivity.this);
            recyclerView.setAdapter(podsAdapter);
            progress.dismiss();
        }
        else
            Toast.makeText(getApplicationContext(),"Something Went Wrong!!",Toast.LENGTH_SHORT);

    }

    @Override
    public void removelay() {
        btnPlayPause.setVisibility(View.GONE);
        seekBarProgress.setVisibility(View.GONE);
        //recyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public void playOrPauseMedia(Pod pod) {
        podObject=pod;

        btnPlayPause.setVisibility(View.VISIBLE);
        btnPlayPause.setOnClickListener(this);
        seekBarProgress.setVisibility(View.VISIBLE);
        seekBarProgress.setMax(99);
        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        try {
            mediaPlayer.setDataSource(podObject.getMediaLink());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        btnPlayPause.setImageResource(R.drawable.pause_icon);

    }


    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btnPlayPause.setVisibility(View.GONE);
        seekBarProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnPlayPause){
            try {
                mediaPlayer.setDataSource(podObject.getMediaLink());
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.pause_icon);
            }else {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.play_icon);
            }

            primarySeekBarProgressUpdater();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v.getId() == R.id.seekBarProgress){
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if(mediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if(flag.equals("1")){

                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    podsAdapter = new PodsAdapter(podsList,"0",MainActivity.this);
                    recyclerView.setAdapter(podsAdapter);
                    flag="0";
                }
                else {

                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    podsAdapter = new PodsAdapter(podsList,"1",MainActivity.this);
                    recyclerView.setAdapter(podsAdapter);

                    flag="1";
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
