package com.linson.xtools.app01.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.linson.xtools.app01.db.MySQLiteOpenHelper;
import com.linson.xtools.app01.domain.PhoneTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class PhoneTemplateDao {
    //private Context context;
    MySQLiteOpenHelper helper;

    public PhoneTemplateDao(Context context) {
        //this.context = context;
        helper = new MySQLiteOpenHelper(context);
    }

    public void add(PhoneTemplate phoneTemplate) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", phoneTemplate.getName());
        cv.put("number", phoneTemplate.getNumber());
        cv.put("content", phoneTemplate.getContent());
        cv.put("hot", phoneTemplate.getHot());
        db.insert("phone_template", null, cv);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("phone_template", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void update(PhoneTemplate phoneTemplate) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", phoneTemplate.getName());
        cv.put("number", phoneTemplate.getNumber());
        cv.put("content", phoneTemplate.getContent());
        cv.put("hot", phoneTemplate.getHot());
        db.update("phone_template", cv, "id=?", new String[]{String.valueOf(phoneTemplate.getId())});
        db.close();
    }

    public List findAll() {
        List list = new ArrayList();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("phone_template", null, null, null, null, null, "hot desc");
        while (cursor.moveToNext()) {
            PhoneTemplate template = new PhoneTemplate();
            template.setId(cursor.getInt(cursor.getColumnIndex("id")));
            template.setName(cursor.getString(cursor.getColumnIndex("name")));
            template.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            template.setContent(cursor.getString(cursor.getColumnIndex("content")));
            template.setHot(cursor.getInt(cursor.getColumnIndex("hot")));
            list.add(template);
        }
        db.close();
        return list;
    }
}
