package edu.uncc.itunestoppaidapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    Button btnFinish;
    ListView listViewFavorites;
    ArrayList<App> appArrayList,favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        btnFinish =(Button)findViewById(R.id.btnFinish);
        btnFinish.setBackgroundColor(ContextCompat.getColor(FavoritesActivity.this, R.color.colorPrimary));

        listViewFavorites=(ListView)findViewById(R.id.listViewFavorites);

        appArrayList = new ArrayList<>();
        favorites = new ArrayList<>();

        if(getIntent().getExtras()!=null){
            appArrayList = (ArrayList<App>)getIntent().getExtras().getSerializable(MainActivity.APPS_LIST);

            final SharedPreferences myPrefs = getSharedPreferences("edu.uncc.itunestoppaidapps", MODE_PRIVATE);
            final Set<String> nullSet = new ArraySet<String>();
            final Set<String> set = myPrefs.getStringSet("app_favorites",nullSet);
            for (App app:appArrayList) {
                if(set.contains(app.getName()))
                    favorites.add(app);
            }

            FillAppsList(favorites);
        }

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    public void FillAppsList(final ArrayList<App> appsList){
        AppsAdapter adapter = new AppsAdapter(this,R.layout.row_item_layout,appsList);
        listViewFavorites.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

    }
}
