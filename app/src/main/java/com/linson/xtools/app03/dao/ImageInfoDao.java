package com.linson.xtools.app03.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.linson.xtools.app03.db.ImageInfoSQLiteOpenHelper;
import com.linson.xtools.app03.domain.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public class ImageInfoDao {
    ImageInfoSQLiteOpenHelper helper;

    public ImageInfoDao(Context context) {
        helper = new ImageInfoSQLiteOpenHelper(context);
    }

    public void add(ImageInfo imageInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fileName", imageInfo.getFileName());
        cv.put("date", imageInfo.getDate());
        cv.put("comments", imageInfo.getComments());
        cv.put("type", imageInfo.getType());
        cv.put("userName", imageInfo.getUserName());
        db.insert("image_info", null, cv);
        db.close();
    }

    public List findAll() {
        List list = new ArrayList();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("image_info", null, null, null, null, null, "date desc");
        while (cursor.moveToNext()) {
            ImageInfo template = new ImageInfo();
            template.setId(cursor.getInt(cursor.getColumnIndex("id")));
            template.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
            template.setDate(cursor.getString(cursor.getColumnIndex("date")));
            template.setComments(cursor.getString(cursor.getColumnIndex("comments")));
            template.setType(cursor.getString(cursor.getColumnIndex("type")));
            template.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            list.add(template);
        }
        db.close();
        return list;
    }

    public ImageInfo findByFileName(String fileName) {
        ImageInfo imageInfo = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("image_info", null, "fileName = ?", new String[]{fileName}, null, null, "date desc");
        if (cursor.getCount() == 1) {
            if (cursor.moveToNext()) {
                imageInfo = new ImageInfo();
                imageInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                imageInfo.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
                imageInfo.setDate(cursor.getString(cursor.getColumnIndex("date")));
                imageInfo.setComments(cursor.getString(cursor.getColumnIndex("comments")));
                imageInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
                imageInfo.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            }
        }
        db.close();
        return imageInfo;
    }

    public void delete(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("image_info", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
