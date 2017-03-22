package edu.uncc.shoppingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sai Yesaswy on 2/26/2017.
 */

public class DatabaseDataManager {
    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private OrdersDAO ordersDAO;

    public DatabaseDataManager(Context mContext) {
        this.mContext = mContext;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        ordersDAO = new OrdersDAO(db);
    }

    public void close(){
        if(db!=null){
            db.close();
        }
    }

    public OrdersDAO getNoteDAO(){
        return this.ordersDAO;
    }

    public void saveAll(ArrayList<Item> items){
        String id = UUID.randomUUID().toString();
        for (Item item:items) {
            item.setId(id);
            saveItem(item);
        }
    }

    public long saveItem(Item item){
        return this.ordersDAO.save(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ArrayList<Item> getAllByid(String id) throws ParseException {
        return this.ordersDAO.getAllByOrderId(id);
    }

    public ArrayList<String> getAllOrderIDs() throws ParseException {
        return this.ordersDAO.getAllOrderIds();

    }

}
