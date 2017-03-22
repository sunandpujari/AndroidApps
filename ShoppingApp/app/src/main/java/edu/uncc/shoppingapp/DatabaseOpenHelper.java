package edu.uncc.shoppingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sunand on 2/26/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "myorders.db";
    static final int DB_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        OrdersTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        OrdersTable.onUpgrade(db,oldVersion,newVersion);
    }
}
