package edu.uncc.shoppingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetAppsAsyncTask.IData,RecyclerAdapter.OncallBackListener {

    ProgressDialog progressBar;
    RecyclerView recycler;
    GridLayoutManager gLayoutManager;
    ArrayList<Item> cart_items;
    ArrayList<Item> products_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar= new ProgressDialog(MainActivity.this);
        cart_items= new ArrayList<>();
        recycler = (RecyclerView)findViewById(R.id.recycler_view);
        progressBar.show();
        new GetAppsAsyncTask(MainActivity.this).execute("http://52.90.79.130:8080/MidTerm/get/products");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_Cart:
                if(cart_items.size()>0){
                    Intent intent = new Intent(MainActivity.this , CartActivity.class);
                    intent.putExtra("mode","cart");
                    intent.putExtra("cartitems",cart_items);
                    startActivityForResult(intent,1001);
                }
                else
                    Toast.makeText(getApplicationContext(),"Cart is Empty!",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_History:
                Intent intent = new Intent(MainActivity.this , CartActivity.class);
                intent.putExtra("mode","history");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupData(ArrayList<Item> result) {
        if(result != null) {
            products_list = result;
            FillRecycler(result);
            progressBar.dismiss();
        }
        else
            Toast.makeText(getApplicationContext(),"Something Went Wrong!",Toast.LENGTH_LONG).show();
    }

    public void FillRecycler(List<Item> productsList){

        gLayoutManager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(gLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(this,productsList);
        recycler.setAdapter(adapter);
    }

    @Override
    public void callback(Item product) {
        cart_items.add(product);
        FillRecycler(products_list);

    }

    @Override
    public ArrayList<Item> getCart() {
        return cart_items;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1001){
            if(resultCode==RESULT_OK){
                cart_items= new ArrayList<>();
                FillRecycler(products_list);

            }
        }
    }
}
