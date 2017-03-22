package edu.uncc.shoppingapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunand on 2/26/2017.
 */

public class OrdersDAO {
    private SQLiteDatabase db;

    private static final String[] allColumns = {
            OrdersTable.COLUMN_ID,
            OrdersTable.COLUMN_APPNAME,
            OrdersTable.COLUMN_PRICE,
            OrdersTable.COLUMN_THUMBURL};

    public OrdersDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(Item app){

        ContentValues values = new ContentValues();
        values.put(OrdersTable.COLUMN_ID,app.getId());
        values.put(OrdersTable.COLUMN_APPNAME,app.getName());
        values.put(OrdersTable.COLUMN_PRICE,app.getSale_price());
        values.put(OrdersTable.COLUMN_THUMBURL,app.getImageUrl());
        return db.insert(OrdersTable.TABLENAME,null,values);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ArrayList<Item> getAllByOrderId(String id) throws ParseException {
        ArrayList<Item> itemsList = new ArrayList<Item>();

        Cursor c = db.query(true,OrdersTable.TABLENAME,allColumns, OrdersTable.COLUMN_ID + "=?",new String[]{id+""},null,null,null,null,null);

        if(c!=null && c.moveToFirst()){
            do{
                Item item = buildNoteFromCursor(c);
                if(item!=null){
                    itemsList.add(item);
                }
            }while (c.moveToNext());
            if(!c.isClosed()){
                c.close();
            }
        }
        return itemsList;
    }

    public ArrayList<String> getAllOrderIds() throws ParseException {
        ArrayList<String> ordersList = new ArrayList<String>();

        Cursor c = db.query(OrdersTable.TABLENAME,new String[] {OrdersTable.COLUMN_ID}, null,null,null,null,null);

        if(c!=null && c.moveToFirst()){
            do{
                if(!ordersList.contains(c.getString(0)))
                ordersList.add(c.getString(0));
            }while (c.moveToNext());
            if(!c.isClosed()){
                c.close();
            }
        }
        return ordersList;
    }

    private Item buildNoteFromCursor(Cursor c) {
        Item item = null;

        if(c!=null){
            item = new Item();
            item.setId(c.getString(0));
            item.setName(c.getString(1));
            item.setSale_price(c.getFloat(2));
            item.setImageUrl(c.getString(3));

        }
        return item;
    }
}
