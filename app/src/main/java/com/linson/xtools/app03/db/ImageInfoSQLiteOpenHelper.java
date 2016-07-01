package com.linson.xtools.app03.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/1.
 */
public class ImageInfoSQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String NAME = "app03.db";
    private final static int VERSION = 1;

    public ImageInfoSQLiteOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //id integer primary key autoincrement
        String sql = "create table image_info(id integer primary key autoincrement," +
                "fileName varchar(100),date varchar(100),type varchar(100),comments varchar(200),userName varchar(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
