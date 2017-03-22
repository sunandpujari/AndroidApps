package edu.uncc.thegamedb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
a. Assignment #. Homework 05
b. File Name. InClass06_Pujari.zip
c. Full name of all students in your group. Sunand Kumar Pujari
*/

public class MainActivity extends AppCompatActivity implements GetGamesAsyncTask.IData,GetGameDetailsAsync.Idata {

    Button btnSearch;
    EditText etSearchTerm;
    ArrayList<Game> gamesList;
    ArrayList<Game> gameDetailsList;
    ListView lvGames;
    ProgressDialog progressBar;
    int size,counter;
    RadioGroup rgGames;
    String selectedId;
    final static String ID_VALUE = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button)findViewById(R.id.btnSearch);
        etSearchTerm = (EditText)findViewById(R.id.etSearchTerm);
        lvGames = (ListView)findViewById(R.id.lvGames);
        btnSearch.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        progressBar= new ProgressDialog(MainActivity.this);

        gamesList=  new ArrayList<Game>();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(IsNullorEmply(etSearchTerm.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter a Search Term",Toast.LENGTH_SHORT).show();
                }
                else {
                    String url = getResources().getString(R.string.base_url)+getResources().getString(R.string.getgames_url)+etSearchTerm.getText().toString();

                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setCancelable(false);
                    progressBar.show();

                    new GetGamesAsyncTask(MainActivity.this).execute(url);

                }
            }
        });
    }

    @Override
    public void setupData(ArrayList<Game> result) {

        if(result!=null) {
            gamesList = result;
            ArrayList<Game> newGameList =new ArrayList<Game>(result.subList(0,10));
            size = newGameList.size();

            for (Game game:newGameList) {
                new GetGameDetailsAsync(MainActivity.this).execute("http://thegamesdb.net/api/GetGame.php?id="+game.getId());
            }

        }
        else {
            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
        }

    }

    public void FillGamesList(final ArrayList<Game> gamesList){
        GamesAdapter adapter = new GamesAdapter(this,R.layout.row_item_layout,gamesList);
        lvGames.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        progressBar.dismiss();

        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,GameDetailsActivity.class);
                intent.putExtra(ID_VALUE,gamesList.get(position).getId()+"");
                startActivity(intent);

            }
        });

    }

    private boolean IsNullorEmply(String str){
        return (str == null || str.isEmpty());
    }

    @Override
    public void setupData(Game result) {

        if(result!=null){
            if(counter==0){
                gameDetailsList = new ArrayList<Game>();
            }

            gameDetailsList.add(result);

            counter++;

            if(counter==size)
                FillGamesList(gameDetailsList);
        }

    }
}
