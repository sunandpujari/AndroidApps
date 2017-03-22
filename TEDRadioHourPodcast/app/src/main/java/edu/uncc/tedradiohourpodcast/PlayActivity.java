package edu.uncc.tedradiohourpodcast;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import com.squareup.picasso.Picasso;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class PlayActivity extends AppCompatActivity implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener{

    Pod podObject;
    TextView txtTitle,txtDescription,txtPublicationDate,txtDuration;
    ImageView imgPod;
    ImageButton btnPlay;
    SeekBar sbProgress;

    MediaPlayer mediaPlayer;
    int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        InitializeItems();

        if(getIntent().getExtras()!=null){
            podObject = (Pod)getIntent().getExtras().getSerializable(MainActivity.POD_ITEM);

            FillMovieDetails(podObject);
        }
    }

    private void InitializeItems(){
        txtTitle = (TextView)findViewById(R.id.txt_title);
        txtDescription=(TextView)findViewById(R.id.txt_description);
        txtDuration=(TextView)findViewById(R.id.txt_duration);
        txtPublicationDate=(TextView)findViewById(R.id.txt_date);

        imgPod =(ImageView)findViewById(R.id.img_pod);
        btnPlay=(ImageButton)findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        btnPlay.setImageResource(R.drawable.play_icon);

        sbProgress=(SeekBar)findViewById(R.id.sb_progress);
        sbProgress.setMax(99);
        sbProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void FillMovieDetails(Pod podObject){

        txtTitle.setText(podObject.getTitle());
        txtDescription.setText("Description: "+podObject.description);
        txtDuration.setText("Duration: "+podObject.getDuration());

        if(podObject.getPublicationDate()!=null) {

            SimpleDateFormat format = new SimpleDateFormat("MM/DD/yyyy");
            String date = format.format(podObject.getPublicationDate());

            txtPublicationDate.setText("Publication Date: "+date);
        }

        String imageURL =IsNullorEmpty(podObject.getImageURL())?"":podObject.getImageURL().trim();

        if(!IsNullorEmpty(imageURL)){
            Picasso.with(getApplicationContext()).load(imageURL)
                    // .placeholder(R.mipmap.user_placeholder)
                    //.error(R.mipmap.user_placeholder_error)
                    .into(imgPod);

        }

    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }

    private void primarySeekBarProgressUpdater() {
        sbProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
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
        sbProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        btnPlay.setImageResource(R.drawable.play_icon);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_play){
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
                mediaPlayer.setDataSource(podObject.getMediaLink());
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause_icon);
            }else {
                mediaPlayer.pause();
                btnPlay.setImageResource(R.drawable.play_icon);
            }

            primarySeekBarProgressUpdater();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v.getId() == R.id.sb_progress){
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if(mediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }
}
