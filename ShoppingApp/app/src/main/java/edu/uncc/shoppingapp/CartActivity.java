package edu.uncc.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class CartActivity extends AppCompatActivity {

    ArrayList<Item> cart_items;
    ListView lvCart;
    Button btnCancel,btnAddCheckout;
    DatabaseDataManager dbManager;
    String Mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        lvCart = (ListView)findViewById(R.id.lvCart);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnAddCheckout=(Button)findViewById(R.id.btn_checkout);
        dbManager = new DatabaseDataManager(this);

        if(getIntent().getExtras()!=null){
            Mode = getIntent().getExtras().getString("mode");
            if(Mode.equals("cart")) {
                cart_items = (ArrayList<Item>) getIntent().getExtras().getSerializable("cartitems");

                FillCart(cart_items);

                btnAddCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(cart_items.size()>0){
                            dbManager.saveAll(cart_items);
                            Toast.makeText(getApplicationContext(),"Checkout Done",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Cart is Empty!",Toast.LENGTH_LONG).show();

                    }
                });
            }
            else {
                btnAddCheckout.setVisibility(View.GONE);
                ArrayList<String> orderids = new ArrayList<>();
                try {
                    orderids = dbManager.getAllOrderIDs();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(orderids.size()>0){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CartActivity.this);
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(CartActivity.this);
                    builderSingle.setTitle("Select One Order:-");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CartActivity.this, android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.addAll(orderids);

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);


                            try {
                                cart_items = dbManager.getAllByid(strName);
                                if(cart_items.size()>0){
                                FillCart(cart_items);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    builderSingle.show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No History",Toast.LENGTH_LONG).show();
                }
            }

        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void FillCart(final ArrayList<Item> cart_items){

        ItemsAdapter adapter = new ItemsAdapter(this,R.layout.item_list_layout,cart_items);
        lvCart.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Are you really want delete this item??")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cart_items.remove(position);
                                FillCart(cart_items);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                    }
                });
                dialog.show();
                return false;
            }
        });

        float totalprice=0;
        for (Item item:cart_items
             ) {
            totalprice=totalprice+item.getSale_price();

        }

        TextView total = (TextView)findViewById(R.id.txt_total_value);
        total.setText("         "+totalprice+"$");


    }
}
