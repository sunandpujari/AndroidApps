package edu.uncc.shoppingapp;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sunand on 2/26/2017.
 */

public class OrdersTable {

    static final String TABLENAME = "orders";
    static final String COLUMN_ID = "id";
    static final String COLUMN_APPNAME = "name";
    static final String COLUMN_PRICE = "price";
    static final String COLUMN_THUMBURL = "image_url";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " +TABLENAME+" (");
        sb.append(COLUMN_ID+ " text not null, ");
        sb.append(COLUMN_APPNAME+ " text not null, ");
        sb.append(COLUMN_PRICE+ " real not null, ");
        sb.append(COLUMN_THUMBURL+ " text not null) ");

        db.execSQL(sb.toString());
    }

    static public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        OrdersTable.onCreate(db);
    }



}
