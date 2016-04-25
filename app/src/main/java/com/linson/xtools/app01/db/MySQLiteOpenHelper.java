package com.linson.xtools.app01.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String NAME = "app01.db";
    private final static int VERSION = 1;


    public MySQLiteOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //id integer primary key autoincrement
        String sql = "create table phone_template(id integer primary key autoincrement," +
                "name varchar(20),number integer(20),content varchar(20),hot integer(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
