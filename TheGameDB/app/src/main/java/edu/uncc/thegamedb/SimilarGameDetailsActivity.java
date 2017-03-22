package edu.uncc.thegamedb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SimilarGameDetailsActivity extends AppCompatActivity implements GetGameDetailsAsync.Idata {

    private String id;
    ProgressDialog progress;
    private String baseUrl = "http://thegamesdb.net/api/GetGame.php?id=";
    Button btnFinish;
    TextView tvTitle,tvGenre,tvPublished;
    LinearLayout svLinearOverview;
    ImageView imgGame;
    String gameTitle,similarGames;
    final static String GAME_DETAIL = "TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_game_details);

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().containsKey(MainActivity.ID_VALUE)){
                id = getIntent().getExtras().getString(MainActivity.ID_VALUE);
            }
        }

        btnFinish = (Button)findViewById(R.id.btnFinish);

        new GetGameDetailsAsync(SimilarGameDetailsActivity.this).execute(baseUrl+id);

        progress = new ProgressDialog(SimilarGameDetailsActivity.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void FillGameDetails(Game game){
        tvTitle= (TextView)findViewById(R.id.tvGameTitle);
        tvGenre = (TextView)findViewById(R.id.tvGenre);
        tvPublished=(TextView)findViewById(R.id.tvPublishedby);
        svLinearOverview = (LinearLayout)findViewById(R.id.svLinearOverview);
        imgGame =(ImageView)findViewById(R.id.imgGame);

        gameTitle=game.getGameTitle();
        tvTitle.setText(gameTitle);

        TextView textView = new TextView(SimilarGameDetailsActivity.this);
        textView.setText(game.getOverview());
        svLinearOverview.addView(textView);

        if(!IsNullorEmpty(game.getGenre()))
            tvGenre.setText("Genre: "+game.getGenre().trim());
        if(!IsNullorEmpty(game.getPublisher()))
            tvPublished.setText("Published: "+game.getPublisher().trim());

        String imageBaseUrl =IsNullorEmpty(game.getBaseImageUrl())?"":game.getBaseImageUrl().trim();
        String imageURL="";

        if(!IsNullorEmpty(imageBaseUrl)){
            if(!IsNullorEmpty(game.getImagePath()))
                imageURL=imageBaseUrl+game.getImagePath().trim();
            else if(!IsNullorEmpty(game.getBoxart()))
                imageURL=imageBaseUrl+game.getBoxart().trim();
        }

        if(!IsNullorEmpty(imageURL)){
            Picasso.with(SimilarGameDetailsActivity.this).load(imageURL)
                    .placeholder(R.mipmap.user_placeholder)
                    .error(R.mipmap.user_placeholder_error)
                    .into(imgGame);

        }

        similarGames = game.getSimilarGameIds();

        btnFinish.setBackgroundColor(ContextCompat.getColor(SimilarGameDetailsActivity.this, R.color.colorPrimary));
        progress.dismiss();
    }

    @Override
    public void setupData(Game result) {
        if(result!=null){
            FillGameDetails(result);
        }
        else
            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();

    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }
}
