package edu.uncc.itunestoppaidapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/*
a.Assignment #. Homework 06
b.File Name.  Group09_HW06.zip
c.Name. Sunand Kumar Pujari, Sai Yesaswy Mylavarapu
*/

public class MainActivity extends AppCompatActivity implements GetAppsAsyncTask.IData{


    ProgressDialog progressBar;
    ListView lvApps;
    ArrayList<App> appArrayList;
    Set<String> favorites;
    final static String APPS_LIST="APPS_LIST";
    public static final int REQ_CODE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar= new ProgressDialog(MainActivity.this);
        lvApps = (ListView)findViewById(R.id.lvTopPaidApps);
        progressBar.show();

        appArrayList=new ArrayList<>();
        new GetAppsAsyncTask(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.refresh_list:
                new GetAppsAsyncTask(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
                break;
            case R.id.favorite:
                Intent intent = new Intent(MainActivity.this,FavoritesActivity.class);
                intent.putExtra(MainActivity.APPS_LIST,appArrayList);
                startActivityForResult(intent,REQ_CODE);
                break;
            case R.id.sort_increasingly:
                Collections.sort(appArrayList, new MainActivity.CustomComparator());
                FillAppsList(appArrayList);
                break;
            case R.id.sort_decreasingly:
                Collections.sort(appArrayList, new MainActivity.CustomComparator());
                Collections.reverse(appArrayList);
                FillAppsList(appArrayList);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupData(ArrayList<App> result) {
        appArrayList=result;
        FillAppsList(appArrayList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CODE){
            if(resultCode==RESULT_OK){
                FillAppsList(appArrayList);
            }
        }
    }

    public void FillAppsList(final ArrayList<App> appsList){
        AppsAdapter adapter = new AppsAdapter(this,R.layout.row_item_layout,appsList);
        lvApps.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        progressBar.dismiss();
/*
        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,GameDetailsActivity.class);
                intent.putExtra(ID_VALUE,gamesList.get(position).getId()+"");
                startActivity(intent);

            }
        });*/

    }

    private class CustomComparator implements Comparator<App> {
        @Override
        public int compare(App o1, App o2) {
            return (o1.getPrice()==o2.getPrice())?0:(o1.getPrice()>o2.getPrice())?1:-1 ;
        }
    }
}
