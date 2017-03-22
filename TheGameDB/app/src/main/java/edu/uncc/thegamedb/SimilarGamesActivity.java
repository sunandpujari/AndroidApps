package edu.uncc.thegamedb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SimilarGamesActivity extends AppCompatActivity implements GetGameDetailsAsync.Idata {

    private String commaSeperatedList;
    TextView textView;
    ProgressDialog progressDialog;
    int counter,size;
    ListView listView;
    Button btnFinish;
    ArrayList<Game> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);

        this.setTitle("Similar Games");

        textView = (TextView)findViewById(R.id.tvSimilarGamesTitle);
        btnFinish=(Button)findViewById(R.id.btnFinish);
        progressDialog = new ProgressDialog(SimilarGamesActivity.this);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().containsKey(MainActivity.ID_VALUE)){
                commaSeperatedList = getIntent().getExtras().getString(MainActivity.ID_VALUE);
            }
        }

        counter=0;

        String[] gameids = commaSeperatedList.split(",");
        size = gameids.length;

        listView = (ListView) findViewById(R.id.lvSimilarGames);
        gamesList= new ArrayList<Game>();

        for (String id :gameids) {
            new GetGameDetailsAsync(SimilarGamesActivity.this).execute("http://thegamesdb.net/api/GetGame.php?id="+id);
        }
        btnFinish.setBackgroundColor(ContextCompat.getColor(SimilarGamesActivity.this, R.color.colorPrimary));
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
    }

    @Override
    public void setupData(Game result) {
        if(result!=null){
            gamesList.add(result);

            if(counter==0){
                textView.setText("Similar games to "+result.getGameTitle());
            }
            counter++;

            if(counter==size)
                FillGamesList(gamesList);
        }
    }

    public void FillGamesList(final ArrayList<Game> gamesList){
        Log.d("demo",gamesList.size()+"");
        GamesAdapter adapter = new GamesAdapter(SimilarGamesActivity.this,R.layout.row_item_layout,gamesList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        progressDialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SimilarGamesActivity.this,SimilarGameDetailsActivity.class);
                intent.putExtra(MainActivity.ID_VALUE,gamesList.get(position).getId()+"");
                startActivity(intent);

            }
        });

    }
    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }
}
